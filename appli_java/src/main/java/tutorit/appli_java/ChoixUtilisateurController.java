package tutorit.appli_java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tutorit.bdd.TableViewConnecteur;

import java.io.IOException;
import java.sql.SQLException;

public class ChoixUtilisateurController {
    public Button boutonNouveau;
    @FXML
    private TableView tableauUtilisateurs;
    @FXML
    private Button boutonConnexion;
    private TableViewConnecteur tableViewConnecteur;

    @FXML
    public void initialize() throws SQLException {
        tableViewConnecteur = new TableViewConnecteur(tableauUtilisateurs, "SELECT * FROM vUtilisateurs", false);

        boutonConnexion.disableProperty().bind(
                tableauUtilisateurs.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    @FXML
    public void connexion() throws SQLException, IOException {
        Etat.idUtilisateur = tableViewConnecteur.premiereCelluleLigneSelectionnee();
        ((Stage) boutonConnexion.getScene().getWindow()).setScene(Contenu.PAGE_PRINCIPALE.scene());
    }

    public void nouveau(ActionEvent actionEvent) throws IOException {
        Etat.observateur = new Observateur() {
            @Override
            public void notifier() {
                try {
                    tableViewConnecteur.mettreAJour();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        Contenu.EDITER_UTILISATEUR.ouvrirDansNouvelleFenetre();
    }
}