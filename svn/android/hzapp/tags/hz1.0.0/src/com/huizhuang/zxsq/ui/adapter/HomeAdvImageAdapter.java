package com.huizhuang.zxsq.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.home.Advertise;
import com.huizhuang.zxsq.ui.adapter.base.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeAdvImageAdapter extends MyBaseAdapter<Advertise> implements
		OnClickListener {

	private LayoutInflater mInflater;
	private Context context;

	public HomeAdvImageAdapter(Context context) {
		super(context);
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (getList().size() == 0)
			return 0;
		return Integer.MAX_VALUE; // 返回很大的值使得getView中的position不断增大来实现循环
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ads_image_item, parent,
					false);
			convertView.findViewById(R.id.imgView).setOnClickListener(this);
		}
		if (getList().size() == 0) {
			return convertView;
		}
		Advertise adv = getList().get(position % getList().size());
		ImageView imageView = ((ImageView) convertView
				.findViewById(R.id.imgView));
		ImageLoader.getInstance().displayImage(adv.getImage().getImg_path(),
				imageView);
		imageView.setTag(adv.getUrl());
		return convertView;
	}

	private void toBrowser(String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		context.startActivity(intent);
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.imgView:
			if (context instanceof Activity)
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						String url = (String) v.getTag();
						try {
							toBrowser(url);
							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (toast == null) {
							toast = Toast.makeText(context, "url地址错误！",
									Toast.LENGTH_SHORT);
						}
						toast.show();
					}
				});
			break;

		default:
			break;
		}
	}

	Toast toast;

}
