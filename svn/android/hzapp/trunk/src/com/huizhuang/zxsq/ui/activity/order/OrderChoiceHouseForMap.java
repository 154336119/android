package com.huizhuang.zxsq.ui.activity.order;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.common.HouseType;
import com.huizhuang.zxsq.http.bean.common.Housing;
import com.huizhuang.zxsq.http.bean.order.MapInfo;
import com.huizhuang.zxsq.ui.activity.SelectHousingActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.HouseInfoAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

@SuppressLint("ResourceAsColor")
public class OrderChoiceHouseForMap extends BaseActivity {
    private CommonActionBar mCommonActionBar;
//    private LinearLayout mRootView;
//    private MapView mMapView;
//    private BaiduMap mBaiduMap;
    private ListView mListView;
    private EditText mTvSearch;
    private boolean mAddrTextIsChange; //
//    private PopupWindow mPopupWindow;
    private HouseInfoAdapter mHouseInfoAdapter;
    private List<MapInfo> mMapInfoList = new ArrayList<MapInfo>();
    // 定位相关
    private LocationClient mLocClient = null;
//    private MyLocationListenner myListener;
    boolean isFirstLoc = true;// 是否首次定位
    // 兴趣点检索
    private PoiSearch mPoiSearch = PoiSearch.newInstance();
    // 地理编码
//    private GeoCoder mGeoCoder = GeoCoder.newInstance();
    private String mHouseName = "";
    private String mHouseDistrict = "";
    private String mHouseLat = "";
    private String mHouseLng = "";
    // 经纬度
    private double mLat;
    private double mlng;
    private String mCityName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choice_house_map);
        getIntentExtra();
        initActionBar();
        initViews();
        initLocation();

    }

    private void getIntentExtra() {
		// TODO Auto-generated method stub
    	mCityName = getIntent().getStringExtra(AppConstants.PARAM_CITY);
	}

	/**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle(R.string.txt_title_choise_house);
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/*        mCommonActionBar.setRightTxtBtn(R.string.ensure_ok, new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 传值
                backResult();
            }
        });*/
    }

    private void backResult(){
        Housing housing = new Housing();
        housing.setName(mHouseName);
        housing.setDistrict(mHouseDistrict);
        housing.setLat(mHouseLat);
        housing.setLon(mHouseLng);
        Bundle bd = new Bundle();
        bd.putSerializable(SelectHousingActivity.PARAM_HOUSING, housing);
        ActivityUtil.backWithResult(OrderChoiceHouseForMap.this, Activity.RESULT_OK, bd);
    }
    private void initViews() {
//        mRootView = (LinearLayout) findViewById(R.id.ll);
        mListView = (ListView) findViewById(R.id.list_house);
        mTvSearch = (EditText) findViewById(R.id.tv_search);
        mHouseInfoAdapter = new HouseInfoAdapter(this);
        mListView.setAdapter(mHouseInfoAdapter);
//        mGeoCoder.setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener);
        mPoiSearch.setOnGetPoiSearchResultListener(mPoiListener);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 设置当前选中
                mHouseName = mHouseInfoAdapter.getItem(position).getHouseName();
                mHouseDistrict = mHouseInfoAdapter.getItem(position).getRoadName();
                mHouseLat = mHouseInfoAdapter.getItem(position).getLat();
                mHouseLng = mHouseInfoAdapter.getItem(position).getLng();
                mHouseInfoAdapter.setCurPos(position);
                mHouseInfoAdapter.notifyDataSetChanged();
                backResult();
            }
        });
        mTvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	if(!mAddrTextIsChange){
            		Intent intent =
                            new Intent(OrderChoiceHouseForMap.this, SelectHousingActivity.class);
                    intent.putExtra(SelectHousingActivity.PARAM_KEY, s.toString());
                    intent.putExtra(SelectHousingActivity.PARAM_LAT, mLat);
                    intent.putExtra(SelectHousingActivity.PARAM_LNG, mlng);
                    intent.putExtra(AppConstants.PARAM_CITY, mCityName);
                    startActivityForResult(intent, OrderEditorInfoActivity.REQ_CODE_SELECT_HOUSING);	
                    mAddrTextIsChange = true;
            	}
                
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    /**
     * 初始化地图
     */
    public void initLocation() {
        LatLng userPoint = ZxsqApplication.getInstance().getUserPoint();
        if(TextUtils.isEmpty(mCityName)){
            mCityName = ZxsqApplication.getInstance().getLocationCity();
            if(TextUtils.isEmpty(mCityName)){
            	mCityName = "成都";
            }
        }
        if(userPoint != null &&!TextUtils.isEmpty(ZxsqApplication.getInstance().getLocationCity())){
        	toPoiSearch(mCityName,ZxsqApplication.getInstance().getLocationCity(),ZxsqApplication.getInstance().getUserPoint());
        }else{
         // 定位初始化
            mLocClient = ZxsqApplication.getInstance().getmLocationClient();
            MyLocationListenner myListener = new MyLocationListenner();
            mLocClient.registerLocationListener(myListener);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);// 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            mLocClient.setLocOption(option);
            mLocClient.start();
        }
    }

    /**
     * 若选择城市与定位城市一致，用searchNearby，否则使用searchInCity
     * @param nativeCity
     * @param locationCity
     */
    private void toPoiSearch(String selectCity,String locationCity,LatLng userPoint){
    	if(locationCity.replace("市", "").equals(selectCity)){
    		mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword("小区").keyword("小区")
            		.location(userPoint).radius(5*1000).pageNum(0).pageCapacity(50));
    	}else{
    		mPoiSearch.searchInCity(new PoiCitySearchOption() 
            .city(mCityName.replace("市", ""))  
            .keyword("小区").pageCapacity(50));
    	}
    }
    
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            mLocClient.stop();
            if (location == null) return;
//            MyLocationData locData =
//                    new MyLocationData.Builder().accuracy(location.getRadius())
//                            // 此处设置开发者获取到的方向信息，顺时针0-360
//                            .direction(100).latitude(location.getLatitude())
//                            .longitude(location.getLongitude()).build();
//            mBaiduMap.setMyLocationData(locData);
            mLat = location.getLatitude();
            mlng = location.getLongitude();
//            if (isFirstLoc) {
//                isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//                MapStatus mMapStatus = new MapStatus.Builder().target(ll).zoom(18).build();
//                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//                mBaiduMap.animateMapStatus(mMapStatusUpdate);
//            }
            LogUtil.e("2mLat:mlng"+mLat+""+mlng);
            // 开始检索
            toPoiSearch(mCityName,location.getCity(),new LatLng(location.getLatitude(), location.getLongitude()));
        }

        public void onReceivePoi(BDLocation poiLocation) {}
    }

    // 兴趣点回调函数
    OnGetPoiSearchResultListener mPoiListener = new OnGetPoiSearchResultListener() {
        public void onGetPoiResult(PoiResult result) {
            List<PoiInfo> poiList = result.getAllPoi();
            if (poiList != null && 0 != poiList.size()) {
                mMapInfoList.clear();
                for (int i = 0; i < poiList.size(); i++) {
                    if (poiList.get(i) != null) {
                        MapInfo mapInfo = new MapInfo();
                        mapInfo.setHouseName(poiList.get(i).name);
                        mapInfo.setRoadName(poiList.get(i).address);
                        mapInfo.setLat(String.valueOf(poiList.get(i).location.latitude));
                        mapInfo.setLng(String.valueOf(poiList.get(i).location.longitude));
                        LogUtil.e("poiList.get(i).postCode:"+poiList.get(i).postCode);
                        mMapInfoList.add(mapInfo);
                    }
                }
                if (mMapInfoList.get(0) != null) {
                    mHouseName = mMapInfoList.get(0).getHouseName();
                    mHouseDistrict = mMapInfoList.get(0).getRoadName();
                }
                mHouseInfoAdapter.setList(mMapInfoList);
                mHouseInfoAdapter.setCurPos(0);
            }
            // 获取POI检索结果
        }

        public void onGetPoiDetailResult(PoiDetailResult result) {
            // 获取Place详情页检索结果
        }
    };

//    OnGetGeoCoderResultListener mOnGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {
//        public void onGetGeoCodeResult(GeoCodeResult result) {
//            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                // 没有检索到结果
//            }
//            // 获取地理编码结果
//        }
//
//
//        @Override
//        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                // 没有找到检索结果
//            }
//            // 获取反向地理编码结果
//            System.out.println("========ReverseGeoCodeResult:" + result.getAddress());
//        }
//    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (OrderEditorInfoActivity.REQ_CODE_SELECT_HOUSING == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
            	if(data.getBooleanExtra(AppConstants.PARAM_IS_CANCLE, false)){
            		finish();
            	}else{
            		Housing housing =
                            (Housing) data.getSerializableExtra(SelectHousingActivity.PARAM_HOUSING);
                    ArrayList<HouseType> houseListType =
                            data.getParcelableArrayListExtra(SelectHousingActivity.PARAM_HOUSETYPE_LIST);
                    Bundle bd = new Bundle();
                    bd.putSerializable(SelectHousingActivity.PARAM_HOUSING, housing);
                    bd.putParcelableArrayList(SelectHousingActivity.PARAM_HOUSETYPE_LIST, houseListType);
                    ActivityUtil.backWithResult(OrderChoiceHouseForMap.this, Activity.RESULT_OK, bd);
            	}
            }
        }
    }
}
