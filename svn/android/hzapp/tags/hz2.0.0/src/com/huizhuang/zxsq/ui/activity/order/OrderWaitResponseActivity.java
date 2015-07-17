package com.huizhuang.zxsq.ui.activity.order;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.order.OrderDetail;
import com.huizhuang.zxsq.http.task.order.GetOrderDetailTask;
import com.huizhuang.zxsq.ui.activity.HomeActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseFragmentActivity;
import com.huizhuang.zxsq.ui.fragment.order.AcceptForemanFragment;
import com.huizhuang.zxsq.ui.fragment.zxbd.ZxbdListFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/**
 * 下单等待工作应答
 * 
 * @author th
 * 
 */
public class OrderWaitResponseActivity extends BaseFragmentActivity implements OnClickListener {

    private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;
    
    private TextView mTvAddress;
    private TextView mTvTime;
    private TextView mTvMessage;
    private TextView mTvForeman;
    private TextView mTvZxbd;

    private Drawable mDrawable;
    private FragmentManager mFragmentManager;
    private AcceptForemanFragment mFragmentForeman;
    private Fragment mFragmentZxbd;
    private String mOdersId;
	private String mNode = "p6";//对应装修宝典的阶段 这里是p6:量房阶段
	private boolean mIsDoremanSource = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_wait_response);
        getIntentExtra();
        initActionBar();
        initViews();
        initFragment();
        httpRequestGetOrderDetail();
    }

    private void getIntentExtra() {
        mOdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
        mIsDoremanSource = getIntent().getBooleanExtra(AppConstants.PARAM_ORDER_SOURCE_NAME, false);
    }
    
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("等待工长应答");
        mCommonActionBar.setRightTxtBtn(R.string.txt_cancel_order, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 取消订单
            	AnalyticsUtil.onEvent(OrderWaitResponseActivity.this, UmengEvent.ID_CLICK_0024);
                Bundle bd = new Bundle();
                bd.putString(AppConstants.PARAM_ORDER_ID, mOdersId);
                bd.putBoolean("is_responese", true);
                bd.putBoolean(AppConstants.PARAM_ORDER_SOURCE_NAME, mIsDoremanSource);
                ActivityUtil.next(OrderWaitResponseActivity.this, OrderCancelReasonListActivity.class, bd, -1);
            }
        });
        
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mIsDoremanSource){
                    ActivityUtil.nextActivityWithClearTop(OrderWaitResponseActivity.this, HomeActivity.class);
                }else{
                    finish();
                }
            }
        });
    }

    private void initViews() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_load_layout);
        
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvMessage = (TextView) findViewById(R.id.tv_message);
        mTvForeman = (TextView) findViewById(R.id.tv_foreman);
        mTvZxbd = (TextView) findViewById(R.id.tv_zxgl);
        findViewById(R.id.tv_foreman).setOnClickListener(this);
        findViewById(R.id.tv_zxgl).setOnClickListener(this);
        mDrawable = getResources().getDrawable(R.drawable.icon_order_triangle);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
    } 

    private void initFragment() { 
    	
        mFragmentManager = getSupportFragmentManager();
        // 接单工长
        mFragmentForeman = new AcceptForemanFragment();
        // 装修宝典
        mFragmentZxbd = new ZxbdListFragment(mNode );//p6:量房阶段

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.fl_main_container, mFragmentForeman);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_foreman:// 接单工长
                FragmentTransaction ft1 = mFragmentManager.beginTransaction();
                if(!mFragmentForeman.isAdded()){
                    ft1.hide(mFragmentZxbd).add(R.id.fl_main_container, mFragmentForeman);
                    ft1.commit();
                }else{
                    ft1.hide(mFragmentZxbd).show(mFragmentForeman).commit();
                }
                /*ft1.replace(R.id.fl_main_container, mFragmentForeman);
                ft1.commit();*/
                mTvForeman.setCompoundDrawables(null, null, null, mDrawable);
                mTvZxbd.setCompoundDrawables(null, null, null, null);
                break;
            case R.id.tv_zxgl:// 装修攻略
                FragmentTransaction ft2 = mFragmentManager.beginTransaction();
                if(!mFragmentZxbd.isAdded()){
                    ft2.hide(mFragmentForeman).add(R.id.fl_main_container, mFragmentZxbd);
                    ft2.commit();
                }else{
                    ft2.hide(mFragmentForeman).show(mFragmentZxbd).commit();
                }
                /*ft2.replace(R.id.fl_main_container, mFragmentZxbd);
                ft2.commit();*/
                mTvZxbd.setCompoundDrawables(null, null, null, mDrawable);
                mTvForeman.setCompoundDrawables(null, null, null, null);
                break;
            default:
                break;
        }
    }

    /**
     * http请求-获取订单详细
     * 
     * @param xListRefreshType
     */
    private void httpRequestGetOrderDetail() {
        GetOrderDetailTask task = new GetOrderDetailTask(this, mOdersId);
        task.setCallBack(new AbstractHttpResponseHandler<OrderDetail>() {

            @Override
            public void onSuccess(OrderDetail result) {
                mDataLoadingLayout.showDataLoadSuccess();
                if(result != null){
                    initData(result);
                }else{
                    mDataLoadingLayout.showDataEmptyView();
                }
            }

            @Override
            public void onFailure(int code, String error) {
                mDataLoadingLayout.showDataLoadFailed(error);
            }

            @Override
            public void onStart() {
                super.onStart();
                mDataLoadingLayout.showDataLoading();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });
        task.send();
    }

    // 初始化数据
    private void initData(OrderDetail orderDetail) {
        if(orderDetail.getParent() != null){
            if (!TextUtils.isEmpty(orderDetail.getParent().getMeasuring_time())) {
                mTvTime.setText(DateUtil.format(orderDetail.getParent().getMeasuring_time(),
                        "yyyy-MM-dd", "yyyy年 MM月dd日") + " 量房");
            }else{
                mTvTime.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(orderDetail.getParent().getUser_remark())) {
                mTvMessage.setText(orderDetail.getParent().getUser_remark());
                mTvMessage.setVisibility(View.VISIBLE);
            } else {
                mTvMessage.setVisibility(View.GONE);
            }
            
            if(!TextUtils.isEmpty(orderDetail.getParent().getHousing_address())){
                mTvAddress.setText(orderDetail.getParent().getHousing_address());
            }else{
                mTvAddress.setVisibility(View.GONE);
            }
        }else{
            mTvMessage.setVisibility(View.GONE);
            mTvTime.setVisibility(View.GONE);
            mTvAddress.setVisibility(View.GONE);
        }
        mFragmentForeman.initData(orderDetail.getChild());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mIsDoremanSource){
                ActivityUtil.nextActivityWithClearTop(OrderWaitResponseActivity.this, HomeActivity.class);
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
        
}
