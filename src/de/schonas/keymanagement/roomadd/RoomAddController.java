package de.schonas.keymanagement.roomadd;


import de.schonas.keymanagement.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static de.schonas.keymanagement.main.MainPage.*;

public class RoomAddController {


    @FXML
    TextField idField, depField, cylField;

    @FXML
    public Text alterField;

    /**
     * Beim Click auf "Hinzufügen"
     * @throws SQLException
     */
    @FXML
    public void onAddRoomClick() throws SQLException {
        Room r = new Room(idField.getText(), depField.getText(), cylField.getText());
        System.out.println(r.getID());
        System.out.println(r.getDepartment());
        System.out.println(r.getCylinder());
        System.out.println(ksql.getRoomIDs().contains(r.getID().getValue()));
        if(!ksql.getRoomIDs().contains(r.getID().getValue())) {
            ksql.insertRoom(r);
        } // else u.sendAlert(alterField, "Diesen Raum gibt es bereits!");
    }

    @FXML
    public void onDelRoomClick() {

    }
}