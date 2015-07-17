package com.huizhuang.zxsq.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;

/**
 * @ClassName: EmailUtil
 * @Description: 邮件发送工具
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3  上午09:41:34
 */
public class EmailUtil {

	/**
	 * 发送邮件
	 * @param activity
	 * @param requestCode
	 * @param receiver
	 * @param title
	 * @param content
	 */
	public static void sendMail(Activity activity, int requestCode, String[] receiver, String title, String content){
	    String addresses = "mailto: ";
	    for (int i = 0; i < receiver.length; i++) {
	    	addresses = addresses + receiver[i];
		}
	    Intent in = new Intent("android.intent.action.SENDTO", Uri.parse(addresses));
	    in.putExtra("android.intent.extra.SUBJECT", title);
	    in.putExtra("android.intent.extra.TEXT", Html.fromHtml(content));
	    activity.startActivityForResult(in, requestCode);
	}
	
	public static void sendMail(Activity activity,String content){
		Intent i = new Intent(Intent.ACTION_SEND);  
		//i.setType("text/plain"); //模拟器
		i.setType("message/rfc822") ; //真机
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{"lgmshare@gmail.com"});  
		i.putExtra(Intent.EXTRA_SUBJECT,"Android客户端");  
		i.putExtra(Intent.EXTRA_TEXT,content);  
		activity.startActivity(Intent.createChooser(i, "Sending mail..."));
	}
}
