package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.solution.Solution;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/** 
* @ClassName: SolutionCaseListAdapter 
* @Description: 我的施工案例列表
* @author lim 
* @mail limshare@gmail.com   
* @date 2014-12-29 上午10:42:43 
*  
*/
public class SolutionCaseListAdapter extends CommonBaseAdapter<Solution> {

    private Holder mHolder;
    private DisplayImageOptions mOptions;
    
    public SolutionCaseListAdapter(Context context) {
        super(context);
        mOptions = ImageLoaderOptions.getDefaultImageOption();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            mHolder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_solution, viewGroup, false);
            mHolder.ivCoverImage = (ImageView) view.findViewById(R.id.iv_cover_image);
            mHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
            mHolder.tvNodeName = (TextView) view.findViewById(R.id.tv_node_name);
            mHolder.tvProperties = (TextView) view.findViewById(R.id.tv_properties);
            view.setTag(mHolder);
        } else {
            mHolder = (Holder) view.getTag();
        }

        Solution constructionSite = getItem(position);
        if (constructionSite.getImage() != null) {
            ImageLoader.getInstance().displayImage(constructionSite.getImage().getThumb_path(), mHolder.ivCoverImage, mOptions);
        }
        mHolder.tvName.setText(constructionSite.getStore_name());
        if(!TextUtils.isEmpty(constructionSite.getZx_node())){
            mHolder.tvNodeName.setText(Util.getNodeNameById(constructionSite.getZx_node()));
        }
        String properties = "";
        if(!TextUtils.isEmpty(constructionSite.getCost()) && !"0".equals(constructionSite.getCost() )){
            properties += constructionSite.getCost()+"万";
        }
        if(constructionSite.getRenovation_way() == 1){
            properties += "/半包";
        }else{
            properties += "/全包";
        }
        
        if(!TextUtils.isEmpty(constructionSite.getDoor_model()) && !TextUtils.isEmpty(properties)){
            properties += " | ";
            properties += constructionSite.getDoor_model();
        }
        if(constructionSite.getRoom_area() != 0 && !TextUtils.isEmpty(properties)){
            properties += " | ";
            properties += constructionSite.getRoom_area()+"㎡";
        }
        mHolder.tvProperties.setText(properties);
        return view;
    }

    private class Holder {
        ImageView ivCoverImage;
        TextView tvName;
        TextView tvNodeName;
        TextView tvProperties;
    }
}