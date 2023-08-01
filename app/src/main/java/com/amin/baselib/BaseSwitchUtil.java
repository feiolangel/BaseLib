package com.amin.baselib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.amin.baselib.ScreenHelper.ScaleScreenHelper;
import com.amin.baselib.ScreenHelper.ScaleScreenHelperFactory;
import com.amin.baselib.activity.ForceUpdateActivity;
import com.amin.baselib.activity.UserAgreementBaseActivity;
import com.amin.baselib.activity.UserAgreementLandscapeBaseActivity;
import com.amin.baselib.activity.WebViewForBaseSwitchActivity;
import com.amin.baselib.conn.GetBaseSwitch;
import com.amin.baselib.conn.GetBmobSwitch;
import com.amin.baselib.http.MyCallback;
import com.amin.baselib.utils.BaseCommonUtils;
import com.amin.baselib.webview.MainWebActivity;

import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LeanCloud;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class BaseSwitchUtil {

    public static Context mContext = null;
    public static String mPackageName = "";
    public static String mTag = "";
    public static String mPrivacyUrl = "file:///android_asset/privacybase.html";
    public static String mUserAgreementUrl = "file:///android_asset/useragreementbase.html";
    public static String mUpdateBG = "";
    public static String mShowText = "";
    public static Class mClass = null;
    public static Activity mActivity;
    public static boolean mPortrait = true;
    private static boolean useIntent = false;
    private static boolean mShow = true;
    private static boolean showProgressBar = true;
    private static Intent mIntent;
    public static SharedPreferences Preferences;
    public static ScaleScreenHelper scaleScreenHelper;
    public static ScaleScreenHelperFactory mFactory;

    public static GetBaseSwitch getBaseSwitch = new GetBaseSwitch(new MyCallback<GetBaseSwitch.Info>() {
        @Override
        public void onSuccess(GetBaseSwitch.Info info) {

            if (info.msg.equals("000")) {

                if (info.type.equals("0") || info.type.equals("")) {

                    if (info.host == null || info.host.equals("")) {

                        LeanCloud.initialize(mContext, info.id, info.key);

                    } else {

                        LeanCloud.initialize(mContext, info.id, info.key, info.host);

                    }

                    useLean();

                } else {

                    getBmobSwitch.url = info.url;
                    getBmobSwitch.packageName = mPackageName;
                    getBmobSwitch.tag = mTag;
                    getBmobSwitch.execute();

                }

            } else {

                startFirst();

            }

        }

        @Override
        public void onFail(String msg) {

            startFirst();

        }
    });

    public static GetBmobSwitch getBmobSwitch = new GetBmobSwitch(new MyCallback<GetBmobSwitch.Info>() {
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

                        ForceUpdateActivity.startActivity(mContext, mUpdateBG, info.downloadUrl, showProgressBar);

//                        mContext.startActivity(new Intent(mContext, ForceUpdateActivity.class)
//                                .putExtra("downLoadUrl", info.downloadUrl)
//                        );

                    }


                } else {

                    startFirst();

                }


            } else {

                startFirst();

            }

        }

        @Override
        public void onFail(String msg) {

            startFirst();

        }
    });


    public BaseSwitchUtil setContext(Context context) {

        mContext = context;
        mActivity = (Activity) context;
        return this;

    }

    public BaseSwitchUtil setPackageName(String packageName) {

        mPackageName = packageName;
        return this;
    }

    public BaseSwitchUtil setTag(String tag) {

        mTag = tag;

        return this;

    }

    public BaseSwitchUtil setBG(String bg) {

        mUpdateBG = bg;
        return this;

    }

    public BaseSwitchUtil setShowProgressBar(boolean show) {

        showProgressBar = show;
        return this;

    }

    public BaseSwitchUtil setFirstClass(Class firstClass) {

        mClass = firstClass;
        return this;

    }

    public BaseSwitchUtil setPrivacyUrl(String privacyUrl) {

        mPrivacyUrl = privacyUrl;
        return this;
    }

    public BaseSwitchUtil setUserAgreementUrl(String userAgreementUrl) {

        mUserAgreementUrl = userAgreementUrl;
        return this;
    }

    public BaseSwitchUtil setShowText(String showText) {

        mShowText = showText;
        return this;
    }

    public Context getContext() {

        return mContext;

    }

    public BaseSwitchUtil setPortrait(boolean portrait) {

        mPortrait = portrait;
        return this;

    }

    public BaseSwitchUtil setFirstIntent(Intent intent) {

        mIntent = intent;
        useIntent = true;
        return this;

    }

    public BaseSwitchUtil setShow(boolean show) {

        mShow = show;
        return this;

    }

    public void init() {

        Log.e("Setting", mPackageName + "&" + mTag);

        if (mShowText.equals("")) {

            mShowText = mContext.getString(R.string.app_explain);

        }

        if (Preferences == null) {
            Preferences = mContext.getSharedPreferences(mPackageName, Context.MODE_PRIVATE);
        }

        if (mPortrait) {

            mFactory.create(mContext, 720);
            scaleScreenHelper = mFactory.getInstance();

        } else {

            mFactory.create(mContext, 1280);
            scaleScreenHelper = mFactory.getInstance();

        }

        showPrivacy();



    }

    private static void  useBase(){

        getBaseSwitch.packageName = mPackageName;
        getBaseSwitch.tag = mTag;
        getBaseSwitch.execute();

    }





    public static void useLean() {

        LCQuery<LCObject> query = new LCQuery<>("Package");
        query.whereEqualTo("packageName", mPackageName);
        query.whereEqualTo("tag", mTag);
        query.findInBackground().subscribe(new Observer<List<LCObject>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<LCObject> avObjects) {

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


                                mContext.startActivity(new Intent(mContext, MainWebActivity.class)
                                        .putExtra("url", avObjects.get(0).getString("url"))
                                );

                            }

                        } else {

//                            mContext.startActivity(new Intent(mContext, ForceUpdateActivity.class)
//                                    .putExtra("downLoadUrl", avObjects.get(0).getString("downloadUrl"))
//                            );

                            ForceUpdateActivity.startActivity(mContext, mUpdateBG, avObjects.get(0).getString("downloadUrl"), showProgressBar);

                        }

                    } else {

                        startFirst();

                    }

                }

            }

            @Override
            public void onError(Throwable e) {

                startFirst();

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public static void showPrivacy() {

        if (Preferences.getBoolean("Privacy", true) && mShow) {

            Intent intent;
            if (mPortrait) {
                intent = new Intent(mContext, UserAgreementBaseActivity.class);
            } else {
                intent = new Intent(mContext, UserAgreementLandscapeBaseActivity.class);
            }
            intent.putExtra("privacy", mPrivacyUrl)
                    .putExtra("agreement", mUserAgreementUrl)
                    .putExtra("showText", mShowText);

            mContext.startActivity(intent);

        } else {

            toFirst();

        }

    }

    public static void toFirst() {

        useBase();

    }

//    private void startPrivacy() {
//
//        Intent intent;
//
//        if (mPortrait) {
//
//            intent = new Intent(mContext, UserAgreementBaseActivity.class);
//
//        } else {
//
//            intent = new Intent(mContext, UserAgreementLandscapeBaseActivity.class);
//
//        }
//
//        intent.putExtra("privacy", mPrivacyUrl)
//                .putExtra("agreement", mUserAgreementUrl)
//                .putExtra("showText", mShowText);
//
//
//        if (Preferences.getBoolean("Privacy", true)) {
//
//            if(mJump) {
//
//                mContext.startActivity(intent);
//
//            }else {
//
//                intent.putExtra("junm",false);
//                mContext.startActivity(intent);
//
//            }
//
//        }else {
//
//            if(!mJump){
//
//                startFirst();
//
//            }
//
//        }
//
//}


    private static void startFirst() {

        mActivity.finish();

        if (useIntent) {

            mContext.startActivity(mIntent);

        } else {

            mContext.startActivity(new Intent(mContext, mClass));
        }

    }

}
