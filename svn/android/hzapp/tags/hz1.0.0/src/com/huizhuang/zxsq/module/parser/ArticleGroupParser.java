package com.huizhuang.zxsq.module.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huizhuang.zxsq.module.Article;
import com.huizhuang.zxsq.module.ArticleGroup;
import com.huizhuang.zxsq.module.parser.base.BaseJsonParser;

/**
 * @ClassName: ArticleGroupParser
 * @Package com.huizhuang.zxsq.parser
 * @Description:
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-9-5 下午4:22:07
 */
public class ArticleGroupParser extends BaseJsonParser<ArticleGroup> {

	@Override
	public ArticleGroup parseIType(String jsonString) throws JSONException {
		ArticleGroup group = new ArticleGroup();
		JSONObject jsonObject = new JSONObject(jsonString);
		parseGroup(jsonObject, group);
		return group;
	}

	/**
	 * @param jsonObject
	 * @param group
	 * @throws JSONException
	 */
	private void parseGroup(JSONObject dataJb, ArticleGroup group) throws JSONException {
		group.setTotalSize(dataJb.optInt("totalrecord"));
		group.setPageTotal(dataJb.optInt("totalpage"));
		if (dataJb.has("list")) {
			JSONArray array = dataJb.getJSONArray("list");
			JSONObject articleJb = null;
			for (int i = 0; i < array.length(); i++) {
				articleJb = array.getJSONObject(i);
				Article article = new Article();

				article.setId(articleJb.optInt("id"));// ID
				article.setType(articleJb.optString("type"));
				article.setTitle(articleJb.optString("title"));
				article.setHtmlUrl(articleJb.optString("detail_url"));
				article.setImageUrl(articleJb.optString("pic_url"));//
				article.setCreatetime(articleJb.optString("add_time"));//
				article.setFavorited(articleJb.optBoolean("is_favored"));//

				ArrayList<Article> articles = new ArrayList<Article>();
				if (articleJb.has("data_list")) {
					JSONArray childArray = articleJb.getJSONArray("data_list");
					int length = childArray.length();
					for (int j = 0; j < length; j++) {
						JSONObject childJb = childArray.getJSONObject(j);
						Article childArticle = new Article();
						childArticle.setId(childJb.optInt("id"));// ID
						childArticle.setType(childJb.optString("type"));
						childArticle.setTitle(childJb.optString("title"));
						childArticle.setHtmlUrl(childJb.optString("detail_url"));
						childArticle.setImageUrl(childJb.optString("pic_url"));//
						childArticle.setFavorited(articleJb.optBoolean("is_favored"));//
						articles.add(childArticle);
					}
				}
				article.setChildArticle(articles);
				
				article.setDiscussNum(articleJb.optString("comment_count"));//
				article.setFavoritNum(articleJb.optString("ff_count"));//
				article.setShareNum(articleJb.optString("share_count"));//

				group.add(article);
			}
		}
	}

}
