package com.huizhuang.zxsq.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.ContractStatus;
import com.huizhuang.zxsq.ui.activity.ImageSimpleBrowseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;

/**
 * 合同备案审核区域视图
 * 
 * @ClassName: ContractCheckZoneView.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-25 上午10:51:45
 */
public class ContractCheckZoneView extends RelativeLayout {

	private ViewGroup mFrameLayoutContractZone, mContractPhotoStateZone;
	private ImageView mIvLeftExample, mIvConractPhoto;
	private TextView mTvUploadText, mTvContractPhotoState, mTvContractPhotoStateTip;
	private Button mBtnCommit;

	private Activity mRelActivity;
	private String mImageUrl;

	public ContractCheckZoneView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public ContractCheckZoneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public ContractCheckZoneView(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.layout_contract_check_zone_view, this);
		mFrameLayoutContractZone = (ViewGroup) rootLayout.findViewById(R.id.fl_contract_zone);
		mContractPhotoStateZone = (ViewGroup) rootLayout.findViewById(R.id.tv_contract_photo_state_zone);
		mIvLeftExample = (ImageView) rootLayout.findViewById(R.id.iv_left_example);
		mIvConractPhoto = (ImageView) rootLayout.findViewById(R.id.iv_contract_photo);
		mTvUploadText = (TextView) rootLayout.findViewById(R.id.tv_upload_text);
		mTvContractPhotoState = (TextView) rootLayout.findViewById(R.id.tv_contract_photo_state);
		mTvContractPhotoStateTip = (TextView) rootLayout.findViewById(R.id.tv_contract_photo_state_tip);
		mBtnCommit = (Button) rootLayout.findViewById(R.id.btn_commit);
	}

	/**
	 * 设置左边示例封面
	 * 
	 * @param resId
	 */
	public void setLeftExampleImage(int resId) {
		mIvLeftExample.setImageResource(resId);
	}

	/**
	 * 设置上传的图片
	 * 
	 * @param resId
	 */
	public void setContractPhoto(String url) {
		mImageUrl = url;
		mIvConractPhoto.setVisibility(View.VISIBLE);
		ImageUtil.displayImage(url, mIvConractPhoto, ImageLoaderOptions.optionsDefaultEmptyPhoto);
	}

	/**
	 * 获取上传的图片
	 * 
	 * @return
	 */
	public String getContractPhoto() {
		return mImageUrl;
	}

	/**
	 * 设置显示上传的文字信息
	 * 
	 * @param resId
	 */
	public void setUploadText(int resId) {
		mTvUploadText.setText(resId);
	}

	/**
	 * 设置按钮事件
	 * 
	 * @param resId
	 */
	public void setBtnCommitOnClickListener(View.OnClickListener onClickListener) {
		mBtnCommit.setOnClickListener(onClickListener);
	}

	/**
	 * 设置上传图片事件
	 * 
	 * @param onClickListener
	 */
	public void setContractUploadOnClickListener(View.OnClickListener onClickListener) {
		mFrameLayoutContractZone.setOnClickListener(onClickListener);
	}

	/**
	 * 
	 * 
	 * @param ContractStatus
	 */
	/**
	 * 设置合同图片状态
	 * 
	 * @param statusId
	 *            ContractStatus中的四种状态
	 */
	public void setContractStatus(int statusId) {
		switch (statusId) {
		case ContractStatus.STATU_NOT_UPDATE: // 未上传
			mContractPhotoStateZone.setVisibility(View.GONE);
			mIvConractPhoto.setVisibility(View.GONE);
			// 允许点击事件
			mFrameLayoutContractZone.setEnabled(true);
			mBtnCommit.setEnabled(true);
			mBtnCommit.setText(R.string.txt_contract_record_commit);
			break;
		case ContractStatus.STATU_UPDATED: // 审核中
			mContractPhotoStateZone.setVisibility(View.VISIBLE);
			mIvConractPhoto.setVisibility(View.VISIBLE);
			// 屏蔽点击事件
			mFrameLayoutContractZone.setEnabled(true);
			// 设置图片可以查看
			mFrameLayoutContractZone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (null != mImageUrl && null != mRelActivity) {
						ArrayList<String> imageUrlList = new ArrayList<String>();
						imageUrlList.add(mImageUrl);
						Bundle bundle = new Bundle();
						bundle.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS, imageUrlList);
						ActivityUtil.next(mRelActivity, ImageSimpleBrowseActivity.class, bundle, -1);
					}
				}
			});
			mBtnCommit.setEnabled(false);
			mBtnCommit.setText(R.string.txt_contract_record_commit);
			mTvContractPhotoState.setText(R.string.txt_contract_record_checking);
			mTvContractPhotoStateTip.setVisibility(View.GONE);
			break;
		case ContractStatus.STATU_NOT_PASS: // 审核没通过
			mContractPhotoStateZone.setVisibility(View.VISIBLE);
			mIvConractPhoto.setVisibility(View.VISIBLE);
			// 允许点击事件
			mFrameLayoutContractZone.setEnabled(true);
			mBtnCommit.setEnabled(true);
			// 显示为重新上传
			mBtnCommit.setText(R.string.txt_contract_record_re_upload);
			mTvContractPhotoState.setText(R.string.txt_contract_record_check_failed);
			mTvContractPhotoStateTip.setVisibility(View.VISIBLE);
			mTvContractPhotoStateTip.setText(R.string.txt_contract_record_check_failed_tip);
			break;
		case ContractStatus.STATU_PASSED: // 审核通过
			mContractPhotoStateZone.setVisibility(View.VISIBLE);
			mIvConractPhoto.setVisibility(View.VISIBLE);
			// 屏蔽点击事件
			mFrameLayoutContractZone.setEnabled(false);
			mBtnCommit.setEnabled(false);
			mBtnCommit.setText(R.string.txt_contract_record_commit);
			mTvContractPhotoState.setText(R.string.txt_contract_record_check_success);
			mTvContractPhotoStateTip.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}

	/**
	 * 设置关联的Activity
	 * 
	 * @param relActivity
	 */
	public void setRelActivity(Activity relActivity) {
		mRelActivity = relActivity;
	}

}
