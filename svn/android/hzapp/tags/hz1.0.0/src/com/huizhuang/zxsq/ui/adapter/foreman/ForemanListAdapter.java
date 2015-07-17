package com.huizhuang.zxsq.ui.adapter.foreman;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.foreman.Foreman;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.text.DecimalFormat;
import java.text.ParseException;

public class ForemanListAdapter extends CommonBaseAdapter<Foreman> {

    private Context context;

    private int sort = 0;

    private DisplayImageOptions mImageOptions;

    public ForemanListAdapter(Context context) {
        super(context);
        this.context = context;
        mImageOptions = ImageLoaderOptions.getDefaultImageOption();
    }

    public void setSortType(int sort) {
        this.sort = sort;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemViewHolder holder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.item_foreman, null);
            holder = new ItemViewHolder();
            holder.item_foreman_head = (CircleImageView) view.findViewById(R.id.item_foreman_head);
            holder.item_foreman_name = (TextView) view.findViewById(R.id.item_foreman_name);
            holder.item_foreman_free = (ImageView) view.findViewById(R.id.item_foreman_free);
            holder.item_foreman_insurance = (ImageView) view.findViewById(R.id.item_foreman_insurance);
            holder.item_foreman_zeroincrease = (ImageView) view.findViewById(R.id.item_foreman_zeroincrease);
            holder.item_foreman_compensation = (ImageView) view.findViewById(R.id.item_foreman_compensation);
            holder.item_foreman_qualit = (ImageView) view.findViewById(R.id.item_foreman_qualit);
            holder.item_foreman_distance = (TextView) view.findViewById(R.id.item_foreman_distance);

            holder.item_foreman_score = (RatingBar) view.findViewById(R.id.item_foreman_score);
            holder.item_foreman_score_text = (TextView) view.findViewById(R.id.item_foreman_score_text);
            holder.item_foreman_score_persons = (TextView) view.findViewById(R.id.item_foreman_score_persons);

            holder.item_foreman_appointment_count = (TextView) view.findViewById(R.id.item_foreman_appointment_count);
            holder.item_foreman_quotation_count = (TextView) view.findViewById(R.id.item_foreman_quotation_count);
            holder.item_foreman_construactionsite_count = (TextView) view.findViewById(R.id.item_foreman_construactionsite_count);
            holder.item_foreman_appointment_txt = (TextView) view.findViewById(R.id.item_foreman_appointment_txt);
            holder.item_foreman_quotation_txt = (TextView) view.findViewById(R.id.item_foreman_quotation_txt);
            holder.item_foreman_construactionsite_txt = (TextView) view.findViewById(R.id.item_foreman_construactionsite_txt);

            holder.item_foreman_count_line1 = (ImageView) view.findViewById(R.id.item_foreman_count_line1);
            holder.item_foreman_count_line2 = (ImageView) view.findViewById(R.id.item_foreman_count_line2);

            view.setTag(holder);
        }else{
            holder = (ItemViewHolder) view.getTag();
        }

        Foreman foreman = getItem(position);

        if (foreman.getLogo() != null) {
            ImageUtil.displayImage(getNotNullString(foreman.getLogo().getThumb_path()), holder.item_foreman_head, mImageOptions);
        }
        holder.item_foreman_name.setText(getNotNullString(foreman.getFull_name()));
        holder.item_foreman_free.setVisibility(View.GONE);
        holder.item_foreman_insurance.setVisibility(View.GONE);
        holder.item_foreman_zeroincrease.setVisibility(View.GONE);
        holder.item_foreman_compensation.setVisibility(View.GONE);
        holder.item_foreman_qualit.setVisibility(View.GONE);
        if (foreman.getServices() != null && foreman.getServices().size() > 0) {
            for (String s : foreman.getServices()) {
                if (s != null && "4".equals(s.trim())) {
                    holder.item_foreman_qualit.setVisibility(View.VISIBLE);
                }
                if (s != null && "6".equals(s.trim())) {
                    holder.item_foreman_compensation.setVisibility(View.VISIBLE);
                }
                if (s != null && "8".equals(s.trim())) {
                    holder.item_foreman_insurance.setVisibility(View.VISIBLE);
                }
                if (s != null && "10".equals(s.trim())) {
                    holder.item_foreman_free.setVisibility(View.VISIBLE);
                }
                if (s != null && "11".equals(s.trim())) {
                    holder.item_foreman_zeroincrease.setVisibility(View.VISIBLE);
                }
            }
        }


        //评论、评分
        float score = 0f;
        try {
        	if (foreman.getRank() != null) {
        		score = Float.valueOf(foreman.getRank());
			}
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        holder.item_foreman_score.setProgress((int) (score * 2));
        holder.item_foreman_score.setMax(10);
        if(foreman.getComment_count() == null || foreman.getComment_count().equals("0")){
            holder.item_foreman_score_text.setVisibility(View.GONE);
            holder.item_foreman_score_persons.setText("暂无评价");
        } else {
            holder.item_foreman_score_text.setVisibility(View.VISIBLE);
            holder.item_foreman_score_text.setText(new DecimalFormat("0.0").format(((int) (score * 2)) / 2.0f));
            holder.item_foreman_score_persons.setText(getNotNullString("（"+ foreman.getComment_count())  + "人评价）");
        }

        //预约数
        if (foreman.getOrder_count() == null || foreman.getOrder_count().equals("0")){
            holder.item_foreman_appointment_count.setVisibility(View.GONE);
            holder.item_foreman_appointment_txt.setVisibility(View.GONE);
            holder.item_foreman_count_line1.setVisibility(View.GONE);

        }else{
            holder.item_foreman_appointment_count.setText(getNotNullString(foreman.getOrder_count()));
            holder.item_foreman_appointment_count.setVisibility(View.VISIBLE);
            holder.item_foreman_appointment_txt.setVisibility(View.VISIBLE);
            holder.item_foreman_count_line1.setVisibility(View.VISIBLE);
        }

        //报价数
        if (foreman.getPrice_list_amount() == null || foreman.getPrice_list_amount().equals("0")){
            holder.item_foreman_quotation_count.setVisibility(View.GONE);
            holder.item_foreman_quotation_txt.setVisibility(View.GONE);
            holder.item_foreman_count_line2.setVisibility(View.GONE);
        }else{
            holder.item_foreman_quotation_count.setText(getNotNullString(foreman.getPrice_list_amount()));
            holder.item_foreman_quotation_count.setVisibility(View.VISIBLE);
            holder.item_foreman_quotation_txt.setVisibility(View.VISIBLE);
            holder.item_foreman_count_line2.setVisibility(View.VISIBLE);
        }

        //施工案例数
        if (foreman.getCase_count() == null || foreman.getCase_count().equals("0")){
            holder.item_foreman_construactionsite_count.setVisibility(View.GONE);
            holder.item_foreman_construactionsite_txt.setVisibility(View.GONE);
        }else{
            holder.item_foreman_construactionsite_count.setText(getNotNullString(foreman.getCase_count()));
            holder.item_foreman_construactionsite_count.setVisibility(View.VISIBLE);
            holder.item_foreman_construactionsite_txt.setVisibility(View.VISIBLE);
        }

        //距离、口碑数、成交数
        holder.item_foreman_distance.setVisibility(View.VISIBLE);
        if (sort == 1) {
            if (foreman.getDistance() == null) {
                holder.item_foreman_distance.setText("");
            } else {
                String distance = "";
                DecimalFormat format = new DecimalFormat("0.0");
                try {
                    double d = format.parse(foreman.getDistance()).doubleValue();
                    if (d >= 1000) {
                        distance = format.format(d / 1000f) + "km";
                    } else {
                        distance = format.format(d) + "m";
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.item_foreman_distance.setText(distance);
            }
        } else if (sort == 3) {
            if (foreman.getDone_count() == null || foreman.getDone_count().trim().length() == 0 || "0".equals(foreman.getDone_count().trim())) {
                holder.item_foreman_distance.setText("暂未成交");
            } else {
                holder.item_foreman_distance.setText(foreman.getDone_count() + "成交");
            }
        } else {
            holder.item_foreman_distance.setVisibility(View.GONE);
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

        public CircleImageView item_foreman_head;

        public TextView item_foreman_name;
        public ImageView item_foreman_free;
        public ImageView item_foreman_insurance;
        public ImageView item_foreman_zeroincrease;
        public ImageView item_foreman_compensation;
        public ImageView item_foreman_qualit;
        public TextView item_foreman_distance;

        public RatingBar item_foreman_score;
        public TextView item_foreman_score_text;
        public TextView item_foreman_score_persons;

        public TextView item_foreman_appointment_count;
        public TextView item_foreman_quotation_count;
        public TextView item_foreman_construactionsite_count;

        public TextView item_foreman_appointment_txt;
        public TextView item_foreman_quotation_txt;
        public TextView item_foreman_construactionsite_txt;

        private ImageView item_foreman_count_line1;
        private ImageView item_foreman_count_line2;

    }

}
