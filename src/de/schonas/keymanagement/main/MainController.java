package de.schonas.keymanagement.main;

import de.schonas.keymanagement.settings.SettingsPage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static de.schonas.keymanagement.main.MainPage.u;


public class MainController {

    @FXML // fx:id="keyTable"
    private TableView<Key> KeyTable;

    @FXML // fx:id="uniqueID"
    private TableColumn<Key, String> UniqueID;

    @FXML // fx:id="owner"
    private TableColumn<Key, String> Owner;

    @FXML // fx:id="expireDate"
    private TableColumn<Key, String> ExpireDate;

    @FXML
    private Button editButton,addButton,removeButton;

    @FXML
    private TextField uidField, ownerField;

    @FXML
    private DatePicker dateField;

    @FXML
    private VBox EditBox;

    @FXML
    private Text statusBar;

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
            stage.getIcons().add(new Image("http://www.dresden.ihk.de/apple-touch-icon.png"));
            alert.setContentText("Du musst ein Schlüssel auswählen!");
            alert.show();
        }
    }

    /**
     * Lädt Tabelle neu
     */
    @FXML
    private void onReloadClick(){
        initialize();
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
    private void onAddClick() throws IOException {
        AddPage ad = new AddPage();
        ad.start();
    }

    /**
     * Befüllt die Tabelle
     */
    @FXML //basically works like an onload() method
    protected void initialize() {

        //setting up the columns
        PropertyValueFactory<Key, String> uidProperty = new PropertyValueFactory<>("UniqueID");
        PropertyValueFactory<Key, String> ownerProperty = new PropertyValueFactory<>("Owner");
        PropertyValueFactory<Key, String> expDateProperty = new PropertyValueFactory<>("ExpireDate");

        //setting up the main data source
        UniqueID.setCellValueFactory(uidProperty);
        Owner.setCellValueFactory(ownerProperty);
        ExpireDate.setCellValueFactory(expDateProperty);

        DataSource data = new DataSource();
        ObservableList<Key> tableItems = data.getData();
        KeyTable.setItems(tableItems);

    }
}