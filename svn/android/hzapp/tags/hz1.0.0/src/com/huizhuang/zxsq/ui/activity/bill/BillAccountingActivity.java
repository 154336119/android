package com.huizhuang.zxsq.ui.activity.bill;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.Constant.JourneyType;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.base.BillAccountingBaseActivity;
import com.huizhuang.zxsq.ui.activity.image.ImageSelectActivity;
import com.huizhuang.zxsq.ui.adapter.BillAccountingGirdViewAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.SelectImageAnimation;
import com.huizhuang.zxsq.utils.SelectImageAnimation.OnSelectAnimation;
import com.huizhuang.zxsq.utils.ToastUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.CalculatorView;
import com.huizhuang.zxsq.widget.CalculatorView.OnCalcuateSuccessListener;
import com.huizhuang.zxsq.widget.actionbar.BillAccountingActionBar;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;
import com.huizhuang.zxsq.widget.wheel.CommonDatePickerWheelPanel;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 记账页面
 * 
 * @ClassName: BillAccountingActivity
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-31 16:20:00
 */
public class BillAccountingActivity extends BillAccountingBaseActivity implements OnClickListener {

	/**
	 * Intent Extras
	 */
	public static final String EXTRA_JOURNEY_TYPE = "journeyType";

	private static final int REQ_PHOTO_CODE = 927;
	private static final int DEF_CURRENT_POSITION = 0;
	private static final int GIRD_ONE_PAGE_COUNT = 10;

	/**
	 * 所有控件
	 */
	private BillAccountingActionBar mBillAccountingActionBar;
	private ViewPager mSelectCategoryViewPager;
	private SelectCategoryPagerAdapter mSelectCategoryPagerAdapter;
	private CalculatorView mCalculatorView;
	private ImageView[] mIndicatorViews;
	private Dialog mPhotoRemarkDialog;
	private CommonDatePickerWheelPanel mWheelSeletDatePanle;
	private EditText mEdtInputRemark;
	private ImageView mImgViewPhotoRemark;
	private ImageView mImgViewPhotoRemarkAdd;
	private CommonAlertDialog mCommonAlertDialog;

	private Context mContext;

	private int mCurPagerIndex = DEF_CURRENT_POSITION;
	private int mGridPageTotalCount = 0;
	private JourneyType mJourneyType; // 记账
	private JourneyType mJourneyTypeItem; // 记账
	private String mDatetime; // 记账日期（格式：2014-11-04）
	private String mStrRemarkNote; // 备注文字信息
	private int mUploadImageId; // 图片上传成功的ID
	private double mCalResutl;
	private int mCTypeId;
	private boolean mIsAnimationFinish = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bill_accounting);
		mContext = BillAccountingActivity.this;

		getIntentExtras();
		initActionBar();
		initViews();
	}

	@Override
	protected void onReceiveBillAccountChanged() {
	}

	/**
	 * 获得Intent传递过来的Extras
	 */
	private void getIntentExtras() {
		mJourneyType = (JourneyType) getIntent().getSerializableExtra(EXTRA_JOURNEY_TYPE);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mBillAccountingActionBar = (BillAccountingActionBar) findViewById(R.id.common_action_bar);
		mBillAccountingActionBar.setBtnBackOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnBackOnClick(v);
			}
		});
		mBillAccountingActionBar.setPhotoRemarkOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				itemPhotoRemarkOnClick();
			}
		});
		mBillAccountingActionBar.setSelectDateOnClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				itemSelectDateOnClick(v);
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		mCalculatorView = (CalculatorView) findViewById(R.id.calculator_view);
		mCalculatorView.setonCalcuateSuccessListener(new OnCalcuateSuccessListener() {

			@Override
			public void calcuateResult(boolean isMust, double result) {
				LogUtil.i("jiengyh  calcuateResult result:" + result);
				if (result == 0) {
					return;
				}
				mCalResutl = result;
				mCTypeId = 0;
				if (mJourneyTypeItem != null) {
					mCTypeId = mJourneyTypeItem.getCid();
				}

				// 如果没有选择日期，则默认当前的时
				if (null == mDatetime) {
					mDatetime = DateUtil.getStandardFormatDate(System.currentTimeMillis());
					System.out.println("mDatetime = " + mDatetime);
				}

				// 如果主要上传了照片或添加了备注信息，就不提示用户是否需要拍照备注
				if ((null != mStrRemarkNote && mStrRemarkNote.length() > 0) || mUploadImageId != 0) {
					httpRequestUpLoadJourney(mJourneyType.getId(), mCTypeId, mStrRemarkNote, mUploadImageId, mDatetime, mCalResutl);
				} else {
					showRemarkPhotoDialog();
				}

			}
		});

		initViewPager();
		initIndicator(mGridPageTotalCount);

	}

	/**
	 * 初始化ViewPager区域
	 * 
	 * @param pageTotalCount
	 */
	private void initViewPager() {
		mGridPageTotalCount = 0;

		List<JourneyType> journeyTypeList = mJourneyType.getStub();
		if (null != journeyTypeList) {

			// 获得分页的页数
			mGridPageTotalCount = journeyTypeList.size() / GIRD_ONE_PAGE_COUNT;
			int lastPageCount = journeyTypeList.size() % GIRD_ONE_PAGE_COUNT;
			if (lastPageCount != 0) {
				mGridPageTotalCount = mGridPageTotalCount + 1;
			}

			// 默认在计算器中显示第一个分类的图标
			if (journeyTypeList.size() > 0) {
				ImageLoader.getInstance().displayImage(journeyTypeList.get(0).getImg(), mCalculatorView.getIcomImageView(),
						ImageLoaderOptions.optionsBillAccoutingDefaultIcon);
			}

			mSelectCategoryViewPager = (ViewPager) findViewById(R.id.select_category_viewpager);

			// 生成分页的GridView并添加到ViewPager中
			List<View> mPagerViews = new ArrayList<View>();
			for (int i = 0; i < mGridPageTotalCount; i++) {
				final GridView gridView = (GridView) LayoutInflater.from(mContext).inflate(R.layout.bill_accounting_gridview, null);

				int startIndex = i * GIRD_ONE_PAGE_COUNT;
				int endIndex;
				if ((i == mGridPageTotalCount - 1) && (mGridPageTotalCount != 0)) {
					endIndex = journeyTypeList.size();
				} else {
					endIndex = startIndex + GIRD_ONE_PAGE_COUNT;
				}

				List<JourneyType> subJourneyTypeList = new ArrayList<JourneyType>(journeyTypeList.subList(startIndex, endIndex));
				BillAccountingGirdViewAdapter billAccountingGirdViewAdapter = new BillAccountingGirdViewAdapter(mContext);
				billAccountingGirdViewAdapter.setList(subJourneyTypeList);
				gridView.setAdapter(billAccountingGirdViewAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						if (mIsAnimationFinish) {
							final JourneyType journeyType = (JourneyType) parent.getAdapter().getItem(position);

							mIsAnimationFinish = false;
							SelectImageAnimation selectMembersAnimation = new SelectImageAnimation(BillAccountingActivity.this);
							selectMembersAnimation.setAnimationView(createAnimationView(journeyType.getImg()));
							selectMembersAnimation.setAniLocation(view, mCalculatorView.getIcomImageView());
							selectMembersAnimation.startAnimation(new OnSelectAnimation() {

								@Override
								public void onAnimationFinish() {
									ImageLoader.getInstance().displayImage(journeyType.getImg(), mCalculatorView.getIcomImageView(),
											ImageLoaderOptions.optionsBillAccoutingDefaultIcon);

									// 当前的JourneyType改变为选中的JourneyType
									mJourneyTypeItem = journeyType;
									mIsAnimationFinish = true;
								}
							});
						}
					}
				});
				mPagerViews.add(gridView);
			}

			mSelectCategoryPagerAdapter = new SelectCategoryPagerAdapter(mPagerViews);
			mSelectCategoryViewPager.setAdapter(mSelectCategoryPagerAdapter);
			mSelectCategoryViewPager.setCurrentItem(mCurPagerIndex);
			mSelectCategoryViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					setCurrentDot(position);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});

		}

	}

	private View createAnimationView(String imgUrl) {
		View aniView = LayoutInflater.from(mContext).inflate(R.layout.layout_bill_accounting_image, null);
		ImageView marksImgView = (ImageView) aniView.findViewById(R.id.marks_category_icon);
		ImageLoader.getInstance().displayImage(imgUrl, marksImgView, ImageLoaderOptions.optionsBillAccoutingDefaultIcon);
		return aniView;
	}

	/**
	 * 初始化小圆点指示区域
	 * 
	 * @param pageTotalCount
	 *            小圆点个数
	 */
	private void initIndicator(int pageTotalCount) {
		if (pageTotalCount > 0) {
			LinearLayout indicatorLayout = (LinearLayout) findViewById(R.id.select_category_viewpager_indicator);

			int imgViewPadding = 5;
			mIndicatorViews = new ImageView[pageTotalCount];
			for (int i = 0; i < pageTotalCount; i++) {
				ImageView imageView = new ImageView(mContext);
				imageView.setPadding(imgViewPadding, imgViewPadding, imgViewPadding, imgViewPadding);
				LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				imageView.setLayoutParams(layoutParams);
				imageView.setImageResource(R.drawable.btn_viewpager_dot);
				imageView.setEnabled(false);
				indicatorLayout.addView(imageView);
				// 将生成的ImageView对象存到数组中
				mIndicatorViews[i] = imageView;
			}

			mCurPagerIndex = DEF_CURRENT_POSITION;
			mIndicatorViews[mCurPagerIndex].setEnabled(true);// 默认第一个为选中状态
		}
	}

	/**
	 * 设置当前的小圆点为高亮状态
	 * 
	 * @param position
	 */
	private void setCurrentDot(int position) {
		if (position < 0 || position > mIndicatorViews.length) {
			return;
		}
		for (int i = 0; i < mIndicatorViews.length; i++) {
			if (position == i) {
				mIndicatorViews[i].setEnabled(true);
			} else {
				mIndicatorViews[i].setEnabled(false);
			}
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
					httpRequestUpLoadImage(new File(path));
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * ActionBar - 返回按钮事件
	 * 
	 * @param v
	 */
	protected void btnBackOnClick(View v) {
		finish();
	}

	/**
	 * ActionBar - 拍照备注点击事件
	 * 
	 * @param v
	 */
	protected void itemPhotoRemarkOnClick() {
		showPhotoRemarkDialog();
	}

	/**
	 * ActionBar - 选择日期点击事件
	 * 
	 * @param v
	 */
	protected void itemSelectDateOnClick(View v) {
		showTimePickerDialog();
	}

	/**
	 * 显示拍照备注对话框
	 */
	private void showPhotoRemarkDialog() {
		if (null == mPhotoRemarkDialog) {
			final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_photo_remark, null);
			mEdtInputRemark = (EditText) view.findViewById(R.id.edt_input_remark);
			mImgViewPhotoRemark = (ImageView) view.findViewById(R.id.img_view_photo_remark);
			mImgViewPhotoRemark.setVisibility(View.GONE);
			mImgViewPhotoRemarkAdd = (ImageView) view.findViewById(R.id.img_view_photo_remark_add);
			mImgViewPhotoRemarkAdd.setOnClickListener(this);
			view.findViewById(R.id.txt_view_save).setOnClickListener(this);
			view.findViewById(R.id.txt_view_cancel).setOnClickListener(this);
			mPhotoRemarkDialog = new Dialog(mContext, R.style.DialogBillAccounting);
			mPhotoRemarkDialog.setContentView(view);
			Window window = mPhotoRemarkDialog.getWindow();
			window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		}
		mPhotoRemarkDialog.show();
	}
	
	@SuppressLint("InflateParams")
    private void showTimePickerDialog() {
        if (null == mWheelSeletDatePanle) {
            mWheelSeletDatePanle = new CommonDatePickerWheelPanel(this);
            mWheelSeletDatePanle.setEnsureClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
					// 将默认的日期格式转化成yyyy-MM-dd格式
					mDatetime = DateUtil.format(mWheelSeletDatePanle.getDatetime(), "yyyyMMddHHmmss", "yyyy-MM-dd");
					mBillAccountingActionBar.setSelectDateChecked(true);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_view_save:
			itemPhotoRemarkSaveOnClick(v);
			break;
		case R.id.txt_view_cancel:
			itemPhotoRemarkCancelOnClick(v);
			break;
		case R.id.img_view_photo_remark_add:
			itemPhotoRemarkAddOnClick(v);
			break;
		default:
			break;
		}
	}

	private void itemPhotoRemarkSaveOnClick(View v) {
		mPhotoRemarkDialog.dismiss();
		mBillAccountingActionBar.setPhotoRemarkChecked(true);

		mStrRemarkNote = mEdtInputRemark.getText().toString();
	}

	private void itemPhotoRemarkCancelOnClick(View v) {
		mPhotoRemarkDialog.dismiss();
	}

	private void itemPhotoRemarkAddOnClick(View v) {
		String imageUrl = Util.createImagePath(mContext);
		if (null != imageUrl) {
			Intent intent = new Intent(this, ImageSelectActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("crop", false);
			bundle.putString("image-path", imageUrl);
			intent.putExtras(bundle);
			startActivityForResult(intent, REQ_PHOTO_CODE);
		}
	}

	/**
	 */
	private void showRemarkPhotoDialog() {
		if (null == mCommonAlertDialog) {
			mCommonAlertDialog = new CommonAlertDialog(mContext);
			mCommonAlertDialog.setTitle(R.string.txt_dialog_title_notice);
			mCommonAlertDialog.setMessage(R.string.txt_dialog_need_to_remark_photo);
			mCommonAlertDialog.setPositiveButton(R.string.txt_dialog_btn_yes, new OnClickListener() {

				@Override
				public void onClick(View v) {
					mCommonAlertDialog.dismiss();
					itemPhotoRemarkOnClick();
				}
			});
			mCommonAlertDialog.setNegativeButton(R.string.txt_dialog_btn_no, new OnClickListener() {

				@Override
				public void onClick(View v) {
					mCommonAlertDialog.dismiss();
					httpRequestUpLoadJourney(mJourneyType.getId(), mCTypeId, mStrRemarkNote, mUploadImageId, mDatetime, mCalResutl);
				}
			});
		}
		mCommonAlertDialog.show();
	}

	/**
	 * ViewPapger Adapter
	 * 
	 * @author Xun.Zhang
	 */
	class SelectCategoryPagerAdapter extends PagerAdapter {

		private List<View> mViewList;

		public SelectCategoryPagerAdapter(List<View> viewList) {
			mViewList = viewList;
		}

		@Override
		public int getCount() {
			return mViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mViewList.get(position), 0);
			return mViewList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			if (observer != null) {
				super.unregisterDataSetObserver(observer);
			}
		}

	}

	/**
	 * HTTP请求 - 上传文件到服务器 会返回url和id
	 * 
	 * @param file
	 */
	private void httpRequestUpLoadImage(File file) {
		LogUtil.d("httpRequestUpLoadImage File = " + file);

		RequestParams params = new RequestParams();
		try {
			params.put("img_1", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		params.add("user_id", ZxsqApplication.getInstance().getUser().getId());
		HttpClientApi.post(HttpClientApi.REQ_ZX_JOURNEY_UPLOAD_IMG, params, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onSuccess(Result result) {
				LogUtil.d("httpRequestUpLoadImage onSuccess Result = " + result);

				try {
					JSONObject obj = new JSONObject(result.data);
					JSONArray array = obj.optJSONArray("succeed");
					if (array != null && array.length() > 0) {
						mUploadImageId = array.optJSONObject(0).optInt("iid");
						String imageUrl = array.optJSONObject(0).optString("url");
						LogUtil.i("jiengyh upLoadImage result:" + result.data + "   mImageid:" + mUploadImageId + "url:" + imageUrl);
						ImageLoader.getInstance().displayImage(imageUrl, mImgViewPhotoRemark, ImageLoaderOptions.optionsDefaultEmptyPhoto);
						// 显示图片按钮并且添加点击事件
						mImgViewPhotoRemark.setVisibility(View.VISIBLE);
						mImgViewPhotoRemark.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								itemPhotoRemarkAddOnClick(v);
							}
						});
						// 隐藏添加按钮
						mImgViewPhotoRemarkAdd.setVisibility(View.GONE);
					} else {
						ToastUtil.showMessage(mContext, R.string.txt_upload_failed);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestUpLoadImage onFailure NetroidError = " + netroidError);

				showToastMsg(netroidError.getMessage());
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestUpLoadImage onFinish");

				hideWaitDialog();
			}

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestUpLoadImage onStart");

				showWaitDialog(getString(R.string.txt_on_waiting));
			}

		});
	}

	/**
	 * HTTP请求 - 上传记账信息
	 * 
	 * @param tType
	 * @param cType
	 * @param detail
	 * @param imgId
	 * @param date
	 * @param amount
	 */
	private void httpRequestUpLoadJourney(int tType, int cType, String detail, int imgId, String date, double amount) {
		LogUtil.d("httpRequestUpLoadJourney");

		RequestParams params = new RequestParams();
		params.add("t_type", String.valueOf(tType));
		params.add("c_type", String.valueOf(cType));
		params.add("detail", detail);
		if (imgId != 0) {
			JSONArray array = new JSONArray();
			array.put(imgId);
			params.add("imgs", array.toString());
		}
		params.add("date", date);
		params.add("amount", amount + "");
		HttpClientApi.post(HttpClientApi.REQ_ZX_JOURNEY_INPUT, params, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onSuccess(Result result) {
				LogUtil.i("httpRequestUpLoadJourney onSuccess Result = " + result);

				BroadCastManager.sendBroadCast(mContext, BroadCastManager.ACTION_BILL_ACCOUNTING_CHANGED);
				ActivityUtil.next(BillAccountingActivity.this, BillTotalDetailActivity.class, true);
			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.i("httpRequestUpLoadJourney onFailure NetroidError = " + netroidError);

				showToastMsg(netroidError.getMessage());
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestUpLoadJourney onFinish");

				hideWaitDialog();
			}

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestUpLoadJourney onStart");

				showWaitDialog(getString(R.string.txt_on_waiting));
			}
		});
	}

}
