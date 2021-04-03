package com.amin.baselib.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amin.baselib.R;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

/**
 * Created 2017/10/21.
 */
public class WebViewNoHideBaseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title_name;

    private LinearLayout back;

    private WebView webView;

    private String title = "", flag = "", url = "";

    private WebSettings mWebSettings;

    private LinearLayout lin_error;

    private boolean isSuccess = false;

    private boolean isError = false;

    private ImageView iv_reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview_nohide_base);

        title = getIntent().getStringExtra("title");

        url = getIntent().getStringExtra("url");

        if(!url.startsWith("http")&&!url.startsWith("file")){

            url = "http://"+url;
        }

        title_name = (TextView) findViewById(R.id.tv_title);

        title_name.setText(title);

        (back = (LinearLayout) findViewById(R.id.back)).setOnClickListener(this);

        webView = (WebView) findViewById(R.id.web_view);

        lin_error = (LinearLayout) findViewById(R.id.lin_error);

        (iv_reload = (ImageView) findViewById(R.id.imageView2)).setOnClickListener(this);

        setUpView();

    }

    private void setUpView() {
        //加载需要显示的网页
        webView.loadUrl(url);
        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);//水平滚动条不显示
        webView.setVerticalScrollBarEnabled(false); //垂直滚动条不显示
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);  //允许加载javascript
        mWebSettings.setSupportZoom(true);     //允许缩放
        mWebSettings.setBuiltInZoomControls(true); //原网页基础上缩放
        mWebSettings.setUseWideViewPort(true);   //任意比例缩放
        mWebSettings.setCacheMode(LOAD_NO_CACHE);
        webView.setWebViewClient(webClient); //设置Web视图
    }

    /***
     * 设置Web视图的方法
     */
    WebViewClient webClient = new WebViewClient() {//处理网页加载失败时

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            isError = true;
            isSuccess = false;
            //6.0以上执行
            webView.setVisibility(View.GONE);
            lin_error.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            isError = true;
            isSuccess = false;
            //6.0以下执行
            webView.setVisibility(View.GONE);
            lin_error.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!isError) {
                isSuccess = true;
                //回调成功后的相关操作
                lin_error.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            } else {
                isError = false;
                lin_error.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
//
            return true;

        }



    };

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.back) {
            finish();
        } else if (id == R.id.imageView2) {
            webView.reload();
        }

    }

}
