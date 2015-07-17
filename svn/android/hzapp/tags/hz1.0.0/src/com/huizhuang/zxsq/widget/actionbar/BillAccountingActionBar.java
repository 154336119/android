package com.huizhuang.zxsq.widget.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huizhuang.hz.R;

/**
 * 记账页面专用ActionBar
 * 
 * @ClassName: BillAccountingActionBar
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-31 16:51:00
 */
public class BillAccountingActionBar extends RelativeLayout {

	private ImageButton mImgBtnBack;
	private ViewGroup mPhotoRemarkZone;
	private ImageView mImgPhotoRemark;
	private ViewGroup mSelectDateZone;
	private ImageView mImgSelectDate;

	public BillAccountingActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public BillAccountingActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public BillAccountingActionBar(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.action_bar_bill_accounting, this);
		mImgBtnBack = (ImageButton) rootLayout.findViewById(R.id.img_btn_back);
		mPhotoRemarkZone = (ViewGroup) rootLayout.findViewById(R.id.ll_photo_remark_zone);
		mImgPhotoRemark = (ImageView) rootLayout.findViewById(R.id.img_photo_remark);
		mSelectDateZone = (ViewGroup) rootLayout.findViewById(R.id.ll_choose_date_zone);
		mImgSelectDate = (ImageView) rootLayout.findViewById(R.id.img_choose_data);
	}

	/**
	 * 设置点击事件 - 返回
	 * 
	 * @param onClickListener
	 */
	public void setBtnBackOnClick(View.OnClickListener onClickListener) {
		mImgBtnBack.setOnClickListener(onClickListener);
	}

	/**
	 * 设置选中事件 - 拍照备注
	 * 
	 * @param isChecked
	 */
	public void setPhotoRemarkChecked(boolean isChecked) {
		int imgResId = isChecked ? R.drawable.ic_bill_checked : R.drawable.ic_bill_uncheck;
		mImgPhotoRemark.setImageResource(imgResId);
	}

	/**
	 * 设置选中事件 - 日期选择
	 * 
	 * @param isChecked
	 */
	public void setSelectDateChecked(boolean isChecked) {
		int imgResId = isChecked ? R.drawable.ic_bill_checked : R.drawable.ic_bill_uncheck;
		mImgSelectDate.setImageResource(imgResId);
	}

	/**
	 * 设置点击事件 - 拍照备注
	 * 
	 * @param onClickListener
	 */
	public void setPhotoRemarkOnClick(View.OnClickListener onClickListener) {
		mPhotoRemarkZone.setOnClickListener(onClickListener);
	}

	/**
	 * 设置点击事件 - 日期选择
	 * 
	 * @param onClickListener
	 */
	public void setSelectDateOnClick(View.OnClickListener onClickListener) {
		mSelectDateZone.setOnClickListener(onClickListener);
	}

}
