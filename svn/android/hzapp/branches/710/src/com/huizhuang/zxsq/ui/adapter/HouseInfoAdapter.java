package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.order.MapInfo;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;

public class HouseInfoAdapter extends CommonBaseAdapter<MapInfo> {
	private int mCurpos = 0;// 当前显示的一行,默认为0项选中

	public HouseInfoAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_choise_house, parent, false);
			viewHolder.tvHouseName = (TextView) convertView.findViewById(R.id.tv_house_name);
			viewHolder.tvRoadName = (TextView) convertView.findViewById(R.id.tv_road_name);
			viewHolder.ivChoice = (ImageView) convertView.findViewById(R.id.iv_choice);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (position == mCurpos) {// 如果当前的行就是ListView中选中的一行，就更改显示样式
			viewHolder.ivChoice.setVisibility(View.VISIBLE);
		}else{
			viewHolder.ivChoice.setVisibility(View.GONE);
		}
		MapInfo mapInfo = getItem(position);
		if(position==0){
			viewHolder.tvHouseName.setText("[当前]"+mapInfo.getHouseName());
		}else{
			viewHolder.tvHouseName.setText(mapInfo.getHouseName());
		}
		viewHolder.tvRoadName.setText(mapInfo.getRoadName());
		return convertView;
	}

	public void setCurPos(int pos){
		this.mCurpos = pos;
	}
	static class ViewHolder {
		private TextView tvHouseName;
		private TextView tvRoadName;
		private ImageView ivChoice;
	}

}