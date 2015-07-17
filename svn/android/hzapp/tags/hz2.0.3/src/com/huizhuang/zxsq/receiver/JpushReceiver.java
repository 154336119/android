package com.huizhuang.zxsq.receiver;

import java.util.List;

import org.json.JSONException;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.task.common.GetOrderListTask;
import com.huizhuang.zxsq.ui.activity.order.OrderAppointmentSignedActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderEvaluationActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderServeStationSignActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderShowPayActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderWaitResponseActivity;
import com.huizhuang.zxsq.ui.activity.supervision.CheckInfoActivity;
import com.huizhuang.zxsq.ui.activity.supervision.EvaluationActivity;
import com.huizhuang.zxsq.ui.activity.supervision.NodeEditActivity;
import com.huizhuang.zxsq.ui.activity.supervision.WaitResponseActivity;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.LogUtil;

import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
	private String mMinId = null;
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		String action = intent.getAction();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {// 接收到注册的ID
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			LogUtil.e("[JpushReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {// 接收到自定义的消息
			String customMessage = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			LogUtil.e("[JpushReceiver] 接收到推送下来的自定义消息: " + customMessage);
			// 对自定义的消息进行处理
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {// 接收到通知
			LogUtil.e("[JpushReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			LogUtil.e("[JpushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {// 打开通知
			LogUtil.e("[JpushReceiver] 用户打开了通知");
			// 打开通知，做相应的操作(这里是打开相应的Activity)
			searchOrderList(context, bundle);
		}
	}

	private void searchOrderList(Context context, Bundle bundle) {
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		try {
			org.json.JSONObject extrasJson = new org.json.JSONObject(extras);
			LogUtil.e(extras);
			String userId = extrasJson.optString("user_id");
			if(TextUtils.isEmpty(extrasJson.optString("order_id"))){
				return;
			}else{
				final int ordersId = Integer.parseInt(extrasJson.optString("order_id"));
				if (userId.equals(ZxsqApplication.getInstance().getUser().getUser_id())) {
					searchOrder(context,ordersId);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void searchOrder(final Context context, final int ordersId) {
		GetOrderListTask getOrderListTask = new GetOrderListTask(context, mMinId);
		getOrderListTask.setCallBack(new AbstractHttpResponseHandler<List<Order>>() {
					@Override
					public void onSuccess(List<Order> list) {
						BroadCastManager.sendBroadCast(context,
										BroadCastManager.ACTION_REFRESH_USER_INFO); // 刷新个人信息
						if(list==null || list.size()<=0){
							return;
						}else{
							for (Order order : list) {
								if (order.getOrder_id() == ordersId) {
									openRelativeActivity(context, order);
									return;
								}
							}
							mMinId = list.get(list.size()-1).getOrder_id()+"";
							searchOrder(context, ordersId);
						}
					}

					@Override
					public void onFailure(int code, String error) {

					}
				});
		getOrderListTask.send();
	}

	protected void openRelativeActivity(Context context, Order order) {
		/** 跳转相应页面所必须的参数 */
		Bundle bd = new Bundle();
		bd.putString(AppConstants.PARAM_ORDER_ID, order.getOrder_id() + "");
		if (order.getStage() != null) {
			bd.putString(AppConstants.PARAM_NODE_ID, order.getStage()
					.getNode_id());
			bd.putString(AppConstants.PARAM_STAGE_ID, order.getStage()
					.getStage_id());
		}
		Intent start = new Intent();
		//因为是用的Context启动Activity，所以这个设置必须有。如果是Activity启动的则不需要
		start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (order.getSub_statu() < 6) {
			if (order.getIs_book() == 2) {// 待客户确认签约，跳签约列表
				start.setClass(context, OrderAppointmentSignedActivity.class);
			} else if (order.getIs_book() == 3) {// 预约签约成功,跳签约成功页面
				start.setClass(context, OrderServeStationSignActivity.class);
			} else if (order.getSub_statu() <= 2) {// 确认，跳工长应答页面
				start.setClass(context, OrderWaitResponseActivity.class);
			} else if (order.getSub_statu() < 6) {// 已量房,跳评价页
				start.setClass(context, OrderEvaluationActivity.class);
			}
		} else if (order.getSub_statu() == 6) {// 装修中
			if (order.getStage().getStatus() == 6 || order.getStage().getStatus() == 4) {// 跳支付页
				start.setClass(context, OrderShowPayActivity.class);
			} else if (order.getStage().getStatus() == 1) {// 等待开工，跳阶段应答
				start.setClass(context, WaitResponseActivity.class);
			} else if (order.getStage().getStatus() == 2) {// 提交监理审核，跳阶段整改中
				start.setClass(context, NodeEditActivity.class);
			} else if (order.getStage().getStatus() == 3) {// 客户确认通过，跳评价页
				bd.putInt(AppConstants.PARAM_IS_CODE, order.getStage().getCost_changed());
				start.setClass(context, EvaluationActivity.class);
			} else if (order.getStage().getStatus() == 5) {// 监理已打分，直接进入详情审核,跳验收详情页
				start.setClass(context, CheckInfoActivity.class);
			}
		}
		start.putExtras(bd);
		context.startActivity(start);
	}

}
