package com.amin.baselib.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.amin.baselib.R;


/**
 * Created 2017/10/21.
 */
public class WebViewForBaseSwitchActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;

    private String title = "", url = "";

    private LinearLayout lin_error;

    private String downloadUrl;

    private ProgressDialog progressDialog;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview_for_h5);

        type = getIntent().getIntExtra("type",0);

        progressDialog = new ProgressDialog(this);

        url = getIntent().getStringExtra("url");

        if (!url.startsWith("http")) {

            url = "https://" + url;
        }

        webView = (WebView) findViewById(R.id.web_view);

        (lin_error = (LinearLayout) findViewById(R.id.lin_error)).setOnClickListener(this);

        setUpView();

        //加载需要显示的网页
        webView.loadUrl(url);

    }


    private void setUpView() {
        if (url.endsWith(".html")) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);                       //可执行js
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);   //设置页面默认缩放密度
        webSettings.setDefaultTextEncodingName("UTF-8");              //设置默认的文本编码名称，以便在解码html页面时使用
        webSettings.setAllowContentAccess(true);                      //启动或禁用WebView内的内容URL访问
        webSettings.setAppCacheEnabled(false);                        //设置是否应该启用应用程序缓存api
        webSettings.setBuiltInZoomControls(false);                    //设置WebView是否应该使用其内置的缩放机制
        webSettings.setUseWideViewPort(true);                         //设置WebView是否应该支持viewport
        webSettings.setLoadWithOverviewMode(true);                    //不管WebView是否在概述模式中载入页面，将内容放大适合屏幕宽度
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);          //重写缓存的使用方式
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);   //告知js自动打开窗口
        webSettings.setLoadsImagesAutomatically(true);                //设置WebView是否应该载入图像资源
        webSettings.setAllowFileAccess(true);                         //启用或禁用WebView内的文件访问
        webSettings.setDomStorageEnabled(true);                       //设置是否启用了DOM存储API,默认为false
        webView.setWebViewClient(webClient);
        webView.setWebChromeClient(new WebChromeClient());

    }

    /***
     * 设置Web视图的方法
     */
    WebViewClient webClient = new WebViewClient() {//处理网页加载失败时

        private String startUrl;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            startUrl = url;

            progressDialog.setTitle("");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //6.0以上执行

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //6.0以下执行

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            progressDialog.dismiss();

        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("weixin://wap/pay?")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;

            } else if (url.startsWith("alipays:") || url.startsWith("alipay")) {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                } catch (Exception e) {
                    new AlertDialog.Builder(WebViewForBaseSwitchActivity.this)
                            .setMessage("未检测到支付宝客户端，请安装后重试。")
                            .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                    startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                                }
                            }).setNegativeButton("取消", null).show();
                }
                return true;
            } else if (url.startsWith("http:") || url.startsWith("https:")) {

                if(startUrl!=null&&startUrl.equals(url)) {
                    view.loadUrl(url);
                }else {

                    //交给系统处理
                    return super.shouldOverrideUrlLoading(view, url);

                }
                return true;

            } else {

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;

                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;
                }


            }

        }


    };

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.lin_error) {
            webView.reload();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(webView.canGoBack()) {

                webView.goBack();

                return true;

            }else {

                if(type == 3) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(WebViewForBaseSwitchActivity.this)
                            .setTitle("")
                            .setMessage("确定退出应用？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    finish();
                                    //参数用作状态码；根据惯例，非 0 的状态码表示异常终止。
                                    System.exit(0);

                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();

                                }
                            });

                    builder.create().show();

                    return false;

                }else {

                    finish();

                }

            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
