package com.ywl5320.wlmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ywl5320.wlmusic.R;
import com.ywl5320.wlmusic.beans.LiveChannelBean;

import java.util.List;

/**
 * Created by ywl on 2017-7-23.
 */

public class RadioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<LiveChannelBean> datas;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public RadioAdapter(Context context, List<LiveChannelBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_channel_layout, parent, false);
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyHolder)holder).tvName.setText(datas.get(position).getName());
        if(!TextUtils.isEmpty(datas.get(position).getLiveSectionName())) {
            ((MyHolder) holder).tvLiveName.setVisibility(View.VISIBLE);
            ((MyHolder) holder).tvLiveName.setText("正在直播：" + datas.get(position).getLiveSectionName());
        }
        else
        {
            ((MyHolder) holder).tvLiveName.setVisibility(View.GONE);
        }
        Glide.with(context) .load(datas.get(position).getImg()).into(((MyHolder)holder).ivImg);
        if(onItemClickListener != null) {
            ((MyHolder) holder).rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(datas.get(position), position);
                }
            });
        }

        if(onItemLongClickListener != null)
        {
            ((MyHolder)holder).rlRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onLongItemClick(datas.get(position), position);
                    return true;
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivImg;
        private TextView tvName;
        private TextView tvLiveName;
        private RelativeLayout rlRoot;
        public MyHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLiveName = itemView.findViewById(R.id.tv_live_name);
            rlRoot = itemView.findViewById(R.id.rl_root);
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(LiveChannelBean liveChannelBean, int position);
    }

    public interface OnItemLongClickListener
    {
        void onLongItemClick(LiveChannelBean liveChannelBean, int position);
    }
}
