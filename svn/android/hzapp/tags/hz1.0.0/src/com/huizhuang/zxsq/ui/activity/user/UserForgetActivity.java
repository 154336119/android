package com.huizhuang.zxsq.ui.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.CodeUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * @ClassName: UserForgetActivity
 * @Package com.huizhuang.zxsq.ui.activity.user
 * @Description: 忘记密码、重置密码
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-8-28  上午11:08:19
 */
public class UserForgetActivity extends BaseActivity implements OnClickListener{

	private TextView mTvErrorTips;
	private ImageView mIvCodeView;
	private CommonActionBar mCommonActionBar;	
	private String mCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_forget_activity);
		initActionBar();
		initViews();
	}
	
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.forget_password);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				}
		});
	}
	
	private void initViews() {
		findViewById(R.id.btn_code).setOnClickListener(this);
		findViewById(R.id.btn_next).setOnClickListener(this);		
		mTvErrorTips = (TextView) findViewById(R.id.tv_error_tips);
		mIvCodeView = (ImageView) findViewById(R.id.btn_code);		
		getCode();
	}

	private void httpRequestsubmit(){
		EditText phoneEditText = (EditText) findViewById(R.id.et_phone);
		EditText codeEditText = (EditText) findViewById(R.id.et_code);
		
		final String phone = phoneEditText.getText().toString();
		String code = codeEditText.getText().toString();
		
		if (TextUtils.isEmpty(phone)) {
			showToastMsg("请输入手机号码");
		} else if (!Util.isMobileNum(phone)) {
			showToastMsg("请输入正确的手机号");
		} else if (TextUtils.isEmpty(code)) {
			showToastMsg("请输入验证码");
		} else if (code.length() < 4) {
			showToastMsg("验证码不能小于4位");
		} else if(!mCode.equals(code)){
			mTvErrorTips.setText("验证码输入错误");
			mTvErrorTips.setVisibility(View.VISIBLE);
		} else {
			hideSoftInput();
			showWaitDialog("请稍候...");
			RequestParams params = new RequestParams();
			params.put("mobile", phone);
			HttpClientApi.post(HttpClientApi.REQ_USER_RESET_PASSWORD, params, new ResultParser(), new RequestCallBack<Result>() {
				
				@Override
				public void onSuccess(Result result) {
					showToastMsg("新密码已发送到手机，请注意查收");
					finish();
				}
				
				@Override
				public void onFailure(NetroidError error) {
					showToastMsg(error.getMessage());
					mTvErrorTips.setText(error.getMessage());
					mTvErrorTips.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onFinish() {
					super.onFinish();
					hideWaitDialog();
				}
			});
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			httpRequestsubmit();
			break;
		case R.id.btn_code:
			getCode();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 生成随机验证码
	 */
	private void getCode(){
		CodeUtil codeUtil = CodeUtil.getInstance();
		mIvCodeView.setImageBitmap(codeUtil.createBitmap());
		mCode = codeUtil.getCode();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 888) {
			if (resultCode == Activity.RESULT_OK) {
				ActivityUtil.backWithResult(UserForgetActivity.this,Activity.RESULT_OK,null);
			}
		}
	}
}
