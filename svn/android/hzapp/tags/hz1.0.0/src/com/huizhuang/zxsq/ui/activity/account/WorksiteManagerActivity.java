package com.huizhuang.zxsq.ui.activity.account;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.ui.activity.base.BaseFragmentActivity;
import com.huizhuang.zxsq.ui.adapter.FragmentViewPagerAdapter;
import com.huizhuang.zxsq.ui.fragment.account.ForemanRecordFragment;
import com.huizhuang.zxsq.ui.fragment.account.WorksiteScheduleManagerFragment;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.PagerSlidingTabStrip;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/** 
* @ClassName: WorksiteManagerActivity 
* @Description: 工地管理
* @author th 
* @mail 342592622@qq.com   
* @date 2015-1-23 下午2:34:11 
*  
*/
public class WorksiteManagerActivity extends BaseFragmentActivity{

	private static String[] SUB_FRAGMENT;

	private ViewPager mPager;
	private FragmentViewPagerAdapter mAdapter;
	private PagerSlidingTabStrip mIndicator;
	private int mOrderId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_worksite_manager_tab);
		initExtraData();
		initActionBar();
		initViews();
		
	}
	
	private void initExtraData(){
		mOrderId = getIntent().getExtras().getInt(AppConstants.PARAM_ORDER_ID);
	}
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.txt_worksite_manager);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initViews(){
		SUB_FRAGMENT = new String[] {"工长记录","监理师巡查"};
		List<Fragment> fragments = new ArrayList<Fragment>();
		ForemanRecordFragment foremanRecordFragment = new ForemanRecordFragment();
		foremanRecordFragment.setOrderId(mOrderId);
		fragments.add(foremanRecordFragment);
		
		WorksiteScheduleManagerFragment worksiteScheduleManagerFragment = new WorksiteScheduleManagerFragment();
		worksiteScheduleManagerFragment.setOrderId(mOrderId);
		fragments.add(worksiteScheduleManagerFragment);
		
		mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, SUB_FRAGMENT);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(0);
		mIndicator = (PagerSlidingTabStrip)findViewById(R.id.indicator);
		mIndicator.setTextColor(getResources().getColor(R.color.color_808080));
		mIndicator.setViewPager(mPager);
	}
}
