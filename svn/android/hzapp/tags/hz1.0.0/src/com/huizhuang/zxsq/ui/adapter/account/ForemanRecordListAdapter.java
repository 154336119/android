package com.huizhuang.zxsq.ui.adapter.account;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.http.bean.account.ForemanRecord;
import com.huizhuang.zxsq.http.bean.common.Image;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;


/** 
* @ClassName: ForemanRecordListAdapter 
* @Description: 工长记录适配器
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-10 下午5:15:36 
*  
*/
public class ForemanRecordListAdapter extends CommonBaseAdapter<ForemanRecord> {

	private ViewHolder mViewHolder;
	private List<KeyValue> mNodes;
	private Context mContext;
	private Handler mHandler;

	public ForemanRecordListAdapter(Context context,Handler handler) {
		super(context);
		this.mNodes = ZxsqApplication.getInstance().getConstant().getJlNodes();
		this.mContext = context;
		this.mHandler = handler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ForemanRecord foremanRecord = getItem(position);
		LogUtil.d("getView position = " + position + " foremanRecord = " + foremanRecord);

		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_foreman_record_list, parent, false);
			mViewHolder.itemLineTop = (View) convertView.findViewById(R.id.v_line_top);
			mViewHolder.itemLineBottom = (View) convertView.findViewById(R.id.v_line_bottom);
			mViewHolder.itemIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
			mViewHolder.itemNodeName = (TextView) convertView.findViewById(R.id.tv_node_name);			
			mViewHolder.itemContent = (TextView) convertView.findViewById(R.id.tv_content);
			mViewHolder.itemTime = (TextView) convertView.findViewById(R.id.tv_time);
			mViewHolder.itemImgContainer = (LinearLayout) convertView.findViewById(R.id.lin_img_container);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		if(position == 0){
			mViewHolder.itemLineTop.setVisibility(View.INVISIBLE);
		}else{
			mViewHolder.itemLineTop.setVisibility(View.VISIBLE);
		}
		
		if(position == (getList().size()-1)){
			mViewHolder.itemLineBottom.setVisibility(View.INVISIBLE);
		}else{
			mViewHolder.itemLineBottom.setVisibility(View.VISIBLE);
		}
		if("p1".equals(foremanRecord.getZx_node())){
			mViewHolder.itemIcon.setBackgroundResource(R.drawable.foreman_constructionsite_stage_start_big);
		}else if("p2".equals(foremanRecord.getZx_node())){
			mViewHolder.itemIcon.setBackgroundResource(R.drawable.foreman_constructionsite_stage_hydropower_big);
		}else if("p3".equals(foremanRecord.getZx_node())){
			mViewHolder.itemIcon.setBackgroundResource(R.drawable.foreman_constructionsite_stage_mudwood_big);
		}else if("p4".equals(foremanRecord.getZx_node())){
			mViewHolder.itemIcon.setBackgroundResource(R.drawable.foreman_constructionsite_stage_paint_big);
		}else if("p5".equals(foremanRecord.getZx_node())){
			mViewHolder.itemIcon.setBackgroundResource(R.drawable.foreman_constructionsite_stage_completed_big);
		}else if("p6".equals(foremanRecord.getZx_node())){
			mViewHolder.itemIcon.setBackgroundResource(R.drawable.foreman_constructionsite_stage_real_big);
		}
		if("p6".equals(foremanRecord.getZx_node())){
			mViewHolder.itemNodeName.setText("完工实景");
		}else{
			for (KeyValue keyValue : mNodes) {
				if(keyValue.getId().equals(foremanRecord.getZx_node())){
					mViewHolder.itemNodeName.setText(keyValue.getName());
					break;
				}
			}
		}
		mViewHolder.itemContent.setText(foremanRecord.getDesc());
		mViewHolder.itemTime.setText(DateUtil.timestampToSdate(foremanRecord.getAdd_time(), "MM/dd HH:mm"));
		List<Image> images = foremanRecord.getImage();
		if(images != null && images.size() > 0){
			childView(images,mViewHolder.itemImgContainer,position);
		}
		return convertView;
	}

	private void childView(List<Image> images,LinearLayout imgContainer,int position){
		imgContainer.removeAllViews();
		switch (images.size()) {
		case 1:
			Image image = images.get(0);
			ImageView imageView = new ImageView(mContext);
			imageView.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 120), DensityUtil.dip2px(mContext, 120)));
			imageView.setScaleType(ScaleType.FIT_XY);
			imgContainer.addView(imageView);
			ImageLoader.getInstance().displayImage(image.getImg_path(), imageView, ImageLoaderOptions.getDefaultImageOption());
			String tag = 0+","+position;
			imageView.setTag(tag);
			imageView.setOnClickListener(mOnClickListener);
			break;
		case 2:
		case 3:
			addHImg(images, imgContainer,position,1);
			break;
		case 4:
			List<Image> images1 = new ArrayList<Image>();
			List<Image> images2 = new ArrayList<Image>();
			for (int i = 0; i < images.size(); i++) {
				if(i < 2){
					images1.add(images.get(i));
				}else{
					images2.add(images.get(i));
				}
			}
			addHImg(images1, imgContainer,position,1);
			addHImg(images2, imgContainer,position,2);
			break;
		case 5:
			List<Image> images_1 = new ArrayList<Image>();
			List<Image> images_2 = new ArrayList<Image>();
			for (int i = 0; i < images.size(); i++) {
				if(i < 3){
					images_1.add(images.get(i));
				}else{
					images_2.add(images.get(i));
				}
			}
			addHImg(images_1, imgContainer,position,1);
			addHImg(images_2, imgContainer,position,2);
			break;
		case 6:
			List<Image> image_1 = new ArrayList<Image>();
			List<Image> image_2 = new ArrayList<Image>();
			for (int i = 0; i < images.size(); i++) {
				if(i < 3){
					image_1.add(images.get(i));
				}else{
					image_2.add(images.get(i));
				}
			}
			addHImg(image_1, imgContainer,position,1);
			addHImg(image_2, imgContainer,position,2);
			break;
		default:
			List<Image> image_11 = new ArrayList<Image>();
			List<Image> image_22 = new ArrayList<Image>();
			for (int i = 0; i < 6; i++) {
				if(i < 3){
					image_11.add(images.get(i));
				}else{
					image_22.add(images.get(i));
				}
			}
			addHImg(image_11, imgContainer,position,1);
			addHImg(image_22, imgContainer,position,2);
			break;
		}
	}
	
	private void addHImg(List<Image> images,LinearLayout imgContainer,int position,int line){
		LinearLayout lin = new LinearLayout(mContext);
		lin.setPadding(0, DensityUtil.dip2px(mContext, 5), 0, 0);
		lin.setOrientation(LinearLayout.HORIZONTAL);
		lin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		imgContainer.addView(lin);
		for (int i = 0;i < images.size();i++) {
			Image image = images.get(i);
			ImageView imageView = new ImageView(mContext);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 60), DensityUtil.dip2px(mContext, 60));
			lp.setMargins(0, 0, DensityUtil.dip2px(mContext, 5), 0);
			imageView.setLayoutParams(lp);
			imageView.setScaleType(ScaleType.FIT_XY);
			lin.addView(imageView);
			ImageLoader.getInstance().displayImage(image.getImg_path(), imageView,ImageLoaderOptions.getDefaultImageOption());
			String tag = "";
			if(line == 1){
				tag = i + ","+position;
			}else{
				tag = (3+i)+ ","+position;
			}
			imageView.setTag(tag);
			imageView.setOnClickListener(mOnClickListener);
		}
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (null != v.getTag()) {
				String tag[] = v.getTag().toString().split(",");
				int position = Integer.valueOf(tag[1]);
				int what = Integer.valueOf(tag[0]);
				Message msg = mHandler.obtainMessage();
				msg.what = what;
				msg.arg1 = position;
				mHandler.sendMessage(msg);
			}
		}
	};
	
	static class ViewHolder {
		View itemLineTop;
		View itemLineBottom;
		ImageView itemIcon;
		TextView itemNodeName;
		TextView itemContent;
		CircleImageView ivHeader;
		TextView itemTime;
		LinearLayout itemImgContainer;
	}

	
}
