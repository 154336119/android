package com.huizhuang.zxsq.ui.fragment.zxbd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.zxdb.Zxbd;
import com.huizhuang.zxsq.http.bean.zxdb.ZxbdListInfoByStage;
import com.huizhuang.zxsq.http.task.zxbd.ZxbdListByStageTask;
import com.huizhuang.zxsq.ui.activity.zxbd.ZxbdActivity;
import com.huizhuang.zxsq.ui.adapter.ZxbdTitleAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

public class ZxbdListFragment extends BaseFragment {

    private XListView mXListView;
    private ZxbdTitleAdapter mZxbdTitleAdapter;
    private DataLoadingLayout mDataLoadLayout;
    public final static int WHAT_ZXBD = 1;
    public final static String EXTRA_ZXBD = "zxbd";
    private int mPage = AppConfig.DEFAULT_START_PAGE;
	private String mNode;

    public ZxbdListFragment(String node) {//node:根据订单的状态来显示对应阶段的装修宝典
    	mNode = node;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zxbd_list, container, false);
        initView(view);
       /* mPage = AppConfig.DEFAULT_START_PAGE;
		initData(XListRefreshType.ON_PULL_REFRESH);*/
        return view;
    }
	@Override
    public void onResume() { 
    	super.onResume();
    }
    @Override
    public void onPause() {
    	super.onPause();
    }
    private void initView(View view) {
		mDataLoadLayout = (DataLoadingLayout) view
				.findViewById(R.id.data_load_layout);
		mDataLoadLayout.setOnReloadClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPage = AppConfig.DEFAULT_START_PAGE;
				initData(XListRefreshType.ON_PULL_REFRESH);
			}
		});
		mXListView = (XListView) view
				.findViewById(R.id.xlv_fragment_zxbd_list_title);
		mXListView.setPullRefreshEnable(true);
		mXListView.setPullLoadEnable(true);
		mXListView.setAutoLoadMoreEnable(true);
		mXListView.setAutoRefreshEnable(true);
		mZxbdTitleAdapter = new ZxbdTitleAdapter(getActivity());
		mXListView.setAdapter(mZxbdTitleAdapter);
		mXListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				mPage = AppConfig.DEFAULT_START_PAGE;
				initData(XListRefreshType.ON_PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				mPage++;
				initData(XListRefreshType.ON_LOAD_MORE);
			}
		});
		mXListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> v, View arg1, int position, long arg3) {
            	//Zxbd zxbd = mZxbdTitleAdapter.getList().get((int)arg3);
            	Zxbd zxbd = (Zxbd)v.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_ZXBD, zxbd);
                ActivityUtil.next(getActivity(), ZxbdActivity.class, bundle, false);
            }
        });
	}

    private void initData(final XListRefreshType onPullRefresh) {
        ZxbdListByStageTask zxbdListByStageTask =
                new ZxbdListByStageTask(getActivity(), mNode, String.valueOf(mPage));
        // ZxbdListByStageTask zxbdListByStageTask = new
        // ZxbdListByStageTask(getActivity(),"p6");
        zxbdListByStageTask.setCallBack(new AbstractHttpResponseHandler<ZxbdListInfoByStage>() {
            @Override
            public void onStart() {
                if (onPullRefresh == XListRefreshType.ON_PULL_REFRESH
                        && mZxbdTitleAdapter.getCount() == 0) {
                    mDataLoadLayout.showDataLoading(); 
                }
            }
            @Override
            public void onSuccess(ZxbdListInfoByStage result) {
                mDataLoadLayout.showDataLoadSuccess();
                if (result != null && result.getList() != null && result.getList().size() > 0) {
                    if (onPullRefresh == XListRefreshType.ON_PULL_REFRESH) {
                        mZxbdTitleAdapter.setList(result.getList());
                    } else {
                        mZxbdTitleAdapter.addList(result.getList());
                    }
                    if (result.getList().size() < AppConfig.pageSize) {
                        mXListView.setPullLoadEnable(false);
                    } else if((++mPage)>Integer.valueOf(result.getTotalpage())){
                    	mXListView.setPullLoadEnable(false);
                    }else {
                        mXListView.setPullLoadEnable(true);
                    }
                } else if (onPullRefresh == XListRefreshType.ON_PULL_REFRESH 
                		&& mZxbdTitleAdapter.getCount() == 0) {
                    mDataLoadLayout.showDataLoadEmpty("抱歉，目前没有相关宝典");
                }
            }

            @Override
            public void onFailure(int code, String error) {
                if (onPullRefresh == XListRefreshType.ON_PULL_REFRESH
                        && mZxbdTitleAdapter.getCount() == 0) {
                    mDataLoadLayout.showDataLoadFailed(error);
                } else {
                    mPage--;
                    showToastMsg(error);
                }
            }

            @Override
            public void onFinish() {
                if (onPullRefresh == XListRefreshType.ON_PULL_REFRESH) {
                    mXListView.onRefreshComplete();
                } else if (onPullRefresh == XListRefreshType.ON_LOAD_MORE) {
                    mXListView.onLoadMoreComplete();
                }
            }
        });

        zxbdListByStageTask.send();
    }

}
