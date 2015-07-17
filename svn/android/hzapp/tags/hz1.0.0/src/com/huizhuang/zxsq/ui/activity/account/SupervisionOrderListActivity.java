package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.CommentsWait;
import com.huizhuang.zxsq.http.task.account.GetCommentsOrderListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.account.SupervisionOrderListAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;




/** 
* @ClassName: SupervisionOrderListActivity 
* @Description: 待点评列表
* @author th 
* @mail 342592622@qq.com   
* @date 2015-1-22 下午4:28:55 
*  
*/
public class SupervisionOrderListActivity extends BaseActivity {
	
	private static final int REQ_TO_GRAD_CODE = 400;
	private DataLoadingLayout mDataLoadingLayout;	
	private XListView mXListView;
	
	private SupervisionOrderListAdapter mAdapter;	
	
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worksite_supervision_node_list);
        initActionBar();
        initVews();
    }  
    
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.txt_all_order);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnBackOnClick();
			}
		});
	}

    private void initVews() {
    	mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.common_dl);
    	
    	mXListView = (XListView) findViewById(R.id.my_list_view);
		mXListView.setPullRefreshEnable(true);
		mXListView.setPullLoadEnable(false);
		mXListView.setAutoRefreshEnable(true);
		mXListView.setAutoLoadMoreEnable(false);
		mAdapter = new SupervisionOrderListAdapter(SupervisionOrderListActivity.this,mClickHandler);
		mXListView.setAdapter(mAdapter);
		mXListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				httpRequestQueryOrderData(XListRefreshType.ON_PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				httpRequestQueryOrderData(XListRefreshType.ON_LOAD_MORE);
			}
		});
		
//		mXListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				if(position > 0){
//					CommentsWait comments = (CommentsWait) parent.getAdapter().getItem(position-1);
//					LogUtil.d("onListItemClick position = " + position + " comments = " + comments);
//	
//					Bundle bundle = new Bundle();
//					bundle.putString(AppConstants.PARAM_RECORD_ID, comments.getRecord_id());
//					bundle.putString(AppConstants.PARAM_NODE_ID, comments.getNode_id());
//					ActivityUtil.next(SupervisionOrderListActivity.this, WorksiteSupervisionGradeActivity.class);
//				}
//			}
//		});
		
    }

	/**
	 * 带点评订单列表
	 * @param xListRefreshType
	 * @param user_id
	 * @param order_type
	 */

    private void httpRequestQueryOrderData(final XListRefreshType xListRefreshType){
    	GetCommentsOrderListTask task = new GetCommentsOrderListTask(SupervisionOrderListActivity.this);
        task.setCallBack(new AbstractHttpResponseHandler<List<CommentsWait>>() {
			
			@Override
			public void onSuccess(List<CommentsWait> result) {
				mDataLoadingLayout.showDataLoadSuccess();
				// 加载更多还是刷新
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					mAdapter.setList(result);
				} else {
					mAdapter.addList(result);
				}
				if (null != result) {
					if(result.size() == 0){
						mDataLoadingLayout.showDataEmptyView();
					}
//					if (result.size() < AppConfig.pageSize) {
						mXListView.setPullLoadEnable(false);
//					} else {
//						mXListView.setPullLoadEnable(true);
//					}
				} else {
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
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mAdapter.getCount()) {
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
    
	@SuppressLint("HandlerLeak")
	private Handler mClickHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			int position = msg.arg1;
			switch (what) {
			case 0://去评分
				CommentsWait comments = mAdapter.getList().get(position);
				Bundle bundle = new Bundle();
				bundle.putString(AppConstants.PARAM_RECORD_ID, comments.getRecord_id());
				bundle.putString(AppConstants.PARAM_NODE_ID, comments.getNode_id());
				ActivityUtil.next(SupervisionOrderListActivity.this, WorksiteSupervisionGradeActivity.class,bundle,REQ_TO_GRAD_CODE);
				break;
			case 1://查看报告
				
				break;
			default:
				break;
			}
		}

	};
	
	/**
	 * 按钮事件 - 返回
	 */
	private void btnBackOnClick() {
		LogUtil.d("btnBackOnClick");

		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode) {
			if (REQ_TO_GRAD_CODE == requestCode) {
				httpRequestQueryOrderData(XListRefreshType.ON_PULL_REFRESH);
			} 
		}
	}
}
