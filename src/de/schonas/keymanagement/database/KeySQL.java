package de.schonas.keymanagement.database;

import de.schonas.keymanagement.main.Action;
import de.schonas.keymanagement.main.Key;
import de.schonas.keymanagement.main.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    public ResultSet getRooms(String keyID){

        statement = "SELECT * FROM Openers o JOIN Access a ON o.id = a.key_id RIGHT OUTER JOIN Rooms r ON r.uid = a.room_id " +
                "RIGHT OUTER JOIN Departments d ON d.uid = r.department_id WHERE o.id = ?";
        try {
            pStmt = conn.prepareStatement(statement);
            pStmt.setString(1, keyID);
            return pStmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public ResultSet getRooms(){
        statement = "SELECT r.uid AS id, name FROM Rooms r JOIN Departments d ON d.uid = r.department_id;";
        try {
            pStmt = conn.prepareStatement(statement);
            return pStmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sucht alle Namen der Abteilungen aus DB
     * @return Alle Namen der Abteilungen als String in einer Liste
     */
    public List<String> getDepartments(){
        List<String> departments = new ArrayList<>();
        statement = "SELECT DISTINCT name FROM Departments";
        ResultSet rs;
        try{
            pStmt = conn.prepareStatement(statement);
            rs = pStmt.executeQuery();
            while(rs.next()){
                departments.add(rs.getString("name"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return departments;
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
     *
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

    public void addLog(Key key, Action action){
        Map<String, Object> logInfos = u.getDBMap("user", u.getSystemUserName());
        logInfos.put("workspace", u.getRemoteHostName());
        logInfos.put("action", getActionLog(key, action));
        ksql.insert("Log", logInfos);
    }

    /**
     * Gibt Actionlog zurück
     * @param key
     * @param action
     * @return
     */
    public String getActionLog(Key key, Action action){
        String actionString = ("Key (UID: " + key.getUID() + ") wurde ");
        switch (action){
            case ADDKEY:    actionString += "hinzugefügt.";
                            break;
            case REMOVEKEY: actionString += "entfernt.";
                            break;
            case UPDATEKEY: actionString += "geändert.";
                            break;
            default:        actionString = "ERROR";
                            break;
        }
        return actionString;
    }

    /**
     * Gibt Räume aus die ein Schlüssel-Typ öffnen kann
     * @param keyID Schlüssel ID
     * @return Räume die der Schlüssel öffnen kann
     */
    public List<Room> getAccessibleRooms(String keyID){
        List<Room> rooms = new ArrayList<>();
        statement = "SELECT * FROM Access WHERE key_id = ?";
        ResultSet rs;
        try {
            pStmt = conn.prepareStatement(statement);
            pStmt.setString(1, keyID);
            rs = pStmt.executeQuery();
            Room r;
            while (rs.next()){
                r = new Room(rs.getString("room_id"), "Verwaltung, Recht und Steuern");
                System.out.println(r.getID().getName());
                rooms.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
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