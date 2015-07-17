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

import com.baidu.mapapi.model.LatLng;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.solution.Solution;
import com.huizhuang.zxsq.http.task.solution.ConstructionSiteListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.SolutionCaseListAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
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
    public static final String PARAME_ALL_SOLUTION_IN_CURRENT_CITY = "all_solution_in_current_city";
    private final int REQ_EDIT_CODE = 100;
    
    private DataLoadingLayout mDataLoadingLayout;
    private CommonActionBar mCommonActionBar;
    
    private XListView mXListView;
    private SolutionCaseListAdapter mAdapter;
    private String mHousingId;
	private LatLng mMyLatLng;
	private String mLatitude;
	private String mLongitude;
	private boolean mAllSolution;
    
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
        mAllSolution = getIntent().getBooleanExtra(PARAME_ALL_SOLUTION_IN_CURRENT_CITY, true);
        mMyLatLng = ZxsqApplication.getInstance().getUserPoint();
        if(mMyLatLng!=null){
        	mLatitude = mMyLatLng.latitude+"";
        	mLongitude = mMyLatLng.longitude+"";
        }
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(mAllSolution){
            		AnalyticsUtil.onEvent(SolutionListActivity.this, UmengEvent.ID_CLICK_0077);
            	}else{
            		AnalyticsUtil.onEvent(SolutionListActivity.this, UmengEvent.ID_CLICK_0079);
            	}
                finish();
            }
        });
        mCommonActionBar.setActionBarTitle("附近工地");
    }
    
    private void initViews() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequestQuerySolutionCase(XListRefreshType.ON_PULL_REFRESH);
            }
        });
        findViewById(R.id.ll_submit).setOnClickListener(this);
        mXListView = (XListView) findViewById(R.id.xlv_solution);
        mXListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
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
            }
        });
        mXListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        mXListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	/**position是当前click的位置*/
            	Solution solution = (Solution)parent.getAdapter().getItem(position);
                Bundle bd = new Bundle();
                bd.putString(SolutionDetailActivity.PARAME_SHOW_CASE_ID_KEY, solution.getId());
                ActivityUtil.next(SolutionListActivity.this, SolutionDetailActivity.class, bd, -1);
            }
        });
        mAdapter = new SolutionCaseListAdapter(this);
        mXListView.setAdapter(mAdapter);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.ll_submit:
        	if(mAllSolution){
        		AnalyticsUtil.onEvent(SolutionListActivity.this, UmengEvent.ID_CLICK_0078);
        	}else{
        		AnalyticsUtil.onEvent(SolutionListActivity.this, UmengEvent.ID_CLICK_0080);
        	}
            construction();
            break;
        default:
            break;
        }
    }
    // 进入下单流程
    private void construction() {
    	Bundle bundle = new Bundle();
        bundle.putBoolean(AppConstants.PARAM_IS_FREE, true);
        bundle.putString(AppConstants.PARAM_ORDER_SOURCE_NAME, "app_showcase_list");
        ActivityUtil.checkAppointmentToJump(this, bundle);
    }
    
    /**
     * 下拉刷新列表
     */
    private void listItemOnRefresh() {
        httpRequestQuerySolutionCase(XListRefreshType.ON_PULL_REFRESH);
    }

    /**
     * HTTP请求 - 获取施工现场列表数据
     * 
     * @param xListRefreshType
     */
    private void httpRequestQuerySolutionCase(final XListRefreshType xListRefreshType) {
    	ConstructionSiteListTask task = null;
    	if(mAllSolution){
    		task = new ConstructionSiteListTask(SolutionListActivity.this,mLatitude,mLongitude);
    	}else{
    		task = new ConstructionSiteListTask(SolutionListActivity.this,mHousingId);
    	}
        task.setCallBack(new AbstractHttpResponseHandler<List<Solution>>() {

            @Override
            public void onSuccess(List<Solution> result) {
                mDataLoadingLayout.showDataLoadSuccess();
                if (null != result && result.size() > 0) {
                	if(!mAllSolution){
                		mCommonActionBar.setActionBarTitle(result.get(0).getHousing_name().trim()+"施工工地");
                	}
                    // 加载更多还是刷新
                    if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        mAdapter.setList(result);
                    } else {
                        mAdapter.addList(result);
                    }
                    
                }else{
                    if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mAdapter.getCount()) {
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
                    showToastMsg(error);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        ImageLoader.getInstance().clearMemoryCache();
    }
}
