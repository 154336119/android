package com.huizhuang.zxsq.ui.activity.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.dialog.LoadingDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * FragmentActivity基类
 * 
 * @ClassName: BaseFragmentActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-12-3 下午3:31:19
 */
public class BaseFragmentActivity extends FragmentActivity {

	public Activity THIS;
	/** 默认提示框 */
	private Toast mToast;
	/** 加载等待框 */
	private LoadingDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ZxsqApplication.getInstance().getStackManager().addActivity(this);
		LogUtil.d("onCreate");
		super.onCreate(savedInstanceState);
		THIS = this;
		// 取消标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsUtil.addAnalytics(THIS);
	}

	@Override
	protected void onPause() {
		super.onPause();
        ImageLoader.getInstance().clearMemoryCache();
		AnalyticsUtil.removeAnalytics(THIS);
	}

	@Override
	protected void onDestroy() {
		ZxsqApplication.getInstance().getRequestQueue().cancelAll(this);
		ZxsqApplication.getInstance().getStackManager().finishActivity(this);
		super.onDestroy();
	}

	/**
	 * 系统toast提示
	 * 
	 * @param msg
	 */
	public void showToastMsg(String msg) {
		if (mToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				mToast.cancel();
			}
		} else {
			mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		}
		if (TextUtils.isEmpty(msg)) {
			msg = "";
		}
		mToast.setText(msg);
		mToast.show();
	}

	/**
	 * 显示加载框
	 * 
	 * @param msg
	 */
	public void showWaitDialog(String msg) {
		if (mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog(this);
		}
		mLoadingDialog.setText(msg);
		if (!mLoadingDialog.isShowing()) {
			mLoadingDialog.show();
		}
	}

	/** 隐藏加载等待框 */
	public void hideWaitDialog() {
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}

}
