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
    private Text keyInfoUIDField,keyInfoIDField,keyInfoOwnerField;

    /**
     * Trägt Werte aus DB in die Felder ein
     */
    @FXML
    protected void initialize() {
            keyInfoIDField.setText(currentKey.getID());
            keyInfoUIDField.setText(currentKey.getUID());
            keyInfoOwnerField.setText(currentKey.getOwner());
    }

}
