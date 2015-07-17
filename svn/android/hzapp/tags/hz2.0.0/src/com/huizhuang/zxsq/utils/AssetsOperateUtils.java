package com.huizhuang.zxsq.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @ClassName: AssetsOperateUtils
 * @Package com.huizhuang.zxsq.utils
 * @Description: AssetsOperateUtils是一个操作应用程序Assets包资源
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月30日 上午10:16:07
 */
public class AssetsOperateUtils {

	/**
	 * 通过文件名从Assets中获得资源,以输入流的形式返回
	 * 
	 * @param context
	 * @param fileName
	 *            文件名应为assets文件下载绝对路径
	 * @return 以InputStream的形式返回
	 */
	public static InputStream getInputStreamForName(Context context,
			String fileName) {
		AssetManager assetManager = context.getAssets();
		InputStream inputStream = null;
		try {
			inputStream = assetManager.open(fileName);
		} catch (IOException e) {
			LogUtil.d(e.getMessage());
		}
		return inputStream;
	}

	/**
	 * 通过文件名从Assets中获得资源，以字符串的形式返回
	 * 
	 * @param context
	 * @param fileName
	 *            文件名应为assets文件下载绝对路径
	 * @return 以字符串的形式返回
	 */
	public static String getStringForName(Context context, String fileName) {
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		try {
			inputStream = getInputStreamForName(context, fileName);
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			LogUtil.d(e.getMessage());
		}
		return outputStream.toString();
	}

	/**
	 * 通过文件名从Assets中获得资源，以位图的形式返回
	 * 
	 * @param context
	 * @param fileName
	 *            文件名应为assets文件下载绝对路径
	 * @return 以位图的形式返回
	 */
	public static Bitmap getBitmapForName(Context context, String fileName) {
		Bitmap bitmap = null;
		InputStream inputStream = null;
		try {
			inputStream = getInputStreamForName(context, fileName);
			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e) {
			LogUtil.d(e.getMessage());
		}
		return bitmap;
	}

}
