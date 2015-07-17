package com.huizhuang.zxsq.ui.activity.foreman;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.foreman.ForemanComment;
import com.huizhuang.zxsq.http.task.foreman.ForemanCommentTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.foreman.ForemanCommentAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class ForemanCommentActivity extends BaseActivity {

    private CommonActionBar mCommonActionBar;
    private XListView mXListView;
    private DataLoadingLayout mDataLoadingLayout;
    
    private ForemanCommentAdapter mAdapter;
    private String mForeman_id;
    private String mMinId;
    private int mCount = AppConfig.pageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreman_comment);
        getIntentExtra();
        initActionBar();
        initViews();
    }

    private void getIntentExtra() {
        mForeman_id = getIntent().getStringExtra(AppConstants.PARAM_ORDER_COMPANY_ID);
    }

    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("口碑评价");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_load_layout);
        mDataLoadingLayout.setOnReloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	mMinId=null;
                httpRequestGetComments(XListRefreshType.ON_PULL_REFRESH);
            }
        });
        mXListView = (XListView) findViewById(R.id.xlist);
        mXListView.setPullRefreshEnable(true);
        mXListView.setPullLoadEnable(true);
        mXListView.setAutoLoadMoreEnable(false);
        mXListView.setAutoRefreshEnable(true);
        mXListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
            	mMinId = null;
                httpRequestGetComments(XListRefreshType.ON_PULL_REFRESH);
            }

            @Override
            public void onLoadMore() {
            	if(mAdapter.getCount()>0){
            		mMinId = mAdapter.getList().get(mAdapter.getCount()-1).getId()+"";
            	}
                httpRequestGetComments(XListRefreshType.ON_LOAD_MORE);
            }
        });
        mAdapter = new ForemanCommentAdapter(this);
        mXListView.setAdapter(mAdapter);
        mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ForemanComment foremanComment = (ForemanComment)parent.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
            	bundle.putInt("comment_id", foremanComment.getId());
            	bundle.putString("operator_id", foremanComment.getOperator_id());
            	bundle.putString("store_id", mForeman_id);
                ActivityUtil.next(ForemanCommentActivity.this, ForemanCommentDetailActivity.class, bundle, false);
			}
		});
    }

    private void httpRequestGetComments(final XListRefreshType xListRefreshType) {
        ForemanCommentTask task = new ForemanCommentTask(this, mForeman_id,mMinId,mCount);
        task.setCallBack(new AbstractHttpResponseHandler<List<ForemanComment>>() {

            @Override
            public void onSuccess(List<ForemanComment> result) {
                mDataLoadingLayout.showDataLoadSuccess();

                if (null != result&&result.size()>0) {
                    if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        mAdapter.setList(result);
                    } else {
                        mAdapter.addList(result);
                    }
                    if(result.size()<mCount){
                    	mXListView.setPullLoadEnable(false);
                    }else{
                    	mXListView.setPullLoadEnable(true);
                    }
                }else if(XListRefreshType.ON_PULL_REFRESH == xListRefreshType
                		&& mAdapter.getCount() == 0){
                			mDataLoadingLayout.showDataLoadEmpty("目前还没有任何评论！");
                		}
            }

            @Override
            public void onFailure(int code, String error) {
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType
                        && 0 == mAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoadFailed(error);
                } else {
                    showToastMsg(error);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                // 没有数据时下拉列表刷新才显示加载等待视图
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType
                        && 0 == mAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoading();
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

        });
        task.send();
    }

}
