package com.ywl5320.wlmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ywl5320.wlmusic.MusicService;
import com.ywl5320.wlmusic.MyApplication;
import com.ywl5320.wlmusic.R;
import com.ywl5320.wlmusic.adapter.RadioAdapter;
import com.ywl5320.wlmusic.adapter.WapHeaderAndFooterAdapter;
import com.ywl5320.wlmusic.base.BaseMusicActivity;
import com.ywl5320.wlmusic.beans.LiveChannelBean;
import com.ywl5320.wlmusic.beans.PlaceBean;
import com.ywl5320.wlmusic.dialog.LocalTypeDialog;
import com.ywl5320.wlmusic.dialog.NormalAskDialog;
import com.ywl5320.wlmusic.http.serviceapi.RadioApi;
import com.ywl5320.wlmusic.http.subscribers.HttpSubscriber;
import com.ywl5320.wlmusic.http.subscribers.SubscriberOnListener;
import com.ywl5320.wlmusic.log.MyLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ywl on 2018/1/10.
 */

public class HomeActivity extends BaseMusicActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.swipRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<PlaceBean> placeBeans;
    private RadioAdapter radioAdapter;
    private WapHeaderAndFooterAdapter headerAndFooterAdapter;
    private List<LiveChannelBean> datas;
    private TextView tvLoadMsg;

    private int pageSize = 10;
    private int currentPage = 1;
    private boolean isLoading = false;
    private String channelPlaceId = "3225";
    private long mPressedTime = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        setTitle("网络广播");
        setRightView(R.mipmap.icon_more);
        showDadaLoad();
        initAdapter();
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_ec4c48));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isLoading) {
                    isLoading = true;
                    currentPage = 1;
                    getLiveList();
                }
            }
        });
        getLiveList();
        getLocalData();
        MyLog.d("token is :" + MyApplication.getInstance().getToken());
    }

    private void initAdapter()
    {
        datas = new ArrayList<>();
        radioAdapter = new RadioAdapter(this, datas);
        radioAdapter.setOnItemClickListener(new RadioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LiveChannelBean liveChannelBean, int position) {
            }
        });
        headerAndFooterAdapter = new WapHeaderAndFooterAdapter(radioAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        View footerView = LayoutInflater.from(this).inflate(R.layout.footer_layout, recyclerView, false);
        tvLoadMsg = footerView.findViewById(R.id.tv_loadmsg);
        headerAndFooterAdapter.addFooter(footerView);

        recyclerView.setAdapter(headerAndFooterAdapter);
        headerAndFooterAdapter.notifyDataSetChanged();

        headerAndFooterAdapter.setOnloadMoreListener(recyclerView, new WapHeaderAndFooterAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(!isLoading)
                {
                    getLiveList();
                }
            }

            @Override
            public void onClickLoadMore() {

            }

            @Override
            public void onItemClick(View view, int position) {

            }
        });

        radioAdapter.setOnItemClickListener(new RadioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LiveChannelBean liveChannelBean, int position) {
                if(liveChannelBean.getStreams() != null && liveChannelBean.getStreams().size() > 0)
                {
                    getPlayBean().setName(liveChannelBean.getName());
                    getPlayBean().setSubname(liveChannelBean.getLiveSectionName());
                    getPlayBean().setChannelId(liveChannelBean.getId());
                    getPlayBean().setImg(liveChannelBean.getImg());
                    getPlayBean().setUrl(liveChannelBean.getStreams().get(0).getUrl());
                    getPlayBean().setTiming(1);
                    liveUrl = liveChannelBean.getStreams().get(0).getUrl();//记录当前直播url
                    startActivity(HomeActivity.this, PlayActivity.class);
                }
                else
                {
                    showToast("数据出错,请稍后再试");
                }
            }
        });

    }

    @Override
    public void onClickMenu() {
        super.onClickMenu();
        if(placeBeans != null)
        {
            LocalTypeDialog localTypeDialog = new LocalTypeDialog(this);
            localTypeDialog.show();
            localTypeDialog.setDatas(placeBeans, channelPlaceId);
            localTypeDialog.setOnTypeSelectedListener(new LocalTypeDialog.OnTypeSelectedListener() {
                @Override
                public void onTypeSelected(PlaceBean placeBean) {
                    channelPlaceId = placeBean.getId();
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(!isLoading) {
                                        isLoading = true;
                                        currentPage = 1;
                                        getLiveList();
                                    }
                                }
                            }, 500);
                        }
                    });
                }
            });
        }
        else
        {
            getLocalData();
        }
    }

    @Override
    public void onBackPressed() {
        if(musicStatus != -1)
        {
            NormalAskDialog normalAskDialog = new NormalAskDialog(this);
            normalAskDialog.show();
            normalAskDialog.setData("是否继续后台播放？", "退出程序", "后台播放", true);
            normalAskDialog.setOnActionListener(new NormalAskDialog.OnActionListener() {
                @Override
                public void onLeftAction() {
                    Intent intent = new Intent(HomeActivity.this, MusicService.class);
                    stopService(intent);
                    onRelease();
                    HomeActivity.this.finish();
                }

                @Override
                public void onRightAction() {
                    HomeActivity.this.finish();
                }
            });
        }
        else
        {
            long mNowTime = System.currentTimeMillis();
            if ((mNowTime - mPressedTime) > 1500) {
                showToast("再按一次退出程序");
                mPressedTime = mNowTime;
            } else {
                HomeActivity.this.finish();
            }
        }
    }

    private void getLocalData()
    {
        RadioApi.getInstance().getLivePlace(MyApplication.getInstance().getToken(), new HttpSubscriber<List<PlaceBean>>(new SubscriberOnListener<List<PlaceBean>>() {
            @Override
            public void onSucceed(List<PlaceBean> data) {
                placeBeans = new ArrayList<>();
                placeBeans.addAll(data);
                MyLog.d(data);
                getLiveList();
            }

            @Override
            public void onError(int code, String msg) {
                MyLog.d(msg);
            }
        }, this));
    }

    private void getLiveList()
    {
        tvLoadMsg.setText("加载中");
        RadioApi.getInstance().getLiveByParam(MyApplication.getInstance().getToken(), channelPlaceId, pageSize, currentPage, new HttpSubscriber<List<LiveChannelBean>>(new SubscriberOnListener<List<LiveChannelBean>>() {
            @Override
            public void onSucceed(List<LiveChannelBean> data) {
                if(data != null)
                {
                    MyLog.d("get data...");
                    if(currentPage == 1) {
                        datas.clear();
                    }
                    if(data.size() == 0)
                    {
                        tvLoadMsg.setText("没有更多了");
                    }
                    else {
                        tvLoadMsg.setText("加载更多");
                        currentPage++;
                    }
                    if(data.size() > 0) {
                        datas.addAll(data);
                    }
                    if(datas.size() < 10)
                    {
                        tvLoadMsg.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvLoadMsg.setVisibility(View.VISIBLE);
                    }
                    headerAndFooterAdapter.notifyDataSetChanged();
                }
                hideDataLoad();
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
                MyLog.d(msg);
                showToast(msg);
            }
        }, this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
