package com.ywl5320.wlmusic.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;

import com.ywl5320.util.RawAssetsUtil;
import com.ywl5320.wlmusic.MyApplication;
import com.ywl5320.wlmusic.R;
import com.ywl5320.wlmusic.base.BaseActivity;
import com.ywl5320.wlmusic.dialog.NormalAskDialog;
import com.ywl5320.wlmusic.http.serviceapi.RadioApi;
import com.ywl5320.wlmusic.http.subscribers.HttpSubscriber;
import com.ywl5320.wlmusic.http.subscribers.SubscriberOnListener;
import com.ywl5320.wlmusic.log.MyLog;

import butterknife.BindView;

/**
 * Created by ywl on 2018/1/10.
 */

public class SplashActivity extends BaseActivity{

    @BindView(R.id.pb_load)
    ProgressBar pbLoad;
    private static final int REQUEST_SDCARD_CODE = 0x0002;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if ( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED//读取存储卡权限
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SDCARD_CODE);
            }
            else
            {
                getToken();
            }
        }
        else
        {
            getToken();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SDCARD_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getToken();
            }
            else
            {
                NormalAskDialog normalAskDialog = new NormalAskDialog(this);
                normalAskDialog.show();
                normalAskDialog.setData("请同意读取存储卡权限", "退出", "好的", false);
                normalAskDialog.setOnActionListener(new NormalAskDialog.OnActionListener() {
                    @Override
                    public void onLeftAction() {
                        SplashActivity.this.finish();
                    }

                    @Override
                    public void onRightAction() {
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SDCARD_CODE);
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void getToken()
    {
        pbLoad.setVisibility(View.VISIBLE);
        RadioApi.getInstance().getToken(new HttpSubscriber<Long>(new SubscriberOnListener<Long>() {
            @Override
            public void onSucceed(Long token) {
                MyApplication.getInstance().setToken(token);
                toHomeActivity();
            }

            @Override
            public void onError(int code, String msg) {
                toHomeActivity();
            }
        }, this));
    }

    private void toHomeActivity()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(SplashActivity.this, HomeActivity.class);
                SplashActivity.this.finish();
            }
        }, 1000);
    }


}
