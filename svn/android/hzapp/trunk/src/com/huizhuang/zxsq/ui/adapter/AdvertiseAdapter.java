package com.huizhuang.zxsq.ui.adapter;


import java.util.List;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.advertise.Advertise;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AdvertiseAdapter extends BaseAdapter/* implements OnClickListener*/{
	private List<Advertise> advertiseList;
	private LayoutInflater mInflater;
	private Context context;
	
	public AdvertiseAdapter(Context context,List<Advertise> advertiselist) {
		this.advertiseList= advertiselist;
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		if (advertiseList.size() == 0)
			return 0;
		//return advertiseList.size();
		return Integer.MAX_VALUE; // 返回很大的值使得getView中的position不断增大来实现循环
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.advertise_image_item, parent,
					false);
			vh = new ViewHolder();
			vh.ivAdvertise = (ImageView) convertView
					.findViewById(R.id.iv_advertise);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		int i = position % advertiseList.size();
		ImageUtil.displayImage(advertiseList.get(i).getImg(), vh.ivAdvertise);
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return advertiseList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	class ViewHolder{
		ImageView ivAdvertise;
	}
	
}

