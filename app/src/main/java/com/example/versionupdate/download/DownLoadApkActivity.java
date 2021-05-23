package com.example.versionupdate.download;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.versionupdate.R;

import java.io.File;

public class DownLoadApkActivity extends AppCompatActivity implements View.OnClickListener {
    MyReceiver receiver = new MyReceiver();
    private Button myselfButton;
    private String url = "https://www.gdaily.org/22200/netflix-apk";
    private DownloadDialog downloadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_apk);


        registerReceiver();
        initView();

//        Intent serviceIntent = new Intent(this, DownloadService.class);
////将下载地址url放入intent中
//        serviceIntent.setData(Uri.parse(url));
//        startService(serviceIntent);
    }


    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(DownloadService.BROADCAST_ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);

    }

    @Override
    public void onClick(View v) {
//        Intent serviceIntent = new Intent(DownLoadApkActivity.this, DownloadService.class);
//        serviceIntent.setData(Uri.parse(url));
//        startService(serviceIntent);
        download();
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra(DownloadService.EXTENDED_DATA_STATUS);
            Log.i("test", data);

            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Toast.makeText(DownLoadApkActivity.this, "编号：" + id + "的下载任务已经完成！", Toast.LENGTH_SHORT).show();
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/myApp.apk")),
                    "application/vnd.android.package-archive");
            startActivity(intent);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        canceledDialog();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void initView() {

        myselfButton = (Button) this.findViewById(R.id.myself_update);

        myselfButton.setOnClickListener(this);
    }





    private void download() {
        showDialog();
        //最好是用单线程池，或者intentService取代
        new Thread(new DownLoadRunnable(this,url, handler)).start();
    }



    private void showDialog() {
        if(downloadDialog==null){
            downloadDialog = new DownloadDialog(this);
        }

        if(!downloadDialog.isShowing()){
            downloadDialog.show();
        }
    }

    private void canceledDialog() {
        if(downloadDialog!=null&&downloadDialog.isShowing()){
            downloadDialog.dismiss();
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    downloadDialog.setProgress(100);
                    canceledDialog();
                    Toast.makeText(DownLoadApkActivity.this, "下载任务已经完成！", Toast.LENGTH_SHORT).show();
                    break;

                case DownloadManager.STATUS_RUNNING:
                    //int progress = (int) msg.obj;
                    downloadDialog.setProgress((int) msg.obj);
                    //canceledDialog();
                    break;

                case DownloadManager.STATUS_FAILED:
                    canceledDialog();
                    break;

                case DownloadManager.STATUS_PENDING:
                    showDialog();
                    break;
            }
        }
    };
}