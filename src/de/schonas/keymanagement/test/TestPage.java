package de.schonas.keymanagement.test;

import de.schonas.keymanagement.util.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static de.schonas.keymanagement.main.MainPage.*;

public class TestPage extends Application {

    Utils u;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        u = new Utils();

        String dateString = "2018-02-18";
        System.out.println(u.getDateString(dateString));
        //LocalDate localDate = u.getLocalDateFromLocalDateString(date);
        //System.out.println(localDate);
        //System.out.println(u.getLocalDateFromLocalDateString(date).toString());

        // System.out.println(ksql.getKeyAmount("SELECT COUNT(id) FROM KEYMANAGEMENT.Keys WEHRE owner = ''"));
        System.exit(0);
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
