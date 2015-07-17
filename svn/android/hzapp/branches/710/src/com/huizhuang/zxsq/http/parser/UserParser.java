package com.huizhuang.zxsq.http.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.http.parser.base.BaseJsonParser;

/**
 * @ClassName: UserParser
 * @Description: 用户信息解析器
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午11:15:29
 */
public class UserParser extends BaseJsonParser<User> {
	
	@Override
	public User parseIType(String jsonString) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		User user = parseUser(jsonObject);
		return user;
	}

	public User parseUser(JSONObject dataJson) throws JSONException {
		//User user = new User();
		User user = setNewBean(dataJson.toString());
		user.setId(dataJson.optString("user_id"));
		user.setUsername(dataJson.optString("username"));
		user.setName(dataJson.optString("name"));
		user.setNickname(dataJson.optString("nickname"));
		//user.setNickname(dataJson.optString("screen_name "));
		if(dataJson.optString("gender").equals("1")){
			user.setSex("男");
		} else if (dataJson.optString("gender").equals("2")){
			user.setSex("女");
		}
		if(dataJson.optString("is_local_user").equals("1")){
			user.setIsOpenLogin(false);
		} else if (dataJson.optString("is_local_user").equals("0")){
			user.setIsOpenLogin(true);
		}
		user.setEmail(dataJson.optString("email"));
		user.setAge(dataJson.optString("age"));
		if(dataJson.optJSONObject("user_thumb") != null){
			user.setAvatar(dataJson.optJSONObject("user_thumb").optString("thumb_path"));
		}
		user.setCDUser((dataJson.optInt("is_cd") == 0?false:true));
		user.setIsComplete((dataJson.optInt("is_complete") == 0?false:true));
		user.setPhone(dataJson.optString("mobile"));
		user.setCity(dataJson.optString("city"));
		user.setToken(dataJson.optString("token"));
		
		return user;
	}
	
	private User setNewBean(String dataJson){
		return JSON.parseObject(dataJson, User.class);
	}
}
