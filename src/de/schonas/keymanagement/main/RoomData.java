package de.schonas.keymanagement.main;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

public class RoomData {

    final ObservableList<String> strings = FXCollections.observableArrayList();
    private VBox box;

    public RoomData(VBox box){
        this.box = box;
    }

    public void load(){
        for (int i = 0; i <= 100; i++) {
            strings.add("Room " + i);
        }

        final CheckComboBox<String> checkComboBox = new CheckComboBox<>(strings);

        checkComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c ->
                System.out.println(checkComboBox.getCheckModel().getCheckedIndices()));

        checkComboBox.setPrefWidth(175);

        box.getChildren().add(box.getChildren().size()-1, checkComboBox);

    }
}
