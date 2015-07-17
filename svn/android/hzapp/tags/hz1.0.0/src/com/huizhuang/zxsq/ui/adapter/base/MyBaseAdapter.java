package com.huizhuang.zxsq.ui.adapter.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;


/**
 * 
 * @ClassName: MyBaseAdapter
 * @Description: 基础适配类
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年7月7日  上午11:04:44
 * @param <T>
 */
public abstract class MyBaseAdapter<T> extends CommonBaseAdapter<T> {

	public MyBaseAdapter(Context context) {
		super(context);
	}
	
	public MyBaseAdapter(Context context, List<T> list) {
		super(context);
		setList(list);
	}
	
	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}

}
