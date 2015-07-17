package com.huizhuang.zxsq.ui.activity.order;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.order.OrderCancelRason;
import com.huizhuang.zxsq.http.task.order.OrderCancelSubmitTask;
import com.huizhuang.zxsq.http.task.order.OrderCancelTask;
import com.huizhuang.zxsq.ui.activity.HomeActivity;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.order.OrderCancelReasonAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.MyListView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * 取消订单
 * 
 * @author th
 * 
 */
public class OrderCancelReasonListActivity extends BaseActivity implements OnClickListener {

    private DataLoadingLayout mDataLoadingLayout;
    private CommonActionBar mCommonActionBar;

    private MyListView mXListView;
    private EditText mEtContent;

    private OrderCancelReasonAdapter mAdapter;
    private String mOrdersId;
    private Boolean mIsResponse = false;
    private boolean mIsDoremanSource = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cancel_reason_list);
        getIntentExtra();
        initActionBar();
        initViews();
        httpRequestQueryCancelReason();
    }

    private void getIntentExtra() {
        mOrdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
        mIsResponse = getIntent().getBooleanExtra("is_responese", false);
        mIsDoremanSource = getIntent().getBooleanExtra(AppConstants.PARAM_ORDER_SOURCE_NAME, false);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        LogUtil.d("initActionBar");

        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("取消订单");
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
                httpRequestQueryCancelReason();
            }
        });
        mEtContent = (EditText) findViewById(R.id.et_other);
        findViewById(R.id.btn_submit).setOnClickListener(this);

        mXListView = (MyListView) findViewById(R.id.xlv_reason);
        mXListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d("onItemClick position = " + position);
                OrderCancelRason orderCancelRason = mAdapter.getList().get(position);
                int code = orderCancelRason.getCode();
                mAdapter.setSelectIds(code);
                String name = orderCancelRason.getLabel();
                if("其他".equals(name)){
                    mEtContent.setVisibility(View.VISIBLE);
                }else{
                    mEtContent.setVisibility(View.GONE);
                }
            }
        });
        mAdapter = new OrderCancelReasonAdapter(this);
        mXListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if(mEtContent.getVisibility() == View.VISIBLE && TextUtils.isEmpty(mEtContent.getText().toString())){
                    showToastMsg("请填写取消订单的原因");
                }else if(mAdapter.getSelectIds() == -1){
                    showToastMsg("请选择取消订单的原因");
                }else{
                    httpRequestSubmit();
                }
                break;

            default:
                break;
        }
    }

    private void httpRequestSubmit(){
        String content = mEtContent.getText().toString();
        OrderCancelSubmitTask task = new OrderCancelSubmitTask(OrderCancelReasonListActivity.this,mOrdersId,mAdapter.getSelectIds(),content);
        task.setCallBack(new AbstractHttpResponseHandler<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.d("onSuccess ConstructionSiteList = " + result);
                showToastMsg("取消成功");
                if(mIsResponse){
                    if(mIsDoremanSource){
                        ActivityUtil.nextActivityWithClearTop(OrderCancelReasonListActivity.this, HomeActivity.class);
                    }else{
                        ActivityUtil.nextActivityWithClearTop(OrderCancelReasonListActivity.this, MyOrderActivity.class);
                    }
                }else{
                    finish();
                }
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

    /**
     * 获取取消订单原因
     * @param xListRefreshType
     */
    private void httpRequestQueryCancelReason() {
        OrderCancelTask task = new OrderCancelTask(OrderCancelReasonListActivity.this, mOrdersId);
        task.setCallBack(new AbstractHttpResponseHandler<List<OrderCancelRason>>() {

            @Override
            public void onSuccess(List<OrderCancelRason> result) {
                LogUtil.d("onSuccess ConstructionSiteList = " + result);
                mDataLoadingLayout.showDataLoadSuccess();
                if (null != result && result.size() > 0) {
                    // 加载更多还是刷新
                    mAdapter.setList(result);
                    mAdapter.setSelectIds(result.get(0).getCode());
                } else {
                    mDataLoadingLayout.showDataEmptyView();
                }
            }

            @Override
            public void onFailure(int code, String error) {
                if (0 == mAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoadFailed(error);
                } else {
                    // mPageIndex--;
                    showToastMsg(error);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.d("onStart");
                if (0 == mAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoading();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });
        task.send();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mIsDoremanSource){
                ActivityUtil.nextActivityWithClearTop(OrderCancelReasonListActivity.this, HomeActivity.class);
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
