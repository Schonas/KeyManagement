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
        allKeys.setText(String.valueOf(ksql.getKeyAmount("SELECT COUNT(id) FROM " + KeySQL.TABLE_KEYS)));
        occupiedKeys.setText(String.valueOf(ksql.getKeyAmount("SELECT COUNT(id) FROM " + KeySQL.TABLE_KEYS + " WHERE owner IS NOT NULL")));
        freeKeys.setText(String.valueOf(ksql.getKeyAmount("SELECT COUNT(id) FROM " + KeySQL.TABLE_KEYS + " WHERE owner IS NULL")));
    }


}
