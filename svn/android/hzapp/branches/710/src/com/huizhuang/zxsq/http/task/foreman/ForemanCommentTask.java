package com.huizhuang.zxsq.http.task.foreman;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.foreman.ForemanComment;
import com.lgmshare.http.netroid.Request;

/**
 * 工长口碑评价
 * @ClassName: ForemanCommentsTask 
 * @author wumaojie.gmail.com  
 * @date 2015-2-10 上午11:53:07
 */
public class ForemanCommentTask extends AbstractHttpTask<List<ForemanComment>> {

    public ForemanCommentTask(Context context, String foreman_id){
        super(context);
        mRequestParams.add("store_id", foreman_id);
    }
    public ForemanCommentTask(Context context, String foreman_id,String min_id,int count){
        super(context);
        mRequestParams.add("store_id", foreman_id);
        mRequestParams.add("min_id", min_id);
        mRequestParams.add("count", String.valueOf(count));
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.FOREMAN_COMMENT_LIST;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public List<ForemanComment> parse(String data) {
        return JSON.parseArray(data, ForemanComment.class);
    }

}