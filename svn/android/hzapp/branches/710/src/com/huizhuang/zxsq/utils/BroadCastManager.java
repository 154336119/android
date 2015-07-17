package com.huizhuang.zxsq.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * 本地广播发送Manager
 * 
 * @ClassName: BroadCastManager.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-11 上午10:47:56
 */
public class BroadCastManager {

	/**
	 * 用户退出登录广播
	 */
	public static final String ACTION_USER_LOGOUT = "action_user_logout";

	// 本地广播 - 文章收藏状态发生改变
	public static final String ACTION_ARTICLE_FAVORITE_STATUS_CHANGED = "action_article_favorite_status_changed";

	// 本地广播 - 文章收藏状态发生改变
	public static final String ACTION_PICTURE_FAVORITE_STATUS_CHANGED = "action_picture_favorite_status_changed";

	// 本地广播 - 分享计数状态发生改变
	public static final String ACTION_PICTURE_SHARE_STATUS_CHANGED = "action_picture_share_status_changed";

	// 本地广播 - 记账内容发生改变
	public static final String ACTION_BILL_ACCOUNTING_CHANGED = "action_bill_accounting_changed";

	public static final String ACTION_USER_LOGIN = "action_user_login";
	// 本地广播 - 返沪到个人中心
	public static final String ACTION_GO_ACCTOUNT= "action_go_account";
	
	// 本地广播 - 刷新订单
	public static final String ACTION_REFRESH_ORDER= "action_order_refresh";

	// 本地广播 - 刷新个人信息
	public static final String ACTION_REFRESH_USER_INFO= "action_user_info_refresh";
	
	// 本地广播 - 关闭支付流程相关界面
	public static final String ACTION_CLOSE_PAY_PAGE= "action_close_pay_page";
	/**
	 * 发送本地广播(不带Bundle数据)
	 * 
	 * @param context
	 * @param action
	 */
	public static void sendBroadCast(Context context, String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
	/**
	 * 发送本地广播(带Bundle数据)
	 * 
	 * @param context
	 * @param action
	 * @param extras
	 */
	public static void sendBroadCast(Context context, String action, Bundle extras) {
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtras(extras);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

}
