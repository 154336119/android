package com.huizhuang.zxsq.widget.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;

public class SearchConditionItemActionBar extends RelativeLayout{

	private TextView mTvConditionName;
	private TextView mTvCondition;
	
	public SearchConditionItemActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public SearchConditionItemActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public SearchConditionItemActionBar(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.search_sift_item_bar, this);
		mTvConditionName = (TextView) view.findViewById(R.id.tv_condition_name);
		mTvCondition = (TextView) view.findViewById(R.id.tv_condition);
	}
	
	public void setOnClick(OnClickListener onClickListener) {
		this.setOnClickListener(onClickListener);
	}
	
	public void setConditionNameTxt(String txt){
		mTvConditionName.setText(txt);
	}
	public void setConditionTxt(String txt){
		mTvCondition.setText(txt);
	}
}
