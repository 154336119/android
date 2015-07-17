package com.huizhuang.zxsq.ui.activity.complaints;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.task.common.GetOrderListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.common.OrderListActivity;
import com.huizhuang.zxsq.ui.activity.common.OrderListActivity.OrderType;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 投诉反馈，问题选择页面
 * 
 * @ClassName: ComplaintsFeedbackChoiceActivity
 * @author wumaojie.gmail.com
 * @date 2015-2-2 下午4:21:02
 */
public class ComplaintsFeedbackChoiceActivity extends BaseActivity implements
		OnClickListener {

	private CommonActionBar mCommonActionBar;

	// 订单布局
	protected LinearLayout order_layout;

	// 更多订单，合同备案，担保交易，第三方监理，投诉闪电处理，其它问题
	protected LinearLayout more_order, contract_record, secured_transactions,
			third_supervision, complaint_handling, other;

	private Order order;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complaints_choice);
		initActionBar();
		initViews();
		initData();
	}

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setVisibility(View.VISIBLE);
		mCommonActionBar.setActionBarTitle("请问需要什么类型的反馈？");
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initViews() {
		order_layout = (LinearLayout) findViewById(R.id.complaints_choice_order_layout);

		order_layout.setVisibility(View.INVISIBLE);

		more_order = (LinearLayout) findViewById(R.id.complaints_choice_more_order);
		contract_record = (LinearLayout) findViewById(R.id.complaints_choice_contract_record);
		secured_transactions = (LinearLayout) findViewById(R.id.complaints_choice_secured_transactions);
		third_supervision = (LinearLayout) findViewById(R.id.complaints_choice_third_supervision);
		complaint_handling = (LinearLayout) findViewById(R.id.complaints_choice_complaint_handling);
		other = (LinearLayout) findViewById(R.id.complaints_choice_other);

		order_layout.setOnClickListener(this);
		more_order.setOnClickListener(this);
		contract_record.setOnClickListener(this);
		secured_transactions.setOnClickListener(this);
		third_supervision.setOnClickListener(this);
		complaint_handling.setOnClickListener(this);
		other.setOnClickListener(this);
	}

	private void initData() {
		GetOrderListTask task = new GetOrderListTask(this, Order.ORDER_TYPE_FOREMAN, null);
		task.setCallBack(new AbstractHttpResponseHandler<List<Order>>() {
			@Override
			public void onStart() {
				super.onStart();
				showWaitDialog("正在加载..");
			}

			@Override
			public void onSuccess(List<Order> t) {
				hideWaitDialog();
				setData(t);
			}

			@Override
			public void onFailure(int code, String error) {
				hideWaitDialog();
			}
		});
		task.send();
	}

	private void setData(List<Order> t) {
		if (t != null && t.size() > 0) {
			//参考订单列表 选取 第一条
			order = t.get(0);
			order_layout.setVisibility(View.VISIBLE);
			TextView tvOrderNo = (TextView) findViewById(R.id.tv_order_no);
			TextView tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
			CircleImageView civHeadImg = (CircleImageView) findViewById(R.id.iv_img);
			TextView tvName = (TextView) findViewById(R.id.tv_name);
			TextView tvStatus = (TextView) findViewById(R.id.tv_status);
			TextView tvAreaName = (TextView) findViewById(R.id.tv_area_name);

			tvOrderNo.setText(order.getOrder_no());
			tvOrderTime.setText(DateUtil.timestampToSdate(order.getAdd_time(), "yyyy年MM月dd日 HH:mm"));
			if (!order.getAvater().endsWith(AppConstants.DEFAULT_IMG)) {
				ImageLoader.getInstance().displayImage(order.getAvater(), civHeadImg, ImageLoaderOptions.optionsDefaultEmptyPhoto);
			}
			tvName.setText(order.getFull_name());
			tvAreaName.setText(order.getHousing_address());
			switch (order.getStatu()) {
			case 0:
				tvStatus.setText("已取消");
				break;
			case 1:
				tvStatus.setText("订单处理中");
				break;
			case 2:
				tvStatus.setText("已量房");
				break;
			case 3:
				tvStatus.setText("已设计报价");
				break;
			case 4:
				tvStatus.setText("已签合同");
				break;
			case -1:
				tvStatus.setText("无效订单");
				break;				
			default:
				break;
			}
		}else{
			
		}
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		Intent intent = new Intent(this, ComplaintsFeedbackActivity.class);
		intent.putExtra("order", order);
		switch (v.getId()) {
		case R.id.complaints_choice_order_layout:
			intent = new Intent(this, ComplaintsFeedbackOrdeChoiceActivity.class);
			intent.putExtra("order", order);
			break;
		case R.id.complaints_choice_more_order:
			bundle.putSerializable(OrderListActivity.EXTRA_ORDER_TYPE, OrderType.COMPLAINTS_FEEDBACK);
			bundle.putInt(OrderListActivity.EXTRA_ORDER_TYP, Order.ORDER_TYPE_FOREMAN);
			ActivityUtil.next(this, OrderListActivity.class, bundle, -1);
			finish();
			intent = null;
			break;
		case R.id.complaints_choice_contract_record:
			intent.putExtra("complaintsfeedback_choice", "合同备案问题");
			break;
		case R.id.complaints_choice_secured_transactions:
			intent.putExtra("complaintsfeedback_choice", "担保交易问题");
			break;
		case R.id.complaints_choice_third_supervision:
			intent.putExtra("complaintsfeedback_choice", "第三方监理问题");
			break;
		case R.id.complaints_choice_complaint_handling:
			intent.putExtra("complaintsfeedback_choice", "投诉闪电处理");
			break;
		case R.id.complaints_choice_other:
			intent.putExtra("complaintsfeedback_choice", "其他问题");
			break;
		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
			finish();
		}
	}

}
