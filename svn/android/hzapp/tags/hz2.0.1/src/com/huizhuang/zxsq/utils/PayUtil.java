package com.huizhuang.zxsq.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.util.EncodingUtils;

import com.huizhuang.zxsq.http.bean.pay.AlipayPayInfo;
import com.huizhuang.zxsq.http.bean.pay.BankPayParams;

/**
 * 支付宝工具类
 * 
 * @ClassName: ToastUtil.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-12 下午2:54:50
 */
public class PayUtil {

						/***============== 支 付 宝=================**/
	
	public static String getAlipayOrderInfo(AlipayPayInfo alipayPayInfo) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + alipayPayInfo.getPartner() + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + alipayPayInfo.getSeller_email() + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + alipayPayInfo.getOut_trade_no() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + alipayPayInfo.getSubject() + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + alipayPayInfo.getBody() + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + alipayPayInfo.getTotal_fee() + "\"";

		// 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + alipayPayInfo.getNotify_url() + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=" + "\""+ alipayPayInfo.getService() + "\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=" + "\""+ alipayPayInfo.getPayment_type() + "\"";
		
		orderInfo += "&show_url=" + "\""+ alipayPayInfo.getShow_url() + "\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=" + "\""+alipayPayInfo.get_input_charset()+"\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
//		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\""+alipayPayInfo.getReturn_url()+"\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	/**
	 * 支付宝-sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public static String sign(String content,String rsaPrivate) {
		return SignUtils.sign(content, rsaPrivate);
	}

	/**
	 * 支付宝- get the sign type we use. 获取签名方式
	 * 
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}	
					/***============== 银联支付=================
					 * @throws UnsupportedEncodingException **/	
	
	public static String getBankOrderInfo(BankPayParams bankPayParams) throws UnsupportedEncodingException {
		
		String orderInfo = "origQid=" + URLEncoder.encode(bankPayParams.getOrigQid(), "UTF-8");

		orderInfo += "&acqCode="  + URLEncoder.encode(bankPayParams.getAcqCode(), "UTF-8");

		orderInfo += "&merCode="  + URLEncoder.encode(bankPayParams.getMerCode(), "UTF-8");
		String a = bankPayParams.getCommodityUrl();
		String s = URLEncoder.encode(bankPayParams.getCommodityUrl(), "UTF-8");
		orderInfo += "&commodityUrl="  + URLEncoder.encode(bankPayParams.getCommodityUrl(), "UTF-8");

		orderInfo += "&commodityName="  + URLEncoder.encode(bankPayParams.getCommodityName(), "UTF-8");

		orderInfo += "&commodityUnitPrice="  + URLEncoder.encode(bankPayParams.getCommodityUnitPrice(), "UTF-8");
		
		orderInfo += "&commodityQuantity="  + URLEncoder.encode(bankPayParams.getCommodityQuantity(), "UTF-8");

		orderInfo += "&commodityDiscount="  + URLEncoder.encode(bankPayParams.getCommodityDiscount(), "UTF-8");
		
		orderInfo += "&transferFee="  + URLEncoder.encode(bankPayParams.getTransferFee(), "UTF-8");

		orderInfo += "&customerName="  + URLEncoder.encode(bankPayParams.getCustomerName(), "UTF-8");

		orderInfo += "&defaultPayType="  + URLEncoder.encode(bankPayParams.getDefaultPayType(), "UTF-8");
		
		orderInfo += "&defaultBankNumber="  + URLEncoder.encode(bankPayParams.getDefaultBankNumber(), "UTF-8");

		orderInfo += "&transTimeout="  + URLEncoder.encode(bankPayParams.getTransTimeout(), "UTF-8");

		orderInfo += "&merReserved="  + URLEncoder.encode(bankPayParams.getMerReserved(), "UTF-8");

		orderInfo += "&version="  + URLEncoder.encode(bankPayParams.getVersion(), "UTF-8");

		orderInfo += "&charset="  + URLEncoder.encode(bankPayParams.getCharset(), "UTF-8");
		
		orderInfo += "&merId="  + URLEncoder.encode(bankPayParams.getMerId(), "UTF-8");

		orderInfo += "&merAbbr="  + URLEncoder.encode(bankPayParams.getMerAbbr(), "UTF-8");
		
		//URLEncoder.encode(bankPayParams.getBackEndUrl());
		
		orderInfo += "&backEndUrl="  + URLEncoder.encode(bankPayParams.getBackEndUrl(), "UTF-8");

		orderInfo += "&frontEndUrl="  + URLEncoder.encode(bankPayParams.getFrontEndUrl(), "UTF-8");

		orderInfo += "&orderNumber="  + URLEncoder.encode(bankPayParams.getOrderNumber(), "UTF-8");
		
		orderInfo += "&orderAmount="  +URLEncoder.encode(bankPayParams.getOrderAmount(), "UTF-8");

		orderInfo += "&transType="  + URLEncoder.encode(bankPayParams.getTransType(), "UTF-8");

		orderInfo += "&orderTime="  + URLEncoder.encode(bankPayParams.getOrderTime(), "UTF-8");

		orderInfo += "&orderCurrency="  + URLEncoder.encode(bankPayParams.getOrderCurrency(), "UTF-8");

		orderInfo += "&customerIp="  + URLEncoder.encode(bankPayParams.getCustomerIp(), "UTF-8");
		
		orderInfo += "&signature="  + URLEncoder.encode(bankPayParams.getSignature(), "UTF-8");

		orderInfo += "&signMethod="  + URLEncoder.encode(bankPayParams.getSignMethod(), "UTF-8");
		
		return orderInfo;
	}	
	
}
