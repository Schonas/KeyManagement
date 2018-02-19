package de.schonas.keymanagement.main;

import de.schonas.keymanagement.keyinfo.KeyInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static de.schonas.keymanagement.main.MainPage.currentKey;
import static de.schonas.keymanagement.main.MainPage.ksql;
import static de.schonas.keymanagement.main.MainPage.u;

public class TableData {

    private ObservableList<Key> masterData = FXCollections.observableArrayList();
    private List<String> autoCompleteData = new ArrayList<>();
    private TableView<Key> keyTable;
    private TableColumn<Key, String> uidCol, idCol, expDateCol, ownerCol;
    private TextField searchField;
    private AutoCompletionBinding ts;

    /**
     * Setzt Daten in lokale Variablien und holt Daten aus DB
     *
     * @param tableView Tabelle die bestückt werden soll
     * @param idCol Id Spalte
     * @param ownerCol Besitzer Spalte
     * @param expDateCol Ablaufdatum Spalte
     * @param searchField Surchfeld
     */
    public TableData(TableView tableView, TableColumn<Key, String> uidCol, TableColumn<Key, String> idCol, TableColumn<Key, String> ownerCol, TableColumn<Key, String> expDateCol, TextField searchField) {
        this.keyTable = tableView;
        this.uidCol = uidCol;
        this.idCol = idCol;
        this.expDateCol = expDateCol;
        this.ownerCol = ownerCol;
        this.searchField = searchField;

        ResultSet rs = ksql.getKeys();
        try {
            Key k;
            while (rs.next()) {
                k = new Key(rs.getString("uid"), rs.getString("id"), rs.getString("owner"), rs.getString("exp_date"));
                masterData.add(k);
                if (!autoCompleteData.contains(k.getID())) autoCompleteData.add(k.getID());
                if (!autoCompleteData.contains(k.getOwner())) autoCompleteData.add(k.getOwner());
                if (!autoCompleteData.contains(k.getExpDate())) autoCompleteData.add(k.getExpDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ts = TextFields.bindAutoCompletion(this.searchField, autoCompleteData);
        ts.setMaxWidth(198);
        ts.setVisibleRowCount(8);
    }

    /**
     * Legt Spalten an, setzt Autocomplete auf Textfeld, Regelt Suche, setzt Daten in die Tabelle, Regelt Hover
     */
    public void load() {

        uidCol.setCellValueFactory(cellData -> cellData.getValue().uidPropety());
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        ownerCol.setCellValueFactory(cellData -> cellData.getValue().ownerProperty());
        expDateCol.setCellValueFactory(cellData -> cellData.getValue().expDateProperty());

        FilteredList<Key> filteredData = new FilteredList<>(masterData, p -> true);

        ts = TextFields.bindAutoCompletion(searchField, autoCompleteData);
        ts.setMaxWidth(198);
        ts.setVisibleRowCount(8);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(key -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (key.getID().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (key.getOwner() != null && key.getOwner().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (key.getExpDate() != null && key.getExpDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Key> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(keyTable.comparatorProperty());
        keyTable.setItems(sortedData);

        keyTable.setRowFactory(tableView -> {
            final TableRow<Key> row = new TableRow<>();

            row.hoverProperty().addListener((observable) -> {
                final Key key = row.getItem();

                if (row.isHover() && !key.getExpDate().isEmpty()) {
                    String dateString = String.valueOf(u.getDateDiff(key.getExpDate()));
                    long dif = u.getDateDiff(key.getExpDate());
                    row.setTooltip(new Tooltip(dif > 0 ? "Abgelaufen seit " + dateString + " Tagen" : "Läuft in " + Math.abs(dif) + " Tagen ab"));
                }

            });

            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    Key key = row.getItem();
                    KeyInfo keyInfo = new KeyInfo();
                    try {
                        currentKey = key;
                        keyInfo.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }
}