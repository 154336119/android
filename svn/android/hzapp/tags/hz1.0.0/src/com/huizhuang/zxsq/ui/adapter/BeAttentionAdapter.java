package com.huizhuang.zxsq.ui.adapter;

import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
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

public class BeAttentionAdapter extends MyBaseAdapter<User> {
	public static final int SAVE_ATTENTION = 0;
	public static final int CANCEL_ATTENTION = 1;

	private ViewHolder mHolder;
	private DisplayImageOptions mOptions;
	private HashMap<String, String> positionMap;
	private Handler mHandle;

	public BeAttentionAdapter(Context context, Handler handle) {
		super(context);
		this.mHandle = handle;
		positionMap = new HashMap<String, String>();
		mOptions = ImageLoaderOptions.optionsUserHeader;
	}

	class ViewHolder {
		ImageView ivPhoto;
		TextView tvSex;
		TextView tvCity;
		TextView tvName;
		TextView btnAttention;
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
			mHolder = new ViewHolder();
			arg1 = getLayoutInflater().inflate(R.layout.account_be_attention_item, arg2, false);
			mHolder.ivPhoto = (ImageView) arg1.findViewById(R.id.cv_icon);
			mHolder.tvSex = (TextView) arg1.findViewById(R.id.tv_sex);
			mHolder.tvCity = (TextView) arg1.findViewById(R.id.tv_city);
			mHolder.tvName = (TextView) arg1.findViewById(R.id.tv_name);
			mHolder.btnAttention = (TextView) arg1.findViewById(R.id.btn_attention);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		mHolder.btnAttention.setTag(position);
		
		final User user = getItem(position);
		mHolder.tvName.setText(user.getName());
		mHolder.tvSex.setText(user.getSex());
		mHolder.tvCity.setText(user.getCity());
		if (user.getFollwoed()) {
			mHolder.btnAttention.setBackgroundResource(R.drawable.btn_attention_select_off);
		} else {
			mHolder.btnAttention.setBackgroundResource(R.drawable.btn_attention_select_on);
		}
		ImageUtil.displayImage(user.getAvatar(), mHolder.ivPhoto, mOptions);
		mHolder.btnAttention.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int index = (Integer) view.getTag();
				if (user.getFollwoed()) {
					click(CANCEL_ATTENTION, index, user.getId());
				} else {
					click(SAVE_ATTENTION, index, user.getId());
				}
			}
		});

		return arg1;
	}

	private void click(int type, int position, String UserId) {
		Message message = mHandle.obtainMessage();
		message.arg1 = type;
		message.arg2 = position;
		message.obj = UserId;
		mHandle.sendMessage(message);
	}

}
