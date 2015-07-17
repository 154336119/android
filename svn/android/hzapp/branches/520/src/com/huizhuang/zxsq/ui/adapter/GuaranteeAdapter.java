package com.huizhuang.zxsq.ui.adapter;


import com.huizhuang.hz.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GuaranteeAdapter extends BaseAdapter/* implements OnClickListener*/{
	private int Gurantee_IMAGE_RESIDS[];
	private LayoutInflater mInflater;
	private Context context;
	
	public GuaranteeAdapter(Context context,int Gurantee_IMAGE_RESIDS[]) {
		this.Gurantee_IMAGE_RESIDS= Gurantee_IMAGE_RESIDS;
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		if (Gurantee_IMAGE_RESIDS.length == 0)
			return 0;
		return Integer.MAX_VALUE; // 返回很大的值使得getView中的position不断增大来实现循环
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.guarantee_image_item, parent,
					false);
			vh = new ViewHolder();
			vh.ivGuarantee = (ImageView) convertView
					.findViewById(R.id.ivguarantee);
			/*vh.ivFinish = (ImageView)convertView
					.findViewById(R.id.ivfinish);*/
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		int i = position % Gurantee_IMAGE_RESIDS.length;
		vh.ivGuarantee.setBackgroundResource(Gurantee_IMAGE_RESIDS[i]);
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return Gurantee_IMAGE_RESIDS[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	class ViewHolder{
		ImageView ivGuarantee;
	}
	
}

