package tutorit.appli_java;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public enum Contenu {
    CHOIX_UTILISATEUR("choixUtilisateur.fxml"),
    PAGE_PRINCIPALE("pagePrincipale.fxml"),
    RECHERCHE_TUTEUR("rechercheTuteur.fxml"),
    EDITER_SESSION("editerSession.fxml"),
    SESSIONS("sessions.fxml"),
    PRESTATIONS("prestations.fxml"),
    EDITER_PRESTATION("editerPrestation.fxml"),
    EDITER_UTILISATEUR("editerUtilisateur.fxml"),
    DOMAINES("domaines.fxml"),
    EDITER_DOMAINES("editerDomaine.fxml"),
    DISPONIBILITE("disponibilites.fxml"),
    DETAIL_SESSION("detailSession.fxml");

    private FXMLLoader fmxloader;
    private final String fichierFXML;

    Contenu(String fichierFXML) {
        this.fichierFXML = fichierFXML;

    }

    public FXMLLoader fmxloader() {
        fmxloader = new FXMLLoader(HelloApplication.class.getResource(fichierFXML));
        return fmxloader;
    }

    public Scene scene() throws IOException {
        return new Scene(fmxloader().load(), 1200, 900);
    }

    public void ouvrirDansNouvelleFenetre() throws IOException {
        Stage stage = new Stage();
        stage.setScene(scene());
        stage.show();
    }
}
