package com.hrobbie.updateapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hrobbie.updateapp.R;
import com.hrobbie.updateapp.application.ExitAppliation;
import com.hrobbie.updateapp.application.MyApplication;
import com.hrobbie.updateapp.bean.UpdateInfoBean;
import com.hrobbie.updateapp.service.DownLoadService;
import com.hrobbie.updateapp.util.CUtils;
import com.hrobbie.updateapp.util.CacheUtils;
import com.hrobbie.updateapp.util.CommonUtils;
import com.hrobbie.updateapp.util.NetUrl;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_version)
    private TextView tv_version;
    @ViewInject(R.id.tv_size)
    private TextView tv_size;
    @ViewInject(R.id.tv_content)
    private TextView tv_content;

    @ViewInject(R.id.btn_cancel)
    private Button btn_cancel;

    @ViewInject(R.id.btn_confirm)
    private Button btn_confirm;
    private UpdateInfoBean.DataBean data;
    private ImageOptions imageOptions;

    @ViewInject(R.id.iv_icon)
    private ImageView iv_icon;

    @ViewInject(R.id.pb_download)
    private ProgressBar pb_download;
    private int force;//0:不强制升级，1：强制升级
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
    private String path=BASE_PATH+"hrobbie"+"/hrobbie.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        x.view().inject(this);

        initData();
    }

    private void initData() {

        //图片大小
        //ImageView圆角半径
        // 如果ImageView的大小不是定义为wrap_content, 不要crop.
        //加载中默认显示图片
        //设置使用缓存
        //加载失败后默认显示图片
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(50), DensityUtil.dip2px(50))//图片大小
                .setRadius(DensityUtil.dip2px(0))//ImageView圆角半径
                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)//加载中默认显示图片
                .setUseMemCache(true)//设置使用缓存
                .setFailureDrawableId(R.mipmap.ic_launcher)//加载失败后默认显示图片
                .build();

        String result = CacheUtils.getInstance(this).getValue(NetUrl.UPDATE_URL, "");
        if(result.contains("data")){
            UpdateInfoBean updateInfoBean = CUtils.getGson().fromJson(result, UpdateInfoBean.class);
            data = updateInfoBean.getData();
            String replace;
            if(data.getInformation().contains("\\n")){

                replace = data.getInformation().replace("\\n", "\n");
            }else{
                replace=data.getInformation();
            }
            tv_content.setText(replace);
            tv_size.setText(data.getSize()+"M");
            tv_version.setText("v"+ data.getVersion());

            x.image().bind(iv_icon,data.getImg(),imageOptions);


        }

        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        pb_download.setMax(100);
        force = data.getForce();
        pb_download.setVisibility(View.GONE);
        if(force==0){
//            pb_download.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.VISIBLE);
        }else{
//            pb_download.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                MyApplication.CANCEL_UPLOAD=true;
                finish();
                break;
            case R.id.btn_confirm:
                if(force==0){
                    checkStorePromission();
                }else{
                    pb_download.setVisibility(View.VISIBLE);
                    downloadFile(data.getUrl());
                }
                break;
        }
    }


    private void checkStorePromission() {
//        CommonUtil.showToast(this,"开始下载应用...");
        Toast.makeText(this,"开始下载应用...",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,DownLoadService.class);
        intent.putExtra("download_url",data.getUrl());
        startService(intent);
        finish();
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
                pb_download.setProgress((int)((double)current/total*100));

            }

            @Override
            public void onSuccess(File result) {
                openFile(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DownloadActivity.this,"下载失败，请检查网络和SD卡",Toast.LENGTH_SHORT).show();

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
        finish();
    }
    private long curMillios = 0;
    private boolean showExitToast(){
        long time = System.currentTimeMillis();
        if (time - curMillios < 2000){
            return true;
        }

        curMillios = time;
        return false;
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
//        super.onBackPressed();

        if(force==0){
            finish();
        }else{

            if(showExitToast()){
                /**
                 * 关闭整个程序
                 */
                ExitAppliation.getInstance().exit();
//
//                finish();
                System.exit(0);
            }else{
                CommonUtils.showToast(this,"再点击一次退出！");
            }


        }

        CommonUtils.cancelProgress();

    }

}

