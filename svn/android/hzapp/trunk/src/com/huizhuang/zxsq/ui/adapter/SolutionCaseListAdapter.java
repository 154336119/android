package com.huizhuang.zxsq.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.solution.Solution;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DistanceUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/** 
* @ClassName: SolutionCaseListAdapter 
* @Description: 我的施工案例列表
* @author lim 
* @mail limshare@gmail.com   
* @date 2014-12-29 上午10:42:43 
*  
*/
public class SolutionCaseListAdapter extends CommonBaseAdapter<Solution> {

    private DisplayImageOptions mOptions;
    private Activity activity;
    public SolutionCaseListAdapter(Activity activity) {
    	super(activity);
    	this.activity = activity;
        mOptions = ImageLoaderOptions.optionsSolutionList;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
    	Holder holder;
		if (view == null) {
			holder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_solution, viewGroup, false);
            holder.ivImage = (ImageView) view.findViewById(R.id.iv_item_solution_image);
            holder.ivBackground = (ImageView) view.findViewById(R.id.iv_item_solution_background);
            holder.tvAddress = (TextView) view.findViewById(R.id.tv_item_solution_address);
            holder.tvNodeName = (TextView) view.findViewById(R.id.tv_item_solution_nodename);
            holder.tvDetail = (TextView) view.findViewById(R.id.tv_item_solution_detail);
            holder.tvDistance = (TextView) view.findViewById(R.id.tv_item_solution_distance);
            holder.tvEye = (TextView) view.findViewById(R.id.tv_item_solution_eye);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Solution solution = getItem(position);
        if (solution.getImage() != null) {
        	LayoutParams lp = UiUtil.getFrameLayoutScreenScale(activity, 2);
        	holder.ivImage.setLayoutParams(lp);
        	holder.ivBackground.setLayoutParams(lp);
        	//ImageAware imageAware = new ImageViewAware(holder.ivImage, false);
        	//ImageLoader.getInstance().displayImage(solution.getImage().getThumb_path(), imageAware, mOptions);
            ImageLoader.getInstance().displayImage(solution.getImage().getThumb_path(), holder.ivImage, mOptions);
        	/*ImageLoader.getInstance().loadImage(solution.getImage().getThumb_path(), new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
					holder.ivImage.setImageBitmap(bitmap);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					
				}
			});*/
        }
        holder.tvAddress.setText(solution.getHousing_name());
        if(!TextUtils.isEmpty(solution.getZx_node())){
            holder.tvNodeName.setText("（"+Util.getNodeNameById(solution.getZx_node()).trim().substring(0, 2)+"）");
        }
        //详情
        String details = "";
        if(!TextUtils.isEmpty(solution.getRoom_style())){//风格
            details += solution.getRoom_style();
        }
        if(!TextUtils.isEmpty(solution.getDoor_model())){//户型
        	if(!TextUtils.isEmpty(details)){
            	details += " | ";
            }
            details += solution.getDoor_model();
        }
        if(!TextUtils.isEmpty(solution.getRoom_area())){
        	if(!TextUtils.isEmpty(details)){
            	details += " | ";
            }
            details += solution.getRoom_area()+"㎡";
        }
        if(!TextUtils.isEmpty(solution.getCost())){
        	if(!TextUtils.isEmpty(details)){
            	details += " | ";
            }
        	details += solution.getCost()+"万";
        }
        holder.tvDetail.setText(details);
        Double d = 0.0;
        try {
            d= Double.valueOf(solution.getDistance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //距离
        String distance = DistanceUtil.getDistance(d);
        if(!TextUtils.isEmpty(distance)){
        	holder.tvDistance.setText(distance);
        	holder.tvDistance.setVisibility(View.VISIBLE);
        }else{
        	holder.tvDistance.setVisibility(View.GONE);
        }
        //人气
        holder.tvEye.setText(solution.getViews()+"");
        return view;
    }

    private class Holder {
        ImageView ivImage;
        ImageView ivBackground;
        TextView tvAddress;
        TextView tvNodeName;
        TextView tvDetail;
        TextView tvDistance;
        TextView tvEye;
    }
}