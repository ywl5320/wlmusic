package com.ywl5320.wlmusic;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnCompleteListener;
import com.ywl5320.listener.OnErrorListener;
import com.ywl5320.listener.OnLoadListener;
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.listener.OnShowPcmDataListener;
import com.ywl5320.wlmusic.base.BaseActivity;
import com.ywl5320.wlmusic.log.MyLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShowPcmdataActivity extends BaseActivity {

    private TextView tvInfo;

    private WlMusic wlMusic;

    private FileOutputStream fos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpcmdata_layout);
        tvInfo = findViewById(R.id.tv_info);
        wlMusic = WlMusic.getInstance();
        wlMusic.setShowPcmData(true, true);
        wlMusic.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                wlMusic.startShowPcmData(150, 200);
            }
        });

        try {
            File pcmfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "pcmdata.pcm");
            fos = new FileOutputStream(pcmfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        wlMusic.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(boolean load) {
                if(load)
                {
                    MyLog.d("加载中");
                }
            }
        });

        wlMusic.setOnShowPcmDataListener(new OnShowPcmDataListener() {
            @Override
            public void onPcmInfo(int samplerate, int bit, int channels) {
                MyLog.d(samplerate + "---" + bit + "---" + channels);
            }

            @Override
            public void onPcmData(byte[] pcmdata, int size, int clock) {
                MyLog.d("size is: " + size + ",clock is: " + clock);
                Message message = Message.obtain();
                message.obj = "进度：" + ((clock - 150) * 100 / 50) + "%";
                handler.sendMessage(message);
                if(fos != null)
                {
                    try {
                        fos.write(pcmdata, 0, size);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        wlMusic.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                MyLog.d("complete");
                Message message = Message.obtain();
                message.obj = "进度：完成";
                handler.sendMessage(message);
                if(fos != null)
                {
                    try {
                        fos.flush();
                        fos.close();
                        fos = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        if(fos != null)
                        {
                            try {
                                fos.close();
                                fos = null;
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }

                    }
                }
            }
        });

        wlMusic.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(int code, String msg) {
                MyLog.d(msg);
                Message message = Message.obtain();
                message.obj = "出错了：" + msg;
                handler.sendMessage(message);
            }
        });

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvInfo.setText((String)msg.obj);
        }
    };

    public void showpcmdata(View view) {

        tvInfo.setText("加载中...");
//        wlMusic.playNext("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
        wlMusic.playNext("/mnt/shared/Other/mydream.m4a");


    }
}
