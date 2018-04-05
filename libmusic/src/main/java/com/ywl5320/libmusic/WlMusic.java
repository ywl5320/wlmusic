package com.ywl5320.libmusic;

import android.text.TextUtils;

import com.ywl5320.bean.TimeBean;
import com.ywl5320.libenum.MuteEnum;
import com.ywl5320.listener.OnCompleteListener;
import com.ywl5320.listener.OnErrorListener;
import com.ywl5320.listener.OnInfoListener;
import com.ywl5320.listener.OnLoadListener;
import com.ywl5320.listener.OnPauseResumeListener;
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.listener.OnRecordListener;
import com.ywl5320.listener.OnVolumeDBListener;

import java.io.File;

/**
 * Created by ywl on 2018-1-7.
 */

public class WlMusic {

    /**
     * the path of file or stream
     */
    private String source;
    /**
     * playing time and totaltime bean
     */
    private TimeBean timeBean;
    /**
     * total times
     */
    private int duration = 0;
    /**
     * volume percent (0-100)
     */
    private int volume = 100;
    /**
     * the speed of playing without change pitch
     * default 1 normal (0.25~4 -> 0.25x~4.0x)
     */
    private float playSpeed = 1f;
    /**
     * the pitch of playing without change speed
     * default 1 normal (0.25~4 -> 0.25x~4.0x)
     */
    private float playPitch = 1f;
    /**
     * this mutex of sound
     */
    private MuteEnum mute = MuteEnum.MUTE_CENTER;//0:left 1:right 2:center

    /**
     * record path dir
     */
    private String recordSavePath = null;

    /**
     * record file name
     */
    private String recordSaveName = null;
    /**
     * will play next
     */
    private boolean playNext = false;
    /**
     * will play still
     */
    private boolean playCircle = false;
    /**
     * play status
     */
    private boolean isPlaying = false;
    /**
     * seek status
     */
    private boolean isSeek = false;
    /**
     * duration seeking showtime callback
     * true:show
     * false:not show
     */
    private boolean seekingShowTime = true;

    /**
     * stop status
     */
    private boolean stopStatus = false;

    private boolean preparedTostart = false;

    /**
     * prepared callback
     */
    private OnPreparedListener onPreparedListener;
    /**
     * error callback
     */
    private OnErrorListener onErrorListener;
    /**
     * load status callback
     */
    private OnLoadListener onLoadListener;
    /**
     * info callback
     */
    private OnInfoListener onInfoListener;
    /**
     * play complete callback
     */
    private OnCompleteListener onCompleteListener;
    /**
     * pause and resume callback
     */
    private OnPauseResumeListener onPauseResumeListener;
    /**
     * the DB of volume value callback
     */
    private OnVolumeDBListener onVolumeDBListener;

    /**
     * this record time listener
     */
    private OnRecordListener onRecordListener;

    public static WlMusic instance = null;

    private WlMusic(){}

    public static synchronized WlMusic getInstance()
    {
        if(instance == null)
        {
            synchronized (WlMusic.class)
            {
                if(instance == null)
                {
                    instance = new WlMusic();
                }
            }
        }
        return instance;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource()
    {
        return source;
    }


    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public void setOnInfoListener(OnInfoListener onInfoListener) {
        this.onInfoListener = onInfoListener;
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public void setOnPauseResumeListener(OnPauseResumeListener onPauseResumeListener) {
        this.onPauseResumeListener = onPauseResumeListener;
    }

    public void setOnVolumeDBListener(OnVolumeDBListener onVolumeDBListener) {
        this.onVolumeDBListener = onVolumeDBListener;
    }

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    public boolean isPlaying() {
        return isPlaying;
    }


    public void prePared()
    {
        if(TextUtils.isEmpty(source))
        {
            return;
        }
        if(stopStatus)
        {
            return;
        }
        isPlaying = true;
        playNext = false;
        preparedTostart = false;
        n_prepared(source);
    }

    public void playNext(String source)
    {
        playNext = true;
        this.source = source;
        stop();
    }

    public void setPlayCircle(boolean playCircle) {
        this.playCircle = playCircle;
    }

    public boolean isPlayCircle()
    {
        return playCircle;
    }

    public void start()
    {
        if(!preparedTostart)
        {
            if(onErrorListener != null)
            {
                onErrorListener.onError(1009, "please call parpared first");
            }
            return;
        }
        if(timeBean == null)
        {
            timeBean = new TimeBean();
        }
        isPlaying = true;
        setVolume(volume);
        setPlaySpeed(playSpeed);
        setPlayPitch(playPitch);
        setMute(mute);
        startRecordPlaying(recordSavePath, recordSaveName);
        n_start();
    }

    public void pause()
    {
        n_pause();
        isPlaying = false;
        if(onPauseResumeListener != null)
        {
            onPauseResumeListener.onPause(true);
        }
    }

    public void resume()
    {
        n_resume();
        isPlaying = true;
        if(onPauseResumeListener != null)
        {
            onPauseResumeListener.onPause(false);
        }
    }

    public void stop()
    {
        if(!stopStatus)
        {
            stopStatus = true;
            timeBean = null;
            recordSaveName = null;
            recordSavePath = null;
            n_stop();
            isPlaying = false;
        }
    }

    public void seek(final int secds, boolean seekingfinished, boolean showTime)
    {
        seekingShowTime = showTime;
        if(duration <= 0)
        {
            return;
        }
        if(seekingfinished)
        {
            isSeek = seekingfinished;
            n_seek(secds);
        }
    }

    public int getDuration()
    {
        return duration;
    }

    public void setVolume(int percent)
    {
        if(percent <= 0)
        {
            percent = 0;
        }
        else if(percent >= 100)
        {
            percent = 100;
        }
        this.volume = percent;
        n_volume(volume);
    }

    public int getVolume()
    {
        return volume;
    }

    public void setPlaySpeed(float speed)
    {
        this.playSpeed = speed;
        n_playspeed(playSpeed);
    }

    public float getPlaySpeed()
    {
        return playSpeed;
    }

    public float getPlayPitch() {
        return playPitch;
    }

    public void setPlayPitch(float pitch) {
        this.playPitch = pitch;
        n_playpitch(pitch);
    }

    public void setMute(MuteEnum mute)
    {
        this.mute = mute;
        n_mute(mute.getValue());
    }

    public MuteEnum getMute()
    {
        return mute;
    }


    public void startRecordPlaying(String recordSavePath, String recordSaveName)
    {
        if(TextUtils.isEmpty(recordSavePath) || TextUtils.isEmpty(recordSaveName))
        {
            return;
        }
        File file = new File(recordSavePath);
        if(!file.exists())
        {
            file.mkdirs();
        }
        if(!file.exists())
        {
            if(onErrorListener != null)
            {
                onErrorListener.onError(1008, "record path is wrong");
            }
            return;
        }
        this.recordSavePath = recordSavePath;
        this.recordSaveName = recordSaveName;
        n_startPlayRecord(recordSavePath + "/" + recordSaveName + ".aac");
    }

    public void stopRecordPlaying()
    {
        this.recordSavePath = null;
        this.recordSaveName = null;
        n_stopPlayRecord();
    }

    public void pauseRecordPlaying()
    {
        n_pauseRecordPlaying();
    }

    public void resumeRecordPlaying()
    {
        n_resumeRecordPlaying();
    }


    /**
     * for native call
     */
    private void onCallParpared()
    {
        if(onPreparedListener != null)
        {
            if(!stopStatus)
            {
                preparedTostart = true;
                onPreparedListener.onPrepared();
            }
        }
    }

    /**
     * for native call
     * @param code
     * @param msg
     */
    private void onCallError(int code, String msg)
    {
        if(onErrorListener != null)
        {
            onErrorListener.onError(code, msg);
        }
    }

    /**
     * for native call
     * @param load
     */
    private void onCallLoad(boolean load)
    {
        if(onLoadListener != null)
        {
            onLoadListener.onLoad(load);
        }
    }

    /**
     * for native call
     * @param currSec
     * @param totalSec
     */
    private void onCallInfo(int currSec, int totalSec)
    {
        if(!seekingShowTime)
        {
            return;
        }
        if(onInfoListener != null && timeBean != null)
        {
            if(!isSeek) {
                timeBean.setCurrSecs(currSec);
                timeBean.setTotalSecs(totalSec);
                duration = totalSec;
                onInfoListener.onInfo(timeBean);
            }
        }
    }

    /**
     * for native call
     */
    private void onCallComplete()
    {
        if(onCompleteListener != null)
        {
            onCallInfo(duration, duration);
            onCompleteListener.onComplete();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    stop();
                }
            }).start();
        }
    }

    /**
     * for native call
     * @return
     */
    private boolean onCallPlayCircle() {
        return playCircle;
    }

    /**
     * for native call
     * @param db
     */
    private void onCallVolumeDB(int db)
    {
        if(onVolumeDBListener != null)
        {
            onVolumeDBListener.onVolumeDB(db);
        }
    }

    /**
     * stop complete callback
     */
    private void onCallStopComplete()
    {
        stopStatus = false;
        if(playNext)
        {
            prePared();
        }
    }

    /**
     * seek complete callback
     */
    private void onCallSeekComplete()
    {
        isSeek = false;
    }

    /**
     * show record time callback
     * @param secds
     */
    private void onCallRecordTime(int secds)
    {
        if(onRecordListener != null)
        {
            onRecordListener.onRecordTime(secds);
        }
    }

    /**
     * record complete callback
     */
    private void onCallRecordComplete()
    {
        if(onRecordListener != null)
        {
            onRecordListener.onRecordComplete();
        }
    }

    /**
     * record pause and resume callback
     * @param pause
     */
    private void onCallRecordPauseResume(boolean pause)
    {
        if(onRecordListener != null)
        {
            onRecordListener.onRecordPauseResume(pause);
        }
    }


    /**
     * native prepared
     * @param source
     */
    private native void n_prepared(String source);

    /**
     * native start
     */
    private native void n_start();

    /**
     * native pause
     */
    private native void n_pause();

    /**
     * native resume
     */
    private native void n_resume();

    /**
     * native stop
     * @return
     */
    private native void n_stop();

    /**
     * native seek to seconeds
     * @param secds
     */
    private native void n_seek(int secds);

    /**
     * native set volume
     * @param percent
     */
    private native void n_volume(int percent);

    /**
     * native set speed
     * @param speed
     */
    private native void n_playspeed(float speed);

    /**
     * native set pitch
     * @param pitch
     */
    private native void n_playpitch(float pitch);

    /**
     * native set mute
     * @param mute
     */
    private native void n_mute(int mute);

    /**
     * start record while playing
     */
    private native void n_startPlayRecord(String aacsavepath);

    /**
     * stop record while playing
     */
    private native void n_stopPlayRecord();

    /**
     * pause the recorder
     */
    private native void n_pauseRecordPlaying();

    /**
     * resume the recorder
     */
    private native void n_resumeRecordPlaying();

    /**
     * load library
     */
    static {
        System.loadLibrary("avutil-55");
        System.loadLibrary("swresample-2");
        System.loadLibrary("avcodec-57");
        System.loadLibrary("avformat-57");
        System.loadLibrary("swscale-4");
        System.loadLibrary("postproc-54");
        System.loadLibrary("avfilter-6");
        System.loadLibrary("avdevice-57");
        System.loadLibrary("wlmusic");
    }
}
