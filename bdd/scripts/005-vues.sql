CREATE VIEW vCodesPostauxPersonne AS
SELECT *
FROM localité_personne
UNION
SELECT lieu.codepostallocalité, personne.id
FROM personne
         INNER JOIN lieu
                    ON personne.idlieuprivé = lieu.id;

CREATE VIEW vLieux AS
SELECT lieu.id,
       CASE
           WHEN lieupublic.nom IS NOT NULL THEN
               lieupublic.nom
           WHEN Personne.id IS NOT NULL THEN
               'Domicile de ' || personne.prénom || ' ' || personne.nom
           END                 AS nom,
       lieu.rueetnuméro        AS adresse,
       lieu.codepostallocalité AS NPA,
       localité.nom            AS localité

FROM lieu
         LEFT JOIN lieuPublic
                   ON lieu.id = lieupublic.idlieu
         LEFT JOIN personne
                   ON lieu.id = personne.idlieuprivé
         INNER JOIN localité
                    ON lieu.codepostallocalité = localité.codepostal;

CREATE VIEW vUtilisateurs AS
SELECT personne.id,
       CASE
           WHEN Élève.idPersonne IS NOT NULL AND tuteur.idpersonne IS NOT NULL THEN 'élève et tuteur'
           WHEN Élève.idPersonne IS NOT NULL THEN 'élève'
           WHEN tuteur.idPersonne IS NOT NULL THEN 'tuteur'
           END                                                      AS rôle,
       personne.prénom,
       personne.nom,
       localité2.codepostal || ' ' || localité2.nom                 AS habite,
       string_agg(DISTINCT (localité.codepostal || ' ' || localité.nom), ', ') AS "se déplace à",
       string_agg(DISTINCT (personne_langue.nomlangue), ', ')                  AS "parle"
FROM personne
         LEFT JOIN Élève
                   ON personne.id = Élève.idpersonne
         LEFT JOIN tuteur
                   ON personne.id = tuteur.idpersonne
         LEFT JOIN localité_personne
                   ON personne.id = localité_personne.idpersonne
         INNER JOIN localité
                    ON localité.codepostal = localité_personne.codepostallocalité
         INNER JOIN lieu
                    ON personne.idlieuprivé = lieu.id
         INNER JOIN localité localité2
                    ON localité2.codepostal = lieu.codepostallocalité
         LEFT JOIN personne_langue
                   ON personne.id = personne_langue.idpersonne
GROUP BY personne.id, Élève.idPersonne, tuteur.idpersonne, localité2.codepostal, localité2.nom;