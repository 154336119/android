package com.huizhuang.zxsq.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.ui.activity.company.CompanyDetailActivity;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;

/**
 * @ClassName: CompanyListViewAdapter
 * @Package com.huizhuang.zxsq.ui.adapter
 * @Description:
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-9-5 下午3:54:25
 */
public class CompanyListViewAdapter extends CommonBaseAdapter<Company> {

	private ViewHolder mHolder;
	private Activity mActivity;

	public CompanyListViewAdapter(Context context, Activity activity) {
		super(context);
		this.mActivity = activity;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			mHolder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.adapter_company_list, viewGroup, false);
			mHolder.ivCover = (ImageView) view.findViewById(R.id.iv_cover);
			mHolder.tvName = (TextView) view.findViewById(R.id.tv_company_name);
			mHolder.tvAveragePrice = (TextView) view.findViewById(R.id.tv_average_price);
			mHolder.tvUndertakePrice = (TextView) view.findViewById(R.id.tv_undertake_price);
			mHolder.tvScore = (TextView) view.findViewById(R.id.tv_score);
			mHolder.tvFilterContext = (TextView) view.findViewById(R.id.tv_filter_context);			
			mHolder.ivFree = (ImageView) view.findViewById(R.id.iv_free);
			mHolder.ivQuality = (ImageView) view.findViewById(R.id.iv_quality);
			mHolder.ivEnsure = (ImageView) view.findViewById(R.id.iv_ensure);
			mHolder.ivZero = (ImageView) view.findViewById(R.id.iv_zero);
			mHolder.ivBack = (ImageView) view.findViewById(R.id.iv_back);
			mHolder.ivPromotion = (ImageView) view.findViewById(R.id.iv_promotion);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		Company company = getList().get(position);
//		if(!TextUtils.isEmpty(company.getShortName())){
//			mHolder.tvName.setText(company.getShortName());
//		}else{
//			mHolder.tvName.setText(company.getFullName());
//		}
//		if("0".equals(company.getContractPrice())){
//			mHolder.tvMoney.setText("暂无报价");
//		}else{
//		    mHolder.tvMoney.setText("￥" + company.getContractPrice());
//		}
//		mHolder.tvOrderNum.setText(company.getOrderNum() + "");
//		mHolder.rbScore.setRating(company.getScore());
//		//设置热门评论
//		if(company.getHotComment()!=null){
//			String author = company.getHotComment().getUsername()+" ";
//			String comment = company.getHotComment().getContent();
//
//		    mHolder.tvHotComment.setText(Html.fromHtml("<font color='#FF6C38'>"+author+"</font>"+"<font color='#808080'>"+comment+"</font>"));
//		}
//
//		//设置距离
//		LogUtil.d("company.getFullName():"+company.getFullName());
//		LogUtil.d("company.getPy():"+company.getPy());
//		if (ZxsqApplication.getInstance().getUserPoint() != null && !TextUtils.isEmpty(company.getPy())
//				&& !TextUtils.isEmpty(company.getPx())) {
//			mHolder.llLocation.setVisibility(View.VISIBLE);
//			LatLng start = ZxsqApplication.getInstance().getUserPoint();
//			LatLng end = new LatLng(Double.parseDouble(getList().get(position).getPy()), Double.parseDouble(getList().get(position).getPx()));
//			double distance = DistanceUtil.getDistance(start, end) / 1000;
//			String dis = String.valueOf(distance);
//			dis = dis.substring(0, dis.indexOf(".") + 2);
//			mHolder.tvDistance.setText(dis + "km");
//		}else{
//			mHolder.llLocation.setVisibility(View.GONE);
//		}
//        //设置口碑
//		if(company.getdiaryCount()==null){
//			mHolder.tvCommentNum.setText("0");
//		}else{
//			mHolder.tvCommentNum.setText(company.getDiscussNum());
//		}
//        //设置日记提及
//		if(company.getdiaryCount()==null){
//			mHolder.tvDiaryNum.setText("0");
//		}else{
//			mHolder.tvDiaryNum.setText(company.getdiaryCount());
//		}
//		if (company.getCoverAtlas() != null) {
//			LogUtil.d("company.getCoverAtlas().getImage() = " + company.getCoverAtlas().getImage());
//			ImageUtil.displayImage(company.getCoverAtlas().getImage(), mHolder.ivPhoto, ImageLoaderOptions.getDefaultImageOption());
//		}
//		// addListener(view, position);
		return view;
	}

	static class ViewHolder {
		ImageView ivCover;
		TextView tvName;
		TextView tvAveragePrice;
		TextView tvUndertakePrice;
		TextView tvScore;
		TextView tvFilterContext;
		
		ImageView ivFree; 	 //免
		ImageView ivQuality; //质
		ImageView ivEnsure;	 //保
		ImageView ivZero;	 //0
		ImageView ivBack;	 //返
		ImageView ivPromotion;//促
	}

	public void addListener(View convertView, final int position) {
		ViewHolder mHolder = (ViewHolder) convertView.getTag();
		mHolder.ivCover.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Company company = getList().get(position);
				Atlas mAtlas = company.getCoverAtlas();
				Bundle bd = new Bundle();
				bd.putSerializable(AppConstants.PARAM_COMPANY, company);
				bd.putSerializable(AppConstants.PARAM_ATLAS, mAtlas);
				ActivityUtil.next(mActivity, CompanyDetailActivity.class, bd, -1);
				AnalyticsUtil.onEvent(mActivity, UmengEvent.ID_ZX_COMPANY);
				// ActivityUtil.next(mActivity, AtlasBrowseActivity.class, bd,
				// -1);
			}
		});
		
//		mHolder.rr.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Company company = getList().get(position);
//				Atlas mAtlas = company.getCoverAtlas();
//				Bundle bd = new Bundle();
//				bd.putSerializable(Constants.PARAM_ATLAS, mAtlas);
//				bd.putSerializable(Constants.PARAM_COMPANY, company);
//				AnalyticsUtil.onEvent(mActivity, UmengEvent.ID_ZX_COMPANY);
//				ActivityUtil.next(mActivity, CompanyDetailActivity.class, bd, -1);
//			}
//		});
	}


}
