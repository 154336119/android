package com.huizhuang.zxsq.ui.activity.account;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.huizhuang.zxsq.ZxsqActivityManager;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.ui.activity.MainActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.base.UserLoginActivityBase;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.FileUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.fb.FeedbackAgent;

/**
 * 设置界面
 * 
 * @ClassName: SettingActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-20 上午9:10:31
 */
public class SettingActivity extends UserLoginActivityBase {

	private TextView mTvCacheZize;
	private CheckBox mChbWifiLoadImage;

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.setting_activity);
		mContext = SettingActivity.this;

		initActionBar();
		initViews();

		getDiskCacheSizeByAsyncTask();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.title_setting);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initViews");

		mTvCacheZize = (TextView) findViewById(R.id.tv_cache_size);
		mChbWifiLoadImage = (CheckBox) findViewById(R.id.ck_wifi_load_image);
		LogUtil.d("AppContext.getInstance().getUser().getIsOpenLogin():"+ZxsqApplication.getInstance().getUser().getIsOpenLogin());
		if(ZxsqApplication.getInstance().getUser().getIsOpenLogin()){
			hideSettingPassword();
		}
		mChbWifiLoadImage.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PreferenceConfig.writeWIFILoadImageSwitch(SettingActivity.this, isChecked);
				ZxsqApplication.getInstance().getCloseLoadImgWhenNotInWifiState();
			}

		});
		mChbWifiLoadImage.setChecked(ZxsqApplication.getInstance().getCloseLoadImgWhenNotInWifiState());
	}

	/**
	 * 点击事件 - 绑定设置
	 * 
	 * @param v
	 */
	public void itemBindOnClick(View v) {
		ActivityUtil.next(SettingActivity.this, SettingBindActivity.class);
	}

	/**
	 * 点击事件 - 消息推送
	 * 
	 * @param v
	 */
	public void itemPushOnClick(View v) {
		ActivityUtil.next(SettingActivity.this, SettingPushActivity.class);
	}

	/**
	 * 点击事件 - 清除缓存
	 * 
	 * @param v
	 */
	public void itemClearDiskCacheOnClick(View v) {
		ZxsqApplication.getInstance().clearCache();
		getDiskCacheSizeByAsyncTask();
	}

	/**
	 * 点击事件 - 意见反馈
	 * 
	 * @param v
	 */
	public void itemFeedBackOnClick(View v) {
		FeedbackAgent agent = new FeedbackAgent(SettingActivity.this);
		agent.startFeedbackActivity();
	}

	/**
	 * 点击事件 - 给个好评
	 * 
	 * @param v
	 */
	public void itemPraiseOnClick(View v) {
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	/**
	 * 点击事件 - 版本信息
	 * 
	 * @param v
	 */
	public void itemEditionOnClick(View v) {
		ActivityUtil.next(SettingActivity.this, SettingEditionActivity.class);
	}
	/**
	 * 点击事件 - 修改密码
	 * 
	 * @param v
	 */
	public void itemChangePasswordOnClick(View v) {
		ActivityUtil.next(SettingActivity.this, SettingPassword.class);
	}
	/**
	 * 点击事件 - 退出登录
	 * 
	 * @param v
	 */
	public void btnLogoutOnClick(View v) {
		ZxsqApplication.getInstance().setUser(null);
		ZxsqApplication.getInstance().setLogged(false);
		BroadCastManager.sendBroadCast(mContext, BroadCastManager.ACTION_USER_LOGOUT);
		ZxsqActivityManager.getInstance().finishAllActivity();
		PrefersUtil.getInstance().removeSpf("token");
		PrefersUtil.getInstance().removeSpf("userId");

		ActivityUtil.nextActivityWithClearTop(SettingActivity.this, MainActivity.class);
	}

	/**
	 * 获取缓存大小
	 */
	private void getDiskCacheSizeByAsyncTask() {
		new DiskCacheSizeAsyncTask().execute();
	}

	/**
	 * 计算磁盘缓存的AsyncTask
	 */
	private class DiskCacheSizeAsyncTask extends AsyncTask<Void, Void, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(Void... params) {
			String size = "0";
			if (ImageLoader.getInstance().getDiskCache().getDirectory().isDirectory()) {
				DecimalFormat df = new DecimalFormat(".##"); // 两位小数
				size = df.format(FileUtil.getDirSize(ImageLoader.getInstance().getDiskCache().getDirectory()));
				if (size.startsWith(".")) {
					size = "0" + size;
				}
			}
			return size;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			mTvCacheZize.setText(result + "M");
		}
	

	}
	
	/**
	 * 若为第三方登录则隐藏密码修改界面
	 */
	private void hideSettingPassword(){
		findViewById(R.id.tv_setting_pw).setVisibility(View.GONE);
		findViewById(R.id.line12).setVisibility(View.GONE);
	}

	@Override
	protected void onUserLogin() {
		
	}

	@Override
	protected void onUserLogOut() {
		finish();
	}

}
