package com.huizhuang.zxsq.utils;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @ClassName: NetworkUtil
 * @Description: 网路检查工具
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月3日  下午4:03:58
 */
public class NetworkUtil {

	///////记得要加权限 android.permission.ACCESS_NETWORK_STATE
	/**
	 * 检测当前时候是否有可用网络
	 * 
	 * @param context
	 * @return 返回true，当前有可用网络 返回false，当前无可用网络
	 */
	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 监测URL地址是否有效
	 * 
	 * @param url
	 * @return
	 */
	public static boolean checkURL(String url){
		try {
			URL u = new URL(url);
			HttpURLConnection urlConn = (HttpURLConnection)u.openConnection();
			urlConn.connect();
			if(urlConn.getResponseCode()==HttpURLConnection.HTTP_OK){
				return true;
			}
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
		}
        return false;
	}
	
    /**
     * 网络连接类型
     * 
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.getType();
        }
        return -1;
    }
    
    /**
	 * 判断是否为WIFI网络 
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否为GPRS网络 
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGprs(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }
    
}
