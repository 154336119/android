package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Activist;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;

/**
 * 我的维权列表Adapter
 * 
 * @ClassName: AccountActivistListAdapter.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-24 下午3:10:29
 */
public class AccountActivistListAdapter extends CommonBaseAdapter<Activist> {

	private ViewHolder mViewHolder;
	private Handler mHandler;

	public AccountActivistListAdapter(Context context, Handler handler) {
		super(context);
		mHandler = handler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Activist activist = getItem(position);
		LogUtil.d("getView position = " + position + " activist = " + activist);

		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.account_activist_list_item, parent, false);
			mViewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			mViewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
			mViewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			mViewHolder.tv_img_name = (TextView) convertView.findViewById(R.id.tv_img_name);
			mViewHolder.tv_item_content = (TextView) convertView.findViewById(R.id.tv_item_content);
			mViewHolder.btn_wq = (Button) convertView.findViewById(R.id.btn_wq);
			mViewHolder.btn_wq.setOnClickListener(mOnClickListener);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		if (TextUtils.isEmpty(activist.getParentName())) {
			mViewHolder.iv_icon.setVisibility(View.INVISIBLE);
			mViewHolder.tv_title.setVisibility(View.INVISIBLE);
		} else {
			mViewHolder.iv_icon.setVisibility(View.VISIBLE);
			mViewHolder.tv_title.setVisibility(View.VISIBLE);
			mViewHolder.tv_title.setText(activist.getParentName());
		}
		if (TextUtils.isEmpty(activist.getImgRemarks())) {
			mViewHolder.tv_img_name.setVisibility(View.INVISIBLE);
		} else {
			mViewHolder.tv_img_name.setVisibility(View.VISIBLE);
		}
		mViewHolder.tv_img_name.setText(activist.getImgRemarks());
		mViewHolder.tv_img_name.getBackground().setAlpha(80);
		mViewHolder.tv_item_content.setText(activist.getName());
		if (activist.getStatu() == 0) {
			mViewHolder.btn_wq.setTag(position);
			mViewHolder.btn_wq.setText(mContext.getResources().getString(R.string.txt_activist_start));
			mViewHolder.btn_wq.setTextColor(mContext.getResources().getColor(R.color.orange_light));
			mViewHolder.btn_wq.setBackgroundResource(R.drawable.bg_orange_red_border);
		} else if (activist.getStatu() == 1) {
			mViewHolder.btn_wq.setText(mContext.getResources().getString(R.string.txt_activist_started));
			mViewHolder.btn_wq.setTextColor(mContext.getResources().getColor(R.color.gray_80));
			mViewHolder.btn_wq.setBackgroundResource(R.drawable.bg_orange_gray_border_norm);
		} else if (activist.getStatu() == 2) {
			mViewHolder.btn_wq.setText(mContext.getResources().getString(R.string.txt_activist_colse));
			mViewHolder.btn_wq.setTextColor(mContext.getResources().getColor(R.color.gray_80));
			mViewHolder.btn_wq.setBackgroundResource(R.drawable.bg_orange_gray_border_norm);
		}
		ImageUtil.displayImage(activist.getImgPath(), mViewHolder.iv_img, ImageLoaderOptions.getDefaultImageOption());
		return convertView;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int what = 0;
			if (null != v.getTag()) {
				int position = (Integer) v.getTag();
				switch (v.getId()) {
				case R.id.btn_wq:
					what = 0;
					break;
				default:
					break;
				}
				Message msg = mHandler.obtainMessage();
				msg.what = what;
				msg.arg1 = position;
				mHandler.sendMessage(msg);
			}
		}
	};

	/**
	 * 更新对应的视图
	 * 
	 * @param listView
	 * @param itemIndex
	 */
	public void updateView(ListView listView, int itemIndex) {
		int visiblePosition = listView.getFirstVisiblePosition();
		View v = listView.getChildAt(itemIndex - visiblePosition);
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		if (viewHolder != null) {
			mViewHolder.btn_wq.setText(mContext.getResources().getString(R.string.txt_activist_started));
			mViewHolder.btn_wq.setTextColor(mContext.getResources().getColor(R.color.gray_80));
			mViewHolder.btn_wq.setBackgroundResource(R.drawable.bg_orange_gray_border_norm);
		}
	}

	static class ViewHolder {
		private ImageView iv_icon;
		private ImageView iv_img;
		private TextView tv_title;
		private TextView tv_img_name;
		private TextView tv_item_content;
		private Button btn_wq;
	}

}
