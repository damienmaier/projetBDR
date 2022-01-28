package tutorit.appli_java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = Contenu.CHOIX_UTILISATEUR.scene();
        stage.setTitle("Tutor'It");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch();
    }
}