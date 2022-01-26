INSERT INTO localité(codepostal, nom) VALUES
                                          (2000, 'Neuchatel'),
                                          (1230, 'Veriyer'),
                                          (1227, 'Carouge'),
                                          (1208, 'Plan-les-Ouates'),
                                          (1205, 'Plainpalais'),
                                          (1202, 'Genève');

INSERT INTO lieu (rueetnuméro,capacité,codepostallocalité) VALUES
                                                               ('5 boulevard des palmiers',2,1227),
                                                               ('6 boulevard des palmiers',2,1230),
                                                               ('5 rue des pottiers',5,1227),
                                                               ('1 avenue rouge',10,2000),
                                                               ('4 rue de lausanne',3,1202),
                                                               ('10 rue des charmettes',5,2000),
                                                               ('111 chemin des Vuattes',11,1208);

INSERT INTO lieuprivé(idlieu, idpersonne) VALUES
                                              (1,1),
                                              (2,2),
                                              (3,3),
                                              (4,4),
                                              (5,5);

INSERT INTO lieupublic(idlieu, nom) VALUES
                                        (6, 'Église orthodoxe'),
                                        (7, 'BCU');

INSERT INTO personne (nom,prénom,datenaissance,sexe,numtéléphone,adressemail) VALUES
                                                                                  ('Terrieur','Alex','2002-01-01',1,'078.585.35.96','alex@gmail.com'),
                                                                                  ('Terrieur','Alain','2002-01-01',1,'079.585.10.66','alain@outlook.com'),
                                                                                  ('Sleigh','Bob','2001-01-01',1,'078.585.15.12','bob@heig-vd.ch'),
                                                                                  ('Bonbeur','Jean','1990-01-01',1,'078.585.35.25','jean@gmail.com'),
                                                                                  ('Croche','Sarah','1995-01-01',2,'077.585.40.11','sarah@hotmail.com');

INSERT INTO tuteur (idpersonne,textedeprésentation) VALUES
                                                        (1,'Je suis la meilleure prof du monde'),
                                                        (5,'Je suis pas cher');

INSERT INTO "Élève" (idpersonne) VALUES
                                     (1),
                                     (2),
                                     (3),
                                     (4);

insert into langue(nom) VALUES
                            ('Français'),
                            ('Anglais'),
                            ('Italien');

insert into personne_langue(idpersonne, nomLangue) VALUES
                                                       (1, 'Français'),
                                                       (1, 'Italien'),
                                                       (2, 'Français'),
                                                       (2, 'Anglais'),
                                                       (3, 'Français'),
                                                       (4, 'Italien');

INSERT INTO domainecompétence (nom) VALUES
                                        ('Math'),
                                        ('Informatique'),
                                        ('Physique'),
                                        ('Histoire'),
                                        ('Biologie'),
                                        ('Chimie');


INSERT INTO domainecompétence_tuteur(nomDomaineCompétence, idtuteur, niveauAcquis) VALUES
                                                                                       ('Informatique',1,'doctorat'),
                                                                                       ('Math',5,'bachelor')
;

INSERT INTO DomaineCompétence_Élève(nomDomaineCompétence, idÉlève,niveauEnCours) VALUES
                                                                                     ('Physique', 2, 'primaire'),
                                                                                     ('Chimie', 3, 'secondaire'),
                                                                                     ('Math', 4, 'maturité');

INSERT INTO Localité_Personne(codepostallocalité, idpersonne) VALUES
                                                                  (2000,1),
                                                                  (1230,2),
                                                                  (1227,3),
                                                                  (1202,4),
                                                                  (1205,5);

INSERT INTO niveauPossible(nomDomaineCompétence, niveauPossible) VALUES
                                                                     ('Physique', 'bachelor'),
                                                                     ('Physique', 'master'),
                                                                     ('Physique', 'doctorat'),

                                                                     ('Chimie', 'maturité'),
                                                                     ('Chimie', 'bachelor'),
                                                                     ('Chimie', 'master'),
                                                                     ('Chimie', 'doctorat'),

                                                                     ('Informatique', 'bachelor'),
                                                                     ('Informatique', 'master'),
                                                                     ('Informatique', 'doctorat'),

                                                                     ('Math', 'bachelor'),
                                                                     ('Math', 'master'),
                                                                     ('Math', 'doctorat');

INSERT INTO niveauRequis(idprestation, niveauRequis) VALUES
                                                         (1,'bachelor'),
                                                         (2,'master'),
                                                         (3,'doctorat');

INSERT INTO prestation (nom,tarif,estcaché,idtuteur,nomdomainecompétence) VALUES
                                                                              ('cours de math',30.00,false,1,'Math'),
                                                                              ('cours informatique',40.00,false,1,'Informatique'),
                                                                              ('cours de physique',20.00,false,1,'Physique');

INSERT INTO session (dateetheuredébut,durée,tempsdéplacementtuteur,idprestation,idlieu) VALUES
                                                                                            ('2022-09-26 00:00:00','01:00:00','00:20:00',1,1),
                                                                                            ('2022-01-30 00:00:00','02:00:00','00:10:00',2,2),
                                                                                            ('2022-02-26 00:00:00','00:45:01','00:15:00',3,3);

INSERT INTO session_Élève (idsession,idÉlève) VALUES
                                                  (1,2),
                                                  (2,2),
                                                  (3,4);

INSERT INTO PlageDeDisponibilité(idtuteur, dateetheuredebut , durée) VALUES
                                                                         (1,'27-01-2022 14:00','01:20:00'),
                                                                         (1,'19-05-2022 17:00','00:50:00'),
                                                                         (1,'28-03-2022 15:00','01:00:00'),
                                                                         (1,'25-10-2022 12:00','02:00:00'),
                                                                         (5,'19-05-2022 17:00','00:50:00'),
                                                                         (5,'28-03-2022 15:00','01:00:00'),
                                                                         (5,'25-10-2022 12:00','00:45:00'),
                                                                         (5,'16-03-2022 15:00','01:45:00');

INSERT INTO Évaluation(ponctualité, commentaire, date) VALUES
                                                           (5,'Très bien','2022-01-26'),
                                                           (4,'Bien','2022-01-26'),
                                                           (3,'Satisfaisant','2022-01-26'),
                                                           (2,'Passable','2022-01-26'),
                                                           (1,'Insuffisant','2022-01-26');

INSERT INTO Évaluationtuteur(idÉvaluation, fiabilité,engagement,idtuteur,idÉlève) VALUES
                                                                                      (1,5,4,1,1),
                                                                                      (2,4,3,1,2),
                                                                                      (3,3,2,3,3),
                                                                                      (4,2,1,3,4),
                                                                                      (5,1,1,1,1);

INSERT INTO ÉvaluationÉlève (idÉvaluation,compétence,pédagogie,idÉlève,idtuteur) VALUES
                                                                                     (1,5,4,1,1),
                                                                                     (2,4,3,2,1),
                                                                                     (3,3,2,3,3),
                                                                                     (4,2,1,4,3),
                                                                                     (5,1,1,1,1);

