package com.huizhuang.zxsq.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.TypeSummary;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.StringEncodeUtil;

/**
 * 账单统计分类列表Adapter
 * 
 * @ClassName: BillTotalCategoryListAdapter
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-31 14:40:00
 */
public class BillTotalCategoryListAdapter extends CommonBaseAdapter<TypeSummary> {

	public BillTotalCategoryListAdapter(Context context) {
		super(context);
	}

	private double mTotalMoney;

	public void setTypeSummaryList(List<TypeSummary> typeSumList, double totalMoney) {
		setList(typeSumList);
		mTotalMoney = totalMoney;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_bill_total_category_list, parent, false);
			viewHolder.itemTvName = (TextView) convertView.findViewById(R.id.item_tv_name);
			viewHolder.itemTvPercentage = (TextView) convertView.findViewById(R.id.item_tv_percentage);
			viewHolder.itemTvPrice = (TextView) convertView.findViewById(R.id.item_tv_moeny);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TypeSummary typeSummary = getItem(position);
		viewHolder.itemTvName.setText(typeSummary.getcTypeName());
		double ratio = 0;
		if (mTotalMoney != 0) {
			ratio = typeSummary.getTotal() * 100 / mTotalMoney;
			ratio = StringEncodeUtil.get2Double(ratio);
		}
		viewHolder.itemTvPercentage.setText(mContext.getString(R.string.txt_budget_percentage, ratio));
		viewHolder.itemTvPrice.setText(mContext.getString(R.string.txt_budget_format, typeSummary.getTotal()));
		return convertView;
	}

	static class ViewHolder {
		private TextView itemTvName;
		private TextView itemTvPercentage;
		private TextView itemTvPrice;
	}

}
