package de.schonas.keymanagement.inventory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static de.schonas.keymanagement.main.MainPage.LOGO;
import static de.schonas.keymanagement.main.MainPage.TITLE;

public class InventoryPage {

    public void start () throws IOException {

        Stage stage = new Stage();
        Parent pane = FXMLLoader.load(getClass().getResource("InventoryController.fxml"));
        Scene scene = new Scene(pane);

        stage.getIcons().add(LOGO);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
    }
}
