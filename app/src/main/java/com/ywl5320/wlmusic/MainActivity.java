package com.ywl5320.wlmusic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ywl5320.bean.TimeBean;
import com.ywl5320.libenum.MuteEnum;
import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnCompleteListener;
import com.ywl5320.listener.OnErrorListener;
import com.ywl5320.listener.OnInfoListener;
import com.ywl5320.listener.OnLoadListener;
import com.ywl5320.listener.OnParparedListener;
import com.ywl5320.listener.OnVolumeDBListener;
import com.ywl5320.util.WlTimeUtil;
import com.ywl5320.wlmusic.log.MyLog;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime;
    private TextView tvTime2;
    private WlMusic wlMusic;
    private SeekBar seekBar;
    private SeekBar seekBar2;
    private CheckBox checkBox;
    private int position = 0;
    private boolean isSeek = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTime = findViewById(R.id.tv_time);
        tvTime2 = findViewById(R.id.tv_time2);
        seekBar = findViewById(R.id.seek_bar);
        seekBar2 = findViewById(R.id.seek_bar2);
        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("ywl5320", isChecked + "");
                wlMusic.setPlayCircle(isChecked);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                position = wlMusic.getDuration() * progress / 100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyLog.d("position:" + position);
                wlMusic.seek(position);
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                wlMusic.setVolume(seekBar.getProgress());
                tvTime2.setText("音量：" + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        wlMusic = new WlMusic();
        wlMusic.setPlaySpeed(1500);
        wlMusic.setPlayCircle(true);
        wlMusic.setVolume(60);
        tvTime2.setText("音量：" + wlMusic.getVolume() + "%");
        seekBar2.setProgress(wlMusic.getVolume());
        checkBox.setChecked(wlMusic.isPlayCircle());


        wlMusic.setOnParparedListener(new OnParparedListener() {
            @Override
            public void onParpared() {
                MyLog.d("onparpared");
                wlMusic.start();
            }
        });

        wlMusic.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(int code, String msg) {
                MyLog.d("code :" + code + ", msg :" + msg);
            }
        });

        wlMusic.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(boolean load) {
                MyLog.d("load --> " + load);
            }
        });

        wlMusic.setOnInfoListener(new OnInfoListener() {
            @Override
            public void onInfo(TimeBean timeBean) {
                isSeek = false;
                MyLog.d("curr:" + timeBean.getCurrSecs() + ", total:" + timeBean.getTotalSecs());
                Message message = Message.obtain();
                message.obj = timeBean;
                message.what = 1;
                handler.sendMessage(message);
            }
        });

        wlMusic.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                MyLog.d("complete");
            }
        });

        wlMusic.setOnVolumeDBListener(new OnVolumeDBListener() {
            @Override
            public void onVolumeDB(int db) {
//                System.out.println("db is " + db);
//                Message message = Message.obtain();
//                message.obj = db;
//                message.what = 2;
//                handler.sendMessage(message);
            }
        });


    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    TimeBean timeBean = (TimeBean) msg.obj;
                    if(!isSeek)
                        seekBar.setProgress(timeBean.getCurrSecs() * 100 / timeBean.getTotalSecs());
                    tvTime.setText("时间：" + WlTimeUtil.secdsToDateFormat(timeBean.getCurrSecs(), timeBean.getTotalSecs()) + "/" + WlTimeUtil.secdsToDateFormat(timeBean.getTotalSecs(), timeBean.getTotalSecs()));
                    break;
                default:
                    break;
            }
        }
    };




    public void pause(View view) {
        wlMusic.pause();
    }

    public void resume(View view) {
        wlMusic.resume();
    }

    public void stop(View view) {
        wlMusic.stop();
    }

    public void start(View view) {
        wlMusic.setSource("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
        wlMusic.parpared();
    }

    public void change(View view) {
        wlMusic.playNext("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
    }

    public void fast(View view) {
        wlMusic.setPlaySpeed(1500);
    }

    public void slow(View view) {
        wlMusic.setPlaySpeed(500);
    }

    public void normal(View view) {
        wlMusic.setPlaySpeed(1000);
    }

    public void left(View view) {
        wlMusic.setMute(MuteEnum.MUTE_LEFT);
    }

    public void right(View view) {
        wlMusic.setMute(MuteEnum.MUTE_RIGHT);
    }

    public void center(View view) {
        wlMusic.setMute(MuteEnum.MUTE_CENTER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wlMusic.stop();
    }
}
