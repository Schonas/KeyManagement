package de.schonas.keymanagement.inventory;

import de.schonas.keymanagement.database.KeySQL;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import static de.schonas.keymanagement.main.MainPage.ksql;

public class InventoryController {

    @FXML
    private Text allKeys, occupiedKeys, freeKeys;

    /**
     * Trägt werte aus DB in Textfelder ein
     */
    @FXML
    protected void initialize() {
        //DATEN AUS DB ABRUFEN UND AUF TEXTE SCHREIBEN
        allKeys.setText(String.valueOf(ksql.getKeyAmount("SELECT COUNT(id) FROM " + KeySQL.TABLE)));
        occupiedKeys.setText(String.valueOf(ksql.getKeyAmount("SELECT COUNT(id) FROM " + KeySQL.TABLE + " WHERE owner IS NOT NULL")));
        freeKeys.setText(String.valueOf(ksql.getKeyAmount("SELECT COUNT(id) FROM " + KeySQL.TABLE + " WHERE owner IS NULL")));
    }


}
