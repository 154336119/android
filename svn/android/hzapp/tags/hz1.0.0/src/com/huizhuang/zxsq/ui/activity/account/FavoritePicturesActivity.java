package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.UserFavorSketch;
import com.huizhuang.zxsq.module.UserFavorSketch.ImageInfo;
import com.huizhuang.zxsq.module.parser.UserFavorSketchParser;
import com.huizhuang.zxsq.ui.activity.AtlasBrowseActivity;
import com.huizhuang.zxsq.ui.adapter.FavoritePicturesListAdapter;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 收藏图片
 * 
 * @ClassName: MyPicturesActivity
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-30 上午11:00:00
 */
public class FavoritePicturesActivity extends BaseActivity {

	/**
	 * Intent Extras
	 */
	// private static final int REQ_DETAIL_CODE = 10;
	private static final String EXTRA_USRE_ID = "userId";
	private static final int DEFAULT_START_PAGE = AppConfig.DEFAULT_START_PAGE;

	private DataLoadingLayout mDataLoadingLayout;
	private XListView mXListView;
	private FavoritePicturesListAdapter mFavoritePicturesListAdapter;

	private Context mContext;

	private BroadcastReceiver mBroadcastReceiver;

	private String mUserId;
	private int mCurLoadPage = DEFAULT_START_PAGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_favorite_pictures);
		mContext = FavoritePicturesActivity.this;
		AnalyticsUtil.onEvent(mContext, UmengEvent.ID_FAVORITES_PICTURES);

		getIntentExtras();
		initActionBar();
		initViews();

		registerReceiver();
		httpRequestQueryFavoritePicturesData(mCurLoadPage);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.d("onDestroy");

		unRegisterReceiver();
	}

	/**
	 * 获得Intent传递过来的参数
	 */
	private void getIntentExtras() {
		LogUtil.d("getIntentExtras");

		mUserId = getIntent().getStringExtra(EXTRA_USRE_ID);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.title_favorite_pictures);
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

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.favorite_pictures_data_loading_layout);

		mXListView = (XListView) findViewById(R.id.my_pictures_list);
		mXListView.setPullRefreshEnable(true);// 显示刷新
		mXListView.setPullLoadEnable(true);// 显示加载更多
		mXListView.setAutoRefreshEnable(false);// 开始自动加载
		mXListView.setAutoLoadMoreEnable(true);// 滚动到底部自动加载更多
		mXListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				LogUtil.d("mXListView onRefresh");
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mXListView.onRefreshComplete();
					}
				}, AppConfig.DEFAULT_THREAD_DELAY_MILLIS);

			}

			@Override
			public void onLoadMore() {
				LogUtil.d("mXListView onLoadMore");

				httpRequestQueryFavoritePicturesData(++mCurLoadPage);
			}

		});
		mFavoritePicturesListAdapter = new FavoritePicturesListAdapter(mContext);
		mXListView.setAdapter(mFavoritePicturesListAdapter);
		mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LogUtil.d("mXListView onItemClick position = " + position);
				ImageInfo imageInfo = (ImageInfo) parent.getAdapter().getItem(position);
				Atlas atlas = new Atlas();
				atlas.setAlbumId((String.valueOf(imageInfo.getAlbum_id())));
				atlas.setId(String.valueOf(imageInfo.getId()));
				Bundle bundle = new Bundle();
				bundle.putSerializable(AppConstants.PARAM_ATLAS, atlas);
				ActivityUtil.next(FavoritePicturesActivity.this, AtlasBrowseActivity.class, bundle, -1);
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
		intentFilter.addAction(BroadCastManager.ACTION_PICTURE_FAVORITE_STATUS_CHANGED);
		intentFilter.addAction(BroadCastManager.ACTION_PICTURE_SHARE_STATUS_CHANGED);
		if (null == mBroadcastReceiver) {
			mBroadcastReceiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					LogUtil.d("mBroadcastReceiver onReceive");

					// 有更新，重置mCurLoadPage; （备注：这步骤在重新请求数据，考虑以后可以不及时刷新，让用户主动下拉刷新）
					mCurLoadPage = DEFAULT_START_PAGE;
					httpRequestQueryFavoritePicturesData(mCurLoadPage);
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
	 * HTTP请求 - 获取收藏图片数据
	 */
	private void httpRequestQueryFavoritePicturesData(int page) {
		LogUtil.d("httpRequestQueryFavoritePicturesData page = " + page);

		RequestParams params = new RequestParams();
		params.add("profile_id", mUserId);
		params.add("page", String.valueOf(page));
		HttpClientApi.get(HttpClientApi.USER_FAVORLIST_SKETCH, params, new UserFavorSketchParser(), new RequestCallBack<UserFavorSketch>() {

			@Override
			public void onSuccess(UserFavorSketch userFavorSketch) {
				LogUtil.d("httpRequestQueryFavoritePicturesData onSuccess UserFavorSketch = " + userFavorSketch);

				if (null != userFavorSketch && null != userFavorSketch.getImageInfoList() && userFavorSketch.getImageInfoList().size() > 0) {
					int totalPage = userFavorSketch.getTotalpage();
					List<ImageInfo> imageInfoList = userFavorSketch.getImageInfoList();

					if (mCurLoadPage == DEFAULT_START_PAGE) {
						mDataLoadingLayout.showDataLoadSuccess();
						mFavoritePicturesListAdapter.setList(imageInfoList);
					} else if (mCurLoadPage <= totalPage) {
						mFavoritePicturesListAdapter.addList(imageInfoList);
					}

					// 如果是最后一页，则禁用自动加载更多
					if (mCurLoadPage >= totalPage) {
						mXListView.setPullLoadEnable(false);
					}
				} else {
					if (mCurLoadPage == DEFAULT_START_PAGE) {
						mDataLoadingLayout.showDataEmptyView();
					} else {
						mXListView.setPullLoadEnable(false);
					}
				}

			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestQueryFavoritePicturesData onFailure NetroidError = " + netroidError);

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
				LogUtil.d("httpRequestQueryFavoritePicturesData onFinish");

				mXListView.onLoadMoreComplete();
			}

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestQueryFavoritePicturesData onStart");

				if (DEFAULT_START_PAGE == mCurLoadPage) {
					mDataLoadingLayout.showDataLoading();
				}
			}

		});
	}

}
