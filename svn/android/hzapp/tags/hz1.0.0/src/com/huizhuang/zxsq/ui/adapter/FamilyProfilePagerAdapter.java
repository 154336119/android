package com.huizhuang.zxsq.ui.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 爱家档案ViewPager列表
 * 
 * @ClassName: FamilyProfilePagerAdapter
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-28 上午11:02:00
 */
public class FamilyProfilePagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> mFragmentList;

	public FamilyProfilePagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
		super(fm);
		mFragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int index) {
		return mFragmentList.get(index);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}

}
