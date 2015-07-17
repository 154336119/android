package com.huizhuang.zxsq.ui.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.common.HouseType;
import com.huizhuang.zxsq.http.bean.common.Housing;
import com.huizhuang.zxsq.http.bean.common.UploadImageResult;
import com.huizhuang.zxsq.http.task.common.UploadImageTask;
import com.huizhuang.zxsq.http.task.order.OrderEditInfoTask;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.ui.activity.SelectHousingActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.CommonEditTextArrowsView;
import com.huizhuang.zxsq.widget.CommonTextViewArrowsView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author th
 * @ClassName: OrderAddEditorInfoActivity
 * @Description: 下单编辑页
 * @mail 342592622@qq.com
 * @date 2015-1-20 上午9:08:54
 */
public class OrderEditorInfoActivity extends BaseActivity implements OnClickListener {

    public static final String TAG = OrderEditorInfoActivity.class.getSimpleName();

    public static final int PARAM_TYPE_COMPANY = 0;
    public static final int PARAM_TYPE_DESIGNER = 1;
    public static final int PARAM_TYPE_FOREMAN = 2;
    public static final int PARAM_TYPE_OTHER = 3;

    public final static int REQ_CODE_SELECT_HOUSING = 300;
    public final static int REQ_CODE_SELECT_DATE = 301;
    public final static int REQ_CODE_SELECT_REMARK = 302;

    private CommonActionBar mCommonActionBar;

    private TextView mMyLocation;
    private CommonEditTextArrowsView mCvName;
    private CommonTextViewArrowsView mCvHousing;
    private CommonTextViewArrowsView mCvAppointmentTime;
    private CommonTextViewArrowsView mCvRemark;
    private CheckBox mCheckBox;


    private int mParamType;
    private int mParamDesignerId;
    private int mParamCompanyId;
    private int mOrderId;
    private String mPhone;

    private Housing mHousing;
    private List<HouseType> mHouseListType;
    private String mSelectDate;
    private boolean mIsAppointment = true;
    private String mRemark;
    private ArrayList<String> mImageList;
    private StringBuffer mImagesIds;

    private int mUploadIndex = 0;
    private int mFailureNums = 0;

    private CommonAlertDialog mCommonAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_editor_info);
        getIntentExtra();
        initActionBar();
        initViews();
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
        mCommonActionBar.setActionBarTitle("免费找我施工");
//        mCommonActionBar.setActionBarTitle((mParamType != PARAM_TYPE_FOREMAN ? "预约设计师上门设计" : "预约工长上门量房"));
        mCommonActionBar.setLeftImgBtn(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsUtil.onEvent(OrderEditorInfoActivity.this, "click34");
                showBackDialog();
            }
        });
    }

    private void initViews() {
        mMyLocation = (TextView) findViewById(R.id.tv_my_location);
        if(ZxsqApplication.getInstance().getLocationCity() != null && ZxsqApplication.getInstance().getLocationDistrict() != null){
            mMyLocation.setText(ZxsqApplication.getInstance().getLocationCity() + ZxsqApplication.getInstance().getLocationDistrict());
        }else{
            mMyLocation.setVisibility(View.GONE);
        }

        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mCheckBox.setChecked(true);
        if (mParamType != PARAM_TYPE_FOREMAN){
            mCheckBox.setVisibility(View.GONE);
        }
        mCvName = (CommonEditTextArrowsView) findViewById(R.id.cv_name);
        mCvHousing = (CommonTextViewArrowsView) findViewById(R.id.cv_housing);
        mCvAppointmentTime = (CommonTextViewArrowsView) findViewById(R.id.cv_appointment_time);
        mCvRemark = (CommonTextViewArrowsView) findViewById(R.id.cv_remark);

        mCvName.setTitle("称呼*");
        mCvName.setInfoHint("请填写您的称呼");
        mCvName.setHideArrowImg();
        mCvHousing.setTitle("小区*");
        mCvHousing.setInfoHint("请填写您的小区名称");
        mCvHousing.setOnClickListener(this);
        mCvAppointmentTime.setTitle("添加量房时间*");
        mCvAppointmentTime.setOnClickListener(this);
        mCvRemark.setTitle("备注");
        mCvRemark.setOnClickListener(this);

        findViewById(R.id.btn_next).setOnClickListener(this);
    }

    private void submit() {
        String name = mCvName.getInfo();
        if (TextUtils.isEmpty(name)) {
            showToastMsg("请填写您的称呼");
        } else if (mHousing == null) {
            showToastMsg("请填写您的小区");
        } else if (mIsAppointment && TextUtils.isEmpty(mSelectDate)) {
            showToastMsg("请添加您的量房时间");
        } else {
            mFailureNums = 0;
            httpRequestUploadImage();
        }
    }


    private void httpRequestUploadImage() {
        if (mImageList == null || mUploadIndex == mImageList.size()) {
            httpRequestSubmit();
            return;
        }

        if (mFailureNums == 3) {
            hideWaitDialog();
            showToastMsg("图片上传失败，请检测网络连接");
            return;
        }

        File file = new File(mImageList.get(mUploadIndex));
        UploadImageTask task = new UploadImageTask(this, HttpClientApi.UploadImageType.comment.toString(), file);
        task.setCallBack(new AbstractHttpResponseHandler<UploadImageResult>() {

            @Override
            public void onStart() {
                super.onStart();
                if (mUploadIndex == 0) {
                    showWaitDialog("");
                }
            }

            @Override
            public void onSuccess(UploadImageResult uploadImageResults) {
                if (mImagesIds == null) {
                    mImagesIds = new StringBuffer();
                }
                
                mImagesIds.append(uploadImageResults.getId());
                mImagesIds.append(",");
                mUploadIndex++;
                httpRequestUploadImage();
            }

            @Override
            public void onFailure(int code, String error) {
                mFailureNums++;
                httpRequestUploadImage();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
        task.send();
    }

    /**
     * 下一步
     */
    private void httpRequestSubmit() {
        String name = mCvName.getInfo();
        int neddMeasurement = 0;
        if(!mIsAppointment){
        	neddMeasurement = 0;
        }else{
        	neddMeasurement = 1;
        }
        String imageUrls = "";
        if(mImagesIds != null){
        	imageUrls = mImagesIds.toString();
        }
        int is_add_other_store = mCheckBox.isChecked() ? 1: 0;
        int orderType = mParamType == PARAM_TYPE_FOREMAN ? 20 : 19;
        OrderEditInfoTask task = new OrderEditInfoTask(this, name, mPhone, mHousing.getId()+"", mHousing.getName(), mSelectDate, orderType, mParamCompanyId,null, mRemark, imageUrls,neddMeasurement,mOrderId,is_add_other_store);
        task.setCallBack(new AbstractHttpResponseHandler<Result>() {

            @Override
            public void onStart() {
                super.onStart();
                showWaitDialog("");
            }

            @Override
            public void onSuccess(Result result) {
                ActivityUtil.next(OrderEditorInfoActivity.this, OrderSuccessActivity.class, true);
            }

            @Override
            public void onFailure(int code, String error) {
                showToastMsg(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        });
        task.send();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_housing:
                Intent intent = new Intent(OrderEditorInfoActivity.this, SelectHousingActivity.class);
                startActivityForResult(intent, REQ_CODE_SELECT_HOUSING);
                break;
            case R.id.cv_appointment_time:
                AnalyticsUtil.onEvent(OrderEditorInfoActivity.this, "click35");
                Bundle bd = new Bundle();
                bd.putInt(AppConstants.PARAM_ORDER_TYPE, mParamType);
                ActivityUtil.next(OrderEditorInfoActivity.this, OrderAppointmentTimeActivity.class, bd, REQ_CODE_SELECT_DATE);
                break;
            case R.id.cv_remark:
                AnalyticsUtil.onEvent(OrderEditorInfoActivity.this, "click36");
                Bundle bd2 = new Bundle();
                bd2.putInt(AppConstants.PARAM_ORDER_TYPE, mParamType);
                bd2.putString(OrderEditRemarkActivity.PARAM_REMARK, mCvRemark.getInfo());
                if(mImageList != null){
                	bd2.putStringArrayList(OrderEditRemarkActivity.PARAM_IMAGES, mImageList);
                }
                ActivityUtil.next(OrderEditorInfoActivity.this, OrderEditRemarkActivity.class, bd2, REQ_CODE_SELECT_REMARK);
                break;
            case R.id.btn_next:
                AnalyticsUtil.onEvent(OrderEditorInfoActivity.this, "click37");
                submit();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_CODE_SELECT_HOUSING == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                mHousing = (Housing) data.getSerializableExtra(SelectHousingActivity.PARAM_HOUSING);
                mHouseListType = data.getParcelableArrayListExtra(SelectHousingActivity.PARAM_HOUSETYPE_LIST);
                mCvHousing.setInfo(mHousing.getName());
            }
        } else if (REQ_CODE_SELECT_DATE == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                mSelectDate = data.getStringExtra(OrderAppointmentTimeActivity.PARAM_SELECT_DATE);
                mIsAppointment = data.getBooleanExtra(OrderAppointmentTimeActivity.PARAM_SELECT_NONE, true);
                if (!mIsAppointment) {
                    mCvAppointmentTime.setInfo("暂不需要量房");
                } else {
                    mCvAppointmentTime.setInfo(mSelectDate);
                }
            }
        } else if (REQ_CODE_SELECT_REMARK == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                mRemark = data.getStringExtra(OrderEditRemarkActivity.PARAM_REMARK);
                mImageList = data.getStringArrayListExtra(OrderEditRemarkActivity.PARAM_IMAGES);
                mCvRemark.setInfo(mRemark);
            }
        }
    }

    private void showBackDialog(){
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

}
