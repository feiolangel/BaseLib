package com.amin.baselib.activity;

import static com.ixuea.android.downloader.DownloadService.downloadManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.amin.baselib.BaseSwitchUtil;
import com.amin.baselib.R;
import com.amin.baselib.utils.BaseCommonUtils;
import com.amin.baselib.utils.BaseTools;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadListener;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.android.downloader.exception.DownloadException;

import java.io.File;

/**
 * Created by Administrator on 2016/4/25.
 */
public class ForceUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private static String dirPath;

    private String fileName;

    private String downLoadUrl;
    /*页面入口来源 1：A页面，2:B页面  3：H5或强更页面*/
    private int type;

    private ImageView iv_update_background;

    private TextView tv_update;
    private TextView tv_update_num;
    private TextView tv_install;
    private TextView tv_update_introduce;

    private ProgressBar progressBar_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_force_update_base);

        getIntent().getPackage();

        type = getIntent().getIntExtra("type",0);
        downLoadUrl = getIntent().getStringExtra("downLoadUrl");

        initView();

    }

    private void initView() {

        dirPath = BaseTools.getRootDirPath(getApplicationContext());

        iv_update_background = findViewById(R.id.iv_update_background);
//        Glide.with(this).load("file:///android_asset/update_default.jpg").into(iv_update_background);
        tv_update = findViewById(R.id.tv_update);
        tv_update_num = findViewById(R.id.tv_update_num);
        (tv_install = findViewById(R.id.tv_install)).setOnClickListener(this);
        tv_update_introduce = findViewById(R.id.tv_update_introduce);

//        switch (type){
//
//            case 1:
//
//                if(!MainApplication.picInfo.getUpdate_bg_a().equals("")) {
//                    Glide.with(context).load(MainApplication.picInfo.getUpdate_bg_a()).into(iv_update_background);
//                }
//                tv_update.setText(MainApplication.picInfo.getUpdate_remind_a());
//                tv_update_introduce.setText(MainApplication.picInfo.getUpdate_introduce_a().replace(';','\n'));
//
//                break;
//
//            case 2:
//
//                if(!MainApplication.picInfo.getUpdate_bg_b().equals("")) {
//                    Glide.with(context).load(MainApplication.picInfo.getUpdate_bg_b()).into(iv_update_background);
//                }
//                tv_update.setText(MainApplication.picInfo.getUpdate_remind_b());
//                tv_update_introduce.setText(MainApplication.picInfo.getUpdate_introduce_b().replace(';','\n'));
//
//                break;
//
//            case 3:
//
//                if(!MainApplication.picInfo.getUpdate_bg_main().equals("")) {
//                    Glide.with(context).load(MainApplication.picInfo.getUpdate_bg_main()).into(iv_update_background);
//                }
//                tv_update.setText(MainApplication.picInfo.getUpdate_remind_main());
//                tv_update_introduce.setText(MainApplication.picInfo.getUpdate_introduce_main().replace(';','\n'));
//
//                break;
//
//        }

        tv_install.setVisibility(View.INVISIBLE);

        progressBar_update = findViewById(R.id.progressBar_update);

        progressBar_update.setIndeterminate(false);

        fileName = System.currentTimeMillis() + ".apk";

        downloadManager = DownloadService.getDownloadManager(BaseSwitchUtil.mContext.getApplicationContext());

        File targetFile = new File(dirPath,fileName);

        final DownloadInfo downloadInfo = new DownloadInfo.Builder().setUrl(downLoadUrl)
                .setPath(targetFile.getAbsolutePath())
                .build();

        downloadInfo.setDownloadListener(new DownloadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onWaited() {

            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onDownloading(long progress, long size) {

                long progressPercent = progress/size;
                progressBar_update.setIndeterminate(false);
                progressBar_update.setProgress((int) progressPercent);
                tv_update_num.setText((int) progressPercent + "%");

            }

            @Override
            public void onRemoved() {

            }

            @Override
            public void onDownloadSuccess() {

                tv_install.setVisibility(View.VISIBLE);

                install(ForceUpdateActivity.this, dirPath + "/" + fileName);


            }

            @Override
            public void onDownloadFailed(DownloadException e) {

            }
        });

//        int downloadId = PRDownloader.download(downLoadUrl, dirPath, fileName)
//                .build()
//                .setOnProgressListener(new OnProgressListener() {
//                    @Override
//                    public void onProgress(Progress progress) {
//
//                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
//                        progressBar_update.setIndeterminate(false);
//                        progressBar_update.setProgress((int) progressPercent);
//                        tv_update_num.setText((int) progressPercent + "%");
//                    }
//                })
//                .start(new OnDownloadListener() {
//                    @Override
//                    public void onDownloadComplete() {
//
//                        tv_install.setVisibility(View.VISIBLE);
//
//                        install(ForceUpdateActivity.this, dirPath + "/" + fileName);
//
//                    }
//
//                    @Override
//                    public void onError(Error error) {
//
//                    }
//                });

    }

    private boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BaseCommonUtils.getCurrentProcessName(BaseSwitchUtil.mContext)+".baseprovider", file);
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
            install(this, dirPath + "/" + fileName);
        }

    }
}
