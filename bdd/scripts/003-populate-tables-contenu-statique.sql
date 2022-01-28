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
VALUES ('rue du savoir 34', 50, 1400),
        ('rue des poires 12', 150, 1227),
        ('rue des pommes 1', 10, 1227),
        ('plainpalais', 1050, 1205),
        ('avenue de la tarte 4', 100, 2000),
        ('rue des dames', 20, 1202),
        ('rue du chêne 1', 10, 1230);

INSERT INTO lieupublic
VALUES (1, 'Bibliothèque d''Yverdon'),
        (2, 'Parc national'),
        (3, 'Bibliothèque de Carouge'),
        (4, 'plainpalais'),
        (5, 'Parc des charmettes'),
        (6, 'Collège Calvin'),
        (7, 'Bibliothèque municipale');

insert into langue(nom)
values ('Français'),
       ('Anglais'),
       ('Italien'),
       ('Allemand'),
       ('Romanche'),
       ('Espagnol'),
       ('Portugais');

INSERT INTO domainecompétence (nom)
VALUES ('Maths'),
       ('Français'),
       ('Anglais'),
       ('Italien'),
       ('Allemand'),
       ('Romanche'),
       ('Espagnol'),
       ('Portugais'),
       ('Physique'),
       ('Histoire'),
       ('Géographie');

INSERT INTO niveauPossible(nomDomaineCompétence, niveauPossible)
VALUES ('Maths', 'bachelor'),
       ('Maths', 'master'),
       ('Maths', 'doctorat'),

       ('Physique', 'bachelor'),
       ('Physique', 'master'),
       ('Physique', 'doctorat'),

        ('Histoire', 'primaire'),
       ('Histoire', 'bachelor'),
       ('Histoire', 'master'),
       ('Histoire', 'doctorat'),

        ('Géographie', 'primaire'),
        ('Géographie', 'bachelor'),
       ('Géographie', 'master'),
       ('Géographie', 'doctorat'),

       ('Français', 'A1'),
       ('Français', 'A2'),
       ('Français', 'B1'),
       ('Français', 'B2'),
       ('Français', 'C1'),
       ('Français', 'C2'),
       
        ('Anglais', 'A1'),
       ('Anglais', 'A2'),
       ('Anglais', 'B1'),
       ('Anglais', 'B2'),
       ('Anglais', 'C1'),
       ('Anglais', 'C2'),
       
        ('Italien', 'A1'),
       ('Italien', 'A2'),
       ('Italien', 'B1'),
       ('Italien', 'B2'),
       ('Italien', 'C1'),
       ('Italien', 'C2'),
       
        ('Romanche', 'A1'),
       ('Romanche', 'A2'),
       ('Romanche', 'B1'),
       ('Romanche', 'B2'),
       ('Romanche', 'C1'),
       ('Romanche', 'C2'),

        ('Portugais', 'A1'),
       ('Portugais', 'A2'),
       ('Portugais', 'B1'),
       ('Portugais', 'B2'),
       ('Portugais', 'C1'),
       ('Portugais', 'C2'),
       
        ('Espagnol', 'A1'),
       ('Espagnol', 'A2'),
       ('Espagnol', 'B1'),
       ('Espagnol', 'B2'),
       ('Espagnol', 'C1'),
       ('Espagnol', 'C2');