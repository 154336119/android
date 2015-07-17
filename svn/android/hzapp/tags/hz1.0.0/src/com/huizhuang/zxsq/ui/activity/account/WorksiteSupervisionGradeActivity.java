package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.Constant.RankType;
import com.huizhuang.zxsq.http.bean.account.Grade;
import com.huizhuang.zxsq.http.task.account.GetGradeInfoTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @ClassName: WorksiteSupervisionGradeActivity
 * @Description: 监理师评分
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-10 下午3:18:37
 * 
 */
public class WorksiteSupervisionGradeActivity extends BaseActivity {

	private DataLoadingLayout mDataLoadingLayout;

	private CircleImageView mCivSupervisorHead;
	private TextView mTvSupervisorName;
	private RatingBar mRbSupervisorScore;
	private LinearLayout mSupervisorZone;
	private EditText mEdtSupervisorEvaluation;

	private LinearLayout mForemanContainer;
	private CircleImageView mCivForemanHead;
	private TextView mTvForemanName;
	private RatingBar mRbForemanScore;
	private LinearLayout mForemanZone;
	private EditText mEdtForemanEvaluation;

	private List<RankType> mSupervisorStaffList;
	private List<RankType> mSupervisorStoreList;
	private String mRecordId;
	private String mNodeId;
	private Grade mGrade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_worksite_supervision_grade);

		initExtrasData();
		initActionBar();
		initViews();
		httpRequestGetInfo();
	}

	/**
	 * 初始化数据
	 */
	private void initExtrasData() {
		LogUtil.d("initExtrasData");
		if (getIntent().getExtras() != null) {
			mRecordId = getIntent().getExtras().getString(
					AppConstants.PARAM_RECORD_ID);
			mNodeId = getIntent().getExtras().getString(
					AppConstants.PARAM_NODE_ID);
		}
		mSupervisorStaffList = ZxsqApplication.getInstance().getConstant()
				.getJlStaff();
		mSupervisorStoreList = ZxsqApplication.getInstance().getConstant()
				.getJlGongzhang();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.txt_jls_score);
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
	private void initViews() {
		LogUtil.d("initViews");
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.common_dl);

		mCivSupervisorHead = (CircleImageView) findViewById(R.id.civ_jls_head);
		mTvSupervisorName = (TextView) findViewById(R.id.tv_jls_name);
		mRbSupervisorScore = (RatingBar) findViewById(R.id.rb_jls_score);
		mSupervisorZone = (LinearLayout) findViewById(R.id.lin_jls);
		mEdtSupervisorEvaluation = (EditText) findViewById(R.id.et_jls_evaluation);
		mForemanContainer = (LinearLayout) findViewById(R.id.lin_foreman_container);

		mCivForemanHead = (CircleImageView) findViewById(R.id.civ_foreman_head);
		mTvForemanName = (TextView) findViewById(R.id.tv_foreman_name);
		mRbForemanScore = (RatingBar) findViewById(R.id.rb_foreman_score);
		mForemanZone = (LinearLayout) findViewById(R.id.lin_foreman);
		mEdtForemanEvaluation = (EditText) findViewById(R.id.et_foreman_evaluation);

		mRbSupervisorScore
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						if (rating < 4) {
							if (mSupervisorZone.getVisibility() == View.GONE) {
								changeScoreAll(mSupervisorStaffList, 0f);
							}
							mSupervisorZone.setVisibility(View.VISIBLE);
						} else {
							mSupervisorZone.setVisibility(View.GONE);
							changeScoreAll(mSupervisorStaffList, rating);
						}
					}
				});

		if ("p5".equals(mNodeId)) {
//			mForemanContainer.setVisibility(View.VISIBLE);
			mRbForemanScore
					.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

						@Override
						public void onRatingChanged(RatingBar ratingBar,
								float rating, boolean fromUser) {
							if (rating < 4) {
								if (mForemanZone.getVisibility() == View.GONE) {
									changeScoreAll(mSupervisorStoreList, 0f);
								}
								mForemanZone.setVisibility(View.VISIBLE);
							} else {
								mForemanZone.setVisibility(View.GONE);
								changeScoreAll(mSupervisorStoreList, rating);
							}
						}
					});
			for (RankType rankType : mSupervisorStoreList) {
				initChildView(rankType, 1);
			}
		} 
//		else {
//			mForemanContainer.setVisibility(View.GONE);
//		}

		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				httpRequestAddCommentToSupervision();
			}
		});

		for (RankType rankType : mSupervisorStaffList) {
			initChildView(rankType, 0);
		}

	}

	private void initViewData(Grade grade) {
		mGrade = grade;
		if (grade.getStaff() != null) {
			if (grade.getStaff().getImage() != null
					&& !TextUtils.isEmpty(grade.getStaff().getImage()
							.getThumb_path())) {
				ImageLoader.getInstance().displayImage(
						grade.getStaff().getImage().getThumb_path(),
						mCivSupervisorHead,
						ImageLoaderOptions.getDefaultImageOption());
			}
			mTvSupervisorName.setText(grade.getStaff().getName());
		}
		if ("p5".equals(mNodeId) && grade.getStore() != null && grade.getStore().getId() != 0) {
			mForemanContainer.setVisibility(View.VISIBLE);
			if (grade.getStore().getImage() != null
					&& !TextUtils.isEmpty(grade.getStore().getImage()
							.getThumb_path())) {
				ImageLoader.getInstance().displayImage(
						grade.getStore().getImage().getThumb_path(),
						mCivForemanHead,
						ImageLoaderOptions.getDefaultImageOption());
			}
			mTvForemanName.setText(grade.getStore().getName());
		}else{
			mForemanContainer.setVisibility(View.GONE);
		}

	}

	private void httpRequestGetInfo() {
		GetGradeInfoTask task = new GetGradeInfoTask(
				WorksiteSupervisionGradeActivity.this, mRecordId);
		task.setCallBack(new AbstractHttpResponseHandler<Grade>() {

			@Override
			public void onSuccess(Grade result) {
				mDataLoadingLayout.showDataLoadSuccess();
				if (result == null) {
					mDataLoadingLayout.showDataEmptyView();
				} else {
					initViewData(result);
				}
			}

			@Override
			public void onFailure(int code, String error) {
				mDataLoadingLayout.showDataLoadFailed(error);
			}

			@Override
			public void onStart() {
				showWaitDialog("加载中...");
				super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
			}

		});
		task.send();
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
	 * 动态加载评分项
	 * 
	 * @param rankType
	 * @param type
	 */
	@SuppressLint("InflateParams")
	private void initChildView(RankType rankType, int type) {
		LinearLayout linearLayout = (LinearLayout) LayoutInflater
				.from(this)
				.inflate(R.layout.activity_worksite_supervision_grade_item, null);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, UiUtil.dp2px(this, 50));
		lp.setMargins(UiUtil.dp2px(this, 10), 0, 0, 0);
		linearLayout.setLayoutParams(lp);
		TextView tvName = (TextView) linearLayout.findViewById(R.id.tv_name);
		tvName.setText(rankType.getName());
		RatingBar rbScore = (RatingBar) linearLayout
				.findViewById(R.id.rb_grade_score);
		rbScore.setMax(rankType.getMaxScore());
		rbScore.setTag(rankType.getId());
		rbScore.setId(rankType.getId());
		if (type == 0) {
			mSupervisorZone.addView(linearLayout);
		} else {
			mForemanZone.addView(linearLayout);
		}
	}

	/**
	 * 提交评分
	 */
	private void httpRequestAddCommentToSupervision() {
		LogUtil.d("httpRequestAddCommentToSupervision");

		RequestParams params = new RequestParams();
		params.put("record_id", mRecordId);
		params.put("node_id", mNodeId);
		params.put("staff_content", mEdtSupervisorEvaluation.getText());
		String staffRank = getRanks(mSupervisorStaffList);
		if(TextUtils.isEmpty(staffRank)){
			showToastMsg("评分不完整");
			return;
		}
		params.put("staff_rank", staffRank);
		if ("p5".equals(mNodeId) && mGrade.getStore() != null && mGrade.getStore().getId() != 0) {
			params.put("store_content", mEdtForemanEvaluation.getText());
			String storeRank = getRanks(mSupervisorStoreList);
			if(TextUtils.isEmpty(storeRank)){
				showToastMsg("评分不完整");
				return;
			}
			params.put("store_rank",storeRank);
		}
		HttpClientApi.post(HttpClientApi.REQ_ACCOUNT_SUPERVISION_ADD_COMMENT,
				params, new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
						LogUtil.d("httpRequestAddCommentToSupervision onStart");

						showWaitDialog(getString(R.string.txt_submit_ing));
					}

					@Override
					public void onFinish() {
						super.onFinish();
						LogUtil.d("httpRequestAddCommentToSupervision onFinish");

						hideWaitDialog();
					}

					@Override
					public void onFailure(NetroidError netroidError) {
						LogUtil.d("httpRequestAddCommentToSupervision onFailure NetroidError = "
								+ netroidError);

						showToastMsg(netroidError.getMessage());
					}

					@Override
					public void onSuccess(String responseInfo) {
						LogUtil.d("httpRequestAddCommentToSupervision onSuccess responseInfo = "
								+ responseInfo);

						showToastMsg(getString(R.string.txt_score_success));
						setResult(RESULT_OK);
						finish();
					}

				});
	}

	/**
	 * 把评分结果封装成{"":,"":}字符串
	 * 
	 * @param rankTypes
	 * @return
	 */
	private String getRanks(List<RankType> rankTypes) {
		StringBuffer rank = new StringBuffer();
		boolean isFirst = false;
		rank.append("{");
		for (RankType rankType : rankTypes) {
			RatingBar ratingBar = (RatingBar) findViewById(rankType.getId());
			float score = ratingBar.getRating();
			if(score == 0){
				return null;
			}
			if (isFirst) {
				rank.append(",");
			}
			isFirst = true;
			rank.append("\"" + rankType.getId() + "\":" + score);
		}
		rank.append("}");
		return rank.toString();
	}

	/**
	 * 统一改变子项的分数
	 * 
	 * @param rankTypes
	 * @param rating
	 */
	private void changeScoreAll(List<RankType> rankTypes, Float rating) {
		for (RankType rankType : rankTypes) {
			RatingBar ratingBar = (RatingBar) findViewById(rankType.getId());
			ratingBar.setRating(rating);
		}
	}

}
