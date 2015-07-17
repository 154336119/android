package com.huizhuang.zxsq.ui.activity.account;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.http.task.account.GetNodeListTask;
import com.huizhuang.zxsq.module.SupervisionNode;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.account.WorksiteSupervisionNodeListAdapter;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;



/** 
* @ClassName: WorksiteSupervisionNodeListActivity 
* @Description: 监理节点列表
* @author th 
* @mail 342592622@qq.com   
* @date 2015-1-22 上午9:56:31 
*  
*/
public class WorksiteSupervisionNodeListActivity extends BaseActivity {
	public static final String NODE_KEY = "node";
	
	public static final String TITLE_KEY = "title";
    public static final String ID_KEY = "id";
    private DataLoadingLayout mDataLoadingLayout;
    
	private XListView mXListView;
	private WorksiteSupervisionNodeListAdapter mAdapter;
	private int mOrderId;
	
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worksite_supervision_node_list);
        initExtras();
        initActionBar();
        initVews();
        httpRequestQueryOrderData();
    }  
    
    private void initExtras() {
    	mOrderId = getIntent().getIntExtra(ID_KEY, 0);
    }
    
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(R.string.txt_supervision_node);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

    private void initVews() {
    	mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.common_dl);
    	
    	mXListView = (XListView) findViewById(R.id.my_list_view);
    	mXListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        mXListView.setPullRefreshEnable(true);
        mXListView.setPullLoadEnable(false);
        mXListView.setAutoRefreshEnable(true);
        mXListView.setAutoLoadMoreEnable(false);
        mAdapter = new WorksiteSupervisionNodeListAdapter(WorksiteSupervisionNodeListActivity.this);
		mXListView.setAdapter(mAdapter);	
		mAdapter.setList(ZxsqApplication.getInstance().getConstant().getJlNodes());
		mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position > 0){
					KeyValue keyValue = mAdapter.getItem(position - 1);
					backResult(keyValue);
				}
			}
			
		});
		mXListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				httpRequestQueryOrderData();
			}

			@Override
			public void onLoadMore() {

			}
		});
    }

    private void httpRequestQueryOrderData(){
    	GetNodeListTask task = new GetNodeListTask(WorksiteSupervisionNodeListActivity.this,mOrderId);
        task.setCallBack(new AbstractHttpResponseHandler<List<SupervisionNode>>() {
			
			@Override
			public void onSuccess(List<SupervisionNode> result) {
				mDataLoadingLayout.showDataLoadSuccess();
				// 加载更多还是刷新
				mAdapter.setListSupervisionNodes(result);
			}
			
			@Override
			public void onFailure(int code, String error) {				
				mDataLoadingLayout.showDataLoadFailed(error);
			}

			@Override
			public void onStart() {
				super.onStart();
				if(mAdapter.getCount() == 0){
					mDataLoadingLayout.showDataLoading();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				mXListView.onRefreshComplete();
			}
			
			
		});
        task.send();
    }
    
    
    private void backResult(KeyValue keyValue) {      
    	List<SupervisionNode> supervisionNodes = mAdapter.getListSupervisionNodes();
    	boolean isBack = false;
    	if(supervisionNodes != null && supervisionNodes.size() > 0){
	    	for (SupervisionNode supervisionNode : supervisionNodes) {
				if(supervisionNode.getNode_id().equals(keyValue.getId()) && supervisionNode.getStatu()==1){
					isBack = true;
					break;
				}
			}  
    	}else{
    		isBack = true;   		
    	}
    	if(isBack){
    		Bundle bundle = new Bundle();
	    	bundle.putString(TITLE_KEY, keyValue.getName());
	        bundle.putString(ID_KEY, keyValue.getId());
	        Intent intent = getIntent();
	        intent.putExtras(bundle);
	        setResult(RESULT_OK,intent);
	        finish();
    	}
    }
}
