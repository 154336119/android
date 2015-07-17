package com.huizhuang.zxsq.ui.activity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.order.CostChange;
import com.huizhuang.zxsq.http.task.order.CostChangeTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.order.CostChangeAdapter;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/**
 * 
 *工程及费用变更
 * @author th
 * 
 */
public class CostChangeActivity extends BaseActivity implements OnClickListener {

    private CommonActionBar mCommonActionBar;
    private TextView mCostAll;
    private TextView mCostAdd;
    private TextView mCostCuts;
    private CostChangeAdapter mAdapter;
    private ExpandableListView mExpListView;
    private DataLoadingLayout mDataLoadingLayout;
    private String mStageId;

    private List<HashMap<String,String>> mList=new ArrayList<HashMap<String,String>>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_change);
        getIntentExtra();
        initActionBar();
        initViews();
        httpRequestGetCostChange();
    }

    private void getIntentExtra() {
    	mStageId = getIntent().getStringExtra(AppConstants.PARAM_STAGE_ID);
    }
    
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("工程及费用变更单");
        
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
    	mCostAll = (TextView) findViewById(R.id.cost_all);
    	mCostAdd = (TextView) findViewById(R.id.cost_add);
    	mCostCuts = (TextView) findViewById(R.id.cost_cuts);
    	mExpListView = (ExpandableListView) findViewById(R.id.exp_list);
    	mAdapter = new CostChangeAdapter();
    	mList = new ArrayList<HashMap<String,String>>();
        mExpListView.setAdapter(mAdapter);
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);

    } 

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:// 支付 - 下个页面

                break;
            default:
                break;
        }
    }

    /**
     * http请求-获取工程及费用变更单
     * 
     * @param xListRefreshType
     */
    private void httpRequestGetCostChange() {
    	CostChangeTask task = new CostChangeTask(this,mStageId);
    	task.setCallBack(new AbstractHttpResponseHandler<CostChange>() {
			
			@Override
			public void onSuccess(CostChange costChange) {
				mDataLoadingLayout.showDataLoadSuccess();
				costChange.initKeyValue();
				setViewData(costChange);
			}
			
			@Override
			public void onFailure(int code, String error) {
                mDataLoadingLayout.showDataLoadFailed(error);
			}
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
                mDataLoadingLayout.showDataLoading();
			}
		});
    	task.send();
    }
    
    public static String[] proName = {
		"增减类型","增减项目","工程量","单价","增项","原因"
};
    
    
    private void setViewData(CostChange costChange) {
    	mCostAdd.setText(costChange.getAdd_cost_total()+"");
    	mCostCuts.setText(costChange.getDed_cost_total()+"");
    	mCostCuts.setText(costChange.getAdd_cost_total()+costChange.getAdd_cost_total()+"");
        mAdapter.setDataList(getApplicationContext(), costChange.getKeyValuelist());
        mExpListView.setAdapter(mAdapter);
    }
    // 初始化数据
//    private void initData(OrderDetail orderDetail) {
//        if(orderDetail.getParent() != null){
//            if (!TextUtils.isEmpty(orderDetail.getParent().getMeasuring_time())) {
//                mTvTime.setText(DateUtil.format(orderDetail.getParent().getMeasuring_time(),
//                        "yyyy-MM-dd", "yyyy年 MM月dd日") + " 量房");
//            }else{
//                mTvTime.setVisibility(View.GONE);
//            }
//            if (!TextUtils.isEmpty(orderDetail.getParent().getUser_remark())) {
//                mTvMessage.setText(orderDetail.getParent().getUser_remark());
//                mTvMessage.setVisibility(View.VISIBLE);
//            } else {
//                mTvMessage.setVisibility(View.GONE);
//            }
//            
//            if(!TextUtils.isEmpty(orderDetail.getParent().getHousing_address())){
//                mTvAddress.setText(orderDetail.getParent().getHousing_address());
//            }else{
//                mTvAddress.setVisibility(View.GONE);
//            }
//        }else{
//            mTvMessage.setVisibility(View.GONE);
//            mTvTime.setVisibility(View.GONE);
//            mTvAddress.setVisibility(View.GONE);
//        }
//        mFragmentForeman.initData(orderDetail.getChild());
//    }
//    
    
}
