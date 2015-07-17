package com.huizhuang.zxsq.ui.activity.foreman;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.foreman.ForemanComment;
import com.huizhuang.zxsq.http.bean.foreman.ForemanCommentList;
import com.huizhuang.zxsq.http.task.foreman.ForemanCommentsTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.foreman.PublicCommentAdapter;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

import java.util.ArrayList;
import java.util.List;

public class PublicCommentsActivity  extends BaseActivity  implements OnItemClickListener{

	private CommonActionBar mCommonActionBar;
	
	private XListView mXListView;
	private PublicCommentAdapter mAadapter;
	
	private String foreman_id = null;
	private boolean isRefresh = true;
	private int page = 1;
	private int pageAll = 0;
	private List<ForemanComment> foremanComments = new ArrayList<ForemanComment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_comments);
		getIntentExtra();
		initActionBar();
		initViews();
	}

	private void getIntentExtra() {
		foreman_id = getIntent().getStringExtra("foreman_id");
	}
	
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle("口碑评价");
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initViews() {
		mXListView = (XListView) findViewById(R.id.xlist);
		mXListView.setOnItemClickListener(this);
		mXListView.setAdapter(mAadapter = new PublicCommentAdapter(this));
		mXListView.setPullRefreshEnable(true);
		mXListView.setPullLoadEnable(false);
		mXListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				refreshData();
			}

			@Override
			public void onLoadMore() {
				loadMoreData();
			}
		});
		mXListView.setAutoRefreshEnable(true);
	}

	private void refreshData() {
		page = 1;
		isRefresh = true;
		ForemanCommentsTask task = new ForemanCommentsTask(this, foreman_id, page+"");
		task.setCallBack(new AbstractHttpResponseHandler<ForemanCommentList>() {
			
			@Override
			public void onSuccess(ForemanCommentList t) {
				isRefresh = true;
				mXListView.onRefreshComplete();
				page = 1;
				foremanComments.clear();
				mAadapter.setList(foremanComments);
				netSuccess(t);
			}
			
			@Override
			public void onFailure(int code, String error) {
				mXListView.onRefreshComplete();
				netFailure(error);
			}
		});
		task.send();
	}
	private void loadMoreData() {
		isRefresh = false;
		ForemanCommentsTask task = new ForemanCommentsTask(this, foreman_id, (page+1)+"");
		task.setCallBack(new AbstractHttpResponseHandler<ForemanCommentList>() {

			@Override
			public void onSuccess(ForemanCommentList t) {
				mXListView.onLoadMoreComplete();
				if (!isRefresh) {
					page++;
					netSuccess(t);
				}
			}

			@Override
			public void onFailure(int code, String error) {
				mXListView.onLoadMoreComplete();
				netFailure(error);
			}
		});
		task.send();
	}
	
	private void netSuccess(ForemanCommentList list) {
		pageAll = list.getTotalpage();
		if (page < pageAll) {
			mXListView.setPullLoadEnable(true);
		} else {
			mXListView.setPullLoadEnable(false);
		}
		if (list.getList() != null) {
			foremanComments.addAll(list.getList());
			mAadapter.addList(list.getList());
		}
		mAadapter.notifyDataSetChanged();
	}
	
	private void netFailure(String error) {
		showToastMsg(error);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

	}

}
