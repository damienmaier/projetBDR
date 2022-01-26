module tutorit.appli_java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens tutorit.appli_java to javafx.fxml;
    exports tutorit.appli_java;
}