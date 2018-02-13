package de.schonas.keymanagement.main;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static de.schonas.keymanagement.main.MainPage.ksql;
import static de.schonas.keymanagement.main.MainPage.u;

public class TableData {

    private ObservableList<Key> masterData = FXCollections.observableArrayList();
    private List<String> autoCompleteData = new ArrayList<>();
    private TableView<Key> keyTable;
    private TableColumn<Key, String> uniqueID, expDate, owner;
    private TextField searchField;

    /**
     * Setzt Daten in lokale Variablien und holt Daten aus DB
     *
     * @param tableView
     * @param uid
     * @param owner
     * @param expDate
     * @param searchField
     */
    public TableData(TableView tableView, TableColumn<Key, String> uid, TableColumn<Key, String> owner, TableColumn<Key, String> expDate, TextField searchField) {
        this.keyTable = tableView;
        this.uniqueID = uid;
        this.expDate = expDate;
        this.owner = owner;
        this.searchField = searchField;

        ResultSet rs = ksql.getKeys();
        try {
            //TODO: hier könnte man Key k davor ziehen und dann die Variable immer nur neu belegen (spart glaub ich ein bisschen Speicher)
            //Key k;
            while (rs.next()) {
                Key k = new Key(rs.getString("uid"), rs.getString("owner"), rs.getString("expire_date"));
                //k = new Key(rs.getString("uid"), rs.getString("owner"), rs.getString("expire_date"));
                masterData.add(k);
                if (!autoCompleteData.contains(k.getUniqueID())) autoCompleteData.add(k.getUniqueID());
                if (!autoCompleteData.contains(k.getOwner())) autoCompleteData.add(k.getOwner());
                if (!autoCompleteData.contains(k.getExpireDate())) autoCompleteData.add(k.getExpireDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Legt Spalten an, setzt Autocomplete auf Textfeld, Regelt Suche, setzt Daten in die Tabelle, Regelt Hover
     */
    public void load() {

        //TODO: this nur da verwenden wo es auch nötig ist (bei lokalen doppelten Variablen namen) -> übersichtlicher
        this.uniqueID.setCellValueFactory(cellData -> cellData.getValue().uidProperty());
        this.owner.setCellValueFactory(cellData -> cellData.getValue().ownerProperty());
        this.expDate.setCellValueFactory(cellData -> cellData.getValue().expDateProperty());

        FilteredList<Key> filteredData = new FilteredList<>(masterData, p -> true);

        AutoCompletionBinding ts = TextFields.bindAutoCompletion(searchField, autoCompleteData);
        ts.setMaxWidth(198);
        ts.setVisibleRowCount(8);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(key -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (key.getUniqueID().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (key.getOwner().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (key.getExpireDate().toLowerCase().contains(lowerCaseFilter)) {
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

                if (row.isHover() && key != null) {
                    Date date = Date.valueOf(key.getExpireDate());
                    //TODO: generell würde ich sagen, dass man bei einmaliger Verwendung keine Variable machen muss, außer der Ausdruck wird mega unübersichtlich
                    String dateString = String.valueOf(u.getDateDiff(date));
                    row.setTooltip(new Tooltip("Überfällig in: " + dateString));
                }
            });

            return row;
        });
    }
}