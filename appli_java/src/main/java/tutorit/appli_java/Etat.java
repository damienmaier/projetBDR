package tutorit.appli_java;

import tutorit.bdd.BaseDeDonnees;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Etat {
    static String idUtilisateur;
    static boolean voirSessionsEnTantQueTuteur;
    static boolean creerSession;
    static String idPrestation;
    static String idSession;
    static Observateur observateur;

    public static String prenomUtilisateur() throws SQLException {
        ResultSet rs = BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT prénom from personne WHERE id = " + idUtilisateur);
        rs.next();
        return rs.getString(1);
    }

    static String nomUtilisateur() throws SQLException {
        ResultSet rs = BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT nom from personne WHERE id = " + idUtilisateur);
        rs.next();
        return rs.getString(1);
    }

    static boolean utilisateurEstEleve() throws SQLException {
        return BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT * from Élève WHERE idPersonne = " + idUtilisateur).next();
    }

    static boolean utilisateurEstTuteur() throws SQLException {
        return BaseDeDonnees.connection().createStatement().executeQuery(
                "SELECT * from Tuteur WHERE idPersonne = " + idUtilisateur).next();
    }
}
