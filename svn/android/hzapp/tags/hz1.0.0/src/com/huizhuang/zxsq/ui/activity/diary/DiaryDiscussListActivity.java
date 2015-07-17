package com.huizhuang.zxsq.ui.activity.diary;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ListView;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.Discuss;
import com.huizhuang.zxsq.module.DiscussGroup;
import com.huizhuang.zxsq.module.parser.DiscussGroupParser;
import com.huizhuang.zxsq.ui.adapter.DiaryDiscussListViewAdapter;
import com.huizhuang.zxsq.ui.adapter.DiscussListViewAdapter;
import com.huizhuang.zxsq.ui.activity.base.BaseListActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LocalRestrictClicksUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * @ClassName: DiaryDiscussListActivity
 * @Description: 日记评论列表
 * @author lim
 * @mail limshare@gmail.com
 * @date 2014-11-7 下午2:25:45
 * 
 */
public class DiaryDiscussListActivity extends BaseListActivity implements OnClickListener, IXListViewListener {

	private final int REQ_DISCUSS_CODE = 666;
	private final int REQ_LOGIN_CODE = 777;
	
	private Diary mDiary;

	private CommonActionBar mCommonActionBar;
	private DataLoadingLayout mDataLoadingLayout;

	private XListView mXListView;
	private DiaryDiscussListViewAdapter mAdapter;
	private int mCurrentPageIndex = 1;

	@SuppressLint("HandlerLeak")
	private Handler mClickHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			comment(msg.arg1, msg.arg2);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDiary = (Diary) getIntent().getExtras().getSerializable(AppConstants.PARAM_DIARY);
		setContentView(R.layout.diary_discuss_list_activity);
		initActionBar();
		initViews();
	}

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle("评论" + "(" + mDiary.getDiscussNum() + ")");
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initViews() {
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_load_layout);
		mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadData(0);
			}
		});

		mAdapter = new DiaryDiscussListViewAdapter(this, mClickHandler);
		View view = new View(this);
		view.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, UiUtil.dp2px(this, 56)));
		mXListView = (XListView) getListView();
		mXListView.addFooterView(view);
		mXListView.setPullRefreshEnable(true);// 显示刷新
		mXListView.setPullLoadEnable(false);// 显示加载更多
		mXListView.setAutoRefreshEnable(true);// 开始自动加载
		mXListView.setAutoLoadMoreEnable(true);// 滚动到底部自动加载更多
		mXListView.setXListViewListener(this);

		PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true);
		mXListView.setOnScrollListener(listener);
		mXListView.setAdapter(mAdapter);

		findViewById(R.id.btn_discuss).setOnClickListener(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_discuss :
				if (!ZxsqApplication.getInstance().isLogged()) {
					ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
				}else{
					Bundle bd = new Bundle();
					bd.putSerializable(AppConstants.PARAM_DIARY, mDiary);
					ActivityUtil.next(THIS, DiaryDiscussEditActivity.class, bd, REQ_DISCUSS_CODE);
				}
				break;

			default :
				break;
		}
	}

	private void loadData(final int loadType) {
		RequestParams params = new RequestParams();
		params.put("link_id", mDiary.getId());
		params.put("comment_key", HttpClientApi.CommentType.app_diary_v2);
		params.put("page", mCurrentPageIndex);
		HttpClientApi.post(HttpClientApi.REQ_GET_COMMENT_LIST, params, new DiscussGroupParser(), new RequestCallBack<DiscussGroup>() {

			@Override
			public void onStart() {
				super.onStart();
				if (loadType == 0) {
					mDataLoadingLayout.showDataLoading();
				}
			}

			@Override
			public void onSuccess(DiscussGroup group) {
				if (group.size() == 0) {
					showToastMsg("还没有评论");
					mDataLoadingLayout.showDataEmptyView();
				} else {
					mDataLoadingLayout.showDataLoadSuccess();
				}
				updateActionBarTitle(group.getTotalSize());
				if (loadType == 0) {
					Message msg = pullRefreshHandler.obtainMessage(PullListHandler.XLIST_REFRESH_FINISH, group);
					pullRefreshHandler.sendMessage(msg);
				} else {
					Message msg = pullRefreshHandler.obtainMessage(PullListHandler.XLIST_LOADH_FINISH, group);
					pullRefreshHandler.sendMessage(msg);
				}
			}

			@Override
			public void onFailure(NetroidError error) {
				if (loadType == 1) {
					showToastMsg(error.getMessage());
					mCurrentPageIndex--;
				} else {
					mDataLoadingLayout.showDataLoadFailed(error.getMessage());
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				Message msg = pullRefreshHandler.obtainMessage(PullListHandler.XLIST_REQUEST_FINISH, loadType);
				pullRefreshHandler.sendMessage(msg);
			}
		});
	}

	/**
	 * 评论点赞
	 * 
	 * @param type
	 * @param position
	 */
	private void comment(final int type, final int position) {
		showWaitDialog("");
		RequestParams params = new RequestParams();
		params.put("comment_id", mAdapter.getList().get(position).getId());
		String url = null;
		if (type == DiscussListViewAdapter.USER_FUL) {
			url = HttpClientApi.REQ_UP_COMMENT;
		} else if (type == DiscussListViewAdapter.STEP) {
			url = HttpClientApi.REQ_DOWN_COMMENT;
		}
		HttpClientApi.post(url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(String data) {
				try {
					JSONObject jsonObject = new JSONObject(data).getJSONObject("data");
					if (type == DiscussListViewAdapter.USER_FUL) {
						mAdapter.getList().get(position).setUpNum(jsonObject.getInt("up_num"));
						mAdapter.updateView(mXListView, position, type);
					} else if (type == DiscussListViewAdapter.STEP) {
						mAdapter.getList().get(position).SetDownNum(jsonObject.getInt("down_num"));
						mAdapter.updateView(mXListView, position, type);
					}
					// 设置本地重复点击事件
					String userId = null;
					if (null != ZxsqApplication.getInstance().getUser()) {
						userId = ZxsqApplication.getInstance().getUser().getId();
					}
					String targetId = null;
					if (null != mAdapter.getList() & mAdapter.getList().size() > 0) {
						Discuss discuss = mAdapter.getList().get(position);
						if (null != discuss) {
							targetId = String.valueOf(discuss.getId());
						}
					}
					LocalRestrictClicksUtil.getInstance().setUserClickStateDiseable(userId, targetId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
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

	private PullListHandler pullRefreshHandler = new PullListHandler();
	@SuppressLint("HandlerLeak")
	private class PullListHandler extends Handler {

		public static final int XLIST_REFRESH_FINISH = 0;
		public static final int XLIST_LOADH_FINISH = 1;
		public static final int XLIST_REQUEST_FINISH = 2;

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case XLIST_REFRESH_FINISH :
				case XLIST_LOADH_FINISH :
					if (mAdapter != null) {
						DiscussGroup group = (DiscussGroup) msg.obj;
						if (msg.what == XLIST_REFRESH_FINISH) {
							mAdapter.setList(group);
						} else {
							mAdapter.addList(group);
						}
						if (group.getTotalSize() == mAdapter.getCount()) {
							mXListView.setPullLoadEnable(false);
						} else {
							mXListView.setPullLoadEnable(true);
						}
						mAdapter.notifyDataSetChanged();
					}
					break;
				case XLIST_REQUEST_FINISH :
					int type = (Integer) msg.obj;
					if (type == 0) {
						mXListView.onRefreshComplete(); // 下拉刷新完成
					} else {
						mXListView.onLoadMoreComplete(); // 加载更多完成
					}
					break;
				default :
					break;
			}
		}

	}

	private void updateActionBarTitle(int size) {
		mCommonActionBar.setActionBarTitle("评论" + "(" + size + ")");
	}

	private void updateListView() {
		mXListView.onRefresh();
	}

	@Override
	public void onRefresh() {
		mCurrentPageIndex = 1;
		loadData(0);
	}

	@Override
	public void onLoadMore() {
		mCurrentPageIndex++;
		loadData(1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_DISCUSS_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				updateListView();
			}
		} else if (requestCode == REQ_LOGIN_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle bd = new Bundle();
				bd.putSerializable(AppConstants.PARAM_DIARY, mDiary);
				ActivityUtil.next(THIS, DiaryDiscussEditActivity.class, bd, REQ_DISCUSS_CODE);
			}
		}
	}
}
