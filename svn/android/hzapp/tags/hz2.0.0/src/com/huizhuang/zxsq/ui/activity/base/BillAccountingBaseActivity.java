package com.huizhuang.zxsq.ui.activity.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.LogUtil;

/**
 * 记账相关页面基类 - 继承BaseActivity
 * 
 * @ClassName: BillAccountingBaseActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-11 下午5:30:35
 */
public abstract class BillAccountingBaseActivity extends BaseActivity {

	private BroadcastReceiver mBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		registerReceiver();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.d("onDestroy");

		unRegisterReceiver();
	}

	/**
	 * 添加本地广播接收者
	 */
	private void registerReceiver() {
		LogUtil.d("registerReceiver");

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BroadCastManager.ACTION_BILL_ACCOUNTING_CHANGED);
		if (null == mBroadcastReceiver) {
			mBroadcastReceiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					LogUtil.d("BroadCastManager.ACTION_BILL_ACCOUNTING_CHANGED onReceive");

					if (null != intent && BroadCastManager.ACTION_BILL_ACCOUNTING_CHANGED.equalsIgnoreCase(intent.getAction())) {
						onReceiveBillAccountChanged();
					}
				}

			};
		}
		LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);
	}

	/**
	 * 移除本地广播接收者
	 */
	private void unRegisterReceiver() {
		LogUtil.d("unRegisterReceiver");

		if (null != mBroadcastReceiver) {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
		}
	}

	/**
	 * 子类实现记账账单状态改变的Receiver
	 */
	protected abstract void onReceiveBillAccountChanged();

}
