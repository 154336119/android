package com.huizhuang.zxsq.http.task.foreman;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Result;
import com.lgmshare.http.netroid.Request;

/**
 * 工长收藏
 * @ClassName: ForemanCollectionTask 
 * @author wumaojie.gmail.com  
 * @date 2015-2-9 下午5:37:42
 */
public class ForemanCollectionDeleteTask extends AbstractHttpTask<Result> {

    public ForemanCollectionDeleteTask(Context context, String foreman_id ){
        super(context);
        mRequestParams.add("cnt_type", "store" );
        mRequestParams.add("cnt_id", foreman_id );
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_FOREMAN_FAVORITE_DELETE;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Result parse(String data) {
        return JSON.parseObject(data, Result.class);
    }

}