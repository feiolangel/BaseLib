package com.amin.baselib.webview;


import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ZpWebView extends WebView {

    private ZpWebChromeClient webChromeClient;

    public ZpWebView(Context context) {
        super(context);
        initWebView();
    }

    public ZpWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    public ZpWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView();
    }

    private void initWebView() {
        webChromeClient = new ZpWebChromeClient();
        setWebChromeClient(webChromeClient);
        setWebViewClient(new ZpWebViewClient());

        WebSettings webviewSettings = getSettings();
        webviewSettings.setSaveFormData(true);
        webviewSettings.setSupportZoom(false);                             // 不支持缩放
        webviewSettings.setJavaScriptEnabled(true);                       //可执行js
        webviewSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);   //设置页面默认缩放密度
        webviewSettings.setDefaultTextEncodingName("UTF-8");              //设置默认的文本编码名称，以便在解码html页面时使用
        webviewSettings.setAllowContentAccess(true);                      //启动或禁用WebView内的内容URL访问
//        webSettings.setAppCacheEnabled(false);                        //设置是否应该启用应用程序缓存api
        webviewSettings.setBuiltInZoomControls(false);                    //设置WebView是否应该使用其内置的缩放机制
        webviewSettings.setUseWideViewPort(true);                         //设置WebView是否应该支持viewport
        webviewSettings.setLoadWithOverviewMode(true);                    //不管WebView是否在概述模式中载入页面，将内容放大适合屏幕宽度
        webviewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);          //重写缓存的使用方式
        webviewSettings.setJavaScriptCanOpenWindowsAutomatically(true);   //告知js自动打开窗口
        webviewSettings.setLoadsImagesAutomatically(true);                //设置WebView是否应该载入图像资源
        webviewSettings.setAllowFileAccess(true);                         //启用或禁用WebView内的文件访问
        webviewSettings.setDomStorageEnabled(true);
    }

    public void setOpenFileChooserCallBack(ZpWebChromeClient.OpenFileChooserCallBack callBack) {
        webChromeClient.setOpenFileChooserCallBack(callBack);
    }

    public void setCreateWindowCallBack(ZpWebChromeClient.CreateWindowCallBack callBack) {
        webChromeClient.setCreateWindowCallBack(callBack);
    }
}
