package de.schonas.keymanagement.inventory;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import static de.schonas.keymanagement.main.MainPage.ksql;

public class InventoryController {

    @FXML
    private Text allKeys, occupiedKeys, freeKeys;

    @FXML
    protected void initialize() {
        //DATEN AUS DB ABRUFEN UND AUF TEXTE SCHREIBEN
        allKeys.setText(String.valueOf(ksql.getKeyAmount("SELECT COUNT(id) FROM KEYMANAGEMENT.Keys")));
        occupiedKeys.setText(String.valueOf(ksql.getKeyAmount("SELECT COUNT(id) FROM KEYMANAGEMENT.Keys WEHRE owner = ''")));
        freeKeys.setText(String.valueOf(ksql.getKeyAmount("SELECT COUNT(id) FROM KEYMANAGEMENT.Keys WEHRE owner != ''")));
    }


}
