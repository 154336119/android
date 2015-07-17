package com.huizhuang.zxsq.ui.activity.account;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.ui.adapter.FamilyProfilePagerAdapter;
import com.huizhuang.zxsq.ui.activity.base.BaseFragmentActivity;
import com.huizhuang.zxsq.ui.fragment.DecorationListFragment;
import com.huizhuang.zxsq.ui.fragment.HouseInfoFragment;
import com.huizhuang.zxsq.ui.fragment.HouseInfoFragment.OnFragmentDataLoadListener;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * 爱家档案
 * 
 * @ClassName: LoveFamilyProfileActivity
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-28 上午11:02:00
 */
public class LoveFamilyProfileActivity extends BaseFragmentActivity implements OnClickListener {

	/**
	 * Intent Extra
	 */
	public static final String EXTRA_USER_ID = "userId";

	/**
	 * 字体的颜色
	 */
	private static final int COLOR_NORMAL = Color.parseColor("#999999");
	private static final int COLOR_CHECKED = Color.parseColor("#ff6c38");

	/**
	 * TAB（房屋信息、装修清单）
	 */
	private static final int TAB_HOUSE_INFO = 0;
	private static final int TAB_DECORATION_LIST = 1;

	private CommonActionBar mCommonActionBar;
	private DataLoadingLayout mDataLoadingLayout;
	private TextView mTvTabHouse;
	private View mTabHouseLine;
	private TextView mTvTabDecoration;
	private View mTabDecorationLine;
	private ViewPager mViewPager;
	private FamilyProfilePagerAdapter mPagerAdapter;
	private HouseInfoFragment mHouseInfoFragment;

	private String mUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_love_famliy_profile);
		AnalyticsUtil.onEvent(this, UmengEvent.ID_LOVE_FAMILY_ACCOUNT);

		getIntentExtras();
		initActionBar();
		initViews();
	}

	private void getIntentExtras() {
		LogUtil.d("getIntentExtras");

		Intent intent = getIntent();
		if (null != intent) {
			mUserId = getIntent().getStringExtra(EXTRA_USER_ID);
		}
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_love_famliy_profile);
		mCommonActionBar.setRightTxtBtn(R.string.btn_finish, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnFinshOnClick(v);
			}
		});
		mCommonActionBar.setRightTxtBtnVisibility(View.GONE);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnBackOnClick(v);
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initViews");

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.love_famliy_profile_data_loading_layout);
		mDataLoadingLayout.showDataLoading();

		// TAB栏
		mTvTabHouse = (TextView) findViewById(R.id.tv_tab_house);
		mTvTabDecoration = (TextView) findViewById(R.id.tv_tab_decoration);
		mTvTabHouse.setOnClickListener(this);
		mTvTabDecoration.setOnClickListener(this);
		mTvTabHouse.setTextColor(COLOR_CHECKED);

		// 初始化Tab下面的Line
		mTabHouseLine = findViewById(R.id.v_tab_house_line);
		mTabDecorationLine = findViewById(R.id.v_tab_decoration_line);
		mTabDecorationLine.setVisibility(View.INVISIBLE);

		// ViewPager
		mViewPager = (ViewPager) findViewById(R.id.famliy_profile_viewpager);
		ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
		mHouseInfoFragment = new HouseInfoFragment();
		mHouseInfoFragment.setOnFragmentDataLoadListener(new OnFragmentDataLoadListener() {

			@Override
			public void onDataLoadSuccess() {
				mDataLoadingLayout.showDataLoadSuccess();
				// 用户ID为空，则用户为登录者自己
				if (null != mUserId) {
					mCommonActionBar.setRightTxtBtnVisibility(View.GONE);
				} else {
					mCommonActionBar.setRightTxtBtnVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onDataLoadStart() {
				mDataLoadingLayout.showDataLoading();
				mCommonActionBar.setRightTxtBtnVisibility(View.GONE);
			}

			@Override
			public void onDataLoadFailed(String strError) {
				mDataLoadingLayout.showDataLoadFailed(strError);
				mCommonActionBar.setRightTxtBtnVisibility(View.GONE);
			}

		});
		DecorationListFragment decorationListFragment = new DecorationListFragment();
		mFragmentList.add(mHouseInfoFragment);
		mFragmentList.add(decorationListFragment);

		mPagerAdapter = new FamilyProfilePagerAdapter(getSupportFragmentManager(), mFragmentList);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(TAB_HOUSE_INFO);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case TAB_HOUSE_INFO:
					if (null != mUserId) {
						mCommonActionBar.setRightTxtBtnVisibility(View.GONE);
					} else {
						mCommonActionBar.setRightTxtBtnVisibility(View.VISIBLE);
					}
					mTvTabHouse.setTextColor(COLOR_CHECKED);
					mTabHouseLine.setVisibility(View.VISIBLE);
					mTvTabDecoration.setTextColor(COLOR_NORMAL);
					mTabDecorationLine.setVisibility(View.INVISIBLE);
					break;
				case TAB_DECORATION_LIST:
					mCommonActionBar.setRightTxtBtnVisibility(View.GONE);
					mTvTabHouse.setTextColor(COLOR_NORMAL);
					mTabHouseLine.setVisibility(View.INVISIBLE);
					mTvTabDecoration.setTextColor(COLOR_CHECKED);
					mTabDecorationLine.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	/**
	 * 返回按钮事件
	 * 
	 * @param v
	 */
	protected void btnBackOnClick(View v) {
		LogUtil.d("btnBackOnClick View = " + v);

		finish();
	}

	/**
	 * 完成按钮事件
	 * 
	 * @param v
	 */
	protected void btnFinshOnClick(View v) {
		LogUtil.d("btnFinshOnClick View = " + v);
		AnalyticsUtil.onEvent(this, UmengEvent.ID_LOVE_FAMILY_ACCOUNT_EDIT);
		mHouseInfoFragment.upDateRoomData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tab_house:
			mViewPager.setCurrentItem(TAB_HOUSE_INFO, true);
			break;
		case R.id.tv_tab_decoration:
			mViewPager.setCurrentItem(TAB_DECORATION_LIST, true);
			break;
		default:
			break;
		}
	}

}
