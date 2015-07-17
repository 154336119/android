package com.huizhuang.zxsq.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.ui.activity.order.OrderAppointmentSignedActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderEvaluationActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderServeStationSignActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderShowPayActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderWaitResponseActivity;
import com.huizhuang.zxsq.ui.activity.supervision.CheckInfoActivity;
import com.huizhuang.zxsq.ui.activity.supervision.EvaluationActivity;
import com.huizhuang.zxsq.ui.activity.supervision.NodeEditActivity;
import com.huizhuang.zxsq.ui.activity.supervision.WaitResponseActivity;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;

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
    private Context mContext;

    public AllOrderAdapter(Context context) {
        super(context);
        mContext = context;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            mHolder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_order, viewGroup, false);
            mHolder.tv_order_time = (TextView) view.findViewById(R.id.tv_order_time);
            mHolder.tv_place = (TextView) view.findViewById(R.id.tv_place);
            mHolder.tv_measure_time = (TextView) view.findViewById(R.id.tv_measure_time);
//            mHolder.tv_message = (TextView) view.findViewById(R.id.tv_message);
            mHolder.btn_appraise = (Button) view.findViewById(R.id.btn_appraise);
            mHolder.rl_order = (RelativeLayout)view.findViewById(R.id.rl_order);
            view.setTag(mHolder);
        } else {
            mHolder = (Holder) view.getTag();
        }

        Order order = getItem(position);
        mHolder.tv_order_time.setText("下单时间："+DateUtil.timestampToSdate(order.getAdd_time(), "yyyy年MM月dd日 HH:mm"));
		mHolder.tv_place.setText(order.getHousing_address());
		mHolder.tv_measure_time.setText(DateUtil.format(order.getMeasuring_time(), "yyyy-MM-dd", "yyyy年MM月dd日")+" 量房");
//		mHolder.tv_message.setText(order.getUser_remark());
		mHolder.btn_appraise.setTag(position);
		mHolder.btn_appraise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    int position = Integer.valueOf((v.getTag()+""));
                Bundle bd = new Bundle();
                bd.putString(AppConstants.PARAM_ORDER_ID, getList().get(position).getOrder_id()+"");
                ActivityUtil.next((Activity)mContext, OrderEvaluationActivity.class,bd,-1);
			}
		});
		mHolder.rl_order.setTag(position);
		mHolder.rl_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    int position = Integer.valueOf((v.getTag()+""));
			    Order order = getList().get(position);
			    Bundle bd = new Bundle();
			    bd.putString(AppConstants.PARAM_ORDER_ID, order.getOrder_id()+"");
			    if(order.getStage() != null){
    			    bd.putString(AppConstants.PARAM_NODE_ID, order.getStage().getNode_id());
    			    bd.putString(AppConstants.PARAM_STAGE_ID, order.getStage().getStage_id());
			    }
			    if(order.getSub_statu() < 6){
    			    if(order.getIs_book() == 2){//待客户确认签约，跳签约列表
    			        ActivityUtil.next((Activity)mContext, OrderAppointmentSignedActivity.class,bd,-1);
    			    }else if(order.getIs_book() == 3){//预约签约成功,跳签约成功页面
    			        ActivityUtil.next((Activity)mContext, OrderServeStationSignActivity.class,bd,-1);
    			    }else  if(order.getSub_statu() <= 2){//确认，跳工长应答页面
                        ActivityUtil.next((Activity)mContext, OrderWaitResponseActivity.class,bd,-1);
                    }else if(order.getSub_statu() < 6){//已量房,跳评价页
                        ActivityUtil.next((Activity)mContext, OrderEvaluationActivity.class,bd,-1);
                    }
			    }else if(order.getSub_statu() == 6){//装修中
			        if(order.getStage().getStatus() == 6 || order.getStage().getStatus() == 4){//跳支付页
			            ActivityUtil.next((Activity)mContext, OrderShowPayActivity.class,bd,-1);
			        }else if(order.getStage().getStatus() == 1){//等待开工，跳阶段应答
			            ActivityUtil.next((Activity)mContext, WaitResponseActivity.class,bd,-1);
			        }else if(order.getStage().getStatus() == 2){//提交监理审核，跳阶段整改中
			            ActivityUtil.next((Activity)mContext, NodeEditActivity.class,bd,-1);
//			        }else if(order.getStage().getStatus() == 2){//提交用户确认,整改确认页
//			            ActivityUtil.next((Activity)mContext, NodeEditActivity.class,bd,-1);
			        }else if(order.getStage().getStatus() == 3){//客户确认通过，跳评价页
			            bd.putInt(AppConstants.PARAM_IS_CODE, order.getStage().getCost_changed());
			            ActivityUtil.next((Activity)mContext, EvaluationActivity.class,bd,-1);
			        }else if(order.getStage().getStatus() == 5){//监理已打分，直接进入详情审核,跳验收详情页
			            ActivityUtil.next((Activity)mContext, CheckInfoActivity.class,bd,-1);
			        }
			    }
//			    bd.putString(AppConstants.PARAM_ORDER_ID, "13902");
//			    bd.putString(AppConstants.PARAM_STAGE_ID, "10");
//			    bd.putString(AppConstants.PARAM_NODE_ID, "1");
//			    ActivityUtil.next((Activity)mContext, EvaluationActivity.class,bd,-1);
			}
		});
		if(order.getSub_statu()<6){ //未开工
			if(order.getCan_pj() == 1){
				mHolder.btn_appraise.setEnabled(true);
			    mHolder.btn_appraise.setBackgroundResource(R.drawable.bg_btn_order_detail_on);
			    mHolder.btn_appraise.setTextColor(mContext.getResources().getColor(R.color.color_ff6c38));
			}else{
			    mHolder.btn_appraise.setEnabled(false);
			    mHolder.btn_appraise.setBackgroundResource(R.drawable.bg_btn_order_detail_off);
			    mHolder.btn_appraise.setTextColor(mContext.getResources().getColor(R.color.gray_disabled));				
			}
		}else if( order.getSub_statu()==6){ //已开工
			if(order.getStage().getStatus() == 3){
				mHolder.btn_appraise.setEnabled(true);
			    mHolder.btn_appraise.setBackgroundResource(R.drawable.bg_btn_order_detail_on);
			    mHolder.btn_appraise.setTextColor(mContext.getResources().getColor(R.color.color_ff6c38));
			}else{
				mHolder.btn_appraise.setEnabled(false);
			    mHolder.btn_appraise.setBackgroundResource(R.drawable.bg_btn_order_detail_off);
			    mHolder.btn_appraise.setTextColor(mContext.getResources().getColor(R.color.gray_disabled));	
			}
		} else{
			mHolder.btn_appraise.setEnabled(false);
		    mHolder.btn_appraise.setBackgroundResource(R.drawable.bg_btn_order_detail_off);
		    mHolder.btn_appraise.setTextColor(mContext.getResources().getColor(R.color.gray_disabled));	
		}
		
//		if(order.getStage().getStatus() == 3 || order.getCan_pj() == 1){
//		    mHolder.btn_appraise.setEnabled(true);
//		    mHolder.btn_appraise.setBackgroundResource(R.drawable.bg_btn_order_detail_on);
//		    mHolder.btn_appraise.setTextColor(mContext.getResources().getColor(R.color.color_ff6c38));
//		}else{
//		    mHolder.btn_appraise.setEnabled(false);
//		    mHolder.btn_appraise.setBackgroundResource(R.drawable.bg_btn_order_detail_off);
//		    mHolder.btn_appraise.setTextColor(mContext.getResources().getColor(R.color.gray_disabled));
//		}
		return view;
    }
    private class Holder {
        TextView tv_order_time;
        TextView tv_place;
        TextView tv_measure_time;
        TextView tv_message;
        Button btn_appraise;
        RelativeLayout rl_order;
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