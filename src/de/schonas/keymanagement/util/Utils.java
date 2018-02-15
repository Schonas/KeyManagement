package de.schonas.keymanagement.util;

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
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static de.schonas.keymanagement.main.MainPage.openStages;

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
     *
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
     * Formt LocalDate in Date um
     * @param localDate
     * @return sql.Date
     */
    public Date asDate(LocalDate localDate) {
        return Date.valueOf(localDate);
    }

    /**
     * Schaut ob ein Datum in der Vergangenheit oder am jetzigen Tag ist
     * @param date
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
    public void reloadTable(TableView tableView, TableColumn id, TableColumn owner, TableColumn expDate, TextField searchField){
        TableData tb = new TableData(tableView, id, owner, expDate, searchField);
        tb.load();
        tableView.getSelectionModel().select(0);
    }

    /**
     * Rechnet die Zeit zwischen 2 sql.Date aus
     * @param date Datum was mit dem jetzigen verglichen werden soll
     * @return Differenz der Daten
     */
    public static long getDateDiff(Date date) {
        Date current = new Date(Calendar.getInstance().getTimeInMillis());
        return TimeUnit.DAYS.convert((current.getTime() - date.getTime()), TimeUnit.MILLISECONDS);
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
     * Druckt etwas aus
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

    public void print(final Node node, Stage stage, String name) {

    }

    /**
     * Schließt alle Fenster
     */
    public void closeEverything(){
        for(Stage stage : openStages){
            stage.close();
        }
    }
}
