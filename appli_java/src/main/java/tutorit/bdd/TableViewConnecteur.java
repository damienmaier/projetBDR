package tutorit.bdd;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TableViewConnecteur {
    private final TableView tableView;
    private final String requete;
    private final boolean afficherPremiereColonne;

    public TableViewConnecteur(TableView tableView, String requete, boolean afficherPremiereColonne) throws SQLException {
        this.afficherPremiereColonne = afficherPremiereColonne;
        this.tableView = tableView;
        this.requete = requete;
        mettreAJour();
    }

    public TableViewConnecteur(TableView tableView, String nomTable) throws SQLException {
        this(tableView, nomTable, true);
    }

    public void mettreAJour() throws SQLException {
        ResultSet rs = BaseDeDonnees.connection().createStatement().executeQuery(requete);
        for (int i = afficherPremiereColonne ? 0 : 1; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(
                    (Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                            new SimpleStringProperty(param.getValue().get(j).toString()));

            tableView.getColumns().addAll(col);
        }

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            data.add(row);
        }

        tableView.setItems(data);
    }

    public int idLigneSelectionnee() {
        String id = ((ObservableList<String>) tableView.getSelectionModel().getSelectedItem()).get(0);
        return Integer.parseInt(id);
    }


}
