package com.huizhuang.zxsq.ui.fragment.account;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.ForemanRecord;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.task.account.GetForemanRecordListTask;
import com.huizhuang.zxsq.ui.activity.account.ScreenImgActivity;
import com.huizhuang.zxsq.ui.adapter.account.ForemanRecordListAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;



/** 
* @ClassName: ForemanRecordFragment 
* @Description: 工地管理工长记录 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-5 下午7:14:12 
*  
*/
public class ForemanRecordFragment extends BaseFragment {

	private DataLoadingLayout mDataLoadingLayout;
	
	private XListView mXListView;
	private ForemanRecordListAdapter mAdapter;
	private int mOrderId;
	private int mPageIndex = AppConfig.DEFAULT_START_PAGE;

    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);    
          
    }  
    
    public void setOrderId(int orderId){
    	this.mOrderId = orderId;
    }
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d("onCreate Bundle = " + savedInstanceState);
    	View view = inflater.inflate(R.layout.fragment_foreman_record_list,
				container, false);
		initView(view);
		return view;
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
				httpRequestSupervisionRecordlist(XListRefreshType.ON_PULL_REFRESH);
			}

			@Override
			public void onLoadMore() {
				httpRequestSupervisionRecordlist(XListRefreshType.ON_LOAD_MORE);
			}
		});
		
		mXListView.setPullRefreshEnable(true);
		mXListView.setPullLoadEnable(true);
		mXListView.setAutoRefreshEnable(true);
		mXListView.setAutoLoadMoreEnable(true);

		mAdapter = new ForemanRecordListAdapter(getActivity(),mClickHandler);
		mXListView.setAdapter(mAdapter);
	}

	@SuppressLint("HandlerLeak")
	private Handler mClickHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			int position = msg.arg1;
			ForemanRecord foremanRecord = mAdapter.getList().get(position);
			Intent intent = new Intent(getActivity(), ScreenImgActivity.class);
			if(foremanRecord.getImage() != null){
				intent.putExtra(ScreenImgActivity.IMAGE_LIST_KEY, foremanRecord.getImage().toArray());
			}
			intent.putExtra(ScreenImgActivity.IMAGE_INDEX_KEY, what);
			startActivity(intent);
		}

	};
	
	/**
	 * HTTP请求 - 获取工长记录
	 * 
	 * @param xListRefreshType
	 */
	private void httpRequestSupervisionRecordlist(final XListRefreshType xListRefreshType) {
		GetForemanRecordListTask task = new GetForemanRecordListTask(getActivity(),mOrderId,mPageIndex,AppConfig.pageSize);
        task.setCallBack(new AbstractHttpResponseHandler<List<ForemanRecord>>() {
			
			@Override
			public void onSuccess(List<ForemanRecord> result) {
				mDataLoadingLayout.showDataLoadSuccess();
				
				if (null != result) {
					// 加载更多还是刷新
					if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
						mAdapter.setList(result);
						if(result.size() == 0){
							mDataLoadingLayout.showDataEmptyView();
						}
					} else {
						mAdapter.addList(result);
					}
					if (result.size() < AppConfig.pageSize) {
						mXListView.setPullLoadEnable(false);
					} else {
						mXListView.setPullLoadEnable(true);
					}
				}
			}
			
			@Override
			public void onFailure(int code, String error) {
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType
						&& 0 == mAdapter.getCount()) {
					mDataLoadingLayout.showDataLoadFailed(error);
				} else {
					mPageIndex--;
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
