package com.huizhuang.zxsq.ui.activity.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.module.parser.UserParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * @ClassName: UserLoginCheckPhoneActivity
 * @Package com.huizhuang.zxsq.ui.activity.user
 * @Description: 第三方登录需要验证手机号
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-8-27  上午10:15:14
 */
public class UserCheckPhoneActivity extends BaseActivity implements OnClickListener{
	
	private final int DELAYED_TIME = 1000;
	private CommonActionBar mCommonActionBar;
	
	private Button mBtnGetCode;
	private EditText mEtPhone;
	private int mTimes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login_third_check_phone);
		initActionBar();
		initViews();
	}
	
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle("验证手机号");
		mCommonActionBar.setRightTxtBtn(R.string.skip, new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				goNextActivity();
			}
		});
	}
	
	private void initViews() {
		findViewById(R.id.btn_code).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		mEtPhone = (EditText) findViewById(R.id.et_phone);
		mBtnGetCode = (Button) findViewById(R.id.btn_code);
	}
	
	/**
	 * 验证
	 */
	private void httpRequesCheck(){
		EditText codeEditText = (EditText) findViewById(R.id.et_code);
		String code = codeEditText.getText().toString();
		String phone = mEtPhone.getText().toString();
		if (TextUtils.isEmpty(code)) {
			showToastMsg("请输入验证码");
		} else if (code.length() < 4) {
			showToastMsg("验证码不能小于4位");
		} else {
			hideSoftInput();
			showWaitDialog("请稍候...");
			RequestParams params = new RequestParams();
			params.put("mobile",phone);
			params.put("user_id", ZxsqApplication.getInstance().getUser().getId());
			params.put("sms_code", code);
			HttpClientApi.post(HttpClientApi.REQ_USER_LOGIN_VIREFY_PHONE, params,new UserParser(), new RequestCallBack<User>() {
				
				@Override
				public void onSuccess(User user) {
					showToastMsg("验证成功");
					goNextActivity();
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
	
	private void getCheckCode(){
		showWaitDialog("请求中...");
		RequestParams params = new RequestParams();
		params.put("mobile",mEtPhone.getText().toString());
		params.put("user_id",ZxsqApplication.getInstance().getUser().getId());
		HttpClientApi.post(HttpClientApi.REQ_USER_LOGIN_BIND_THIRD_SEND_CODE, params, new ResultParser(), new RequestCallBack<Result>() {
			
			@Override
			public void onSuccess(Result responseInfo) {
				showToastMsg("验证码已发送，请注意查收！");
				Message msg = mHandler.obtainMessage();
				msg.what = 2;
				mHandler.sendMessage(msg);
				updateTimes();
			}
			
			@Override
			public void onFailure(NetroidError error) {
				showToastMsg(error.getMessage());
				Message message = new Message();
				message.what = 1;
				mHandler.sendMessageDelayed(message, DELAYED_TIME);
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
			}
		});
	}
	
	private void updateTimes(){
		Message msg = mHandler.obtainMessage();
		if (mTimes <= 0) {
			msg.what = 1;
		}else {
			msg.what = 0;
		}
		mHandler.sendMessageDelayed(msg, DELAYED_TIME);
	}
	
	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch (what) {
			case 0:
				mTimes--;
				mBtnGetCode.setText(mTimes+"秒重新获取");
				updateTimes();
				break;
			case 1:
				mBtnGetCode.setText("获取验证码");
				mBtnGetCode.setClickable(true);
				mBtnGetCode.setBackgroundResource(R.drawable.btn_orange);
				break;
			case 2:
				mBtnGetCode.setClickable(false);
				mBtnGetCode.setBackgroundResource(R.drawable.btn_gray_normal);
				break;
			case 3:
				
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_code:
			mTimes = 60;
			getCheckCode();
			break;
		case R.id.btn_submit:
			httpRequesCheck();
			break;
		default:
			break;
		}
	}
	
	private void goNextActivity(){
		ActivityUtil.next(UserCheckPhoneActivity.this, GuessULikeActivity.class, true); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 888) {
			if (resultCode == Activity.RESULT_OK) {
				ActivityUtil.backWithResult(UserCheckPhoneActivity.this,Activity.RESULT_OK,null);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goNextActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}