package com.huizhuang.zxsq.ui.activity.bill;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.Constant.JourneyType;
import com.huizhuang.zxsq.ui.adapter.BillCategoryListAdapter;
import com.huizhuang.zxsq.ui.activity.base.BillAccountingBaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * 选择装修记主分类
 * 
 * @ClassName: ChooseCategoryActivity
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-30 下午6:20:00
 */
public class ChooseCategoryActivity extends BillAccountingBaseActivity {

	/**
	 * 所有控件（至上而下）
	 */
	private GridView mGridView;
	private BillCategoryListAdapter mBillCategoryListAdapter;

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_choose_category);
		mContext = ChooseCategoryActivity.this;

		initActionBar();
		initViews();
	}

	@Override
	protected void onReceiveBillAccountChanged() {
		finish();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.title_choose_bill_category);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnBackOnClick(v);
			}

		});
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initActionBar");

		mGridView = (GridView) findViewById(R.id.category_grid_view);
		mBillCategoryListAdapter = new BillCategoryListAdapter(mContext);
		mBillCategoryListAdapter.setList(ZxsqApplication.getInstance().getConstant().getJourneyTypeList());
		mGridView.setAdapter(mBillCategoryListAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LogUtil.d("mGridView onItemClick position = " + position);

				JourneyType journeyType = (JourneyType) parent.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(BillAccountingActivity.EXTRA_JOURNEY_TYPE, journeyType);
				ActivityUtil.next(ChooseCategoryActivity.this, BillAccountingActivity.class, bundle, -1, false);
			}

		});
	}

	/**
	 * ActionBar - 返回按钮事件
	 * 
	 * @param v
	 */
	protected void btnBackOnClick(View v) {
		LogUtil.d("btnBackOnClick View = " + v);

		finish();
	}

}
