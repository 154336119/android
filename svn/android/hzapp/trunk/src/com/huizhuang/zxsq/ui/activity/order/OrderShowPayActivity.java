package com.huizhuang.zxsq.ui.activity.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.order.PayInfo;
import com.huizhuang.zxsq.http.task.order.GetPayInfoTask;
import com.huizhuang.zxsq.ui.activity.HomeActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/**
 * 
 * 
 * @author th
 * 
 */
public class OrderShowPayActivity extends BaseActivity implements OnClickListener {

    private CommonActionBar mCommonActionBar;
    private TextView mTvAddress;
    private TextView mTvTime;
    private TextView mTvMessage;
    
    private TextView mContractNum;
    private TextView mSignDate;
    private TextView mStartDate;
    private TextView mContractAccount;
    private TextView mEarnestMoney;
    private TextView mChangeMoney;
    private TextView mPayMoney;
    private TextView mTvTips;
    private TextView mTvTipsTow;
    private Button mBtnPay;
    private DataLoadingLayout mDataLoadingLayout;
    private PayInfo mPayInfo;
    private String mOrderId;
    private String mNode;
    
    private ClosePageBroadcastReceiver mHomeBroadcastReceiver;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_show_pay);
        getIntentExtra();
        initActionBar();
        initViews();
        initClosePageBroadcastReceiver();
        httpRequestGetDecoratePayment();
    }
    private void getIntentExtra() {
    	mOrderId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
    	mNode = getIntent().getStringExtra(AppConstants.PARAM_NODE_ID);
    }
    
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("支付装修款");
        
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvMessage = (TextView) findViewById(R.id.tv_message);
        mContractNum = (TextView) findViewById(R.id.contract_num);
        mSignDate = (TextView) findViewById(R.id.sign_date);
        mStartDate = (TextView) findViewById(R.id.start_date);
        mContractAccount = (TextView) findViewById(R.id.contract_account);
        mEarnestMoney = (TextView) findViewById(R.id.earnest_money);
        mChangeMoney = (TextView) findViewById(R.id.change_money);
        mPayMoney = (TextView) findViewById(R.id.pay_money);
        mTvTips = (TextView) findViewById(R.id.tv_tips);
        mTvTipsTow = (TextView) findViewById(R.id.tv_tips_tow);
        mBtnPay = (Button) findViewById(R.id.btn_pay);
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        
        mBtnPay.setOnClickListener(this);
    } 

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:// 支付 - 下个页面
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0027);
            	Bundle bundle = new Bundle();
            	//bundle.putFloat(AppConstants.PARAM_PAY_MONEY, mPayInfo.getAmount_receivable());
            	bundle.putString(AppConstants.PARAM_PAY_MONEY,String.valueOf(mPayInfo.getAmount())); 
            	bundle.putString(AppConstants.PARAM_ORDER_ID, mOrderId);
            	bundle.putString(AppConstants.PARAM_ORDER_NO, mPayInfo.getOrder_no());
            	bundle.putString(AppConstants.PARAM_FINANCE_ID, String.valueOf(mPayInfo.getId()));
            	bundle.putString(AppConstants.PARAM_NODE_ID, mNode);
            	if(mPayInfo.getCoupon_amount()!=null){
            		bundle.putString(AppConstants.PARAM_COUPON, mPayInfo.getCoupon_amount());
            	}
        		ActivityUtil.next(this, OrderPayActivity.class,bundle,false);

                break;
            default:
                break;
        }
    }

    /**
     * http请求-获取装修支付款详情
     * 
     * @param xListRefreshType
     */
    private void httpRequestGetDecoratePayment() {
    	//GetPayInfoTask task = new GetPayInfoTask(OrderShowPayActivity.this, "13270", "1");
    	GetPayInfoTask task = new GetPayInfoTask(OrderShowPayActivity.this, mOrderId, mNode);
    	task.setCallBack(new AbstractHttpResponseHandler<PayInfo>() {
			
			@Override
			public void onSuccess(PayInfo  payInfo) {
				mDataLoadingLayout.showDataLoadSuccess();
				mPayInfo = payInfo;
				setData(payInfo);
			}
			
			@Override
			public void onFailure(int code, String error) {
                mDataLoadingLayout.showDataLoadFailed(error);
				
			}
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
                mDataLoadingLayout.showDataLoading();
			}			
		});
    	task.send();
    }
    
	private void setData(PayInfo payInfo) {
		mTvAddress.setText(payInfo.getHousing_address());
		if("1".equals(mNode)){
		    mTvTime.setText(DateUtil.timestampToSdate(payInfo.getRecord_time(), "yyyy年MM月dd日")+" 完成合同备案");
		}else{
		    mTvTime.setText(DateUtil.timestampToSdate(payInfo.getYanshou_time(), "yyyy年MM月dd日")+" 完成"+Util.getNodeNameById(mNode)+"验收");
		}
        mContractNum.setText("合同号："+String.valueOf(payInfo.getContract_no()));
        mSignDate.setText("签约日期："+DateUtil.timestampToSdate(payInfo.getRecord_time(), "yyyy年MM月dd日"));
        mStartDate.setText("开工日期："+DateUtil.timestampToSdate(payInfo.getTime(), "yyyy年MM月dd日"));
        mContractAccount.setText("合同金额："+Util.formatMoneyNoSingle(payInfo.getActual_amount(), 2)+"元（已付金额："+Util.formatMoneyNoSingle(payInfo.getReceive(), 2)+"元）");
        mEarnestMoney.setText("预付定金："+Util.formatMoneyNoSingle(payInfo.getOrder_prepay(), 2)+"元");
        mChangeMoney.setText("增减费用："+Util.formatMoneyNoSingle(payInfo.getZj_price(), 2)+"元");
        mTvTipsTow.setText("为了保证您的新家能如期进场装修，您需要支付"+payInfo.getRate()+"%装修款");
        if(mNode.equals("1")){
        	mTvTips.setText("* 本阶段支付金额 =合同金额×"+payInfo.getRate()+"%+增减费用-预付定金");
        }else{
        	 mTvTips.setText("* 本阶段支付金额 =合同金额×"+payInfo.getRate()+"%+增减费用");
        }
        mPayMoney.setText(Util.formatMoney(payInfo.getAmount(),2));
	}
	
	/**
	 * 初始化-广播
	 */
	private void initClosePageBroadcastReceiver(){
		IntentFilter intentFilter = new IntentFilter();
		ClosePageBroadcastReceiver mHomeBroadcastReceiver = new ClosePageBroadcastReceiver();
		intentFilter.addAction(BroadCastManager.ACTION_CLOSE_PAY_PAGE);//关闭支付
		LocalBroadcastManager.getInstance(OrderShowPayActivity.this).registerReceiver(mHomeBroadcastReceiver, intentFilter);

	}
	
	public class ClosePageBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(BroadCastManager.ACTION_CLOSE_PAY_PAGE)){  //刷新个人信息广播
				OrderShowPayActivity.this.finish();
			}
		}
		
	}
}
