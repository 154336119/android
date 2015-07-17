package com.huizhuang.zxsq.ui.adapter;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;

public class SelectFriendListViewAdapter extends CommonBaseAdapter<User> {

	private SparseArray<String> mSparseArray;

	@SuppressLint("UseSparseArrays")
	public SelectFriendListViewAdapter(Context context) {
		super(context);
		mSparseArray = new SparseArray<String>();
	}

	/**
	 * 获取选中的用户ID的格式化字符串（例如[172,365]）
	 * 
	 * @return
	 */
	public String getSelectPeopleIdFormatString() {
		ArrayList<String> strList = new ArrayList<String>();
		for (int i = 0; i < mSparseArray.size(); i++) {
			strList.add(mSparseArray.valueAt(i));
		}
		String[] strArray = strList.toArray(new String[strList.size()]);
		return Arrays.toString(strArray);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = mLayoutInflater.inflate(R.layout.item_recommend_friend, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.cv_icon);
			viewHolder.tvSex = (TextView) convertView.findViewById(R.id.tv_sex);
			viewHolder.tvCity = (TextView) convertView.findViewById(R.id.tv_city);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.ckbCheck = (CheckBox) convertView.findViewById(R.id.cb_select);
			viewHolder.ckbCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
					CheckBox CheckBox = (CheckBox) compoundButton;
					int position = (Integer) CheckBox.getTag();
					if (checked) {
						mSparseArray.put(position, getItem(position).getId());
					} else {
						mSparseArray.remove(position);
					}
				}
			});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final User user = getItem(position);
		LogUtil.d("getView position = " + position + " User = " + user);

		ImageUtil.displayImage(user.getAvatar(), viewHolder.ivPhoto, ImageLoaderOptions.optionsUserHeader);
		viewHolder.tvName.setText(user.getName());
		viewHolder.tvSex.setText(user.getSex());
		viewHolder.tvCity.setText(user.getCity());
		viewHolder.ckbCheck.setTag(position);
		if (null != mSparseArray.get(position)) {
			viewHolder.ckbCheck.setChecked(true);
		} else {
			viewHolder.ckbCheck.setChecked(false);
		}

		return convertView;
	}

	static class ViewHolder {
		private ImageView ivPhoto;
		private CheckBox ckbCheck;
		private TextView tvSex;
		private TextView tvCity;
		private TextView tvName;
	}
}
