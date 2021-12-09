package com.amin.baselib.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amin.baselib.BaseSwitchUtil;
import com.amin.baselib.R;
import com.amin.baselib.app.MyActivity;
import com.amin.baselib.utils.BaseCommonUtils;


/**
 * Created by Administrator on 2016/3/11.
 */
public class UserAgreementLandscapeBaseActivity extends MyActivity implements View.OnClickListener {

    private TextView tv_privacy,tv_explain,tv_refuse,tv_confirm;

    public static SharedPreferences Preferences;

    private String mPrivacyUrl;
    private String mAgreementUrl;
    private String mShowText;
    private boolean mJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_agreement_landspace_base);

        mPrivacyUrl = getIntent().getStringExtra("privacy");
        mAgreementUrl = getIntent().getStringExtra("agreement");
        mShowText = getIntent().getStringExtra("showText");
        mJump = getIntent().getBooleanExtra("jump",true);

        this.setFinishOnTouchOutside(false);

        if(Preferences == null){

            Preferences = this.getSharedPreferences(BaseCommonUtils.getCurrentProcessName(this), Context.MODE_PRIVATE);

        }

        tv_privacy = findViewById(R.id.tv_privacy);
        tv_explain = findViewById(R.id.tv_explain);
        tv_explain.setText(mShowText);
        tv_explain.setMovementMethod(ScrollingMovementMethod.getInstance());
        (tv_refuse = findViewById(R.id.tv_refuse)).setOnClickListener(this);
        (tv_confirm = findViewById(R.id.tv_confirm)).setOnClickListener(this);

//        String str = new String("请你务必审慎阅读、充分理解“服务协议和隐私政策”各条款，包括但不限于：" +
//                "读取本地存储、使用网络。你可以在手机“设置”中查看、变更、删除存储的信息并变更授权。" +
//                "请阅读《服务协议和隐私政策》了解详细信息。如你同意，请点击“同意”开始使用。");
        String str = new String("亲爱的用户，为了更好地保护您的权益，同时遵守相关监管要求，" +
                "特向您提供《用户协议》及《隐私政策》，并说明如下：");

        int index1 = str.indexOf("《用户协议》");
        int index2 = str.indexOf("《隐私政策》");

        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(str);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                startActivity(new Intent(UserAgreementLandscapeBaseActivity.this,WebViewNoHideLandscapeBaseActivity.class)
                        .putExtra("url",mAgreementUrl)
                        .putExtra("title", "用户协议")
                );

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.parseColor("#2488ff"));
                ds.setUnderlineText(false);
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                startActivity(new Intent(UserAgreementLandscapeBaseActivity.this,WebViewNoHideLandscapeBaseActivity.class)
                        .putExtra("url", mPrivacyUrl)
                        .putExtra("title", "隐私政策")
                );

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.parseColor("#2488ff"));
                ds.setUnderlineText(false);
            }
        };

        spannableBuilder.setSpan(clickableSpan1,index1,index1+6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableBuilder.setSpan(clickableSpan2,index2,index2+6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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

            if(!mJump){

                BaseSwitchUtil.toFirst();

            }

            finish();

        }
    }

}
