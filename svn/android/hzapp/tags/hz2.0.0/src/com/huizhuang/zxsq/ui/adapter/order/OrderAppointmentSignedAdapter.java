package com.huizhuang.zxsq.ui.adapter.order;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.bean.order.OrderSignForeman;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;




/**
 * 预约签约适配器
 * @author th
 *
 */
public class OrderAppointmentSignedAdapter extends CommonBaseAdapter<OrderSignForeman> {

	private ViewHolder mHolder;
	private Context mContext;
	private Handler mHandler;
	
	public OrderAppointmentSignedAdapter(Context context,Handler handler) {
		super(context);
		mHandler = handler;
		mContext = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);
		OrderSignForeman orderSignForeman = getItem(position);
		if (null == convertView) {
			mHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_order_appointment_signed, parent, false);
			mHolder.itemImg = (CircleImageView) convertView.findViewById(R.id.iv_head);
			mHolder.itemImgTextBg = (ImageView) convertView.findViewById(R.id.iv_img_text_bg);
			mHolder.itemImgText = (TextView) convertView.findViewById(R.id.tv_img_text);
            mHolder.itemName = (TextView) convertView.findViewById(R.id.tv_name);
			mHolder.itemCity = (TextView) convertView.findViewById(R.id.tv_city);
			mHolder.itemOrderCount = (TextView) convertView.findViewById(R.id.tv_order_count);
			mHolder.itemScore = (RatingBar) convertView.findViewById(R.id.rb_score);
			mHolder.itemScoreText = (TextView) convertView.findViewById(R.id.tv_score);
			mHolder.itemStatus = (TextView) convertView.findViewById(R.id.tv_status);
			
			mHolder.itemCancel = (Button) convertView.findViewById(R.id.btn_cancel);
			mHolder.itemOK = (Button) convertView.findViewById(R.id.btn_appointment);
			
			mHolder.itemCancel.setOnClickListener(mOnClickListener);
			mHolder.itemOK.setOnClickListener(mOnClickListener);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		if(orderSignForeman.getImage() != null && !TextUtils.isEmpty(orderSignForeman.getImage().getThumb_path())){
            ImageLoader.getInstance().displayImage(orderSignForeman.getImage().getThumb_path(), mHolder.itemImg, ImageLoaderOptions.optionsDefaultEmptyPhoto);
        }
        mHolder.itemName.setText(orderSignForeman.getShort_name());
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_rz);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
        if(orderSignForeman.getIs_auth() == 1){
            mHolder.itemName.setCompoundDrawables(null, null, drawable, null);
        }else{
            mHolder.itemName.setCompoundDrawables(null, null, null, null);
        }
        mHolder.itemCity.setText(orderSignForeman.getCity_name());
        mHolder.itemOrderCount.setText(orderSignForeman.getGongzhang_orders()+"");
        mHolder.itemScore.setRating(orderSignForeman.getScore() == 0?5:orderSignForeman.getScore());
        mHolder.itemScoreText.setText(orderSignForeman.getScore() == 0?"5分":orderSignForeman.getScore()+"分");
        if(orderSignForeman.getStatu() == 3){//已量房
            mHolder.itemStatus.setText("已量房");
            mHolder.itemStatus.setVisibility(View.VISIBLE);
        }else{
            mHolder.itemStatus.setVisibility(View.GONE);
        }
        if(orderSignForeman.getIs_book() == 2 && orderSignForeman.getStatu() != -1){//工长发起签约
            mHolder.itemOK.setTextColor(mContext.getResources().getColor(R.color.color_ff6c38));
            mHolder.itemOK.setEnabled(true);
            mHolder.itemImgText.setVisibility(View.VISIBLE);
            mHolder.itemImgText.setText("发起签约");
            mHolder.itemImgTextBg.setVisibility(View.VISIBLE);
            
            mHolder.itemCancel.setEnabled(true);
            mHolder.itemCancel.setTextColor(mContext.getResources().getColor(R.color.color_808080));
        }else{
            mHolder.itemOK.setTextColor(mContext.getResources().getColor(R.color.gray_disabled));
            mHolder.itemOK.setEnabled(false);
            mHolder.itemImgText.setVisibility(View.GONE);
            mHolder.itemImgTextBg.setVisibility(View.GONE);
            
            mHolder.itemCancel.setEnabled(false);
            mHolder.itemCancel.setTextColor(mContext.getResources().getColor(R.color.gray_disabled));
            if(orderSignForeman.getStatu() == -1){
                mHolder.itemImgText.setVisibility(View.VISIBLE);
                mHolder.itemImgText.setText("已取消");
                mHolder.itemImgTextBg.setVisibility(View.VISIBLE);
            }
        }
        mHolder.itemCancel.setTag(position);
        mHolder.itemOK.setTag(position);
		return convertView;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int what = 0;
			if (null != v.getTag()) {
				int position = (Integer) v.getTag();
				switch (v.getId()) {
				case R.id.btn_cancel:
					what = 0;//取消签约
					break;
               case R.id.btn_appointment://预约签约
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
	    private ImageView itemImgTextBg;
	    private TextView itemImgText;
        private TextView itemName;
		private TextView itemCity;
		private TextView itemOrderCount;
		private RatingBar itemScore;	
		private TextView itemScoreText;
		private TextView itemStatus;
		private Button itemCancel;
		private Button itemOK;
	}

}
