package com.ywl5320.wlmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ywl5320.wlmusic.R;
import com.ywl5320.wlmusic.beans.ChannelSchedulBean;

import java.util.List;

/**
 * Created by ywl on 2017-7-23.
 */

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<ChannelSchedulBean> datas;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public HistoryAdapter(Context context, List<ChannelSchedulBean> datas) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_channel_layout, parent, false);
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyHolder)holder).tvName.setText(datas.get(position).getName());
        if(datas.get(position).getTiming() == 0)
        {
            ((MyHolder) holder).tvLiveName.setText("回听：" + datas.get(position).getStart() + " ~ " + datas.get(position).getEnd());
        }
        else if(datas.get(position).getTiming() == 1)
        {
            ((MyHolder) holder).tvLiveName.setText("直播：" + datas.get(position).getStart() + " ~ " + datas.get(position).getEnd());
        }
        else if(datas.get(position).getTiming() == 2)
        {
            ((MyHolder) holder).tvLiveName.setText("未播：" + datas.get(position).getStart() + " ~ " + datas.get(position).getEnd());
        }

        if(onItemClickListener != null && (datas.get(position).getTiming() == 0 || datas.get(position).getTiming() == 1)) {
            ((MyHolder) holder).rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(datas.get(position), position);
                }
            });
        }

        if(onItemLongClickListener != null && (datas.get(position).getTiming() == 0 || datas.get(position).getTiming() == 1))
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
        private TextView tvName;
        private TextView tvLiveName;
        private RelativeLayout rlRoot;
        public MyHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLiveName = itemView.findViewById(R.id.tv_live_name);
            rlRoot = itemView.findViewById(R.id.rl_root);
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(ChannelSchedulBean liveChannelBean, int position);
    }

    public interface OnItemLongClickListener
    {
        void onLongItemClick(ChannelSchedulBean liveChannelBean, int position);
    }
}
