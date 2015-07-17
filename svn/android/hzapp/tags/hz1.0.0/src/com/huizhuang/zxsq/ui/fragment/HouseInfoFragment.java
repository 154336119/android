package com.huizhuang.zxsq.ui.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.RoomInfo;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.module.parser.RoomInfoParser;
import com.huizhuang.zxsq.ui.activity.account.LoveFamilyProfileActivity;
import com.huizhuang.zxsq.ui.activity.image.ImageSelectActivity;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.ToastUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.RoomInfoItemBar;
import com.huizhuang.zxsq.widget.wheel.CommonDatePickerWheelPanel;
import com.huizhuang.zxsq.widget.wheel.WheelVerticalView;
import com.huizhuang.zxsq.widget.wheel.adapter.AbstractWheelTextAdapter;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 房屋信息
 * 
 * @ClassName: HouseInfoFragment
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-28 上午11:02:00
 */
public class HouseInfoFragment extends BaseFragment {

	/**
	 * 图片相关常量
	 */
	private static final int REQ_PHOTO_CODE = 927;

	enum HouseWheel {
		HOUSE_TYPE, // 房型
		HOUSE_STYLE, // 风格
		DECORATION_TIME, // 装修时间
		DECORATION_BUDGET // 装修预算
	}

	enum UploadPhotoType {
		ROOM_TYPE, // 房型图
		ROOM_DESIGN, // 设计图
		ROOM_EFFECT // 效果图
	}

	private ImageView mImgHousePhoto;
	private ImageView mImgDesignPhoto;
	private ImageView mImgEffectPhoto;

	private WheelVerticalView mWheelVerticalView;
	private HouseWheelAdapter mHouseWheelAdapter;
	private CommonDatePickerWheelPanel mWheelSeletDatePanle;

	private RoomInfoItemBar mRoomTypeBar;
	private RoomInfoItemBar mRoomAreaBar;
	private RoomInfoItemBar mRoomStyleBar;
	private RoomInfoItemBar mRoomZxStoreBar;
	private RoomInfoItemBar mRoomZxDesigneBar;
	private RoomInfoItemBar mRoomZxBudgetBar;
	private RoomInfoItemBar mRoomZxTimeBar;

	private Context mContext;

	private int mHouseTypeCurIndex = -1;
	private int mHouseStyleCurIndex = -1;
	private int mDecorationBudgetCurIndex = -1;
	private int mImgHouseId = 0;
	private int mImgDesignId = 0;
	private int mImgEffectId = 0;
	private String mDatetime;

	private ImageView mCurUploadImageView;
	private UploadPhotoType mUploadPhotoType;
	private RoomInfo mRoomInfo;
	private String mUserId;

	private boolean mIsEditAble = false;

	public static HouseInfoFragment newInstance() {
		return new HouseInfoFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_house_info, container, false);

		mImgHousePhoto = (ImageView) view.findViewById(R.id.img_house_photo);
		mImgDesignPhoto = (ImageView) view.findViewById(R.id.img_design_photo);
		mImgEffectPhoto = (ImageView) view.findViewById(R.id.img_effect_photo);
		mImgHousePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPhotoSelectDialog(UploadPhotoType.ROOM_TYPE);
			}
		});
		mImgDesignPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPhotoSelectDialog(UploadPhotoType.ROOM_DESIGN);
			}
		});
		mImgEffectPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPhotoSelectDialog(UploadPhotoType.ROOM_EFFECT);
			}
		});

		mRoomTypeBar = (RoomInfoItemBar) view.findViewById(R.id.room_type_bar);
		mRoomAreaBar = (RoomInfoItemBar) view.findViewById(R.id.room_area_bar);
		mRoomAreaBar.setInputOnlyNumber();
		mRoomAreaBar.setItemUnitEnable();
		mRoomStyleBar = (RoomInfoItemBar) view.findViewById(R.id.room_style_bar);
		mRoomZxTimeBar = (RoomInfoItemBar) view.findViewById(R.id.room_zx_time_bar);
		mRoomZxStoreBar = (RoomInfoItemBar) view.findViewById(R.id.room_zxstore_bar);
		mRoomZxDesigneBar = (RoomInfoItemBar) view.findViewById(R.id.room_zx_designe_bar);
		mRoomZxBudgetBar = (RoomInfoItemBar) view.findViewById(R.id.room_zx_budget_bar);
		mRoomTypeBar.setItemOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSelectDialog(HouseWheel.HOUSE_TYPE);
			}
		});
		mRoomStyleBar.setItemOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSelectDialog(HouseWheel.HOUSE_STYLE);
			}
		});
		mRoomZxTimeBar.setItemOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTimePickerDialog();
			}
		});
		mRoomZxBudgetBar.setItemOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSelectDialog(HouseWheel.DECORATION_BUDGET);
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mContext = getActivity();
		mUserId = getActivity().getIntent().getStringExtra(LoveFamilyProfileActivity.EXTRA_USER_ID);
		if (mUserId == null) {
			mUserId = ZxsqApplication.getInstance().getUser().getId();
			mRoomAreaBar.setInputHint(R.string.txt_house_square_hint);
			mRoomAreaBar.setInputMaxLength(10);
			mRoomZxStoreBar.setInputHint(R.string.txt_house_decorate_company_hint);
			mRoomZxStoreBar.setInputMaxLength(15);
			mRoomZxDesigneBar.setInputHint(R.string.txt_house_designer_hint);
			mRoomZxDesigneBar.setInputMaxLength(10);
			mIsEditAble = true;
		} else {// 不是自己访问 不能编辑
			mImgHousePhoto.setClickable(false);
			mImgDesignPhoto.setClickable(false);
			mImgEffectPhoto.setClickable(false);
			mRoomTypeBar.setItemEnabled(false);
			mRoomAreaBar.setItemEnabled(false);
			mRoomStyleBar.setItemEnabled(false);
			mRoomZxTimeBar.setItemEnabled(false);
			mRoomZxStoreBar.setItemEnabled(false);
			mRoomZxDesigneBar.setItemEnabled(false);
			mRoomZxBudgetBar.setItemEnabled(false);
		}

		LogUtil.i("onActivityCreated mUserId:" + mUserId);
		httpRequestQueryHouseInfoByUserId(mUserId);
	}

	/**
	 * @param userId
	 */
	public void httpRequestQueryHouseInfoByUserId(String profileId) {
		LogUtil.i("httpRequestQueryHouseInfoByUserId profileId:" + profileId);

		RequestParams params = new RequestParams();
		params.add("profile_id", profileId);
		HttpClientApi.get(HttpClientApi.REQ_USER_CENTER_GET_ROOM_INFO, params, new RoomInfoParser(), new RequestCallBack<RoomInfo>() {

			@Override
			public void onSuccess(RoomInfo roomInfo) {
				LogUtil.i("jiengyh   onSuccess RoomInfo roomInfo:" + roomInfo);
				mRoomInfo = roomInfo;
				String roomTypeName = ZxsqApplication.getInstance().getConstant().getRoomTypeIdByRoomName(roomInfo.getRoomType());
				mRoomTypeBar.setContent(roomTypeName);
				mRoomAreaBar.setContent(String.valueOf(roomInfo.getRoomArea()));

				String roomStyleName = ZxsqApplication.getInstance().getConstant().getRoomStyleIdByRoomName(roomInfo.getRoomStyle());
				mRoomStyleBar.setContent(roomStyleName);
				mRoomZxStoreBar.setContent(roomInfo.getZxStore());
				mRoomZxDesigneBar.setContent(roomInfo.getZxDesigne());

				String decorationBudget = ZxsqApplication.getInstance().getConstant().getDecorationBudgetIdByRoomName(roomInfo.getCostRange()+"");
				mRoomZxBudgetBar.setContent(decorationBudget);

				if (roomInfo.getZxDate() != null && !roomInfo.getZxDate().equals("null")) {
					mRoomZxTimeBar.setContent(roomInfo.getZxDate());
				}

				String housePhotoImage = null;
				if (mRoomInfo.getHxImage() != null && mRoomInfo.getHxImage().size() > 0) {
					housePhotoImage = mRoomInfo.getHxImage().get(0).getImgPath();
					mImgHouseId = mRoomInfo.getHxImage().get(0).getId();
					LogUtil.i("jiengyh img:" + housePhotoImage);
				}
				if (mIsEditAble) {
					ImageLoader.getInstance().displayImage(housePhotoImage, mImgHousePhoto, ImageLoaderOptions.optionsHouseInfoPhoto);
				} else {
					ImageLoader.getInstance().displayImage(housePhotoImage, mImgHousePhoto, ImageLoaderOptions.optionsDefaultEmptyPhoto);
				}

				String roomPhotoImage = null;
				if (mRoomInfo.getSjImage() != null && mRoomInfo.getSjImage().size() > 0) {
					roomPhotoImage = mRoomInfo.getSjImage().get(0).getImgPath();
					mImgDesignId = mRoomInfo.getSjImage().get(0).getId();
					LogUtil.i("jiengyh img:" + roomPhotoImage);
				}
				if (mIsEditAble) {
					ImageLoader.getInstance().displayImage(roomPhotoImage, mImgDesignPhoto, ImageLoaderOptions.optionsHouseInfoPhoto);
				} else {
					ImageLoader.getInstance().displayImage(roomPhotoImage, mImgDesignPhoto, ImageLoaderOptions.optionsDefaultEmptyPhoto);
				}

				String effectPhotoImage = null;
				if (mRoomInfo.getXgImage() != null && mRoomInfo.getXgImage().size() > 0) {
					effectPhotoImage = mRoomInfo.getXgImage().get(0).getImgPath();
					mImgEffectId = mRoomInfo.getXgImage().get(0).getId();
					LogUtil.i("jiengyh img:" + effectPhotoImage);
				}
				if (mIsEditAble) {
					ImageLoader.getInstance().displayImage(effectPhotoImage, mImgEffectPhoto, ImageLoaderOptions.optionsHouseInfoPhoto);
				} else {
					ImageLoader.getInstance().displayImage(effectPhotoImage, mImgEffectPhoto, ImageLoaderOptions.optionsDefaultEmptyPhoto);
				}

				if (null != mOnFragmentDataLoadListener) {
					mOnFragmentDataLoadListener.onDataLoadSuccess();
				}

			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.i("jiengyh onFailure");

				if (null != mOnFragmentDataLoadListener) {
					mOnFragmentDataLoadListener.onDataLoadFailed(netroidError.getMessage());
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				if (null != mOnFragmentDataLoadListener) {
					mOnFragmentDataLoadListener.onDataLoadStart();
				}
			}

		});
	}

	private void showPhotoSelectDialog(UploadPhotoType uploadPhotoType) {
		mUploadPhotoType = uploadPhotoType;
		switch (mUploadPhotoType) {
		case ROOM_TYPE:
			mCurUploadImageView = mImgHousePhoto;
			break;
		case ROOM_DESIGN:
			mCurUploadImageView = mImgDesignPhoto;
			break;
		case ROOM_EFFECT:
			mCurUploadImageView = mImgEffectPhoto;
			break;
		default:
			mCurUploadImageView = mImgHousePhoto;
			break;
		}
		String imageUrl = Util.createImagePath(mContext);
		if (null != imageUrl) {
			Intent intent = new Intent(mContext, ImageSelectActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("crop", false);
			bundle.putString("image-path", imageUrl);
			intent.putExtras(bundle);
			startActivityForResult(intent, REQ_PHOTO_CODE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (Activity.RESULT_OK == resultCode) {
			switch (requestCode) {
			case REQ_PHOTO_CODE:
				if (null != intent) {
					String path = intent.getStringExtra("image-path");
					httpRequestUploadImage(new File(path));
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param imgFile
	 */
	private void httpRequestUploadImage(File imgFile) {
		LogUtil.i("file:" + imgFile.toString());
		RequestParams params = new RequestParams();
		try {
			params.put("img_1", imgFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		params.add("user_id", ZxsqApplication.getInstance().getUser().getId());
		HttpClientApi.post(HttpClientApi.REQ_USER_CENTER_UPLOAD_IMG, params, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onStart() {
				showWaitDialog("图片上传中...");
				super.onStart();
			}

			@Override
			public void onSuccess(Result result) {
				try {
					JSONObject obj = new JSONObject(result.data);
					JSONArray array = obj.optJSONArray("succeed");
					if (array != null && array.length() > 0) {
						int iid = array.optJSONObject(0).optInt("iid");
						String url = array.optJSONObject(0).optString("url");
						LogUtil.i("jiengyh upLoadImage result:" + result.data + "   iid:" + iid);

						if (mCurUploadImageView == mImgHousePhoto) {
							mImgHouseId = iid;
						} else if (mCurUploadImageView == mImgDesignPhoto) {
							mImgDesignId = iid;
						} else if (mCurUploadImageView == mImgEffectPhoto) {
							mImgEffectId = iid;
						}
						ImageLoader.getInstance().displayImage(url, mCurUploadImageView, ImageLoaderOptions.optionsHouseInfoPhoto);
					} else {
						ToastUtil.showMessage(getActivity(), "上传图片失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(NetroidError error) {
				LogUtil.i("jiengyh onFailure error:" + error.getMessage());
			}

			@Override
			public void onFinish() {
				LogUtil.i("jiengyh onFinish");
				hideWaitDialog();
				super.onFinish();
			}
		});

	}

	/**
	 * 显示时间选择器Dialog
	 */
	@SuppressLint("InflateParams")
	private void showTimePickerDialog() {
		if (null == mDatetime) {
			mDatetime = DateUtil.getTimestamp();
		}
		if (null == mWheelSeletDatePanle) {
			mWheelSeletDatePanle = new CommonDatePickerWheelPanel(getActivity());
			mWheelSeletDatePanle.setEnsureClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 将默认的日期格式转化成yyyy-MM-dd格式
					mDatetime = DateUtil.format(mWheelSeletDatePanle.getDatetime(), "yyyyMMddHHmmss", "yyyy-MM-dd");
					mRoomZxTimeBar.setContent(mDatetime);
					mWheelSeletDatePanle.dismissDialog();
				}
			});
		}

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		mWheelSeletDatePanle.setTitle("");
		mWheelSeletDatePanle.initDateTimePicker(year, month, day);
		mWheelSeletDatePanle.showDialog();
	}

	private void showSelectDialog(final HouseWheel houseWheel) {
		final View view = LayoutInflater.from(mContext).inflate(R.layout.search_wheel, null, false);
		mWheelVerticalView = (WheelVerticalView) view.findViewById(R.id.wheel_view);
		switch (houseWheel) {
		case HOUSE_TYPE:
			mHouseWheelAdapter = new HouseWheelAdapter(mContext, ZxsqApplication.getInstance().getConstant().getRoomTypes());
			break;
		case HOUSE_STYLE:
			mHouseWheelAdapter = new HouseWheelAdapter(mContext, ZxsqApplication.getInstance().getConstant().getRoomStyles());
			break;
		case DECORATION_BUDGET:
			mHouseWheelAdapter = new HouseWheelAdapter(mContext, ZxsqApplication.getInstance().getConstant().getCostRanges());
			break;
		default:
			break;
		}
		mHouseWheelAdapter.setItemResource(R.layout.zxbd_wheel_item);
		mHouseWheelAdapter.setItemTextResource(R.id.name);
		mWheelVerticalView.setViewAdapter(mHouseWheelAdapter);
		mWheelVerticalView.setCyclic(false);// 可循环滚动
		mWheelVerticalView.setCurrentItem(0);// 初始化时显示的数据
		mWheelVerticalView.setVisibleItems(5);
		final Dialog dialog = new Dialog(mContext, R.style.DialogBottomPop);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = UiUtil.getScreenWidth(getActivity());
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		dialog.getWindow().setAttributes(lp);
		dialog.setContentView(view);
		dialog.show();
		view.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		view.findViewById(R.id.btn_ensure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (houseWheel) {
				case HOUSE_TYPE:
					mHouseTypeCurIndex = mWheelVerticalView.getCurrentItem();
					mRoomTypeBar.setContent(mHouseWheelAdapter.getKeyValueList().get(mHouseTypeCurIndex).getName());
					mRoomInfo.setRoomType(mHouseWheelAdapter.getKeyValueList().get(mHouseTypeCurIndex).getId() + "");
					break;
				case HOUSE_STYLE:
					mHouseStyleCurIndex = mWheelVerticalView.getCurrentItem();
					mRoomStyleBar.setContent(mHouseWheelAdapter.getKeyValueList().get(mHouseStyleCurIndex).getName());
					mRoomInfo.setRoomStyle(mHouseWheelAdapter.getKeyValueList().get(mHouseStyleCurIndex).getId() + "");
					break;
				case DECORATION_BUDGET:
					mDecorationBudgetCurIndex = mWheelVerticalView.getCurrentItem();
					mRoomZxBudgetBar.setContent(mHouseWheelAdapter.getKeyValueList().get(mDecorationBudgetCurIndex).getName());
					mRoomInfo.setZxBudget(mHouseWheelAdapter.getKeyValueList().get(mDecorationBudgetCurIndex).getId() + "");
					break;
				default:
					break;
				}
				dialog.cancel();
			}
		});
	}

	class HouseWheelAdapter extends AbstractWheelTextAdapter {

		private List<KeyValue> mKeyValueList;

		public List<KeyValue> getKeyValueList() {
			return mKeyValueList;
		}

		public HouseWheelAdapter(Context context, List<KeyValue> keyValueList) {
			super(context);
			mKeyValueList = keyValueList;
		}

		@Override
		public CharSequence getItemText(int index) {
			if (index >= 0 && index < mKeyValueList.size()) {
				KeyValue keyValue = mKeyValueList.get(index);
				return keyValue.getName();
			}
			return null;
		}

		@Override
		public int getItemsCount() {
			return mKeyValueList.size();
		}
	}

	public void upDateRoomData() {
		if (!mIsEditAble) {
			getActivity().finish();
			return;
		}
		String area = mRoomAreaBar.getContent();
		double roomArea = 0;
		try {
			roomArea = Double.parseDouble(area);
		} catch (NumberFormatException ex) {
		}
		String zxTime = mRoomZxTimeBar.getContent();
//		String zxBudget = mRoomZxBudgetBar.getContent();
		String zxDesigne = mRoomZxDesigneBar.getContent();
		String zxStore = mRoomZxStoreBar.getContent();
		// String roomType = mRoomTypeBar.getContent();
		// String roomStyle = mRoomStyleBar.getContent();

		RequestParams params = new RequestParams();
		params.add("user_id", ZxsqApplication.getInstance().getUser().getId());
		LogUtil.i("jiengyh testSaveRoomInfo  userid:" + ZxsqApplication.getInstance().getUser().getId());
		params.add("room_type", mRoomInfo.getRoomType() + "");
		params.add("room_style", mRoomInfo.getRoomStyle() + "");
		params.add("room_area", roomArea + "");
		params.add("zx_store", zxStore);
		params.add("zx_designer", zxDesigne);
		params.add("cost_range", mRoomInfo.getZxBudget());
		
		params.add("zx_date", zxTime);
		params.add("id", mRoomInfo.getId() + "");

		if (mImgHouseId != 0) {
			params.add("hx_image", getJsonArrayString(mImgHouseId));
		}
		if (mImgEffectId != 0) {
			params.add("xg_image", getJsonArrayString(mImgEffectId));
		}
		if (mImgDesignId != 0) {
			params.add("sj_image", getJsonArrayString(mImgDesignId));
		}
		HttpClientApi.get(HttpClientApi.REQ_USER_CENTER_SAVE_ROOM_INFO, params, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onStart() {
				showWaitDialog(getString(R.string.txt_on_waiting));
				super.onStart();
			}

			@Override
			public void onSuccess(Result result) {
				LogUtil.i("jiengyh onSuccess  result:" + result.data);
				showToastMsg(getString(R.string.txt_update_success));
				getActivity().finish();
			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.i("jiengyh onFailure");
				showToastMsg(netroidError.getMessage());
			}

			@Override
			public void onFinish() {
				LogUtil.i("jiengyh onFinish");
				super.onFinish();
				hideWaitDialog();
			}
		});

	}

	private String getJsonArrayString(int id) {
		JSONArray array = new JSONArray();
		array.put(id);
		return array.toString();
	}

	public interface OnFragmentDataLoadListener {

		/**
		 * 数据加载开始
		 */
		void onDataLoadStart();

		/**
		 * 数据加载错误
		 */
		void onDataLoadFailed(String strError);

		/**
		 * 数据加载完成
		 */
		void onDataLoadSuccess();
	}

	private OnFragmentDataLoadListener mOnFragmentDataLoadListener;

	public void setOnFragmentDataLoadListener(OnFragmentDataLoadListener onFragmentDataLoadListener) {
		mOnFragmentDataLoadListener = onFragmentDataLoadListener;
	}
}