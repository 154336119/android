package com.huizhuang.zxsq.widget.component;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;

/**
 * 过滤下拉拦(工长列表)
 * @ClassName: FilterForemanAdapter
 * @author wumaojie.gmail.com  
 * @date 2015-2-4 上午9:50:02
 */
public class FilterForemanAdapter extends CommonBaseAdapter<KeyValue> {

	private int barPostion = -1;
	private String barText = null;

    private String mSelectKeyValueId;

	public FilterForemanAdapter(Context context) {
		super(context);
	}

	public void setSeparateBar(int postion, String text) {
		barPostion = postion;
		barText = text;
	}
    public void setSelectedKeyValueId(String selectKeyValueId) {
        mSelectKeyValueId = selectKeyValueId;
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.adapter_filter_foreman, null);
			viewHolder.tvPrivilegeType = (TextView) convertView
					.findViewById(R.id.tv_privilege_type);
			viewHolder.tvPrivilegeName = (TextView) convertView
					.findViewById(R.id.tv_privilege_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		KeyValue keyValue = getItem(position);

		viewHolder.tvPrivilegeType.setVisibility(View.GONE);
		viewHolder.tvPrivilegeName.setVisibility(View.VISIBLE);
		viewHolder.tvPrivilegeName.setText(keyValue.getName());

		if (barPostion == position) {
			viewHolder.tvPrivilegeType.setVisibility(View.VISIBLE);
			viewHolder.tvPrivilegeType.setText(barText);
		}

        if (keyValue.getId().equalsIgnoreCase(mSelectKeyValueId)) {
            viewHolder.tvPrivilegeName.setEnabled(true);
        } else {
            viewHolder.tvPrivilegeName.setEnabled(false);
        }

		return convertView;
	}

	static class ViewHolder {
		private TextView tvPrivilegeType;
		private TextView tvPrivilegeName;
	}

}