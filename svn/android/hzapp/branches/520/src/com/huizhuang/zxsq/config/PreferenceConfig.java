package com.huizhuang.zxsq.config;

import java.util.Date;
import java.util.Set;

import android.content.Context;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;

/**
 * @ClassName: AppConfig
 * @Description: 应用运行配置参数
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月9日  下午5:13:33
 */
public class PreferenceConfig{

	public static final String PREFERENCES_FILE = AppConfig.APP_NAME;

	/** 上次安装应用版本 */
	public static final String APP_LAST_VERSION_CODE = "app_last_version_code";

	public static final String APP_NAME = "lgmsharebase";
	public static final String IS_USE = "isUse";
	/** 失效时间-自登录起14天后 */
	public static final String FAILURE_TIME = "failure_time";
	/** 上次安装应用版本 */
	public static final String APP_LAST_VER = "app_last_ver";
	/** 应用唯一标识 */
	public static final String APP_UNIQUEID = "app_uniqueid";
	/**是否自动弹出保障页浮层*/
	public static final String APP_NEED_AUTO_POP_GUARANTEE = "app_need_auto_pop_guarantee";
	/**APP是否已评过分*/
	public static final String APP_ALREADY_EVALUATED = "app_already_evaluated";
	/**根据订单状态来确定分享的文案*/
	public static final String SHARE_BY_ORDER_STATE = "share_by_order_state";
	
	public static final String LAST_SAVE_TIME = "lastSaveTime";

	public static final String USER_USERNAME = "username";

	public static final int TIME_DELAY_GUIDE = 2000;
	public static final int TIME_DELAY_WELCOME = 2000;

	public static final int LEFT_MENU_OAGE_YIXIN = 0;
	public static final int LEFT_MENU_OAGE_FRIDENT = 1;
	
	public static final String WIFI_LOAD_IMAGE_TAG = "wifi";
	
	public static final String MY_SAFEGUARD="mySafeguard";

	/** 获取安装版本号 */
	public static int getAppLastVersionCode(Context context) {
		return PrefersUtil.getInstance().getIntValue(APP_LAST_VERSION_CODE, 0);
	}

	/** 保存安装版本号 */
	public static void saveAppLastVersionCode(Context context, int versionCode) {
		PrefersUtil.getInstance().setValue(APP_LAST_VERSION_CODE, versionCode);
	}

	/** 获取应用唯一标识号 */
	public static String getAppUniqueID(Context context) {
		return PrefersUtil.getInstance().getStrValue(APP_UNIQUEID);
	}

	/** 保存应用唯一标识号 */
	public static void saveAppUniqueId(Context context, String uniqueId) {
		PrefersUtil.getInstance().setValue(APP_UNIQUEID, uniqueId);
	}

	/**获取用户上一次登录账户*/
	public static String getUsername(Context context) {
		return PrefersUtil.getInstance().getStrValue(USER_USERNAME);
	}

	/**保存用户登录账户*/
	public static void saveUsername(Context context, String username) {
		PrefersUtil.getInstance().setValue(USER_USERNAME, username);
	}
	
	public static boolean isValidatePhone(Context context, String username){
		return PrefersUtil.getInstance().getBooleanValue("phone_validate"+username, true);
	}
	
	public static void saveValidatePhone(Context context, String username, boolean isValidate){
		PrefersUtil.getInstance().setValue("phone_validate"+username, isValidate);
	}
	
	public static void writeWIFILoadImageSwitch(Context context,boolean isOpen) {
		PrefersUtil.getInstance().setValue(WIFI_LOAD_IMAGE_TAG, isOpen);
	}
	
	public static boolean readWIFILoadImageSwitch(Context context) {
		return PrefersUtil.getInstance().getBooleanValue(WIFI_LOAD_IMAGE_TAG, false);
	}
	//得到当前APP是否需要自动弹窗
	public static boolean getNeedAutoPopGuarantee(){
		return PrefersUtil.getInstance().getBooleanValue(APP_NEED_AUTO_POP_GUARANTEE, false); 	
	}
	/**保存当前APP是否需要自动弹窗*/
	public static void setNeedAutoPopGuarantee(boolean isNeed){
		PrefersUtil.getInstance().setValue(APP_NEED_AUTO_POP_GUARANTEE, isNeed);
	}
	/**保存当前APP是否已经评过分*/
	public static void setAppAlreadyEvaluated(boolean isAlready){
		PrefersUtil.getInstance().setValue(APP_ALREADY_EVALUATED, isAlready);
	}
	/**得到当前APP是否已经评过分*/
	public static boolean getAppAlreadyEvaluated(){
		return PrefersUtil.getInstance().getBooleanValue(APP_ALREADY_EVALUATED, false);
	}
	/**设置订单的状态，以便确定分享文案*/
	public static void setShareByOrderState(int orderState){
		PrefersUtil.getInstance().setValue(SHARE_BY_ORDER_STATE, orderState);
	}
	/**得到订单的状态，从而确定分享文案*/
	public static int getShareByOrderState(){
		return PrefersUtil.getInstance().getIntValue(SHARE_BY_ORDER_STATE, AppConstants.SHARE_BY_ORDER_COMMON);
	}
	public static boolean readIsFirstUse(Context context) {
		return PrefersUtil.getInstance().getBooleanValue(IS_USE, false);
	}

	public static void writeIsFirstUse(Context context) {
		PrefersUtil.getInstance().setValue(IS_USE, true);
	}

	public static String readFailureTime() {
		return PrefersUtil.getInstance().getStrValue(FAILURE_TIME);
	}

	public static void writeFailureTime() {
		PrefersUtil.getInstance().setValue(FAILURE_TIME, DateUtil.dateToStrShort(DateUtil.getNextDay(new Date(System.currentTimeMillis()), 14)));
	}
	
	
	public static void setMySafeguard(String orderId){
		Set<String> mySafeguard=PrefersUtil.getSavedStringList(ZxsqApplication.getInstance(), MY_SAFEGUARD);
		if(!mySafeguard.contains(orderId)){
			mySafeguard.add(orderId);
			PrefersUtil.saveStringList(ZxsqApplication.getInstance(), mySafeguard, MY_SAFEGUARD);
		}
	}
	
	public static Set<String> getMySafeguard(){
		return PrefersUtil.getSavedStringList(ZxsqApplication.getInstance(), MY_SAFEGUARD);
	}
	
	public static boolean isMySafeguardContainsId(String orderId){
		Set<String> mySafeguard=PrefersUtil.getSavedStringList(ZxsqApplication.getInstance(), MY_SAFEGUARD);
		return mySafeguard.contains(orderId);
	}

	/** 删除 */
//	public static void clearData(Context context) {
//		PrefersUtil.getInstance().clearData();
//	}
}