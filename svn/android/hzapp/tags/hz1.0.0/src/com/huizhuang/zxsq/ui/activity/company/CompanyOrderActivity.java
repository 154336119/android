package com.huizhuang.zxsq.ui.activity.company;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.base.BaseFragmentActivity;
import com.huizhuang.zxsq.ui.activity.user.GuessULikeActivity;
import com.huizhuang.zxsq.ui.activity.user.UserSelectFriendActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.calendar.CaldroidFragment;
import com.huizhuang.zxsq.widget.calendar.CaldroidListener;
import com.lgmshare.http.netroid.exception.*;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;

public class CompanyOrderActivity extends BaseFragmentActivity implements
		OnClickListener {

	private Company mCompany;
	private CommonActionBar mCommonActionBar;

	private CaldroidFragment dialogCaldroidFragment;
	private TextView mTvDatetime;

	private Dialog mDialog;
	private Date mDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.company_order);

		getIntentExtras();
		initActionBar();
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_phonebook).setOnClickListener(this);
		findViewById(R.id.btn_datetime).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		mTvDatetime = (TextView) findViewById(R.id.btn_datetime);
	}

	private void getIntentExtras() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if (intent != null) {
			mCompany = (Company) intent.getExtras().getSerializable(AppConstants.PARAM_COMPANY);
		}
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_company_order);
		
		mCommonActionBar.setRightImgBtn(R.drawable.ic_header_phone, new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				callPhonePanel();
				}
		});
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				}
		});
	}

	private void httpRequestSubmit() {
		EditText etName = (EditText) findViewById(R.id.et_name);
		EditText etPhone = (EditText) findViewById(R.id.et_phone);
		String name = etName.getText().toString();
		String phone = etPhone.getText().toString();
		if (TextUtils.isEmpty(name)) {
			showToastMsg("请输入联系人");
		} else if (TextUtils.isEmpty(phone)) {
			showToastMsg("请输入手机号");
		} else if (!Util.isMobileNum(phone)) {
			showToastMsg("错误的手机号");
		} else if (mDate==null) {
			showToastMsg("请填写回访时间");
		} else {
			showWaitDialog("数据提交中...");
			RequestParams params = new RequestParams();
		//	params.put("user_id", AppContext.getInstance().getUser().getId());
			params.put("mobile", phone);
			params.put("date", mDate.getTime()+"");
			params.put("name", name);
			params.put("add_entrance", "app_store_v2");
			params.put("add_store_id", mCompany.getId());
			params.put("housing", "");
			params.put("add_app_guidebook", "");
			
			HttpClientApi.post(HttpClientApi.CREATE_ORDER, params, new ResultParser(), new RequestCallBack<Result>() {

				@Override
				public void onSuccess(Result response) {
					showToastMsg("预约成功");
					finish();
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

	private void showCalendar() {
		if (dialogCaldroidFragment == null) {
			dialogCaldroidFragment = new CaldroidFragment();
			dialogCaldroidFragment.setCaldroidListener(new CaldroidListener() {
				
				@Override
				public void onSelectDate(Date date, View view) {
					mDate = date;
					mTvDatetime.setText(DateUtil.dateToStrShort(date));
					dialogCaldroidFragment.dismiss();
				}
			});
		}

		// If activity is recovered from rotation
		final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
		// Setup arguments
		Bundle bundle = new Bundle();
		// Setup dialogTitle
        // bundle.putString(CaldroidFragment.DIALOG_TITLE, "预约时间");
		bundle.putString(CaldroidFragment.DIALOG_TITLE, null);
		dialogCaldroidFragment.setArguments(bundle);

		dialogCaldroidFragment.show(getSupportFragmentManager(), dialogTag);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_datetime:
			showCalendar();
			break;
		case R.id.btn_phonebook:
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 1);
			break;
		case R.id.btn_submit:
			httpRequestSubmit();
			break;
		case R.id.btn_call:
			mDialog.dismiss();
			mDialog = null;
			Util.callPhone(CompanyOrderActivity.this, mCompany.getPhone());
			break;
		case R.id.btn_cancel:
			mDialog.dismiss();
			mDialog = null;
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				Uri contactData = data.getData();
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();

				String name = this.getContactName(cursor);
				String phone = this.getContactPhone(cursor);
				EditText etName = (EditText) findViewById(R.id.et_name);
				EditText etPhone = (EditText) findViewById(R.id.et_phone);
				etName.setText(name);
				etPhone.setText(phone);
			}
			break;

		default:
			break;
		}
	}

	private String getContactPhone(Cursor cursor) {
		int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String result = "";
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(BaseColumns._ID);
			String contactId = cursor.getString(idColumn);
			// 获得联系人电话的cursor
			Cursor phone = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
			if (phone.moveToFirst()) {
				for (; !phone.isAfterLast(); phone.moveToNext()) {
					int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					String phoneNumber = phone.getString(index);
					result = phoneNumber;
				}
				if (!phone.isClosed()) {
					phone.close();
				}
			}
		}
		return result;
	}

	private String getContactName(Cursor cursor) {
		// 获得联系人
		int displayNameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		String contactName = cursor.getString(displayNameColumn);
		return contactName;
	}

	@SuppressLint("InflateParams") 
	private void callPhonePanel() {
		final View view = LayoutInflater.from(this).inflate(R.layout.company_detail_call_phone_dialog, null);
		view.findViewById(R.id.btn_call).setOnClickListener(this);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
		
		Button callButton = (Button) view.findViewById(R.id.btn_call);
		callButton.setText("拨打电话  " + mCompany.getPhone());
		mDialog = new Dialog(CompanyOrderActivity.this, R.style.DialogBottomPop);
		mDialog.setContentView(view);
		Window window = mDialog.getWindow();
		window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		mDialog.show();
	}
}
