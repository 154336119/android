package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;

/**
 * 装修清单Item条
 * 
 * @ClassName: DecorationListItemBar
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-28 上午11:02:00
 */
public class DecorationListItemBar extends RelativeLayout {

	private TextView mTvTitle;
	private TextView mTvPrice;
	private View mViewLine;

	private Context mContext;

	public DecorationListItemBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context, attrs);
	}

	public DecorationListItemBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context, attrs);
	}

	public DecorationListItemBar(Context context) {
		super(context);
		initViews(context, null);
	}

	private void initViews(Context context, AttributeSet attrs) {
		mContext = context;
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.decoration_list_item_bar, this);
		mTvTitle = (TextView) rootLayout.findViewById(R.id.tv_title);
		mTvPrice = (TextView) rootLayout.findViewById(R.id.tv_price);
		mViewLine = rootLayout.findViewById(R.id.v_line);
	}

	/**
	 * 设置显示的内容
	 * 
	 * @param title
	 *            标题
	 * @param price
	 *            价格
	 */
	public void setContent(String title, double price) {
		mTvTitle.setText(title);
		mTvPrice.setText(mContext.getString(R.string.txt_budget_format, price));
	}

	/**
	 * 设置是否显示下划线
	 * 
	 * @param enable
	 */
	public void enableViewLine(boolean enable) {
		if (enable) {
			mViewLine.setVisibility(View.VISIBLE);
		} else {
			mViewLine.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置字体为20sp的大字体模式
	 */
	public void setBigTextMode() {
		float bigTextSize = 20; // 20sp
		mTvTitle.setTextSize(bigTextSize);
		mTvPrice.setTextSize(bigTextSize);
	}
}
