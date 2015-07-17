package com.huizhuang.zxsq.utils;

import java.io.File;

import android.net.Uri;

/**
 * 
 * ImageLoaderUriUtils.java - ImageLoaderUri转换工具类
 * 
 * @author Kevin.Zhang
 * 
 *         2014-6-27 下午3:45:32
 */
public class ImageLoaderUriUtils {

	/**
	 * ImageLoader 支持的URI格式
	 * 
	 * String imageUri = "http://site.com/image.png"; from Web String imageUri =
	 * "file:///mnt/sdcard/image.png"; from SD card String imageUri
	 * ="content://media/external/audio/albumart/13"; from content provider
	 * String imageUri = "assets://image.png"; from assets String imageUri =
	 * "drawable://" + R.drawable.image; from drawables (only images,
	 * non-9patch)
	 */

	public static String getUriFromLocalFile(String strFile) {
		return Uri.decode(Uri.fromFile(new File(strFile)).toString());
	}

	public static String getUriFormLocalFile(File photoFile) {
		return Uri.decode(Uri.fromFile(photoFile).toString());
	}
}
