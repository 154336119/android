package com.huizhuang.zxsq.ui.adapter.account;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.bean.order.OrderDetailChild;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.OrderProgressStateBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 订单详情Adapter
 * 
 * @ClassName: MyOrderListAdapter
 * 
 * @author Jean
 * 
 * @date 2014-10-30 上午10:40:00
 */
public class OrderDetailAdapter extends CommonBaseAdapter<OrderDetailChild> {
	public static final int HTTP_APPOINTMENT = 2;
	public static final int HTTP_MEASURE = 1;
	private Handler mHandle;
	private boolean mIsSign;//是否已签约合同
    public OrderDetailAdapter(Context context, Handler handle,boolean mIsSign) {
        super(context);
        this.mHandle = handle;
        this.mIsSign = mIsSign;
    }

    public void setIsSign(boolean mIsSign){
    	this.mIsSign = mIsSign;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.adapter_order_progress, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_froeman_name);
            viewHolder.orderProgressStateBar = (OrderProgressStateBar) convertView.findViewById(R.id.order_progress_state_bar);
            viewHolder.btnMeasurementOver = (Button) convertView.findViewById(R.id.btn_measurement_over);
            viewHolder.btnAppointmentSign = (Button) convertView.findViewById(R.id.btn_appointment_sign);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final OrderDetailChild child = getItem(position);
        viewHolder.tvName.setText(child.getName());
        viewHolder.orderProgressStateBar.setProgressState(child.getStatu());
        viewHolder.btnMeasurementOver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				click(HTTP_MEASURE,child.getAllot_id(),0);
			}
		});
        viewHolder.btnAppointmentSign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				click(HTTP_APPOINTMENT,child.getAllot_id(),0);
			}
		});
        
        
        if(child.getMeasure()==0 || mIsSign){
        	viewHolder.btnMeasurementOver.setTextColor(mContext.getResources().getColor(R.color.color_808080));
        	viewHolder.btnMeasurementOver.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_btn_order_detail_off));
        	viewHolder.btnMeasurementOver.setClickable(false);
        }else{
        	viewHolder.btnMeasurementOver.setTextColor(mContext.getResources().getColor(R.color.color_ff6c38));
        	viewHolder.btnMeasurementOver.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_btn_order_detail_on));
        	viewHolder.btnMeasurementOver.setClickable(true);        	
        }
        
        if(child.getContract()==0){
        	viewHolder.btnAppointmentSign.setTextColor(mContext.getResources().getColor(R.color.color_808080));
        	viewHolder.btnAppointmentSign.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_btn_order_detail_off));
        	viewHolder.btnAppointmentSign.setClickable(false);
        }else{
        	   
        	viewHolder.btnAppointmentSign.setTextColor(mContext.getResources().getColor(R.color.color_ff6c38));
        	viewHolder.btnAppointmentSign.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_btn_order_detail_on));
        	viewHolder.btnAppointmentSign.setClickable(true);        	
        }        


        
        return convertView;
    }

    static class ViewHolder {
        private TextView tvName;
        private OrderProgressStateBar orderProgressStateBar;
        private Button btnMeasurementOver;
        private Button btnAppointmentSign;
    }
    
	private void click(int type,int childId, int time) {
		Message message = mHandle.obtainMessage();
		message.arg1 = childId;
		message.arg2 = time;
		message.what = type;
		mHandle.sendMessage(message);
	}
}
