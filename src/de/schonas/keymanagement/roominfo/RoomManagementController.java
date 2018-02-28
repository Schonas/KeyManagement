package de.schonas.keymanagement.roominfo;

import de.schonas.keymanagement.main.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
    private ProgressIndicator progressIndicator;

    @FXML
    protected void initialize() {

        checkCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        roomCol.setCellValueFactory(cellData -> cellData.getValue().getID());
        departmentCol.setCellValueFactory(cellData -> cellData.getValue().getDepartment());
        GlyphFont fontAwesome= GlyphFontRegistry.font("FontAwesome");
        Glyph checkIcon = fontAwesome.create(FontAwesome.Glyph.CHECK_SQUARE);
        checkIcon.setFontSize(14);
        checkCol.setGraphic(checkIcon);

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

        AutoCompletionBinding ts = TextFields.bindAutoCompletion(searchKeyField, ksql.getKeyTypes());
        ts.setMaxWidth(164);
        ts.setVisibleRowCount(8);

        roomTableView.prefWidth(360);
        roomTableView.setItems(masterData);
    }

    /**
     * Holt Werte aus checkTreeView und trägt sich in Tabelle ein
     */
    @FXML
    public void onSaveRoomClick(){
        String keyType = searchKeyField.getText();

        new Thread(() -> {
            progressIndicator.setVisible(true);
            for(int i=0;i<= roomTableView.getItems().size()-1;i++){
                Room room = roomTableView.getItems().get(i);
                if(room.getStatus().isSelected()){
                    ksql.insertIfNotExists(room.getID().getValue(), keyType);
                } else if(!room.getStatus().isSelected()){
                    ksql.deleteIfExists(room.getID().getValue(), keyType);
                }
            }
            progressIndicator.setVisible(false);
        }).start();

    }

    /**
     * Setzt Checkboxen auf entsprechenden Wert
     */
    @FXML
    public void onSelectKeyTypeButtonClick(){

        List<String> accessibleRooms = ksql.getAccessibleRoomIDs(searchKeyField.getText());
        clearCheckboxes();
        progressIndicator.setVisible(false);
        for(int i=0;i<= roomTableView.getItems().size()-1;i++){
            Room room = roomTableView.getItems().get(i);
            if(accessibleRooms.contains(room.getID().getValue())){
                roomTableView.getItems().get(i).setStatus(true);
            }
        }
    }

    /**
     * Unchecked alle Checkboxen
     */
    private void clearCheckboxes(){
        for(Room r : roomTableView.getItems()){
            r.setStatus(false);
        }
    }
}
