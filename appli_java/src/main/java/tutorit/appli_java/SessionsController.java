package tutorit.appli_java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.sql.SQLException;

public class SessionsController {

    @FXML
    Button boutonAnnuler;

    @FXML
    TableView tableauSessions;

    private TableViewConnecteur tableViewConnecteur;

    public void initialize() throws SQLException {
        tableViewConnecteur = new TableViewConnecteur(tableauSessions,requeteSessions(),false);

        boutonAnnuler.disableProperty().bind(
                tableauSessions.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    private String requeteSessions() {
        //language=PostgreSQL
        return """
                SELECT session.id, session.dateetheuredébut AS début, session.durée, vuelieux.nom AS lieu,
                vuelieux.rueetnuméro AS adresse, vuelieux.codepostallocalité as localité
                FROM session
                    INNER JOIN "session_Élève"
                        ON session.id = "session_Élève".idsession
                    INNER JOIN vuelieux
                        ON session.idlieu = vuelieux.id                            
                WHERE "session_Élève"."idÉlève" =""" + Utilisateur.actuel().id() + """
                
                ORDER BY session.dateetheuredébut
                """;
    }

    public void annulerSession(ActionEvent actionEvent) {
    }
}
