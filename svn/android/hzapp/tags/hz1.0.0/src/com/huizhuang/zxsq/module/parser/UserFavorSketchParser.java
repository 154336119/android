package com.huizhuang.zxsq.module.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.http.bean.common.Image;
import com.huizhuang.zxsq.module.UserFavorSketch;
import com.huizhuang.zxsq.module.UserFavorSketch.ImageInfo;
import com.huizhuang.zxsq.module.UserFavorSketch.Visitor;
import com.huizhuang.zxsq.module.UserFavorSketch.Visitors;
import com.huizhuang.zxsq.module.parser.base.BaseParser;
import com.huizhuang.zxsq.utils.LogUtil;

public class UserFavorSketchParser implements BaseParser<UserFavorSketch> {

	@Override
	public UserFavorSketch parse(String jsonString) throws JSONException {
		
		JSONObject obj=new JSONObject(jsonString);
		return parseUserFavorSketch(obj);
	}

	private UserFavorSketch parseUserFavorSketch(JSONObject obj) {
		UserFavorSketch userFavorSketch=new UserFavorSketch();
		userFavorSketch.setTotalpage(obj.optInt("totalpage"));
		userFavorSketch.setTotalRecord(obj.optInt("totalrecord"));
		JSONArray array=obj.optJSONArray("list");
		List<ImageInfo> infoList=new ArrayList<ImageInfo>();
		for(int i=0;i<array.length();i++){
			ImageInfo info=userFavorSketch.new ImageInfo();
			JSONObject objItem=array.optJSONObject(i);
			LogUtil.i("jiengyh parseUserFavorSketch objItem:"+objItem);
			info.setId(objItem.optInt("id"));
			info.setAlbum_id(objItem.optInt("album_id"));
			info.setName(objItem.optString("name"));
			info.setDigest(objItem.optString("digest"));
			info.setViewNum(objItem.optInt("view_num"));
			info.setLikeNum(objItem.optInt("like_num"));
			info.setFavorNum(objItem.optInt("favor_num"));
			info.setShareNum(objItem.optInt("share_num"));
			info.setFavored(objItem.optInt("favored"));
			info.setLiked(objItem.optInt("liked"));
			JSONArray stArray=objItem.optJSONArray("shared_types");
			List<Integer> stList=new ArrayList<Integer>();
			if(stArray!=null){
				for(int j=0;j<stArray.length();j++){
					stList.add(stArray.optInt(j));
				}
				info.setSharedTypes(stList);
			}
			
			JSONObject imgObj=objItem.optJSONObject("img");
			LogUtil.i("jiengyh jiengyh parseUserFavorSketch");
			if(imgObj!=null){
				
				Image image=new Image();
				image.setImgExt(imgObj.optString("img_ext"));
				image.setImg_path(imgObj.optString("img_path"));
				image.setThumb_path(imgObj.optString("thumb_path"));
				info.setImg(image);
			}
			JSONObject visitorsObj=objItem.optJSONObject("visitors");
			if(visitorsObj!=null){
				Visitors visitors=userFavorSketch.new Visitors();
				visitors.setTotalrecord(visitorsObj.optInt("totalrecord"));
				JSONArray visArrayObj=visitorsObj.optJSONArray("list");
				if(visArrayObj!=null){
					List<Visitor> visList=new ArrayList<Visitor>();
					for(int j=0;j<visArrayObj.length();j++){
						Visitor visitor=userFavorSketch.new Visitor();
						JSONObject visObj=visArrayObj.optJSONObject(j);
						visitor.setUserId(visObj.optInt("user_id"));
						JSONObject userObj=visObj.optJSONObject("user_thumb");
						Image userImg=new Image();
						userImg.setImgExt(userObj.optString("img_ext"));
						userImg.setImg_path(userObj.optString("img_path"));
						userImg.setThumb_path(userObj.optString("thumb_path"));
						visitor.setImage(userImg);
						visList.add(visitor);
					}
					visitors.setVisitorList(visList);
				}
				info.setVisitors(visitors);
			}
			
			infoList.add(info);
		
		}
		userFavorSketch.setImageInfoList(infoList);
		return userFavorSketch;
	}

}
