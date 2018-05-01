package com.example.myweather.util;


import java.util.List;

public class City {
    public List<Basic> basic;

    public String status;

    public class Basic{
        public String location; //地区／城市名称
        public String cnty; //该地区／城市所属国家名称
        public String lat; //纬度
        public String lon; //经度

    }
}
