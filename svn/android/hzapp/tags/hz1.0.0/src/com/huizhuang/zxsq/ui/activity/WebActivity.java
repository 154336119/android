package com.huizhuang.zxsq.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class WebActivity extends BaseActivity {

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
		};
		mWebView.setWebViewClient(webViewClient);
		mWebView.loadUrl(mUrl);
	}

}
