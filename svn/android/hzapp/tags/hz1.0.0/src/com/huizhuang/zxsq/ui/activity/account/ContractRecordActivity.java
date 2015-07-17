package com.huizhuang.zxsq.ui.activity.account;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.ContractStatus;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.parser.ContractStatusParser;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.image.ImageSelectActivity;
import com.huizhuang.zxsq.utils.ImageLoaderUriUtils;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.ContractCheckZoneView;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 合同备案
 * 
 * @ClassName: ContractRecordActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-25 上午9:24:32
 */
public class ContractRecordActivity extends BaseActivity {

	private static final int REQ_PHOTO_CODE = 928;

	private DataLoadingLayout mDataLoadingLayout;
	private CommonAlertDialog mCommonAlertDialog;

	private ContractCheckZoneView mContractCheckZoneViewFrontCover;
	private ContractCheckZoneView mContractCheckZoneViewMoney;
	private ContractCheckZoneView mContractCheckZoneViewBackCover;

	private int mOrderId;
	private int mCurPhotoType;

	private File mFrontCoverFile;
	private File mMoneyCoverFile;
	private File mBackCoverFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_contract_record);

		getIntentExtras();
		initActionBar();
		initView();

		httpRequestQueryOrderPhotoStatus(mOrderId);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (Activity.RESULT_OK == resultCode) {
			switch (requestCode) {
			case REQ_PHOTO_CODE:
				if (null != intent) {
					String path = intent.getStringExtra("image-path");
					File photoFile = new File(path);
					String photoUrl = ImageLoaderUriUtils.getUriFromLocalFile(path);
					switch (mCurPhotoType) {
					case ContractStatus.IMAGE_TYPE_INDEX:
						mFrontCoverFile = photoFile;
						mContractCheckZoneViewFrontCover.setContractPhoto(photoUrl);
						break;
					case ContractStatus.IMAGE_TYPE_MONEY:
						mMoneyCoverFile = photoFile;
						mContractCheckZoneViewMoney.setContractPhoto(photoUrl);
						break;
					case ContractStatus.IMAGE_TYPE_END:
						mBackCoverFile = photoFile;
						mContractCheckZoneViewBackCover.setContractPhoto(photoUrl);
						break;
					default:
						break;
					}
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		// 当都没有上传过图片时
		if (null == mFrontCoverFile && null == mMoneyCoverFile && null == mBackCoverFile) {
			super.onBackPressed();
		} else {
			showAlertDialog();
		}

	}

	/**
	 * 获取Intent传递进来的数据
	 */
	private void getIntentExtras() {
		mOrderId = getIntent().getExtras().getInt(AppConstants.PARAM_ORDER_ID);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.txt_title_contract_record);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnBackOnClick(v);
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		LogUtil.d("initView");

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);

		mContractCheckZoneViewFrontCover = (ContractCheckZoneView) findViewById(R.id.contract_front_cover_zone);
		mContractCheckZoneViewFrontCover.setRelActivity(this);
		mContractCheckZoneViewFrontCover.setLeftExampleImage(R.drawable.ic_contract_record_1);
		mContractCheckZoneViewFrontCover.setUploadText(R.string.txt_contract_record_take_photo_1);
		mContractCheckZoneViewFrontCover.setContractUploadOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurPhotoType = ContractStatus.IMAGE_TYPE_INDEX;
				showUploadPhotoDialog();
			}
		});
		mContractCheckZoneViewFrontCover.setBtnCommitOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mFrontCoverFile) {
					httpRequestUploadContractPhoto(mOrderId, ContractStatus.IMAGE_TYPE_INDEX, mFrontCoverFile);
				}
			}
		});

		mContractCheckZoneViewMoney = (ContractCheckZoneView) findViewById(R.id.contract_money_zone);
		mContractCheckZoneViewMoney.setRelActivity(this);
		mContractCheckZoneViewMoney.setLeftExampleImage(R.drawable.ic_contract_record_2);
		mContractCheckZoneViewMoney.setUploadText(R.string.txt_contract_record_take_photo_2);
		mContractCheckZoneViewMoney.setContractUploadOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurPhotoType = ContractStatus.IMAGE_TYPE_MONEY;
				showUploadPhotoDialog();
			}
		});
		mContractCheckZoneViewMoney.setBtnCommitOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mMoneyCoverFile) {
					httpRequestUploadContractPhoto(mOrderId, ContractStatus.IMAGE_TYPE_MONEY, mMoneyCoverFile);
				}
			}
		});

		mContractCheckZoneViewBackCover = (ContractCheckZoneView) findViewById(R.id.contract_back_cover_zone);
		mContractCheckZoneViewBackCover.setRelActivity(this);
		mContractCheckZoneViewBackCover.setLeftExampleImage(R.drawable.ic_contract_record_3);
		mContractCheckZoneViewBackCover.setUploadText(R.string.txt_contract_record_take_photo_3);
		mContractCheckZoneViewBackCover.setContractUploadOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurPhotoType = ContractStatus.IMAGE_TYPE_END;
				showUploadPhotoDialog();
			}
		});
		mContractCheckZoneViewBackCover.setBtnCommitOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mBackCoverFile) {
					httpRequestUploadContractPhoto(mOrderId, ContractStatus.IMAGE_TYPE_END, mBackCoverFile);
				}
			}
		});
	}

	/**
	 * 显示退出提示对话框
	 */
	private void showAlertDialog() {
		LogUtil.d("showAlertDialog");

		if (null == mCommonAlertDialog) {
			mCommonAlertDialog = new CommonAlertDialog(this);
			mCommonAlertDialog.setTitle(R.string.txt_contract_record_quit_title);
			mCommonAlertDialog.setMessage(R.string.txt_contract_record_quit_message);
			mCommonAlertDialog.setPositiveButton(R.string.txt_contract_record_sure, new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCommonAlertDialog.dismiss();
					finish();
				}
			});
			mCommonAlertDialog.setNegativeButton(R.string.txt_contract_record_cancel, new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCommonAlertDialog.dismiss();
				}
			});
		}
		mCommonAlertDialog.show();
	}

	/**
	 * 按钮事件 - 返回
	 * 
	 * @param v
	 */
	private void btnBackOnClick(View v) {
		LogUtil.d("btnBackOnClick View = " + v);
		onBackPressed();
	}

	/**
	 * 显示选择图片对话框
	 */
	private void showUploadPhotoDialog() {
		String imageUrl = Util.createImagePath(this);
		String viewImageUrl = null;
		switch (mCurPhotoType) {
		case ContractStatus.IMAGE_TYPE_INDEX:
			viewImageUrl = mContractCheckZoneViewFrontCover.getContractPhoto();
			break;
		case ContractStatus.IMAGE_TYPE_MONEY:
			viewImageUrl = mContractCheckZoneViewMoney.getContractPhoto();
			break;
		case ContractStatus.IMAGE_TYPE_END:
			viewImageUrl = mContractCheckZoneViewBackCover.getContractPhoto();
			break;
		default:
			break;
		}
		if (null != imageUrl) {
			Intent intent = new Intent(this, ImageSelectActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("crop", false);
			bundle.putString("image-path", imageUrl);
			bundle.putString("view-image-uri", viewImageUrl);
			intent.putExtras(bundle);
			startActivityForResult(intent, REQ_PHOTO_CODE);
		}
	}

	/**
	 * HTTP请求 - 获取合同图片的状态
	 * 
	 * @param orderId
	 */
	private void httpRequestQueryOrderPhotoStatus(final int orderId) {
		LogUtil.d("httpRequestQueryOrderPhotoStatus orderId = " + orderId);

		RequestParams requestParams = new RequestParams();
		requestParams.add("order_id", String.valueOf(orderId));
		HttpClientApi.get(HttpClientApi.REQ_ACCOUNT_GET_ORDER_STATUS, requestParams, new ContractStatusParser(), new RequestCallBack<List<ContractStatus>>() {

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestQueryOrderPhotoStatus onStart");

				mDataLoadingLayout.showDataLoading();

			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestQueryOrderPhotoStatus onFinish");
			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestQueryOrderPhotoStatus onFailure NetroidError = " + netroidError);

				mDataLoadingLayout.showDataLoadFailed(netroidError.getMessage());
			}

			@Override
			public void onSuccess(List<ContractStatus> contractStatusList) {
				LogUtil.d("httpRequestQueryOrderPhotoStatus onSuccess contractStatusList = " + contractStatusList);

				mDataLoadingLayout.showDataLoadSuccess();
				if (null != contractStatusList && contractStatusList.size() > 0) {
					for (ContractStatus contractStatus : contractStatusList) {
						int photoType = contractStatus.getType();
						int photoStatus = contractStatus.getStatu();
						String photoUrl = null;
						if (contractStatus.getImage() != null) {
							photoUrl = contractStatus.getImage().getImg_path();
						}
						switch (photoType) {
						case ContractStatus.IMAGE_TYPE_INDEX:
							mContractCheckZoneViewFrontCover.setContractStatus(photoStatus);
							mContractCheckZoneViewFrontCover.setContractPhoto(photoUrl);
							break;
						case ContractStatus.IMAGE_TYPE_MONEY:
							mContractCheckZoneViewMoney.setContractStatus(photoStatus);
							mContractCheckZoneViewMoney.setContractPhoto(photoUrl);
							break;
						case ContractStatus.IMAGE_TYPE_END:
							mContractCheckZoneViewBackCover.setContractStatus(photoStatus);
							mContractCheckZoneViewBackCover.setContractPhoto(photoUrl);
							break;
						default:
							break;
						}
					}

				}
			}

		});
	}

	/**
	 * HTTP请求 - 上传合同图片
	 * 
	 * @param orderId
	 * @param photoType
	 * @param photoFile
	 */
	private void httpRequestUploadContractPhoto(final int orderId, final int photoType, final File photoFile) {
		LogUtil.d("httpRequestUploadContractPhoto orderId = " + orderId + " photoType = " + photoType + " photoFile = " + photoFile);

		RequestParams requestParams = new RequestParams();
		requestParams.add("order_id", String.valueOf(orderId));
		requestParams.add("type", String.valueOf(photoType));
		try {
			requestParams.put("img", photoFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		HttpClientApi.post(HttpClientApi.REQ_ACCOUNT_UPDATE_ORDER_STATUS, requestParams, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestUploadContractPhoto onStart");

				showWaitDialog(getString(R.string.txt_on_waiting));
			}

			@Override
			public void onProgress(long fileSize, long downloadedSize) {
				LogUtil.i("  fileSize:" + fileSize + " downloadedSize:" + downloadedSize);
				super.onProgress(fileSize, downloadedSize);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestUploadContractPhoto onFinish");

				hideWaitDialog();

			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestUploadContractPhoto onFailure NetroidError = " + netroidError);

				showToastMsg(netroidError.getMessage());
			}

			@Override
			public void onSuccess(Result result) {
				LogUtil.d("httpRequestUploadContractPhoto onSuccess Result = " + result);

				showToastMsg(getString(R.string.txt_contract_record_upload_photo_success));

				switch (photoType) {
				case ContractStatus.IMAGE_TYPE_INDEX:
					mContractCheckZoneViewFrontCover.setContractStatus(ContractStatus.STATU_UPDATED);
					mFrontCoverFile = null;
					break;
				case ContractStatus.IMAGE_TYPE_MONEY:
					mContractCheckZoneViewMoney.setContractStatus(ContractStatus.STATU_UPDATED);
					mMoneyCoverFile = null;
					break;
				case ContractStatus.IMAGE_TYPE_END:
					mContractCheckZoneViewBackCover.setContractStatus(ContractStatus.STATU_UPDATED);
					mBackCoverFile = null;
					break;
				default:
					break;
				}
			}

		});
	}

}
