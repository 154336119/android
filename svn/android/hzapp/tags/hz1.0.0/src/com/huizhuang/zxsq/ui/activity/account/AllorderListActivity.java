package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.task.common.GetOrderListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.AllOrderAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.ToastUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * @author Jean
 * @ClassName: AllorderListActivity
 * @Description: 全部订单
 * @mail 154336119@qq.com
 * @date 2015-1-29 上午10:34:22
 */
public class AllorderListActivity extends BaseActivity {
    /**
     * 调试代码TAG
     */
    protected static final String TAG = AllorderListActivity.class.getSimpleName();
    private CommonActionBar mCommonActionBar;

    private DataLoadingLayout mDataLoadingLayout;
    private XListView mXListView;
    private AllOrderAdapter mAllOrderAdapter;
    private FinishReceiver mFinishReceiver;
    private String mMinId;

    private boolean mNeedShowLoadingLayout = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order_list);
        initActionBar();
        initView();

        listItemOnRefresh();
    }

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.txt_all_order);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


    private void initView() {
    	IntentFilter filter = new IntentFilter();
    	mFinishReceiver = new FinishReceiver();
        filter.addAction(BroadCastManager.ACTION_GO_ACCTOUNT);
		LocalBroadcastManager.getInstance(this).registerReceiver(mFinishReceiver, filter);
    	
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	httpRequestOrderList(XListRefreshType.ON_PULL_REFRESH);
            }
        });

        mXListView = (XListView) findViewById(R.id.order_listview);
        mXListView.setPullRefreshEnable(true);
        mXListView.setPullLoadEnable(false);
        mXListView.setAutoRefreshEnable(true);
        mXListView.setAutoLoadMoreEnable(true);
        mXListView.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
                mNeedShowLoadingLayout = false;
                listItemOnRefresh();
            }

            @Override
            public void onLoadMore() {
                mNeedShowLoadingLayout = false;
                listItemOnLoadMore();
            }
        });
        mXListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        mAllOrderAdapter = new AllOrderAdapter(this);
        mXListView.setAdapter(mAllOrderAdapter);
        mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Order order = mAllOrderAdapter.getItem(arg2-1);
				int statu = order.getContract_statu(); //1.不能付 2.没有付 3.付不够 4.已付完
				if(statu == 1){
					ToastUtil.showMessage(AllorderListActivity.this, "你的订单暂时不能开始担保");
				}else if(statu == 4){
					ActivityUtil.next(AllorderListActivity.this, AccountBalanceActivity.class);
				}else if(statu == 3){
					ToastUtil.showMessage(AllorderListActivity.this, "你的订单款项未付完，请联系客服处理");
				}else{
					String orderId = order.getOrder_id()+"";
					Intent intent = new Intent(AllorderListActivity.this, EnsuredTradeActivity.class);
					intent.putExtra(AppConstants.PARAM_ORDER_ID, orderId);
					startActivity(intent);
				}
			}
		});
    }


    
    
    /**
     * 下拉刷新列表
     */
    private void listItemOnRefresh() {
        mMinId = null;
        httpRequestOrderList(XListRefreshType.ON_PULL_REFRESH);
    }

    /**
     * 列表自动加载更多
     */
    private void listItemOnLoadMore() {
        int listItemCount = mAllOrderAdapter.getCount();
        if (listItemCount > 0) {
            Order order = mAllOrderAdapter.getItem(listItemCount - 1);
            mMinId = String.valueOf(order.getOrder_id());
        }
        httpRequestOrderList(XListRefreshType.ON_LOAD_MORE);
    }

    /**
     * HTTP请求 - 全部订单
     *
     * @param xListRefreshType
     */
    private void httpRequestOrderList(final XListRefreshType xListRefreshType) {

        GetOrderListTask task = new GetOrderListTask(AllorderListActivity.this, mMinId);
        task.setCallBack(new AbstractHttpResponseHandler<List<Order>>() {

			@Override
			public void onSuccess(List<Order> orderList) {
				// TODO Auto-generated method stub
				  mDataLoadingLayout.showDataLoadSuccess();

	                // 加载更多还是刷新
	                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
	                	mAllOrderAdapter.setList(orderList);
	                } else {
	                	mAllOrderAdapter.addList(orderList);
	                }

	                if (null != orderList) {
	                    if (orderList.size() < AppConfig.pageSize) {
	                        mXListView.setPullLoadEnable(false);
	                    } else {
	                        mXListView.setPullLoadEnable(true);
	                    }
	                } else {
	                    mXListView.setPullLoadEnable(false);
	                }
	                if (mAllOrderAdapter.getCount() == 0) {
	                    mDataLoadingLayout.showDataEmptyView();
	                    return;
	                }
			}

			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mAllOrderAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoadFailed(error);
                } else {
                    showToastMsg(error);
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
			@Override
			public void onStart() {
				super.onStart();
	            if (mNeedShowLoadingLayout) {
                    mDataLoadingLayout.showDataLoading();
                } else if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mAllOrderAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoading();
                }
			}
		});
        task.send();
    }
    
   public class FinishReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			//unregisterReceiver(mFinishReceiver); 
            AllorderListActivity.this.finish();			
		}
    	
    }
	       
    }

