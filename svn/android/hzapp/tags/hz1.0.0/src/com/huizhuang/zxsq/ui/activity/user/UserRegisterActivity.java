package com.huizhuang.zxsq.ui.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.base.UserLoginActivityBase;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.dialog.TipsAlertDialog;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * @ClassName: UserRegisterActivity
 * @Description: 用户注册
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月3日 下午3:34:43
 */
public class UserRegisterActivity extends UserLoginActivityBase implements OnClickListener {

	private CommonActionBar mCommonActionBar;

	private EditText phoneEditText;
	private EditText passwordEditText;
	private ImageView imageViewShowCode;
	private boolean isShowCode = false;
	private TextView tvPotocol;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register_activity);
		initActionBar();
		initViews();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_register);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initViews() {
		findViewById(R.id.btn_register).setOnClickListener(this);
		findViewById(R.id.tv_protocol).setOnClickListener(this);
		tvPotocol = (TextView) findViewById(R.id.tv_protocol);
		phoneEditText = (EditText) findViewById(R.id.et_phone);
		passwordEditText = (EditText) findViewById(R.id.et_password);
		imageViewShowCode = (ImageView) findViewById(R.id.im_showCode);
		imageViewShowCode.setOnClickListener(this);
	}

	/**
	 * 获取验证码
	 */
	private void httpRequestQueryCheckCode() {
		showWaitDialog("");
		RequestParams params = new RequestParams();
		params.put("mobile", ZxsqApplication.getInstance().getUser().getPhone());
		HttpClientApi.post(HttpClientApi.REQ_USER_REGISTE_SEND_CODE, params, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onSuccess(Result result) {
				showToastMsg("验证码已发送，请注意查收！");
				ActivityUtil.next(UserRegisterActivity.this, UserCheckCodeActivity.class);
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

	private void showDialog() {
		CheckBox cbProtocol = (CheckBox) findViewById(R.id.checkBox);
		final String phone = phoneEditText.getText().toString();
		final String password = passwordEditText.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			showToastMsg("请输入手机号码");
		} else if (!Util.isMobileNum(phone)) {
			showToastMsg("请输入正确的手机号码");
		} else if (TextUtils.isEmpty(password)) {
			showToastMsg("请输入密码");
		} else if (password.length() < 6) {
			showToastMsg("密码不能小于6位");
		} else if (!cbProtocol.isChecked()) {
			showToastMsg("请勾选服务协议");
		} else {
			hideSoftInput();
			StringBuffer buffer = new StringBuffer(phone);
			buffer.insert(7, " ");
			buffer.insert(3, " ");
			new TipsAlertDialog(this).Builder().setTitle("确认手机号").setMsg("我们将发送验证码短信到这个手机号:\n" + "+86 " + buffer.toString())
					.setPositiveButton("好", new OnClickListener() {
						@Override
						public void onClick(View v) {
							User user = new User();
							user.setPhone(phone);
							user.setPassword(password);
							ZxsqApplication.getInstance().setUser(user);
							httpRequestQueryCheckCode();
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();

		}
	}

	private void showCode(boolean isShow) {
		if (isShow) {
			passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		} else {
			passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back :
				finish();
				break;
			case R.id.btn_register :
				AnalyticsUtil.onEvent(THIS, UmengEvent.ID_LOGIN_NEXT);
				showDialog();
				break;
			case R.id.tv_protocol :
				ActivityUtil.next(UserRegisterActivity.this, UserRegisterAgreementActicity.class);
				break;
			case R.id.im_showCode :
				if (isShowCode == true) {
					isShowCode = false;
					imageViewShowCode.setImageDrawable(getResources().getDrawable(R.drawable.code_hide));
				} else if (isShowCode == false) {
					isShowCode = true;
					imageViewShowCode.setImageDrawable(getResources().getDrawable(R.drawable.code_show));
				}
				showCode(isShowCode);
				break;
			default :
				break;
		}
	}

	@Override
	protected void onUserLogin() {
		ActivityUtil.backWithResult(THIS, Activity.RESULT_OK, null);
	}

	@Override
	protected void onUserLogOut() {
	}
}
