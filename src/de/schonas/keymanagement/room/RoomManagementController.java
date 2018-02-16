package de.schonas.keymanagement.room;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import org.controlsfx.control.CheckModel;
import org.controlsfx.control.CheckTreeView;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import static de.schonas.keymanagement.main.MainPage.ksql;

public class RoomManagementController {

    @FXML
    private Pane roomViewPane;

    @FXML
    private TextField searchKeyField;

    private CheckTreeView<String>  roomsTreeView;

    @FXML
    protected void initialize() {

        CheckBoxTreeItem<String> root = new CheckBoxTreeItem<>("Verwaltung, Recht & Steuern");
        root.setExpanded(true);
        root.getChildren().addAll(
                new CheckBoxTreeItem<>("Raum 404"),
                new CheckBoxTreeItem<>("Raum 405"),
                new CheckBoxTreeItem<>("Raum 403"),
                new CheckBoxTreeItem<>("Raum 402"));

        // Create the CheckTreeView with the data
        roomsTreeView = new CheckTreeView<>(root);

        // and listen to the relevant events (e.g. when the checked items change).
       // roomsTreeView.getCheckModel().getCheckedItems().addListener((ListChangeListener<TreeItem<String>>) c ->
         //       System.out.println(roomsTreeView.getCheckModel().getCheckedItems()));

        AutoCompletionBinding ts = TextFields.bindAutoCompletion(searchKeyField, ksql.getKeyTypes());
        ts.setMaxWidth(164);
        ts.setVisibleRowCount(8);

        roomViewPane.getChildren().add(roomsTreeView);
        roomsTreeView.setPrefSize(310,410);
    }

    /**
     * Holt Werte aus checkTreeView und trägt sich in Tabelle ein
     */
    //TODO: Key ID aus searchKeyField bekommen und werte in DB mit Werte aus roomsTreeView überschreiben
    @FXML
    public void onSaveRoomClick(){
        ObservableList checkedItems = roomsTreeView.getCheckModel().getCheckedItems();
        CheckBoxTreeItem tm;
        for(int i = 0; i <= checkedItems.size()-1; i++){
            if(checkedItems.get(i) instanceof CheckBoxTreeItem) {
                tm = (CheckBoxTreeItem) checkedItems.get(i);
                System.out.println(tm.getValue().toString().substring(5));
            }

        }
    }
}
