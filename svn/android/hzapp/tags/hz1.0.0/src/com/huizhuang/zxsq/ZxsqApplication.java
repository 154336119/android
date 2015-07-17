package com.huizhuang.zxsq;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.exception.CrashException;
import com.huizhuang.zxsq.http.bean.CityDistrict;
import com.huizhuang.zxsq.http.bean.Constant;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.receiver.NetChangeObserver;
import com.huizhuang.zxsq.receiver.NetChangeObserver.NetType;
import com.huizhuang.zxsq.receiver.NetworkStateReceiver;
import com.huizhuang.zxsq.service.UMengPushIntentService;
import com.huizhuang.zxsq.ui.activity.NetworkStateActivity;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.FileUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.utils.StringEncodeUtil;
import com.lgmshare.http.netroid.Network;
import com.lgmshare.http.netroid.RequestQueue;
import com.lgmshare.http.netroid.stack.HttpClientStack;
import com.lgmshare.http.netroid.stack.HttpStack;
import com.lgmshare.http.netroid.stack.HurlStack;
import com.lgmshare.http.netroid.toolbox.BasicNetwork;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.sharesdk.framework.ShareSDK;

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
    private LatLng mUserPoint; //定位坐标
    private String mLocationCity;//定位城市
    private String mDistrict; //定位区县
    private LocationClient mLocationClient;
    //选择城市
    private String selectCity;
    //开通城市列表
    private List<CityDistrict> cityDistricts;

    private List<CityDistrict> citys;

    public static ZxsqApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化sharedsdk
        ShareSDK.initSDK(this);
        mInstance = this;
        mRequestQueue = newRequestQueue(mInstance, null);
        mStackManager = ZxsqActivityManager.getInstance();
        initExceptionHandler();
        initNetworkObserver();
        initAppVsersionInfo();
        initImageLoader(getApplicationContext());
        initLocation();

        getCloseLoadImgWhenNotInWifiState();
        initConstant();
        initUMengMessage();

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
        mPushAgent.setPushIntentServiceClass(UMengPushIntentService.class);

        mDeviceToken = mPushAgent.getRegistrationId();
        LogUtil.i("deviceToken:" + mDeviceToken);
    }

    /**
     * 从本地或者资产目录获取常量
     */
    private void initConstant() {
        mConstant = (Constant) FileUtil.readFile(mInstance, "Constant");
        if (mConstant == null) {
            FileUtil.readFileFromAssets(mInstance, "Constant");
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
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int maxImageMemoryCacheSize = (maxMemory == 0) ? ImageLoaderOptions.MAX_IMAGE_MEMORY_CACHE_SIZE : (maxMemory / 8);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(applicationContext).memoryCache(new LruMemoryCache(maxImageMemoryCacheSize))
                .memoryCacheExtraOptions(ImageLoaderOptions.MAX_IMAGE_WIDTH, ImageLoaderOptions.MAX_IMAGE_HEIGHT).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().defaultDisplayImageOptions(ImageLoaderOptions.optionsDefaultEmptyPhoto)
                .diskCacheSize(ImageLoaderOptions.MAX_IMAGE_DISK_CACHE_SIZE).diskCacheFileCount(ImageLoaderOptions.MAX_IMAGE_DISK_FILE_COUNT)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();

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
        if (info == null)
            info = new PackageInfo();
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
        if (mUser == null)
            mUser = new User();
        return mUser;
    }

    public void setUser(User user) {
        LogUtil.i("user:" + user);
        if (user != null && user.getToken() != null) {
            PrefersUtil.getInstance().setValue("userId", user.getId());
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
            PrefersUtil.getInstance().setValue("userId", user.getId());
            PrefersUtil.getInstance().setValue("token", user.getToken());
            setLogged(true);
        } else {
            setLogged(false);
        }
        mUser = user;
    }

    public boolean isLogged() {
        if (getUser() == null || TextUtils.isEmpty(getUser().getId())) {
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

    public Constant getConstant() {
        return mConstant;
    }

    public void setConstant(Constant mConstant) {
        this.mConstant = mConstant;
    }

    /**
     * 初始化地图
     */
    public void initLocation() {
        //初始化百度地图sdk
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
            mLocationCity = arg0.getCity();
            mDistrict = arg0.getDistrict();
            LogUtil.d("locationCity:" + mLocationCity);
            mUserPoint = new LatLng(arg0.getLatitude(), arg0.getLongitude());
            mLocationClient.stop();
        }

        @Override
        public void onReceivePoi(BDLocation arg0) {
        }

    }

    public LatLng getUserPoint() {
        if (mUserPoint != null) {
            return mUserPoint;
        } else {
            return null;
        }
    }

    public String getLocationCity() {
        if (mLocationCity != null) {
            return mLocationCity;
        } else {
            return null;
        }
    }

    public String getLocationDistrict() {
        return mDistrict;
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
        setCloseLoadImgWhenNotInWifi(PreferenceConfig.readWIFILoadImageSwitch(getApplicationContext()));
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

    public List<CityDistrict> getCityDistricts() {
        return cityDistricts;
    }

    public void setCityDistricts(List<CityDistrict> cityDistricts) {
        this.cityDistricts = cityDistricts;
        if (cityDistricts != null) {
            citys = new ArrayList<CityDistrict>();
            for (CityDistrict cityDistrict : cityDistricts) {
                if (cityDistrict.getLevel() == 2) {
                    cityDistrict.setDistrict(new ArrayList<CityDistrict>());
                    citys.add(cityDistrict);
                }
            }
            for (CityDistrict cityDistrict : cityDistricts) {
                if (cityDistrict.getLevel() == 3) {
                    for (CityDistrict cd : citys) {
                        if (cityDistrict.getParent_id() == cd.getId()) {
                            cd.getDistrict().add(cityDistrict);
                        }
                    }
                }
            }
        }
    }

    public List<CityDistrict> getCitys() {
        return citys;
    }

    public List<CityDistrict> getDistricts(String city) {
        if (city != null && citys != null)
            for (CityDistrict cd : citys) {
                if (city.equals(cd.getName())) {
                    return cd.getDistrict();
                }
            }
        return null;
    }

    public CityDistrict getCity(String city) {
        if (city != null && citys != null)
            for (CityDistrict cd : citys) {
                if (city.equals(cd.getName())) {
                    return cd;
                }
            }
        return null;
    }
}
