package de.schonas.keymanagement.main;

public class Room {

    private String id;
    private String department;

    public Room(String id, String department){
        this.id = id;
        this.department = department;
    }

    public String getID() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

}
