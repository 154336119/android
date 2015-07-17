package com.huizhuang.zxsq.ui.activity.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.BillDetail;
import com.huizhuang.zxsq.module.BillDetail.MonthSummary;
import com.huizhuang.zxsq.module.parser.BillDetailParser;
import com.huizhuang.zxsq.ui.adapter.BillCountMonthListAdapter;
import com.huizhuang.zxsq.ui.adapter.BillTotalCategoryListAdapter;
import com.huizhuang.zxsq.ui.activity.base.BillAccountingBaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.ListViewForScrollView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 账单详情页面
 * 
 * @ClassName: BillTotalDetailActivity
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-31 上午11:15:00
 */
public class BillTotalDetailActivity extends BillAccountingBaseActivity {

	/**
	 * 所有控件（从上到下排列）
	 */
	private CommonActionBar mCommonActionBar;
	private DataLoadingLayout mDataLoadingLayout;
	private TextView mTxtTotalMoney;
	private ListViewForScrollView mBillMonthListView;
	private BillCountMonthListAdapter mBillCountMonthListAdapter;
	private ViewGroup mLabelZone;
	private ImageView mImgIndicator;
	private ListViewForScrollView mBillCategoryListView;
	private BillTotalCategoryListAdapter mBillCountCategoryListAdapter;

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_bill_total_detail);
		mContext = BillTotalDetailActivity.this;

		initActionBar();
		initViews();

		httpRequestQueryBillTotalDetail();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		httpRequestQueryBillTotalDetail();
	}

	@Override
	protected void onReceiveBillAccountChanged() {
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_bill_total_detail);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnBackOnClick(v);
			}

		});
		mCommonActionBar.setRightImgBtn(R.drawable.ic_actionbar_add, new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnAddOnClick(v);
			}

		});
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initViews");

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.bill_total_detail_data_loading_layout);

		// 初始化月份列表
		mTxtTotalMoney = (TextView) findViewById(R.id.tv_bill_total_money);
		mBillMonthListView = (ListViewForScrollView) findViewById(R.id.bill_month_list);
		mBillCountMonthListAdapter = new BillCountMonthListAdapter(mContext);
		mBillMonthListView.setAdapter(mBillCountMonthListAdapter);
		mBillMonthListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LogUtil.d("mBillMonthListView onItemClick position = " + position);

				MonthSummary monthSummary = (MonthSummary) parent.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(BillMonthDetailActivity.EXTRA_MONTH_SUMMARY, monthSummary);
				ActivityUtil.next(BillTotalDetailActivity.this, BillMonthDetailActivity.class, bundle, -1, false);
			}

		});

		// 初始化分类列表
		mLabelZone = (ViewGroup) findViewById(R.id.ll_label_zone);
		mLabelZone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				itemCategoryLabelOnClick(v);
			}

		});
		mImgIndicator = (ImageView) findViewById(R.id.img_indicator_folder);
		mBillCategoryListView = (ListViewForScrollView) findViewById(R.id.bill_category_list);
		mBillCountCategoryListAdapter = new BillTotalCategoryListAdapter(mContext);
		mBillCategoryListView.setAdapter(mBillCountCategoryListAdapter);

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

	/**
	 * ActionBar - 添加按钮事件
	 * 
	 * @param v
	 */
	protected void btnAddOnClick(View v) {
		LogUtil.d("btnAddOnClick View = " + v);

		ActivityUtil.next(BillTotalDetailActivity.this, ChooseCategoryActivity.class);
	}

	/**
	 * 点击分类标签总栏事件
	 * 
	 * @param v
	 */
	protected void itemCategoryLabelOnClick(View v) {
		LogUtil.d("itemCategoryLabelOnClick View = " + v);

		if (View.VISIBLE == mBillCategoryListView.getVisibility()) {
			mImgIndicator.setImageResource(R.drawable.ic_bill_arrow_down);
			mBillCategoryListView.setVisibility(View.GONE);
		} else {
			mImgIndicator.setImageResource(R.drawable.ic_bill_arrow_up);
			mBillCategoryListView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * HTTP请求 - 获取全部账单分类列表数据
	 */
	private void httpRequestQueryBillTotalDetail() {
		LogUtil.d("httpRequestQueryBillTotalDetail");

		HttpClientApi.post(HttpClientApi.REQ_ZX_JOURNEY_SUMMARY, new BillDetailParser(), new RequestCallBack<BillDetail>() {

			@Override
			public void onSuccess(BillDetail billDetail) {
				LogUtil.i("httpRequestQueryBillTotalDetail onSuccess BillDetail = " + billDetail);

				mDataLoadingLayout.showDataLoadSuccess();
				mCommonActionBar.setRightImgBtnVisibility(View.VISIBLE);

				mTxtTotalMoney.setText(getString(R.string.txt_budget_format, billDetail.getTotal()));
				mBillCountMonthListAdapter.setList(billDetail.getMonSumList());
				mBillCountCategoryListAdapter.setTypeSummaryList(billDetail.getTypeSumList(), billDetail.getTotal());
			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.i("httpRequestQueryBillTotalDetail onFailure NetroidError = " + netroidError);

				mDataLoadingLayout.showDataLoadFailed(netroidError.getMessage());
			}

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.i("httpRequestQueryBillTotalDetail onStart");

				mDataLoadingLayout.showDataLoading();
				mCommonActionBar.setRightImgBtnVisibility(View.INVISIBLE);
			}

		});
	}

}
