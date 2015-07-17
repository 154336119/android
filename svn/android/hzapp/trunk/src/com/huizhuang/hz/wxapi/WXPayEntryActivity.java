package com.huizhuang.hz.wxapi;




import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderPayActivity;
import com.huizhuang.zxsq.ui.activity.supervision.WaitResponseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;
    
    private String mOrderId; 
    private String mNode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_data_editor);
        
    	api = WXAPIFactory.createWXAPI(this, AppConstants.WECHAT_ID);

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.about_company);
			builder.setMessage(getString(R.string.about_company, resp.errStr +";code=" + String.valueOf(resp.errCode)));
			builder.show();
		}

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode == 0){     //支付成功
				mOrderId = PrefersUtil.getInstance().getStrValue(AppConstants.PARAM_ORDER_ID);
				mNode = PrefersUtil.getInstance().getStrValue(AppConstants.PARAM_NODE_ID); 
				Bundle bundle = new Bundle();
            	bundle.putString(AppConstants.PARAM_ORDER_ID, mOrderId);
            	bundle.putString(AppConstants.PARAM_NODE_ID, String.valueOf(Integer.valueOf(mNode)+1));
            	if(String.valueOf(Integer.valueOf(mNode)+1).equals("5")){  //竣工阶段
            		ActivityUtil.nextActivityWithClearTop(WXPayEntryActivity.this, MyOrderActivity.class, bundle);
            	}else{
            		bundle.putBoolean(AppConstants.PARAM_IS_PAY, true);
            		ActivityUtil.next(WXPayEntryActivity.this, WaitResponseActivity.class,bundle,false);		
            	}	
            	finish();
			}else if(resp.errCode == -1){ //支付失败
				showToastMsg("支付失败");
				finish();
			}else if(resp.errCode == -2){ //支付取消
				showToastMsg("支付取消");
				finish();
			}
		}
	}
	}
