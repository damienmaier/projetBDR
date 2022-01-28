package tutorit.appli_java;

import javafx.scene.control.TableView;
import tutorit.bdd.TableViewConnecteur;

import java.sql.SQLException;

public class DetailSessionController {
    public TableView tableauTuteur;
    public TableView tableauEleves;

    TableViewConnecteur tableViewConnecteurTuteur;
    TableViewConnecteur tableViewConnecteurEleves;

    public void initialize() throws SQLException {
        //language=PostgreSQL
        String requeteCoordonneesTuteur =
                """
                        SELECT personne.nom, personne.prénom, personne.datenaissance AS "date de naissance",
                        personne.numtéléphone AS téléphone, personne.adressemail AS "e-mail"
                        FROM personne
                            INNER JOIN prestation
                                ON personne.id = prestation.idtuteur
                            INNER JOIN session
                                ON session.idprestation = prestation.id
                        WHERE session.id =""" + Etat.idSession;

        //language=PostgreSQL
        String requeteCoordonnesEleves =
                """
                        SELECT personne.nom, personne.prénom, personne.datenaissance AS "date de naissance",
                        personne.numtéléphone AS téléphone, personne.adressemail AS "e-mail"
                        FROM personne
                            INNER JOIN "session_Élève"
                                ON personne.id = "session_Élève"."idÉlève"
                        WHERE "session_Élève".idsession =""" + Etat.idSession;


        tableViewConnecteurTuteur = new TableViewConnecteur(tableauTuteur, requeteCoordonneesTuteur, true);
        tableViewConnecteurEleves = new TableViewConnecteur(tableauEleves, requeteCoordonnesEleves, true);

        tableauTuteur.setSelectionModel(null);
        tableauEleves.setSelectionModel(null);
    }
}
