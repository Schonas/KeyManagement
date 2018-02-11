package de.schonas.keymanagement.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddPageController {

    @FXML
    private TextField uidField, ownerField;

    @FXML
    private DatePicker expDateField;

    @FXML
    private void addButtonClick(){

    }

    @FXML
    private void clearButtonClick(){
        expDateField.getEditor().clear();
        uidField.setText(null);
        ownerField.setText(null);
    }

}
