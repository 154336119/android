package com.huizhuang.zxsq.widget;


import com.huizhuang.hz.R;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuoteItemDetailView extends LinearLayout {
    
    private Context mContext;
    private TextView mTxtTitle;
    private TextView mTxtArea;
    private TextView mTextInfo;
    public QuoteItemDetailView(Context context) {
        super(context);
        mContext=context;
        initViews();
    }


    public QuoteItemDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initViews();
    }
    
    private void initViews() {
        View rootLayout = LayoutInflater.from(mContext).inflate(R.layout.item_quote_detail, this);
        mTxtTitle = (TextView) rootLayout.findViewById(R.id.tv_title);
        mTxtArea = (TextView) rootLayout.findViewById(R.id.tv_area);
        mTextInfo = (TextView) rootLayout.findViewById(R.id.tv_info);
        setClickable(false);
        setFocusable(false);
    }
    public void setInfos(String title,String area,String info){
        mTxtTitle.setText(title);
        mTextInfo.setText(info);
        mTxtArea.setText(Html.fromHtml("<font color='#ff6c38'>"+area+"</font>"));
    }

    

}
