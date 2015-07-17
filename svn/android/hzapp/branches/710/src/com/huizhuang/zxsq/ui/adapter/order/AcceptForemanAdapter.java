package com.huizhuang.zxsq.ui.adapter.order;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.bean.order.OrderDetailChild;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;




/**
 * 接单工长适配器
 * @author th
 *
 */
public class AcceptForemanAdapter extends CommonBaseAdapter<OrderDetailChild> {

	private ViewHolder mHolder;
	private Handler mHandler;
	private Context mContext;
	
	public AcceptForemanAdapter(Context context,Handler handler) {
		super(context);
		mHandler = handler;
		mContext = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);
		OrderDetailChild orderDetailChild = getItem(position);
		if (null == convertView) {
			mHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_accept_foreman, parent, false);
			mHolder.itemImg = (CircleImageView) convertView.findViewById(R.id.iv_head);
            mHolder.itemName = (TextView) convertView.findViewById(R.id.tv_name);
			mHolder.itemCity = (TextView) convertView.findViewById(R.id.tv_city);
			mHolder.itemOrderCount = (TextView) convertView.findViewById(R.id.tv_order_count);
			mHolder.itemScore = (RatingBar) convertView.findViewById(R.id.rb_score);
			mHolder.itemScoreText = (TextView) convertView.findViewById(R.id.tv_score);
			
			mHolder.itemSendMessage = (ImageButton) convertView.findViewById(R.id.ib_send_message);
			mHolder.itemToCall = (ImageButton) convertView.findViewById(R.id.ib_to_call);
			
			mHolder.itemSendMessage.setOnClickListener(mOnClickListener);
			mHolder.itemToCall.setOnClickListener(mOnClickListener);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.itemSendMessage.setTag(position);
		mHolder.itemToCall.setTag(position);
		if(!TextUtils.isEmpty(orderDetailChild.getAvater())){
			ImageLoader.getInstance().displayImage(orderDetailChild.getAvater(), mHolder.itemImg, ImageLoaderOptions.optionsDefaultEmptyPhoto);
		}
		mHolder.itemName.setText(orderDetailChild.getName());
		Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_rz);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
        if(orderDetailChild.getIs_auth() == 1){
            mHolder.itemName.setCompoundDrawables(null, null, drawable, null);
        }else{
            mHolder.itemName.setCompoundDrawables(null, null, null, null);
        }
		mHolder.itemCity.setText(orderDetailChild.getCity());
		mHolder.itemOrderCount.setText(orderDetailChild.getOrders()+"");
		float score = 0;
		if(TextUtils.isEmpty(orderDetailChild.getScore())){
		    score = 5;
		}else{
		    score = Float.valueOf(orderDetailChild.getScore());
		}
		mHolder.itemScore.setRating(score == 0?5:score);
		mHolder.itemScoreText.setText(score == 0?"5分":score+"分");
		return convertView;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int what = 0;
			if (null != v.getTag()) {
				int position = (Integer) v.getTag();
				switch (v.getId()) {
				case R.id.ib_send_message:
					what = 0;
					break;
               case R.id.ib_to_call:
                    what = 1;
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
	    private CircleImageView itemImg;
        private TextView itemName;
		private TextView itemCity;
		private TextView itemOrderCount;
		private RatingBar itemScore;	
		private TextView itemScoreText;
		private ImageButton itemSendMessage;
		private ImageButton itemToCall;
	}

}
