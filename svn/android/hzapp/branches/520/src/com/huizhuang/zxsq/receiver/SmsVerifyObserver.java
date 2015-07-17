package com.huizhuang.zxsq.receiver;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.ui.activity.user.UserMessageLoginActivity;

//手机收件箱的监听器，监听获取到的验证码
public class SmsVerifyObserver extends ContentObserver{
	private Handler mSmsHandler;
	private Context mContext;
	private String mNumber = AppConstants.SEND_VERIFY_NUMBER;//发送验证码的号码
	
	public SmsVerifyObserver(Context context,Handler handler) {
		super(handler);
		this.mContext = context;
		this.mSmsHandler = handler;
	}
	/**
	 * 当短信收件箱收到短信时的具体处理方法
	 */
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Uri SMS_URI_INBOX = Uri.parse("content://sms/inbox");
		String[] projection = new String[]{"body"};
		String selection = "address=? and read=? and type=?";
		String[] selectionArgs = new String[]{mNumber,"0","1"}; 
		Cursor c = mContext.getContentResolver().query(SMS_URI_INBOX, projection, selection, selectionArgs, "_id desc");
		StringBuilder sb = null;
		if(c != null){//短信为特定号码发送的且是未读
			c.moveToFirst();
			if(c.moveToFirst()){
				sb = new StringBuilder();
				sb.append(c.getString(c.getColumnIndex("body")));
			}
			c.close();
		}
		if(sb!=null){
			mSmsHandler.obtainMessage(UserMessageLoginActivity.WHAT_VERIFY_AUTO_FILL,sb.toString()).sendToTarget();
		}
	}
}