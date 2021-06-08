package com.amin.baselib.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.amin.baselib.R;
import com.amin.baselib.app.MyActivity;
import com.amin.baselib.dialog.BaseTipDialog;
import com.amin.baselib.utils.BaseTools;


/**
 * Created by Administrator on 2016/4/25.
 */
public class SuggestionActivity extends MyActivity implements android.view.View.OnClickListener {

    private android.widget.TextView title_name;

    private EditText et_suggestion,et_phone;

    private android.widget.TextView tv_sumbit;

    private BaseTipDialog tipDialog;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggestion);

        initView();

    }

    private void initView(){

        (title_name = (android.widget.TextView) findViewById(R.id.tv_title)).setText("意见反馈");

        et_suggestion = (EditText)findViewById(R.id.et_suggestion);

        et_phone = (EditText)findViewById(R.id.et_phone);

        (tv_sumbit = (android.widget.TextView) findViewById(R.id.tv_sumbit)).setOnClickListener(this);

        tipDialog = new BaseTipDialog(this);

    }

    @Override
    public void onClick(android.view.View v) {

        if (v.getId() == R.id.tv_sumbit) {
            if (TextUtils.isEmpty(et_suggestion.getText().toString().trim())) {
                tipDialog.setTip("请输入反馈内容");
                tipDialog.show();

            } else {
                if (!TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                    if (!BaseTools.isMobile(et_phone.getText().toString().trim())) {
                        tipDialog.setTip("手机号格式错误");
                        tipDialog.show();
                    } else {

                        tipDialog.setTip("我们已收到您的宝贵意见，感谢您对本应用的支持。");
                        tipDialog.show();

                    }

                } else {

                    tipDialog.setTip("我们已收到您的宝贵意见，感谢您对本应用的支持。");
                    tipDialog.show();

                }

            }
        }

    }
}
