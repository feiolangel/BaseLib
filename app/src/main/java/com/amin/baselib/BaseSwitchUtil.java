package com.amin.baselib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.amin.baselib.ScreenHelper.ScaleScreenHelper;
import com.amin.baselib.ScreenHelper.ScaleScreenHelperFactory;
import com.amin.baselib.activity.ForceUpdateActivity;
import com.amin.baselib.activity.PrivacyDialogBaseActivity;
import com.amin.baselib.activity.UserAgreementBaseActivity;
import com.amin.baselib.activity.WebViewForBaseSwitchActivity;
import com.amin.baselib.conn.GetBaseSwitch;
import com.amin.baselib.conn.GetBmobSwitch;
import com.amin.baselib.http.MyCallback;
import com.amin.baselib.utils.BaseCommonUtils;

import java.util.List;

import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class BaseSwitchUtil {

    public static Context mContext;
    private static String mPackageName;
    private static String mTag;
    private static String mPrivacyUrl;
    private static String mUserAgreementUrl;
    private static Class mClass;
    private static Activity mActivity;
    public static SharedPreferences Preferences;
    public static ScaleScreenHelper scaleScreenHelper;
    public static ScaleScreenHelperFactory mFactory;

    private static GetBaseSwitch getBaseSwitch = new GetBaseSwitch(new MyCallback<GetBaseSwitch.Info>() {
        @Override
        public void onSuccess(GetBaseSwitch.Info info) {

            if (info.msg.equals("000")) {

                if (info.type.equals("0") || info.type.equals("")) {

                    AVOSCloud.initialize(mContext, info.id, info.key);

                    useLean();

                } else {

                    getBmobSwitch.url = info.url;
                    getBmobSwitch.packageName = mPackageName;
                    getBmobSwitch.tag = mTag;
                    getBmobSwitch.execute();

                }

            } else {

                mContext.startActivity(new Intent(mContext, mClass));
                Finish();

            }

        }

        @Override
        public void onFail(String msg) {

            mContext.startActivity(new Intent(mContext, mClass));
            Finish();

        }
    });

    private static GetBmobSwitch getBmobSwitch = new GetBmobSwitch(new MyCallback<GetBmobSwitch.Info>() {
        @Override
        public void onSuccess(GetBmobSwitch.Info info) {

            if (info.msg.equals("000")) {

                if (info.isOpen.equals("1")) {

                    if (info.type.equals("0") || info.type.equals("")) {

                        if (info.h5Type.equals("1")) {

                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(info.url);
                            intent.setData(content_url);
                            mContext.startActivity(intent);

                        } else {

                            mContext.startActivity(new Intent(mContext, WebViewForBaseSwitchActivity.class)
                                    .putExtra("url", info.url)
                                    .putExtra("type", 3)
                            );

                        }

                    } else {

                        mContext.startActivity(new Intent(mContext, ForceUpdateActivity.class)
                                .putExtra("downLoadUrl", info.downloadUrl)
                        );

                    }


                } else {

                    mContext.startActivity(new Intent(mContext, mClass));
                    Finish();

                }


            } else {

                mContext.startActivity(new Intent(mContext, mClass));
                Finish();

            }

        }

        @Override
        public void onFail(String msg) {

            mContext.startActivity(new Intent(mContext, mClass));
            Finish();

        }
    });


    /*
    基本接口
    */
    public static void init(Context context, String packageName,String tag, Class firstClass) {

        init(context, packageName, tag, firstClass, "file:///android_asset/newprivacy.html","file:///android_asset/useragreement.html");

    }

    public static void init(Context context, String packageName,String tag, Class firstClass, String privacyUrl,String useragreementUrl) {

        mContext = context;
        mActivity = (Activity) context;
        mPackageName = packageName;
        mTag = tag;
        mClass = firstClass;
        if (Preferences == null) {
            Preferences = context.getSharedPreferences(BaseCommonUtils.getCurrentProcessName(context), Context.MODE_PRIVATE);
        }
        getBaseSwitch.packageName = packageName;
        getBaseSwitch.tag = tag;
        getBaseSwitch.execute();

        mFactory.create(context, 720);
        scaleScreenHelper = mFactory.getInstance();
        mPrivacyUrl = privacyUrl;
        mUserAgreementUrl = useragreementUrl;

    }

    private static void useLean() {

        AVQuery<AVObject> query = new AVQuery<>("Package");
        query.whereEqualTo("packageName", mPackageName);
        query.whereEqualTo("tag", mTag);
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

                            if (avObjects.get(0).getString("h5Type").equals("1")) {

                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(avObjects.get(0).getString("url"));
                                intent.setData(content_url);
                                mContext.startActivity(intent);

                            } else {

                                mContext.startActivity(new Intent(mContext, WebViewForBaseSwitchActivity.class)
                                        .putExtra("url", avObjects.get(0).getString("url"))
                                        .putExtra("type", 3)
                                );

                            }

                        } else {

                            mContext.startActivity(new Intent(mContext, ForceUpdateActivity.class)
                                    .putExtra("downLoadUrl", avObjects.get(0).getString("downloadUrl"))
                            );

                        }
                        mActivity.finish();

                    } else {

                        mContext.startActivity(new Intent(mContext, mClass));
                        Finish();

                    }

                }

            }

            @Override
            public void onError(Throwable e) {

                mContext.startActivity(new Intent(mContext, mClass));
                Finish();

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private static void Finish() {

        if (Preferences.getBoolean("Privacy", true)) {
            mContext.startActivity(new Intent(mContext, UserAgreementBaseActivity.class)
                    .putExtra("privacy", mPrivacyUrl)
                    .putExtra("agreement", mUserAgreementUrl)
            );
        }
        mActivity.finish();

    }

}
