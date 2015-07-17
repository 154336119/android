package com.huizhuang.zxsq.ui.activity.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.ui.activity.ImageSimpleBrowseActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.FileUtil;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PictureUtil;

/**
 * @ClassName: ImageSelectActivity
 * @Description: 获取图片
 * @author LGM
 * @mail lgmshare@gmail.com
 * @date 2014-6-10 下午10:43:30
 */
public class ImageSelectActivity extends BaseActivity implements OnClickListener {

	private static final String IMAGE_DEFAULT_NAME = "default.png";

	private String IMAGE_DEFAULE_PATH = AppConfig.PICTURE_PATH;

	private static final int REQ_RESULT_PHOTO_CODE = 500;
	private static final int REQ_RESULT_CAPTURE_CODE = 600;
	private static final int REQ_RESULT_CORP_CODE = 666;
	// 图片缓存路径
	private Uri mCameraImageUri;
	// 图片缓存路径
	private String mCameraImagePath;
	private boolean mIsCrop = false;

	/**
	 * 将取消改为查看功能
	 */
	private String mViewImageUrl;
	private Button mBtnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.image_select_layout);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		getWindow().setAttributes(lp);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mIsCrop = extras.getBoolean("crop");
			mCameraImagePath = extras.getString("image-path");
			mCameraImageUri = extras.getParcelable("image-path-uri");
			mViewImageUrl = extras.getString("view-image-uri");
			if (mCameraImageUri == null) {
				if (extras.containsKey("image-path")) {
					mCameraImageUri = getImageUri(mCameraImagePath);
				}
			}
		}

		findViewById(R.id.btn_take_photo).setOnClickListener(this);
		findViewById(R.id.btn_pick_photo).setOnClickListener(this);
		mBtnCancel = (Button) findViewById(R.id.btn_cancel);
		mBtnCancel.setOnClickListener(this);
		if (null != mViewImageUrl) {
			mBtnCancel.setText("查看");
		}
	}

	/**
	 * 打开手机相册
	 */
	private void openMediaStore() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			try {
				if (mCameraImageUri == null) {
					mCameraImageUri = Uri.fromFile(FileUtil.createSDFile(IMAGE_DEFAULE_PATH, IMAGE_DEFAULT_NAME));
					mCameraImagePath = mCameraImageUri.getPath();
				}
			} catch (IOException e) {
				e.printStackTrace();
				showToastMsg("在存储卡中创建图片失败");
			}
		} else {
			showToastMsg("存储卡已取出，相册功能暂不可用");
			return;
		}
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.putExtra("noFaceDetection", false);
		startActivityForResult(intent, REQ_RESULT_PHOTO_CODE);
	}

	/**
	 * 打开照相机
	 */
	private void openCamera() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			try {
				if (mCameraImageUri == null) {
					mCameraImageUri = Uri.fromFile(FileUtil.createSDFile(IMAGE_DEFAULE_PATH, IMAGE_DEFAULT_NAME));
					mCameraImagePath = mCameraImageUri.getPath();
				}
			} catch (IOException e) {
				e.printStackTrace();
				showToastMsg("在存储卡中创建图片失败");
			}
		} else {
			showToastMsg("存储卡已取出，拍照功能暂不可用");
			return;
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQ_RESULT_CAPTURE_CODE);
	}

	private Uri getImageUri(String path) {
		return Uri.fromFile(new File(path));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			if (null != mViewImageUrl) {
				ArrayList<String> imageUrlList = new ArrayList<String>();
				imageUrlList.add(mViewImageUrl);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS, imageUrlList);
				ActivityUtil.next(ImageSelectActivity.this, ImageSimpleBrowseActivity.class, bundle, -1);
			} else {
				if (!TextUtils.isEmpty(mCameraImagePath)) {
					FileUtil.deteleFile(mCameraImagePath);
				}
			}
			finish();
			break;
		case R.id.btn_pick_photo:
			openMediaStore();
			break;
		case R.id.btn_take_photo:
			openCamera();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_RESULT_PHOTO_CODE) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			if (resultCode == Activity.RESULT_OK) {
				crop(data);
			}
		} else if (requestCode == REQ_RESULT_CAPTURE_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				crop(null);
			}
		} else if (requestCode == REQ_RESULT_CORP_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				backResult();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void crop(Intent data) {
		if (mIsCrop) {
			Intent intent = new Intent(ImageSelectActivity.this, ImageCropActivity.class);
			intent.putExtra("image-path", mCameraImageUri.getPath());
			if (data != null) {
				Uri imageUri = data.getData();
				intent.putExtra("image-path-uri", imageUri);
			}
			intent.putExtra("rotate", true);
			intent.putExtra("scale", false);
			startActivityForResult(intent, REQ_RESULT_CORP_CODE);
		} else {
			
            if (data != null) {
                Uri imageUri = data.getData();
                Bitmap selectedImage = ImageUtil.getBitmap(this, imageUri);
                try {
                    ImageUtil.saveBitmap(mCameraImageUri.getPath(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
			
//			if (data != null) {
//				Uri uri = data.getData();
//				PictureUtil.compressFile(this, uri, mCameraImageUri.getPath());
//			} else {
//				PictureUtil.compressFile(mCameraImageUri.getPath());
//			}
			backResult();
		}
	}

	private void backResult() {
		Bundle bd = new Bundle();
		bd.putString("image-path", mCameraImagePath);
		bd.putParcelable("image-path-uri", mCameraImageUri);

		setResult(Activity.RESULT_OK, getIntent().putExtras(bd));
		finish();
	}
}
