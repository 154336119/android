package com.huizhuang.zxsq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.ui.activity.base.BaseFragmentActivity;
import com.huizhuang.zxsq.ui.fragment.AtlasFragment;
import com.huizhuang.zxsq.ui.fragment.CompanyFragment;
import com.huizhuang.zxsq.ui.fragment.DiaryFragment;
import com.huizhuang.zxsq.ui.fragment.HomeFragment3;
import com.huizhuang.zxsq.ui.fragment.account.AccountFargment;
import com.huizhuang.zxsq.ui.fragment.decoration.DecorationFragment;
import com.huizhuang.zxsq.ui.fragment.decoration.DecorationFragment.DecorationType;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.MainBottomBar;
import com.huizhuang.zxsq.widget.MainBottomBar.BottomBarFragmentType;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;

/**
 * 主界面
 * 
 * @ClassName: MainActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-12-3 下午3:01:34
 */
public class MainActivity extends BaseFragmentActivity {

	private FrameLayout mMainContainer;
	private MainBottomBar mMainBottomBar;
	private CommonAlertDialog mCommonAlertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.main_activity);

		initViews();
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initViews");

		mMainContainer = (FrameLayout) findViewById(R.id.main_container);

		mMainBottomBar = (MainBottomBar) findViewById(R.id.main_bottom_bar);
		mMainBottomBar.setBottomItemCheckedListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// instantiateItem从FragmentManager中查找Fragment，找不到就getItem新建一个，setPrimaryItem设置隐藏和显示，最后finishUpdate提交事务。
				Fragment fragment = (Fragment) mFragmentPagerAdapter.instantiateItem(mMainContainer, checkedId);
				mFragmentPagerAdapter.setPrimaryItem(mMainContainer, 0, fragment);
				mFragmentPagerAdapter.finishUpdate(mMainContainer);
			}
		});
		mMainBottomBar.setItemPerformClick(BottomBarFragmentType.HOME);
	}

	/**
	 * 初始化FragmentPagerAdapter
	 */
	private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

		@Override
		public Fragment getItem(int resId) {
			switch (resId) {
			case R.id.btn_company:
				return new DecorationFragment();
			case R.id.btn_diary:
				return new DiaryFragment();
			case R.id.btn_atlas:
				return new AtlasFragment();
			case R.id.btn_account:
				return new AccountFargment();
			case R.id.btn_home:
			default:
				return new HomeFragment3();
			}
		}

		@Override
		public int getCount() {
			return mMainBottomBar.getItemCount();
		}

	};

	/**
	 * 切换当前的Fragment
	 * 
	 * @param bottomBarFragmentType
	 */
	public void switchToFragment(BottomBarFragmentType bottomBarFragmentType) {
		mMainBottomBar.setItemPerformClick(bottomBarFragmentType);
	}

	/**
	 * 切换当前的Fragment到找装修tab并指定找装修tab里面的切换页
	 * 
	 * @param bottomBarFragmentType
	 */
	public void switchToDecorationFragment(DecorationType decorationType) {
		mMainBottomBar.setItemPerformClick(BottomBarFragmentType.COMPANY);
		if(mMainBottomBar.getCheckedRadioButtonId() == R.id.btn_company){
			DecorationFragment decorationFragment = (DecorationFragment) mFragmentPagerAdapter.instantiateItem(mMainContainer, mMainBottomBar.getCheckedRadioButtonId());
			if(decorationFragment != null){
				decorationFragment.switchToFragment(decorationType);
			}
		}
	}
	
	/**
	 * 切换为CompanyFragment并更新对应的数据
	 * 
	 * @param bundle
	 */
	public void updateCompanyData(Bundle bundle) {
		CompanyFragment companyFragment = (CompanyFragment) mFragmentPagerAdapter.instantiateItem(mMainContainer, R.id.btn_company);
		companyFragment.updateComditionData(bundle);
	}

	/**
	 * 切换为AtlasFragment并更新对应的数据
	 * 
	 * @param bundle
	 */
	public void updateAtlasData(Bundle bundle) {
		AtlasFragment atlasFragment = (AtlasFragment) mFragmentPagerAdapter.instantiateItem(mMainContainer, R.id.btn_atlas);
//		atlasFragment.updateComditionData(bundle);
	}

	/**
	 * 切换为DiaryFragment并更新对应的数据
	 * 
	 * @param bundle
	 */
	public void updateDiaryData(Bundle bundle) {
		DiaryFragment diaryFragment = (DiaryFragment) mFragmentPagerAdapter.instantiateItem(mMainContainer, R.id.btn_diary);
		diaryFragment.updateComditionData(bundle);
	}
	
	public void mainToForeman() {
		switchToFragment(BottomBarFragmentType.COMPANY);
	}
	public void mainToAtlas() {
		switchToFragment(BottomBarFragmentType.ATLAS);
	}
	public void mainToDiary() {
		switchToFragment(BottomBarFragmentType.DIARY);
	}

	@Override
	public void onBackPressed() {
		LogUtil.d("onBackPressed");

		showExitAlertDialog();
	}

	/**
	 * 显示退出提示对话框
	 */
	private void showExitAlertDialog() {
		LogUtil.d("showExitAlertDialog");

		if (null == mCommonAlertDialog) {
			mCommonAlertDialog = new CommonAlertDialog(this);
			mCommonAlertDialog.setTitle(R.string.txt_quit_app);
			mCommonAlertDialog.setMessage(R.string.txt_are_you_sure_to_quit);
			mCommonAlertDialog.setPositiveButton(R.string.txt_quit, new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCommonAlertDialog.dismiss();
					ZxsqApplication.getInstance().exit();
				}
			});
			mCommonAlertDialog.setNegativeButton(R.string.txt_cancel, new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCommonAlertDialog.dismiss();
				}
			});
		}
		mCommonAlertDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.e("activity onActivityResult()");
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
