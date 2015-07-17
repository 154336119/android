package com.huizhuang.zxsq.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.UserGuideAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用户引导页面（首次安装程序时显示）
 * 
 * @ClassName: UserGuideActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-26 下午5:10:31
 */
public class UserGuideActivity extends BaseActivity {

	private static final int GUIDE_IMAGE_RESIDS[] = { R.drawable.welcome_page_1, R.drawable.welcome_page_2, R.drawable.welcome_page_3};

	private ViewPager mViewPager;
	// 2秒没有响应计时器
	private final int BACKKEY_DELAY_TIME = 2000;
	private Timer mTimer = new Timer();
	private boolean mIsNoActions = false;
	
	private boolean mIsScrolling = false;

	private Button mExperienceBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.user_guide_activity);

		initViews();
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initViews");
		List<ImageView> imageViewList = new ArrayList<ImageView>();
		for (int i = 0; i < GUIDE_IMAGE_RESIDS.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(GUIDE_IMAGE_RESIDS[i]);
			imageViewList.add(imageView);
		}
		UserGuideAdapter userGuideAdapter = new UserGuideAdapter(imageViewList);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mExperienceBtn = (Button)findViewById(R.id.btn_user_guide_experience);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 到达最后一页，开启2S无响应计时器
				if (position == GUIDE_IMAGE_RESIDS.length - 1) {
/*					mIsNoActions = true;
					if (mIsNoActions) {
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								if (mIsNoActions) {
									ZxsqApplication.getInstance().updateNewVersion();
									ActivityUtil.next(UserGuideActivity.this, HomeActivity.class, R.anim.fade_in, R.anim.fade_out, true);
								}
							}
						};
						mTimer.schedule(task, BACKKEY_DELAY_TIME);
					}*/
					ZxsqApplication.getInstance().updateNewVersion();
					mExperienceBtn.setVisibility(View.VISIBLE);
				} else {
					//mIsNoActions = false;
					mExperienceBtn.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				/*// 末页向左拉动时，进入首页
				if (position == GUIDE_IMAGE_RESIDS.length - 1 && positionOffset == 0 && positionOffsetPixels == 0 && mIsScrolling) {
					ZxsqApplication.getInstance().updateNewVersion();
					mIsNoActions = false;
					ActivityUtil.next(UserGuideActivity.this, HomeActivity.class, R.anim.fade_in, R.anim.fade_out, true);
				}*/
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				/*// 记录当前的滑动状态
				mIsScrolling = (state == 1) ? true : false;*/
			}
		});
		mExperienceBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtil.next(UserGuideActivity.this, HomeActivity.class, R.anim.fade_in, R.anim.fade_out, true);
			}
		});
		
		mViewPager.setAdapter(userGuideAdapter);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onBackPressed() {
		LogUtil.d("onBackPressed");
		// 屏蔽Back按键
		// super.onBackPressed();
	}
}
