package com.huizhuang.zxsq.http.task.coupon;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.coupon.Coupon;
import com.lgmshare.http.netroid.Request;

/**
 * 添加优惠券
 * @author th
 *
 */
public class CouponAddTask extends AbstractHttpTask<Coupon> {

	public CouponAddTask(Context context,String order_id,int type) {
		super(context);
		mRequestParams.put("share_order_id", order_id);
		mRequestParams.put("type", type);
	}

	@Override
	public String getUrl() {
		return HttpClientApi.BASE_URL + HttpClientApi.COUPON_ADD;
	}

	@Override
	public int getMethod() {
		return Request.Method.GET;
	}

	@Override
	public Coupon parse(String data) {
		return JSON.parseObject(data, Coupon.class);
	}

}
