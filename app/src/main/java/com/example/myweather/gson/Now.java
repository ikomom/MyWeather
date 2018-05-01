package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    public String cond_txt;

    @SerializedName("tmp")
    public String temperature;
}
