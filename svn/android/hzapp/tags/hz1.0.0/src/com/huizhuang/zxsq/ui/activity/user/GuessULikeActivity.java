package com.huizhuang.zxsq.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.GuessLikeGroup;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.parser.GuessLikeGroupParser;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.adapter.GuessLikeGridViewAdapter;
import com.huizhuang.zxsq.ui.adapter.GuessLikeGridViewAdapter.OnStyleCheckListener;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 猜你喜欢页面
 * 
 * @ClassName: GuessULikeActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-20 下午6:08:17
 */
public class GuessULikeActivity extends BaseActivity {

	private GridView mGrideView;
	private GuessLikeGridViewAdapter mGuessLikeGridViewAdapter;
	private Button mBtnSubmitOff;
	private Button mBtnSubmitOn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.grideview_ulike);

		initActionBar();
		initViews();

		httpRequestQueryStyleList();
	}

	@Override
	public void onBackPressed() {
		LogUtil.d("onBackPressed");

		btnSkipOnClick();
		super.onBackPressed();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.title_guess_like);
		commonActionBar.setRightTxtBtn(R.string.skip, new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnSkipOnClick();
			}

		});
	}

	/**
	 * 初始化视图
	 */
	private void initViews() {
		LogUtil.d("initViews");

		mGrideView = (GridView) findViewById(R.id.gv_guessLike);
		mGuessLikeGridViewAdapter = new GuessLikeGridViewAdapter(this);
		mGuessLikeGridViewAdapter.setOnStyleCheckListener(new OnStyleCheckListener() {

			@Override
			public void onCheckedChanged(int total) {
				if (0 != total) {
					mBtnSubmitOff.setVisibility(View.GONE);
					mBtnSubmitOn.setVisibility(View.VISIBLE);
				} else {
					mBtnSubmitOff.setVisibility(View.VISIBLE);
					mBtnSubmitOn.setVisibility(View.GONE);
				}
			}
		});
		mGrideView.setAdapter(mGuessLikeGridViewAdapter);

		mBtnSubmitOff = (Button) findViewById(R.id.tv_guessLike_off);
		mBtnSubmitOn = (Button) findViewById(R.id.tv_guessLike_on);
		mBtnSubmitOn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnSumbitOnClick(v);
			}

		});
	}

	/**
	 * 按钮事件 - 提交
	 * 
	 * @param v
	 */
	protected void btnSumbitOnClick(View v) {
		LogUtil.d("btnSumbitOnClick View = " + v);

		String formatString = mGuessLikeGridViewAdapter.getSelectStyleFormatString();
		httpRequestSaveStyleList(formatString);
	}

	/**
	 * 按钮事件 - 跳过
	 */
	protected void btnSkipOnClick() {
		LogUtil.d("btnSkipOnClick");

		ActivityUtil.next(this, UserSelectFriendActivity.class, true);
	}

	/**
	 * HTTP请求 - 获取猜你喜欢的列表
	 */
	private void httpRequestQueryStyleList() {
		LogUtil.d("httpRequestQueryStyleList");

		RequestParams params = new RequestParams();
		HttpClientApi.get(HttpClientApi.REQ_USER_GET_STYLE, params, new GuessLikeGroupParser(), new RequestCallBack<GuessLikeGroup>() {

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestQueryStyleList onStart");

				showWaitDialog(getString(R.string.txt_on_waiting));
			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestQueryStyleList onFailure NetroidError = " + netroidError);

				showToastMsg(netroidError.getMessage());
			}

			@Override
			public void onSuccess(GuessLikeGroup guessLikeGroup) {
				LogUtil.d("httpRequestQueryStyleList onSuccess GuessLikeGroup = " + guessLikeGroup);

				mGuessLikeGridViewAdapter.setList(guessLikeGroup);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestQueryStyleList onFinish");

				hideWaitDialog();
			}

		});
	}

	/**
	 * HTTP请求 - 保存猜你喜欢的列表
	 */
	private void httpRequestSaveStyleList(final String strArray) {
		LogUtil.d("httpRequestSaveStyleList strArray = " + strArray);

		RequestParams params = new RequestParams();
		params.put("styles", strArray);
		HttpClientApi.post(HttpClientApi.REQ_USER_SAVE_STYLE, params, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestSaveStyleList onStart");

				showWaitDialog(getString(R.string.txt_on_waiting));
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestSaveStyleList onFinish");

				hideWaitDialog();
			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestSaveStyleList onFailure NetroidError = " + netroidError);

				showToastMsg(netroidError.getMessage());
			}

			@Override
			public void onSuccess(Result result) {
				LogUtil.d("httpRequestSaveStyleList onSuccess Result = " + result);

				btnSkipOnClick();
			}

		});
	}

}
