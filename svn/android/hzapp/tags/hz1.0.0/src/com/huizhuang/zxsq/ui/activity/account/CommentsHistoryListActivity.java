package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.CommentHistory;
import com.huizhuang.zxsq.http.task.account.GetCommentHistoryListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.account.CommentHistoryListAdapter;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * @ClassName: CommentsHistoryListActivity
 * @Description: 已点评列表
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-12 上午10:42:04
 * 
 */
public class CommentsHistoryListActivity extends BaseActivity {

	private DataLoadingLayout mDataLoadingLayout;
	private XListView mXListView;
	private CommentHistoryListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_comments_history_list);
		initActionBar();
		initView();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.txt_commtent_history);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						btnBackOnClick(v);
					}
				});
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		LogUtil.d("initView");

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.common_dl);

		mXListView = (XListView) findViewById(R.id.my_list_view);
		mXListView.setPullRefreshEnable(false);
		mXListView.setPullLoadEnable(false);
		mXListView.setAutoRefreshEnable(true);
		mXListView.setAutoLoadMoreEnable(false);
		mAdapter = new CommentHistoryListAdapter(this);
		mXListView.setAdapter(mAdapter);
		mXListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				httpRequestQueryData(XListRefreshType.ON_PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
			}
		});
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

	private void httpRequestQueryData(final XListRefreshType xListRefreshType) {
		GetCommentHistoryListTask task = new GetCommentHistoryListTask(CommentsHistoryListActivity.this);
        task.setCallBack(new AbstractHttpResponseHandler<List<CommentHistory>>() {
			
			@Override
			public void onSuccess(List<CommentHistory> result) {
				mDataLoadingLayout.showDataLoadSuccess();
				mAdapter.setList(result);
				if(result == null){
					mDataLoadingLayout.showDataEmptyView();
				}else{
					if(result.size() == 0){
						mDataLoadingLayout.showDataEmptyView();
					}
				}
			}
			
			@Override
			public void onFailure(int code, String error) {
				mDataLoadingLayout.showDataLoadFailed(error);
			}

			@Override
			public void onStart() {
				super.onStart();
				mDataLoadingLayout.showDataLoading();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				mXListView.onRefreshComplete();
			}
			
			
		});
        task.send();
	}

}
