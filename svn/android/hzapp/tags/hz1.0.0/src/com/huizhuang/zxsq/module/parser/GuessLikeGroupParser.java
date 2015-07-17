package com.huizhuang.zxsq.module.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.DiaryGroup;
import com.huizhuang.zxsq.module.GuessLike;
import com.huizhuang.zxsq.module.GuessLikeGroup;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;


/** 
* @ClassName: DiaryZXGroupParser 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Jean 
* @mail 154336119@qq.com   
* @date 2014-11-7 上午10:58:53 
*  
*/
public class GuessLikeGroupParser extends BaseJsonParser<GuessLikeGroup> {

	@Override
	public GuessLikeGroup parseIType(String jsonString) throws JSONException {
		GuessLikeGroup group = new GuessLikeGroup();
		JSONObject jsonObject = new JSONObject(jsonString);
		parseGroup(jsonObject, group);
		return group;
	}

	/**
	 * @param jsonObject
	 * @param group
	 * @throws JSONException
	 */
	private void parseGroup(JSONObject dataJb, GuessLikeGroup group) throws JSONException {
		
			JSONArray array = dataJb.optJSONArray("styles");
            for(int i=0;i<array.length();i++){
    			GuessLike guessLike = new GuessLike();
            	JSONObject jb = array.optJSONObject(i);
            	guessLike.setStyles(jb.optString("id"));
            	guessLike.setDes(jb.optString("name"));
            	guessLike.setPic(jb.optString("pic"));
            	group.add(guessLike);
            }
	}

}
