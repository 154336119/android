package com.huizhuang.zxsq.ui.activity.complaints;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 投诉反馈，订单问题选择页面
 */
public class ComplaintsFeedbackOrdeChoiceActivity extends BaseActivity
		implements OnClickListener {

	private CommonActionBar mCommonActionBar;

	// 更多订单，合同备案，担保交易，第三方监理，投诉闪电处理，其它问题
	protected LinearLayout price, design, attitude, quality, timelimit, other,
			order_layout;

	private Order order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complaints_order_choice);
		getIntentExtra();
		initActionBar();
		initViews();
		initData();
	}

	private void getIntentExtra() {
		order = (Order) getIntent().getSerializableExtra("order");
	}

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setVisibility(View.VISIBLE);
		mCommonActionBar.setActionBarTitle("选择问题");
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initViews() {

		order_layout = (LinearLayout) findViewById(R.id.complaints_order_choice_order_layout);
		price = (LinearLayout) findViewById(R.id.complaints_order_choice_price);
		design = (LinearLayout) findViewById(R.id.complaints_order_choice_design);
		attitude = (LinearLayout) findViewById(R.id.complaints_order_choice_attitude);
		quality = (LinearLayout) findViewById(R.id.complaints_order_choice_quality);
		timelimit = (LinearLayout) findViewById(R.id.complaints_order_choice_timelimit);
		other = (LinearLayout) findViewById(R.id.complaints_order_choice_other);

		price.setOnClickListener(this);
		design.setOnClickListener(this);
		attitude.setOnClickListener(this);
		quality.setOnClickListener(this);
		timelimit.setOnClickListener(this);
		other.setOnClickListener(this);

		order_layout.setVisibility(View.INVISIBLE);
	}

	private void initData() {
		if (order != null) {
			order_layout.setVisibility(View.VISIBLE);
			TextView tvOrderNo = (TextView) findViewById(R.id.tv_order_no);
			TextView tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
			CircleImageView civHeadImg = (CircleImageView) findViewById(R.id.iv_img);
			TextView tvName = (TextView) findViewById(R.id.tv_name);
			TextView tvStatus = (TextView) findViewById(R.id.tv_status);
			TextView tvAreaName = (TextView) findViewById(R.id.tv_area_name);

			tvOrderNo.setText(order.getOrder_no());
			tvOrderTime.setText(DateUtil.timestampToSdate(order.getAdd_time(),
					"yyyy年MM月dd日 HH:mm"));
			if (!order.getAvater().endsWith(AppConstants.DEFAULT_IMG)) {
				ImageLoader.getInstance()
						.displayImage(order.getAvater(), civHeadImg,
								ImageLoaderOptions.optionsDefaultEmptyPhoto);
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
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, ComplaintsFeedbackActivity.class);
		intent.putExtra("order", order);
		switch (v.getId()) {
		case R.id.complaints_order_choice_price:
			intent.putExtra("complaintsfeedback_choice", "装修价格问题");
			break;
		case R.id.complaints_order_choice_design:
			intent.putExtra("complaintsfeedback_choice", "室内设计问题");
			break;
		case R.id.complaints_order_choice_attitude:
			intent.putExtra("complaintsfeedback_choice", "服务态度问题");
			break;
		case R.id.complaints_order_choice_quality:
			intent.putExtra("complaintsfeedback_choice", "施工质量问题");
			break;
		case R.id.complaints_order_choice_timelimit:
			intent.putExtra("complaintsfeedback_choice", "施工工期问题");
			break;
		case R.id.complaints_choice_other:
			intent.putExtra("complaintsfeedback_choice", "其他问题");
			break;
		default:
			break;
		}
		startActivity(intent);
		finish();
	}
}
