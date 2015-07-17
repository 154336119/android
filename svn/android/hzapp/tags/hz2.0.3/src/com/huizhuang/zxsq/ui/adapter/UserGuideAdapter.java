package com.huizhuang.zxsq.ui.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 用户使用引导页PagerAdapter
 * 
 * @ClassName: UserGuideAdapter.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-26 下午5:20:03
 */
public class UserGuideAdapter extends PagerAdapter {

	private List<ImageView> mImageViewList;

	public UserGuideAdapter(List<ImageView> imageViewList) {
		mImageViewList = imageViewList;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mImageViewList.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mImageViewList.get(position), 0);
		return mImageViewList.get(position);
	}

	@Override
	public int getCount() {
		return mImageViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
