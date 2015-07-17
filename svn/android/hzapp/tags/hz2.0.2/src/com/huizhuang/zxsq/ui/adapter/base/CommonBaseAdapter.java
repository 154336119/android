package com.huizhuang.zxsq.ui.adapter.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 抽象类 - 程序中通用泛型Adapter，继承Android本身的BaseAdapter
 * 
 * @ClassName: CommonBaseAdapter
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-30 上午11:20:00
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter implements CommonAdapterOptible<T> {

	/**
	 * LayoutInflater对象（父类可以直接用）
	 */
	protected final LayoutInflater mLayoutInflater;

	/**
	 * Context对象引用（父类可以直接用）
	 */
	protected final Context mContext;

	/**
	 * Adapter的列表对象，注意是用final修饰的，被初始化之后，不能重新赋值，便于保证List<T>的唯一性
	 */
	private final List<T> mList;

	public CommonBaseAdapter(Context context) {
		mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		mList = new ArrayList<T>();
	}

	public List<T> getList() {
		return mList;
	}

	@Override
	public int getCount() {
		return (null == mList) ? 0 : mList.size();
	}

	@Override
	public T getItem(int i) {
		return mList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public void setList(List<T> list) {
		mList.clear();
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public void addList(List<T> list) {
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public void removeItem(int position) {
		mList.remove(position);
		notifyDataSetChanged();
	}

}
