package com.huizhuang.zxsq.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.huizhuang.zxsq.module.Author;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.DiaryGroup;
import com.huizhuang.zxsq.module.parser.DiaryGroupParser;
import com.huizhuang.zxsq.ui.activity.SearchActivity;
import com.huizhuang.zxsq.ui.activity.diary.DiaryDetailActivity;
import com.huizhuang.zxsq.ui.activity.diary.DiaryEditActivity;
import com.huizhuang.zxsq.ui.activity.image.ImageSelectActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.ui.adapter.DiaryListViewAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
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

/**
 * 日记列表页面
 * 
 * @ClassName: DiaryFragment.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-27 下午5:02:58
 */
public class DiaryFragment extends BaseFragment {

	private static final int REQ_FILTER_SEARCH_CODR = 600;
	private static final int REQ_SEARCH_CODE = 666;
	private static final int REQ_LOGIN_FOR_DIARY_CODE = 102;

	private DataLoadingLayout mDataLoadingLayout;
	private XListView mXListView;
	private DiaryListViewAdapter mDiaryListViewAdapter;

	private RelativeLayout mConditionActionBar;
	private ImageView mBtnCleanCondition;
	private TextView mTvCondition;

	private LinearLayout mBtnFunctionBar;
	private ImageView mBtnGoTop;
	private ImageView mBtnSearch;

	private String mKeyword;
	private KeyValue mStyle;
	private KeyValue mCostRange;
	private KeyValue mOrderBy;
	private KeyValue mType;
	private KeyValue zxNode;
	private int mPageIndex = AppConfig.DEFAULT_START_PAGE;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_diary, container, false);
		initActionBar(rootView);
		initVews(rootView);
		return rootView;
	}

	private void initActionBar(View v) {
		LogUtil.d("initActionBar View = " + v);

		CommonActionBar commonActionBar = (CommonActionBar) v.findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.txt_bottom_tab_diary);
		commonActionBar.setRightImgBtn(R.drawable.diary_edit, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnDiaryEditOnClick(v);
			}
		});
	}

	private void initVews(View v) {
		LogUtil.d("initVews View = " + v);

		mDataLoadingLayout = (DataLoadingLayout) v.findViewById(R.id.diary_data_loading_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mXListView.onRefresh();
            }
        });

		// 搜索及返回顶部条
		mBtnFunctionBar = (LinearLayout) v.findViewById(R.id.ll_btn_bar);
		mBtnSearch = (ImageView) v.findViewById(R.id.btn_search);
		mBtnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnSearchOnClick();
			}
		});
		mBtnGoTop = (ImageView) v.findViewById(R.id.btn_go_top);
		mBtnGoTop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnGoTopOnClick();
			}
		});

		// 筛选条件框
		mConditionActionBar = (RelativeLayout) v.findViewById(R.id.condition_action_bar);
		mConditionActionBar.setVisibility(View.GONE);
		mBtnCleanCondition = (ImageView) v.findViewById(R.id.btn_clean_condition);
		mBtnCleanCondition.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnCleanConditionOnClick();
			}
		});
		mTvCondition = (TextView) v.findViewById(R.id.tv_search_condition);
		mTvCondition.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnSearchOnClick();
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtil.d("onActivityCreated Bundle = " + savedInstanceState);

		final CommonTopSearchBar commonTopSearchBar = new CommonTopSearchBar(getActivity());
		commonTopSearchBar.setOnKeyboradListener(new OnKeyboradListener() {
			@Override
			public void onSearchKeyClick() {
				clearCondition();
				mKeyword = commonTopSearchBar.getInputKeyWord();
				updateCondition();
				updateListView();
			}
		});
		commonTopSearchBar.setBtnCancelOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commonTopSearchBar.setInputKeyWordEmpty();
				btnCleanConditionOnClick();
			}
		});

		mXListView = (XListView) findViewById(R.id.diary_list_view);
		mXListView.addHeaderView(commonTopSearchBar);
		mXListView.setPullRefreshEnable(true);
		mXListView.setPullLoadEnable(true);
		mXListView.setAutoRefreshEnable(true);
		mXListView.setAutoLoadMoreEnable(true);
		mXListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > 1) {
					if (mBtnFunctionBar.getVisibility() != View.VISIBLE) {
						mBtnFunctionBar.setVisibility(View.VISIBLE);
					}
					if (mTvCondition.getText().toString().trim().length() > 0) {
						mConditionActionBar.setVisibility(View.VISIBLE);
					}
				} else {
					if (mBtnFunctionBar.getVisibility() != View.INVISIBLE && totalItemCount > 3) {
						mBtnFunctionBar.setVisibility(View.INVISIBLE);
					}
					mConditionActionBar.setVisibility(View.GONE);
				}
			}
		});
		mXListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position >= mXListView.getHeaderViewsCount()) {
					Diary diary = (Diary) parent.getAdapter().getItem(position);
					Author author = diary.getAuthor();
					Bundle bundle = new Bundle();
					bundle.putSerializable(AppConstants.PARAM_AUTHOR, author);
					bundle.putSerializable(AppConstants.PARAM_DIARY, diary);
					ActivityUtil.next(getActivity(), DiaryDetailActivity.class, bundle, -1);
				}
			}
		});
		mXListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				mPageIndex = AppConfig.DEFAULT_START_PAGE;
				httpRequestQueryDiaryList(XListRefreshType.ON_PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				mPageIndex++;
				httpRequestQueryDiaryList(XListRefreshType.ON_LOAD_MORE);
			}
		});
		mDiaryListViewAdapter = new DiaryListViewAdapter(getActivity());
		mXListView.setAdapter(mDiaryListViewAdapter);
	}

	/**
	 * 按钮事件 - 清空过滤搜索条件
	 */
	protected void btnCleanConditionOnClick() {
		LogUtil.d("btnCleanConditionOnClick");

		clearCondition();
		updateCondition();
		updateListView();
	}

	/**
	 * 按钮事件 - 返回列表顶部
	 */
	protected void btnGoTopOnClick() {
		LogUtil.d("btnGoTopOnClick");

		mXListView.smoothScrollToPosition(0);
		mBtnFunctionBar.setVisibility(View.INVISIBLE);
	}

	/**
	 * 按钮事件 - 搜索
	 */
	protected void btnSearchOnClick() {
		LogUtil.d("btnSearchOnClick");

		AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_DIARY_SIFT);
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("search_module", 2);
		bundle.putString("condition_keyword", mKeyword);
		bundle.putSerializable("condition_1", mStyle);
		bundle.putSerializable("condition_2", mType);
		bundle.putSerializable("condition_3", mCostRange);
		bundle.putSerializable("condition_4", mOrderBy);
		bundle.putSerializable("condition_5", zxNode);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQ_FILTER_SEARCH_CODR);
	}

	private void httpRequestQueryDiaryList(final XListRefreshType xListRefreshType) {
		LogUtil.d("httpRequestQueryDiaryList mPageIndex = " + mPageIndex);

		RequestParams params = new RequestParams();
		if (mStyle != null) {
			params.put("room_style", mStyle.getId());
		}
		if (mType != null) {
			params.put("room_type", mType.getId());
		}
		if (mCostRange != null) {
			params.put("cost_range", mCostRange.getId());
		}
		if (mOrderBy != null) {
			params.put("orderby", mOrderBy.getId());
			if (mOrderBy.getId().equals("location") && ZxsqApplication.getInstance().getUserPoint() != null) {
				params.put("lat", ZxsqApplication.getInstance().getUserPoint().latitude);
				params.put("lon", ZxsqApplication.getInstance().getUserPoint().longitude);
			}
		}
		if (zxNode != null) {
			params.put("zx_node", zxNode.getId());
		}
		if (!TextUtils.isEmpty(mKeyword)) {
			params.put("keyword", mKeyword);
		}
		params.put("page", mPageIndex);
		HttpClientApi.post(HttpClientApi.REQ_DIARY_LIST, params, new DiaryGroupParser(), new RequestCallBack<DiaryGroup>() {

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestQueryDiaryList onStart");

				// 没有数据时下拉列表刷新才显示加载等待视图
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mDiaryListViewAdapter.getCount()) {
					mDataLoadingLayout.showDataLoading();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestQueryDiaryList onFinish");

				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					mXListView.onRefreshComplete();
				} else {
					mXListView.onLoadMoreComplete();
				}
			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestQueryDiaryList onFailure NetroidError = " + netroidError);

				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					if (0 == mDiaryListViewAdapter.getCount()) {
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
			public void onSuccess(DiaryGroup diaryGroup) {
				LogUtil.d("httpRequestQueryDiaryList onSuccess OrderGroup = " + diaryGroup);

				mDataLoadingLayout.showDataLoadSuccess();

				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					mDiaryListViewAdapter.setList(diaryGroup);
					if (0 == diaryGroup.size()) {
						mDataLoadingLayout.showDataEmptyView();
					}
				} else {
					mDiaryListViewAdapter.addList(diaryGroup);
				}

				// 是否可以加载更多
				if (diaryGroup.getTotalSize() == mDiaryListViewAdapter.getCount()) {
					mXListView.setPullLoadEnable(false);
				} else {
					mXListView.setPullLoadEnable(true);
				}

				// 是否显示快捷搜索底部框
				if (diaryGroup.size() == 0) {
					showSearchBar();
				} else {
					hideSearchBar();
				}
			}

		});
	}

	/**
	 * 显示选择图片对话框
	 */
	private void showTakePhotoDialog() {
		String path = Util.createImagePath(getActivity());
		if (path == null) {
			return;
		}
		Intent intent = new Intent(getActivity(), ImageSelectActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("crop", false);
		bundle.putString("image-path", path);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQ_SEARCH_CODE);
	}

	private void showSearchBar() {
		mBtnFunctionBar.setVisibility(View.VISIBLE);
		mBtnSearch.setVisibility(View.VISIBLE);
		mBtnGoTop.setVisibility(View.INVISIBLE);
	}

	private void hideSearchBar() {
		mBtnFunctionBar.setVisibility(View.INVISIBLE);
		mBtnSearch.setVisibility(View.VISIBLE);
		mBtnGoTop.setVisibility(View.VISIBLE);
	}

	/**
	 * 刷新list
	 */
	private void updateListView() {
		mXListView.onRefresh();
	}

	private void clearCondition() {
		mStyle = null;
		mCostRange = null;
		mOrderBy = null;
		mType = null;
		mKeyword = null;
		zxNode = null;
	}

	/**
	 * 更新筛选条件
	 */
	private void updateCondition() {
		StringBuffer sb = new StringBuffer();
		if (mStyle != null) {
			sb.append(mStyle.getName() + "+");
		}
		if (mCostRange != null) {
			sb.append(mCostRange.getName() + "+");
		}
		if (mOrderBy != null) {
			sb.append(mOrderBy.getName() + "+");
		}
		if (mType != null) {
			sb.append(mType.getName() + "+");
		}
		if (!TextUtils.isEmpty(mKeyword)) {
			sb.append(mKeyword + "+");
		}
		if (zxNode != null) {
			sb.append(zxNode.getName() + "+");
		}
		if (sb.toString().length() > 0) {
			mBtnCleanCondition.setVisibility(View.VISIBLE);
			mTvCondition.setText(sb.toString().substring(0, sb.toString().length() - 1));
		} else {
			mBtnCleanCondition.setVisibility(View.INVISIBLE);
			mTvCondition.setText("");
		}
	}

	/**
	 * 日记记账拍照
	 * 
	 * @param v
	 */
	protected void btnDiaryEditOnClick(View v) {
		LogUtil.d("btnDiaryEditOnClick View = " + v);

		AnalyticsUtil.onEvent(getActivity(), AppConstants.UmengEvent.ID_DIARY_EDIT_FROM_LIST);
		if (ZxsqApplication.getInstance().isLogged()) {
			showTakePhotoDialog();
		} else {
			Intent intent = new Intent(getActivity(), UserLoginActivity.class);
			startActivityForResult(intent, REQ_LOGIN_FOR_DIARY_CODE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_FILTER_SEARCH_CODR) {
			if (resultCode == Activity.RESULT_OK) {
				mKeyword = data.getStringExtra("condition_keyword");
				mStyle = (KeyValue) data.getSerializableExtra("condition_1");
				mType = (KeyValue) data.getSerializableExtra("condition_2");
				mCostRange = (KeyValue) data.getSerializableExtra("condition_3");
				mOrderBy = (KeyValue) data.getSerializableExtra("condition_4");
				zxNode = (KeyValue) data.getSerializableExtra("condition_5");
				updateCondition();
				updateListView();
			}
		} else if (requestCode == REQ_SEARCH_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri imageUri = data.getParcelableExtra("image-path-uri");
				DiaryEditActivity.show(getActivity(), imageUri);
			}
		} else if (requestCode == REQ_LOGIN_FOR_DIARY_CODE && resultCode == Activity.RESULT_OK) {
			showTakePhotoDialog();
		}
	}

	public void updateComditionData(Bundle bd) {
		mKeyword = bd.getString("condition_keyword");
		mStyle = (KeyValue) bd.getSerializable("condition_1");
		mType = (KeyValue) bd.getSerializable("condition_2");
		mCostRange = (KeyValue) bd.getSerializable("condition_3");
		mOrderBy = (KeyValue) bd.getSerializable("condition_4");
		zxNode = (KeyValue) bd.getSerializable("condition_5");
		updateCondition();
		updateListView();
	}

}