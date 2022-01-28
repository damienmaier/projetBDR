INSERT INTO lieu (rueetnuméro, capacité, codepostallocalité)
VALUES ('5 boulevard des palmiers', 2, 1227),
       ('3 boulevard des palmiers', 10, 1230),
       ('rue du savoir 34', 15, 1227),
       ('2 boulevard du poivre', 4, 1202),
       ('1 boulevard de la solitude', 1, 1400),
       ('22 rue des palmiers', 2, 1230),
       ('6 rue du sel', 12, 1462);

INSERT INTO lieuprivé
VALUES (8),
       (9),
       (10),
       (11),
       (12),
       (13),
       (14);

INSERT INTO personne (nom, prénom, datenaissance, sexe, numtéléphone, adressemail, idlieuprivé)
VALUES ('Terrieur', 'Alex', '2002-01-01', 1, '078.585.35.96', 'alex@gmail.com', 8),
       ('Terrieur', 'Alain', '2002-01-01', 1, '079.582.10.66', 'alain@outlook.com', 9),
       ('Croche', 'Sarah', '1990-02-01', 2, '079.515.20.46', 'sarah@outlook.com', 10),
       ('Pasbon', 'Vincent', '1998-01-05', 1, '076.525.10.66', 'vincent@aol.com', 11),
       ('Ettedetenis', 'Marc', '2005-01-01', 1, '079.185.12.66', 'marc@outlook.com', 12),
       ('Selavaissel', 'Luca', '2000-01-01', 1, '074.585.10.66', 'luca@outlook.com', 13),
       ('Vapasbien', 'Lisa', '2002-01-01', 1, '077.585.10.46', 'lisa@outlook.com', 14);

INSERT INTO localité_personne
VALUES (1230, 1),
       (1400, 2),
       (1227, 3),
       (1227, 4),
       (1400, 5),
       (1400, 6),
       (1227, 7),
       (1400, 1),
       (1400, 2);

INSERT INTO personne_langue
VALUES (1, 'Français'),
       (1, 'Anglais'),
       (1, 'Italien'),
       (2, 'Romanche'),
       (3, 'Français'),
       (4, 'Anglais'),
       (5, 'Italien'),
       (6, 'Portugais'),
       (7, 'Français'),
       (7, 'Anglais'),
       (7, 'Italien'),
       (2, 'Français');

INSERT INTO "Élève"
VALUES (1),(2),(3),(4),(5);

INSERT INTO "domainecompétence_Élève"
VALUES ('Maths', 1, 'bachelor'),
        ('Physique', 2, 'master'),
        ('Anglais', 2, 'A1'),
        ('Italien', 3, 'A2'),
        ('Italien', 4, 'B1'),
        ('Italien', 5, 'B2'),
        ('Maths', 5, 'bachelor'),
       ('Maths', 4, 'bachelor'),;

INSERT INTO Tuteur
VALUES (5, 'je suis le meilleur'),(6, 'non c'' est moi le meilleur'),(7, 'salut !');

INSERT INTO "domainecompétence_tuteur"
VALUES ('Maths', 2, 'doctorat');

INSERT INTO prestation(nom, tarif, idtuteur, nomdomainecompétence)
VALUES ('Cours de maths niveau uni', 50, 2, 'Maths');

INSERT INTO niveaurequis
VALUES(1, 'bachelor'),
        (1, 'master');