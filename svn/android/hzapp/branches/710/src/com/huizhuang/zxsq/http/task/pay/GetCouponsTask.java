package com.huizhuang.zxsq.http.task.pay;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.webkit.JsPromptResult;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.foreman.ForemanList;
import com.huizhuang.zxsq.http.bean.order.DeylayChangeOrder;
import com.huizhuang.zxsq.http.bean.pay.Coupon;
import com.lgmshare.http.netroid.Request;

/**
 *  优惠券列表
 * @author admin
 *
 */
public class GetCouponsTask extends AbstractHttpTask<List<Coupon>> {

    public GetCouponsTask(Context context){
        super(context);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.GET_POUPON;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

	@Override
	public List<Coupon> parse(String data) {
		// TODO Auto-generated method stub
		return JSON.parseArray(data, Coupon.class);
	}

}