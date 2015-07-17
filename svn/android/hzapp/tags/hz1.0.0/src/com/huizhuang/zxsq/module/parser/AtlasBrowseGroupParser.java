package com.huizhuang.zxsq.module.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.AtlasGroup;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;

/** 
* @ClassName: AtlasBrowseGroupParser 
* @Description: 图集浏览，查看大图
* @author lim 
* @mail limshare@gmail.com   
* @date 2014-11-5 上午11:21:06 
*  
*/
public class AtlasBrowseGroupParser extends BaseJsonParser<AtlasGroup> {

	@Override
	public AtlasGroup parseIType(String jsonString) throws JSONException {
		AtlasGroup group = new AtlasGroup();
		parseGroup(jsonString, group);
		return group;
	}

	/**
	 * @param jsonObject
	 * @param group
	 * @throws JSONException
	 */
	private void parseGroup(String data, AtlasGroup group) throws JSONException {
		JSONObject dataJson = new JSONObject(data);
		group.setTotalSize(dataJson.optInt("total_order"));
		
		JSONArray array = dataJson.getJSONArray("sketches");
		JSONObject jb = null;
		for (int i = 0; i < array.length(); i++) {
			jb = array.getJSONObject(i);
			Atlas atlas = new Atlas();
			atlas.setId(jb.optString("id"));
			atlas.setAlbumId(jb.optString("album_id"));
			atlas.setName(jb.optString("name"));
			atlas.setDescription(jb.optString("digest"));
			atlas.setReadNum(jb.optString("view_num"));
			atlas.setLikeNum(jb.optString("like_num"));
			atlas.setFavoriteNum(jb.optString("favor_num"));
			atlas.setShareNum(jb.optString("share_num"));
			atlas.setImage(jb.getJSONObject("img").optString("img_path"));
			atlas.setFavorited(jb.optInt("favored") == 0 ? false : true);
			atlas.setLiked(jb.optInt("liked") == 0 ? false : true);
			group.add(atlas);
		}
	}
}
