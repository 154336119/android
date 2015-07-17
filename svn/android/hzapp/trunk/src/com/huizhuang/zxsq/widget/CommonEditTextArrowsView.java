package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;

public class CommonEditTextArrowsView extends RelativeLayout {

    private Context mContext;
    private TextView mTxtTile;
    private EditText mTxtInfo;
    private TextView mTvLable;
    private ImageView mImgArr;
    private boolean isArrowClicked;

    public CommonEditTextArrowsView(Context context) {
        super(context);
        mContext = context;
        initViews();
    }

    public CommonEditTextArrowsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    private void initViews() {
        View rootLayout = LayoutInflater.from(mContext).inflate(R.layout.widget_edittext_arrows_view, this);
        mTxtTile = (TextView) rootLayout.findViewById(R.id.tv_title);
        mTxtInfo = (EditText) rootLayout.findViewById(R.id.et_info);
        mTvLable = (TextView) rootLayout.findViewById(R.id.tv_lable);
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
    
    public String getInfo(){
        return mTxtInfo.getText().toString().trim();
    }

    public void setRightImage(Drawable drawable) {
        mImgArr.setImageDrawable(drawable);
    }
    
    public void setLable(String lable){
        mTvLable.setText(lable);
    }
    
    public void setInfoHint(String hint){
        mTxtInfo.setHint(hint);
    }
    
    public void setInfoInputType(int type){
        mTxtInfo.setInputType(type);
    }
    
    public void setInfoDigits(String digits){
        mTxtInfo.setKeyListener(DigitsKeyListener.getInstance(digits));   
    }

    public void setInfoEnabled(boolean enabled){
        mTxtInfo.setEnabled(enabled);
    }
    
    public void hideInfo() {
        mTxtInfo.setVisibility(View.GONE);
    }

    public void setHideArrowImg(){
        mImgArr.setVisibility(View.INVISIBLE);
    }

    public void setOnArrowClickListener(OnClickListener l) {
        mImgArr.setOnClickListener(l);
    }

    public boolean isArrowClicked() {
        return isArrowClicked;
    }

    public void setArrowClicked(boolean isArrowClicked) {
        this.isArrowClicked = isArrowClicked;
    }

    /**
     * 设置为数字模式，且最多输入8个数字
     */
    public void setInputOnlyNumber() {
        mTxtInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
        mTxtInfo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });
    }
    
    public void setOneLotListener() {
        mTxtInfo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int d = temp.indexOf(".");
                if (d < 0)
                    return;
                if (temp.length() - d - 1 > 1) {
                    s.delete(d + 2, d + 3);
                } else if (d == 0) {
                    s.delete(d, d );
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }
    
    /**
     * 改变中间info文本框
     * 
     * @param v
     */
    public void changeInfoView(View v) {

    }

}
