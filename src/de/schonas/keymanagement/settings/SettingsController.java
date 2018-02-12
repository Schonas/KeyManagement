package de.schonas.keymanagement.settings;


import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static de.schonas.keymanagement.main.MainPage.ksql;
import static de.schonas.keymanagement.main.MainPage.prop;

public class SettingsController {

    @FXML
    private TextField settingsHostField, settingsSchemaField, settingsUsernameField;

    @FXML
    private Text settingsConValueField;

    @FXML
    private PasswordField settingsPasswordField;

    @FXML
    protected void initialize() {

        settingsHostField.setText(prop.getProperty("host"));
        settingsSchemaField.setText(prop.getProperty("database"));
        settingsUsernameField.setText(prop.getProperty("username"));
        settingsPasswordField.setText(prop.getProperty("password"));

    }

    @FXML
    private void onTestConClick() throws IOException {

        if(ksql.testConnection(settingsHostField.getText(), settingsSchemaField.getText(), settingsUsernameField.getText(), settingsPasswordField.getText())){
            settingsConValueField.setText("Connected!");
        } else {
            settingsConValueField.setText("Connection failed.");
        }
    }

    @FXML
    private void onSaveButtonClick() {
        try {
            FileInputStream in = new FileInputStream("config");
            Properties props = new Properties();
            props.load(in);
            in.close();
            FileOutputStream out = new FileOutputStream("config");
            props.setProperty("host", settingsHostField.getText());
            props.setProperty("database", settingsSchemaField.getText());
            props.setProperty("username", settingsUsernameField.getText());
            props.setProperty("password", settingsPasswordField.getText());
            props.store(out, "Config Datei für Schlüsselverwaltungssystem");
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
