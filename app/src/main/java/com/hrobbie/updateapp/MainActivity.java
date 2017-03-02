package com.hrobbie.updateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hrobbie.updateapp.activity.DownloadActivity;
import com.hrobbie.updateapp.application.ExitAppliation;
import com.hrobbie.updateapp.bean.UpdateInfoBean;
import com.hrobbie.updateapp.util.CUtils;
import com.hrobbie.updateapp.util.CacheUtils;
import com.hrobbie.updateapp.util.CommonUtils;
import com.hrobbie.updateapp.util.NetUrl;
import com.hrobbie.updateapp.util.inteface.MyCallBack;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUpdateInfo();
            }
        });

        ExitAppliation.getInstance().addActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 联网获取更新数据
     */
    private void getUpdateInfo(){
        Log.e("TAG", "FolderActivity getUpdateInfo");
        Map<String,String> map=new HashMap<>();
        map.put("flag","1");
        CommonUtils.showProgress(this,"正在检查更新，请稍等...");
        CommonUtils.Get(NetUrl.UPDATE_URL, map, new MyCallBack<String>() {

            @Override
            public void onSuccess(String result) {
//                if(result.contains("data")){
                CacheUtils.getInstance(MainActivity.this).getValue(NetUrl.UPDATE_URL, result);
                CommonUtils.cancelProgress();
                CacheUtils.getInstance(MainActivity.this).putValue(NetUrl.UPDATE_URL,result);
                parseUpdateData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.cancelProgress();
                CommonUtils.showToast(MainActivity.this,getResources().getString(R.string.get_error_text));
            }

        });

    }

    /**
     * 解析更新数据
     * @param result
     */
    private void parseUpdateData(String result) {
        UpdateInfoBean updateInfoBean = CUtils.getGson().fromJson(result, UpdateInfoBean.class);
        UpdateInfoBean.DataBean data = updateInfoBean.getData();
        double versionName = Double.parseDouble(CommonUtils.getAppVersionName(this));
        Log.e("TAG", ""+versionName+":"+data.getVersion());
        if(versionName<data.getVersion()){
            showUpdateDialog();
        }else{
            CommonUtils.showToast(this,"当前已经是最新版本！");
        }

    }

    /**
     * 显示更新的对话框
     */
    private void showUpdateDialog() {
        Intent intent = new Intent(this, DownloadActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        CommonUtils.cancelProgress();
    }
}
