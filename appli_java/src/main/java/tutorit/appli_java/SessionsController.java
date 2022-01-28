package tutorit.appli_java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.io.IOException;
import java.sql.SQLException;

public class SessionsController {

    public Button boutonInfos;
    @FXML
    Button boutonAnnuler;
    @FXML
    Button boutonModifier;

    @FXML
    TableView tableauSessions;

    private TableViewConnecteur tableViewConnecteur;

    public void initialize() throws SQLException {
        tableViewConnecteur = new TableViewConnecteur(tableauSessions, requeteSessions(), false);

        boutonAnnuler.disableProperty().bind(
                tableauSessions.getSelectionModel().selectedItemProperty().isNull()
        );
        boutonModifier.disableProperty().bind(
                tableauSessions.getSelectionModel().selectedItemProperty().isNull()
        );
        boutonInfos.disableProperty().bind(
                tableauSessions.getSelectionModel().selectedItemProperty().isNull()
        );
        if (!Etat.voirSessionsEnTantQueTuteur) {
            boutonModifier.setVisible(false);
        }
    }

    private String requeteSessions() {
        //language=PostgreSQL
        String sql =
                """
                SELECT session.id, session.dateetheuredébut AS début, session.durée,
                prestation.nom AS prestation, prestation.tarif AS "tarif horaire",
                vLieux.nom AS lieu, vLieux.adresse, vLieux.NPA
                FROM session
                    INNER JOIN vLieux
                        ON session.idlieu = vLieux.id    
                    INNER JOIN prestation
                        ON session.idprestation = prestation.id                        
                """
                + (Etat.voirSessionsEnTantQueTuteur ?
                """
                WHERE prestation.idtuteur = """ + Etat.idUtilisateur
                : """   
                    INNER JOIN "session_Élève"
                        ON session.id = "session_Élève".idsession
                WHERE "session_Élève"."idÉlève" =""" + Etat.idUtilisateur) +
                """
                
                ORDER BY session.dateetheuredébut
                """;
        return sql;
    }

    public void annulerSession(ActionEvent actionEvent) throws SQLException {
        //language=PostgreSQL
        String sql = """
                DELETE FROM session
                WHERE id =
                """ + tableViewConnecteur.premiereCelluleLigneSelectionnee();
        BaseDeDonnees.requeteSansResultat(sql);
        tableViewConnecteur.mettreAJour();
    }

    public void modifierSession(ActionEvent actionEvent) throws IOException {
        Etat.creerSession = false;
        Etat.idSession = tableViewConnecteur.premiereCelluleLigneSelectionnee();
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

        Contenu.EDITER_SESSION.ouvrirDansNouvelleFenetre();
    }

    public void infos(ActionEvent actionEvent) throws IOException {
        Etat.idSession = tableViewConnecteur.premiereCelluleLigneSelectionnee();
        Contenu.DETAIL_SESSION.ouvrirDansNouvelleFenetre();
    }
}
