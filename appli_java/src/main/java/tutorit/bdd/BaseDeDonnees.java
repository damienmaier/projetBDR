package tutorit.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

public class BaseDeDonnees {
    private static Connection connection = null;

    public static Connection connection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:4242/postgres?user=postgres");
        }
        return connection;
    }

    public static void requeteSansResultat(String requete) throws SQLException {
        connection().createStatement().executeUpdate(requete);
    }

    public static ResultSet requeteAvecResultat(String requete) throws SQLException {
        return connection().createStatement().executeQuery(requete);
    }

    public static String requeteLieuxCommuns(String requeteIdPersonnes) {
        return
                """
                WITH
                    PersonnesLieuCommun AS
                        (SELECT * FROM personne WHERE id IN (""" + requeteIdPersonnes + """
                        )),
                    LieuxCandidats AS
                        (SELECT * 
                        FROM vLieux
                        WHERE id IN 
                            (SELECT idlieu FROM lieupublic 
                            UNION SELECT idlieuprivé FROM PersonnesLieuCommun)
                        ),
                    LieuxQuiNeVontPasPourToutLeMonde AS
                        (SELECT LieuxCandidats.id, LieuxCandidats.nom,  LieuxCandidats.adresse,
                            LieuxCandidats.NPA, LieuxCandidats.localité
                        FROM LieuxCandidats CROSS JOIN PersonnesLieuCommun
                            LEFT JOIN vCodesPostauxPersonne
                                ON vCodesPostauxPersonne.codepostallocalité = LieuxCandidats.NPA
                                AND vCodesPostauxPersonne.idpersonne = PersonnesLieuCommun.id
                        WHERE vCodesPostauxPersonne.idpersonne IS NULL
                        )
                SELECT * FROM LieuxCandidats
                EXCEPT
                SELECT * FROM LieuxQuiNeVontPasPourToutLeMonde
                """;
    }

}
