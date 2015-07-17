package com.huizhuang.zxsq.module.parser;

import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.AtlasGroup;
import com.huizhuang.zxsq.module.Visitor;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author lim
 * @ClassName: AtlasGroupParser
 * @Description: 效果图数据格式化
 * @mail limshare@gmail.com
 * @date 2014-10-16 下午2:39:11
 */
public class AtlasGroupParser extends BaseJsonParser<AtlasGroup> {

    @Override
    public AtlasGroup parseIType(String jsonString) throws JSONException {
        AtlasGroup group = new AtlasGroup();
        JSONObject jsonObject = new JSONObject(jsonString);
        parseGroup(jsonObject, group);
        return group;
    }

    /**
     * @param jsonObject
     * @param group
     * @throws JSONException
     */
    private void parseGroup(JSONObject jsonObject, AtlasGroup group) throws JSONException {
        group.setTotalSize(jsonObject.optInt("totalrecord"));
        group.setPageTotal(jsonObject.optInt("totalpage"));
        if (jsonObject.has("list")) {
            JSONArray array = jsonObject.getJSONArray("list");
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
                atlas.setStyle(jb.optString("room_style"));
                JSONObject obj = jb.optJSONObject("img");
                if (obj != null) {
                    atlas.setImage(obj.optString("img_path"));
                }
                atlas.setFavorited(jb.optInt("favored") == 0 ? false : true);
                atlas.setLiked(jb.optInt("liked") == 0 ? false : true);

                JSONObject visitorsJson = jb.getJSONObject("visitors");
                JSONArray visitorsArray = visitorsJson.getJSONArray("list");
                ArrayList<Visitor> readers = new ArrayList<Visitor>();
                int length = visitorsArray.length();
                for (int j = 0; j < length; j++) {
                    JSONObject visitorJson = visitorsArray.getJSONObject(j);
                    Visitor reader = new Visitor();
                    reader.setId(visitorJson.optString("user_id"));
                    reader.setAvatar(visitorJson.getJSONObject("user_thumb").optString("img_path"));
                    readers.add(reader);
                }

                atlas.setReaderNum(visitorsJson.getString("totalrecord"));
                atlas.setReaders(readers);

                group.add(atlas);
            }
        }
    }
}
