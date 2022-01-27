package tutorit.appli_java;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public enum Controlleurs {
    CHOIX_UTILISATEUR("choixUtilisateur.fxml"),
    PAGE_PRINCIPALE("pagePrincipale.fxml"),
    RECHERCHE_TUTEUR("rechercheTuteur.fxml"),
    EDITER_SESSION("editerSession.fxml"),
    SESSIONS("sessions.fxml");

    private FXMLLoader fmxloader;
    private final String fichierFXML;

    Controlleurs(String fichierFXML) {
        this.fichierFXML = fichierFXML;

    }

    public FXMLLoader fmxloader() {
        fmxloader = new FXMLLoader(HelloApplication.class.getResource(fichierFXML));
        return fmxloader;
    }

    public Scene scene() throws IOException {
        return new Scene(fmxloader().load(), 1000, 500);
    }
}
