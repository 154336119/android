package com.huizhuang.zxsq.module.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;
import com.huizhuang.zxsq.utils.LogUtil;

/**
 * @ClassName: ResultParser
 * @Description:
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午11:15:35
 */
public class ResultParser extends BaseJsonParser<Result> {

	@Override
	public Result parseIType(String jsonString) throws JSONException {
		Result result = new Result();
		result.data = jsonString;
		return result;
	}

}
