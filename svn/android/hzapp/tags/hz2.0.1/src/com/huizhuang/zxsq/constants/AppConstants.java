package com.huizhuang.zxsq.constants;

public class AppConstants {

	public enum XListRefreshType {
		ON_PULL_REFRESH, // 下拉刷新
		ON_LOAD_MORE // 底部加载更多
	}

	public static final int XLIST_REFRESH = 0;
	public static final int XLIST_LOADH = 1;
	public static final int XLIST_REQUEST = 2;
	public static final String PARAM_COMPANY = "company";
	public static final String PARAM_ATLAS = "atlas";
	public static final String PARAM_ARTICLE = "Article";
	public static final String PARAM_AUTHOR = "author";
	public static final String PARAM_DIARY = "diary";
	public static final String PARAM_VISOTOR = "visotor";
	public static final String PARAM_SUPERVISIONPROGRESS = "supervisionProgress";
	public static final String PARAM_SHARE = "share";
	public static final String PARAM_MAP = "map";
	public static final String PARAM_ORDER_ID = "order_id";
	public static final String PARAM_ORDER_NO = "order_no";
	public static final String PARAM_ORDER = "order";
	public static final String PARAM_ENSURE_TRADE = "ensure_trade";
	public static final String PARAM_NODE_ID = "node_id";
	public static final String PARAM_RECORD_ID = "record_id";
	public static final String PARAM_ORDER_TYPE = "order_type";
	public static final String PARAM_ORDER_DESIGNER_ID = "order_desinger_id";
	public static final String PARAM_ORDER_COMPANY_ID = "order_company_id";
	public static final String PARAM_ORDER_CASE_ID = "order_case_id";
	public static final String PARAM_ORDER_SOURCE_NAME = "order_source_name";
	public static final String DEFAULT_IMG = "nopic.gif";
	public static final String PARAM_PHONE = "phone";
	public static final String PARAM_PAY_WAY = "pay";
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_CITY = "city";
	public static final String PARAM_PRO = "province";
	public static final String PARAM_ARRD = "addr";
	public static final String PARAM_HOUSE_NAME = "housing_name";
	public static final String PARAM_AREA = "area";
	public static final String PARAM_AREA_NAME = "area_name";
	public static final String PARAM_MEASURE_TIME = "measure_time";
	public static final String PARAM_CITY_ID = "city_id";
	public static final String PARAM_IS_FREE = "is_free";
	public static final String PARAM_LAT = "lat";
	public static final String PARAM_LNG = "lng";
	public static final String PARAM_DISTRICT = "District";
	public static final String PARAM_STAGE_ID = "stage_id";
	public static final String PARAM_PAY_MONEY = "pay_money";
	public static final String PARAM_FINANCE_ID = "finance_id";
	public static final String PARAM_PAY_TYPE = "pay_type";
	public static final String PARAM_IS_CODE = "is_cost";
	public static final String PARAM_WEBWIEW_URL = "url";
	public static final String PARAM_WEBVIEW_DATA= "url_data";
	public static final String PARAM_IS_PAY= "is_pay";
	public static final String PARAM_IS_HISTORY= "is_history";//验收历史页
	/**
	 * 电话
	 */
	public static final String SERVICE_PHONE = "4006070035";
	
	/**
	 * 根据订单状态确定分享的内容
	 */
	public static final int SHARE_BY_ORDER_COMMON =0;//微信--通用 
	public static final int SHARE_BY_ORDER_START_WORKING =1;//微信--开工阶段
	public static final int SHARE_BY_ORDER_WATER =2;//微信--水电阶段
	public static final int SHARE_BY_ORDER_WOOD =3;//微信--泥木阶段
	public static final int SHARE_BY_ORDER_PAINT =4;//微信--油漆阶段
	public static final int SHARE_BY_ORDER_COMPLETE =5;//微信--竣工阶段

    /**
     * 分享requestCode
     */
    public static final int TO_SHARE_CODE = 66;

	public static class UmengEvent {
		/** 首页-个人中心*/
		public static final String ID_CLICK_0001 = "click0001";
		/** 首页-切换城市*/
        public static final String ID_CLICK_0002 = "click0002";
        /** 首页-施工现场缩略图*/
        public static final String ID_CLICK_0003 = "click0003";
        /** 首页-服务保障*/
        public static final String ID_CLICK_0004 = "click0004";
        /** 首页-立即预约装修*/
        public static final String ID_CLICK_0005 = "click0005";
        /** 首页-我要选工长*/
        public static final String ID_CLICK_0006 = "click0006";
        /** 下单-返回*/
        public static final String ID_CLICK_0007 = "click0007";
        /** 下单-切换城市*/
        public static final String ID_CLICK_0008 = "click0008";
        /** 下单-立即预约*/
        public static final String ID_CLICK_0009 = "click0009";
        /** 验证手机-获取验证码*/
        public static final String ID_CLICK_0010 = "click0010";
        /** 验证手机-下一步*/
        public static final String ID_CLICK_0011 = "click0011";
        /** 验证手机-返回*/
        public static final String ID_CLICK_0012 = "click0012";
        /** 验证手机-重新发送*/
        public static final String ID_CLICK_0013 = "click0013";
        /** 工长列表页-地图模式*/
        public static final String ID_CLICK_0014 = "click0014";
        /** 工长详情页-报价清单*/
        public static final String ID_CLICK_0015 = "click0015";
        /** 工长详情页-查看更多*/
        public static final String ID_CLICK_0016 = "click0016";
        /** 施工现场详情-顶部报名*/
        public static final String ID_CLICK_0017 = "click0017";
        /** 施工现场详情-底部报名*/
        public static final String ID_CLICK_0018 = "click0018";
        /** 登录-发送验证码*/
        public static final String ID_CLICK_0019 = "click0019";
        /** 登录-验证并登录*/
        public static final String ID_CLICK_0020 = "click0020";
        /** 登录-短信登录*/
        public static final String ID_CLICK_0021 = "click0021";
        /**登录-关闭*/
        public static final String ID_CLICK_0022 = "click0022";
        /**上行短信登录-确定*/
        public static final String ID_CLICK_0023 = "click0023";
        /**订单-等待工长应答-取消订单*/
        public static final String ID_CLICK_0024 = "click0024";
        /**订单-跪求好评-取消订单*/
        public static final String ID_CLICK_0025 = "click0025";
        /**订单-预约签约-预约TA签约*/
        public static final String ID_CLICK_0026 = "click0026";
        /**订单-支付装修款-立即支付*/
        public static final String ID_CLICK_0027 = "click0027";
        /**订单-惠装收银台-确认支付*/
        public static final String ID_CLICK_0028 = "click0028";
        /**订单-惠装收银台-确认完成刷卡*/
        public static final String ID_CLICK_0029 = "click0029";
        /**订单-开工验收-验收合格*/
        public static final String ID_CLICK_0030 = "click0030";
        /**订单-开工验收-需要整改*/
        public static final String ID_CLICK_0031 = "click0031";
        /**订单-开工跪求好评-评价*/
        public static final String ID_CLICK_0032 = "click0032";
        /**订单-水电验收-验收合格*/
        public static final String ID_CLICK_0033 = "click0033";
        /**订单-水电验收-需要整改*/
        public static final String ID_CLICK_0034 = "click0034";
        /**订单-水电跪求好评-评价*/
        public static final String ID_CLICK_0035 = "click0035";
        /**订单-泥木验收-验收合格*/
        public static final String ID_CLICK_0036 = "click0036";
        /**订单-泥木验收-需要整改*/
        public static final String ID_CLICK_0037 = "click0037";
        /**订单-泥木跪求好评-评价*/
        public static final String ID_CLICK_0038 = "click0038";
        /**订单-油漆验收-验收合格*/
        public static final String ID_CLICK_0039 = "click0039";
        /**订单-油漆验收-需要整改*/
        public static final String ID_CLICK_0040 = "click0040";
        /**订单-油漆跪求好评-评价*/
        public static final String ID_CLICK_0041 = "click0041";
        /**订单-竣工验收-验收合格*/
        public static final String ID_CLICK_0042 = "click0042";
        /**订单-竣工验收-需要整改*/
        public static final String ID_CLICK_0043 = "click0043";
        /**订单-竣工跪求好评-评价*/
        public static final String ID_CLICK_0044 = "click0044";
	}

}
