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
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 分类过滤bar(工长列表)
 * @ClassName: ForemanListFilterBar 
 * @author wumaojie.gmail.com  
 * @date 2015-2-4 上午11:44:26
 */
public class ForemanListFilterBar extends RelativeLayout implements
		OnClickListener, OnItemClickListener {

	public enum TopType {
		FILTER_NEARBY, // 附近
		FILTER_SENIORITY, // 工龄
		FILTER_SCOPE, // 范围
		FILTER_SORT // 排序
	}

	public static final String[] FILTER_NAMES = { "附近", "工龄", "范围", "排序" };

	public static String[][] FILTER_VALUES = {
			{ "当前位置", "青羊区", "高新区", "武侯区", "锦江区" },
			{ "全部工龄", "1年以下", "1~3年", "3~5年", "5~10年", "10年以上" },
			{ "全部范围", "新房装修", "别墅装修", "二手房", "局部装修", "商业办公" },
			{ "默认排序", "离我最近", "人气预约", "成交最高", "评价最高", "施工现场数量" } };

    private Context mContext;

	public int[] area_id;

	/**
	 * 顶部文本显示区域
	 */
	private TextView mTvFilter1;
	private TextView mTvFilter2;
	private TextView mTvFilter3;
	private TextView mTvFilter4;

	private FilterForemanAdapter foremanAdapter1;
	private FilterForemanAdapter foremanAdapter2;
	private FilterForemanAdapter foremanAdapter3;
	private FilterForemanAdapter foremanAdapter4;

	// 箭头
	private Drawable mDrawableIndicatorUp;
	private Drawable mDrawableIndicatorDown;

    private PopupWindow mOptionPopupWindow;//筛选条件popWindow
	// 弹出列表
	private ListView mListViewDropDown;
	// 记录当前焦点的TextView
	private TextView mTvFilterFocus;

	/**
	 * 数据区
	 */
	private List<KeyValue> mKeyValueList1;
	private List<KeyValue> mKeyValueList2;
	private List<KeyValue> mKeyValueList3;
	private List<KeyValue> mKeyValueList4;

	private ForemanListFilterBarListener listener;
	
	public static ForemanListFilterBar filterBar;

	public interface ForemanListFilterBarListener {
		void onItemClick(TopType topType, int position);
	}

	public ForemanListFilterBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public ForemanListFilterBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public ForemanListFilterBar(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		filterBar = this;
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

	public void initData() {
		for (int i = 0; i < FILTER_VALUES.length; i++) {
			List<KeyValue> keyValues;
			if (i == 0) {
				mKeyValueList1 = keyValues = new ArrayList<KeyValue>();
				foremanAdapter1 = new FilterForemanAdapter(getContext());
				foremanAdapter1.setSeparateBar(1, "行政区域");
			} else if (i == 1) {
				mKeyValueList2 = keyValues = new ArrayList<KeyValue>();
				foremanAdapter2 = new FilterForemanAdapter(getContext());
				foremanAdapter2.setSeparateBar(1, "工龄年限");
			} else if (i == 2) {
				mKeyValueList3 = keyValues = new ArrayList<KeyValue>();
				foremanAdapter3 = new FilterForemanAdapter(getContext());
				foremanAdapter3.setSeparateBar(1, "装修范围");
			} else {
				mKeyValueList4 = keyValues = new ArrayList<KeyValue>();
				foremanAdapter4 = new FilterForemanAdapter(getContext());
			}
			String[] FILTER_Lists = FILTER_VALUES[i];
			for (int j = 0; j < FILTER_Lists.length; j++) {
				KeyValue value = new KeyValue();
				value.setId(j + "");
				value.setName(FILTER_Lists[j]);
				keyValues.add(value);
			}
		}
	}

    @SuppressLint("InflateParams")
    private void showPopUp(final View selectedView,FilterForemanAdapter foremanAdapter1) {
        //筛选条件
        View view = LayoutInflater.from(mContext).inflate(R.layout.activites_filter_condition_popup, null);
        mListViewDropDown = (ListView) view.findViewById(R.id.list_view_drop_down);
        mListViewDropDown.setAdapter(foremanAdapter1);
        mListViewDropDown.setOnItemClickListener(this);
        // 将当前TextView绑定的选中ID设置到Adapter中
        foremanAdapter1.setSelectedKeyValueId((String) selectedView.getTag());
        int width = DensityUtil.getScreenWidth((Activity) mContext);
        int height = DensityUtil.getScreenHeight((Activity)mContext)- DensityUtil.dip2px(mContext, 185.0f);
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

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int postion,
			long l) {
        KeyValue filterKeyValue = (KeyValue) adapterView.getAdapter().getItem(postion);
		if (null != mTvFilterFocus) {

			hideDropdownBar();
            // 将选中ID设置到当前的TextView中
            mTvFilterFocus.setTag(filterKeyValue.getId());
			TopType topType = TopType.FILTER_NEARBY;
			if (mTvFilter1 == mTvFilterFocus) {
				topType = TopType.FILTER_NEARBY;
				if(area_id!=null&&area_id.length>postion){
					postion = area_id[postion];
				}else{
					postion = -1;
				}
			} else if (mTvFilter2 == mTvFilterFocus) {
				topType = TopType.FILTER_SENIORITY;
			} else if (mTvFilter3 == mTvFilterFocus) {
				topType = TopType.FILTER_SCOPE;
			} else if (mTvFilter4 == mTvFilterFocus) {
				topType = TopType.FILTER_SORT;
			}
			if (null != listener) {
				listener.onItemClick(topType, postion);
			}
		}
	}

	/**
	 * 设置选择回调接口
	 * 
	 * @Title: setForemanListFilterBarListener
	 * @Description: TODO
	 * @param listener
	 *            void
	 * @throws
	 */
	public void setOnForemanListFilterBarListener(
			ForemanListFilterBarListener listener) {
		this.listener = listener;
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_filter_1:
            AnalyticsUtil.onEvent(mContext, "click13");
			foremanAdapter1.setList(mKeyValueList1);
			onTextTitleClick(mTvFilter1, mKeyValueList1,foremanAdapter1);
			break;
		case R.id.tv_filter_2:
            AnalyticsUtil.onEvent(mContext, "click14");
			foremanAdapter2.setList(mKeyValueList2);
			onTextTitleClick(mTvFilter2, mKeyValueList2, foremanAdapter2);
			break;
		case R.id.tv_filter_3:
            AnalyticsUtil.onEvent(mContext, "click15");
			foremanAdapter3.setList(mKeyValueList3);
			onTextTitleClick(mTvFilter3, mKeyValueList3,foremanAdapter3);
			break;
		case R.id.tv_filter_4:
            AnalyticsUtil.onEvent(mContext, "click16");
			foremanAdapter4.setList(mKeyValueList4);
			onTextTitleClick(mTvFilter4, mKeyValueList4,foremanAdapter4);
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
	private void onTextTitleClick(TextView currentTextView, List<KeyValue> keyValueList,FilterForemanAdapter adapter) {
		if (null != keyValueList) {
			if (null != mTvFilterFocus) {
				setIndicatorExpand(mTvFilterFocus, false);
			}
			setIndicatorExpand(currentTextView, true);
			mTvFilterFocus = currentTextView;

            showPopUp(currentTextView, adapter);
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

}
