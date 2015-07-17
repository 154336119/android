package com.huizhuang.zxsq.module.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.MyArticles;
import com.huizhuang.zxsq.module.MyArticles.ArticleDigest;
import com.huizhuang.zxsq.module.parser.base.BaseParser;

public class MyArticlesParser implements BaseParser<MyArticles> {

	@Override
	public MyArticles parse(String jsonString) throws JSONException {
		JSONObject obj=new JSONObject(jsonString);
		return parseMyArticles(obj);
	}

	private MyArticles parseMyArticles(JSONObject obj) {
		MyArticles articls=new MyArticles();
		articls.setTotalpage(obj.optInt("totalpage"));
		articls.setTotalRecord(obj.optInt("totalrecord"));
		List<ArticleDigest> list=new ArrayList<MyArticles.ArticleDigest>();
		JSONArray array=obj.optJSONArray("list");
		for(int i=0;i<array.length();i++){
			ArticleDigest digest=articls.new ArticleDigest();
			JSONObject objItem=array.optJSONObject(i);
			digest.setAddTime(objItem.optLong("add_time"));
			digest.setCntId(objItem.optInt("cnt_id"));
			digest.setCntType(objItem.optString("cnt_type"));
			digest.setCommentCount(objItem.optInt("comment_count"));
			digest.setDetailUrl(objItem.optString("detail_url"));
			digest.setDigest(objItem.optString("digest"));
			digest.setFfCount(objItem.optInt("ff_count"));
			digest.setShareCount(objItem.optInt("share_count"));
			digest.setThumb(objItem.optString("thumb"));
			digest.setTitle(objItem.optString("title"));
			digest.setTop(objItem.optInt("top"));
			list.add(digest);
			
		}
		articls.setAcArticleDigestList(list);
		return articls;
	}

}
