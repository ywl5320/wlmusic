package com.ywl5320.wlmusic.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ywl5320.wlmusic.R;
import com.ywl5320.wlmusic.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ywl on 2017/2/5.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.ly_system_parent)
    LinearLayout lySystemParent;

    @Nullable
    @BindView(R.id.iv_line)
    ImageView ivLine;

    @Nullable
    @BindView(R.id.ly_system_bar)
    LinearLayout lySystemBar;

    @Nullable
    @BindView(R.id.tv_title)
    TextView mtvTitle;

    @Nullable
    @BindView(R.id.iv_back)
    ImageView mivBack;

    @Nullable
    @BindView(R.id.iv_right)
    ImageView mivRight;

    @Nullable
    @BindView(R.id.tv_right)
    TextView tvRight;

    @Nullable
    @BindView(R.id.ly_data_load)
    LinearLayout lyDataLoad;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //透明状态栏
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4全透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        lySystemBar = (LinearLayout) findViewById(R.id.ly_system_bar);
        if(lySystemBar != null) {
            initSystembar(lySystemBar);
        }
        if(mivBack != null)
        {
            mivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickBack();
                }
            });
        }
        if(mivRight != null)
        {
            mivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickMenu();
                }
            });
        }

        if(tvRight != null)
        {
            tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickTxtMenu();
                }
            });
        }

    }

    public void setTitleTrans(int color)
    {
        if(lySystemParent != null)
        {
            lySystemParent.setBackgroundColor(getResources().getColor(color));
        }
    }

    public void setTitleLine(int color)
    {
        if(ivLine != null)
        {
            ivLine.setBackgroundColor(getResources().getColor(color));
        }
    }

    public void onClickMenu(){}

    public void onClickBack(){
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    public void onClickTxtMenu(){}



    public void initSystembar(View lySystemBar)
    {
        if (Build.VERSION.SDK_INT >= 19) {
            if (lySystemBar != null) {
                lySystemBar.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lySystemBar.getLayoutParams();
                lp.height = CommonUtil.getStatusHeight(this);
                lySystemBar.requestLayout();
            }
        } else {
            if (lySystemBar != null) {
                lySystemBar.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title)
    {
        if(mtvTitle != null)
        {
            mtvTitle.setText(title);
        }
    }

    /**
     * 显示返回图标
     */
    public void setBackView()
    {
        if(mivBack != null)
        {
            mivBack.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示返回图标
     */
    public void setBackView(int resId)
    {
        if(mivBack != null)
        {
            mivBack.setVisibility(View.VISIBLE);
            mivBack.setImageResource(resId);
        }
    }

    /**
     * 显示返回图标
     */
    public void setRightView(int resId)
    {
        if(mivRight != null)
        {
            mivRight.setVisibility(View.VISIBLE);
            mivRight.setImageResource(resId);
        }
    }

    public void setRightTxtMenu(String menu)
    {
        if(tvRight != null)
        {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(menu);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public static void startActivity(Context context, Class clz)
    {
        Intent intent = new Intent(context, clz);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

    }

    public static void startActivity(Context context, Class clz, Bundle bundle)
    {
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

    }

    public void onBack(View view) {
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    public void showDadaLoad()
    {
        if(lyDataLoad != null)
        {
            lyDataLoad.setVisibility(View.VISIBLE);
        }
    }

    public void hideDataLoad()
    {
        if(lyDataLoad != null)
        {
            lyDataLoad.setVisibility(View.GONE);
        }
    }


}
