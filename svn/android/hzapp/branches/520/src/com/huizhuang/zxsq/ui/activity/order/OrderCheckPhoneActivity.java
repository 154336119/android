package com.huizhuang.zxsq.ui.activity.order;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.ProviceCityArea;
import com.huizhuang.zxsq.http.bean.Result;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.bean.order.Moblie;
import com.huizhuang.zxsq.http.parser.ResultParser;
import com.huizhuang.zxsq.http.task.CityDistrictTask;
import com.huizhuang.zxsq.http.task.order.OrderSubmitTask;
import com.huizhuang.zxsq.http.task.order.QueryMobileTask;
import com.huizhuang.zxsq.ui.activity.account.CityWheelMain;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

public class OrderCheckPhoneActivity extends BaseActivity implements OnClickListener {

    private CommonActionBar mCommonActionBar;
    private String mPro;
    private String mCity;
    private EditText mEtName;
    private EditText mEtPhone;
    private TextView mTvPlace;
    private CityWheelMain mCityWheelMain;
    private Button mBtnNext;
    private CommonAlertDialog mCommonAlertDialog;
//    private String mParamCaseId;
//    private int mParamCompanyId;
    private String mSourceName;
    private int mResult = 0;
    private String[] mOnes;
    private String[][] mTwos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_check_phone);
        getIntentExtra();
        initActionBar();
        initViews();
        List<ProviceCityArea> provinces = ZxsqApplication.getInstance().getCitys();
        if(provinces != null && provinces.size() > 0){
            getCityDate(provinces);
        }else{
            httpRequestAllCity();
        }
        if (ZxsqApplication.getInstance().isLogged()) {
            httpRequestGetMobile();
        }

    }

    private void getIntentExtra() {
        // mParamType = getIntent().getIntExtra(AppConstants.PARAM_ORDER_TYPE, 0);
        // mParamDesignerId = getIntent().getIntExtra(AppConstants.PARAM_ORDER_DESIGNER_ID, 0);
//        mParamCompanyId = getIntent().getIntExtra(AppConstants.PARAM_ORDER_COMPANY_ID, 0);
//        mParamCaseId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_CASE_ID);
        mSourceName = getIntent().getStringExtra(AppConstants.PARAM_ORDER_SOURCE_NAME);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.showRightTtile();
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        mBtnNext = (Button) findViewById(R.id.btn_next);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mTvPlace = (TextView) findViewById(R.id.tv_place);
        mPro = ZxsqApplication.getInstance().getmProvince();
        mCity = ZxsqApplication.getInstance().getLocationCity();
        LogUtil.e("pro:", mPro);
        LogUtil.e("city:", mCity);
        if (mPro != null && mCity != null && !TextUtils.isEmpty(mCity) && !TextUtils.isEmpty(mPro))
        {
            mPro = mPro.replace("省", "");
            mCity = mCity.replace("市", "");
            mTvPlace.setText(mPro + " " + mCity);
        }
        mTvPlace.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        if (ZxsqApplication.getInstance().isLogged()) {
            mEtPhone.setText(ZxsqApplication.getInstance().getUser().getPhone());
        }
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Util.isValidMobileNumber(mEtPhone.getText().toString())
                        && !TextUtils.isEmpty(mEtName.getText().toString())
                        && !TextUtils.isEmpty(mTvPlace.getText().toString())) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                    mBtnNext.setAlpha(50);
                }
            }
        });
        
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Util.isValidMobileNumber(mEtPhone.getText().toString())
                        && !TextUtils.isEmpty(mEtName.getText().toString())
                        && !TextUtils.isEmpty(mTvPlace.getText().toString())) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                    mBtnNext.setAlpha(50);
                }
            }
        });
    }

    /**
     * 获取手机号
     */
    private void httpRequestGetMobile() {
        QueryMobileTask task = new QueryMobileTask(this);
        task.setCallBack(new AbstractHttpResponseHandler<Moblie>() {

            @Override
            public void onStart() {
                super.onStart();
                showWaitDialog(getResources().getString(R.string.txt_on_waiting));
            }

            @Override
            public void onSuccess(Moblie result) {
                mEtPhone.setText(result.getMobile());
            }

            @Override
            public void onFailure(int code, String error) {}

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        });
        task.send();
    }

    /**
     * 获取城市列表
     */
    private void httpRequestAllCity() {
    	CityDistrictTask task = new CityDistrictTask(this);
        task.setCallBack(new AbstractHttpResponseHandler<List<ProviceCityArea>>() {

            @Override
            public void onStart() {
                super.onStart();
                showWaitDialog("加载中");
            }

            @Override
            public void onSuccess(List<ProviceCityArea> result) {
                getCityDate(result);
            }

            @Override
            public void onFailure(int code, String error) {}

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        });
        task.send();
    }

//
//    /**
//     * 下意向单
//     */
//    private void httpOrderSubmit() {
//        String name = mEtName.getText().toString();
//        OrderSubmitTask task =
//                new OrderSubmitTask(this, name,mEtPhone.getText().toString().trim(),mPro, mCity,mSourceName);
//        task.setCallBack(new AbstractHttpResponseHandler<Order>() {
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                showWaitDialog("加载中");
//            }
//
//            @Override
//            public void onSuccess(Order result) {
//                if(result != null){
////                    if(result.getArea_id() == 0){
////                        showNoneOpenCitytDialog();
////                    }else{
//                        Bundle bd = null;
//                        if (getIntent().getExtras() != null) {
//                            bd = getIntent().getExtras();
//                        } else {
//                            bd = new Bundle();
//                        }
//                        bd.putInt(AppConstants.PARAM_ORDER_ID, result.getOrder_id());
//                        bd.putString(AppConstants.PARAM_PHONE, mEtPhone.getText().toString());
//                        bd.putInt(OrderVerifyPhoneNumerActivity.PARAM_IS_LOGIN_KEY, mResult);
//                        ActivityUtil.next(OrderCheckPhoneActivity.this,
//                                OrderVerifyPhoneNumerActivity.class, bd, -1, true);
////                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int code, String error) {
//                
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideWaitDialog();
//            }
//        });
//        task.send();
//    }

    /**
     * 验证手机号是否注册
     */
    private void httpRequestCheckPhone() {
        showWaitDialog("请求中...");
        String phone = mEtPhone.getText().toString();
        RequestParams params = new RequestParams();
        params.put("mobile", phone);
        HttpClientApi.post(HttpClientApi.URL_ORDER_CHECK_PHONE, params, new ResultParser(),
                new RequestCallBack<Result>() {

                    @Override
                    public void onSuccess(Result responseInfo) {
                        try {
                            JSONObject data = new JSONObject(responseInfo.data);
                            int result = data.optInt("result");
                            mResult = result;
                           // httpOrderSubmit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (TextUtils.isEmpty(mEtName.getText().toString())) {
                    showToastMsg("请输入您的称呼");
                } else if (TextUtils.isEmpty(mEtPhone.getText().toString())) {
                    showToastMsg("请输入您的手机号码");
                } else if (TextUtils.isEmpty(mTvPlace.getText().toString())) {
                    showToastMsg("请选择城市");
                } else {
                    if (ZxsqApplication.getInstance().isLogged()// 如果用户已登录并且下单的手机号是自己的手机号，直接跳到完善信息
                            && !TextUtils.isEmpty(ZxsqApplication.getInstance().getUser()
                                    .getMobile())
                            && ZxsqApplication.getInstance().getUser().getMobile()
                                    .equals(mEtPhone.getText().toString().trim())) {
                       // httpOrderSubmit();
                    } else {
                        httpRequestCheckPhone();
                    }
                }
                break;
            case R.id.tv_place:
                showMomentDialog();
                break;
            default:
                break;
        }
    }

    // private void showAlertDialog() {
    // LogUtil.d("showExitAlertDialog");
    //
    // if (null == mCommonAlertDialog) {
    // mCommonAlertDialog = new CommonAlertDialog(this);
    // mCommonAlertDialog.setButtonTextColor(getResources().getColor(R.color.color_ff6c38));
    // mCommonAlertDialog.setTitle(R.string.txt_check_phone);
    // mCommonAlertDialog.setMessage("该手机号已被注册，验证手机号可直接登录");
    // mCommonAlertDialog.setPositiveButton(R.string.sent_verify, new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // mCommonAlertDialog.dismiss();
    // httpOrderSubmit();
    // }
    // });
    // mCommonAlertDialog.setNegativeButton(R.string.txt_cancel, new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // mCommonAlertDialog.dismiss();
    // }
    // });
    // }
    // mCommonAlertDialog.show();
    // }

    private void showMomentDialog() {
        final View pickerview = LayoutInflater.from(this).inflate(R.layout.zxbd_wheel, null, false);
        mCityWheelMain = new CityWheelMain(this, pickerview);
        if (mOnes != null && mOnes.length != 0) {
            mCityWheelMain.setProList(mOnes);
        }
        if (mTwos != null && mTwos.length != 0) {
            mCityWheelMain.setCityList(mTwos);
        }
        mCityWheelMain.init();
        final Dialog dialog = new Dialog(this, R.style.DialogBottomPop);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = UiUtil.getScreenWidth(this);
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        dialog.setContentView(pickerview);
        pickerview.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        pickerview.findViewById(R.id.btn_ensure).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPro = mCityWheelMain.getPro();
                mCity = mCityWheelMain.getCity();
                mTvPlace.setText(mPro + " " + mCity);
                if (Util.isValidMobileNumber(mEtPhone.getText().toString())
                        && !TextUtils.isEmpty(mEtName.getText().toString())
                        && !TextUtils.isEmpty(mTvPlace.getText().toString())) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                    mBtnNext.setAlpha(50);
                }
                dialog.cancel();
            }
        });

    }

    private void getCityDate(List<ProviceCityArea> provinces) {
        if(provinces != null && provinces.size() > 0){
            mOnes = new String[provinces.size()];
            mTwos = new String[provinces.size()][];
            for (int i = 0; i < provinces.size(); i++) {
                ProviceCityArea province = provinces.get(i);
                mOnes[i] = province.getArea_name();
                List<ProviceCityArea> citys = province.getCitys();
                if (citys != null && citys.size() > 0) {
                    mTwos[i] = new String[citys.size()];
                    for (int j = 0; j < citys.size(); j++) {
                        ProviceCityArea city = citys.get(j);
                        mTwos[i][j] = city.getArea_name();
                    }
                }
            }
        }
    }

    /**
     * 显示无开通城市提示对话框
     */
    private void showNoneOpenCitytDialog() {
        LogUtil.d("showNoneOpenCitytDialog");

        if (null == mCommonAlertDialog) {
            mCommonAlertDialog = new CommonAlertDialog(this);
            mCommonAlertDialog.setMessage(R.string.txt_none_open_city_alert);
            mCommonAlertDialog.setPositiveButton(R.string.txt_encourage_hz, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommonAlertDialog.dismiss();
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            mCommonAlertDialog.setNegativeButton(R.string.txt_left, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommonAlertDialog.dismiss();
                    finish();
                }
            });
        }
        mCommonAlertDialog.show();
    }
}
