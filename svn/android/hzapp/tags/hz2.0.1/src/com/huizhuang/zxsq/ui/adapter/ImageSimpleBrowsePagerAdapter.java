package com.huizhuang.zxsq.ui.adapter;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

/**
 * 图片浏览PagerAdapter（类似微信的图片浏览查看）
 * 
 * @ClassName: ImageSimpleBrowsePagerAdapter.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-27 下午5:59:57
 */
public class ImageSimpleBrowsePagerAdapter extends PagerAdapter {

	private LayoutInflater mLayoutInflater;
	private List<String> mImageUrlList;

	private Activity mCurrentActivity;

	public ImageSimpleBrowsePagerAdapter(Context context, List<String> imageUrlList) {
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageUrlList = imageUrlList;
	}

	public void setCurrentActivity(Activity currentActivity) {
		mCurrentActivity = currentActivity;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		View view = mLayoutInflater.inflate(R.layout.adapter_image_simple_browse_item, container, false);
		PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
		photoView.setOnViewTapListener(new OnViewTapListener() {
			@Override
			public void onViewTap(View arg0, float arg1, float arg2) {
				if (null != mCurrentActivity) {
					mCurrentActivity.finish();
				}
			}
		});
		String imageUrl = mImageUrlList.get(position);
		ImageLoader.getInstance().displayImage(imageUrl, photoView, ImageLoaderOptions.optionsDefaultEmptyPhoto);
		container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		return view;
	}

	@Override
	public int getCount() {
		return mImageUrlList.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
