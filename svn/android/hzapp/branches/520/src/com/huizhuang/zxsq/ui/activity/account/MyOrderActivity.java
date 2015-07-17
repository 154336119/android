package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.task.common.GetOrderListTask;
import com.huizhuang.zxsq.http.task.order.GetOrderTask;
import com.huizhuang.zxsq.ui.activity.HomeActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderEditorInfoActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderWaitResponseActivity;
import com.huizhuang.zxsq.ui.adapter.AllOrderAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * User : jean
 * Email: lgmshare@gmail.com
 * Date : 2015/2/6
 * Time : 11:46
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements OnClickListener{
	private XListView mXListView;
	private AllOrderAdapter mAdapter;
	private CommonActionBar mCommonActionBar;
	private DataLoadingLayout mDataLoadingLayout;
	private String mMinId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        initActionBar();
        initViews();
    }

    private void initActionBar() {
        CommonActionBar mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("我的订单");
        mCommonActionBar.setLeftImgBtn(R.drawable.back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.nextActivityWithClearTop(MyOrderActivity.this, HomeActivity.class);
            }
        });
    }

    private void initViews() {
    	mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.common_dl);
    	mAdapter = new AllOrderAdapter(this);
    	mXListView = (XListView) findViewById(R.id.order_list_view);
    	mXListView.setPullLoadEnable(true);
    	mXListView.setPullRefreshEnable(true);
    	mXListView.setAutoRefreshEnable(true);
    	mXListView.setAdapter(mAdapter);
    	mXListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				mMinId = null;
				httRequestGetOrderList(XListRefreshType.ON_PULL_REFRESH);
			}
			
			@Override
			public void onLoadMore() {
				int listItemCount = mAdapter.getCount();
				if (listItemCount > 0) {
					Order order = mAdapter.getItem(listItemCount - 1);
					mMinId = order.getOrder_id()+"";
				}			
				httRequestGetOrderList(XListRefreshType.ON_LOAD_MORE);
			}
		});
    }
    
    private void httRequestGetOrderList(final XListRefreshType xListRefreshType){
    	GetOrderListTask getOrderListTask = new GetOrderListTask(this,mMinId);
    	getOrderListTask.setCallBack(new AbstractHttpResponseHandler<List<Order>>() {
			@Override
			public void onSuccess(List<Order> list) {
				mDataLoadingLayout.showDataLoadSuccess();
				if(list!=null){   //列表是否存在
					if(xListRefreshType == XListRefreshType.ON_PULL_REFRESH){ //上拉还是下拉
						mAdapter.setList(list);
					}else{
						mAdapter.addList(list);
					}
					
					if(list.size()==0&&mAdapter.getCount()==0){   //是否没数据
						mDataLoadingLayout.showDataLoadEmpty("呜～～，您还没有在惠装预约工长装修服务\n惠装工长比装修公司省40%");
						mDataLoadingLayout.setOnAppointmentClickListener(MyOrderActivity.this);
					}
					
					if (list.size() < AppConfig.pageSize) { //是否总数小于10条
						mXListView.setPullLoadEnable(false);
					} else {
						mXListView.setPullLoadEnable(true);
					}
				}else{
					mXListView.setPullLoadEnable(false);
				}
				
				BroadCastManager.sendBroadCast(MyOrderActivity.this, BroadCastManager.ACTION_REFRESH_USER_INFO); //刷新个人信息
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				if(xListRefreshType == XListRefreshType.ON_PULL_REFRESH&&mAdapter.getCount() == 0){
					mDataLoadingLayout.showDataLoadEmpty("呜～～，您还没有在惠装预约工长装修服务、你惠装工长比装修公司省40%");
					mDataLoadingLayout.setOnAppointmentClickListener(MyOrderActivity.this);
				}else {
					showToastMsg(error);
				}
			}
			@Override
			public void onStart() {
				super.onStart();
				if(xListRefreshType == XListRefreshType.ON_PULL_REFRESH&&mAdapter.getCount()==0){
					mDataLoadingLayout.showDataLoading();
				}
			}
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					mXListView.onRefreshComplete();
				} else {
					mXListView.onLoadMoreComplete();
				}
			}
		});
    	getOrderListTask.send();
    }

	@Override
	public void onClick(View v) {
		ActivityUtil.checkAppointmentToJump(MyOrderActivity.this, null);
	}

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("onResume()");
        mXListView.onRefresh();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ActivityUtil.nextActivityWithClearTop(MyOrderActivity.this, HomeActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }

    
}
