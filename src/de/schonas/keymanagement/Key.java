package de.schonas.keymanagement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Key {

    private final SimpleStringProperty UID;
    private final SimpleStringProperty ID;
    private final SimpleStringProperty Owner;
    private final SimpleStringProperty expDate;

    public Key(String uid, String ID, String owner, String expDate) {
        this.ID = new SimpleStringProperty(ID);
        this.Owner = new SimpleStringProperty(owner);
        this.expDate = new SimpleStringProperty(expDate);
        this.UID = new SimpleStringProperty(uid);
    }

    /*
    public Key(String id, String owner){
        this.UID = new SimpleStringProperty(null);
        this.ID = new SimpleStringProperty(id);
        this.Owner = new SimpleStringProperty(owner);
        this.expDate = new SimpleStringProperty(null);
    }
    */

    public Key(String uid, String id){
        this.UID = new SimpleStringProperty(uid);
        this.ID = new SimpleStringProperty(id);
        this.Owner = new SimpleStringProperty(null);
        this.expDate = new SimpleStringProperty(null);
    }

    public Key(String ID){
        this.UID = new SimpleStringProperty(null);
        this.ID = new SimpleStringProperty(ID);
        this.Owner = new SimpleStringProperty(null);
        this.expDate = new SimpleStringProperty(null);
    }

    public String getUID(){
        return  UID.get();
    }

    public String getID() {
        return ID.get();
    }

    public String getOwner() {
        return Owner.get();
    }

    public String getExpDate() {
        return expDate.get();
    }


    public StringProperty uidPropety() {
        return  UID;
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