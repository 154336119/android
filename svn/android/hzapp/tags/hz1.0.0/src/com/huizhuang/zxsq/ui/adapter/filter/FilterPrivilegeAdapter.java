package com.huizhuang.zxsq.ui.adapter.filter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;

/**
 * 过滤栏下拉列表的Adapter（优惠选项特有）
 * 
 * @ClassName: FilterPrivilegeAdapter.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2015-1-23 下午3:29:19
 */
public class FilterPrivilegeAdapter extends CommonBaseAdapter<KeyValue> {

	private static final int[] ICON_RESIDS = { R.drawable.ic_privilege_1, R.drawable.ic_privilege_2, R.drawable.ic_privilege_3, R.drawable.ic_privilege_4,
			R.drawable.ic_privilege_5, R.drawable.ic_privilege_6 };

	private String mSelectKeyValueId;

	public FilterPrivilegeAdapter(Context context) {
		super(context);
	}

	public void setSelectedKeyValueId(String selectKeyValueId) {
		mSelectKeyValueId = selectKeyValueId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_filter_privilege, null);
			viewHolder.tvPrivilegeType = (TextView) convertView.findViewById(R.id.tv_privilege_type);
			viewHolder.ivPrivilegeIcon = (ImageView) convertView.findViewById(R.id.iv_privilege_icon);
			viewHolder.tvPrivilegeName = (TextView) convertView.findViewById(R.id.tv_privilege_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		KeyValue keyValue = getItem(position);

		if (position == 0) {
			viewHolder.ivPrivilegeIcon.setVisibility(View.GONE);
		} else {
			viewHolder.ivPrivilegeIcon.setVisibility(View.VISIBLE);
			if (position <= ICON_RESIDS.length) {
				viewHolder.ivPrivilegeIcon.setImageResource(ICON_RESIDS[position - 1]);
			} else {
				viewHolder.ivPrivilegeIcon.setImageResource(ICON_RESIDS[ICON_RESIDS.length - 1]);
			}
		}

		if (position == 1) {
			viewHolder.tvPrivilegeType.setVisibility(View.VISIBLE);
			viewHolder.tvPrivilegeType.setText(R.string.txt_item_privilege_1);
		} else if (position == 3) {
			viewHolder.tvPrivilegeType.setVisibility(View.VISIBLE);
			viewHolder.tvPrivilegeType.setText(R.string.txt_item_privilege_2);
		} else {
			viewHolder.tvPrivilegeType.setVisibility(View.GONE);
		}

		if (keyValue.getId().equalsIgnoreCase(mSelectKeyValueId)) {
			viewHolder.tvPrivilegeName.setEnabled(true);
		} else {
			viewHolder.tvPrivilegeName.setEnabled(false);
		}
		viewHolder.tvPrivilegeName.setText(keyValue.getName());
		return convertView;
	}

	static class ViewHolder {
		private TextView tvPrivilegeType;
		private ImageView ivPrivilegeIcon;
		private TextView tvPrivilegeName;
	}

}