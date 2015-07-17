package com.huizhuang.zxsq.ui.activity.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.http.task.order.QueryTagListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.image.ImageSelectActivity;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageLoaderUriUtils;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class OrderEditRemarkActivity extends BaseActivity {

    public static final String PARAM_REMARK = "remark";
    public static final String PARAM_IMAGES = "images";

    private final int REQ_IMAGE_CAPTURE_CODE = 662;
    private CommonActionBar mCommonActionBar;

    private List<KeyValue> mTags;

//    private int mParamType;
    private EditText mEtRemark;
    private String mParamRemark;

    private TagsAdapter mTagsAdapter;
    private ImageGridViewAdapter mGvImageAdapter;
    private ArrayList<String> mImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_edit_remark);
        getIntentExtra();
        initActionBar();
        initViews();

        httpRequestTagList();
    }

    private void getIntentExtra() {
//        mParamType = getIntent().getIntExtra(AppConstants.PARAM_ORDER_TYPE, 0);
        mParamRemark = getIntent().getStringExtra("remark");
        mImageList = getIntent().getStringArrayListExtra(OrderEditRemarkActivity.PARAM_IMAGES);
    }

    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("备注");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCommonActionBar.setRightTxtBtn(R.string.txt_search_completed, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bd = new Bundle();
                bd.putString(PARAM_REMARK, mTagsAdapter.getMacthTxt());
                bd.putStringArrayList(PARAM_IMAGES, mGvImageAdapter.getImageList());
                ActivityUtil.backWithResult(OrderEditRemarkActivity.this, Activity.RESULT_OK, bd);
            }
        });
    }

    private void initViews() {
        mEtRemark = (EditText) findViewById(R.id.et_remark);
        mEtRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mTagsAdapter != null) {
                    mTagsAdapter.setMacthTxt(mEtRemark.getText().toString());
                    mTagsAdapter.notifyDataSetChanged();
                }
            }
        });
        mEtRemark.setText(mParamRemark);
        mTagsAdapter = new TagsAdapter(this);
        mTagsAdapter.setMacthTxt(mParamRemark);
        GridView gvTags = (GridView) findViewById(R.id.gv_tags);
        gvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mTagsAdapter.isSelected(position)) {
                    String content = mEtRemark.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        content = mTagsAdapter.getItem(position).getName();
                    } else {
                        content += "," + mTagsAdapter.getItem(position).getName();
                    }
                    mEtRemark.setText(content);
                    mEtRemark.setSelection(content.length());
                    mTagsAdapter.setMacthTxt(content);
                    mTagsAdapter.notifyDataSetChanged();
                }
            }
        });
        gvTags.setAdapter(mTagsAdapter);

        mGvImageAdapter = new ImageGridViewAdapter(this);
        GridView gvImage = (GridView) findViewById(R.id.gv_image);
        gvImage.setAdapter(mGvImageAdapter);
        gvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mGvImageAdapter.getList().size() == ImageGridViewAdapter.MAX_IMAGE_COUNT) {
                    return;
                }
                if (position == mGvImageAdapter.getCount() - 1) {
                    capture();
                }
            }
        });
        if(mImageList != null){
	        for (String imageUrl : mImageList) {
	        	mGvImageAdapter.addImageAndNotify(imageUrl);
			}
        }
    }


    private void httpRequestTagList() {
        QueryTagListTask task = new QueryTagListTask(this);
        task.setCallBack(new AbstractHttpResponseHandler<List<KeyValue>>() {

            @Override
            public void onStart() {
                super.onStart();
                showWaitDialog("");
            }

            @Override
            public void onSuccess(List<KeyValue> keyValues) {
                mTags = keyValues;
                mTagsAdapter.setList(mTags);
                mTagsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int code, String error) {
                showToastMsg(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        });

        task.send();
    }

    private void capture() {
        String imagePath = Util.createImagePath(this);
        if (imagePath == null) {
            return;
        }
        Intent intent = new Intent(OrderEditRemarkActivity.this, ImageSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("image-path", imagePath);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQ_IMAGE_CAPTURE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_IMAGE_CAPTURE_CODE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getParcelableExtra("image-path-uri");
                mGvImageAdapter.addImageAndNotify(imageUri.getPath());
            }
        }
    }
	
    private class TagsAdapter extends CommonBaseAdapter<KeyValue> {

        private Context mContext;
        private String mMacthTxt;

        public TagsAdapter(Context context) {
            super(context);
            mContext = context;
        }

        public String getMacthTxt() {
            return mMacthTxt;
        }

        public void setMacthTxt(String macthTxt) {
            this.mMacthTxt = macthTxt;
        }

        public boolean isSelected(int position) {
            if (mMacthTxt == null) {
                return false;
            }
            return mMacthTxt.contains(getItem(position).getName());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.adapter_order_remark_tag, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.name);
            tvName.setText(getItem(position).getName());
            if (mMacthTxt != null && mMacthTxt.contains(getItem(position).getName())) {
                tvName.setTextColor(getResources().getColor(R.color.color_cccccc));
            } else {
                tvName.setTextColor(getResources().getColor(R.color.color_666666));

            }
            return convertView;
        }
    }

    class ImageGridViewAdapter extends CommonBaseAdapter<String> {

        public static final int MAX_IMAGE_COUNT = 4;
        private int mWidth = 0;
        private ArrayList<String> mImageList;

        public ImageGridViewAdapter(Context context) {
            super(context);
            mImageList = new ArrayList<String>();
            mWidth = (UiUtil.getScreenWidth((Activity) context) - UiUtil.dp2px(context, 20)) / 4;
        }

        public ArrayList<String> getImageList() {
            return mImageList;
        }

        public void addImageAndNotify(String imageUri) {
            mImageList.add(0, imageUri);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mImageList == null)
                return 1;
            if (mImageList.size() < MAX_IMAGE_COUNT) {
                return mImageList.size() + 1;
            }
            return mImageList.size();
        }

        @Override
        public String getItem(int position) {
            return mImageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ImageView ivImage = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_image, viewGroup, false);
                ivImage = (ImageView) convertView.findViewById(R.id.imgView);
                ivImage.setLayoutParams(new AbsListView.LayoutParams(mWidth, mWidth));
            } else {
                ivImage = (ImageView) convertView;
            }

            if (mImageList.size() < MAX_IMAGE_COUNT && position == getCount() - 1) {
                ImageLoader.getInstance().displayImage(null, ivImage, ImageLoaderOptions.optionsUploadPhotoAdd);
            } else {
                String path = getItem(position);
                String urliStr = ImageLoaderUriUtils.getUriFromLocalFile(path);
                ImageLoader.getInstance().displayImage(urliStr, ivImage);
            }
            return convertView;
        }
    }
}
