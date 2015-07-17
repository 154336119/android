package com.huizhuang.zxsq.ui.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.BillDetailMonth.BillDetailItem;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.StringEncodeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 账单统计分类列表Adapter（按月）
 * 
 * @ClassName: BillMonthCategoryListAdapter
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-04 15:30:00
 */
public class BillMonthCategoryListAdapter extends CommonBaseAdapter<BillDetailItem> {

	private double mTotalMoney;
	private boolean[] mRemarkHideStateArrays;

	public BillMonthCategoryListAdapter(Context context) {
		super(context);
	}

	public void setDetailItemList(List<BillDetailItem> billDetailItemList, double totalMoney) {
		setList(billDetailItemList);
		if (null != billDetailItemList) {
			mRemarkHideStateArrays = new boolean[billDetailItemList.size()];
			// 默认所有都备注都是收起的
			Arrays.fill(mRemarkHideStateArrays, true);
		}
		mTotalMoney = totalMoney;
	}

	public void onCategoryItemClick(int position) {
		mRemarkHideStateArrays[position] = !mRemarkHideStateArrays[position];
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_bill_month_category_list, parent, false);
			viewHolder.itemImgIndicator = (ImageView) convertView.findViewById(R.id.item_img_indicator);
			viewHolder.itemTvName = (TextView) convertView.findViewById(R.id.item_tv_name);
			viewHolder.itemTvPercentage = (TextView) convertView.findViewById(R.id.item_tv_percentage);
			viewHolder.itemTvPrice = (TextView) convertView.findViewById(R.id.item_tv_moeny);
			viewHolder.itemRemarkZone = (ViewGroup) convertView.findViewById(R.id.ll_remark_zone);
			viewHolder.itemImgRemarkPhoto = (ImageView) convertView.findViewById(R.id.item_img_remark_photo);
			viewHolder.itemTvRemarkDetail = (TextView) convertView.findViewById(R.id.item_tv_remark_detail);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		BillDetailItem billDetailItem = getItem(position);
		viewHolder.itemTvName.setText(billDetailItem.getcTypeName());
		double ratio = 0;
		if (mTotalMoney != 0) {
			ratio = billDetailItem.getTotal() * 100 / mTotalMoney;
			ratio = StringEncodeUtil.get2Double(ratio);
		}
		viewHolder.itemTvPercentage.setText(mContext.getString(R.string.txt_budget_percentage, ratio));
		viewHolder.itemTvPrice.setText(mContext.getString(R.string.txt_budget_format, billDetailItem.getTotal()));

		String remarkImgUrl = null;
		if (billDetailItem.getImgs() != null && billDetailItem.getImgs().size() > 0 && billDetailItem.getImgs().get(0).getImg_path() != null) {
			remarkImgUrl = billDetailItem.getImgs().get(0).getImg_path();
		}
		String strRemark = billDetailItem.getDetail();
		if (null == remarkImgUrl && (null == strRemark || strRemark.length() == 0)) {
			viewHolder.itemRemarkZone.setVisibility(View.GONE);
			viewHolder.itemImgIndicator.setVisibility(View.INVISIBLE);
		} else {
			ImageLoader.getInstance().displayImage(remarkImgUrl, viewHolder.itemImgRemarkPhoto, ImageLoaderOptions.optionsDefaultEmptyPhoto);
			viewHolder.itemTvRemarkDetail.setText(billDetailItem.getDetail());
			if (mRemarkHideStateArrays[position]) {
				viewHolder.itemImgIndicator.setImageResource(R.drawable.ic_bill_item_unfolder);
				viewHolder.itemRemarkZone.setVisibility(View.GONE);
			} else {
				viewHolder.itemImgIndicator.setImageResource(R.drawable.ic_bill_item_folder);
				viewHolder.itemRemarkZone.setVisibility(View.VISIBLE);
			}
		}

		return convertView;
	}

	static class ViewHolder {
		private ImageView itemImgIndicator;
		private TextView itemTvName;
		private TextView itemTvPercentage;
		private TextView itemTvPrice;
		private ViewGroup itemRemarkZone;
		private ImageView itemImgRemarkPhoto;
		private TextView itemTvRemarkDetail;
	}

}
