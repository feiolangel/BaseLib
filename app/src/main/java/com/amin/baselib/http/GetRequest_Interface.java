package com.amin.baselib.http;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


/**
 * Created by Carson_Ho on 17/3/20.
 */

public interface GetRequest_Interface {


    @GET("{detail}")
    Call<ResponseBody> getCall(@Path(value = "detail", encoded = true) String detail, @QueryMap Map<String, String> options);
    // 注解里传入 网络请求 的部分URL地址
    // getCall()是接受网络请求数据的方法
}
