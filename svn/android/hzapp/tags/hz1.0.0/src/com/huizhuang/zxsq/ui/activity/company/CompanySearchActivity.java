package com.huizhuang.zxsq.ui.activity.company;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.task.CompanySearchHotKeywordTask;
import com.huizhuang.zxsq.http.task.CompanySearchTask;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.CompanyListViewAdapter;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.SearchHistoryKeywordUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;

public class CompanySearchActivity extends BaseActivity {

	private DataLoadingLayout mDataLoadingLayout;

	private EditText mEtKeyword;
	
	private FrameLayout mLayoutList;
	private LinearLayout mLayoutkeyword;
	
	private ListView mLvCompany;
	private ListView mLvHotKeyword;
	private ListView mLvHistoryKeyword;

	private CompanyListViewAdapter mCompanyListAdapter;
	private KeywordAdapter mHotKeywordAdapter;
	private KeywordAdapter mHistoryKeywordAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_search);
		getIntentExtra();
		initViews();
		initHistoryData();
		httpRequestHotSearch();
	}

	private void getIntentExtra() {

	}

	private void initViews() {
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String keyword = mEtKeyword.getText().toString();
				if (!TextUtils.isEmpty(keyword)) {
					SearchHistoryKeywordUtil.getInstance().save(keyword);
				}
				finish();
			}
		});
		// 搜索关键字
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_load_layout);
		mDataLoadingLayout.showDataLoadSuccess();
		
		mEtKeyword = (EditText) findViewById(R.id.edt_keywork);
		mEtKeyword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String keyword = s.toString();
				if (!TextUtils.isEmpty(keyword)) {
					mLayoutList.setVisibility(View.VISIBLE);
					mLayoutkeyword.setVisibility(View.GONE);
					httpRequestSearchByKeyword(keyword);
				} else {
					mLayoutkeyword.setVisibility(View.VISIBLE);
					mLayoutList.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		mLayoutList = (FrameLayout) findViewById(R.id.fl_list);
		mLayoutkeyword = (LinearLayout) findViewById(R.id.ll_keyword);
		
		// 公司列表
		mCompanyListAdapter = new CompanyListViewAdapter(THIS, THIS);
		mLvCompany = (ListView) findViewById(R.id.lv_filter);
		mLvCompany.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//选择公司
			}
		});
		mLvCompany.setAdapter(mCompanyListAdapter);
		// 关键字匹配列表
		mHotKeywordAdapter = new KeywordAdapter(THIS);
		mHistoryKeywordAdapter = new KeywordAdapter(THIS);
		mLvHotKeyword = (ListView) findViewById(R.id.lv_hot_filter);
		mLvHotKeyword.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String keyword = mHistoryKeywordAdapter.getItem(position);
				mEtKeyword.setText(keyword);
			}
		});
		mLvHotKeyword.setAdapter(mHotKeywordAdapter);
		
		mLvHistoryKeyword = (ListView) findViewById(R.id.lv_history_filter);
		mLvHistoryKeyword.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String keyword = mHistoryKeywordAdapter.getItem(position);
				mEtKeyword.setText(keyword);
			}
		});
		mLvHistoryKeyword.setAdapter(mHistoryKeywordAdapter);
	}

	/**
	 * 初始化历史关键字数据
	 */
	private void initHistoryData() {
		List<String> list = SearchHistoryKeywordUtil.getInstance().getKeywords();
		mHotKeywordAdapter.setList(list);
		mHotKeywordAdapter.notifyDataSetChanged();
		
		mHistoryKeywordAdapter.setList(list);
		mHistoryKeywordAdapter.notifyDataSetChanged();
	}

	/**
	 * 获取热门搜索条件
	 */
	private void httpRequestHotSearch() {
		CompanySearchHotKeywordTask task = new CompanySearchHotKeywordTask(THIS);
		task.setCallBack(new AbstractHttpResponseHandler<List<String>>() {

			@Override
			public void onSuccess(List<String> list) {
				mHotKeywordAdapter.setList(list);
				mHotKeywordAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(int code, String error) {

			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

		});
		task.send();
	}

	/**
	 * 根据关键字搜索
	 * 
	 * @param keyword
	 */
	private void httpRequestSearchByKeyword(String keyword) {
		CompanySearchTask task = new CompanySearchTask(this, keyword);
		task.setCallBack(new AbstractHttpResponseHandler<List<Company>>() {

			@Override
			public void onSuccess(List<Company> list) {
				if (list.size() > 0) {
					mCompanyListAdapter.setList(list);
					mCompanyListAdapter.notifyDataSetChanged();
					mDataLoadingLayout.showDataLoadSuccess();
				} else {
					mDataLoadingLayout.showDataLoadEmpty(getResources().getString(R.string.txt_company_search_empty));
				}
			}

			@Override
			public void onFailure(int code, String error) {

			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

		});
		task.send();
	}

	private class KeywordAdapter extends CommonBaseAdapter<String> {
		
		public KeywordAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv;
			if (convertView == null) {
				tv = new TextView(mContext);
			} else {
				tv = (TextView) convertView;
			}
			tv.setText(getItem(position));
			tv.setPadding(8, 8, 8, 8);

			return tv;
		}
	}
}
