package com.example.myweather.db;

import org.litepal.crud.DataSupport;

public class CityRecond extends DataSupport {
    private int id;
    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
