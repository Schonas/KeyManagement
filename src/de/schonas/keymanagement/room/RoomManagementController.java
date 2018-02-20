package de.schonas.keymanagement.room;

import com.sun.org.apache.xpath.internal.operations.Bool;
import de.schonas.keymanagement.main.Key;
import de.schonas.keymanagement.main.Room;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.controlsfx.control.CheckModel;
import org.controlsfx.control.CheckTreeView;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static de.schonas.keymanagement.main.MainPage.currentKey;
import static de.schonas.keymanagement.main.MainPage.ksql;

public class RoomManagementController {

    private ObservableList<Room> masterData = FXCollections.observableArrayList();

    @FXML
    private TextField searchKeyField;

    @FXML
    private TableView<Room> roomTableView;

    @FXML
    private TableColumn<Room, Boolean> checkCol;

    @FXML
    private TableColumn<Room, String> roomCol, departmentCol;

    @FXML
    protected void initialize() {

        checkCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        roomCol.setCellValueFactory(cellData -> cellData.getValue().getID());
        departmentCol.setCellValueFactory(cellData -> cellData.getValue().getDepartment());

        ResultSet rs = ksql.getRooms();
        try {
            Room r;
            while (rs.next()) {
                r = new Room(rs.getString("id"), rs.getString("name"));
                masterData.add(r);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        for(Room r : ksql.getAccessibleRooms("OK1927")){
            System.out.println(r.getID());
        }
        roomTableView.setItems(masterData);

        AutoCompletionBinding ts = TextFields.bindAutoCompletion(searchKeyField, ksql.getKeyTypes());
        ts.setMaxWidth(164);
        ts.setVisibleRowCount(8);
        roomTableView.setMinSize(320,420);
    }

    /**
     * Holt Werte aus checkTreeView und trägt sich in Tabelle ein
     */
    //TODO: Key ID aus searchKeyField bekommen und werte in DB mit Werte aus roomsTreeView überschreiben
    @FXML
    public void onSaveRoomClick(){
        roomTableView.getItems().get(0).setStatus(true);
        roomTableView.getItems().get(5).setStatus(true);
    }

    /**
     * Setzt Checkboxen auf entsprechenden Wert
     */
    @FXML
    public void onSelectKeyTypeButtonClick(){

        List<Room> accessibleRooms = ksql.getAccessibleRooms(searchKeyField.getText());
        for(Room r : accessibleRooms){
            System.out.println(r.getID());
            System.out.println(r.getDepartment());
        }
        System.out.println(roomTableView.getItems().size());

        for(int i=0;i<= roomTableView.getItems().size()-1;i++){
            System.out.println(i);
            Room room = roomTableView.getItems().get(i);
            System.out.println(room.getID());
            System.out.println(room.getDepartment());
            if(accessibleRooms.contains(room)){
                System.out.println("yoooooooooooooooo");
                roomTableView.getItems().get(i).setStatus(true);
            }
        }
    }
}
