package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    public String status;

    @SerializedName("basic")
    public Basic basic_data;

    public Now now;

    @SerializedName("daily_forecast")
    public List<Forecast> forecast;

    public List<Hourly> hourly;

    public Lifestyle lifestyle;

    @SerializedName("update")
    public Update update_time;

    public class Update{
        @SerializedName("loc")
        public String local_time; //当前更新时间

    }
}
