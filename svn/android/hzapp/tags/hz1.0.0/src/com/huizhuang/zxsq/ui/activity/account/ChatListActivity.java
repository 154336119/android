package com.huizhuang.zxsq.ui.activity.account;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.ChatList;
import com.huizhuang.zxsq.http.bean.account.ForeManChat;
import com.huizhuang.zxsq.http.bean.account.SystemChat;
import com.huizhuang.zxsq.http.task.account.DelectChatTask;
import com.huizhuang.zxsq.http.task.account.GetChatListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.account.ChatListAdapter;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * @author Jean
 * @ClassName: ChatListActivity
 * @Description: 会话列表
 * @mail 154336119@qq.com
 * @date 2015-1-29 上午10:34:22
 */
public class ChatListActivity extends BaseActivity {
    /**
     * 调试代码TAG
     */
    protected static final String TAG = ChatListActivity.class.getSimpleName();
    
    protected static final String CHAT_ID = "chat_id";
    protected static final String CHAT_NAME ="chat_name";

    private CommonActionBar mCommonActionBar;

    private DataLoadingLayout mDataLoadingLayout;
    private XListView mXListView;
    private ChatListAdapter mChatListAdapter;
    private SystemChat mSystemChat;
    private String mMinId;
	private CommonAlertDialog mCommonAlertDialog;
    private boolean mNeedShowLoadingLayout = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        initActionBar();
        initView();

        listItemOnRefresh();
    }

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.txt_title_chat);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


    private void initView() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	httpRequestChatList(XListRefreshType.ON_PULL_REFRESH);
            }
        });

        mXListView = (XListView) findViewById(R.id.chat_listview);
        mXListView.setPullRefreshEnable(false);
        mXListView.setPullLoadEnable(true);
        mXListView.setAutoRefreshEnable(false);
        mXListView.setAutoLoadMoreEnable(true);
        mXListView.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
                mNeedShowLoadingLayout = false;
                listItemOnRefresh();
            }

            @Override
            public void onLoadMore() {
                mNeedShowLoadingLayout = false;
                listItemOnLoadMore();
            }
        });
        mXListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        mChatListAdapter = new ChatListAdapter(this,false);
        mXListView.setAdapter(mChatListAdapter);
        mXListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg2!=1){
					showDelectChatDialog(mChatListAdapter.getItem(arg2-1).getId()+"");
				}
				return false;
			}
		});
        mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
                Intent intent = new Intent(ChatListActivity.this, MessageListActivity.class);
				if(arg2==1){
					if(mSystemChat!=null){
						intent.putExtra(CHAT_ID, mSystemChat.getId());
					}
                    intent.putExtra(CHAT_NAME, "与惠装旺旺");
                    startActivity(intent);
				}else{
					ForeManChat foreManChat = mChatListAdapter.getItem(arg2-1);
                    intent.putExtra(CHAT_ID, foreManChat.getId()+"");
                    intent.putExtra(CHAT_NAME, "与"+foreManChat.getUsername());
                    startActivity(intent);
				}
			}
		});
    }


    /**
     * 下拉刷新列表
     */
    private void listItemOnRefresh() {
        mMinId = null;
        httpRequestChatList(XListRefreshType.ON_PULL_REFRESH);
    }

    /**
     * 列表自动加载更多
     */
    private void listItemOnLoadMore() {
        int listItemCount = mChatListAdapter.getCount();
        if (listItemCount > 0) {
        	ForeManChat foreManChat = mChatListAdapter.getItem(listItemCount - 1);
            mMinId = String.valueOf(foreManChat.getId());
        }
        httpRequestChatList(XListRefreshType.ON_LOAD_MORE);
    }

    /**
     * HTTP请求 - 会话列表
     *
     * @param xListRefreshType
     */
    private void httpRequestChatList(final XListRefreshType xListRefreshType) {

    	GetChatListTask task = new GetChatListTask(ChatListActivity.this, mMinId);
        task.setCallBack(new AbstractHttpResponseHandler<ChatList>() {

			@Override
			public void onSuccess(ChatList chat) {
				// TODO Auto-generated method stub
				  mDataLoadingLayout.showDataLoadSuccess();
				  List<ForeManChat> foremanChatList = new ArrayList<ForeManChat>();
				  foremanChatList.add(0, new ForeManChat());
				  if(chat.getSystem()!=null){
					  boolean hasNewSystem = false;
					  int unreadCount = chat.getSystem().get(0).getUnread_count();
					  if(unreadCount != 0){
						  hasNewSystem = true;
					  }else{
						  hasNewSystem = false;
					  }
					  
					  	mChatListAdapter.setmHasNewSystemMsg(hasNewSystem); //是否有新消息

				  }
				  if(chat.getForeman()!=null){
					  foremanChatList.addAll(chat.getForeman());
					 
				  }
				  
	                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
	                	mChatListAdapter.setList(foremanChatList);
	                	mXListView.setPullRefreshEnable(false); //暂时这样
	                } else {
	                	mChatListAdapter.addList(foremanChatList);
	                }

	                if (null != foremanChatList) {
	                    if (foremanChatList.size() < AppConfig.pageSize) {
	                        mXListView.setPullLoadEnable(false);
	                    } else {
	                        mXListView.setPullLoadEnable(true);
	                    }
	                } else {
	                    mXListView.setPullLoadEnable(false);
	                }
	                if (mChatListAdapter.getCount() == 0) {
	                    mDataLoadingLayout.showDataEmptyView();
	                    return;
	                }
	               int t =  mChatListAdapter.getList().size();
			}

			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mChatListAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoadFailed(error);
                } else {
                    showToastMsg(error);
                }
			}
			@Override
			public void onFinish() {
				super.onFinish();

                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    mXListView.onRefreshComplete();
                } else {
                    mXListView.onLoadMoreComplete();
                }				
			}
			@Override
			public void onStart() {
				super.onStart();
	            if (mNeedShowLoadingLayout) {
                    mDataLoadingLayout.showDataLoading();
                } else if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mChatListAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoading();
                }
			}
		});
        task.send();
    }

    /**
     * HTTP请求 - 删除会话
     *
     */
    private void httpRequestChatList(String chaId){
    	DelectChatTask delectChatTask = new DelectChatTask(ChatListActivity.this, chaId);
    	delectChatTask.setCallBack(new AbstractHttpResponseHandler<String>() {
			
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				listItemOnRefresh();
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				showToastMsg(error);
			}
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showWaitDialog("");
			}
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				hideWaitDialog();
				mCommonAlertDialog.dismiss();
			}
		});
    	delectChatTask.send();
    } 

	/**
	 * 显示删除会话对话框
	 */
	private void showDelectChatDialog(final String chatId) {
		LogUtil.d("showExitAlertDialog");

		if (null == mCommonAlertDialog) {
			mCommonAlertDialog = new CommonAlertDialog(this);
			mCommonAlertDialog.setTitle(R.string.txt_delect_chat);
			mCommonAlertDialog.setMessage(R.string.txt_are_you_sure_to_delect_chat);
			mCommonAlertDialog.setPositiveButton(R.string.ensure, new OnClickListener() {
				@Override
				public void onClick(View v) {
					httpRequestChatList(chatId);
				}
			});
			mCommonAlertDialog.setNegativeButton(R.string.txt_cancel, new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCommonAlertDialog.dismiss();
				}
			});
		}
		mCommonAlertDialog.show();
	}
}
