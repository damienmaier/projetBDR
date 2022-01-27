package tutorit.appli_java;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.io.IOException;
import java.sql.SQLException;

public class RechercheTuteurController {
    @FXML
    private TableView tableauPrestations;
    @FXML
    private Button boutonPlanifier;

    private TableViewConnecteur tableViewConnecteur;

    public void initialize() throws SQLException {
        tableViewConnecteur = new TableViewConnecteur(tableauPrestations, requeteRecherchePrestation(), false);

        boutonPlanifier.disableProperty().bind(
                tableauPrestations.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    private String requeteRecherchePrestation() {
        //language=PostgreSQL
        String sql = """
                SELECT *
                FROM prestation
                    INNER JOIN niveaurequis
                        ON prestation.id = niveaurequis.idprestation
                    INNER JOIN personne_langue
                        ON prestation.idtuteur = personne_langue.idpersonne
                WHERE
                    (prestation.nomdomainecompétence, niveaurequis.niveaurequis) IN 
                        (SELECT nomdomainecompétence, niveauencours
                        FROM "domainecompétence_Élève"
                        WHERE "idÉlève" = """ + Utilisateur.actuel().id() + """
                    )
                AND personne_langue.nomlangue IN
                    (SELECT nomlangue
                    FROM personne_langue
                    WHERE idpersonne = """ + Utilisateur.actuel().id() + """
                    )
                AND EXISTS(""" + BaseDeDonnees.requeteLieuxCommuns(
                "prestation.idtuteur, " + Utilisateur.actuel().id()
        ) + """
                    )
                """;
        return sql;
    }

    @FXML
    private void planifierSession() throws IOException {
        SessionActuelle.idPrestation = tableViewConnecteur.idLigneSelectionnee();
        Stage stage = new Stage();
        stage.setScene(Controlleurs.EDITER_SESSION.scene());
        stage.show();
    }
}
