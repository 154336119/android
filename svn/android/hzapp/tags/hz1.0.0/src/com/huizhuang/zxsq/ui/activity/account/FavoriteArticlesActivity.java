package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.CompanyArticles;
import com.huizhuang.zxsq.module.MyArticles;
import com.huizhuang.zxsq.module.MyArticles.ArticleDigest;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.parser.MyArticlesParser;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.company.CompanyArticleDetailsActicity;
import com.huizhuang.zxsq.ui.activity.company.CompanyArticlesActicity;
import com.huizhuang.zxsq.ui.adapter.FavoriteArticlesListAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.ToastUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.swipemenulistview.SwipeMenu;
import com.huizhuang.zxsq.widget.swipemenulistview.SwipeMenuCreator;
import com.huizhuang.zxsq.widget.swipemenulistview.SwipeMenuItem;
import com.huizhuang.zxsq.widget.swipemenulistview.SwipeMenuListView;
import com.huizhuang.zxsq.widget.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 收藏文章
 * 
 * @ClassName: MyPicturesActivity
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-30 上午11:20:00
 */
public class FavoriteArticlesActivity extends BaseActivity {

	/**
	 * Intent Extras
	 */
	public static final String EXTRA_USER_ID = "userId";

	private static final int DEFAULT_START_PAGE = AppConfig.DEFAULT_START_PAGE;

	/**
	 * 所有控件（页面中从上到下排列）
	 */
	private DataLoadingLayout mDataLoadingLayout;
	private SwipeMenuListView mSwipeMenuListView;
	private FavoriteArticlesListAdapter mFavoriteArticlesListAdapter;

	private Context mContext;

	private BroadcastReceiver mBroadcastReceiver;

	private String mUserId;
	private int mCurLoadPage = DEFAULT_START_PAGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_favorite_articles);
		mContext = FavoriteArticlesActivity.this;
		AnalyticsUtil.onEvent(mContext, UmengEvent.ID_FAVORITES_ARTICLES);

		getIntentExtras();
		initActionBar();
		initViews();

		registerReceiver();
		httpRequestQueryFavoriteArticlesData(mCurLoadPage);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.d("onDestroy");

		unRegisterReceiver();
	}
	/**
	 * 获取Intent中的Extras
	 */
	private void getIntentExtras() {
		LogUtil.d("getIntentExtras");

		mUserId = getIntent().getStringExtra(EXTRA_USER_ID);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.title_favorite_articles);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnBackOnClick(v);
			}

		});
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initViews");

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.favorite_articles_data_loading_layout);

		mSwipeMenuListView = (SwipeMenuListView) findViewById(R.id.my_articles_list);
		mSwipeMenuListView.setPullRefreshEnable(true);// 显示刷新
		mSwipeMenuListView.setPullLoadEnable(true);// 显示加载更多
		mSwipeMenuListView.setAutoRefreshEnable(false);// 开始自动加载
		mSwipeMenuListView.setAutoLoadMoreEnable(true);// 滚动到底部自动加载更多
		mSwipeMenuListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				LogUtil.d("mXListView onRefresh");
				// TODO - 目前没有请求接口，所以只是暂时延迟1秒，做一个效果
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mSwipeMenuListView.onRefreshComplete();
					}
				}, AppConfig.DEFAULT_THREAD_DELAY_MILLIS);
			}

			@Override
			public void onLoadMore() {
				LogUtil.d("mXListView onLoadMore");

				httpRequestQueryFavoriteArticlesData(++mCurLoadPage);
			}

		});

		// 如果当前查看的用户是登录者，则启用列表滑动功能
		User user = ZxsqApplication.getInstance().getUser();
		if (null != user && null != mUserId && mUserId.equals(user.getId())) {
			SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

				@Override
				public void create(SwipeMenu swipeMenu) {
					LogUtil.d("SwipeMenuCreator create SwipeMenu = " + swipeMenu);

					SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
					openItem.setBackground(R.color.color_f22c48);
					openItem.setWidth(dp2px(137));
					openItem.setTitle(R.string.txt_cancel_favorite);
					openItem.setTitleSize(20);
					openItem.setTitleColor(Color.WHITE);
					swipeMenu.addMenuItem(openItem);
				}

			};
			mSwipeMenuListView.setMenuCreator(swipeMenuCreator);
			mSwipeMenuListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
					LogUtil.d("mSwipeMenuListView onMenuItemClick position = " + position);

					httpRequestCancelFavoriteArticle(position);
					return false;
				}

			});

		}

		mFavoriteArticlesListAdapter = new FavoriteArticlesListAdapter(mContext);

		mSwipeMenuListView.setAdapter(mFavoriteArticlesListAdapter);
		mSwipeMenuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LogUtil.d("mSwipeMenuListView onItemClick position = " + position);

				ArticleDigest articleDigest = (ArticleDigest) parent.getAdapter().getItem(position);
				LogUtil.d("articleDigest = " +articleDigest);
				
				CompanyArticles companyArticles = new CompanyArticles();
				companyArticles.setAddTime(String.valueOf(articleDigest.getAddTime()));
				companyArticles.setCommentCount(articleDigest.getCommentCount());
				companyArticles.setDetailUrl(articleDigest.getDetailUrl());
				companyArticles.setFfCount(articleDigest.getFfCount());
				companyArticles.setId(String.valueOf(articleDigest.getCntId()));
				companyArticles.setImg(articleDigest.getThumb());
				
				companyArticles.setIsFavored(true);
				companyArticles.setShareCount(articleDigest.getShareCount());
				Bundle bundle = new Bundle();
				bundle.putSerializable(AppConstants.PARAM_DIARY, companyArticles);
				if(CompanyArticleDetailsActicity.CNT_TYPE_RENOVATION.equals(articleDigest.getCntType())){//后台埋的坑  这个收藏类型没有真的返回回来 直接使用的
					bundle.putInt(CompanyArticlesActicity.COMPANY_ARTICLES, 1);
				}else if(CompanyArticleDetailsActicity.CNT_TYPE_SUPERVISION.equals(articleDigest.getCntType())){
					bundle.putInt(CompanyArticlesActicity.COMPANY_ARTICLES, 0);
				}
				ActivityUtil.next(FavoriteArticlesActivity.this, CompanyArticleDetailsActicity.class, bundle, -1);
			}

		});
	}

	/**
	 * ActionBar - 返回按钮事件
	 * 
	 * @param v
	 */
	protected void btnBackOnClick(View v) {
		LogUtil.d("btnBackOnClick View = " + v);

		finish();
	}

	/**
	 * 添加本地广播接收者
	 */
	private void registerReceiver() {
		LogUtil.d("registerReceiver");

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BroadCastManager.ACTION_ARTICLE_FAVORITE_STATUS_CHANGED);
		if (null == mBroadcastReceiver) {
			mBroadcastReceiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					LogUtil.d("mBroadcastReceiver onReceive");

					// 有更新，重置mCurLoadPage;
					mCurLoadPage = DEFAULT_START_PAGE;
					httpRequestQueryFavoriteArticlesData(mCurLoadPage);
				}

			};
		}
		LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver, intentFilter);
	}

	/**
	 * 移除本地广播接收者
	 */
	private void unRegisterReceiver() {
		LogUtil.d("unRegisterReceiver");

		if (null != mBroadcastReceiver) {
			LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastReceiver);
		}
	}

	/**
	 * HTTP请求 - 获取收藏文章数据
	 */
	private void httpRequestQueryFavoriteArticlesData(int page) {
		LogUtil.d("httpRequestQueryFavoriteArticlesData page = " + page);

		RequestParams params = new RequestParams();
		params.add("profile_id", mUserId);
		params.add("page", String.valueOf(page));
		HttpClientApi.get(HttpClientApi.USER_FAVORLIST, params, new MyArticlesParser(), new RequestCallBack<MyArticles>() {

			@Override
			public void onSuccess(MyArticles myArticles) {
				LogUtil.i("httpRequestQueryFavoriteArticlesData onSuccess MyArticles = " + myArticles);

				if (null != myArticles && null != myArticles.getAcArticleDigestList() && myArticles.getAcArticleDigestList().size() > 0) {

					int totalPage = myArticles.getTotalpage();
					List<ArticleDigest> articleDigestList = myArticles.getAcArticleDigestList();

					if (mCurLoadPage == DEFAULT_START_PAGE) {
						mDataLoadingLayout.showDataLoadSuccess();
						mFavoriteArticlesListAdapter.setList(articleDigestList);
					} else if (mCurLoadPage <= totalPage) {
						mFavoriteArticlesListAdapter.addList(articleDigestList);
					}

					// 如果是最后一页，则禁用自动加载更多
					if (mCurLoadPage >= totalPage) {
						mSwipeMenuListView.setPullLoadEnable(false);
					}

				} else {
					if (mCurLoadPage == DEFAULT_START_PAGE) {
						mDataLoadingLayout.showDataEmptyView();
					} else {
						mSwipeMenuListView.setPullLoadEnable(false);
					}
				}

			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestQueryFavoriteArticlesData onFailure NetroidError = " + netroidError);

				String strError = netroidError.getMessage();
				if (mCurLoadPage == DEFAULT_START_PAGE) {
					mDataLoadingLayout.showDataLoadFailed(strError);
				} else {
					showToastMsg(strError);
					mCurLoadPage--;
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestQueryFavoriteArticlesData onFinish");

				mSwipeMenuListView.onLoadMoreComplete();
			}

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestQueryFavoriteArticlesData onStart");

				if (DEFAULT_START_PAGE == mCurLoadPage) {
					mDataLoadingLayout.showDataLoading();
				}
			}

		});
	}

	/**
	 * HTTP请求 - 取消收藏
	 * 
	 * @param digest
	 */
	private void httpRequestCancelFavoriteArticle(final int position) {
		LogUtil.d("httpRequestCancelFavoriteArticle position = " + position);

		ArticleDigest articleDigest = mFavoriteArticlesListAdapter.getItem(position);
		RequestParams params = new RequestParams();
		params.add("cnt_type", articleDigest.getCntType());
		params.add("cnt_id", String.valueOf(articleDigest.getCntId()));
		HttpClientApi.post(HttpClientApi.REQ_COMMON_FAVORITE_DEL, params, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onSuccess(Result result) {
				LogUtil.d("httpRequestCancelFavoriteArticle onSuccess Result = " + result);

				mFavoriteArticlesListAdapter.removeArticleDigest(position);

			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestCancelFavoriteArticle onFailure NetroidError = " + netroidError);

				ToastUtil.showMessageLong(mContext, netroidError.getMessage());
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestCancelFavoriteArticle onFinish");

				hideWaitDialog();
			}

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestCancelFavoriteArticle onStart");

				showWaitDialog(getString(R.string.txt_on_waiting));
			}

		});
	}

	private int dp2px(int dp) {
		LogUtil.d("dp2px dp = " + dp);

		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
}
