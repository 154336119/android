package com.huizhuang.zxsq.ui.activity;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.GuaranteeAdapter;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.widget.ViewFlow;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class GuaranteeActivity extends BaseActivity {
	private ViewFlow mViewFlowShow;
	private int DELAY_TIME = 5000;
	private final static float mGuaranteeImageWidth = 560f;// 原始图片的宽度为560px
	private final static float mGuaranteeImageHeight = 800f;// 原始图片的高度为560px
	private final static int mGuranteeImageSpace = 20;// 浮层与边界的距离40px/2--->20dp
	private ImageView mIvFinish;
	private static final int Gurantee_IMAGE_RESIDS[] = {
			R.drawable.guarantee_page_1, R.drawable.guarantee_page_2,
			R.drawable.guarantee_page_3, R.drawable.guarantee_page_4 };
	private GuaranteeAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guarantee);
		setFinishOnTouchOutside(false);
		initViews();
		setImageScale();// 必须在setContentView()后面调用
		setListener();
	}

	private void initViews() {
		mViewFlowShow = (ViewFlow) findViewById(R.id.viewflow_guarantee_show);
		mIvFinish = (ImageView) findViewById(R.id.iv_finish);
		mAdapter = new GuaranteeAdapter(this,
				Gurantee_IMAGE_RESIDS);
		mViewFlowShow.setAdapter(mAdapter);
		mViewFlowShow.setSelection(Gurantee_IMAGE_RESIDS.length * 1000);
		mViewFlowShow.setValidCount(Gurantee_IMAGE_RESIDS.length);
		mViewFlowShow.setTimeSpan(DELAY_TIME);
		mViewFlowShow.startAutoFlowTimer();
	}

	private void setImageScale() {
		// 为了适配不同的安卓手机屏幕，需要动态设置图片的大小，这里即为activity的大小
		// 这里原始图片的大小是560px*800px 边界是40px
		DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 获取当前activity的参数值
		WindowManager.LayoutParams activityLps = getWindow().getAttributes();
		activityLps.width = dm.widthPixels - UiUtil.dp2px(this, mGuranteeImageSpace) * 2;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("GuaranteeActivity....onDestroy()");
        mAdapter.clear();
    }
	
}