package com.huizhuang.zxsq.ui.activity.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.ActivistGroup;
import com.huizhuang.zxsq.module.parser.ActivistGroupParser;
import com.huizhuang.zxsq.ui.activity.MainActivity;
import com.huizhuang.zxsq.ui.adapter.AccountActivistListAdapter;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * 我的维权列表页
 * 
 * @ClassName: AccountActivistListActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-24 下午3:07:08
 */
public class AccountActivistListActivity extends BaseActivity {

	private DataLoadingLayout mDataLoadingLayout;
	private XListView mXListView;
	private AccountActivistListAdapter mAccountActivistListAdapter;
	private ImageView mBtnGoTop;

	private int mOrderId;
	private int mPageIndex = AppConfig.DEFAULT_START_PAGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.account_activist_list);

		getIntentExtras();
		initActionBar();
		initView();
	}

	@SuppressLint("HandlerLeak")
	private Handler mClickHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			int position = msg.arg1;
			if (what == 0) {
				httpRequestStartActivistState(position);
			}
		}

	};

	/**
	 * 获取Intent传递的参数
	 */
	private void getIntentExtras() {
		LogUtil.d("getIntentExtras");

		mOrderId = getIntent().getExtras().getInt(AppConstants.PARAM_ORDER_ID);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.txt_my_activist);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnBackOnClick(v);
			}
		});
		commonActionBar.setRightImgBtn(R.drawable.account_wsm_home, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnHomeOnClick(v);
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		LogUtil.d("initView");

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.dll_wait);

		mBtnGoTop = (ImageView) findViewById(R.id.btn_go_top);
		mBtnGoTop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnGoTopOnClick(v);
			}
		});

		mXListView = (XListView) findViewById(R.id.activist_list_view);
		mXListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true)); // 滚动不加载\
		mXListView.setPullRefreshEnable(true);
		mXListView.setPullLoadEnable(false);
		mXListView.setAutoRefreshEnable(true);
		mXListView.setAutoLoadMoreEnable(true);
		mXListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				mPageIndex = AppConfig.DEFAULT_START_PAGE;
				httpRequestQueryActivistListByOrderId(XListRefreshType.ON_PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				mPageIndex++;
				httpRequestQueryActivistListByOrderId(XListRefreshType.ON_LOAD_MORE);
			}
		});
		mXListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > 1) {
					if (mBtnGoTop.getVisibility() != View.VISIBLE) {
						mBtnGoTop.setVisibility(View.VISIBLE);
					}
				} else {
					if (mBtnGoTop.getVisibility() != View.INVISIBLE) {
						mBtnGoTop.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
		mAccountActivistListAdapter = new AccountActivistListAdapter(this, mClickHandler);
		mXListView.setAdapter(mAccountActivistListAdapter);
	}

	/**
	 * 按钮事件 - 返回
	 * 
	 * @param v
	 */
	private void btnBackOnClick(View v) {
		LogUtil.d("btnBackOnClick View = " + v);

		finish();
	}

	/**
	 * 按钮事件 - 首页
	 * 
	 * @param v
	 */
	private void btnHomeOnClick(View v) {
		LogUtil.d("btnHomeOnClick View = " + v);

		ActivityUtil.nextActivityWithClearTop(this, MainActivity.class);
	}

	/**
	 * 按钮事件 - 返回列表顶部
	 * 
	 * @param v
	 */
	protected void btnGoTopOnClick(View v) {
		LogUtil.d("btnGoTopOnClick View = " + v);

		mXListView.smoothScrollToPosition(0);
		mBtnGoTop.setVisibility(View.INVISIBLE);
	}

	/**
	 * HTTP请求 - 获取我的维权信息列表
	 * 
	 * @param xListRefreshType
	 * @param curLoadPage
	 */
	private void httpRequestQueryActivistListByOrderId(final XListRefreshType xListRefreshType) {
		LogUtil.d("httpRequestQueryActivistListByOrderId mPageIndex = " + mPageIndex);

		RequestParams params = new RequestParams();
		params.put("order_id", mOrderId);
		
		HttpClientApi.get(HttpClientApi.REQ_ACCOUNT_SUPERVISION_CURRENT_NODE_PROBLEMS, params, new ActivistGroupParser(), new RequestCallBack<ActivistGroup>() {

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestQueryActivistListByOrderId onStart");

				// 没有数据时下拉列表刷新才显示加载等待视图
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mAccountActivistListAdapter.getCount()) {
					mDataLoadingLayout.showDataLoading();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestQueryActivistListByOrderId onFinish");

				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					mXListView.onRefreshComplete();
				} else {
					mXListView.onLoadMoreComplete();
				}

			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestQueryActivistListByOrderId onFailure NetroidError = " + netroidError);

				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					if (0 == mAccountActivistListAdapter.getCount()) {
						mDataLoadingLayout.showDataLoadFailed(netroidError.getMessage());
					} else {
						showToastMsg(netroidError.getMessage());
					}
				} else {
					mPageIndex--;
					showToastMsg(netroidError.getMessage());
				}
			}

			@Override
			public void onSuccess(ActivistGroup activistGroup) {
				LogUtil.d("httpRequestQueryActivistListByOrderId onSuccess ActivistGroup = " + activistGroup);

				mDataLoadingLayout.showDataLoadSuccess();

				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					PrefersUtil.getSavedStringList(THIS, "");
					mAccountActivistListAdapter.setList(activistGroup);
					if (0 == activistGroup.size()) {
						mDataLoadingLayout.showDataEmptyView();
					}
				} else {
					mAccountActivistListAdapter.addList(activistGroup);
				}

				// 是否可以加载更多
				// if (activistGroup.getTotalSize() ==
				// mAccountActivistListAdapter.getCount()) {
				// mXListView.setPullLoadEnable(false);
				// } else {
				// mXListView.setPullLoadEnable(true);
				// }
			}

		});
	}

	/**
	 * HTTP请求 - 发起维权
	 * 
	 * @param position
	 */
	private void httpRequestStartActivistState(final int position) {
		LogUtil.d("httpRequestStartActivistState position = " + position);
		
		RequestParams requestParams = new RequestParams();
		requestParams.put("problem_id", mAccountActivistListAdapter.getItem(position).getId());
		HttpClientApi.get(HttpClientApi.REQ_ACCOUNT_SUPERVISION_PROBLEM_WQ, requestParams, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestStartActivistState onStart");

				showWaitDialog(getString(R.string.txt_start_activist));
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestStartActivistState onFinish");

				hideWaitDialog();
			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestStartActivistState onFailure NetroidError = " + netroidError);

				showToastMsg(netroidError.getMessage());
			}

			@Override
			public void onSuccess(String resaon) {
				LogUtil.d("httpRequestStartActivistState onSuccess resaon = " + resaon);

				showToastMsg(getResources().getString(R.string.txt_activist_success));
				mAccountActivistListAdapter.updateView(mXListView, position);
			}

		});
	}
}
