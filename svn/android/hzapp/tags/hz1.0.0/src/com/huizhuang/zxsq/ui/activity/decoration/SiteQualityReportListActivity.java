package com.huizhuang.zxsq.ui.activity.decoration;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.decoration.ConstructionSiteListAdapter;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;


/**
 * 
* @ClassName: SiteQualityReportListActivity 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Jean 工地质量报告列表
* @mail 154336119@qq.com   
* @date 2015-1-23 上午11:10:55 
*
 */
public class SiteQualityReportListActivity extends BaseActivity {
	
	private DataLoadingLayout mDataLoadingLayout;
	private XListView mXListView;
	private ConstructionSiteListAdapter mAdapter;
	
	private LinearLayout mBtnFunctionBar;
	private ImageView mBtnGoTop;
	private ImageView mBtnSearch;
	
//	private int mPageIndex = AppConfig.DEFAULT_START_PAGE;
	private String mMinId=null;
	
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_quality_report_list);
        initVews();
        initXlistView();
    }  

    private void initVews() {

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.diary_data_loading_layout);
		// 搜索及返回顶部条
		mBtnFunctionBar = (LinearLayout) findViewById(R.id.ll_btn_bar);
		mBtnSearch = (ImageView) findViewById(R.id.btn_search);
		mBtnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnSearchOnClick();
			}
		});
		mBtnGoTop = (ImageView) findViewById(R.id.btn_go_top);
		mBtnGoTop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnGoTopOnClick();
			}
		});
    }
    
    private void initXlistView(){
    	mXListView = (XListView) findViewById(R.id.decoration_design_list_view);
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
				} else {
					if (mBtnFunctionBar.getVisibility() != View.INVISIBLE && totalItemCount > 3) {
						mBtnFunctionBar.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
		mXListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}
		});
		mXListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
//				mPageIndex = AppConfig.DEFAULT_START_PAGE;
				mMinId = null;
//				httpRequestQueryDiaryList(XListRefreshType.ON_PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
//				mPageIndex++;
//				if(mAdapter.getList().size() > 0){
//                    mMinId = mAdapter.getList().get(mAdapter.getList().size() - 1).getId()+"";
//                }
//				httpRequestQueryDiaryList(XListRefreshType.ON_LOAD_MORE);
			}
		});
		mAdapter = new ConstructionSiteListAdapter(SiteQualityReportListActivity.this);
		mXListView.setAdapter(mAdapter);
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
	
	}

	
//    private void httpRequestGetForeman(final XListRefreshType xListRefreshType) {
//        // 1：构建task任务，并传入请求参数
//        RepresentativeWorkTask task = new RepresentativeWorkTask(mMinId);
//        // 2：设置解析类的参数(如果需要解析成一个object,可以调用不传type的方法，如果需要解析成list 则传入Task.TYPE_ARRAY)
//        task.setBeanClass(BluePrintList.class, RepresentativeWorkTask.TYPE_ARRAY);
//        // 设置回调
//        task.setCallBack(new IHttpResponseHandler<List<BluePrintList>>() {
//
//            @Override
//            public void onSuccess(int statusCode, List<BluePrintList> result) {
//                LoggerUtil.d(TAG, "onSuccess List<RepresentativeWork> = " + result);
//
//                mDataLoadingLayout.showDataLoadSuccess();
//                
//                if(0 == result.size() && XListRefreshType.ON_PULL_REFRESH == xListRefreshType){
//                    mDataLoadingLayout.showDataEmptyView();
//                }
//
//                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
//                    mAdapter.setList(result);
//                } else {
//                    mAdapter.addList(result);
//                }
//
//                if (null != result) {
//                    if (result.size() < AppConfig.DEFAULT_ONE_PAGE_COUNT) {
//                        mXListView.setPullLoadEnable(false);
//                    } else {
//                        mXListView.setPullLoadEnable(true);
//                    }
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onStart() {
//                LoggerUtil.d(TAG, "onStart");
//                if (0 == mAdapter.getCount() && XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
//                    mDataLoadingLayout.showDataLoading();
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                LoggerUtil.d(TAG, "onFinish");
//
//                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
//                    mXListView.onRefreshComplete();
//                } else {
//                    mXListView.onLoadMoreComplete();
//                }
//            }
//
//            @Override
//            public void onError(int statusCode, String error) {
//                LoggerUtil.d(TAG, "onError statusCode = " + statusCode + " error = " + error);
//                // 只有在下拉刷新且列表没有数据时才显示问题信息，其余情况都用Toast显示提示
//                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mAdapter.getCount()) {
//                    mDataLoadingLayout.showDataLoadFailed(error);
//                } else {
//                    showToastMsg(error);
//                }
//            }
//
//            @Override
//            public void onDataError(int statusCode, String error) {
//                LoggerUtil.d(TAG, "onDataError statusCode = " + statusCode + " error = " + error);
//
//                // 只在调试模式时显示数据格式错误的信息
//                if (AppConfig.DEBUG_MODE) {
//                    mDataLoadingLayout.showDataLoadFailed(error);
//                }
//            }
//        });
//        // 真正把任务增加到任务队列中的方法
//        task.doGet(this);
//    }
}
