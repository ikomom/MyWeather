package com.example.myweather.util;

import com.example.myweather.gson.Weather;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

public class Utility {
    /**
     *
     * 处理得到的 weather 数据，转化为 weather 对象
     */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6"); //根据版本号改变
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static City handleCityResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String cityContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(cityContent, City.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
