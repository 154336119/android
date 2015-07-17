package com.huizhuang.zxsq.ui.activity.account;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.module.Discuss;
import com.huizhuang.zxsq.module.DiscussGroup;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.UserGroup;
import com.huizhuang.zxsq.module.Visitor;
import com.huizhuang.zxsq.module.parser.DiscussGroupParser;
import com.huizhuang.zxsq.module.parser.UserGroupParser;
import com.huizhuang.zxsq.ui.activity.PersonalHomepageActivity;
import com.huizhuang.zxsq.ui.adapter.AttentionAdapter;
import com.huizhuang.zxsq.ui.adapter.DiscussListViewAdapter;
import com.huizhuang.zxsq.ui.activity.base.BaseListActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.exception.*;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;

/**
 * @ClassName: AccountAttentionActivity
 * @Package com.huizhuang.zxsq.ui.activity.account
 * @Description: 我关注
 * @author 
 * @mail 
 * @date 
 */
public class AccountAttentionActivity extends BaseListActivity implements
	OnClickListener,IXListViewListener {

	private DataLoadingLayout mDataLoadingLayout;
	private AttentionAdapter mAdapter;
	private XListView mXListView;
	private CommonActionBar mCommonActionBar;
	private View mHeaderView;
	private int mCurrentPageIndex = 1;
	private UserGroup mGroup;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_attention);
		initActionBar();
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.favorite_pictures_data_loading_layout);
		mAdapter = new AttentionAdapter(AccountAttentionActivity.this);
		mXListView = (XListView) getListView();
		mXListView.setPullRefreshEnable(true);//显示刷新
		mXListView.setPullLoadEnable(false);//显示加载更多
		mXListView.setAutoRefreshEnable(true);//开始自动加载
		mXListView.setAutoLoadMoreEnable(true);//滚动到底部自动加载更多
		mXListView.setXListViewListener(this);
		//mXListView.addHeaderView(mHeaderView);
		mXListView.setFocusable(true);
		mXListView.setClickable(true);
		mXListView.setAdapter(mAdapter);
		mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User user=(User) parent.getAdapter().getItem(position);
				Visitor visitor=new Visitor();
				visitor.setId(user.getId());
				visitor.setName(user.getNickname());
				visitor.setAvatar(user.getAvatar());
				Bundle bd = new Bundle();
				bd.putSerializable(AppConstants.PARAM_VISOTOR, visitor);
				ActivityUtil.next(AccountAttentionActivity.this, PersonalHomepageActivity.class, bd, -1);
				
			}
		});
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_attention);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				}
		});
	}

	


	@Override
	public void onClick(View v) {
	}
	
	/**
	 * 好友列表
	 * @param loadType
	 */
	public void httpRequestLoadData(final int loadType){
		RequestParams params = new RequestParams();
		params.put("user_id",ZxsqApplication.getInstance().getUser().getId());
		params.put("page", mCurrentPageIndex);
//		params.put("pageSize", AppConfig.pageSize);
		HttpClientApi.get(HttpClientApi.REQ_GET_FOLLOWING, params, new UserGroupParser(), new RequestCallBack<UserGroup>() {

			@Override
			public void onSuccess(UserGroup group) {
				mDataLoadingLayout.showDataLoadSuccess();
				mGroup=group;
				if (group.size() == 0) {
					mDataLoadingLayout.showDataEmptyView();
				}
				if (mAdapter!=null) {
				if (loadType == 0) {
					mAdapter.setList(group);
				}else{
					mAdapter.addList(group);
				}
				if (group.getTotalSize() == mAdapter.getCount()) {
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
					mCurrentPageIndex--;
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
			
			@Override
			public void onFinish() {
				super.onFinish();
				if (loadType == 0) {
					mXListView.onRefreshComplete();	//下拉刷新完成
				}else{
					mXListView.onLoadMoreComplete(); //加载更多完成
				}
			}
			
		});
	}

	@Override
	public void onRefresh() {
		mCurrentPageIndex = 1;
		httpRequestLoadData(0);
	}

	@Override
	public void onLoadMore() {
		mCurrentPageIndex++;
		httpRequestLoadData(1);
	}

}
