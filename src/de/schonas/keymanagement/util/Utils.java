package de.schonas.keymanagement.util;

import de.schonas.keymanagement.Room;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static de.schonas.keymanagement.main.MainPage.ksql;
import static java.time.temporal.ChronoUnit.DAYS;

public class Utils {

    /**
     * Sendet Text in ein Textfeld und lässt diesen Text nach 5 sek verschwinden
     * @param t Textfeld
     * @param s Anzuzeigender Text
     */
    public void sendAlert(Text t, String s){
        t.setText(s);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                t.setText(null);
            }
        }, 5000);
    }

    /**
     * Liefert Hashmap mit einem KV Paar
     * @param column Key
     * @param value  Value
     * @return Map
     */
    public Map<String, Object> getDBMap(String column, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(column, value);
        return map;
    }

    /**
     * Lädt Tabelle mit Informationen der Datenbank neu
     * @param tableView Tabelle um die es geht
     * @param id ID Spalte
     * @param owner Besitzer Spalte
     * @param expDate Ablaufdatum Spalte
     * @param searchField Seachfield um Autocomplete usw zu aktivieren
     */
    public void reloadTable(TableView tableView, TableColumn uid, TableColumn id, TableColumn owner, TableColumn expDate, TextField searchField){
        TableData tb = new TableData(tableView, uid, id, owner, expDate, searchField);
        tb.load();
        tableView.getSelectionModel().select(0);
    }

    /**
     * Erzeugt ein LocalDate von einem String
     * @param dateString String mit dem Format dd.MM.yyyy
     * @return LocalDate
     */
    public LocalDate getLocalDateFromString(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        formatter = formatter.withLocale(Locale.GERMANY);
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Wandelt LocalDate String in ein normales Datum um
     * @param dateString Local Date String
     * @return String eines normalen Datums
     */
    public String getDateString(String dateString){

        SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outSDF = new SimpleDateFormat("dd.MM.yyyy");
        String outDate = "";
        if (dateString != null) try {
            java.util.Date date = inSDF.parse(dateString);
            outDate = outSDF.format(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return outDate;
    }

    /**
     * Liefert Differenz von String im Format dd.MM.yyyy zum akutellen Datum in Tagen
     * @param dateString dd.MM.yyyy String
     * @return Differenz in Tagen
     */
    public long getDateDiff(String dateString){
        LocalDate current = LocalDate.now();
        return DAYS.between(getLocalDateFromString(dateString), current);
    }

    /**
     * Gibt Remote Namen zurück
     * @return Remote Namen
     */
    public String getRemoteHostName(){
        try {
            return InetAddress.getLocalHost ().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gibt Namen des angemeldeten Users zurück
     * @return Username
     */
    public String getSystemUserName(){
        return System.getProperty("user.name");
    }

    /**
     * Gibt verfügbare Räume eines Schlüsseltyps zurück
     * @param keyID Schlüsseltyp ID
     * @return String aller Räume die mit einem Schlüssel dieser SchlüsselID geöffnet werden können
     */
    public String getAccessibleKeyList(String keyID){
        String rooms = "";
        for(Room room : ksql.getAccessibleRooms(keyID)){
            rooms += room.getID().getValue() + ", ";
        }
        if(rooms.length() >= 3) rooms = rooms.substring(0, rooms.length()-2);
        if(rooms.isEmpty())rooms="keine";
        return rooms;
    }
}
