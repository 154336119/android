package com.huizhuang.zxsq.ui.activity.supervision;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.Share;
import com.huizhuang.zxsq.http.bean.order.OrderDetail;
import com.huizhuang.zxsq.http.task.order.GetOrderDetailTask;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseFragmentActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderPayActivity;
import com.huizhuang.zxsq.ui.fragment.supervision.WaitCheckInfoFragment;
import com.huizhuang.zxsq.ui.fragment.zxbd.ZxbdListFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/**
 * 各阶段等待应答
 * 
 * @author th
 * 
 */
public class WaitResponseActivity extends BaseFragmentActivity implements OnClickListener {

    private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;
    
    private TextView mTvAddress;
    private TextView mTvTime;
    private TextView mTvCheckInfo;
    private TextView mTvZxbd;
    private ImageView mIvShareTip;

    private Drawable mDrawable;
    private FragmentManager mFragmentManager;
    private WaitCheckInfoFragment mFragmentWaitCheckInfo;
    private Fragment mFragmentZxbd;
    private String mOdersId;
    private String mNodeId;
    private String mZxbdNode;//根据mNode得到不同的装修宝典列表
    private boolean mIsPay = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_response);
        getIntentExtra();
        setZxbdNode();
        initActionBar();
        initViews();
        initFragment();
        httpRequestGetOrderDetail();
    }

	private void getIntentExtra() {
        mOdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
        mNodeId = getIntent().getStringExtra(AppConstants.PARAM_NODE_ID).trim();
        mIsPay = getIntent().getBooleanExtra(AppConstants.PARAM_IS_PAY, false);
    }
	/**
	 * 根据mNodeId得到装修宝典对应的mNode
	 */
	private void setZxbdNode() {
		mZxbdNode = Util.getZxbdNodeByNodeId(mNodeId);
	}
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        String nodeName = Util.getNodeNameById(mNodeId).substring(0, 2);
        mCommonActionBar.setActionBarTitle("等待"+nodeName+"验收");
        
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mIsPay){
                    ActivityUtil.nextActivityWithClearTop(WaitResponseActivity.this, MyOrderActivity.class);
                }else{
                    finish();
                }
            }
        });
        
        mCommonActionBar.setRightImgBtn(R.drawable.ic_actionbar_share, new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
            	//根据不同的订单状态来确定分享的文案
                Util.setShareByNodeId(mNodeId);
                Share share = new Share();
				Util.showShare(false, WaitResponseActivity.this,share);
            }
        });
        
    }

    private void initViews() {
        mDataLoadingLayout = (DataLoadingLayout)findViewById(R.id.data_load_layout);
        
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mTvCheckInfo = (TextView) findViewById(R.id.tv_check_info);
        mTvZxbd = (TextView) findViewById(R.id.tv_zxgl);
        mIvShareTip = (ImageView) findViewById(R.id.iv_share_tip);
        findViewById(R.id.tv_check_info).setOnClickListener(this);
        findViewById(R.id.tv_zxgl).setOnClickListener(this);
        mDrawable = getResources().getDrawable(R.drawable.icon_order_triangle);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
        mTvTime = (TextView)findViewById(R.id.tv_time);
    } 

    private void initFragment() { 
    	
        mFragmentManager = getSupportFragmentManager();
        // 验收信息
        mFragmentWaitCheckInfo = new WaitCheckInfoFragment();
        // 装修宝典
        mFragmentZxbd = new ZxbdListFragment(mZxbdNode);//根据mNode得到相应的装修宝典

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.fl_main_container, mFragmentWaitCheckInfo);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_info://验收信息
                switchFragment(0);
                break;
            case R.id.tv_zxgl:// 装修攻略
                switchFragment(1);
                break;
            default:
                break;
        }
    }

    /**
     * 切换fragmenty
     * @param position 0第一个fragment 1第二个fragment
     */
    public void switchFragment(final int position){
        switch (position) {
            case 0:
                FragmentTransaction ft1 = mFragmentManager.beginTransaction();
                if(!mFragmentWaitCheckInfo.isAdded()){
                    ft1.hide(mFragmentZxbd).add(R.id.fl_main_container, mFragmentWaitCheckInfo);
                    ft1.commit();
                }else{
                    ft1.hide(mFragmentZxbd).show(mFragmentWaitCheckInfo).commit();
                }
                mTvCheckInfo.setCompoundDrawables(null, null, null, mDrawable);
                mTvZxbd.setCompoundDrawables(null, null, null, null);
                break;
            case 1:
                FragmentTransaction ft2 = mFragmentManager.beginTransaction();
                if(!mFragmentZxbd.isAdded()){
                    ft2.hide(mFragmentWaitCheckInfo).add(R.id.fl_main_container, mFragmentZxbd);
                    ft2.commit();
                }else{
                    ft2.hide(mFragmentWaitCheckInfo).show(mFragmentZxbd).commit();
                }
                mTvZxbd.setCompoundDrawables(null, null, null, mDrawable);
                mTvCheckInfo.setCompoundDrawables(null, null, null, null);
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
            if(!TextUtils.isEmpty(orderDetail.getParent().getHousing_name())){
                mTvAddress.setText(orderDetail.getParent().getHousing_name());
            }else{
                mTvAddress.setVisibility(View.GONE);
            }
            String text = "";
            String time = DateUtil.timestampToSdate(orderDetail.getParent().getStage_time(), "yyyy-MM-dd");
            if("1".equals(mNodeId)){
                text = time +" 开工";
            }else{
                String nodeName = Util.getNodeNameById((Integer.valueOf(mNodeId)-1)+"");
                text = time +" 完成"+nodeName+"验收";
            }
            mTvTime.setText(text);
        }else{
            mTvAddress.setVisibility(View.GONE);
            mTvTime.setVisibility(View.GONE);
        }
        if(orderDetail.getChild() != null && orderDetail.getChild().size() > 0){
            mFragmentWaitCheckInfo.initData(orderDetail.getChild(),mNodeId);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mIsPay){
                ActivityUtil.nextActivityWithClearTop(WaitResponseActivity.this, MyOrderActivity.class);
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    
}
