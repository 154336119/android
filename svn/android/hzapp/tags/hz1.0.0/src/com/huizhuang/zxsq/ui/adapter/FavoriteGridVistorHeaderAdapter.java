package com.huizhuang.zxsq.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.UserFavorSketch;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 收藏图片列表中访问者表格的头像Adapter
 * 
 * @ClassName: FavoriteGridVistorHeaderAdapter
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-31 上午10:13:00
 */
public class FavoriteGridVistorHeaderAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private List<UserFavorSketch.Visitor> mVisitorList;

	public FavoriteGridVistorHeaderAdapter(Context context, List<UserFavorSketch.Visitor> visitorList) {
		mInflater = LayoutInflater.from(context);
		mVisitorList = visitorList;
	}

	@Override
	public int getCount() {
		return (null == mVisitorList) ? 0 : mVisitorList.size();
	}

	@Override
	public UserFavorSketch.Visitor getItem(int position) {
		return mVisitorList.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.adapter_favorite_gird_vistor_header, parent, false);
			viewHolder.itemImgHeader = (ImageView) convertView.findViewById(R.id.item_img_header);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		UserFavorSketch.Visitor visitor = getItem(position);
		LogUtil.i("FavoriteGridVistorHeaderAdapter getView position = " + position + " UserFavorSketch.Visitor = " + visitor);

		// 设置需要显示的数据
		String headerUrl = null;
		if (null != visitor.getImage()) {
			headerUrl = visitor.getImage().getThumb_path();
		}
		ImageLoader.getInstance().displayImage(headerUrl, viewHolder.itemImgHeader, ImageLoaderOptions.optionsUserHeader);
		return convertView;
	}
	private class ViewHolder {
		ImageView itemImgHeader;
	}

}
