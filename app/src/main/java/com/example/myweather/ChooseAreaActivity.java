package com.example.myweather;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.myweather.db.CityRecond;
import com.example.myweather.util.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends BaseActivity {
    private EditText searchText;
    private Button searchButton;

    private ListView listView;
    private ListView listViewRecond;
    private List<String> cityList = new ArrayList<>();
    private List<String> recondList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> recondAdapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_chooase_area);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("选择地区");
        }
        //bind
        searchText = (EditText) findViewById(R.id.search_text);
        searchButton = (Button) findViewById(R.id.search_button);
        //城市列表
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getNetworkInfo() != null && getNetworkInfo().isAvailable()) {
                    String location = cityList.get(position);
                    MainActivity.actionStart(ChooseAreaActivity.this, location);
                    finish();
                } else {
                    ToastUtil.showMessage(getApplicationContext(), "当前没有网络");
                }
            }
        });
        //查找记录
        listViewRecond = (ListView) findViewById(R.id.list_view_recond);
        recondAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recondList);
        listViewRecond.setAdapter(recondAdapter);
        listViewRecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getNetworkInfo() != null && getNetworkInfo().isAvailable()) {
                    String cityName = recondList.get(position);
                    MainActivity.actionStart(ChooseAreaActivity.this, cityName);
                    finish();
                } else {
                    ToastUtil.showMessage(ChooseAreaActivity.this, "当前没有网络");
                }
            }
        });

        //读取之前查询的数据
        showRecond();
    }

    @Override
    public void initData() {
        LitePal.getDatabase();
    }

    @Override
    public void initListener() {
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_button:
                showSearchResult();
                break;
            default:
                break;
        }
    }

    /**
     * 显示查询的城市
     */
    public void showSearchResult() {
        String location = searchText.getText().toString();
        if (!TextUtils.isEmpty(location)) {
            // 如果不为空，则进行查询
            if (getNetworkInfo() != null && getNetworkInfo().isAvailable()) {
                String address = "https://search.heweather.com/find?location=" + location + "&key=15d020fee1b84aad93d5c708005a417d";
                requestData(address);
            } else {
                ToastUtil.showMessage(ChooseAreaActivity.this, "当前网络无连接");
            }
        } else {
            ToastUtil.showMessage(ChooseAreaActivity.this, "请输入城市名称");
        }

    }


    /**
     * 查询数据
     * @param address
     */
    public void requestData(String address){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showMessage(ChooseAreaActivity.this,"请求数据失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final City city = Utility.handleCityResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCity(city);
                    }
                });
            }
        });
    }

    /**
     * 查询城市并且储存进数据库
     * @param city
     */
    private void showCity(City city){
        cityList.clear();
        if ("ok".equals(city.status) && city != null){
            for (City.Basic basic : city.basic){
                cityList.add(basic.location);
                saveSearchRecond(basic.location);//储存进数据库
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);

        }else{
            ToastUtil.showMessage(ChooseAreaActivity.this,"未找到该城市");
        }


    }


    /**
     * 保存城市名称 -> 数据库
     */
    public void saveSearchRecond(String location){
        CityRecond cityRecond = new CityRecond();
        cityRecond.setLocation(location);
        cityRecond.save();
    }

    /**
     * 显示数据库中保存的信息
     */
    public void showRecond(){
        recondList.clear();
        List<CityRecond> list = DataSupport.select("location").find(CityRecond.class);
        for (CityRecond recond:list){
            recondList.add(recond.getLocation());
        }
        recondAdapter.notifyDataSetChanged();
        listViewRecond.setSelection(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
