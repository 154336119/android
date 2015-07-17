package com.huizhuang.zxsq.service;

import java.util.ArrayList;

import org.android.agoo.client.BaseConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.Author;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.NotificationUtil;
import com.huizhuang.zxsq.utils.ToastUtil;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.umeng.common.message.Log;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

/**
 * Developer defined push intent service. 
 * Remember to call {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}. 
 * @author lucas
 *
 */
public class UMengPushIntentService extends UmengBaseIntentService{
	private static final String TAG = UMengPushIntentService.class.getName();

	@Override
	protected void onMessage(Context context, Intent intent) {
		super.onMessage(context, intent);
		try {
			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
			UMessage msg = new UMessage(new JSONObject(message));
			LogUtil.d("custom: msg:"+msg);
			LogUtil.d( "custom:"+msg.custom);
			String custom=msg.custom;
			doMessage(msg);
			// code  to handle message here
			// ...
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
	/**
	 * 收到消息以后怎么处理
	 * @param custom  服务器通过友盟返回回来的json串
	 */
	private void doMessage(UMessage msg) {
		
		JSONObject obj;
		try {
			obj = new JSONObject(msg.custom);
			int messageType=obj.optInt("m");
			switch (messageType) {
			case 3://日记
				doRecommendDiary(msg);
				break;
				
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void doRecommendDiary(UMessage msg) throws JSONException {
		JSONObject obj=new JSONObject(msg.custom);
		String userId=obj.optString("id");
		LogUtil.i("doRecommendDiary userId:"+userId);
		RequestParams params = new RequestParams();
		params.put("profile_id", userId);
		HttpClientApi.post(HttpClientApi.REQ_ACCOUNT_USER, params, new ResultParser(), new RequestCallBack<Result>() {
			
			@Override
			public void onSuccess(Result result) {
				try{
					processResult(result.data);
				} catch (JSONException e) {
					
				}
			}
			
			@Override
			public void onFailure(NetroidError error) {
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			
		});
	}
	private void processResult(String result) throws JSONException {
		JSONObject dataJson = new JSONObject(result);
		//格式化用户详情
		String id = dataJson.optString("user_id");
		String nickname = dataJson.optString("nickname");
		String imagePath = dataJson.getString("user_thumb");
		String sex = dataJson.getString("gender");
		String province = dataJson.optString("province");
		String city = dataJson.optString("city");
		String roomStyle = dataJson.optString("room_style");
		String roomType = dataJson.optString("room_type");
		boolean isFollowed = false;
		if (!TextUtils.isEmpty(dataJson.getString("is_followed"))) {
			isFollowed = dataJson.getInt("is_followed") == 0 ? false : true;
		}
		Author author = new Author();
		author.setId(id);
		author.setName(nickname);
		author.setAvatar(imagePath);
		author.setGender(sex);
		author.setProvince(province);
		author.setCity(city);
		author.setRoomStyle(roomStyle);
		author.setRoomType(roomType);
		author.setFollowed((isFollowed ? "1" : "0")); // 是否已关注 1 是 0 否
		String msg="您的好友"+nickname+"写新日记了，快去看看吧";
		LogUtil.i("author:"+author);
		NotificationUtil.notifyRecommendDiary(UMengPushIntentService.this,AppConfig.NOTIFICATION_DIARY_TITLE , msg, AppConfig.NOTIFICATION_DIARY_TITLE, author);
	}
}
