package tutorit.appli_java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.SQLException;

public class PagePrincipaleController {

    public Button boutonDomaines;
    public Button boutonDisponibilités;
    @FXML
    Button boutonChercherTuteur;
    @FXML
    Button boutonSessionsEleve;
    @FXML
    Button boutonSessionsTuteur;
    @FXML
    Button boutonPrestations;

    @FXML
    StackPane contenu;

    public void initialize() throws SQLException {
        if (!Etat.utilisateurEstEleve()) {
            boutonChercherTuteur.setVisible(false);
            boutonSessionsEleve.setVisible(false);
            boutonDomaines.setVisible(false);
        }

        if (!Etat.utilisateurEstTuteur()) {
            boutonSessionsTuteur.setVisible(false);
            boutonPrestations.setVisible(false);
            boutonDisponibilités.setVisible(false);
        }
    }

    public void chercherTuteur(ActionEvent actionEvent) throws IOException {
        changerContenu(Contenu.RECHERCHE_TUTEUR);
    }


    public void sessionsEleve(ActionEvent actionEvent) throws IOException {
        Etat.voirSessionsEnTantQueTuteur = false;
        changerContenu(Contenu.SESSIONS);
    }

    public void sessionsTuteur(ActionEvent actionEvent) throws IOException {
        Etat.voirSessionsEnTantQueTuteur = true;
        changerContenu(Contenu.SESSIONS);
    }

    public void prestations(ActionEvent actionEvent) throws IOException {
        changerContenu(Contenu.PRESTATIONS);
    }

    public void domaines(ActionEvent actionEvent) throws IOException {
        changerContenu(Contenu.DOMAINES);
    }

    public void disponibilites(ActionEvent actionEvent) throws IOException {
        changerContenu(Contenu.DISPONIBILITE);
    }

    private void changerContenu(Contenu controlleur) throws IOException {
        contenu.getChildren().setAll((Node) controlleur.fmxloader().load());
    }
}
