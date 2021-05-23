package com.example.versionupdate;
//downLoad Manger
//1.  <uses-permission android:name="android.permission.INTERNET"/>
//    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.versionupdate.download.DownLoadApkActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = "hank";
    private String name;
    private PackageManagerUtils packageManagerUtils = new PackageManagerUtils();
    private FloatingActionButton floatingActionButton;
    public final static int WRITE_EXTERNAL_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getLog();
        floatingActionButton = findViewById(R.id.floBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {

                    downLoadFile("test","https://www.gdaily.org/22200/netflix-apk");

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_PERMISSION);
                    }
                }

            }
        });
    }


        if (ContextCompat.checkSelfPermission(
    MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
    PackageManager.PERMISSION_GRANTED) {

        downLoadFile("test","https://www.gdaily.org/22200/netflix-apk");

    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == WRITE_EXTERNAL_PERMISSION){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                downLoadFile("test","https://www.gdaily.org/22200/netflix-apk");
            }else {
                Toast.makeText(MainActivity.this,"權限失敗:" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void downLoadFile(String file, String url) {
        Uri uri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        try {
            if(downloadManager != null){
                DownloadManager.Request request = new DownloadManager.Request(uri);
                /**設定用於下載時的網路狀態*/
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle("下載apk");
                request.setAllowedOverMetered(true);
                /**設定漫遊狀態下是否可以下載*/
                request.setAllowedOverRoaming(true);
                request.setDescription("下載" + file +"中...");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,file);
//                request.setMimeType(gerMimeType(uri));
                request.setMimeType("application/vnd.android.package-archive");
                downloadManager.enqueue(request);
                Toast.makeText(MainActivity.this,"下載成功:",Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }catch (Exception e){
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();

        }
    }

    private String gerMimeType(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));
    }

    private void getLog() {
        //        name = AppUtils.getPackageName(this);
        Log.d(TAG, "appName:" + name);
        Log.d(TAG, "getAllApp:" + packageManagerUtils.getAllApp(this));
        Log.d(TAG, "isInstalled:" + packageManagerUtils.isInstalled(this, "com.hulu.plus"));

        PackageInfo huluPackageInfo = packageManagerUtils.getPackageInfo(this, "com.netflix.mediaclient");
        String huluVersionName = huluPackageInfo.versionName;
        int huluVersionCode = huluPackageInfo.versionCode;
        String huluPackageName = huluPackageInfo.packageName;
        long huluFirstInstallTime = huluPackageInfo.firstInstallTime;
        long huluLastUpdateTime = huluPackageInfo.lastUpdateTime;
        String huluSharedUserId = huluPackageInfo.sharedUserId;

        Log.d(TAG,
                "huluVersionName:" + huluVersionName + "\n"
                        + "huluVersionCode:" + huluVersionCode + "\n"
                        + "huluPackageName:" + huluPackageName + "\n"
                        + "huluFirstInstallTime:" + huluFirstInstallTime + "\n"
                        + "huluLastUpdateTime:" + huluLastUpdateTime + "\n"
                        + "huluSharedUserId:" + huluSharedUserId + "\n"
        );

//        https://www.gdaily.org/22200/netflix-apk
//        Log.d(TAG,PackageUtils.getAllPackageInfo(this));
    }

    public void goToDownLoadPage(View view) {
        startActivity(new Intent(MainActivity.this, DownLoadApkActivity.class));
    }
}