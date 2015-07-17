package com.huizhuang.zxsq.ui.activity.user;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.Phone;
import com.huizhuang.zxsq.http.bean.Result;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.http.parser.PhoneParser;
import com.huizhuang.zxsq.http.parser.ResultParser;
import com.huizhuang.zxsq.http.parser.UserParser;
import com.huizhuang.zxsq.receiver.SmsVerifyObserver;
import com.huizhuang.zxsq.ui.activity.base.UserLoginActivityBase;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.dialog.TipsAlertDialog;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.umeng.fb.model.UserInfo;

public class UserMessageLoginActivity extends UserLoginActivityBase implements OnClickListener,
		TextWatcher{
	private final int DELAYED_TIME = 1000;//验证码的渐变时间：间隔1秒
    private final int WATING_TIME = 60;//1分钟之后重新发送验证码
    private int mTimes;
	private CommonActionBar mCommonActionBar;
	private EditText mEdtTxtPhoneNumber;
	private Button mBtnSendVerify;
	private EditText mEdtTxtVerify;
	private Button mBtnLogin;
	private String mPhoneNumber;
	private Handler mhandler;
	private String mVerifyCode;
	private TextView mTvUseProtocal;
	private TextView mTvLoginByMessage;
	private SmsVerifyObserver mSmsVerifyObserver;
	private TagAliasCallback mTagAliasCallback;
	public static final int WHAT_VERIFY_TIME_UPDATE = 0;
	public static final int WHAT_VERIFY_SUCCESS = 2;//验证码发送成功
	public static final int WHAT_VERIFY_FAILURE = 1;//验证码发送失败
	public static final int WHAT_VERIFY_AUTO_FILL = 3;//验证码自动填充
	public static final int WHAT_SET_ALIAS = 4;//为JPush设置别名
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_message);
		initActionBar();
		initViews();
		setTagAliasCallback();
		setHandler();
		mTimes = WATING_TIME;
		//onRegisterSmsVerifyObserver();
	}

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar)findViewById(R.id.login_message_common_action_bar);
		mCommonActionBar.setActionBarTitle("短信验证码登录");
		mCommonActionBar.setLeftImgBtn(R.drawable.ic_close, new OnClickListener() {
			@Override
			public void onClick(View v) {
				AnalyticsUtil.onEvent(UserMessageLoginActivity.this, UmengEvent.ID_CLICK_0022);
				finish();
			}
		});
	}
	private void initViews() {
		mEdtTxtPhoneNumber = (EditText)findViewById(R.id.edtTxt_login_message_phonenumber);
		mEdtTxtVerify = (EditText)findViewById(R.id.edtTxt_login_message_verify);
		mBtnSendVerify = (Button)findViewById(R.id.btn_login_message_send_verify);
		mBtnLogin = (Button)findViewById(R.id.btn_login_message_login);
		mTvLoginByMessage = (TextView)findViewById(R.id.tv_login_message_by_message);
		mTvUseProtocal = (TextView)findViewById(R.id.tv_login_message_protocal);
		
		//设置手机号输入框的监听器以激活登录按钮
		mEdtTxtPhoneNumber.addTextChangedListener(this);
		//设置验证码输入框的监听器以激活登录按钮
		mEdtTxtVerify.addTextChangedListener(this);
		//设置点击"发送验证码"按钮事件的监听器
		mBtnSendVerify.setOnClickListener(this);
		//设置点击"登录"按钮事件的监听器
		mBtnLogin.setOnClickListener(this);
		//设置点击"登录"按钮事件的监听器
		mTvLoginByMessage.setOnClickListener(this);
		//设置使用协议的监听器
		mTvUseProtocal.setOnClickListener(this);
		
		//String username = PreferenceConfig.getUsername(this);
		String mobile = PrefersUtil.getInstance().getStrValue("mobile");
		if(!TextUtils.isEmpty(mobile)){
			if(Util.isValidMobileNumber(mobile)){
				mEdtTxtPhoneNumber.setText(mobile);
				mEdtTxtPhoneNumber.setSelection(mobile.length());
				//默认不弹出软键盘
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			}else{
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			}
		}else{
			//username为空或者不是有效的手机号自动弹出软键盘
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}
	}
	
	//注册监听手机短信收件箱的观察者
	private void onRegisterSmsVerifyObserver() {
		mSmsVerifyObserver = new SmsVerifyObserver(this , mhandler);
		this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mSmsVerifyObserver);
	}
	
	//提取短信字符串中的（6位数字）验证码
	private String getVerifyFromSms(String smsBody) {
		String verify = null;
		Pattern pattern = Pattern.compile("[^[0-9]]");
		Matcher matcher = pattern.matcher(smsBody);
		verify = matcher.replaceAll("").trim().substring(0,4);
		
		Log.i("verify", verify);
		return verify;
	}
	//取消注册监听手机短信收件箱的观察者
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//onUnregisterSmsVerifyObserver();
	}
	private void onUnregisterSmsVerifyObserver() {
		this.getContentResolver().unregisterContentObserver(mSmsVerifyObserver);
	}

	@Override
	protected void onUserLogin() {
		ActivityUtil.backWithResult(THIS, Activity.RESULT_OK, null);
	}

	@Override
	protected void onUserLogOut() {
		
	}
	@Override
	public void onClick(View v) {
		//该页面没有统计按钮事件的点击
		switch(v.getId()){
		//发送验证码
		case R.id.btn_login_message_send_verify:
			AnalyticsUtil.onEvent(this, UmengEvent.ID_CLICK_0019);
			mPhoneNumber = mEdtTxtPhoneNumber.getText().toString();
			if(TextUtils.isEmpty(mPhoneNumber)){
				showToastMsg("请先输入手机号");
			}else if(!Util.isValidMobileNumber(mPhoneNumber)){
				showToastMsg("请输入正确的手机号");
			}else if(Util.isValidMobileNumber(mPhoneNumber)){
				HttpRequestGetVerifyCode();
			}
			break;
		//短信登录
		case R.id.btn_login_message_login:
			AnalyticsUtil.onEvent(this, UmengEvent.ID_CLICK_0020);
			HttpMessageLogin();
			break;
	    //使用协议
		case R.id.tv_login_message_protocal:
			ActivityUtil.next(this, UserRegisterAgreementActicity.class);
			break;
		case R.id.tv_login_message_by_message:
			popTipsDialog();
			break;
		default:
			break;
		}
	}
	private void popTipsDialog() {
		TipsAlertDialog tipsDialog = new TipsAlertDialog(this).Builder();
		tipsDialog.setMsg("验证码太调皮了\n马上给惠装发个短信来登录吧~");
		tipsDialog.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtil.next(UserMessageLoginActivity.this, ConfirmPhoneActivity.class);
			}
		});
		tipsDialog.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		tipsDialog.show();
	}

	//请求得到验证码
	private void HttpRequestGetVerifyCode() {
		if(!ZxsqApplication.getInstance().isNetworkAvailable()){
			showToastMsg("请检测网络连接");
			return;
		}
		showWaitDialog("请求中...");
		RequestParams requestParams = new RequestParams();
		mPhoneNumber = mEdtTxtPhoneNumber.getText().toString();
		requestParams.put("mobile", mPhoneNumber);
		HttpClientApi.post(HttpClientApi.URL_LOGIN_MESSAGE_VERIFY_CODE, requestParams, new PhoneParser(), new RequestCallBack<Phone>(){
			@Override
			public void onFailure(NetroidError error) {
				showToastMsg(error.getMessage());
				Message msg = new Message();
				msg.what = WHAT_VERIFY_FAILURE;
				mhandler.sendMessageDelayed(msg, DELAYED_TIME);
			}

			@Override
			public void onSuccess(Phone phone) {
				showToastMsg("验证码发送成功，请查收");
				PreferenceConfig.setSendVerifyNumberByLogin(phone.getSend_phone());
				Message msg = new Message();
				msg.what = WHAT_VERIFY_SUCCESS;
				mhandler.sendMessage(msg);
				updateTimes();
				LogUtil.i(phone.toString());
			}
			@Override
			public void onFinish() {
				hideWaitDialog();
			}
		});
	}
	private void HttpMessageLogin() {
		if(!ZxsqApplication.getInstance().isNetworkAvailable()){
			showToastMsg("请检测网络连接");
			return;
		}
		showWaitDialog("请稍后，正在登录...");
		RequestParams requestParams = new RequestParams();
		mVerifyCode = mEdtTxtVerify.getText().toString().trim();
		mPhoneNumber = mEdtTxtPhoneNumber.getText().toString();
		requestParams.put("mobile", mPhoneNumber);
		requestParams.put("verify", mVerifyCode);
		HttpClientApi.post(HttpClientApi.REQ_USER_LOGIN_MESSAGE, requestParams, new UserParser(), new RequestCallBack<User>() {

			@Override
			public void onFailure(NetroidError error) {
				showToastMsg(error.getMessage());
				LogUtil.i(error.getMessage());
			}

			@Override
			public void onSuccess(User user) { 
				showToastMsg("登录成功");
				LogUtil.i(user.toString());
				loginSuccess(user);
			}
			@Override
			public void onFinish() {
				hideWaitDialog();
			}
		});
	}
	protected void loginSuccess(User user) {
		userLogin(user);
		setResult(RESULT_OK);
		finish();
	}

	private void userLogin(User user) {
		onUnregisterReceiver();
		PreferenceConfig.saveUsername(this, user.getUsername());
		//userId、token、mobile必须保存起来，否则在没有下过单的情况下，不管用户的登录信息是否失效，在闪屏页都不能走自动登录的流程
		PrefersUtil.getInstance().setValue("userId", user.getUser_id());
		PrefersUtil.getInstance().setValue("token", user.getToken());
		PrefersUtil.getInstance().setValue("mobile", mPhoneNumber);
		ZxsqApplication.getInstance().setUser(user, true);
		ZxsqApplication.getInstance().getUser().setMobile(mPhoneNumber);
		PreferenceConfig.writeFailureTime();
		JPushInterface.resumePush(this);
		setAlias(user);//将用户的id上传到JPush服务器，以便JPush针对单位用户推送消息
	}

	private void setAlias(User user) {
		String alias = user.getUser_id();
		if(alias!=null){
			if(!Util.isValidTagAndAlias(alias)){
				LogUtil.i("针对JPush,不是有效的别名");
			}else{
				mhandler.sendMessage(mhandler.obtainMessage(WHAT_SET_ALIAS, alias));
			}
		}
	}

	protected void updateTimes() {
		Message msg = new Message();
		if(mTimes<=1){
			msg.what = WHAT_VERIFY_FAILURE;
		}else{
			msg.what = WHAT_VERIFY_TIME_UPDATE;
		}
		mhandler.sendMessageDelayed(msg, DELAYED_TIME);
	}
	
	/**
	 * 将用户的别名绑定到JPush上之后的回调（可能会出现没有绑定成功的错误）
	 */
	private void setTagAliasCallback() {
		mTagAliasCallback = new TagAliasCallback() {
			@Override
			public void gotResult(int code, String alias, Set<String> tags) {
				switch(code){
				case 0://绑定成功
					LogUtil.i("用户Id作为别名绑定成功！");
					//这里可以往sharepreference里面写一个成功设置的状态。成功设置之后不必再次设置了。
					break;
				case 6002://设置超时，建议重试
					//延迟60秒来调用Handler设置别名
					mhandler.sendMessageDelayed(mhandler.obtainMessage(WHAT_SET_ALIAS, alias), 1000*60);
					break;
				default:
					break;	
				}
			}
		};
	}
	
	private void setHandler() {
		mhandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				int what = msg.what;
				switch(what){
				case WHAT_VERIFY_TIME_UPDATE://更新验证码的时间显示
					mTimes--;
					mBtnSendVerify.setText(mTimes+"	秒后发送验证码");
					updateTimes();
					break;
				case WHAT_VERIFY_FAILURE://验证码发送失败
					mBtnSendVerify.setText("发送验证码");
					mBtnSendVerify.setClickable(true);
					mBtnSendVerify.setTextColor(getResources().getColor(R.color.orange_light));
					break;
				case WHAT_VERIFY_SUCCESS://验证码发送成功
					mBtnSendVerify.setClickable(false);
					mBtnSendVerify.setTextColor(getResources().getColor(R.color.color_cccccc));
					break;
				case WHAT_VERIFY_AUTO_FILL://自动填充验证码
					String smsBody = (String)msg.obj;
					Log.i("smsBody", smsBody);
					String verify = getVerifyFromSms(smsBody);
					mEdtTxtVerify.setText(verify);
					//填充验证码之后激活"验证并登录"按钮
					mBtnLogin.setEnabled(true);
					//自动填充验证码的情况下，验证码填充后自动登录，无需用户手动点击"验证并登录"按钮
					HttpMessageLogin();
					break;
				case WHAT_SET_ALIAS://为JPush设置别名
					String alias = (String)msg.obj;
					LogUtil.i("调用JPush API，设置有效的别名");
					JPushInterface.setAliasAndTags(UserMessageLoginActivity.this, alias, null, mTagAliasCallback);
					break;
				default:
					break;
				}
			}
		};
	}

	//在mVerifyEdtTxt输入验证码满足4位数字的时候激活登录按钮
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		mVerifyCode = mEdtTxtVerify.getText().toString().trim();
		mPhoneNumber = mEdtTxtPhoneNumber.getText().toString();
		if(TextUtils.isEmpty(mVerifyCode)){
			mBtnLogin.setEnabled(false);
		}else if(mVerifyCode.length()==0){
			mBtnLogin.setEnabled(false);
		}else{
			if(!Util.isValidMobileNumber(mPhoneNumber)){
				mBtnLogin.setEnabled(false);
			}else{
				mBtnLogin.setEnabled(true);
			}
		}
	}
	

}
