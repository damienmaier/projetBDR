package tutorit.appli_java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.io.IOException;
import java.sql.SQLException;

public class PrestationsController {
    @FXML
    Button boutonSupprimer;
    @FXML
    Button boutonAjouter;

    @FXML
    TableView tableauPrestations;

    private TableViewConnecteur tableViewConnecteur;

    public void initialize() throws SQLException {
        String requetePrestations =
                """
                        SELECT prestation.id, prestation.nom, prestation.tarif, prestation.nomdomainecompétence AS domaine,
                        STRING_AGG(niveaurequis.niveaurequis::VARCHAR(255), ', ') AS niveaux
                        FROM prestation
                            INNER JOIN niveaurequis
                                ON prestation.id = niveaurequis.idprestation
                        WHERE estcaché IS FALSE
                        AND idTuteur =""" + Etat.idUtilisateur + """
                        
                        GROUP BY prestation.id
                        """;

        tableViewConnecteur = new TableViewConnecteur(tableauPrestations, requetePrestations, false);

        boutonSupprimer.disableProperty().bind(
                tableauPrestations.getSelectionModel().selectedItemProperty().isNull()
        );
    }


    public void ajouter(ActionEvent actionEvent) throws IOException {
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
        Contenu.EDITER_PRESTATION.ouvrirDansNouvelleFenetre();
    }

    public void supprimer(ActionEvent actionEvent) throws SQLException {
        //language=PostgreSQL
        String sql = """
                UPDATE prestation
                SET estcaché = TRUE
                WHERE id =""" + tableViewConnecteur.premiereCelluleLigneSelectionnee();
        BaseDeDonnees.requeteSansResultat(sql);
        tableViewConnecteur.mettreAJour();
    }
}
