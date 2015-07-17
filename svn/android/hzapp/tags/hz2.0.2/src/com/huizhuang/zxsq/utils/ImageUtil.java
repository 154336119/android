package com.huizhuang.zxsq.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.huizhuang.zxsq.ZxsqApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @ClassName: ImageUtil
 * @Description: 图片工具
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月3日 下午4:02:29
 */
public class ImageUtil {

	private static float[] carray = new float[20];
	
	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 以最省内存的方式读取本地资源的图片 或者SDCard中的图片
	 * 
	 * @param imagePath
	 *             图片在SDCard中的路径
	 * @return
	 */
	public static Bitmap getSDCardImg(String imagePath) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		return BitmapFactory.decodeFile(imagePath, opt);
	}

	/**
	 * 编辑图片大小，保持图片不变形。
	 * 
	 * @param sourceBitmap
	 * @param resetWidth
	 * @param resetHeight
	 * @return
	 */
	public static Bitmap resetImage(Bitmap sourceBitmap, int resetWidth,
			int resetHeight) {
		int width = sourceBitmap.getWidth();
		int height = sourceBitmap.getHeight();
		int tmpWidth;
		int tmpHeight;
		float scaleWidth = (float) resetWidth / (float) width;
		float scaleHeight = (float) resetHeight / (float) height;
		float maxTmpScale = scaleWidth >= scaleHeight ? scaleWidth
				: scaleHeight;
		// 保持不变形
		tmpWidth = (int) (maxTmpScale * width);
		tmpHeight = (int) (maxTmpScale * height);
		Matrix m = new Matrix();
		m.setScale(maxTmpScale, maxTmpScale, tmpWidth, tmpHeight);
		sourceBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, width, height,
				m, false);
		// 切图
		int x = (tmpWidth - resetWidth) / 2;
		int y = (tmpHeight - resetHeight) / 2;
		return Bitmap.createBitmap(sourceBitmap, x, y, resetWidth, resetHeight);
	}

	/**
	 * 获取本地图片并指定高度和宽度
	 * 
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getNativeImage(String imagePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		Bitmap myBitmap = BitmapFactory.decodeFile(imagePath, options); // 此时返回myBitmap为空
		// 计算缩放比
		int be = (int) (options.outHeight / (float) 200);
		int ys = options.outHeight % 200;// 求余数
		float fe = ys / (float) 200;
		if (fe >= 0.5)
			be = be + 1;
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
		options.inJustDecodeBounds = false;
		myBitmap = BitmapFactory.decodeFile(imagePath, options);
		return myBitmap;
	}

	/**
	 * 代码创建一个selector 代码生成会清除padding
	 */
	public static Drawable CreateStateDrawable(Context context, int bulr,
			int focus) {
		Drawable bulrDrawable = context.getResources().getDrawable(bulr);
		Drawable focusDrawable = context.getResources().getDrawable(focus);
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[] { android.R.attr.state_pressed },
				focusDrawable);
		drawable.addState(new int[] {}, bulrDrawable);
		return drawable;
	}

	public static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressForQuality(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 质量压缩方法：
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressForQuality(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 图片按比例大小压缩方法
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressForScale(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressForQuality(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {

		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		paint.setColorFilter(null);
		c.drawBitmap(bmpGrayscale, 0, 0, paint);
		ColorMatrix cm = new ColorMatrix();
		getValueBlackAndWhite();
		cm.set(carray);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	public static void getValueSaturation() {
		// 高饱和度
		carray[0] = 5;
		carray[1] = 0;
		carray[2] = 0;
		carray[3] = 0;
		carray[4] = -254;
		carray[5] = 0;
		carray[6] = 5;
		carray[7] = 0;
		carray[8] = 0;
		carray[9] = -254;
		carray[10] = 0;
		carray[11] = 0;
		carray[12] = 5;
		carray[13] = 0;
		carray[14] = -254;
		carray[15] = 0;
		carray[16] = 0;
		carray[17] = 0;
		carray[18] = 5;
		carray[19] = -254;

	}

	private static void getValueBlackAndWhite() {
		// 黑白
		carray[0] = (float) 0.308;
		carray[1] = (float) 0.609;
		carray[2] = (float) 0.082;
		carray[3] = 0;
		carray[4] = 0;
		carray[5] = (float) 0.308;
		carray[6] = (float) 0.609;
		carray[7] = (float) 0.082;
		carray[8] = 0;
		carray[9] = 0;
		carray[10] = (float) 0.308;
		carray[11] = (float) 0.609;
		carray[12] = (float) 0.082;
		carray[13] = 0;
		carray[14] = 0;
		carray[15] = 0;
		carray[16] = 0;
		carray[17] = 0;
		carray[18] = 1;
		carray[19] = 0;
	}

	/***/
	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}

	/**
	 * 生成图片倒影
	 * 
	 * @param originalImage
	 * @return
	 */
	public static Bitmap createReflectedImage(Bitmap originalImage) {
		final int reflectionGap = 4;
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(originalImage, 0, 0, null);
		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
				TileMode.MIRROR);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		return bitmapWithReflection;
	}

	/**
	 * bitmap保存为本地图片
	 */
	public static void saveBitmap(String url, Bitmap mBitmap)
			throws IOException {
		File f = new File(url);
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getBitmap(Context context, Uri uri) {
		InputStream in = null;
		try {
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize = 2;// getPowerOfTwoForSampleRatio(ratio);
			bitmapOptions.inDither = true;// optional
			bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
			in = context.getContentResolver().openInputStream(uri);
			return BitmapFactory.decodeStream(in, null, bitmapOptions);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public static void deleteImage(String path) {
		File file = new File(path);
		file.deleteOnExit();
	}

	/**
	 * 图片资源回收
	 * 
	 * @param bitmap
	 */
	public static void distoryBitmap(Bitmap bitmap) {
		if (null != bitmap && bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}
	
	/**
	 * Resize the bitmap
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}


	/**
	 * Check the SD card 
	 * @return
	 */
	public static boolean checkSDCardAvailable(){
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	
	/**
	 * Save image to the SD card 
	 * @param photoBitmap
	 * @param photoName
	 * @param path
	 */
	public static File savePhotoToSDCard(Bitmap photoBitmap,String path,String photoName){
		File photoFile = null;

		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (!dir.exists()){
				dir.mkdirs();
			}
			
		 photoFile = new File(path , photoName + ".png");
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
						fileOutputStream.flush();
//						fileOutputStream.close();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally{
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(photoFile.isFile()){
			System.out.println("aaaaa");
		}
		return photoFile; 
	}
	
	public static void displayImage(String uri,ImageView imageAware){
		if(imageAware==null) return;
		if(ZxsqApplication.getInstance().isWifiConnectNetWork()||!ZxsqApplication.getInstance().isCloseLoadImgWhenNotInWifi()){
			ImageLoader.getInstance().displayImage(uri, imageAware);
		}else{
			File file=ImageLoader.getInstance().getDiskCache().get(uri);
			if(file!=null&&file.exists()){
				ImageLoader.getInstance().displayImage(uri, imageAware);
			}else{
				
			}
		}
	}
	
	
	public static void displayImage(String uri,ImageView imageAware,DisplayImageOptions options){
		if(imageAware==null) return;
		if(ZxsqApplication.getInstance().isWifiConnectNetWork()||!ZxsqApplication.getInstance().isCloseLoadImgWhenNotInWifi()){//是wifi  直接连接
			ImageLoader.getInstance().displayImage(uri, imageAware,options);
		}else{//不是wifi网络
			File file=ImageLoader.getInstance().getDiskCache().get(uri);
			if(file!=null&&file.exists()){//本地有，加载
				ImageLoader.getInstance().displayImage(uri, imageAware,options);
			}else{//本地没有 不管
				
			}
		}
	}
	
	public static void displayImage(String uri,ImageView imageAware,ImageLoadingListener listener){
		if(imageAware==null) return;
		if(ZxsqApplication.getInstance().isWifiConnectNetWork()||!ZxsqApplication.getInstance().isCloseLoadImgWhenNotInWifi()){//是wifi  直接连接
			ImageLoader.getInstance().displayImage(uri, imageAware, listener);
		}else{//不是wifi网络
			File file=ImageLoader.getInstance().getDiskCache().get(uri);
			if(file!=null&&file.exists()){//本地有，加载
				ImageLoader.getInstance().displayImage(uri, imageAware,listener);
			}else{//本地没有 不管
				
			}
		}
	}
	public static void displayImage(String uri,ImageView imageAware,DisplayImageOptions options,ImageLoadingListener listener){
		if(imageAware==null) return;
		if(ZxsqApplication.getInstance().isWifiConnectNetWork()||!ZxsqApplication.getInstance().isCloseLoadImgWhenNotInWifi()){//是wifi  直接连接
			ImageLoader.getInstance().displayImage(uri, imageAware, listener);
		}else{//不是wifi网络
			File file=ImageLoader.getInstance().getDiskCache().get(uri);
			if(file!=null&&file.exists()){//本地有，加载
				ImageLoader.getInstance().displayImage(uri, imageAware,options,listener);
			}else{//本地没有 不管
				
			}
		}
	}	
	/**
	 * Obtain the ImageLoader display parameters
	 * 
	 * @param imageResId
	 *            The resource ID of image
	 * @param cornerRadiusPixels
	 *            The rounded corners of image
	 * @return DisplayImageOptions
	 */
	private static DisplayImageOptions getDisplayImageOptions(int imageResId, int... cornerRadiusPixels) {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.showImageOnLoading(imageResId);
		builder.showImageForEmptyUri(imageResId);
		builder.showImageForEmptyUri(imageResId);
		builder.cacheInMemory(true);
		builder.cacheOnDisk(true);
		builder.bitmapConfig(Bitmap.Config.RGB_565);
		builder.imageScaleType(ImageScaleType.EXACTLY);
		if (null != cornerRadiusPixels && cornerRadiusPixels.length > 0) {
			builder.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels[0]));
		}
		builder.resetViewBeforeLoading(true);
		return builder.build();
	}
	
	/**
	 * view转bitmap
	 * @param v
	 * @return
	 */
   public static Bitmap createViewBitmap(View v) {
       if (v == null) {  
           return null;  
       }  
       Bitmap screenshot;  
       screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.RGB_565);  
       Canvas c = new Canvas(screenshot);  
       c.translate(-v.getScrollX(), -v.getScrollY());  
       v.draw(c);  
       return screenshot;  
    }
}
