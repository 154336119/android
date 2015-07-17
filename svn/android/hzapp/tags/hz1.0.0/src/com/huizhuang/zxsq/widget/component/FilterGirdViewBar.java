package com.huizhuang.zxsq.widget.component;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.ui.adapter.filter.FilterGirdAdapter;
import com.huizhuang.zxsq.ui.adapter.filter.FilterGirdAdapter.OnFilterItemCheckListener;

/**
 * 筛选过滤栏控件 （效率有待优化，可以换成ExpandGridView控件实现）
 * 
 * @ClassName: FilterGirdViewBar.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2015-1-22 下午2:24:26
 */
public class FilterGirdViewBar extends RelativeLayout {

	public static final String FILTER_TYPE = "范围";
	public static final String FILTER_WAY = "方式";
	public static final String FILTER_LEVEL = "档次";
	public static final String FILTER_STYLE = "风格";
	public static final String FILTER_BRAND = "品牌";

	private ViewGroup mFilterTitleZone;
	private TextView mTvTtile;
	private TextView mTvSelectItems;
	private ImageView mIvFilterArrow;

	private ViewGroup mFitterGvZone;
	private GridViewForScrollView mGridView;
	private FilterGirdAdapter mFilterGirdAdapter;
	private TextView mTvFilterWayTip;

	private String mFilterBarTitle = FILTER_TYPE;

	public FilterGirdViewBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public FilterGirdViewBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public FilterGirdViewBar(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.component_filter_gird_view_bar, this);
		mFilterTitleZone = (ViewGroup) rootLayout.findViewById(R.id.rl_filter_title_zone);
		mFilterTitleZone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (View.VISIBLE == mFitterGvZone.getVisibility()) {
					mFitterGvZone.setVisibility(View.GONE);
					mIvFilterArrow.setImageResource(R.drawable.ic_fliter_arrow_up);
				} else {
					mFitterGvZone.setVisibility(View.VISIBLE);
					mIvFilterArrow.setImageResource(R.drawable.ic_fliter_arrow_down);
				}

			}
		});
		mTvTtile = (TextView) rootLayout.findViewById(R.id.tv_title);
		mTvTtile.setText(mFilterBarTitle);
		mTvSelectItems = (TextView) rootLayout.findViewById(R.id.tv_select_items);
		mIvFilterArrow = (ImageView) rootLayout.findViewById(R.id.iv_filter_arrow);

		mFilterGirdAdapter = new FilterGirdAdapter(context);
		mFilterGirdAdapter.setOnFilterItemCheckListener(new OnFilterItemCheckListener() {
			@Override
			public void onCheckedChanged(String strItems) {
				mTvSelectItems.setText(strItems);
			}
		});

		mFitterGvZone = (ViewGroup) rootLayout.findViewById(R.id.ll_gv_zone);
		mGridView = (GridViewForScrollView) rootLayout.findViewById(R.id.gv_items);
		mGridView.setAdapter(mFilterGirdAdapter);

		mTvFilterWayTip = (TextView) rootLayout.findViewById(R.id.tv_filter_way_tip);
		mTvFilterWayTip.setVisibility(View.GONE);
	}

	/**
	 * 设置过滤栏标题
	 * 
	 * @param title
	 */
	public void setFilterBarTitle(String title) {
		mTvTtile.setText(title + ":");
		mTvSelectItems.setText("全部" + title);
		mFilterBarTitle = title;
		if (FILTER_WAY.equalsIgnoreCase(title)) {
			mTvFilterWayTip.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置数据
	 * 
	 * @param keyValueList
	 */
	public void setKeyValueListData(List<KeyValue> keyValueList) {
		if (null != mFilterGirdAdapter) {
			mFilterGirdAdapter.setList(keyValueList);
		}
	}

	/**
	 * 清空选中数据
	 */
	public void resetSelectItems() {
		mTvSelectItems.setText("全部" + mFilterBarTitle);
		if (null != mFilterGirdAdapter) {
			mFilterGirdAdapter.resetSelectItems();
		}
	}

	/**
	 * 获取选中项目的ID集合
	 * 
	 * @return
	 */
	public String getSelectKeyValueIds() {
		if (null != mFilterGirdAdapter) {
			return mFilterGirdAdapter.getSelectKeyValueIds();
		} else {
			return "";
		}
	}
}
