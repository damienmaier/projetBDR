package tutorit.appli_java;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tutorit.bdd.TableViewConnecteur;

import java.io.IOException;
import java.sql.SQLException;

public class ChoixUtilisateurController {
    @FXML
    private TableView tableauUtilisateurs;
    @FXML
    private Button boutonConnexion;
    private TableViewConnecteur tableViewConnecteur;

    @FXML
    public void initialize() throws SQLException {
        tableViewConnecteur = new TableViewConnecteur(tableauUtilisateurs, "SELECT * FROM Personne", false);

        boutonConnexion.disableProperty().bind(
                tableauUtilisateurs.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    @FXML
    public void connexion() throws SQLException, IOException {
        Utilisateur.definirId(tableViewConnecteur.idLigneSelectionnee());
        System.out.println(Utilisateur.actuel());
        ((Stage) boutonConnexion.getScene().getWindow()).setScene(Controlleurs.PAGE_PRINCIPALE.scene());
    }

}