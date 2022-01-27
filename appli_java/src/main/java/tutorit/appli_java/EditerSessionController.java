package tutorit.appli_java;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditerSessionController {
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

    public void initialize() throws SQLException {
        texteErreur.setVisible(false);
        tableViewConnecteur = new TableViewConnecteur(tableLieu,
                BaseDeDonnees.requeteLieuxCommuns(
                        "SELECT idTuteur FROM Prestation WHERE id=" + SessionActuelle.idPrestation +
                                " UNION SELECT " + Utilisateur.actuel().id()),
                false);

        boutonEnregistrer.disableProperty().bind(
                tableLieu.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    @FXML
    void enregistrer() {
        try {
            String debut =
                    "'" + Date.valueOf(champDate.getValue()) + " " + champHeure.getText() + ":" + champMinute.getText() + "'";
            String duree = "'" + champDuree.getText() + " minute'";
            ResultSet rs = BaseDeDonnees.connection().createStatement().executeQuery(
                    "INSERT INTO Session(dateetheuredébut, durée, idprestation, idlieu) VALUES" +
                            "(" + debut + "," + duree + "," + SessionActuelle.idPrestation +
                            "," + tableViewConnecteur.idLigneSelectionnee() + ") RETURNING id");
            rs.next();
            int idSession = rs.getInt(1);
            BaseDeDonnees.connection().createStatement().executeUpdate(
                    "INSERT INTO session_Élève VALUES (" + idSession + ", " + Utilisateur.actuel().id() + ")"
            );
            ((Stage) boutonEnregistrer.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            texteErreur.setVisible(true);
        }
    }
}
