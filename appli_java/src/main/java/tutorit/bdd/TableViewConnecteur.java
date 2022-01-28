package tutorit.bdd;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class TableViewConnecteur {
    private final TableView tableView;
    private String requete;
    private final boolean afficherPremiereColonne;
    private final boolean selectionMultiple;

    public TableViewConnecteur(TableView tableView, String requete, boolean afficherPremiereColonne, boolean selectionMultiple) throws SQLException {
        this.afficherPremiereColonne = afficherPremiereColonne;
        this.tableView = tableView;
        this.requete = requete;
        this.selectionMultiple = selectionMultiple;

        if (selectionMultiple) {
            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
        mettreAJour();
    }

    public TableViewConnecteur(TableView tableView, String nomTable, boolean afficherPremiereColonne) throws SQLException {
        this(tableView, nomTable, afficherPremiereColonne, false);
    }

    public void mettreAJour(String requete) throws SQLException {
        this.requete = requete;
        mettreAJour();
    }

    public void mettreAJour() throws SQLException {
        if (requete == null) {
            return;
        }
        tableView.getColumns().clear();

        ResultSet rs = BaseDeDonnees.requeteAvecResultat(requete);
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

    public String premiereCelluleLigneSelectionnee() {
        String id = ((ObservableList<String>) tableView.getSelectionModel().getSelectedItem()).get(0);
        return id;
    }

    public Collection<String> premiereCelluleLignesSelectionnees() {
        Collection<String> ids = new LinkedList<>();
        for (ObservableList<String> selectedItem : (ObservableList<ObservableList<String>>) tableView.getSelectionModel().getSelectedItems()) {
            ids.add(selectedItem.get(0));
        }
        return ids;
    }


}
