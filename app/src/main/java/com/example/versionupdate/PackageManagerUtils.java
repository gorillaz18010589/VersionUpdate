package com.example.versionupdate;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.List;

public class PackageManagerUtils {

    public PackageManagerUtils() {
    }

    /**
     * 获取当前设备上安装的所有App
     */
    public List<PackageInfo> getAllApp(Context context){
        PackageManager pm = context.getPackageManager();
        List<PackageInfo>  packageInfos = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        return packageInfos;
    }

    /**
     * 判断 App 是否安装
     */
    public boolean isInstalled (Context context,String packageName){
        if(packageName == null || packageName.length() < 1){
            return false;
        }
        PackageManager pm = context.getPackageManager();
        try{
            PackageInfo packageInfo = pm.getPackageInfo(packageName,PackageManager.GET_ACTIVITIES);
            return packageInfo != null;
        }catch(Throwable ignore){

        }

        return false;
    }

    /**
     * 根据包名获取 PackageInfo
     */
    public PackageInfo getPackageInfo (Context context,String packageName){
        if(packageName == null || packageName.length() < 1){
            return null;
        }
        PackageManager pm = context.getPackageManager();
        try{
            PackageInfo packageInfo = pm.getPackageInfo(packageName,PackageManager.GET_ACTIVITIES);
            return packageInfo ;
        }catch(Throwable ignore){

        }

        return null;
    }

    /**
     * 根据包名获取 版本号
     */
    public int getPackageVersionCode (Context context,String packageName){
        PackageInfo packageInfo = getPackageInfo (context, packageName);
        if(packageName != null){
            return packageInfo.versionCode;
        }

        return-1;
    }

    /**
     * 根据包名获取 版本名
     */
    public String getPackageVersionName (Context context,String packageName){
        PackageInfo packageInfo = getPackageInfo (context, packageName);
        if(packageName != null){
            return packageInfo.versionName;
        }

        return null;
    }

    /**
     * 获取 App名
     */
    public String getApplicationLabel (Context context,String packageName){
        if(packageName == null || packageName.length() < 1){
            return null;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = getPackageInfo (context, packageName);
        if(packageInfo != null){
            return packageInfo.applicationInfo.loadLabel(pm).toString();
        }
        return null;
    }

    /**
     * 获取 App的 icon
     */
    public Drawable getApplicationIcon (Context context, String packageName){
        if(packageName == null || packageName.length()< 1){
            return null;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = getPackageInfo (context, packageName);
        if(packageInfo != null){
            return packageInfo.applicationInfo.loadIcon(pm);
        }
        return null;
    }

    /**
     * 通过Apk路径，获取Apk信息
     */
    public PackageInfo getPackageArchiveInfo (Context context, String apkPath){
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
            return packageInfo;
        }catch(Throwable ignore){
            return null;
        }
    }
}
