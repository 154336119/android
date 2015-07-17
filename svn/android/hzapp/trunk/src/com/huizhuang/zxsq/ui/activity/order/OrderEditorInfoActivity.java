package com.huizhuang.zxsq.ui.activity.order;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.bean.common.HouseType;
import com.huizhuang.zxsq.http.bean.common.Housing;
import com.huizhuang.zxsq.http.task.order.OrderSubmitTask;
import com.huizhuang.zxsq.ui.activity.HomeActivity;
import com.huizhuang.zxsq.ui.activity.SelectHousingActivity;
import com.huizhuang.zxsq.ui.activity.HomeActivity.MyOnGetGeoCoderResultListener;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.company.CompanyCityList;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;
import com.huizhuang.zxsq.widget.wheel.CommonDatePickerWheelPanel;

/**
 * @author th
 * @ClassName: OrderAddEditorInfoActivity
 * @Description: 下单编辑页
 * @mail 342592622@qq.com
 * @date 2015-1-20 上午9:08:54
 */
public class OrderEditorInfoActivity extends BaseActivity implements OnClickListener {

    public static final String TAG = OrderEditorInfoActivity.class.getSimpleName();
    private static final int CITY_LIST = 33545;
    public static final int PARAM_TYPE_COMPANY = 0;
    public static final int PARAM_TYPE_DESIGNER = 1;
    public static final int PARAM_TYPE_FOREMAN = 2;
    public static final int PARAM_TYPE_OTHER = 3;
    private static final int DECIMAL_DIGITS = 2; // 保留两位小数
    private static final int DIGITS = 2; // 保留3位整数
    // 地理编码
    private GeoCoder mGeoCoder;
    public final static int REQ_CODE_SELECT_HOUSING = 300;
    private CommonActionBar mCommonActionBar;
    private String mCityId = "2291";
    private String mAddr;
    private String mAera;
    private String mSelectDate;
    private String mhouseName;
    private String mSourceName = "";
    private String mPhone;
    private String mPx;
    private String mPy;
    private String mForemanId = "";
    private String mDistrict = "";
    private String mProvinceName;
    private String mCityName;

    private TextView mTvHousing;
    private TextView mTvAppointmentTime;
    private TextView mTvCity;
    private EditText mEdArea;
    private Button mBtnNext;
    private Housing mHousing;
    private TextView mTvDes;

    private List<HouseType> mHouseListType;
    private boolean mIsAppointment = true;

    private CommonAlertDialog mCommonAlertDialog;
    private CommonDatePickerWheelPanel mWheelSeletDatePanle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_editor_info);
        getIntentExtra();
        initActionBar();
        initViews();
        initGeocoder();
    }

    private void getIntentExtra() {
        if (getIntent().getStringExtra(AppConstants.PARAM_ORDER_SOURCE_NAME) != null)
            mSourceName = getIntent().getStringExtra(AppConstants.PARAM_ORDER_SOURCE_NAME);
        if (getIntent().getStringExtra(AppConstants.PARAM_ORDER_COMPANY_ID) != null)
            mForemanId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_COMPANY_ID);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle(getResources().getString(R.string.txt_none_money_apply));
        mCommonActionBar.setLeftImgBtn(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0007);
                finish();
                // showBackDialog();
            }
        });
    }

    private void initViews() {
        mEdArea = (EditText) findViewById(R.id.ed_area);
        mTvCity = (TextView) findViewById(R.id.tv_city);
        mTvHousing = (TextView) findViewById(R.id.tv_housing);
        mTvAppointmentTime = (TextView) findViewById(R.id.tv_appointment_time);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        mTvDes = (TextView) findViewById(R.id.tv_des);
        findViewById(R.id.tv_housing).setOnClickListener(this);
        findViewById(R.id.tv_city).setOnClickListener(this);
        findViewById(R.id.tv_appointment_time).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);

        if (ZxsqApplication.getInstance().getSelectCity() != null) {
            mTvCity.setText(ZxsqApplication.getInstance().getSelectCity());
        } else if (ZxsqApplication.getInstance().getLocationCity() != null) {
            mTvCity.setText(ZxsqApplication.getInstance().getLocationCity());
            LogUtil.e("==========getLocationCity" + ZxsqApplication.getInstance().getLocationCity());
        }
        mEdArea.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("mTvHousing.getText().toString():"
                        + mTvHousing.getText().toString());
                if (mEdArea.getText().length() > 0
                        && !TextUtils.isEmpty(mTvHousing.getText().toString())
                        && !TextUtils.isEmpty(mTvAppointmentTime.getText().toString())) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                }
            }
        });
        mEdArea.setFilters(new InputFilter[] {lengthfilter});
        mTvDes.setText(Html
                .fromHtml("1.资深工长<font color='#ff6c38'>免费上门量房报价</font>，比装修公司省<font color='#ff6c38'>40%</font>;<br/>2.在线查看工程进度，全程36个质量节点监管;<br/>3.分阶段支付装修款，装修<font color='#ff6c38'>满意再付款</font>;<br/>4.<font color='#ff6c38'>工地5年质保</font>，投诉5分钟内响应，100%解决。"));
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_housing:
                Intent intent =
                        new Intent(OrderEditorInfoActivity.this, OrderChoiceHouseForMap.class);
                intent.putExtra(AppConstants.PARAM_CITY, mTvCity.getText().toString());
                startActivityForResult(intent, REQ_CODE_SELECT_HOUSING);
                break;
            case R.id.tv_appointment_time:
                LogUtil.e("cv_appointment_time:onclick");
                showSelectItemDialog();
                break;
            case R.id.btn_next:
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0009);

                // if(ZxsqApplication.getInstance().getUser()!=null&&ZxsqApplication.getInstance().getUser().getToken()!=null&&ZxsqApplication.getInstance().getUser().getUser_id()!=null){
                // if(DateUtil.strToDate(mSelectDate, "yyyy-MM-dd").before(new Date()) &&
                // !DateUtil.getStringDateShort().equals(mSelectDate)){
                // showToastMsg("量房时间必须大于当前时间");
                // }else{
                // httpOrderSubmit();
                // }
                // }else{
                mAera = mEdArea.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.PARAM_CITY_ID, mCityId);
                bundle.putString(AppConstants.PARAM_ARRD, mHousing.getDistrict());
                bundle.putString(AppConstants.PARAM_AREA, mAera);
                bundle.putString(AppConstants.PARAM_MEASURE_TIME, mSelectDate);
                bundle.putString(AppConstants.PARAM_ORDER_SOURCE_NAME, mSourceName);
                bundle.putString(AppConstants.PARAM_HOUSE_NAME, mHousing.getName());
                bundle.putString(AppConstants.PARAM_LNG, mHousing.getLon());
                bundle.putString(AppConstants.PARAM_LAT, mHousing.getLat());
                bundle.putString(AppConstants.PARAM_ORDER_COMPANY_ID, mForemanId);
                bundle.putString(AppConstants.PARAM_PRO, mProvinceName);
                bundle.putString(AppConstants.PARAM_CITY, mCityName);
                bundle.putString(AppConstants.PARAM_DISTRICT, mDistrict);
                ActivityUtil.next(OrderEditorInfoActivity.this,
                        OrderVerifyPhoneNumerActivity.class, bundle, false);
                // }
                break;
            case R.id.tv_city:
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0008);
                toCityList();
                break;
            default:
                break;
        }
    }

    private void showSelectItemDialog() {
        if (null == mWheelSeletDatePanle) {
            mWheelSeletDatePanle = new CommonDatePickerWheelPanel(this);
            Calendar calendar = Calendar.getInstance();
            mWheelSeletDatePanle.setStartYear(calendar.get(Calendar.YEAR));
            mWheelSeletDatePanle.setEnsureClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mSelectDate =
                            DateUtil.format(mWheelSeletDatePanle.getDate(), "yyyy年MM月dd日",
                                    "yyyy-MM-dd");
                    long selectDateTimestamp =
                            Long.decode(DateUtil.sdateToTimestamp1(mWheelSeletDatePanle.getDate()));
                    long nextDayTimestamp =
                            Long.decode(DateUtil.sdateToTimestamp2(DateUtil.getNextDay(
                                    DateUtil.getStringDateShort(), "yyyy-MM-dd", 1)));
                    if (selectDateTimestamp < nextDayTimestamp) {
                        showToastMsg("请选择明天及以后的日期");
                    } else {
                        String nowTime = DateUtil.getStringDateShort();
                        String measurementTime = mWheelSeletDatePanle.getDate();
                        if (mSelectDate.equals(nowTime)) {
                            measurementTime = "今天" + measurementTime;
                        } else if (mSelectDate.equals(DateUtil.getNextDay(nowTime, "yyyy-MM-dd", 1))) {
                            measurementTime = "明天" + measurementTime;
                        }
                        mTvAppointmentTime.setText(measurementTime);
                        String s = mEdArea.getText().toString();
                        String ss = mTvHousing.getText().toString();
                        if (!TextUtils.isEmpty(mTvHousing.getText().toString())
                                && !TextUtils.isEmpty(mEdArea.getText().toString())) {
                            mBtnNext.setEnabled(true);
                        }
                        mWheelSeletDatePanle.dismissDialog();
                    }
                }
            });
        }
        Calendar calendar = Calendar.getInstance();
        String date = DateUtil.getNextDay(DateUtil.getStringDateShort(), "yyyy-MM-dd", 1);
        String[] sourceStrArray = date.split("-");

        mWheelSeletDatePanle.setTitle("量房时间");
        // mWheelSeletDatePanle.setmIsStartNowDay(true);
        mWheelSeletDatePanle.initDateTimePicker(Integer.parseInt(sourceStrArray[0]),
                Integer.parseInt(sourceStrArray[1]) - 1, Integer.parseInt(sourceStrArray[2]));
        // mWheelSeletDatePanle.initDateTimePicker(year, month, day);

        mWheelSeletDatePanle.showDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_CODE_SELECT_HOUSING == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                mHousing = (Housing) data.getSerializableExtra(SelectHousingActivity.PARAM_HOUSING);
                mHouseListType =
                        data.getParcelableArrayListExtra(SelectHousingActivity.PARAM_HOUSETYPE_LIST);
                mTvHousing.setText(mHousing.getName());

                if (mEdArea.getText().toString() != null
                        && !TextUtils.isEmpty(mTvAppointmentTime.getText().toString())) {
                    mBtnNext.setEnabled(true);
                }
                showWaitDialog("加载中...");
                LogUtil.e("mHousing:" + mHousing.toString());
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(Double
                        .valueOf(mHousing.getLat()), Double.valueOf(mHousing.getLon()))));

            }
        } else if (CITY_LIST == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                String city = data.getStringExtra("city");
                mCityId = data.getStringExtra(AppConstants.PARAM_CITY_ID);
                System.out.println("======mCityId:" + mCityId);
                mTvCity.setText(city);
            }

        }
    }

    private void showBackDialog() {
        if (null == mCommonAlertDialog) {
            mCommonAlertDialog = new CommonAlertDialog(this);
            mCommonAlertDialog.setMessage("完善信息将更方便我们与您沟通哦，确认要退出吗？");
            mCommonAlertDialog.setPositiveButton(R.string.txt_quit, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommonAlertDialog.dismiss();
                    OrderEditorInfoActivity.this.finish();

                }
            });
            mCommonAlertDialog.setNegativeButton(R.string.txt_cancel, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommonAlertDialog.dismiss();
                }
            });
        }
        mCommonAlertDialog.show();
    }

    /**
     * 跳转到城市列表
     */
    private void toCityList() {
        String city = ZxsqApplication.getInstance().getLocationCity();
        if (city != null) {
            city = city.replace("市", "");
        }
        Intent intent = new Intent(this, CompanyCityList.class);
        intent.putExtra("myCity", city);
        startActivityForResult(intent, CITY_LIST);
    }

    /**
     * 初始化-地理编码（地理名称转化经纬度）
     */
    private void initGeocoder() {
        // TODO Auto-generated method stub
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new MyOnGetGeoCoderResultListener());
    }

    /**
     * 接口-地理编码监听器
     * 
     */
    public class MyOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult arg0) {}

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
            mDistrict = arg0.getAddressDetail().district;
            mProvinceName = arg0.getAddressDetail().province;
            mCityName = arg0.getAddressDetail().city;
            hideWaitDialog();
        }

    }

    // 两位小数控制器
    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

}
