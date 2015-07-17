package com.huizhuang.zxsq.ui.adapter;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
* @ClassName: AdapterAllOrder 
* @Description: 全部账单适配器
* @author Jean 
* @mail 154336119@qq.com   
* @date 2015-1-29 上午11:38:24 
*
 */
public class AllOrderAdapter extends CommonBaseAdapter<Order> {

    private Holder mHolder;

    public AllOrderAdapter(Context context) {
        super(context);
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            mHolder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_all_order, viewGroup, false);
            mHolder.tv_order_num = (TextView) view.findViewById(R.id.tv_order_num);
            mHolder.tv_order_time = (TextView) view.findViewById(R.id.tv_order_time);
            mHolder.iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            mHolder.tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            mHolder.tv_status = (TextView) view.findViewById(R.id.tv_status);
            mHolder.tv_place = (TextView) view.findViewById(R.id.tv_place);
            view.setTag(mHolder);
        } else {
            mHolder = (Holder) view.getTag();
        }

        Order order = getItem(position);
        mHolder.tv_order_num.setText(Html.fromHtml("<font color='#808080'>"+"订单号："+"</font>"+"<font color='#ff6c38'>"+order.getOrder_no()+"</font>"));
        mHolder.tv_order_time.setText("下单时间："+DateUtil.timestampToSdate(order.getAdd_time(), "yyyy年MM月dd日 HH:mm"));
        if(!order.getAvater().endsWith(AppConstants.DEFAULT_IMG)){
        	ImageUtil.displayImage(order.getAvater(), mHolder.iv_photo, ImageLoaderOptions.optionsUserHeader);
        }
		mHolder.tv_user_name.setText(order.getFull_name());
		mHolder.tv_status.setText(getStatus(order.getStatu())+"");
		if(!TextUtils.isEmpty(order.getHousing_address())){
			mHolder.tv_place.setText(order.getFull_name());
		}else{
			mHolder.tv_place.setVisibility(View.INVISIBLE);
		}
        return view;
    }
    //mViewHolder.tvDate.setText(DateUtil.timestampToSdate(diary.getDatetime(), "yyyy-MM-dd"));
    private class Holder {
        TextView tv_order_num;
        TextView tv_order_time;
        ImageView iv_photo;
        TextView tv_user_name;
        TextView tv_status;
        TextView tv_place;
    }
    private String getStatus(int status){
    	if(status == 0){
    		return "已取消";
		}else if(status == 1){
			return "订单处理中";
		}else if(status == 2){
			return "已量房";
		}else if(status == 3){
			return "已设计报价";
		}else if(status == 4){
			return "已签合同";
		}else if(status == -1){
			return "无效订单";
		}
    	return "已取消";
    }
}