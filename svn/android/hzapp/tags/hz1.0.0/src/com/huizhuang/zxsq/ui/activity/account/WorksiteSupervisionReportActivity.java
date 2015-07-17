package com.huizhuang.zxsq.ui.activity.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.SupervisionReport;
import com.huizhuang.zxsq.module.parser.SupervisionReportParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.diary.TipsFriendsOnlookersActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 质量报告详情
 * 
 * @ClassName: WorksiteSupervisionReportActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-24 下午4:39:57
 */
public class WorksiteSupervisionReportActivity extends BaseActivity {

	private CommonActionBar mCommonActionBar;
	private DataLoadingLayout mDataLoadingLayout;
	private TextView mTvLastQuestion, mTvNowQuestion, mTvEditQuestion, mTvScore, mTvResult;

	private int mOrderId;
	private String mNoteId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.account_worksite_supervision_report);

		getIntentExtras();
		initActionBar();
		initView();

		httpRequestQuerySupervisionReportDetail();
	}

	/**
	 * 获取Intent传递过来的数据
	 */
	private void getIntentExtras() {
		LogUtil.d("getIntentExtras");

		mOrderId = getIntent().getExtras().getInt(AppConstants.PARAM_ORDER_ID);
		mNoteId = getIntent().getExtras().getString(AppConstants.PARAM_NODE_ID);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
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

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.dll_wait);
		mTvLastQuestion = (TextView) findViewById(R.id.last_question);
		mTvNowQuestion = (TextView) findViewById(R.id.now_question);
		mTvEditQuestion = (TextView) findViewById(R.id.edit_question);
		mTvScore = (TextView) findViewById(R.id.tv_score_ws_r);
		mTvResult = (TextView) findViewById(R.id.tv_jlResult);
		findViewById(R.id.btn_yq).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtil.next(WorksiteSupervisionReportActivity.this, TipsFriendsOnlookersActivity.class);
			}
		});
		findViewById(R.id.lin_range).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnLinkRangeOnClick(v);
			}
		});
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

	/**
	 * 按钮事件 - 评分
	 * 
	 * @param v
	 */
	protected void btnLinkRangeOnClick(View v) {
		LogUtil.d("btnLinkRangeOnClick View = " + v);

		Bundle bundle = new Bundle();
		bundle.putInt("order_id", mOrderId);
		bundle.putString("node_id", mNoteId);
		ActivityUtil.next(WorksiteSupervisionReportActivity.this, WorksiteSupervisionReportDetailActivity.class, bundle, -1);
	}

	/**
	 * HTTP请求 - 获取报表详情数据
	 */
	private void httpRequestQuerySupervisionReportDetail() {
		LogUtil.d("httpRequestQuerySupervisionReportDetail");

		RequestParams requestParams = new RequestParams();
		requestParams.put("order_id", mOrderId);
		requestParams.put("node_id", mNoteId);
		HttpClientApi.post(HttpClientApi.REQ_ACCOUNT_SUPERVISION_RECORD_DETAIL, requestParams, new SupervisionReportParser(),
				new RequestCallBack<SupervisionReport>() {

					@Override
					public void onStart() {
						super.onStart();
						LogUtil.d("httpRequestQuerySupervisionReportDetail onStart");

						mDataLoadingLayout.showDataLoading();
					}

					@Override
					public void onFinish() {
						super.onFinish();
						LogUtil.d("httpRequestQuerySupervisionReportDetail onFinish");
					}

					@Override
					public void onFailure(NetroidError netroidError) {
						LogUtil.d("httpRequestQuerySupervisionReportDetail onFailure NetroidError = " + netroidError);

						mDataLoadingLayout.showDataLoadFailed(netroidError.getMessage());
					}

					@Override
					public void onSuccess(SupervisionReport supervisionReport) {
						LogUtil.d("httpRequestQuerySupervisionReportDetail onSuccess SupervisionReport = " + supervisionReport);

						mDataLoadingLayout.showDataLoadSuccess();

						if (null == supervisionReport || TextUtils.isEmpty(supervisionReport.getId())) {
							mDataLoadingLayout.showDataEmptyView();
						} else {
							mCommonActionBar.setActionBarTitle(supervisionReport.getTitle());
							mTvLastQuestion.setText(String.format(getResources().getString(R.string.txt_prov_problem_count),
									supervisionReport.getLastProblems()));
							mTvNowQuestion.setText(String.format(getResources().getString(R.string.txt_curr_problem_count), supervisionReport.getNowProblems()));
							mTvEditQuestion.setText(String.format(getResources().getString(R.string.txt_prov_problem_count),
									supervisionReport.getEditproblems()));
							mTvScore.setText(String.valueOf(supervisionReport.getScore()));
							mTvResult.setText(supervisionReport.getResult());
						}
					}

				});
	}
}
