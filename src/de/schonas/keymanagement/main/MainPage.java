package de.schonas.keymanagement.main;

import de.schonas.keymanagement.Key;
import de.schonas.keymanagement.database.KeySQL;
import de.schonas.keymanagement.util.print.Print;
import de.schonas.keymanagement.util.Utils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class MainPage extends Application {

    public static Utils u;
    public static KeySQL ksql;
    public static Properties prop;
    public static Key CURRENT_KEY;

    public static Image LOGO = new Image("de/schonas/keymanagement/images/icon.png");
    public static String TITLE = "Key Management";

    @Override
    public void start(Stage stage) throws IOException {

        Parent pane = FXMLLoader.load(getClass().getResource("MainController.fxml"));
        Scene scene = new Scene(pane);

        stage.setWidth(750);
        stage.setHeight(530);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.getIcons().add(LOGO);
        stage.show();

        stage.setOnCloseRequest(e -> Platform.exit());
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws SQLException {
        prop = new Properties();
        u = new Utils();
        try {
            InputStream input = new FileInputStream("config");
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ksql = new KeySQL();
        //ksql.createTables();
        //printer = new Print();
    }

}