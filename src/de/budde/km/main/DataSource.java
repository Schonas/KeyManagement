package de.budde.km.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

import static de.budde.km.main.MainPage.ksql;

public class DataSource {

    private final ObservableList<Key> data = FXCollections.observableArrayList();

    public ObservableList<Key> getData() {
        return data;
    }

    public DataSource() {

        ResultSet rs = ksql.getKeys();
        try {
            while(rs.next()){
                data.add(new Key(rs.getString("uid"),rs.getString("owner"), rs.getString("expire_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}