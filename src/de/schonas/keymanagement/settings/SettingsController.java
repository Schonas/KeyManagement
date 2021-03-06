package de.schonas.keymanagement.settings;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

import static de.schonas.keymanagement.main.MainPage.*;

public class SettingsController {

    @FXML
    private TextField settingsHostField, settingsDatabaseField, settingsUsernameField;

    @FXML
    private Text settingsConValueField;

    @FXML
    private PasswordField settingsPasswordField;

    /**
     * Holt Werte aus Config und setzt sie in die Textfelder
     */
    @FXML
    protected void initialize() {

        settingsHostField.setText(prop.getProperty("host"));
        settingsDatabaseField.setText(prop.getProperty("database"));
        settingsUsernameField.setText(prop.getProperty("username"));
        settingsPasswordField.setText(prop.getProperty("password"));

    }

    /**
     * Testet Connection mit den eingegebenen Parametern
     * @throws IOException
     */
    @FXML
    private void onTestConClick() throws IOException {
        //TODO: handle or declare, nicht beides
        try{
            if(ksql.testConnection(settingsHostField.getText(), settingsDatabaseField.getText(), settingsUsernameField.getText(), settingsPasswordField.getText())){
                settingsConValueField.setText("Connected!");
                settingsConValueField.setFill(Color.GREEN);
            }
        } catch (Exception e){
            settingsConValueField.setText("Connection failed.");
            settingsConValueField.setFill(Color.RED);
        }
    }

    /**
     * Holt Werte aus Textfeldern und setzt sie in der Config
     */
    @FXML
    private void onSaveButtonClick() {
        try {
            FileInputStream in = new FileInputStream("config");
            Properties props = new Properties();
            props.load(in);
            in.close();
            FileOutputStream out = new FileOutputStream("config");
            props.setProperty("host", settingsHostField.getText());
            props.setProperty("database", settingsDatabaseField.getText());
            props.setProperty("username", settingsUsernameField.getText());
            props.setProperty("password", settingsPasswordField.getText());
            props.store(out, "Config Datei für Schlüsselverwaltungssystem");
            out.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(TITLE);
            alert.setHeaderText("Erfolg!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(LOGO);
            alert.setContentText("Einstellungen wurden erfolgreich gespeichert.");
            alert.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
