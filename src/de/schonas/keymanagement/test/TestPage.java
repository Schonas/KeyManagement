package de.schonas.keymanagement.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;

import static de.schonas.keymanagement.main.MainPage.LOGO;
import static de.schonas.keymanagement.main.MainPage.TITLE;
import static de.schonas.keymanagement.main.MainPage.ksql;

public class TestPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {


        System.out.println(ksql.getKeyAmount("SELECT COUNT(id) FROM KEYMANAGEMENT.Keys WEHRE owner = ''"));

        /*
        Parent pane = FXMLLoader.load(getClass().getResource("TestController.fxml"));
        Scene scene = new Scene(pane);

        stage.getIcons().add(LOGO);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.setTitle(TITLE + " | Test");
        stage.show();
        */
    }
}