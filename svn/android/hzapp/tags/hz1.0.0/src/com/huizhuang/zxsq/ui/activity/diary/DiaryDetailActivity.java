package com.huizhuang.zxsq.ui.activity.diary;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.module.Author;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.DiaryGroup;
import com.huizhuang.zxsq.module.DiaryNode;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.module.Visitor;
import com.huizhuang.zxsq.module.parser.DiaryGroupDetailParser;
import com.huizhuang.zxsq.module.parser.DiaryNodeParser;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.PersonalHomepageActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseListActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.ui.activity.zxbd.ZxbdIntroActivity;
import com.huizhuang.zxsq.ui.adapter.DiaryDetailListViewAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LocalRestrictClicksUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.ToastUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * @ClassName: DiaryDetailActivity
 * @Description: 日记详情
 * @author lim
 * @mail limshare@gmail.com
 * @date 2014-11-5 下午2:50:20
 * 
 */
public class DiaryDetailActivity extends BaseListActivity implements
		OnClickListener, IXListViewListener {

	private final int REQ_LOGIN_CODE = 661;

	private Author mAuthor;
	private Diary mShowDiary;

	private DataLoadingLayout mDataLoadingLayout;
	// /////////////顶部用户信息view///////////////////
	private View mActionBar;
	// actionbar关注按钮
	private Button mBtnActionBarFollow;
	private View mListHeaderView;
	// listheader关注按钮
	private Button mBtnHeaderFollow;
	//
	private XListView mXListView;
	private DiaryDetailListViewAdapter mAdapter;
	private int mPageIndex = 1;
	private PullListHandler mPullHandler;

	// 新日记提醒围观dialog
	private boolean mIsShowDialog;
	private Dialog mTipsDialog;
	private int mPostion;

	private boolean mIsInitLoad = true;

	private ArrayList<DiaryNode> mNodes;

	@SuppressLint("HandlerLeak")
	private Handler mClickHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			mPostion = msg.arg1;
			if (what == 0) {
				// 点赞
				String userId = null;
				if (null != ZxsqApplication.getInstance().getUser()) {
					userId = ZxsqApplication.getInstance().getUser().getId()
							+ "_diary";
				}
				if (LocalRestrictClicksUtil.getInstance().isUserCanClick(
						userId,
						String.valueOf(mAdapter.getItem(mPostion).getId()))) {
					AnalyticsUtil.onEvent(THIS, "18");
					like(mPostion);
				} else {
					ToastUtil.showMessageLong(THIS,
							getString(R.string.txt_restrict_clicks));
				}

			} else if (what == 1) {
				// 查看评论
				AnalyticsUtil.onEvent(THIS, "17");
				Bundle bd = new Bundle();
				bd.putSerializable(AppConstants.PARAM_DIARY,
						mAdapter.getItem(mPostion));
				ActivityUtil.next(THIS, DiaryDiscussListActivity.class, bd, -1);
			} else {
				// 分享
				Share share = new Share();
				share.setCallBack(true);
				share.setText(getResources().getString(R.string.txt_share_fkdq));
				// String url = AppConfig.URL_SHARE_DIARY +
				// mAdapter.getItem(mPostion).getId();
				// String text = getString(R.string.txt_share_zxrj);
				// share.setText(text);
				// share.setUrl(url);
				// share.setSite(url);
				// share.setSiteUrl(url);
				Util.showShare(false, DiaryDetailActivity.this, share);
			}
		}
	};

	private TextView mTxtNode;

	private ImageView ivHeadImg;

	private ImageView ivSex;

	private TextView tvName;

	private TextView tvRoomStyle;

	private TextView tvRoomType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mIsShowDialog = savedInstanceState.getBoolean("dialog");
		}
		setContentView(R.layout.diary_detail_activity);
		initExtrasData();
		initActionBar();
		initViews();
		updateFollowBtn();

	}

	private void initExtrasData() {
		mAuthor = (Author) getIntent().getExtras().getSerializable(
				AppConstants.PARAM_AUTHOR);
		mShowDiary = (Diary) getIntent().getExtras().getSerializable(
				AppConstants.PARAM_DIARY);
		mIsShowDialog = getIntent().getExtras().getBoolean("dialog", false);

	}

	private void initActionBar() {
		findViewById(R.id.btn_back).setOnClickListener(this);
		mActionBar = findViewById(R.id.ll_header);
		mBtnActionBarFollow = (Button) mActionBar.findViewById(R.id.btn_right);
		mBtnActionBarFollow.setOnClickListener(this);
		mTxtNode = (TextView) findViewById(R.id.txt_node);
		ImageView ivHeadImg = (ImageView) mActionBar
				.findViewById(R.id.iv_header_headimg);
		ivHeadImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				itemClickPersonalPage();
			}
		});
		ImageLoader.getInstance().displayImage(mAuthor.getAvatar(), ivHeadImg);
	}

	private void initUserData() {
		ImageLoader.getInstance().displayImage(mAuthor.getAvatar(), ivHeadImg);
		tvName.setText(mAuthor.getName());
		List<KeyValue> styles = ZxsqApplication.getInstance().getConstant()
				.getRoomStyles();
		List<KeyValue> type = ZxsqApplication.getInstance().getConstant()
				.getRoomTypes();
		tvRoomStyle.setText(getValue(mAuthor.getRoomStyle(), styles));
		tvRoomType.setText(getValue(mAuthor.getRoomType(), type));
		if (mAuthor.getGender() != null && mAuthor.getGender().equals("1")) {
			ivSex.setImageResource(R.drawable.icon_boy);
		} else {
			ivSex.setImageResource(R.drawable.icon_girl);
		}
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
		mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadData(0);
			}
		});

		View view = LayoutInflater.from(this).inflate(
				R.layout.diary_detail_header_layout, null, false);
		mListHeaderView = view;
		mBtnHeaderFollow = (Button) mListHeaderView
				.findViewById(R.id.btn_follow);
		mBtnHeaderFollow.setOnClickListener(this);

		ivHeadImg = (ImageView) view.findViewById(R.id.iv_headimg);
		ivSex = (ImageView) view.findViewById(R.id.iv_sex);
		tvName = (TextView) view.findViewById(R.id.tv_name);
		tvRoomStyle = (TextView) view.findViewById(R.id.tv_room_style);
		tvRoomType = (TextView) view.findViewById(R.id.tv_room_type);

		ivHeadImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				itemClickPersonalPage();
			}
		});

		initUserData();

		mPullHandler = new PullListHandler();
		mAdapter = new DiaryDetailListViewAdapter(this, mClickHandler);
		mXListView = (XListView) getListView();
		mXListView.addHeaderView(view);
		mXListView.setPullRefreshEnable(true);// 显示刷新
		mXListView.setPullLoadEnable(false);// 显示加载更多
		mXListView.setAutoRefreshEnable(true);// 开始自动加载
		mXListView.setAutoLoadMoreEnable(true);// 滚动到底部自动加载更多
		mXListView.setXListViewListener(this);
		// 滚动不加载
		PauseOnScrollListener listener = new PauseOnScrollListener(
				ImageLoader.getInstance(), true, true);
		mXListView.setOnScrollListener(listener);
		mXListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			private int visibleItemId;
			private String visibleString = "";

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (visibleItemId != firstVisibleItem) {
					LogUtil.i(" size:" + view.getAdapter().getCount());
					if (mAdapter.getNodes() != null
							&& view.getAdapter().getItem(firstVisibleItem) != null) {
						String name = ((Diary) view.getAdapter().getItem(
								firstVisibleItem)).getZxNode();
						for (int i = 0; i < mNodes.size(); i++) {
							if (mNodes.get(i).getId().equals(name)) {
								String nodeName = mNodes.get(i).getName();
								if (!visibleString.equals(nodeName)) {
									int count = mNodes.get(i).getDiaryCount();
									LogUtil.i("  change:" + nodeName);
									mTxtNode.setText(nodeName + "(" + count
											+ "篇)");
								}
								visibleString = nodeName;
							}
						}
						LogUtil.i("  name:" + name + "  firstVisibleItem:"
								+ firstVisibleItem);
					}
				}
				visibleItemId = firstVisibleItem;
				if (firstVisibleItem > 1) {
					if (mActionBar.getVisibility() != View.VISIBLE) {
						mActionBar.setVisibility(View.VISIBLE);
					}
				} else {
					if (mActionBar.getVisibility() != View.INVISIBLE) {
						mActionBar.setVisibility(View.INVISIBLE);
					}
				}
			}

		});
		mXListView.setAdapter(mAdapter);
	}

	private void loadData(final int loadType) {
		RequestParams params = new RequestParams();
		params.put("profile_id", mAuthor.getId());
		params.put("orderby", "new");
		params.put("page", mPageIndex);
		HttpClientApi.post(HttpClientApi.REQ_USER_DIARY_LIST, params,
				new ResultParser(), new RequestCallBack<Result>() {

					@Override
					public void onStart() {
						super.onStart();
						if (loadType == 0 && mAdapter.getCount() == 0) {
							mDataLoadingLayout.showDataLoading();
						}
					}
					@Override
					public void onSuccess(Result result) {
						DiaryGroup group = null;
						mNodes = null;
						try {
							group = new DiaryGroupDetailParser()
									.parse(result.data);
							mNodes = new DiaryNodeParser().parse(result.data);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (group.size() == 0) {
							mDataLoadingLayout.showDataEmptyView();
						} else {
							mDataLoadingLayout.showDataLoadSuccess();
							mAuthor = group.get(0).getAuthor();
							initUserData();
							updateFollowBtn();
						}
						mAdapter.setNodes(mNodes);

						if (loadType == 0) {
							Message msg = mPullHandler.obtainMessage(
									PullListHandler.XLIST_REFRESH, group);
							mPullHandler.sendMessage(msg);
						} else {
							Message msg = mPullHandler.obtainMessage(
									PullListHandler.XLIST_LOAD, group);
							mPullHandler.sendMessage(msg);
						}
					}

					@Override
					public void onFailure(NetroidError error) {
						if (loadType == 1) {
							showToastMsg(error.getMessage());
							mPageIndex--;
						} else {
							mDataLoadingLayout.showDataLoadFailed(error
									.getMessage());
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Message msg = mPullHandler.obtainMessage(
								PullListHandler.XLIST_FINISH, loadType);
						mPullHandler.sendMessageDelayed(msg, 300);
					}
				});
	}

	private void like(final int position) {
		showWaitDialog("");
		RequestParams params = new RequestParams();
		params.put("cnt_id", mAdapter.getItem(position).getId());
		params.put("cnt_type", HttpClientApi.CntType.app_diary_v2);

		HttpClientApi.post(HttpClientApi.REQ_COMMON_LIKE, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(String response) {
						try {
							JSONObject json = new JSONObject(response);
							mAdapter.getItem(position).setLikeNum(json.getJSONObject("data").getString("like_num"));
						} catch (JSONException e) {
							e.printStackTrace();
						}

						// 设置本地重复点击事件
						String userId = null;
						if (null != ZxsqApplication.getInstance().getUser()) {
							userId = ZxsqApplication.getInstance().getUser().getId() + "_diary";
						}
						String targetId = null;
						if (null != mAdapter.getList() & mAdapter.getList().size() > 0) {
							Diary diary = mAdapter.getItem(position);
							if (null != diary) {
								targetId = String.valueOf(diary.getId());
							}
						}
						LocalRestrictClicksUtil.getInstance().setUserClickStateDiseable(userId, targetId);
						mAdapter.updateView(mXListView, position);
					}

					@Override
					public void onFailure(NetroidError error) {
						showToastMsg(error.getMessage());
					}

					@Override
					public void onFinish() {
						super.onFinish();
						hideWaitDialog();
					}
				});
	}

	/**
	 * 关注好友
	 */
	private void follow() {
		if (mAuthor != null) {
			if (!ZxsqApplication.getInstance().isLogged()) {
				ActivityUtil.next(THIS, UserLoginActivity.class, null,
						REQ_LOGIN_CODE);
				return;
			}

			// 自己不能关注自己
			if (mAuthor.getId().equals(
					ZxsqApplication.getInstance().getUser().getId())) {
				return;
			}

			showWaitDialog("");
			RequestParams params = new RequestParams();
			if (ZxsqApplication.getInstance().isLogged()) {
				params.put("user_id", ZxsqApplication.getInstance().getUser().getId());
			}
			String url = null;
			if ("0".equals(mAuthor.getFollowed())) {
				url = HttpClientApi.REQ_USER_SAVE_FRIENDS;
				params.put("friends", "[" + mAuthor.getId() + "]");
			} else {
				url = HttpClientApi.REQ_USER_DELETE_FRIENDS;
				params.put("followings", "[" + mAuthor.getId() + "]");
			}
			HttpClientApi.post(url, params, new RequestCallBack<String>() {

				@Override
				public void onSuccess(String data) {
					if ("0".equals(mAuthor.getFollowed())) {
						mAuthor.setFollowed("1");
						showToastMsg("关注成功");
					} else {
						mAuthor.setFollowed("0");
						showToastMsg("取消关注成功");
					}
					updateFollowBtn();
				}

				@Override
				public void onFailure(NetroidError arg0) {
					showToastMsg(arg0.getMessage());
				}

				@Override
				public void onFinish() {
					super.onFinish();
					hideWaitDialog();
				}

			});
		}
	}

	/**
	 * 新日记提醒dialog
	 */
	@SuppressLint("InflateParams")
	private void showTipsDialog() {
		View view = LayoutInflater.from(THIS).inflate(
				R.layout.diary_edit_tips_dialog, null);
		view.findViewById(R.id.btn_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mTipsDialog.dismiss();
					}
				});
		view.findViewById(R.id.btn_weiguan).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						ActivityUtil.next(THIS,
								TipsFriendsOnlookersActivity.class);
					}
				});
		view.findViewById(R.id.btn_jnmj).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						ActivityUtil.next(THIS, ZxbdIntroActivity.class);
					}
				});
		mTipsDialog = new Dialog(THIS, R.style.Dialog);
		mTipsDialog.setContentView(view);
		mTipsDialog.setCancelable(false);
		mTipsDialog.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mIsShowDialog) {
			mIsShowDialog = false;
			showTipsDialog();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("dialog", mIsShowDialog);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:
			follow();
			break;
		case R.id.btn_follow:
			follow();
			break;

		default:
			break;
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	@SuppressLint("HandlerLeak")
	private class PullListHandler extends Handler {

		public static final int XLIST_REFRESH = 0;
		public static final int XLIST_LOAD = 1;
		public static final int XLIST_FINISH = 2;

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case XLIST_REFRESH:
			case XLIST_LOAD:
				if (mAdapter != null) {
					DiaryGroup group = (DiaryGroup) msg.obj;
					if (msg.what == XLIST_REFRESH) {
						mAdapter.setList(group);
					} else {
						mAdapter.addList(group);
					}
					// 是否可以加载更多
					if (group.getTotalSize() == mAdapter.getCount()) {
						mXListView.setPullLoadEnable(false);
					} else {
						mXListView.setPullLoadEnable(true);
					}

					mAdapter.notifyDataSetChanged();
					if (mIsInitLoad && mShowDiary != null) {
						mIsInitLoad = false;
						for (int i = 0; i < group.size(); i++) {
							if (mShowDiary.getId() == group.get(i).getId()) {
								mXListView.setSelection(i + 2);
								break;
							}
						}
					}

				}
				break;
			case XLIST_FINISH:
				int type = (Integer) msg.obj;
				if (type == 0) {
					mXListView.onRefreshComplete(); // 下拉刷新完成
				} else {
					mXListView.onLoadMoreComplete(); // 加载更多完成
				}
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onRefresh() {
		mPageIndex = 1;
		loadData(0);
	}

	@Override
	public void onLoadMore() {
		mPageIndex++;
		loadData(1);
	}

	/**
	 * 更新关注按钮状态
	 */
	private void updateFollowBtn() {
		boolean showFollow = true;
		if (ZxsqApplication.getInstance().isLogged()) {
			if (mAuthor.getId().equals(
					ZxsqApplication.getInstance().getUser().getId())) {
				showFollow = false;
			}
		}
		if (showFollow) {
			if (mAuthor.getFollowed() == null
					|| mAuthor.getFollowed().equals("0")) {
				mBtnActionBarFollow.setText("+关注");
				mBtnHeaderFollow.setText("+关注");
			} else {
				mBtnActionBarFollow.setText("—取消关注");
				mBtnHeaderFollow.setText("—取消关注");
			}
		} else {
			mBtnActionBarFollow.setVisibility(View.GONE);
			mBtnHeaderFollow.setVisibility(View.GONE);
		}

	}

	private String getValue(String str, List<KeyValue> type) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		int length = type.size();
		String name = null;
		for (int i = 0; i < length; i++) {
			KeyValue keyValue = type.get(i);
			if (keyValue.getId().equals(str)) {
				name = keyValue.getName();
				break;
			}
		}
		return name;
	}

	/**
	 * 分享日记
	 * 
	 * @param id
	 *            日记ID
	 * @param share_type
	 *            分享类型
	 */
	private void httpRequestToShare(final int position, int share_type) {
		showWaitDialog("分享中...");
		RequestParams params = new RequestParams();
		params.put("cnt_id", mAdapter.getItem(position).getId());
		params.put("cnt_type", HttpClientApi.CntType.app_diary_v2);
		params.put("share_type", share_type);
		HttpClientApi.post(HttpClientApi.REQ_COMMON_SHARE, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(String response) {
						showToastMsg("分享成功");
						try {
							JSONObject json = new JSONObject(response);
							mAdapter.getItem(position).setShareNum(
									json.getJSONObject("data").getString(
											"share_num"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mAdapter.updateView(mXListView, position);
					}

					@Override
					public void onFailure(NetroidError error) {
						showToastMsg(error.getMessage());
					}

					@Override
					public void onFinish() {
						super.onFinish();
						hideWaitDialog();
					}

				});
	}

	private void itemClickPersonalPage() {
		// 查看个人主页
		Visitor visitor = new Visitor();
		visitor.setId(mAuthor.getId());
		visitor.setName(mAuthor.getName());
		Bundle bd = new Bundle();
		bd.putSerializable(AppConstants.PARAM_VISOTOR, visitor);
		ActivityUtil.next(THIS, PersonalHomepageActivity.class, bd, -1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQ_LOGIN_CODE) {
				updateFollowBtn();
				follow();
			} else if (requestCode == AppConstants.TO_SHARE_CODE) {
				int shareType = data.getIntExtra("shareType", 0);
				httpRequestToShare(mPostion, shareType);
			}
		}
	}
}
