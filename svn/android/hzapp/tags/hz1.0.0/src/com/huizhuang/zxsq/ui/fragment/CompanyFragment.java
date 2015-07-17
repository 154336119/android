package com.huizhuang.zxsq.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.module.CompanyGroup;
import com.huizhuang.zxsq.module.parser.CompanyGroupParser;
import com.huizhuang.zxsq.ui.activity.SearchActivity;
import com.huizhuang.zxsq.ui.activity.company.CompanyCityList;
import com.huizhuang.zxsq.ui.activity.company.CompanyDetailActivity;
import com.huizhuang.zxsq.ui.adapter.CompanyListViewAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseListFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.CommonTopSearchBar;
import com.huizhuang.zxsq.widget.CommonTopSearchBar.OnKeyboradListener;
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
 * @ClassName: CompanyFragment
 * @Package com.huizhuang.zxsq.ui.activity.fragment
 * @Description: 公司查找
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-8-21 下午6:01:18
 */
public class CompanyFragment extends BaseListFragment implements OnClickListener, IXListViewListener {
	private static final int REQ_CITY_CODE = 666;
	private CommonActionBar mCommonActionBar;
	private DataLoadingLayout mDataLoadingLayout;

	private XListView mXListView;
	private CompanyListViewAdapter mAdapter;
	private int mScrollIndex;
	private int mPageIndex = 1;
	private RelativeLayout mConditionActionBar; // 顶部搜索状态bar
	private LinearLayout mBtnBar; // 底部搜索bar
	private ImageView mBtnGoTop;// 返回顶部
	private ImageView mBtnSearch;// 筛选
	private ImageView mBtnCleanCondition; // 顶部搜索状态bar-清除按钮
	private TextView mTvCondition;

	private String city;
	private String keyword;// 关键字
	private KeyValue mOptionType;
	private KeyValue mOptionWay;
	private KeyValue mOptionsOrder;
	private KeyValue mOptionsRanges;

	public static CompanyFragment newInstance() {
		return new CompanyFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_company, container, false);
		initActionBar(view);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		mDataLoadingLayout = (DataLoadingLayout) view.findViewById(R.id.favorite_pictures_data_loading_layout);

		// 底部筛选条件bar
		mBtnBar = (LinearLayout) view.findViewById(R.id.ll_btn_bar);
		mBtnGoTop = (ImageView) view.findViewById(R.id.btn_go_top);
		mBtnSearch = (ImageView) view.findViewById(R.id.btn_search);
		mBtnGoTop.setOnClickListener(this);
		mBtnSearch.setOnClickListener(this);
		// 顶部搜索状态bar
		mConditionActionBar = (RelativeLayout) view.findViewById(R.id.condition_action_bar);
		mConditionActionBar.setVisibility(View.GONE);

		mBtnCleanCondition = (ImageView) view.findViewById(R.id.btn_clean_condition);
		mBtnCleanCondition.setOnClickListener(this);
		mTvCondition = (TextView) view.findViewById(R.id.tv_search_condition);
		mTvCondition.setOnClickListener(this);
	}

	private void initActionBar(View view) {
		// TODO Auto-generated method stub
		mCommonActionBar = (CommonActionBar) view.findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle("装修公司");
		mCommonActionBar.setLeftImgBtn(R.drawable.company_locationg_icon, new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CompanyCityList.class);
				Bundle bd = new Bundle();
				bd.putString("myCity", ZxsqApplication.getInstance().getLocationCity());
				intent.putExtras(bd);
				startActivityForResult(intent, REQ_CITY_CODE);
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final CommonTopSearchBar commonTopSearchBar = new CommonTopSearchBar(getActivity());
		commonTopSearchBar.setOnKeyboradListener(new OnKeyboradListener() {
			@Override
			public void onSearchKeyClick() {
				clearCondition();
				keyword = commonTopSearchBar.getInputKeyWord();
				updateCondition();
				updateListView();
			}
		});
		commonTopSearchBar.setBtnCancelOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				commonTopSearchBar.setInputKeyWordEmpty();
				clearCondition();
				updateCondition();
				updateListView();
			}
		});
		mAdapter = new CompanyListViewAdapter(getActivity(), getActivity());
		View view = new View(getActivity());
		view.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, UiUtil.dp2px(getActivity(), 32)));
		mXListView = (XListView) getListView();
		mXListView.addHeaderView(commonTopSearchBar);
		mXListView.setPullRefreshEnable(true);// 显示刷新
		mXListView.setPullLoadEnable(false);// 显示加载更多
		mXListView.setAutoRefreshEnable(true);// 开始自动加载
		mXListView.setAutoLoadMoreEnable(true);// 滚动到底部自动加载更多
		mXListView.setXListViewListener(this);

		PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true);
		mXListView.setOnScrollListener(listener);
		mXListView.setAdapter(mAdapter);

		mXListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > 1) {
					if (mBtnBar.getVisibility() != View.VISIBLE) {
						mBtnBar.setVisibility(View.VISIBLE);
					}
					if (mTvCondition.getText().toString().trim().length() > 0) {
						mConditionActionBar.setVisibility(View.VISIBLE);
					}
				} else {
					if (mBtnBar.getVisibility() != View.INVISIBLE && totalItemCount > 3) {
						mBtnBar.setVisibility(View.INVISIBLE);
					}
					mConditionActionBar.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (position >= mXListView.getHeaderViewsCount()) {
			Company company = mAdapter.getItem(position - mXListView.getHeaderViewsCount());
			Atlas mAtlas = company.getCoverAtlas();
			Bundle bd = new Bundle();
			bd.putSerializable(AppConstants.PARAM_ATLAS, mAtlas);
			bd.putSerializable(AppConstants.PARAM_COMPANY, company);
			AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_ZX_COMPANY);
			ActivityUtil.next(getActivity(), CompanyDetailActivity.class, bd, -1);
		}
	}

	/**
	 * 刷新list
	 */
	private void updateListView() {
		mXListView.onRefresh();
	}

	private void clearCondition() {
		mOptionType = null;
		mOptionWay = null;
		mOptionsOrder = null;
		mOptionsRanges = null;
		keyword = null;
	}

	/**
	 * 更新顶部搜索状态bar
	 */
	private void updateCondition() {
		StringBuffer sb = new StringBuffer();
		if (mOptionType != null) {
			sb.append(mOptionType.getName() + "+");
		}
		if (mOptionWay != null) {
			sb.append(mOptionWay.getName() + "+");
		}
		if (mOptionsOrder != null) {
			sb.append(mOptionsOrder.getName() + "+");
		}
		if (mOptionsRanges != null) {
			sb.append(mOptionsRanges.getName() + "+");
		}
		if (!TextUtils.isEmpty(keyword)) {
			sb.append(keyword + "+");
		}
		if (sb.toString().length() > 0) {
			mBtnCleanCondition.setVisibility(View.VISIBLE);
			mTvCondition.setText(sb.toString().substring(0, sb.toString().length() - 1));
		} else {
			mBtnCleanCondition.setVisibility(View.INVISIBLE);
			mTvCondition.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_go_top:
			mXListView.setSelection(0);
			// setSelection是瞬间滚动到顶部
			// mXListView.requestFocusFromTouch();
			// mXListView.setSelection(0);
			mBtnBar.setVisibility(View.INVISIBLE);
			break;
		case R.id.btn_search:
			goSearchPage();
			break;
		case R.id.tv_search_condition:
			goSearchPage();
			break;
		case R.id.btn_clean_condition:
			clearCondition();
			updateCondition();
			updateListView();
			break;
		default:
			break;
		}
	}

	/**
	 * 前往搜索页面
	 */
	private void goSearchPage() {
		AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_ATALS_SIFT);
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		Bundle bd = new Bundle();
		bd.putInt("search_module", 3);
		bd.putString("condition_keyword", keyword);
		bd.putSerializable("condition_1", mOptionType);
		bd.putSerializable("condition_2", mOptionWay);
		bd.putSerializable("condition_3", mOptionsOrder);
		bd.putSerializable("condition_4", mOptionsRanges);
		intent.putExtras(bd);
		startActivityForResult(intent, 600);
	}

	/**
	 * 请求公司列表
	 * 
	 * @param xListRefreshType
	 *            请求类型1.XListRefreshType.ON_PULL_REFRESH为刷新，
	 *            2.XListRefreshType.ON_LOAD_MORE为加载更多
	 */
	private void httpRequestQueryCompanyList(final XListRefreshType xListRefreshType) {
		RequestParams params = new RequestParams();
		if (city != null) {
			params.put("service_city", city);
		}
		if (mOptionType != null) {
			params.put("decoration_type", mOptionType.getId());
		}
		if (mOptionWay != null) {
			params.put("renovation_way", mOptionWay.getId());
		}
		if (mOptionsOrder != null) {
			params.put("rank_type", mOptionsOrder.getId());
			if ("location".equals(mOptionsOrder.getId()) && ZxsqApplication.getInstance().getUserPoint() != null) { // 离我最近
				params.put("lon", String.valueOf(ZxsqApplication.getInstance().getUserPoint().longitude));
				params.put("lat", String.valueOf(ZxsqApplication.getInstance().getUserPoint().latitude));
			}
		}
		if (mOptionsRanges != null) {
			params.put("cost_range", mOptionsRanges.getId());
		}
		if (!TextUtils.isEmpty(keyword)) {
			params.put("keyword", keyword);
		}
		params.put("page", mPageIndex);
		HttpClientApi.get(HttpClientApi.REQ_COMPANY_LSIT, params, new CompanyGroupParser(), new RequestCallBack<CompanyGroup>() {

			@Override
			public void onSuccess(CompanyGroup group) {
				mDataLoadingLayout.showDataLoadSuccess();
				// 是刷新还是加载更多
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					mAdapter.setList(group);
					if (0 == group.size()) {
						mDataLoadingLayout.showDataEmptyView();
					}
				} else {
					mAdapter.addList(group);
				}
				LogUtil.d("mAdapter.list:" + mAdapter.getCount());
				// 是否可以加载更多
				if (group.getTotalSize() == mAdapter.getCount()) {
					mXListView.setPullLoadEnable(false);
				} else {
					mXListView.setPullLoadEnable(true);
				}
				// 是否显示底部搜索框
				if (0 == group.size()) {
					showSearchBar();
				} else {
					hideSearchBar();
				}

			}

			@Override
			public void onFailure(NetroidError error) {
				// 是刷新还是加载更多
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					if (0 == mAdapter.getCount()) { // 是否有数据
						mDataLoadingLayout.showDataLoadFailed(error.getMessage());
					} else {
						showToastMsg(error.getMessage());
					}
				} else {
					mPageIndex--;
					showToastMsg(error.getMessage());
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					mXListView.onRefreshComplete();
				} else {
					mXListView.onLoadMoreComplete();
				}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				// 上拉刷新没有的数据的时候加载等待布局
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && mAdapter.getCount() == 0) {
					mDataLoadingLayout.showDataLoading();
				}
			}
		});
	}

	private void showSearchBar() {
		mBtnBar.setVisibility(View.VISIBLE);
		mBtnSearch.setVisibility(View.VISIBLE);
		mBtnGoTop.setVisibility(View.INVISIBLE);
	}

	private void hideSearchBar() {
		mBtnBar.setVisibility(View.INVISIBLE);
		mBtnSearch.setVisibility(View.VISIBLE);
		mBtnGoTop.setVisibility(View.VISIBLE);
	}

	@Override
	public void onRefresh() {
		mPageIndex = AppConfig.DEFAULT_START_PAGE;
		httpRequestQueryCompanyList(XListRefreshType.ON_PULL_REFRESH);
	}

	@Override
	public void onLoadMore() {
		mPageIndex++;
		httpRequestQueryCompanyList(XListRefreshType.ON_LOAD_MORE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 600) {
			if (resultCode == Activity.RESULT_OK) {
				keyword = data.getStringExtra("condition_keyword");
				mOptionType = (KeyValue) data.getSerializableExtra("condition_1");
				mOptionWay = (KeyValue) data.getSerializableExtra("condition_2");
				mOptionsRanges = (KeyValue) data.getSerializableExtra("condition_3");
				mOptionsOrder = (KeyValue) data.getSerializableExtra("condition_4");
				updateListView();
				updateCondition();
			}
		} else if (requestCode == REQ_CITY_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				city = data.getStringExtra("city");
				updateListView();
			}
		}
	}

	public void updateComditionData(Bundle bd) {
		mOptionType = (KeyValue) bd.getSerializable("condition_1");
		mOptionWay = (KeyValue) bd.getSerializable("condition_2");
		mOptionsRanges = (KeyValue) bd.getSerializable("condition_3");
		mOptionsOrder = (KeyValue) bd.getSerializable("condition_4");
		city = bd.getString("city");
		keyword = bd.getString("keyword");
		updateListView();
		updateCondition();
	}

}
