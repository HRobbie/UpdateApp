package com.hrobbie.updateapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.hrobbie.updateapp.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 作者：Hrobbie on 2017/3/2 19:11
 * 邮箱：hwwyouxiang159@sina.com
 */
public class DownLoadService extends Service {
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
    private String path=BASE_PATH+"hrobbie"+"/hrobbie.apk";
    private Notification.Builder builder;
    private NotificationManager notificationManager;
    private int number;
    private Notification build;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new Notification.Builder(DownLoadService.this);
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        number = startId;
        String download_url = intent.getStringExtra("download_url");
//        downloadFile(download_url);
        Log.e("TAG", "DownloadService download_url="+download_url);
        Log.e("TAG", "DownloadService path="+path);
        builder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle("一款应用正在下载0%").setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX).setTicker("一款应用下载").setOngoing(false).setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0));


        downloadFile(download_url);



        return super.onStartCommand(intent, flags, startId);

    }

    private void downloadFile(String download_url) {
        RequestParams requestParams = new RequestParams(download_url);
        requestParams.setAutoResume(true);
//        requestParams.setAutoRename(true);//断点下载

        requestParams.setSaveFilePath(path);


        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                builder.setContentTitle("一款应用正在下载"+(int)((double)current/total*100)+"%").setProgress((int)total,(int)current,false);
               send();

            }

            @Override
            public void onSuccess(File result) {
                builder.setContentTitle("一款应用下载完成100%").setProgress(100, 100, false);
                send();
                openFile(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DownLoadService.this,"下载失败，请检查网络和SD卡",Toast.LENGTH_SHORT).show();
//                CommonUtil.showToast(DownLoadService.this,"下载失败，请检查网络和SD卡");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    //打开APK程序代码

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void send(){
        build = builder.build();
        notificationManager.notify(number, build);
    }
}
