package com.huizhuang.zxsq.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class WebActivity extends BaseActivity {

	private DataLoadingLayout mDataLoadingLayout;
	private WebView mWebView;
	private String mUrl;
	private String mUrlData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_webview);

		getIntentExtras();
		initActionBar();
		initViews();
	}

	private void getIntentExtras() {
		mUrl = getIntent().getStringExtra(AppConstants.PARAM_WEBWIEW_URL);
		mUrlData = getIntent().getStringExtra(AppConstants.PARAM_WEBVIEW_DATA);
//		mUrl = "http://www.minmasoft.com/Noname2.php";
//		mUrlData = "11622817742296a2dd1af5c1ef9ac7c7&finance_id=3766" +
//				"&token=11622817742296a2dd1af5c1ef9ac7c7"+
//				"&access_token=cf79056c5a0eefc85671bdc49910d0f8e71ed3b92af8f2e9bf5301187a731176&user_id=5156";
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
		webSettings.setJavaScriptEnabled(true);
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
			}
			
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view,
					String url) {
				// TODO Auto-generated method stub
				return super.shouldInterceptRequest(view, url);
			}
		};
		mWebView.setWebViewClient(webViewClient);
		if(!TextUtils.isEmpty(mUrlData)){
			LogUtil.e("mUrlData", mUrlData.getBytes()+"");
			//mWebView.postUrl(mUrl,mUrlData.getBytes());
			//mWebView.postUrl(mUrl, EncodingUtils.getBytes(mUrlData, "base64"));
				mWebView.postUrl(mUrl, mUrlData.getBytes());
			//URLEncoder.encode(bankPayParams.getVersion(), "UTF-8")
		//	mWebView.loadUrl(mUrl);
		}else{
			mWebView.loadUrl(mUrl);
		}
	}

}
