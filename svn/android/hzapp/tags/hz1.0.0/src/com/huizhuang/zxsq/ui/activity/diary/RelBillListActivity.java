package com.huizhuang.zxsq.ui.activity.diary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.BillDate;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.parser.BillDateGroupParser;
import com.huizhuang.zxsq.ui.activity.base.BaseListActivity;
import com.huizhuang.zxsq.ui.adapter.DiaryRelBillListViewAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** 
* @ClassName: RelBillListActivity 
* @Description: 时间分组模式 
* @author lim 
* @mail limshare@gmail.com   
* @date 2014-10-10 下午6:51:22 
*  
*/
public class RelBillListActivity extends BaseListActivity implements XListView.IXListViewListener, OnClickListener{

	private boolean isFastScroll = false;
	private boolean isShadowVisible = false;

	private DiaryRelBillListViewAdapter mAdapter;
	private XListView mXLV;
	private int mCurrentMouth = 0;
	private String mKeyword;
	private EditText etSearch;
	private TextView mTvDate;
	
	private String mBillIds;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			isFastScroll = savedInstanceState.getBoolean("isFastScroll");
			isShadowVisible = savedInstanceState.getBoolean("isShadowVisible");
		}
		mBillIds = getIntent().getStringExtra("ids");
		setContentView(R.layout.diary_edit_rel_bill_list);
		initActionBar();
		initViews();
		
	}
	
	private void initActionBar(){
		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle("关联清单");
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		commonActionBar.setRightTxtBtn(R.string.save, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String ids = proRecommendList(mAdapter.getBillIds());
				Bundle bd = new Bundle();
				bd.putString("ids", ids);
				ActivityUtil.backWithResult(THIS, Activity.RESULT_OK, bd);
			}
		});
	}
	
	private void initViews(){
		etSearch = (EditText) findViewById(R.id.etSearch);
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					mKeyword = v.getText().toString();
					loadMessage(0);
					hideSoftInput(v);
					return true;
				}
				return false;
			}
		});
		findViewById(R.id.btn_prev).setOnClickListener(this);
		findViewById(R.id.btn_next).setOnClickListener(this);
		XListView xlv = (XListView) getListView();
		getListView().setFastScrollEnabled(isFastScroll);
		mAdapter = new DiaryRelBillListViewAdapter(THIS); 
		if (!TextUtils.isEmpty(mBillIds)) {
			try {
				mAdapter.setBillIds(mBillIds);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		xlv.setPullLoadEnable(false);
		xlv.setPullRefreshEnable(true);
		xlv.setAutoRefreshEnable(true);
		xlv.setXListViewListener(this);
		setListAdapter(mAdapter);
		mXLV = xlv;
		mTvDate = (TextView) findViewById(R.id.tv_date);
	}
	
	private void loadMessage(final int loadType){
		showWaitDialog("加载中...");
		String date = DateUtil.getNextMouth(DateUtil.getStringDate("yyyy-MM-dd"), mCurrentMouth);
		updateDateTitle(date.substring(0, 7));
		User user = ZxsqApplication.getInstance().getUser();
		RequestParams params = new RequestParams();
		params.put("sid", user.getId());
		params.put("year", date.substring(0, 4));//1-按时间倒序；2-按内容分类
		params.put("month", date.substring(5, 7));
		params.put("keyword", mKeyword);
		HttpClientApi.get(HttpClientApi.REQ_ZX_JOURNEY_LIST, params, new BillDateGroupParser(), new RequestCallBack<ArrayList<BillDate>>() {

			@Override
			public void onSuccess(ArrayList<BillDate> t) {
				mAdapter.setList(t);
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(NetroidError error) {
				showToastMsg(error.getMessage());
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
				android.os.Message msg = mPullHandler.obtainMessage(PullListHandler.XLIST_REQUEST, loadType);
				mPullHandler.sendMessageDelayed(msg, 500);
			}
			
		});
	}
	
	private void updateDateTitle(String date) {
		mTvDate.setText(date);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isFastScroll", isFastScroll);
		outState.putBoolean("isShadowVisible", isShadowVisible);
	}
	
	private  PullListHandler mPullHandler = new PullListHandler();
	@SuppressLint("HandlerLeak")
	private class PullListHandler extends Handler{
		
		public static final int XLIST_REFRESH = 0;
		public static final int XLIST_LOAD = 1;
		public static final int XLIST_REQUEST = 2;
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case XLIST_REFRESH:
			case XLIST_LOAD:
				if(mAdapter!=null){
					mAdapter.notifyDataSetChanged();
				}
				break;
			case XLIST_REQUEST:
				int type = (Integer) msg.obj;
				if (type == 0) {
					mXLV.onRefreshComplete();	//下拉刷新完成
				}else{
					mXLV.onLoadMoreComplete(); //加载更多完成
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	}

	@Override
	public void onRefresh() {
        mKeyword = etSearch.getText().toString();
		loadMessage(0);
	}

	@Override
	public void onLoadMore() {
	}
	
	@SuppressWarnings("rawtypes")
	private String proRecommendList(HashMap<String, String> map){
		String array = "[";
		ArrayList<String> list = new ArrayList<String>();
		Iterator<?>  iterator= map.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			list.add(String.valueOf(entry.getValue()));
		}
		for(int i=0;i<list.size();i++){
			array = array+list.get(i)+",";
		}
		array = array.substring(0, array.length()-1);
		array = array+"]";
		return array;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_prev:
			mCurrentMouth--;
			loadMessage(0);
			break;
		case R.id.btn_next:
			mCurrentMouth++;
			loadMessage(0);
			break;

		default:
			break;
		}
	}
}
