package com.huizhuang.zxsq.ui.activity.zxbd;

import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.zxdb.Zxbd;
import com.huizhuang.zxsq.http.bean.zxdb.ZxbdDetail;
import com.huizhuang.zxsq.http.task.zxbd.ZxbdDetailTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.fragment.zxbd.ZxbdListFragment;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class ZxbdActivity extends BaseActivity {
	private CommonActionBar mCommonActionBar;
	private TextView mTvTitle;
	private DataLoadingLayout mDataLoadingLayout;
	private TextView mTvDigest;
	private WebView mWbContent;
	private TextView mTvTime;
	private Zxbd mZxbd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zxbd);
		getIntentExtra();
		initCommonActionBar();
		initView();
		initData();
	}

	private void getIntentExtra() {
		mZxbd = (Zxbd) getIntent().getExtras().getSerializable(
				ZxbdListFragment.EXTRA_ZXBD);
	}

	private void initCommonActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.zxbd_common_action_bar);
		mCommonActionBar.setActionBarTitle("装修宝典");
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initView() {
		/*mTvTitle = (TextView) findViewById(R.id.tv_zxbd_title);
		mTvDigest = (TextView) findViewById(R.id.tv_zxbd_digest);
		mTvTime = (TextView) findViewById(R.id.tv_zxbd_time);*/
		mWbContent = (WebView) findViewById(R.id.wb_zxbd_content);
		mWbContent.getSettings().setDefaultTextEncodingName("utf-8");
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_load_layout);
		mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initData();
			}
		});
	}

	private void initData() {
		if (mZxbd == null) {
			return;
		} else {
			ZxbdDetailTask zxbdDetailTask = new ZxbdDetailTask(this,
					mZxbd.getId());
			zxbdDetailTask
					.setCallBack(new AbstractHttpResponseHandler<ZxbdDetail>() {
						@Override
						public void onStart() {
							mDataLoadingLayout.showDataLoading();
						}

						@Override
						public void onSuccess(ZxbdDetail result) {
							if (result == null) {
								mDataLoadingLayout.showDataEmptyView();
							} else {
								mDataLoadingLayout.showDataLoadSuccess();
								/*mTvTitle.setText(result.getTitle());
								mTvDigest.setText(result.getDigest());
								String time = DateUtil.dateToStr(new Date(
										result.getAdd_time()), "yyyyMMdd");
								String year = time.substring(0, 4);
								String month = time.substring(4, 6);
								String day = time.substring(6);
								mTvTime.setText(year + "年" + month + "月" + day
										+ "日");*/
								mWbContent.loadDataWithBaseURL(null,result.getContent(), "text/html",  "utf-8", null);
							}
						}
						@Override
						public void onFailure(int code, String error) {
							mDataLoadingLayout.showDataLoadFailed(error);
						}
					});
			zxbdDetailTask.send();
		}
	}
}
