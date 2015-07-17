package com.huizhuang.zxsq.ui.activity.account;

import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.Supervisioner;
import com.huizhuang.zxsq.http.bean.account.SupervisionerGroup;
import com.huizhuang.zxsq.http.task.account.GetSupervisionerListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.wheel.CommonDatePickerWheelPanel;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @ClassName: WorksiteSupervisionOrderActivity
 * @Description: 预约监理
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-1-22 下午3:37:01
 * 
 */
public class WorksiteSupervisionOrderActivity extends BaseActivity {

	private final static int REQUEST_NODE_CODE = 310;
	private final static int REQUEST_SUPERVISON_CODE = 311;

	private Button mBtnDatetime;
	private TextView mTvTimeDes;
	private CircleImageView mCivHead1;
	private CircleImageView mCivHead2;
	private CircleImageView mCivHead3;
	private TextView mTvNodeName;
	private TextView mTvJlsDes;
	private TextView mTvJlsName;
	private CommonActionBar mCommonActionBar;

	private String mCheckSupervisionerId = "";

	private CommonDatePickerWheelPanel mWheelSeletDatePanle;

	private int mOrderId;
	private String mDatetime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_worksite_supervision_order);
		initExtraData();
		initActionBar();
		initView();
		httpRequestQuerySupervisionerData();
	}

	private void initExtraData() {
		mOrderId = getIntent().getExtras().getInt(AppConstants.PARAM_ORDER_ID);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.txt_yuyue_lj_head);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						btnBackOnClick();
					}
				});
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		LogUtil.d("initView");

		mTvNodeName = (TextView) findViewById(R.id.tv_node_name);
		mTvJlsDes = (TextView) findViewById(R.id.tv_jls_des);
		mCivHead1 = (CircleImageView) findViewById(R.id.civ_head1);
		mCivHead2 = (CircleImageView) findViewById(R.id.civ_head2);
		mCivHead3 = (CircleImageView) findViewById(R.id.civ_head3);
		mBtnDatetime = (Button) findViewById(R.id.btn_datetime);
		mTvTimeDes = (TextView) findViewById(R.id.tv_time_des);
		mTvJlsName = (TextView) findViewById(R.id.tv_jls_name);

		mTvTimeDes
				.setText(Html
						.fromHtml("该监理大概需要耗时<font color='#ff6c38'>1.5</font>小时，请您确定您的时间是否充裕，如需变更预约时间，请您提前与监理师电话沟通"));
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnSumbitOnClick();
			}
		});

		findViewById(R.id.btn_datetime_zone).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						itemDateTimeOnClick(v);
					}
				});

		findViewById(R.id.lin_node).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 选择监理节点
				itemCheckNodeOnClick(v);
			}
		});

		findViewById(R.id.lin_jls).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 选择监理师
				itemLinSupervisionersOnClick(v);
			}
		});

		// findViewById(R.id.tv_check_node).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// itemCheckNodeOnClick(v);
		// }
		// });
		//
		// findViewById(R.id.lin_supervisioners).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// itemLinSupervisionersOnClick(v);
		// }
		// });
	}

	/**
	 * 按钮事件 - 返回
	 */
	private void btnBackOnClick() {
		LogUtil.d("btnBackOnClick");

		finish();
	}

	/**
	 * 按钮事件 - 选择日历时间
	 */
	private void itemDateTimeOnClick(View v) {
		LogUtil.d("itemDateTimeOnClick View = " + v);

		showTimePickerDialog();
	}

	/**
	 * 按钮事件 - 查看监理师
	 */
	private void itemLinSupervisionersOnClick(View v) {
		LogUtil.d("itemLinSupervisionersOnClick View = " + v);
		Bundle bd = new Bundle();
		bd.putString(WorksiteSupervisionerListActivity.ID_KEY,
				mCheckSupervisionerId);
		ActivityUtil.next(WorksiteSupervisionOrderActivity.this,
				WorksiteSupervisionerListActivity.class, bd,
				REQUEST_SUPERVISON_CODE);
	}

	/**
	 * 按钮事件 - 选择监理节点
	 */
	private void itemCheckNodeOnClick(View v) {
		LogUtil.d("itemCheckNodeOnClick View = " + v);
		Intent intent = new Intent(WorksiteSupervisionOrderActivity.this,
				WorksiteSupervisionNodeListActivity.class);
		intent.putExtra(WorksiteSupervisionNodeListActivity.ID_KEY, mOrderId);
		startActivityForResult(intent, REQUEST_NODE_CODE);
	}

	/**
	 * 按钮事件 - 预约监理
	 */
	private void btnSumbitOnClick() {
		LogUtil.d("btnSumbitOnClick");
		if (TextUtils.isEmpty(mDatetime)) {
			showToastMsg("你未选择预约时间或节点，请完善后提交");
		} else if (mTvNodeName.getTag() == null) {
			showToastMsg("你未选择预约时间或节点，请完善后提交");
		} else {
			httpRequestOrderReserve();
			AnalyticsUtil.onEvent(this,
					UmengEvent.ID_ACCOUNT_SUPERVISION_ORDER_SUBMIT);
		}
	}

	/**
	 * 显示时间选择对话框
	 */
	@SuppressLint("InflateParams")
	private void showTimePickerDialog() {
		if (null == mWheelSeletDatePanle) {
			mWheelSeletDatePanle = new CommonDatePickerWheelPanel(this, true);
			mWheelSeletDatePanle.setEnsureClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 将默认的日期格式转化成yyyy-MM-dd格式
					mDatetime = DateUtil.format(
							mWheelSeletDatePanle.getDatetime(),
							"yyyyMMddHHmmss", "yyyy-MM-dd HH:mm")+":00";
					mWheelSeletDatePanle.dismissDialog();
					mBtnDatetime.setText(mDatetime);
				}
			});
		}

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		mWheelSeletDatePanle.setTitle("选择预约时间");
		mWheelSeletDatePanle.initDateTimePicker(year, month, day);
		mWheelSeletDatePanle.showDialog();
	}

	/**
	 * HTTP请求 - 预约监理
	 */
	public void httpRequestOrderReserve() {
		LogUtil.d("httpRequestOrderReserve");

		RequestParams params = new RequestParams();
		params.put("order_id", mOrderId);
		params.put("node_id", mTvNodeName.getTag().toString());
		params.put("reserve_time", mDatetime);
		HttpClientApi.post(HttpClientApi.REQ_ACCOUNT_SUPERVISION_RESERVE,
				params, new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
						LogUtil.d("httpRequestOrderReserve onStart");

						showWaitDialog(getString(R.string.txt_none_yuyue_in));
					}

					@Override
					public void onFinish() {
						super.onFinish();
						LogUtil.d("httpRequestOrderReserve onFinish");

						hideWaitDialog();
					}

					@Override
					public void onFailure(NetroidError netroidError) {
						LogUtil.d("httpRequestOrderReserve onFailure NetroidError = "
								+ netroidError);
						showPopupWindow();
					}

					@Override
					public void onSuccess(String responseInfo) {
						LogUtil.d("httpRequestOrderReserve onSuccess responseInfo = "
								+ responseInfo);

						showToastMsg(getString(R.string.txt_yuyue_success));
						finish();
						//
						// Bundle bundle = new Bundle();
						// bundle.putString("nodeName",
						// getResources().getString(SUPERVISION_NODE_RID[mMyAdapter.getIndex()]));
						// ActivityUtil.next(WorksiteSupervisionOrderActivity.this,
						// WorksiteSupervisionOrderSuccessActivity.class,
						// bundle, -1, true);
					}

				});
	}

	private void showPopupWindow(){
		LayoutInflater inflater = LayoutInflater.from(this); 
        // 引入窗口配置文件 
        View view = inflater.inflate(R.layout.popupwindow_supervision_order, null); 
        // 创建PopupWindow对象 
        final PopupWindow pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);  
        // 需要设置一下此参数，点击外边可消失 
        pop.setBackgroundDrawable(new BitmapDrawable()); 
        //设置点击窗口外边窗口消失 
        pop.setOutsideTouchable(true); 
        // 设置此参数获得焦点，否则无法点击 
        pop.setFocusable(true);
        pop.showAsDropDown(mCommonActionBar);
	}
	
	private void httpRequestQuerySupervisionerData() {
		GetSupervisionerListTask task = new GetSupervisionerListTask(
				WorksiteSupervisionOrderActivity.this, 3, 1);
		task.setCallBack(new AbstractHttpResponseHandler<SupervisionerGroup>() {

			@Override
			public void onSuccess(SupervisionerGroup result) {
				if (result != null && result.getList() != null) {
					List<Supervisioner> supervisioners = result.getList();
					boolean isFirst = true;
					for (Supervisioner supervisioner : supervisioners) {
						if (!isFirst) {
							mCheckSupervisionerId += ",";
						}
						isFirst = false;
						mCheckSupervisionerId += supervisioner.getId();
					}
					initPhotoView(supervisioners);
				} else {
					mCivHead1.setVisibility(View.INVISIBLE);
					mCivHead2.setVisibility(View.INVISIBLE);
					mCivHead3.setVisibility(View.INVISIBLE);
					mTvJlsDes.setText(Html
							.fromHtml("成功匹配<font color='#ff6c38'>" + 0
									+ "</font>个监理师"));
				}
			}

			@Override
			public void onFailure(int code, String error) {

				showToastMsg(error);
			}

			@Override
			public void onStart() {
				showWaitDialog("加载中...");
				super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
			}

		});
		task.send();
	}

	private void initPhotoView(List<Supervisioner> supervisioners) {
		String text = "";
		switch (supervisioners.size()) {
		case 0:
			mCivHead1.setVisibility(View.INVISIBLE);
			mCivHead2.setVisibility(View.INVISIBLE);
			mCivHead3.setVisibility(View.INVISIBLE);
			text = "成功匹配<font color='#ff6c38'>" + 0 + "</font>个监理师";
			break;
		case 1:
			text = "成功匹配<font color='#ff6c38'>" + 1 + "</font>个监理师";
			if (supervisioners.get(0).getPhoto() != null) {
				ImageLoader.getInstance().displayImage(
						supervisioners.get(0).getPhoto().getThumb_path(),
						mCivHead1, ImageLoaderOptions.getDefaultImageOption());
				mCivHead1.setVisibility(View.VISIBLE);
			}
			break;
		case 2:
			text = "成功匹配<font color='#ff6c38'>" + 2 + "</font>个监理师";
			if (supervisioners.get(0).getPhoto() != null) {
				ImageLoader.getInstance().displayImage(
						supervisioners.get(0).getPhoto().getThumb_path(),
						mCivHead1, ImageLoaderOptions.getDefaultImageOption());
				mCivHead1.setVisibility(View.VISIBLE);
			}
			if (supervisioners.get(1).getPhoto() != null) {
				ImageLoader.getInstance().displayImage(
						supervisioners.get(1).getPhoto().getThumb_path(),
						mCivHead2, ImageLoaderOptions.getDefaultImageOption());
				mCivHead2.setVisibility(View.VISIBLE);
			}
			break;
		case 3:
			text = "成功匹配<font color='#ff6c38'>" + 3 + "</font>个监理师";
			if (supervisioners.get(0).getPhoto() != null) {
				ImageLoader.getInstance().displayImage(
						supervisioners.get(0).getPhoto().getThumb_path(),
						mCivHead1, ImageLoaderOptions.getDefaultImageOption());
				mCivHead1.setVisibility(View.VISIBLE);
			}
			if (supervisioners.get(1).getPhoto() != null) {
				ImageLoader.getInstance().displayImage(
						supervisioners.get(1).getPhoto().getThumb_path(),
						mCivHead2, ImageLoaderOptions.getDefaultImageOption());
				mCivHead2.setVisibility(View.VISIBLE);
			}
			if (supervisioners.get(2).getPhoto() != null) {
				ImageLoader.getInstance().displayImage(
						supervisioners.get(2).getPhoto().getThumb_path(),
						mCivHead3, ImageLoaderOptions.getDefaultImageOption());
				mCivHead3.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
		mTvJlsDes.setText(Html.fromHtml(text));
		mTvJlsName.setText("");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode) {
			if (REQUEST_NODE_CODE == requestCode) {
				String name = data.getExtras().getString(
						WorksiteSupervisionNodeListActivity.TITLE_KEY);
				String id = data.getExtras().getString(
						WorksiteSupervisionNodeListActivity.ID_KEY);
				mTvNodeName.setText(name);
				mTvNodeName.setTag(id);
			} else if (REQUEST_SUPERVISON_CODE == requestCode) {
				Supervisioner supervisioner = (Supervisioner) data
						.getSerializableExtra(WorksiteSupervisionerDetailActivity.SUPERVISIONER_KEY);
				if (supervisioner != null && supervisioner.getPhoto() != null) {
					mCheckSupervisionerId = supervisioner.getId();
					mCivHead1.setVisibility(View.VISIBLE);
					mCivHead2.setVisibility(View.INVISIBLE);
					mCivHead3.setVisibility(View.INVISIBLE);
					ImageLoader.getInstance().displayImage(
							supervisioner.getPhoto().getThumb_path(),
							mCivHead1,
							ImageLoaderOptions.getDefaultImageOption());
					mTvJlsName.setText("更换监理师");
					String text = supervisioner.getName();
					if(supervisioner.getWork_year() > 0){
						text += ",从事监理"+supervisioner.getWork_year()+"年";
					}
					mTvJlsDes.setText(text);
				}
			}
		}
	}

}
