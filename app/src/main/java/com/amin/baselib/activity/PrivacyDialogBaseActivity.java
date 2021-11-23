package com.amin.baselib.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amin.baselib.R;
import com.amin.baselib.utils.BaseCommonUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by Administrator on 2016/3/11.
 */
public class PrivacyDialogBaseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_privacy,tv_refuse,tv_confirm;

    public static SharedPreferences Preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_privacy_dialog_base);

        this.setFinishOnTouchOutside(false);

        if(Preferences == null){

            Preferences = this.getSharedPreferences(BaseCommonUtils.getCurrentProcessName(this), Context.MODE_PRIVATE);

        }

        tv_privacy = findViewById(R.id.tv_privacy);
        (tv_refuse = findViewById(R.id.tv_refuse)).setOnClickListener(this);
        (tv_confirm = findViewById(R.id.tv_confirm)).setOnClickListener(this);

        String str = new String("请你务必审慎阅读、充分理解“服务协议和隐私政策”各条款，包括但不限于：" +
                "读取本地存储、使用网络。你可以在手机“设置”中查看、变更、删除存储的信息并变更授权。" +
                "请阅读《服务协议和隐私政策》了解详细信息。如你同意，请点击“同意”开始使用。");

        int index = str.indexOf("《服务协议和隐私政策》");

        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(str);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                startActivity(new Intent(PrivacyDialogBaseActivity.this,WebViewNoHideBaseActivity.class)
                        .putExtra("url", "file:///android_asset/useragreementbase.html")
                        .putExtra("title", "服务协议和隐私政策")
                );

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.parseColor("#2488ff"));
                ds.setUnderlineText(false);
            }
        };

        spannableBuilder.setSpan(clickableSpan,index,index+11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_privacy.setMovementMethod(LinkMovementMethod.getInstance());
        tv_privacy.setText(spannableBuilder);

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.tv_refuse) {

            BaseCommonUtils.exitApp(this);

//            System.exit(0);

        } else if (id == R.id.tv_confirm) {

            Preferences.edit().putBoolean("Privacy",false).commit();

            finish();

        }
    }

}
