package de.schonas.keymanagement.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.schonas.keymanagement.main.MainPage.prop;

public class MySQL {

    /**
     * Attribute / hÃ¤ufig genutzte Variablen
     */
    Connection conn;
    PreparedStatement pStmt;
    String statement;
    ResultSet res;

    /**
     * legt neue Connection als Objekt an
     */
    MySQL() {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.err.println("Exception: " + ex.getMessage());
        }
        createConn();

    }

    /**
     * stellt Verbindung zur Datenbank her
     */
    private void createConn() {

        String serverName = prop.getProperty("host");
        String schema = prop.getProperty("database");
        String url = "jdbc:mysql://" + serverName + "/" + schema + "?autoReconnect=true";
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Beendet DB Verbindung
     */
    private void closeConn() {

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Baut Verbindung zur DB neu auf
     */
    private void reconnect() {
        closeConn();
        createConn();
        System.out.print("Verbindung zur Datenbank wird neuaufgebaut!");
    }


    /**
     * Fügt neuen DB Eintrag hinzu
     *
     * @param table  Tabelle
     * @param values Spalten + Werte
     */
    public void insert(String table, Map<String, Object> values) {

        statement = "INSERT INTO " + table + " (";
        for (String s : values.keySet()) {
            statement += "`" + s + "`, ";
        }
        statement = statement.substring(0, statement.length() - 2) + ") VALUES (";

        for (int i = 0; i < values.values().size(); i++) {
            statement += "?, ";
        }
        statement = statement.substring(0, statement.length() - 2) + ")";

        try {
            pStmt = conn.prepareStatement(statement);
            setObjects(pStmt, values).executeUpdate();
            interrupt();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Liefert gewünschte Spalten einer Tabelle
     * @param table Tabelle
     * @param label Spalte
     * @return Ergebnisse als Map
     */
    public List<Object> getList(String table, String label) {

        try {
            statement = "SELECT " + label + " FROM " + table;
            pStmt = conn.prepareStatement(statement);
            res = pStmt.executeQuery();
            List<Object> results = new ArrayList<>();
            while (res.next()) {
                results.add(res.getObject(1));
            }
            interrupt();
            return results;
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Liefert gewünschte Spalten einer Tabelle
     * @param table Tabelle
     * @param column Spalte
     * @return Ergebnisse als Map
     */
    public List<Object> getList(String table, String column, String filter, String label) {

        try {
            statement = "SELECT " + label + " FROM " + table + " WHERE " + column + " = ?";
            pStmt = conn.prepareStatement(statement);
            pStmt.setObject(1, filter);
            res = pStmt.executeQuery();
            List<Object> results = new ArrayList<>();
            while (res.next()) {
                results.add(res.getObject(1));
            }
            interrupt();
            return results;
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Liefert mehrere Spalten durch mehrere Attribute gefiltert
     * @param table Tabelle
     * @param filter Filter
     * @param columns GewÃ¼nschte Spalten
     * @return Werte
     */
    public HashMap<String, Object> get(String table, Map<String, Object> filter, String... columns) {

        try {
            statement = "SELECT ";
            for (String c : columns) {
                statement += c + ", ";
            }
            statement = statement.substring(0, statement.length() - 2) + " FROM " + table + " WHERE ";
            for (String column : filter.keySet()) {
                statement += column + " = ? AND ";
            }
            statement = statement.substring(0, statement.length() - 5);
            pStmt = conn.prepareStatement(statement);
            setObjects(pStmt, filter);
            res = pStmt.executeQuery();
            HashMap<String, Object> results = new HashMap<>();
            while (res.next()) {
                for (int i = 0; i < columns.length; i++) {
                    results.put(columns[i], res.getObject(i + 1));
                    System.out.println(columns[i] + " " + res.getObject(i + 1));
                }
            }
            interrupt();
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * PrÃ¼ft ob Wert in Tabelle enthalten ist
     *
     * @param table  Tabelle
     * @param values Map aus Spalten + Werten
     * @return Ergebnis
     */
    public boolean contains(String table, Map<String, Object> values) {

        statement = "SELECT * FROM " + table + " WHERE ";
        for (String s : values.keySet()) {
            statement += s + " = ? AND ";
        }
        statement = statement.substring(0, statement.length() - 5);
        try {
            pStmt = conn.prepareStatement(statement);
            res = setObjects(pStmt, values).executeQuery();
            interrupt();
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Löscht Einträge aus DB
     *
     * @param table  Tabelle
     * @param filter Bed. für zu löschende Einträge
     */
    public void delete(String table, Map<String, Object> filter) {

        statement = "DELETE FROM " + table + " WHERE ";
        for (String s : filter.keySet()) {
            statement += s + " = ? AND ";
        }
        statement = statement.substring(0, statement.length() - 5);
        try {
            pStmt = conn.prepareStatement(statement);
            setObjects(pStmt, filter);
            pStmt.executeUpdate();
            interrupt();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updatet ausgewählten Eintrag
     * @param table Tabelle
     * @param filter Map zum Filtern der EintrÃ¤ge
     * @param values Neue Werte
     */
    public void update(String table, Map<String, Object> filter, Map<String, Object> values) {

        try {
            statement = "UPDATE " + table + " SET ";
            for (String column : values.keySet()) {
                statement += "`" + column + "` = ?, ";
            }
            statement = statement.substring(0, statement.length() - 2) + " WHERE ";
            for (String column : filter.keySet()) {
                statement += column + " = ? AND ";
            }


            statement = statement.substring(0, statement.length() - 5);
            pStmt = conn.prepareStatement(statement);

            int i = 1;
            for (String column : values.keySet()) {
                pStmt.setObject(i, values.get(column));
                i++;
            }
            for (String column : filter.keySet()) {
                pStmt.setObject(i, filter.get(column));
                i++;
            }

            pStmt.executeUpdate();
            interrupt();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Hilfsmethode zum Einsetzen der Where Bed.
     *
     * @param statement Statement
     * @param values    Werte
     * @return vollst. Statement
     */
    private PreparedStatement setObjects(PreparedStatement statement, Map<String, Object> values) {

        int i = 0;
        for (String s : values.keySet()) {
            i++;
            try {
                statement.setObject(i, values.get(s));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return statement;

    }

    void interrupt() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}