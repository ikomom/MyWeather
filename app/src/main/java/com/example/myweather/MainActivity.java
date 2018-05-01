package com.example.myweather;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.myweather.gson.Forecast;
import com.example.myweather.gson.Hourly;
import com.example.myweather.gson.Weather;
import com.example.myweather.hourList.Hour;
import com.example.myweather.hourList.HourAdapter;
import com.example.myweather.util.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    //titleBar
    private TextView titleCity;

    //hourly_forecast
    private TextView hourTime;
    private TextView hourText;
    private TextView hourDegree;
    private RecyclerView hourRecycler;
    private HourAdapter hourAdapter;
    private List<Hour> hourList = new ArrayList<>();

    //weather_forecast
    private LinearLayout forecastLayout;

    //weather_now
    private TextView degreeText;
    private TextView weatherInfo;
    private RelativeLayout nowLayout;

    //weather_lifestyle
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

    //
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

    //update
    private TextView update_time;

    //下拉刷新
    public SwipeRefreshLayout swipeRefresh;

    //LBS定位
    public LocationClient mlocationClient;
    public static String currentPosition = "";

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        titleCity = (TextView) findViewById(R.id.title_city);

        //forecast
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);

        //设置hourly
        hourDegree = (TextView) findViewById(R.id.hour_degree);
        hourText = (TextView) findViewById(R.id.hour_text);
        hourTime = (TextView) findViewById(R.id.hout_time);

        hourRecycler = (RecyclerView) findViewById(R.id.weather_hourly);
        hourAdapter = new HourAdapter(hourList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourRecycler.setLayoutManager(linearLayoutManager);
        hourRecycler.setAdapter(hourAdapter);

        //weather_now
        nowLayout = (RelativeLayout) findViewById(R.id.weather_now_layout);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfo = (TextView) findViewById(R.id.weather_info_text);
        update_time = (TextView) findViewById(R.id.update_time_text);

        //weather_lifestyle
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        uvText = (TextView) findViewById(R.id.uv_text);
        clothesText = (TextView) findViewById(R.id.clothes_text);
        coldText = (TextView) findViewById(R.id.cold_text);

        comfortBtn = (Button) findViewById(R.id.comfort_button);
        carWashBtn = (Button) findViewById(R.id.car_wash_button);
        sportBtn = (Button) findViewById(R.id.sport_button);
        uvBtn = (Button) findViewById(R.id.uv_button);
        clothesBtn = (Button) findViewById(R.id.clothes_button);
        coldBtn = (Button) findViewById(R.id.cold_button);

        //刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        //LBS定位
        mlocationClient = new LocationClient(getApplicationContext());
        mlocationClient.registerLocationListener(new MyLocationListener());//监听定位位置
        List<String> permissionList = new ArrayList<>();//用List来管理权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }


    }

    @Override
    public void initData() {
        String location = getIntent().getStringExtra("location");
        if (!TextUtils.isEmpty(location)) {
            requestWeather(location);
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String weatherString = prefs.getString("weatherResponse", null);        // weather 保存API 返回的字符串

            if (weatherString != null){
                // 有缓存时直接解析天气数据
                Weather weather = Utility.handleWeatherResponse(weatherString);
                showWeatherInfo(weather);
            //    coordinatorLayout.setVisibility(View.VISIBLE);
            }else {
                // 无缓存时向服务器查询数据
                if (getNetworkInfo() != null && getNetworkInfo().isAvailable()){
                    // 查询完之后显示 coordinatorLayout.setVisibility(View.VISIBLE);
                    LocationClientOption option = new LocationClientOption();
                    option.setIsNeedAddress(true);
                    mlocationClient.setLocOption(option);
                    mlocationClient.start();
                }else{
                    showDialog();
                }

            }
//            Intent intent = new Intent(MainActivity.this, AutoUpdateService.class);
//            startService(intent);
        }
    }

    @Override
    public void initListener() {
        //lifestyle
        comfortBtn.setOnClickListener(this);
        carWashBtn.setOnClickListener(this);
        sportBtn.setOnClickListener(this);
        uvBtn.setOnClickListener(this);
        clothesBtn.setOnClickListener(this);
        coldBtn.setOnClickListener(this);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getNetworkInfo() == null) {
                    Snackbar.make(swipeRefresh, "当前无网络，无法刷新 QAQ ", Snackbar.LENGTH_LONG).setAction("去设置网络", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();
                    swipeRefresh.setRefreshing(false);
                } else {
                    showAnimationAlpha(swipeRefresh);
                }
            }
        });
    }

    /**
     * 画渐变动画
     * @param view
     */
    private void showAnimationAlpha(final View view) {
        Animation alpha = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_before);
        view.startAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String cityName = prefs.getString("cityName", null);
                //   requestWeather(cityName);
                Animation alpha = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_after);
                view.startAnimation(alpha);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 根据城市地点请求城市天气信息
     */
    public void requestWeather(final String location) {

        String address = "https://free-api.heweather.com/s6/weather?location=" + location + "&key=15d020fee1b84aad93d5c708005a417d";
        //发送请求
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showMessage(MainActivity.this, "获取天气信息1失败");
//                    }
//                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                            editor.putString("weatherResponse", responseText);
                            editor.putString("location", location);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            ToastUtil.showMessage(MainActivity.this, "获取天气信息2失败");
                        }
                    }
                });

            }
        });
        swipeRefresh.setRefreshing(false);
    }

    /**
     * 显示天气信息
     */

    private void showWeatherInfo(Weather weather) {
        String location = weather.basic_data.location;
        String degree = weather.now.temperature;
        String weatherInfo_show = weather.now.cond_txt;
        String updateTime = weather.update_time.local_time;

        //weather_now
        titleCity.setText(location);
        degreeText.setText(degree);
        weatherInfo.setText(weatherInfo_show);
        update_time.setText("数据更新时间"+updateTime.split(" ")[1]);

        //weather_forecast
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecast){
            //添加未来天气
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.weather_forecast_item, forecastLayout, false);
            TextView dateText = (TextView)view.findViewById(R.id.data_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxMinText = (TextView)view.findViewById(R.id.max_min_text);
            ImageView weatherPic = (ImageView)view.findViewById(R.id.weather_pic);

            //获取资源id,并且设置图片
            String weatherCode = "weather_"+forecast.cond_code_d;
            int resId = getResources().getIdentifier(weatherCode, "drawable", this.getPackageName());
            if (resId != 0){
                weatherPic.setImageResource(resId);
            }

            dateText.setText(Time.parseTime(forecast.date));
            infoText.setText(forecast.cond_txt_d);
            maxMinText.setText(forecast.temperature_max + " ～ " + forecast.temperature_min);
            forecastLayout.addView(view);
        }

        //hour
        hourList.clear();
        for (Hourly hourly:weather.hourly){
            Hour hour = new Hour();
            hour.setDegree(hourly.temperature + "°" );
            hour.setText(hourly.cond_text);
            hour.setTime(hourly.time_now.split(" ")[1]);
            hourList.add(hour);
        }

        hourAdapter.notifyDataSetChanged();
    }

    /**
     * 权限申请处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            // 如果存在某个权限没有处理
                          //  finish();
                        }
                    }
                } else {
                    // 发生未知错误
                    ToastUtil.showMessage(getApplicationContext(), "权限申请出现位置错误");
                }
                break;
            default:
        }
    }

    /**
     * 用来自动定位,显示第一次的天气信息
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            currentPosition = bdLocation.getCity();
            if (currentPosition != null) {
                requestWeather(currentPosition);
                ToastUtil.showMessage(getApplicationContext(), currentPosition + " 定位成功");
            } else {
                ToastUtil.showMessage(getApplicationContext(), "没有获取到定位权限，请打开定位权限后再打开此应用");
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public void showDialog(){
        AlertDialog.Builder alertDialog  = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setMessage("当前无网络,请先打开网络");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                TaskKiller.dropAllAcitivty();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationClient.stop();
    }
}
