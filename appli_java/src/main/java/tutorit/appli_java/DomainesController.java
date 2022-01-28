package tutorit.appli_java;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.io.IOException;
import java.sql.SQLException;

public class DomainesController {
    public TableView tableauDomaines;
    public Button boutonAjouter;
    public Button boutonSupprimer;

    TableViewConnecteur tableViewConnecteur;

    public void initialize() throws SQLException {
        String requeteDomaines = """
                SELECT nomdomainecompétence AS domaine, niveauencours AS "niveau en cours"
                FROM "domainecompétence_Élève"
                WHERE "idÉlève" = """ + Etat.idUtilisateur;
        tableViewConnecteur = new TableViewConnecteur(tableauDomaines, requeteDomaines, true, false);

        boutonSupprimer.disableProperty().bind(
                tableauDomaines.getSelectionModel().selectedItemProperty().isNull()
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
        Contenu.EDITER_DOMAINES.ouvrirDansNouvelleFenetre();
    }

    public void supprimer(ActionEvent actionEvent) throws SQLException {
        String sql = """
                DELETE FROM "domainecompétence_Élève"
                WHERE nomdomainecompétence='"""+tableViewConnecteur.premiereCelluleLigneSelectionnee()+"""
                '
                AND idÉlève =""" + Etat.idUtilisateur;
        BaseDeDonnees.requeteSansResultat(sql);
        tableViewConnecteur.mettreAJour();
    }
}
