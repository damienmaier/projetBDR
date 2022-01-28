package tutorit.appli_java;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.sql.SQLException;

public class EditerPrestationController {

    @FXML
    TextField champNom;
    @FXML
    TextField champTarif;
    @FXML
    TableView tableauDomaine;
    @FXML
    TableView tableauNiveaux;
    @FXML
    Label texteErreur;
    @FXML
    Button boutonEnregistrer;

    TableViewConnecteur tableViewConnecteurDomaine;
    TableViewConnecteur tableViewConnecteurNiveaux;

    public void initialize() throws SQLException {
        texteErreur.setVisible(false);
        tableViewConnecteurDomaine = new TableViewConnecteur(tableauDomaine,
                "SELECT nom FROM domainecompétence",true,false);

        tableViewConnecteurNiveaux = new TableViewConnecteur(tableauNiveaux, null, true, true);

        tableauDomaine.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                String sql = """
                        SELECT niveaupossible
                        FROM niveaupossible
                        WHERE nomdomainecompétence ='""" + tableViewConnecteurDomaine.premiereCelluleLigneSelectionnee() + "'";
                try {
                    tableViewConnecteurNiveaux.mettreAJour(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        boutonEnregistrer.disableProperty().bind(
                tableauNiveaux.getSelectionModel().selectedItemProperty().isNull()
        );
    }


    public void enregistrer(ActionEvent actionEvent)  {
        try {
            String sql = """
                    BEGIN;
                    
                    INSERT INTO prestation(nom, tarif, idtuteur, nomdomainecompétence)
                    VALUES(
                    '"""+champNom.getText()+"""
                    ',""" +champTarif.getText()+ """
                    ,"""+Etat.idUtilisateur+"""
                    ,'"""+tableViewConnecteurDomaine.premiereCelluleLigneSelectionnee()+"""
                    ');
                    """;

            for (String niveau : tableViewConnecteurNiveaux.premiereCelluleLignesSelectionnees()) {
                sql += """
                        INSERT INTO niveaurequis
                        VALUES(
                        (SELECT currval(pg_get_serial_sequence('prestation', 'id'))),
                        '"""+niveau+"""
                        ');
                        """;
            }

            sql+="COMMIT;";

            BaseDeDonnees.requeteSansResultat(sql);
            Etat.observateur.notifier();
            ((Stage) boutonEnregistrer.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                BaseDeDonnees.requeteSansResultat("ROLLBACK;");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            texteErreur.setVisible(true);
        }
    }
}
