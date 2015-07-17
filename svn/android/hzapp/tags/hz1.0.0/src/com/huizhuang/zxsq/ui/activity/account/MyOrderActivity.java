package com.huizhuang.zxsq.ui.activity.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.activity.base.BaseFragmentActivity;
import com.huizhuang.zxsq.ui.adapter.FragmentViewPagerAdapter;
import com.huizhuang.zxsq.ui.fragment.account.MyOrderCompanyFragment;
import com.huizhuang.zxsq.ui.fragment.account.MyOrderForemanFragment;
import com.huizhuang.zxsq.widget.PagerSlidingTabStrip;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * User : Lim
 * Email: lgmshare@gmail.com
 * Date : 2015/2/6
 * Time : 11:46
 * 我的订单
 */
public class MyOrderActivity extends BaseFragmentActivity {
    private static final String[] SUB_FRAGMENT = new String[]{"工长订单", "装修公司订单"};

    private ViewPager mPager;
    private FragmentViewPagerAdapter mAdapter;
    private PagerSlidingTabStrip mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        initActionBar();
        initViews();
    }

    private void initActionBar() {
        CommonActionBar mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("我的订单");
        mCommonActionBar.setLeftImgBtn(R.drawable.back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new MyOrderForemanFragment());
        fragments.add(new MyOrderCompanyFragment());

        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, SUB_FRAGMENT);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(0);
        mIndicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
        mIndicator.setTextColor(getResources().getColor(R.color.gray_b3));
        mIndicator.setTextSize(34);
        mIndicator.setViewPager(mPager);
    }

}
