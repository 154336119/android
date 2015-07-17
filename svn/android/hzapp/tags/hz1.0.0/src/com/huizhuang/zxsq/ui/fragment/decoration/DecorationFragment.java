package com.huizhuang.zxsq.ui.fragment.decoration;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.activity.common.FilterActivity;
import com.huizhuang.zxsq.ui.adapter.FragmentViewPagerAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.PagerSlidingTabStrip;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/** 
* @ClassName: DecorationFragment 
* @Description: 找装修tab
* @author th 
* @mail 342592622@qq.com   
* @date 2015-1-13 上午11:15:10 
*  
*/
public class DecorationFragment extends BaseFragment {
	private static String[] SUB_FRAGMENT;
	private final static int TO_SERACH_CODE = 100;

	private ViewPager mPager;
	private FragmentViewPagerAdapter mAdapter;
	private PagerSlidingTabStrip mIndicator;
	public enum DecorationType {
		/**
		 * 找工长
		 */
        FIND_FOREMAN,
        /**
		 * 找设计师
		 */
        FIND_DESIGNER,
        /**
		 * 找装修公司
		 */
        FIND_DECORATION_COMPANY
    }
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);    
          
    }  
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_decoration_tab,
				container, false);
    	//影藏设计师和装修公司后的添加的bar
    	CommonActionBar mCommonActionBar = (CommonActionBar) view.findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle("找工长");
		
    	SUB_FRAGMENT = new String[] {"工长","设计师","装修公司"};
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new ForemanListFragment());
		//fragments.add(new DesignerListFragment());
		//fragments.add(new DecorationCompanyListFragment());
		
		mAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), fragments, SUB_FRAGMENT);
		mPager = (ViewPager) view.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(0);
		mIndicator = (PagerSlidingTabStrip) view.findViewById(R.id.indicator);		
		mIndicator.setViewPager(mPager);
		initIndicator();
		view.findViewById(R.id.iv_serach).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//综合搜索
				Intent intent = new Intent(getActivity(), FilterActivity.class);
				startActivityForResult(intent, TO_SERACH_CODE);
			}
		});

		return view;
		
    } 
    
    private void initIndicator(){
    	mIndicator.setTextColor(getResources().getColor(R.color.color_ffbba2));
		mIndicator.setTextSize(34);
    }
    
    /**
     * 切换fragment
     * @param decorationType
     */
    public void switchToFragment(DecorationType decorationType){
    	switch (decorationType) {
    	
		case FIND_FOREMAN:
			mPager.setCurrentItem(0);
			break;
		case FIND_DESIGNER:
			mPager.setCurrentItem(1);
			break;
		case FIND_DECORATION_COMPANY:
			mPager.setCurrentItem(2);
			break;
		default:
			break;
		}
    	mIndicator.notifyDataSetChanged();
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.e("fragment onActivityResult()");
		if(resultCode == Activity.RESULT_OK && requestCode == TO_SERACH_CODE){
			//调用对应fragment的搜索方法
			LogUtil.e("page:"+mIndicator.getSelectedPosition());
			Fragment fragment = mAdapter.getFragments().get(mIndicator.getSelectedPosition());
		}
	}   
    
}
