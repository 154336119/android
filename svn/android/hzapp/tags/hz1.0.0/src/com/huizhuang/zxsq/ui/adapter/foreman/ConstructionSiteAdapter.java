package com.huizhuang.zxsq.ui.adapter.foreman;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.foreman.ConstructionSite;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;

public class ConstructionSiteAdapter extends
		CommonBaseAdapter<ConstructionSite> {

	private Context context;

	public ConstructionSiteAdapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.item_foreman_constructionsite, null);

			ItemViewHolder holder = new ItemViewHolder();

			holder.item_foreman_constructionsite_img = (ImageView) convertView
					.findViewById(R.id.item_foreman_constructionsite_img);
			holder.item_foreman_constructionsite_stage = (TextView) convertView
					.findViewById(R.id.item_foreman_constructionsite_stage);
			holder.item_foreman_constructionsite_community = (TextView) convertView
					.findViewById(R.id.item_foreman_constructionsite_community);
			holder.item_foreman_constructionsite_housetype = (TextView) convertView
					.findViewById(R.id.item_foreman_constructionsite_housetype);
			holder.item_foreman_constructionsite_measure = (TextView) convertView
					.findViewById(R.id.item_foreman_constructionsite_measure);
			holder.item_foreman_constructionsite_days = (TextView) convertView
					.findViewById(R.id.item_foreman_constructionsite_days);
			holder.item_foreman_constructionsite_price = (TextView) convertView
					.findViewById(R.id.item_foreman_constructionsite_price);

			holder.item_foreman_constructionsite_measure_line = (ImageView) convertView
					.findViewById(R.id.item_foreman_constructionsite_measure_line);
			holder.item_foreman_constructionsite_days_line = (ImageView) convertView
					.findViewById(R.id.item_foreman_constructionsite_days_line);
			holder.item_foreman_constructionsite_price_line = (ImageView) convertView
					.findViewById(R.id.item_foreman_constructionsite_price_line);
			convertView.setTag(holder);
		}

		ItemViewHolder holder = (ItemViewHolder) convertView.getTag();

		ConstructionSite site = getItem(position);

		//图片
		if (site.getImage() != null) {
			ImageUtil.displayImage(getNotNullString(site.getImage()
					.getThumb_path()),
					holder.item_foreman_constructionsite_img,
					ImageLoaderOptions.getDefaultImageOption());
		}
		int stageI = 5;
		String stage = getNotNullString(site.getPhrase());
		if(stage.length()>0){
			try {
				stageI = Integer.valueOf(stage);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		switch (stageI) {
		case 1:
			stage = "开工阶段";
			break;
		case 2:
			stage = "水电阶段";
			break;
		case 3:
			stage = "泥木阶段";
			break;
		case 4:
			stage = "油漆阶段";
			break;
		case 5:
			stage = "竣工阶段";
			break;
		case 6:
			stage = "完工实景";
			break;
		default:
			stage = "";
			break;
		}
		//阶段名
		holder.item_foreman_constructionsite_stage
				.setText(stage);
		//装修现场名称
		holder.item_foreman_constructionsite_community
				.setText(getNotNullString(site.getSite_name()));
		//户型
		String door_model =  getNotNullString(site.getDoor_model());
		if(door_model.trim().length()==0){
			holder.item_foreman_constructionsite_housetype
			.setVisibility(View.GONE);
			holder.item_foreman_constructionsite_measure_line
			.setVisibility(View.GONE);
		}else{
			holder.item_foreman_constructionsite_housetype
			.setText(door_model);
		}
		String size =  getNotNullString(site.getSize());
		if(size.trim().length()==0){
			holder.item_foreman_constructionsite_measure
			.setVisibility(View.GONE);
			holder.item_foreman_constructionsite_days_line
			.setVisibility(View.GONE);
		}else{
			holder.item_foreman_constructionsite_measure
			.setText(size+"㎡ ");
		}
		String construction_period =  getNotNullString(site.getConstruction_period());
		if(construction_period.trim().length()==0){
			holder.item_foreman_constructionsite_days
			.setVisibility(View.GONE);
			holder.item_foreman_constructionsite_price_line
			.setVisibility(View.GONE);
		}else{
			holder.item_foreman_constructionsite_days
			.setText(construction_period+"天");
		}

		String coset = site.getCost()==0?"":site.getCost()+"万";
		String way = getNotNullString(site.getRenovation_way());
		if(way.length()>0&&coset.length()>0){
			coset = coset+"/"+way;
		}else{
			coset = coset+way;
		}
		holder.item_foreman_constructionsite_price.setText(coset);
		return convertView;
	}

	private String getNotNullString(String str) {
		if (str == null) {
			str = "";
		}
		return str.trim();
	}

	class ItemViewHolder {

		public ImageView item_foreman_constructionsite_img;
		public TextView item_foreman_constructionsite_stage;

		public TextView item_foreman_constructionsite_community;
		public TextView item_foreman_constructionsite_housetype;
		public TextView item_foreman_constructionsite_measure;
		public TextView item_foreman_constructionsite_days;
		public TextView item_foreman_constructionsite_price;

		public ImageView item_foreman_constructionsite_measure_line;
		public ImageView item_foreman_constructionsite_days_line;
		public ImageView item_foreman_constructionsite_price_line;

	}

}
