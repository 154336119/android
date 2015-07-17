package com.huizhuang.zxsq.http;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.http.parser.base.BaseParser;
import com.huizhuang.zxsq.security.Security;
import com.huizhuang.zxsq.utils.LogUtil;
import com.lgmshare.http.netroid.DefaultRetryPolicy;
import com.lgmshare.http.netroid.Request;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.RequestQueue;
import com.lgmshare.http.netroid.request.ImageRequest;

import java.util.concurrent.TimeUnit;

/**
 * @author lim
 * @ClassName: HttpClientApi
 * @Description: 网络请求api
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午11:21:26
 */
public class HttpClientApi {
    
    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 10 * 1000;
    

    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 1;

    /** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    public static final String BASE_URL = AppConfig.HTTP_BASE_URL;
    /**
     * ************************************************************
     * *******************惠装APP功能接口*************************
     * **************************************************************
     * *************************************************************
     */
    // ====================通用相关接口====================//
    public static final String REQ_VERSION_INFO = "common/app_version/get_hz_version.json";
    public static final String REQ_SPLASH_IMG = "index/index/splashScreen.json";
    /**
     * 常量
     */
    public static final String REQ_CONSTANTS = "common/constants/index.json";
    /**
     * 自动登录
     */
    public static final String USER_LOGIN_BY_TOKEN_AND_USER_ID = "user/user/profile.json";
    /**
     * 订单列表
     */
    public static final String REQ_GET_ORDER_ORDER_OLIST = "order/order/olist.json";
    // 搜索小区
    public static final String URL_COMMON_HOUSING_LIST = "/common/housing/hlist.json";
    // 上传图片
    public static final String URL_COMMON_UPLOAD_IMAGE = "/common/upload/add_image.json";
    // 顶
    public static final String URL_FOREMAN_COMMENTS_UP = "/comment/comment/up.json";
    // 踩
    public static final String URL_FOREMAN_COMMENTS_DOWN = "/comment/comment/down.json";

    public static final String URL_ORDER_APPOINTMENT_OR_MEASURE = "/order/order/update_statu.json";

    public static final String URL_GET_ORDER = "/order/order/olist.json";
    /**
     * 用户使用短信登录
     */
    public static final String REQ_USER_LOGIN_MESSAGE = "/login/login/quick_login.json";
    // 注册协议
    public static final String REGISTRATION_AGREEMENT =
            "http://m.huizhuang.com/registration_agreement.html";
    // 使用手机号+验证码登录时请求得到验证码
    public static final String URL_LOGIN_MESSAGE_VERIFY_CODE = "/login/login/quick_login_sms.json";

    public static final String REQ_SAVE_INFO = "user/user/saveInfo.json";
    // 搜索小区
    public static final String HOUSE_SOLUTION = "/common/housing/hlist.json";
    // 工长列表
    public static final String FOREMEN_LIST = "/foreman/foreman/get_formen_list.json";
    // 工长详情
    public static final String FOREMAN_GET_DETAILS = "/foreman/foreman/get_formen_details.json";
    //工长口碑
    public static final String FOREMAN_COMMENT_LIST ="/foreman/foreman/complete_comment_list.json"; 
    //工长口碑详情
    public static final String FOREMAN_COMMENT_DETAIL ="/foreman/foreman/comment_datail.json"; 
    //获取当前手机号
    public static final String URL_ORDER_GET_MOBILE = "/order/order/get_current_mobile.json";
    // 全国所有省市区（层级）
    public static final String URL_CONSTANTS_AREAS = "/common/constants/areas.json";
    // 开通的省市区
    public static final String URL_CONSTANTS_AREAS_OPEN = "/common/constants/open_areas.json";
    // 判断手机号是否注册
    public static final String URL_ORDER_CHECK_PHONE = "/order/order/is_mobile_registed.json";
    // 发送验证码
    public static final String URL_ORDER_SEND_VIREFY_CODE = "/order/order/send_virefy.json";
    //验证验证码
    // 验证验证码
    public static final String URL_ORDER_SEND_CHECK_CODE_UNLOGIN = "/order/order/verify_sms_login_code.json";
    // 完善订单
    public static final String URL_ORDER_ADD_INFO = "/order/order/add_info.json";
    // 施工现场列表
    public static final String SOLUTION_CASE_LIST = "/showcase/showcase/case_list.json";
    // 施工现场阶段列表
    public static final String SOLUTION_LIST = "/showcase/showcase/subcase.json";
    // 施工现场详情头部
    public static final String SOLUTION_DETAIL_HEAD = "/showcase/showcase/index.json";
    // 根据装修阶段得到装修宝典文章列表
    public static final String URL_GET_ZXBD_LIST_BY_STAGE = "/cms/cms/zxbooklist.json";
    // 根据ID得到装修宝典文章
    public static final String URL_GET_ARTICLE_BY_ID = "/cms/cms/zxbookdetail.json";
    // 立即预约
    public static final String URL_ORDER_SUBMIT = "/order/order/add_order.json";
    // 等待工长应答
    public static final String URL_ORDER_DETAIL = "/order/order/order_detail.json";
    // 取消订单原因
    public static final String URL_ORDER_CANCEL = "/order/order/cancel_reson.json";
    // 取消订单
    public static final String ORDER_CANCEL_ORDER = "/order/order/cancel_order.json";
    // 量房评价列表
    public static final String ORDER_MEASURING_LIST = "/order/order/measuring_list.json";
    // 量房评价提交
    public static final String ORDER_MEASURING_PJ = "/order/order/measuring_pj.json";
    // 签约工长列表
    public static final String BOOK_BOOK_LIST = "/book/book/book_list.json";
    // 取消预约签约原因列表
    public static final String BOOK_CANCEL_BOOK = "/book/book/cancel_book_reson.json";
    // 取消预约签约
    public static final String BOOK_CANCEL_BOOK_RESON = "/book/book/cancel_book.json";
    // 签约工长
    public static final String BOOK_BOOK_STORE = "/book/book/book_store.json";
    // 报价清单
    public static final String URL_QUOTE_GET_PRICE_SHEET = "/quote/quote/get_price_sheet.json";
    /**
     * ************************************************************ *******************惠装APP
     * 2.0功能接口*************************
     * **************************************************************
     * *************************************************************
     */
    public static final String URL_COST_CHANGE = "/check/check/build_cost.json";
    // 各阶段用户发表评价
    public static final String COMMENT_COMMENT_ADD = "/comment/comment/add.json";
    // 各阶段验收信息
    public static final String CHECK_CHECK_STAGE_INFO = "/check/check/stage_info.json";
    // 获取阶段对应评价项
    public static final String COMMENT_GET_COMMENT_INFO = "/comment/comment/getCommentInfo.json";
    // 各阶段验收通过
    public static final String CHECK_CHECK_CALIDATION = "/check/check/validation.json";
    // 各阶段验收不通过
    public static final String CHECK_CHECK_RECTIFY = "/check/check/rectify.json";
    // 支付信息
    public static final String CHECK_GET_PAY_INFO = "/pay/receive/pay_info.json";
    // 延期变更单
    public static final String CHECK_GET_DELAY_ORDER = "/check/check/build_delay.json";
    // 获取支付相关数据
    public static final String CHECK_GET_PAY_DATA = "/pay/receive/pay_money.json";
    // 各阶段整改
    public static final String CHECK_CHECK_STAGE_LIST = "/check/check/stage_list.json";
    // pos机检查
    public static final String CHECK_POS = "pay/receive/pos_check.json";
    // 检查是否银联支付成功
    public static final String CHECK_PAY_SUCCESS = "pay/receive/pos_check.json";

    // 活动
    public static final String ADVERTISE = "/ad/ad/bargain_price.json";
    // 投诉节点列表
    public static final String DISPUTE_NODE = "/dispute/dispute/dispute_node.json";
    // 当前节点是否有投诉
    public static final String DISPUTE_INFO = "/dispute/dispute/dispute_info.json";
    // 投诉分类
    public static final String DISPUTE_CATEGORY = "/dispute/dispute/dispute_category.json";
    // 提交投诉
    public static final String DISPUTE_ADD = "/dispute/dispute/dispute_add.json";

    public static BuildStringRequest get(String url, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams();
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BuildStringRequest request =
                new BuildStringRequest(Request.Method.GET, getAbsoluteUrl(url), params, callBack);
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        setUseCache(request);
        queue.add(request);
        return request;
    }

    public static BuildStringRequest get(String url, RequestParams params,
            RequestCallBack<String> callBack) {
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BuildStringRequest request =
                new BuildStringRequest(Request.Method.GET, getAbsoluteUrl(url), params, callBack);
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        setUseCache(request);
        queue.add(request);
        return request;
    }

    public static BuildStringRequest post(String url, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams();
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BuildStringRequest request =
                new BuildStringRequest(Request.Method.POST, getAbsoluteUrl(url), params, callBack);
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        queue.add(request);
        return request;
    }

    public static BuildStringRequest post(String url, RequestParams params,
            RequestCallBack<String> callBack) {
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BuildStringRequest request =
                new BuildStringRequest(Request.Method.POST, getAbsoluteUrl(url), params, callBack);
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        queue.add(request);
        LogUtil.e("request params=[" + params.toString() + "]");
        return request;
    }

    public static <T> BeanRequest<T> post(String url, BaseParser<T> parser,
            RequestCallBack<T> callBack) {
        RequestParams params = new RequestParams();
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BeanRequest<T> request =
                new BeanRequest<T>(Request.Method.POST, getAbsoluteUrl(url), params, parser,
                        callBack);
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        queue.add(request);
        return request;
    }

    public static <T> BeanRequest<T> post(String url, RequestParams params, BaseParser<T> parser,
            RequestCallBack<T> callBack) {
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BeanRequest<T> request =
                new BeanRequest<T>(Request.Method.POST, getAbsoluteUrl(url), params, parser,
                        callBack);
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        queue.add(request);
        LogUtil.e("request params=[" + params.toString() + "]");
        return request;
    }

    public static <T> BeanRequest<T> get(String url, BaseParser<T> parser,
            RequestCallBack<T> callBack) {
        RequestParams params = new RequestParams();
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BeanRequest<T> request =
                new BeanRequest<T>(Request.Method.GET, getAbsoluteUrl(url), params, parser,
                        callBack);
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        setUseCache(request);
        queue.add(request);
        return request;
    }

    public static <T> BeanRequest<T> get(String url, RequestParams params, BaseParser<T> parser,
            RequestCallBack<T> callBack) {
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BeanRequest<T> request =
                new BeanRequest<T>(Request.Method.GET, getAbsoluteUrl(url), params, parser,
                        callBack);
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        queue.add(request);
        LogUtil.e("request params=[" + params.toString() + "]");
        return request;
    }

    public static ImageRequest get(String url, RequestCallBack<Bitmap> listener, int maxWidth,
            int maxHeight) {
        ImageRequest request = new ImageRequest(url, listener, maxWidth, maxHeight, Config.RGB_565);
        request.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        queue.add(request);
        return request;
    }

    private static RequestParams addGlobParams(RequestParams params) {
        params.remove("user_id");
        User user = ZxsqApplication.getInstance().getUser();
        if (user != null && user.getUser_id() != null && user.getToken() != null) {
            String tokenValue = user.getUser_id() + user.getToken();
            params.add("user_id", user.getUser_id());
            params.put("access_token", Security.getHMACSHA256String(tokenValue));
            params.put("token", user.getToken());
            LogUtil.i(" device_token:" + ZxsqApplication.getInstance().getDeviceToken());
            params.put("device_token", ZxsqApplication.getInstance().getDeviceToken());
        } else {
            params.put("access_token", Security.getHMACSHA256String(""));
            params.put("device_token", ZxsqApplication.getInstance().getDeviceToken());
        }
        return params;

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        LogUtil.e("request url=[" + BASE_URL + relativeUrl + "]");
        return BASE_URL + relativeUrl;
    }

    @SuppressLint("NewApi")
    private static void setUseCache(Request<?> request) {
        request.setShouldCache(true);
        request.setForceUpdate(true);
        request.addHeader("User-Agent", "huizhuang;"
                + ZxsqApplication.getInstance().getVersionName() + ";android-phone");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
            request.setCacheExpireTime(TimeUnit.DAYS, 1);
        } else {
            request.setCacheExpireTime(TimeUnit.SECONDS, 30);
        }
    }

    public static enum CntType {
        app_diary_v2, app_sketch_v2, store, app_renovation_diary, app_supervision_diary
    }

    /**
     * 业务类型, app_diary_v2:网友日记，app_user_room_info：爱家档案，app_user_journey：装修记账，comment：评论
     */
    public static enum UploadImageType {
        app_diary_v2, app_user_room_info, app_user_journey, comment,
    }

    public static enum CommentType {
        app_store, app_diary_v2
    }
}
