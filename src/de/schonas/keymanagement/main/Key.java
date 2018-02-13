package de.schonas.keymanagement.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Key {

    private final SimpleStringProperty UniqueID;
    private final SimpleStringProperty Owner;
    private final SimpleStringProperty expDate;

    public Key(String UniqueID, String owner, String expDate) {
        this.UniqueID = new SimpleStringProperty(UniqueID);
        this.Owner = new SimpleStringProperty(owner);
        this.expDate = new SimpleStringProperty(expDate);
    }

    public String getUniqueID() {
        return UniqueID.get();
    }

    public String getOwner() {
        return Owner.get();
    }


    public String getExpireDate() {
        return expDate.get();
    }

    public StringProperty uidProperty(){
        return UniqueID;
    }

    public StringProperty ownerProperty(){
        return Owner;
    }

    public StringProperty expDateProperty(){
        return expDate;
    }

}