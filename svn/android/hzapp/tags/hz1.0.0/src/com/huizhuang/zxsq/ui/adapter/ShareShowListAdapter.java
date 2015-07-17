package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;


/** 
* @ClassName: ShareShowListAdapter 
* @Description: 分享界面apdater
* @author th 
* @mail 342592622@qq.com   
* @date 2014-11-20 上午9:38:21 
*  
*/
public class ShareShowListAdapter extends CommonBaseAdapter<Share> {

	public ShareShowListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.share_show_dialog_item, parent, false);
			viewHolder.itemImgIcon = (ImageView) convertView.findViewById(R.id.img_icon);
			viewHolder.itemTvLabel = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Share share = getItem(position);
		viewHolder.itemImgIcon.setImageResource(share.getImgResouceId());
		viewHolder.itemTvLabel.setText(share.getName());
		return convertView;
	}

	private class ViewHolder {
		ImageView itemImgIcon;
		TextView itemTvLabel;
	}

}
