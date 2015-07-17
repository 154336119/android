package com.huizhuang.zxsq.ui.adapter;

import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.UserGroup;
import com.huizhuang.zxsq.ui.adapter.base.MyBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class AttentionAdapter extends MyBaseAdapter<User> {

	private ViewHolder holder;
	private DisplayImageOptions mOptions;
	private HashMap<String, String> positionMap;

	public AttentionAdapter(Context context) {
		super(context);
		positionMap = new HashMap<String, String>();
		mOptions = ImageLoaderOptions.optionsUserHeader;
	}

	class ViewHolder {
		ImageView ivPhoto;
		TextView tvSex;
		TextView tvCity;
		TextView tvName;
	}

	public void setList(UserGroup list) {
		getList().clear();
		getList().addAll(list);
	}

	public HashMap<String, String> getSelectUserId() {
		return positionMap;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = getLayoutInflater().inflate(R.layout.account_attention_item, arg2, false);
			holder.ivPhoto = (ImageView) arg1.findViewById(R.id.cv_icon);
			holder.tvSex = (TextView) arg1.findViewById(R.id.tv_sex);
			holder.tvCity = (TextView) arg1.findViewById(R.id.tv_city);
			holder.tvName = (TextView) arg1.findViewById(R.id.tv_name);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}

		User user = getItem(position);
		holder.tvName.setText(user.getName());
		holder.tvSex.setText(user.getSex());
		holder.tvCity.setText(user.getCity());
		ImageUtil.displayImage(user.getAvatar(), holder.ivPhoto, mOptions);

		return arg1;
	}
}
