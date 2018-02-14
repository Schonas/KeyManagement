package de.schonas.keymanagement.util;

import de.schonas.keymanagement.main.TableData;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Utils {

    public Timestamp convertStringToTimestamp(String s){

        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = (Date) formatter.parse(s);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());

            return timeStampDate;
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
    }

    /**
     * Sendet Text in ein Textfeld und lässt Text nach 5 sek verschwinden
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
     * @return
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

    public void reloadTable(TableView tableView, TableColumn uid, TableColumn owner, TableColumn expDate, TextField searchField){
        TableData tb = new TableData(tableView, uid, owner, expDate, searchField);
        tb.load();
    }

    public static long getDateDiff(Date date) {
        Date current = new Date(Calendar.getInstance().getTimeInMillis());
        return TimeUnit.DAYS.convert((current.getTime() - date.getTime()), TimeUnit.MILLISECONDS);
    }


}
