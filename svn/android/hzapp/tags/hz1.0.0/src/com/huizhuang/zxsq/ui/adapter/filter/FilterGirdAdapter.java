package com.huizhuang.zxsq.ui.adapter.filter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;

/**
 * 公用筛选GridView的Adapter
 * 
 * @ClassName: FilterGirdAdapter.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2015-1-22 上午11:06:10
 */
public class FilterGirdAdapter extends CommonBaseAdapter<KeyValue> {

	private SparseArray<String> mSparseArray;

	public FilterGirdAdapter(Context context) {
		super(context);
		mSparseArray = new SparseArray<String>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		KeyValue keyValue = getItem(position);

		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = mLayoutInflater.inflate(R.layout.adapter_filter_grid, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.itemCheckBox = (CheckBox) convertView.findViewById(R.id.chb_item);
			viewHolder.itemCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
					int position = (Integer) compoundButton.getTag();
					if (isChecked) {
						mSparseArray.put(position, getItem(position).getName());
					} else {
						mSparseArray.remove(position);
					}
					if (null != mOnFilterItemCheckListener) {
						mOnFilterItemCheckListener.onCheckedChanged(getSelectStyleFormatString());
					}
				}
			});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.itemCheckBox.setText(keyValue.getName());
		viewHolder.itemCheckBox.setTag(position);
		if (null != mSparseArray.get(position)) {
			viewHolder.itemCheckBox.setChecked(true);
		} else {
			viewHolder.itemCheckBox.setChecked(false);
		}

		return convertView;
	}

	static class ViewHolder {
		private CheckBox itemCheckBox;
	}

	/**
	 * 清空所有选项
	 */
	public void resetSelectItems() {
		mSparseArray.clear();
		notifyDataSetChanged();
	}

	/**
	 * 获取选中的样式的格式化字符串（选项0+选项1）
	 * 
	 * @return
	 */
	private String getSelectStyleFormatString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int index = 0; index < mSparseArray.size(); index++) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append("+");
			}
			stringBuilder.append(mSparseArray.valueAt(index));
		}
		return stringBuilder.toString();
	}

	/**
	 * 获取选中的样式ID的格式化字符串（1,3,4）
	 * 
	 * @return
	 */
	public String getSelectKeyValueIds() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int index = 0; index < mSparseArray.size(); index++) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(",");
			}
			stringBuilder.append(mSparseArray.keyAt(index));
		}
		return stringBuilder.toString();
	}

	public interface OnFilterItemCheckListener {
		void onCheckedChanged(String strItems);
	}

	private OnFilterItemCheckListener mOnFilterItemCheckListener;

	public void setOnFilterItemCheckListener(OnFilterItemCheckListener onFilterItemCheckListener) {
		mOnFilterItemCheckListener = onFilterItemCheckListener;
	}

}
