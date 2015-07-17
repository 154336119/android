package com.huizhuang.zxsq.ui.activity.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.ProblemGroup;
import com.huizhuang.zxsq.module.parser.ProblemGroupParser;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.WorksiteSupervisionReportDetailAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;


/** 
* @ClassName: WorksiteSupervisionReportDetailActivity 
* @Description: 质量报告详情
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-5 下午8:53:39 
*  
*/
public class WorksiteSupervisionReportDetailActivity extends BaseActivity implements OnClickListener{

	private CommonActionBar mCommonActionBar;
	private DataLoadingLayout mDataLoadingLayout;
	private ListView mXListView;
	private TextView mTvScore;
	private TextView mTvNode;
	private TextView mTvResult;
	private WorksiteSupervisionReportDetailAdapter mAdapter;

	private int mOrderId; // 订单ID
	private String mNodeId; // 结点ID
	private String mRecordId;//记录ID
	private String mNodeName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.activity_worksite_supervision_report_detail);

		getIntentExtras();
		initActionBar();
		initView();
		httpRequestQueryRecordDetail(XListRefreshType.ON_PULL_REFRESH);
	}

	private void getIntentExtras() {
		mOrderId = getIntent().getExtras().getInt(AppConstants.PARAM_ORDER_ID);
		mNodeId = getIntent().getExtras().getString(AppConstants.PARAM_NODE_ID);
		mRecordId = getIntent().getExtras().getString(AppConstants.PARAM_RECORD_ID);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);

		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnBackOnClick();
			}
		});
	}

	/**
	 * 初始化控件
	 */
	@SuppressLint("InflateParams")
	private void initView() {
		LogUtil.d("initView");

		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.common_dl);

		mXListView = (ListView) findViewById(R.id.report_list_view);
		View headView = LayoutInflater.from(this).inflate(R.layout.adapter_worksite_supervision_report_detail_head, null);
		mTvScore = (TextView)headView.findViewById(R.id.tv_score);
		mTvNode = (TextView)headView.findViewById(R.id.tv_node);
		headView.findViewById(R.id.lin_head).setOnClickListener(this);
		mXListView.addHeaderView(headView);
		
		View footView = LayoutInflater.from(this).inflate(R.layout.adapter_worksite_supervision_report_detail_foot, null);
		mTvResult = (TextView)footView.findViewById(R.id.tv_result);
		footView.findViewById(R.id.btn_submit).setOnClickListener(this);
		mXListView.addFooterView(footView);
		mAdapter = new WorksiteSupervisionReportDetailAdapter(this);
		mXListView.setAdapter(mAdapter);
	}

	private void initData(ProblemGroup problemGroup){
		mNodeName = problemGroup.getTitle();
		mCommonActionBar.setActionBarTitle(problemGroup.getTitle()+"报告");
		mTvScore.setText(problemGroup.getScore()+"分");
		mTvNode.setText(problemGroup.getTitle());
		mTvResult.setText(problemGroup.getRemark());
	}
	
	/**
	 * 按钮事件 - 返回
	 */
	private void btnBackOnClick() {
		LogUtil.d("btnBackOnClick");

		finish();
	}


	/**
	 * HTTP请求 - 查询报告详情
	 * 
	 * @param xListRefreshType
	 */
	private void httpRequestQueryRecordDetail(final XListRefreshType xListRefreshType) {

		RequestParams requestParams = new RequestParams();
		requestParams.put("order_id", mOrderId);
		requestParams.put("node_id", mNodeId);
		HttpClientApi.post(HttpClientApi.REQ_ACCOUNT_SUPERVISION_RECORD_DETAIL, requestParams, new ProblemGroupParser(), new RequestCallBack<ProblemGroup>() {

			@Override
			public void onStart() {
				super.onStart();
				LogUtil.e("httpRequestQueryRecordDetail onStart");

				// 没有数据时下拉列表刷新才显示加载等待视图
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType && 0 == mAdapter.getCount()) {
					mDataLoadingLayout.showDataLoading();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				LogUtil.e("httpRequestQueryRecordDetail onFinish");

			}

			@Override
			public void onFailure(NetroidError netroidError) {
				LogUtil.e("httpRequestQueryRecordDetail onFailure NetroidError = " + netroidError);
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					if (0 == mAdapter.getCount()) {
						mDataLoadingLayout.showDataLoadFailed(netroidError.getMessage());
					} else {
						showToastMsg(netroidError.getMessage());
					}
				} else {
					showToastMsg(netroidError.getMessage());
				}
			}

			@Override
			public void onSuccess(ProblemGroup problemGroup) {
				LogUtil.e("httpRequestQueryRecordDetail onSuccess ProblemGroup = " + problemGroup);
				
				mDataLoadingLayout.showDataLoadSuccess();
				initData(problemGroup);
				if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
					mAdapter.setList(problemGroup);
					if (0 == problemGroup.size()) {
						mDataLoadingLayout.showDataEmptyView();
					}
				} else {
					mAdapter.addList(problemGroup);
				}
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_head:
			//查看图集
			Bundle bd = new Bundle();
			bd.putString(WorksiteGalleryListActivity.GALLERY_TITLE, mNodeName+"图集");
			bd.putInt(AppConstants.PARAM_ORDER_ID, mOrderId);
			bd.putString(AppConstants.PARAM_NODE_ID, mNodeId);
			ActivityUtil.next(WorksiteSupervisionReportDetailActivity.this, WorksiteGalleryListActivity.class,bd,-1);
			break;
		case R.id.btn_submit:
			//评分
			Bundle bundle = new Bundle();
			bundle.putString(AppConstants.PARAM_RECORD_ID, mRecordId);
			bundle.putString(AppConstants.PARAM_NODE_ID, mNodeId);
			ActivityUtil.next(WorksiteSupervisionReportDetailActivity.this, WorksiteSupervisionGradeActivity.class,bundle,-1);
			break;
		default:
			break;
		}
	}

}
