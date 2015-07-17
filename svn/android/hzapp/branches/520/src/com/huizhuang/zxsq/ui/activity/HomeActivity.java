package com.huizhuang.zxsq.ui.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.CityOpen;
import com.huizhuang.zxsq.http.bean.ProviceCityArea;
import com.huizhuang.zxsq.http.bean.Share;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.http.bean.foreman.Foreman;
import com.huizhuang.zxsq.http.bean.foreman.ForemanList;
import com.huizhuang.zxsq.http.bean.solution.HouseSolution;
import com.huizhuang.zxsq.http.task.account.GetUserInfoTask;
import com.huizhuang.zxsq.http.task.foreman.ForemanListTask;
import com.huizhuang.zxsq.http.task.solution.HouseSolutionTask;
import com.huizhuang.zxsq.receiver.NetChangeObserver;
import com.huizhuang.zxsq.receiver.NetworkStateReceiver;
import com.huizhuang.zxsq.ui.activity.account.AccountDataEditorActivity;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.account.SettingActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.company.CompanyCityList;
import com.huizhuang.zxsq.ui.activity.foreman.ForemanDetailsActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderCancelAppointmentReasonListActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderShowPayActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderWaitResponseActivity;
import com.huizhuang.zxsq.ui.activity.solution.SolutionDetailActivity;
import com.huizhuang.zxsq.ui.activity.solution.SolutionListActivity;
import com.huizhuang.zxsq.ui.activity.user.UserMessageLoginActivity;
import com.huizhuang.zxsq.ui.adapter.foreman.ForemanListAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.utils.ScreenShotUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;
import com.huizhuang.zxsq.widget.dialog.LoadingDialog;
import com.huizhuang.zxsq.widget.slidingmenu.SlidingMenu;
import com.huizhuang.zxsq.widget.slidingmenu.SlidingMenu.OnMenuClose;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class HomeActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

    // 城市列表返回
    private static final int CITY_LIST = 33545;
    private static final int USER_LOGIN = 33547;
    private static final int EDIT_USER_INFO = 33546;
	private final int DELAYED_TIME_10 = 10000;//
    private final int DELAYED_TIME_5 = 5000;//1
    public static final String ROB_ORDER_NOW= "rob_order_now";
    private String mRobOrderId;
    private SlidingMenu mMenu;
    private CircleImageView mBtnLeft;
    private TextView mTvChoiceCity;
    private MapView mMapView;
    private ImageView mIvShadow;
    private Button mBtnChoice;
    private LinearLayout mLlGuarantee;
    private View mMapViewStub;
    private View mForemanViewStub;
    private LinearLayout mLlTitle;
    private boolean mIsRobNow;
    private RelativeLayout mRobOrderActionBar;
    private RelativeLayout mCommonActionBar;
    private Button mBtnAppointment;
    private ImageView mIvOrderPoint;
    // 地图视图
    private Button mBtnMyLocation;
    private DataLoadingLayout mDataLoadingLayout;
    private XListView mXListView;
    private ForemanListAdapter mAdapter;
    private int mPageIndex = AppConfig.DEFAULT_START_PAGE;
    // 定位相关
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient = null;
    private MyLocationListenner myListener;
    boolean isFirstLoc = true;// 是否首次定位
    private LatLng mMyLatLng; // 我的地理位置
    // 地理编码
    private GeoCoder mGeoCoder;
    // 施工缩略图
    private InfoWindow mInfoWindow;
    private CommonAlertDialog mCommonAlertDialog;
    private HashMap<LatLng, HouseSolution> mHouseMap;
    private HouseSolution mPopupHouseSolution; //弹出的气泡的数据
    private boolean mInfoWindowIsShow;
    // 个人中心相关
    private RelativeLayout mRlHeader;
    private CircleImageView mCiHead;
    private TextView mTvName;
    private TextView mTvProCity;
    private RelativeLayout mRlMyOrder;
    private RelativeLayout mRlMyWallet;
    private RelativeLayout mRlMyMessage;
    private RelativeLayout mRlMyGift;
    private RelativeLayout mRlMySetting;
    private ImageView mIvHomeOrderPoint;
    private LoadingDialog mRobOrderDialog;
    private NetChangeObserver mNetChangeObserver;
    //广播监听者
    private HomeBroadcastReceiver mHomeBroadcastReceiver;
    private String mLatitude;
    private String mLongitude;
    private Handler mHandler = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		// TODO Auto-generated method stub
    		super.handleMessage(msg);
    		switch (msg.what) {
			case DELAYED_TIME_10:
				hideRobOrderView();
				Bundle bundle = new Bundle();
				bundle.putString(AppConstants.PARAM_ORDER_ID, mRobOrderId);
				ActivityUtil.next(HomeActivity.this, OrderWaitResponseActivity.class,bundle,false);
				break;
			case DELAYED_TIME_5:
				hideRobOrderView();
				Message message = new Message();
				message.what = DELAYED_TIME_10;
				showRobOrderView("小惠正在努力为您筛选中...");
				mHandler.sendMessageDelayed(message, DELAYED_TIME_10);
				break;
			default:
				break;
			}
    	}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_home);
        AutoPopGuarantee();
        initViews();
        initHomeBroadcastReceiver();
        initBaiduMap();
        initLocation();
        initGeocoder();
        initBroadcastReceiver();
       // showRobOrderView("小惠正在努力为您筛选中...");
        if(ZxsqApplication.getInstance().isLogged()){
            httpRequestQueryUserProfile();
        }
    }
    
    private void initBroadcastReceiver() {
    	mNetChangeObserver = new NetChangeObserver();
    	//注册网络的观察者
    	NetworkStateReceiver.registerObserver(new NetChangeObserver(){
    		@Override
    		public void onConnect(NetType type) {
    			if(ZxsqApplication.getInstance().getLocationCity()==null){
    				isFirstLoc = true;
    				mLocClient.start();
    			}
    			//initBaiduMap();
    			//mLocClient.start();
    			mMapView.onResume();
    			//ZxsqApplication.getInstance().initLocation();
//    			if(mMyLatLng!=null){
//    				httpRequestGetHouseList(mMyLatLng);
//    			}
    		}
    	});
    	//发送广播
    	NetworkStateReceiver.registerNetworkStateReceiver(this);
	}
    
	@Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    //	getIntentExtras();
    }
    @Override
    protected void onResume() {
        super.onResume();
//        initViews();
        //mLocClient.start();
        mMapView.onResume();
        if (mMapViewStub.getVisibility() == View.VISIBLE) {
            mBtnChoice.setText("我要选工长");
            mTvChoiceCity.setVisibility(View.VISIBLE);
        } else {
            mBtnChoice.setText("地图模式");
            mTvChoiceCity.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
    	// TODO Auto-generated method stub
    	super.onNewIntent(intent);
    	if(intent.getStringExtra(ROB_ORDER_NOW)!=null && intent.getStringExtra(AppConstants.PARAM_ORDER_ID)!=null){
    		mRobOrderId = intent.getStringExtra(AppConstants.PARAM_ORDER_ID);
            mMapViewStub.setVisibility(View.VISIBLE);
            if(mForemanViewStub != null){
            	mForemanViewStub.setVisibility(View.GONE);
            }
			mIsRobNow = true;
			showRobOrderView("3名工长抢单中...");
			Message msg = new Message();
			msg.what = DELAYED_TIME_5;
			mHandler.sendMessageDelayed(msg, DELAYED_TIME_5);
		}
    }
    /**
     * 初始化-地理编码（地理名称转化经纬度）
     */
    private void initGeocoder() {
        // TODO Auto-generated method stub
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new MyOnGetGeoCoderResultListener());
    }

    /**
     * 初始化-地图控件相关
     */
    private void AutoPopGuarantee() {
    	boolean need = PreferenceConfig.getNeedAutoPopGuarantee();
		if(need){
			ActivityUtil.next(THIS, GuaranteeActivity.class);
			PreferenceConfig.setNeedAutoPopGuarantee(false);
		}
	}
    /**
     * 初始化-地图控制器
     */
    private void initBaiduMap() {
        // TODO Auto-generated method stub
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapClickListener(new MyOnMapClickListener());
        mBaiduMap.setOnMarkerClickListener(new MyMarkerClickListener());
        mBaiduMap.setOnMapStatusChangeListener(new MyOnMapStatusChangeListener());
    }


    /**
     * 初始化-控件
     */
    private void initViews() {
        // TODO Auto-generated method stub
        mMenu = (SlidingMenu) findViewById(R.id.slidingmenu);
        mBtnLeft = (CircleImageView) findViewById(R.id.img_btn_left);
        mTvChoiceCity = (TextView) findViewById(R.id.tv_city);
        mIvShadow = (ImageView) findViewById(R.id.iv_shadow);
        mBtnChoice = (Button) findViewById(R.id.btn_choice_foreman);
        mCommonActionBar = (RelativeLayout) findViewById(R.id.common_action_bar);
        mRobOrderActionBar = (RelativeLayout) findViewById(R.id.rob_order_action_bar);
        mBtnAppointment = (Button) findViewById(R.id.btn_appointment);
        mIvHomeOrderPoint = (ImageView) findViewById(R.id.iv_home_order_point);
        initMapStubView();
        initAccountView();
        if(ZxsqApplication.getInstance().getUser()!=null&&ZxsqApplication.getInstance().getUser().getUser_thumb()!=null
        		&&ZxsqApplication.getInstance().getUser().getUser_thumb().getThumb_path()!=null){
        	String uri = ZxsqApplication.getInstance().getUser().getUser_thumb().getThumb_path();
        	ImageLoader.getInstance().displayImage(uri, mBtnLeft, ImageLoaderOptions.optionsUserHeader);
        }
        if (!TextUtils.isEmpty(ZxsqApplication.getInstance().getLocationCity())) {
            mTvChoiceCity.setText(ZxsqApplication.getInstance().getLocationCity());
        }
        findViewById(R.id.img_btn_left).setOnClickListener(this);
        findViewById(R.id.iv_shadow).setOnClickListener(this);
        findViewById(R.id.btn_choice_foreman).setOnClickListener(this);
        mBtnAppointment.setOnClickListener(this);
        mLlGuarantee.setOnClickListener(this);
        mTvChoiceCity.setOnClickListener(this);
        mMenu.setOnMenuClose(new OnMenuClose() {

            @Override
            public void close() {
                // TODO Auto-generated method stub
                mIvShadow.setVisibility(View.INVISIBLE);
                mMapView.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     * 初始化-个人中心视图
     */
    private void initAccountView() {
    	mRlHeader = (RelativeLayout) findViewById(R.id.rl_header);
        mCiHead = (CircleImageView) findViewById(R.id.ci_head);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvProCity = (TextView) findViewById(R.id.tv_pro_city);
        mIvOrderPoint = (ImageView) findViewById(R.id.iv_order_point);
        mRlMyOrder = (RelativeLayout) findViewById(R.id.rl_my_order);
        mRlMyWallet = (RelativeLayout) findViewById(R.id.rl_my_wallet);
        mRlMyMessage = (RelativeLayout) findViewById(R.id.rl_my_message);
        mRlMyGift = (RelativeLayout) findViewById(R.id.rl_my_gift);
        mRlMySetting = (RelativeLayout) findViewById(R.id.rl_my_setting);
        mRlMyOrder
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_account_selector));
        mRlMyWallet.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.bg_account_selector));
        mRlMyMessage.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.bg_account_selector));
        mRlMyGift
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_account_selector));
        mRlMySetting.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.bg_account_selector));
        if(ZxsqApplication.getInstance().isLogged()){
        	if(null!=ZxsqApplication.getInstance().getUser().getAvatar()){
                ImageUtil.displayImage(ZxsqApplication.getInstance().getUser().getAvatar(), mCiHead, ImageLoaderOptions.optionsUserHeader);
        	}
        }
        mRlMyOrder.setOnClickListener(this);
        mRlMyGift.setOnClickListener(this);
        mRlMySetting.setOnClickListener(this);
        mRlHeader.setOnClickListener(this);
    }

    /**
     * 初始化-地图视图
     */
    private void initMapStubView() {
        ViewStub stub = (ViewStub) findViewById(R.id.viewstub_import_home);
        mMapViewStub = stub.inflate();
        mMapView = (MapView) mMapViewStub.findViewById(R.id.bmapView);
        mLlGuarantee = (LinearLayout) mMapViewStub.findViewById(R.id.ll_home_guarantee);
        mBtnMyLocation = (Button) mMapViewStub.findViewById(R.id.btn_my_location);
        mBtnMyLocation.setOnClickListener(this);
        mMapView.showZoomControls(false);
        
        
    }

    /**
     * 初始化-工长列表视图
     */
    private void initForemanStubView() {
        ViewStub stub = (ViewStub) findViewById(R.id.viewstub_import_foreman);
        mForemanViewStub = stub.inflate();
        mDataLoadingLayout =
                (DataLoadingLayout) mForemanViewStub.findViewById(R.id.data_load_layout);
        mDataLoadingLayout.setOnReloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageIndex = AppConfig.DEFAULT_START_PAGE;
                httpRequestGetForemans(XListRefreshType.ON_PULL_REFRESH);
            }
        });


        mXListView = (XListView) mForemanViewStub.findViewById(R.id.xlist);
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
        mForemanViewStub.setVisibility(View.GONE);
        mMapViewStub.setVisibility(View.VISIBLE);
//      mPageIndex = AppConfig.DEFAULT_START_PAGE;
//      httpRequestGetForemans(XListRefreshType.ON_PULL_REFRESH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_btn_left: //切换到个人中心或登录页面
            	if(!ZxsqApplication.getInstance().isLogged()){     
            		ActivityUtil.next(this, UserMessageLoginActivity.class, null, USER_LOGIN);
            	}else{
            	    httpRequestQueryUserProfile();
            		 if (!mMenu.getIsOpen()) {
                         mIvShadow.setImageBitmap(ScreenShotUtil.takeScreenShot(HomeActivity.this));
                         mIvShadow.setVisibility(View.VISIBLE);
                         mMapView.setVisibility(View.GONE);
                         mMenu.toggle();
                     } else {
                         mMenu.toggle(); 
                         mIvShadow.setVisibility(View.GONE);
                         mMapView.setVisibility(View.VISIBLE);
                     }
            	}
                break;
            case R.id.iv_shadow: // 切换到首页
                if (!mMenu.getIsOpen()) {
                    mIvShadow.setImageBitmap(ScreenShotUtil.takeScreenShot(HomeActivity.this));
                    mIvShadow.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.GONE);
                    mMenu.toggle();
                } else {
                    mMenu.toggle();
                    mIvShadow.setVisibility(View.INVISIBLE);
                    mMapView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_choice_foreman:// 选择工长按钮切换
                if(mForemanViewStub == null){
                    initForemanStubView();
                }
                if (mMapViewStub.getVisibility() == View.VISIBLE) {
                    AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0006);
                    mBtnChoice.setText("地图模式");
                    mMapViewStub.setVisibility(View.GONE);
                    mForemanViewStub.setVisibility(View.VISIBLE);
                    mTvChoiceCity.setVisibility(View.INVISIBLE);
                } else {
                    AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0014);
                    mBtnChoice.setText("我要选工长");
                    mForemanViewStub.setVisibility(View.GONE);
                    mMapViewStub.setVisibility(View.VISIBLE);
                    mTvChoiceCity.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_my_location: // 回到定位位置
                MapStatus mMapStatus = new MapStatus.Builder().target(mMyLatLng).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.animateMapStatus(mMapStatusUpdate);
                if(ZxsqApplication.getInstance().getLocationCity()!=null){
                	mTvChoiceCity.setText(ZxsqApplication.getInstance().getLocationCity());
                }
                break;
            case R.id.tv_city:
                toCityList();
                break;
            case R.id.btn_appointment:
            	//ActivityUtil.next(this, OrderShowPayActivity.class);
        		//ActivityUtil.next(this, CostChangeActivity.class);
            	
//            	Bundle bundle = new Bundle();
//            	bundle.putString(AppConstants.PARAM_ORDER_ID, "13270");
//            	bundle.putString(AppConstants.PARAM_NODE_ID, "1");
//        		ActivityUtil.next(this, OrderShowPayActivity.class,bundle,false);
            	
         //   	ActivityUtil.next(this, DelayOrderActivity.class);
            	ActivityUtil.checkAppointmentToJump(HomeActivity.this,null);
            	break;                
            case R.id.rl_my_gift:
            	PreferenceConfig.setShareByOrderState(AppConstants.SHARE_BY_ORDER_COMMON);
            	Share share = new Share();
				Util.showShare(false, HomeActivity.this,share);
                break;
            case R.id.rl_my_order:
            	ActivityUtil.next(HomeActivity.this, MyOrderActivity.class);
                break;
            case R.id.rl_my_setting:
            	ActivityUtil.next(HomeActivity.this, SettingActivity.class);
                break;
            case R.id.ll_home_guarantee:
    			ActivityUtil.next(HomeActivity.this, GuaranteeActivity.class);
            	//ActivityUtil.next(HomeActivity.this,OrderStartWorkingActivity.class);
            	//ActivityUtil.next(HomeActivity.this,OrderWaitResponseActivity.class);
            	//ActivityUtil.next(HomeActivity.this,OrderServeStationSignActivity.class);
            	//ActivityUtil.next(HomeActivity.this, MyOrderActivity.class);
            	break;
            case R.id.rl_header:
            	Intent intent = new Intent();
            	ActivityUtil.next(HomeActivity.this,AccountDataEditorActivity.class, null, EDIT_USER_INFO);
                break;
            default:
                break;
        }
    }
    /**
     * http请求-获取小姑工地地理位置列表
     * 
     * @param xListRefreshType
     */
    private void httpRequestGetHouseList(LatLng latLng){
    	if(latLng!=null){
    		HouseSolutionTask task = new HouseSolutionTask(this, "", String.valueOf(latLng.longitude), String.valueOf(latLng.latitude));
    		task.setCallBack(new AbstractHttpResponseHandler<List<HouseSolution>>() {
				
				@Override
				public void onSuccess(List<HouseSolution> list) {
					// TODO Auto-generated method stub
					if(list!=null&&list.size()>0){
						mHouseMap = new HashMap<LatLng, HouseSolution>();
						mBaiduMap.clear();
						for(int i=0;i<list.size();i++){
							HouseSolution houseSolution = list.get(i);
							System.out.println("-------houseSolution:"+houseSolution.getName());
							LatLng latLng = new LatLng(Double.valueOf(houseSolution.getLat()), Double.valueOf(houseSolution.getLon()));
							addMarker(latLng);
							mHouseMap.put(latLng, houseSolution);
						}
						addMyMarker();
					}
				}
				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					showToastMsg(error);
				}
			});
    		task.send();
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
            Foreman foreman = mAdapter.getList().get(position - 1);;
            Bundle bd = new Bundle();
            bd.putString(AppConstants.PARAM_ORDER_COMPANY_ID, foreman.getStore_id());
            bd.putString("latitude", mLatitude);
            bd.putString("longitude", mLongitude);
            ActivityUtil.next(this, ForemanDetailsActivity.class, bd, -1);

        }
    }

    /**
     * 初始化-定位相关
     */
    public void initLocation() {
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
            if (location == null || mMapView == null) return;
            MyLocationData locData =
                    new MyLocationData.Builder().accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(100).latitude(location.getLatitude())
                            .longitude(location.getLongitude()).build();
//            mBaiduMap.setMyLocationEnabled(true);
//            mBaiduMap.setMyLocationData(locData);
              System.out.println("=============:"+location.getLongitude());
              System.out.println("=============:"+location.getLatitude());
              System.out.println("=============:"+location.getCity());
            if (isFirstLoc) { //测试
                isFirstLoc = false;
                mMyLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                addMyMarker();
                MapStatus mMapStatus = new MapStatus.Builder().target(mMyLatLng).zoom(15).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.animateMapStatus(mMapStatusUpdate);
               // httpRequestGetHouseList(mMyLatLng);
            }
            ZxsqApplication.getInstance().setmProvince(location.getProvince());
            if (location.getCity() != null) {
                String locationCity = location.getCity().replace("市", "");
                ZxsqApplication.getInstance().setLocationCity(locationCity);
                hideChoice(locationCity);
            }
               //httpRequestGetHouseList(mMyLatLng);
        }

        public void onReceivePoi(BDLocation poiLocation) {}
    }
    /**
     * 初始化-定位相关
     */
    public class MyMarkerClickListener implements OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(final Marker arg0) {
        	MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newLatLng(arg0.getPosition());
        	mBaiduMap.animateMapStatus(statusUpdate);
        	final View view =
                    LayoutInflater.from(HomeActivity.this).inflate(R.layout.view_map_info_woindow,
                            null);
        	Animation anim=AnimationUtils.loadAnimation(HomeActivity.this, R.anim.pop_show);
        	view.setAnimation(anim);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mPopupHouseSolution = mHouseMap.get(arg0.getPosition());
            final ImageView ivMarkerIcon = (ImageView) view.findViewById(R.id.tv_marker_icon);
            TextView tvMarkerName = (TextView) view.findViewById(R.id.tv_marker_name);
            TextView tvMarkerNum = (TextView) view.findViewById(R.id.tv_marker_num);
            if(mPopupHouseSolution!=null){
            	mInfoWindowIsShow = true;
	            if(null!=mPopupHouseSolution.getImage()){
	                ImageLoader.getInstance().displayImage(mPopupHouseSolution.getImage().getThumb_path(), ivMarkerIcon, ImageLoaderOptions.getDefaultImageOption(),new ImageLoadingListener() {
                        
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            LogUtil.e("onLoadingStarted", "onLoadingStarted");
                        }
                        
                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            LogUtil.e("onLoadingFailed", "onLoadingFailed");
                        }
                        
                        @Override
                        public void onLoadingComplete(String imageUri, View view01, Bitmap loadedImage) {
                            LogUtil.e("onLoadingComplete", "onLoadingComplete");
                            ivMarkerIcon.setImageBitmap(loadedImage);
            	        //    mBaiduMap.showInfoWindow(mInfoWindow);
	            	            mInfoWindow =new InfoWindow(BitmapDescriptorFactory.fromView(view), arg0.getPosition(), -DensityUtil.dip2px(getApplicationContext(), 45),new MyonInfoWindowClick());
	            	            mBaiduMap.showInfoWindow(mInfoWindow);
	            	     //       mInfoWindowIsFristShow = false;
                            
                        }
                        
                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            LogUtil.e("onLoadingCancelled", "onLoadingCancelled");
                        }
                    });
	            }
	            tvMarkerName.setText(mPopupHouseSolution.getName());
	            tvMarkerNum.setText("施工现场"+mPopupHouseSolution.getCase_count()+"个");
	            Point p =  mBaiduMap.getProjection().toScreenLocation(mMyLatLng);
	            
//	            PopupWindow popupWindow = new PopupWindow(view,LayoutParams.WRAP_CONTENT,
//	                    LayoutParams.WRAP_CONTENT, false);
//	            popupWindow.setOutsideTouchable(true);
//	            System.err.println("====UiUtil.getScreenHeight(HomeActivity.this)/2:"+UiUtil.getScreenHeight(HomeActivity.this)/2);
//	            System.err.println("====p.y"+p.y);
//	            popupWindow.showAtLocation(view, Gravity.CENTER, 0, UiUtil.getScreenHeight(HomeActivity.this)/2-p.y);
	         //   Point p = mBaiduMap.getProjection().toScreenLocation(popupPoint);
	            mInfoWindow =new InfoWindow(BitmapDescriptorFactory.fromView(view), arg0.getPosition(), -DensityUtil.dip2px(getApplicationContext(), 45),new MyonInfoWindowClick());
	            mBaiduMap.showInfoWindow(mInfoWindow);
            }
            return true;
        }


    }
    /**
     * 接口-地理编码监听器
     * 
     */
    public class MyOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult arg0) {
            MapStatus mMapStatus = new MapStatus.Builder().target(arg0.getLocation()).build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.animateMapStatus(mMapStatusUpdate);
            httpRequestGetHouseList(arg0.getLocation());
        }
        
        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {

        }

    }

    /**
     * 接口-InfoWindow浮层点击监听器
     * 
     */
    public class MyonInfoWindowClick implements OnInfoWindowClickListener {

        @Override
        public void onInfoWindowClick() {
            Bundle bd = new Bundle();
            // 跳转到施工现场模块
            if(mPopupHouseSolution.getCase_count() > 0){
            	if(mPopupHouseSolution.getCase_count() == 1){
            	    bd.putString(SolutionDetailActivity.PARAME_SHOW_CASE_ID_KEY, mPopupHouseSolution.getShowcase_id()+"");
            	    ActivityUtil.next(HomeActivity.this, SolutionDetailActivity.class, bd, -1);
            	}else{
            	    bd.putString(SolutionListActivity.PARAME_HOUSE_ID, mPopupHouseSolution.getId()+"");
            	    ActivityUtil.next(HomeActivity.this, SolutionListActivity.class, bd, -1);
            	}
            }
        }
    }
    /**
     * 接口-地图图层点击监听器
     * 
     */
    public class MyOnMapClickListener implements OnMapClickListener {

        @Override
        public void onMapClick(LatLng arg0) {
            // TODO Auto-generated method stub
            mBaiduMap.hideInfoWindow();
            mInfoWindowIsShow = false;
        }

        @Override
        public boolean onMapPoiClick(MapPoi arg0) {
            // TODO Auto-generated method stub
            return false;
        }
    }
    /**
     * 接口-地图移动监听器
     * 
     */    
    public class MyOnMapStatusChangeListener implements OnMapStatusChangeListener{

		@Override
		public void onMapStatusChange(MapStatus arg0) {}

		@Override
		public void onMapStatusChangeFinish(MapStatus arg0) {
			if(!mInfoWindowIsShow){
				httpRequestGetHouseList(arg0.target);
			}
		}

		@Override
		public void onMapStatusChangeStart(MapStatus arg0) {}}     
    /**
     * 添加坐标图标到百度view
     * @param lng
     * @param lat
     */
    public void addMarker(LatLng latlng) {
        if (latlng != null) {
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.ic_home_marker);
            MarkerOptions markerOptions = new MarkerOptions().icon(bd).position(latlng);
            mBaiduMap.addOverlay(markerOptions);
        }
    }

    /**
     * 添加我的坐标图标到百度view
     * @param lng
     * @param lat
     */
    public void addMyMarker() {
        if (mMyLatLng != null) {
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
            MarkerOptions markerOptions = new MarkerOptions().icon(bd).position(mMyLatLng);
            mBaiduMap.addOverlay(markerOptions);
        }
    }
    @Override
    public void onBackPressed() {
        LogUtil.d("onBackPressed");

        showExitAlertDialog();
    }

    /**
     * 显示退出提示对话框
     */
    private void showExitAlertDialog() {
        LogUtil.d("showExitAlertDialog");

        if (null == mCommonAlertDialog) {
            mCommonAlertDialog = new CommonAlertDialog(this);
            mCommonAlertDialog.setTitle(R.string.txt_quit_app);
            mCommonAlertDialog.setMessage(R.string.txt_are_you_sure_to_quit);
            mCommonAlertDialog.setPositiveButton(R.string.txt_quit, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                	mCommonAlertDialog.dismiss();
                    ZxsqApplication.getInstance().exit();
                }
            });
            mCommonAlertDialog.setNegativeButton(R.string.txt_cancel, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommonAlertDialog.dismiss();
                }
            });
        }
        mCommonAlertDialog.show();
    }

    /**
     * 跳转到城市列表
     */
    private void toCityList() {
        String city = ZxsqApplication.getInstance().getLocationCity();
        if (city != null) {
            city = city.replace("市", "");
        }
        Intent intent = new Intent(this, CompanyCityList.class);
        intent.putExtra("myCity", city);
        startActivityForResult(intent, CITY_LIST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CITY_LIST == requestCode && resultCode == Activity.RESULT_OK && data != null) {
            // 切换城市
            String city = data.getStringExtra("city");
            ZxsqApplication.getInstance().setSelectCity(city);
            if (city == null) {
                city = ZxsqApplication.getInstance().getLocationCity();
            }
            mTvChoiceCity.setText(city);
            mGeoCoder.geocode(new GeoCodeOption().city(city).address(city));
            hideChoice(city);
            if(mForemanViewStub != null && mBtnChoice.getVisibility() == View.VISIBLE){
                mPageIndex = AppConfig.DEFAULT_START_PAGE;
                httpRequestGetForemans(XListRefreshType.ON_PULL_REFRESH);
            }
        }else if(EDIT_USER_INFO == requestCode && resultCode == Activity.RESULT_OK ){
        	httpRequestQueryUserProfile();
        }else if(USER_LOGIN == requestCode && resultCode == Activity.RESULT_OK ){
        	httpRequestQueryUserProfile();
        }
    }
    
    private void hideChoice(String cityName){
        mBtnChoice.setVisibility(View.GONE);
        List<CityOpen> cityOpens = ZxsqApplication.getInstance().getmCityOpens();
        if(cityOpens != null && cityOpens.size() > 0){
            for (CityOpen cityOpen : cityOpens) {
                if(cityOpen.getName().replace("市", "").equals(cityName.replace("市", ""))){
                    mBtnChoice.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    }
    /**
     * 显示抢单加载框
     */
    private void showRobOrderView(String msg) {
    	mCommonActionBar.setVisibility(View.INVISIBLE);
    	mRobOrderActionBar.setVisibility(View.VISIBLE);
		if (mRobOrderDialog == null) {
			mRobOrderDialog = new LoadingDialog(this);
			mRobOrderDialog.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if(keyCode ==KeyEvent.KEYCODE_BACK){
						return true;
					}
					return false;
				}
			});
		}
		mRobOrderDialog.setText(msg);
		if (!mRobOrderDialog.isShowing()) {
			mRobOrderDialog.show();
		}
		mRobOrderDialog.setOnRightBtnListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideRobOrderView();//隐藏等待加载框
				mHandler.removeMessages(DELAYED_TIME_10);
				mHandler.removeMessages(DELAYED_TIME_5);
				ActivityUtil.next(HomeActivity.this, OrderCancelAppointmentReasonListActivity.class);
				System.out.println("=======mRobOrderDialog");
			}
		});
    }
	/** 隐藏加载等待框 */
	public void hideRobOrderView() {
    	mCommonActionBar.setVisibility(View.VISIBLE);
    	mRobOrderActionBar.setVisibility(View.INVISIBLE);
		if (mRobOrderDialog != null && mRobOrderDialog.isShowing()) {
			mRobOrderDialog.dismiss();
		}
	}
    /**
     * http请求-个人资料
     * 
     */	
   private void httpRequestQueryUserProfile() {
        GetUserInfoTask task = new GetUserInfoTask(HomeActivity.this);
        task.setCallBack(new AbstractHttpResponseHandler<User>() {

            @Override
            public void onSuccess(User result) {
                initAccountViewData(result);
            }

            @Override
            public void onFailure(int code, String error) {
                showToastMsg(error);
            }

            @Override
            public void onStart() {
                super.onStart();
                
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });
        task.send();
    }

	protected void initAccountViewData(User result) {
		// TODO Auto-generated method stub
		String token = ZxsqApplication.getInstance().getUser().getToken(); //请求个人数据无token所以自己保存了一个
		result.setToken(token);
		ZxsqApplication.getInstance().setUser(result);
		if(TextUtils.isEmpty(result.getMobile())){
		    ZxsqApplication.getInstance().getUser().setMobile(PrefersUtil.getInstance().getStrValue("mobile"));
		}
    	if(null!=ZxsqApplication.getInstance().getUser().getUser_thumb()&&null!=ZxsqApplication.getInstance().getUser().getUser_thumb().getThumb_path()){
            ImageUtil.displayImage(ZxsqApplication.getInstance().getUser().getUser_thumb().getThumb_path(), mCiHead, ImageLoaderOptions.optionsUserHeader);
            ImageUtil.displayImage(ZxsqApplication.getInstance().getUser().getUser_thumb().getThumb_path(), mBtnLeft, ImageLoaderOptions.optionsUserHeader);
    	}
    	if(result.getOrder_changed() == 1){
    		mIvOrderPoint.setVisibility(View.VISIBLE);
    		mIvHomeOrderPoint.setVisibility(View.VISIBLE);
    	}else{
    		mIvOrderPoint.setVisibility(View.GONE);
    		mIvHomeOrderPoint.setVisibility(View.GONE);
    	}
    	System.err.println("======getScreen_name:"+result.getScreen_name());
    	mTvName.setText(result.getScreen_name());
        String city = "";
        if (!TextUtils.isEmpty(result.getProvince())) {
            city += result.getProvince();
        }
        if (!TextUtils.isEmpty(result.getCity())) {
            city += (" " + result.getCity());
        }
        mTvProCity.setText(city);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.err.println("========onStart:");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		NetworkStateReceiver.removeRegisterObserver(mNetChangeObserver);
		NetworkStateReceiver.unRegisterNetworkStateReceiver(this);
	}
	
	/**
	 * 初始化-广播
	 */
	private void initHomeBroadcastReceiver(){
		IntentFilter intentFilter = new IntentFilter();
		mHomeBroadcastReceiver = new HomeBroadcastReceiver();
		intentFilter.addAction(BroadCastManager.ACTION_REFRESH_USER_INFO);//刷新个人信息广播
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);//刷新个人信息广播
		LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(mHomeBroadcastReceiver, intentFilter);

	}
	
	public class HomeBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(BroadCastManager.ACTION_REFRESH_USER_INFO)){  //刷新个人信息广播
				httpRequestQueryUserProfile();
			}else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
				LogUtil.e(ConnectivityManager.CONNECTIVITY_ACTION, "ConnectivityManager.CONNECTIVITY_ACTION");
			}
		}
		
	}
}
