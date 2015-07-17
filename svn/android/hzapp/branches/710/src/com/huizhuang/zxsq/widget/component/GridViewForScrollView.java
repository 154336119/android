package com.huizhuang.zxsq.widget.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 适应在ScrollView中嵌套的GridView
 * 
 * @ClassName: GridViewForScrollView.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-12-25 上午10:36:56
 */
public class GridViewForScrollView extends GridView {

    public GridViewForScrollView(Context context) {
        super(context);
    }

    public GridViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 重写该方法，达到使GridView适应ScrollView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
