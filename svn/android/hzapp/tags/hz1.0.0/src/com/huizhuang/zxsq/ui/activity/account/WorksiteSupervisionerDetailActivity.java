package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Comments;
import com.huizhuang.zxsq.http.bean.account.Supervisioner;
import com.huizhuang.zxsq.http.task.account.GetSupervisionerDetailTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @ClassName: WorksiteSupervisionerDetailActivity
 * @Description: 监理师详情
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-1-22 上午11:06:10
 * 
 */
public class WorksiteSupervisionerDetailActivity extends BaseActivity {
	public static final String SUPERVISIONER_KEY = "supervisioner";

	private DataLoadingLayout mDataLoadingLayout;

	private LinearLayout mLinCommentsContainer;
	private CircleImageView mCivHeadImg;
	private TextView mTvName;
	private TextView mTvWorkYear;
	private TextView mTvServiceCount;
	private TextView mTvCount;
	private RatingBar mRbScore;
	
	private Supervisioner mSupervisioner;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worksite_supervisioner_detail);
		initIntentExtraData();
		initActionBar();
		initVews();
		httpRequestQuerySupervisionerData();
	}

	private void initIntentExtraData(){
		mSupervisioner = (Supervisioner)getIntent().getSerializableExtra(SUPERVISIONER_KEY);
	}
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.txt_supervisioner_detail);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						btnBackOnClick();
					}
				});
	}

	private void initVews() {
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.common_dl);
		mLinCommentsContainer = (LinearLayout) findViewById(R.id.lin_comments_container);
		mCivHeadImg = (CircleImageView) findViewById(R.id.civ_head);
		mTvName = (TextView) findViewById(R.id.tv_name);
		mTvWorkYear = (TextView) findViewById(R.id.tv_work_year);
		mTvServiceCount = (TextView) findViewById(R.id.tv_service_count);
		mTvCount = (TextView) findViewById(R.id.tv_evaluate_count);
		mRbScore = (RatingBar) findViewById(R.id.rb_score);
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnSubmitOnClick();
			}
		});
	}

	private void httpRequestQuerySupervisionerData() {
		GetSupervisionerDetailTask task = new GetSupervisionerDetailTask(
				WorksiteSupervisionerDetailActivity.this,mSupervisioner.getId());
		task.setCallBack(new AbstractHttpResponseHandler<Supervisioner>() {

			@Override
			public void onSuccess(Supervisioner result) {
				mDataLoadingLayout.showDataLoadSuccess();
				if(result == null){
					mDataLoadingLayout.showDataEmptyView();
				}else{
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
	
	private void initViewData(Supervisioner supervisioner){
		if(supervisioner.getPhoto() != null){
			ImageLoader.getInstance().displayImage(supervisioner.getPhoto().getThumb_path(), mCivHeadImg, ImageLoaderOptions.getDefaultImageOption());
		}
		mTvName.setText(supervisioner.getName());
		Float score = supervisioner.getScore() == null ? 0:supervisioner.getScore();
		mRbScore.setRating(score);
		mTvCount.setText(Html.fromHtml("<font color='#ff6c38'>"+score+"</font>("+supervisioner.getComments_num()+"评价)"));
		mTvWorkYear.setText(Html.fromHtml("从事监理<font color='#ff6c38'>"+supervisioner.getWork_year()+"</font>年"));
		mTvServiceCount.setText(Html.fromHtml("服务业主<font color='#ff6c38'>"+supervisioner.getService_num()+"</font>户"));
		if(supervisioner.getComments_list() != null && supervisioner.getComments_list().size() > 0){
			addCommentsView(supervisioner.getComments_list());
		}
	}
	
	@SuppressLint("InflateParams")
	private void addCommentsView(List<Comments> commentses){
		for (Comments comments : commentses) {
			View view = LayoutInflater.from(this).inflate(R.layout.activity_worksite_supervisioner_detail_comments_item, null);
			CircleImageView itemHeadImg = (CircleImageView)view.findViewById(R.id.iv_item_img);
			TextView itemName = (TextView)view.findViewById(R.id.tv_item_name);
			RatingBar itemScore = (RatingBar)view.findViewById(R.id.rb_item_score);
			TextView itemTime = (TextView)view.findViewById(R.id.tv_item_time);
			TextView itemContent = (TextView)view.findViewById(R.id.tv_item_content);
			if(comments.getOperator_photo() != null){
				ImageLoader.getInstance().displayImage(comments.getOperator_photo().getImg_path(), itemHeadImg, ImageLoaderOptions.getDefaultImageOption());
			}
			itemName.setText(comments.getOperator_name());
			if(comments.getA_source() != null){
				itemScore.setRating(comments.getA_source());
			}
			itemContent.setText(comments.getContent());
			itemTime.setText(comments.getDate());
			mLinCommentsContainer.addView(view);
		}
	}
	
	/**
	 * 按钮事件 - 返回
	 */
	private void btnBackOnClick() {
		LogUtil.d("btnBackOnClick");

		finish();
	}

	/**
	 * 按钮事件 - 优化提交
	 */
	private void btnSubmitOnClick() {
		LogUtil.d("btnBackOnClick");
		Bundle bd = new Bundle();
		bd.putSerializable(SUPERVISIONER_KEY, mSupervisioner);
		ActivityUtil.backWithResult(WorksiteSupervisionerDetailActivity.this, RESULT_OK, bd);
	}
}
