package de.schonas.keymanagement.keyinfo;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import static de.schonas.keymanagement.main.MainPage.*;

public class KeyInfoController {

    @FXML
    private Text keyInfoUIDField,keyInfoIDField,keyInfoOwnerField, keyInfoExpDateField, roomView;

    @FXML
    private TableView historyTable;

    /**
     * Trägt Werte aus DB in die Felder ein
     */
    @FXML
    protected void initialize() {

        historyTable.getStylesheets().add("de/schonas/keymanagement/CSS/tableStylesheet");

        keyInfoIDField.setText(CURRENT_KEY.getID());
        keyInfoUIDField.setText(CURRENT_KEY.getUID());

        if(CURRENT_KEY.getOwner() != null) {
            keyInfoOwnerField.setText(CURRENT_KEY.getOwner());
        } else {
            keyInfoOwnerField.setText("-");
        }

        if(!CURRENT_KEY.getExpDate().isEmpty()) {
            keyInfoExpDateField.setText(CURRENT_KEY.getExpDate());
        } else {
            keyInfoExpDateField.setText("-");
        }

        roomView.setText(u.getAccessibleKeyList(CURRENT_KEY.getID()));
    }

}
