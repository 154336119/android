package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;

/**
 * 数据加载（HTTP请求或读取数据库）过程中视图
 * 
 * @ClassName: DataLoadingLayout.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-4 下午4:20:20
 */
public class DataLoadingLayout extends RelativeLayout {

	/**
	 * 数据加载中的显示布局
	 */
	private ViewGroup mLayoutDataLoading;

	/**
	 * 数据加载失败的显示布局
	 */
	private ViewGroup mLayoutDataLoadFailed;

	/**
	 * 数据加载失败的错误信息
	 */
	private TextView mTxtViewErrorInfo;

	/**
	 * 重新加载按钮
	 */
	private TextView mTxtViewReload;
	/**
	 * 
	 */
	private TextView mTxtViewApppintment;
	
	public DataLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public DataLoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public DataLoadingLayout(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View rootLayout = LayoutInflater.from(context).inflate(R.layout.layout_loading, this);
		if(rootLayout != null){
			mLayoutDataLoading = (ViewGroup) rootLayout.findViewById(R.id.data_loading_layout);
			mLayoutDataLoadFailed = (ViewGroup) rootLayout.findViewById(R.id.data_load_failed_layout);
		}
		if(mLayoutDataLoadFailed != null){
			mTxtViewErrorInfo = (TextView) mLayoutDataLoadFailed.findViewById(R.id.txt_view_error);
			mTxtViewReload = (TextView) mLayoutDataLoadFailed.findViewById(R.id.txt_view_reload);
			mTxtViewApppintment = (TextView) mLayoutDataLoadFailed.findViewById(R.id.txt_view_appointment);
			mTxtViewReload.setVisibility(View.GONE);
			mTxtViewApppintment.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示数据加载中的布局
	 */
	public void showDataLoading() {
		this.setVisibility(View.VISIBLE);
		mLayoutDataLoading.setVisibility(View.VISIBLE);
		mLayoutDataLoadFailed.setVisibility(View.GONE);
	}

	/**
	 * 数据加载成功 - 隐藏布局
	 */
	public void showDataLoadSuccess() {
		this.setVisibility(View.GONE);
	}

	/**
	 * 显示请求成功但请求返回数据为空的情况
	 */
	public void showDataEmptyView() {
		this.setVisibility(View.VISIBLE);
	//	mLayoutDataLoading.setVisibility(View.GONE);
		mLayoutDataLoadFailed.setVisibility(View.VISIBLE);
		mTxtViewErrorInfo.setText(R.string.txt_layout_empty);
	}

	/**
	 * 显示数据加载失败原因
	 */
	public void showDataLoadFailed(String strReason) {
		this.setVisibility(View.VISIBLE);
		//mLayoutDataLoading.setVisibility(View.GONE);
		mLayoutDataLoadFailed.setVisibility(View.VISIBLE);
		mTxtViewErrorInfo.setText(strReason);
		mTxtViewReload.setVisibility(View.GONE);
	}
	
	/**
	 * 空数据
	 */
	public void showDataLoadEmpty(String strReason) {
		this.setVisibility(View.VISIBLE);
		//mLayoutDataLoading.setVisibility(View.GONE);
		mLayoutDataLoadFailed.setVisibility(View.VISIBLE);
		mTxtViewErrorInfo.setText(strReason);
		mTxtViewReload.setVisibility(View.GONE);
	}

	/**
	 * 设置点击重新加载事件
	 * 
	 * @param onClickListener
	 */
	public void setOnReloadClickListener(OnClickListener onClickListener) {
		mTxtViewReload.setVisibility(View.VISIBLE);
		mTxtViewReload.setOnClickListener(onClickListener);
	}
	/**
	 * 设置点击预约工长事件
	 * 
	 * @param onClickListener
	 */
	public void setOnAppointmentClickListener(OnClickListener onClickListener) {
		mTxtViewApppintment.setVisibility(View.VISIBLE);
		mTxtViewApppintment.setOnClickListener(onClickListener);
	}}
