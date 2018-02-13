package de.schonas.keymanagement.main;

import de.schonas.keymanagement.database.KeySQL;
import de.schonas.keymanagement.util.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainPage extends Application {

    //TODO: generell auf einheitliches Schema für Setzen von Leerzeichen und Leerzeilen achten. Dadurch wird es besser lesbar

    public static Utils u;
    public static KeySQL ksql;
    public static Stage gStage;
    public static Properties prop;
    //TODO: ist automatisch null
    InputStream input = null;
    //TODO: Sowas als Konstante machen -> static + final + name in Caps mit _
    //public static final Image LOGO_IHK = new Image("http://www.dresden.ihk.de/apple-touch-icon.png");
    public static Image logo = new Image("http://www.dresden.ihk.de/apple-touch-icon.png");

    @Override
    public void start(Stage stage) throws IOException {

        Parent pane = FXMLLoader.load(getClass().getResource("MainController.fxml"));
        Scene scene = new Scene(pane);
        gStage = stage;

        stage.getIcons().add(logo);
        //stage.getIcons().add(new Image("lib/images/icon.png"));
        stage.setWidth(750);
        stage.setHeight(530);
        stage.setResizable(false);
        stage.setScene(scene);
        //TODO: Sowas wie Title oder andere wichtige Werte in Konstanten oben speichern -> besserer Überblick wenn man was davon ändern will
        stage.setTitle("Key Management");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init(){
        prop = new Properties();
        try {
            input = new FileInputStream("config");
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        u = new Utils();
        ksql = new KeySQL();
    }
}