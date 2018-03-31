package com.ywl5320.wlmusic.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ywl5320.wlmusic.R;
import com.ywl5320.wlmusic.base.BaseDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2018/1/17.
 */

public class NormalAskDialog extends BaseDialog{

    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.tv_background)
    TextView tvBackGround;

    private OnActionListener onActionListener;
    private boolean canback;

    public NormalAskDialog(Context context) {
        super(context);
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ask_layout);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(R.color.color_trans);
            getWindow().setLayout(width * 3 / 4, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        setCanceledOnTouchOutside(false);
        this.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK  && event.getAction() == KeyEvent.ACTION_UP)
                {
                    if(canback)
                    {
                        return false;
                    }
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
    }

    @OnClick(R.id.tv_exit)
    public void onClickExit(View view)
    {
        if(onActionListener != null)
        {
            onActionListener.onLeftAction();
        }
        dismiss();
    }

    @OnClick(R.id.tv_background)
    public void onClickBackGround(View view)
    {
        if(onActionListener != null)
        {
            onActionListener.onRightAction();
        }
        dismiss();
    }

    public void setData(String msg, String leftmsg, String rightmsg, boolean canback)
    {
        this.canback = canback;
        if(tvMsg != null)
        {
            tvMsg.setText(msg);
        }
        if(tvExit != null)
        {
            tvExit.setText(leftmsg);
        }
        if(tvBackGround != null)
        {
            tvBackGround.setText(rightmsg);
        }
    }

    public interface OnActionListener
    {
        void onLeftAction();

        void onRightAction();
    }
}
