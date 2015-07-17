package com.huizhuang.zxsq.ui.activity.diary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage.OnPictureSavedListener;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.GPUImageFilterTools;
import com.huizhuang.zxsq.utils.GPUImageFilterTools.FilterList;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.widget.ActionSheetDialog;
import com.huizhuang.zxsq.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.huizhuang.zxsq.widget.ActionSheetDialog.SheetItemColor;

public class ImageEffectsActivity extends BaseActivity implements OnClickListener, OnPictureSavedListener{

	private static final int REQUEST_PICK_IMAGE = 1;
	private final String[] mNode = { "开工阶段", "水电阶段","泥木阶段", "油漆阶段", "竣工阶段" };
	
	private Uri mImageUri;
	private Uri mSaveImageUri;
	private GPUImageView mGPUImageView;
	private FilterList mFilterList;

    private List<KeyValue> mZxNodeList;
	private TextView mTvNode;
	private String mZXNode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUri = getImageUri(getIntent().getData().getPath());
		setContentView(R.layout.image_effects_activity);
		initViews();
	}
	
	private void initViews(){
		findViewById(R.id.btn_close).setOnClickListener(this);
		findViewById(R.id.btn_zx_node).setOnClickListener(this);
		findViewById(R.id.button_effect).setOnClickListener(this);
        findViewById(R.id.button_save).setOnClickListener(this);
        
        mGPUImageView = (GPUImageView) findViewById(R.id.gpuimage);
        mFilterList = GPUImageFilterTools.getFilterList();
        
        int length = mFilterList.names.size();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int gridviewWidth = (int) (UiUtil.dp2px(this, 64) * length + 8 * (length + 1));
        int itemWidth = (int) (UiUtil.dp2px(this, 64));
        
        //根据屏幕密度和你的数据长度设置gridView的宽度，和每个item的宽度
        ImageAdapter adapter = new ImageAdapter(this);
        adapter.setImages(mFilterList.names);
        
        GridView gridView = (GridView) findViewById(R.id.gv);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth , LinearLayout.LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params); //重点
        gridView.setColumnWidth(itemWidth); //重点
        gridView.setHorizontalSpacing(8); //间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(length); //重点
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                GPUImageFilter filter = GPUImageFilterTools.createFilterForType(ImageEffectsActivity.this, mFilterList.filters.get(arg2));
                mGPUImageView.setFilter(filter);
                mGPUImageView.requestRender();
            }
        });
        mZxNodeList = ZxsqApplication.getInstance().getConstant().getZxNodes();
        mTvNode = (TextView) findViewById(R.id.btn_zx_node);
        mZXNode = mZxNodeList.get(0).getId();
        updateZxNodeView(mZxNodeList.get(0).getName());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mGPUImageView.setImage(mImageUri);
	}
	
	private Uri getImageUri(String path) {
		return Uri.fromFile(new File(path));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_close:
			finish();
//			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//			photoPickerIntent.setType("image/*");
//			startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
			break;
		case R.id.btn_zx_node:
			selectZxNode();
			break;
		case R.id.button_effect:
//			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//			photoPickerIntent.setType("image/*");
//			startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
			break;
		case R.id.button_save:
			saveImage();
			break;

		default:
			break;
		}
	}
	
	private void selectZxNode(){
        ActionSheetDialog dialog = new ActionSheetDialog(THIS);
        for(int i=0; i< mZxNodeList.size();i++){
            dialog.addSheetItem(mZxNodeList.get(i).getName(), SheetItemColor.Blue, new OnSheetItemClickListener() {

                @Override
                public void onClick(int which) {
                    mZXNode = mZxNodeList.get(which-1).getId();
                    updateZxNodeView(mZxNodeList.get(which-1).getName());
                }
            });
        }

        dialog.builder().show();
	}
	
	private void updateZxNodeView(String node){
		mTvNode.setText(node);
	}
	
	public static void show(Activity activity, Uri uri,int reqCode){
		Intent intent = new Intent(activity, ImageEffectsActivity.class);
		intent.setData(uri);
		if (!(activity instanceof Activity)) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		activity.startActivityForResult(intent, reqCode);
	}
	
	private void saveImage() {
		showWaitDialog("保存图片...");
		String fileName = System.currentTimeMillis() + ".jpg";
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "zxsq" + "/" + fileName);
		mSaveImageUri = Uri.fromFile(file);
		mGPUImageView.saveToPictures("zxsq", fileName, this);
	}
    
    
    private void handleImage(final Uri selectedImage) {
        mGPUImageView.setImage(selectedImage);
    }
	
	@Override
	public void onPictureSaved(Uri uri) {
		hideWaitDialog();
    	Bundle bd = new Bundle();
    	bd.putParcelable("image-path-uri", mSaveImageUri);
    	bd.putString("zx_node", mZXNode);
    	setResult(Activity.RESULT_OK, getIntent().putExtras(bd));
		finish();
	}

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    handleImage(data.getData());
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    
    class ImageAdapter extends BaseAdapter {  
        private Context mContext;  
        private List<String> images = new ArrayList<String>();  
      
        private int width;
        private int height;
        
		private final int[] IAMGE_RES = { R.drawable.image_effect_1,
				R.drawable.image_effect_2, R.drawable.image_effect_3,
				R.drawable.image_effect_4, R.drawable.image_effect_5,
				R.drawable.image_effect_6, R.drawable.image_effect_7,
				R.drawable.image_effect_8, R.drawable.image_effect_9,
				R.drawable.image_effect_10, R.drawable.image_effect_11,
				R.drawable.image_effect_12, R.drawable.image_effect_13,
				R.drawable.image_effect_14, R.drawable.image_effect_15,
				R.drawable.image_effect_16 };
        
        public ImageAdapter(Context c) {  
            mContext = c;  
            width = UiUtil.dp2px(mContext, 64);
            height = UiUtil.dp2px(mContext, 64);
        }  
      
        public List<String> getImages() {  
            return images;  
        }  
        public void setImages(List<String> images) {  
            this.images = images;  
        }  
      
        public int getCount() {  
            return images.size();  
        }  
      
        public Object getItem(int position) {  
            return images.get(position);  
        }  
      
        public long getItemId(int position) {  
            return 0;  
        }  
      
        // create a new ImageView for each item referenced by the Adapter  
        public View getView(int position, View convertView, ViewGroup parent) {  
            if (convertView == null) {  // if it's not recycled, initialize some attributes  
                convertView = LayoutInflater.from(mContext).inflate(R.layout.image_effects_item, parent, false);
//            	imageView = new ImageView(mContext);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name); 
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_img); 
            imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));  
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);  
            imageView.setImageResource(IAMGE_RES[position]);  
            tvName.setText(images.get(position));
            return convertView;  
        }  
    }  
}
