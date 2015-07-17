package com.huizhuang.zxsq.ui.activity.order;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.pay.AlipayPayInfo;
import com.huizhuang.zxsq.http.bean.pay.BankPayInfo;
import com.huizhuang.zxsq.http.bean.pay.PayResult;
import com.huizhuang.zxsq.http.task.pay.GetPayDataTask;
import com.huizhuang.zxsq.ui.activity.WebActivity;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderShowPayActivity.ClosePageBroadcastReceiver;
import com.huizhuang.zxsq.ui.activity.supervision.WaitResponseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PayUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/**
 * 
 * 惠装收银台
 * @author th
 * 
 */
public class OrderPayActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	public static final String PAY_TYPE_ALIPAY = "mobile_alipay"; //支付宝
	public static final String PAY_TYPE_WECHAT = "weixinpay"; //微信支付
	public static final String PAY_TYPE_POS = "pospay"; //POS支付
	public static final String PAY_TYPE_BANK = "upop"; //银联支付
    private CommonActionBar mCommonActionBar;
    
    private Button mBtnPay;
    private CheckBox mCbAlipay;
    private CheckBox mCbWechat;
    private CheckBox mCbBank;
    private CheckBox mCbPos;
    private TextView mTvorderNumber;
    private TextView mTvpayMoney;
    private String mFinanceId; //应收款ID
    private String mOrderId; //
    private String mNode;
    private String mOrderNo;
    
    private String mPayMoney;
    private String mPayType = PAY_TYPE_ALIPAY;
    
    private String mAlipayPayInfo;
    private String mBankPayInfo;
    private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(OrderPayActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					Bundle bundle = new Bundle();
                	bundle.putString(AppConstants.PARAM_ORDER_ID, mOrderId);
                	bundle.putString(AppConstants.PARAM_NODE_ID, String.valueOf(Integer.valueOf(mNode)+1));
                	if(String.valueOf(Integer.valueOf(mNode)+1).equals("5")){  //竣工阶段
                		ActivityUtil.nextActivityWithClearTop(OrderPayActivity.this, MyOrderActivity.class, bundle);
                	}else{
                		ActivityUtil.next(OrderPayActivity.this, WaitResponseActivity.class,bundle,false);		
                	}
					
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(OrderPayActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(OrderPayActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(OrderPayActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        getIntentExtra();
        initActionBar();
        initViews();
        initClosePageBroadcastReceiver();
       // httpRequestGetDecoratePayment();
    }

    private void getIntentExtra() {
    	mOrderId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
    	mFinanceId = getIntent().getStringExtra(AppConstants.PARAM_FINANCE_ID);
    	mPayMoney= getIntent().getStringExtra(AppConstants.PARAM_PAY_MONEY);
    	mNode = getIntent().getStringExtra(AppConstants.PARAM_NODE_ID);
    	mOrderNo = getIntent().getStringExtra(AppConstants.PARAM_ORDER_NO);
    }
    
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.activity_pay_common_action_bar);
        mCommonActionBar.setActionBarTitle("惠装收银台");
        
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
    	mTvorderNumber = (TextView)findViewById(R.id.tv_activity_pay_order_number);
    	mTvpayMoney = (TextView)findViewById(R.id.tv_activity_pay_pay_money);
    	mTvorderNumber.setText(mOrderNo);
    	mTvpayMoney.setText(Util.formatMoney(mPayMoney, 2));
        mCbAlipay = (CheckBox)findViewById(R.id.cb_activity_pay_zhifubao);
        mCbWechat = (CheckBox)findViewById(R.id.cb_activity_pay_weixin);
        mCbBank = (CheckBox)findViewById(R.id.cb_activity_pay_online);
        mCbPos = (CheckBox)findViewById(R.id.cb_activity_pay_pos);
        
        mCbAlipay.setOnCheckedChangeListener(this);
        mCbWechat.setOnCheckedChangeListener(this);
        mCbBank.setOnCheckedChangeListener(this);
        mCbPos.setOnCheckedChangeListener(this);
        
        mBtnPay = (Button)findViewById(R.id.btn_activity_pay_pay);
        mBtnPay.setOnClickListener(this);
    } 

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_pay_pay:// 支付 - 下个页面
            	if(mPayType.equals(PAY_TYPE_POS)){
                	Bundle bundle = new Bundle();
                	bundle.putString(AppConstants.PARAM_PAY_MONEY,mPayMoney); //测试
                	bundle.putString(AppConstants.PARAM_ORDER_ID, mOrderId);
                	bundle.putString(AppConstants.PARAM_FINANCE_ID, mFinanceId);
                	bundle.putString(AppConstants.PARAM_NODE_ID, mNode);
            		ActivityUtil.next(this, OrderPayByPosActivity.class,bundle,false);
            	}else{
                	httpRequestGetPayData(mFinanceId,mPayType);
            	}
            	//httpRequestGetPayData("3598",PAY_TYPE_ALIPAY);
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
    private void httpRequestGetPayData(String finance_id,String pay_type) {
    	GetPayDataTask task = new GetPayDataTask(OrderPayActivity.this, finance_id, pay_type);
    //	GetPayDataTask task = new GetPayDataTask(OrderPayActivity.this, "3598", PAY_TYPE_ALIPAY); //测试
    	task.setCallBack(new AbstractHttpResponseHandler<String>() {
			
			@Override
			public void onSuccess(String payData) {
				// TODO Auto-generated method stub
				mAlipayPayInfo = payData;
				choicePayTaype(mAlipayPayInfo);
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				showToastMsg(error);
			}
		});
    	task.send();
    }
    /**
     * 设置付款方式
     * 
     * @param progressSate
     */
    private void setCheckBoxState(View view) {
        switch (view.getId()) {
        case R.id.cb_activity_pay_zhifubao:
        	mCbAlipay.setChecked(true);
            mCbWechat.setChecked(false);
            mCbBank.setChecked(false);
            mCbPos.setChecked(false);
            
            mCbAlipay.setClickable(false);
            mCbWechat.setClickable(true);
            mCbBank.setClickable(true);
            mCbPos.setClickable(true);
            mPayType = PAY_TYPE_ALIPAY;
            break;
        case R.id.cb_activity_pay_weixin:
        	mCbAlipay.setChecked(false);
            mCbWechat.setChecked(true);
            mCbBank.setChecked(false);
            mCbPos.setChecked(false);
            
            mCbAlipay.setClickable(true);
            mCbWechat.setClickable(false);
            mCbBank.setClickable(true);
            mCbPos.setClickable(true);
            
            mPayType = PAY_TYPE_WECHAT;
            break;
        case R.id.cb_activity_pay_online:
        	mCbAlipay.setChecked(false);
            mCbWechat.setChecked(false);
            mCbBank.setChecked(true);
            mCbPos.setChecked(false);
            
            mCbAlipay.setClickable(true);
            mCbWechat.setClickable(true);
            mCbBank.setClickable(false);
            mCbPos.setClickable(true);
            mPayType = PAY_TYPE_BANK;            
            break;
        case R.id.cb_activity_pay_pos:
        	mCbAlipay.setChecked(false);
            mCbWechat.setChecked(false);
            mCbBank.setChecked(false);
            mCbPos.setChecked(true);
            
            mCbAlipay.setClickable(true);
            mCbWechat.setClickable(true);
            mCbBank.setClickable(true);
            mCbPos.setClickable(false);
            mPayType = PAY_TYPE_POS;
            break;
        default:
            break;
        }
    }
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if(arg1){

			setCheckBoxState(arg0);
		}
	} 
	/**
	 * 支付-支付宝
	 */
	private void payAlipay(AlipayPayInfo alipayOrderInfo){
		// 必须异步调用
		String orderInfo = PayUtil.getAlipayOrderInfo(alipayOrderInfo);
		System.out.println("===orderInfo:"+orderInfo);
		// 对订单做RSA 签名
				String sign = PayUtil.sign(orderInfo,alipayOrderInfo.getRsa_private_key());
				try {
					// 仅需对sign 做URL编码
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// 完整的符合支付宝参数规范的订单信息
				mAlipayPayInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ PayUtil.getSignType();		
		
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	Runnable payRunnable = new Runnable() {

		@Override
		public void run() {
			// 构造PayTask 对象
			PayTask alipay = new PayTask(OrderPayActivity.this);
			// 调用支付接口，获取支付结果
			String result = alipay.pay(mAlipayPayInfo);
			Message msg = new Message();
			msg.what = SDK_PAY_FLAG;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}
	};
	
	/**
	 * 支付-银联
	 */
	private void payBank(BankPayInfo bankPayInfo) throws UnsupportedEncodingException{
		String orderInfo;
		orderInfo = PayUtil.getBankOrderInfo(bankPayInfo.getArgs());
		Bundle bundle = new Bundle();
		bundle.putString(AppConstants.PARAM_WEBVIEW_DATA, orderInfo);
		bundle.putString(AppConstants.PARAM_WEBWIEW_URL, bankPayInfo.getUrl());
		LogUtil.e("PARAM_WEBVIEW_DATA", orderInfo);
		ActivityUtil.next(OrderPayActivity.this, WebActivity.class,bundle,false);

	}
	
	private void choicePayTaype(String payData){
		if(mPayType.equals(PAY_TYPE_ALIPAY)){
			AlipayPayInfo alipayPayInfo =  JSON.parseObject(payData, AlipayPayInfo.class);
			payAlipay(alipayPayInfo);
		}else if(mPayType.equals(PAY_TYPE_POS)){
			Bundle bundle = new Bundle();
        	bundle.putString(AppConstants.PARAM_PAY_MONEY,mPayMoney); //测试
        	bundle.putString(AppConstants.PARAM_ORDER_ID, mOrderId);
        	bundle.putString(AppConstants.PARAM_FINANCE_ID, mFinanceId);
    		ActivityUtil.next(this, OrderPayByPosActivity.class,bundle,false);
		}else if(mPayType.equals(PAY_TYPE_BANK)){
			BankPayInfo bankPayInfo = JSON.parseObject(payData, BankPayInfo.class);
			try {
				payBank(bankPayInfo);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 初始化-广播
	 */
	private void initClosePageBroadcastReceiver(){
		IntentFilter intentFilter = new IntentFilter();
		ClosePageBroadcastReceiver mHomeBroadcastReceiver = new ClosePageBroadcastReceiver();
		intentFilter.addAction(BroadCastManager.ACTION_CLOSE_PAY_PAGE);//关闭支付
		LocalBroadcastManager.getInstance(OrderPayActivity.this).registerReceiver(mHomeBroadcastReceiver, intentFilter);

	}
	public class ClosePageBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(BroadCastManager.ACTION_CLOSE_PAY_PAGE)){  //刷新个人信息广播
				OrderPayActivity.this.finish();
			}
		}
		
	}	
}