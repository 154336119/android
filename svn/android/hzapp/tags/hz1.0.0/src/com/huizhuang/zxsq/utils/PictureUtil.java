package com.huizhuang.zxsq.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

/**
 * 
 * PictureUtil.java - 图片压缩处理工具类
 * 
 * @author Kevin.Zhang
 * 
 *         2014-5-4 上午11:42:57
 */
public class PictureUtil {

	private static final int COMPRESS_LEVEL = 40; // 压缩率60%
	private static final int PIC_LOAD_WIDTH = 360; // 标准宽度360px
	private static final int PIC_LOAD_HEIGHT = 600; // 标准高度600px

	/**
	 * 压缩图片文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static File getCompressPicFile(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, PIC_LOAD_WIDTH, PIC_LOAD_HEIGHT);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		if (null == bitmap) {
			return null;
		}
		int degree = readPictureDegree(filePath);
		bitmap = rotateBitmap(bitmap, degree);

		FileOutputStream fos = null;
		File compressImgFile = null;
		try {
			File imgFile = new File(filePath);
			compressImgFile = new File(Environment.getExternalStorageDirectory(), "small_" + imgFile.getName());
			fos = new FileOutputStream(compressImgFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_LEVEL, fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fos) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return compressImgFile;
	}

	public static void compressFile(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, PIC_LOAD_WIDTH, PIC_LOAD_HEIGHT);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		if (null == bitmap) {
			return;
		}
		int degree = readPictureDegree(filePath);
		bitmap = rotateBitmap(bitmap, degree);

		FileOutputStream fos = null;
		try {
			File imgFile = new File(filePath);
			fos = new FileOutputStream(imgFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_LEVEL, fos);
			bitmap.isRecycled();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fos) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void compressFile(Context context, Uri uri, String filePath) {
		InputStream inputStream = openInputStream(context, uri);
		if (null == inputStream) {
			return;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream, null, options);
		options.inSampleSize = calculateInSampleSize(options, PIC_LOAD_WIDTH, PIC_LOAD_HEIGHT);
		closeInputStream(inputStream);
		options.inJustDecodeBounds = false;

		inputStream = openInputStream(context, uri);
		Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
		bitmap.isRecycled();
		closeInputStream(inputStream);
		if (null == bitmap) {
			return;
		}

		FileOutputStream fos = null;
		try {
			File imgFile = new File(filePath);
			fos = new FileOutputStream(imgFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_LEVEL, fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fos) {
					fos.close();
				}
				System.gc();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 根据路径删除图片
	 * 
	 * @param filePath
	 */
	public static void deleteTempFile(String filePath) {
		File tempFile = new File(filePath);
		if (tempFile.exists()) {
			tempFile.delete();
		}
	}

	/**
	 * 获取图片的旋转值
	 * 
	 * @param path
	 * @return
	 */
	private static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param bitmap
	 * @param rotate
	 * @return
	 */
	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		// Setting post rotate to 90
		Matrix matrix = new Matrix();
		matrix.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}

	/**
	 * 计算图片的缩放值（从图片路径中读取图片,如果图片很大，不能全部加在到内存中处理，要是全部加载到内存中会内存溢出)
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
		// 源图片的宽度
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (width > reqWidth) {
			// 计算出实际宽度和目标宽度的比率
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(String pathName, int reqWidth) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}

	private static InputStream openInputStream(Context context, Uri uri) {
		InputStream inputStream = null;
		try {
			inputStream = context.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	private static void closeInputStream(InputStream inputStream) {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
