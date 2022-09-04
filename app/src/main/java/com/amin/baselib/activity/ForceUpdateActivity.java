package com.amin.baselib.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.amin.baselib.BaseSwitchUtil;
import com.amin.baselib.R;
import com.amin.baselib.utils.BaseCommonUtils;
import com.amin.baselib.utils.BaseTools;
import com.amin.baselib.webview.MyWebViewActivity;
import com.bumptech.glide.Glide;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.HttpUrlConnectionDownloader;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2core.Downloader;
import com.tonyodev.fetch2core.Extras;
import com.tonyodev.fetch2core.FetchObserver;
import com.tonyodev.fetch2core.Func;
import com.tonyodev.fetch2core.MutableExtras;
import com.tonyodev.fetch2core.Reason;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by Administrator on 2016/4/25.
 */
public class ForceUpdateActivity extends AppCompatActivity implements View.OnClickListener, FetchObserver<Download> {

    private static String dirPath;

    private String fileName;

    private ImageView iv_update_background;

    private TextView tv_update;
    private TextView tv_update_num;
    private TextView tv_install;
    private TextView tv_update_introduce;

    private ProgressBar progressBar_update;
    private RelativeLayout update_progress;

    private Request request;
    private Fetch fetch;

    private static final int STORAGE_PERMISSION_CODE = 100;

    public static final String KEY_URL = "KEY_URL";
    public static final String KEY_PROGRESS = "KEY_PROGRESS";
    public static final String KEY_BG = "KEY_BG";

    private String bg;
    private String url;
    private boolean show;//是否显示进度条

    public static void startActivity(Context context, String bg, String url,boolean show) {
        Intent intent = new Intent(context, ForceUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BG, bg);
        bundle.putString(KEY_URL, url);
        bundle.putBoolean(KEY_PROGRESS, show);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_force_update_base);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bg = bundle.getString(KEY_BG);
            url = bundle.getString(KEY_URL);
            show = bundle.getBoolean(KEY_PROGRESS,true);
        }

        final FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .enableRetryOnNetworkGain(true)
                .setDownloadConcurrentLimit(3)
                .setHttpDownloader(new HttpUrlConnectionDownloader(Downloader.FileDownloaderType.PARALLEL))
                // OR
                //.setHttpDownloader(getOkHttpDownloader())
                .build();
        Fetch.Impl.setDefaultInstanceConfiguration(fetchConfiguration);

        initView();

    }

    private void initView() {

        dirPath = BaseTools.getRootDirPath(getApplicationContext());

        iv_update_background = findViewById(R.id.iv_update_background);
        if(!bg.equals("")){

            Glide.with(this).load(bg).into(iv_update_background);

        }

        tv_update = findViewById(R.id.tv_update);
        tv_update_num = findViewById(R.id.tv_update_num);
        (tv_install = findViewById(R.id.tv_install)).setOnClickListener(this);
        tv_update_introduce = findViewById(R.id.tv_update_introduce);
        update_progress = findViewById(R.id.update_progress);

        tv_install.setVisibility(View.INVISIBLE);

        progressBar_update = findViewById(R.id.progressBar_update);

        progressBar_update.setIndeterminate(false);

        fileName = System.currentTimeMillis() + ".apk";

        fetch = Fetch.Impl.getDefaultInstance();

        if(!show){

            update_progress.setVisibility(View.INVISIBLE);

        }




        checkStoragePermission();
//        enqueueDownload();

    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            enqueueDownload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enqueueDownload();
        } else {

        }
    }

    private void enqueueDownload() {
        final String filePath = dirPath + "/apk/" + fileName;
        request = new Request(url, filePath);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);
        request.setExtras(getExtrasForRequest(request));

        fetch.attachFetchObserversForDownload(request.getId(), this)
                .enqueue(request, new Func<Request>() {
                    @Override
                    public void call(@NotNull Request result) {
                        request = result;
                    }
                }, new Func<Error>() {
                    @Override
                    public void call(@NotNull Error result) {
                        Log.d("Error: %1$s", result.toString());
                    }
                });
    }

    private Extras getExtrasForRequest(Request request) {
        final MutableExtras extras = new MutableExtras();
        extras.putBoolean("testBoolean", true);
        extras.putString("testString", "test");
        extras.putFloat("testFloat", Float.MIN_VALUE);
        extras.putDouble("testDouble", Double.MIN_VALUE);
        extras.putInt("testInt", Integer.MAX_VALUE);
        extras.putLong("testLong", Long.MAX_VALUE);
        return extras;
    }

    private boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BaseSwitchUtil.mPackageName + ".baseprovider", file);
                i.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(i);
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tv_install) {
            install(this, dirPath + "/apk/" + fileName);
        }

    }

    @Override
    public void onChanged(Download download, @NonNull Reason reason) {

        updateViews(download, reason);

    }

    private void updateViews(@NotNull Download download, Reason reason) {
        if (request.getId() == download.getId()) {

            if (download.getStatus() == Status.COMPLETED) {

                tv_install.setVisibility(View.VISIBLE);

                install(ForceUpdateActivity.this, dirPath + "/apk/" + fileName);

            } else {
                progressBar_update.setIndeterminate(false);
                progressBar_update.setProgress(download.getProgress());
                tv_update_num.setText(download.getProgress() + "%");
            }

        }
    }
}
