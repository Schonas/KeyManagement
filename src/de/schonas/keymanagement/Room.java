package de.schonas.keymanagement;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class Room {

    private SimpleStringProperty id;
    private SimpleStringProperty department;
    private CheckBox status;

    public Room(String id, String department){
        this.id = new SimpleStringProperty(id);
        this.department = new SimpleStringProperty(department);
        this.status = new CheckBox();
    }

    public SimpleStringProperty getID() {
        return id;
    }

    public SimpleStringProperty getDepartment() {
        return department;
    }

    public CheckBox getStatus() {
        return status;
    }

    public void setStatus(CheckBox status){
        this.status = status;
    }

    public void setStatus(boolean selected) {
        status.setSelected(selected);
    }
}
