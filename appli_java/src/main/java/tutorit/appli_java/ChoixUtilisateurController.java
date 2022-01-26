package tutorit.appli_java;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import totorit.bdd.TableViewConnecteur;

import java.sql.SQLException;

public class HelloController {
    @FXML
    private TableView tableauUtilisateurs;
    @FXML
    private Button boutonConnexion;
    private TableViewConnecteur tableViewConnecteur;

    @FXML
    public void initialize() throws SQLException {
        tableViewConnecteur = new TableViewConnecteur(tableauUtilisateurs, "personne", false);

        boutonConnexion.disableProperty().bind(
                tableauUtilisateurs.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    @FXML
    public void connexion() throws SQLException {
        Utilisateur.definirId(tableViewConnecteur.idLigneSelectionnee());
        System.out.println(Utilisateur.actuel());
    }

}