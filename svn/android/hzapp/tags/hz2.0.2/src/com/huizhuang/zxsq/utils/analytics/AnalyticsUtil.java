package com.huizhuang.zxsq.utils.analytics;

import android.content.Context;

/**
 * 统计工具类
 * 
 * @ClassName: AnalyticsUtil.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-20 下午2:18:06
 */
public class AnalyticsUtil {

	/**
	 * Activity生命周期onResume调用
	 * 
	 * @param context
	 */
	public static void addAnalytics(Context context) {
		UmengAnalytics.getInstance().addAnalytics(context);
	}

	/**
	 * Activity生命周期onPause调用
	 * 
	 * @param context
	 */
	public static void removeAnalytics(Context context) {
		UmengAnalytics.getInstance().removeAnalytics(context);
	}

	/**
	 * 点击事件调用
	 * 
	 * @param context
	 * @param strTag
	 */
	public static void onEvent(Context context, String strTag) {
		UmengAnalytics.getInstance().onEvent(context, strTag);
	}
	
	/**
	 * 统计页面
	 * 
	 */
	public static void onPageStart(){
		UmengAnalytics.getInstance().onPageStart();
	}
	
	/**
	 * 统计页面
	 * 
	 */
	public static void onPageEnd(){
		UmengAnalytics.getInstance().onPageEnd();
	}
}
