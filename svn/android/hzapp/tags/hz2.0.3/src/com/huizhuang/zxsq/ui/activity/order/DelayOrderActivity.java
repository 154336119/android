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
import com.huizhuang.zxsq.http.bean.order.DeylayChangeOrder;
import com.huizhuang.zxsq.http.task.order.CostChangeTask;
import com.huizhuang.zxsq.http.task.order.GetDelayOrderTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.order.CostChangeAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/**
 * 
 *延期变更单
 * @author th
 * 
 */
public class DelayOrderActivity extends BaseActivity{

    private CommonActionBar mCommonActionBar;
    private TextView mTvDayNum;
    private TextView mTvDelayDate;
    private TextView mTvDelayCase;
    
    private DataLoadingLayout mDataLoadingLayout;
    private String mStageId;
    private List<HashMap<String,String>> mList=new ArrayList<HashMap<String,String>>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay_order);
        getIntentExtra();
        initActionBar();
        initViews();
        httpRequestGetDelayChangeOrder();
    }

    private void getIntentExtra() {
    	mStageId = getIntent().getStringExtra(AppConstants.PARAM_STAGE_ID);
    }
    
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("延期变更单");
        
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
    	mTvDayNum = (TextView) findViewById(R.id.tv_day_num);
    	mTvDelayDate = (TextView) findViewById(R.id.tv_delay_date);
    	mTvDelayCase = (TextView) findViewById(R.id.tv_delay_case);
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                httpRequestGetDelayChangeOrder();
            }
        });
    } 

    /**
     * http请求-延期变更单
     * 
     * @param xListRefreshType
     */
    private void httpRequestGetDelayChangeOrder() {
    	GetDelayOrderTask task = new GetDelayOrderTask(this, mStageId);
    	task.setCallBack(new AbstractHttpResponseHandler<DeylayChangeOrder>() {
			
			@Override
			public void onSuccess(DeylayChangeOrder daylayChangeOrder) {
				mDataLoadingLayout.showDataLoadSuccess();
				setViewData(daylayChangeOrder);
			}
			
			@Override
			public void onFailure(int code, String error) {
				mDataLoadingLayout.showDataLoadFailed(error);

			}
			@Override
			public void onStart() {
				super.onStart();
				mDataLoadingLayout.showDataLoading();
			}
			@Override
			public void onFinish() {
				super.onFinish();
			}
		});
    	task.send();
    }
    
    
    
    private void setViewData(DeylayChangeOrder deylayChangeOrder) {
    	mTvDayNum.setText(deylayChangeOrder.getDays()+"天");
    	mTvDelayDate.setText(DateUtil.timestampToSdate(deylayChangeOrder.getCompleted_date(), "yyyy年MM月dd日"));
    	mTvDelayCase.setText(deylayChangeOrder.getCause());
    }
   
    
}
