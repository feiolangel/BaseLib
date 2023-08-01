package com.amin.baselib.webview;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.amin.baselib.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author zpan
 */
public class MainWebActivity extends AppCompatActivity {

    public static final int REQUEST_SELECT_FILE_CODE = 100;
    private static final int REQUEST_FILE_CHOOSER_CODE = 101;
    private static final int REQUEST_FILE_CAMERA_CODE = 102;
    /** 默认图片压缩大小（单位：K） */
    public static final int IMAGE_COMPRESS_SIZE_DEFAULT = 400;
    /** 压缩图片最小高度 */
    public static final int COMPRESS_MIN_HEIGHT = 900;
    /** 压缩图片最小宽度 */
    public static final int COMPRESS_MIN_WIDTH = 675;

    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mUploadMsgs;
    /** 相机拍照返回的图片文件 */
    private File mFileFromCamera;
    private Dialog selectPicDialog;
    private boolean mIsOpenCreateWindow;
    private WindowWebFragment mNewWindowWebFragment;

    private String url = "https://www.yomaexsh.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_web);

        //状态栏字体设置黑色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initView();
    }

    private void initView() {
        ZpWebView webView = findViewById(R.id.webview);
        webView.setOpenFileChooserCallBack(new ZpWebChromeClient.OpenFileChooserCallBack() {
            @Override
            public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMsg = uploadMsg;
                showSelectPictrueDialog(0, null);
            }

            @Override
            public void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {
                if (mUploadMsgs != null) {
                    mUploadMsgs.onReceiveValue(null);
                }
                mUploadMsgs = filePathCallback;
                showSelectPictrueDialog(1, fileChooserParams);
            }
        });
        webView.setCreateWindowCallBack(new ZpWebChromeClient.CreateWindowCallBack() {
            @Override
            public void onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                mIsOpenCreateWindow = true;
                mNewWindowWebFragment = WindowWebFragment.newInstance();
                mNewWindowWebFragment.setMessage(resultMsg);
                getSupportFragmentManager().beginTransaction().
                    add(R.id.container_for_fragment, mNewWindowWebFragment).commit();
            }
        });
//        String htmlSrc = "<html>"
//            + "<input style=\"font-size:40px;margin:40px 0px 0px 40px;\" type=\"file\" accept=\"image/*\" multiple=\"multiple\">"
//            + "</html>";
//        webView.loadData(htmlSrc, "text/html", "UTF-8");
        webView.loadUrl(url);

    }

    /**
     * 选择图片弹框
     */
    private void showSelectPictrueDialog(final int tag, final WebChromeClient.FileChooserParams fileChooserParams) {
        selectPicDialog = new Dialog(this, R.style.Dialog_NoTitle);
        selectPicDialog.setCanceledOnTouchOutside(false);
        selectPicDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                if (mUploadMsgs != null) {
//                    mUploadMsgs.onReceiveValue(null);
//                    mUploadMsgs = null;
//                }
            }
        });
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_select_pictrue, null);
        // 相册
        TextView album = view.findViewById(R.id.tv_select_pictrue_album);
        // 相机
        TextView camera = view.findViewById(R.id.tv_select_pictrue_camera);
        // 取消
        TextView cancel = view.findViewById(R.id.tv_select_pictrue_cancel);

        album.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (tag == 0) {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");
                    startActivityForResult(Intent.createChooser(i, "File Browser"), REQUEST_FILE_CHOOSER_CODE);
                } else {
                    try {
                        Intent intent = fileChooserParams.createIntent();
                        startActivityForResult(intent, REQUEST_SELECT_FILE_CODE);
                    } catch (ActivityNotFoundException e) {
                        mUploadMsgs = null;
                    }
                }
                selectPicDialog.dismiss();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeCameraPhoto();
                selectPicDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUploadMsg != null) {
                    mUploadMsg.onReceiveValue(null);
                    mUploadMsg = null;
                }
                if (mUploadMsgs != null) {
                    mUploadMsgs.onReceiveValue(null);
                    mUploadMsgs = null;
                }

                selectPicDialog.dismiss();
            }
        });

        selectPicDialog.setContentView(view);
        selectPicDialog.show();
    }

    public void takeCameraPhoto() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(this, "设备无摄像头", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, "need use storage", 200, Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            mFileFromCamera = getFileFromCamera();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imgUrl;
            if (getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.M) {
                String authority = getApplicationInfo().packageName+".UploadFileProvider";
                imgUrl = FileProvider.getUriForFile(this, authority, mFileFromCamera);
            } else {
                imgUrl = Uri.fromFile(mFileFromCamera);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUrl);
            startActivityForResult(intent, REQUEST_FILE_CAMERA_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = true;
        for(int i=0;i<grantResults.length;i++){

            if(grantResults[i] == PackageManager.PERMISSION_DENIED){

                result = false;

            }

        }

        if(result){

            mFileFromCamera = getFileFromCamera();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imgUrl;
            if (getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.M) {
                String authority = getApplicationInfo().packageName+".UploadFileProvider";
                imgUrl = FileProvider.getUriForFile(this, authority, mFileFromCamera);
            } else {
                imgUrl = Uri.fromFile(mFileFromCamera);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUrl);
            startActivityForResult(intent, REQUEST_FILE_CAMERA_CODE);

        }

    }

    private File getFileFromCamera() {
        File imageFile = null;
        String storagePath;
        File storageDir;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            storagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            storageDir = new File(storagePath);
            storageDir.mkdirs();
            imageFile = File.createTempFile(timeStamp, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CANCELED) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
                mUploadMsg = null;
            }
            if (mUploadMsgs != null) {
                mUploadMsgs.onReceiveValue(null);
                mUploadMsgs = null;
            }
            return;
        }
        switch (requestCode) {
            case REQUEST_SELECT_FILE_CODE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (mUploadMsgs == null) {
                        return;
                    }
                    mUploadMsgs.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                    mUploadMsgs = null;
                }
                break;
            case REQUEST_FILE_CHOOSER_CODE:
                if (mUploadMsg == null) {
                    return;
                }
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                mUploadMsg.onReceiveValue(result);
                mUploadMsg = null;
                break;
            case REQUEST_FILE_CAMERA_CODE:
                takePictureFromCamera();
                break;
            default:
                break;
        }
    }

    /**
     * 处理相机返回的图片
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void takePictureFromCamera() {
        if (mFileFromCamera != null && mFileFromCamera.exists() && mFileFromCamera.length()>0) {
            String filePath = mFileFromCamera.getAbsolutePath();
            // 压缩图片到指定大小
            File imgFile = ZpImageUtils.compressImage(this, filePath, COMPRESS_MIN_WIDTH, COMPRESS_MIN_HEIGHT, IMAGE_COMPRESS_SIZE_DEFAULT);

            Uri localUri = Uri.fromFile(imgFile);
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            this.sendBroadcast(localIntent);
            Uri result = Uri.fromFile(imgFile);

            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(Uri.parse(filePath));
                mUploadMsg = null;
            }
            if (mUploadMsgs != null) {
                mUploadMsgs.onReceiveValue(new Uri[] { result });
                mUploadMsgs = null;
            }
        }else {

            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
                mUploadMsg = null;
                return;
            }
            if (mUploadMsgs != null) {
                mUploadMsgs.onReceiveValue(null);
                mUploadMsgs = null;
                return;
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onBack() {
        if (mIsOpenCreateWindow) {
            getSupportFragmentManager().beginTransaction().remove(mNewWindowWebFragment).commit();
            mIsOpenCreateWindow = false;
        } else {
            finish();
        }
    }
}
