package com.huizhuang.zxsq.ui.adapter.decoration;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.LogUtil;
import com.umeng.message.proguard.T;


/** 
* @ClassName: DesignSchemeListAdapter 
* @Description: 设计方案列表Adapter 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-1-13 下午4:11:47 
*  
*/
public class DesignSchemeListAdapter extends CommonBaseAdapter<T> {

	private ViewHolder mHolder;
	
	public DesignSchemeListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);

		if (null == convertView) {
			mHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_decoration_design_scheme, parent, false);
			mHolder.itemPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
			mHolder.itemHeadimg = (ImageView) convertView.findViewById(R.id.iv_headimg);
			mHolder.itemTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.itemContent = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		
//		Message message = getItem(position);
//		LogUtil.d("getView Message = " + message);
//
//		ImageLoader.getInstance().displayImage(message.getUserThumb(), mHolder.itemHeader, ImageLoaderOptions.optionsMyMessageHeader);
//		String date = DateUtil.dateToStrLong(new Date(message.getAddTime()));
//		mHolder.itemTime.setText(date);
//		mHolder.itemContent.setText(Html.fromHtml(message.getContent()));

		return convertView;
	}

	static class ViewHolder {
		private ImageView itemPhoto;
		private ImageView itemHeadimg;
		private TextView itemTitle;
		private TextView itemContent;
	}

}
