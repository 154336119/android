package com.huizhuang.zxsq.widget.component;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.ui.adapter.filter.FilterPrivilegeAdapter;

/**
 * 顶部分类过滤条
 * 
 * @ClassName: TopTypeFilterBar.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-12-17 下午4:19:48
 */
public class TopTypeFilterBar extends RelativeLayout implements OnClickListener {

	public enum TopType {
		FILTER_NEARBY, // 附近
		FILTER_PRICE, // 价格
		FILTER_PRIVILEGE, // 优惠
		FILTER_SORT // 排序
	}

	public static final String[] FILTER_NAMES = { "附近", "价格", "优惠", "排序" };

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
	private ViewGroup mDropDownZone;
	private ListView mListViewDropDown;
	private FilterDropdownAdapter mFilterDropdownAdapter;
	// 优惠特定Adapter
	private FilterPrivilegeAdapter mFilterPrivilegeAdapter;

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

	public TopTypeFilterBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public TopTypeFilterBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public TopTypeFilterBar(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.widget_top_type_filter_bar, this);

		mTvFilter1 = (TextView) rootLayout.findViewById(R.id.tv_filter_1);
		mTvFilter1.setText(FILTER_NAMES[0]);
		mTvFilter2 = (TextView) rootLayout.findViewById(R.id.tv_filter_2);
		mTvFilter2.setText(FILTER_NAMES[1]);
		mTvFilter3 = (TextView) rootLayout.findViewById(R.id.tv_filter_3);
		mTvFilter3.setText(FILTER_NAMES[2]);
		mTvFilter4 = (TextView) rootLayout.findViewById(R.id.tv_filter_4);
		mTvFilter4.setText(FILTER_NAMES[3]);

		mDropDownZone = (ViewGroup) rootLayout.findViewById(R.id.rl_drop_down_zone);
		mListViewDropDown = (ListView) rootLayout.findViewById(R.id.list_view_drop_down);
		mFilterDropdownAdapter = new FilterDropdownAdapter(context);
		mFilterPrivilegeAdapter = new FilterPrivilegeAdapter(context);
		mListViewDropDown.setAdapter(mFilterDropdownAdapter);
		mListViewDropDown.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				KeyValue filterKeyValue = (KeyValue) parent.getAdapter().getItem(position);
				if (null != mTvFilterFocus) {
					// 将选中ID设置到当前的TextView中
					mTvFilterFocus.setTag(filterKeyValue.getId());
					hideDropdownBar();

					TopType topType = TopType.FILTER_NEARBY;
					if (mTvFilter1 == mTvFilterFocus) {
						topType = TopType.FILTER_NEARBY;
					} else if (mTvFilter2 == mTvFilterFocus) {
						topType = TopType.FILTER_PRICE;
					} else if (mTvFilter3 == mTvFilterFocus) {
						topType = TopType.FILTER_PRIVILEGE;
					} else if (mTvFilter4 == mTvFilterFocus) {
						topType = TopType.FILTER_SORT;
					}

					if (null != mOnDropdownBarItemClickListener) {
						mOnDropdownBarItemClickListener.onItemClick(topType, filterKeyValue);
					}
				}
			}
		});

		hideDropdownBar();
		mDropDownZone.setOnClickListener(this);
		mTvFilter1.setOnClickListener(this);
		mTvFilter2.setOnClickListener(this);
		mTvFilter3.setOnClickListener(this);
		mTvFilter4.setOnClickListener(this);

		mDrawableIndicatorDown = getResources().getDrawable(R.drawable.ic_filter_indicator_down);
		mDrawableIndicatorDown.setBounds(0, 0, mDrawableIndicatorDown.getMinimumWidth(), mDrawableIndicatorDown.getMinimumHeight());

		mDrawableIndicatorUp = getResources().getDrawable(R.drawable.ic_filter_indicator_up);
		mDrawableIndicatorUp.setBounds(0, 0, mDrawableIndicatorUp.getMinimumWidth(), mDrawableIndicatorUp.getMinimumHeight());

		initTestData();
	}

	/**
	 * 是否已经显示下拉框
	 * 
	 * @return
	 */
	public boolean isDropdownBarShowing() {
		return View.VISIBLE == mDropDownZone.getVisibility();
	}

	/**
	 * 隐藏下拉框
	 */
	public void hideDropdownBar() {
		mDropDownZone.setVisibility(View.GONE);
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
			mListViewDropDown.setAdapter(mFilterDropdownAdapter);
			onTextTitleClick(mTvFilter1, mKeyValueList1);
			break;
		case R.id.tv_filter_2:
			mListViewDropDown.setAdapter(mFilterDropdownAdapter);
			onTextTitleClick(mTvFilter2, mKeyValueList2);
			break;
		case R.id.tv_filter_3:
			mListViewDropDown.setAdapter(mFilterPrivilegeAdapter);
			onTextTitleClick(mTvFilter3, mKeyValueList3);
			break;
		case R.id.tv_filter_4:
			mListViewDropDown.setAdapter(mFilterDropdownAdapter);
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

			if (mTvFilter3 == currentTextView) {
				mFilterPrivilegeAdapter.setList(keyValueList);
				// 将当前TextView绑定的选中ID设置到Adapter中
				mFilterPrivilegeAdapter.setSelectedKeyValueId((String) currentTextView.getTag());
			} else {
				mFilterDropdownAdapter.setList(keyValueList);
				// 将当前TextView绑定的选中ID设置到Adapter中
				mFilterDropdownAdapter.setSelectedKeyValueId((String) currentTextView.getTag());
			}

			mDropDownZone.setVisibility(View.VISIBLE);
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

	/**
	 * 测试数据
	 * 
	 * @param totalCount
	 * @param filterName
	 * @return
	 */
	private List<KeyValue> initTestData(int totalCount, String filterName) {
		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		for (int i = 0; i < totalCount; i++) {
			KeyValue keyValue = new KeyValue();
			keyValue.setId(i + "");
			keyValue.setName(filterName + i);
			keyValueList.add(keyValue);
		}
		return keyValueList;
	}

	/**
	 * 测试数据
	 */
	private void initTestData() {
		mKeyValueList1 = initTestData(5, "附近");
		mKeyValueList2 = initTestData(6, "价格");
		mKeyValueList3 = initTestData(7, "优惠");
		mKeyValueList4 = initTestData(7, "排序");
	}

}
