package com.huizhuang.zxsq.widget.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;

/**
 * 通用ActionBar
 * 
 * @ClassName: CommonActionBar
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-28 上午11:02:00
 */
public class CommonActionBar extends RelativeLayout {

	/**
	 * 左边图标按钮
	 */
	private ImageButton mImgBtnLeft;

	/**
	 * 左边文字按钮
	 */
	private Button mBtnLeft;

	/**
	 * 中间文字标题
	 */
	private TextView mTvTitle;

	/**
	 * 右边图标按钮
	 */
	private ImageButton mImgBtnRight;

	/**
	 * 右边文字按钮
	 */
	private Button mTxtBtnRight;

	public CommonActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public CommonActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public CommonActionBar(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.common_action_bar, this);
		mBtnLeft = (Button) rootLayout.findViewById(R.id.btn_left);
		mImgBtnLeft = (ImageButton) rootLayout.findViewById(R.id.img_btn_left);
		mTvTitle = (TextView) rootLayout.findViewById(R.id.tv_title);
		mTxtBtnRight = (Button) rootLayout.findViewById(R.id.btn_right);
		mImgBtnRight = (ImageButton) rootLayout.findViewById(R.id.img_btn_right);
	}

	/**
	 * 设置ActionBar标题
	 * 
	 * @param txtResId
	 */
	public void setActionBarTitle(int txtResId) {
		mTvTitle.setText(txtResId);
	}

	/**
	 * 设置ActionBar标题
	 * 
	 * @param strTitle
	 */
	public void setActionBarTitle(String strTitle) {
		mTvTitle.setText(strTitle);
	}

	/**
	 * 设置左边文字按钮及事件
	 * 
	 * @param imgResId
	 *            资源ID
	 * @param onClickListener
	 *            事件
	 */
	public void setLeftBtn(int txtResId, OnClickListener onClickListener) {
		mBtnLeft.setVisibility(View.VISIBLE);
		mBtnLeft.setText(txtResId);
		mBtnLeft.setOnClickListener(onClickListener);
		// 隐藏左边图标按钮
		mImgBtnLeft.setVisibility(View.GONE);
	}
	
	/**
	 * 设置左边文字按钮及事件
	 * 
	 * @param imgResId
	 *            资源ID
	 * @param onClickListener
	 *            事件
	 */
	public void setLeftBtn(int imgResId, int txtResId, OnClickListener onClickListener) {
		mBtnLeft.setVisibility(View.VISIBLE);
		mBtnLeft.setCompoundDrawablesWithIntrinsicBounds(imgResId, 0, 0, 0);
		mBtnLeft.setText(txtResId);
		mBtnLeft.setOnClickListener(onClickListener);
		// 隐藏左边图标按钮
		mImgBtnLeft.setVisibility(View.GONE);
	}
	
	/**
	 * 设置左边文字按钮及事件
	 * 
	 * @param imgResId
	 *            资源ID
	 * @param onClickListener
	 *            事件
	 */
	public void setLeftBtn(int imgResId, String txt, OnClickListener onClickListener) {
		mBtnLeft.setVisibility(View.VISIBLE);
		mBtnLeft.setCompoundDrawablesWithIntrinsicBounds(imgResId, 0, 0, 0);
        mBtnLeft.setCompoundDrawablePadding(8);
		mBtnLeft.setText(txt);
		mBtnLeft.setOnClickListener(onClickListener);
		// 隐藏左边图标按钮
		mImgBtnLeft.setVisibility(View.GONE);
	}

	/**
	 * 设置左边图标按钮及事件
	 * 
	 * @param imageResId
	 *            资源ID
	 * @param onClickListener
	 *            事件
	 */
	public void setLeftImgBtn(int imageResId, OnClickListener onClickListener) {
		mImgBtnLeft.setVisibility(View.VISIBLE);
		mImgBtnLeft.setImageResource(imageResId);
		mImgBtnLeft.setOnClickListener(onClickListener);
		// 隐藏左边文字按钮
		mBtnLeft.setVisibility(View.GONE);
	}

	/**
	 * 设置右边图标按钮及事件
	 * 
	 * @param imageResId
	 *            资源ID
	 * @param onClickListener
	 *            事件
	 */
	public void setRightImgBtn(int imageResId, OnClickListener onClickListener) {
		mImgBtnRight.setVisibility(View.VISIBLE);
		mImgBtnRight.setImageResource(imageResId);
		mImgBtnRight.setOnClickListener(onClickListener);
		// 隐藏右边文字按钮
		mTxtBtnRight.setVisibility(View.GONE);
	}

	/**
	 * 设置右边图标按钮是否显示
	 * 
	 * @param visibility
	 */
	public void setRightImgBtnVisibility(int visibility) {
		mImgBtnRight.setVisibility(visibility);
	}

	/**
	 * 设置右边文字按钮及事件
	 * 
	 * @param txtResId
	 *            资源ID
	 * @param onClickListener
	 *            事件
	 */
	public void setRightTxtBtn(int txtResId, OnClickListener onClickListener) {
		mTxtBtnRight.setVisibility(View.VISIBLE);
		mTxtBtnRight.setText(txtResId);
		mTxtBtnRight.setOnClickListener(onClickListener);
		// 隐藏右边图标按钮
		mImgBtnRight.setVisibility(View.GONE);
	}

	/**
	 * 设置右边文字按钮是否显示
	 * 
	 * @param visibility
	 */
	public void setRightTxtBtnVisibility(int visibility) {
		mTxtBtnRight.setVisibility(visibility);
	}

}
