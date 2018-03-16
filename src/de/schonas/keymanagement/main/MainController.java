package de.schonas.keymanagement.main;

import de.schonas.keymanagement.Key;
import de.schonas.keymanagement.inventory.InventoryPage;
import de.schonas.keymanagement.roominfo.RoomManagementPage;
import de.schonas.keymanagement.settings.SettingsPage;
import de.schonas.keymanagement.util.Action;
import de.schonas.keymanagement.util.TableData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.IOException;
import java.util.Map;
import java.util.TimerTask;

import static de.schonas.keymanagement.database.KeySQL.TABLE_KEYS;
import static de.schonas.keymanagement.main.MainPage.*;


public class MainController {

    @FXML // fx:id="keyTable"
    private TableView<Key> keyTable;

    @FXML // fx:id="idCol","ownerCol","expDateCol","Valid"
    private TableColumn<Key, String> uidCol, idCol, ownerCol, expDateCol;

    @FXML //EDIT ANSICHT & Search
    private TextField idEditField, ownerEditField, searchField;

    @FXML //EDIT DATEPICKER
    private DatePicker dateEditField;

    @FXML //MENU BAR BUTTONS
    private MenuItem quitMenuButton, roomManagementMenuButton, inventoryMenuButton;

    @FXML //BOXEN DER AKTIONEN
    private VBox EditBox, AddBox, RemoveBox;

    @FXML //
    public Text statusBar, RemoveText;

    @FXML
    private Button addTaskButton, removeTaskButton, editTaskButton, reloadTaskButton;

    @FXML
    private MenuBar menuBar;

    @FXML //ADD BOX
    private TextField idAddField, ownerAddField;

    @FXML //ADD BOX
    private DatePicker expDateAddField;

    /**
     * init Methode die alles vorbereitet
     */
    @FXML
    protected void initialize() {

        GlyphFont fontAwesome= GlyphFontRegistry.font("FontAwesome");

        Glyph addIcon = fontAwesome.create(FontAwesome.Glyph.PLUS);
        Glyph removeIcon = fontAwesome.create(FontAwesome.Glyph.TRASH);
        Glyph editIcon = fontAwesome.create(FontAwesome.Glyph.PENCIL);
        Glyph reloadIcon = fontAwesome.create(FontAwesome.Glyph.REFRESH);

        addIcon.setFontSize(28);
        removeIcon.setFontSize(28);
        editIcon.setFontSize(28);
        reloadIcon.setFontSize(28);

        addTaskButton.setText(null);
        removeTaskButton.setText(null);
        editTaskButton.setText(null);
        reloadTaskButton.setText(null);

        addTaskButton.setGraphic(addIcon);
        removeTaskButton.setGraphic(removeIcon);
        editTaskButton.setGraphic(editIcon);
        reloadTaskButton.setGraphic(reloadIcon);

        addTaskButton.setTooltip(new Tooltip("Füge einen Schlüssel hinzu"));
        removeTaskButton.setTooltip(new Tooltip("Lösche den ausgewählten Schlüssel"));
        editTaskButton.setTooltip(new Tooltip("Bearbeite den ausgewählten Schlüssel"));
        searchField.setPromptText("Search...");

        new Thread(new TimerTask() {
            @Override
            public void run() {
                TableData tb = new TableData(keyTable, uidCol, idCol, ownerCol, expDateCol, searchField);
                tb.load();
                keyTable.getSelectionModel().select(0);
                keyTable.getStylesheets().add("de/schonas/keymanagement/CSS/tableStylesheet");
                CURRENT_KEY = keyTable.getSelectionModel().getSelectedItem();
            }
        }).start();

        menuBar.getStylesheets().add("de/schonas/keymanagement/CSS/menuBarStylesheet");

        quitMenuButton.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCodeCombination.CONTROL_DOWN));
        roomManagementMenuButton.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCodeCombination.CONTROL_DOWN));
        inventoryMenuButton.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCodeCombination.CONTROL_DOWN));
    }

    /**
     * Öffnet Settings Page
     * @throws IOException
     */
    @FXML
    private void onSettingsClick() throws IOException {
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
        if(keyTable.getSelectionModel().getSelectedItem() != null) {
            if(EditBox.isVisible()){
                EditBox.setVisible(false);
                idEditField.setText(null);
                ownerEditField.setText(null);
                dateEditField.setValue(null);
            } else {
                Key key = keyTable.getSelectionModel().getSelectedItem();
                EditBox.setVisible(true);
                AddBox.setVisible(false);
                RemoveBox.setVisible(false);
                idEditField.setText(key.getID());
                ownerEditField.setText(key.getOwner());
                CURRENT_KEY = key;
                if (!key.getExpDate().isEmpty()) dateEditField.setValue(u.getLocalDateFromString(key.getExpDate()));
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Key Management");
            alert.setHeaderText("Fehler!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(LOGO);
            alert.setContentText("Du musst ein Schlüssel auswählen!");
            alert.show();
        }
    }

    /**
     * Lädt Tabelle neu
     */
    @FXML
    private void onReloadClick(){
        u.reloadTable(keyTable, uidCol, idCol, ownerCol, expDateCol, searchField);
        u.sendAlert(statusBar, "Tabelle wurde neu geladen.");
    }

    /**
     * Löscht Schlüssel
     */
    @FXML
    private void onRemoveClick(){

        if(keyTable.getSelectionModel().getSelectedItem() != null) {
            Key key = keyTable.getSelectionModel().getSelectedItem();
            RemoveBox.setVisible(true);
            AddBox.setVisible(false);
            EditBox.setVisible(false);
            RemoveText.setText("Möchtest du den Schlüssel " + key.getID() + " wirklich löschen?");
        } else {
            RemoveBox.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(TITLE);
            alert.setHeaderText("Fehler!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(LOGO);
            alert.setContentText("Du musst ein Schlüssel auswählen!");
            alert.show();
        }
    }

    /**
     * Ändert ausgewählten Key
     */
    @FXML
    private void onUpdateClick(){
        Key key;
        if(ownerEditField.getText() == null || dateEditField.getValue()== null){
            key = new Key(CURRENT_KEY.getUID(), idEditField.getText());
        } else {
            key = new Key(CURRENT_KEY.getUID(), idEditField.getText(), ownerEditField.getText(), dateEditField.getValue().toString());
        }
        Map<String, Object> data = u.getDBMap("owner", key.getOwner());
        data.put("exp_date", u.getDateString(key.getExpDate()));
        ksql.update("KEYMANAGEMENT.Openers", u.getDBMap("id", key.getID()), data);
        u.reloadTable(keyTable, uidCol, idCol, ownerCol, expDateCol, searchField);
        ksql.addLog(key, Action.UPDATEKEY);
        u.sendAlert(statusBar, "Key " + key.getID() + " wurde erfolgreich geändert.");
        EditBox.setVisible(false);
    }

    /**
     * Öffnet AddPage
     */
    @FXML
    private void onAddClick() {
        if(AddBox.isVisible()){
            AddBox.setVisible(false);
            idAddField.setText(null);
            ownerAddField.setText(null);
            expDateAddField.setValue(null);
        } else {
            EditBox.setVisible(false);
            AddBox.setVisible(true);
            RemoveBox.setVisible(false);
        }
    }

    /**
     * Hinzufügen Button auf der Add-Seite
     */
    @FXML
    private void onAddKeyClick(){
        Key key;
        if(ownerAddField.getText() == null || expDateAddField.getValue()== null){
            key = new Key(CURRENT_KEY.getUID(), idAddField.getText());
        } else {
            key = new Key(CURRENT_KEY.getUID(), idAddField.getText(), ownerAddField.getText(), expDateAddField.getValue().toString());
        }
        ksql.insertKey(key);
        u.sendAlert(statusBar, "Key " + key.getID() + " wurde \nerfolgreich hinzugefügt.");
        u.reloadTable(keyTable, uidCol, idCol, ownerCol, expDateCol, searchField);
        keyTable.getSelectionModel().select(key);
        ksql.addLog(keyTable.getSelectionModel().getSelectedItem(), Action.ADDKEY);
        AddBox.setVisible(false);
        expDateAddField.setValue(null);
        ownerAddField.setText(null);
        idAddField.setText(null);
    }

    /**
     * Greift wenn auf der Remove-Page auf ja geklickt wird
     */
    @FXML
    private void onRemoveYesClick(){
        Key key = keyTable.getSelectionModel().getSelectedItem();
        ksql.delete(TABLE_KEYS, u.getDBMap("id", key.getID()));
        u.reloadTable(keyTable, uidCol, idCol, ownerCol, expDateCol, searchField);
        ksql.addLog(key, Action.REMOVEKEY);
        u.sendAlert(statusBar, "Key " +  key.getID() + " wurde gelöscht.");
        RemoveBox.setVisible(false);
    }

    /**
     * Greift wenn auf der Remove-Page auf nein geklickt wird
     */
    @FXML
    private void onRemoveNoClick(){
        RemoveBox.setVisible(false);
    }

    /**
     * Öffnet Raumverwaltungs-Seite
     */
    @FXML
    private void onRoomManagementClick() throws IOException {
        RoomManagementPage rm = new RoomManagementPage();
        rm.start();
    }

    /**
     * Druckt Dokument für die Vergabe eines Schlüssels
     */
    @FXML
    private void onAddKeyPrintClick() {

    }

    /**
     * Druckt Dokument für die Abgabe eines Schlüssels
     */
    @FXML
    private void onRemoveKeyPrintClick(){

    }

    /**
     * Druckt Dokument aus
     */
    @FXML
    private void onManualDocumentClick(){

    }

    /**
     * Öffnet Seite mit Infos über alle Schlüssel
     */
    @FXML
    private void onInventoryClick() throws IOException {
        InventoryPage invPage = new InventoryPage();
        invPage.start();
    }

}