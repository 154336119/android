package com.huizhuang.zxsq.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.huizhuang.zxsq.ZxsqApplication;

public class SearchHistoryKeywordUtil {

	/**
	 * SharedPreferences需要读取的文件
	 */
	private final static String FILE_NAME = "search_history_keyword";
	private final static String DEFAULT_USER_ID = "user_id";

	/**
	 * 单例模式
	 */
	private static SearchHistoryKeywordUtil mInstance;

	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;

	/**
	 * 单例模式（懒汉式）
	 * 
	 * @return
	 */
	public static SearchHistoryKeywordUtil getInstance() {
		if (null == mInstance) {
			mInstance = new SearchHistoryKeywordUtil(ZxsqApplication.getInstance());
		}
		return mInstance;
	}

	private SearchHistoryKeywordUtil(Context context) {
		mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public List<String> getKeywords() {
		String localUserId = DEFAULT_USER_ID;
		Set<String> set = mSharedPreferences.getStringSet(localUserId, null);
		List<String> list = new ArrayList<String>();
		if (set == null) {
			return list;
		}
		for (String string : set) {
			list.add(string);
		}
		return list;
	}
	
	public void save(final String keyword) {
		String localUserId = DEFAULT_USER_ID;
		Set<String> stringSet = mSharedPreferences.getStringSet(localUserId, null);

		if (null == stringSet) {
			stringSet = new HashSet<String>();
		}

		// 如果之前不存在，则添加进去
		if (!stringSet.contains(keyword)) {
			stringSet.add(keyword);
		}

		mEditor.clear();
		mEditor.putStringSet(localUserId, stringSet);
		mEditor.commit();
	}
}
