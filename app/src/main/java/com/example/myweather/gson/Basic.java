package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    public String location;

    @SerializedName("cid")
    public String weatherId;


}
