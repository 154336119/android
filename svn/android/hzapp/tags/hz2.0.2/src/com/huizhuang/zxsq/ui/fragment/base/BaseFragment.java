package com.huizhuang.zxsq.ui.fragment.base;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.dialog.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: BaseFragment
 * @Description:
 * @author LGM
 * @mail lgmshare@gmail.com
 * @date 2014-6-9 下午09:17:04
 */
public class BaseFragment extends Fragment {

	/** Toast */
	private Toast mToast;
	/** ProgressDialog */
	private LoadingDialog mLoadingDialog;
	
	/*@Override
	public void onResume() {
		super.onResume();
		AnalyticsUtil.onPageStart();
	}
	@Override
	public void onPause() {
		super.onPause();
		AnalyticsUtil.onPageEnd();
	}*/
	
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);

		if (this.getView() != null) {
			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
		}
	}

	protected View findViewById(int res) {
		return getView().findViewById(res);
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
			mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);
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
			mLoadingDialog = new LoadingDialog(getActivity(), msg);
		}
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
