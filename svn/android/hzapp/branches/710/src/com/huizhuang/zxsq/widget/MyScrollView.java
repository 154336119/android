package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.huizhuang.zxsq.utils.LogUtil;

public class MyScrollView extends ScrollView {
	
    private OnScrollListener onScrollListener;  
    /** 
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较 
     */  
    private int lastScrollY; 
    
	private int mLastMotionY;
	
	private ListView listView;
	
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int y = (int) ev.getRawY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			mLastMotionY = y;
			if (getScrollY() <= 0) {
				// 到顶部了  
            } else if (getChildAt(0).getMeasuredHeight() <= getHeight() + getScrollY()) {
            	//滑动到底部  
            	// deltaY > 0 是向下运动,< 0是向上运动
				if (deltaY < 0) {
					return false;
				} else {
					if (listView == null) {
						return super.onInterceptTouchEvent(ev);
					}
					View child = listView.getChildAt(0);
					if (child == null) {
						return super.onInterceptTouchEvent(ev);
					}
					
					if (listView.getFirstVisiblePosition() == 0 && child.getTop() == 0) {
						return super.onInterceptTouchEvent(ev);
					}else {
						return false;
					}
				}
            }
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int y = (int) ev.getRawY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			break;		
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			mLastMotionY = y;
			if (getScrollY() <= 0) {
				// 到顶部了  
            } else if (getChildAt(0).getMeasuredHeight() <= getHeight() + getScrollY()) {
            	//滑动到底部  
            	// deltaY > 0 是向下运动,< 0是向上运动
				if (deltaY < 0) {
					return false;
				} else {
					if (listView == null) {
						return super.onTouchEvent(ev);
					}
					View child = listView.getChildAt(0);
					if (child == null) {
						return super.onTouchEvent(ev);
					}
					
					if (listView.getFirstVisiblePosition() == 0 && child.getTop() == 0) {
						return super.onTouchEvent(ev);
					}else {
						return false;
					}
				}
            }
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}
	
	  /** 
     * 滚动的回调接口 
     */  
    public interface OnScrollListener{  
        /** 
         * 回调方法， 返回MyScrollView滑动的Y方向距离 
         * @param scrollY     、 
         */  
        public void onScroll(int scrollY);  
    }  

    /** 
     * 设置滚动接口 
     * @param onScrollListener 
     */  
    public void setOnScrollListener(OnScrollListener onScrollListener) {  
        this.onScrollListener = onScrollListener;  
    }  
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    	// TODO Auto-generated method stub
    	super.onScrollChanged(l, t, oldl, oldt);
    	if(onScrollListener != null){  
            onScrollListener.onScroll(t);  
        } 
    }
}
