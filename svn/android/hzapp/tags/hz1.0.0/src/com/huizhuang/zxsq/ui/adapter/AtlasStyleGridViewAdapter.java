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
import com.huizhuang.zxsq.utils.UiUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AtlasStyleGridViewAdapter extends MyBaseAdapter<Atlas>{

	private ViewHolder mHolder;
	private int mWidth;
	
	public AtlasStyleGridViewAdapter(Context context){
		super(context);
		mWidth = (UiUtil.getScreenWidth((Activity)context) - UiUtil.dp2px(context, 20))/4;
	}
	
	public AtlasStyleGridViewAdapter(Context context, ArrayList<Atlas> list){
		super(context,list);
		mWidth = (UiUtil.getScreenWidth((Activity)context) - UiUtil.dp2px(context, 20))/4;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			mHolder = new ViewHolder();
			view = getLayoutInflater().inflate(R.layout.atlas_style_listview_item, viewGroup, false);
			mHolder.ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
			mHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
			
			mHolder.ivPhoto.setLayoutParams(new LinearLayout.LayoutParams(mWidth, mWidth));
			view.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) view.getTag();
		}
		Atlas style = getList().get(position);
		mHolder.tvName.setText(style.getName());
		ImageLoader.getInstance().displayImage(style.getImage(), mHolder.ivPhoto);
		return view;
	}

	static class ViewHolder {
		ImageView ivPhoto;
		TextView tvName;
	}

}
