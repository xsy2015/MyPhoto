package com.xsy.photo.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class MyUtils {

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static void executeInThread(Runnable runnable) {
        new Thread(runnable).start();
    }
    public static boolean checkReadStoragePermissions(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (PackageManager.PERMISSION_GRANTED == permissionCheck) {
                return true;
            } else {//if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MyConstants.SUCCESS_CODE);
                return false;
            }
        } else {
            return true;
        }
    }
    //照相机
    public static boolean checkTakePhotoPermissions(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = context.checkSelfPermission(Manifest.permission.CAMERA);
            if (PackageManager.PERMISSION_GRANTED == permissionCheck) {
                return true;
            } else {//if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1001);
                return false;
            }
        } else {
            return true;
        }
    }
}
