package com.huizhuang.zxsq.ui.activity.order;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.order.OrderSignForeman;
import com.huizhuang.zxsq.http.task.order.GetOrderForemanTask;
import com.huizhuang.zxsq.http.task.order.OrderAppointmentSubmitTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.order.OrderAppointmentSignedAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/**
 * 预约签约
 * 
 * @author th
 * 
 */
public class OrderAppointmentSignedActivity extends BaseActivity {

    public final static String EXTRA_SIGN_FOREMAN = "signforeman";
    
    private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;
    private XListView mXListView;
    
    private String mOdersId;
    private OrderAppointmentSignedAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_appointment_signed);
        getIntentExtra();
        initActionBar();
        initViews();
    }

    private void getIntentExtra() {
        mOdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_load_layout);
        mDataLoadingLayout.setOnReloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequestQueryForemans(XListRefreshType.ON_PULL_REFRESH);
            }
        });
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("预约签约");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        mXListView = (XListView) findViewById(R.id.xlist);
        mXListView.setPullRefreshEnable(false);
        mXListView.setPullLoadEnable(false);
        mXListView.setAutoLoadMoreEnable(false);
        mXListView.setAutoRefreshEnable(true);
        mXListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                httpRequestQueryForemans(XListRefreshType.ON_PULL_REFRESH);
            }

            @Override
            public void onLoadMore() {

            }
        });
        mAdapter = new OrderAppointmentSignedAdapter(this, mClickHandler);
        mXListView.setAdapter(mAdapter);
    }

    /**
     * 获取签约工长列表
     * 
     * @param xListRefreshType
     */
    private void httpRequestQueryForemans(final XListRefreshType xListRefreshType) {
        GetOrderForemanTask task =
                new GetOrderForemanTask(OrderAppointmentSignedActivity.this, mOdersId);
        task.setCallBack(new AbstractHttpResponseHandler<List<OrderSignForeman>>() {

            @Override
            public void onSuccess(List<OrderSignForeman> result) {
                mDataLoadingLayout.showDataLoadSuccess();

                if (null != result) {
                    // 加载更多还是刷新
                    if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        if (result.size() == 0) {
                            mDataLoadingLayout.showDataEmptyView();
                        } else {
                            mAdapter.setList(result);
                        }
                    } else {
                        mAdapter.addList(result);
                    }
                    if (result.size() < AppConfig.pageSize) {
                        mXListView.setPullLoadEnable(false);
                    } else {
                        mXListView.setPullLoadEnable(true);
                    }
                }
            }

            @Override
            public void onFailure(int code, String error) {
                mDataLoadingLayout.showDataLoadSuccess();
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

    @SuppressLint("HandlerLeak")
    private Handler mClickHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            int position = msg.arg1;
            OrderSignForeman orderSignForeman = mAdapter.getList().get(position);
            switch (what) {
                case 0:// 取消签约
                    Bundle bd = new Bundle();
                    bd.putString(AppConstants.PARAM_ORDER_ID, orderSignForeman.getAllot_id());
                    ActivityUtil.next(OrderAppointmentSignedActivity.this,
                            OrderCancelAppointmentReasonListActivity.class, bd, -1);
                    break;
                case 1:// 签约
                    httpRequestSubmit(orderSignForeman);
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 预约工长
     * 
     * @param orderSignForeman
     */
    private void httpRequestSubmit(final OrderSignForeman orderSignForeman) {
        OrderAppointmentSubmitTask task =
                new OrderAppointmentSubmitTask(OrderAppointmentSignedActivity.this,
                        orderSignForeman.getAllot_id());
        task.setCallBack(new AbstractHttpResponseHandler<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.d("onSuccess ConstructionSiteList = " + result);
                // 跳转签约成功页面
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_SIGN_FOREMAN, orderSignForeman);
            	ActivityUtil.next(OrderAppointmentSignedActivity.this, OrderServeStationSignActivity.class, bundle,true);
            }

            @Override
            public void onFailure(int code, String error) {
                showToastMsg(error);
            }

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.d("onStart");
                showWaitDialog(getResources().getString(R.string.txt_on_waiting));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }

        });
        task.send();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mXListView.onRefresh();
    }
     
}
