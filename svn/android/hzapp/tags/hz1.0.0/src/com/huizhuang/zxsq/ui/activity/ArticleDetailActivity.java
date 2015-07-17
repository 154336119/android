package com.huizhuang.zxsq.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Article;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.Util;
import com.lgmshare.http.netroid.exception.*;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;

public class ArticleDetailActivity extends BaseActivity implements OnClickListener{

	private Article mArticle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mArticle = (Article) getIntent().getExtras().getSerializable(AppConstants.PARAM_ARTICLE);
		
		setContentView(R.layout.recommed_article_detial);
		setHeader("文章详情", this);
		
		findViewById(R.id.btn_favorite).setOnClickListener(this);
		findViewById(R.id.btn_share).setOnClickListener(this);
		
		queryDetail();
	}
	
	private void loadHtml(){
		WebView webView = (WebView) findViewById(R.id.webView);
//		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(mArticle.getHtmlUrl());
	}
	
	private void queryDetail(){
		showWaitDialog("");
		RequestParams params = new RequestParams();
		if (ZxsqApplication.getInstance().isLogged()) {
			params.put("user_id", ZxsqApplication.getInstance().getUser().getId());
		}
		params.put("cnt_id", mArticle.getId());
		params.put("cnt_type", mArticle.getType());
		HttpClientApi.post(HttpClientApi.REQ_ARTICLE_RECOMMED_DETAIL, params, new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject strJson = new JSONObject(response);
					JSONObject dataJson = strJson.getJSONObject("data");
					boolean favorited = dataJson.getInt("is_ff") == 0?false:true;
					String url = dataJson.getString("url");
					mArticle.setFavorited(favorited);
					mArticle.setHtmlUrl(url);
					
					loadHtml();
					updateFavotiteStatus();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
	
	private void favorite(){
		showWaitDialog("收藏中...");
		RequestParams params = new RequestParams();
		params.put("user_id", ZxsqApplication.getInstance().getUser().getId());
		params.put("cnt_id", mArticle.getId());
		params.put("cnt_type", mArticle.getType());
		String url = null;
		if (mArticle.isFavorited()) {
			url = HttpClientApi.REQ_COMMON_FAVORITE_DEL;
		}else{
			url = HttpClientApi.REQ_COMMON_FAVORITE;
		}
		HttpClientApi.post(url, params, new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(String response) {
				if (mArticle.isFavorited()) {
					mArticle.setFavorited(false);
					showToastMsg("取消成功");
				}else{
					mArticle.setFavorited(true);
					showToastMsg("收藏成功");
				}
				updateFavotiteStatus();
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

	private void updateFavotiteStatus(){
		ImageView ivFavorite = (ImageView) findViewById(R.id.btn_favorite);
		if (mArticle.isFavorited()) {
			ivFavorite.setImageResource(R.drawable.ic_favorited);
		}else{
			ivFavorite.setImageResource(R.drawable.ic_favorite);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ActivityUtil.back(ArticleDetailActivity.this);
			break;
		case R.id.btn_favorite:
			favorite();
			break;
		case R.id.btn_share:
			Share share = new Share();
			String text=String.format(getString(R.string.txt_share_zxwz),mArticle.getTitle());
			share.setText(text);
			Util.showShare(false,ArticleDetailActivity.this,share);
			break;
		default:
			break;
		}
	}
}
