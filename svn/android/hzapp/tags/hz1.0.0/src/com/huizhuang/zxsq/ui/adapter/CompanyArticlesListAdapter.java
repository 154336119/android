package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.CompanyArticles;
import com.huizhuang.zxsq.ui.adapter.base.MyBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;


/**
 * @ClassName: CompanyArticlesListAdapter
 * @Package com.huizhuang.zxsq.ui.adapter
 * @Description: 
 * @author jean
 * @mail 
 * @date 
 */
public class CompanyArticlesListAdapter extends MyBaseAdapter<CompanyArticles> {

	public CompanyArticlesListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = getLayoutInflater().inflate(R.layout.adapter_favorite_articles_list, parent, false);
			viewHolder.itemImgPhoto = (ImageView) convertView.findViewById(R.id.item_img_photo);
			viewHolder.itemTvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
			viewHolder.itemTvContent = (TextView) convertView.findViewById(R.id.item_tv_content);
			viewHolder.itemTvLove = (TextView) convertView.findViewById(R.id.item_tv_love);
			viewHolder.itemTvShare = (TextView) convertView.findViewById(R.id.item_tv_share);
			viewHolder.itemTvTime = (TextView) convertView.findViewById(R.id.item_tv_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

			
		}
		CompanyArticles companyArticles = getList().get(position);
		ImageUtil.displayImage(companyArticles.getImg(), viewHolder.itemImgPhoto, ImageLoaderOptions.optionsDefaultEmptyPhoto);

		viewHolder.itemTvTitle.setText(companyArticles.getTtitle());
		viewHolder.itemTvContent.setText(companyArticles.getSummary());
		viewHolder.itemTvLove.setText(companyArticles.getFfCount()+"");
		viewHolder.itemTvShare.setText(companyArticles.getShareCount()+"");
		viewHolder.itemTvTime.setText(DateUtil.timestampToSdate(companyArticles.getAddTime(), "yyyy-MM-dd"));
		return convertView;
	}

	private class ViewHolder {
		ImageView itemImgPhoto;
		TextView itemTvTitle;
		TextView itemTvContent;
		TextView itemTvLove;
		TextView itemTvShare;
		TextView itemTvTime;
	}

}
