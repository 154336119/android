package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
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
 * 选择装修记账分类列表Adapter
 * 
 * @ClassName: BillCategoryListAdapter
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-30 下午7:30:00
 */
public class BillCategoryListAdapter extends CommonBaseAdapter<JourneyType> {

	public BillCategoryListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_bill_category_list, parent, false);
			viewHolder.itemImgIcon = (ImageView) convertView.findViewById(R.id.item_img_icon);
			viewHolder.itemTvLabel = (TextView) convertView.findViewById(R.id.item_tv_label);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		JourneyType journeyType = getItem(position);
		ImageLoader.getInstance().displayImage(journeyType.getImg(), viewHolder.itemImgIcon, ImageLoaderOptions.optionsBillAccoutingDefaultIcon);
		viewHolder.itemTvLabel.setText(journeyType.getName());
		return convertView;
	}

	private class ViewHolder {
		ImageView itemImgIcon;
		TextView itemTvLabel;
	}

}
