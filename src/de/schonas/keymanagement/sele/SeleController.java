package de.schonas.keymanagement.sele;

import de.schonas.keymanagement.Room;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import static de.schonas.keymanagement.main.MainPage.ksql;

public class SeleController {

    @FXML
    private TextField seleSearchField;

    @FXML
    private TextArea aRooms;

    @FXML
    protected void initialize() {

        AutoCompletionBinding ts = TextFields.bindAutoCompletion(seleSearchField, ksql.getOwners());
        ts.setMaxWidth(164);
        ts.setVisibleRowCount(8);
    }

    @FXML
    public void onSeleSearch(){

    }
    @FXML
    public void onSearchRoomsClick(){
        StringBuilder list= new StringBuilder();
        for(Room room : ksql.getAccessibleRoomsByOwner(seleSearchField.getText())){
            list.append(room.getID().getValue()).append(" | ").append(room.getDepartment().getValue()).append("\n");
        }
        aRooms.setText(list.toString());
    }
}
