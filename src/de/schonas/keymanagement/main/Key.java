package de.schonas.keymanagement.main;

import javafx.beans.property.SimpleStringProperty;

public class Key {
    private final SimpleStringProperty UniqueID;
    private final SimpleStringProperty Owner;
    private final SimpleStringProperty ExpireDate;


    public Key(String UniqueID, String owner, String expireDate) {
        this.UniqueID = new SimpleStringProperty(UniqueID);
        this.Owner = new SimpleStringProperty(owner);
        this.ExpireDate = new SimpleStringProperty(expireDate);
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
        return ExpireDate.get();
    }
    public void setExpireDate(String expireDate) {
        this.ExpireDate.set(expireDate);
    }
}