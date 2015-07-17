package com.huizhuang.zxsq.ui.activity.complaints;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.task.ComplaintsFeedbackTask;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.common.OrderListActivity;
import com.huizhuang.zxsq.ui.activity.common.OrderListActivity.OrderType;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * 投诉内容输入页面
 */
public class ComplaintsFeedbackActivity extends BaseActivity implements
		OnClickListener {

	private static final int REQCODE = 5432;

	private CommonActionBar mCommonActionBar;

	protected TextView order;
	protected TextView prompt;

	protected ImageView order_select;

	protected EditText content;

	protected TextView submit;

	private Order orderO;

	private String question;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complaints_feedback);
		getIntentExtra();
		initActionBar();
		initViews();
		initData();
	}

	private void getIntentExtra() {
		orderO = (Order) getIntent().getSerializableExtra("order");
		question = getIntent().getStringExtra("complaintsfeedback_choice");
	}

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setVisibility(View.VISIBLE);
		mCommonActionBar.setActionBarTitle("请提供更多信息");
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initViews() {
		order = (TextView) findViewById(R.id.complaints_feedback_order);
		prompt = (TextView) findViewById(R.id.complaints_feedback_prompt);
		content = (EditText) findViewById(R.id.complaints_feedback_content);
		order_select = (ImageView) findViewById(R.id.complaints_feedback_order_select);
		submit = (TextView) findViewById(R.id.complaints_feedback_submit);
		order_select.setOnClickListener(this);
		submit.setOnClickListener(this);

		SpannableStringBuilder spanStringBuilder = new SpannableStringBuilder();
		SpannableString text1 = new SpannableString("请提供更详细的问题描述，以便我们的及时跟踪和处理。我们将在您发起投诉后");
		SpannableString text2 = new SpannableString("30分钟内");
		SpannableString text3 = new SpannableString("与您联系。");

		text2.setSpan(new ForegroundColorSpan(0xffff6c38), 0, text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStringBuilder.append(text1);
		spanStringBuilder.append(text2);
		spanStringBuilder.append(text3);
		prompt.setText(spanStringBuilder);
	}

	private void initData() {
		if (orderO != null) {
			order.setText(orderO.getOrder_no());
		} else {
			order.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.complaints_feedback_submit:
			submit();
			break;
		case R.id.complaints_feedback_order_select:
			Bundle bundle = new Bundle();
			bundle.putSerializable(OrderListActivity.EXTRA_ORDER_TYPE,
					OrderType.COMPLAINTS_FEEDBACK);
			bundle.putInt(OrderListActivity.EXTRA_ORDER_TYP,
					Order.ORDER_TYPE_FOREMAN);
			ActivityUtil.next(this, OrderListActivity.class, bundle, REQCODE);
			break;
		default:
			break;
		}
	}

	private void submit() {
		question = question == null || question.length() == 0 ? "" : "("
				+ question + ")";
		String contentS = content.getText().toString();

		if (orderO == null) {
			showToastMsg("请选择要投诉的订单！");
			return;
		}

		if (contentS.trim().length() == 0) {
			showToastMsg("请描述你的投诉反馈内容！");
			return;
		}

		ComplaintsFeedbackTask task = new ComplaintsFeedbackTask(this,
				orderO.getOrder_id() + "", question + contentS);

		task.setCallBack(new AbstractHttpResponseHandler<Result>() {
			@Override
			public void onStart() {
				super.onStart();
				showWaitDialog("请稍后...");
			}

			@Override
			public void onSuccess(Result t) {
				hideWaitDialog();
				showToastMsg("投诉已提交");
				finish();
			}

			@Override
			public void onFailure(int code, String error) {
				showToastMsg(error);
				hideWaitDialog();
			}
		});
		task.send();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQCODE && resultCode == RESULT_OK && data != null) {
			orderO = (Order) data.getSerializableExtra("order");
			initData();
		}
	}
}
