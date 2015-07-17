package com.huizhuang.zxsq.ui.activity.supervision;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.supervision.ComplaintsData;
import com.huizhuang.zxsq.http.bean.supervision.NodeEdit;
import com.huizhuang.zxsq.http.task.supervision.ComplaintsInfoTask;
import com.huizhuang.zxsq.http.task.supervision.NodeEditInfoTask;
import com.huizhuang.zxsq.ui.activity.base.BaseFragmentActivity;
import com.huizhuang.zxsq.ui.fragment.supervision.NodeEditReportFragment;
import com.huizhuang.zxsq.ui.fragment.zxbd.ZxbdListFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/**
 * 各阶段整改
 * 
 * @author th
 * 
 */
public class NodeEditActivity extends BaseFragmentActivity implements OnClickListener {

    private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;
    
    private TextView mTvCheckInfo;
    private TextView mTvZxbd;

    private Drawable mDrawable;
    private FragmentManager mFragmentManager;
    private NodeEditReportFragment mFragmenNodeEditReport;
    private Fragment mFragmentZxbd;
    private String mOdersId;
    private String mNodeId;
    private String mZxbdNode;//根据mNode得到不同的装修宝典列表
    private int mStatus;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_edit);
        getIntentExtra();
        setZxbdNode();
        initActionBar();
        initViews();
        initFragment();
        httpRequestGetNodeEditInfo();
    }

	private void getIntentExtra() {
        mOdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
        mNodeId = getIntent().getStringExtra(AppConstants.PARAM_NODE_ID);
    }
	/**
	 * 根据mNodeId得到装修宝典对应的mNode
	 */
	private void setZxbdNode() {
		mZxbdNode = Util.getZxbdNodeByNodeId(mNodeId);
	}
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);       
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
//        mCommonActionBar.setRightImgBtn(R.drawable.ic_actionbar_share, new OnClickListener() {
//            
//            @Override
//            public void onClick(View arg0) {
//            	//根据不同的订单状态来确定分享的文案
//                Util.setShareByNodeId(mNodeId);
//                Share share = new Share();
//				Util.showShare(false, NodeEditActivity.this,share);
//            }
//        });
        mCommonActionBar.setRightTxtBtn(R.string.txt_complaints, new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                switch (Integer.valueOf(mNodeId)) {
                    case 1:
                        if(mStatus == -1){
                            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0054);
                            httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_EDIT_ING_1));
                        }else{
                            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0055);
                            httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_EDIT_END_1));
                        }
                        break;
                    case 2:
                        if(mStatus == -1){
                            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0057);
                            httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_EDIT_ING_2));
                        }else{
                            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0058);
                            httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_EDIT_END_2));
                        }
                        break;
                    case 3:
                        if(mStatus == -1){
                            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0061);
                            httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_EDIT_ING_3));
                        }else{
                            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0062);
                            httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_EDIT_END_3));
                        }
                        break;
                    case 4:
                        if(mStatus == -1){
                            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0065);
                            httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_EDIT_ING_4));
                        }else{
                            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0066);
                            httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_EDIT_END_4));
                        }
                        break;
                    case 5:
                        if(mStatus == -1){
                            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0069);
                            httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_EDIT_ING_5));
                        }else{
                            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0070);
                            httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_EDIT_END_5));
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initViews() {
        mDataLoadingLayout = (DataLoadingLayout)findViewById(R.id.data_load_layout);
        
        mTvCheckInfo = (TextView) findViewById(R.id.tv_check_info);
        mTvZxbd = (TextView) findViewById(R.id.tv_zxgl);
        findViewById(R.id.tv_check_info).setOnClickListener(this);
        findViewById(R.id.tv_zxgl).setOnClickListener(this);
        mDrawable = getResources().getDrawable(R.drawable.icon_order_triangle);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
    } 

    private void initFragment() { 
    	
        mFragmentManager = getSupportFragmentManager();
        // 验收信息
        mFragmenNodeEditReport = new NodeEditReportFragment();
        // 装修宝典
        mFragmentZxbd = new ZxbdListFragment(mZxbdNode);//根据mNode得到相应的装修宝典

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.fl_main_container, mFragmenNodeEditReport);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_info://验收信息
                switchFragment(0);
                break;
            case R.id.tv_zxgl:// 装修攻略
                switchFragment(1);
                break;
            default:
                break;
        }
    }

    /**
     * 切换fragmenty
     * @param position 0第一个fragment 1第二个fragment
     */
    public void switchFragment(final int position){
        switch (position) {
            case 0:
                FragmentTransaction ft1 = mFragmentManager.beginTransaction();
                if(!mFragmenNodeEditReport.isAdded()){
                    ft1.hide(mFragmentZxbd).add(R.id.fl_main_container, mFragmenNodeEditReport);
                    ft1.commit();
                }else{
                    ft1.hide(mFragmentZxbd).show(mFragmenNodeEditReport).commit();
                }
                mTvCheckInfo.setCompoundDrawables(null, null, null, mDrawable);
                mTvZxbd.setCompoundDrawables(null, null, null, null);
                break;
            case 1:
                FragmentTransaction ft2 = mFragmentManager.beginTransaction();
                if(!mFragmentZxbd.isAdded()){
                    ft2.hide(mFragmenNodeEditReport).add(R.id.fl_main_container, mFragmentZxbd);
                    ft2.commit();
                }else{
                    ft2.hide(mFragmenNodeEditReport).show(mFragmentZxbd).commit();
                }
                mTvZxbd.setCompoundDrawables(null, null, null, mDrawable);
                mTvCheckInfo.setCompoundDrawables(null, null, null, null);
                break;
            default:
                break;
        }
    }
    
    /**
     * 获取投诉节点信息
     * @param context
     * @param orders_id
     * @param dispute_node_id
     */
    private void httpRequestComplaintsInfo(String orders_id,final String dispute_node_id) {
        ComplaintsInfoTask task =
                new ComplaintsInfoTask(NodeEditActivity.this, orders_id,dispute_node_id);
        task.setCallBack(new AbstractHttpResponseHandler<ComplaintsData>() {

            @Override
            public void onSuccess(ComplaintsData result) {
                if(result.getStauts() == 0){//无投诉
                    Bundle bd = new Bundle();
                    bd.putString(AppConstants.PARAM_NODE_ID, dispute_node_id);
                    bd.putString(AppConstants.PARAM_ORDER_ID, mOdersId);
                    ActivityUtil.next(NodeEditActivity.this, ComplaintsListActivity.class,bd,-1);
                }else{//有投诉
                    Bundle bd = new Bundle();
                    bd.putString(AppConstants.PARAM_COMPLAINTS_TYPE, result.getDatas().getCategory_name());
                    bd.putString(AppConstants.PARAM_COMPLAINTS_QUESTION, result.getDatas().getFirst_category_name());
                    ActivityUtil.next(NodeEditActivity.this, ComplaintsSucessActivity.class,bd,-1);
                }
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
    
    /**
     * http请求-获取订单详细
     * 
     * @param xListRefreshType
     */
    private void httpRequestGetNodeEditInfo() {
        NodeEditInfoTask task = new NodeEditInfoTask(this, mOdersId);
        task.setCallBack(new AbstractHttpResponseHandler<List<NodeEdit>>() {

            @Override
            public void onSuccess(List<NodeEdit> result) {
                mDataLoadingLayout.showDataLoadSuccess();
                if(result != null){
                    initData(result);
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

    // 初始化数据
    private void initData(List<NodeEdit> nodeEdits) {
        String nodeName = Util.getNodeNameById(mNodeId).substring(0, 2);
        if(nodeEdits.size() > 0){
            if(nodeEdits.get(0).getStatus() == -1){
                mStatus = -1;
                mCommonActionBar.setActionBarTitle(nodeName+"验收整改中...");
            }else{
                mStatus = 0;
                mCommonActionBar.setActionBarTitle(nodeName+"验收整改确认");
            }
            mFragmenNodeEditReport.initData(nodeEdits,mOdersId);
        }
    }  
    
}
