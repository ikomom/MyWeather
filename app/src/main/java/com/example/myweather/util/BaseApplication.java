package com.example.myweather.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;
import org.litepal.LitePal;

import static org.litepal.LitePalApplication.getContext;

public class BaseApplication extends Application {
    private static Context context;


    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePal.initialize(context);
        super.onCreate();
        showTheme();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                TaskKiller.addActivity(activity);
                // 用来对每一个 Activity 进行监听, 包括第三方 Activity
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                showTheme();
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                TaskKiller.dropActivity(activity);
            }
        });
    }


    public void showTheme(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isNight = pref.getBoolean("isNight",false);
        if (isNight){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }
}
