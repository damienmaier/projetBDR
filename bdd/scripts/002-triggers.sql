-- Checks

create or replace procedure check_session_langue(p_idSession smallint)
LANGUAGE plpgsql
AS $body$
begin
	if exists (select personne_langue.nomlangue
	from prestation
	inner join personne_langue
	on personne_langue.idpersonne = prestation.idTuteur
	where prestation.id = (select prestation.id
		from "session"
		inner join prestation
		on prestation.id = session.idprestation
		where session.id = p_idSession)
	intersect
	select personne_langue.nomlangue
	from personne_langue
	inner join "session_Élève"
	on personne_langue.idpersonne = session_Élève."idÉlève"
	where session_Élève.idsession = p_idSession
	group by personne_langue.nomlangue
	having count(personne_langue.nomlangue) = (
		select count(*)
		from personne
		inner join "session_Élève"
		on personne.id = session_Élève."idÉlève"
		where session_Élève.idsession = p_idSession
		group by session_Élève.idsession
		))
	then
	raise exception 'all the participants from a session must share a language';
end if;
end;
$body$
;

CREATE OR REPLACE procedure check_session_lieu(p_idSession smallint ,p_idPrestation smallint,p_idLieu smallint)
LANGUAGE plpgsql
AS $body$
begin
	if p_idLieu not in (
	select lieupublic.idlieu
	from lieupublic
	union
	select personne.idlieuprivé
	from prestation
	inner join personne
	on personne.id = prestation.idTuteur
	where prestation.id = p_idPrestation
	union
	select personne.idlieuprivé
	from "session_Élève"
	inner join personne
	on personne.id = session_Élève."idÉlève"
	where session_Élève.idsession = p_idSession)
	then
	raise exception 'lieu of session must be public or private place of the tutor or one of the students';
end if;
end;
$body$
;

CREATE OR REPLACE procedure check_session_eleve_tuteur_different(p_idÉlève smallint)
LANGUAGE plpgsql
AS $body$
begin
	if p_idÉlève in (
	select prestation.idtuteur
	from session
	inner join prestation
	on prestation.id = session.idprestation)
	then
	raise exception 'élève must not be the tutor of the session';
end if;
end;
$body$
;

CREATE OR REPLACE FUNCTION CheckCapacity()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$BODY$
BEGIN
SELECT lieu.capacité
FROM Session, COUNT(*) AS nb_élève_par_session
                  INNER JOIN Session_Élève ON Session_Élève.idSession = session.id,
     INNER JOIN Lieu ON Session.idLieu = lieu.id,

    IF (nb_élève_par_session + 1) > lieu.capacité
THEN RAISE EXCEPTION("The maximum number of people who can participate in this place has been reached.");

END;
$BODY$
;


CREATE OR REPLACE FUNCTION CheckEvaluationDate()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$BODY$
BEGIN
    IF NEW.date < MIN(SELECT DISTINCT date
                         FROM Session
                            INNER JOIN Prestation ON Prestation.id = Session.idPrestation
                            INNER JOIN Tuteur ON  Prestation.idTuteur = Tuteur.idPersonne
                            INNER JOIN Session_Élève ON Session_Élève.id = session.id
                      );
THEN RAISE EXCEPTION('The valuation date must be greater than or equal to the date of the most
                    old session that the student and the tutor concerned by this evaluation
                    have done togethe.')
END;
$BODY$
;


-- Trigger functions

CREATE OR REPLACE FUNCTION check_session()
 RETURNS trigger
 LANGUAGE plpgsql
AS $body$
begin
call check_session_lieu(new.id, new.idprestation, new.idLieu);
call check_session_langue(new.id);
return new;
end;
$body$
;

drop trigger if exists before_insert_session on session;
CREATE TRIGGER before_insert_session
    BEFORE insert or update
                         ON "session"
                         FOR EACH ROW
                         EXECUTE PROCEDURE check_session();

CREATE OR REPLACE FUNCTION check_session_élève()
 RETURNS trigger
 LANGUAGE plpgsql
AS $body$
begin
call check_session_eleve_tuteur_different(new.idÉlève);
call check_session_langue(new.idSession);
return new;
end;
$body$
;

drop trigger if exists before_insert_session_élève on session_Élève;
CREATE TRIGGER before_insert_session_élève
    BEFORE insert or update
                         ON "session_Élève"
                         FOR EACH ROW
                         EXECUTE PROCEDURE check_session_élève();


CREATE OR REPLACE TRIGGER before_insert_or_update_session_Élève
    BEFORE INSERT OR UPDATE ON Session_Élève
                                FOR EACH ROW
                                EXECUTE FUNCTION CheckCapacity();


CREATE OR REPLACE TRIGGER before_insert_or_update_evalutation_Date
    BEFORE INSERT OR UPDATE ON Evaluation
                                FOR EACH ROW
                                EXECUTE FUNCTION CheckEvaluationDate();