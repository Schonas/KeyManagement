package de.schonas.keymanagement.roominfo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static de.schonas.keymanagement.main.MainPage.LOGO;
import static de.schonas.keymanagement.main.MainPage.TITLE;

public class RoomManagementPage {

    public void start () throws IOException {

        Stage stage = new Stage();
        Parent pane = FXMLLoader.load(getClass().getResource("RoomManagementController.fxml"));
        Scene scene = new Scene(pane);

        stage.getIcons().add(LOGO);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.setHeight(400);
        stage.setWidth(600);
        stage.setTitle(TITLE + " | Raumverwaltung");
        stage.show();

    }
}
