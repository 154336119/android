package com.huizhuang.zxsq.widget.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 顶部分类过滤条
 * 
 * @ClassName: TopTypeFilterBar.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-12-17 下午4:19:48
 */
public class PictureListFilterBar extends RelativeLayout implements OnClickListener {

	public enum TopType {
        FILTER_STYLE, // 风格
        FILTER_SPACE, // 空间
        FILTER_PARTS, // 局部
		FILTER_TYPE // 风格
	}

	public static final String[] FILTER_NAMES = { "风格", "空间", "局部", "户型" };

    private Context mContext;

	/**
	 * 顶部文本显示区域
	 */
	private TextView mTvFilter1;
	private TextView mTvFilter2;
	private TextView mTvFilter3;
	private TextView mTvFilter4;
	private Drawable mDrawableIndicatorUp;
	private Drawable mDrawableIndicatorDown;

	/**
	 * 弹出区域
	 */
	private ListView mListViewDropDown;
	private FilterDropdownAdapter mFilterDropdownAdapter;
    private PopupWindow mOptionPopupWindow;//筛选条件popWindow
	/**
	 * 记录当前焦点的TextView
	 */
	private TextView mTvFilterFocus;

	/**
	 * 数据区
	 */
	private List<KeyValue> mKeyValueList1;
	private List<KeyValue> mKeyValueList2;
	private List<KeyValue> mKeyValueList3;
	private List<KeyValue> mKeyValueList4;

	private OnDropdownBarItemClickListener mOnDropdownBarItemClickListener;

	public interface OnDropdownBarItemClickListener {
		void onItemClick(TopType topType, KeyValue filterKeyValue);
	}

	public PictureListFilterBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public PictureListFilterBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public PictureListFilterBar(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
        mContext = context;
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.widget_top_type_filter_bar, this);

		mTvFilter1 = (TextView) rootLayout.findViewById(R.id.tv_filter_1);
		mTvFilter1.setText(FILTER_NAMES[0]);
		mTvFilter2 = (TextView) rootLayout.findViewById(R.id.tv_filter_2);
		mTvFilter2.setText(FILTER_NAMES[1]);
		mTvFilter3 = (TextView) rootLayout.findViewById(R.id.tv_filter_3);
		mTvFilter3.setText(FILTER_NAMES[2]);
		mTvFilter4 = (TextView) rootLayout.findViewById(R.id.tv_filter_4);
		mTvFilter4.setText(FILTER_NAMES[3]);

		mTvFilter1.setOnClickListener(this);
		mTvFilter2.setOnClickListener(this);
		mTvFilter3.setOnClickListener(this);
		mTvFilter4.setOnClickListener(this);

		mDrawableIndicatorDown = getResources().getDrawable(R.drawable.ic_filter_indicator_down);
		mDrawableIndicatorDown.setBounds(0, 0, mDrawableIndicatorDown.getMinimumWidth(), mDrawableIndicatorDown.getMinimumHeight());

		mDrawableIndicatorUp = getResources().getDrawable(R.drawable.ic_filter_indicator_up);
		mDrawableIndicatorUp.setBounds(0, 0, mDrawableIndicatorDown.getMinimumWidth(), mDrawableIndicatorDown.getMinimumHeight());

		initData();
	}

    @SuppressLint("InflateParams")
    private void showPopUp(final View selectedView,List<KeyValue> keyValueList) {
        //筛选条件
        View view = LayoutInflater.from(mContext).inflate(R.layout.activites_filter_condition_popup, null);
        mListViewDropDown = (ListView) view.findViewById(R.id.list_view_drop_down);
        mFilterDropdownAdapter = new FilterDropdownAdapter(mContext);
        mFilterDropdownAdapter.setList(keyValueList);
        mListViewDropDown.setAdapter(mFilterDropdownAdapter);
        // 将当前TextView绑定的选中ID设置到Adapter中
        mFilterDropdownAdapter.setSelectedKeyValueId((String) selectedView.getTag());
        mListViewDropDown.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KeyValue filterKeyValue = (KeyValue) parent.getAdapter().getItem(position);
                if (null != mTvFilterFocus) {
                    // 将选中ID设置到当前的TextView中
                    mTvFilterFocus.setTag(filterKeyValue.getId());
                    hideDropdownBar();

                    TopType topType = TopType.FILTER_STYLE;
                    if (mTvFilter1 == mTvFilterFocus) {
                        topType = TopType.FILTER_STYLE;
                    } else if (mTvFilter2 == mTvFilterFocus) {
                        topType = TopType.FILTER_SPACE;
                    } else if (mTvFilter3 == mTvFilterFocus) {
                        topType = TopType.FILTER_PARTS;
                    } else if (mTvFilter4 == mTvFilterFocus) {
                        topType = TopType.FILTER_TYPE;
                    }

                    if (null != mOnDropdownBarItemClickListener) {
                        mOnDropdownBarItemClickListener.onItemClick(topType, filterKeyValue);
                    }
                }
            }
        });

        int width = DensityUtil.getScreenWidth((Activity)mContext);
        int height = DensityUtil.getScreenHeight((Activity)mContext)- DensityUtil.dip2px(mContext, 186.0f);
        mOptionPopupWindow = new PopupWindow(view , width, height);
        // 设置点击返回键使其消失，且不影响背景，此时setOutsideTouchable函数即使设置为false
        // 点击PopupWindow 外的屏幕，PopupWindow依然会消失；相反，如果不设置BackgroundDrawable
        // 则点击返回键PopupWindow不会消失，同时，即时setOutsideTouchable设置为true
        // 点击PopupWindow 外的屏幕，PopupWindow依然不会消失
        mOptionPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mOptionPopupWindow.setOutsideTouchable(true);  // 设置是否允许在外点击使其消失
        mOptionPopupWindow.setAnimationStyle(R.style.PopupAnimationAt); // 设置动画
        mOptionPopupWindow.setFocusable(true);
        mOptionPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                hideDropdownBar();
            }
        });

        mOptionPopupWindow.update();
        mOptionPopupWindow.showAsDropDown(selectedView,0,2);
    }

	/**
	 * 是否已经显示下拉框
	 * 
	 * @return
	 */
	public boolean isDropdownBarShowing() {
		return mOptionPopupWindow.isShowing();
	}

	/**
	 * 隐藏下拉框
	 */
	public void hideDropdownBar() {
		mOptionPopupWindow.dismiss();
		if (null != mTvFilterFocus) {
			setIndicatorExpand(mTvFilterFocus, false);
		}
	}

	/**
	 * 设置DropdownBar点击事件
	 * 
	 * @param onDropdownBarItemClickListener
	 */
	public void setOnDropdownBarItemClickListener(OnDropdownBarItemClickListener onDropdownBarItemClickListener) {
		mOnDropdownBarItemClickListener = onDropdownBarItemClickListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_filter_1:
            AnalyticsUtil.onEvent(mContext, "click40");
			onTextTitleClick(mTvFilter1, mKeyValueList1);
			break;
		case R.id.tv_filter_2:
            AnalyticsUtil.onEvent(mContext, "click41");
			onTextTitleClick(mTvFilter2, mKeyValueList2);
			break;
		case R.id.tv_filter_3:
            AnalyticsUtil.onEvent(mContext, "click42");
			onTextTitleClick(mTvFilter3, mKeyValueList3);
			break;
		case R.id.tv_filter_4:
            AnalyticsUtil.onEvent(mContext, "click43");
			onTextTitleClick(mTvFilter4, mKeyValueList4);
			break;
		case R.id.rl_drop_down_zone:
			hideDropdownBar();
			break;
		default:
			break;
		}
	}

	/**
	 * 设置当前的TextView文字和Adapter的数据
	 * 
	 * @param currentTextView
	 * @param keyValueList
	 */
	private void onTextTitleClick(TextView currentTextView, List<KeyValue> keyValueList) {
		if (null != keyValueList) {
			if (null != mTvFilterFocus) {
				setIndicatorExpand(mTvFilterFocus, false);
			}
			setIndicatorExpand(currentTextView, true);
			mTvFilterFocus = currentTextView;
            showPopUp(currentTextView,keyValueList);
		}
	}

	/**
	 * TextView设置CompoundDrawables属性
	 * 
	 * @param targetTextView
	 * @param isExpand
	 */
	private void setIndicatorExpand(TextView targetTextView, boolean isExpand) {
		if (isExpand) {
			targetTextView.setCompoundDrawables(null, null, mDrawableIndicatorUp, null);
		} else {
			targetTextView.setCompoundDrawables(null, null, mDrawableIndicatorDown, null);
		}
	}

    private void initData(){
        KeyValue keyValue = new KeyValue();
        keyValue.setId("");
        keyValue.setName("全部");
        mKeyValueList1 = new ArrayList<KeyValue>();
        mKeyValueList2 = new ArrayList<KeyValue>();
        mKeyValueList3 = new ArrayList<KeyValue>();
        mKeyValueList4 = new ArrayList<KeyValue>();
        mKeyValueList1.addAll(ZxsqApplication.getInstance().getConstant().getRoomStyles());
        mKeyValueList2.addAll(ZxsqApplication.getInstance().getConstant().getRoomSpaces());
        mKeyValueList3.addAll(ZxsqApplication.getInstance().getConstant().getRoomParts());
        mKeyValueList4.addAll(ZxsqApplication.getInstance().getConstant().getRoomTypes());
        mKeyValueList1.add(0, keyValue);
        mKeyValueList2.add(0, keyValue);
        mKeyValueList3.add(0, keyValue);
        mKeyValueList4.add(0, keyValue);
    }

}
