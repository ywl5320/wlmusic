package com.ywl5320.wlmusic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ywl5320.bean.TimeBean;
import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnErrorListener;
import com.ywl5320.listener.OnInfoListener;
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.listener.OnRecordListener;
import com.ywl5320.wlmusic.log.MyLog;

/**
 * Created by ywl5320 on 2018/5/4.
 */

public class CutAudioActivity extends AppCompatActivity {

    private WlMusic wlMusic;
    private int type = 0;

    private EditText etStart;
    private EditText etEnd;
    private TextView tvTotaltime;
    private TextView tvCutStatus;

    private int start = 0;
    private int end = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutaudio_layout);
        etStart = findViewById(R.id.et_start);
        etEnd = findViewById(R.id.et_end);
        tvTotaltime = findViewById(R.id.tv_totaltime);
        tvCutStatus = findViewById(R.id.tv_status);
        wlMusic = WlMusic.getInstance();
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
            }
        });
        wlMusic.setOnInfoListener(new OnInfoListener() {
            @Override
            public void onInfo(TimeBean timeBean) {
                Message message = Message.obtain();
                message.what = 3;
                message.obj = timeBean;
                handler.sendMessage(message);
            }
        });
        wlMusic.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(int code, String msg) {
                MyLog.d(code + " --> " + msg);
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
        type = 3;
        wlMusic.setSource("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
        wlMusic.prePared();


    }

    public void cutAudio(View view) {
        type = 1;
        start = Integer.parseInt(etStart.getText().toString().toLowerCase().replace("s", ""));
        end = Integer.parseInt(etEnd.getText().toString().toLowerCase().replace("s", ""));
        wlMusic.playNext("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
    }

    public void playCutAudio(View view) {
        type = 2;
        start = Integer.parseInt(etStart.getText().toString().toLowerCase().replace("s", ""));
        end = Integer.parseInt(etEnd.getText().toString().toLowerCase().replace("s", ""));
        wlMusic.playNext("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
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
                    tvCutStatus.setText("剪切片段时间：" + msg.obj + "s");
                    break;
                case 2:
                    tvCutStatus.setText(tvCutStatus.getText().toString() + " 裁剪完成！");
                    break;
                case 3:
                    TimeBean timeBean = (TimeBean) msg.obj;
                    tvCutStatus.setText("裁剪预览播放时间：" + (timeBean.getCurrSecs() - start) + "s");
                    break;
                case 4:
                    tvTotaltime.setText("总时长：" + wlMusic.getDuration() + "s");
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wlMusic.stop();
    }
}
