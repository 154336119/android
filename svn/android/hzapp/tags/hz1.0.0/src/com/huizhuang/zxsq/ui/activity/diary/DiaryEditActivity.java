package com.huizhuang.zxsq.ui.activity.diary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Author;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.ui.adapter.base.MyBaseAdapter;
import com.huizhuang.zxsq.ui.activity.base.BaseFragmentActivity;
import com.huizhuang.zxsq.ui.activity.image.ImageSelectActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * @ClassName: DiaryEditActivity
 * @Description: 编辑日记
 * @author lim
 * @mail limshare@gmail.com
 * @date 2014-9-26 上午11:17:49
 * 
 */
public class DiaryEditActivity extends BaseFragmentActivity implements OnClickListener, Callback, PlatformActionListener {

	private static final int MAX_UPLOAD_IMAGE_COUNT = 6;

	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR = 4;
	private static final int MSG_AUTH_COMPLETE = 5;

	private final int REQ_IMAGE_EFFECTS_CODE = 661;
	private final int REQ_IMAGE_CAPTURE_CODE = 662;
	private final int REQ_UN_BILL_CODE = 663;
	private final int REQ_UN_FRIENDS_CODE = 664;
	private final int REQ_LOGIN_CODE = 665;

	private Uri mImageUri;//

	private ImageGridViewAdapter mAdapter;
	private ArrayList<String> mImageIds;
	private ArrayList<String> mImages;

	private ToggleButton mSwSina;
	private ToggleButton mSwQQ;
	private EditText mEtContent;
	private TextView mTvUnionBill;

	private HeadImgAdapter mHeadImgAdapter;
	private ArrayList<User> mUserList;

	private String mNode;
	private String mBillIds;
	private int mSecrecy = 1;// 是否公开
	private String mWeather;

	private int mCurPhotoPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUri = getIntent().getData();
		ImageEffectsActivity.show(this, mImageUri, REQ_IMAGE_EFFECTS_CODE);
		setContentView(R.layout.diary_edit_activity);
		initActionBar();
		initViews();
	}

	private void initActionBar() {
		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle("编辑日记");
		commonActionBar.setLeftBtn(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		commonActionBar.setRightTxtBtn(R.string.diary_discuss_edit_submit, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ZxsqApplication.getInstance().isLogged()) {
					publishDiary();
				} else {
					ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
				}
			}
		});
	}

	private void initViews() {
		findViewById(R.id.btn_select_bill).setOnClickListener(this);
		findViewById(R.id.btn_select_friend).setOnClickListener(this);
		findViewById(R.id.btn_add_weather).setOnClickListener(this);
		// sina
		mSwSina = (ToggleButton) findViewById(R.id.switch_sina);
		mSwSina.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					authorize(SinaWeibo.NAME);
				}
			}
		});
		// mSwSina.isChecked();
		// QQ
		mSwQQ = (ToggleButton) findViewById(R.id.switch_qq);
		mSwQQ.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					authorize(TencentWeibo.NAME);
				}
			}
		});
		// mSwQQ.isChecked();
		// 是否公开
		CheckBox cbSecrecy = (CheckBox) findViewById(R.id.checkBox);
		cbSecrecy.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mSecrecy = 1;
				} else {
					mSecrecy = 0;
				}
			}
		});
		cbSecrecy.setChecked(true);

		mImages = new ArrayList<String>();
		mImages.add("");
		mAdapter = new ImageGridViewAdapter(this);
		GridView styleGridView = (GridView) findViewById(R.id.gv);
		/* 设置为横向滚动的一行GridView */
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		float density = outMetrics.density;
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		density = outMetrics.density;
		int listSize = MAX_UPLOAD_IMAGE_COUNT + 1; // GridView最多为7个
		ViewGroup.LayoutParams params = styleGridView.getLayoutParams();
		int itemWidth = UiUtil.dp2px(this, 40);
		int spacingWidth = (int) (5 * density);
		params.width = itemWidth * listSize + (listSize - 1) * spacingWidth;
		styleGridView.setStretchMode(GridView.NO_STRETCH); // 设置为禁止拉伸模式
		styleGridView.setNumColumns(listSize);
		styleGridView.setHorizontalSpacing(spacingWidth);
		styleGridView.setColumnWidth(itemWidth);
		styleGridView.setLayoutParams(params);

		styleGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == MAX_UPLOAD_IMAGE_COUNT) {
					showToastMsg(getString(R.string.txt_diary_upload_limit, MAX_UPLOAD_IMAGE_COUNT));
					return;
				}
				mCurPhotoPosition = position;
				capture();
			}
		});
		styleGridView.setAdapter(mAdapter);

		mTvUnionBill = (TextView) findViewById(R.id.tv_union_status);
		mEtContent = (EditText) findViewById(R.id.et_content);
	}

	private void updateTipsUser() {
		if (mHeadImgAdapter == null) {
			mHeadImgAdapter = new HeadImgAdapter(THIS);
		}
		if (mUserList.size() > 5) {
			mHeadImgAdapter.setList(mUserList.subList(0, 5));
		} else {
			mHeadImgAdapter.setList(mUserList);
		}
		GridView styleGrid = (GridView) findViewById(R.id.my_gv);
		styleGrid.setAdapter(mHeadImgAdapter);
	}

	private void updateUnionStatus() {
		if (!TextUtils.isEmpty(mBillIds)) {
			mTvUnionBill.setText("已关联");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_select_bill:
			if (ZxsqApplication.getInstance().isLogged()) {
				Bundle bd = new Bundle();
				bd.putString("ids", mBillIds);
				ActivityUtil.next(THIS, RelBillListActivity.class, bd, REQ_UN_BILL_CODE);
			} else {
				ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
			}
			break;
		case R.id.btn_select_friend:
			if (ZxsqApplication.getInstance().isLogged()) {
				Bundle bd = new Bundle();
				bd.putParcelableArrayList("users", mUserList);
				ActivityUtil.next(THIS, SelectFriendsActivity.class, bd, REQ_UN_FRIENDS_CODE);
			} else {
				ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
			}
			break;
		case R.id.btn_add_weather:
			showWeatherDialog();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void submit() {
		String content = mEtContent.getText().toString();
		if (TextUtils.isEmpty(content)) {
			showToastMsg("还没有填写日记内容哦");
		} else {

			// 发送微博分享
			if (mSwQQ.isChecked()) {
				Share share = new Share();
				share.setPlatformName(TencentWeibo.NAME);
				share.setText(content);
				Util.showShare(true, this, share);
			}
			if (mSwSina.isChecked()) {
				Share share = new Share();
				share.setPlatformName(SinaWeibo.NAME);
				share.setText(content);
				Util.showShare(true, this, share);
			}
			showWaitDialog("请稍后...");
			RequestParams params = new RequestParams();
			params.put("user_id", ZxsqApplication.getInstance().getUser().getId());
			params.put("content", content);
			params.put("public", mSecrecy);
			params.put("zx_node", mNode);
			params.put("weather", mWeather);
			params.put("journey_list", mBillIds);
			if (mImageIds != null && mImageIds.size() > 0) {
				params.put("imgs", getImageIds());
			}
			if (mUserList != null && mUserList.size() > 0) {
				params.put("user_list", getUserIds());
			}
			HttpClientApi.post(HttpClientApi.REQ_DIARY_ADD, params, new RequestCallBack<String>() {

				@Override
				public void onSuccess(String t) {
					showToastMsg("添加成功");
					Bundle bd = new Bundle();
					Author author = new Author();
					author.setId(ZxsqApplication.getInstance().getUser().getId());
					author.setAvatar(ZxsqApplication.getInstance().getUser().getAvatar());
					author.setName(ZxsqApplication.getInstance().getUser().getNickname());
					author.setGender(ZxsqApplication.getInstance().getUser().getSex());
					author.setFollowed("0");
					bd.putSerializable(AppConstants.PARAM_AUTHOR, author);
					bd.putBoolean("dialog", true);
					ActivityUtil.next(DiaryEditActivity.this, DiaryDetailActivity.class, bd, -1, true);
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

	private void publishDiary() {
		if (mImages != null && mImages.size() > 0) {
			uploadImage();
		} else {
			submit();
		}
	}

	private void uploadImage() {
		String content = mEtContent.getText().toString();
		if (TextUtils.isEmpty(content)) {
			showToastMsg("还没有填写日记内容哦");
		} else {
			showWaitDialog("请稍后...");
			RequestParams params = new RequestParams();
			params.put("user_id", ZxsqApplication.getInstance().getUser().getId());
			for (int i = 0; i < mImages.size() - 1; i++) {
				try {
					params.put("img" + (i + 1), new File(mImages.get(i)), "image/jpg");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			HttpClientApi.post(HttpClientApi.REQ_DIARY_ADD_UPLOAD_IMG, params, new RequestCallBack<String>() {

				@Override
				public void onSuccess(String result) {
					try {
						processUploadResult(result);
						submit();
					} catch (JSONException e) {
						e.printStackTrace();
						showToastMsg("数据格式化错误");
					}
				}

				@Override
				public void onFailure(NetroidError error) {
					hideWaitDialog();
					showToastMsg(error.getMessage());
				}

				@Override
				public void onFinish() {
					super.onFinish();
				}
			});
		}
	}

	// 选择天气
	@SuppressLint("InflateParams")
	private void showWeatherDialog() {
		final String[] weather = { "晴", "多云", "雪", "雷阵雨", "阴", "雨" };
		final View pickerview = LayoutInflater.from(this).inflate(R.layout.diary_edit_weather_select_layout, null, false);
		final Dialog dialog = new Dialog(this, R.style.DialogBottomPop);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = UiUtil.getScreenWidth(this);
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		dialog.getWindow().setAttributes(lp);
		dialog.setContentView(pickerview);
		dialog.show();

		pickerview.findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWeather = weather[0];
				dialog.cancel();
			}
		});
		pickerview.findViewById(R.id.imageView2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWeather = weather[1];
				dialog.cancel();
			}
		});
		pickerview.findViewById(R.id.imageView3).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWeather = weather[2];
				dialog.cancel();
			}
		});
		pickerview.findViewById(R.id.imageView4).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWeather = weather[3];
				dialog.cancel();
			}
		});
		pickerview.findViewById(R.id.imageView5).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWeather = weather[4];
				dialog.cancel();
			}
		});
		pickerview.findViewById(R.id.imageView6).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWeather = weather[5];
				dialog.cancel();
			}
		});
	}

	private void capture() {
		String imagePath = Util.createImagePath(THIS);
		if (imagePath == null) {
			return;
		}
		Intent intent = new Intent(DiaryEditActivity.this, ImageSelectActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("image-path", imagePath);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQ_IMAGE_CAPTURE_CODE);
	}

	private String getImageIds() {
		String array = "[";
		int length = mImageIds.size();
		for (int i = 0; i < length; i++) {
			array = array + mImageIds.get(i) + ",";
		}
		array = array.substring(0, array.length() - 1);
		array = array + "]";
		return array;
	}

	private String getUserIds() {
		String array = "[";
		int length = mUserList.size();
		for (int i = 0; i < length; i++) {
			array = array + mUserList.get(i).getId() + ",";
		}
		array = array.substring(0, array.length() - 1);
		array = array + "]";
		return array;
	}

	/**
	 * 第三方授权
	 * 
	 * @param plat
	 */
	private void authorize(String platformName) {
		// ShareSDK.initSDK(this);
		Platform plat = ShareSDK.getPlatform(DiaryEditActivity.this, platformName);
		if (plat == null) {
			showToastMsg("平台还未安装");
			return;
		}
		if (plat.isValid()) {
			String userId = plat.getDb().getUserId();
			if (!TextUtils.isEmpty(userId)) {
				plat.setPlatformActionListener(this);
				plat.SSOSetting(false); // true表示不使用SSO方式授权
				// plat.authorize();
				plat.showUser(null);
				// login(plat.getName(), userId, null);
				return;
			}
		}
		plat.showUser(null);
		plat.setPlatformActionListener(this);
		plat.SSOSetting(false); // true表示不使用SSO方式授权
		// plat.authorize();
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
		}
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();
		if (action == Platform.ACTION_USER_INFOR) {
			if (platform.getName() == SinaWeibo.NAME) {
				mSwSina.setChecked(false);
			} else if (platform.getName() == TencentWeibo.NAME) {
				mSwQQ.setChecked(false);
			}
			UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
		}
	}

	@Override
	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			if (platform.getName() == SinaWeibo.NAME) {
				mSwSina.setChecked(false);
			} else if (platform.getName() == TencentWeibo.NAME) {
				mSwQQ.setChecked(false);
			}
			UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_AUTH_CANCEL:
			showToastMsg("登录已取消");
			break;
		case MSG_AUTH_ERROR:
			showToastMsg("授权失败");
			break;
		case MSG_AUTH_COMPLETE:
			showToastMsg("授权成功");
			break;
		}
		return false;
	}

	/**
	 * 处理图片上传结果
	 * 
	 * @param result
	 * @throws JSONException
	 */
	private void processUploadResult(String result) throws JSONException {
		mImageIds = new ArrayList<String>();
		JSONObject resultJson = new JSONObject(result);
		JSONObject dataJson = resultJson.getJSONObject("data");
		JSONArray succeedArray = dataJson.getJSONArray("succeed");
		int length = succeedArray.length();
		for (int i = 0; i < length; i++) {
			mImageIds.add(succeedArray.getJSONObject(i).getString("iid"));
		}
	}

	public static void show(Context context, Uri uri) {
		Intent intent = new Intent(context, DiaryEditActivity.class);
		intent.setData(uri);
		if (!(context instanceof Activity)) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (REQ_IMAGE_CAPTURE_CODE == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				Uri imageUri = data.getParcelableExtra("image-path-uri");
				ImageEffectsActivity.show(DiaryEditActivity.this, imageUri, REQ_IMAGE_EFFECTS_CODE);
			}
		} else if (REQ_IMAGE_EFFECTS_CODE == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				Uri imageUri = data.getParcelableExtra("image-path-uri");
				System.out.println("mCurPhotoPosition = " + mCurPhotoPosition);
				mNode = data.getStringExtra("zx_node");
				if (-1 == mCurPhotoPosition || mCurPhotoPosition == mAdapter.getCount() - 1) {
					mImages.add(0, imageUri.getPath());
				} else {
					mImages.set(mCurPhotoPosition, imageUri.getPath());
				}
				mAdapter.setList(mImages);
				mAdapter.notifyDataSetChanged();
			} else {
				finish();
			}
		} else if (REQ_UN_FRIENDS_CODE == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				mUserList = data.getParcelableArrayListExtra("users");
				updateTipsUser();
			}
		} else if (REQ_UN_BILL_CODE == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				mBillIds = data.getStringExtra("ids");
				updateUnionStatus();
			}
		} else if (requestCode == REQ_LOGIN_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				// publishDiary();
			}
		}
	}

	class ImageGridViewAdapter extends MyBaseAdapter<String> {

		private int mWidth;

		public ImageGridViewAdapter(Context context) {
			super(context);
			mWidth = UiUtil.dp2px(context, 40);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.ads_image_item, viewGroup, false);
				viewHolder = new ViewHolder();
				viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.imgView);
				viewHolder.ivPhoto.setLayoutParams(new AbsListView.LayoutParams(mWidth, mWidth));
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (position == getCount() - 1) {
				ImageLoader.getInstance().displayImage(null, viewHolder.ivPhoto, ImageLoaderOptions.optionsDiaryPhotoAdd);
			} else {
				String path = getItem(position);
				ImageLoader.getInstance().displayImage("file://" + path, viewHolder.ivPhoto, ImageLoaderOptions.optionsDefaultEmptyPhoto);
			}
			return convertView;
		}

		class ViewHolder {
			private ImageView ivPhoto;
		}
	}

	class HeadImgAdapter extends MyBaseAdapter<User> {

		private int mWidth;
		private DisplayImageOptions mOptions;
		private Context mContext;

		public HeadImgAdapter(Context context) {
			super(context);
			mContext = context;
			mWidth = (UiUtil.getScreenWidth((Activity) context) - UiUtil.dp2px(context, 212)) / 5;
			mOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.icon01).showImageForEmptyUri(R.drawable.icon01)
					.bitmapConfig(Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY)// 设置图片缩放
					.cacheInMemory(true).cacheOnDisk(true).build();
		}

		@Override
		public View getView(int position, View view, ViewGroup viewGroup) {
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(R.layout.item_visitor_header_image, viewGroup, false);
				view.setLayoutParams(new AbsListView.LayoutParams(mWidth, mWidth));
			}
			ImageView ivHeadImg = (ImageView) view;
			User reader = getList().get(position);
			if (!TextUtils.isEmpty(reader.getAvatar())) {
				ImageLoader.getInstance().displayImage(reader.getAvatar(), ivHeadImg, mOptions);
			} else {
				ivHeadImg.setImageResource(R.drawable.icon01);
			}
			return view;
		}

	}
}
