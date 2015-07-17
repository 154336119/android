package com.huizhuang.zxsq.ui.activity.foreman;

import java.util.List;

import android.os.Bundle;
import android.view.View;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.foreman.ForemanComment;
import com.huizhuang.zxsq.http.task.foreman.ForemanCommentDetailTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.foreman.ForemanCommentDetailAdapter;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class ForemanCommentDetailActivity extends BaseActivity {
	private String mCommentId;
	private String mStoreId;
	private String mOperatorId;
	private DataLoadingLayout mDataLoadingLayout;
	private XListView mXListView;
	private ForemanCommentDetailAdapter mForemanCommentDetailAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foreman_comment_detail);
		getIntentExtra();
		initViews();
	}

	private void getIntentExtra() {
		mCommentId = String.valueOf(getIntent().getExtras().getInt("comment_id"));
		mStoreId = getIntent().getExtras().getString("store_id");
		mOperatorId = getIntent().getExtras().getString("operator_id");
	}

    private void initViews() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_load_layout);
        mDataLoadingLayout.setOnReloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequestGetCommentDetail();
            }
        });
        CommonActionBar commonActionBar = (CommonActionBar)findViewById(R.id.common_action_bar);
        commonActionBar.setActionBarTitle("评价详情");
        commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mXListView = (XListView) findViewById(R.id.xlist);
        mXListView.setPullRefreshEnable(true);
        mXListView.setPullLoadEnable(false);
        mXListView.setAutoLoadMoreEnable(false);
        mXListView.setAutoRefreshEnable(true);
        mXListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                httpRequestGetCommentDetail();
            }

			@Override
			public void onLoadMore() {
				
			}
        });
        mForemanCommentDetailAdapter = new ForemanCommentDetailAdapter(this);
        mXListView.setAdapter(mForemanCommentDetailAdapter);
    }

	protected void httpRequestGetCommentDetail() {
		ForemanCommentDetailTask foremanCommentDetailTask = new ForemanCommentDetailTask(this, mStoreId,mOperatorId,mCommentId);
		foremanCommentDetailTask.setCallBack(new AbstractHttpResponseHandler<List<ForemanComment>>() {
			
			@Override
			public void onSuccess(List<ForemanComment> result) {
				mDataLoadingLayout.showDataLoadSuccess();
				if(result!=null && result.size()>0){
					mForemanCommentDetailAdapter.setList(result);
				}else if(mForemanCommentDetailAdapter.getCount()==0){
					mDataLoadingLayout.showDataLoadEmpty("抱歉，还没有相关评论！");
				}
			}
			
			@Override
			public void onFailure(int code, String error) {
				if(mForemanCommentDetailAdapter.getCount()>0){
					showToastMsg(error);
				}else{
					mDataLoadingLayout.showDataLoadFailed(error);
				}
			}
			@Override
			public void onStart() {
				super.onStart();
				if(mForemanCommentDetailAdapter.getCount()==0){
					mDataLoadingLayout.showDataLoading();
				}
			}
			@Override
			public void onFinish() {
				super.onFinish();
				mXListView.onRefreshComplete();
			}
		});
		foremanCommentDetailTask.send();
	}
}