package com.huizhuang.zxsq.ui.activity.advertise;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class AdvertiseWebActivity extends BaseActivity {

	private DataLoadingLayout mDataLoadingLayout;
	private WebView mWebView;
	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		getIntentExtras();
		initActionBar();
		initViews();
	}

	private void getIntentExtras() {
		mUrl = getIntent().getStringExtra("url");
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(getString(R.string.app_name));
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 初始化视图
	 */
	private void initViews() {
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
		mWebView = (WebView) findViewById(R.id.web_view);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);//设置支持javascript脚本
		WebViewClient webViewClient = new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			};

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mDataLoadingLayout.showDataLoading();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mDataLoadingLayout.showDataLoadSuccess();
				// html加载完成之后，添加监听按钮的点击js函数  
				//addButtonClickListener();
			}
		};
		mWebView.setWebViewClient(webViewClient);
		mWebView.loadUrl(mUrl);

		//添加js交互接口类，并起别名：shareToWechatMoment
		mWebView.addJavascriptInterface(new JavascriptInterface(AdvertiseWebActivity.this), "shareToWeiXin");//分享到微信朋友圈
	}
	/**
	 * 注入js函数监听
	 * */
	protected void addButtonClickListener() {
		LogUtil.e("addButtonClickListener()");
		mWebView.loadUrl("javascript:(function(){"
				+ "var obj1 = document.getElementById('fx_wx');"//分享至微信好友按钮的id
				+ "obj1.onclick = function(){"
				+ "window.shareToWeiXin.shareToWechat(this.share_url);}"
				+ "var obj2 = document.getElementById('fx_pyq');"//分享至微信朋友圈按钮的id
				+ "obj2.onclick = function(){"
				+ "window.shareToWeiXin.shareToWechatMoment(this.share_url);}"
				//+ "})()");
				+ "})");
	}
	//js通信接口
	public class JavascriptInterface{
		private Context context;
		public JavascriptInterface(Context context) {
			super();
			this.context = context;
		}
		
		/**
		 * 分享至微信好友
		 * @param url
		 */
		
		public void shareToWechat(String url){
			LogUtil.e(url);
			Util.ShareToWeiXin(context,url,Wechat.NAME);
		}
		
		/**
		 * 分享至微信朋友圈
		 * @param url
		 */
		public void shareToWechatMoment(String url){
			LogUtil.e(url);
			Util.ShareToWeiXin(context,url,WechatMoments.NAME);
		}
		
		public boolean isAndroid(){
			return true;
		}
		public void finish(){
			AdvertiseWebActivity.this.finish();
		}
	}
	//如果webview有回退的页面，则点击系统的返回键后返回到mwebview的上一个页面
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK && mWebView.canGoBack()){
			mWebView.goBack();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
}
