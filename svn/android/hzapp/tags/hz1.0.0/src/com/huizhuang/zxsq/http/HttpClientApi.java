package com.huizhuang.zxsq.http;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.module.parser.base.BaseParser;
import com.huizhuang.zxsq.security.Security;
import com.huizhuang.zxsq.utils.LogUtil;
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

    public static final String BASE_URL = AppConfig.HTTP_BASE_URL;

    /**
     * ************************************************************
     * ******************** Version-1.X功能接口*************************
     * **************************************************************
     * *************************************************************
     */
    // ====================首页相关接口====================//
    public static final String REQ_HOMEPAGE_DATA = "index/index/index.json";

    public static final String REQ_SPLASH_IMG = "index/index/splashScreen.json";

    // ====================通用相关接口====================//
    public static final String REQ_VERSION_INFO = "common/app_version/index.json";
    /**
     * 通用分享
     */
    public static final String REQ_COMMON_SHARE = "common/share/index.json";
    /**
     * 通用喜欢
     */
    public static final String REQ_COMMON_LIKE = "common/like/index.json";
    /**
     * 常量
     */
    public static final String REQ_CONSTANTS = "common/constants/index.json";
    // ===================用户登录、注册相关接口===================//
    /**
     * 用户注册
     */
    public static final String REQ_USER_REGISTE = "user/user/register.json";
    /**
     * 用户注册验证码
     */
    public static final String REQ_USER_REGISTE_SEND_CODE = "user/user/sendRegisterCode.json";
    /**
     * 用户登录
     */
    public static final String REQ_USER_LOGIN = "user/user/login.json";
    /**
     * 第三方登录
     */
    public static final String REQ_USER_LOGIN_THIRD = "user/user/openLogin.json";
    /**
     * 保存推荐好友
     */
    public static final String REQ_USER_SAVE_FRIENDS = "user/relationship/follow.json";
    /**
     * 删除好友
     */
    public static final String REQ_USER_DELETE_FRIENDS = "user/relationship/unfollow.json";
    /**
     * 获取推荐好友列表
     */
    public static final String REQ_USER_GET_FRIEND_LIST = "user/relationship/recommendFriends.json";
    /**
     * 保存猜你喜欢
     */
    public static final String REQ_USER_SAVE_STYLE = "user/user/saveStyles.json";
    /**
     * 风格列表
     */
    public static final String REQ_USER_GET_STYLE = "user/user/getStyles.json";
    /**
     * 绑定第三方账户
     */
    public static final String REQ_USER_LOGIN_BIND_THIRD = "user/user/bindSNS.json";
    /**
     * 第三方状态
     */
    public static final String REQ_USER_LOGIN_THIRD_STATUS = "user/user/snsStatus.json";
    /**
     * 解除第三方账户
     */
    public static final String REQ_USER_LOGIN_UNBIND_THIRD = "user/user/unbindSNS.json";
    /**
     * 第三方登录获取手机验证码
     */
    public static final String REQ_USER_LOGIN_BIND_THIRD_SEND_CODE = "user/user/sendVirefyCode.json";
    /**
     * 第三方登录绑定手机号
     */
    public static final String REQ_USER_LOGIN_VIREFY_PHONE = "user/user/virefyPhone.json";
    /**
     * 重置密码
     */
    public static final String REQ_USER_RESET_PASSWORD = "user/user/resetPwd.json";
    /**
     * 自动登录
     */
    public static final String USER_LOGIN_BY_TOKEN_AND_USER_ID = "user/user/profile.json";
    public static final String REQ_USER_CHANGE_PASSWORD = "user/user/changePwd.json";
    // ====================用户相关接口====================//
    /**
     * 通用收藏
     */
    public static final String REQ_COMMON_FAVORITE = "user/favorite/add.json";
    /**
     * 通用取消收藏
     */
    public static final String REQ_COMMON_FAVORITE_DEL = "user/favorite/delete.json";
    /**
     * 获取评论列表
     */
    public static final String REQ_GET_COMMENT_LIST = "comment/comment/getList.json";
    /**
     * 顶评论
     */
    public static final String REQ_UP_COMMENT = "comment/comment/up.json";
    /**
     * 踩评论
     */
    public static final String REQ_DOWN_COMMENT = "comment/comment/down.json";
    /**
     * 增加评论
     */
    public static final String REQ_ADD_COMMENT = "comment/comment/add.json";
    /**
     * 检查是否升级url
     */
    public static final String REQ_CLIENT_CHECK_UPDATE = "android_version.json";
    // ===================效果图相关接口=======================//
    /**
     * 图集列表
     */
    public static final String REQ_ATLAS_HOMEPAGE_LIST = "sketch/sketch/listSketchs.json";
    public static final String REQ_ATLAS_COMPANY_CORVER_LIST = "sketch/listAlbums.json";
    /**
     * 图集效果图列表
     */
    public static final String REQ_ATLAS_LIST = "sketch/sketch/albumDetail.json";
    // ====================公司相关接口=======================//
    /**
     * 公司列表
     */
    public static final String REQ_COMPANY_LSIT = "store/store/getStoreList.json";
    /**
     * 公司详情
     */
    public static final String REQ_COMPANY_DETAIL = "store/store/detail.json";
    /**
     * 公司监理日记
     */
    public static final String REQ_COMPANY_DIARY_JL = "store/store/supervisionDiaryList.json";
    /**
     * 公司装修日记
     */
    public static final String REQ_COMPANY_DIARY_ZX = "store/store/renovationDiaryList.json";
    // ===================日记相关接口=======================//
    /**
     * 日记列表
     */
    public static final String REQ_DIARY_LIST = "diary/diary/diaryList.json";
    /**
     * 用户日记列表
     */
    public static final String REQ_USER_DIARY_LIST = "diary/diary/userDiaryList.json";
    public static final String REQ_DIARY_ADD = "diary/diary/input.json";
    public static final String REQ_DIARY_ADD_UPLOAD_IMG = "diary/diary/upload_img.json";
    // ===================个人中心相关接口=======================//
    // 个人中心
    public static final String REQ_ACCOUNT_USER = "user/user/account.json";
    public static final String REQ_SAVE_INFO = "user/user/saveInfo.json";
    /**
     * 账户余额
     */
    public static final String REQ_GET_ACCOUNT_BALANCE = "user/user_finance/account_balance.json";   
    /**
     * 我关注
     */
    public static final String REQ_GET_FOLLOWING = "user/relationship/followings.json";
    /**
     * 关注我
     */
    public static final String REQ_GET_BE_FOLLOWING = "user/relationship/followers.json";
    /**
     * 订单列表
     */
    public static final String REQ_GET_ORDER_ORDER_OLIST = "order/order/olist.json";
    /**
     * 消息列表
     */
    public static final String REQ_GET_MESSAGE_LIST = "user/msg/message_list.json";        
    /**
     * 会话列表
     */
    public static final String REQ_GET_CHAT_LIST = "user/msg/chat_list.json"; 
    /**
     * 删除会话
     */
    public static final String REQ_DELECT_CHAR = "user/msg/delete_chat.json";    
    /**
     * 推送报价单详情 
     */
    public static final String REQ_QUOTE_DETAIL_CHAR = "quote/quote/detail.json";    
    /**    
    /**
     * 应付款确认
     */
    public static final String REQ_GET_ENSURE_TRADE = "user/user_finance/confirm_amount.json";        
    /**
     * 待点评列表
     */
    public static final String REQ_GET_COMMENTS_LIST = "user/jl/to_comment_list.json";
    /**
     * 已点评列表
     */
    public static final String REQ_GET_COMMENTS_HISTORY_LIST = "user/jl/complete_comment_list.json";
    /**
     * 监理师巡查
     */
    public static final String REQ_GET_RECORD_LIST = "user/jl/record_list.json";
    /**
     * 工长记录
     */
    public static final String REQ_GET_FOREMAN_RECORD_LIST = "user/jl/jl_showcase.json";
    /**
     * 获取监理师及工长信息
     */
    public static final String REQ_GET_STAFF_AND_STORE = "user/jl/get_staff_and_store.json";
    /**
     * 取消订单
     */
    public static final String REQ_CANCEL_ORDER = "order/order/cancel.json";
    /**
     * 标准拍照
     */
    public static final String REQ_GET_NODE_STATIC_PIC= "user/jl/nodeStaticPic.json";
    /**
     * 推荐文章列表
     */
    public static final String REQ_ARTICLE_RECOMMED_LIST = "cms/recommendList.json";
    public static final String REQ_ARTICLE_RECOMMED_DETAIL = "cms/detail.json";
    public static final String REQ_ACCOUNT_SUPERVISION_ORDER_LIST = "user/jl/orderList.json";
    /**
     * 验收监理报告
     */
    public static final String REQ_ACCOUNT_SUPERVISION_REPORT_CONFIRM = "user/jl/confirm.json";
    /**
     * 预约监理
     */
    public static final String REQ_ACCOUNT_SUPERVISION_RESERVE = "user/jl/reserve.json";
    /**
     * 节点预约状态
     */
    public static final String REQ_GET_NODE_STATUS_LIST = "user/jl/reserve_statu.json";
    /**
     * 监理师列表
     */
    public static final String REQ_ACCOUNT_SUPERVISION_LIST= "user/jl/jl_staff_list.json";
    /**
     * 监理师详情
     */
    public static final String REQ_ACCOUNT_SUPERVISION_DETAIL= "user/jl/jl_staff_info.json";
    /**
     * 工地进度
     */
    public static final String REQ_ACCOUNT_SUPERVISION_RECORDLIST = "user/jl/recordList.json";
    /**
     * 监理师评分
     */
    public static final String REQ_ACCOUNT_SUPERVISION_ADD_COMMENT = "user/jl/add_comment.json";
    /**
     * 装修维权
     */
    public static final String REQ_ACCOUNT_SUPERVISION_CURRENT_NODE_PROBLEMS = "user/jl/currentNodeProblems.json";
    /**
     * 发起维权
     */
    public static final String REQ_ACCOUNT_SUPERVISION_PROBLEM_WQ = "user/jl/problemWq.json";
    /**
     * 报告详情
     */
    public static final String REQ_ACCOUNT_SUPERVISION_RECORD_DETAIL = "user/jl/record_detail.json";

    public static final String REQ_ACCOUNT_GET_ORDER_STATUS = "user/jl/contractAttaches.json";

    public static final String REQ_ACCOUNT_UPDATE_ORDER_STATUS = "user/jl/setContractAttach.json";
    // ===================装修记账模块相关接口=======================//
    public static final String REQ_ZX_JOURNEY_LIST = "user/zx_journey/getList.json";
    public static final String REQ_ZX_JOURNEY = "user/zx_journey/index.json";
    public static final String REQ_ZX_JOURNEY_INPUT = "user/zx_journey/input.json";
    public static final String REQ_ZX_JOURNEY_SUMMARY = "user/zx_journey/summary.json";
    public static final String REQ_ZX_JOURNEY_TYPE_SUMMARY = "user/zx_journey/typeSummary.json";
    public static final String REQ_ZX_JOURNEY_MONTH_SUMMARY = "user/zx_journey/monthDetail.json";
    public static final String REQ_ZX_JOURNEY_UPLOAD_IMG = "user/zx_journey/upload_img.json";
    public static final String REQ_USER_CENTER_TYPE_TOTAL = "user/zx_journey/typeTotal.json";
    // 爱家
    public static final String REQ_USER_CENTER_GET_ROOM_INFO = "user/room_info/getRoomInfo.json";
    public static final String REQ_USER_CENTER_SAVE_ROOM_INFO = "user/room_info/saveRoomInfo.json";
    public static final String REQ_USER_CENTER_UPLOAD_IMG = "user/room_info/upload_img.json";
    // 收藏效果图
    public static final String USER_FAVORLIST_SKETCH = "user/favorite/sketches.json";
    // 收藏文章
    public static final String USER_FAVORLIST = "user/favorite/articles.json";
    // 收藏公司
    public static final String USER_FAVOR_STORES_LIST = "user/favorite/stores.json";
    // 我的消息
    public static final String MSG_LIST = "user/msg/msgList.json";
    public static final String MSG_SET_READ = "user/msg/readMsg.json";
    // 下单
    public static final String CREATE_ORDER = "order/order/create.json";
    // 注册协议
    public static final String REGISTRATION_AGREEMENT = "http://m.huizhuang.com/registration_agreement.html";

    /**
     * ************************************************************
     * ******************** Version-2.X功能接口*************************
     * **************************************************************
     * *************************************************************
     */

    public static final String URL_COMMON_UPLOAD_IMAGE = "/common/upload/add_image.json";
    public static final String URL_COMMON_HOUSING_LIST = "/common/housing/hlist.json";
    public static final String URL_COMMON_TAG_LIST = "/common/tag/index.json";
    public static final String URL_GET_ORDER = "/order/order/olist.json";
    public static final String URL_ORDER_GET_MOBILE = "/order/order/get_current_mobile.json";
    public static final String URL_ORDER_SEND_VIREFY_CODE = "/order/order/send_virefy.json";
    public static final String URL_ORDER_SEND_CHECK_CODE_UNLOGIN = "/order/order/verify_sms_code.json";
    public static final String URL_ORDER_SEND_CHECK_CODE_LOGIN = "/order/order/check_virefy.json";
    public static final String URL_ORDER_RECOMMEND_COMPANY = "/order/order/store_info.json";
    public static final String URL_ORDER_ADD_INFO = "/order/order/add_info.json";
    public static final String URL_ORDER_SUBMIT = "/order/order/order.json";
    public static final String URL_ORDER_DETAIL = "/order/order/detail.json";
    public static final String URL_ORDER_CANCEL = "/order/order/cancel.json";
    public static final String URL_ORDER_APPOINTMENT_OR_MEASURE = "/order/order/update_statu.json";
    public static final String URL_ORDER_DELETE = "/order/order/delete.json";
    public static final String URL_ORDER_CHECK_PHONE= "/order/order/is_mobile_registed.json";
    
    
    public static final String URL_FOREMAN_FAVORITE_GET_FOREMEN = "/user/favorite/getForemen.json";
    public static final String URL_FOREMAN_GET_FOREMEN = "/foreman/foreman/getForemen.json";
    public static final String URL_FOREMAN_GET_DETAILS = "/foreman/foreman/getDetails.json";
    public static final String URL_SHOWCASE_DETAIL = "/showcase/showcase/detail.json";
    public static final String URL_QUOTE_GET_PRICE_SHEET = "/quote/quote/get_price_sheet.json";
    public static final String URL_FOREMAN_FAVORITE_ADD = "/user/favorite/add.json";
    public static final String URL_FOREMAN_FAVORITE_DELETE = "/user/favorite/delete.json";
    public static final String URL_FOREMAN_COMMENTS ="/comment/comment/getList.json"; 
    public static final String URL_FOREMAN_COMMENTS_UP ="/comment/comment/up.json"; 
    public static final String URL_FOREMAN_COMMENTS_DOWN ="/comment/comment/down.json"; 
    
    public static final String URL_HOME_PAGE_lIST ="/index/index/lists.json"; 
    public static final String URL_DISPUTE_INDEX ="/dispute/dispute/index.json"; 
    public static final String URL_CONSTANTS_AREAS ="/common/constants/areas.json"; 
    
    

    public static BuildStringRequest get(String url, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams();
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BuildStringRequest request = new BuildStringRequest(Request.Method.GET, getAbsoluteUrl(url), params, callBack);
        setUseCache(request);
        queue.add(request);
        return request;
    }

    public static BuildStringRequest get(String url, RequestParams params, RequestCallBack<String> callBack) {
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BuildStringRequest request = new BuildStringRequest(Request.Method.GET, getAbsoluteUrl(url), params, callBack);
        setUseCache(request);
        queue.add(request);
        return request;
    }

    public static BuildStringRequest post(String url, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams();
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BuildStringRequest request = new BuildStringRequest(Request.Method.POST, getAbsoluteUrl(url), params, callBack);
        queue.add(request);
        return request;
    }

    public static BuildStringRequest post(String url, RequestParams params, RequestCallBack<String> callBack) {
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BuildStringRequest request = new BuildStringRequest(Request.Method.POST, getAbsoluteUrl(url), params, callBack);
        queue.add(request);
        LogUtil.e("request params=[" + params.toString() + "]");
        return request;
    }

    public static <T> BeanRequest<T> post(String url, BaseParser<T> parser, RequestCallBack<T> callBack) {
        RequestParams params = new RequestParams();
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BeanRequest<T> request = new BeanRequest<T>(Request.Method.POST, getAbsoluteUrl(url), params, parser, callBack);
        queue.add(request);
        return request;
    }

    public static <T> BeanRequest<T> post(String url, RequestParams params, BaseParser<T> parser, RequestCallBack<T> callBack) {
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BeanRequest<T> request = new BeanRequest<T>(Request.Method.POST, getAbsoluteUrl(url), params, parser, callBack);
        queue.add(request);
        LogUtil.e("request params=[" + params.toString() + "]");
        return request;
    }

    public static <T> BeanRequest<T> get(String url, BaseParser<T> parser, RequestCallBack<T> callBack) {
        RequestParams params = new RequestParams();
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BeanRequest<T> request = new BeanRequest<T>(Request.Method.GET, getAbsoluteUrl(url), params, parser, callBack);
        setUseCache(request);
        queue.add(request);
        return request;
    }

    public static <T> BeanRequest<T> get(String url, RequestParams params, BaseParser<T> parser, RequestCallBack<T> callBack) {
        params = addGlobParams(params);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        BeanRequest<T> request = new BeanRequest<T>(Request.Method.GET, getAbsoluteUrl(url), params, parser, callBack);
        queue.add(request);
        LogUtil.e("request params=[" + params.toString() + "]");
        return request;
    }

    public static ImageRequest get(String url, RequestCallBack<Bitmap> listener, int maxWidth, int maxHeight) {
        ImageRequest request = new ImageRequest(url, listener, maxWidth, maxHeight, Config.RGB_565);
        RequestQueue queue = ZxsqApplication.getInstance().getRequestQueue();
        queue.add(request);
        return request;
    }

    private static RequestParams addGlobParams(RequestParams params) {
        params.remove("user_id");
        User user = ZxsqApplication.getInstance().getUser();
        if (user != null && user.getId() != null && user.getToken() != null) {
            String tokenValue = user.getId() + user.getToken();
            params.add("user_id", user.getId());
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
        request.addHeader("User-Agent", "huizhuang;" + ZxsqApplication.getInstance().getVersionName() + ";android-phone");
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
