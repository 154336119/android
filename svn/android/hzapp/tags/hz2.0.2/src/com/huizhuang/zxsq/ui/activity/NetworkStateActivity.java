package com.huizhuang.zxsq.ui.activity;

import com.huizhuang.zxsq.receiver.NetChangeObserver.NetType;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;

public abstract class NetworkStateActivity extends BaseActivity {

	/**
	 * 网络连接连接时调用
	 */
	@Override
	public void onConnect(NetType type) {
		showToastMsg("检测到网路连接");
	}

	/**
	 * 当前没有网络连接
	 */
	@Override
	public void onDisConnect() {
		showToastMsg("网路连接已断开");
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
}
