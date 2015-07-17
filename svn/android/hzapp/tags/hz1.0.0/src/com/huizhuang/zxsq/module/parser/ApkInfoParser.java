package com.huizhuang.zxsq.module.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.ApkInfo;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;
import com.huizhuang.zxsq.utils.LogUtil;

/**
 * @ClassName: ApkInfoParser
 * @Package com.huizhuang.zxsq.parser
 * @Description: 应用安装包信息
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年7月7日 下午3:33:56
 */
public class ApkInfoParser extends BaseJsonParser<ApkInfo> {

	@Override
	public ApkInfo parseIType(String jsonString) throws JSONException {
		LogUtil.i("ApkInfoParser parseIType jsonString:"+jsonString);
		JSONObject jsonObject = new JSONObject(jsonString);
		ApkInfo apk = parseUser(jsonObject);
		return apk;
	}

	public ApkInfo parseUser(JSONObject jb) throws JSONException {
			ApkInfo apkInfo=new ApkInfo();
			apkInfo.setApkVersion(jb.optString("apkVersion"));
			apkInfo.setApkCode(Integer.parseInt(jb.optString("apkVerCode")));
			apkInfo.setApkSize(jb.optString("apkSize"));
			apkInfo.setApkName(jb.optString("apkName"));
			apkInfo.setApkLog(jb.optString("apklog"));
			apkInfo.setForceUpdate(jb.optBoolean("isForceUpdate"));
			return apkInfo;
	}
}
