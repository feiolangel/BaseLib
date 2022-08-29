package com.amin.baselib.conn;

import com.amin.baselib.http.HttpServer;
import com.amin.baselib.http.HttpUrlGet;
import com.amin.baselib.http.MyCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/2.
 */

@HttpServer("http://nodeapi.feiolangel.com/7531d7165cf80273/BaseSwitch")
public class GetBaseSwitch extends HttpUrlGet<GetBaseSwitch.Info> {

    public String packageName = "";

    public String tag = "";

    public GetBaseSwitch(MyCallback<Info> asyCallBack) {

        super(asyCallBack);

    }

    @Override
    protected Info parser(JSONObject jsonObject) throws JSONException {

        Info info = new Info();

        info.msg = jsonObject.optString("msg");

        info.id = jsonObject.optString("id");

        info.key = jsonObject.optString("key");

        info.host = jsonObject.optString("host");

        info.type = jsonObject.optString("type");

        info.url = jsonObject.optString("url");

        return info;

    }

    public static class Info{

        public String msg;

        /*Lean id*/
        public String id;

        /*Lean key*/
        public String key;

        /*Lean host*/
        public String host;

        /*接口方式 0：lean，1：bmob*/
        public String type;

        /*Bmob地址*/
        public String url;

    }



}
