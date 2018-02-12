package de.schonas.keymanagement.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

import static de.schonas.keymanagement.main.MainPage.ksql;
import static de.schonas.keymanagement.main.MainPage.u;

public class DataSource {

    private final ObservableList<Key> data = FXCollections.observableArrayList();

    public ObservableList<Key> getData() {
        return data;
    }

    public DataSource() {

        ResultSet rs = ksql.getKeys();
        try {
            while(rs.next()){
                Key k = new Key(rs.getString("uid"),rs.getString("owner"), rs.getString("expire_date"));
                data.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}