package com.ywl5320.wlmusic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.listener.OnVolumeDBListener;
import com.ywl5320.util.WlTimeUtil;
import com.ywl5320.wlmusic.log.MyLog;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime;
    private TextView tvTime2;
    private TextView tvStyle;
    private WlMusic myMusic;
    private SeekBar seekBar;
    private SeekBar seekBar2;
    private CheckBox checkBox;
    private int position = 0;

    private String spStyle = "正常播放1.0x";
    private String muStyle = "立体声";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTime = findViewById(R.id.tv_time);
        tvTime2 = findViewById(R.id.tv_time2);
        seekBar = findViewById(R.id.seek_bar);
        seekBar2 = findViewById(R.id.seek_bar2);
        checkBox = findViewById(R.id.checkbox);
        tvStyle = findViewById(R.id.tv_style);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myMusic.setPlayCircle(isChecked);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                myMusic.setSeeking(true);
                position = myMusic.getDuration() * progress / 100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                myMusic.seek(position, false, false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyLog.d("position:" + position);
                myMusic.seek(position, true, true);
//                myMusic.setSeeking(false);
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                myMusic.setVolume(seekBar.getProgress());
                tvTime2.setText("音量：" + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        myMusic = WlMusic.getInstance();
        myMusic.setPlaySpeed(1);
        myMusic.setPlayCircle(true);
        myMusic.setVolume(65);
        myMusic.setPlaySpeed(1.0f);
        myMusic.setPlayPitch(1.0f);
        tvTime2.setText("音量：" + myMusic.getVolume() + "%");
        seekBar2.setProgress(myMusic.getVolume());
        checkBox.setChecked(myMusic.isPlayCircle());
        setStyle();

        myMusic.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                MyLog.d("onparpared");
                myMusic.start();
            }
        });

        myMusic.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(int code, String msg) {
                MyLog.d("code :" + code + ", msg :" + msg);
            }
        });

        myMusic.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(boolean load) {
                MyLog.d("load --> " + load);
            }
        });

        myMusic.setOnInfoListener(new OnInfoListener() {
            @Override
            public void onInfo(TimeBean timeBean) {
                MyLog.d("curr:" + timeBean.getCurrSecs() + ", total:" + timeBean.getTotalSecs());
                Message message = Message.obtain();
                message.obj = timeBean;
                message.what = 1;
                handler.sendMessage(message);
            }
        });

        myMusic.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                MyLog.d("complete");
            }
        });

        myMusic.setOnVolumeDBListener(new OnVolumeDBListener() {
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
                    seekBar.setProgress(timeBean.getCurrSecs() * 100 / timeBean.getTotalSecs());
                    tvTime.setText("时间：" + WlTimeUtil.secdsToDateFormat(timeBean.getCurrSecs(), timeBean.getTotalSecs()) + "/" + WlTimeUtil.secdsToDateFormat(timeBean.getTotalSecs(), timeBean.getTotalSecs()));
                    break;
                case 2:
                    int db = (int) msg.obj;
                    break;
                default:
                    break;
            }
        }
    };




    public void pause(View view) {
        myMusic.pause();
    }

    public void resume(View view) {
        myMusic.resume();
    }

    public void stop(View view) {
        myMusic.stop();
    }

    public void start(View view) {
        myMusic.setSource("http://ngcdn001.cnr.cn/live/zgzs/index.m3u8");
        myMusic.prePared();
    }

    public void change(View view) {
        myMusic.playNext("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
//        myMusic.playNext("/mnt/shared/Other/zzlfz.ape");
    }

    public void fast(View view) {
        myMusic.setPlaySpeed(1.5f);
        myMusic.setPlayPitch(1.0f);
        spStyle = "变速不变调1.5x";
        setStyle();
    }

    public void slow(View view) {
        myMusic.setPlaySpeed(0.5f);
        myMusic.setPlayPitch(1.0f);
        spStyle = "变速不变调0.5x";
        setStyle();
    }

    public void normal(View view) {
        myMusic.setPlaySpeed(1f);
        myMusic.setPlayPitch(1.0f);
        spStyle = "变速不变调1.0x";
        setStyle();
    }

    public void left(View view) {
        myMusic.setMute(MuteEnum.MUTE_LEFT);
        muStyle = "左声道";
        setStyle();
    }

    public void right(View view) {
        myMusic.setMute(MuteEnum.MUTE_RIGHT);
        muStyle = "右声道";
        setStyle();
    }

    public void center(View view) {
        myMusic.setMute(MuteEnum.MUTE_CENTER);
        muStyle = "立体声";
        setStyle();
    }

    public void fpitch(View view) {
        myMusic.setPlayPitch(1.5f);
        myMusic.setPlaySpeed(1.0f);
        spStyle = "变调不变速1.5x";
        setStyle();
    }

    public void spitch(View view) {
        myMusic.setPlayPitch(0.5f);
        myMusic.setPlaySpeed(1.0f);
        spStyle = "变调不变速0.5x";
        setStyle();
    }

    public void npitch(View view) {
        myMusic.setPlayPitch(1f);
        myMusic.setPlaySpeed(1.0f);
        spStyle = "变调不变速1.0x";
        setStyle();
    }

    public void sffast(View view) {
        myMusic.setPlaySpeed(1.5f);
        myMusic.setPlayPitch(1.5f);
        spStyle = "变调又变速1.5x";
        setStyle();
    }

    public void sfslow(View view) {
        myMusic.setPlaySpeed(0.5f);
        myMusic.setPlayPitch(0.5f);
        spStyle = "变调又变速0.5x";
        setStyle();
    }

    public void sfnormal(View view) {
        myMusic.setPlayPitch(1);
        myMusic.setPlaySpeed(1);
        spStyle = "变调又变速1.0x";
        setStyle();
    }

    private void setStyle()
    {
        tvStyle.setText(spStyle + " -- " + muStyle);
    }
}
