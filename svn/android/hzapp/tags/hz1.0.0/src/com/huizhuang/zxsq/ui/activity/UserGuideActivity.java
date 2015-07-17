package com.huizhuang.zxsq.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.UserGuideAdapter;
import com.huizhuang.zxsq.utils.LogUtil;

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

    private int images[];

	private ViewPager mViewPager;
	// 2秒没有响应计时器
	private final int BACKKEY_DELAY_TIME = 2000;
	private Timer mTimer = new Timer();
	private boolean mIsNoActions = false;
	
	private boolean mIsScrolling = false;

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
		try {
		    ApplicationInfo appInfo = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
            PackageManager.GET_META_DATA);
            int count = appInfo.metaData.getInt("INDEX_TYPE");
            switch (count) {
                case 4:
                    images = new int[4];
                    images[0] = R.drawable.welcome_page_1;
                    images[1] = R.drawable.welcome_page_2;
                    images[2] = R.drawable.welcome_page_3;
                    images[3] = R.drawable.welcome_page_4;
                    break;
                case 5:
                    images = new int[5];
                    images[0] = R.drawable.welcome_page_1;
                    images[1] = R.drawable.welcome_page_2;
                    images[2] = R.drawable.welcome_page_3;
                    images[3] = R.drawable.welcome_page_4;
                    images[4] = R.drawable.welcome_page_5;
                    break;
                default:
                    break;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
		List<ImageView> imageViewList = new ArrayList<ImageView>();
		for (int i = 0; i < images.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(images[i]);
			imageViewList.add(imageView);
		}
		UserGuideAdapter userGuideAdapter = new UserGuideAdapter(imageViewList);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 到达最后一页，开启2S无响应计时器
				if (position == images.length - 1) {
					mIsNoActions = true;
					if (mIsNoActions) {
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								if (mIsNoActions) {
									ZxsqApplication.getInstance().updateNewVersion();
									finish();
								}
							}
						};
						mTimer.schedule(task, BACKKEY_DELAY_TIME);
					}
				} else {
					mIsNoActions = false;
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// 末页向左拉动时，退出进入首页
				if (position == images.length - 1 && positionOffset == 0 && positionOffsetPixels == 0 && mIsScrolling) {
					ZxsqApplication.getInstance().updateNewVersion();
					mIsNoActions = false;
					finish();
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// 记录当前的滑动状态
				mIsScrolling = (state == 1) ? true : false;
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
