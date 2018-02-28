package de.schonas.keymanagement.keyinfo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static de.schonas.keymanagement.main.MainPage.LOGO;
import static de.schonas.keymanagement.main.MainPage.TITLE;

public class KeyInfo {

    public void start () throws IOException {

        Stage stage = new Stage();
        Parent pane = FXMLLoader.load(getClass().getResource("KeyInfoController.fxml"));
        Scene scene = new Scene(pane);

        stage.getIcons().add(LOGO);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.setTitle(TITLE + " | Schlüsselinformationen");
        stage.show();
    }
}
