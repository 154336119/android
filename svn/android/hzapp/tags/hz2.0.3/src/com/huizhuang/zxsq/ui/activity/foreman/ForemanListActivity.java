package com.huizhuang.zxsq.ui.activity.foreman;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.huizhuang.zxsq.http.task.foreman.ForemanListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.foreman.ForemanListAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class ForemanListActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

    // 地图视图
    private DataLoadingLayout mDataLoadingLayout;
    private XListView mXListView;
    private ForemanListAdapter mAdapter;
    private int mPageIndex = AppConfig.DEFAULT_START_PAGE;
    private LocationClient mLocClient = null;
    private LatLng mMyLatLng; // 我的地理位置
    //广播监听者
    private String mLatitude;
    private String mLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_foreman_list);
        initActionBar();
        initViews();
        LatLng latLng = ZxsqApplication.getInstance().getUserPoint();
        if(latLng == null){
            initLocation();
        }
    }

    private void initActionBar() {
        CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        commonActionBar.showRightTtile();
        commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new View.OnClickListener() {
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
        findViewById(R.id.btn_appointment).setOnClickListener(this);
        findViewById(R.id.btn_choice_foreman).setOnClickListener(this);
        mDataLoadingLayout = (DataLoadingLayout)findViewById(R.id.data_load_layout);
        mDataLoadingLayout.setOnReloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageIndex = AppConfig.DEFAULT_START_PAGE;
                httpRequestGetForemans(XListRefreshType.ON_PULL_REFRESH);
            }
        });


        mXListView = (XListView) findViewById(R.id.xlist);
        mXListView.setOnItemClickListener(this);

        mXListView.setPullRefreshEnable(true);
        mXListView.setPullLoadEnable(true);
        mXListView.setAutoLoadMoreEnable(true);
        mXListView.setAutoRefreshEnable(true);
        mXListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                mPageIndex = AppConfig.DEFAULT_START_PAGE;
                httpRequestGetForemans(XListRefreshType.ON_PULL_REFRESH);
            }

            @Override
            public void onLoadMore() {
                mPageIndex++;
                httpRequestGetForemans(XListRefreshType.ON_LOAD_MORE);
            }
        });	
        mAdapter = new ForemanListAdapter(this);
        mXListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_appointment: // 立即预约
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0072);
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.PARAM_ORDER_SOURCE_NAME, "app_index");
                ActivityUtil.checkAppointmentToJump(ForemanListActivity.this,bundle);
                break;
            case R.id.btn_choice_foreman://地图模式
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0006);
                finish();
                break;
            default:
                break;
        }
    }


    /**
     * http请求-获取工长数据
     * 
     * @param xListRefreshType
     */
    private void httpRequestGetForemans(final XListRefreshType xListRefreshType) {
        String cityName = ZxsqApplication.getInstance().getSelectCity();
        if(TextUtils.isEmpty(cityName)){
            cityName = ZxsqApplication.getInstance().getLocationCity();
        }
        String city_id = getCityId(cityName);
        LatLng latLng = ZxsqApplication.getInstance().getUserPoint();
        if(latLng == null){
            latLng = mMyLatLng;
        }
        if(latLng != null){
            mLatitude = latLng.latitude+"";
            mLongitude = latLng.longitude+"";
        }
        ForemanListTask task =
                new ForemanListTask(this, city_id, mLatitude, mLongitude, mPageIndex);
        task.setCallBack(new AbstractHttpResponseHandler<ForemanList>() {

            @Override
            public void onSuccess(ForemanList result) {
                mDataLoadingLayout.showDataLoadSuccess();

                if (null != result && result.getList() != null) {
                    // 加载更多还是刷新
                    if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        mAdapter.setList(result.getList());
                        if (result.getList().size() == 0) {
                            mDataLoadingLayout.showDataLoadEmpty("抱歉，您周围暂无工长为您服务");
                        }
                    } else {
                        mAdapter.addList(result.getList());
                    }
                    if (mPageIndex >= result.getTotalpage()) {
                        mXListView.setPullLoadEnable(false);
                    } else {
                        mXListView.setPullLoadEnable(true);
                    }
                }
            }

            @Override
            public void onFailure(int code, String error) {
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType
                        && 0 == mAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoadFailed(error);
                } else {
                    mPageIndex--;
                    if(code == 0){
                        showToastMsg("请检查网络状况");
                    }else{
                        showToastMsg(error);
                    }
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                // 没有数据时下拉列表刷新才显示加载等待视图
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType
                        && 0 == mAdapter.getCount()) {
                    mDataLoadingLayout.showDataLoading();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                    mXListView.onRefreshComplete();
                } else {
                    mXListView.onLoadMoreComplete();
                }
            }

        });
        task.send();
    }

    private String getCityId(String cityName) {
        if (TextUtils.isEmpty(cityName)) {
            return null;
        }
        ProviceCityArea city = ZxsqApplication.getInstance().getCity(cityName.replace("市", ""));
        if (city != null) {
            return city.getArea_id() + "";
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (position >= mXListView.getHeaderViewsCount()) {
            Foreman foreman = mAdapter.getList().get(position - 1);
            Bundle bd = new Bundle();
            bd.putString(AppConstants.PARAM_ORDER_COMPANY_ID, foreman.getStore_id());
            bd.putString("latitude", mLatitude);
            bd.putString("longitude", mLongitude);
            bd.putString("foreman_name", foreman.getFull_name());
            ActivityUtil.next(this, ForemanDetailsActivity.class, bd, -1);

        }
    }

    /**
     * 初始化-定位相关
     */
    private void initLocation() {
        // 定位初始化
        mLocClient = ZxsqApplication.getInstance().getmLocationClient();
        MyLocationListenner myListener = new MyLocationListenner();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 接口-定位监听器
     * 
     * @author jean
     * 
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            mLocClient.stop();
            if (location == null) return;
            mMyLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            ZxsqApplication.getInstance().setmProvince(location.getProvince());
            if (location.getCity() != null) {
                String locationCity = location.getCity().replace("市", "");
                ZxsqApplication.getInstance().setLocationCity(locationCity);
            }
            mXListView.onRefresh();
        }

        public void onReceivePoi(BDLocation poiLocation) {}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

	
	@Override
	protected void onStart() {
		super.onStart();
		System.err.println("========onStart:");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
