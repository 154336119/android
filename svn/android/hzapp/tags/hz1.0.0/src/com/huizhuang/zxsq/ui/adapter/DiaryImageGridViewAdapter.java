package com.huizhuang.zxsq.ui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.ui.adapter.base.MyBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 日记列表文章图片Adapter
 * 
 * @ClassName: DiaryImageGridViewAdapter.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-28 上午11:33:23
 */
public class DiaryImageGridViewAdapter extends MyBaseAdapter<Atlas> {

	private ViewHolder mViewHolder;

	private int mIvWidth;
	private int mIvHeight;

	public DiaryImageGridViewAdapter(Context context, ArrayList<Atlas> list, int column) {
		super(context, list);
		mIvWidth = (UiUtil.getScreenWidth((Activity) context) - UiUtil.dp2px(context, 16 + (column - 1) * 8)) / column;
		if (1 == column) {
			mIvHeight = mIvWidth / 2;
		} else {
			mIvHeight = mIvWidth;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Atlas atlas = getItem(position);
		LogUtil.d("getView position = " + position + " Atlas = " + atlas);

		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.atlas_style_listview_item, viewGroup, false);
			mViewHolder = new ViewHolder();
			mViewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			mViewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
			mViewHolder.ivPhoto.setLayoutParams(new LinearLayout.LayoutParams(mIvWidth, mIvHeight));
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(atlas.getImage(), mViewHolder.ivPhoto, ImageLoaderOptions.getDefaultImageOption());
		String photoName = atlas.getName();
		if (null != photoName && photoName.length() > 0) {
			mViewHolder.tvName.setVisibility(View.VISIBLE);
			mViewHolder.tvName.setText(photoName);
		} else {
			mViewHolder.tvName.setVisibility(View.GONE);
		}

		return convertView;
	}

	static class ViewHolder {
		private ImageView ivPhoto;
		private TextView tvName;
	}

}
