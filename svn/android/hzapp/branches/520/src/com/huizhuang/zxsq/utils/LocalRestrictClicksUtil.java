package com.huizhuang.zxsq.utils;

import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.huizhuang.zxsq.ZxsqApplication;

/**
 * 本地点击次数限制
 * 
 * @ClassName: LocalRestrictClicksUtil.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-11 下午2:39:13
 */
public class LocalRestrictClicksUtil {

	/**
	 * SharedPreferences需要读取的文件
	 */
	private final static String FILE_NAME = "restrict_clicks";
	private final static String DEFAULT_USER_ID = "default_user_id";

	/**
	 * 单例模式
	 */
	private static LocalRestrictClicksUtil mInstance;

	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;

	/**
	 * 单例模式（懒汉式）
	 * 
	 * @return
	 */
	public static LocalRestrictClicksUtil getInstance() {
		if (null == mInstance) {
			mInstance = new LocalRestrictClicksUtil(ZxsqApplication.getInstance());
		}
		return mInstance;
	}

	private LocalRestrictClicksUtil(Context context) {
		mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	/**
	 * 判断用户对于一个目标是否已经重复点击
	 * 
	 * @param userId
	 *            用户ID
	 * @param targetId
	 *            目标对象（效果图、文章等）ID
	 * @return true 可点击 false 不可点击
	 */

	@SuppressLint("NewApi")
	public boolean isUserCanClick(final String userId, final String targetId) {
		String localUserId = DEFAULT_USER_ID;
		if (null != userId) {
			localUserId = userId;
		}
		// 默认不可点击
		boolean canClick = false;
		Set<String> stringSet = mSharedPreferences.getStringSet(localUserId, null);
		if (null != stringSet && !stringSet.isEmpty()) {
			if (stringSet.contains(targetId)) {
				// 已经存在，则不能点击
				canClick = false;
			} else {
				// 不存在，可以点击
				canClick = true;
			}
		} else {
			// 不存在，可以点击
			canClick = true;
		}
		return canClick;
	}

	/**
	 * 设置用户对特定的点击事件不可用
	 * 
	 * @param userId
	 *            用户ID
	 * @param targetId
	 *            目标对象（效果图、文章等）ID
	 */
	@SuppressLint("NewApi")
	public void setUserClickStateDiseable(final String userId, final String targetId) {
		String localUserId = DEFAULT_USER_ID;
		if (null != userId) {
			localUserId = userId;
		}
		Set<String> stringSet = mSharedPreferences.getStringSet(localUserId, null);

		if (null == stringSet) {
			stringSet = new HashSet<String>();
		}

		// 如果之前不存在，则添加进去
		if (!stringSet.contains(targetId)) {
			stringSet.add(targetId);
		}

		mEditor.putStringSet(localUserId, stringSet);
		mEditor.commit();
	}
}
