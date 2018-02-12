package de.schonas.keymanagement.settings;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static de.schonas.keymanagement.main.MainPage.logo;

public class SettingsPage {

    public void start () throws IOException {
        Stage stage = new Stage();
        Parent pane = FXMLLoader.load(getClass().getResource("SettingsController.fxml"));
        Scene scene = new Scene(pane);
        stage.getIcons().add(logo);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.setTitle("Key Management | Settings");
        stage.show();
    }
}