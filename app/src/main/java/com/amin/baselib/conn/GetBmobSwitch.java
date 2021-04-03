package com.amin.baselib.conn;

import com.amin.baselib.http.HttpDynamicUrlGet;
import com.amin.baselib.http.MyCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/2.
 */


public class GetBmobSwitch extends HttpDynamicUrlGet<GetBmobSwitch.Info> {

    public String url = "";

    public String packageName = "";

    public String versionCode = "";

    public GetBmobSwitch(MyCallback<Info> asyCallBack) {

        super(asyCallBack);

    }

    @Override
    protected Info parser(JSONObject jsonObject) throws JSONException {

        Info info = new Info();

        info.msg = jsonObject.optString("msg");

        info.type = jsonObject.optString("type");

        info.isOpen = jsonObject.optString("isOpen");

        info.url = jsonObject.optString("url");

        info.downloadUrl = jsonObject.optString("downloadUrl");

        info.h5Type = jsonObject.optString("h5Type");

        return info;

    }

    public static class Info{

        public String msg;

        /*0:h5 1:强更*/
        public String type;

        public String isOpen;

        public String url;

        public String downloadUrl;

        /*H5方式 0：APP内  1：外跳系统浏览器*/
        public String h5Type;

    }



}
