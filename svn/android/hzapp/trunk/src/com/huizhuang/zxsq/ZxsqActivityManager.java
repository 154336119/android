package com.huizhuang.zxsq;



import java.util.Stack;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;

/**
 * @ClassName: ActivityStackManager
 * @Package com.huizhuang.zxsq
 * @Description: 应用activity堆栈管理器
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月24日 下午4:39:59
 */
public class ZxsqActivityManager {

	private static ZxsqActivityManager instance;
	private static Stack<Activity> activityStack;

	private ZxsqActivityManager() {
	}

	/**
	 * 单例
	 */
	public static ZxsqActivityManager getInstance() {
		if (instance == null) {
			instance = new ZxsqActivityManager();
		}
		return instance;
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity getCurrentActivity() {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		if (activityStack.size() > 0) {
			Activity activity = activityStack.lastElement();
			return activity;
		}
		return null;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.push(activity);
	}

	/**
	 * 移除指定的Activity
	 */
	public void removeActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
		}
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		removeActivity(activity);
	}

	/**
	 * 结束Activity
	 * 
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		while (!activityStack.isEmpty()) {
			finishActivity(activityStack.pop());
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 * 
	 * @param context 上下文
	 * @param isBackground 是否开开启后台运行
	 */
	public void appExit(Context context) {
		finishAllActivity();
		MobclickAgent.onKillProcess(context);
		System.exit(0);
	}
}