INSERT INTO localité VALUES (1400, 'Yverdon');
INSERT INTO localité VALUES (1000, 'Lausanne');
INSERT INTO localité VALUES (1462, 'Yvonand');
INSERT INTO localité VALUES (1700, 'Fribourg');
INSERT INTO localité VALUES (1201, 'Genève');

INSERT INTO lieu VALUES (1, 'à la rue 42', 2, 1400);
INSERT INTO lieu VALUES (2, 'impasse de la fin 28', 3, 1000);
INSERT INTO lieu VALUES (3, 'route de l''univers 56', 5, 1201);

INSERT INTO personne VALUES (3, 'Schneider', 'Nicolas', date '1995-09-20', 1, '0791231212', 'sn@a.com');
INSERT INTO personne VALUES (2, 'Pittet', 'Julie', date '1993-02-01', 2, '0791231212', 'jp@a.com');
INSERT INTO personne VALUES (4, 'Maier', 'Damien', date '1993-02-01', 2, '0791231212', 'dm@a.com');

INSERT INTO lieuprivé VALUES (1, 2);

INSERT INTO "Élève" VALUES (3);
INSERT INTO "Élève" VALUES (4);

INSERT INTO Tuteur VALUES (2, 'Je suis la meilleure prof du monde');
INSERT INTO Tuteur VALUES (4, 'Je suis pas cher');