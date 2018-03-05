package com.ywl5320.wlmusic.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ywl5320.wlmusic.R;
import com.ywl5320.wlmusic.adapter.LocalAdapter;
import com.ywl5320.wlmusic.base.BaseDialog;
import com.ywl5320.wlmusic.beans.PlaceBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2018/1/15.
 */

public class LocalTypeDialog extends BaseDialog{

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private LocalAdapter localAdapter;
    private List<PlaceBean> datas;
    private OnTypeSelectedListener onTypeSelectedListener;
    private PlaceBean selectedPlaceBean;

    public LocalTypeDialog(Context context) {
        super(context);
    }

    public void setOnTypeSelectedListener(OnTypeSelectedListener onTypeSelectedListener) {
        this.onTypeSelectedListener = onTypeSelectedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_place_type_layout);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(R.color.color_trans);
            getWindow().setLayout(width * 3 / 4, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        lp.weight = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = height / 3;
        recyclerView.setLayoutParams(lp);

    }

    @OnClick(R.id.tv_selected)
    public void onClickSelected(View view)
    {
        if(onTypeSelectedListener != null && selectedPlaceBean != null)
        {
            onTypeSelectedListener.onTypeSelected(selectedPlaceBean);
        }
        dismiss();
    }

    public void setDatas(List<PlaceBean> d, String currid) {
        datas = new ArrayList<>();
        PlaceBean placeBean = new PlaceBean();
        placeBean.setId("3225");
        placeBean.setName("中央");
        datas.add(placeBean);
        datas.addAll(d);
        setSelected(currid);
        localAdapter = new LocalAdapter(context, datas);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(localAdapter);

        localAdapter.setOnItemClickListener(new LocalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PlaceBean placeBean) {
                selectedPlaceBean = placeBean;
                setSelected(placeBean.getId());
                localAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setSelected(String currid)
    {
        for(PlaceBean placeBean : datas)
        {
            if(placeBean.getId().equals(currid))
            {
                placeBean.setSelected(true);
            }
            else
            {
                placeBean.setSelected(false);
            }
        }
    }

    public interface OnTypeSelectedListener
    {
        void onTypeSelected(PlaceBean placeBean);
    }
}
