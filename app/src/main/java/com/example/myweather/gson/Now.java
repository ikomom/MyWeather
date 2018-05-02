package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    public String cond_txt;

    @SerializedName("tmp")
    public String temperature;

    public String wind_dir;//风向

    public String wind_sc;//风力

    public String wind_spd;//风速 km/h

    public String hum;//相对湿度 %

    public String pcpn;//降水量 ms

    public String pres;//大气压强 Pa


}
