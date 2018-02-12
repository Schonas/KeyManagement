package de.schonas.keymanagement.main;

import de.schonas.keymanagement.settings.SettingsPage;
import de.schonas.keymanagement.util.FXUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static de.schonas.keymanagement.database.KeySQL.TABLE;
import static de.schonas.keymanagement.main.MainPage.*;


public class MainController {

    @FXML // fx:id="keyTable"
    private TableView<Key> KeyTable;

    @FXML // fx:id="uniqueID","Owner","ExpireDate","Valid"
    private TableColumn<Key, String> UniqueID,ExpireDate,Owner;

    @FXML
    private TextField uidField, ownerField;

    @FXML
    private DatePicker dateField;

    @FXML
    private MenuItem quitMenuButton;

    @FXML
    private VBox EditBox, AddBox, RemoveBox;

    @FXML
    public Text statusBar;

    @FXML
    private RadioMenuItem viewButtonExpDateInDays, viewButtonExpired;

    @FXML
    private Text RemoveText;

    @FXML //basically works like an onload() method
    protected void initialize() {
        viewButtonExpired.setSelected(Boolean.parseBoolean(prop.getProperty("showExpired")));
        viewButtonExpDateInDays.setSelected(Boolean.parseBoolean(prop.getProperty("showDaysUntilExpDate")));

        u.reloadTable(KeyTable, UniqueID, Owner, ExpireDate);
        quitMenuButton.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCodeCombination.CONTROL_DOWN));

    }

    /**
     * Öffnet Settings Page
     * @throws IOException
     */
    @FXML
    private void onSettingsClick() throws IOException {
        //TODO: Settings Page
        SettingsPage s = new SettingsPage();
        s.start();
    }

    /**
     * Schließt das Programm
     */
    @FXML
    private void onQuitClick(){
        System.exit(0);
    }

    /**
     * Zeigt Edix vBox, wenn Key ausgewählt
     */
    @FXML
    private void onEditButton(){
        try {
            Key key = KeyTable.getSelectionModel().getSelectedItem();
            EditBox.setVisible(true);
            AddBox.setVisible(false);
            RemoveBox.setVisible(false);
            uidField.setText(key.getUniqueID());
            ownerField.setText(key.getOwner());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(key.getExpireDate(), formatter);
            dateField.setValue(dateTime);
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Key Management");
            alert.setHeaderText("Fehler!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(logo);
            alert.setContentText("Du musst ein Schlüssel auswählen!");
            alert.show();
        }
    }

    /**
     * Lädt Tabelle neu
     */
    @FXML
    private void onReloadClick(){
        u.reloadTable(KeyTable, UniqueID, Owner, ExpireDate);
        u.sendAlert(statusBar, "Tabelle wurde neu geladen.");
    }

    /**
     * Löscht Schlüssel
     */
    @FXML
    private void onRemoveClick(){

        if(KeyTable.getSelectionModel().getSelectedItem() != null) {
            Key key = KeyTable.getSelectionModel().getSelectedItem();
            RemoveBox.setVisible(true);
            AddBox.setVisible(false);
            EditBox.setVisible(false);
            RemoveText.setText("Möchtest du den Schlüssel " + key.getUniqueID() + " wirklich löschen?");
        } else {
            RemoveBox.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Key Management");
            alert.setHeaderText("Fehler!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(logo);
            alert.setContentText("Du musst ein Schlüssel auswählen!");
            alert.show();
        }
    }

    /**
     * Ändert ausgewählten Key
     */
    @FXML
    private void onUpdateClick(){
        Key key = KeyTable.getSelectionModel().getSelectedItem();
        key.setExpireDate(dateField.getValue().toString());
        key.setOwner(ownerField.getText());
        key.setUniqueID(uidField.getText());
        u.sendAlert(statusBar, "Key " + key.getUniqueID() + " wurde erfolgreich geändert.");
        EditBox.setVisible(false);
    }

    /**
     * Öffnet AddPage
     * @throws IOException
     */
    @FXML
    private void onAddClick() {
        EditBox.setVisible(false);
        AddBox.setVisible(true);
    }

    // ADD BOX
    @FXML
    private TextField uidAddField, ownerAddField;

    @FXML
    private DatePicker expDateAddField;

    @FXML
    private void onAddKeyClick(){
        Key key = new Key(uidAddField.getText() ,ownerAddField.getText(), u.asDate(expDateAddField.getValue()).toString());
        ksql.insertKey(key);
        u.sendAlert(statusBar, "Key " + key.getUniqueID() + " wurde \n erfolgreich hinzugefügt.");
        u.reloadTable(KeyTable, UniqueID, Owner, ExpireDate);
        KeyTable.getSelectionModel().select(key);
    }

    //REMOVE BOX

    @FXML
    private void onRemoveYesClick(){
        Key key = KeyTable.getSelectionModel().getSelectedItem();
        ksql.delete(TABLE, u.getDBMap("uid", key.getUniqueID()));
        u.reloadTable(KeyTable, UniqueID, Owner, ExpireDate);
        u.sendAlert(statusBar, "Key " +  key.getUniqueID() + " wurde gelöscht.");
        RemoveBox.setVisible(false);
    }

    @FXML
    private void onRemoveNoClick(){
        RemoveBox.setVisible(false);
    }

}