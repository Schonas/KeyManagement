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

    public void setUniqueID(String uniqueid) {
        this.UniqueID.set(uniqueid);
    }


    public String getOwner() {
        return Owner.get();
    }

    public void setOwner(String owner) {
        this.Owner.set(owner);
    }


    public String getExpireDate() {
        return expDate.get();
    }
    public void setExpireDate(String expireDate) {
        this.expDate.set(expireDate);
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