package com.huizhuang.zxsq.module.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.Activist;
import com.huizhuang.zxsq.module.ActivistGroup;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;


public class ActivistGroupParser extends BaseJsonParser<ActivistGroup> {

	@Override
	public ActivistGroup parseIType(String jsonString)
			throws JSONException {
		ActivistGroup activistGroup = new ActivistGroup();
		if(jsonString != null && !"null".equals(jsonString)){
			JSONArray data = new JSONArray(jsonString);
			parseGroup(data, activistGroup);
		}
		return activistGroup;
	}
	
	/**
	 * @param jsonObject
	 * @param group
	 * @throws JSONException
	 */
	private void parseGroup(JSONArray data, ActivistGroup activistGroup) throws JSONException {
		for (int i = 0; i < data.length(); i++) {
			JSONObject jsonObject = data.optJSONObject(i);
			int id = jsonObject.optInt("id");
			String name = jsonObject.optString("name");
			if (jsonObject.has("sub")) {
				JSONArray array = jsonObject.optJSONArray("sub");
				for (int j = 0; j < array.length(); j++) {
					JSONObject jb = array.optJSONObject(j);
					Activist activist = new Activist();
					activist.setId(jb.optInt("id"));
					activist.setName(jb.optString("name"));
					activist.setDataTime(jb.optString("add_time"));
					activist.setImgPath(jb.optString("img_path"));
					activist.setThumbPath(jb.optString("thumb_path"));
					activist.setImgRemarks(jb.optString("img_remarks"));
					activist.setClosed(jb.optInt("closed")==0?false:true);
					activist.setStatu(jb.optInt("wq_statu"));
					if(j == 0){
						activist.setParentId(id);
						activist.setParentName(name);
					}
					activistGroup.add(activist);
					activist = null;
				}
			}
			
		}
		
	}
}
