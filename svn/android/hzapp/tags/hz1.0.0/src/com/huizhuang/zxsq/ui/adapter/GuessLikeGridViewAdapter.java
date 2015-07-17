package com.huizhuang.zxsq.ui.adapter;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.GuessLike;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;

public class GuessLikeGridViewAdapter extends CommonBaseAdapter<GuessLike> {

	private SparseArray<String> mSparseArray;
	private int mAdaptiveWidth;

	public GuessLikeGridViewAdapter(Context context) {
		super(context);
		mAdaptiveWidth = (UiUtil.getScreenWidth((Activity) context) - UiUtil.dp2px(context, 20)) / 4;
		mSparseArray = new SparseArray<String>();
	}

	/**
	 * 获取选中的样式的格式化字符串（例如[172,365]）
	 * @return
	 */
	public String getSelectStyleFormatString() {
		ArrayList<String> strList = new ArrayList<String>();
		for (int i = 0; i < mSparseArray.size(); i++) {
			strList.add(mSparseArray.valueAt(i));
		}
		String[] strArray = strList.toArray(new String[strList.size()]);
		return Arrays.toString(strArray);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = mLayoutInflater.inflate(R.layout.item_guess_ulike, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.guessLike_icon);
			viewHolder.ivPhoto.setLayoutParams(new RelativeLayout.LayoutParams(mAdaptiveWidth, mAdaptiveWidth));
			viewHolder.ckbCheck = (CheckBox) convertView.findViewById(R.id.guessLike_check_icon);
			viewHolder.ckbCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
					CheckBox CheckBox = (CheckBox) compoundButton;
					int position = (Integer) CheckBox.getTag();
					if (checked) {
						mSparseArray.put(position, getItem(position).getStyles());
					} else {
						mSparseArray.remove(position);
					}
					if (null != mOnStyleCheckListener) {
						mOnStyleCheckListener.onCheckedChanged(mSparseArray.size());
					}
				}
			});
			viewHolder.tvDes = (TextView) convertView.findViewById(R.id.guessLike_text);
			viewHolder.rlLayout = (RelativeLayout) convertView.findViewById(R.id.guessLike_rlayout);
			viewHolder.rlLayout.setLayoutParams(new LinearLayout.LayoutParams(mAdaptiveWidth, mAdaptiveWidth));
			viewHolder.rlLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox checkBox = (CheckBox) v.findViewById(R.id.guessLike_check_icon);
					if (checkBox.isChecked()) {
						checkBox.setChecked(false);
					} else {
						checkBox.setChecked(true);
					}
				}
			});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final GuessLike guessLike = getItem(position);
		LogUtil.d("GuessLikeGridViewAdapter getView position = " + position + " GuessLike = " + guessLike);

		ImageUtil.displayImage(guessLike.getPic(), viewHolder.ivPhoto, ImageLoaderOptions.optionsUserHeader);
		viewHolder.tvDes.setText(guessLike.getDes());
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
		private TextView tvDes;
		private RelativeLayout rlLayout;
	}

	/**
	 * 选中样式个数改变的Listener
	 */
	public interface OnStyleCheckListener {
		void onCheckedChanged(int total);
	}

	private OnStyleCheckListener mOnStyleCheckListener;

	public void setOnStyleCheckListener(OnStyleCheckListener onStyleCheckListener) {
		mOnStyleCheckListener = onStyleCheckListener;
	}

}
