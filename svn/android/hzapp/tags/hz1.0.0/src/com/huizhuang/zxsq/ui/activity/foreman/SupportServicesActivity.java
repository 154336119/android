package com.huizhuang.zxsq.ui.activity.foreman;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

public class SupportServicesActivity extends FragmentActivity implements
		OnClickListener {

    public static final String FROM_TYPE = "from_type";

	private ImageButton left;
	private ImageButton right;
	private ImageView complete;
	private ImageView up;

	private VerticalViewPager viewPager;
	private MyPagerAdapter myPagerAdapter;
	private List<ImageView> views = new ArrayList<ImageView>();

    private int mFromType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_support_service);
        getIntentExtra();
		initActionBar();
		initViews();
		initData();

	}

    public void getIntentExtra(){
        mFromType = getIntent().getIntExtra(FROM_TYPE, 0);
    }

	private void initActionBar() {
		left = (ImageButton) findViewById(R.id.support_service_header_left);
		right = (ImageButton) findViewById(R.id.support_service_header_right);

		left.setOnClickListener(this);
		right.setOnClickListener(this);
	}

	private void initViews() {
		viewPager = (VerticalViewPager) findViewById(R.id.verticalViewPager);
		complete = (ImageView) findViewById(R.id.support_service_complete);
		up = (ImageView) findViewById(R.id.support_service_up);

		viewPager.setAdapter(myPagerAdapter = new MyPagerAdapter());
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		complete.setOnClickListener(this);
		complete.setVisibility(View.GONE);
	}

	private void initData() {
		views.clear();
		for (int i = 0; i < 5; i++) {
			ImageView view = new ImageView(this);
			switch (i) {
			case 0:
				view.setBackgroundResource(R.drawable.support_service_page_1);
				break;
			case 1:
				view.setBackgroundResource(R.drawable.support_service_page_2);
				break;
			case 2:
				view.setBackgroundResource(R.drawable.support_service_page_3);
				break;
			case 3:
				view.setBackgroundResource(R.drawable.support_service_page_4);
				break;
			case 4:
				view.setBackgroundResource(R.drawable.support_service_page_5);
				break;
			default:
				break;
			}
			views.add(view);
		}
		myPagerAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.support_service_header_left:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.support_service_complete:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.support_service_header_right:
			share();
			break;
		default:
			break;
		}
	}

	private void share() {
		// 分享
		Share share = new Share();
		String text = "我正在查看#"+getResources().getString(R.string.app_name)+"APP#上的装修保障，担保交易，装修满意再付款。装修的人都在用这个软件，比传统装修节省40%的费用！强烈推荐你试试~";
		share.setText(text);
		Util.showShare(false, SupportServicesActivity.this, share);
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(views.get(position));
			return views.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
		}

	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int i, float f, int j) {
		}

		@Override
		public void onPageSelected(int position) {
			complete.setVisibility(View.GONE);
			up.setVisibility(View.VISIBLE);
			switch (position) {
			case 0:
			case 2:
				up.setImageResource(R.drawable.support_service_up_orange);
				break;
			case 1:
			case 3:
				up.setImageResource(R.drawable.support_service_up_white);
				break;
			default:
				up.setVisibility(View.GONE);
                if (mFromType != 1){
                    complete.setVisibility(View.VISIBLE);
                }
				break;
			}
		}

	}
}
