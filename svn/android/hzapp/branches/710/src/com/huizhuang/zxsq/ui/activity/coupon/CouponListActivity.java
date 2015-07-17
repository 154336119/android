package com.huizhuang.zxsq.ui.activity.coupon;

import java.util.List;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.ProviceCityArea;
import com.huizhuang.zxsq.http.bean.foreman.Foreman;
import com.huizhuang.zxsq.http.bean.foreman.ForemanList;
import com.huizhuang.zxsq.http.bean.pay.Coupon;
import com.huizhuang.zxsq.http.task.foreman.ForemanListTask;
import com.huizhuang.zxsq.http.task.pay.GetCouponsTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.foreman.ForemanDetailsActivity;
import com.huizhuang.zxsq.ui.adapter.coupon.CouponAdapter;
import com.huizhuang.zxsq.ui.adapter.foreman.ForemanListAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class CouponListActivity extends BaseActivity{

	private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;
    private XListView mXListView;
    private CouponAdapter mAdapter;
    private int mPageIndex = AppConfig.DEFAULT_START_PAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        initActionBar();
        initViews();
        httpRequestGetCoupons();
    }

    private void initActionBar() {
		mCommonActionBar = (CommonActionBar)findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle("我的优惠券");
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    }
    
    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 初始化
     */
    private void initViews() {
        mDataLoadingLayout = (DataLoadingLayout)findViewById(R.id.common_dl);
/*        mDataLoadingLayout.setOnReloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageIndex = AppConfig.DEFAULT_START_PAGE;
                httpRequestGetForemans(XListRefreshType.ON_PULL_REFRESH);
            }
        });*/


        mXListView = (XListView) findViewById(R.id.coupon_list_view);

        mXListView.setPullRefreshEnable(false);
        mXListView.setPullLoadEnable(false);
        mXListView.setAutoLoadMoreEnable(false);
        mXListView.setAutoRefreshEnable(false);
//        mXListView.setXListViewListener(new IXListViewListener() {
//
//            @Override
//            public void onRefresh() {
//                mPageIndex = AppConfig.DEFAULT_START_PAGE;
//                httpRequestGetForemans(XListRefreshType.ON_PULL_REFRESH);
//            }
//
//            @Override
//            public void onLoadMore() {
//                mPageIndex++;
//                httpRequestGetForemans(XListRefreshType.ON_LOAD_MORE);
//            }
//        });	
        mAdapter = new CouponAdapter(this);
        mXListView.setAdapter(mAdapter);
    }

   

    /**
     * http请求-获取优惠券
     * 
     * @param xListRefreshType
     */
    private void httpRequestGetCoupons() {
        
        GetCouponsTask task = new GetCouponsTask(this);
        task.setCallBack(new AbstractHttpResponseHandler<List<Coupon>>() {
			
			@Override
			public void onSuccess(List<Coupon> list) {
				// TODO Auto-generated method stub
				if(list!=null&&list.size()!=0){
					mAdapter.setList(list);
					mDataLoadingLayout.showDataLoadSuccess();
				}else{
					mDataLoadingLayout.showDataLoadEmpty("您还没有优惠券");
				}
				mDataLoadingLayout.showDataLoadSuccess();
			}
			
			@Override
			public void onFailure(int code, String error) {
				// TODO Auto-generated method stub
				mDataLoadingLayout.showDataLoadFailed(error);
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				mDataLoadingLayout.showDataLoading();
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}
		});
        task.send();
    }


}
