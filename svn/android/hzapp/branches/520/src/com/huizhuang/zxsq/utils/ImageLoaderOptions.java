package com.huizhuang.zxsq.utils;

import android.graphics.Bitmap;

import com.huizhuang.hz.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * ImageLoaderOptions.java - ImageLoader config parameters
 * 
 * @author Kevin.Zhang
 * 
 *         2014-2-27 下午5:17:25
 */
public class ImageLoaderOptions {

	/**
	 * ImageLoader配置参数
	 */
	public static final int MAX_IMAGE_WIDTH = 480; // 480px
	public static final int MAX_IMAGE_HEIGHT = 800; // 800px
	public static final int MAX_IMAGE_MEMORY_CACHE_SIZE = 2 * 1024 * 1024; // 2MB
	public static final int MAX_IMAGE_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
	public static final int MAX_IMAGE_DISK_FILE_COUNT = 200;

	/**
	 * 图片圆角像素
	 */
	private static final int HEADER_CORNER_RADIUS_PIXELS = 50;

	/**
	 * 用户头像和群组头像
	 */
	public static DisplayImageOptions optionsHouseInfoPhoto = getDisplayImageOptions(R.drawable.ic_photo_add);
	public static DisplayImageOptions optionsDiaryPhotoAdd = getDisplayImageOptions(R.drawable.diary_add_img);

	/**
	 * 默认图片
	 */
	public static DisplayImageOptions optionsDefaultEmptyPhoto = getDisplayImageOptions(R.drawable.bg_photo_default);
//	public static DisplayImageOptions optionsMyMessageHeader = getDisplayImageOptions(R.drawable.ic_my_messages_header, HEADER_CORNER_RADIUS_PIXELS);
	public static DisplayImageOptions optionsBillAccoutingDefaultIcon = getDisplayImageOptions(R.drawable.ic_category_6);

	/**
	 * 用户头像
	 */
	public static DisplayImageOptions optionsUserHeader = getDisplayImageOptions(R.drawable.ic_system_header, HEADER_CORNER_RADIUS_PIXELS);

    /**
     * 上传图片按钮
     */
    public static DisplayImageOptions optionsUploadPhotoAdd = getDisplayImageOptions(R.drawable.ic_photo_add);
	/**
	 * 获得默认的DisplayImageOptions（不加载任何东西图片）
	 * 
	 * @return
	 */
	public static DisplayImageOptions getDefaultImageOption() {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.cacheInMemory(true);
		builder.cacheOnDisk(true);
		builder.bitmapConfig(Bitmap.Config.RGB_565);
		builder.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
		builder.resetViewBeforeLoading(true);
		return builder.build();
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
//		if (null != cornerRadiusPixels && cornerRadiusPixels.length > 0) {
//			builder.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels[0]));
//		}
		builder.resetViewBeforeLoading(true);
		return builder.build();
	}
}
