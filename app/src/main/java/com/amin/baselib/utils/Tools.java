package com.amin.baselib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by 80534 on 2018/03/15.
 */

public class Tools {

    //转换成深色
    public static int getDarkerColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv); // convert to hsv
        // make darker
        hsv[1] = hsv[1] + 0.2f; // more saturation
        hsv[2] = hsv[2] - 0.2f; // less brightness
        int darkerColor = Color.HSVToColor(hsv);
        return  darkerColor ;
    }

    //转换成浅色
    public static int getBrighterColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv); // convert to hsv

        hsv[1] = hsv[1] - 0.2f; // less saturation
        hsv[2] = hsv[2] + 0.2f; // more brightness
        int darkerColor = Color.HSVToColor(hsv);
        return  darkerColor ;
    }


    public static String F_num(String num){

        if(num.equals("")){

            return "0";

        }else {

            Float price = Float.valueOf(num);
            DecimalFormat format = new DecimalFormat("#.##");
            num = format.format(price);

            if (num.indexOf(".") > 0) {
                num = num.replaceAll("0+?$", "");
                num = num.replaceAll("[.]$", "");
            }
            return num;
        }
    }

    public static String getTime_HH(String time_stamp){

        String true_time = time_stamp.length() == 10 ? time_stamp+"000" : time_stamp;

        SimpleDateFormat format =  new SimpleDateFormat("HH");

        if(time_stamp.equals("")){

            return "";

        }else {

            Long time_long = new Long(Long.parseLong(true_time));
            String time = format.format(time_long);
            return time;

        }

    }

    public static String getTime_mm(String time_stamp){

        String true_time = time_stamp.length() == 10 ? time_stamp+"000" : time_stamp;

        SimpleDateFormat format =  new SimpleDateFormat("mm");

        if(time_stamp.equals("")){

            return "";

        }else {

            Long time_long = new Long(Long.parseLong(true_time));
            String time = format.format(time_long);
            return time;

        }

    }

    public static String getTime_ss(String time_stamp){

        String true_time = time_stamp.length() == 10 ? time_stamp+"000" : time_stamp;

        SimpleDateFormat format =  new SimpleDateFormat("ss");

        if(time_stamp.equals("")){

            return "";

        }else {

            Long time_long = new Long(Long.parseLong(true_time));
            String time = format.format(time_long);
            return time;

        }

    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches("^1[3|4|5|6|7|8]\\d{9}$", mobile);
    }

    public static String getTime(String time_stamp){

        String true_time = time_stamp.length() == 10 ? time_stamp+"000" : time_stamp;

        SimpleDateFormat format =  new SimpleDateFormat("yyyy年MM月dd日");

        if(time_stamp.equals("")){

            return "";

        }else {
            Long time_long = new Long(Long.parseLong(true_time));

            String time = format.format(time_long);

            return time;
        }
    }

    public static String getTime1(String time_stamp){

        String true_time = time_stamp.length() == 10 ? time_stamp+"000" : time_stamp;

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");

        if(time_stamp.equals("")){

            return "";

        }else {
            Long time_long = new Long(Long.parseLong(true_time));

            String time = format.format(time_long);

            return time;
        }
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    //强更三个方法
    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes){
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }



}
