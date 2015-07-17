package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;

public class CommonTextViewArrowsView extends RelativeLayout {

    private Context mContext;
    private TextView mTxtTile;
    private TextView mTxtInfo;
    private ImageView mImgArr;
    private boolean isArrowClicked = true;

    public CommonTextViewArrowsView(Context context) {
        super(context);
        mContext = context;
        initViews();
    }

    public CommonTextViewArrowsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    private void initViews() {
        View rootLayout = LayoutInflater.from(mContext).inflate(R.layout.widget_textview_arrows_view, this);
        mTxtTile = (TextView) rootLayout.findViewById(R.id.tv_title);
        mTxtInfo = (TextView) rootLayout.findViewById(R.id.tv_info);
        mImgArr = (ImageView) rootLayout.findViewById(R.id.iv_img_arr);
    }

    public void setTitleAndInfo(String title, String info) {
        mTxtTile.setText(title);
        mTxtInfo.setText(info);
    }

    public void setTitle(String title) {
        mTxtTile.setText(title);
    }

    public void setInfo(String info) {
        mTxtInfo.setText(info);
    }

    public void setInfoHint(String hint){
        mTxtInfo.setHint(hint);
    }

    public void setRightImage(Drawable drawable) {
        mImgArr.setImageDrawable(drawable);
    }

    public void hideInfo() {
        mTxtInfo.setVisibility(View.GONE);
    }

    public void setOnArrowClickListener(OnClickListener l) {
        mImgArr.setOnClickListener(l);
    }

    public boolean isArrowClicked() {
        return isArrowClicked;
    }
    
    public String getInfo(){
        return mTxtInfo.getText().toString().trim();
    }

    public void setArrowClicked(boolean isArrowClicked) {
        this.isArrowClicked = isArrowClicked;
        if(isArrowClicked){
            mImgArr.setImageDrawable(mContext.getResources().getDrawable(R.drawable.btn_green_on));
        }else{
            mImgArr.setImageDrawable(mContext.getResources().getDrawable(R.drawable.btn_green_off));
        }
    }

    /**
     * 改变中间info文本框
     * 
     * @param v
     */
    public void changeInfoView(View v) {

    }

}
