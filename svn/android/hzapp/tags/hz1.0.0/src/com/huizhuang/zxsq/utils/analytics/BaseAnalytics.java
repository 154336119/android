package com.huizhuang.zxsq.utils.analytics;

import android.content.Context;

/**
 * 事件统计接口
 * 
 * @ClassName: BaseAnalytics.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-20 下午2:41:52
 */
public interface BaseAnalytics<Params> {

	void addAnalytics(Context context);

	void removeAnalytics(Context context);

	void onEvent(Context context, Params params);

}
