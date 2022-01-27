CREATE VIEW VueCodesPostauxPersonne AS
SELECT *
FROM localité_personne
UNION
SELECT lieu.codepostallocalité, personne.id
FROM personne
         INNER JOIN lieu
                    ON personne.idlieuprivé = lieu.id;

CREATE VIEW VueLieux AS
SELECT lieu.id,
       lieu.rueetnuméro,
       lieu.capacité,
       lieu.codepostallocalité,
       CASE
           WHEN lieupublic.nom IS NOT NULL THEN
               lieupublic.nom
           WHEN Personne.id IS NOT NULL THEN
               'Domicile de ' || personne.prénom || ' ' || personne.nom
           END AS nom
FROM lieu
         LEFT JOIN
     lieuPublic
     ON lieu.id = lieupublic.idlieu
         LEFT JOIN
     personne
     ON lieu.id = personne.idlieuprivé;