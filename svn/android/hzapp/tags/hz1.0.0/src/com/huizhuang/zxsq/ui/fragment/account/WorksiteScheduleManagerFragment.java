package com.huizhuang.zxsq.ui.fragment.account;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.SupervisionPatrol;
import com.huizhuang.zxsq.http.task.account.GetSupervisionPatrolListTask;
import com.huizhuang.zxsq.ui.activity.account.WorksiteSupervisionReportDetailActivity;
import com.huizhuang.zxsq.ui.adapter.account.WorksiteScheduleManagerAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;


/** 
* @ClassName: WorksiteScheduleManagerFragment 
* @Description:  工地管理监理师巡查
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-5 下午6:54:05 
*  
*/
public class WorksiteScheduleManagerFragment extends BaseFragment {

	private DataLoadingLayout mDataLoadingLayout;
	private XListView mXListView;
	private WorksiteScheduleManagerAdapter mAdapter;
	private int mOrderId;
	private String mMinId;
	

    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);    
          
    }  
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d("onCreate Bundle = " + savedInstanceState);
    	View view = inflater.inflate(R.layout.fragment_worksite_schedule_manager_list,
				container, false);
		initView(view);
		return view;
	}

    public void setOrderId(int orderId){
    	this.mOrderId = orderId;
    }
    
	/**
	 * 初始化控件
	 */
	private void initView(View view) {
		LogUtil.d("initView");
		mDataLoadingLayout = (DataLoadingLayout) view.findViewById(R.id.common_dl);
		mXListView = (XListView) view.findViewById(R.id.my_list_view);
		mXListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				mMinId = null;
				httpRequestSupervisionRecordlist(XListRefreshType.ON_PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				int listItemCount = mAdapter.getCount();
				if (listItemCount > 0) {
					SupervisionPatrol supervisionPatrol = mAdapter.getItem(listItemCount - 1);
					mMinId = supervisionPatrol.getId();
				}
				httpRequestSupervisionRecordlist(XListRefreshType.ON_LOAD_MORE);
			}
		});
		
		mXListView.setPullRefreshEnable(true);
		mXListView.setPullLoadEnable(true);
		mXListView.setAutoRefreshEnable(true);
		mXListView.setAutoLoadMoreEnable(true);

		mAdapter = new WorksiteScheduleManagerAdapter(getActivity());
		mXListView.setAdapter(mAdapter);
		mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt(AppConstants.PARAM_ORDER_ID, mOrderId);
				bundle.putString(AppConstants.PARAM_RECORD_ID, mAdapter.getList().get(position-1).getId());
				bundle.putString(AppConstants.PARAM_NODE_ID, mAdapter.getList().get(position-1).getNode());
				ActivityUtil.next(getActivity(), WorksiteSupervisionReportDetailActivity.class,bundle,-1);
			}
			
		});
	}

//	/**
//	 * HTTP请求 - 获取工地管理进度信息
//	 * 
//	 * @param xListRefreshType
//	 */
	private void httpRequestSupervisionRecordlist(final XListRefreshType xListRefreshType) {
		GetSupervisionPatrolListTask task = new GetSupervisionPatrolListTask(getActivity(),mOrderId,mMinId);
        task.setCallBack(new AbstractHttpResponseHandler<List<SupervisionPatrol>>() {
			
			@Override
			public void onSuccess(List<SupervisionPatrol> result) {
				mDataLoadingLayout.showDataLoadSuccess();
				// 加载更多还是刷新
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					mAdapter.setList(result);
				} else {
					mAdapter.addList(result);
				}
				if (null != result) {
					if(result.size() == 0){
						mDataLoadingLayout.showDataEmptyView();
					}
					if (result.size() < AppConfig.pageSize) {
						mXListView.setPullLoadEnable(false);
					} else {
						mXListView.setPullLoadEnable(true);
					}
				} else {
					mXListView.setPullLoadEnable(false);
				}
			}
			
			@Override
			public void onFailure(int code, String error) {
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType
						&& 0 == mAdapter.getCount()) {
					mDataLoadingLayout.showDataLoadFailed(error);
				} else {
					showToastMsg(error);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				// 没有数据时下拉列表刷新才显示加载等待视图
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mAdapter.getCount()) {
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
