package com.huizhuang.zxsq.widget;



import com.huizhuang.hz.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CostChangeeItemTitleView extends LinearLayout {
    
    
    private Context mContext;
    private ImageView mImgArr;
    private TextView mTxtTitle;
    public CostChangeeItemTitleView(Context context) {
        super(context);
        mContext=context;
        initViews();
    }
    
    private void initViews() {
        View rootLayout = LayoutInflater.from(mContext).inflate(R.layout.adapter_cost_change_parent, this);
        mImgArr = (ImageView) rootLayout.findViewById(R.id.img_arr);
        mTxtTitle = (TextView) rootLayout.findViewById(R.id.tv_name);
    }

    public CostChangeeItemTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initViews();
    }
    public void setTitle(String text){
        mTxtTitle.setText(text);
    }
    
    public void changeArrState(boolean isOpen){
        if(isOpen){
            mImgArr.setImageResource(R.drawable.ic_cost_change_arr_down);
        }else{
            mImgArr.setImageResource(R.drawable.ic_cost_change_arr_left);
        }
    }

   

}
