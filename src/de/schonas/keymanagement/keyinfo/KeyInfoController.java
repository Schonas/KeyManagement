package de.schonas.keymanagement.keyinfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import static de.schonas.keymanagement.main.MainPage.*;

public class KeyInfoController {

    @FXML
    private Text keyInfoUIDField,keyInfoIDField,keyInfoOwnerField, keyInfoExpDateField, roomView;

    /**
     * Trägt Werte aus DB in die Felder ein
     */
    @FXML
    protected void initialize() {
            keyInfoIDField.setText(currentKey.getID());
            keyInfoUIDField.setText(currentKey.getUID());
            if(currentKey.getOwner() != null) {
                keyInfoOwnerField.setText(currentKey.getOwner());
            } else {
                keyInfoOwnerField.setText("-");
            }
            if(!currentKey.getExpDate().isEmpty()) {
                keyInfoExpDateField.setText(currentKey.getExpDate());
            } else {
                keyInfoExpDateField.setText("-");
            }
            roomView.setText(u.getAccessibleKeyList(currentKey.getID()));
    }

}
