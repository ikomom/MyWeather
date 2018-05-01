package com.example.myweather;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.example.myweather.util.BaseActivity;

public class SettingActivity extends BaseActivity {
    private Button chooseArea;
    private Button aboutApplication;
    private Button autoUpdateTime;

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("设置");
        }

        chooseArea = (Button)findViewById(R.id.choose_area);
        autoUpdateTime = (Button)findViewById(R.id.auto_update_time);
        aboutApplication = (Button)findViewById(R.id.about_app);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        chooseArea.setOnClickListener(this);
        autoUpdateTime.setOnClickListener(this);
        aboutApplication.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.choose_area:
                actionStart(ChooseAreaActivity.class);
                break;
            case R.id.auto_update_time:
                actionStart(AutoUpdateTimeActivity.class);
                break;
            case R.id.about_app:
                actionStart(AboutApplicationActivity.class);
                break;
        }
    }

    public void actionStart(Class<?> c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
