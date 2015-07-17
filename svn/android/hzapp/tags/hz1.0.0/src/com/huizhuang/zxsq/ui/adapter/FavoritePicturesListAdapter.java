package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.module.UserFavorSketch.ImageInfo;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;

import java.util.List;

/**
 * 收藏图片列表Adapter
 * 
 * @ClassName: FavoritePicturesListAdapter
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-30 上午10:40:00
 */
public class FavoritePicturesListAdapter extends CommonBaseAdapter<ImageInfo> {

    private List<KeyValue> mRoomStyle;

	public FavoritePicturesListAdapter(Context context) {
		super(context);
        mRoomStyle = ZxsqApplication.getInstance().getConstant().getRoomStyles();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);

		final ImageInfo imageInfo = getItem(position);
		LogUtil.d("getView ImageInfo = " + imageInfo);

		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_favorite_pictures_list, parent, false);
			viewHolder.itemImgPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
			viewHolder.itemTvTitle = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.itemTvContent = (TextView) convertView.findViewById(R.id.tv_desc);
            viewHolder.itemTvStyle = (TextView) convertView.findViewById(R.id.tv_style);
            convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 设置需要显示的数据
		ImageUtil.displayImage(imageInfo.getImg().getImg_path(), viewHolder.itemImgPhoto, ImageLoaderOptions.getDefaultImageOption());
		viewHolder.itemTvTitle.setText(imageInfo.getName());
		viewHolder.itemTvContent.setText(imageInfo.getDigest());
        viewHolder.itemTvStyle.setText(getStyleById(imageInfo.getRoom_style()));
        return convertView;
	}

    private String getStyleById(String id){
        for (int i = 0; i < mRoomStyle.size(); i++) {
            if (mRoomStyle.get(i).getId().equals(id)) {
                return mRoomStyle.get(i).getName();
            }
        }
        return "";
    }

	static class ViewHolder {
		private ImageView itemImgPhoto;
		private TextView itemTvTitle;
		private TextView itemTvContent;
		private TextView itemTvStyle;
	}

}
