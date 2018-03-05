package com.ywl5320.wlmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ywl5320.wlmusic.R;
import com.ywl5320.wlmusic.beans.PlaceBean;

import java.util.List;

/**
 * Created by ywl on 2017-7-25.
 */

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.TypeHolder>{

    private Context context;
    private List<PlaceBean> datas;
    private OnItemClickListener onItemClickListener;

    public LocalAdapter(Context context, List<PlaceBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public TypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_layout, null);
        TypeHolder holder = new TypeHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(TypeHolder holder, final int position) {
        holder.btn_type.setText(datas.get(position).getName());
        if(datas.get(position).isSelected())
        {
            holder.btn_type.setSelected(true);
            holder.btn_type.setTextColor(context.getResources().getColor(R.color.color_ec4c48));
        }
        else
        {
            holder.btn_type.setSelected(false);
            holder.btn_type.setTextColor(context.getResources().getColor(R.color.color_333333));
        }
        holder.btn_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null)
                {
                    onItemClickListener.onItemClick(datas.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class TypeHolder extends RecyclerView.ViewHolder
    {
        private TextView btn_type;
        public TypeHolder(View itemView) {
            super(itemView);
            btn_type = (TextView) itemView.findViewById(R.id.btn_type);
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(PlaceBean placeBean);
    }

}
