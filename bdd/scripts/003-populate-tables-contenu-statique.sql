INSERT INTO localité
VALUES (2000, 'Neuchatel'),
       (1230, 'Veriyer'),
       (1227, 'Carouge'),
       (1208, 'Plan-les-Ouates'),
       (1205, 'Plainpalais'),
       (1202, 'Genève'),
       (1462, 'Yvonand'),
       (1400, 'Yverdon');

INSERT INTO lieu(rueetnuméro, capacité, codepostallocalité)
VALUES ('rue du savoir 34', 50, 1400);

INSERT INTO lieupublic
VALUES (1, 'Bibliothèque d''Yverdon');

insert into langue(nom)
values ('Français'),
       ('Anglais'),
       ('Italien');

INSERT INTO domainecompétence (nom)
VALUES ('Maths'),
       ('Anglais');

INSERT INTO niveauPossible(nomDomaineCompétence, niveauPossible)
VALUES ('Maths', 'bachelor'),
       ('Maths', 'master'),
       ('Maths', 'doctorat'),

       ('Anglais', 'A1'),
       ('Anglais', 'A2'),
       ('Anglais', 'B1'),
       ('Anglais', 'B2'),
       ('Anglais', 'C1'),
       ('Anglais', 'C2');