package de.schonas.keymanagement.database;

import de.schonas.keymanagement.util.Action;
import de.schonas.keymanagement.Key;
import de.schonas.keymanagement.Room;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.schonas.keymanagement.main.MainPage.ksql;
import static de.schonas.keymanagement.main.MainPage.u;

public class KeySQL extends MySQL {

    public static final String TABLE_KEYS = "KEYMANAGEMENT.Openers";
    public static final String TABLE_ACCESS = "KEYMANAGEMENT.Access";
    private static final String TABLE_ROOMS = "KEYMANAGEMENT.Rooms";
    private static final String TABLE_DEPARTMENTS = "KEYMANAGEMENT.Departments";

    /**
     * Liefer alle Schlüssel als RS
     * @return ResultSet aller Schlüssel
     */
    public ResultSet getKeys(){

        statement = ("SELECT * FROM " + TABLE_KEYS);
        try {
            pStmt= conn.prepareStatement(statement);
            //pStmt.setString(1, TABLE_KEYS);
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
        Map<String, Object> map = u.getDBMap("id", key.getID());
        map.put("owner", key.getOwner());
        map.put("exp_date", u.getDateString(key.getExpDate()));
        ksql.insert(TABLE_KEYS, map);
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
            return !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Listet irgendwas auf
     * @param
     * @return
     */
    public ResultSet getRooms(String keyID){

        statement = "SELECT * FROM ? o JOIN " + TABLE_ACCESS + " a ON o.id = a.key_id RIGHT OUTER JOIN " + TABLE_ROOMS
                + " r ON r.uid = a.room_id RIGHT OUTER JOIN " + TABLE_DEPARTMENTS + " d ON d.uid = r.department_id WHERE o.id = ?";
        try {
            pStmt = conn.prepareStatement(statement);
            pStmt.setString(1, TABLE_KEYS);
            pStmt.setString(2, keyID);
            return pStmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Gib alle Räume und ihre zugehörigen Departments als ResultSet
     * @return ResultSet Raum, Department
     */
    public ResultSet getRooms(){
        statement = "SELECT r.uid AS id, name FROM " + TABLE_ROOMS + " r JOIN " + TABLE_DEPARTMENTS + " d ON d.uid = r.department_id;";
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
        statement = "SELECT DISTINCT name FROM " + TABLE_DEPARTMENTS;
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

        statement = "SELECT id FROM " + TABLE_KEYS;
        try {
            pStmt= conn.prepareStatement(statement);
            //pStmt.setString(1, TABLE_KEYS);
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
        statement = "SELECT uid FROM " + TABLE_KEYS + " WHERE id = ?";
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

    /**
     * Fügt Logeintrag hinzu
     * @param key verwendeter Schlüssel
     * @param action ADD REMOVE OR UPDATE
     */
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
        statement = "SELECT * FROM " + TABLE_ACCESS + " WHERE key_id = ?";
        ResultSet rs;
        try {
            pStmt = conn.prepareStatement(statement);
            pStmt.setString(1, keyID);
            rs = pStmt.executeQuery();
            Room r;
            while (rs.next()){
                r = new Room(rs.getString("room_id"), "Verwaltung, Recht und Steuern");
                rooms.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Gibt eine Liste von Räumen aus die ein KeyType öffnen kann
     * @param keyID keyType
     * @return Liste von roomIds die der Schlüssel öffnen kann
     */
    public List<String> getAccessibleRoomIDs(String keyID){
        List<String> room_ids = new ArrayList<>();
        statement = "SELECT * FROM " + TABLE_ACCESS + " WHERE key_id = ?";
        ResultSet rs;
        try {
            pStmt = conn.prepareStatement(statement);
            pStmt.setString(1, keyID);
            rs = pStmt.executeQuery();
            while (rs.next()){
                room_ids.add(rs.getString("room_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room_ids;
    }

    /**
     * Fügt room_id - key_id Paar in Tabelle ein wenn es noch nicht da ist
     * @param room_id Raum ID
     * @param key_id Schlüssel ID
     */
    public void insertIfNotExists(String room_id, String key_id){
        Map values = u.getDBMap("key_id", key_id);
        values.put("room_id", room_id);
        if(!contains(TABLE_ACCESS, values)){
            insert(TABLE_ACCESS, values);
        }
    }

    /**
     * Löscht Eintrag eines room_id - key_id Paares wenn es drin ist.
     * @param room_id
     * @param key_id
     */
    public void deleteIfExists(String room_id, String key_id){
        Map values = u.getDBMap("key_id", key_id);
        values.put("room_id", room_id);
        if(contains(TABLE_ACCESS, values)){
            delete(TABLE_ACCESS, values);
        }
    }

    /**
     * Legt Tabellen an
     * @throws SQLException
     */
    public void createTables() throws SQLException {
        String s;
        StringBuffer sb = new StringBuffer();

        try {
            FileReader fr = new FileReader(new File("de/schonas/keymanagement/database/tables.sql"));
            // be sure to not have line starting with "--" or "/*" or any other non aplhabetical character
            BufferedReader br = new BufferedReader(fr);

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }

            br.close();

            String[] inst = sb.toString().split(";");
            Statement stmt = conn.createStatement();
            for (int i = 0; i < inst.length; i++) {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if (!inst[i].trim().equals("")) {
                    stmt.executeUpdate(inst[i]);
                    System.out.println(">>" + inst[i]);
                }
            }

        } catch (Exception e) {
            System.out.println("[Error] " + e.toString());
        }
    }

}