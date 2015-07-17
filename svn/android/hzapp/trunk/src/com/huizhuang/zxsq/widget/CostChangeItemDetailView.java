package com.huizhuang.zxsq.widget;


import com.huizhuang.hz.R;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CostChangeItemDetailView extends LinearLayout {
    
    private Context mContext;
    private TextView mTxtName;
    private TextView mTxtContent;
    public CostChangeItemDetailView(Context context) {
        super(context);
        mContext=context;
        initViews();
    }


    public CostChangeItemDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initViews();
    }
    
    private void initViews() {
        View rootLayout = LayoutInflater.from(mContext).inflate(R.layout.adapter_cost_change_child, this);
        mTxtName = (TextView) rootLayout.findViewById(R.id.tv_name);
        mTxtContent = (TextView) rootLayout.findViewById(R.id.tv_content);
        setClickable(false);
        setFocusable(false);
    }
    public void setInfos(String title,String info){
    	mTxtName.setText(title);
    	mTxtContent.setText(info);
    }

    

}
