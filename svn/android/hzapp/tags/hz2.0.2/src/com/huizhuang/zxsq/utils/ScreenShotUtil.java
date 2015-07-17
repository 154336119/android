package com.huizhuang.zxsq.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

public class ScreenShotUtil {
	
	/**
	 * 获取指定Activity的截屏，保存到png文件
	 * @param activity
	 * @return
	 */
	public static Bitmap takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
		Bitmap b1 = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 获取屏幕长和高
		int width = dm.widthPixels;
//		int height = dm.heightPixels;
		// 去掉标题栏
		// Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
		Bitmap b = null;
		if(b1 != null){
		    LogUtil.e("y:"+statusBarHeight);
//		    LogUtil.e("screen_height"+height);
//		    LogUtil.e("height:"+(height - statusBarHeight));
		    LogUtil.e("bitmap_height:"+b1.getHeight());
    		b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, b1.getHeight()
    				- statusBarHeight);
		}
		view.destroyDrawingCache();
		return b;
	}

	/**
	 * 保存到sdcard
	 * @param b
	 * @return
	 */
	public static String savePic(Bitmap b,Context context) {
		Bitmap bitmap = ImageUtil.compressForScale(b);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
//		String strFileName = FileUtil.getSDPath()+ "/" + AppConfig.PICTURE_PATH + sdf.format(new Date()) + ".png";
		String path = Util.createImagePath(context);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			if (null != fos) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

//	/**
//	 *  截取应用的屏幕并且保存到本地
//	 * @param a
//	 * @return
//	 */
//	public static String shoot(Activity a) {
//		return ScreenShotUtil.savePic(ScreenShotUtil.takeScreenShot(a));
//	}
}
