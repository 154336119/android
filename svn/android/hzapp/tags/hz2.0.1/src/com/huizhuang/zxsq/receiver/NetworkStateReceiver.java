package com.huizhuang.zxsq.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.receiver.NetChangeObserver.NetType;
import com.huizhuang.zxsq.utils.LogUtil;

/**
 * @Title NetworkStateReceiver
 * @Package com.huizhuang.zxsq.receiver
 * @Description 是一个检测网络状态改变的，需要配置 
 * 				<receiver android:name="com.huizhuang.zxsq.receiver.NetworkStateReceiver" >
 *              <intent-filter> 
 *              	<action android:name="android.net.conn.CONNECTIVITY_CHANGE" /> 
 *              	<action android:name="lgmshare.android.net.conn.CONNECTIVITY_CHANGE" />
 *              </intent-filter> 
 *              </receiver>
 * 
 *              需要开启权限 
 *              <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
 *              <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 *              <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *              <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * @author lgmshare
 * @date 2013-5-5 下午 22:47
 * @version V1.2
 */
public class NetworkStateReceiver extends BroadcastReceiver {

	private static final String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private static final String LGMSHARE_ANDROID_NET_CHANGE_ACTION = "lgmshare.android.net.conn.CONNECTIVITY_CHANGE";

	private static BroadcastReceiver receiver;
	private static ArrayList<NetChangeObserver> netChangeObserverArrayList = new ArrayList<NetChangeObserver>();
	private static Boolean networkAvailable = false;
	private static NetType netType;

	private static BroadcastReceiver getReceiver() {
		if (receiver == null) {
			receiver = new NetworkStateReceiver();
		}
		return receiver;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.e("NetworkStateReceiver", "NetworkStateReceiver===ConnectivityManager.CONNECTIVITY_ACTION");
		receiver = NetworkStateReceiver.this;
		if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION) || intent.getAction().equalsIgnoreCase(LGMSHARE_ANDROID_NET_CHANGE_ACTION)) {
 
			LogUtil.d("网络状态改变.");
			
			if (!isNetworkAvailable(context)) {        
				LogUtil.d("没有网络连接.");
				netType = NetType.noneNet;    
				ZxsqApplication.getInstance().setWifiConnectNetWork(false);
				networkAvailable = false;
			} else {
				LogUtil.d("网络连接成功.");
				netType = getNetworkType(context);
				if(netType==NetType.wifi){
					ZxsqApplication.getInstance().setWifiConnectNetWork(true);
				}else{
					ZxsqApplication.getInstance().setWifiConnectNetWork(false);
				}
				networkAvailable = true;
			}
			notifyObserver();
		}
	}

	/**
	 * 注册网络状态广播
	 * 
	 * @param mContext
	 */
	public static void registerNetworkStateReceiver(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(LGMSHARE_ANDROID_NET_CHANGE_ACTION);
		filter.addAction(ANDROID_NET_CHANGE_ACTION);
		context.getApplicationContext().registerReceiver(getReceiver(), filter);
	}

	/**
	 * 注销网络状态广播
	 * 
	 * @param mContext
	 */
	public static void unRegisterNetworkStateReceiver(Context context) {
		if (receiver != null) {
			try {
				context.getApplicationContext().unregisterReceiver(receiver);
			} catch (Exception e) {
				LogUtil.e(e.getMessage());
			}
		}
	}

	private void notifyObserver() {
		for (int i = 0; i < netChangeObserverArrayList.size(); i++) {
			NetChangeObserver observer = netChangeObserverArrayList.get(i);
			if (observer != null) {
				if (isNetworkAvailable()) {
					observer.onConnect(netType);
				} else {
					observer.onDisConnect();
				}
			}
		}
	}

	/**
	 * 注册网络连接观察者
	 * 
	 * @param observerKey
	 *            observerKey
	 */
	public static void registerObserver(NetChangeObserver observer) {
		if (netChangeObserverArrayList == null) {
			netChangeObserverArrayList = new ArrayList<NetChangeObserver>();
		}
		netChangeObserverArrayList.add(observer);
	}

	/**
	 * 注销网络连接观察者
	 * 
	 * @param resID
	 *            observerKey
	 */
	public static void removeRegisterObserver(NetChangeObserver observer) {
		if (netChangeObserverArrayList != null) {
			netChangeObserverArrayList.remove(observer);
		}
	}

	public static NetType getNetworkType() {
		return netType;
	}
	
	/**
	 * 获取当前网络状态，true为网络连接成功，否则网络连接失败
	 * 
	 * @return
	 */
	public static Boolean isNetworkAvailable() {
		return networkAvailable;
	}

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();//获取设备支持的所有网络类型的连接状态信息
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 检查网络状态
	 * 
	 * @param mContext
	 */
	public static void checkNetworkState(Context mContext) {
		Intent intent = new Intent();
		intent.setAction(LGMSHARE_ANDROID_NET_CHANGE_ACTION);
		intent.setAction(ANDROID_NET_CHANGE_ACTION);
		mContext.sendBroadcast(intent);
	}
	
	/**
	 * 
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap 网络3：net网络
	 * 
	 * @param context
	 * @return
	 */
	public static NetType getNetworkType(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();//获取当前连接可用的网络
		if (networkInfo == null) {
			return NetType.noneNet;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				return NetType.CMNET;
			} else {
				return NetType.CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			return NetType.wifi;
		}
		return NetType.noneNet;

	}
}
