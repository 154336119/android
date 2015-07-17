package com.huizhuang.zxsq.utils;

import android.content.Context;
import android.widget.Toast;

import com.huizhuang.zxsq.config.AppConfig;

/**
 * @ClassName: LogUtil
 * @Description: 日志打印
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月3日 下午3:57:54
 */
public class LogUtil {

	/** 应用调试模式 */
	public static final boolean DEBUG = AppConfig.DEBUG_MODE;

	public static void show(Context context, String str) {
		if (DEBUG) {
			Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Send a VERBOSE log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void v(String msg) {
		if (DEBUG)
			android.util.Log.v(buildTag(), buildMessage(msg));
	}

	/**
	 * Send a VERBOSE log message and log the exception.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 * @param thr
	 *            An exception to log
	 */
	public static void v(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.v(buildTag(), buildMessage(msg), thr);
	}

	/**
	 * Send a DEBUG log message.
	 * 
	 * @param msg
	 */
	public static void d(String msg) {
		if (DEBUG)
			android.util.Log.d(buildTag(), buildMessage(msg));
	}

	/**
	 * Send a DEBUG log message and log the exception.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 * @param thr
	 *            An exception to log
	 */
	public static void d(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.d(buildTag(), buildMessage(msg), thr);
	}

	/**
	 * Send an INFO log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void i(String msg) {
		if (DEBUG)
			android.util.Log.i(buildTag(), buildMessage(msg));
	}

	/**
	 * Send a INFO log message and log the exception.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 * @param thr
	 *            An exception to log
	 */
	public static void i(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.i(buildTag(), buildMessage(msg), thr);
	}

	/**
	 * Send an ERROR log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void e(String msg) {
		if (DEBUG)
			android.util.Log.e(buildTag(), buildMessage(msg));
	}
	
	/**
	 * Send an ERROR log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void e(String tag, String msg) {
		if (DEBUG)
			android.util.Log.e(tag, buildMessage(msg));
	}

	/**
	 * Send a WARN log message
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void w(String msg) {
		if (DEBUG)
			android.util.Log.w(buildTag(), buildMessage(msg));
	}
	/**
	 * Send a WARN log message
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void w(String tag,String msg) {
		if (DEBUG)
			android.util.Log.w(tag, buildMessage(msg));
	}

	/**
	 * Send a WARN log message and log the exception.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 * @param thr
	 *            An exception to log
	 */
	public static void w(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.w(buildTag(), buildMessage(msg), thr);
	}

	/**
	 * Send an empty WARN log message and log the exception.
	 * 
	 * @param thr
	 *            An exception to log
	 */
	public static void w(Throwable thr) {
		if (DEBUG)
			android.util.Log.w(buildTag(), buildMessage(""), thr);
	}

	/**
	 * Send an ERROR log message and log the exception.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 * @param thr
	 *            An exception to log
	 */
	public static void e(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.e(buildTag(), buildMessage(msg), thr);
	}
	
	/**
	 * Send an ERROR log message and log the exception.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 * @param thr
	 *            An exception to log
	 */
	public static void e(String tag, String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.e(tag, buildMessage(msg), thr);
	}

	protected static String buildTag() {
		StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
		return new StringBuilder().append(caller.getClassName()+"."+caller.getMethodName()).append("()").toString();
	}
	
	/**
	 * Building Message
	 * 
	 * @param msg
	 *            The message you would like logged.
	 * @return Message String
	 */
	protected static String buildMessage(String msg) {
		return new StringBuilder().append("[ ").append(msg).append("]").toString();
	}
}
