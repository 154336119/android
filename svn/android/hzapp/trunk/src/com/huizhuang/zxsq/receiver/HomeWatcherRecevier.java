package com.huizhuang.zxsq.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huizhuang.zxsq.utils.LogUtil;


/**
 * @ClassName: HomeWatcherRecevier
 * @Description: home键监听广播
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3  上午11:14:55
 */
public class HomeWatcherRecevier extends BroadcastReceiver{
	
	private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
	private static final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
	private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
	private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

	private HomeWatcherObserver watcherManager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
			if (reason != null) {
				LogUtil.d("home键监听：action:" + action + ",reason:" + reason);
				if (watcherManager != null) {
					if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
						// 短按home键
						watcherManager.homePressed();
					} else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
						// 长按home键
						watcherManager.homeLongPressed();
					}
				}
			}
		}
	}

	public HomeWatcherObserver getWatcherManager() {
		return watcherManager;
	}

	public void setWatcherManager(HomeWatcherObserver watcherManager) {
		this.watcherManager = watcherManager;
	}
	
}
