package com.huizhuang.zxsq.ui.adapter.foreman;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.foreman.Foreman;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.TextViewRotate;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ForemanListAdapter extends CommonBaseAdapter<Foreman> {

    private Context context;

    private DisplayImageOptions mImageOptions;
    private DecimalFormat mDf;

    public ForemanListAdapter(Context context) {
        super(context);
        this.context = context;
        mImageOptions = ImageLoaderOptions.getDefaultImageOption();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemViewHolder holder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.item_foreman, null);
            holder = new ItemViewHolder();
            holder.item_foreman_head = (CircleImageView) view.findViewById(R.id.item_foreman_head);
            holder.item_foreman_name = (TextView) view.findViewById(R.id.item_foreman_name);

            holder.item_foreman_score = (RatingBar) view.findViewById(R.id.item_foreman_score);
            holder.item_foreman_score_text = (TextView) view.findViewById(R.id.item_foreman_score_text);
            holder.item_foreman_price = (TextViewRotate) view.findViewById(R.id.tv_price);

            holder.item_foreman_appointment_count = (TextView) view.findViewById(R.id.item_foreman_appointment_count);
            holder.item_foreman_quotation_count = (TextView) view.findViewById(R.id.item_foreman_quotation_count);
            holder.item_foreman_appointment_txt = (TextView) view.findViewById(R.id.item_foreman_appointment_txt);
            holder.item_foreman_quotation_txt = (TextView) view.findViewById(R.id.item_foreman_quotation_txt);
            holder.item_foreman_dis = (TextView) view.findViewById(R.id.item_dis);
            view.setTag(holder);
        }else{
            holder = (ItemViewHolder) view.getTag();
        }
        
        Foreman foreman = getItem(position);

        if (foreman.getLogo() != null) {
            ImageUtil.displayImage(getNotNullString(foreman.getLogo().getThumb_path()), holder.item_foreman_head, mImageOptions);
        }
        holder.item_foreman_name.setText(getNotNullString(foreman.getFull_name()));
        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_rz);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
        if(foreman.getIs_auth() == 1){
            holder.item_foreman_name.setCompoundDrawables(null, null, drawable, null);
        }else{
            holder.item_foreman_name.setCompoundDrawables(null, null, null, null);
        }

        //评论、评分
        float score = 5;
        try {
        	if (!TextUtils.isEmpty(foreman.getRank())) {
        		score = Float.valueOf(foreman.getRank()) == 0?5:Float.valueOf(foreman.getRank());
			}
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        holder.item_foreman_score.setRating(score);
        holder.item_foreman_score_text.setText(new DecimalFormat("0.0").format(score)+"分");
        Float price = 2f;
        if(!TextUtils.isEmpty(foreman.getAvg_price()) && !foreman.getAvg_price().equals("0")){
            price = Float.valueOf(foreman.getAvg_price())/10000;
        }
        if(mDf == null){
            mDf = new DecimalFormat("#.#");
        }
        String p = mDf.format(price);
        if("0".equals(p)){
            p = "2";
        }
        String str1 = "均价\n"+p+"万";
        SpannableString text = new SpannableString(str1);
        text.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mContext, 10)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mContext, 12)), 3, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.item_foreman_price.setText(text);
        //预约数
        holder.item_foreman_appointment_count.setText(getNotNullString(foreman.getOrder_count()));
        //工龄
        holder.item_foreman_quotation_count.setText(foreman.getEmployed_age()+"年");
        if(TextUtils.isEmpty(foreman.getDistance())){
            holder.item_foreman_dis.setVisibility(View.GONE);
        }else{
            holder.item_foreman_dis.setVisibility(View.VISIBLE);
            float distance = Float.valueOf(foreman.getDistance());
            if(distance > 1){
                holder.item_foreman_dis.setText(mDf.format(distance)+"Km");
            }else{
                int dis = Float.valueOf((distance * 1000)+"").intValue();
                holder.item_foreman_dis.setText(dis+"m");
            }
        }
        return view;
    }

    private String getNotNullString(String str) {
        if (str == null) {
            str = "";
        }
        return str.trim();
    }

    class ItemViewHolder {

        private CircleImageView item_foreman_head;

        private TextView item_foreman_name;
        private RatingBar item_foreman_score;
        private TextView item_foreman_score_text;
        private TextViewRotate item_foreman_price;
        private TextView item_foreman_appointment_count;
        private TextView item_foreman_quotation_count;
        private TextView item_foreman_appointment_txt;
        private TextView item_foreman_quotation_txt;
        private TextView item_foreman_dis;        

    }

}
