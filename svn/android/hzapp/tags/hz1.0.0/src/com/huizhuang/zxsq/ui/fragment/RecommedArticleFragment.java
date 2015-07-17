package com.huizhuang.zxsq.ui.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Article;
import com.huizhuang.zxsq.module.ArticleGroup;
import com.huizhuang.zxsq.module.parser.ArticleGroupParser;
import com.huizhuang.zxsq.ui.activity.ArticleDetailActivity;
import com.huizhuang.zxsq.ui.adapter.ArticleELVAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.widget.XExpandableListView;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * @ClassName: RecommedArticleFragment
 * @Package com.huizhuang.zxsq.ui.activity.fragment
 * @Description: 推荐文章
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-8-26  上午10:14:43\
 * 
 * 罗云平说是测试版的，现在已经不用了？
 */
@Deprecated 
public class RecommedArticleFragment extends BaseFragment implements XExpandableListView.IXListViewListener {

	private ArticleELVAdapter mAdapter;
	private XExpandableListView mXListView;
	
	private int mPageIndex = 1;
	private PullListHandler mPullHandler;
	
	public static RecommedArticleFragment newInstance(){
		return new RecommedArticleFragment();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPullHandler = new PullListHandler();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_recommed_article, container, false);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText("美文推荐");
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ArrayList<Article> list = new ArrayList<Article>();
		mAdapter = new ArticleELVAdapter(getActivity(), list);

		mXListView = (XExpandableListView) findViewById(R.id.xlv);
		mXListView.setPullRefreshEnable(true);//显示刷新
		mXListView.setPullLoadEnable(false);//显示加载更多
		mXListView.setAutoRefreshEnable(true);//开始自动加载
		mXListView.setAutoLoadMoreEnable(true);//滚动到底部自动加载更多
		mXListView.setXListViewListener(this);
		mXListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				Article article = mAdapter.getList().get(groupPosition);
				Bundle bd = new Bundle();
				bd.putSerializable(AppConstants.PARAM_ARTICLE, article);
				ActivityUtil.next(getActivity(), ArticleDetailActivity.class, bd, -1);
				return true;
			}
		});
		mXListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				Article article = mAdapter.getList().get(groupPosition).getChildArticle().get(childPosition);
				Bundle bd = new Bundle();
				bd.putSerializable(AppConstants.PARAM_ARTICLE, article);
				ActivityUtil.next(getActivity(), ArticleDetailActivity.class, bd, -1);
				return true;
			}
		});
		
		PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true);
		mXListView.setOnScrollListener(listener);
		mXListView.setAdapter(mAdapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	private void loadData(final int loadType){
		RequestParams params = new RequestParams();
		params.put("page", mPageIndex);
		if (ZxsqApplication.getInstance().isLogged()) {
			params.put("user_id", ZxsqApplication.getInstance().getUser().getId());
		}
		HttpClientApi.post(HttpClientApi.REQ_ARTICLE_RECOMMED_LIST, params, new ArticleGroupParser(), new RequestCallBack<ArticleGroup>() {
			
			@Override
			public void onSuccess(ArticleGroup group) {
				if (group.size() == 0) {
					showToastMsg("没有数据");
				}
				if (loadType == 0) {
					Message msg = mPullHandler.obtainMessage(PullListHandler.XLIST_REFRESH, group);
					mPullHandler.sendMessage(msg);
				}else{
					Message msg = mPullHandler.obtainMessage(PullListHandler.XLIST_LOAD, group);
					mPullHandler.sendMessage(msg);
				}
			}
			
			@Override
			public void onFailure(NetroidError error) {
				showToastMsg(error.getMessage());
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				Message msg = mPullHandler.obtainMessage(PullListHandler.XLIST_FINISH, loadType);
				mPullHandler.sendMessageDelayed(msg, 300);
			}
			
		});
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
					ArticleGroup group = (ArticleGroup) msg.obj;
					if (msg.what == XLIST_REFRESH) {
						mAdapter.setList(group);
					} else {
						mAdapter.addList(group);
					}
					//是否可以加载更多
					if (group.getTotalSize() == mAdapter.getGroupCount()) {
						mXListView.setPullLoadEnable(false);
					} else {
						mXListView.setPullLoadEnable(true);
					}
					
					for (int i = 0; i < group.size(); i++) {
						mXListView.expandGroup(i);
					}
					
					mAdapter.notifyDataSetChanged();
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

}
