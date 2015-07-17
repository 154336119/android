/*
 * Copyright 2014-2024 Helome. All rights reserved. 
 */
package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.huizhuang.hz.R;

/**
 * 主页底部导航条
 * 
 * @ClassName: MainBottomBar.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-12-3 上午10:09:46
 */
public class MainBottomBar extends RelativeLayout {

	public enum BottomBarFragmentType {
		HOME, COMPANY, DIARY, ATLAS, ACCOUNT
	}

	private RadioGroup mRadioGroup;

	public MainBottomBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public MainBottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public MainBottomBar(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.layout_main_bottom_bar, this);
		mRadioGroup = (RadioGroup) rootLayout.findViewById(R.id.bottom_radio_group);
	}

	/**
	 * 获得RadioGroup中的Button个数
	 * 
	 * @return
	 */
	public int getItemCount() {
		return mRadioGroup.getChildCount();
	}

	/**
	 * 模拟点击事件
	 * 
	 * @param itemPostion
	 */
	public void setItemPerformClick(BottomBarFragmentType bottomBarFragmentType) {
		mRadioGroup.getChildAt(bottomBarFragmentType.ordinal()).performClick();
	}
	
	public int getCheckedRadioButtonId(){
		return mRadioGroup.getCheckedRadioButtonId();
	}
	/**
	 * 设置RadioGroupItem选中事件
	 * 
	 * @param onCheckedChangeListener
	 */
	public void setBottomItemCheckedListener(RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
		mRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
	}

}
