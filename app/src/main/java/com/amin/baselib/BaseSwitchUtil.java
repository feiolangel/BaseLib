package com.amin.baselib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

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

import java.util.List;

import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class BaseSwitchUtil {

    public static Context mContext = null;
    public String mPackageName = "";
    public String mTag = "";
    public String mPrivacyUrl = "file:///android_asset/privacybase.html";
    public String mUserAgreementUrl = "file:///android_asset/useragreementbase.html";
    public String mShowText = "";
    public Class mClass = null;
    public Activity mActivity;
    public boolean mPortrait = true;
    private boolean useIntent = false;
    private Intent mIntent;
    public static SharedPreferences Preferences;
    public static ScaleScreenHelper scaleScreenHelper;
    public static ScaleScreenHelperFactory mFactory;

    public GetBaseSwitch getBaseSwitch = new GetBaseSwitch(new MyCallback<GetBaseSwitch.Info>() {
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

                startFirst();
                Finish();

            }

        }

        @Override
        public void onFail(String msg) {

            startFirst();
            Finish();

        }
    });

    public GetBmobSwitch getBmobSwitch = new GetBmobSwitch(new MyCallback<GetBmobSwitch.Info>() {
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

                    startFirst();
                    Finish();

                }


            } else {

                startFirst();
                Finish();

            }

        }

        @Override
        public void onFail(String msg) {

            startFirst();
            Finish();

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

    public void init() {

        if (mShowText.equals("")) {

            mShowText = mContext.getString(R.string.app_explain);

        }

        if (Preferences == null) {
            Preferences = mContext.getSharedPreferences(BaseCommonUtils.getCurrentProcessName(mContext), Context.MODE_PRIVATE);
        }

        if (mPortrait) {

            mFactory.create(mContext, 720);
            scaleScreenHelper = mFactory.getInstance();

        } else {

            mFactory.create(mContext, 1280);
            scaleScreenHelper = mFactory.getInstance();

        }

        getBaseSwitch.packageName = mPackageName;
        getBaseSwitch.tag = mTag;
        getBaseSwitch.execute();

    }


    public void useLean() {

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

                        startFirst();
                        Finish();

                    }

                }

            }

            @Override
            public void onError(Throwable e) {

                startFirst();
                Finish();

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void Finish() {

        Intent intent;

        if (mPortrait) {

            intent = new Intent(mContext, UserAgreementBaseActivity.class);

        } else {

            intent = new Intent(mContext, UserAgreementLandscapeBaseActivity.class);

        }

        if (Preferences.getBoolean("Privacy", true)) {
            mContext.startActivity(intent
                    .putExtra("privacy", mPrivacyUrl)
                    .putExtra("agreement", mUserAgreementUrl)
                    .putExtra("showText", mShowText)
            );
        }
        mActivity.finish();

    }

    private void startFirst(){

        if(useIntent){

            mContext.startActivity(mIntent);

        }else {

            mContext.startActivity(new Intent(mContext, mClass));
        }

    }

}
