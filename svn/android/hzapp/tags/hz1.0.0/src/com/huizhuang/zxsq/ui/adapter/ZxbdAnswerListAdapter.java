package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.ZxbdAnswer;
import com.huizhuang.zxsq.ui.adapter.base.MyBaseAdapter;

public class ZxbdAnswerListAdapter extends MyBaseAdapter<ZxbdAnswer>{

	private ViewHolder holder;
	
	public ZxbdAnswerListAdapter(Context context) {
		super(context);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			holder = new ViewHolder();
			view = getLayoutInflater().inflate(R.layout.zxbd_list_item, parent, false);
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
		ZxbdAnswer node = getItem(position);
		holder.tvTitle.setText(node.title);
		return view;
	}
	
	class ViewHolder {
		TextView tvTitle;
	}
}