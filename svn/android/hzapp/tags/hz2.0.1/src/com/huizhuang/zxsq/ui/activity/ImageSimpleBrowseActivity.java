package com.huizhuang.zxsq.ui.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.adapter.ImageSimpleBrowsePagerAdapter;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.HackyViewPager;

/**
 * 图片浏览页面（类似微信的图片浏览查看）
 * 
 * @ClassName: ImageSimpleBrowseActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-27 下午5:57:45
 */
public class ImageSimpleBrowseActivity extends BaseActivity {

	public static final String EXTRA_IMAGE_URLS = "image_urls";
	public static final String EXTRA_POSITION = "position";

	private ViewPager mViewPager;
	private ImageSimpleBrowsePagerAdapter mImageSimpleBrowsePagerAdapter;
	private LinearLayout mIndicatorViewGroup;

	private List<String> mImageUrlList;
	private int mLastIndicator = 0;
	private int mCurPostion = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_simple_browse);

		getIntentExtras();
		initViews();
	}

	/**
	 * 获得Intent传递的Extras参数
	 */
	private void getIntentExtras() {
		LogUtil.d("getIntentExtras");

		mImageUrlList = (List<String>) getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
		mCurPostion = getIntent().getExtras().getInt(EXTRA_POSITION);
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initViews");

		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		mImageSimpleBrowsePagerAdapter = new ImageSimpleBrowsePagerAdapter(this, mImageUrlList);
		mImageSimpleBrowsePagerAdapter.setCurrentActivity(this);
		mViewPager.setAdapter(mImageSimpleBrowsePagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mIndicatorViewGroup.getChildAt(position).setEnabled(true);
				mIndicatorViewGroup.getChildAt(mLastIndicator).setEnabled(false);
				mLastIndicator = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		initIndicator(mImageUrlList.size());
		mViewPager.setCurrentItem(mCurPostion);
	}

	/**
	 * 初始化小圆点指示区域
	 * 
	 * @param pageTotalCount
	 *            小圆点个数
	 */
	private void initIndicator(int pageTotalCount) {
		mIndicatorViewGroup = (LinearLayout) findViewById(R.id.layout_indicator);
		if (pageTotalCount > 0) {
			int imgViewPadding = 5;
			for (int i = 0; i < pageTotalCount; i++) {
				ImageView imageView = new ImageView(this);
				imageView.setPadding(imgViewPadding, imgViewPadding, imgViewPadding, imgViewPadding);
				LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				imageView.setLayoutParams(layoutParams);
				imageView.setImageResource(R.drawable.btn_viewpager_dot);
				imageView.setEnabled(false);
				mIndicatorViewGroup.addView(imageView);
			}
			mIndicatorViewGroup.getChildAt(mCurPostion).setEnabled(true);// 默认第一个为选中状态
		}

		if (mIndicatorViewGroup.getChildCount() > 0) {
			mIndicatorViewGroup.setVisibility(View.VISIBLE);
		}
	}

}
