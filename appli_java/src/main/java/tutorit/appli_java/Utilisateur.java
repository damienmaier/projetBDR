package tutorit.appli_java;

import tutorit.bdd.BaseDeDonnees;

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

    private final int id;

    private Utilisateur(int id) throws SQLException {
        this.id = id;
    }

    public String prenom() throws SQLException {
        ResultSet rs = BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT prénom from personne WHERE id = " + id);
        rs.next();
        return rs.getString(1);
    }

    public String nom() throws SQLException {
        ResultSet rs = BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT nom from personne WHERE id = " + id);
        rs.next();
        return rs.getString(1);
    }

    public boolean estEleve() throws SQLException {
        return BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT * from Élève WHERE idPersonne = " + id).next();
    }

    public boolean estTuteur() throws SQLException {
        return BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT * from Tuteur WHERE idPersonne = " + id).next();
    }

    @Override
    public String toString() {
        try {
            return prenom() + " " + nom() + (estEleve() ? " élève" : "") + (estTuteur() ? " tuteur" : "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int id() {
        return id;
    }
}
