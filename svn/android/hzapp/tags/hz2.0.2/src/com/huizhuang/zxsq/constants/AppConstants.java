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
	public static final String PARAM_COMPLAINTS_TYPE_ID= "complaints_type_id";
	public static final String PARAM_COMPLAINTS_TYPE= "complaints_type";
	public static final String PARAM_COMPLAINTS_QUESTION= "complaints_question";
	public static final String PARAM_IS_COMPLAINTS_SUCESS= "is_cimplaints_sucess";
	/**
	 * 电话
	 */
	public static final String SERVICE_PHONE = "4006070035";
	public static final String WECHAT_ID = "wxecff969544958f39";//微信ID	
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
	 * 投诉节点名称
	 */
	/**跪求好评*/
	public static final String COMPLAINTS_EVALUATION = "跪求好评";
	/**预约签约*/
	public static final String COMPLAINTS_APPOINTMENT = "预约签约";
	/**成功预约签约*/
	public static final String COMPLAINTS_APPOINTMENT_SUCCESS = "成功预约签约";
	/**等待开工验收*/
	public static final String COMPLAINTS_WAIT_1 = "等待开工验收";
	/**开工验收整改中*/
	public static final String COMPLAINTS_EDIT_ING_1 = "开工验收整改中";
	/**开工验收整改确认*/
	public static final String COMPLAINTS_EDIT_END_1 = "开工验收整改确认";
	/**等待水电验收*/
	public static final String COMPLAINTS_WAIT_2 = "等待水电验收";
	/**水电验收整改中*/
	public static final String COMPLAINTS_EDIT_ING_2 = "水电验收整改中";
	/**水电验收整改确认*/
	public static final String COMPLAINTS_EDIT_END_2 = "水电验收整改确认";
	/**等待泥木验收*/
	public static final String COMPLAINTS_WAIT_3 = "等待泥木验收";
	/**泥木验收整改中*/
	public static final String COMPLAINTS_EDIT_ING_3 = "泥木验收整改中";
	/**泥木验收整改确认*/
	public static final String COMPLAINTS_EDIT_END_3 = "泥木验收整改确认";
	/**等待油漆验收*/
	public static final String COMPLAINTS_WAIT_4 = "等待油漆验收";
	/**油漆验收整改中*/
	public static final String COMPLAINTS_EDIT_ING_4 = "油漆验收整改中";
	/**油漆验收整改确认*/
	public static final String COMPLAINTS_EDIT_END_4 = "油漆验收整改确认";
	/**等待竣工验收*/
	public static final String COMPLAINTS_WAIT_5 = "等待竣工验收";
	/**竣工验收整改中*/
	public static final String COMPLAINTS_EDIT_ING_5 = "竣工验收整改中";
	/**竣工验收整改确认*/
	public static final String COMPLAINTS_EDIT_END_5 = "竣工验收整改确认";

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
        /** 工长详情页-评论*/
        public static final String ID_CLICK_0015 = "click0015";
        /** 工长详情页-底部报名*/
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
        /**订单-预约签约-预约他签约*/
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
        /**引导页-进入惠装*/
        public static final String ID_CLICK_0045 = "click0045";
        /**选择投诉问题-返回*/
        public static final String ID_CLICK_0046 = "click0046";
        /**选择投诉问题-下一步*/
        public static final String ID_CLICK_0047 = "click0047";
        /**选择投诉原因-返回价*/
        public static final String ID_CLICK_0048 = "click0048";
        /**选择投诉原因-下一步*/
        public static final String ID_CLICK_0049 = "click0049";
        /**订单-跪求好评-投诉*/
        public static final String ID_CLICK_0050 = "click0050";
        /**订订单-预约签约-投诉*/
        public static final String ID_CLICK_0051 = "click0051";
        /**订单-成功预约签约-投诉*/
        public static final String ID_CLICK_0052 = "click0052";
        /**订单-等待开工验收-投诉*/
        public static final String ID_CLICK_0053 = "click0053";
        /**订单-开工验收整改中-投诉*/
        public static final String ID_CLICK_0054 = "click0054";
        /**订单-开工验收整改确认-投诉*/
        public static final String ID_CLICK_0055 = "click0055";
        /**订单-等待水电验收-投诉*/
        public static final String ID_CLICK_0056 = "click0056";
        /**订单-水电验收整改中-投诉*/
        public static final String ID_CLICK_0057 = "click0057";
        /**订单-水电验收整改确认-投诉*/
        public static final String ID_CLICK_0058 = "click0058";
        /**订单-水电支付装修款*/
        public static final String ID_CLICK_0059 = "click0059";
        /**订单-等待泥木验收-投诉*/
        public static final String ID_CLICK_0060 = "click0060";
        /**订单-泥木验收整改中-投诉*/
        public static final String ID_CLICK_0061 = "click0061";
        /**订单-泥木验收整改确认-投诉*/
        public static final String ID_CLICK_0062 = "click0062";
        /**订单-泥木支付装修款*/
        public static final String ID_CLICK_0063 = "click0063";
        /**订单-等待油漆验收-投诉*/
        public static final String ID_CLICK_0064 = "click0064";
        /**订单-油漆验收整改中-投诉*/
        public static final String ID_CLICK_0065 = "click0065";
        /**订单-油漆验收整改确认-投诉*/
        public static final String ID_CLICK_0066 = "click0066";
        /**订单-油漆支付装修款*/
        public static final String ID_CLICK_0067 = "click0067";
        /**订单-等待竣工验收-投诉*/
        public static final String ID_CLICK_0068 = "click0068";
        /**订单-竣工验收整改中-投诉*/
        public static final String ID_CLICK_0069 = "click0069";
        /**订单-竣工验收整改确认-投诉*/
        public static final String ID_CLICK_0070 = "click0070";
	}

}
