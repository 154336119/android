package com.huizhuang.zxsq.ui.adapter.foreman;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.foreman.ForemanComment;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.widget.CircleImageView;

public class ForemanCommentAdapter extends CommonBaseAdapter<ForemanComment>{

    private BaseActivity baseActivity;

    public ForemanCommentAdapter(BaseActivity baseActivity) {
        super(baseActivity);
        this.baseActivity = baseActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ItemViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(baseActivity, R.layout.item_foreman_comment, null);

            holder = new ItemViewHolder();

            holder.ivHead = (CircleImageView) convertView.findViewById(R.id.iv_item_public_comment_head);
            holder.rbScore = (RatingBar) convertView.findViewById(R.id.rb_item_public_comment_score);
            holder.tvPhone = (TextView) convertView.findViewById(R.id.tv_item_public_comment_phone);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_item_public_comment_time);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_item_public_comment_content);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_public_comment_title);
            convertView.setTag(holder);
        }else{
        	holder = (ItemViewHolder) convertView.getTag();
        }
       ForemanComment foremanComment = getItem(position);

       if (foremanComment.getImage() != null && foremanComment.getImage().getThumb_path() != null) {
            ImageUtil.displayImage(getNotNullString(foremanComment.getImage().getThumb_path()),
                    holder.ivHead, ImageLoaderOptions.getDefaultImageOption());
        }

        float score = 0f;
        if (foremanComment.getRank() != null) {
            try {
                score = foremanComment.getRank();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        holder.rbScore.setRating(score);
        
        holder.tvPhone.setText(getNotNullString(foremanComment.getName()));
        if (!TextUtils.isEmpty(foremanComment.getTime())) {
            holder.tvTime.setText(DateUtil.timestampToSdate(foremanComment.getTime(), "yyyy-MM-dd"));
        }else{
        	holder.tvTime.setVisibility(View.GONE);
        }

        holder.tvTitle.setText("最新（"+foremanComment.getNode_name()+"）：");
        if(TextUtils.isEmpty(foremanComment.getContent())){
        	holder.tvContent.setText("默认好评");
        }else{
        	holder.tvContent.setText(foremanComment.getContent());
        }
        return convertView;
    }

    private String getNotNullString(String str) {
        if (str == null) {
            str = "";
        }
        return str.trim();
    }

    class ItemViewHolder {
        private CircleImageView ivHead;
        private RatingBar rbScore;
        private TextView tvPhone;
        private TextView tvTime;
        private TextView tvContent;
        private TextView tvTitle;
    }

}
