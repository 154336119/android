package com.huizhuang.zxsq.widget.swipemenulistview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * @ClassName: SwipeMenu.java
 * 
 * @author https://github.com/baoyongzhang/SwipeMenuListView
 * 
 * @date 2014-11-5 下午4:31:20
 */
public class SwipeMenu {

	private Context mContext;
	private List<SwipeMenuItem> mItems;
	private int mViewType;

	public SwipeMenu(Context context) {
		mContext = context;
		mItems = new ArrayList<SwipeMenuItem>();
	}

	public Context getContext() {
		return mContext;
	}

	public void addMenuItem(SwipeMenuItem item) {
		mItems.add(item);
	}

	public void removeMenuItem(SwipeMenuItem item) {
		mItems.remove(item);
	}

	public List<SwipeMenuItem> getMenuItems() {
		return mItems;
	}

	public SwipeMenuItem getMenuItem(int index) {
		return mItems.get(index);
	}

	public int getViewType() {
		return mViewType;
	}

	public void setViewType(int viewType) {
		this.mViewType = viewType;
	}

}
