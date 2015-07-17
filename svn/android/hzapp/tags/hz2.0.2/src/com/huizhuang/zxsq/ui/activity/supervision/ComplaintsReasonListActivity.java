package com.huizhuang.zxsq.ui.activity.supervision;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.supervision.ComplaintsType;
import com.huizhuang.zxsq.http.task.supervision.ComplaintsAddTask;
import com.huizhuang.zxsq.http.task.supervision.ComplaintsTypeTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.supervision.ComplaintsListAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.MyListView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * 投诉原因列表
 * @author th
 *
 */
public class ComplaintsReasonListActivity extends BaseActivity implements OnClickListener{
   
    private DataLoadingLayout mDataLoadingLayout;
    private MyListView mXListView;
    private EditText mEtOtherQuestion;
    
    private ComplaintsListAdapter mAdapter;
    private String mParentId;
    private String mNodeId;
    private String mOdersId;
    private String mTypeName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reason_complaints_list);
        getIntentExtra();
        initActionBar();
        initViews();
        httpRequestComplaintsInfo(mParentId);
    }

    private void getIntentExtra() {
        mParentId = getIntent().getStringExtra(AppConstants.PARAM_COMPLAINTS_TYPE_ID);
        mNodeId = getIntent().getStringExtra(AppConstants.PARAM_NODE_ID);
        mOdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
        mTypeName = getIntent().getStringExtra(AppConstants.PARAM_COMPLAINTS_TYPE);
    }
    
    private void initActionBar() {
        CommonActionBar mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle(R.string.txt_check_complaints_requestion);
        mCommonActionBar.setLeftImgBtn(R.drawable.back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0048);
                finish();
            }
        });
    }
    
    private void initViews(){
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.common_dl);
        mXListView = (MyListView)findViewById(R.id.complaints_list_view);
        mEtOtherQuestion = (EditText)findViewById(R.id.et_other);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        mAdapter = new ComplaintsListAdapter(this);
        mXListView.setAdapter(mAdapter);
    }
    
    /**
     * 获取投诉分类
     * @param parent_id
     */
    private void httpRequestComplaintsInfo(String parent_id) {
        ComplaintsTypeTask task =
                new ComplaintsTypeTask(ComplaintsReasonListActivity.this, parent_id);
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
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0049);
                if(TextUtils.isEmpty(mAdapter.getSelectIds())){
                    showToastMsg(getResources().getString(R.string.txt_please_check_or_input_complaints));
                }else{
                    httpRequestUbmit();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 提交投诉
     * @param order_id
     * @param dispute_node_id
     * @param title
     * @param category
     */
    private void httpRequestUbmit() {
        ComplaintsAddTask task =
                new ComplaintsAddTask(ComplaintsReasonListActivity.this, mOdersId,mNodeId,mEtOtherQuestion.getText().toString(),mAdapter.getSelectIds());
        task.setCallBack(new AbstractHttpResponseHandler<String>() {

            @Override
            public void onSuccess(String result) {
                Bundle bd = new Bundle();
                bd.putString(AppConstants.PARAM_COMPLAINTS_TYPE, mTypeName);
                bd.putString(AppConstants.PARAM_COMPLAINTS_QUESTION, mAdapter.getSelectNames());
                ActivityUtil.next(ComplaintsReasonListActivity.this, ComplaintsSucessActivity.class, bd,-1);
            }

            @Override
            public void onFailure(int code, String error) {
                showToastMsg(error);
            }

            @Override
            public void onStart() {
                super.onStart();
                showWaitDialog(getResources().getString(R.string.txt_on_waiting));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        });
        task.send();
    }
    
}
