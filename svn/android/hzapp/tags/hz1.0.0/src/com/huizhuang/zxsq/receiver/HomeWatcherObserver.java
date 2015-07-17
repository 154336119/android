package com.huizhuang.zxsq.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;



/**
 * @ClassName: HomeWatcher
 * @Description: Home键监听封装
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3  上午11:15:12
 */
public class HomeWatcherObserver {

	private Context mContext;
	private IntentFilter mFilter;
	private OnHomePressedListener mListener;
	private HomeWatcherRecevier mRecevier;

	// 回调接口
	public interface OnHomePressedListener {
		public void onHomePressed();

		public void onHomeLongPressed();
	}

	public HomeWatcherObserver(Context context) {
		mContext = context;
		mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	}

	/**
	 * 设置监听
	 * 
	 * @param listener
	 */
	public void setOnHomePressedListener(OnHomePressedListener listener) {
		mListener = listener;
		mRecevier = new HomeWatcherRecevier();
		mRecevier.setWatcherManager(this);
	}

	/**
	 * 开始监听，注册广播
	 */
	public void startWatch() {
		if (mRecevier != null) {
			mContext.registerReceiver(mRecevier, mFilter);
		}
	}

	/**
	 * 停止监听，注销广播
	 */
	public void stopWatch() {
		if (mRecevier != null) {
			mContext.unregisterReceiver(mRecevier);
		}
	}
	
	/**
	 * home键按下
	 */
	public void homePressed(){
		mListener.onHomePressed();
	}

	/**
	 * home键长按
	 */
	public void homeLongPressed(){
		mListener.onHomeLongPressed();
	}
}
