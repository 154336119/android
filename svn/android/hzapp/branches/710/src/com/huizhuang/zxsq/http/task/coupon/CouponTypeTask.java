package com.huizhuang.zxsq.http.task.coupon;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.coupon.CouponType;
import com.lgmshare.http.netroid.Request;

/**
 * 优惠券类型
 * @author th
 *
 */
public class CouponTypeTask extends AbstractHttpTask<CouponType> {

	public CouponTypeTask(Context context) {
		super(context);
	}

	@Override
	public String getUrl() {
		return HttpClientApi.BASE_URL + HttpClientApi.COUPON_TYPE;
	}

	@Override
	public int getMethod() {
		return Request.Method.GET;
	}

	@Override
	public CouponType parse(String data) {
		return JSON.parseObject(data, CouponType.class);
	}

}
