package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.AccountBalance;
import com.huizhuang.zxsq.http.bean.account.AccountFlow;
import com.huizhuang.zxsq.http.bean.account.ChatList;
import com.huizhuang.zxsq.http.bean.account.ForeManChat;
import com.huizhuang.zxsq.http.bean.account.ForemanRecord;
import com.huizhuang.zxsq.http.bean.account.SystemChat;
import com.huizhuang.zxsq.http.task.account.DelectChatTask;
import com.huizhuang.zxsq.http.task.account.GetAccountBalanceTask;
import com.huizhuang.zxsq.http.task.account.GetChatListTask;
import com.huizhuang.zxsq.http.task.account.GetForemanRecordListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.account.AccountFlowListAdapter;
import com.huizhuang.zxsq.ui.adapter.account.ChatListAdapter;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * @author Jean
 * @ClassName: AccountBalanceActivity
 * @Description: 账户余额
 * @mail 154336119@qq.com
 * @date 2015-1-29 上午10:34:22
 */
public class AccountBalanceActivity extends BaseActivity {
    /**
     * 调试代码TAG
     */
    protected static final String TAG = AccountBalanceActivity.class.getSimpleName();

    private CommonActionBar mCommonActionBar;

    private TextView mTvAccountBlanace;
    private TextView mTvOverPayed;
    private DataLoadingLayout mDataLoadingLayout;
    private XListView mXListView;
    private AccountFlowListAdapter mAccountFlowListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_balance);
        initActionBar();
        initView();

        httpRequestAccountBalancelist(XListRefreshType.ON_PULL_REFRESH);
    }

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle("账户余额");
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
            	httpRequestAccountBalancelist(XListRefreshType.ON_PULL_REFRESH);
            }
        });
        mTvAccountBlanace = (TextView)findViewById(R.id.tv_account_blanace);
        mTvOverPayed = (TextView)findViewById(R.id.tv_over_payed);
        mXListView = (XListView) findViewById(R.id.flow_listview);
        mXListView.setPullRefreshEnable(false);
        mXListView.setPullLoadEnable(false);
        mXListView.setAutoRefreshEnable(false);
        mXListView.setAutoLoadMoreEnable(false);
        mAccountFlowListAdapter = new AccountFlowListAdapter(this);
        mXListView.setAdapter(mAccountFlowListAdapter);
        mXListView.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
            	httpRequestAccountBalancelist(XListRefreshType.ON_PULL_REFRESH);
            }

            @Override
            public void onLoadMore() {
            	httpRequestAccountBalancelist(XListRefreshType.ON_LOAD_MORE);
            }
        });
     
    }




	/**
	 * HTTP请求 - 账户余额
	 * 
	 * @param xListRefreshType
	 */
	private void httpRequestAccountBalancelist(final XListRefreshType xListRefreshType) {
		GetAccountBalanceTask task = new GetAccountBalanceTask(this);
        task.setCallBack(new AbstractHttpResponseHandler<AccountBalance>() {
			
			@Override
			public void onSuccess(AccountBalance result) {
				mDataLoadingLayout.showDataLoadSuccess();
				
				if (null != result) {
					List<AccountFlow> accountFlowList = result.getList();
					mTvAccountBlanace.setText(Util.formatMoneyNoUnitTwo(result.getTotal_amount()+""));
					mTvOverPayed.setText(Util.formatMoneyNoUnitTwo(result.getPay_amount()+""));
					// 加载更多还是刷新
					if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
						mAccountFlowListAdapter.setList(accountFlowList);
						if(accountFlowList.size() == 0){
							mDataLoadingLayout.showDataEmptyView();
						}
					} else {
						mAccountFlowListAdapter.addList(accountFlowList);
					}
					if (accountFlowList.size() < AppConfig.pageSize) {
						mXListView.setPullLoadEnable(false);
					} else {
						mXListView.setPullLoadEnable(true);
					}
				}
			}
			
			@Override
			public void onFailure(int code, String error) {
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType
						&& 0 == mAccountFlowListAdapter.getCount()) {
					mDataLoadingLayout.showDataLoadFailed(error);
				} else {
					//mPageIndex--;
					showToastMsg(error);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				// 没有数据时下拉列表刷新才显示加载等待视图
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mAccountFlowListAdapter.getCount()) {
					mDataLoadingLayout.showDataLoading();
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
			
		});
        task.send();
	}
    
}
