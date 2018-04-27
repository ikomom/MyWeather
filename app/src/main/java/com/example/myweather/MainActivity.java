package com.example.myweather;

import android.content.pm.PackageManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.example.myweather.hour.Hour;
import com.example.myweather.hour.HourAdapter;
import com.example.myweather.util.BaseActivity;
import com.example.myweather.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ScrollView weatherLayout;
    private CoordinatorLayout mainLayout;
    private TextView titleCity;
    List<String> permissionList  = new ArrayList<>();

    //main中的Toolbar
    private Toolbar toolbar;

    // 以下是 weather_now 的内容
    private TextView degreeText;
    private TextView weatherInfoText;
    private RelativeLayout weaherNowLayout;
    private TextView updateTimeText;
    // 以下是 weather_hour 的内容
    private TextView hourTime;
    private  TextView hourText;
    private TextView hourDegree;
    private List<Hour> hourList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HourAdapter hourAdapter;

    // 以下是 weather_aqi 内容
    private TextView aqiText;
    private TextView pm25Text;
    private TextView coText;
    private TextView o3Text;
    private TextView pm10Text;
    private TextView so2Text;

    // 以下是 weather_forecast 内容
    private LinearLayout forecastLayout;

    // 以下是 weather_suggestion 内容
    private TextView carWashText;
    private TextView sportText;
    private TextView comfortText;
    private TextView uvText;
    private TextView clothesText;
    private TextView coldText;

    private Button carWashBtn;
    private Button sportBtn;
    private Button comfortBtn;
    private Button uvBtn;
    private Button clothesBtn;
    private Button coldBtn;

    private String carWashInfo;
    private String carWashSign;

    private String sportInfo;
    private String sportSign;

    private String comfortInfo;
    private String comfortSign;

    private String uvInfo;
    private String uvSign;

    private String clothesInfo;
    private String clothesSign;

    private String coldInfo;
    private String coldSign;



    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;
    private long triggerAtTimefirst = 0;


    @Override
    public void initView() {
        setContentView(R.layout.activity_main);

        //设置自定义的菜单栏
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
        }

        titleCity = (TextView)findViewById(R.id.title_city); //当前城市名

        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);//main中包裹着主界面
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);//未来天气预报
        mainLayout = (CoordinatorLayout) findViewById(R.id.coor_layout);//main

        // weather_now
        weaherNowLayout = (RelativeLayout)findViewById(R.id.weather_now_layout);
        degreeText = (TextView)findViewById(R.id.degree_text);  //天气状况
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text); //天气温度
        updateTimeText = (TextView)findViewById(R.id.update_time_text); //数据更新时间


        // weather_hour
        hourDegree = (TextView)findViewById(R.id.hour_degree);  //天气温度
        hourText = (TextView)findViewById(R.id.hour_text);      //天气描述
        hourTime = (TextView)findViewById(R.id.hout_time);      //时间

        //初始化适配器
        recyclerView = (RecyclerView)findViewById(R.id.weather_hourly);
        hourAdapter = new HourAdapter(hourList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(hourAdapter);

        // weather_suggestion
        comfortText = (TextView)findViewById(R.id.comfort_text);
        carWashText = (TextView)findViewById(R.id.car_wash_text);
        sportText = (TextView)findViewById(R.id.sport_text);
        uvText = (TextView)findViewById(R.id.uv_text);
        clothesText = (TextView)findViewById(R.id.clothes_text);
        coldText = (TextView)findViewById(R.id.cold_text);
        comfortBtn = (Button)findViewById(R.id.comfort_button);
        carWashBtn = (Button)findViewById(R.id.car_wash_button);
        sportBtn = (Button)findViewById(R.id.sport_button);
        uvBtn = (Button)findViewById(R.id.uv_button);
        clothesBtn = (Button)findViewById(R.id.clothes_button);
        coldBtn = (Button)findViewById(R.id.cold_button);

        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    /**
     * 权限申请处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result:grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            // 如果存在某个权限没有处理
                            finish();
                        }
                    }
                }else{
                    // 发生未知错误
                    ToastUtil.showMessage(getApplicationContext() ,"权限申请出现位置错误");
                }
                break;
            default:
        }
    }


    @Override
    public void onClick(View view) {

    }
}
