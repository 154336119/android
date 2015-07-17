package com.huizhuang.zxsq.ui.activity.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * 惠装服务协议（注册）页面
 * 
 * @ClassName: UserRegisterAgreementActicity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-24 上午9:18:43
 */
public class UserRegisterAgreementActicity extends BaseActivity {

	private DataLoadingLayout mDataLoadingLayout;
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.user_registration_agreement);

		initView();
		initActionBar();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.title_protocol);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnBackOnClick(v);
			}
		});
	}

	private void initView() {
		LogUtil.d("initView");

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);

		mWebView = (WebView) findViewById(R.id.web_view);
		WebViewClient webViewClient = new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				LogUtil.d("mWebView onPageStarted url = " + url);

				mDataLoadingLayout.showDataLoading();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				LogUtil.d("mWebView onPageFinished url = " + url);

				mDataLoadingLayout.showDataLoadSuccess();
			}

		};
		mWebView.setWebViewClient(webViewClient);
		mWebView.loadUrl(HttpClientApi.REGISTRATION_AGREEMENT);
	}

	/**
	 * 按钮事件 - 返回
	 * 
	 * @param v
	 */
	protected void btnBackOnClick(View v) {
		LogUtil.d("initActionBar View = " + v);

		finish();
	}

}
