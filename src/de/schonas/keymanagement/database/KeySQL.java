package de.schonas.keymanagement.database;

import de.schonas.keymanagement.main.Key;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.schonas.keymanagement.main.MainPage.ksql;
import static de.schonas.keymanagement.main.MainPage.u;

public class KeySQL extends MySQL {

    public static final String TABLE = "KEYMANAGEMENT.Openers";

    /**
     * Liefer alle Schlüssel als RS
     * @return ResultSet aller Schlüssel
     */
    public ResultSet getKeys(){

        statement = "SELECT * FROM KEYMANAGEMENT.Openers";
        try {
            pStmt= conn.prepareStatement(statement);
            //pStmt.setString(1, TABLE);
            return pStmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Trägt Schlüssel in Datenbank ein
     * @param key Schlüssel
     */
    public void insertKey(Key key){
        Map map = u.getDBMap("id", key.getID());
        map.put("owner", key.getOwner());
        map.put("exp_date", u.getDateString(key.getExpDate()));
        ksql.insert(TABLE, map);
    }

    /**
     * Testet Connection zu den in den Settings eingetragenen Server
     * @param serverName Host-IP
     * @param schema DB name
     * @param username DB Username
     * @param password DB User Password
     * @return Verbindung erfolgreich oder nicht
     */
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

    /**
     * Listet irgendwas auf
     * @param key
     * @return
     */
    public List<String> getRooms(Key key){
        List<String> allowedKeys = new ArrayList<>();

        return allowedKeys;
    }

    /**
     * Liefert alle verschiedenen Schlüsseltypen
     * @return Schlüsseltypen mit unterschiedlicher ID
     */
    public List<String> getKeyTypes() {
        List<String> keyTypes = new ArrayList<>();

        ResultSet rs = getResultTypes();
        String id;
        try {
            while (rs.next()) {
                id = rs.getString("id");
                if (!keyTypes.contains(id)) keyTypes.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keyTypes;
    }

    /**
     * ???????????????????????????????????????????
     * @return braucht man für die Methode da oben
     */
    private ResultSet getResultTypes(){

        statement = "SELECT id FROM KEYMANAGEMENT.Openers";
        try {
            pStmt= conn.prepareStatement(statement);
            //pStmt.setString(1, TABLE);
            return pStmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Liefert Schlüsselanzahl nach den im String festgelegten Einschränkungen
     * @param statement Statement
     * @return Schlüsselanzahl
     */
    public int getKeyAmount(String statement){
        this.statement = statement;
        ResultSet rs;
        try {
            pStmt= conn.prepareStatement(this.statement);
            rs = pStmt.executeQuery();
            if(!rs.next()){
                return 0;
            } else {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Liefert UID eines Schlüssels was aber jetzt eigentlich komplett unnötig ist
     * @param key
     * @return
     */
    public int getKeyUID(Key key){
        statement = "SELECT uid FROM Openers WHERE id = ?";
        ResultSet rs;
        try {
            pStmt = conn.prepareStatement(statement);
            pStmt.setString(1, key.getID());
            rs = pStmt.executeQuery();
            if(!rs.next()){
                return 0;
            } else {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void addLog(Key key){

    }

    /**
     * Legt Tabellen an
     * @throws SQLException
     */
    public void createTables() throws SQLException {
        String keysTable = "CREATE TABLE IF NOT EXISTS Openers" +
                "(uid INT PRIMARY KEY AUTO_INCREMENT," +
                "    id VARCHAR(30) NOT NULL," +
                "    owner VARCHAR(50)," +
                "    exp_date VARCHAR(10));" +
                "CREATE UNIQUE INDEX Openers_uid_uindex ON Openers(uid);";
        String roomsTable = "CREATE TABLE IF NOT EXISTS Rooms" +
                "(uid VARCHAR(30) PRIMARY KEY," +
                "  department_id INT);" +
                "CREATE UNIQUE INDEX Rooms_uid_uindex ON Rooms (uid);";
        String departmentsTable = "CREATE TABLE IF NOT EXISTS Departments" +
                "(uid INT PRIMARY KEY AUTO_INCREMENT," +
                "    name VARCHAR(50));" +
                "CREATE UNIQUE INDEX Departments_uid_uindex ON Departments(uid);";
        String accessTable = "CREATE TABLE IF NOT EXISTS Access" +
                "(key_id VARCHAR(50)," +
                "    room_id VARCHAR(50));";
        String logTable = "CREATE TABLE Log" +
                "(time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    user VARCHAR(30) NOT NULL," +
                "    action VARCHAR(300) NOT NULL);";

        Statement stmt = conn.createStatement();
        stmt.execute(keysTable);
        stmt.execute(roomsTable);
        stmt.execute(departmentsTable);
        stmt.execute(accessTable);
        stmt.execute(logTable);
    }

}