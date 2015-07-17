package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 适应在ScrollView中嵌套的ListView
 * 
 * @ClassName: ListViewForScrollView
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-31 14:40:00
 */
public class ListViewForScrollView extends ListView {
	
	public ListViewForScrollView(Context context) {
		super(context);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
