package tutorit.appli_java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = Controlleurs.CHOIX_UTILISATEUR.scene();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch();
    }
}