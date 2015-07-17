package com.huizhuang.zxsq.ui.activity.account;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.Result;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.http.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.image.ImageSelectActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageLoaderUriUtils;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 
 * 
 * @Description:
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午11:08:47
 */
public class AccountDataEditorActivity extends BaseActivity implements OnClickListener {
	private CommonActionBar mCommonActionBar;
	private static final int REQ_CAPTURE_CODE = 666;

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private Dialog mDialog;
	private static final int SCALE = 3;// 照片缩小比例
	private CircleImageView cvIcon;
	private EditText etNick;
	private RadioGroup rgSex;
	private TextView tvCity;
	private EditText etPhone;
	private ImageView ivArrow;
	private RadioButton rbFmale;
	private RadioButton rbMale;
	private CityWheelMain mCityWheel;
	private String gender = "2";
	private String city;
	private String pro;
	private File photoFile = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_data_editor);
		initActionBar();
		initView();
		initViewData();
	}

	private void initView() {
		cvIcon = (CircleImageView) findViewById(R.id.cv_icon);
		etNick = (EditText) findViewById(R.id.et_nick);
		etPhone = (EditText) findViewById(R.id.et_phone);
		rgSex = (RadioGroup) findViewById(R.id.rg_sex);
		tvCity = (TextView) findViewById(R.id.tv_city);
		rbFmale = (RadioButton) findViewById(R.id.rb_fmale);
		rbMale = (RadioButton) findViewById(R.id.rb_male);
		ivArrow = (ImageView) findViewById(R.id.iv_arrow);
		ivArrow.setOnClickListener(this);
		cvIcon.setOnClickListener(this);
		findViewById(R.id.iv_arrow).setOnClickListener(this);

		rgSex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == rbFmale.getId()) {
					gender = "2";
				} else if (arg1 == rbMale.getId()) {
					gender = "1";
				}
			}
		});
	}

	private void initViewData(){
		User user = ZxsqApplication.getInstance().getUser();
    	if(null!=ZxsqApplication.getInstance().getUser().getUser_thumb()&&null!=ZxsqApplication.getInstance().getUser().getUser_thumb().getThumb_path()){
            ImageUtil.displayImage(ZxsqApplication.getInstance().getUser().getUser_thumb().getThumb_path(), cvIcon, ImageLoaderOptions.optionsUserHeader);
    	}
//		if (user.getNickname() != null){
//			etNick.setText(user.getNickname());
//		}
//		String name = TextUtils.isEmpty(user.getNickname()) ? user.getName() : user.getNickname();
//		name = TextUtils.isEmpty(name) ? user.getMobile():name;
		if(user.getScreen_name()!=null){
			etNick.setText(user.getScreen_name());
		}
		if (user.getGender() == 1) {
			rbMale.setChecked(true);
		} else if (user.getGender() == 2) {
			rbFmale.setChecked(true);
		}
		String city = "";
		if(!TextUtils.isEmpty(user.getProvince())){
			city += user.getProvince();
		}
		if(!TextUtils.isEmpty(user.getCity())){
			city += (" "+user.getCity());
		}
		tvCity.setText(city);
		if (user.getPhone() != null) {
			etPhone.setText(user.getPhone());
		}
	}
	
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_user_data_editor);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtil.back(AccountDataEditorActivity.this);
			}
		});
		mCommonActionBar.setRightTxtBtn(R.string.save, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				httpRequestsubmit();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.cv_icon :
				capture();
				break;
			case R.id.iv_arrow :
				showMomentDialog();
				break;				
			case R.id.btn_cancel :
				mDialog.dismiss();
				break;
			default :
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CAPTURE_CODE) {
			// cvIcon.setImageURI(imageUri);
			if(data!=null){
			String path = data.getStringExtra("image-path");
			photoFile = new File(path);
			String uriFromLocalFile = ImageLoaderUriUtils.getUriFromLocalFile(path);
			LogUtil.d("uriFromLocalFile:"+uriFromLocalFile);
			ImageUtil.displayImage(uriFromLocalFile, cvIcon, ImageLoaderOptions.optionsUserHeader);
			}
		}
	}

	private void httpRequestsubmit() {
		String nickName = "";
		String cityPro = tvCity.getText().toString();
		if(!TextUtils.isEmpty(cityPro)){
		pro = cityPro.substring(0, cityPro.indexOf(" "));
		city = cityPro.substring(cityPro.indexOf(" ") + 1);
		}
		if (etNick.getText().toString() != null) {
			nickName = etNick.getText().toString();
		}
		showWaitDialog("数据提交中...");
		RequestParams params = new RequestParams();
		params.put("user_id", ZxsqApplication.getInstance().getUser().getUser_id());
		try {
			params.put("avatar", photoFile);
			if (photoFile.isFile()) {
				LogUtil.i("jiengyh photoFile:" + photoFile);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("e:" + e);
		}
		params.put("nickname", nickName);
		params.put("gender", gender);
		params.put("province", pro);
		params.put("city", city);
		HttpClientApi.post(HttpClientApi.REQ_SAVE_INFO, params, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onSuccess(Result response) {
				showToastMsg("保存成功");
				setResult(RESULT_OK);
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
	private void capture() {
		String mImagePath = Util.createImagePath(this);
		if (mImagePath == null) {
			return;
		}
		Intent intent = new Intent(this, ImageSelectActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("crop", true);
		bundle.putString("image-path", mImagePath);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQ_CAPTURE_CODE);
	}

	private void showMomentDialog() {
		final View pickerview = LayoutInflater.from(this).inflate(R.layout.zxbd_wheel, null, false);
		mCityWheel = new CityWheelMain(this, pickerview);
		mCityWheel.init();
		final Dialog dialog = new Dialog(this, R.style.DialogBottomPop);
		Window window = dialog.getWindow();
		// window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = UiUtil.getScreenWidth(this);
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		dialog.getWindow().setAttributes(lp);
		dialog.setContentView(pickerview);
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
				pro = mCityWheel.getPro();
				city = mCityWheel.getCity();
				tvCity.setText(pro + " " + city);
				dialog.cancel();
			}
		});
	}
}
