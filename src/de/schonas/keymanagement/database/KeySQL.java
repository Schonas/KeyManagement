package de.schonas.keymanagement.database;

import de.schonas.keymanagement.main.Key;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static de.schonas.keymanagement.main.MainPage.ksql;
import static de.schonas.keymanagement.main.MainPage.prop;
import static de.schonas.keymanagement.main.MainPage.u;

public class KeySQL extends MySQL {

    public static final String TABLE = "KEYMANAGEMENT.Keys";

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

    public void insertKey(Key key){
        Map map = u.getDBMap("uid", key.getUniqueID());
        map.put("owner", key.getOwner());
        map.put("expire_date", Date.valueOf(key.getExpireDate()));
        ksql.insert("KEYMANAGEMENT.Keys", map);
    }

    public boolean testConnection(String serverName, String schema, String username, String password){

        String url = "jdbc:mysql://" + serverName + "/" + schema;
        try {
            conn = DriverManager.getConnection(url, username, password);
            if(conn.isClosed()){
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}