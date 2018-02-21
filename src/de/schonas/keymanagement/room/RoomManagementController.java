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

        roomTableView.setItems(masterData);

        AutoCompletionBinding ts = TextFields.bindAutoCompletion(searchKeyField, ksql.getKeyTypes());
        ts.setMaxWidth(164);
        ts.setVisibleRowCount(8);
        roomTableView.setMinSize(340,410);
    }

    /**
     * Holt Werte aus checkTreeView und trägt sich in Tabelle ein
     */
    //TODO: werte in DB mit Werte aus TableView überschreiben
    @FXML
    public void onSaveRoomClick(){
        initialize();
        /*String keyType = searchKeyField.getText();
        for(int i=0;i<= roomTableView.getItems().size()-1;i++){
            Room room = roomTableView.getItems().get(i);
            if(room.getStatus().isSelected()){
                ksql.setNewRoomIDs(keyType, room.getID().getValue());
            }
        }*/
    }

    /**
     * Setzt Checkboxen auf entsprechenden Wert
     */
    @FXML
    public void onSelectKeyTypeButtonClick(){

        List<String> accessibleRooms = ksql.getAccessibleRoomIDs(searchKeyField.getText());
        for(int i=0;i<= roomTableView.getItems().size()-1;i++){
            Room room = roomTableView.getItems().get(i);
            if(accessibleRooms.contains(room.getID().getValue())){
                roomTableView.getItems().get(i).setStatus(true);
            }
        }
    }
}
