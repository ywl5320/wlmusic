package com.ywl5320.wlmusic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ywl5320.bean.TimeBean;
import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnCompleteListener;
import com.ywl5320.listener.OnErrorListener;
import com.ywl5320.listener.OnInfoListener;
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.listener.OnRecordListener;
import com.ywl5320.listener.OnShowPcmDataListener;
import com.ywl5320.wlmusic.log.MyLog;

import java.sql.Time;

/**
 * Created by hlwky001 on 2018/5/4.
 */

public class CutAudioActivity extends AppCompatActivity{

    private WlMusic wlMusic;
    private int type = 0;

    private EditText etStart;
    private EditText etEnd;
    private TextView tvTotaltime;
    private TextView tvCutStatus;

    private int start = 0;
    private int end = 0;

    private String source;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutaudio_layout);
        etStart = findViewById(R.id.et_start);
        etEnd = findViewById(R.id.et_end);
        tvTotaltime = findViewById(R.id.tv_totaltime);
        tvCutStatus = findViewById(R.id.tv_status);

        source = "/mnt/shared/Other/林俊杰 - 爱不会绝迹.wav";

        wlMusic = WlMusic.getInstance();
        wlMusic.setCallBackPcmData(true);
        wlMusic.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                if(type == 1)
                {
                    wlMusic.cutAudio(start, end, "/mnt/shared/Other", "cut.aac");
                }
                else if(type == 2)
                {
                    wlMusic.playCutAudio(start, end);
                }
                else if(type == 3)
                {
                    Message message = Message.obtain();
                    message.what = 4;
                    handler.sendMessage(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wlMusic.stop();
                        }
                    });
                }
                else if(type == 4)
                {
                    wlMusic.cutAudio(start, end);
                }
            }
        });
        wlMusic.setOnInfoListener(new OnInfoListener() {
            @Override
            public void onInfo(TimeBean timeBean) {
                Message message = Message.obtain();
                message.what = 3;
                message.obj = timeBean;
                handler.sendMessage(message);
                MyLog.d(timeBean.toString());
            }
        });
        wlMusic.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(int code, final String msg) {
                MyLog.d(code + " --> " + msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CutAudioActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        wlMusic.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onRecordTime(int scds) {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = scds;
                handler.sendMessage(message);
            }

            @Override
            public void onRecordComplete() {
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);
            }

            @Override
            public void onRecordPauseResume(boolean pause) {

            }
        });

        wlMusic.setOnShowPcmDataListener(new OnShowPcmDataListener() {
            @Override
            public void onPcmInfo(int samplerate, int bit, int channels) {
                MyLog.d(samplerate + "---" + bit + "---" + channels);
            }

            @Override
            public void onPcmData(byte[] pcmdata, int size, long clock) {
                MyLog.d("pcm data size is: " + size + " time is : " + clock);
                if(type == 4)
                {
                    Message message = Message.obtain();
                    message.what = 5;
                    message.obj = clock / 1000000;
                    handler.sendMessage(message);
                }
            }
        });

        type = 3;
        wlMusic.setSource(source);
        wlMusic.prePared();


    }

    public void cutAudio(View view) {
        type = 1;
        start = Integer.parseInt(etStart.getText().toString().toLowerCase().replace("s", ""));
        end = Integer.parseInt(etEnd.getText().toString().toLowerCase().replace("s", ""));
        wlMusic.playNext(source);
    }

    public void playCutAudio(View view) {
        type = 2;
        start = Integer.parseInt(etStart.getText().toString().toLowerCase().replace("s", ""));
        end = Integer.parseInt(etEnd.getText().toString().toLowerCase().replace("s", ""));
        wlMusic.playNext(source);
    }

    public void stop(View view)
    {
        wlMusic.stop();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    tvCutStatus.setText("剪切片段录制时间：" + msg.obj + "s");
                    break;
                case 2:
                    tvCutStatus.setText(tvCutStatus.getText().toString() + " 裁剪完成！");
                    break;
                case 3:
                    TimeBean timeBean = (TimeBean) msg.obj;
                    if(timeBean.getCurrSecs() != timeBean.getTotalSecs())
                    {
                        tvCutStatus.setText("裁剪预览播放时间：" + (timeBean.getCurrSecs() - start) + "s");
                    }
                    break;
                case 4:
                    tvTotaltime.setText("总时长：" + wlMusic.getDuration() + "s  (林俊杰 - 爱不会绝迹.wav)");
                    break;
                case 5:
                    tvCutStatus.setText("快速裁剪回调PCM时间：" + msg.obj + "s");
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wlMusic.stop();
    }

    public void cutAudioNorecord(View view) {
        type = 4;
        start = Integer.parseInt(etStart.getText().toString().toLowerCase().replace("s", ""));
        end = Integer.parseInt(etEnd.getText().toString().toLowerCase().replace("s", ""));
        wlMusic.playNext(source);
    }
}
