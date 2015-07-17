package com.huizhuang.zxsq.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Atlas;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class AtlasBrowseViewPagerAdapter extends PagerAdapter {

	private LayoutInflater mLayoutInflater;
	
	private DisplayImageOptions mOptions;
	private ArrayList<Atlas> mList;

	public AtlasBrowseViewPagerAdapter(Context context, ArrayList<Atlas> list) {
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = list;
		mOptions = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.NONE)
				// 设置图片缩放
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisk(true).build();
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		View view = mLayoutInflater.inflate(R.layout.atlas_browse_viewpager_item, container, false);
		ImageView ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
		
		Atlas photo = mList.get(position);
		ImageLoader.getInstance().displayImage(photo.getImage(), ivPhoto, mOptions);
		container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		return view;
	}

	@Override
	public int getCount() {
		return mList.size();
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
