package com.amin.baselib.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amin.baselib.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.yanzhenjie.permission.setting.Setting;


import java.util.List;

/**
 * 类名称: BaseActivity
 * 类描述: 页面基类中做动态权限申请相关处理逻辑
 * 创建人: 陈书东
 * 创建时间: 2018/8/8 08:08
 */
public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected RelativeLayout rl_title;
    protected TextView tv_title;
    protected ImageView iv_back;

    private static final int REQUEST_CODE_SETTING = 100;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        if (iv_back != null) {
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void setTitle(String title) {
        if (tv_title != null) {
            tv_title.setText(title);
        }
    }

    protected void cameraReadWriteStorage() {

    }

    protected void camera() {

    }

    protected void chooseImage() {

    }

    protected void chooseVideo() {

    }

    protected void chooseAudio() {

    }

    @SuppressLint("WrongConstant")
    protected void requestPermission(final int type, String... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (type == Constant.permission_CAMERA_READ_WRITE_EXTERNAL_STORAGE) {
                            cameraReadWriteStorage();
                        } else if (type == Constant.permission_CAMERA) {
                            camera();
                        } else if (type == Constant.permission_IMAGE) {
                            chooseImage();
                        } else if (type == Constant.permission_VIDEO) {
                            chooseVideo();
                        } else if (type == Constant.permission_AUDIO) {
                            chooseAudio();
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(BaseActivity.this, permissions)) {
                            showSettingDialog(BaseActivity.this, permissions);
                        }
                    }
                })
                .start();
    }

    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void setPermission() {

        AndPermission.with(this)
                .runtime()
                .setting()
                .start(REQUEST_CODE_SETTING);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                Toast.makeText(BaseActivity.this, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

}