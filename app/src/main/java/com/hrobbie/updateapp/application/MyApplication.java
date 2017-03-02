package com.hrobbie.updateapp.application;

import android.app.Application;

import com.hrobbie.updateapp.util.CUtils;

import org.xutils.x;

/**
 * user:HRobbie
 * Date:2017/3/2
 * Time:16:18
 * 邮箱：hwwyouxiang@163.com
 * Description:Page Function.
 */

public class MyApplication extends Application {
    //是否取消过更新软件
    public static boolean CANCEL_UPLOAD=false;//没有取消过
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化XUtils3
        x.Ext.init(this);

        CUtils.init(this);
    }
}
