package com.huizhuang.zxsq.ui.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.parser.UserParser;
import com.huizhuang.zxsq.ui.activity.base.UserLoginActivityBase;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @ClassName: UserLoginActivity
 * @Package com.huizhuang.zxsq.ui.activity.user
 * @Description: 用户登录页面
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月16日 下午4:40:37
 */
public class UserLoginActivity extends UserLoginActivityBase implements Callback,
		OnClickListener, PlatformActionListener {
	
	private boolean isShowCode = false;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private ImageView imageViewShowCode;
	private CommonActionBar mCommonActionBar;

	private static final int REQ_REGISTER_CODE = 100;
	private static final int REQ_FORGET_CODE = 101;

	private static final int MSG_USERID_FOUND = 1;
	private static final int MSG_LOGIN = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR = 4;
	private static final int MSG_AUTH_COMPLETE = 5;
	private static final int MSG_WECHAT_EXSIT = 6;
	
	private User mTempuser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login_activity);
		//初始化ShareSDK
		//ShareSDK.initSDK(this);
		initActionBar();
		initViews();
	}
	
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_user_login);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initViews() {
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btn_forget).setOnClickListener(this);
		findViewById(R.id.btn_register).setOnClickListener(this);

		findViewById(R.id.btn_weixin).setOnClickListener(this);
		findViewById(R.id.btn_qq).setOnClickListener(this);
		findViewById(R.id.btn_weibo_sina).setOnClickListener(this);
		findViewById(R.id.btn_weibo_tengxun).setOnClickListener(this);
		findViewById(R.id.im_showCode).setOnClickListener(this);
		String username = PreferenceConfig.getUsername(this);
		
		usernameEditText = (EditText) findViewById(R.id.et_username);
		passwordEditText = (EditText) findViewById(R.id.et_password);
		imageViewShowCode = (ImageView)findViewById(R.id.im_showCode);

		if (!TextUtils.isEmpty(username)) {
			usernameEditText.setText(username);
			usernameEditText.setSelection(username.length());
			//默认不弹出软键盘
			getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}

		if (AppConfig.DEBUG_MODE) {
			usernameEditText.setText("13551871367");
			passwordEditText.setText("135165");
			usernameEditText.setSelection("13551871367".length());
			//默认不弹出软键盘
			getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * 直接登录
	 */
	private void httpRequestlogin() {
		if (!ZxsqApplication.getInstance().isNetworkAvailable()) {
			showToastMsg("请检测网路连接");
			return;
		}

		String username = usernameEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		if (TextUtils.isEmpty(username)) {
			showToastMsg("请输入用户名");
		} else if (TextUtils.isEmpty(password)) {
			showToastMsg("请输入密码");
		} else if (password.length() < 6) {
			showToastMsg("密码不能小于6位");
		} else {
			hideSoftInput();
			showWaitDialog("登录中...");
			RequestParams params = new RequestParams();
			params.put("username", username);
			params.put("password", password);
			HttpClientApi.post(HttpClientApi.REQ_USER_LOGIN, params, new UserParser(), new RequestCallBack<User>() {

				@Override
				public void onSuccess(User user) {
					showToastMsg("登录成功");
					loginSuccess(user);
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
	}

	/**
	 * 第三方登录
	 * @param platname
	 * @param userid
	 * @param nickname
	 * @param icon
	 */
	private void openLogin(String platname, String userid, String nickname, String icon){
		LogUtil.e("openLogin:openLogin");
		showWaitDialog("请稍后...");
		RequestParams params = new RequestParams();
		params.put("open_type", platname);
		params.put("open_id", userid);
		params.put("nickname", nickname);
		params.put("avatar", icon);
		HttpClientApi.post(HttpClientApi.REQ_USER_LOGIN_THIRD, params, new UserParser(), new RequestCallBack<User>() {

			@Override
			public void onSuccess(User user) {
				LogUtil.e("onSuccess:onSuccess");
				userLogin(user);
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
	
	private void loginSuccess(User user){
		userLogin(user);
		setResult(RESULT_OK);
		finish();
	}
	
	/**
	 * 验证手机号
	 */
	private void validPhone(){
		LogUtil.e("validPhone:validPhone");
		User user = ZxsqApplication.getInstance().getUser();
		LogUtil.e("user:"+user);
		if (TextUtils.isEmpty(user.getPhone()) && PreferenceConfig.isValidatePhone(this, user.getUsername())) {
			PreferenceConfig.saveValidatePhone(this, ZxsqApplication.getInstance().getUser().getUsername(), false);
			ActivityUtil.next(UserLoginActivity.this, UserCheckPhoneActivity.class, true);
		}else{
			setResult(RESULT_OK);
			finish();
		}
	}
	
	private void userLogin(User user){
		onUnregisterReceiver();
		PreferenceConfig.saveUsername(UserLoginActivity.this, user.getUsername());
		ZxsqApplication.getInstance().setUser(user);
	}
	
	@Override
	public void onClick(View v) {
		LogUtil.i("jiengyh  onClick");
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_login:
			httpRequestlogin();
			break;
		case R.id.btn_forget:
			ActivityUtil.next(UserLoginActivity.this, UserForgetActivity.class, null, REQ_FORGET_CODE);
			break;
		case R.id.btn_register:
			AnalyticsUtil.onEvent(THIS, UmengEvent.ID_LOGIN_REGISTER);
			ActivityUtil.next(UserLoginActivity.this, UserRegisterActivity.class, null, REQ_REGISTER_CODE);
			break;
		case R.id.btn_weixin:
			showWaitDialog("正在授权");
			LogUtil.e("正在授权");
			authorize(Wechat.NAME);
			break;
		case R.id.btn_qq:
			showWaitDialog("正在授权");
			AnalyticsUtil.onEvent(THIS, UmengEvent.ID_LOGIN_QQ_BUTTON);
			authorize(QQ.NAME);
			break;
		case R.id.btn_weibo_sina:
			showWaitDialog("正在授权");
			AnalyticsUtil.onEvent(THIS, UmengEvent.ID_LOGIN_SINA_BUTTON);
			authorize(SinaWeibo.NAME);
			break;
		case R.id.btn_weibo_tengxun:
			showWaitDialog("正在授权");
			authorize(TencentWeibo.NAME);
			break;
		case R.id.im_showCode:
			if(isShowCode==true){
				isShowCode = false;
				imageViewShowCode.setImageDrawable(getResources().getDrawable(R.drawable.code_hide));
			}else if(isShowCode==false){
				isShowCode = true;
				imageViewShowCode.setImageDrawable(getResources().getDrawable(R.drawable.code_show));
			}
			showCode(isShowCode);
			break;
		default:
			break;
		}
	}
	/**
	 * 第三方授权登录
	 * @param platformName
	 */
	private void authorize(String platformName) {
		ShareSDK.initSDK(this);
		
		Platform plat = ShareSDK.getPlatform(UserLoginActivity.this, platformName);
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
//				plat.authorize();
				plat.showUser(null);
//				login(plat.getName(), userId, null);
				LogUtil.e("jiengyh authorize platformName plat.isValid():"+plat.isValid()+"  userId:"+userId);
				return;
			}
		}
		
		plat.setPlatformActionListener(this);
		//关闭SSO授权
		plat.SSOSetting(false);
		plat.showUser(null);
//		plat.authorize();
		LogUtil.e("jiengyh authorize platformName plat.isValid():"+plat.isValid());
	}
	private void login(String plat, String userId, HashMap<String, Object> userInfo) {
		Message msg = new Message();
		msg.what = MSG_LOGIN;
		msg.obj = userId;
		mTempuser = Util.parseShareSDKUser(plat, userInfo);
		mTempuser.setOpenId(userId);
		mTempuser.setPlatName(plat);
		UIHandler.sendMessage(msg, this);
	}
	
	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
		hideWaitDialog();
		LogUtil.e("jiengyh onComplete action:"+action);
		LogUtil.e("res:"+res.toString());
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
		LogUtil.e("jiengyh onError ");
		t.printStackTrace();
		if ("WechatClientNotExistException".equals(t.getClass().getSimpleName())) {
			UIHandler.sendEmptyMessage(MSG_WECHAT_EXSIT, this);
		}else if(action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
		}
	}

	@Override
	public void onCancel(Platform platform, int action) {
		hideWaitDialog();
		LogUtil.e("jiengyh onCancel:");
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_REGISTER_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				ActivityUtil.backWithResult(UserLoginActivity.this, Activity.RESULT_OK, null);
			}
		}
	}
	
	private void showCode(boolean isShow){
		if(isShow){
			passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); 
		}else{
			passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);    

		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		LogUtil.e("msg:"+msg);
		switch (msg.what) {
		case MSG_USERID_FOUND:
			break;
		case MSG_LOGIN:
			hideWaitDialog();
			showToastMsg("授权成功");
			LogUtil.e("授权成功!");
			openLogin(mTempuser.getPlatName(), mTempuser.getOpenId(), mTempuser.getNickname(), mTempuser.getAvatar());
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
//			showToastMsg("授权成功");
			break;
		case MSG_WECHAT_EXSIT:
			showToastMsg("请安装微信客户端");
			break;
		}
		
		return false;
	}
	
	@Override
	protected void onUserLogin() {
		ActivityUtil.backWithResult(THIS, Activity.RESULT_OK, null);
	}

	@Override
	protected void onUserLogOut() {
	}
}
