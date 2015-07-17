package com.huizhuang.zxsq.ui.activity.account;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.module.CompanyGroup;
import com.huizhuang.zxsq.module.parser.CompanyGroupParser;
import com.huizhuang.zxsq.ui.adapter.CompanyListViewAdapter;
import com.huizhuang.zxsq.ui.activity.base.BaseListActivity;
import com.huizhuang.zxsq.ui.activity.company.CompanyDetailActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

public class FavoriteCompanyActivity extends BaseListActivity implements OnClickListener,IXListViewListener{
	private XListView mXListView;
	private LinearLayout mBtnBar;
	private CompanyListViewAdapter mAdapter;
	private DataLoadingLayout mDataLoadingLayout;
	private int mPageIndex = 1;
	private CommonActionBar mCommonActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.account_favonite_company);
		initActionBar();
		initViews();

	}
	private void initViews() {
		// TODO Auto-generated method stub	
		mDataLoadingLayout = (DataLoadingLayout)findViewById(R.id.favorite_pictures_data_loading_layout);
		mAdapter = new CompanyListViewAdapter(this,this);
		mXListView = (XListView) getListView();
		mXListView.setPullRefreshEnable(true);//显示刷新

		mXListView.setPullLoadEnable(false);//显示加载更多
		mXListView.setAutoRefreshEnable(true);//开始自动加载
		mXListView.setAutoLoadMoreEnable(true);//滚动到底部自动加载更多
		mXListView.setXListViewListener(this);
		mXListView.setAdapter(mAdapter);
		mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Company company = mAdapter.getItem(position-1);
				Atlas mAtlas = company.getCoverAtlas();
				Bundle bd = new Bundle();
				bd.putSerializable(AppConstants.PARAM_ATLAS, mAtlas);
				bd.putSerializable(AppConstants.PARAM_COMPANY, company);
				ActivityUtil.next(FavoriteCompanyActivity.this, CompanyDetailActivity.class, bd, -1);
			}
		});
	}
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.txt_title_favorite_company);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				}
		});
	}
	
	
	@Override
	public void onRefresh() {
		mPageIndex = 1;
		loadData(0);
	}

	@Override
	public void onLoadMore() {
		mPageIndex++;
		loadData(1);
	}
	private void loadData(final int loadType){
		RequestParams params = new RequestParams();
		params.put("page", mPageIndex);
		HttpClientApi.get(HttpClientApi.USER_FAVOR_STORES_LIST, params, new CompanyGroupParser(), new RequestCallBack<CompanyGroup>() {

			@Override
			public void onSuccess(CompanyGroup group) {
				mDataLoadingLayout.showDataLoadSuccess();
				CompanyGroup companys = group; 
				if (group.size() == 0) {
					mDataLoadingLayout.showDataEmptyView();
				}
				if (mAdapter != null) {
				if (loadType == 0) {
					mAdapter.setList(group);
				}else{
					mAdapter.addList(group);
				}
				if (group.getTotalSize() == mAdapter.getCount()) {
					mXListView.setPullLoadEnable(false);
				} else {
					mXListView.setPullLoadEnable(true);
				}
			    	mAdapter.notifyDataSetChanged();

				}
			}

			@Override
			public void onFailure(NetroidError error) {
				mDataLoadingLayout.showDataLoadFailed(error.getMessage());
				if (loadType == 1) {
					mPageIndex--;
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (loadType == 0) {
					mXListView.onRefreshComplete(); // 下拉刷新完成
				} else {
					mXListView.onLoadMoreComplete(); // 加载更多完成
				}

			}
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				if (loadType == 0 && mAdapter.getCount() == 0) {
					mDataLoadingLayout.showDataLoading();
				}
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}

	}


