package com.ywl5320.wlmusic.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ywl5320.bean.TimeBean;
import com.ywl5320.wlmusic.MyApplication;
import com.ywl5320.wlmusic.R;
import com.ywl5320.wlmusic.activity.PlayActivity;
import com.ywl5320.wlmusic.beans.ChannelSchedulBean;
import com.ywl5320.wlmusic.beans.EventBusBean;
import com.ywl5320.wlmusic.beans.PlayBean;
import com.ywl5320.wlmusic.config.EventType;
import com.ywl5320.wlmusic.dialog.HistoryDialog;
import com.ywl5320.wlmusic.log.MyLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by ywl on 2018/1/12.
 */

public abstract class BaseMusicActivity extends BaseActivity{

    @Nullable
    @BindView(R.id.iv_mini_menu)
    ImageView ivMiniMenu;

    @Nullable
    @BindView(R.id.iv_mini_bg)
    ImageView ivMiniBg;
    
    @Nullable
    @BindView(R.id.tv_mini_name)
    TextView tvMiniName;

    @Nullable
    @BindView(R.id.tv_mini_subname)
    TextView tvMiniSubName;

    @Nullable
    @BindView(R.id.iv_mini_playstatus)
    ImageView ivMiniPlayStatus;

    @Nullable
    @BindView(R.id.pb_mini_load)
    ProgressBar pbMiniLoad;

    @Nullable
    @BindView(R.id.rl_mini_bar)
    RelativeLayout rlMiniBar;

    private static EventBusBean eventPauseResumeBean;//暂停、播放状态
    private static float cdRodio = 0f;
    private static PlayBean playBean;
    private static TimeBean timeBean;
    private static boolean isPlaying = false;

    private HistoryDialog historyDialog;
    private EventBusBean eventNextBean;
    public static String playUrl = "";//当前播放url
    public static String liveUrl = "";//直播url

    public static int musicStatus = -1;

    public static final int PLAY_STATUS_ERROR = 0;
    public static final int PLAY_STATUS_LOADING = 1;
    public static final int PLAY_STATUS_UNLOADING = 2;
    public static final int PLAY_STATUS_PLAYING = 3;
    public static final int PLAY_STATUS_PAUSE = 4;
    public static final int PLAY_STATUS_RESUME = 5;
    public static final int PLAY_STATUS_COMPLETE = 6;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ivMiniBg != null)
        {
            Glide.with(this).load(getPlayBean().getImg()).apply(RequestOptions.placeholderOf(R.mipmap.icon_mini_default_bg)).into(ivMiniBg);
        }
        if(tvMiniName != null)
        {
            if(!tvMiniName.getText().toString().trim().equals(getPlayBean().getName()))
            {
                tvMiniName.setText(getPlayBean().getName());
            }
        }
        if(tvMiniSubName != null)
        {
            if(!tvMiniSubName.getText().toString().trim().equals(getPlayBean().getSubname()))
            {
                tvMiniSubName.setText(getPlayBean().getSubname());
            }
        }
        onMusicStatus(musicStatus);
        if(rlMiniBar != null)
        {
            if(playBean != null && !TextUtils.isEmpty(playBean.getUrl()))
            {
                if(rlMiniBar.getVisibility() != View.VISIBLE)
                {
                    rlMiniBar.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                rlMiniBar.setVisibility(View.GONE);
            }
        }
    }

    @Optional
    @OnClick(R.id.iv_mini_menu)
    public void onClickHistory(View view)
    {
        showHistory(getPlayBean().getChannelId());
    }

    public void showHistory(final String channleId)
    {
        if(historyDialog == null)
        {
            historyDialog = new HistoryDialog(this, this);
        }
        historyDialog.show();
        historyDialog.setName(getPlayBean().getName());
        historyDialog.getHistoryData(channleId, MyApplication.getInstance().getToken());
        historyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                historyDialog = null;
            }
        });
        historyDialog.setOnItemClickListener(new HistoryDialog.OnItemClickListener() {
            @Override
            public void onItemClick(ChannelSchedulBean channelSchedulBean) {
                if(channelSchedulBean != null && channelSchedulBean.getStreams() != null && channelSchedulBean.getStreams().size() > 0)
                {
                    getPlayBean().setSubname(channelSchedulBean.getName());
                    if(channelSchedulBean.getTiming() == 0)
                    {
                        if(!TextUtils.isEmpty(channelSchedulBean.getDownLoadUrl()))
                        {
                            getPlayBean().setUrl(channelSchedulBean.getDownLoadUrl());
                        }
                        else {
                            getPlayBean().setUrl(channelSchedulBean.getStreams().get(0).getUrl());
                        }
                        playUrl = getPlayBean().getUrl();
                    }
                    else if(channelSchedulBean.getTiming() == 1)
                    {
                        getPlayBean().setUrl(liveUrl);
                    }
                    getPlayBean().setIndex(channelSchedulBean.getIndex());
                    getPlayBean().setTiming(channelSchedulBean.getTiming());
                    if(eventNextBean == null)
                    {
                        eventNextBean = new EventBusBean(EventType.MUSIC_NEXT, getPlayBean().getUrl());
                    }
                    else
                    {
                        eventNextBean.setType(EventType.MUSIC_NEXT);
                        eventNextBean.setObject(getPlayBean().getUrl());
                    }
                    EventBus.getDefault().post(eventNextBean);
                    getTimeBean().setTotalSecs(0);
                    getTimeBean().setCurrSecs(0);
                    if(tvMiniSubName != null)
                    {
                        tvMiniSubName.setText(getPlayBean().getSubname());
                    }
                    onPlayHistoryChange();
                }
            }
        });
    }

    @Optional
    @OnClick(R.id.rl_mini_bar)
    public void onClickLive(View view)
    {
        if(playBean != null && !TextUtils.isEmpty(playBean.getChannelId()))
        {
            startActivity(this, PlayActivity.class);
        }
    }

    @Optional
    @OnClick(R.id.iv_mini_playstatus)
    public void onClickPlaystatus(View view)
    {
        if(musicStatus == PLAY_STATUS_PLAYING)
        {
            pauseMusic(true);
            if(ivMiniPlayStatus != null)
            {
                ivMiniPlayStatus.setImageResource(R.drawable.svg_play);
            }
        }
        else if(musicStatus == PLAY_STATUS_PAUSE)
        {
            pauseMusic(false);
            if(ivMiniPlayStatus != null)
            {
                ivMiniPlayStatus.setImageResource(R.mipmap.icon_menu_pause);
            }
        }
        else if(musicStatus == PLAY_STATUS_ERROR || musicStatus == PLAY_STATUS_COMPLETE)
        {
            playUrl = "";
            replayRadio();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMsg(final EventBusBean messBean) {
        if(messBean.getType() == EventType.MUSIC_TIME_INFO)//时间信息
        {
            MyLog.d("播放中...");
            if(!isPlaying)
            {
                ivMiniPlayStatus.setImageResource(R.mipmap.icon_menu_pause);
            }
            isPlaying = true;
            timeBean = (TimeBean) messBean.getObject();
            timeInfo(timeBean);
            musicStatus = PLAY_STATUS_PLAYING;
            onMusicStatus(musicStatus);
        }
        else if(messBean.getType() == EventType.MUSIC_ERROR)
        {
            String errormsg = (String) messBean.getObject();
            MyLog.d("播放失败...");
            musicStatus = PLAY_STATUS_ERROR;
            onMusicStatus(musicStatus);
            showToast("播放出错：" + errormsg);
        }
        else if(messBean.getType() == EventType.MUSIC_LOAD)
        {
            boolean load = (boolean) messBean.getObject();
            onLoad(load);
            if(load)
            {
                MyLog.d("加载中...");
                musicStatus = PLAY_STATUS_LOADING;
            }
            else
            {
                MyLog.d("加载完成...");
                musicStatus = PLAY_STATUS_UNLOADING;
            }
            onMusicStatus(musicStatus);
        }
        else if(messBean.getType() == EventType.MUSIC_COMPLETE)
        {
            isPlaying = false;
            MyLog.d("播放完成...");
            musicStatus = PLAY_STATUS_COMPLETE;
            onMusicStatus(musicStatus);
        }
        else if(messBean.getType() == EventType.MUSIC_PAUSE_RESUME_RESULT)
        {
            boolean pause = (boolean) messBean.getObject();
            if(pause)
            {
                MyLog.d("暂停（pause）...");
                musicStatus = PLAY_STATUS_PAUSE;
            }
            else
            {
                MyLog.d("播放（resume）...");
                musicStatus = PLAY_STATUS_RESUME;
            }
            onMusicStatus(musicStatus);
        }
    }

    /**
     * 暂停播放
     * @param pause
     */
    public void pauseMusic(boolean pause)
    {
        if(eventPauseResumeBean == null)
        {
            eventPauseResumeBean = new EventBusBean(EventType.MUSIC_PAUSE_RESUME, pause);
        }
        else
        {
            eventPauseResumeBean.setType(EventType.MUSIC_PAUSE_RESUME);
            eventPauseResumeBean.setObject(pause);
        }
        isPlaying = !pause;
        EventBus.getDefault().post(eventPauseResumeBean);
    }

    public void timeInfo(TimeBean timeBean){}

    public void onLoad(boolean load){}

    public void onPlayHistoryChange(){}

    public boolean isPlaying()
    {
        return isPlaying;
    }

    public void setCdRodio(float rodio) {
        cdRodio = rodio;
    }

    public float getCdRodio()
    {
        return cdRodio;
    }


    public PlayBean getPlayBean() {
        if(playBean == null)
        {
            playBean = new PlayBean();
        }
        MyLog.d("url is :" + playBean.getUrl());
        return playBean;
    }

    public static TimeBean getTimeBean() {
        if(timeBean == null)
        {
            timeBean = new TimeBean();
        }
        return timeBean;
    }

    public int getProgress()
    {
        if(timeBean != null && timeBean.getTotalSecs() > 0)
        {
            return timeBean.getCurrSecs() * 100 / timeBean.getTotalSecs();
        }
        return 0;
    }

    public void onMusicStatus(int status)
    {
        switch (status)
        {
            case PLAY_STATUS_ERROR:
                if(ivMiniPlayStatus != null) {
                    ivMiniPlayStatus.setImageResource(R.drawable.svg_play);
                }
                break;
            case PLAY_STATUS_LOADING:
                if(pbMiniLoad != null && ivMiniPlayStatus != null)
                {
                    pbMiniLoad.setVisibility(View.VISIBLE);
                    ivMiniPlayStatus.setVisibility(View.GONE);
                }
                break;
            case PLAY_STATUS_UNLOADING:
                if(pbMiniLoad != null && ivMiniPlayStatus != null)
                {
                    pbMiniLoad.setVisibility(View.GONE);
                    ivMiniPlayStatus.setVisibility(View.VISIBLE);
                }
                break;
            case PLAY_STATUS_PLAYING:
                if(ivMiniPlayStatus != null)
                {
                    ivMiniPlayStatus.setImageResource(R.mipmap.icon_menu_pause);
                    if(pbMiniLoad != null)
                    {
                        pbMiniLoad.setVisibility(View.GONE);
                        ivMiniPlayStatus.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case PLAY_STATUS_PAUSE:
                if(ivMiniPlayStatus != null)
                {
                    ivMiniPlayStatus.setImageResource(R.drawable.svg_play);
                    if(pbMiniLoad != null)
                    {
                        pbMiniLoad.setVisibility(View.GONE);
                        ivMiniPlayStatus.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case PLAY_STATUS_RESUME:
                if(ivMiniPlayStatus != null)
                {
                    ivMiniPlayStatus.setImageResource(R.drawable.svg_play);
                }
                break;
            case PLAY_STATUS_COMPLETE:
                if(ivMiniPlayStatus != null) {
                    ivMiniPlayStatus.setImageResource(R.drawable.svg_play);
                }
                break;
                default:
                    break;
        }
    }

    public void addIndexForHistory(List<ChannelSchedulBean> datas, PlayBean livePlayBean)
    {

        if(datas != null && playBean != null)
        {
            List<ChannelSchedulBean> tempdatas = new ArrayList<>();
            int size = datas.size();
            for(int i = 0; i < size; i++)
            {
                if(datas.get(i).getTiming() == 0)
                {
                    datas.get(i).setIndex(i);
                    tempdatas.add(datas.get(i));
                }
                else if(datas.get(i).getTiming() == 1)
                {
                    datas.get(i).setIndex(i);
                    tempdatas.add(datas.get(i));
                    if(livePlayBean.getTiming() == 1)
                    {
                        livePlayBean.setIndex(i);
                    }
                }
            }
            datas.clear();
            datas.addAll(tempdatas);
            MyLog.d(datas);
        }
    }

    private void replayRadio()
    {
        if(!getPlayBean().getUrl().equals(playUrl))
        {
            setCdRodio(0f);
            if(eventNextBean == null)
            {
                eventNextBean = new EventBusBean(EventType.MUSIC_NEXT, getPlayBean().getUrl());
            }
            else
            {
                eventNextBean.setType(EventType.MUSIC_NEXT);
                eventNextBean.setObject(getPlayBean().getUrl());
            }
            EventBus.getDefault().post(eventNextBean);
            playUrl = getPlayBean().getUrl();
            getTimeBean().setTotalSecs(0);
            getTimeBean().setCurrSecs(0);
        }
    }

    public void onRelease()
    {
        eventPauseResumeBean = null;
        cdRodio = 0f;
        playBean = null;
        timeBean = null;
        isPlaying = false;
        historyDialog = null;
        eventNextBean = null;
        playUrl = "";
        liveUrl = "";
        musicStatus = -1;
    }

}
