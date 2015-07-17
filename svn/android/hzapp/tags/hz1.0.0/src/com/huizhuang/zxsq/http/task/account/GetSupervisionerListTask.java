package com.huizhuang.zxsq.http.task.account;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.account.SupervisionerGroup;
import com.lgmshare.http.netroid.Request.Method;
import com.lgmshare.http.netroid.RequestParams;





/** 
* @ClassName: GetSupervisionerListTask 
* @Description: 监理师列表task
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-9 下午2:50:48 
*  
*/
public class GetSupervisionerListTask extends AbstractHttpTask<SupervisionerGroup> {

    public GetSupervisionerListTask(Context context, int pagesize,int page) {
    	super(context);
        mRequestParams = new RequestParams();
        mRequestParams.put("pagesize", pagesize);
        mRequestParams.put("page", page);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.REQ_ACCOUNT_SUPERVISION_LIST;
    }

	@Override
	public int getMethod() {
		return Method.GET;
	}

	@Override
	public SupervisionerGroup parse(String data) {
		return JSON.parseObject(data, SupervisionerGroup.class);
	}

}
