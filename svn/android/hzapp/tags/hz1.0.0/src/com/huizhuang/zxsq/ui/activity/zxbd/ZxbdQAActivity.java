package com.huizhuang.zxsq.ui.activity.zxbd;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.module.ZxbdAnswer;
import com.huizhuang.zxsq.module.parser.ZxbdAnswerParser;
import com.huizhuang.zxsq.ui.adapter.ZxbdQAListAdapter;
import com.huizhuang.zxsq.ui.activity.base.BaseListActivity;
import com.huizhuang.zxsq.utils.FileUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * 装修宝典问答列表
 * 
 * @ClassName: ZxbdQAActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-27 下午2:31:13
 */
public class ZxbdQAActivity extends BaseListActivity {

	private ArrayList<ZxbdAnswer> mZxbdAnswerList;
	private String mStrTitle;
	private HashMap<String, String> mTitleMap = new HashMap<String,String>();
	private String mMomentOneLevel;
	private String mMomentTwoLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);
		setContentView(R.layout.zxbd_qa_list_activity);
		getIntentExtras();
		initActionBar();
		initViews();
	}

	/**
	 * 获取Intent传递过来的参数
	 */
	private void getIntentExtras() {
		LogUtil.d("getIntentExtras");
		mStrTitle = getIntent().getExtras().getString("title");
		mZxbdAnswerList = getIntent().getExtras().getParcelableArrayList("answer");
		if(mZxbdAnswerList==null){
			initData();
		}
	}
	
	
	private void initTitleMap(){
		mTitleMap.put("收房验房", "guideline_1.json");
		mTitleMap.put("量房设计", "guideline_2.json");
		mTitleMap.put("预算", "guideline_3.json");
		mTitleMap.put("选定装修公司", "guideline_4.json");
		mTitleMap.put("建材采购", "guideline_5.json");
		mTitleMap.put("买家具", "guideline_6.json");
		mTitleMap.put("开工", "guideline_7.json");
		mTitleMap.put("水电改造", "guideline_8.json");
		mTitleMap.put("泥瓦工程", "guideline_9.json");
		mTitleMap.put("土木工程", "guideline_10.json");
		mTitleMap.put("油漆工程", "guideline_11.json");
		mTitleMap.put("竣工", "guideline_12.json");
		mTitleMap.put("软装", "guideline_13.json");
		mTitleMap.put("入住", "guideline_14.json");
		mTitleMap.put("防坑大全", "guideline_15.json");
	}

	private void initData(){
		try {
			mMomentTwoLevel=getIntent().getStringExtra("name");
			String idStr=getIntent().getStringExtra("id");
			int id=Integer.parseInt(idStr);
			if(id>0) id-=1;
			initTitleMap();
			String data = FileUtil.getFromAssets(this, mTitleMap.get(mMomentTwoLevel));
			ArrayList<ZxbdAnswer> list = parseData(data);;
			mZxbdAnswerList=list.get(id).childs;
			mStrTitle=list.get(id).title;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<ZxbdAnswer> parseData(String data) throws JSONException{
		return new ZxbdAnswerParser().parse(data);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		LogUtil.d("initActionBar");

		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle(mStrTitle);
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnBackOnClick();
			}
		});
		commonActionBar.setRightImgBtn(R.drawable.ic_actionbar_share, new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnShareOnClick();
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		LogUtil.d("initViews");

		ZxbdQAListAdapter zxbdQAListAdapter = new ZxbdQAListAdapter(this);
		zxbdQAListAdapter.setList(mZxbdAnswerList);
		setListAdapter(zxbdQAListAdapter);
	}

	/**
	 * 按钮事件 - 返回
	 */
	protected void btnBackOnClick() {
		LogUtil.d("btnBackOnClick");

		finish();
	}

	/**
	 * 按钮事件 - 分享
	 */
	protected void btnShareOnClick() {
		LogUtil.d("btnShareOnClick");

		Share share = new Share();
		share.setCallBack(true);
		String text="我正在查看#"+getResources().getString(R.string.app_name)+"APP#上的装修日记，学习到了很多装修的知识。装修的人都在用这个软件，比传统装修节省40%的费用！强烈推荐你试试~";
		share.setText(text);
		
		Util.showShare(false, this, share);
	}

}
