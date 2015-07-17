package com.huizhuang.zxsq.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * 
 * @ClassName: ToastUtil.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-12 下午2:54:50
 */
public class ToastUtil {

	/**
	 * Show Toast in short time by system
	 * 
	 * @param context
	 * @param strMsg
	 */
	public static void showMessage(final Context context, final String strMsg) {
		Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show Toast in short time by system
	 * 
	 * @param context
	 * @param resId
	 */
	public static void showMessage(final Context context, final int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show Toast in long time by system
	 * 
	 * @param context
	 * @param resId
	 */
	public static void showMessageLong(final Context context, final int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
	}

	/**
	 * Show Toast in long time by system
	 * 
	 * @param context
	 * @param strMsg
	 */
	public static void showMessageLong(final Context context, final String strMsg) {
		Toast.makeText(context, strMsg, Toast.LENGTH_LONG).show();
	}

}
