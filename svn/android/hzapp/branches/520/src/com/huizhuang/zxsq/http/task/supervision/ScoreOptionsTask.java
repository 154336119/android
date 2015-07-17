package com.huizhuang.zxsq.http.task.supervision;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.AbstractHttpTask;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.supervision.ScoreOptionListList;
import com.lgmshare.http.netroid.Request;
/**
 * 各阶段评价选项
 * @author th
 *
 */
public class ScoreOptionsTask extends AbstractHttpTask<ScoreOptionListList> {
    public ScoreOptionsTask(Context context,String comment_key) {
        super(context);
        mRequestParams.put("comment_key", comment_key);
    }

    @Override
    public String getUrl() {
        return HttpClientApi.BASE_URL + HttpClientApi.COMMENT_GET_COMMENT_INFO;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public ScoreOptionListList parse(String data) {
        return JSON.parseObject(data, ScoreOptionListList.class);
    }

}