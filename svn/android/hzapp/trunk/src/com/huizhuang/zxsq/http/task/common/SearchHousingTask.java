package com.huizhuang.zxsq.http.task.common;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.common.Housing;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;


/** 
* @ClassName: SearchHousingTask 
* @Description: 搜索小区task
* @author th 
* @mail 342592622@qq.com   
* @date 2015-1-20 上午10:08:49 
*  
*/
public class SearchHousingTask extends AbstractHttpTask<List<Housing>> {

    public SearchHousingTask(Context context,String name, String lat, String lon) {
    	super(context);
        mRequestParams = new RequestParams();
        if (name != null){
            mRequestParams.put("keyword", name);
        }
        if (lat != null){
            mRequestParams.put("lat", lat);
        }
        if (lon != null) {
            mRequestParams.put("lon", lon);
        }
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_COMMON_HOUSING_LIST;
    }

	@Override
	public int getMethod() {
		return Method.GET;
	}

	@Override
	public List<Housing> parse(String data) {
		return JSON.parseArray(data, Housing.class);
	}

}
