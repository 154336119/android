package com.huizhuang.zxsq.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

//自动发送上行登录短信
public class AutoSendSmsBroadcastReceiver extends BroadcastReceiver {
	public final static String ACTION_SEDN = "android.intent.action.SEND";
	public final static String ACTION_DELIVERY = "android.intent.action.DELIVERY";
	public final static String EXTRA_SMSBODY = "smsbody";
	public final static String EXTRA_SMSSRCPHONE= "smssrcphone";
	public final static String EXTRA_SMSDSCPHONE = "smsdesphone";
	public final static String EXTRA_SMSDATE = "smsdate";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		switch(getResultCode()){
		case Activity.RESULT_OK:
			if(action.equals(ACTION_SEDN)){
				String body = intent.getStringExtra(EXTRA_SMSBODY);
				String desphone = intent.getStringExtra(EXTRA_SMSDSCPHONE);
				String date = intent.getStringExtra(EXTRA_SMSDATE);
				insertSms(context,body,desphone,date);
			}else if(action.equals(ACTION_DELIVERY)){
				
			}
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
		case SmsManager.RESULT_ERROR_NO_SERVICE:
		case SmsManager.RESULT_ERROR_NULL_PDU: 
		case SmsManager.RESULT_ERROR_RADIO_OFF: 
			Toast.makeText(context, "短信发送失败", Toast.LENGTH_LONG).show();
		default:
			break;
		}
	}

	private void insertSms(Context context,String body, String desphone, String date) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("body", body);
		contentValues.put("address", desphone);
		contentValues.put("date", date);
		contentValues.put("read", 0);
		contentValues.put("type", 2);
		context.getContentResolver().insert(Uri.parse("content://sms/sent"), contentValues);
	}

}
