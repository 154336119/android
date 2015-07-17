package com.huizhuang.zxsq.ui.activity.order;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.task.pay.CheckPosPayTask;
import com.huizhuang.zxsq.security.Security;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.supervision.WaitResponseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 
 * POS机收款
 * @author th
 * 
 */
public class OrderPayByPosActivity extends BaseActivity implements OnClickListener {
    private CommonActionBar mCommonActionBar;
    
    private Button mBtnPay;
    private TextView mTvorderNumber;
    private TextView mTvpayMoney;
    private ImageView mIvBg;
    private TextView mTvTips;
    private String mFinanceId; //应收款ID
    private String mOrderId; //
    private ImageView mIvCode;
    private String mPayMoney;
    private String mPayType;
    private String mAlipayPayInfo;
    private String mNode;
    private boolean mPromo ;//使用优惠券
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payinfo_by_pos);
        getIntentExtra();
        initActionBar();
        initViews();
       // httpRequestGetDecoratePayment();
    }

    private void getIntentExtra() {
    	mOrderId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
    	mFinanceId = getIntent().getStringExtra(AppConstants.PARAM_FINANCE_ID);
    	mPayMoney= getIntent().getStringExtra(AppConstants.PARAM_PAY_MONEY);
    	mPayType= getIntent().getStringExtra(AppConstants.PARAM_PAY_TYPE);
    	mNode = getIntent().getStringExtra(AppConstants.PARAM_NODE_ID);
    	if(getIntent().getStringExtra(AppConstants.PARAM_COUPON)!=null){
    		mPromo = true;
    	}
    }
    
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("惠装收银台");
        
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
    	mTvorderNumber = (TextView)findViewById(R.id.tv_activity_payinfo_by_pos_order_number);
    	mTvpayMoney = (TextView)findViewById(R.id.tv_activity_payinfo_by_pos_pay_money);
    	mIvBg = (ImageView)findViewById(R.id.iv_bg);
    	mIvCode = (ImageView)findViewById(R.id.iv_code);
    	mTvTips = (TextView)findViewById(R.id.tv_tips);
    	
    	int width = UiUtil.getScreenWidth(OrderPayByPosActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, (int) (width / 2.37));
        mIvBg.setLayoutParams(lp);
    	mTvorderNumber.setText(mFinanceId);
    	mTvpayMoney.setText(Util.formatMoney(mPayMoney, 2));
    	mTvTips.setText(Html.fromHtml("<font b='#808080'>"+"我们的客服和服务站工作人员将全程协助您完成POS机刷卡，请在"+"</font>"+"<font color='#ff6c38'>"+"48小时内"+"</font>"+"<font color='#808080'>"
    			+"完成刷卡，以保证您的新家如期开工装修。"+"</font>"));
        mBtnPay = (Button)findViewById(R.id.btn_pay);
        mBtnPay.setOnClickListener(this);
		String tokenValue = ZxsqApplication.getInstance().getUser().getUser_id() + ZxsqApplication.getInstance().getUser().getToken();
		LogUtil.e("mIvCode", AppConfig.HTTP_BASE_URL+"/pay/receive/pay_money.json?"
        		+"token="+ZxsqApplication.getInstance().getUser().getToken()
        		+"&pay_type="+mPayType
        		+"&finance_id="+mFinanceId
        		+"&access_token="+Security.getHMACSHA256String(tokenValue)
        		+"&user_id="+ZxsqApplication.getInstance().getUser().getUser_id());
        ImageLoader.getInstance().displayImage(AppConfig.HTTP_BASE_URL+"/pay/receive/pay_money.json?"
        		+"token="+ZxsqApplication.getInstance().getUser().getToken()
        		+"&pay_type="+"pospay"
        		+"&finance_id="+mFinanceId
        		+"&access_token="+Security.getHMACSHA256String(tokenValue)
        		+"&user_id="+ZxsqApplication.getInstance().getUser().getUser_id(), mIvCode, ImageLoaderOptions.getDefaultImageOption());
    } 

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay://
            	AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0029);
            	httpRequestCheckPosPay();
                break;
            default:
                break;
        }
    }

    /**
     * http请求-检查POS机是否支付成功
     * 
     */
    private void httpRequestCheckPosPay() {

    	CheckPosPayTask task = new CheckPosPayTask(OrderPayByPosActivity.this,mFinanceId);
    	task.setCallBack(new AbstractHttpResponseHandler<String>() {
			
			@Override
			public void onSuccess(String t) {
				Bundle bundle = new Bundle();
            	bundle.putString(AppConstants.PARAM_ORDER_ID, mOrderId);
            	if("1".equals(mNode)){
                    bundle.putString(AppConstants.PARAM_NODE_ID, mNode);
                }else{
                    bundle.putString(AppConstants.PARAM_NODE_ID, String.valueOf(Integer.valueOf(mNode)+1));
                }
            	if(mNode.equals("5")){  //竣工阶段
            		ActivityUtil.nextActivityWithClearTop(OrderPayByPosActivity.this, MyOrderActivity.class, bundle);
            	}else{
            		bundle.putBoolean(AppConstants.PARAM_IS_PAY, true);
            		ActivityUtil.next(OrderPayByPosActivity.this, WaitResponseActivity.class,bundle,false);		
            	}	
            	finish();
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				showToastMsg(error);
			}
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showWaitDialog("确认中...");
			}
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				hideWaitDialog();
			}
		});
    	task.send();
    }
}
