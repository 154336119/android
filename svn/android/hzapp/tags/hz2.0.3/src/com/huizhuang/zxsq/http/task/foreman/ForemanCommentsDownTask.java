package com.huizhuang.zxsq.http.task.foreman;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.foreman.CommentResult;
import com.lgmshare.http.netroid.Request;

/**
 * 工长口碑评价
 * @ClassName: ForemanCommentsTask 
 * @author wumaojie.gmail.com  
 * @date 2015-2-10 上午11:53:07
 */
public class ForemanCommentsDownTask extends AbstractHttpTask<CommentResult> {

    public ForemanCommentsDownTask(Context context, String comment_id){
        super(context);
        mRequestParams.add("comment_id", comment_id);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.URL_FOREMAN_COMMENTS_DOWN;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public CommentResult parse(String data) {
        return JSON.parseObject(data, CommentResult.class);
    }

}