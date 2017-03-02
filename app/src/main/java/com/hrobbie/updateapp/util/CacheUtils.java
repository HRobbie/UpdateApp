package com.hrobbie.updateapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.xutils.common.util.MD5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 作者：黄伟伟 on 2016/2/29
 * 作用：
 */
public class CacheUtils {
    private static File mCacheDir;

    static {
        mCacheDir = CUtils.getApplication().getExternalCacheDir();
        if (mCacheDir == null) {
            mCacheDir = CUtils.getApplication().getCacheDir();
        }

    }

    private static CacheUtils instance;
    private static SharedPreferences sp;

    private CacheUtils() {
    }

    /**
     * 得到sp
     * @param context
     * @return
     */
    public static CacheUtils getInstance(Context context) {
        if (instance == null) {
            instance = new CacheUtils();
            sp = context.getSharedPreferences("towatt", Context.MODE_PRIVATE);
        }
        return instance;
    }

    /**
     * 存储到sp中
     * @param key
     * @param values
     */
    public void putValue(String key, Object values) {
        if (values instanceof Integer) {
            sp.edit().putInt(key, (Integer) values).commit();
        } else if (values instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) values).commit();
        } else if (values instanceof String) {
            sp.edit().putString(key, (String) values).commit();

        }
    }



    /**
     * 得到缓存的数据
     * @param key
     * @param defValues
     * @param <T>
     * @return
     */
    public <T> T getValue(String key, Object defValues) {
        T t = null;
        if (defValues instanceof Integer) {
            Integer value = sp.getInt(key, (Integer) defValues);
            t = (T) value;
        } else if (defValues instanceof Boolean) {
            Boolean value = sp.getBoolean(key, (Boolean) defValues);
            t = (T) value;
        } else if (defValues instanceof String) {
            t = (T) sp.getString(key, (String) defValues);
        }

        return t;
    }

    /**
     * 清除sp存储
     */
    public void clearSp(){
        sp.edit().clear().commit();
    }
    /**
     * 保存数据到文件中
     * @param url
     * @param content
     * @return
     */
    public static boolean saveTextToFile(String url, String content) {
        if (url == null) return false;
        File cacheFile = new File(mCacheDir, MD5.md5(url));
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(cacheFile);
            fos.write(content.getBytes());
            return true;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;


    }

    /**
     * 从缓存中读取数据
     * @param url
     * @return
     */
    public static String readTextFile(String url) {
        if (url == null) return "";
        File cacheFile = new File(mCacheDir, MD5.md5(url));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (cacheFile.exists()) {
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(cacheFile);

                byte[] buffer = new byte[4096];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != fis) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            return out.toString();
        }
        return "";
    }
}
