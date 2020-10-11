package com.amin.baselib.http;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Carson_Ho on 17/3/21.
 */
public interface PostRequest_Interface {

    @FormUrlEncoded
    @POST("{detail}")
    Call<ResponseBody> getCall(@Path(value = "detail", encoded = true) String detail, @FieldMap Map<String, String> fields);
    //采用@Post表示Post方法进行请求（传入部分url地址）
    // 采用@FormUrlEncoded注解的原因:API规定采用请求格式x-www-form-urlencoded,即表单形式
    // 需要配合@Field使用
}

