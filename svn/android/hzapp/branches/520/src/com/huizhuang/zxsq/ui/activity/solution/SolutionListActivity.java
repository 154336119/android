package com.huizhuang.zxsq.ui.activity.solution;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.solution.Solution;
import com.huizhuang.zxsq.http.task.solution.ConstructionSiteListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.SolutionCaseListAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * 施工现场列表
 * @author th
 *
 */
public class SolutionListActivity extends BaseActivity implements OnClickListener{
    
    public static final String PARAME_HOUSE_ID = "housing_id";
    private final int REQ_EDIT_CODE = 100;
    
    private DataLoadingLayout mDataLoadingLayout;
    private CommonActionBar mCommonActionBar;
    
    private XListView mXListView;
    private SolutionCaseListAdapter mAdapter;
    private String mHousingId;
    
//    private int mPageIndex = AppConfig.DEFAULT_START_PAGE;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_list);
        getIntentExtra();
        initActionBar();
        initViews();
    }
    
    private void getIntentExtra() {
        mHousingId = getIntent().getStringExtra(PARAME_HOUSE_ID);
        // foreman_id = getIntent().getStringExtra("foreman_id");
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        LogUtil.d("initActionBar");

        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    private void initViews() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPageIndex = AppConfig.DEFAULT_START_PAGE;
                httpRequestQuerySolutionCase(XListRefreshType.ON_PULL_REFRESH);
            }
        });
        mXListView = (XListView) findViewById(R.id.xlv_solution);
        mXListView.setPullRefreshEnable(true);
        mXListView.setPullLoadEnable(false);
        mXListView.setAutoRefreshEnable(true);
        mXListView.setAutoLoadMoreEnable(false);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 51)));
        mXListView.addFooterView(ll);
        mXListView.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
                listItemOnRefresh();
            }

            @Override
            public void onLoadMore() {
//                listItemOnLoadMore();
            }
        });
        mXListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        mXListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d("onItemClick position = " + position);
                if (position > 0) {
                    Bundle bd = new Bundle();
                    bd.putString(SolutionDetailActivity.PARAME_SHOW_CASE_ID_KEY, mAdapter.getList().get(position-1).getId());
                    ActivityUtil.next(SolutionListActivity.this, SolutionDetailActivity.class, bd, -1);
                }
            }
        });
        mAdapter = new SolutionCaseListAdapter(this);
        mXListView.setAdapter(mAdapter);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        default:
            break;
        }
    }
    
    /**
     * 下拉刷新列表
     */
    private void listItemOnRefresh() {
//        mPageIndex = AppConfig.DEFAULT_START_PAGE;
        httpRequestQuerySolutionCase(XListRefreshType.ON_PULL_REFRESH);
    }

//    /**
//     * 列表自动加载更多
//     */
//    private void listItemOnLoadMore() {
//        mPageIndex++;
//        httpRequestQuerySolutionCase(XListRefreshType.ON_LOAD_MORE);
//    }
//    
    /**
     * HTTP请求 - 获取施工现场列表数据
     * 
     * @param xListRefreshType
     * @param filter
     * @param keyword
     * @param max_id
     * @param min_id
     */
    private void httpRequestQuerySolutionCase(final XListRefreshType xListRefreshType) {
        LogUtil.d("httpRequestQueryClientList XListRefreshType = " + xListRefreshType);
        ConstructionSiteListTask task = new ConstructionSiteListTask(SolutionListActivity.this,mHousingId);
        task.setCallBack(new AbstractHttpResponseHandler<List<Solution>>() {

            @Override
            public void onSuccess(List<Solution> result) {
                LogUtil.d("onSuccess ConstructionSiteList = " + result);
                mDataLoadingLayout.showDataLoadSuccess();
                if (null != result && result.size() > 0) {
                    mCommonActionBar.setActionBarTitle(result.get(0).getHousing_name());
                    // 加载更多还是刷新
                    if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        mAdapter.setList(result);
                    } else {
                        mAdapter.addList(result);
                    }
                    if (result.size() < AppConfig.pageSize) {
                        mXListView.setPullLoadEnable(false);
                    } else {
                        mXListView.setPullLoadEnable(true);
                    }
                }else{
                    if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        mDataLoadingLayout.showDataEmptyView();
                    }
                }
            }
            
            @Override
            public void onFailure(int code, String error) {
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType
                        && 0 == mAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoadFailed(error);
                } else {
//                    mPageIndex--;
                    showToastMsg(error);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.d("onStart");
                if (xListRefreshType == XListRefreshType.ON_PULL_REFRESH && 0 == mAdapter.getCount()) {
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                listItemOnRefresh();
            }
        }
    }

}
