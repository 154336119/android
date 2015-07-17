package com.huizhuang.zxsq.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Looper;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.utils.LogUtil;

/**
 * @ClassName: CrashException
 * @Description: 应用崩溃异常
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月9日 下午4:51:44
 */
public class CrashException extends BaseException implements
		UncaughtExceptionHandler {

	private static final long serialVersionUID = 171420830016802708L;
	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	public CrashException() {
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
			ZxsqApplication.getInstance().exit();
		}
	}

	/**
	 * 自定义异常处理:收集错误信息&发送错误报告
	 * 
	 * @param ex
	 * @return true:处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		final Context context = ZxsqApplication.getInstance().getStackManager().getCurrentActivity();

		if (context == null) {
			return false;
		}

		final String crashReport = getCrashReport(context, ex);
		LogUtil.e(crashReport);
		if (!AppConfig.SEND_CARSH) {
			return false;
		}
		// 显示异常信息&发送报告
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				// UIHelper.sendAppCrashReport(context, crashReport);
				// 写在Looper.loop()之后的代码不会被执行，这个函数内部应该是一个循环，当调用mHandler.getLooper().quit()后，loop才会中止，其后的代码才能得以运行。
				ZxsqApplication.getInstance().exit();
				Looper.loop();
			}
		}.start();
		return true;
	}

	/**
	 * 获取APP崩溃异常报告
	 * 
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex) {
		PackageInfo pinfo = ((ZxsqApplication) context.getApplicationContext()).getPackageInfo();
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: " + pinfo.versionName + "(" + pinfo.versionCode + ")\n");
		exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.MODEL + ")\n");
		exceptionStr.append("Exception: " + ex.getMessage() + "\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString() + "\n");
		}
		return exceptionStr.toString();
	}
}
