package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    public String date;

    @SerializedName("tmp_max")
    public String temperature_max;

    @SerializedName("tmp_min")
    public String temperature_min;

    public String cond_code_d;//白天天气状况

    public String cond_txt_d;//白天天气状况描述
}
