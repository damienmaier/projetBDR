INSERT INTO lieu (rueetnuméro, capacité, codepostallocalité)
VALUES ('5 boulevard des palmiers', 2, 1227),
       ('6 boulevard des palmiers', 2, 1230);

INSERT INTO lieuprivé
VALUES (2),
       (3);

INSERT INTO personne (nom, prénom, datenaissance, sexe, numtéléphone, adressemail, idlieuprivé)
VALUES ('Terrieur', 'Alex', '2002-01-01', 1, '078.585.35.96', 'alex@gmail.com', 2),
       ('Terrieur', 'Alain', '2002-01-01', 1, '079.585.10.66', 'alain@outlook.com', 3);

INSERT INTO localité_personne
VALUES (1230, 1),
       (1400, 1),
       (1400, 2);

INSERT INTO personne_langue
VALUES (1, 'Français'),
       (1, 'Anglais'),
       (1, 'Italien'),
       (2, 'Français');

INSERT INTO "Élève"
VALUES (1);

INSERT INTO "domainecompétence_Élève"
VALUES ('Maths', 1, 'bachelor');

INSERT INTO Tuteur
VALUES (2, 'je suis le meilleur');

INSERT INTO "domainecompétence_tuteur"
VALUES ('Maths', 2, 'doctorat');

INSERT INTO prestation(nom, tarif, idtuteur, nomdomainecompétence)
VALUES ('Cours de maths niveau uni', 50, 2, 'Maths');

INSERT INTO niveaurequis
VALUES(1, 'bachelor'),
        (1, 'master');