package com.amin.baselib.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 类名称: MyWebViewActivity
 * 类描述: WebView加载H5页面
 * 创建人: 陈书东
 * 创建时间: 2018/8/8 08:08
 */
public class MyWebViewActivity extends CustomWebViewActivity {

    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_URL = "KEY_URL";
    public static final String TYPE = "TYPE";

    private String title;
    private String url;
    private int type = 0;

    public static void startActivity(Context context, String title, String url,int type) {
        Intent intent = new Intent(context, MyWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_URL, url);
        bundle.putInt(TYPE, type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString(KEY_TITLE);
            url = bundle.getString(KEY_URL);
            type = bundle.getInt(TYPE);
        }

        if(type == 1){

            toolbar.setVisibility(View.GONE);

        }

        setTitle(title);
        initDialog(title);
        webView.loadUrl(url);

        //测试加载失败点击重新加载逻辑
//        loadingFailed();
    }

    @Override
    protected void againLoad() {
        super.againLoad();
        webView.loadUrl(url);
    }
}