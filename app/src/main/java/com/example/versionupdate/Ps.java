package com.example.versionupdate;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Ps {
    public final static int SYSTEM_TYPE = 0;
    public final static int USER_TYPE = 1;

    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }


    public static String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }


    public static void getPackageSystemDataLog(Context context) {
         PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                Log.v(MainActivity.TAG, "Installed package (System) :" + packageInfo.packageName);
            }
            else
                Log.v(MainActivity.TAG, "Installed package (User) :" + packageInfo.packageName);
        }
    }


    public static List<ApplicationInfo> getApplicationInfoList(Context context, int type) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<ApplicationInfo> data = new ArrayList<>();


        switch (type) {
            case SYSTEM_TYPE:
                for (ApplicationInfo packageInfo : packages) {
                    if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        data.add(packageInfo);
                        Log.v(MainActivity.TAG, "SYSTEM_TYPE" + packageInfo.packageName);
                    }
                }
                break;
            case USER_TYPE:
                for (ApplicationInfo packageInfo : packages) {
                    if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        data.add(packageInfo);
                        Log.v(MainActivity.TAG, "USER_TYPE" + packageInfo.packageName);
                    }
                }
                break;
        }

        Log.d(MainActivity.TAG,"getApplicationInfoList:" + data.get(0).className);
        return data;
    }

    public static List<ApplicationInfo> getUserPackageApplicationInfoList(Context context){
        return  getApplicationInfoList(context,USER_TYPE);
    }

    public static ApplicationInfo getUserPackageApplicationInfoByClassName(Context context, String className) {
        List<ApplicationInfo> applicationInfos = getUserPackageApplicationInfoList(context);
        ApplicationInfo info = null;
        for (ApplicationInfo applicationInfo : applicationInfos) {
            if (applicationInfo.className.equals(className)) {
                info = applicationInfo;
            }
        }
        return info;
    }


}
