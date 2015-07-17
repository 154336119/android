package com.huizhuang.zxsq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;

import android.R.array;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.exception.CrashException;
import com.huizhuang.zxsq.http.bean.CityOpen;
import com.huizhuang.zxsq.http.bean.Constant;
import com.huizhuang.zxsq.http.bean.ProviceCityArea;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.http.bean.supervision.ComplaintsNode;
import com.huizhuang.zxsq.http.parser.ConstantParser;
import com.huizhuang.zxsq.http.task.CityDistrictTask;
import com.huizhuang.zxsq.http.task.CityOpenTask;
import com.huizhuang.zxsq.http.task.supervision.ComplaintsNodeTask;
import com.huizhuang.zxsq.receiver.NetChangeObserver;
import com.huizhuang.zxsq.receiver.NetChangeObserver.NetType;
import com.huizhuang.zxsq.receiver.NetworkStateReceiver;
import com.huizhuang.zxsq.ui.activity.NetworkStateActivity;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.FileUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.utils.StringEncodeUtil;
import com.huizhuang.zxsq.utils.Util;
import com.lgmshare.http.netroid.Network;
import com.lgmshare.http.netroid.RequestQueue;
import com.lgmshare.http.netroid.stack.HttpClientStack;
import com.lgmshare.http.netroid.stack.HttpStack;
import com.lgmshare.http.netroid.stack.HurlStack;
import com.lgmshare.http.netroid.toolbox.BasicNetwork;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.message.PushAgent;

/**
 * @author lim
 * @ClassName: AppContext
 * @Description:
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午11:12:01
 */
public class ZxsqApplication extends Application {

    private static ZxsqApplication mInstance;
    /**
     * 应用activity管理器
     */
    private ZxsqActivityManager mStackManager;
    /**
     * app的上一次安装版本号
     */
    private int mAppLastVersionCode;
    /**
     * app的当前安装版本号
     */
    private int mAppCurVersionCode;
    /**
     * app的codeName
     */
    private String mAppCurVersionName;

    private RequestQueue mRequestQueue = null;
    private Constant mConstant;
    private User mUser = null;
    /**
     * 登录状态-是否已登录
     */
    private boolean mLogged = true;
    private boolean mNetworkAvailable = true;
    private boolean mIsWifiConnectNetWork;
    private boolean mCloseLoadImgWhenNotInWifi;
    /**
     * 友盟推送
     */
    private PushAgent mPushAgent;
    private String mDeviceToken;
    /**
     * LBS
     */
    private String mProvince; // 定位省
    private LatLng mUserPoint; // 定位坐标
    private String mLocationCity;// 定位城市
    private String mDistrict; // 定位区县
    private String mPostCode;// 邮编
    private LocationClient mLocationClient;
    private PoiSearch mPoiSearch;
    // 地理编码
    private GeoCoder mGeoCoder;
    // 选择城市
    private String selectCity;
    private List<ProviceCityArea> cityDistricts;
    private List<ProviceCityArea> citys;
    private List<ProviceCityArea> mAllcitys;
    private boolean mNoSite = true;
    private List<CityOpen> mCityOpens;
    private List<ComplaintsNode> mComplaintsNode;//投诉节点\
    public static ZxsqApplication getInstance() {
        return mInstance;
    }
    private String mAddstr;// 定位具体位置，精确到街道

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化sharedsdk
        ShareSDK.initSDK(this);
        mInstance = this;
        mRequestQueue = newRequestQueue(mInstance, null);
        mStackManager = ZxsqActivityManager.getInstance();
        initExceptionHandler();
        initNetworkObserver();
        initAppVsersionInfo();
        initImageLoader(getApplicationContext());
        initAreas();
        initCityOpen();
//        initPoiSearch();
        getCloseLoadImgWhenNotInWifiState();
        initConstant();
        initAreas();
        initLocation();
        initGeocoder();
        initComplaintsNode();
        initUMengMessage();
        initJpush();
        //MobclickAgent.openActivityDurationTrack(false);//因为包含Fragment,所以不能用默认的页面统计方式
    }

//	private void initPoiSearch() {
//		// TODO Auto-generated method stub
//		mPoiSearch = PoiSearch.newInstance();
//		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
//	}
	
//	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
//
//		@Override
//		public void onGetPoiDetailResult(PoiDetailResult arg0) {
//			
//		}
//
//		@Override
//		public void onGetPoiResult(PoiResult arg0) {
//			List<PoiInfo> lists = arg0.getAllPoi();
//			for(PoiInfo p : lists){
//				LogUtil.e("=====PoiInfo:"+p.postCode );
//			}
//		}  
//		
//	};
    /** 
     * 初始化-地理编码（地理名称转化经纬度）
     */
    private void initGeocoder() {
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new MyOnGetGeoCoderResultListener());
    }	
	public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
        String packageName = context.getPackageName();
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(packageName, 0);
            String name = info.packageName;
            String userAgent = "huizhuang;shenqi;" + name + ";android-phone";
            if (stack == null) {
                if (Build.VERSION.SDK_INT >= 9) {
                    stack = new HurlStack(null);
                } else {
                    stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
                }
            }
            Network network = new BasicNetwork(stack, "UTF-8");
            RequestQueue queue = new RequestQueue(network);
            queue.start();
            return queue;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化友盟消息推送
     */
    private void initUMengMessage() {
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        mPushAgent.enable();
//        mPushAgent.setPushIntentServiceClass(UMengPushIntentService.class);

        mDeviceToken = mPushAgent.getRegistrationId();
        LogUtil.i("deviceToken:" + mDeviceToken);
    }

    /**
     * 从本地或者资产目录获取常量
     */
    private void initConstant() {
        mConstant = (Constant) FileUtil.readFile(mInstance, "Constant");
        if (mConstant == null) {
            String data = FileUtil.getFromAssets(mInstance, "Constant");
            try {
                mConstant = new ConstantParser().parse(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从本地或者本地目录获取全国所有城市
     */
    private void initAreas() {
        @SuppressWarnings("unchecked")
        List<ProviceCityArea> areas = (List<ProviceCityArea>) FileUtil.readFile(mInstance, "Areas");
        if (areas != null) {
            setCityDistricts(areas);
        } else {
            String data = FileUtil.getFromAssets(mInstance, "Areas");
            areas = new CityDistrictTask(mInstance).parse(data); // 接口更改
            setCityDistricts(areas);
        }
    }

    /**
     * 从本地或者本地目录获取开通城市
     */
    private void initCityOpen() {
        @SuppressWarnings("unchecked")
        List<CityOpen> cityOpens = (List<CityOpen>) FileUtil.readFile(mInstance, "CityOpen");
        if (cityOpens != null) {
            setmCityOpens(cityOpens);
        } else {
            String data = FileUtil.getFromAssets(mInstance, "CityOpen");
            cityOpens = new CityOpenTask(mInstance).parse(data); // 接口更改
            setmCityOpens(cityOpens);
        }
    }
    
    /**
     * 从本地或者本地目录获取投诉节点信息
     */
    private void initComplaintsNode() {
        @SuppressWarnings("unchecked")
        List<ComplaintsNode> complaintsNodes = (List<ComplaintsNode>) FileUtil.readFile(mInstance, "Complaints");
        if (complaintsNodes != null) {
            setmComplaintsNode(complaintsNodes);
        } else {
            String data = FileUtil.getFromAssets(mInstance, "Complaints");
            complaintsNodes = new ComplaintsNodeTask(mInstance).parse(data);
            setmComplaintsNode(complaintsNodes);
        }
    }
    
    /**
     * 覆盖系统异常处理
     */
    private void initExceptionHandler() {
        // 注册App异常崩溃处理器
        if (!AppConfig.DEBUG_MODE) {
            Thread.setDefaultUncaughtExceptionHandler(new CrashException());
        }
    }

    /**
     * 初始话图片加载模块
     * <p/>
     * Xun.Zhang
     */
    private void initImageLoader(Context applicationContext) {
        // ImageLoader内存缓存最大为系统可用内存数的1/8，默认为2MB
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();
//        int maxImageMemoryCacheSize =
//                (maxMemory == 0) ? ImageLoaderOptions.MAX_IMAGE_MEMORY_CACHE_SIZE : (maxMemory / 8);

//        ImageLoaderConfiguration config =
//                new ImageLoaderConfiguration.Builder(applicationContext)
//                        .memoryCache(new LruMemoryCache(maxImageMemoryCacheSize))
//                        .memoryCacheExtraOptions(ImageLoaderOptions.MAX_IMAGE_WIDTH,
//                                ImageLoaderOptions.MAX_IMAGE_HEIGHT)
//                        .threadPriority(Thread.NORM_PRIORITY - 2)
//                        .denyCacheImageMultipleSizesInMemory()
//                        .defaultDisplayImageOptions(ImageLoaderOptions.optionsDefaultEmptyPhoto)
//                        .diskCacheSize(ImageLoaderOptions.MAX_IMAGE_DISK_CACHE_SIZE)
//                        .diskCacheFileCount(ImageLoaderOptions.MAX_IMAGE_DISK_FILE_COUNT)
//                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                        .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        long maxMemory = Runtime.getRuntime().maxMemory();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(3)
                .memoryCacheSize((int) maxMemory / 4)
                .memoryCache(new WeakMemoryCache())
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                        // 100 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDownloader(
                        new BaseImageDownloader(getApplicationContext(), 5000,
                                5000))
                .diskCache(
                        new UnlimitedDiskCache(Util.getDiskCacheDir(getApplicationContext())))
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 注册网路连接状态监听
     */
    private void initNetworkObserver() {
        if (NetworkStateReceiver.getNetworkType(mInstance) == NetType.wifi) {
            mIsWifiConnectNetWork = true;
        }
        NetworkStateReceiver.registerObserver(new NetChangeObserver() {

            @Override
            public void onConnect(NetType type) {
                super.onConnect(type);
                mNetworkAvailable = true;
                if (mStackManager != null) {
                    Activity activity = mStackManager.getCurrentActivity();
                    if (activity != null && (activity instanceof NetworkStateActivity)) {
                        ((NetworkStateActivity) activity).onConnect(type);
                    }
                }
                //==initLocation();
            }

            @Override
            public void onDisConnect() {
                super.onDisConnect();
                mNetworkAvailable = false;
                if (mStackManager != null) {
                    Activity activity = mStackManager.getCurrentActivity();
                    if (activity != null && (activity instanceof NetworkStateActivity)) {
                        ((NetworkStateActivity) activity).onDisConnect();
                    }
                }
            }

        });
    }

    /**
     * 初始化应用版本信息
     */
    private void initAppVsersionInfo() {
        mAppLastVersionCode = PreferenceConfig.getAppLastVersionCode(this);
        try {
            PackageInfo pkg = getPackageManager().getPackageInfo(getPackageName(), 0);
            mAppCurVersionCode = pkg.versionCode;
            mAppCurVersionName = pkg.versionName;
        } catch (NameNotFoundException e) {
            mAppCurVersionCode = mAppLastVersionCode;
            mAppCurVersionName = "";
        }
    }

    /**
     * 获取App安装包信息
     * 
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    /**
     * 获取App唯一标识
     * 
     * @return
     */
    public String getAppUniqueID() {
        String uniqueID = PreferenceConfig.getAppUniqueID(this);
        if (StringEncodeUtil.isEmpty(uniqueID)) {
            // 获取设备ID
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            uniqueID = tm.getDeviceId();
            // 获取不到，则随机生成一个标识
            if (StringEncodeUtil.isEmpty(uniqueID)) {
                uniqueID = UUID.randomUUID().toString();
            }
            PreferenceConfig.saveAppUniqueId(this, uniqueID);
        }
        return uniqueID;
    }

    /**
     * Retrieves application's version name from the manifest
     * 
     * @return
     */
    public String getVersionName() {
        return mAppCurVersionName;
    }

    /**
     * 是否是新安装应用
     * 
     * @return
     */
    public boolean isNewInstall() {
        return mAppLastVersionCode != mAppCurVersionCode;
    }

    /**
     * 更新安装版本信息
     */
    public void updateNewVersion() {
        PreferenceConfig.setNeedAutoPopGuarantee(true);
        PreferenceConfig.saveAppLastVersionCode(this, mAppCurVersionCode);
    }

    /**
     * 清楚缓存
     */
    public void clearCache() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }

    public User getUser() {
        if (mUser == null) mUser = new User();
        return mUser;
    }

    public void setUser(User user) {
        LogUtil.i("user:" + user);
        if (user != null && user.getToken() != null) {
            PrefersUtil.getInstance().setValue("userId", user.getUser_id());
            PrefersUtil.getInstance().setValue("token", user.getToken());
            setLogged(true);
            BroadCastManager.sendBroadCast(mInstance, BroadCastManager.ACTION_USER_LOGIN);
        } else {
            setLogged(false);
        }
        mUser = user;
    }

    public void setUser(User user, boolean r) {
        LogUtil.i("user:" + user);
        if (user != null && user.getToken() != null) {
            PrefersUtil.getInstance().setValue("userId", user.getUser_id());
            PrefersUtil.getInstance().setValue("token", user.getToken());
            setLogged(true);
        } else {
            setLogged(false);
        }
        mUser = user;
    }

    
    public List<CityOpen> getmCityOpens() {
        return mCityOpens;
    }

    public void setmCityOpens(List<CityOpen> mCityOpens) {
        this.mCityOpens = mCityOpens;
    }

    public boolean isLogged() {
        if (getUser() == null || TextUtils.isEmpty(getUser().getUser_id())) {
            return false;
        }
        LogUtil.i("mLogged:" + mLogged);
        return mLogged;
    }

    public void setLogged(boolean logged) {
        LogUtil.i("logged:" + logged);
        mLogged = logged;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * 网络连接状态
     */
    public boolean isNetworkAvailable() {
        return mNetworkAvailable;
    }

    /**
     * activity管理堆栈
     */
    public ZxsqActivityManager getStackManager() {
        if (mStackManager == null) {
            mStackManager = ZxsqActivityManager.getInstance();
        }
        return mStackManager;
    }

    /**
     * 退出系统
     */
    public void exit() {
        mRequestQueue.stop();
        mStackManager.appExit(this);
        ShareSDK.stopSDK(this);
    }

    public LocationClient getmLocationClient() {
        return mLocationClient;
    }

    public Constant getConstant() {
        return mConstant;
    }

    public void setConstant(Constant mConstant) {
        this.mConstant = mConstant;
    }
    /**
     * 初始化Jpush
     */
    private void initJpush() {
		JPushInterface.setDebugMode(AppConfig.DEBUG_MODE);//设置开启日志，发布时关闭日志
		JPushInterface.init(this);//初始化Jpush
	}

    /**
     * 初始化地图
     */
    public void initLocation() {
        // 初始化百度地图sdk
        SDKInitializer.initialize(this);
        // 定位初始化
        mLocationClient = new LocationClient(this);
        MyLocationListenner myListener = new MyLocationListenner();
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            mProvince = arg0.getProvince();
            if (arg0.getCity() != null) {
               // mLocationCity = arg0.getCity().replace("市", "");
                mLocationCity = matchingSite(mCityOpens, arg0.getCity().replace("市", ""), mProvince,new LatLng(arg0.getLatitude(), arg0.getLongitude()));
            //	mLocationCity = matchingSite(mCityOpens, "拉萨", "西藏自治区",new LatLng(arg0.getLatitude(), arg0.getLongitude()));
//            	if(mLocationCity.equals(arg0.getCity().replace("市", ""))){
            		mUserPoint = new LatLng(arg0.getLatitude(), arg0.getLongitude());
//            	}else{
//            		mGeoCoder.geocode(new GeoCodeOption().city(mLocationCity).address(mLocationCity));
//            	}
            }
            mAddstr = arg0.getAddrStr();
            mDistrict = arg0.getDistrict();
            LogUtil.e("locationCity:" + mLocationCity);
            mLocationClient.stop();
           // matchingSite(mCityOpens, mLocationCity, mProvince);
//            mPoiSearch.searchNearby(new PoiNearbySearchOption().location(mUserPoint).keyword("美食").radius(1000));
        }

        @Override
        public void onReceivePoi(BDLocation arg0) {}

    }
    
    public String getmProvince() {
        return mProvince;
    }

    public void setmProvince(String province){
        this.mProvince = province;
    }
    public LatLng getUserPoint() {
        if (mUserPoint != null) {
            return mUserPoint;
        } else {
            return null;
        }
    }
    
    public void setmUserPoint(LatLng mUserPoint) {
		this.mUserPoint = mUserPoint;
	}

	public String getLocationCity() {
        if (mLocationCity != null) {
            return mLocationCity;
        } else {
            return null;
        }
    }
    
    public void setLocationCity(String locationCity){
        this.mLocationCity = locationCity;
    }

    public String getLocationDistrict() {
        return mDistrict;
    }

    public String getmAddstr() {
        return mAddstr;
    }

    public boolean isWifiConnectNetWork() {
        return mIsWifiConnectNetWork;
    }

    public void setWifiConnectNetWork(boolean isWifiConnectNetWork) {
        this.mIsWifiConnectNetWork = isWifiConnectNetWork;
    }

    public boolean isCloseLoadImgWhenNotInWifi() {
        return mCloseLoadImgWhenNotInWifi;
    }

    public void setCloseLoadImgWhenNotInWifi(boolean closeLoadImgWhenNotInWifi) {
        this.mCloseLoadImgWhenNotInWifi = closeLoadImgWhenNotInWifi;
    }

    public boolean getCloseLoadImgWhenNotInWifiState() {
        setCloseLoadImgWhenNotInWifi(PreferenceConfig
                .readWIFILoadImageSwitch(getApplicationContext()));
        return mCloseLoadImgWhenNotInWifi;
    }
    public String getDeviceToken() {
        return mDeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.mDeviceToken = deviceToken;
    }

    public String getSelectCity() {
        return selectCity;
    }

    public void setSelectCity(String selectCity) {
        this.selectCity = selectCity;
    }

    public List<ProviceCityArea> getCityDistricts() {
        return cityDistricts;
    }

    public void setCityDistricts(List<ProviceCityArea> cityDistricts) {
        this.cityDistricts = cityDistricts;
        this.citys = cityDistricts;
        setAllCity(cityDistricts);
    }

    public void setAllCity(List<ProviceCityArea> cityDistricts) {
        if (cityDistricts != null) {
            mAllcitys = new ArrayList<ProviceCityArea>();
            for (ProviceCityArea proviceCityArea : cityDistricts) {
                List<ProviceCityArea> citys = proviceCityArea.getCitys();
                if(citys!=null&&citys.size()>0){
                	mAllcitys.addAll(citys);
                }
            }
        }
    }
    
    

    public List<ComplaintsNode> getmComplaintsNode() {
        return mComplaintsNode;
    }

    public void setmComplaintsNode(List<ComplaintsNode> mComplaintsNode) {
        this.mComplaintsNode = mComplaintsNode;
    }

    public List<ProviceCityArea> getmAllcitys() {
        return mAllcitys;
    }


    public List<ProviceCityArea> getCitys() {
        return citys;
    }

    /**
     * 根据省的名称得到城市列表
     * 
     * @param city
     * @return
     */
    public List<ProviceCityArea> getDistricts(String city) {
        if (city != null && citys != null) for (ProviceCityArea cd : citys) {
            if (city.equals(cd.getArea_name())) {
                return cd.getCitys();
            }
        }
        return null;
    }

    /**
     * 根据城市名称得到城市信息
     * 
     * @param city
     * @return
     */
    public ProviceCityArea getCity(String city) {
        if (city != null && citys != null){
            for (ProviceCityArea province : citys) {
                List<ProviceCityArea> citysList = province.getCitys();
                if (citysList != null && citysList.size() > 0) {
                    for (ProviceCityArea cd : citysList) {
                        if (city.equals(cd.getArea_name().replace("市", ""))) {
                            return cd;
                        }
                    }
                }
            }
        }
        return null;
    }
    /**
     * 根据城市ID得到 名称：省.城市
     * @param cityId
     * @return
     */
    public String getNameForProvinceAndCity(int cityId){
        String name = "";
        if (citys != null){
            for (ProviceCityArea province : citys) {
                List<ProviceCityArea> citysList = province.getCitys();
                if (citysList != null && citysList.size() > 0) {
                    for (ProviceCityArea cd : citysList) {
                        if (cd.getArea_id() == cityId) {
                            name = province.getArea_name()+" 。"+cd.getArea_name();
                            return name;
                        }
                    }
                }
            }
        }
        return name;
    }

	public String getmAppCurVersionName() {
		return mAppCurVersionName;
	}
    /**
     * 匹配站点
     */
	public  String matchingSite(List<CityOpen> cityOpens ,String locationCity,String locationPro,LatLng userPoint){
		String stieCity = null;
		CityOpen openPro = null;
		List<CityOpen> proContainCitys = new ArrayList<CityOpen>();;
		for(CityOpen co : cityOpens){
			if(co.getLevel()==2){
				if(co.getName().equals(locationCity)){
					stieCity = co.getName();
				}
			}
			if(co.getLevel()==1){
				if(co.getName().equals(locationPro)){
					openPro = co;
				}
			}
		}
		if(!TextUtils.isEmpty(stieCity)){
			mNoSite = false;
			return stieCity;
		}else{
			if(openPro!=null){
				for(CityOpen co : cityOpens){
					if(co.getParent_id() == openPro.getId()){
						proContainCitys.add(co);
					}
				}
				if(proContainCitys!=null&&proContainCitys.size()>0){
					if(proContainCitys.size() == 1){
						//该省只有一个开通城市
						mNoSite = false;
						return proContainCitys.get(0).getName();  
					}else{
						//根据距离计算站点
						String px;
						String py;
						HashMap<Double, CityOpen> map = new HashMap<Double, CityOpen>();
						double[] distanceArray = null;
						ArrayList<Double> arrayList = new ArrayList<Double>();
						for(int i=0;i<proContainCitys.size();i++){
						//for(CityOpen pct : proContainCitys){
							px = proContainCitys.get(i).getArea_px();
							py = proContainCitys.get(i).getArea_py();
						
								double dpx = 0.0;
								double dpy = 0.0;
								try {
									dpx = Double.valueOf(px);
									dpy = Double.valueOf(py);
								} catch (Exception e) {
									// TODO: handle exception
									dpx = 0.0;
									dpy = 0.0;
								}
									Double mapKey = DistanceUtil. getDistance(new LatLng(dpy, dpx), userPoint);
									java.text.DecimalFormat df  = new java.text.DecimalFormat("#.######");
									map.put(Double.valueOf(df.format(mapKey)), proContainCitys.get(i));
								
								
							
						}
						for(Map.Entry<Double, CityOpen> entry:map.entrySet()){    
							arrayList.add(entry.getKey());
						     System.out.println(entry.getKey()+"--->"+entry.getValue());    
						}   
						if(arrayList.size()>0&&arrayList!=null){
							for(Double d : arrayList){
								LogUtil.e("a====sort:"+d);
							}
							bubble(arrayList);
							for(Double a : arrayList){
								LogUtil.e("b====sort:"+a);
							}
							mNoSite = false;
 							return map.get(arrayList.get(0)).getName();
						}else{
							mNoSite = false;
							return proContainCitys.get(0).getName();
						}
					}
				}
			}
			mNoSite = true;
			return locationCity;
		}
	}
	
    /**
     * 接口-地理编码监听器
     * 
     */
    public class MyOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult arg0) {
        	mUserPoint = arg0.getLocation();
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {

        }

    }

	public boolean ismNoSite() {
		return mNoSite;
	}

	public void setmNoSite(boolean mNoSite) {
		this.mNoSite = mNoSite;
	}
    
	/*  
	 * 冒泡排序  
	 */  
    public void bubble(ArrayList<Double> list){  
        for(int i=0;i<list.size();i++){  
            for(int j=0;j<list.size()-1-i;j++){  
                if(list.get(j)>list.get(j+1)){   //如果后一个数小于前一个数交换  
                    double tmp =list.get(j);  
                    list.set(j, list.get(j+1));  
                    list.set(j+1, tmp);  
                }  
            }  
        }  
    }  

}
