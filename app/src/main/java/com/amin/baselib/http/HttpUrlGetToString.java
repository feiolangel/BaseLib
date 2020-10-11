package com.amin.baselib.http;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


/**
 * Created by  on 2017/3/29.
 */

public class HttpUrlGetToString<T> {

    private String mUrl;

    private Field[] fields;

    private MyCallback<T> callback;

    private String class_name;

    private String http_response = "";

    private JSONObject jsonObject = null;

    public HttpUrlGetToString(MyCallback<T> callback) {

        this.callback = callback;

    }


    public void execute() {

        HttpServer mServer = this.getClass().getAnnotation(HttpServer.class);

        String url = mServer.value();

        fields = this.getClass().getDeclaredFields();

        String params = "";

        Map<String, String> options = new HashMap<>();

        for (int i = 0; i < fields.length; i++) {

            Field f = fields[i];

            f.setAccessible(true);

            Object value = null;

            try {
                value = f.get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            params = params + f.getName() + "=" + (value instanceof String ? value : String.valueOf(value)) + "&";

            options.put(f.getName(),value instanceof String ? value+"" : String.valueOf(value));

        }

        mUrl = url + "?" + params;

//        mUrl = Conn.SERVICE + type + "-" + String.valueOf(num) + ".json";

        class_name = this.getClass().toString();

        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url+"/")
                .build();

        // 步骤5:创建 网络请求接口 的实例
        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

        //对 发送请求 进行封装
//        Call<ResponseBody> call = request.getCall(url_detail + "?" + params);
        Call<ResponseBody> call = request.getCall(url+"?",options);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时候的回调
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

               handleResponse(response);

            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                callback.onFail("网络连接不可用");
            }
        });

//        Log.e("请求"+class_name,mUrl);

    }

    protected void handleResponse(retrofit2.Response<ResponseBody> response){

        if(response != null){

            try {
                http_response = response.body().string();
//                Log.e("HTTPResponse:", class_name + "->" + http_response);
                callback.onSuccess(parser(http_response));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    protected T parser(String http_response) throws Exception {

        return null;

    }

}
