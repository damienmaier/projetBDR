package tutorit.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
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

    public static String requeteLieuxCommuns(String requeteIdPersonnes) {
        return """
                WITH
                    PersonnesDevantTrouverLieu AS
                        (SELECT * FROM personne WHERE id IN (""" + requeteIdPersonnes + """
                        )),
                    LieuxCandidats AS
                        (SELECT * 
                        FROM VueLieux
                        WHERE id IN 
                            (SELECT idlieu FROM lieupublic 
                            UNION SELECT idlieuprivé FROM PersonnesDevantTrouverLieu)
                        ),
                    LieuxQuiNeVontPasPourToutLeMonde AS
                        (SELECT LieuxCandidats.id, LieuxCandidats.rueetnuméro, LieuxCandidats.capacité,
                            LieuxCandidats.codepostallocalité, LieuxCandidats.nom
                        FROM LieuxCandidats CROSS JOIN PersonnesDevantTrouverLieu
                            LEFT JOIN vuecodespostauxpersonne
                                ON vuecodespostauxpersonne.codepostallocalité = LieuxCandidats.codepostallocalité
                                AND vuecodespostauxpersonne.idpersonne = PersonnesDevantTrouverLieu.id
                        WHERE vuecodespostauxpersonne.idpersonne IS NULL
                        )
                SELECT * FROM LieuxCandidats
                EXCEPT
                SELECT * FROM LieuxQuiNeVontPasPourToutLeMonde
                """;
    }

}
