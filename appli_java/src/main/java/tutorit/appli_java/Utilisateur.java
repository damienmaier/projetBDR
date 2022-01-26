package tutorit.appli_java;

import totorit.bdd.BaseDeDonnees;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Utilisateur {
    private static Utilisateur actuel;

    public static void definirId(int id) throws SQLException {
        actuel = new Utilisateur(id);
    }

    public static Utilisateur actuel() {
        return actuel;
    }

    private final String prenom;
    private final String nom;
    private final boolean estEleve;
    private final boolean estTuteur;

    Utilisateur(int id) throws SQLException {
        ResultSet rs = BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT prénom, nom from personne WHERE id = " + id);
        rs.next();
        prenom = rs.getString(1);
        nom = rs.getString(2);

        estEleve = BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT * from Élève WHERE idPersonne = " + id).next();
        estTuteur = BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT * from Tuteur WHERE idPersonne = " + id).next();
    }

    @Override
    public String toString() {
        return prenom + " " + nom + (estEleve ? " élève" : "") + (estTuteur ? " tuteur" : "");
    }
}
