package com.huizhuang.zxsq.ui.activity.account;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import cn.sharesdk.douban.Douban;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;

import com.huizhuang.zxsq.ZxsqActivityManager;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.module.parser.UserParser;
import com.huizhuang.zxsq.ui.activity.MainActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

public class SettingPassword extends BaseActivity implements OnClickListener{
	private CommonActionBar mCommonActionBar;	
	private Button btnEnsureOn;
	private Button btnEnsureOff;
	private EditText etOriginalPassword;
	private EditText etNewPassword;
	private EditText etAgainPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_password);
		initActionBar();
		initView();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		btnEnsureOn = (Button)findViewById(R.id.btn_ensure_on);
		btnEnsureOn.setOnClickListener(this);
		btnEnsureOff = (Button)findViewById(R.id.btn_ensure_off);
		etOriginalPassword = (EditText)findViewById(R.id.et_original_pw);
		etNewPassword = (EditText)findViewById(R.id.et_new_pw);
		etAgainPassword = (EditText)findViewById(R.id.et_again_new_pw);
		etOriginalPassword.addTextChangedListener(watcher);
		etNewPassword.addTextChangedListener(watcher);
		etAgainPassword.addTextChangedListener(watcher);
	}
	
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_setting_passwrod);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				}
		});
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		
		case R.id.btn_ensure_on:
			LogUtil.d("openId:"+ZxsqApplication.getInstance().getUser().getOpenId());
			if(etNewPassword.getText().toString().equals(etAgainPassword.getText().toString())){
				HttpchangePwd();
			}else{
				showToastMsg("亲，你的原密码或新密码输入有误");
			}
			break;
		default:
			break;
			
		}
		
    }
	/**
	 * 检查3个edittext是否满足至少输入6位
	 */
	private boolean checkIsLegal(){
		if(etOriginalPassword.getText().toString().length()>5&&etNewPassword.getText().toString().length()>5&&etAgainPassword.getText().toString().length()>5)
			return true;
		return false;
	}

	/**
	 * 监听edittext字数变化
	 */
	private TextWatcher watcher = new TextWatcher() {
		private CharSequence temp;  
	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	        // TODO Auto-generated method stub
	    }
	    
	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	        // TODO Auto-generated method stub
	        
	    }
	    
	    @Override
	    public void afterTextChanged(Editable s) {
	        // TODO Auto-generated method stub
	    	if(checkIsLegal()){
				btnEnsureOff.setVisibility(View.GONE);
				btnEnsureOn.setVisibility(View.VISIBLE);
			}else{
				btnEnsureOn.setVisibility(View.GONE);
				btnEnsureOff.setVisibility(View.VISIBLE);
			}
	    }
	};
	
	/**
	 * 修改密码
	 */
	private void HttpchangePwd(){
		RequestParams params = new RequestParams();
		params.put("ori_pwd", etOriginalPassword.getText().toString());
		params.put("new_pwd", etNewPassword.getText().toString());
		HttpClientApi.post(HttpClientApi.REQ_USER_CHANGE_PASSWORD, params, new RequestCallBack<String>() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showWaitDialog("修改中...");
			}
			@Override
			public void onSuccess(String response) {
					showToastMsg("密码修改成功");
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ZxsqApplication.getInstance().setUser(null);
							ZxsqApplication.getInstance().setLogged(false);
							BroadCastManager.sendBroadCast(SettingPassword.this, BroadCastManager.ACTION_USER_LOGOUT);
							//AppManager.getInstance().finishAllActivity();
							PrefersUtil.getInstance().removeSpf("token");
							PrefersUtil.getInstance().removeSpf("userId");
							ActivityUtil.nextActivityWithClearTop(SettingPassword.this, UserLoginActivity.class);
							finish();
						}
					}, 1000);
					
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
