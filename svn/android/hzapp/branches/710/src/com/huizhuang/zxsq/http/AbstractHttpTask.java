package com.huizhuang.zxsq.http;


import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.util.Log;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.security.Security;
import com.huizhuang.zxsq.utils.LogUtil;
import com.lgmshare.http.netroid.DefaultRetryPolicy;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.RequestQueue;
import com.lgmshare.http.netroid.exception.NetroidError;


public abstract class AbstractHttpTask<T> implements ResponseParser<T>{

	private final String TAG = AbstractHttpTask.class.getSimpleName();
	
    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 10 * 1000;
    

    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 1;

    /** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULT = 1f;
    
	private Context mContext;
	private RequestQueue mRequestQueue;
	
	protected RequestParams mRequestParams;
	
	protected AbstractHttpResponseHandler<T> mHandler;
	
	protected BeanParserRequest<T> mRequest;
	
	private boolean mUseCache = false;
	private int mCacheExpireTime = 30;
	
	public AbstractHttpTask(Context context) {
		mContext = context;
		mRequestQueue = ZxsqApplication.getInstance().getRequestQueue();
		mRequestParams = new RequestParams();
	}
	
	public void send(){
		addGlobParams(mRequestParams);
		mRequest = new BeanParserRequest<T>(getMethod(), getUrl(), mRequestParams, this, mRequestCallBack);
		mRequest.setShouldCache(mUseCache);
		mRequest.setCacheExpireTime(TimeUnit.MINUTES, mCacheExpireTime);
		mRequest.setTag(mContext);
		mRequest.addHeader("User-Agent", "huizhuang;shengqi;"+ZxsqApplication.getInstance().getVersionName()+";android-phone");
		mRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
		LogUtil.e(TAG, mRequest.toString());
		LogUtil.e(TAG, mRequestParams.toString());
		mRequestQueue.add(mRequest);
	}
	
	private void addGlobParams(RequestParams params) {
		params.remove("user_id");
		User user = ZxsqApplication.getInstance().getUser();
		if (user != null && user.getUser_id() != null && user.getToken() != null) {
			String tokenValue = user.getUser_id() + user.getToken();
			params.add("user_id", user.getUser_id());
			params.put("access_token", Security.getHMACSHA256String(tokenValue));
			params.put("token", user.getToken());
			LogUtil.i(" device_token:" + ZxsqApplication.getInstance().getDeviceToken());
			params.put("device_token", ZxsqApplication.getInstance().getDeviceToken());
		} else {
			params.put("access_token", Security.getHMACSHA256String(""));
			params.put("device_token", ZxsqApplication.getInstance().getDeviceToken());
		}
	}
	
	private RequestCallBack<T> mRequestCallBack = new RequestCallBack<T>() {
		
		@Override
		public void onStart() {
			super.onStart();
			mHandler.onStart();
		}
		
		@Override
		public void onSuccess(T t) {
			mHandler.onSuccess(t);
		}
		
		@Override
		public void onFailure(NetroidError error) {
			mHandler.onFailure(error.exceptionCode, error.getMessage());
		}
		
		@Override
		public void onProgress(long fileSize, long downloadedSize) {
			super.onProgress(fileSize, downloadedSize);
			mHandler.onProgress(fileSize, downloadedSize);
		}
		
		@Override
		public void onFinish() {
			super.onFinish();
			mHandler.onFinish();
		}
		
		@Override
		public void onCancel() {
			super.onCancel();
			mHandler.onCancel();
		}
		
	};

	public void setCallBack(AbstractHttpResponseHandler<T> handler){
		mHandler = handler;
	}
	
	public void cancel(){
		if (mRequest != null) {
			mRequest.cancel();
		}
	}
	
	public void cancelAll(){
		mRequestQueue.cancelAll(mContext);
	}
	
	public void setUseCache(boolean useCache){
		mUseCache = useCache;
	}
	
	public abstract String getUrl();
	
	public abstract int getMethod();
	
}
