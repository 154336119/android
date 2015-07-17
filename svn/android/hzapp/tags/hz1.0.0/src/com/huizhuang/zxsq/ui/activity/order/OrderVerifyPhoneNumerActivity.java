package com.huizhuang.zxsq.ui.activity.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.module.parser.UserParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;


/** 
* @ClassName: OrderVerifyPhoneNumerActivity
* @Description: 未注册手机号下单验证页 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-13 下午7:09:23 
*  
*/
public class OrderVerifyPhoneNumerActivity extends BaseActivity implements OnClickListener {

    private final int DELAYED_TIME = 1000;
    private final int WATING_TIME = 60;
    private CommonActionBar mCommonActionBar;

    private Button mBtnGetCode;
    private TextView mDes;
    private EditText mEtCode;
    private int mTimes;
    private int mParamType;
    private int mParamDesignerId;
    private int mParamCompanyId;
    private int mOrderId;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_vertify_phone_number);
        getIntentExtra();
        initActionBar();
        initViews();
        mTimes = WATING_TIME;
        httpRequestGetCheckCode();
    }

    private void getIntentExtra() {
        mParamType = getIntent().getIntExtra(AppConstants.PARAM_ORDER_TYPE, 0);
        mParamDesignerId = getIntent().getIntExtra(AppConstants.PARAM_ORDER_DESIGNER_ID, 0);
        mParamCompanyId = getIntent().getIntExtra(AppConstants.PARAM_ORDER_COMPANY_ID, 0);
        mOrderId = getIntent().getIntExtra(AppConstants.PARAM_ORDER_ID, 0);
        mPhone = getIntent().getStringExtra(AppConstants.PARAM_PHONE);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("输入验证码");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                AnalyticsUtil.onEvent(OrderVerifyPhoneNumerActivity.this, "click31");
                finish();
            }
        });
    }

    private void initViews() {
        findViewById(R.id.btn_code).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        mDes = (TextView) findViewById(R.id.tv_des);
        mEtCode = (EditText) findViewById(R.id.et_code);
        mBtnGetCode = (Button) findViewById(R.id.btn_code);
        String phone = "";
        for (int i = 0; i < mPhone.toCharArray().length; i++) {
			if(i>2 && i < 7){
				phone +="*";
			}else{
				phone += mPhone.toCharArray()[i];
			}
		}
        mDes.setText("验证码短信已发送至手机"+phone);
    }

    /**
     * 验证
     */
    private void httpRequestCheck() {
        String code = mEtCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            showToastMsg("请输入验证码");
        } else if (code.length() < 4) {
            showToastMsg("验证码不能小于4位");
        } else {
            hideSoftInput();
            showWaitDialog("请稍候...");
            RequestParams params = new RequestParams();
            params.put("mobile", mPhone);
            params.put("sms_code", code);
            params.put("order_id", mOrderId);
            HttpClientApi.post(HttpClientApi.URL_ORDER_SEND_CHECK_CODE_UNLOGIN, params, new UserParser(), new RequestCallBack<User>() {

                @Override
                public void onSuccess(User user) {
                    showToastMsg("验证成功");
                    Bundle bd = getIntent().getExtras();
                    LogUtil.e("order_type:" + bd.getInt(AppConstants.PARAM_ORDER_TYPE));
                    if(!ZxsqApplication.getInstance().isLogged()){
            			ZxsqApplication.getInstance().setUser(user,true);
                    }
                    ActivityUtil.next(OrderVerifyPhoneNumerActivity.this, OrderEditorInfoActivity.class, bd, -1, true);
                }

                @Override
                public void onFailure(NetroidError error) {
                    showToastMsg(error.getMessage());
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    hideWaitDialog();
                }
            });
        }
    }

    private void httpRequestGetCheckCode() {
        showWaitDialog("请求中...");
        RequestParams params = new RequestParams();
        params.put("mobile", mPhone);
        HttpClientApi.post(HttpClientApi.URL_ORDER_SEND_VIREFY_CODE, params, new ResultParser(), new RequestCallBack<Result>() {

            @Override
            public void onSuccess(Result responseInfo) {
                showToastMsg("验证码已发送，请注意查收！");
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                mHandler.sendMessage(msg);
                updateTimes();
            }

            @Override
            public void onFailure(NetroidError error) {
                showToastMsg(error.getMessage());
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessageDelayed(message, DELAYED_TIME);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        });
    }

    private void updateTimes() {
        Message msg = mHandler.obtainMessage();
        if (mTimes <= 0) {
            msg.what = 1;
        } else {
            msg.what = 0;
        }
        mHandler.sendMessageDelayed(msg, DELAYED_TIME);
    }

    @SuppressLint("HandlerLeak")
    protected Handler mHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 0:
                    mTimes--;
                    mBtnGetCode.setText(mTimes + "秒重新获取");
                    updateTimes();
                    break;
                case 1:
                    mBtnGetCode.setText("发送验证码");
                    mBtnGetCode.setClickable(true);
                    mBtnGetCode.setTextColor(getResources().getColor(R.color.orange_drak));
                    break;
                case 2:
                    mBtnGetCode.setClickable(false);
                    mBtnGetCode.setTextColor(getResources().getColor(R.color.color_cccccc));
                    break;
                case 3:

                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_code:
                AnalyticsUtil.onEvent(this, "click32");
                mTimes = WATING_TIME;
                httpRequestGetCheckCode();
                break;
            case R.id.btn_submit:
                AnalyticsUtil.onEvent(this, "click33");
                httpRequestCheck();
                break;
            default:
                break;
        }
    }

}