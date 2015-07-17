package com.huizhuang.zxsq.ui.activity.company;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.CompanyArticles;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

public class CompanyArticleDetailsActicity extends BaseActivity implements OnClickListener {
	public static final String URL = "url";
	private final int REQ_LOGIN_CODE = 661;
	private DataLoadingLayout mDataLoadingLayout;
	private WebView mWebView;
	private CompanyArticles mCompanyArticles;
	private ImageButton mImgBtnShare;
	private ImageButton mImgBtnFavorite;
	private int mCompanyArticleType;
	public static final String CNT_TYPE_RENOVATION=HttpClientApi.CntType.app_renovation_diary.name();
	public static final String CNT_TYPE_SUPERVISION=HttpClientApi.CntType.app_supervision_diary.name();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_articles_details);
		getIntentExtras();
		initView();
	}

	private void getIntentExtras() {
		mCompanyArticles = (CompanyArticles) getIntent().getExtras().getSerializable(AppConstants.PARAM_DIARY);
		mCompanyArticleType = getIntent().getExtras().getInt(CompanyArticlesActicity.COMPANY_ARTICLES);
	}

	private void initView() {
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);

		findViewById(R.id.img_btn_back).setOnClickListener(this);
		mImgBtnFavorite = (ImageButton) findViewById(R.id.img_btn_favorite);
		mImgBtnFavorite.setOnClickListener(this);
		mImgBtnShare = (ImageButton) findViewById(R.id.img_btn_share);
		mImgBtnShare.setOnClickListener(this);

		if (mCompanyArticles.getIsFavored()) {
			mImgBtnFavorite.setImageResource(R.drawable.company_article_on);
		}

		mWebView = (WebView) findViewById(R.id.web_view);
		WebViewClient webViewClient = new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mDataLoadingLayout.showDataLoading();
				mImgBtnFavorite.setVisibility(View.INVISIBLE);
				mImgBtnShare.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mDataLoadingLayout.showDataLoadSuccess();
				mImgBtnFavorite.setVisibility(View.VISIBLE);
				mImgBtnShare.setVisibility(View.VISIBLE);
			}

		};
		mWebView.setWebViewClient(webViewClient);
		mWebView.loadUrl(mCompanyArticles.getDetailUrl());
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_btn_back :
				finish();
				break;
			case R.id.img_btn_share :
				Share share = new Share();
				String text=String.format(getString(R.string.txt_share_zxwz),mCompanyArticles.getTtitle());
				share.setText(text);
				Util.showShare(false,this,share);
				break;
			case R.id.img_btn_favorite :
				if (!ZxsqApplication.getInstance().isLogged()) {
					ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
					return;
				}
				httpRequestFavorite();
				break;
			default :
				break;

		}
	}

	private void httpRequestFavorite() {
		showWaitDialog("收藏中...");
		RequestParams params = new RequestParams();
		params.put("cnt_id", mCompanyArticles.getId());
		if (mCompanyArticleType == CompanyArticlesActicity.RENOVATION_ARTICLES) {
			params.put("cnt_type", CNT_TYPE_RENOVATION);
		} else if (mCompanyArticleType == CompanyArticlesActicity.SUPERVISOR_ARTICLES) {
			params.put("cnt_type", CNT_TYPE_SUPERVISION);
		}
		String url = null;
		if (mCompanyArticles.getIsFavored()) {
			url = HttpClientApi.REQ_COMMON_FAVORITE_DEL;
		} else {
			url = HttpClientApi.REQ_COMMON_FAVORITE;
		}
		HttpClientApi.post(url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(String response) {
				;
				if (mCompanyArticles.getIsFavored()) {
					mCompanyArticles.setIsFavored(false);
					showToastMsg("取消成功");
				} else {
					mCompanyArticles.setIsFavored(true);
					showToastMsg("收藏成功");
				}
				// 发送文章收藏状态改变广播
				BroadCastManager.sendBroadCast(CompanyArticleDetailsActicity.this, BroadCastManager.ACTION_ARTICLE_FAVORITE_STATUS_CHANGED);
				updateFavorite();
			}

			@Override
			public void onFailure(NetroidError error) {
				showToastMsg(error.getMessage());
			}

			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
			}
		});
	}
	private void updateFavorite() {

		if (mCompanyArticles.getIsFavored()) {
			mImgBtnFavorite.setImageResource(R.drawable.company_article_on);
		} else {
			mImgBtnFavorite.setImageResource(R.drawable.company_article_off);
		}
	}

	/**
	 * 是否为监理日记
	 * 
	 * @return
	 */
	private boolean isSupervisorList() {
		if (mCompanyArticleType == CompanyArticlesActicity.SUPERVISOR_ARTICLES)
			return true;
		return false;
	}

	/**
	 * 是否为装修日记
	 * 
	 * @return
	 */
	private boolean isRenovationList() {
		if (mCompanyArticleType == CompanyArticlesActicity.RENOVATION_ARTICLES)
			return true;
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == REQ_LOGIN_CODE){
 			if (resultCode == Activity.RESULT_OK) {
 				initView();
			}
         }
	}
}
