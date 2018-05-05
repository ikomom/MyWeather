package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Hourly {
    public String cond_code;

    public String cond_txt;

    @SerializedName("time")
    public String time_now;

    @SerializedName("tmp")
    public String temperature;
}
