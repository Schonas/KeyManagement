package de.budde.km.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddPage {

    public void start () throws IOException {
        Stage stage = new Stage();
        Parent pane = FXMLLoader.load(getClass().getResource("AddPageController.fxml"));
        Scene scene = new Scene(pane);
        //stage.getIcons().add(new Image("http://www.dresden.ihk.de/apple-touch-icon.png"));
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.setTitle("Add a new Key");
        stage.show();
    }
}
