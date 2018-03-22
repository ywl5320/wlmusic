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
import com.ywl5320.listener.OnVolumeDBListener;

/**
 * Created by ywl on 2018-1-7.
 */

public class WlMusic {

    /**
     * the path of file or stream
     */
    private static String source;
    /**
     * playing time and totaltime bean
     */
    private static TimeBean timeBean;
    /**
     * total times
     */
    private static int duration = 0;
    /**
     * volume percent (0-100)
     */
    private static int volume = 100;
    /**
     * the speed of playing without change pitch
     * default 1 normal (0.25~4 -> 0.25x~4.0x)
     */
    private static float playSpeed = 1f;
    /**
     * the pitch of playing without change speed
     * default 1 normal (0.25~4 -> 0.25x~4.0x)
     */
    private static float playPitch = 1f;
    /**
     * this mutex of sound
     */
    private static MuteEnum mute = MuteEnum.MUTE_CENTER;//0:left 1:right 2:center
    /**
     * will play next
     */
    private static boolean playNext = false;
    /**
     * will play still
     */
    private static boolean playCircle = false;
    /**
     * play status
     */
    private static boolean isPlaying = false;
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

    public WlMusic(){}

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

    public boolean isPlaying() {
        return isPlaying;
    }

    public void parpared()
    {
        if(TextUtils.isEmpty(source))
        {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                isPlaying = true;
                n_parpared(source);
            }
        }).start();
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
        if(timeBean == null)
        {
            timeBean = new TimeBean();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                isPlaying = true;
                setVolume(volume);
                setPlaySpeed(playSpeed);
                setPlayPitch(playPitch);
                setMute(mute);
                n_start();
            }
        }).start();
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
        timeBean = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                n_stop();
                isPlaying = false;
                if(playNext)
                {
                    playNext = false;
                    parpared();
                }
            }
        }).start();
    }

    public void seek(final int secds)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                n_seek(secds);
            }
        }).start();
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

    public static float getPlayPitch() {
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

    /**
     * for native call
     */
    private void onCallParpared()
    {
        if(onPreparedListener != null)
        {
            onPreparedListener.onPrepared();
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
        if(onInfoListener != null && timeBean != null)
        {
            timeBean.setCurrSecs(currSec);
            timeBean.setTotalSecs(totalSec);
            duration = totalSec;
            onInfoListener.onInfo(timeBean);
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
            stop();
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
     * native prepared
     * @param source
     */
    private native void n_parpared(String source);

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
    private native int n_stop();

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
