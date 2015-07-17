package com.huizhuang.zxsq.widget.wheel;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.widget.wheel.adapter.AbstractWheelAdapter;
import com.huizhuang.zxsq.widget.wheel.adapter.AbstractWheelTextAdapter;
import com.huizhuang.zxsq.widget.wheel.adapter.ArrayListWheelAdapter;

public class CommonSeleteItemPanel {

    private Context mContext;

    private View mRootView;
    private WheelVerticalView mWheelView;
    
    private TextView mTvTtile;
    private Button mBtnCancel;
    private Button mBtnEnsure;

    private Dialog mSelectDialog;
    
    public CommonSeleteItemPanel(Context context) {
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.widget_wheel_select_item, null);
        mTvTtile = (TextView) mRootView.findViewById(R.id.tv_title);
        mWheelView = (WheelVerticalView) mRootView.findViewById(R.id.wl_list);
    }

    public void setViewAdapter(AbstractWheelTextAdapter adapter) {
        setViewAdapter(0, false, adapter);
    }

    public void setViewAdapter(int selected, AbstractWheelTextAdapter adapter) {
        setViewAdapter(selected, false, adapter);
    }

    public void setViewAdapter(int selected, boolean cyclic, AbstractWheelTextAdapter adapter) {
        adapter.setItemResource(R.layout.zxbd_wheel_item);
        adapter.setItemTextResource(R.id.name);
        mWheelView.setViewAdapter(adapter);// 设置显示数据
        mWheelView.setCyclic(cyclic);// 可循环滚动
        mWheelView.setVisibleItems(5);
        mWheelView.setCurrentItem(selected);// 初始化时显示的数据
    }

    public void setTitle(String title) {
        mTvTtile.setVisibility(View.VISIBLE);
        mTvTtile.setText(title);
    }
    
    public void setTitle(String title, OnClickListener listener) {
        setTitle(title);
        setEnsureClickListener(listener);
    }
    
    public void setCancelClickListener(OnClickListener listener) {
        mBtnCancel = (Button) mRootView.findViewById(R.id.btn_cancel);
        mBtnCancel.setVisibility(View.VISIBLE);
        mBtnCancel.setOnClickListener(listener);
    }

    public void setEnsureClickListener(OnClickListener listener) {
        mBtnEnsure = (Button) mRootView.findViewById(R.id.btn_ensure);
        mBtnEnsure.setVisibility(View.VISIBLE);
        mBtnEnsure.setOnClickListener(listener);
    }

    public void setOnWheelChangedListener(OnWheelChangedListener listener) {
        mWheelView.addChangingListener(listener);
    }
    
    public int getItemPosition(){
        return mWheelView.getCurrentItem();
    }
    
    public void dismissDialog(){
        if (null != mSelectDialog) {
            mSelectDialog.dismiss();
        }
    }
    
    public void showDialog() {
        if (null == mSelectDialog) {
            mSelectDialog = new Dialog(mContext, R.style.DatetimePickerDialog);
            Window window = mSelectDialog.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = LayoutParams.MATCH_PARENT;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.BOTTOM;
            mSelectDialog.getWindow().setAttributes(layoutParams);
            mSelectDialog.setContentView(mRootView);
        }
        if (mBtnCancel == null) {
            setCancelClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    mSelectDialog.dismiss();
                }
            });
        }
        if (mBtnEnsure == null) {
            setEnsureClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    mSelectDialog.dismiss();
                }
            });
        }
        mSelectDialog.show();
    }
}
