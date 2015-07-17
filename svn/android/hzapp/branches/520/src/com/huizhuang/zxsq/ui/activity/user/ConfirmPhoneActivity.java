package com.huizhuang.zxsq.ui.activity.user;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.receiver.AutoSendSmsBroadcastReceiver;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.ClearEditText;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConfirmPhoneActivity extends BaseActivity implements OnClickListener,TextWatcher {
	private CommonActionBar mCommonActionBar;
	private ClearEditText mClrEdtTxtPhone;
	private Button mBtnSubmit;
	private String mPhoneNumber;
	private AutoSendSmsBroadcastReceiver mAutoSendSmsBroadcastReceiver;
	private TelephonyManager mTelephonyManager;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_phone);
		initActionBar();
		initView();
		readPhone();
		setHandler();
		onRegisterAutoSendSmsBroadcastReceiver();
	}
	
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar)findViewById(R.id.confirm_phone_common_action_bar);
		mCommonActionBar.setActionBarTitle("确认您的手机号");
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private void initView() {
		mClrEdtTxtPhone = (ClearEditText)findViewById(R.id.edtTxt_confirm_phone_phone);		
		mClrEdtTxtPhone.addTextChangedListener(mClrEdtTxtPhone);
		mBtnSubmit = (Button)findViewById(R.id.btn_confirm_phone_submit);
		mBtnSubmit.setOnClickListener(this);
		mClrEdtTxtPhone.addTextChangedListener(this);
	}
	private void readPhone() {
		mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String phone = mTelephonyManager.getLine1Number();
		if(phone!=null){
			if(Util.isValidMobileNumber(phone)){
				mClrEdtTxtPhone.setText(phone);
			}else{
				showToastMsg("自动获取您的手机号失败，请手动输入！");
			}
		}else{
			showToastMsg("请输入您的手机号！");
			return;
		}
	}
	private void setHandler() {
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				
			}
		};
	}
	private void onRegisterAutoSendSmsBroadcastReceiver(){
		mAutoSendSmsBroadcastReceiver = new AutoSendSmsBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AutoSendSmsBroadcastReceiver.ACTION_SEDN);
		intentFilter.addAction(AutoSendSmsBroadcastReceiver.ACTION_DELIVERY);
		registerReceiver(mAutoSendSmsBroadcastReceiver, intentFilter);
	}
	@Override
	public void onClick(View v) {
		//自动发送上行短信
		//sendLoginMessage("这是一条自动发送的短信","13541034596","13541034596");
		//请求得到短信登录码
	}
	
	private void sendLoginMessage(String sms,String srcPhone,String desPhone) {
		SmsManager smsManager = SmsManager.getDefault();
		//ArrayList<String> mutipleSms = smsManager.divideMessage(sms);
		Intent sendIntent = new Intent(AutoSendSmsBroadcastReceiver.ACTION_SEDN);
		sendIntent.putExtra(AutoSendSmsBroadcastReceiver.EXTRA_SMSBODY, sms);
		//sendIntent.putExtra(EXTRA_SMSSRCPHONE, srcPhone);
		sendIntent.putExtra(AutoSendSmsBroadcastReceiver.EXTRA_SMSDSCPHONE, desPhone);
		sendIntent.putExtra(AutoSendSmsBroadcastReceiver.EXTRA_SMSDATE, String.valueOf(System.currentTimeMillis()));
		PendingIntent sendPi = PendingIntent.getBroadcast(
				this, 0,sendIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent deliveryPi = PendingIntent.getBroadcast(
				this, 0, new Intent(AutoSendSmsBroadcastReceiver.ACTION_DELIVERY),PendingIntent.FLAG_UPDATE_CURRENT);
		smsManager.sendTextMessage(desPhone, srcPhone, sms, sendPi, deliveryPi);
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		onUnregisterAutoSendSmsBroadcastReceiver();
	}
	private void onUnregisterAutoSendSmsBroadcastReceiver() {
		unregisterReceiver(mAutoSendSmsBroadcastReceiver);
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
	@Override
	public void afterTextChanged(Editable s) {
		mPhoneNumber = mClrEdtTxtPhone.getText().toString().trim();
		if(Util.isValidMobileNumber(mPhoneNumber)){
			mBtnSubmit.setEnabled(true);
		}else{
			mBtnSubmit.setEnabled(false);
		}
	}
}
