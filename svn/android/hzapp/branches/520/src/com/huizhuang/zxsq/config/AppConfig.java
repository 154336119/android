package com.huizhuang.zxsq.config;

/**
 * @ClassName: AppConfig
 * @Description: 应用基本配置参数
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午11:12:20
 */
public class AppConfig {
	// /////////////////应用发布时修改////////////////////////
	/** 应用名称 */
	public static final String APP_NAME = "zxsq";
	/** 应用BUILD版本号 */
	public static final String VERSION_BUILD = "1.0";
	/** 应用调试模式 */
	public static final boolean DEBUG_MODE = true;
	/** 发送carsh报告 */
	public static final boolean SEND_CARSH = false;
	/** splash加载延迟等待时间 */
	public static final int SPLASH_TIME_DELAY = 2 * 1000;
	/** http连接url */
    //public static final String HTTP_BASE_URL = "http://app.huizhuang.com/v2.2/";
	public static final String HTTP_BASE_URL = "http://app.rls.huizhuang.com/";
	//public static final String HTTP_BASE_URL = "http://192.168.5.9:8009/v1.2/";
	// /////////////////应用开发时配置////////////////////////
	/** 客户端的DES 加密的key */
	public static final String MD5_KEY = "huizhuanggougou";
	/** 百度地图AK,应用签名包名绑定 */
	public static final String BAIDU_MAP_AK = "697f50541f8d4779124896681cb6584d";
	public static final String SHARE_URL = "http://app.huizhuang.com/index.php?module=index&file=index&op=transfer";
	/** SD卡文件存储根目录 */
	public static final String FILE_ROOT_URL = "/base/";
	/** 升级包保存路径 */
	public static final String UPDATE_APK_SAVE_PATH = FILE_ROOT_URL + "/udpate/";
	/** 日志保存路径 */
	public static final String LOG_PATH = FILE_ROOT_URL + "/log/";
	public static final String CACHE_xPATH = "/files/";
	public static final String PICTURE_PATH = FILE_ROOT_URL + "/image/";
	/** 在多少天内不检查升级 */
	public static final int defaultMinUpdateDay = 7;
	/** 每页加载数据大小 */
	public static final int pageSize = 10;
	/** 分页请求页数从1开始 */
	public static final int DEFAULT_START_PAGE = 1;
	/** 默认线程请求延时1秒钟 */
	public static final int DEFAULT_THREAD_DELAY_MILLIS = 1000;
	
	public static final String SPLASH_IMAGE_NAME="splash.jpg";
	
	public static final String NOTIFICATION_DIARY_TITLE="好友日记更新通知";
	
//	public static final String URL_SHARE_ATLAS="http://m.huizhuang.com/index.php?module=new&file=sketch&op=detail&";
//	public static final String URL_SHARE_COMPANY="http://m.huizhuang.com/index.php?module=new&file=store&op=detail&store_id=";
//	public static final String URL_SHARE_DIARY="http://m.huizhuang.com/index.php?module=new&file=diary&op=detail&diary_id=";

}
