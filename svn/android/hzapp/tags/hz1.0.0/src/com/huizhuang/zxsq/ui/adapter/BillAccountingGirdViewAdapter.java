/*
 * Copyright 2014-2024 Helome. All rights reserved.
 */
package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.Constant.JourneyType;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 记账页面分类列表Adapter
 * 
 * @ClassName: BillAccountingGirdViewAdapter
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-03 10:25:00
 */
public class BillAccountingGirdViewAdapter extends CommonBaseAdapter<JourneyType> {

	public BillAccountingGirdViewAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_bill_accounting_gridview, parent, false);
			viewHolder.itemCategoryIcon = (ImageView) convertView.findViewById(R.id.item_category_icon);
			viewHolder.itemCategoryName = (TextView) convertView.findViewById(R.id.item_category_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		JourneyType journeyType = getItem(position);
		ImageLoader.getInstance().displayImage(journeyType.getImg(), viewHolder.itemCategoryIcon, ImageLoaderOptions.optionsBillAccoutingDefaultIcon);
		viewHolder.itemCategoryName.setText(journeyType.getName());
		return convertView;
	}

	static class ViewHolder {
		private ImageView itemCategoryIcon;
		private TextView itemCategoryName;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (null != observer) {
			super.unregisterDataSetObserver(observer);
		}
	}

}
