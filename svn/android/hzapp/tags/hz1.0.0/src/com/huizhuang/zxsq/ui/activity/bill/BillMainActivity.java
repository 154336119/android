package com.huizhuang.zxsq.ui.activity.bill;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.JourneyIndex;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.module.parser.JourneyIndexParser;
import com.huizhuang.zxsq.ui.activity.base.BillAccountingBaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.HistogramView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 装修记账主页
 * 
 * @ClassName: BillMainActivity
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-30 上午11:20:00
 */
public class BillMainActivity extends BillAccountingBaseActivity {

	/**
	 * 所有控件（至上而下）
	 */
	private CommonActionBar mCommonActionBar;
	private DataLoadingLayout mDataLoadingLayout;
	private TextView mTxtMoney;
	private TextView mTxtRate;
	private LinearLayout mChartZone;

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_bill_main);
		mContext = BillMainActivity.this;
		AnalyticsUtil.onEvent(mContext, UmengEvent.ID_BILL_ACCOUNTING);

		initActionBar();
		initViews();

		httpRequestQueryDecorateBill();
	}

	@Override
	protected void onReceiveBillAccountChanged() {
		httpRequestQueryDecorateBill();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_decorate_bill_report);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnBackOnClick(v);
			}

		});
		mCommonActionBar.setRightTxtBtn(R.string.title_bill_total_detail, new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnBillDetailOnClick(v);
			}

		});
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initViews");

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.bill_main_data_loading_layout);
		mTxtMoney = (TextView) findViewById(R.id.bill_total_money);
		mTxtRate = (TextView) findViewById(R.id.bill_txt_rate);
		mChartZone = (LinearLayout) findViewById(R.id.ll_chart_zone);
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
	 * ActionBar - 账单详情事件
	 * 
	 * @param v
	 */
	protected void btnBillDetailOnClick(View v) {
		LogUtil.d("btnBillDetailOnClick View = " + v);

		ActivityUtil.next(BillMainActivity.this, BillTotalDetailActivity.class);
	}

	/**
	 * XML反射方法
	 * 
	 * @param v
	 */
	public void btnStartRecordOnClick(View v) {
		LogUtil.d("btnStartRecordOnClick View = " + v);

		ActivityUtil.next(BillMainActivity.this, ChooseCategoryActivity.class);
	}

	/**
	 * XML反射方法
	 * 
	 * @param v
	 */
	public void btnBillShareOnClick(View v) {
		LogUtil.d("btnBillShareOnClick View = " + v);

		Share share = new Share();
		String text="#"+getResources().getString(R.string.app_name)+"#"+mTxtRate.getText().toString().trim();
		share.setText(text);
		Util.showShare(false, BillMainActivity.this, share);
	}

	/**
	 * HTTP请求 - 获取分类数据图
	 */
	private void httpRequestQueryDecorateBill() {
		LogUtil.d("httpRequestQueryDecorateBill");

		RequestParams params = new RequestParams();
		params.add("user_id", ZxsqApplication.getInstance().getUser().getId());
		HttpClientApi.get(HttpClientApi.REQ_ZX_JOURNEY, params, new JourneyIndexParser(), new RequestCallBack<JourneyIndex>() {

			@Override
			public void onSuccess(JourneyIndex journeyIndex) {
				LogUtil.i("httpRequestQueryDecorateBill onSuccess JourneyIndex = " + journeyIndex);
				mDataLoadingLayout.showDataLoadSuccess();
				mCommonActionBar.setRightTxtBtnVisibility(View.VISIBLE);

				// 显示总金额
				double journeyTotal = journeyIndex.getJourneyTotal();
				if (0 != journeyTotal) {
					mTxtMoney.setText(mContext.getString(R.string.txt_budget_format, journeyIndex.getJourneyTotal()));

					// 有数据则显示为百分比，没有数据显示“暂无数据”
					String strRank = journeyIndex.getRank();
					LogUtil.d("strRank = " + strRank);

					if (null != strRank && strRank.length() > 1 && strRank.endsWith("%")) {
						double doubleRate = Double.parseDouble(strRank.substring(0, strRank.length() - 1));
						LogUtil.d("doubleRate = " + doubleRate);
						if (doubleRate > 70) {
							mTxtRate.setText(getString(R.string.txt_bill_main_rate_more_than_70_percent, strRank));
						} else if (doubleRate < 30) {
							mTxtRate.setText(getString(R.string.txt_bill_main_rate_less_than_30_percent, strRank));
						} else {
							mTxtRate.setText(getString(R.string.txt_bill_main_rate_between_30_and_70_percent, strRank));
						}
					} else {
						mTxtRate.setText(R.string.txt_bill_none_info);
					}

				} else {
					mTxtMoney.setText(R.string.txt_bill_none);
					mTxtRate.setText(R.string.txt_bill_none_info);
					mChartZone.setVisibility(View.GONE);
					
				}
				HistogramView view = new HistogramView(BillMainActivity.this);
				view.setDataMap(journeyIndex.getJourneyMap(),journeyIndex.getMinDate(),journeyIndex.getMaxDate());
				mChartZone.removeAllViews();
				mChartZone.addView(view);
				mChartZone.setVisibility(View.VISIBLE);

			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.i("httpRequestQueryDecorateBill onFailure NetroidError = " + netroidError);

				mDataLoadingLayout.showDataLoadFailed(netroidError.getMessage());
			}

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.i("httpRequestQueryDecorateBill onStart");
				mCommonActionBar.setRightTxtBtnVisibility(View.INVISIBLE);

				mDataLoadingLayout.showDataLoading();
			}

		});
	}

}
