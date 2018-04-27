package com.example.myweather.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast mToast;
    public static void showMessage(Context context, String msg){
        if (mToast == null){
            mToast = Toast.makeText(context.getApplicationContext(), msg ,Toast.LENGTH_LONG);
        }else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
