package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Gallery;
import com.huizhuang.zxsq.http.task.account.GetGalleryListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.account.WorksiteGalleryGridAdapter;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * @ClassName: WorksiteGalleryListActivity
 * @Description: 质量报告图集
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-5 下午8:50:18
 * 
 */
public class WorksiteGalleryListActivity extends BaseActivity {
	public static final String GALLERY_TITLE = "title";

	private DataLoadingLayout mDataLoadingLayout;
	private GridView mGridView;
	private WorksiteGalleryGridAdapter mAdapter;

	private int mOrderId; // 订单ID
	private String mNodeId; // 结点ID
	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_worksite_gallery_grid);
		initExtrasData();
		initActionBar();
		initView();
		httpRequestQueryOrderData();
	}

	private void initExtrasData() {
		mOrderId = getIntent().getExtras().getInt(AppConstants.PARAM_ORDER_ID);
		mNodeId = getIntent().getExtras().getString(AppConstants.PARAM_NODE_ID);
		mTitle = getIntent().getExtras().getString(GALLERY_TITLE);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(mTitle);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						btnBackOnClick(v);
					}
				});
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		LogUtil.d("initView");

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.common_dl);

		mGridView = (GridView) findViewById(R.id.grid_view);
		int mImgViewWidth = UiUtil.getScreenWidth(this) / 4;
		mAdapter = new WorksiteGalleryGridAdapter(this, mImgViewWidth);
		mGridView.setAdapter(mAdapter);
	}

	/**
	 * 按钮事件 - 返回
	 * 
	 * @param v
	 */
	private void btnBackOnClick(View v) {
		LogUtil.d("btnBackOnClick View = " + v);

		finish();
	}

	private void httpRequestQueryOrderData() {
		GetGalleryListTask task = new GetGalleryListTask(
				WorksiteGalleryListActivity.this, mOrderId, mNodeId);
		task.setCallBack(new AbstractHttpResponseHandler<List<Gallery>>() {

			@Override
			public void onSuccess(List<Gallery> result) {
				mDataLoadingLayout.showDataLoadSuccess();
				mAdapter.setList(result);
				if(result.size() == 0){
					mDataLoadingLayout.showDataEmptyView();
				}
			}

			@Override
			public void onFailure(int code, String error) {
				if (0 == mAdapter.getCount()) {
					mDataLoadingLayout.showDataLoadFailed(error);
				} else {
					showToastMsg(error);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				mDataLoadingLayout.showDataLoading();
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}
		});
		task.send();
	}

}
