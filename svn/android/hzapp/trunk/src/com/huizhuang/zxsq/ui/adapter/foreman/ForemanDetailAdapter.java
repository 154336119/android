package com.huizhuang.zxsq.ui.adapter.foreman;

import java.text.DecimalFormat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.foreman.ConstructionSite;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class ForemanDetailAdapter extends CommonBaseAdapter<ConstructionSite> {

    private Context context;
    private DecimalFormat mDf;
    private int mWdith;
    private ImageSize mImageSize;

    public ForemanDetailAdapter(Context context,int width) {
        super(context);
        this.context = context;
        this.mWdith = width;
        mImageSize = new ImageSize(480, 300);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemViewHolder holder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.adapter_foreman_detail, null);
            holder = new ItemViewHolder();
            holder.itemImg = (ImageView) view.findViewById(R.id.iv_img);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,((int)((mWdith-DensityUtil.dip2px(mContext, 34)) / 1.6)));
            holder.itemImg.setLayoutParams(lp);
            holder.itemSiteName = (TextView) view.findViewById(R.id.tv_site_name);
            holder.itemDes = (TextView) view.findViewById(R.id.tv_des);
            holder.itemSeeCount = (TextView) view.findViewById(R.id.tv_see);
            view.setTag(holder);
        }else{
            holder = (ItemViewHolder) view.getTag();
        }
        
        ConstructionSite cnstructionSite = getItem(position);

        if (cnstructionSite.getLogo() != null) {
            ImageLoader.getInstance().displayImage(getNotNullString(cnstructionSite.getLogo().getImg_path()),holder.itemImg, ImageLoaderOptions.optionsDefaultEmptyPhoto, mImageSize);
        }
        
        if(mDf == null){
            mDf = new DecimalFormat("#.#");
        }
        holder.itemSiteName.setText(cnstructionSite.getHousing_name());
        String des = "";
        if(!TextUtils.isEmpty(cnstructionSite.getRoom_style())){
            des += cnstructionSite.getRoom_style();
        }
        if(!TextUtils.isEmpty(cnstructionSite.getRoom_number())){
            if(TextUtils.isEmpty(des)){
                des += cnstructionSite.getRoom_number();
            }else{
                des += " | "+cnstructionSite.getRoom_number();
            }
        }
        if(!TextUtils.isEmpty(cnstructionSite.getCost()) && !"0".equals(cnstructionSite.getCost())){
            String price = mDf.format(Double.valueOf(cnstructionSite.getCost()));
            if(!"0".equals(price)){
                if(TextUtils.isEmpty(des)){
                    des += price+"万";
                }else{
                    des += " | "+price+"万";
                }
            }
        }
        holder.itemDes.setText(des);
        if(!TextUtils.isEmpty(des)){
            holder.itemDes.setVisibility(View.VISIBLE); 
        }else{
            holder.itemDes.setVisibility(View.GONE); 
        }
        holder.itemSeeCount.setText(cnstructionSite.getViews() + "");
        return view;
    }

    private String getNotNullString(String str) {
        if (str == null) {
            str = "";
        }
        return str.trim();
    }

    class ItemViewHolder {

        private ImageView itemImg;
        private TextView itemSiteName;
        private TextView itemDes;
        private TextView itemSeeCount;
    }

}
