package tutorit.appli_java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.SQLException;

public class PagePrincipaleController {

    @FXML
    Button boutonChercherTuteur;
    @FXML
    Button boutonSessionsEleve;

    @FXML
    StackPane contenu;

    public void initialize() throws SQLException {
        if (!Utilisateur.actuel().estEleve()) {
            boutonChercherTuteur.setVisible(false);
            boutonSessionsEleve.setVisible(false);
        }
    }

    public void chercherTuteur(ActionEvent actionEvent) throws IOException {
        changerContenu(Controlleurs.RECHERCHE_TUTEUR);
    }


    public void sessionsEleve(ActionEvent actionEvent) throws IOException {
        changerContenu(Controlleurs.SESSIONS);
    }

    private void changerContenu(Controlleurs controlleur) throws IOException {
        contenu.getChildren().setAll((Node) controlleur.fmxloader().load());
    }
}
