package com.huizhuang.zxsq.ui.activity.test;

import android.content.Context;
import android.os.Bundle;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ToastUtil;
import com.huizhuang.zxsq.widget.component.TopTypeFilterBar;
import com.huizhuang.zxsq.widget.component.TopTypeFilterBar.OnDropdownBarItemClickListener;
import com.huizhuang.zxsq.widget.component.TopTypeFilterBar.TopType;

/**
 * 测试过滤条自定义控件页面
 * 
 * @ClassName: TestFilterBarActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2015-1-13 下午4:26:52
 */
public class TestFilterBarActivity extends BaseActivity {

	private TopTypeFilterBar mTopTypeFilterBar;

	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_test_filter_bar);
		mContext = TestFilterBarActivity.this;

		initViews();
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		mTopTypeFilterBar = (TopTypeFilterBar) findViewById(R.id.top_type_filter_bar);
		mTopTypeFilterBar.setOnDropdownBarItemClickListener(new OnDropdownBarItemClickListener() {

			@Override
			public void onItemClick(TopType topType, KeyValue filterKeyValue) {
				// TODO 获得是哪个筛选项的信息 在这里刷新页面
				switch (topType) {
				case FILTER_NEARBY:
					ToastUtil.showMessage(mContext, TopTypeFilterBar.FILTER_NAMES[0] + filterKeyValue);
					break;
				case FILTER_PRICE:
					ToastUtil.showMessage(mContext, TopTypeFilterBar.FILTER_NAMES[1] + filterKeyValue);
					break;
				case FILTER_PRIVILEGE:
					ToastUtil.showMessage(mContext, TopTypeFilterBar.FILTER_NAMES[2] + filterKeyValue);
					break;
				case FILTER_SORT:
					ToastUtil.showMessage(mContext, TopTypeFilterBar.FILTER_NAMES[3] + filterKeyValue);
					break;
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (mTopTypeFilterBar.isDropdownBarShowing()) {
			mTopTypeFilterBar.hideDropdownBar();
		} else {
			super.onBackPressed();
		}
	}
}
