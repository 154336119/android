package com.huizhuang.zxsq.http.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.http.bean.Phone;
import com.huizhuang.zxsq.http.parser.base.BaseJsonParser;

public class PhoneParser extends BaseJsonParser<Phone>{

	@Override
	public Phone parseIType(String jsonString) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		Phone phone = new Phone();
		phone.setSend_phone(jsonObject.optString("send_phone"));
		phone.setVerify(jsonObject.optString("verify"));
		return phone;
	}

}
