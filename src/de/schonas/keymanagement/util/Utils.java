package de.schonas.keymanagement.util;

import de.schonas.keymanagement.main.Key;
import de.schonas.keymanagement.main.Room;
import de.schonas.keymanagement.main.TableData;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
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
     * Schaut ob ein Datum in der Vergangenheit oder am jetzigen Tag ist
     * @param date zu vergleichenes Datum
     * @return in vergangenheit oder jetzt?
     */
    public boolean isInPast(Date date){
        Date current = new Date(Calendar.getInstance().getTimeInMillis());

        if(date.before(current) || date.equals(current)){
            return true;
        } else {
            return false;
        }
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
     * Braucht man eher nicht
     * @param date LocalDate
     * @return liefert String aus Local Date
     */
    public String getStringFromLocalDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.GERMANY);
        return date.format(formatter);
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
     * Druckt Node aus
     * @param node
     */
    public void print(Node node) {
        System.out.println("Creating a printer job...");

        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null && job.showPageSetupDialog(node.getScene().getWindow())) {
            System.out.println(job.jobStatusProperty().asString());
            boolean printed = job.printPage(node);
            if (printed) {
                job.endJob();
            } else {
                System.out.println("Printing failed.");
            }
        } else {
            System.out.println("Could not create a printer job.");
        }
    }

    /**
     * Gibt TestString aus und ist kürzer als System.out.println
     * @param test TestString
     */
    public void test(String test){
        System.out.println(test);
    }

    /**
     * Gibt unterstes KeyObject aus einer Tabelle voller Keys zurück
     * @param keyTable Tabelle mit Key Objekten
     * @return letzter Key
     */
    public Key getLastAddedKey(TableView<Key> keyTable){
        return keyTable.getItems().get(keyTable.getItems().size());
    }

    public String getAccessibleKeyList(String keyID){
        String rooms = "";
        for(Room room : ksql.getAccessibleRooms(keyID)){
            rooms += room.getID().getValue() + ", ";
        }
        rooms = rooms.substring(0, rooms.length()-2);
        if(rooms.isEmpty())rooms="keine";
        return rooms;
    }
}
