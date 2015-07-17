package com.huizhuang.zxsq.ui.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.parser.UserParser;
import com.huizhuang.zxsq.ui.activity.base.UserLoginActivityBase;
import com.huizhuang.zxsq.ui.activity.user.UserCheckPhoneActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.ui.activity.user.UserRegisterActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;



/** 
* @ClassName: StartActivity 
* @Description: 应用开始页面
* @author Jean 
* @mail 154336119@qq.com   
* @date 2014-11-7 上午10:47:34 
*  
*/
/** 
* @ClassName: StartActivity 
* @Description:  
* @author Jean 
* @mail 154336119@qq.com   
* @date 2014-11-7 上午10:47:55 
*  
*/
public class StartActivity extends UserLoginActivityBase implements Callback,
		OnClickListener, PlatformActionListener {

	private static final int MSG_USERID_FOUND = 1;
	private static final int MSG_LOGIN = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR = 4;
	private static final int MSG_AUTH_COMPLETE = 5;

	private User mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);
		//开启用户指引
		if (ZxsqApplication.getInstance().isNewInstall()) {
			ActivityUtil.next(StartActivity.this, UserGuideActivity.class, -1, -1);
		}
		initViews();
	}

	private void initViews(){
		findViewById(R.id.btn_login_qq).setOnClickListener(this);
		findViewById(R.id.btn_login_sina).setOnClickListener(this);
		findViewById(R.id.btn_other).setOnClickListener(this);
		findViewById(R.id.btn_register).setOnClickListener(this);
		findViewById(R.id.btn_see).setOnClickListener(this);
	}
	
	/**
	 * 第三方登录
	 */
	private void openLogin(String platname, String userid, String nickname, String icon){
		LogUtil.d("openLogin");
		showWaitDialog("请稍后...");
		RequestParams params = new RequestParams();
		params.put("open_type", platname);
		params.put("open_id", userid);
		params.put("nickname", nickname);
		params.put("avatar", icon);
		HttpClientApi.post(HttpClientApi.REQ_USER_LOGIN_THIRD, params, new UserParser(), new RequestCallBack<User>() {

			@Override
			public void onSuccess(User user) {
				onUnregisterReceiver();
				
				PreferenceConfig.saveUsername(StartActivity.this, user.getUsername());
				ZxsqApplication.getInstance().setUser(user);
				validPhone();
			}

			@Override
			public void onFailure(NetroidError error) {
				showToastMsg(error.getMessage());
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_qq:
			showWaitDialog("正在授权");
			AnalyticsUtil.onEvent(THIS, UmengEvent.ID_LOGIN_QQ);
			authorize(QQ.NAME);
			break;
		case R.id.btn_login_sina:
			showWaitDialog("正在授权");
			AnalyticsUtil.onEvent(THIS, UmengEvent.ID_LOGIN_SINA);
			authorize(SinaWeibo.NAME);
			break;
		case R.id.btn_other:
			AnalyticsUtil.onEvent(THIS, UmengEvent.ID_LOGIN_OTHER);
			ActivityUtil.next(StartActivity.this, UserLoginActivity.class);
			break;
		case R.id.btn_register:
			ActivityUtil.next(StartActivity.this, UserRegisterActivity.class);
			break;
		case R.id.btn_see:
			ActivityUtil.nextActivityWithClearTop(StartActivity.this, MainActivity.class);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 第三方授权登录
	 * @param plat
	 */
	private void authorize(String platformName) {
		// ShareSDK.initSDK(this);
		Platform plat = ShareSDK.getPlatform(StartActivity.this, platformName);
		if (plat == null) {
			showToastMsg("平台还未安装");
			return;
		}
		if (plat.isValid()) {
			String userId = plat.getDb().getUserId();
			if (!TextUtils.isEmpty(userId)) {
				UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
				plat.setPlatformActionListener(this);
				plat.SSOSetting(false); // true表示不使用SSO方式授权
				// plat.authorize();
				plat.showUser(null);
				// login(plat.getName(), userId, null);
				return;
			}
		}
		plat.showUser(null);
		plat.setPlatformActionListener(this);
		plat.SSOSetting(false); // true表示不使用SSO方式授权
		// plat.authorize();
	}

	private void login(String plat, String userId, HashMap<String, Object> userInfo) {
		Message msg = new Message();
		msg.what = MSG_LOGIN;
		msg.obj = userId;
		mUser = Util.parseShareSDKUser(plat, userInfo);
		mUser.setOpenId(userId);
		mUser.setPlatName(plat);
		UIHandler.sendMessage(msg, this);
	}
	
	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
		hideWaitDialog();
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
			login(platform.getName(), platform.getDb().getUserId(), res);
		}
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		hideWaitDialog();
		ShareSDK.removeCookieOnAuthorize(true);
		platform.removeAccount();
		t.printStackTrace();
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
		}
	}

	@Override
	public void onCancel(Platform platform, int action) {
		hideWaitDialog();
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
		}
	}

	/**
	 * 验证手机号
	 */
	private void validPhone(){
		User user = ZxsqApplication.getInstance().getUser();
		//是否需要跳过手机号绑定
		if (TextUtils.isEmpty(user.getPhone()) && PreferenceConfig.isValidatePhone(this, user.getUsername())) {
			PreferenceConfig.saveValidatePhone(this, ZxsqApplication.getInstance().getUser().getUsername(), false);
			ActivityUtil.next(StartActivity.this, UserCheckPhoneActivity.class, true);
		}else{
			ActivityUtil.next(StartActivity.this, MainActivity.class, true);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_USERID_FOUND:
			break;
		case MSG_LOGIN:
			hideWaitDialog();
			showToastMsg("授权成功");
			openLogin(mUser.getPlatName(), mUser.getOpenId(), mUser.getNickname(), mUser.getAvatar());
			break;
		case MSG_AUTH_CANCEL:
			hideWaitDialog();
			showToastMsg("登录已取消");
			break;
		case MSG_AUTH_ERROR:
			hideWaitDialog();
			showToastMsg("授权失败");
			break;
		case MSG_AUTH_COMPLETE:
			hideWaitDialog();
			showToastMsg("授权成功");
			break;
		}
		return false;
	}

	@Override
	protected void onUserLogin() {
        ActivityUtil.nextActivityWithClearTop(StartActivity.this, MainActivity.class);
	}

	@Override
	protected void onUserLogOut() {
	}
}
