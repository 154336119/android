package com.huizhuang.zxsq.ui.activity.account;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.common.Image;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.account.ImageScreenViewPagerAdapter;
import com.huizhuang.zxsq.widget.HackyViewPager;


/** 
* @ClassName: ScreenImgActivity 
* @Description: 查看全屏大图 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-11 下午5:42:52 
*  
*/
public class ScreenImgActivity extends BaseActivity implements OnClickListener {
	public static final String IMAGE_LIST_KEY = "image_list";
	public static final String IMAGE_INDEX_KEY = "image_list_index";
    private ViewPager mViewPager;
    
    private ImageScreenViewPagerAdapter mAdapter;
    private TextView mTvPageTotal;// 总数
    private TextView mTvPageIndex;
    private int mCurrentIndex = 0;
    
    private List<Image> mImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_img_viewpager);
        getIntentExtra();
        intiViews();
    }

    private void getIntentExtra() {
    	Object[] cobjs = (Object[]) getIntent().getSerializableExtra(IMAGE_LIST_KEY);
    	mImages = new ArrayList<Image>();
    	for (int i = 0; i < cobjs.length; i++) {   
    		Image image = (Image) cobjs[i];   
    		mImages.add(image);   
        }
    	mCurrentIndex = getIntent().getIntExtra(IMAGE_INDEX_KEY, 0);
    }

    private void intiViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);

        mTvPageTotal = (TextView) findViewById(R.id.tv_total);
        mTvPageIndex = (TextView) findViewById(R.id.tv_index);

        mTvPageIndex.setText((mCurrentIndex+1)+"");
        mTvPageTotal.setText(mImages.size()+"");
        mAdapter = new ImageScreenViewPagerAdapter(this, mImages);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                updateUI(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void updateUI(int position) {
        mTvPageIndex.setText(String.valueOf(position + 1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
