-- ================ Lieu / Localité ============================

DROP TABLE IF EXISTS Lieu CASCADE;
CREATE TABLE Lieu(
                     id SMALLSERIAL,
                     rueEtNuméro VARCHAR(50) NOT NULL,
                     capacité SMALLINT NOT NULL,
                     codePostalLocalité SMALLINT NOT NULL,

                     CONSTRAINT PK_Lieu PRIMARY KEY (id),
                     CONSTRAINT CK_Lieu_capacité CHECK (capacité >= 2)
);

DROP TABLE IF EXISTS LieuPrivé CASCADE;
CREATE TABLE LieuPrivé(
                          idLieu SMALLINT,
                          CONSTRAINT PK_LieuPrivé PRIMARY KEY (idLieu)
);

DROP TABLE IF EXISTS LieuPublic CASCADE;
CREATE TABLE LieuPublic(
                           idLieu SMALLINT,
                           nom VARCHAR(50) NOT NULL,
                           CONSTRAINT PK_LieuPublic PRIMARY KEY (idLieu)
);

DROP TABLE IF EXISTS Localité CASCADE;
CREATE TABLE Localité(
                         codePostal SMALLSERIAL,
                         nom VARCHAR(50) NOT NULL,
                         CONSTRAINT PK_Localité PRIMARY KEY (codePostal),

                         CONSTRAINT CK_Localité_codePostal CHECK (codePostal >= 0)
);

DROP TABLE IF EXISTS Localité_Personne CASCADE;
CREATE TABLE Localité_Personne(
                                  codePostalLocalité SMALLINT,
                                  idPersonne SMALLINT,

                                  CONSTRAINT PK_Localité_Personne PRIMARY KEY (codePostalLocalité, idPersonne)
);


-- ================ Personne / Élève / Tuteur ============================

DROP TABLE IF EXISTS Personne CASCADE;
CREATE TABLE Personne(
                         id SMALLSERIAL,
                         nom VARCHAR(50) NOT NULL,
                         prénom VARCHAR(50) NOT NULL,
                         dateNaissance DATE NOT NULL,
                         sexe SMALLINT NOT NULL,
                         numTéléphone VARCHAR(20),
                         adresseMail VARCHAR(50) NOT NULL,
                         idLieuPrivé SMALLINT NOT NULL,


                         CONSTRAINT UC_Personne_idLieuPrivé UNIQUE (idLieuPrivé),
                         CONSTRAINT PK_Personne PRIMARY KEY (id),
                         CONSTRAINT CK_Personne_dateNaissance CHECK (dateNaissance < CURRENT_DATE),
    -- voir ici https://en.wikipedia.org/wiki/ISO/IEC_5218
                         CONSTRAINT CK_Personne_sexe CHECK (sexe IN (1, 2)),
                         CONSTRAINT UC_Personne_adresseMail UNIQUE (adresseMail)
);


DROP TABLE IF EXISTS Personne_Langue CASCADE;
CREATE TABLE Personne_Langue(
                                idPersonne SMALLINT,
                                nomLangue VARCHAR(50),

                                CONSTRAINT PK_Personne_Langue PRIMARY KEY (idPersonne, nomLangue)
);


DROP TABLE IF EXISTS Langue CASCADE;
CREATE TABLE Langue(
                       nom VARCHAR(50),

                       CONSTRAINT PK_Langue PRIMARY KEY (nom)
);


DROP TABLE IF EXISTS Tuteur CASCADE;
CREATE TABLE Tuteur(
                       idPersonne SMALLINT,
                       texteDePrésentation VARCHAR(1000),

                       CONSTRAINT PK_Tuteur PRIMARY KEY (idPersonne)
);

DROP TABLE IF EXISTS PlageDeDisponibilité CASCADE;
CREATE TABLE PlageDeDisponibilité(
                                     idTuteur SMALLINT,
                                     dateEtHeureDebut TIMESTAMP,
                                     durée INTERVAL NOT NULL,

                                     CONSTRAINT PK_PlageDeDisponibilité PRIMARY KEY (idTuteur, dateEtHeureDebut),
                                     CONSTRAINT CK_PlageDeDisponibilité_dateEtHeureDebut
                                         CHECK (dateEtHeureDebut >= CURRENT_TIMESTAMP),
                                     CONSTRAINT CK_PlageDeDisponibilité_durée CHECK (durée > INTERVAL '0')
);

DROP TABLE IF EXISTS Élève CASCADE;
CREATE TABLE Élève(
                      idPersonne SMALLINT,
                      CONSTRAINT PK_Élève PRIMARY KEY (idPersonne)
);


-- ================ Évaluations ============================

DROP TABLE IF EXISTS Évaluation CASCADE;
CREATE TABLE Évaluation(
                           id SMALLSERIAL,
                           ponctualité SMALLINT NOT NULL,
                           commentaire VARCHAR(1000),
                           date DATE NOT NULL DEFAULT CURRENT_DATE,

                           CONSTRAINT PK_Évaluation PRIMARY KEY (id),
                           CONSTRAINT CK_Évaluation_ponctualité CHECK (ponctualité BETWEEN 0 AND 5),
                           CONSTRAINT CK_Évaluttion_date CHECK (date = CURRENT_DATE)
);


DROP TABLE IF EXISTS ÉvaluationTuteur CASCADE;
CREATE TABLE ÉvaluationTuteur(
                                 idÉvaluation SMALLINT,
                                 fiabilité SMALLINT NOT NULL,
                                 engagement SMALLINT NOT NULL,
                                 idTuteur SMALLINT,
                                 idÉlève SMALLINT NOT NULL,

                                 CONSTRAINT PK_ÉvaluationTuteur PRIMARY KEY (idÉvaluation),
                                 CONSTRAINT CK_Évaluation_fiabilité CHECK (fiabilité BETWEEN 0 AND 5),
                                 CONSTRAINT CK_Évaluation_engagement CHECK (engagement BETWEEN 0 AND 5)
);

DROP TABLE IF EXISTS ÉvaluationÉlève CASCADE;
CREATE TABLE ÉvaluationÉlève(
                                idÉvaluation SMALLINT,
                                compétence SMALLINT NOT NULL,
                                pédagogie SMALLINT NOT NULL,
                                idÉlève SMALLINT,
                                idTuteur SMALLINT NOT NULL,

                                CONSTRAINT PK_ÉvaluationÉlève PRIMARY KEY (idÉvaluation),
                                CONSTRAINT CK_Évaluation_compétence CHECK (compétence BETWEEN 0 AND 5),
                                CONSTRAINT CK_Évaluation_pédagogie CHECK (pédagogie BETWEEN 0 AND 5)
);



-- ================ Session / Prestation / DomaineCompétence ============================

DROP TYPE IF EXISTS niveau CASCADE;
CREATE TYPE niveau AS ENUM (
    'primaire',
    'secondaire',
    'CFC',
    'maturité',
    'bachelor',
    'master',
    'doctorat'
    );

DROP TABLE IF EXISTS Session CASCADE;
CREATE TABLE Session(
                        id SMALLSERIAL,
                        dateEtHeureDébut TIMESTAMP NOT NULL,
                        durée INTERVAL NOT NULL,
                        tempsDéplacementTuteur INTERVAL NOT NULL DEFAULT INTERVAL '0',
                        idPrestation SMALLINT NOT NULL,
                        idLieu SMALLINT NOT NULL,

                        CONSTRAINT PK_Session PRIMARY KEY (id),
                        CONSTRAINT CK_Session_dateEtHeureDébut
                            CHECK (dateEtHeureDébut >= CURRENT_TIMESTAMP),
                        CONSTRAINT CK_Session_durée CHECK (durée > INTERVAL '0'),
                        CONSTRAINT CK_Session_tempsDéplacementTuteur
                            CHECK (tempsDéplacementTuteur >= INTERVAL '0')
);

DROP TABLE IF EXISTS Session_Élève CASCADE;
CREATE TABLE Session_Élève(
                              idSession SMALLINT,
                              idÉlève SMALLINT,

                              CONSTRAINT PK_Session_Élève PRIMARY KEY (idSession, idÉlève)
);

DROP TABLE IF EXISTS Prestation CASCADE;
CREATE TABLE Prestation(
                           id SMALLSERIAL,
                           nom VARCHAR(50) NOT NULL,
                           tarif NUMERIC(5, 2) NOT NULL,
                           estCaché BOOLEAN NOT NULL DEFAULT FALSE,
                           idTuteur SMALLINT,
                           nomDomaineCompétence VARCHAR(50) NOT NULL,

                           CONSTRAINT PK_Prestation PRIMARY KEY (id),
                           CONSTRAINT CK_Prestation_tarif CHECK (tarif >= 0)
);

DROP TABLE IF EXISTS NiveauRequis CASCADE;
CREATE TABLE NiveauRequis(
                             idPrestation SMALLINT,
                             niveauRequis niveau,
                             CONSTRAINT PK_NiveauRequis PRIMARY KEY (idPrestation, niveauRequis)
);

DROP TABLE IF EXISTS DomaineCompétence CASCADE;
CREATE TABLE DomaineCompétence(
                                  nom VARCHAR(50),
                                  CONSTRAINT PK_DomaineCompétence PRIMARY KEY (nom)
);

DROP TABLE IF EXISTS NiveauPossible CASCADE;
CREATE TABLE NiveauPossible(
                               nomDomaineCompétence VARCHAR(50),
                               niveauPossible niveau,
                               CONSTRAINT PK_NiveauPossible PRIMARY KEY (nomDomaineCompétence, niveauPossible)
);

DROP TABLE IF EXISTS DomaineCompétence_Élève CASCADE;
CREATE TABLE DomaineCompétence_Élève(
                                        nomDomaineCompétence VARCHAR(50),
                                        idÉlève SMALLINT,
                                        niveauEnCours niveau NOT NULL,

                                        CONSTRAINT PK_DomaineCompétence_Élève PRIMARY KEY (nomDomaineCompétence, idÉlève)
);

DROP TABLE IF EXISTS DomaineCompétence_Tuteur CASCADE;
CREATE TABLE DomaineCompétence_Tuteur(
                                         nomDomaineCompétence VARCHAR(50),
                                         idTuteur SMALLINT,
                                         niveauAcquis niveau NOT NULL,

                                         CONSTRAINT PK_DomaineCompétence_Tuteur PRIMARY KEY (nomDomaineCompétence, idTuteur)
);






-- ================ Lieu / Localité ============================

ALTER TABLE Lieu
    ADD CONSTRAINT FK_Lieu_codePostalLocalité
        FOREIGN KEY (codePostalLocalité)
            REFERENCES Localité(codePostal)
            ON UPDATE CASCADE;

ALTER TABLE LieuPublic
    ADD CONSTRAINT FK_LieuPublic_idLieu
        FOREIGN KEY (idLieu)
            REFERENCES Lieu(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;


ALTER TABLE LieuPrivé
    ADD CONSTRAINT FK_LieuPrivé_idLieu
        FOREIGN KEY (idLieu)
            REFERENCES Lieu(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

ALTER TABLE Localité_Personne
    ADD CONSTRAINT FK_Localité_Personne_codePostalLocalité
        FOREIGN KEY (codePostalLocalité)
            REFERENCES Localité(codePostal)
            ON UPDATE CASCADE
            ON DELETE CASCADE;
ALTER TABLE Localité_Personne
    ADD CONSTRAINT FK_Localité_Personne_idPersonne
        FOREIGN KEY (idPersonne)
            REFERENCES Personne(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;



-- ================ Personne / Élève / Tuteur ============================

ALTER TABLE Personne
    ADD CONSTRAINT FK_Personne_idLieuPrivé
        FOREIGN KEY (idLieuPrivé)
            REFERENCES LieuPrivé(idLieu)
            ON UPDATE CASCADE;

ALTER TABLE Personne_Langue
    ADD CONSTRAINT FK_Personne_Langue_idPersonne
        FOREIGN KEY (idPersonne)
            REFERENCES Personne(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,
    ADD CONSTRAINT FK_Personne_Langue_nomLangue
        FOREIGN KEY (nomLangue)
            REFERENCES Langue(nom)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

ALTER TABLE Élève
    ADD CONSTRAINT FK_Élève_idPersonne
        FOREIGN KEY (idPersonne)
            REFERENCES Personne(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

ALTER TABLE Tuteur
    ADD CONSTRAINT FK_Tuteur_idPersonne
        FOREIGN KEY (idPersonne)
            REFERENCES Personne(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

ALTER TABLE PlageDeDisponibilité
    ADD CONSTRAINT FK_PlagesDeDisponibilité_idTuteur
        FOREIGN KEY (idTuteur)
            REFERENCES Tuteur(idPersonne)
            ON UPDATE CASCADE
            ON DELETE CASCADE;


-- ================ Évaluations ============================

ALTER TABLE ÉvaluationTuteur
    ADD CONSTRAINT FK_ÉvaluationTuteur_idÉvaluation
        FOREIGN KEY (idÉvaluation)
            REFERENCES Évaluation(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;
ALTER TABLE ÉvaluationTuteur
    ADD CONSTRAINT FK_ÉvaluationTuteur_idTuteur
        FOREIGN KEY (idTuteur)
            REFERENCES Tuteur(idPersonne)
            ON UPDATE CASCADE
            ON DELETE SET NULL;
ALTER TABLE ÉvaluationTuteur
    ADD CONSTRAINT FK_ÉvaluationTuteur_idÉlève
        FOREIGN KEY (idÉlève)
            REFERENCES Élève(idPersonne)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

ALTER TABLE ÉvaluationÉlève
    ADD CONSTRAINT FK_ÉvaluationÉlève_idÉvaluation
        FOREIGN KEY (idÉvaluation)
            REFERENCES Évaluation(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;
ALTER TABLE ÉvaluationÉlève
    ADD CONSTRAINT FK_ÉvaluationÉlève_idÉlève
        FOREIGN KEY (idÉlève)
            REFERENCES Élève(idPersonne)
            ON UPDATE CASCADE
            ON DELETE SET NULL;
ALTER TABLE ÉvaluationÉlève
    ADD CONSTRAINT FK_ÉvaluationÉlève_idTuteur
        FOREIGN KEY (idTuteur)
            REFERENCES Tuteur(idPersonne)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

-- ================ Session / Prestation / DomaineCompétence ============================

ALTER TABLE Session
    ADD CONSTRAINT FK_Session_idPrestation
        FOREIGN KEY (idPrestation)
            REFERENCES Prestation(id)
            ON UPDATE CASCADE;
ALTER TABLE Session
    ADD CONSTRAINT FK_Session_idLieu
        FOREIGN KEY (idLieu)
            REFERENCES Lieu(id)
            ON UPDATE CASCADE;

ALTER TABLE Session_Élève
    ADD CONSTRAINT FK_Session_Élève_idSession
        FOREIGN KEY (idSession)
            REFERENCES Session(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;
ALTER TABLE Session_Élève
    ADD CONSTRAINT FK_Session_Élève_idÉlève
        FOREIGN KEY (idÉlève)
            REFERENCES Élève(idPersonne)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

ALTER TABLE Prestation
    ADD CONSTRAINT FK_Prestation_idTuteur
        FOREIGN KEY (idTuteur)
            REFERENCES Tuteur(idPersonne)
            ON UPDATE CASCADE
            ON DELETE SET NULL;
ALTER TABLE Prestation
    ADD CONSTRAINT FK_Prestation_nomDomaineCompétence
        FOREIGN KEY (nomDomaineCompétence)
            REFERENCES DomaineCompétence(nom)
            ON UPDATE CASCADE;

ALTER TABLE NiveauRequis
    ADD CONSTRAINT FK_NiveauRequis_idPrestation
        FOREIGN KEY (idPrestation)
            REFERENCES Prestation(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

ALTER TABLE NiveauPossible
    ADD CONSTRAINT FK_NiveauPossible_nomDomaineCompétence
        FOREIGN KEY (nomDomaineCompétence)
            REFERENCES DomaineCompétence(nom)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

ALTER TABLE DomaineCompétence_Élève
    ADD CONSTRAINT FK_DomaineCompétence_Élève_nomDomaineCompétence
        FOREIGN KEY (nomDomaineCompétence)
            REFERENCES DomaineCompétence(nom)
            ON UPDATE CASCADE
            ON DELETE CASCADE;
ALTER TABLE DomaineCompétence_Élève
    ADD CONSTRAINT FK_DomaineCompétence_Élève_idÉlève
        FOREIGN KEY (idÉlève)
            REFERENCES Élève(idPersonne)
            ON UPDATE CASCADE
            ON DELETE CASCADE;

ALTER TABLE DomaineCompétence_Tuteur
    ADD CONSTRAINT FK_DomaineCompétence_Tuteur_nomDomaineCompétence
        FOREIGN KEY (nomDomaineCompétence)
            REFERENCES DomaineCompétence(nom)
            ON UPDATE CASCADE
            ON DELETE CASCADE;
ALTER TABLE DomaineCompétence_Tuteur
    ADD CONSTRAINT FK_DomaineCompétence_Tuteur_idTuteur
        FOREIGN KEY (idTuteur)
            REFERENCES Tuteur(idPersonne)
            ON UPDATE CASCADE
            ON DELETE CASCADE;