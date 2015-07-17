package com.huizhuang.zxsq.ui.adapter.account;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;


/** 
* @ClassName: WorksiteOrderListAdapter 
* @Description: 预约监理订单列表适配器
* @author th 
* @mail 342592622@qq.com   
* @date 2015-1-22 下午4:13:26 
*  
*/
public class WorksiteOrderListAdapter extends CommonBaseAdapter<Order> {

	private Handler mHandler;
	private boolean mIsBtn = false;;
	public WorksiteOrderListAdapter(Context context,Handler handler,boolean isBtn) {
		super(context);
		this.mHandler = handler;
		this.mIsBtn = isBtn;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_worksite_order_list, parent, false);
			viewHolder.tvOrderNo = (TextView) convertView.findViewById(R.id.tv_order_no);
			viewHolder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_order_time);
			viewHolder.civHeadImg = (CircleImageView) convertView.findViewById(R.id.iv_img);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			viewHolder.tvAreaName = (TextView) convertView.findViewById(R.id.tv_area_name);
			viewHolder.btnCancelOrder = (TextView) convertView.findViewById(R.id.btn_cancel_order);
			viewHolder.btnCancelOrder.setOnClickListener(mOnClickListener);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Order order = getItem(position);
		viewHolder.tvOrderNo.setText(order.getOrder_no());
		viewHolder.tvOrderTime.setText(DateUtil.timestampToSdate(order.getAdd_time(), "yyyy年MM月dd日 HH:mm:ss"));
//		if(!order.getAvater().endsWith(AppConstants.DEFAULT_IMG)){
			ImageLoader.getInstance().displayImage(order.getAvater(), viewHolder.civHeadImg, ImageLoaderOptions.optionsDefaultEmptyPhoto);
//		}else {
//			ImageLoader.getInstance().displayImage(order.getAvater(), viewHolder.civHeadImg, ImageLoaderOptions.optionsDefaultEmptyPhoto);
//		}
		viewHolder.tvName.setText(order.getFull_name());
		viewHolder.tvAreaName.setText(order.getHousing_address());
		switch (order.getStatu()) {
		case 0:
			viewHolder.btnCancelOrder.setVisibility(View.GONE);
			viewHolder.tvStatus.setText("已取消");
			break;		
		case 1:
			if(mIsBtn){
				viewHolder.btnCancelOrder.setVisibility(View.VISIBLE);
			}else{
				viewHolder.btnCancelOrder.setVisibility(View.GONE);
			}
			viewHolder.tvStatus.setText("订单处理中");
			break;
		case 2:
			viewHolder.btnCancelOrder.setVisibility(View.GONE);
			viewHolder.tvStatus.setText("已量房");
			break;
		case 3:
			viewHolder.btnCancelOrder.setVisibility(View.GONE);
			viewHolder.tvStatus.setText("已设计报价");
			break;
		case 4:
			viewHolder.btnCancelOrder.setVisibility(View.GONE);
			viewHolder.tvStatus.setText("已签合同");
			break;
		case -1:
			viewHolder.btnCancelOrder.setVisibility(View.GONE);
			viewHolder.tvStatus.setText("无效订单");
			break;			
		default:
			break;
		}
		viewHolder.btnCancelOrder.setTag(position);
		return convertView;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int what = 0;
			if (null != v.getTag()) {
				int position = (Integer) v.getTag();
				switch (v.getId()) {
				case R.id.btn_cancel_order:
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
		private TextView tvOrderNo;
		private TextView tvOrderTime;
		private CircleImageView civHeadImg;
		private TextView tvName;
		private TextView tvStatus;
		private TextView tvAreaName;
		private TextView btnCancelOrder;
	}

}
