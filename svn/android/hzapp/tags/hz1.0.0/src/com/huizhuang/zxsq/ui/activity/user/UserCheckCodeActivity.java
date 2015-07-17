package com.huizhuang.zxsq.ui.activity.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.module.parser.UserParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName: UserCheckCodeActivity
 * @Package com.huizhuang.zxsq.ui.activity.user
 * @Description: 注册获取验证码
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-8-28  上午11:07:20
 */
public class UserCheckCodeActivity extends BaseActivity implements OnClickListener{

	private final int DELAYED_TIME = 1000;

	private int mTimes;
	private Dialog mDialog;
	
	private User mUser;
	private String mCheckCode;
	private CommonActionBar mCommonActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register_checkcode_activity);
		initActionBar();
		initView();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_check_code);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

    private void initView() {
        findViewById(R.id.btn_code).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

		TextView tvPhone = (TextView) findViewById(R.id.tv_phone);
		mUser = ZxsqApplication.getInstance().getUser();
		StringBuffer buffer = new StringBuffer(mUser.getPhone());
		buffer.insert(7, " ");
		buffer.insert(3, " ");
		tvPhone.setText("+86 "+buffer.toString());
	}

	/**
	 * 注册账号
	 */
	private void httpRequestRegister(){
		EditText codeEditText = (EditText) findViewById(R.id.et_code);
		String code = codeEditText.getText().toString();
		if (TextUtils.isEmpty(code)) {
			showToastMsg("请输入验证码");
		} else if (code.length() < 4) {
			showToastMsg("验证码不能小于4位");
		} else if (!TextUtils.isEmpty(mCheckCode) && !mCheckCode.equals(code)) {
			showToastMsg("验证码错误");
		} else {
			hideSoftInput();
			showWaitDialog("请稍候...");
			RequestParams params = new RequestParams();
			params.put("mobile", mUser.getPhone());
			params.put("password", mUser.getPassword());
			params.put("sms_code", code);
			HttpClientApi.post(HttpClientApi.REQ_USER_REGISTE, params,new UserParser(), new RequestCallBack<User>() {
				
				@Override
				public void onSuccess(User user) {
					showToastMsg("注册成功");
					ZxsqApplication.getInstance().setUser(user);
					ActivityUtil.next(UserCheckCodeActivity.this, GuessULikeActivity.class);
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
	 * 获取验证码
	 */
	private void getCheckCode(){
		RequestParams params = new RequestParams();
		params.put("mobile", mUser.getPhone());
		HttpClientApi.post(HttpClientApi.REQ_USER_REGISTE_SEND_CODE, params, new ResultParser(), new RequestCallBack<Result>() {
			
			@Override
			public void onSuccess(Result result) {
				showToastMsg("验证码已发送，请注意查收！");
				try {
					JSONObject dataObject = new JSONObject(result.data);
					mCheckCode = dataObject.optString("code");
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
	
	/**
	 * 重新发送验证码
	 */
	@SuppressLint("InflateParams") 
	private void resetCode() {
		final View view = LayoutInflater.from(this).inflate(R.layout.user_register_send_code_dialog, null);
		view.findViewById(R.id.btn_send).setOnClickListener(this);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
		
		mDialog = new Dialog(UserCheckCodeActivity.this, R.style.DialogBottomPop);
		mDialog.setContentView(view);
		Window window = mDialog.getWindow();
		window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
//		window.setWindowAnimations(R.style.PopupwindowAnim); // 添加动画
		mDialog.show();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			AnalyticsUtil.onEvent(THIS, UmengEvent.ID_LOGIN_COMPLATE);			
			httpRequestRegister();
			break;
		case R.id.btn_code:
			View view = findViewById(R.id.et_code);
			hideSoftInput(view);
			if (mTimes <= 0) {
				resetCode();
			}else{
				showToastMsg("不能频繁操作，请稍后再试");
			}
			break;
		case R.id.btn_send:
			mDialog.dismiss();
			mDialog = null;
			mTimes = 60;
			getCheckCode();
			break;
		case R.id.btn_cancel:
			mDialog.dismiss();
			mDialog = null;
			break;
		default:
			break;
		}
	}
	
	/**
	 * 更新重新发送时间
	 */
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
				updateTimes();
				break;
			case 1:
				break;

			default:
				break;
			}
		};
	};

}
