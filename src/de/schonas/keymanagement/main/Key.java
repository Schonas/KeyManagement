package de.schonas.keymanagement.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Key {

    private final SimpleStringProperty ID;
    private final SimpleStringProperty Owner;
    private final SimpleStringProperty expDate;

    public Key(String ID, String owner, String expDate) {
        this.ID = new SimpleStringProperty(ID);
        this.Owner = new SimpleStringProperty(owner);
        this.expDate = new SimpleStringProperty(expDate);
    }

    public String getID() {
        return ID.get();
    }

    public String getOwner() {
        return Owner.get();
    }


    public String getExpireDate() {
        return expDate.get();
    }

    public StringProperty idProperty(){
        return ID;
    }

    public StringProperty ownerProperty(){
        return Owner;
    }

    public StringProperty expDateProperty(){
        return expDate;
    }

}