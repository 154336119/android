package com.huizhuang.zxsq.ui.activity.supervision;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqActivityManager;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.Share;
import com.huizhuang.zxsq.http.bean.supervision.CheckInfo;
import com.huizhuang.zxsq.http.task.supervision.CheckInfoTask;
import com.huizhuang.zxsq.http.task.supervision.CheckNotThroughTask;
import com.huizhuang.zxsq.http.task.supervision.CheckThroughTask;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseFragmentActivity;
import com.huizhuang.zxsq.ui.fragment.supervision.CheckInfoFragment;
import com.huizhuang.zxsq.ui.fragment.zxbd.ZxbdListFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/**
 * 各阶段验收信息
 * 
 * @author th
 * 
 */
public class CheckInfoActivity extends BaseFragmentActivity implements OnClickListener {

    private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;
    
    private TextView mTvScore;
    private TextView mTvCheckInfo;
    private TextView mTvZxbd;
    private LinearLayout mLinBottom;
    private ImageView mIvShareTip;

    private Drawable mDrawable;
    private FragmentManager mFragmentManager;
    private CheckInfoFragment mFragmentCheckInfo;
    private Fragment mFragmentZxbd;
    private String mOdersId;
    private String mStageId;
    private String mNodeId;
	private String mZxbdNode;//根据mNode得到不同的装修宝典列表
	private CheckInfo mCheckInfo;
	public static boolean mIsHistory = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_info);
        getIntentExtra();
        initActionBar();
        setZxbdNode();
        initViews();
        initFragment();
        httpRequestGetCheckInfo();
        PrefersUtil.getInstance().setValue(mNodeId, true);
    }

    private void getIntentExtra() {
        mOdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
        mStageId = getIntent().getStringExtra(AppConstants.PARAM_STAGE_ID);
        mNodeId = getIntent().getStringExtra(AppConstants.PARAM_NODE_ID);
        if(getIntent().getStringExtra(AppConstants.PARAM_IS_HISTORY)!=null){
        	mIsHistory = true;
        }
    }
    
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        String nodeName = Util.getNodeNameById(mNodeId);
        if("1".equals(mNodeId)){
            nodeName = "开工";
        }
        mCommonActionBar.setActionBarTitle(nodeName+"验收");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        mCommonActionBar.setRightImgBtn(R.drawable.ic_actionbar_share, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mIvShareTip.setVisibility(View.GONE);
            	//根据不同的订单状态来确定分享的文案
                Util.setShareByNodeId(mNodeId);
                Share share = new Share();
				Util.showShare(false, CheckInfoActivity.this,share);
            }
        });
        
    }
    /**
	 * 根据mNodeId得到装修宝典对应的mNode
	 */
	private void setZxbdNode() {
		mZxbdNode = Util.getZxbdNodeByNodeId(mNodeId);
	}
    private void initViews() {
        mDataLoadingLayout = (DataLoadingLayout)findViewById(R.id.data_load_layout);
        mTvScore = (TextView) findViewById(R.id.tv_score);
        mTvCheckInfo = (TextView) findViewById(R.id.tv_check_info);
        mTvZxbd = (TextView) findViewById(R.id.tv_zxgl);
        mIvShareTip = (ImageView) findViewById(R.id.iv_share_tip);
        findViewById(R.id.tv_check_info).setOnClickListener(this);
        findViewById(R.id.tv_zxgl).setOnClickListener(this);
        mDrawable = getResources().getDrawable(R.drawable.icon_order_triangle);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
        mLinBottom = (LinearLayout)findViewById(R.id.lin_bottom);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_no_ok).setOnClickListener(this);
        findViewById(R.id.iv_share_tip).setOnClickListener(this);
        boolean isShowShareTip = PrefersUtil.getInstance().getBooleanValue(mNodeId, false);
        if(!isShowShareTip && "1".equals(mNodeId)){
            mIvShareTip.setVisibility(View.VISIBLE);
        }else{
            mIvShareTip.setVisibility(View.GONE);
        }
    } 

    private void initFragment() { 
    	
        mFragmentManager = getSupportFragmentManager();
        // 验收信息
        mFragmentCheckInfo = new CheckInfoFragment();
        // 装修宝典
        mFragmentZxbd = new ZxbdListFragment(mZxbdNode);//根据mNode得到相应的装修宝典

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.fl_main_container, mFragmentCheckInfo);
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
            case R.id.btn_ok://验收合格
                switch (Integer.valueOf(mNodeId)) {
                    case 1:
                        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0030);
                        break;
                    case 2:
                        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0033);
                        break;
                    case 3:
                        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0036);
                        break;
                    case 4:
                        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0039);
                        break;
                    case 5:
                        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0042);
                        break;
                    default:
                        break;
                }
                httpRequestCheckThrough();
                break;
            case R.id.btn_no_ok://需要整改
                switch (Integer.valueOf(mNodeId)) {
                    case 1:
                        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0031);
                        break;
                    case 2:
                        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0034);
                        break;
                    case 3:
                        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0037);
                        break;
                    case 4:
                        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0040);
                        break;
                    case 5:
                        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0043);
                        break;
                    default:
                        break;
                }
                httpRequestCheckNotThrough();
                break;
            case R.id.iv_share_tip://分享
            	//根据不同的订单状态来确定分享的文案
//                mIvShareTip.setVisibility(View.GONE);
                /*Util.setShareByNodeId(mNodeId);
                Share share = new Share();
				Util.showShare(false, CheckInfoActivity.this,share);*/
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
                mLinBottom.setVisibility(View.VISIBLE);
                FragmentTransaction ft1 = mFragmentManager.beginTransaction();
                if(!mFragmentCheckInfo.isAdded()){
                    ft1.hide(mFragmentZxbd).add(R.id.fl_main_container, mFragmentCheckInfo);
                    ft1.commit();
                }else{
                    ft1.hide(mFragmentZxbd).show(mFragmentCheckInfo).commit();
                }
                mTvCheckInfo.setCompoundDrawables(null, null, null, mDrawable);
                mTvZxbd.setCompoundDrawables(null, null, null, null);
                break;
            case 1:
                mLinBottom.setVisibility(View.GONE);
                FragmentTransaction ft2 = mFragmentManager.beginTransaction();
                if(!mFragmentZxbd.isAdded()){
                    ft2.hide(mFragmentCheckInfo).add(R.id.fl_main_container, mFragmentZxbd);
                    ft2.commit();
                }else{
                    ft2.hide(mFragmentCheckInfo).show(mFragmentZxbd).commit();
                }
                mTvZxbd.setCompoundDrawables(null, null, null, mDrawable);
                mTvCheckInfo.setCompoundDrawables(null, null, null, null);
                break;
            default:
                break;
        }
    }
    
    /**
     * http请求-获取订单详细
     * 
     * @param xListRefreshType
     */
    private void httpRequestGetCheckInfo() {
        CheckInfoTask task = new CheckInfoTask(this, mStageId);
        task.setCallBack(new AbstractHttpResponseHandler<CheckInfo>() {

            @Override
            public void onSuccess(CheckInfo result) {
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
    private void initData(CheckInfo checkInfo) {
        mCheckInfo = checkInfo;
        if(checkInfo.getStatus() == -1){
            mLinBottom.setVisibility(View.GONE);
        }else{
            mLinBottom.setVisibility(View.VISIBLE);
        }
        if(checkInfo.getTotal_score() >= 80){
            mTvScore.setTextColor(Color.GREEN);
        }else if(checkInfo.getTotal_score() < 60){
            mTvScore.setTextColor(Color.RED);
        }else{
            mTvScore.setTextColor(getResources().getColor(R.color.color_ff6c38));
        }
        mTvScore.setText(checkInfo.getTotal_score()+"");
        mFragmentCheckInfo.initData(checkInfo);
    }
    
    /**
     * 验收通过
     */
    private void httpRequestCheckThrough() {
        CheckThroughTask task = new CheckThroughTask(this, mStageId);
        task.setCallBack(new AbstractHttpResponseHandler<String>() {

            @Override
            public void onSuccess(String result) {
                //跳评价页
                Bundle bd = new Bundle();
                bd.putString(AppConstants.PARAM_ORDER_ID, mOdersId);
                bd.putString(AppConstants.PARAM_NODE_ID, mNodeId);
                bd.putInt(AppConstants.PARAM_IS_CODE, mCheckInfo.getCost());
                ActivityUtil.next(CheckInfoActivity.this, EvaluationActivity.class,bd,true);
                ZxsqActivityManager.getInstance().finishActivity(NodeEditActivity.class);
                
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
     * 验收不通过
     */
    private void httpRequestCheckNotThrough() {
        CheckNotThroughTask task = new CheckNotThroughTask(this, mStageId);
        task.setCallBack(new AbstractHttpResponseHandler<String>() {

            @Override
            public void onSuccess(String result) {
                //跳整改确认页
                Bundle bd = new Bundle();
                bd.putString(AppConstants.PARAM_ORDER_ID, mOdersId);
                bd.putString(AppConstants.PARAM_NODE_ID, mNodeId);
                ActivityUtil.next(CheckInfoActivity.this, NodeEditActivity.class,bd,true);
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
