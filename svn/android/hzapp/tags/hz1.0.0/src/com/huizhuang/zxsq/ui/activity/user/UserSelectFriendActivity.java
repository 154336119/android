package com.huizhuang.zxsq.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.UserGroup;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.module.parser.UserGroupParser;
import com.huizhuang.zxsq.ui.activity.MainActivity;
import com.huizhuang.zxsq.ui.adapter.SelectFriendListViewAdapter;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * 选择推荐好友页面
 * 
 * @ClassName: UserSelectFriendActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-20 下午6:33:15
 */
public class UserSelectFriendActivity extends BaseActivity {

	private ListView mListView;
	private SelectFriendListViewAdapter mSelectFriendListViewAdapter;
	private Button mBtnStrat;

	// TODO 以后要动态获取城市和用户名
	private String mUserCity = "";
	private String mUserName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.user_recommendfriends_activity);

		initActionBar();
		initViews();

		httpRequestQueryRecommend(mUserCity, mUserName);
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
		commonActionBar.setActionBarTitle(R.string.txt_select_friends);
		commonActionBar.setRightTxtBtn(R.string.skip, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnSkipOnClick();
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initViews");

		mListView = (ListView) findViewById(R.id.lv_select_friend);
		mSelectFriendListViewAdapter = new SelectFriendListViewAdapter(this);
		mListView.setAdapter(mSelectFriendListViewAdapter);

		mBtnStrat = (Button) findViewById(R.id.btn_beginning);
		mBtnStrat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnStartOnClick(v);
			}
		});
	}

	/**
	 * 按钮事件 - 跳过
	 */
	private void btnSkipOnClick() {
		LogUtil.d("btnSkipOnClick");

		ActivityUtil.nextActivityWithClearTop(this, MainActivity.class);
	}

	/**
	 * 按钮事件 - 开启
	 * 
	 * @param v
	 */
	protected void btnStartOnClick(View v) {
		LogUtil.d("btnStartOnClick View = " + v);

		String formatFriendIdString = mSelectFriendListViewAdapter.getSelectPeopleIdFormatString();
		httpRequestSaveRecommendPeople(formatFriendIdString);
	}

	/**
	 * HTTP请求 - 获取推荐好友列表
	 */
	private void httpRequestQueryRecommend(String city, String username) {
		LogUtil.d("httpRequestQueryRecommend city = " + city + " username = " + username);

		RequestParams params = new RequestParams();
		params.put("city", city);
		params.put("username", username);
		HttpClientApi.get(HttpClientApi.REQ_USER_GET_FRIEND_LIST, params, new UserGroupParser(), new RequestCallBack<UserGroup>() {

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestQueryRecommend onStart");

				showWaitDialog(getString(R.string.txt_on_waiting));
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestQueryRecommend onFinish");

				hideWaitDialog();
			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestQueryRecommend onFailure NetroidError = " + netroidError);

				showToastMsg(netroidError.getMessage());
			}

			@Override
			public void onSuccess(UserGroup userGroup) {
				LogUtil.d("httpRequestQueryRecommend onSuccess UserGroup = " + userGroup);

				mSelectFriendListViewAdapter.setList(userGroup);
			}

		});
	}

	/**
	 * HTTP请求 - 保存关注的人
	 */
	private void httpRequestSaveRecommendPeople(final String strFriends) {
		LogUtil.d("httpRequestSaveRecommendPeople strFriends = " + strFriends);

		RequestParams params = new RequestParams();
		params.put("friends", strFriends);
		HttpClientApi.get(HttpClientApi.REQ_USER_SAVE_FRIENDS, params, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.d("httpRequestSaveRecommendPeople onStart");

				showWaitDialog(getString(R.string.txt_on_waiting));
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.d("httpRequestSaveRecommendPeople onFinish");

				hideWaitDialog();
			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.d("httpRequestSaveRecommendPeople onFailure NetroidError = " + netroidError);

				showToastMsg(netroidError.getMessage());
			}

			@Override
			public void onSuccess(Result result) {
				LogUtil.d("httpRequestSaveRecommendPeople onSuccess Result = " + result);

				showToastMsg(getString(R.string.txt_focus_success));
				btnSkipOnClick();
			}

		});
	}

}
