package com.huizhuang.zxsq.ui.activity.advertise;

import java.util.List;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.advertise.Advertise;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.AdvertiseAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.widget.ViewFlow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class AdvertiseActivity extends BaseActivity {
	private ViewFlow mViewFlowShow;
	private ImageView mIvFinish;
	private int DELAY_TIME = 5000;
	private List<Advertise> advertiseList;
	private final static float mGuaranteeImageWidth = 560f;// 原始图片的宽度为560px
	private final static float mGuaranteeImageHeight = 800f;// 原始图片的高度为560px
	private final static int mGuranteeImageSpace = 20;// 浮层与边界的距离40px/2--->20dp
	protected static final String AdvertiseActivity = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_advertise);
		setFinishOnTouchOutside(false);
		getIntentExtra();
		initViews();
		setImageScale();//必须在setContentView()后面调用
		setListener();
	}

	private void getIntentExtra() {
		advertiseList = (List<Advertise>)getIntent().getExtras().getSerializable("advertiselist");
	}

	private void initViews() {
		mViewFlowShow = (ViewFlow) findViewById(R.id.viewflow_advertise_show);
		mIvFinish = (ImageView) findViewById(R.id.iv_finish);
		AdvertiseAdapter mAdvertiseAdapter = new AdvertiseAdapter(this, advertiseList);
		mViewFlowShow.setAdapter(mAdvertiseAdapter);
		mViewFlowShow.setSelection(advertiseList.size() * 1000);
		//mViewFlowShow.setSelection(0);
		mViewFlowShow.setValidCount(advertiseList.size());
		mViewFlowShow.setTimeSpan(DELAY_TIME);
		mViewFlowShow.startAutoFlowTimer();
		if(advertiseList.size()>1){
			mViewFlowShow.setNoScroll(false);//默认是false
		}else{
			mViewFlowShow.setNoScroll(true);
		}
		mViewFlowShow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Advertise advertise = advertiseList.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("url", advertise.getUrl());
				ActivityUtil.next(AdvertiseActivity.this, AdvertiseWebActivity.class,bundle,true);
			}
		});
	}

	private void setImageScale() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams activityLps = getWindow().getAttributes();
		activityLps.width = display.getWidth()
				- UiUtil.dp2px(this, mGuranteeImageSpace) * 2;
		activityLps.height = (int) (activityLps.width * mGuaranteeImageHeight / mGuaranteeImageWidth);
		getWindow().setAttributes(activityLps);
	}

	private void setListener() {
		mIvFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.iv_finish:
					finish();
					break;
				}
			}
		});
	}
}
