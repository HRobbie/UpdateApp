package com.hrobbie.updateapp.application;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * user:hrobbie
 * Date:2016/5/6
 * Time:0:38
 * Description:Page Function.
 */
public class ExitAppliation {
    //运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static ExitAppliation instance;
    //构造方法
    private ExitAppliation(){}
    //实例化一次
    public synchronized static ExitAppliation getInstance(){
        if (null == instance) {
            instance = new ExitAppliation();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            System.exit(0);
        }
    }

    public void exit(Activity activity1){

        try {
            for (Activity activity:mList) {
                if (activity != null&&!(activity.getClass().getName().equals(activity1.getClass().getName())))
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            System.exit(0);
        }
    }

}
