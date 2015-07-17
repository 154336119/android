package com.huizhuang.zxsq.ui.activity.zxbd;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * 装修宝典申请成功页面
 * 
 * @ClassName: ApplySuccessActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-27 上午9:31:50
 */
public class ApplySuccessActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.zxbd_apply_success_activity);

		initActionBar();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.txt_apply_success);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnBackOnClick();
			}
		});
		commonActionBar.setRightImgBtn(R.drawable.ic_actionbar_share, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnShareOnClick();
			}
		});
	}

	/**
	 * 按钮事件 - 返回
	 * 
	 * @param v
	 */
	protected void btnBackOnClick() {
		LogUtil.d("btnBackOnClick");

		finish();
	}

	/**
	 * 按钮事件 - 分享
	 * 
	 * @param v
	 */
	protected void btnShareOnClick() {
		LogUtil.d("btnShareOnClick");
		Share share = new Share();
		String text=(getString(R.string.txt_share_bmcg));
		share.setText(text);
		Util.showShare(false,this,share);
	}
}
