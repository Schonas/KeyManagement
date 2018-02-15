package de.schonas.keymanagement.database;

import de.schonas.keymanagement.main.Key;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.schonas.keymanagement.main.MainPage.ksql;
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
        Map map = u.getDBMap("id", key.getID());
        map.put("owner", key.getOwner());
        map.put("exp_date", Date.valueOf(key.getExpDate()));
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
            return false;
        }
    }

    public List<String> getRooms(Key key){
        List<String> allowedKeys = new ArrayList<>();

        return allowedKeys;
    }

    public List<String> getKeyTypes() {
        List<String> keyTypes = new ArrayList<>();

        ResultSet rs = getResultTypes();
        String id;
        System.out.println(rs);
        try {
            while (rs.next()) {
                id = rs.getString("id");
                System.out.println(id);
                if (!keyTypes.contains(id)) keyTypes.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keyTypes;
    }

    private ResultSet getResultTypes(){

        statement = "SELECT id FROM KEYMANAGEMENT.Keys";
        try {
            pStmt= conn.prepareStatement(statement);
            //pStmt.setString(1, TABLE);
            return pStmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getKeyAmount(String statement){
        this.statement = statement;
        ResultSet rs = null;
        this.statement = "";
        try {
            pStmt= conn.prepareStatement(this.statement);
            rs = pStmt.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}