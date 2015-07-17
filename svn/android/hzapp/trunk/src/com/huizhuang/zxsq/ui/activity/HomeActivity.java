package com.huizhuang.zxsq.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.CityOpen;
import com.huizhuang.zxsq.http.bean.Share;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.http.bean.advertise.Advertise;
import com.huizhuang.zxsq.http.bean.coupon.Coupon;
import com.huizhuang.zxsq.http.bean.solution.HouseSolution;
import com.huizhuang.zxsq.http.bean.solution.NearHouseSolution;
import com.huizhuang.zxsq.http.task.account.GetUserInfoTask;
import com.huizhuang.zxsq.http.task.advertise.AdvertiseTask;
import com.huizhuang.zxsq.http.task.coupon.CouponAddTask;
import com.huizhuang.zxsq.http.task.solution.HouseSolutionTask;
import com.huizhuang.zxsq.receiver.NetChangeObserver;
import com.huizhuang.zxsq.receiver.NetworkStateReceiver;
import com.huizhuang.zxsq.ui.activity.account.AccountDataEditorActivity;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.account.SettingActivity;
import com.huizhuang.zxsq.ui.activity.advertise.AdvertiseWebActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.company.CompanyCityList;
import com.huizhuang.zxsq.ui.activity.coupon.CouponListActivity;
import com.huizhuang.zxsq.ui.activity.foreman.ForemanListActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderCancelAppointmentReasonListActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderWaitResponseActivity;
import com.huizhuang.zxsq.ui.activity.solution.SolutionDetailActivity;
import com.huizhuang.zxsq.ui.activity.solution.SolutionListActivity;
import com.huizhuang.zxsq.ui.activity.user.UserMessageLoginActivity;
import com.huizhuang.zxsq.ui.adapter.AdvertiseAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.NetworkUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.utils.ScreenShotUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.utils.analytics.UmengAnalytics;
import com.huizhuang.zxsq.widget.CircleFlowIndicator;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.ViewFlow;
import com.huizhuang.zxsq.widget.dialog.CommonAlertDialog;
import com.huizhuang.zxsq.widget.dialog.CouponDialog;
import com.huizhuang.zxsq.widget.dialog.LoadingDialog;
import com.huizhuang.zxsq.widget.slidingmenu.SlidingMenu;
import com.huizhuang.zxsq.widget.slidingmenu.SlidingMenu.OnMenuClose;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends BaseActivity implements OnClickListener {

    // 城市列表返回
    private static final int CITY_LIST = 33545;
    private static final int USER_LOGIN = 33547;
    private static final int EDIT_USER_INFO = 33546;
    private final int DELAYED_TIME_10 = 10000;//
    private final int DELAYED_TIME_5 = 5000;// 1
    public static final String ROB_ORDER_NOW = "rob_order_now";
    private String mRobOrderId;
    private SlidingMenu mMenu;
    private CircleImageView mBtnLeft;
    private TextView mTvChoiceCity;
    private MapView mMapView;
    private ImageView mIvShadow;
    private Button mBtnChoice;
    private LinearLayout mLlGuarantee;
    private View mMapViewStub;
    private RelativeLayout mRobOrderActionBar;
    private RelativeLayout mCommonActionBar;
    private Button mBtnAppointment;
    private ImageView mIvOrderPoint;
    // 地图视图
    private Button mBtnMyLocation;
    private LinearLayout mllBottom;
    // 定位相关
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient = null;
    boolean isFirstLoc = true;// 是否首次定位
    private LatLng mMyLatLng = ZxsqApplication.getInstance().getUserPoint(); // 我的地理位置
    private String mNearHouseSolutionNum; //附近施工数目
    // 地理编码
    private GeoCoder mGeoCoder;
    // 施工缩略图
    private InfoWindow mInfoWindow;
    private CommonAlertDialog mCommonAlertDialog;
    private HashMap<LatLng, HouseSolution> mHouseMap;
    private HouseSolution mPopupHouseSolution; // 弹出的气泡的数据
  //  private boolean mInfoWindowIsShow;
    private boolean mMyInfoWindowFirstShow = true;
    // 个人中心相关
    private RelativeLayout mRlHeader;
    private CircleImageView mCiHead;
    private TextView mTvName;
    private TextView mTvProCity;
    private RelativeLayout mRlMyOrder;
    private RelativeLayout mRlMyCoupon;
    private RelativeLayout mRlMyMessage;
    private RelativeLayout mRlMyGift;
    private RelativeLayout mRlMySetting;
    private ImageView mIvHomeOrderPoint;
    private LoadingDialog mRobOrderDialog;
    private NetChangeObserver mNetChangeObserver;
    private boolean sss;
    //广告位
    private RelativeLayout mRlAd;
    private ViewFlow mViewFlowShow;
    private CircleFlowIndicator mCircleFlowIndicator;
    private ImageView mIvFinish;
    private int DELAY_TIME = 5000;
    // 广播监听者
    private HomeBroadcastReceiver mHomeBroadcastReceiver;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELAYED_TIME_10:
                    hideRobOrderView();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstants.PARAM_ORDER_ID, mRobOrderId);
                    ActivityUtil.next(HomeActivity.this, OrderWaitResponseActivity.class, bundle,
                            false);
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
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_home);
        //AutoPopGuarantee();
        initViews();
        initHomeBroadcastReceiver();
        initBaiduMap();
        initLocation();
        initGeocoder();
        initBroadcastReceiver();
        if (ZxsqApplication.getInstance().isLogged()) {
            httpRequestQueryUserProfile();
        }
        httpRequestAutoPopAdvertise();
        httpRequestGetHouseList(mMyLatLng);
        if(ZxsqApplication.getInstance().ismNoSite()){
        	showToastMsg("您所在的城市暂未开通，敬请期待！");
        }
    }

    private void initBroadcastReceiver() {
        mNetChangeObserver = new NetChangeObserver();
        // 注册网络的观察者
        NetworkStateReceiver.registerObserver(new NetChangeObserver() {
            @Override
            public void onConnect(NetType type) {
                if (ZxsqApplication.getInstance().getLocationCity() == null) {
                    isFirstLoc = true;
                    mLocClient.start();
                }
                mMapView.onResume();
                if (ZxsqApplication.getInstance().getLocationCity() == null) {
                    isFirstLoc = true;
                    mLocClient.start();
                }
            }
        });
        // 发送广播
        NetworkStateReceiver.registerNetworkStateReceiver(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
 
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra(ROB_ORDER_NOW) != null
                && intent.getStringExtra(AppConstants.PARAM_ORDER_ID) != null) {
            if (mMenu.getIsOpen()) {
                mMenu.toggle();
                mIvShadow.setVisibility(View.GONE);
                mMapView.setVisibility(View.VISIBLE);
            }
            mRobOrderId = intent.getStringExtra(AppConstants.PARAM_ORDER_ID);
            mMapViewStub.setVisibility(View.VISIBLE);
            showRobOrderView("3名工长抢单中...");
            Message msg = new Message();
            msg.what = DELAYED_TIME_5;
            mHandler.sendMessageDelayed(msg, DELAYED_TIME_5);
        }
        boolean isLogOut = intent.getBooleanExtra(AppConstants.PARAM_IS_LOGOUT, false);
        if(isLogOut){
//            mCiHead.setImageBitmap(null);
            mCiHead.setImageResource(R.drawable.ic_default_header);
            mTvName.setText("一键登录");
            mIvOrderPoint.setVisibility(View.GONE);
            mTvProCity.setText("");
//            mBtnLeft.setImageBitmap(null);
            mBtnLeft.setImageResource(R.drawable.ic_home_account_bg);
            mIvHomeOrderPoint.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化-地理编码（地理名称转化经纬度）
     */
    private void initGeocoder() {
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new MyOnGetGeoCoderResultListener());
    }

    /**
     * 首次启动app需要自动弹出服务保障页
     */
    private void AutoPopGuarantee() {
        boolean need = PreferenceConfig.getNeedAutoPopGuarantee();
        if (need) {
            ActivityUtil.next(THIS, GuaranteeActivity.class);
            PreferenceConfig.setNeedAutoPopGuarantee(false);
        }
    }

    /**
     * 如果有活动需要弹出活动的弹窗
     */
    private void httpRequestAutoPopAdvertise() {
    	if(PreferenceConfig.getReturnHomeFromLoginOut()){
    		PreferenceConfig.setReturnHomeFromLoginOut(false);
    		return;
    	}else{
	        AdvertiseTask advertisetask = new AdvertiseTask(this);
	        advertisetask.setCallBack(new AbstractHttpResponseHandler<List<Advertise>>() {
	
	            @Override
	            public void onSuccess(List<Advertise> advertiseList) {
	                List<Advertise> advertises = new ArrayList<Advertise>();
	                for (Advertise advertise : advertiseList) {
	                    if (advertise.getState().equals("1")) {// 0未启用，1为启用
	                        advertises.add(advertise);
	                    }
	                };
	                if(advertises.size() > 0){
	                    mRlAd.setVisibility(View.VISIBLE);
	                    initViewFlowData(advertises);
	                }else{
	                    mRlAd.setVisibility(View.GONE);
	                }
	            }
	
	            @Override
	            public void onFailure(int code, String error) {
	
	            }
	        });
	        advertisetask.send();
    	}
    }

    /**
     * 初始化-地图控制器
     */
    private void initBaiduMap() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapClickListener(new MyOnMapClickListener());
        mBaiduMap.setOnMarkerClickListener(new MyMarkerClickListener());
        mBaiduMap.setOnMapStatusChangeListener(new MyOnMapStatusChangeListener());
        if(mMyLatLng!=null){
        	MapStatus mMapStatus = new MapStatus.Builder().target(mMyLatLng).zoom(15).build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.animateMapStatus(mMapStatusUpdate);
        }
    }


    /**
     * 初始化-控件
     */
    private void initViews() {
        mMenu = (SlidingMenu) findViewById(R.id.slidingmenu);
        mBtnLeft = (CircleImageView) findViewById(R.id.img_btn_left);
        mTvChoiceCity = (TextView) findViewById(R.id.tv_city);
        mIvShadow = (ImageView) findViewById(R.id.iv_shadow);
        mCommonActionBar = (RelativeLayout) findViewById(R.id.common_action_bar);
        mRobOrderActionBar = (RelativeLayout) findViewById(R.id.rob_order_action_bar);
        mIvHomeOrderPoint = (ImageView) findViewById(R.id.iv_home_order_point);
        
        initMapStubView();
        initAccountView();
        if (ZxsqApplication.getInstance().getUser() != null
                && ZxsqApplication.getInstance().getUser().getUser_thumb() != null
                && ZxsqApplication.getInstance().getUser().getUser_thumb().getThumb_path() != null) {
            String uri = ZxsqApplication.getInstance().getUser().getUser_thumb().getThumb_path();
            ImageLoader.getInstance().displayImage(uri, mBtnLeft,
                    ImageLoaderOptions.optionsUserHeader);
        }
        if (!TextUtils.isEmpty(ZxsqApplication.getInstance().getLocationCity())) {
        	LogUtil.e("==========getLocationCity"+ZxsqApplication.getInstance().getLocationCity());
            mTvChoiceCity.setText(ZxsqApplication.getInstance().getLocationCity());
        }
        findViewById(R.id.img_btn_left).setOnClickListener(this);
        findViewById(R.id.iv_shadow).setOnClickListener(this);
        mLlGuarantee.setOnClickListener(this);
        mTvChoiceCity.setOnClickListener(this);
        mMenu.setOnMenuClose(new OnMenuClose() {

            @Override
            public void close() {
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
        mRlMyCoupon = (RelativeLayout) findViewById(R.id.rl_my_wallet);
        mRlMyMessage = (RelativeLayout) findViewById(R.id.rl_my_message);
        mRlMyGift = (RelativeLayout) findViewById(R.id.rl_my_gift);
        mRlMySetting = (RelativeLayout) findViewById(R.id.rl_my_setting);
        // mRlMyOrder.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_account_selector));
        // mRlMyWallet.setBackgroundDrawable(getResources()
        // .getDrawable(R.drawable.bg_account_selector));
        // mRlMyMessage.setBackgroundDrawable(getResources().getDrawable(
        // R.drawable.bg_account_selector));
        // mRlMyGift.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_account_selector));
        // mRlMySetting.setBackgroundDrawable(getResources().getDrawable(
        // R.drawable.bg_account_selector));
        mRlMyOrder.setBackgroundResource(R.drawable.bg_account_selector);
        mRlMyCoupon.setBackgroundResource(R.drawable.bg_account_selector);
        mRlMyMessage.setBackgroundResource(R.drawable.bg_account_selector);
        mRlMyGift.setBackgroundResource(R.drawable.bg_account_selector);
        mRlMySetting.setBackgroundResource(R.drawable.bg_account_selector);
        if (ZxsqApplication.getInstance().isLogged()) {
            if (null != ZxsqApplication.getInstance().getUser().getAvatar()) {
                ImageUtil.displayImage(ZxsqApplication.getInstance().getUser().getAvatar(),
                        mCiHead, ImageLoaderOptions.optionsUserHeader);
            }
        }
        mRlMyCoupon.setOnClickListener(this);
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
        mllBottom = (LinearLayout)findViewById(R.id.ll_bottom);
        mBtnChoice = (Button) findViewById(R.id.btn_choice_foreman);
        mBtnAppointment = (Button) findViewById(R.id.btn_appointment);
        if(ZxsqApplication.getInstance().getLocationCity()!=null){
        	hideChoice(ZxsqApplication.getInstance().getLocationCity());
        }
        findViewById(R.id.btn_choice_foreman).setOnClickListener(this);
        mBtnAppointment.setOnClickListener(this);
        mLlGuarantee = (LinearLayout) mMapViewStub.findViewById(R.id.ll_home_guarantee);
        mBtnMyLocation = (Button) mMapViewStub.findViewById(R.id.btn_my_location);
        mBtnMyLocation.setOnClickListener(this);
        mMapView.showZoomControls(false);
        initViewFlow();
    }

    private void initViewFlow(){
        int width = UiUtil.getScreenWidth(this);
        mRlAd = (RelativeLayout) mMapViewStub.findViewById(R.id.ic_ad);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width,Double.valueOf(width * 0.155).intValue());
        mRlAd.setLayoutParams(lp);
        mViewFlowShow = (ViewFlow) mMapViewStub.findViewById(R.id.viewflow_advertise_show);
        mIvFinish = (ImageView) mMapViewStub.findViewById(R.id.iv_finish);
        mIvFinish.setVisibility(View.GONE);
        mCircleFlowIndicator = (CircleFlowIndicator) mMapViewStub.findViewById(R.id.main_page_viewflowindic);
        mViewFlowShow.setTimeSpan(DELAY_TIME);
        mViewFlowShow.setFlowIndicator(mCircleFlowIndicator);
        mViewFlowShow.startAutoFlowTimer(); 
        mMapViewStub.findViewById(R.id.iv_finish).setOnClickListener(this);
    }
    
    private void initViewFlowData(final List<Advertise> advertises){
        AdvertiseAdapter advertiseAdapter = new AdvertiseAdapter(this, advertises);
        mViewFlowShow.setAdapter(advertiseAdapter);
        mViewFlowShow.setSelection(advertises.size() * 1000);
        mViewFlowShow.setValidCount(advertises.size());
        if (advertises.size() > 1) {
            mViewFlowShow.setNoScroll(false);// 默认是false
            mCircleFlowIndicator.setVisibility(View.VISIBLE);
        } else {
            mViewFlowShow.setNoScroll(true);
            mCircleFlowIndicator.setVisibility(View.GONE);
        }
        mViewFlowShow.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0073);
                Advertise advertise = advertises.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("url", advertise.getUrl());
                ActivityUtil.next(HomeActivity.this, AdvertiseWebActivity.class, bundle, false);
            }
        });
        mIvFinish.setVisibility(View.VISIBLE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_btn_left: // 切换到个人中心或登录页面
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0001);
                System.gc();
                ImageLoader.getInstance().clearMemoryCache();
//            	if(!ZxsqApplication.getInstance().isLogged()){     
//            		ActivityUtil.next(this, UserMessageLoginActivity.class, null, USER_LOGIN);
//            	}else{
            	if(ZxsqApplication.getInstance().isLogged()){     
            		httpRequestQueryUserProfile();
            	}
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
        		 break;
            case R.id.iv_shadow: // 切换到首页
                System.gc();
                ImageLoader.getInstance().clearMemoryCache();
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
            case R.id.btn_choice_foreman:// 选择工长按钮
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0014);
                ActivityUtil.next(HomeActivity.this, ForemanListActivity.class);
                break;
            case R.id.btn_my_location: // 回到定位位置
            	//mMyInfoWindowFirstShow = true;
            	if(NetworkUtil.isConnect(HomeActivity.this)){
            		showWaitDialog("正在定位");
                	mLocClient.start();
                	isFirstLoc = true;
            	}else{
            		showToastMsg("网络未连接！");
            	}
//                MapStatus mMapStatus = new MapStatus.Builder().target(mMyLatLng).build();
//                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//                mBaiduMap.animateMapStatus(mMapStatusUpdate);
//                mMyInfoWindowFirstShow = true;
//                if (ZxsqApplication.getInstance().getLocationCity() != null) {
//                    mTvChoiceCity.setText(ZxsqApplication.getInstance().getLocationCity());
//                    ZxsqApplication.getInstance().setSelectCity(ZxsqApplication.getInstance().getLocationCity());
//                    hideChoice(ZxsqApplication.getInstance().getLocationCity());
//                }
                break;
            case R.id.tv_city:
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0002);
                toCityList();
                break;
            case R.id.btn_appointment:
            	LogUtil.e("sss:"+sss);
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0005);
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.PARAM_ORDER_SOURCE_NAME, "app_index");
                ActivityUtil.checkAppointmentToJump(HomeActivity.this, bundle);
                break;
            case R.id.rl_my_gift:
                PreferenceConfig.setShareByOrderState(AppConstants.SHARE_BY_ORDER_COMMON);
                Share share = new Share(HomeActivity.this);
                Util.showShare(false, HomeActivity.this, share);
                break;
            case R.id.rl_my_order:
                if (!ZxsqApplication.getInstance().isLogged()) {
                    ActivityUtil.next(this, UserMessageLoginActivity.class, null, USER_LOGIN);
                } else {
                    ActivityUtil.next(HomeActivity.this, MyOrderActivity.class);
                }
                break;
            case R.id.rl_my_setting:
                if (!ZxsqApplication.getInstance().isLogged()) {
                    ActivityUtil.next(this, UserMessageLoginActivity.class, null, USER_LOGIN);
                } else {
                    ActivityUtil.next(HomeActivity.this, SettingActivity.class);
                }
                break;
            case R.id.ll_home_guarantee:
                ActivityUtil.next(HomeActivity.this, GuaranteeActivity.class);
                break;
            case R.id.rl_header:
                if (!ZxsqApplication.getInstance().isLogged()) {
                    ActivityUtil.next(this, UserMessageLoginActivity.class, null, USER_LOGIN);
                } else {
                    ActivityUtil.next(HomeActivity.this, AccountDataEditorActivity.class, null,
                            EDIT_USER_INFO);
                }
                break;
            case R.id.rl_my_wallet: //优惠券
                if (!ZxsqApplication.getInstance().isLogged()) {
                    ActivityUtil.next(this, UserMessageLoginActivity.class, null, USER_LOGIN);
                } else {
                    ActivityUtil.next(HomeActivity.this, CouponListActivity.class);
                }
                break;
            case R.id.iv_finish: //关闭广告
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0074);
                mRlAd.setVisibility(View.GONE);
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
    private void httpRequestGetHouseList(LatLng latLng) {
        if (latLng != null) {
            HouseSolutionTask task =
                    new HouseSolutionTask(this, "", String.valueOf(latLng.longitude),
                            String.valueOf(latLng.latitude));
            task.setCallBack(new AbstractHttpResponseHandler<NearHouseSolution>() {

                @Override
                public void onSuccess(NearHouseSolution data) {
                	mNearHouseSolutionNum = data.getNear_case_count();
                    if (data.getList() != null && data.getList().size() > 0) {
                        mHouseMap = new HashMap<LatLng, HouseSolution>();
                        mBaiduMap.clear();
                        for (int i = 0; i < data.getList().size(); i++) {
                            HouseSolution houseSolution = data.getList().get(i);
                            if (!TextUtils.isEmpty(houseSolution.getLat())
                                    && !TextUtils.isEmpty(houseSolution.getLon())) {
                                LatLng latLng =
                                        new LatLng(Double.valueOf(houseSolution.getLat()), Double
                                                .valueOf(houseSolution.getLon()));
                                addMarker(latLng);
                                mHouseMap.put(latLng, houseSolution);
                            }
                        }
                        addMyMarker();
                        if(mMyInfoWindowFirstShow && data.getList() != null&& data.getList().size()>0){
                        	showMyInfoWindow();
                        }
                    }
                }

                @Override
                public void onFailure(int code, String error) {
                    showToastMsg(error);
                }
            });
            task.send();
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
        if(mMyLatLng == null){
        	mLocClient.start();	
        }
       // mLocClient.start();
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
        	hideWaitDialog();
            mLocClient.stop();
            if (location == null || mMapView == null) return;
            if (isFirstLoc&&location.getProvince()!=null) { // 测试
                isFirstLoc = false;
                ZxsqApplication.getInstance().setmProvince(location.getProvince());
                ZxsqApplication.getInstance().setLocationCity(ZxsqApplication.getInstance().matchingSite(ZxsqApplication.getInstance().getmCityOpens()
                		, location.getCity().replace("市", ""), location.getProvince(),new LatLng(location.getLatitude(), location.getLongitude())));          
                hideChoice(ZxsqApplication.getInstance().getLocationCity());
                mTvChoiceCity.setText(ZxsqApplication.getInstance().getLocationCity());
                if(ZxsqApplication.getInstance().ismNoSite()){
                	showToastMsg("您所在的城市暂未开通，敬请期待！");
                }
//                if(ZxsqApplication.getInstance().getLocationCity().equals(location.getCity().replace("市", ""))){
                	ZxsqApplication.getInstance().setmNoSite(false);
            		ZxsqApplication.getInstance().setmUserPoint(new LatLng(location.getLatitude(), location.getLongitude()));
            		mMyLatLng = ZxsqApplication.getInstance().getUserPoint();
            		mMyInfoWindowFirstShow = true;
            		addMyMarker();
                    MapStatus mMapStatus = new MapStatus.Builder().target(mMyLatLng).zoom(15).build();
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    mBaiduMap.animateMapStatus(mMapStatusUpdate);
//            	}else{
//            		mGeoCoder.geocode(new GeoCodeOption().city(location.getCity()).address(location.getCity()));
//            		//showToastMsg("您所在的城市暂未开通，敬请期待！");
//            		mllBottom.setVisibility(View.GONE);
//            	}
//                mMyLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//                addMyMarker();
//                MapStatus mMapStatus = new MapStatus.Builder().target(mMyLatLng).zoom(15).build();
//                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//                mBaiduMap.animateMapStatus(mMapStatusUpdate);
            }
//            ZxsqApplication.getInstance().setmProvince(location.getProvince());
//            if (location.getCity() != null) {
//                String locationCity = location.getCity().replace("市", "");
//                ZxsqApplication.getInstance().setLocationCity(locationCity);
//                hideChoice(locationCity);
//            }
        }

        public void onReceivePoi(BDLocation poiLocation) {}
    }
    /**
     * 初始化-定位相关
     */
    public class MyMarkerClickListener implements OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(final Marker arg0) {
//            MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newLatLng(arg0.getPosition());
//            mBaiduMap.animateMapStatus(statusUpdate);
        	if(arg0.getExtraInfo()!=null){
        		showMyInfoWindow();
        		mPopupHouseSolution = null;
        	}else{
	            final View view =
	                    LayoutInflater.from(HomeActivity.this).inflate(R.layout.view_map_info_woindow,
	                            null);
	            Animation anim = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.pop_show);
	            view.setAnimation(anim);
	            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
	                    ViewGroup.LayoutParams.WRAP_CONTENT));
	            mPopupHouseSolution = mHouseMap.get(arg0.getPosition());
	            // final ImageView ivMarkerIcon = (ImageView) view.findViewById(R.id.tv_marker_icon);
	            TextView tvMarkerName = (TextView) view.findViewById(R.id.tv_marker_name);
	            TextView tvMarkerNum = (TextView) view.findViewById(R.id.tv_marker_num);
	            if (mPopupHouseSolution != null) {
	               // mInfoWindowIsShow = true;
	                tvMarkerName.setText(mPopupHouseSolution.getName());
	                tvMarkerNum.setText("施工现场" + mPopupHouseSolution.getCase_count() + "个");
	                mInfoWindow =
	                        new InfoWindow(BitmapDescriptorFactory.fromView(view), arg0.getPosition(),
	                                -DensityUtil.dip2px(getApplicationContext(), 70),
	                                new MyonInfoWindowClick());
	                mBaiduMap.showInfoWindow(mInfoWindow);

            	}
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
            if(ZxsqApplication.getInstance().getUserPoint() == null){
            	ZxsqApplication.getInstance().setmUserPoint(arg0.getLocation());
//            	showMyInfoWindow();
            }
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
            if(mPopupHouseSolution == null){
            	AnalyticsUtil.onEvent(HomeActivity.this,UmengEvent.ID_CLICK_0075);
            	bd.putBoolean(SolutionListActivity.PARAME_ALL_SOLUTION_IN_CURRENT_CITY, true);//跳转至当前城市所有的施工列表
            	ActivityUtil.next(HomeActivity.this, SolutionListActivity.class, bd, -1);
            }else if (mPopupHouseSolution.getCase_count() > 0) {
                if (mPopupHouseSolution.getCase_count() == 1) {
                    bd.putString(SolutionDetailActivity.PARAME_SHOW_CASE_ID_KEY,
                            mPopupHouseSolution.getShowcase_id() + "");
                    ActivityUtil.next(HomeActivity.this, SolutionDetailActivity.class, bd, -1);
                } else {
                	bd.putBoolean(SolutionListActivity.PARAME_ALL_SOLUTION_IN_CURRENT_CITY, false);//跳转至当前小区所有的施工列表
                    bd.putString(SolutionListActivity.PARAME_HOUSE_ID, mPopupHouseSolution.getId()
                            + "");
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
            mBaiduMap.hideInfoWindow();
           // mInfoWindowIsShow = false;
        }

        @Override
        public boolean onMapPoiClick(MapPoi arg0) {
            return false;
        }
    }
    /**
     * 接口-地图移动监听器
     * 
     */
    public class MyOnMapStatusChangeListener implements OnMapStatusChangeListener {

        @Override
        public void onMapStatusChange(MapStatus arg0) {}

        @Override
        public void onMapStatusChangeFinish(MapStatus arg0) {
//            if (!mInfoWindowIsShow) {
                httpRequestGetHouseList(arg0.target);
//            }
        }

        @Override
        public void onMapStatusChangeStart(MapStatus arg0) {}
    }

    /**
     * 添加坐标图标到百度view
     * 
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
     * 
     * @param lng
     * @param lat
     */
    public void addMyMarker() {
        if (mMyLatLng != null) {
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
            MarkerOptions markerOptions = new MarkerOptions().icon(bd).position(mMyLatLng);
            Bundle bundle = new Bundle();
            bundle.putBoolean(AppConstants.PARAM_IS_MY_MARKER, true);
            markerOptions.extraInfo(bundle);
            mBaiduMap.addOverlay(markerOptions);
            //showMyInfoWindow();
        }
    }
    
    /**
     * 
     */
    private void showMyInfoWindow(){
    	mMyInfoWindowFirstShow = false;
    	if (mMyLatLng != null) {
        final View view1 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.view_user_info_woindow, null);
        TextView tvContent = (TextView)view1.findViewById(R.id.tv_user_marker);
      //  tvContent.setText("附近有13个工地正在施工");
        tvContent.setText(Html
                .fromHtml("<font color='#808080'>附近有</font><font color='#ff6c38'>"+mNearHouseSolutionNum+"个</font><font color='#808080'>工地正在施工</font>"));
	        mInfoWindow =
			new InfoWindow(BitmapDescriptorFactory.fromView(view1), mMyLatLng,
	              -DensityUtil.dip2px(getApplicationContext(), 20),
	              new MyonInfoWindowClick());
	        mBaiduMap.showInfoWindow(mInfoWindow);
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
        } else if (EDIT_USER_INFO == requestCode && resultCode == Activity.RESULT_OK) {
            httpRequestQueryUserProfile();
        } else if (USER_LOGIN == requestCode && resultCode == Activity.RESULT_OK) {
            httpRequestQueryUserProfile();
            httpRequestAddCoupon();
        }
    }

    /**
     * 新用户登录得优惠券
     */
    private void httpRequestAddCoupon() {
        CouponAddTask task = new CouponAddTask(this, null,1);
        task.setCallBack(new AbstractHttpResponseHandler<Coupon>() {

            @Override
            public void onStart() {
                super.onStart();
                showWaitDialog(getResources().getString(R.string.txt_on_waiting));
            }

            @Override
            public void onSuccess(Coupon coupon) {
                if(coupon.getStatu() == 1){
                    CouponDialog couponDialog = new CouponDialog(HomeActivity.this);
                    couponDialog.builder(coupon.getAmount());
                    couponDialog.show();
                }
            }

            @Override
            public void onFailure(int code, String error) {
                showToastMsg(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        });
        task.send();
    }
    
    private void hideChoice(String cityName) {
    	mllBottom.setVisibility(View.GONE);
        List<CityOpen> cityOpens = ZxsqApplication.getInstance().getmCityOpens();
        if (cityOpens != null && cityOpens.size() > 0) {
            for (CityOpen cityOpen : cityOpens) {
                if (cityOpen.getName().replace("市", "").equals(cityName.replace("市", ""))) {
                	mllBottom.setVisibility(View.VISIBLE);
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
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
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
                hideRobOrderView();// 隐藏等待加载框
                mHandler.removeMessages(DELAYED_TIME_10);
                mHandler.removeMessages(DELAYED_TIME_5);
                ActivityUtil
                        .next(HomeActivity.this, OrderCancelAppointmentReasonListActivity.class);
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
        String token = ZxsqApplication.getInstance().getUser().getToken(); // 请求个人数据无token所以自己保存了一个
        result.setToken(token);
        ZxsqApplication.getInstance().setUser(result);
        if (TextUtils.isEmpty(result.getMobile())) {
            ZxsqApplication.getInstance().getUser()
                    .setMobile(PrefersUtil.getInstance().getStrValue("mobile"));
        }
        if (null != ZxsqApplication.getInstance().getUser().getUser_thumb()
                && null != ZxsqApplication.getInstance().getUser().getUser_thumb().getThumb_path()) {
            ImageUtil.displayImage(ZxsqApplication.getInstance().getUser().getUser_thumb()
                    .getThumb_path(), mCiHead, ImageLoaderOptions.optionsUserHeader);
            ImageUtil.displayImage(ZxsqApplication.getInstance().getUser().getUser_thumb()
                    .getThumb_path(), mBtnLeft, ImageLoaderOptions.optionsUserHeader);
        }
        if (result.getOrder_changed() == 1) {
            mIvOrderPoint.setVisibility(View.VISIBLE);
            mIvHomeOrderPoint.setVisibility(View.VISIBLE);
        } else {
            mIvOrderPoint.setVisibility(View.GONE);
            mIvHomeOrderPoint.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(result.getNickname())){
        	mTvName.setText(result.getNickname());
        }else if(!TextUtils.isEmpty(result.getUsername())){
        	mTvName.setText(result.getUsername());
        }else{
        	mTvName.setText(result.getMobile());
        }
        String city = "";
        if (!TextUtils.isEmpty(result.getProvince())) {
            city += result.getProvince();
        }
        if (!TextUtils.isEmpty(result.getCity())) {
            if (!TextUtils.isEmpty(city)) {
                city += "·";
            }
            city += result.getCity();
        }
        mTvProCity.setText(city);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    private void initHomeBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        mHomeBroadcastReceiver = new HomeBroadcastReceiver();
        intentFilter.addAction(BroadCastManager.ACTION_REFRESH_USER_INFO);// 刷新个人信息广播
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);// 刷新个人信息广播
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(
                mHomeBroadcastReceiver, intentFilter);

    }

    public class HomeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadCastManager.ACTION_REFRESH_USER_INFO)) { // 刷新个人信息广播
                httpRequestQueryUserProfile();
            } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                LogUtil.e(ConnectivityManager.CONNECTIVITY_ACTION,
                        "ConnectivityManager.CONNECTIVITY_ACTION");
            }
        }

    }
}
