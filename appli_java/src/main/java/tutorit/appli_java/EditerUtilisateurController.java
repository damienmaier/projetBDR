package tutorit.appli_java;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tutorit.bdd.BaseDeDonnees;
import tutorit.bdd.TableViewConnecteur;

import java.sql.Date;
import java.sql.SQLException;

public class EditerUtilisateurController {
    public TextField champNom;
    public TextField champPrenom;
    public DatePicker champDateNaissance;
    public TextField champTelephone;
    public TextField champMail;
    public TextField champCapacite;
    public TextField champAdresse;
    public TableView tableauLangues;
    public Button boutonEnregistrer;
    public RadioButton radioHomme;
    public RadioButton radioFemme;
    public Label texteErreur;
    public TableView tableauLocaliteDomicile;
    public CheckBox checkTuteur;
    public CheckBox checkEleve;
    public TableView tableauLocalitesDeplacements;

    ToggleGroup radioGroup = new ToggleGroup();

    TableViewConnecteur tableViewConnecteurLocaliteDomicile;
    TableViewConnecteur tableViewConnecteurLangues;
    TableViewConnecteur tableViewConnecteurLocalitesDeplacements;

    public void initialize() throws SQLException {
        texteErreur.setVisible(false);
        tableViewConnecteurLocaliteDomicile = new TableViewConnecteur(tableauLocaliteDomicile, "SELECT * FROM localité",
                true, false);
        tableViewConnecteurLangues = new TableViewConnecteur(tableauLangues, "SELECT * FROM langue", true, true);
        tableViewConnecteurLocalitesDeplacements = new TableViewConnecteur(tableauLocalitesDeplacements, null,
                true, true);

        tableauLocaliteDomicile.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                String sql = """
                        SELECT *
                        FROM localité
                        WHERE codepostal <>"""
                        + tableViewConnecteurLocaliteDomicile.premiereCelluleLigneSelectionnee();
                try {
                    tableViewConnecteurLocalitesDeplacements.mettreAJour(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });


        boutonEnregistrer.disableProperty().bind(
                tableauLocaliteDomicile.getSelectionModel().selectedItemProperty().isNull().or(
                        tableauLangues.getSelectionModel().selectedItemProperty().isNull()).or(
                        tableauLocalitesDeplacements.getSelectionModel().selectedItemProperty().isNull().or(
                                radioGroup.selectedToggleProperty().isNull()
                        )
                ));
        radioHomme.setToggleGroup(radioGroup);
        radioFemme.setToggleGroup(radioGroup);
    }

    public void enregistrer(ActionEvent actionEvent) {
        //language=PostgreSQL
        try {
            if (!checkTuteur.isSelected() && !checkEleve.isSelected()) {
                throw new Exception("doit etre soit élève soit tuteur");
            }
            String sql = """
                    BEGIN;
                                    
                    INSERT INTO lieu(rueetnuméro, capacité, codepostallocalité)
                    VALUES(
                    '""" + champAdresse.getText() + """
                    ',""" + champCapacite.getText() + """
                    ,""" + tableViewConnecteurLocaliteDomicile.premiereCelluleLigneSelectionnee() + """
                    );
                                    
                    INSERT INTO lieuprivé
                    VALUES(
                    (SELECT currval(pg_get_serial_sequence('lieu', 'id')))
                    );
                                    
                    INSERT INTO personne(nom, prénom, datenaissance, sexe, numtéléphone, adressemail, idlieuprivé)
                    VALUES(
                    '""" + champNom.getText() + """
                    ','""" + champPrenom.getText() + """
                    ','""" + Date.valueOf(champDateNaissance.getValue()) + """
                    ',""" + (radioHomme.isSelected() ? 1 : 2) + """
                    ,'""" + champTelephone.getText() + """
                    ','""" + champMail.getText() + """
                    ',(SELECT currval(pg_get_serial_sequence('lieu', 'id')))
                    );
                    """;

            if (checkEleve.isSelected()) {
                sql += """
                        INSERT INTO "Élève"
                        VALUES((SELECT currval(pg_get_serial_sequence('personne', 'id'))));
                        """;
            }
            if (checkTuteur.isSelected()) {
                sql += """
                        INSERT INTO tuteur
                        VALUES(
                        (SELECT currval(pg_get_serial_sequence('personne', 'id'))),
                        '');
                        """;
            }

            for (String langue : tableViewConnecteurLangues.premiereCelluleLignesSelectionnees()) {
                sql += """
                        INSERT INTO personne_langue
                        VALUES(
                        (SELECT currval(pg_get_serial_sequence('personne', 'id'))),
                        '""" + langue + """
                        ');
                        """;
            }

            for (String codePostal : tableViewConnecteurLocalitesDeplacements.premiereCelluleLignesSelectionnees()) {
                //language=PostgreSQL
                sql += """
                        INSERT INTO localité_personne
                        VALUES(
                        """ + codePostal + """
                        ,(SELECT currval(pg_get_serial_sequence('personne', 'id')))
                        );
                        """;
            }

            sql += "COMMIT;";

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
        } catch (Exception e) {
            texteErreur.setVisible(true);
        }
    }
}
