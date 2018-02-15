package de.schonas.keymanagement.room;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import org.controlsfx.control.CheckTreeView;

public class RoomManagementController {

    @FXML
    private Pane roomViewPane;

    @FXML
    private TextField searchKeyField;

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
        final CheckTreeView<String> roomsTreeView = new CheckTreeView<>(root);

        // and listen to the relevant events (e.g. when the checked items change).
        roomsTreeView.getCheckModel().getCheckedItems().addListener((ListChangeListener<TreeItem<String>>) c ->
                System.out.println(roomsTreeView.getCheckModel().getCheckedItems()));

        //System.out.println(ksql.getKeyTypes());
        ///AutoCompletionBinding ts = TextFields.bindAutoCompletion(searchKeyField, ksql.getKeyTypes());
        //ts.setMaxWidth(164);
        //ts.setVisibleRowCount(8);

        roomViewPane.getChildren().add(roomsTreeView);
        roomsTreeView.setPrefSize(310,410);
    }

}
