package tutorit.appli_java;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.sql.Date;
import java.sql.SQLException;

public class EditerSessionController {
    public TableView tableauDispo;
    public TableView tableauSessions;
    @FXML
    DatePicker champDate;
    @FXML
    TextField champHeure;
    @FXML
    TextField champMinute;
    @FXML
    TextField champDuree;
    @FXML
    TableView tableLieu;
    @FXML
    Button boutonEnregistrer;
    @FXML
    Label texteErreur;

    TableViewConnecteur tableViewConnecteur;
    TableViewConnecteur tableViewConnecteurDispo;
    TableViewConnecteur tableViewConnecteurSessions;

    public void initialize() throws SQLException {
        texteErreur.setVisible(false);
        tableauDispo.setSelectionModel(null);
        tableauSessions.setSelectionModel(null);

        String requeteDispo = """
                SELECT dateetheuredebut AS début, durée
                FROM plagededisponibilité
                WHERE idtuteur=(""" + requeteIdTuteur() + ")";

        //language=PostgreSQL
        String requeteSession = """
                SELECT dateetheuredébut AS début, durée
                FROM session
                    INNER JOIN prestation
                        ON session.idprestation = prestation.id
                WHERE prestation.idTuteur = (""" + requeteIdTuteur() + ")";

        // si on est en train de modifier une session, on ne veut pas qu'elle apparaisse dans la liste
        if (!Etat.creerSession) {
            requeteSession += """

            AND session.id <>""" + Etat.idSession;
        }


        tableViewConnecteurDispo = new TableViewConnecteur(tableauDispo, requeteDispo, true, false);
        tableViewConnecteurSessions = new TableViewConnecteur(tableauSessions, requeteSession, true, false);
        tableViewConnecteur = new TableViewConnecteur(tableLieu,
                BaseDeDonnees.requeteLieuxCommuns(requeteIdParticipants()), false);

        boutonEnregistrer.disableProperty().bind(
                tableLieu.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    private String requeteIdParticipants() {
        if (Etat.creerSession) {
            //language=PostgreSQL
            return """
                    SELECT idTuteur 
                    FROM Prestation
                    WHERE id=""" + Etat.idPrestation + """
                                        
                    UNION 
                    SELECT
                    """ + Etat.idUtilisateur;
        } else {
            //language=PostgreSQL
            return """
                    SELECT "idÉlève"
                    FROM "session_Élève"
                    WHERE idsession = """ + Etat.idSession + """
                                        
                    UNION
                    SELECT prestation.idtuteur
                    FROM session
                        INNER JOIN prestation
                            ON session.idprestation = prestation.id
                    WHERE session.id =""" + Etat.idSession
                    ;
        }
    }

    private String requeteIdTuteur() {
        if (Etat.creerSession)
            //language=PostgreSQL
            return """
                    SELECT idTuteur
                    FROM Prestation
                    WHERE id=""" + Etat.idPrestation;
        else
            return Etat.idUtilisateur;
    }

    @FXML
    void enregistrer() {
        try {
            String debut =
                    "'" + Date.valueOf(champDate.getValue()) + " " + champHeure.getText() + ":" + champMinute.getText() + "'";
            String duree = "'" + champDuree.getText() + " minute'";
            String idLieu = tableViewConnecteur.premiereCelluleLigneSelectionnee();

            //language=PostgreSQL
            String sql;

            if (Etat.creerSession) {
                sql = """
                        BEGIN;

                        INSERT INTO Session(dateetheuredébut, durée, idprestation, idlieu)
                        VALUES(
                                """ + debut + """
                        , """ + duree + """
                        , """ + Etat.idPrestation + """
                        , """ + idLieu + """
                        );

                        INSERT INTO "session_Élève"
                        VALUES(
                                (SELECT currval(pg_get_serial_sequence('session', 'id'))),
                                """ + Etat.idUtilisateur + """
                                        );

                        COMMIT;
                        """;
            } else {
                sql = """
                        UPDATE session
                        SET
                        dateetheuredébut = """ + debut + """
                        , durée = """ + duree + """
                        , idLieu = """ + idLieu + """

                        WHERE id = """ + Etat.idSession;
            }

            BaseDeDonnees.requeteSansResultat(sql);

            if (!Etat.creerSession) {
                Etat.observateur.notifier();
            }

            ((Stage) boutonEnregistrer.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                BaseDeDonnees.requeteSansResultat("ROLLBACK;");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            texteErreur.setVisible(true);
        }
    }
}
