package com.huizhuang.zxsq.ui.activity.order;

import java.util.List;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.common.Image;
import com.huizhuang.zxsq.http.bean.order.OrderSignForeman;
import com.huizhuang.zxsq.http.bean.supervision.ComplaintsData;
import com.huizhuang.zxsq.http.bean.zxdb.Zxbd;
import com.huizhuang.zxsq.http.bean.zxdb.ZxbdListInfoByStage;
import com.huizhuang.zxsq.http.task.order.GetOrderForemanTask;
import com.huizhuang.zxsq.http.task.supervision.ComplaintsInfoTask;
import com.huizhuang.zxsq.http.task.zxbd.ZxbdListByStageTask;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.supervision.ComplaintsListActivity;
import com.huizhuang.zxsq.ui.activity.supervision.ComplaintsSucessActivity;
import com.huizhuang.zxsq.ui.activity.zxbd.ZxbdActivity;
import com.huizhuang.zxsq.ui.fragment.zxbd.ZxbdListFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class OrderServeStationSignActivity extends BaseActivity implements
		OnClickListener {
	private CommonActionBar mCommonActionBar;
	private TextView mTvMaterial;
	private TextView mTvForemanInfo;
	private CircleImageView mIvHead;
	private TextView mTvName;
	private TextView mTvCity;
	private TextView mTvOrderCount;
	private RatingBar mRbScore;
	private TextView mTvScore;
	private ImageButton mIbSendMessage;
	private ImageButton mIbCall;
	private OrderSignForeman mOrderSignForeman;
	private TextView mTvStatus;
	private LinearLayout mLlStationInfo;
	private String mOdersId;
	private DataLoadingLayout mDataLoadingLayout;
	private String mNode = "p7";//对应装修宝典的阶段 这里是p7:预约阶段

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_success);
		initActionBar();
		initView();
		initIntentExtra();
	}

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.sign_success_common_action_bar);
		mCommonActionBar.setActionBarTitle("工长确认");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.nextActivityWithClearTop(OrderServeStationSignActivity.this, MyOrderActivity.class);
            }
        });
        mCommonActionBar.setRightTxtBtn(R.string.txt_complaints, new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0052);
                httpRequestComplaintsInfo(mOdersId, Util.getIdByComplaintsName(AppConstants.COMPLAINTS_APPOINTMENT_SUCCESS));
            }
        });
	}

	private void initView() {
		mTvForemanInfo = (TextView) findViewById(R.id.tv_sign_success_foreman_info);
		mLlStationInfo = (LinearLayout) findViewById(R.id.ll_sign_success_station_info);
		mTvMaterial = (TextView) findViewById(R.id.tv_sign_success_material);
		mIvHead = (CircleImageView) findViewById(R.id.iv_head);
		mTvName = (TextView) findViewById(R.id.tv_name);
		mTvCity = (TextView) findViewById(R.id.tv_city);
		mTvOrderCount = (TextView) findViewById(R.id.tv_order_count);
		mRbScore = (RatingBar) findViewById(R.id.rb_score);
		mTvScore = (TextView) findViewById(R.id.tv_score);
		mIbSendMessage = (ImageButton) findViewById(R.id.ib_send_message);
		mIbCall = (ImageButton) findViewById(R.id.ib_to_call);
		mTvStatus = (TextView) findViewById(R.id.tv_status);
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_load_layout);
		mRbScore.setIsIndicator(true);
		mTvMaterial.setOnClickListener(this);
		mIbSendMessage.setOnClickListener(this);
		mIbCall.setOnClickListener(this);
	}

    /**
     * 获取投诉节点信息
     * @param context
     * @param orders_id
     * @param dispute_node_id
     */
    private void httpRequestComplaintsInfo(String orders_id,final String dispute_node_id) {
        ComplaintsInfoTask task =
                new ComplaintsInfoTask(OrderServeStationSignActivity.this, orders_id,dispute_node_id);
        task.setCallBack(new AbstractHttpResponseHandler<ComplaintsData>() {

            @Override
            public void onSuccess(ComplaintsData result) {
                if(result.getStauts() == 0){//无投诉
                    Bundle bd = new Bundle();
                    bd.putString(AppConstants.PARAM_NODE_ID, dispute_node_id);
                    bd.putString(AppConstants.PARAM_ORDER_ID, mOdersId);
                    ActivityUtil.next(OrderServeStationSignActivity.this, ComplaintsListActivity.class,bd,-1);
                }else{//有投诉
                    Bundle bd = new Bundle();
                    bd.putString(AppConstants.PARAM_COMPLAINTS_TYPE, result.getDatas().getCategory_name());
                    bd.putString(AppConstants.PARAM_COMPLAINTS_QUESTION, result.getDatas().getFirst_category_name());
                    ActivityUtil.next(OrderServeStationSignActivity.this, ComplaintsSucessActivity.class,bd,-1);
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
    
	private void initIntentExtra() {
		mOrderSignForeman = (OrderSignForeman) getIntent().getExtras()
				.getSerializable(
						OrderAppointmentSignedActivity.EXTRA_SIGN_FOREMAN);
		// 如果传递的mOrderSignForeman为null，则说明不是点击预约按钮进入的该界面，而是通过订单列表进入的该界面。此时需要联网请求数据
		if (mOrderSignForeman == null) {
		    mOdersId = getIntent().getExtras()
					.getString(AppConstants.PARAM_ORDER_ID).trim();
			GetOrderForemanTask getOrderForemanTask = new GetOrderForemanTask(
					OrderServeStationSignActivity.this, mOdersId);
			getOrderForemanTask
					.setCallBack(new AbstractHttpResponseHandler<List<OrderSignForeman>>() {
						@Override
						public void onStart() {
							mDataLoadingLayout.showDataLoading();
						}

						@Override
						public void onSuccess(List<OrderSignForeman> result) {
							if (result == null || result.size() == 0) {
								mDataLoadingLayout.showDataEmptyView();
							} else {
								mDataLoadingLayout.showDataLoadSuccess();
								for (OrderSignForeman orderSignForeman : result) {
									if (orderSignForeman.getStatu() >= 4) {//待签合同
										mOrderSignForeman = orderSignForeman;
										initData();
										return;
									}
								}
							}
						}

						@Override
						public void onFailure(int code, String error) {
							mDataLoadingLayout.showDataLoadFailed(error);
						}
					});
			getOrderForemanTask.send();
		}else{
			initData();
		}
	}

	private void initData() {
		mTvName.setText(mOrderSignForeman.getFull_name());
		if (mOrderSignForeman.getIs_auth() == 0) {// 0:未认证 1:已认证
			mTvName.setCompoundDrawables(null, null, null, null);
		}
		mTvStatus.setVisibility(View.VISIBLE);
		mTvStatus.setText("待签合同");
		String city = mOrderSignForeman.getCity_name();//返回的结果：成都市
		mTvCity.setText(city);
		StringBuffer foremanInfo = new StringBuffer(getResources().getString(
				R.string.txt_sign_info_first));
		foremanInfo.append(mOrderSignForeman.getFull_name());
		if (city.contains("成都")) {
			mLlStationInfo.setVisibility(View.VISIBLE);
			foremanInfo.append(getResources().getString(
					R.string.txt_sign_info_third));
		} else {
			mLlStationInfo.setVisibility(View.GONE);
			foremanInfo.append(getResources().getString(
					R.string.txt_sign_info_second));
		}
		mTvForemanInfo.setText(foremanInfo.toString());
		mTvOrderCount.setText(mOrderSignForeman.getGongzhang_orders() + "");

		mRbScore.setRating(mOrderSignForeman.getScore() == 0 ? 5
				: mOrderSignForeman.getScore());
		mTvScore.setText(mOrderSignForeman.getScore() == 0 ? "5分"
				: mOrderSignForeman.getScore() + "分");
		//DisplayImageOptions imageOptions = ImageLoaderOptions.optionsDefaultEmptyPhoto;
		DisplayImageOptions imageOptions = ImageLoaderOptions.getDefaultImageOption();
		Image image = mOrderSignForeman.getImage();
		if (image != null) {
			String thumbPath = image.getThumb_path();
			if (thumbPath != null) {
				ImageUtil.displayImage(thumbPath, mIvHead, imageOptions);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_send_message:
			Util.sendMessage(OrderServeStationSignActivity.this,
					mOrderSignForeman.getMobile(), "");
			break;
		case R.id.ib_to_call:
			Util.callPhone(OrderServeStationSignActivity.this,
					mOrderSignForeman.getMobile());
			break;
		case R.id.tv_sign_success_material:
			ZxbdListByStageTask zxbdListByStageTask = new ZxbdListByStageTask(
					OrderServeStationSignActivity.this, mNode);
			zxbdListByStageTask
					.setCallBack(new AbstractHttpResponseHandler<ZxbdListInfoByStage>() {
						@Override
						public void onSuccess(ZxbdListInfoByStage result) {
							if (result == null || result.getList() == null
									|| result.getList().size() == 0) {
								showToastMsg("抱歉！目前还没有相关材料！");
							} else {
								Zxbd zxbd = result.getList().get(0);// 这里后台只配置一篇文章
								Bundle bundle = new Bundle();
								bundle.putSerializable(
										ZxbdListFragment.EXTRA_ZXBD, zxbd);
								ActivityUtil.next(
										OrderServeStationSignActivity.this,
										ZxbdActivity.class, bundle, false);
							}
						}

						@Override
						public void onFailure(int code, String error) {
							showToastMsg(error);
						}
					});
			zxbdListByStageTask.send();
		default:
			break;
		}
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ActivityUtil.nextActivityWithClearTop(OrderServeStationSignActivity.this, MyOrderActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }	
}
