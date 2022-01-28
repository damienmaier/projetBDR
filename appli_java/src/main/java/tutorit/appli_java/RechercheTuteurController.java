package tutorit.appli_java;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
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
                SELECT prestation.id,
                        personne.prénom, personne.nom,
                        prestation.nom AS prestation, prestation.tarif AS "tarif horaire"
                FROM prestation
                    INNER JOIN niveaurequis
                        ON prestation.id = niveaurequis.idprestation
                    INNER JOIN personne_langue
                        ON prestation.idtuteur = personne_langue.idpersonne
                    INNER JOIN personne
                        ON prestation.idtuteur = personne.id
                WHERE
                    prestation.estcaché IS FALSE
                AND
                    prestation.idtuteur <>"""+Etat.idUtilisateur+"""
                
                AND
                    (prestation.nomdomainecompétence, niveaurequis.niveaurequis) IN 
                        (SELECT nomdomainecompétence, niveauencours
                        FROM "domainecompétence_Élève"
                        WHERE "idÉlève" = """ + Etat.idUtilisateur + """
                    )
                AND personne_langue.nomlangue IN
                    (SELECT nomlangue
                    FROM personne_langue
                    WHERE idpersonne = """ + Etat.idUtilisateur + """
                    )
                AND EXISTS(""" + BaseDeDonnees.requeteLieuxCommuns(
                "prestation.idtuteur, " + Etat.idUtilisateur
        ) + """
                    )
                """;
        return sql;
    }

    @FXML
    private void planifierSession() throws IOException {
        Etat.creerSession = true;
        Etat.idPrestation = tableViewConnecteur.premiereCelluleLigneSelectionnee();

        Contenu.EDITER_SESSION.ouvrirDansNouvelleFenetre();
    }
}
