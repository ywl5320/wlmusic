package com.ywl5320.wlmusic.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ywl5320.wlmusic.R;
import com.ywl5320.wlmusic.adapter.HistoryAdapter;
import com.ywl5320.wlmusic.base.BaseDialog;
import com.ywl5320.wlmusic.beans.ChannelSchedulBean;
import com.ywl5320.wlmusic.http.serviceapi.RadioApi;
import com.ywl5320.wlmusic.http.subscribers.HttpSubscriber;
import com.ywl5320.wlmusic.http.subscribers.SubscriberOnListener;
import com.ywl5320.wlmusic.log.MyLog;
import com.ywl5320.wlmusic.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2018-1-13.
 */

public class HistoryDialog extends BaseDialog {

    @BindView(R.id.ly_content)
    LinearLayout lycontent;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.pb_load)
    ProgressBar pbLoad;
    @BindView(R.id.tv_msg)
    TextView tvMsg;

    private HistoryAdapter historyAdapter;
    private List<ChannelSchedulBean> datas;
    private OnItemClickListener onItemClickListener;

    private Activity activity;


    public HistoryDialog(Context context, Activity activity) {
        super(context);
        this.activity = activity;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogEnter);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_history_layout);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(R.color.color_trans);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) lycontent.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = CommonUtil.getScreenHeight(activity) * 1 / 2;
        lycontent.setLayoutParams(layoutParams);
        initAdapter();
        pbLoad.setVisibility(View.VISIBLE);
        tvMsg.setVisibility(View.GONE);
    }

    private void initAdapter()
    {
        datas = new ArrayList<>();
        historyAdapter = new HistoryAdapter(context, datas);
        historyAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ChannelSchedulBean liveChannelBean, int position) {
                if(onItemClickListener != null && liveChannelBean != null)
                {
                    onItemClickListener.onItemClick(liveChannelBean);
                }
                dismiss();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(historyAdapter);

    }


    public void getHistoryData(String chennelId, String token)
    {
        RadioApi.getInstance().getHistoryByChannelId(chennelId, token, new HttpSubscriber<List<ChannelSchedulBean>>(new SubscriberOnListener<List<ChannelSchedulBean>>() {
            @Override
            public void onSucceed(List<ChannelSchedulBean> data) {
                if(data != null)
                {
                    MyLog.d(data);
                    datas.clear();
                    datas.addAll(data);
                    addIndexHistory(datas);
                    historyAdapter.notifyDataSetChanged();
                    if(datas.size() > 0)
                    {
                        pbLoad.setVisibility(View.GONE);
                        tvMsg.setVisibility(View.GONE);
                    }
                    else
                    {
                        pbLoad.setVisibility(View.GONE);
                        tvMsg.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    pbLoad.setVisibility(View.GONE);
                    tvMsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(int code, String msg) {
                pbLoad.setVisibility(View.GONE);
                tvMsg.setVisibility(View.VISIBLE);
                MyLog.d(msg);
            }
        }, context));
    }

    public void setName(String name)
    {
        if(tvName != null)
        {
            tvName.setText(name);
        }
    }

    public void addIndexHistory(List<ChannelSchedulBean> datas)
    {
        if(datas != null)
        {
            int size = datas.size();
            for(int i = 0; i < size; i++)
            {
                datas.get(i).setIndex(i);
            }
        }
    }

    @OnClick(R.id.rl_root)
    public void onClickClose(View view)
    {
        dismiss();
    }

    public interface OnItemClickListener
    {
        void onItemClick(ChannelSchedulBean channelSchedulBean);
    }
}
