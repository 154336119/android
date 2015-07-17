package com.huizhuang.zxsq.ui.activity.supervision;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.supervision.ComplaintsType;
import com.huizhuang.zxsq.http.task.supervision.ComplaintsTypeTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.supervision.ComplaintsListAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.MyListView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * 投诉问题列表
 * @author th
 *
 */
public class ComplaintsListActivity extends BaseActivity implements OnClickListener{

    private MyListView mXListView;
    private DataLoadingLayout mDataLoadingLayout;
    private ComplaintsListAdapter mAdapter;
    private String mNodeId;
    private String mOdersId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_list);
        getIntentExtra();
        initActionBar();
        initViews();
        httpRequestComplaintsType(null);
    }

    private void getIntentExtra() {
        if(getIntent().getBooleanExtra(AppConstants.PARAM_IS_COMPLAINTS_SUCESS, false)){
            finish();
        }
        mNodeId = getIntent().getStringExtra(AppConstants.PARAM_NODE_ID);
        mOdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
    }
    
    private void initActionBar() {
        CommonActionBar mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle(R.string.txt_check_complaints_requestion);
        mCommonActionBar.setLeftImgBtn(R.drawable.back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0046);
                finish();
            }
        });
    }
    
    private void initViews(){
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.common_dl);
        mXListView = (MyListView)findViewById(R.id.complaints_list_view);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        mAdapter = new ComplaintsListAdapter(this);
        mXListView.setAdapter(mAdapter);
    }
    
    /**
     * 获取投诉分类
     * @param parent_id
     */
    private void httpRequestComplaintsType(String parent_id) {
        ComplaintsTypeTask task =
                new ComplaintsTypeTask(ComplaintsListActivity.this, parent_id);
        task.setCallBack(new AbstractHttpResponseHandler<List<ComplaintsType>>() {

            @Override
            public void onSuccess(List<ComplaintsType> result) {
                mDataLoadingLayout.showDataLoadSuccess();
                if(result != null && result.size() > 0){
                    mAdapter.setList(result);
                }else{
                    mDataLoadingLayout.showDataEmptyView();
                }
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
    
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0047);
                if(TextUtils.isEmpty(mAdapter.getSelectIds())){
                    showToastMsg(getResources().getString(R.string.txt_please_check_complaints_type));
                }else{
                    Bundle bd = new Bundle();
                    bd.putString(AppConstants.PARAM_NODE_ID, mNodeId);
                    bd.putString(AppConstants.PARAM_ORDER_ID, mOdersId);
                    bd.putString(AppConstants.PARAM_COMPLAINTS_TYPE_ID, mAdapter.getSelectIds());
                    bd.putString(AppConstants.PARAM_COMPLAINTS_TYPE, mAdapter.getSelectNames());
                    ActivityUtil.next(ComplaintsListActivity.this, ComplaintsReasonListActivity.class, bd, -1);
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getBooleanExtra(AppConstants.PARAM_IS_COMPLAINTS_SUCESS, false)){
            finish();
        }
    }
}
