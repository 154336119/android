	package com.huizhuang.zxsq.ui.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqActivityManager;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.http.bean.account.EnsureTrade;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.task.account.GetEnSureTradeTask;
import com.huizhuang.zxsq.http.task.order.GetOrderTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.AllOrderAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.OrderProgressStateBar.ProgressSate;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.List;

/**
 * @author Jean
 * @ClassName: EnsuredTradeActivity
 * @Description: 担保交易
 * @mail 154336119@qq.com
 * @date 2015-1-29 上午10:34:22
 */
public class EnsuredTradeActivity extends BaseActivity implements OnCheckedChangeListener{
	   /**
     * 调试代码TAG
     */
	
    protected static final String TAG = TradeSuccessActivity.class.getSimpleName();
    public static final int ALIPAY = 1;
    public static final int WECHAT = 2;
    public static final int BANK = 3;
    public static final int POS = 4;
    
    private CommonActionBar mCommonActionBar;

    private DataLoadingLayout mDataLoadingLayout;
    private TextView mTvOrderNum;
    private TextView mTvOrderTime;
    private TextView mTvPaymentNode;
    private TextView mTvOrderMoney;
    private TextView mTEscrowDeposit;
    private TextView mTvUseCoupons;
    private TextView mTvNeedToPay;
    private CheckBox mCbRead;
    private TextView mTvRead;
    private Button mBtnConfirm;
    private CheckBox mCbAlipay;
    private CheckBox mCbWechat;
    private CheckBox mCbBank;
    private CheckBox mCbPos;
    private String mOrderId;
    private EnsureTrade mEnsureTrade;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encured_transactions);
        getIntentExtras();
        initActionBar();
        initView();
        httpRequestEnsureTrade();
        
    }

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.txt_title_transaction_security);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void getIntentExtras() {
		LogUtil.d("getIntentExtras");

		Intent intent = getIntent();
		if (null != intent) {
			mOrderId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
		}
	}
    private void initView() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        mTvOrderNum = (TextView)findViewById(R.id.tv_order_num);
        mTvOrderTime = (TextView)findViewById(R.id.tv_order_time);
        mTvPaymentNode = (TextView)findViewById(R.id.tv_payment_node);
        mTvOrderMoney = (TextView)findViewById(R.id.tv_order_money);
        mTEscrowDeposit = (TextView)findViewById(R.id.tv_escrow_deposit);
        mTvUseCoupons = (TextView)findViewById(R.id.tv_use_coupons);
        mTvNeedToPay = (TextView)findViewById(R.id.tv_need_to_pay);
        mCbRead = (CheckBox)findViewById(R.id.cb_read);
        mTvRead = (TextView)findViewById(R.id.tv_read);
        mTvRead.setText(Html.fromHtml("<font color='#808080'>"+"我已阅读并同意"+"</font>"+"<font color='#ff6d39'>"+"《惠装支付协议》"+"</font>"));
        mBtnConfirm = (Button)findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putSerializable(AppConstants.PARAM_ENSURE_TRADE, mEnsureTrade);
				if(mCbPos.isChecked()){
					bundle.putInt(AppConstants.PARAM_PAY_WAY, POS);
					ActivityUtil.next(EnsuredTradeActivity.this, TradeSuccessActivity.class,bundle,-1,true);
				}else{
					ActivityUtil.next(EnsuredTradeActivity.this, TradeSuccessActivity.class,bundle,-1,true);
				}
			}
		});
        
        mCbAlipay = (CheckBox)findViewById(R.id.cb_alipay);
        mCbWechat = (CheckBox)findViewById(R.id.cb_wechat);
        mCbBank = (CheckBox)findViewById(R.id.cb_bank);
        mCbPos = (CheckBox)findViewById(R.id.cb_pos);
        
        mCbAlipay.setOnCheckedChangeListener(this);
        mCbWechat.setOnCheckedChangeListener(this);
        mCbBank.setOnCheckedChangeListener(this);
        mCbPos.setOnCheckedChangeListener(this);
    }

    
    private void updataEnsureTradeInfo(){
    	mTvOrderNum.setText(mEnsureTrade.getOrder_no());
    	mTvOrderTime.setText(DateUtil.timestampToSdate(mEnsureTrade.getAdd_time(), "yyyy年MM月dd日"));
    	mTvPaymentNode.setText(getValue(mEnsureTrade.getPay_node(),ZxsqApplication.getInstance().getConstant().getJlNodes()));
    	mTvOrderMoney.setText("￥"+mEnsureTrade.getContract_amount());
    	mTEscrowDeposit.setText("￥"+mEnsureTrade.getPay_amount());
    	mTvNeedToPay.setText("￥"+mEnsureTrade.getDiscounted_amount());
    }

    private void httpRequestEnsureTrade(){
    	GetEnSureTradeTask getEnSureTradeTask = new GetEnSureTradeTask(this, mOrderId);
    	getEnSureTradeTask.setCallBack(new AbstractHttpResponseHandler<EnsureTrade>() {
			
			@Override
			public void onSuccess(EnsureTrade ensureTrade) {
				// TODO Auto-generated method stub
				mDataLoadingLayout.showDataLoadSuccess();
				mEnsureTrade = ensureTrade;
				updataEnsureTradeInfo();
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				mDataLoadingLayout.showDataLoadFailed(error);
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				mDataLoadingLayout.showDataLoading();
			}
		});
    	getEnSureTradeTask.send();
    }
    /**
     * 设置付款方式
     * 
     * @param progressSate
     */
    private void setCheckBoxState(View view) {
        switch (view.getId()) {
        case R.id.cb_alipay:
        	mCbAlipay.setChecked(true);
            mCbWechat.setChecked(false);
            mCbBank.setChecked(false);
            mCbPos.setChecked(false);
            break;
        case R.id.cb_wechat:
        	mCbAlipay.setChecked(false);
            mCbWechat.setChecked(true);
            mCbBank.setChecked(false);
            mCbPos.setChecked(false);
            break;
        case R.id.cb_bank:
        	mCbAlipay.setChecked(false);
            mCbWechat.setChecked(false);
            mCbBank.setChecked(true);
            mCbPos.setChecked(false);
            break;
        case R.id.cb_pos:
        	mCbAlipay.setChecked(false);
            mCbWechat.setChecked(false);
            mCbBank.setChecked(false);
            mCbPos.setChecked(true);
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
	
    private String getStatus(int status){
    	if(status == 0){
    		return "已取消";
		}else if(status == 1){
			return "待领取";
		}else if(status == 2){
			return "已量房";
		}else if(status == 3){
			return "已设计报价";
		}else if(status == 4){
			return "已签合同";
		}
    	return "已取消";
    }
    
	private String getValue(String str, List<KeyValue> type) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		int length = type.size();
		String name = null;
		for (int i = 0; i < length; i++) {
			KeyValue keyValue = type.get(i);
			if (keyValue.getId().equals(str)) {
				name = keyValue.getName();
				break;
			}
		}
		return name;
	}
}
