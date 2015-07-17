package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;

/**
 * 房屋信息Item条
 * 
 * @ClassName: RoomInfoItemBar
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-28 上午11:02:00
 */
public class RoomInfoItemBar extends RelativeLayout {

	private ViewGroup mItemClickZone;
	private ImageView mImgIndicator;
	private EditText mItemContent;
	private TextView mItemTvUnit;

	public RoomInfoItemBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context, attrs);
	}

	public RoomInfoItemBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context, attrs);
	}

	public RoomInfoItemBar(Context context) {
		super(context);
		initViews(context, null);
	}

	private void initViews(Context context, AttributeSet attrs) {
		int txtInfoResId = -1;
		boolean showImgIndicator = true;
		if (null != attrs) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HouseInfoItem);
			txtInfoResId = typedArray.getResourceId(R.styleable.HouseInfoItem_itemTitle, R.string.txt_house_type);
			showImgIndicator = typedArray.getBoolean(R.styleable.HouseInfoItem_imgIndicator, true);
			typedArray.recycle();
		} else {
			txtInfoResId = R.string.txt_house_type;
		}
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.room_info_item_bar, this);
		TextView itemTitle = (TextView) rootLayout.findViewById(R.id.item_title);
		mItemClickZone = (ViewGroup) rootLayout.findViewById(R.id.item_click_zone);
		mItemContent = (EditText) rootLayout.findViewById(R.id.txt_content);
		mItemTvUnit = (TextView) rootLayout.findViewById(R.id.txt_unit_of_area);
		mItemTvUnit.setVisibility(View.GONE);
		itemTitle.setText(txtInfoResId);

		mImgIndicator = (ImageView) rootLayout.findViewById(R.id.img_indicator);
		if (showImgIndicator) {
			mImgIndicator.setVisibility(View.VISIBLE);
			mItemContent.setEnabled(false);
		} else {
			mImgIndicator.setVisibility(View.GONE);
			mItemContent.setEnabled(true);
		}
	}

	public void setContent(String content) {
		mItemContent.setText(content);
	}

	public String getContent() {
		return mItemContent.getText().toString().trim();
	}

	public void setInputOnlyNumber() {
		mItemContent.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	/**
	 * @param enabled
	 */
	public void setItemEnabled(boolean enabled) {
		this.setEnabled(enabled);
		mItemContent.setEnabled(enabled);
		mItemClickZone.setEnabled(enabled);
		// 不显示下拉箭头
		if (!enabled) {
			mImgIndicator.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置点击区域事件
	 * 
	 * @param onClickListener
	 */
	public void setItemOnClickListener(View.OnClickListener onClickListener) {
		mItemClickZone.setOnClickListener(onClickListener);
	}

	/**
	 * 设置输入框提示事件
	 * 
	 * @param resId
	 */
	public void setInputHint(int resId) {
		mItemContent.setHint(resId);
	}

	/**
	 * 设置输入框最大输入字符数
	 * 
	 * @param length
	 */
	public void setInputMaxLength(int length) {
		if (length > 0) {
			mItemContent.setFilters(new InputFilter[] { new InputFilter.LengthFilter(length) });
		}
	}

	/**
	 * 显示面积单位
	 */
	public void setItemUnitEnable() {
		mItemTvUnit.setVisibility(View.VISIBLE);
	}

}
