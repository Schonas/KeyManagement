package de.budde.km.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KeySQL extends MySQL {

    private static final String TABLE = "Keys";

    public ResultSet getKeys(){

        statement = "SELECT * FROM KEYMANAGEMENT.Keys";
        try {
            pStmt= conn.prepareStatement(statement);
            //pStmt.setString(1, TABLE);
            return pStmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insertKey(){

    }


}