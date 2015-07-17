package com.huizhuang.zxsq.utils.analytics;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * 友盟事件统计方法封装
 * 
 * @ClassName: UmengAnalytics.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-20 下午2:53:32
 */
public class UmengAnalytics implements BaseAnalytics<String> {

	private static UmengAnalytics umengAnalytics;

	public static UmengAnalytics getInstance() {
		if (null == umengAnalytics) {
			umengAnalytics = new UmengAnalytics();
		}
		return umengAnalytics;
	}

	@Override
	public void addAnalytics(Context context) {
		MobclickAgent.onResume(context);
	}

	@Override
	public void removeAnalytics(Context context) {
		MobclickAgent.onPause(context);
	}

	@Override
	public void onEvent(Context context, String strTag) {
		MobclickAgent.onEvent(context, strTag);
	}
	
	@Override
	public void onPageStart(){
		StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
		String className = caller.getClassName();
		MobclickAgent.onPageStart(className);
	}

	@Override
	public void onPageEnd() {
		StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
		String className = caller.getClassName();
		MobclickAgent.onPageStart(className);
	}

}
