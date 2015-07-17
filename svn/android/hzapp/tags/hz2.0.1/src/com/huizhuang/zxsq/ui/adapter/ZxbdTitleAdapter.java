package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.zxdb.Zxbd;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;

public class ZxbdTitleAdapter extends CommonBaseAdapter<Zxbd>{
	public ZxbdTitleAdapter(Context context) {
		super(context);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView==null){
			convertView = mLayoutInflater.inflate(R.layout.adapter_zxbd_title_item, parent,false);
			vh = new ViewHolder();
			vh.tv_title = (TextView)convertView.findViewById(R.id.tv_adaper_zxbd_title_item_title);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		vh.tv_title.setText("  · "+getList().get(position).getTitle());
		return convertView;
	}
	class ViewHolder{
		private TextView tv_title;
	}
}
