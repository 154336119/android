package com.huizhuang.zxsq.ui.activity.foreman;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.bean.foreman.ForemanPriceChildren;
import com.huizhuang.zxsq.http.bean.foreman.ForemanPriceList;
import com.huizhuang.zxsq.http.bean.foreman.ForemanPriceParent;
import com.huizhuang.zxsq.http.task.foreman.ForemanPriceListTask;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderCheckPhoneActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

import java.util.ArrayList;
import java.util.List;

public class ForemanPriceListActivity extends BaseActivity implements
		OnClickListener {

	private CommonActionBar mCommonActionBar;
	private ExpandableListView expandableListView;

	protected MyBaseExpandableListAdapter adapter;

	private TextView foreman_pricelist_submit;

	private List<ForemanPriceParent> list = new ArrayList<ForemanPriceParent>();

	private String foreman_id = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foreman_pricelist);
		getIntentExtra();
		initActionBar();
		initViews();
		initData();
		showWaitDialog("正在加载");
	}

	private void getIntentExtra() {
		foreman_id = getIntent().getStringExtra("foreman_id");
	}

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle("透明报价清单");
        mCommonActionBar.setRightImgBtn(R.drawable.ic_actionbar_share, new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("btnShareOnClick");
                AnalyticsUtil.onEvent(ForemanPriceListActivity.this, "click25");
                Share share = new Share();
                share.setCallBack(true);
                String text="我正在查看#"+getResources().getString(R.string.app_name)+"APP#上的装修日记，学习到了很多装修的知识。装修的人都在用这个软件，比传统装修节省40%的费用！强烈推荐你试试~";
                share.setText(text);

                Util.showShare(false, ForemanPriceListActivity.this, share);
            }
        });
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initViews() {
		foreman_pricelist_submit = (TextView) findViewById(R.id.foreman_pricelist_submit);
		foreman_pricelist_submit.setOnClickListener(this);

		expandableListView = (ExpandableListView) findViewById(R.id.elist);
		expandableListView.setAdapter(adapter = new MyBaseExpandableListAdapter());

		// 这里是控制只有一个group展开的效果
		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {
					@Override
					public void onGroupExpand(int groupPosition) {
						for (int i = 0; i < adapter.getGroupCount(); i++) {
							if (groupPosition != i) {
								expandableListView.collapseGroup(i);
							}
						}
					}
				});
	}

	private void initData() {
		ForemanPriceListTask task = new ForemanPriceListTask(this, foreman_id);
		task.setCallBack(new AbstractHttpResponseHandler<ForemanPriceList>() {

			@Override
			public void onSuccess(ForemanPriceList t) {
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

	protected void setData(ForemanPriceList priceList) {
		if (priceList != null && priceList.getList() != null
				&& priceList.getList().size() > 0) {
			list = priceList.getList();
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.foreman_pricelist_submit:
			construction();
			break;

		default:
			break;
		}
	}
	
	private void construction() {
        AnalyticsUtil.onEvent(this, "click26");
		int id = 0;
		try {
			id = Integer.valueOf(foreman_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Bundle bundle = new Bundle();
		bundle.putInt(AppConstants.PARAM_ORDER_TYPE, Order.ORDER_TYPE_FOREMAN);
		bundle.putInt(AppConstants.PARAM_ORDER_COMPANY_ID, id);
        bundle.putString(AppConstants.PARAM_ORDER_SOURCE_NAME, "app_quote_detail");
		ActivityUtil.next(this, OrderCheckPhoneActivity.class, bundle, -1);
	}

	class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return list.get(groupPosition).getChildren() != null ? list
					.get(groupPosition).getChildren().size() : 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupPosition;
		}

		@Override
		public int getGroupCount() {
			return list.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(ForemanPriceListActivity.this,
						R.layout.item_foreman_pricelist_group, null);
			}
			TextView group_name = (TextView) convertView
					.findViewById(R.id.item_foreman_pricelist_group_name);
			if (list.get(groupPosition) != null) {
				group_name.setText(getNotNullString(list.get(groupPosition)
						.getCategory_name()));
			} else {
				group_name.setText("");
			}
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(ForemanPriceListActivity.this,
						R.layout.item_foreman_pricelist, null);
			}
			TextView name = (TextView) convertView
					.findViewById(R.id.item_foreman_pricelist_name);
			TextView unitprice = (TextView) convertView
					.findViewById(R.id.item_foreman_pricelist_unitprice);
			TextView explain = (TextView) convertView
					.findViewById(R.id.item_foreman_pricelist_explain);
			ImageView line_s = (ImageView) convertView
					.findViewById(R.id.item_foreman_pricelist_child_s);
			ImageView line_l = (ImageView) convertView
					.findViewById(R.id.item_foreman_pricelist_child_l);

			line_s.setVisibility(View.VISIBLE);
			line_l.setVisibility(View.GONE);

			if (childPosition == list.get(groupPosition).getChildren().size() - 1) {
				line_s.setVisibility(View.GONE);
				line_l.setVisibility(View.VISIBLE);
			}

			ForemanPriceChildren children = list.get(groupPosition)
					.getChildren().get(childPosition);
			if (children != null) {
				String unit = getNotNullString(children.getUnit());
				if (unit.length() > 0) {
					unit = "（" + unit + "）";
				}
				name.setText(getNotNullString(children.getItem_name()) + unit);
				explain.setText(getNotNullString(children.getDescription()));
				unitprice.setText(getNotNullString(children.getPrice()));
			}

			return convertView;
		}

		private String getNotNullString(String str) {
			if (str == null) {
				str = "";
			}
			return str.trim();
		}

	}
}
