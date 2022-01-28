package tutorit.appli_java;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.sql.Date;
import java.sql.SQLException;

public class DisponibilitesController {
    public Button boutonSupprimer;
    public Button boutonAjouter;
    public TableView tableau;
    public Label texteErreur;
    public DatePicker champDate;
    public TextField champHeure;
    public TextField champMinute;
    public TextField champDuree;

    TableViewConnecteur tableViewConnecteur;

    public void initialize() throws SQLException {
        String requete = """
                SELECT dateetheuredebut AS début, durée
                FROM plagededisponibilité
                WHERE idtuteur=""" + Etat.idUtilisateur;

        texteErreur.setVisible(false);
        tableViewConnecteur = new TableViewConnecteur(tableau, requete, true, false);

        boutonSupprimer.disableProperty().bind(
                tableau.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    public void ajouter(ActionEvent actionEvent) {
        try {
            String debut =
                    "'" + Date.valueOf(champDate.getValue()) + " " + champHeure.getText() + ":" + champMinute.getText() + "'";
            String duree = "'" + champDuree.getText() + " minute'";

            //language=PostgreSQL
            String sql = """
                    INSERT INTO plagededisponibilité
                    VALUES(
                    """ + Etat.idUtilisateur + """
                    ,"""+debut+"""
                    ,"""+duree+"""
                    )
                    """;

            BaseDeDonnees.requeteSansResultat(sql);
            tableViewConnecteur.mettreAJour();
            texteErreur.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            texteErreur.setVisible(true);
        }
    }

    public void supprimer(ActionEvent actionEvent) throws SQLException {
        //language=PostgreSQL
        String sql = """
                DELETE FROM plagededisponibilité
                WHERE idtuteur = """ + Etat.idUtilisateur + """
                                
                AND dateetheuredebut ='""" + tableViewConnecteur.premiereCelluleLigneSelectionnee() + "'";

        BaseDeDonnees.requeteSansResultat(sql);
        tableViewConnecteur.mettreAJour();
    }
}
