package com.huizhuang.zxsq.ui.adapter.supervision;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.supervision.CheckInfoDetail;
import com.huizhuang.zxsq.ui.activity.supervision.CheckInfoActivity;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;




/**
 * 接单工长适配器
 * @author th
 *
 */
public class CheckInfoAdapter extends CommonBaseAdapter<CheckInfoDetail> {

	private ViewHolder mHolder;
	private Handler mHandler;
	private Context mContext;
	
	public CheckInfoAdapter(Context context,Handler handler) {
		super(context);
		mHandler = handler;
		mContext = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);
		CheckInfoDetail checkInfoDetail = getItem(position);
		if (null == convertView) {
			mHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_check_info, parent, false);
			mHolder.itemVLine1 = (View) convertView.findViewById(R.id.v_line1);
            mHolder.itemVLine2 = (View) convertView.findViewById(R.id.v_line2);
			mHolder.itemTvNode = (TextView) convertView.findViewById(R.id.tv_node);
			mHolder.itemTvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.itemTvContent = (TextView) convertView.findViewById(R.id.tv_content);
			mHolder.itemIvExample = (ImageView) convertView.findViewById(R.id.iv_example);
			mHolder.itemTvExample = (TextView) convertView.findViewById(R.id.tv_example);
			mHolder.itemIvReal = (ImageView) convertView.findViewById(R.id.iv_real);
			mHolder.itemTvReal = (TextView) convertView.findViewById(R.id.tv_real);
			mHolder.itemRealOk = (ImageView) convertView.findViewById(R.id.iv_real_ok);
			mHolder.itemIvReal.setOnClickListener(mOnClickListener);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(position == 0){
		    mHolder.itemVLine1.setVisibility(View.GONE);
		}else{
		    mHolder.itemVLine1.setVisibility(View.VISIBLE);
		}
		if(position == (getList().size()-1)){
            mHolder.itemVLine2.setVisibility(View.GONE);
        }else{
            mHolder.itemVLine2.setVisibility(View.VISIBLE);
        }
		if(checkInfoDetail.getDemo_title().length() > 3){
		    mHolder.itemTvNode.setText(checkInfoDetail.getDemo_title().substring(0, 3));
		}else{
		    mHolder.itemTvNode.setText(checkInfoDetail.getDemo_title());
		}
		mHolder.itemIvReal.setTag(position);
		mHolder.itemTvTitle.setText(checkInfoDetail.getDemo_title());
		mHolder.itemTvContent.setText(checkInfoDetail.getDemo_remark());
		ImageLoader.getInstance().displayImage(checkInfoDetail.getDemo_img_path(), mHolder.itemIvExample, ImageLoaderOptions.optionsDefaultEmptyPhoto);
		ImageLoader.getInstance().displayImage(checkInfoDetail.getScene_img_path(), mHolder.itemIvReal, ImageLoaderOptions.optionsDefaultEmptyPhoto);
		if(checkInfoDetail.getScene_score() < 6 || CheckInfoActivity.mIsHistory){
		    mHolder.itemRealOk.setImageResource(R.drawable.icon_check_no_ok);
		}else{
		    mHolder.itemRealOk.setImageResource(R.drawable.icon_check_ok);
		}
		return convertView;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int what = 0;
			if (null != v.getTag()) {
				int position = (Integer) v.getTag();
				switch (v.getId()) {
				case R.id.iv_real:
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
	
	static class ViewHolder {
	    private View itemVLine1;
	    private View itemVLine2;
	    private TextView itemTvNode;
        private TextView itemTvTitle;
		private TextView itemTvContent;
		private ImageView itemIvExample;
		private TextView itemTvExample;
		private ImageView itemIvReal;
		private TextView itemTvReal;
		private ImageView itemRealOk;
	}

}
