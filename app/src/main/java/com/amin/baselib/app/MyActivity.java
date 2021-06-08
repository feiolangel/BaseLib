package com.amin.baselib.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;


import com.amin.baselib.BaseSwitchUtil;

import java.lang.reflect.Field;


/**
 * Created by  on 2017/4/1.
 */

public abstract class MyActivity extends AppCompatActivity {

    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        if(Build.VERSION.SDK_INT >= 24){
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {}
        }

        Log.e(getClass().toString(), "show");

        this.context = this;

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        super.setContentView(layoutResID);

        BaseSwitchUtil.scaleScreenHelper.loadView((ViewGroup) getWindow().getDecorView());


    }

    public void onRightClick(View v){


    }



    @Override
    protected void onDestroy() {

        super.onDestroy();


        System.gc();

    }

    public void onBack(View v){

        finish();

    }
}
