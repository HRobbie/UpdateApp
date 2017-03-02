package com.hrobbie.updateapp.util;

import android.app.Application;

import com.google.gson.Gson;


public class CUtils {
    private static Application mApplication;
    private static Gson mGson;

    //
    public static void init(Application application){
        mApplication=application;
        mGson=new Gson();
    }


    public static Application getApplication() {
        return mApplication;
    }
    public static Gson getGson() {
        return mGson;
    }
}
