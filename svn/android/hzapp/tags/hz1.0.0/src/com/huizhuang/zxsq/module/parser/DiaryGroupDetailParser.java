package com.huizhuang.zxsq.module.parser;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.Author;
import com.huizhuang.zxsq.module.BillDate;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.DiaryGroup;
import com.huizhuang.zxsq.module.Visitor;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;
import com.huizhuang.zxsq.utils.DateUtil;

/**
 * @ClassName: DiaryGroupParser
 * @Package com.huizhuang.zxsq.parser
 * @Description:
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-9-5 下午4:22:07
 */
public class DiaryGroupDetailParser extends BaseJsonParser<DiaryGroup> {

	@Override
	public DiaryGroup parseIType(String jsonString) throws JSONException {
		DiaryGroup group = new DiaryGroup();
		JSONObject jsonObject = new JSONObject(jsonString);
		parseGroup(jsonObject, group);
		return group;
	}

	/**
	 * @param jsonObject
	 * @param group
	 * @throws JSONException
	 */
	private void parseGroup(JSONObject jsonObject, DiaryGroup group) throws JSONException {
		if (jsonObject.has("diaries")) {
			JSONObject diaryObject = jsonObject.getJSONObject("diaries");
			group.setTotalSize(diaryObject.optInt("totalrecord"));
			group.setPageTotal(diaryObject.optInt("totalpage"));
			if (diaryObject.has("list")) {
				JSONArray array = diaryObject.getJSONArray("list");
				JSONObject diaryJb = null;
				for (int i = 0; i < array.length(); i++) {
					diaryJb = array.getJSONObject(i);
					Diary diary = new Diary();
					diary.setId(diaryJb.optInt("id"));// ID
					diary.setTitle(diaryJb.optString("title"));//
					diary.setSummary(diaryJb.optString("summary"));// 简介
					diary.setContent(diaryJb.optString("content"));//
					diary.setImageUrl(diaryJb.optString("img"));//
					diary.setDatetime(diaryJb.optString("add_time"));//
					diary.setDiscussNum(diaryJb.optString("comment_num"));//
					diary.setLikeNum(diaryJb.optString("like_num"));//
					diary.setShareNum(diaryJb.optString("share_num"));//
					diary.setProvince(diaryJb.optString("province"));//
					diary.setCity(diaryJb.optString("city"));//
					diary.setWeather(diaryJb.optString("weather"));//
					diary.setZxNode(diaryJb.optString("zx_node"));//
					diary.setSecrecy(diaryJb.optInt("public"));//是否保密
					diary.setFavored(diaryJb.optInt("favored"));//是否已收藏
					//获取图片列表
					ArrayList<Atlas> list = parserImageList(diaryJb);
					diary.setAtlass(list);
					//获取作者信息
					Author author = parserAuthorInfo(diaryJb);
					diary.setAuthor(author);
					//访问者
					JSONObject visitorsJson = diaryJb.getJSONObject("visitors");
					ArrayList<Visitor> visitors = parserVistorList(visitorsJson);
					String readNum = visitorsJson.getString("totalrecord"); 
					diary.setReadNum(readNum);
					diary.setVisitors(visitors);
					//账单列表
					ArrayList<BillDate> bills = parserBillList(diaryJb);
					diary.setBills(bills);
					
					group.add(diary);
				}
			}
		}
		
	}
	
	private ArrayList<Atlas> parserImageList(JSONObject object) throws JSONException{
		ArrayList<Atlas> list = new ArrayList<Atlas>();
		if (object.has("images")) {
			JSONArray imageArray = object.getJSONArray("images");
			int length = imageArray.length();
			for (int j = 0; j < length; j++) {
				JSONObject imageJson = imageArray.getJSONObject(j);
				Atlas atlas = new Atlas();
				atlas.setId(imageJson.getString("id"));
				atlas.setImage(imageJson.getJSONObject("path").getString("img_path"));
				list.add(atlas);
			}
		}
		return list;
	}
	
	private ArrayList<Visitor> parserVistorList(JSONObject object) throws JSONException{
		ArrayList<Visitor> visitors = new ArrayList<Visitor>();
		JSONArray visitorsArray = object.getJSONArray("list");
		int length = visitorsArray.length();
		for (int j = 0; j < length; j++) {
			JSONObject visitorJson = visitorsArray.getJSONObject(j);
			Visitor visitor = new Visitor();
			visitor.setId(visitorJson.optString("user_id"));
			visitor.setAvatar(visitorJson.getJSONObject("user_thumb").optString("img_path"));
			visitors.add(visitor);
		}
		return visitors;
	}
	
	private Author parserAuthorInfo(JSONObject object) throws JSONException{
		Author author = new Author();
		if (object.has("author")) {
			JSONObject authorJson = object.getJSONObject("author");
			author.setId(authorJson.optString("id"));
			author.setName(authorJson.optString("screen_name"));
			author.setGender(authorJson.optString("gender"));
			author.setRoomStyle(authorJson.optString("room_style"));
			author.setRoomType(authorJson.optString("room_type"));
			author.setAvatar(authorJson.getJSONObject("user_thumb").optString("img_path"));
			author.setFollowed(authorJson.optString("is_followed"));
		}
		return author;
	}
	
	private ArrayList<BillDate> parserBillList(JSONObject object) throws JSONException{
		ArrayList<BillDate> bills = new ArrayList<BillDate>();
		JSONArray array = object.getJSONArray("journey_list");
		int length = array.length();
		for (int i = 0; i < length; i++) {
			JSONObject jb = array.getJSONObject(i);
			BillDate billtitle = new BillDate();
			billtitle.setId(jb.optInt("id"));
			billtitle.setDetail(jb.optString("detail"));
			billtitle.setAmount(jb.optString("amount"));
			billtitle.setDatetime(jb.optString("date"));
			billtitle.setTtypeId(jb.optString("t_type"));
			billtitle.setCtypeId(jb.optString("c_type"));
			billtitle.setTtypeName(jb.optString("t_type_name"));
			billtitle.setCtypeName(jb.optString("c_type_name"));
			bills.add(billtitle);
		}
		return bills;
	}
}
