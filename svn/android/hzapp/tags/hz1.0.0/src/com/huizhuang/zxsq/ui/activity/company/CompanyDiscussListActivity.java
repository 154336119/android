package com.huizhuang.zxsq.ui.activity.company;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.module.Discuss;
import com.huizhuang.zxsq.module.DiscussGroup;
import com.huizhuang.zxsq.module.parser.DiscussGroupParser;
import com.huizhuang.zxsq.ui.activity.base.BaseListActivity;
import com.huizhuang.zxsq.ui.adapter.DiscussListViewAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

/**
 * @ClassName: DiscussListActivity
 * @Package com.huizhuang.zxsq.ui.activity
 * @Description: 公司评论
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-9-3  上午11:24:18
 */
public class CompanyDiscussListActivity extends BaseListActivity implements
		IXListViewListener {
	private boolean httpIsFirst = false;

	public static final String COMPANY_TAG= "app_store";
	public static final String DIARY_TAG= "app_diary_v2";
	private DiscussListViewAdapter mAdapter;
	private XListView mXListView;
	private View mHeaderView;
	private Button mButton;
	private CommonActionBar mCommonActionBar;
	private DataLoadingLayout mDataLoadingLayout;

	private Company mCompany;
	private int mCurrentPageIndex = 1;
	private Handler mClickHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			int type = msg.arg1;
			int position = msg.arg2;
			httpRequestComment(type,position);
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.discuss_list_activity);
		getIntentExtras();
		initActionBar();
		initViews();
		httpRequestLoadData(0);
	}


	private void initViews() {
		// TODO Auto-generated method stub
		mDataLoadingLayout = (DataLoadingLayout)findViewById(R.id.favorite_pictures_data_loading_layout);
		mButton = (Button)findViewById(R.id.btn_appointment);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bd3 = new Bundle();
				bd3.putSerializable(AppConstants.PARAM_COMPANY, mCompany);
				ActivityUtil.next(CompanyDiscussListActivity.this, CompanyOrderActivity.class,bd3, -1);
			}
		});
		mAdapter = new DiscussListViewAdapter(this,mClickHandler);
		TextView tvCp = (TextView)findViewById(R.id.tv_cp); 
	    if("0".equals(mCompany.getContractPrice())){
	    	tvCp.setText("暂无报价");
	    }else{
			tvCp.setText("￥"+mCompany.getContractPrice());
	    }
		mXListView = (XListView) getListView();
		mXListView.setPullRefreshEnable(true);//显示刷新
		mXListView.setPullLoadEnable(false);//显示加载更多
		mXListView.setAutoRefreshEnable(true);//开始自动加载
		mXListView.setAutoLoadMoreEnable(true);//滚动到底部自动加载更多
		mXListView.setXListViewListener(this);		
		mXListView.setAdapter(mAdapter);
		httpIsFirst = true;
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_discusslist);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				}
		});
	}

	private void getIntentExtras() {
		// TODO Auto-generated method stub
		mCompany = (Company) getIntent().getExtras().getSerializable(AppConstants.PARAM_COMPANY);
	}


	
	/**
	 * 评论列表
	 * @param loadType
	 */
	public void httpRequestLoadData(final int loadType){
		RequestParams params = new RequestParams();
		params.put("link_id",mCompany.getId());
		params.put("comment_key", HttpClientApi.CommentType.app_store);
		params.put("page", mCurrentPageIndex);
		HttpClientApi.post(HttpClientApi.REQ_GET_COMMENT_LIST, params, new DiscussGroupParser(), new RequestCallBack<DiscussGroup>() {

			@Override
			public void onSuccess(DiscussGroup group) {
				if (httpIsFirst)
					mDataLoadingLayout.showDataLoadSuccess();
				if (group.size() == 0) {
					mDataLoadingLayout.showDataEmptyView();
				}
				if (loadType == 0) {
					mAdapter.setList(group);
				}else{
					mAdapter.addList(group);
				}
				if (group.getTotalSize() == mAdapter.getCount()) {
					mXListView.setPullLoadEnable(false);
				} else {
					mXListView.setPullLoadEnable(true);
				}
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(NetroidError error) {
				mDataLoadingLayout.showDataLoadFailed(error.getMessage());
				if (loadType == 1) {
					mCurrentPageIndex--;
				}
				
//
//				if (loadType == 0) {
//					Message msg = mPullHandler.obtainMessage(PullListHandler.XLIST_REFRESH, mCompany);
//					mPullHandler.sendMessage(msg);
//				}else{
//					Message msg = mPullHandler.obtainMessage(PullListHandler.XLIST_LOAD, mCompany);
//					mPullHandler.sendMessage(msg);
//				}
				
			}
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				if (httpIsFirst)
					mDataLoadingLayout.showDataLoading();
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				if (loadType == 0) {
					mXListView.onRefreshComplete();	//下拉刷新完成
				}else{
					mXListView.onLoadMoreComplete(); //加载更多完成
				}
				if (httpIsFirst)
					httpIsFirst = false;
			}
		});
	}
	 


	@Override
	public void onRefresh() {
		mCurrentPageIndex = 1;
		httpRequestLoadData(0);
	}

	@Override
	public void onLoadMore() {
		mCurrentPageIndex++;
		httpRequestLoadData(1);
	}
	public void httpRequestComment(final int type,final int position){
		showWaitDialog("");
		RequestParams params = new RequestParams();
		params.put("comment_id",mAdapter.getList().get(position).getId());
		//params.put("user_id", AppContext.getInstance().getUser().getId());
		params.put("user_id","");

		String url = null;
		if (type == DiscussListViewAdapter.USER_FUL) {
			url = HttpClientApi.REQ_UP_COMMENT;
		}else if(type == DiscussListViewAdapter.STEP){
			url = HttpClientApi.REQ_DOWN_COMMENT;
		}
		
		HttpClientApi.post(url, params, new RequestCallBack<String>(){

			@Override
			public void onFailure(NetroidError arg0) {
				// TODO Auto-generated method stub
				
			}
 
			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				try {
					JSONObject jsonObject = new JSONObject(arg0).getJSONObject("data");
					if (type == DiscussListViewAdapter.USER_FUL) {
                        mAdapter.getList().get(position).setUpNum(jsonObject.optInt("up_num"));
                        Discuss discuss = mAdapter.getList().get(position);
                        discuss.setUpNum(jsonObject.optInt("up_num"));
						mAdapter.notifyDataSetChanged();
					}else if(type == DiscussListViewAdapter.STEP){
						mAdapter.getList().get(position).SetDownNum(jsonObject.optInt("down_num"));
						mAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				hideWaitDialog();
			}
			
		});
	}
}
