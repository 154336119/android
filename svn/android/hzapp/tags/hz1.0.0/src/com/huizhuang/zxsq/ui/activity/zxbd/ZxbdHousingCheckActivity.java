package com.huizhuang.zxsq.ui.activity.zxbd;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.ZxbdAnswer;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.wheel.CommonDatePickerWheelPanel;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

public class ZxbdHousingCheckActivity extends BaseActivity implements OnClickListener{

	private final int REQ_LOGIN_CODE = 661;
	
	private String mTitle;
	private ArrayList<ZxbdAnswer> mAnswerList;
	
	private CommonDatePickerWheelPanel mWheelSeletDatePanle;
	private Button mBtnSubmit;

	private TextView mBtnDatetime;
	private String mDatetime;
	private TextView mTxtData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zxbd_housing_check_activity);
		initExtraData();
		initActionBar();
		initViews();
	}
	
	private void initExtraData(){
		mTitle = getIntent().getExtras().getString("title"); 
		mAnswerList = getIntent().getExtras().getParcelableArrayList("answer");
	}
	
	private void initActionBar(){
		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(mTitle);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initViews(){
		mDatetime = DateUtil.getTimestamp();
		mBtnDatetime = (TextView) findViewById(R.id.tv_datetime);
		mTxtData = (TextView) findViewById(R.id.tv_content);
		mBtnDatetime.setOnClickListener(this);
		mBtnDatetime.setText(DateUtil.getStringDate("yyyy-MM-dd  HH:mm"));
		
		mBtnSubmit = (Button) findViewById(R.id.btn_submit);
		mBtnSubmit.setOnClickListener(this);
		mTxtData.setText(mAnswerList.get(0).content);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.tv_datetime:
			showTimePickerDialog();
			break;
		case R.id.btn_submit:
			sendApply();
			break;

		default:
			break;
		}
	}
	
	private void sendApply(){
		if (!ZxsqApplication.getInstance().isLogged()) {
			ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
			return;
		}
		
		EditText etUsername = (EditText) findViewById(R.id.et_username);
		EditText etCellphone = (EditText) findViewById(R.id.et_cellphone);
		EditText etHousing = (EditText) findViewById(R.id.et_housing);
		
		String username = etUsername.getText().toString();
		String cellphone = etCellphone.getText().toString();
		String housing = etHousing.getText().toString();
		String datetime = mDatetime;
		if (TextUtils.isEmpty(username)) {
			showToastMsg("请输入姓名名");
		} else if (TextUtils.isEmpty(cellphone)) {
			showToastMsg("请输入电话");
		} else if (TextUtils.isEmpty(housing)) {
			showToastMsg("请输入小区");
		} else if (TextUtils.isEmpty(datetime)) {
			showToastMsg("请输入时间");
		} else {
			hideSoftInput();
			showWaitDialog("申请中...");
			RequestParams params = new RequestParams();
			params.put("mobile", cellphone);
			params.put("housing", housing);
			params.put("date", datetime);
			params.put("name", username);
			params.put("add_entrance", "app_guidebook");
			params.put("add_app_guidebook", mTitle);
			HttpClientApi.post(HttpClientApi.CREATE_ORDER, params, new ResultParser(), new RequestCallBack<Result>() {

				@Override
				public void onSuccess(Result result) {
					showToastMsg("提交成功");
					ActivityUtil.next(THIS, ApplySuccessActivity.class, true);
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
	
	/**
     * 显示时间选择对话框
     */
    @SuppressLint("InflateParams")
    private void showTimePickerDialog() {
        if (null == mWheelSeletDatePanle) {
            mWheelSeletDatePanle = new CommonDatePickerWheelPanel(this, true);
            mWheelSeletDatePanle.setEnsureClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                	mDatetime = DateUtil.sdateToTimestamp(mWheelSeletDatePanle.getDatetime());
    				mBtnDatetime.setText(DateUtil.format(mWheelSeletDatePanle.getDatetime(), "yyyyMMddHHmmss", "yyyy-MM-dd  HH:mm"));
    				mWheelSeletDatePanle.dismissDialog();
                }
            });
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.strToDate(DateUtil.timestampToSdate(mDatetime, "yyyyMMddHHmmss"), "yyyyMMddHHmmss"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);
        mWheelSeletDatePanle.setTitle("");
        mWheelSeletDatePanle.initDateTimePicker(year, month, day, hours, mins);
        mWheelSeletDatePanle.showDialog();
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_LOGIN_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				sendApply();
			}
		}
	}
}
