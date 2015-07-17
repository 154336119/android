package com.huizhuang.zxsq.ui.activity.foreman;

import java.util.List;

import android.os.Bundle;
import android.view.View;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.foreman.ForemanComment;
import com.huizhuang.zxsq.http.task.foreman.ForemanCommentsTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.foreman.PublicCommentAdapter;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class PublicCommentsActivity extends BaseActivity {

    private CommonActionBar mCommonActionBar;
    private XListView mXListView;
    private DataLoadingLayout mDataLoadingLayout;
    
    private PublicCommentAdapter mAdapter;
    private String foreman_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_comments);
        getIntentExtra();
        initActionBar();
        initViews();
    }

    private void getIntentExtra() {
        foreman_id = getIntent().getStringExtra(AppConstants.PARAM_ORDER_COMPANY_ID);
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
                httpRequestGetComments(XListRefreshType.ON_PULL_REFRESH);
            }

            @Override
            public void onLoadMore() {
                httpRequestGetComments(XListRefreshType.ON_LOAD_MORE);
            }
        });
        mAdapter = new PublicCommentAdapter(this);
        mXListView.setAdapter(mAdapter);
    }

    private void httpRequestGetComments(final XListRefreshType xListRefreshType) {
        ForemanCommentsTask task = new ForemanCommentsTask(this, foreman_id);
        task.setCallBack(new AbstractHttpResponseHandler<List<ForemanComment>>() {

            @Override
            public void onSuccess(List<ForemanComment> result) {
                mDataLoadingLayout.showDataLoadSuccess();

                if (null != result) {
                    // 加载更多还是刷新
                    if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        mAdapter.setList(result);
                        if (result.size() == 0) {
                            mDataLoadingLayout.showDataEmptyView();
                        }
                    } else {
                        mAdapter.addList(result);
                    }
                    mXListView.setPullLoadEnable(false);
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
