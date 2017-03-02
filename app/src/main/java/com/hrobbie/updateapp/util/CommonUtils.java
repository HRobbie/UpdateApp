package com.hrobbie.updateapp.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * user:HRobbie
 * Date:2017/3/2
 * Time:16:58
 * 邮箱：hwwyouxiang@163.com
 * Description:Page Function.
 */

public class CommonUtils {
    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        String versionCode="";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
//            versionCode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static void showToast(final Activity ctx, final String msg){

        if(ctx!=null){

            //如果是主线程，直接弹出toast
            if("main".equals(Thread.currentThread().getName())){
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            }else{
                //如果不是主线程，则调用context中 runOnUIThread方法弹出toast
                ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

    }


    public static ProgressDialog progressLoading=null;
    public static void showProgress(Context context,String tips){
        progressLoading=ProgressDialog.show(context,
                "", tips, true, false);
//        progressLoading.setCancelable(true);
        progressLoading.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    cancelProgress();
                }
                return false;
            }
        });
    }

    public static void cancelProgress(){
        if(progressLoading!=null&&progressLoading.isShowing()){
            progressLoading.dismiss();
        }
    }

    /**
     * 发起网络get请求
     * url:请求的url
     * requestCallBack：回调
     */
    public static <T> Callback.Cancelable Get(String url, Map<String,String> map, Callback.CommonCallback<T> callback){
        RequestParams params=new RequestParams(url);
        params.setConnectTimeout(60000);
        params.setMaxRetryCount(0);//只请求一次，必须设置超时时间

        if(null!=map){
            for(Map.Entry<String, String> entry : map.entrySet()){
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }
}
