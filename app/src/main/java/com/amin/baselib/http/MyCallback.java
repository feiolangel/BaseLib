package com.amin.baselib.http;

/**
 * Created by  on 2017/3/30.
 */

public abstract class MyCallback<T> {

    public abstract void onSuccess(T t);

    public abstract void onFail(String msg);

}
