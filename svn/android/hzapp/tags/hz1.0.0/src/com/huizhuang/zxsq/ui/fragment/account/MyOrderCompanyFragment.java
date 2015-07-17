package com.huizhuang.zxsq.ui.fragment.account;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.task.account.CancelOrderTask;
import com.huizhuang.zxsq.http.task.common.GetOrderListTask;
import com.huizhuang.zxsq.ui.activity.account.AllorderListActivity.FinishReceiver;
import com.huizhuang.zxsq.ui.activity.account.OrderDetailActivity;
import com.huizhuang.zxsq.ui.adapter.account.WorksiteOrderListAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;


/** 
* @ClassName: MyOrderForemanFragment 
* @Description: 工地管理订单列表
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-5 下午8:52:11 
*  
*/
public class MyOrderCompanyFragment extends BaseFragment {

	private DataLoadingLayout mDataLoadingLayout;
	private XListView mXListView;
	private CommonAlertDialog mCommonAlertDialog;
	
	private WorksiteOrderListAdapter mAdapter;
	private String mMinId;
	private int mOrderId;
	private RefreshReceiver mRefreshReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("onCreate Bundle = " + savedInstanceState);
        View view = inflater.inflate(R.layout.activity_worksite_order_list, container, false);
		initView(view);
        return view;
    }
	
	/**
	 * 初始化控件
	 */
	private void initView(View view) {
		LogUtil.d("initView");
		CommonActionBar commonActionBar = (CommonActionBar) view.findViewById(R.id.common_action_bar);
		commonActionBar.setVisibility(View.GONE);
		
    	IntentFilter filter = new IntentFilter();
    	mRefreshReceiver = new RefreshReceiver();
        filter.addAction(BroadCastManager.ACTION_REFRESH_ORDER);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRefreshReceiver, filter);
		
		mDataLoadingLayout = (DataLoadingLayout) view.findViewById(R.id.common_dl);

		mXListView = (XListView) view.findViewById(R.id.order_list_view);
		mXListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		mXListView.setPullRefreshEnable(true);
		mXListView.setPullLoadEnable(true);
		mXListView.setAutoRefreshEnable(true);
		mXListView.setAutoLoadMoreEnable(true);
		mAdapter = new WorksiteOrderListAdapter(getActivity(),mClickHandler,true);
		mXListView.setAdapter(mAdapter);

		mXListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				mMinId = null;
				httpRequestQueryOrderData(XListRefreshType.ON_PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				int listItemCount = mAdapter.getCount();
				if (listItemCount > 0) {
					Order order = mAdapter.getItem(listItemCount - 1);
					mMinId = order.getOrder_id()+"";
				}
				httpRequestQueryOrderData(XListRefreshType.ON_LOAD_MORE);
			}
		});

		mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position > 0){
					Order order = mAdapter.getItem(position-1);
					LogUtil.d("onListItemClick position = " + position + " order = " + order.getOrder_no());
	
					Bundle bundle = new Bundle();
					bundle.putInt(AppConstants.PARAM_ORDER_ID, order.getOrder_id());
					
					ActivityUtil.next(getActivity(), OrderDetailActivity.class, bundle, -1);
					
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mClickHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			int position = msg.arg1;
			switch (what) {
			case 0://取消订单
				Order order = mAdapter.getList().get(position);
				mOrderId = order.getOrder_id();
				showCancelDialog();			
				break;
			default:
				break;
			}
		}

	};
	
	/**
	 * 工地管理订单列表
	 * @param xListRefreshType
	 * @param user_id
	 * @param order_type
	 */

    private void httpRequestQueryOrderData(final XListRefreshType xListRefreshType){
    	GetOrderListTask task = new GetOrderListTask(getActivity(),Order.ORDER_TYPE_COMPANY,mMinId);
        task.setCallBack(new AbstractHttpResponseHandler<List<Order>>() {
			
			@Override
			public void onSuccess(List<Order> result) {
				mDataLoadingLayout.showDataLoadSuccess();
				// 加载更多还是刷新
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					mAdapter.setList(result);
				} else {
					mAdapter.addList(result);
				}
				if (null != result) {
					if(result.size() == 0 && mAdapter.getCount() == 0){
						mDataLoadingLayout.showDataEmptyView();
					}
					if (result.size() < AppConfig.pageSize) {
						mXListView.setPullLoadEnable(false);
					} else {
						mXListView.setPullLoadEnable(true);
					}
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
	
    /**
     * 取消订单
     * @param orderId
     */
	private void httpRequestCancelOrder(int orderId) {
		CancelOrderTask task = new CancelOrderTask(
				getActivity(),orderId);
		task.setCallBack(new AbstractHttpResponseHandler<String>() {

			@Override
			public void onSuccess(String result) {
				showToastMsg("取消成功");
				mXListView.onRefresh();
			}

			@Override
			public void onFailure(int code, String error) {
				showToastMsg(error);
			}

			@Override
			public void onStart() {
				showWaitDialog("加载中...");
				super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
			}

		});
		task.send();
	}
	
	/**
	 * 显示取消订单提示对话框
	 */
	private void showCancelDialog() {
		LogUtil.d("showExitAlertDialog");

		if (null == mCommonAlertDialog) {
			mCommonAlertDialog = new CommonAlertDialog(getActivity());
			mCommonAlertDialog.setMessage("亲爱的小伙伴，您确定要取消本订单吗?");
			mCommonAlertDialog.setPositiveButton(R.string.ensure, new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCommonAlertDialog.dismiss();
					httpRequestCancelOrder(mOrderId);
				}
			});
			mCommonAlertDialog.setNegativeButton(R.string.give_up, new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCommonAlertDialog.dismiss();
				}
			});
		}
		mCommonAlertDialog.show();
	}
	   public class RefreshReceiver extends BroadcastReceiver{

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				//unregisterReceiver(mFinishReceiver); 
	            httpRequestQueryOrderData(XListRefreshType.ON_PULL_REFRESH);		
			}
	    	
	    }
}
