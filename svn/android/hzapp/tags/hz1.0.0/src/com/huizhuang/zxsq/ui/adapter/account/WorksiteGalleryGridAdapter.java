package com.huizhuang.zxsq.ui.adapter.account;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.account.Gallery;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;



/** 
* @ClassName: WorksiteGalleryGridAdapter 
* @Description: 质量报告图集适配器
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-6 下午5:24:55 
*  
*/
public class WorksiteGalleryGridAdapter extends CommonBaseAdapter<Gallery> {

	private int mWidth;
	public WorksiteGalleryGridAdapter(Context context,int width) {
		super(context);
		this.mWidth = width;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Gallery gallery = getItem(position);
		LogUtil.e("getView position = " + position + " gallery = " + gallery);

		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_worksite_gallery, parent, false);
			convertView.setLayoutParams(new AbsListView.LayoutParams(mWidth,mWidth));
			viewHolder.ivImg = (ImageView) convertView.findViewById(R.id.iv_img);
			viewHolder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(gallery.getPic().getImg_path(), viewHolder.ivImg, ImageLoaderOptions.getDefaultImageOption());
		if(TextUtils.isEmpty(gallery.getName())){
			viewHolder.tvDes.setVisibility(View.GONE);
		}else{
			viewHolder.tvDes.setVisibility(View.VISIBLE);
			viewHolder.tvDes.setText(gallery.getName());
		}
		return convertView;
	}

	static class ViewHolder {
		private ImageView ivImg;
		private TextView tvDes;
	}

}
