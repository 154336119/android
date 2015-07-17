package com.huizhuang.zxsq.ui.activity.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.utils.LogUtil;

public class ImageCropActivity extends MonitoredActivity{
	private static final String TAG = "CropImage";
	private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;
	// Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    
 // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
    
    private int mGuidelines = 1;
    
    private final Handler mHandler = new Handler();
    
    private Uri mSaveUri = null;
    private ContentResolver mContentResolver;
    private String mImagePath;
    private Uri mImagePathUri;
    
    private CropImageView cropImageView;
    private Bitmap mBitmap;
    
    private boolean mSaving;
    
    private boolean mScale;
    private boolean isSuppotRotate = true;
    
    
 // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_crop);
        mContentResolver = getContentResolver();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            mImagePath = extras.getString("image-path");
            mImagePathUri = extras.getParcelable("image-path-uri");

            if (mImagePathUri != null) {
            	mSaveUri = getImageUri(mImagePath);
                InputStream in = null;
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inSampleSize = 2;// getPowerOfTwoForSampleRatio(ratio);
                    bitmapOptions.inDither = true;// optional
                    bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    in = mContentResolver.openInputStream(mImagePathUri);
                    mBitmap = BitmapFactory.decodeStream(in, null, bitmapOptions);
                } catch (FileNotFoundException e) {
                	e.printStackTrace();
                }
            } else {
                mSaveUri = getImageUri(mImagePath);
                mBitmap = getBitmap(mImagePath);
            }
            mAspectRatioX = extras.getInt("aspectRatioX");
            mAspectRatioY = extras.getInt("aspectRatioY");
            mGuidelines = extras.getInt("guidelines");
            mScale = extras.getBoolean("scale", true);
            isSuppotRotate = extras.getBoolean("rotate", true);
        }
        
        if (mBitmap == null) {
            Log.d(TAG, "finish!!!");
            finish();
            return;
        }
        
     // Make UI fullscreen.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
    	cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
        cropImageView.setFixedAspectRatio(mScale);
        cropImageView.setGuidelines(mGuidelines);
        cropImageView.setImageBitmap(mBitmap);
        findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveClicked();
            }
        });
        
        if (isSuppotRotate) {
        	findViewById(R.id.rotate).setVisibility(View.VISIBLE);
        	findViewById(R.id.rotate).setOnClickListener(new View.OnClickListener() {
        		
        		public void onClick(View v) {
        			if (isFinishing()) {
        				return;
        			}
        			cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
        		}
        	});
		}else{
			findViewById(R.id.rotate).setVisibility(View.GONE);
		}
        
    }
    
    private void onSaveClicked() {
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mSaving)
            return;
        mSaving = true;
        Bitmap croppedImage = cropImageView.getCroppedImage();
        Bundle myExtras = getIntent().getExtras();
        if (myExtras != null && (myExtras.getParcelable("data") != null || myExtras.getBoolean("return-data"))) {
            Bundle extras = new Bundle();
            extras.putParcelable("data", croppedImage);
            setResult(RESULT_OK, (new Intent()).setAction("inline-data").putExtras(extras));
            finish();
        } else {
            final Bitmap b = croppedImage;
            Util.startBackgroundJob(this, null, (String) getText(R.string.image_select_crop_img), new Runnable() {
                public void run() {
                    saveOutput(b);
                }
            }, mHandler);
        }
    }
    
    private void saveOutput(Bitmap croppedImage) {
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = mContentResolver.openOutputStream(mSaveUri);
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 75, outputStream);
                }
            } catch (IOException ex) {
                // TODO: report error to caller
                LogUtil.e(TAG, "Cannot open file: " + mSaveUri, ex);
            } finally {
                Util.closeSilently(outputStream);
            }
            Bundle extras = new Bundle();
            setResult(RESULT_OK, new Intent(mSaveUri.toString()).putExtras(extras));
        } else {
            LogUtil.e(TAG, "neni definovana adresa pro ulozeni");
            /*
             * Bundle extras = new Bundle(); extras.putString("rect", mCrop.getCropRect().toString());
             * 
             * File oldPath = new File(mImage.getDataPath()); File directory = new File(oldPath.getParent());
             * 
             * int x = 0; String fileName = oldPath.getName(); fileName = fileName.substring(0,
             * fileName.lastIndexOf("."));
             * 
             * // Try file-1.jpg, file-2.jpg, ... until we find a filename which // does not exist yet. while (true) { x
             * += 1; String candidate = directory.toString() + "/" + fileName + "-" + x + ".jpg"; boolean exists = (new
             * File(candidate)).exists(); if (!exists) { break; } }
             * 
             * try { Uri newUri = ImageManager.addImage( mContentResolver, mImage.getTitle(), mImage.getDateTaken(),
             * null, // TODO this null is going to cause us to lose // the location (gps). 0, // TODO this is going to
             * cause the orientation // to reset. directory.toString(), fileName + "-" + x + ".jpg");
             * 
             * Cancelable<Void> cancelable = ImageManager.storeImage( newUri, mContentResolver, 0, // TODO fix this
             * orientation croppedImage, null);
             * 
             * cancelable.get(); setResult(RESULT_OK, new Intent() .setAction(newUri.toString()) .putExtras(extras)); }
             * catch (Exception ex) { // basically ignore this or put up // some ui saying we failed Log.e(TAG,
             * "store image fail, continue anyway", ex); }
             */
        }
        croppedImage.recycle();
        finish();
    }
    
    private Bitmap getBitmap(String path) {
        Uri uri = getImageUri(path);
        InputStream in = null;
        try {
            in = mContentResolver.openInputStream(uri);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 2;// getPowerOfTwoForSampleRatio(ratio);
            bitmapOptions.inDither = true;// optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
            return BitmapFactory.decodeStream(in, null, bitmapOptions);
        } catch (FileNotFoundException e) {
            LogUtil.e(TAG, "file " + path + " not found");
        }
        return null;
    }
        
    private Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }
}
