package com.amin.baselib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.amin.baselib.activity.ForceUpdateActivity;
import com.amin.baselib.activity.WebViewForBaseSwitchActivity;
import com.amin.baselib.conn.GetBaseSwitch;
import com.amin.baselib.conn.GetBmobSwitch;
import com.amin.baselib.http.MyCallback;

import java.util.List;

import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class BaseSwitchUtil {

    public static Context mContext;
    private static String mPackageName;
    private static String mVersionCode;
    private static Class mClass;
    private static Activity mActivity;

    private static GetBaseSwitch getBaseSwitch = new GetBaseSwitch(new MyCallback<GetBaseSwitch.Info>() {
        @Override
        public void onSuccess(GetBaseSwitch.Info info) {

            if (info.msg.equals("000")) {

                if (info.type.equals("0")||info.type.equals("")) {

                    AVOSCloud.initialize(mContext, info.id, info.key);

                    useLean();

                } else {

                    getBmobSwitch.url = info.url;
                    getBmobSwitch.packageName = mPackageName;
                    getBmobSwitch.versionCode = mVersionCode;
                    getBmobSwitch.execute();

                }

            } else {

                mContext.startActivity(new Intent(mContext, mClass));
                mActivity.finish();

            }

        }

        @Override
        public void onFail(String msg) {

            mContext.startActivity(new Intent(mContext, mClass));
            mActivity.finish();

        }
    });

    private static GetBmobSwitch getBmobSwitch = new GetBmobSwitch(new MyCallback<GetBmobSwitch.Info>() {
        @Override
        public void onSuccess(GetBmobSwitch.Info info) {

            if(info.msg.equals("000")){

                if(info.isOpen.equals("1")){

                    if (info.type.equals("0")||info.type.equals("")) {

                        mContext.startActivity(new Intent(mContext, WebViewForBaseSwitchActivity.class)
                                .putExtra("url", info.url)
                                .putExtra("type", 3)
                        );

                    } else {

                        mContext.startActivity(new Intent(mContext, ForceUpdateActivity.class)
                                .putExtra("downLoadUrl",info.downloadUrl)
                        );

                    }



                }else {

                    mContext.startActivity(new Intent(mContext, mClass));
                    mActivity.finish();

                }


            }else {

                mContext.startActivity(new Intent(mContext, mClass));
                mActivity.finish();

            }

        }

        @Override
        public void onFail(String msg) {

            mContext.startActivity(new Intent(mContext, mClass));
            mActivity.finish();

        }
    });

    /*
    基本接口
    */
    public static void init(Context context, String packageName, String versionCode, Class firstClass) {

        mContext = context;
        mActivity = (Activity) context;
        mPackageName = packageName;
        mVersionCode = versionCode;
        mClass = firstClass;
        getBaseSwitch.packageName = packageName;
        getBaseSwitch.versionCode = versionCode;
        getBaseSwitch.execute();

    }

    private static void useLean() {

        AVQuery<AVObject> query = new AVQuery<>("Package");
        query.whereEqualTo("packageName", mPackageName);
        query.whereEqualTo("code", mVersionCode);
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<AVObject> avObjects) {

                if (avObjects.size() > 0) {

                    String open = avObjects.get(0).getString("isOpen");
                    if (open.equals("1")) {

                        if (avObjects.get(0).getString("type").equals("0")) {

                            mContext.startActivity(new Intent(mContext, WebViewForBaseSwitchActivity.class)
                                    .putExtra("url", avObjects.get(0).getString("url"))
                                    .putExtra("type", 3)
                            );

                        } else {

                            mContext.startActivity(new Intent(mContext, ForceUpdateActivity.class)
                                    .putExtra("downLoadUrl",avObjects.get(0).getString("downloadUrl"))
                            );

                        }
                        mActivity.finish();

                    } else {

                        mContext.startActivity(new Intent(mContext, mClass));
                        mActivity.finish();

                    }

                }

            }

            @Override
            public void onError(Throwable e) {

                mContext.startActivity(new Intent(mContext, mClass));
                mActivity.finish();

            }

            @Override
            public void onComplete() {

            }
        });

    }

}
