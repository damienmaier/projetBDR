package tutorit.appli_java;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.sql.SQLException;

public class EditerDomaineController {
    public TableView tableauDomaine;
    public TableView tableauNiveau;
    public Button boutonEnregistrer;

    TableViewConnecteur tableViewConnecteurDomaine;
    TableViewConnecteur tableViewConnecteurNiveau;

    public void initialize() throws SQLException {
        //language=PostgreSQL
        String requeteDomaines = """
                SELECT nom FROM domainecompétence
                EXCEPT
                SELECT nomdomainecompétence
                FROM "domainecompétence_Élève"
                WHERE idÉlève=""" + Etat.idUtilisateur;

        tableViewConnecteurDomaine = new TableViewConnecteur(tableauDomaine,
                requeteDomaines,true,false);

        tableViewConnecteurNiveau = new TableViewConnecteur(tableauNiveau, null, true, false);

        tableauDomaine.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                String sql = """
                        SELECT niveaupossible
                        FROM niveaupossible
                        WHERE nomdomainecompétence ='""" + tableViewConnecteurDomaine.premiereCelluleLigneSelectionnee() + "'";
                try {
                    tableViewConnecteurNiveau.mettreAJour(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        boutonEnregistrer.disableProperty().bind(
                tableauNiveau.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    public void enregistrer(ActionEvent actionEvent) throws SQLException {
        //language=PostgreSQL
        String sql = """
                INSERT INTO "domainecompétence_Élève"
                VALUES(
                '"""+tableViewConnecteurDomaine.premiereCelluleLigneSelectionnee()+"""
                ',"""+Etat.idUtilisateur+"""
                ,'"""+tableViewConnecteurNiveau.premiereCelluleLigneSelectionnee()+"""
                '
                )
                """;

        BaseDeDonnees.requeteSansResultat(sql);
        Etat.observateur.notifier();
        ((Stage) boutonEnregistrer.getScene().getWindow()).close();
    }
}
