package com.huizhuang.zxsq.ui.activity.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Author;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.DiaryGroup;
import com.huizhuang.zxsq.module.DiaryNode;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.module.parser.DiaryGroupDetailParser;
import com.huizhuang.zxsq.module.parser.DiaryNodeParser;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.base.BaseListActivity;
import com.huizhuang.zxsq.ui.activity.diary.DiaryDetailActivity;
import com.huizhuang.zxsq.ui.activity.diary.DiaryDiscussListActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyDiaryActivity extends BaseListActivity implements OnClickListener, IXListViewListener {

    private DataLoadingLayout mDataLoadingLayout;
    private DiaryDetailListViewAdapter mAdapter;
    private XListView mXListView;
    private int mPageIndex = 1;
    private PullListHandler mPullHandler;
    private ArrayList<DiaryNode> mNodes;

    private View mNodeZone;
    private TextView mTvNode;
    private int mPostion;

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
                    userId = ZxsqApplication.getInstance().getUser().getId() + "_diary";
                }
                if (LocalRestrictClicksUtil.getInstance().isUserCanClick(userId, String.valueOf(mAdapter.getItem(mPostion).getId()))) {
                    AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_DIARY_SHARE);
                    like(mPostion);
                } else {
                    ToastUtil.showMessageLong(THIS, getString(R.string.txt_restrict_clicks));
                }

            } else if (what == 1) {
                // 查看评论
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_DIARY_DISCUSS);
                Bundle bd = new Bundle();
                bd.putSerializable(AppConstants.PARAM_DIARY, mAdapter.getItem(mPostion));
                ActivityUtil.next(THIS, DiaryDiscussListActivity.class, bd, -1);
            } else {
                // 分享
                Share share = new Share();
                share.setCallBack(true);
                String text = getString(R.string.txt_share_zxrj);
                share.setText(text);
                Util.showShare(false, MyDiaryActivity.this, share);
            }
        }
    };

    private void like(final int position) {
        showWaitDialog("");
        RequestParams params = new RequestParams();
        params.put("cnt_id", mAdapter.getItem(position).getId());
        params.put("cnt_type", HttpClientApi.CntType.app_diary_v2);

        HttpClientApi.post(HttpClientApi.REQ_COMMON_LIKE, params, new RequestCallBack<String>() {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_my_diary_activity);
        setHeader("我的日记", this);

        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_load_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadData(0);
            }
        });

        mNodeZone = findViewById(R.id.ll_node_zone);
        mTvNode = (TextView) findViewById(R.id.txt_node);

        mPullHandler = new PullListHandler();
        mAdapter = new DiaryDetailListViewAdapter(THIS, mClickHandler);
        mXListView = (XListView) getListView();
        mXListView.setPullRefreshEnable(true);// 显示刷新
        mXListView.setPullLoadEnable(false);// 显示加载更多
        mXListView.setAutoRefreshEnable(true);// 开始自动加载
        mXListView.setAutoLoadMoreEnable(true);// 滚动到底部自动加载更多
        mXListView.setXListViewListener(this);
        // 滚动不加载
        mXListView.setOnScrollListener(new OnScrollListener() {

            private int visibleItemId;
            private String visibleString = "";

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemId != firstVisibleItem) {
                    LogUtil.i(" size:" + view.getAdapter().getCount());
                    if (mAdapter.getNodes() != null && view.getAdapter().getItem(firstVisibleItem) != null) {
                        String name = ((Diary) view.getAdapter().getItem(firstVisibleItem)).getZxNode();
                        for (int i = 0; i < mNodes.size(); i++) {
                            if (mNodes.get(i).getId().equals(name)) {
                                String nodeName = mNodes.get(i).getName();
                                if (!visibleString.equals(nodeName)) {
                                    int count = mNodes.get(i).getDiaryCount();
                                    LogUtil.i("  change:" + nodeName);
                                    mTvNode.setText(nodeName + "(" + count + "篇)");
                                }
                                visibleString = nodeName;
                            }
                        }
                        LogUtil.i("  name:" + name + "  firstVisibleItem:" + firstVisibleItem);
                    }
                }
                visibleItemId = firstVisibleItem;
                if (firstVisibleItem > 0) {
                    if (mNodeZone.getVisibility() != View.VISIBLE) {
                        mNodeZone.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mNodeZone.getVisibility() != View.INVISIBLE) {
                        mNodeZone.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        mXListView.setAdapter(mAdapter);

    }

    private void loadData(final int loadType) {
        RequestParams params = new RequestParams();
        params.put("profile_id", ZxsqApplication.getInstance().getUser().getId());
        params.put("orderby", "new");
        params.put("page", mPageIndex);
        HttpClientApi.post(HttpClientApi.REQ_USER_DIARY_LIST, params, new ResultParser(), new RequestCallBack<Result>() {

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
                    group = new DiaryGroupDetailParser().parse(result.data);
                    mNodes = new DiaryNodeParser().parse(result.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (group.size() == 0) {
                    mDataLoadingLayout.showDataEmptyView();
                } else {
                    mDataLoadingLayout.showDataLoadSuccess();
                }

                mAdapter.setNodes(mNodes);

                if (loadType == 0) {
                    Message msg = mPullHandler.obtainMessage(PullListHandler.XLIST_REFRESH, group);
                    mPullHandler.sendMessage(msg);
                } else {
                    Message msg = mPullHandler.obtainMessage(PullListHandler.XLIST_LOAD, group);
                    mPullHandler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(NetroidError error) {
                if (loadType == 1) {
                    showToastMsg(error.getMessage());
                    mPageIndex--;
                } else {
                    mDataLoadingLayout.showDataLoadFailed(error.getMessage());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Message msg = mPullHandler.obtainMessage(PullListHandler.XLIST_FINISH, loadType);
                mPullHandler.sendMessageDelayed(msg, 300);
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Author author = mAdapter.getList().get(position - 1).getAuthor();
        Bundle bd = new Bundle();
        bd.putSerializable(AppConstants.PARAM_AUTHOR, author);
        ActivityUtil.next(THIS, DiaryDetailActivity.class, bd, -1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;

            default:
                break;
        }
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
                        // if (mIsInitLoad && mShowDiary != null) {
                        // mIsInitLoad = false;
                        // for (int i = 0; i < group.size(); i++) {
                        // if (mShowDiary.getId() == group.get(i).getId()) {
                        // mXListView.setSelection(i + 2);
                        // break;
                        // }
                        // }
                        // }

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
     * 分享日记
     *
     * @param position   日记ID
     * @param share_type 分享类型
     */
    private void httpRequestToShare(final int position, int share_type) {
        showWaitDialog("分享中...");
        RequestParams params = new RequestParams();
        params.put("cnt_id", mAdapter.getItem(position).getId());
        params.put("cnt_type", HttpClientApi.CntType.app_diary_v2);
        params.put("share_type", share_type);
        HttpClientApi.post(HttpClientApi.REQ_COMMON_SHARE, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(String response) {
                showToastMsg("分享成功");
                try {
                    JSONObject json = new JSONObject(response);
                    mAdapter.getItem(position).setShareNum(json.getJSONObject("data").getString("share_num"));
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.TO_SHARE_CODE) {
                int shareType = data.getIntExtra("shareType", 0);
                httpRequestToShare(mPostion, shareType);
            }
        }
    }
}
