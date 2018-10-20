package com.ywl5320.wlmusic;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ywl5320.bean.TimeBean;
import com.ywl5320.libenum.MuteEnum;
import com.ywl5320.libenum.SampleRateEnum;
import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnCompleteListener;
import com.ywl5320.listener.OnErrorListener;
import com.ywl5320.listener.OnInfoListener;
import com.ywl5320.listener.OnLoadListener;
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.listener.OnRecordListener;
import com.ywl5320.listener.OnShowPcmDataListener;
import com.ywl5320.listener.OnVolumeDBListener;
import com.ywl5320.util.WlTimeUtil;
import com.ywl5320.wlmusic.log.MyLog;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime;
    private TextView tvTime2;
    private TextView tvStyle;
    private TextView tvRecord;
    private TextView tvRecordStatus;
    private WlMusic wlMusic;
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
        tvRecord = findViewById(R.id.tv_record);
        tvRecordStatus = findViewById(R.id.tv_record_status);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                wlMusic.seek(position, false, false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wlMusic.seek(position, true, true);
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

        wlMusic = WlMusic.getInstance();
        wlMusic.setCallBackPcmData(true);//是否返回音频PCM数据
        wlMusic.setShowPCMDB(true);//是否返回音频分贝大小
        wlMusic.setPlayCircle(true);//循环播放
        wlMusic.setVolume(65);//声音大小65%
        wlMusic.setPlaySpeed(1.0f);//播放速度正常
        wlMusic.setPlayPitch(1.0f);//播放音调正常
        wlMusic.setConvertSampleRate(SampleRateEnum.RATE_44100);//设定恒定采样率（null为取消）
        tvTime2.setText("音量：" + wlMusic.getVolume() + "%");
        seekBar2.setProgress(wlMusic.getVolume());
        checkBox.setChecked(wlMusic.isPlayCircle());
        setStyle();

        wlMusic.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                MyLog.d("onparpared");
                wlMusic.start();
            }
        });

        wlMusic.setOnErrorListener(new OnErrorListener() {
            @Override
            public void onError(int code, String msg) {
                MyLog.d("code :" + code + ", msg :" + msg);
                Log.d("ywl5320", "code :" + code + ", msg :" + msg);
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
                //MyLog.d("curr:" + timeBean.getCurrSecs() + ", total:" + timeBean.getTotalSecs());
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
                MyLog.d("db is : " + db);
            }
        });

        wlMusic.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onRecordTime(int scds) {
                Message message = Message.obtain();
                message.obj = scds;
                message.what = 2;
                handler.sendMessage(message);

                MyLog.d("record time is:" + scds);
            }

            @Override
            public void onRecordComplete() {
                Message message = Message.obtain();
                message.what = 3;
                handler.sendMessage(message);
            }

            @Override
            public void onRecordPauseResume(boolean pause) {
                Message message = Message.obtain();
                message.obj = pause;
                message.what = 4;
                handler.sendMessage(message);
            }
        });

        wlMusic.setOnShowPcmDataListener(new OnShowPcmDataListener() {
            @Override
            public void onPcmInfo(int samplerate, int bit, int channels) {
                MyLog.d(samplerate + "---" + bit + "---" + channels);
            }

            @Override
            public void onPcmData(byte[] pcmdata, int size, long clock) {
                MyLog.d("pcm size is : " + size + " time is : " + clock);
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
                    int secd = (int) msg.obj;
                    tvRecord.setText("录音时间：" + WlTimeUtil.secdsToDateFormat(secd, 0));
                    tvRecordStatus.setText("正在录音");
                    break;
                case 3:
                    tvRecordStatus.setText("录音完成");
                    break;
                case 4:
                    boolean pause = (boolean) msg.obj;
                    if(pause)
                    {
                        tvRecordStatus.setText("正在暂停");
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        wlMusic.stop();
    }


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
//        wlMusic.setSource("/mnt/shared/Other/林俊杰 - 爱不会绝迹.wav");
//        wlMusic.setSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/林俊杰 - 爱不会绝迹.wav");
        wlMusic.setSource("https://vips-static.pnlyy.com/Fn57-AhK0RjfjymvEoIErGKmmcPm");
        wlMusic.prePared();
    }

    public void change(View view) {
//        wlMusic.playNext("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
        wlMusic.playNext("http://ngcdn004.cnr.cn/live/dszs/index.m3u8");
    }

    public void fast(View view) {
        wlMusic.setPlaySpeed(1.5f);
        wlMusic.setPlayPitch(1.0f);
        spStyle = "变速不变调1.5x";
        setStyle();
    }

    public void slow(View view) {
        wlMusic.setPlaySpeed(0.5f);
        wlMusic.setPlayPitch(1.0f);
        spStyle = "变速不变调0.5x";
        setStyle();
    }

    public void normal(View view) {
        wlMusic.setPlaySpeed(1f);
        wlMusic.setPlayPitch(1.0f);
        spStyle = "变速不变调1.0x";
        setStyle();
    }

    public void left(View view) {
        wlMusic.setMute(MuteEnum.MUTE_LEFT);
        muStyle = "左声道";
        setStyle();
    }

    public void right(View view) {
        wlMusic.setMute(MuteEnum.MUTE_RIGHT);
        muStyle = "右声道";
        setStyle();
    }

    public void center(View view) {
        wlMusic.setMute(MuteEnum.MUTE_CENTER);
        muStyle = "立体声";
        setStyle();
    }

    public void fpitch(View view) {
        wlMusic.setPlayPitch(1.5f);
        wlMusic.setPlaySpeed(1.0f);
        spStyle = "变调不变速1.5x";
        setStyle();
    }

    public void spitch(View view) {
        wlMusic.setPlayPitch(0.5f);
        wlMusic.setPlaySpeed(1.0f);
        spStyle = "变调不变速0.5x";
        setStyle();
    }

    public void npitch(View view) {
        wlMusic.setPlayPitch(1f);
        wlMusic.setPlaySpeed(1.0f);
        spStyle = "变调不变速1.0x";
        setStyle();
    }

    public void sffast(View view) {
        wlMusic.setPlaySpeed(1.5f);
        wlMusic.setPlayPitch(1.5f);
        spStyle = "变调又变速1.5x";
        setStyle();
    }

    public void sfslow(View view) {
        wlMusic.setPlaySpeed(0.5f);
        wlMusic.setPlayPitch(0.5f);
        spStyle = "变调又变速0.5x";
        setStyle();
    }

    public void sfnormal(View view) {
        wlMusic.setPlayPitch(1);
        wlMusic.setPlaySpeed(1);
        spStyle = "变调又变速1.0x";
        setStyle();
    }

    private void setStyle()
    {
        tvStyle.setText(spStyle + " -- " + muStyle);
    }

    public void startrecord(View view) {
        wlMusic.startRecordPlaying("/mnt/shared/Other", "myrecord");//生成的录音文件为：myrecord.aac
    }

    public void stoprecord(View view) {

        wlMusic.stopRecordPlaying();

    }

    public void pauserecord(View view) {
        wlMusic.pauseRecordPlaying();
    }

    public void resumerecord(View view) {
        wlMusic.resumeRecordPlaying();
    }
}
