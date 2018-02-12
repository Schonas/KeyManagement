package de.schonas.keymanagement.util;

import de.schonas.keymanagement.main.DataSource;
import de.schonas.keymanagement.main.Key;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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
    public static Date asDate(LocalDate localDate) {
        return Date.valueOf(localDate);
    }

    /**
     * Schaut ob ein Datum in der Vergangenheit oder am jetzigen Tag ist
     * @param date
     * @return in vergangenheit oder jetzt?
     */
    public boolean isInPast(Date date){
        Date current = new Date(Calendar.getInstance().getTimeInMillis());
        if(date.before(current)){
            return true;
        }else if(date.equals(current)){
            return true;
        } else {
            return false;
        }
    }

    public void reloadTable(TableView tableView, TableColumn uid, TableColumn owner, TableColumn expDate){

        //setting up the columns
        PropertyValueFactory<Key, String> uidProperty = new PropertyValueFactory<>("UniqueID");
        PropertyValueFactory<Key, String> ownerProperty = new PropertyValueFactory<>("Owner");
        PropertyValueFactory<Key, String> expDateProperty = new PropertyValueFactory<>("ExpireDate");

        //setting up the main data source
        uid.setCellValueFactory(uidProperty);
        owner.setCellValueFactory(ownerProperty);
        expDate.setCellValueFactory(expDateProperty);

        DataSource data = new DataSource();
        ObservableList<Key> tableItems = data.getData();
        tableView.setItems(tableItems);

    }


}
