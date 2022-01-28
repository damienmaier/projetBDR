/* ------------------------------------------------------------------ */
-- 						TRIGGERS SUR SESSION
/* ------------------------------------------------------------------ */

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
/* ------------------------------------------------------------------ */

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
/* ------------------------------------------------------------------ */

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

/* ------------------------------------------------------------------ */
-- Trigger functions to check session

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
/* ------------------------------------------------------------------ */

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


/* ------------------------------------------------------------------ */
-- 						TRIGGERS D'HÉRITAGE DISJOINT
/* ------------------------------------------------------------------ */
-- Vérifie que le nouveau lieuPublic n'est pas aussi un autre Lieu

CREATE OR REPLACE FUNCTION function_check_LieuPublic()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $BODY$
BEGIN
    IF NEW.idLieu IN (SELECT idlieu FROM lieupublic) THEN
        RAISE EXCEPTION 'No of ''lieuPublic'' is invalid --> %', NEW.idLieu
            USING HINT = 'Inheritance on ''Lieu'' is disjoint.'
                '''Lieu'' can not belong to several subtype.';
    ELSE
        RETURN NEW;
    END IF;
END;
$BODY$
;

CREATE TRIGGER check_lieuPublic
    BEFORE INSERT OR UPDATE ON lieuPublic
    FOR EACH ROW
EXECUTE FUNCTION function_check_LieuPublic();


/* ------------------------------------------------------------------ */
-- Vérifie que le nouveau lieu privé n'est pas aussi un autre lieu

CREATE OR REPLACE FUNCTION function_check_lieuPrivé()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $BODY$
BEGIN
    IF NEW.idLieu IN (SELECT idLieu FROM lieuprivé) THEN
        RAISE EXCEPTION 'No of ''lieuPrivé'' is invalid --> %', NEW.idLieu
            USING HINT = 'Inheritance on ''Lieu'' is disjoint.'
                '''Lieu'' can not belong to several subtype.';
    ELSE
        RETURN NEW;
    END IF;
END;
$BODY$
;

CREATE TRIGGER check_lieuPrivé
    BEFORE INSERT ON lieuPrivé
    FOR EACH ROW
EXECUTE FUNCTION function_check_lieuPrivé();

/* ------------------------------------------------------------------ */
-- 						TRIGGERS SUR ÉVALUTATION
/* ------------------------------------------------------------------ */
--  Vérifie que la date de l'évaluation soit plus grande que la plus ancienne session que l'élève et le tuteur concernés ont faite ensemble.

CREATE OR REPLACE FUNCTION function_check_evaluation_date()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$BODY$
	BEGIN
    	IF NEW.date < MIN(date)
                         FROM Session
                            INNER JOIN Prestation ON Prestation.id = Session.idPrestation
                            INNER JOIN Tuteur ON  Prestation.idTuteur = Tuteur.idPersonne
                            INNER JOIN Session_Élève ON Session_Élève.id = session.id
			THEN RAISE EXCEPTION 'The Evaluation date must be greater than or equal to the date of the most
                    old session that the student and the tutor concerned by this evaluation
                    have done together.';
		ELSE
			RETURN NEW;
		END IF;
END;
$BODY$
;

CREATE OR REPLACE TRIGGER check_evalutation_Date
    BEFORE INSERT OR UPDATE ON Évaluation
    FOR EACH ROW
EXECUTE FUNCTION function_check_evaluation_date();

/* ------------------------------------------------------------------ */
/* Vérifie que pour chaque EvaluationTuteur il doit exister au moins une Session à laquelle ont participé
   l’Élève et le Tuteur concernés par cette évaluation
*/

CREATE OR REPLACE FUNCTION function_check_evaluation_tuteur()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$BODY$
BEGIN
    IF NEW.idÉlève NOT IN (SELECT idÉlève
                           FROM Session
                                    INNER JOIN prestation ON prestation.id = Session.idPrestation
                               AND prestation.idtuteur = NEW.idTuteur
                                    INNER JOIN "session_Élève" ON session_Élève.idsession = Prestation.id)
    THEN RAISE EXCEPTION 'The tutor must have atleast given one course to the student.';
    ELSE
        RETURN NEW;
    END IF;
END;
$BODY$
;

CREATE OR REPLACE TRIGGER check_evalutation_tuteur
    BEFORE INSERT OR UPDATE ON "Évaluation"
    FOR EACH ROW
EXECUTE FUNCTION function_check_evaluation_tuteur();

/* ------------------------------------------------------------------ */
/* Il ne peut pas y avoir deux EvaluationTuteur qui concernent le même Élève et le même Tuteur.
*/
CREATE OR REPLACE FUNCTION function_check_same_evaluation_tuteur()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$BODY$
BEGIN
    IF NEW.idtuteur IN (SELECT idtuteur
                        FROM "Évaluationtuteur"
                        WHERE "idÉlève" = NEW.idÉlèv)
    THEN RAISE EXCEPTION 'The tutor has already evaluated this student.';
    ELSE
        RETURN NEW;
    END IF;
END;
$BODY$
;

CREATE OR REPLACE TRIGGER check_same_evalutation_tuteur
    BEFORE INSERT OR UPDATE ON Évaluationtuteur
    FOR EACH ROW
EXECUTE FUNCTION function_check_same_evaluation_tuteur();





CREATE OR REPLACE PROCEDURE CI_capacite_lieux_sessions(idLieuControle INTEGER)
    LANGUAGE plpgsql
AS
$$
DECLARE
    occupation_actuelle INTEGER;
    ligne       RECORD;
BEGIN
    occupation_actuelle := 0;
    FOR ligne IN
        WITH sessions_lieu_nbParticipants AS
                 (
                     SELECT session.id, session.dateetheuredébut, session.durée, COUNT(*) + 1 AS nbParticipants
                     FROM session
                              INNER JOIN "session_Élève"
                                         ON session.id = "session_Élève".idsession
                     WHERE session.idLieu = idLieuControle
                     GROUP BY session.id
                 )
        SELECT dateetheuredébut AS moment, nbParticipants AS difference
        FROM sessions_lieu_nbParticipants
        UNION ALL
        SELECT dateetheuredébut + durée AS moment, -nbParticipants AS difference
        FROM sessions_lieu_nbParticipants
        ORDER BY moment

        LOOP
            occupation_actuelle := occupation_actuelle + ligne.difference;
            if occupation_actuelle > (SELECT capacité FROM lieu WHERE id = idLieuControle)
            then
                raise exception 'la capacité maximum d''un lieu ne doit pas être dépassée';
            end if;
        end loop;
    RETURN;
END;
$$;


CREATE OR REPLACE FUNCTION CI_capacite_lieux_sessions_modification_ajout_session()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    CALL CI_capacite_lieux_sessions(NEW.idLieu);
    RETURN NULL;
END;
$$;

CREATE OR REPLACE FUNCTION CI_capacite_lieux_sessions_modification_ajout_session_élève()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
DECLARE
    idLieuControle INTEGER;
BEGIN
    SELECT session.idLieu
    INTO idLieuControle
    FROM session
             INNER JOIN session_Élève
                        ON session.id = session_Élève.idSession
    WHERE NEW = (session_Élève.idSession, session_Élève.idÉlève);

    CALL CI_capacite_lieux_sessions(idLieuControle);
    RETURN NULL;
END;
$$;

CREATE OR REPLACE TRIGGER after_insert_update_session
AFTER INSERT OR UPDATE ON Session
FOR EACH ROW
EXECUTE FUNCTION CI_capacite_lieux_sessions_modification_ajout_session();

CREATE OR REPLACE TRIGGER after_insert_update_session_Élève
AFTER INSERT OR UPDATE
    ON Session_Élève
        FOR EACH ROW
EXECUTE FUNCTION CI_capacite_lieux_sessions_modification_ajout_session_élève();


-- CREATE OR REPLACE FUNCTION CI_pas_superposer_sessions_tuteur()
--     RETURNS TRIGGER
--     LANGUAGE plpgsql
-- AS
-- $$
-- BEGIN
-- If exists(SELECT *
--     FROM session
--         INNER JOIN prestation
--             ON session.idprestation = prestation.id
--         INNER JOIN session session2
--                 ON prestation.id = session2.idprestation
--     WHERE session.id = NEW.id
--     AND (session2.dateetheuredébut between NEW.dateetheuredébut AND NEW.dateetheuredébut + durée
--         OR)
--     )
-- END;
-- $$;