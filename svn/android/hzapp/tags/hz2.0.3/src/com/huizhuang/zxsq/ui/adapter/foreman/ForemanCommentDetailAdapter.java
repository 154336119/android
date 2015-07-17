package com.huizhuang.zxsq.ui.adapter.foreman;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.foreman.ForemanComment;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.CircleImageView;

public class ForemanCommentDetailAdapter extends CommonBaseAdapter<ForemanComment>{
	private Context context;
	public ForemanCommentDetailAdapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = View.inflate(context, R.layout.item_foreman_comment_detail, null);
			holder = new ViewHolder();
			holder.ivStagePhoto = (CircleImageView)convertView.findViewById(R.id.iv_item_foreman_comment_detail_stage_photo);
			holder.tvStageName = (TextView)convertView.findViewById(R.id.item_foreman_comment_detail_stage_name);
			holder.tvTime = (TextView)convertView.findViewById(R.id.item_foreman_comment_detail_stage_time);
			holder.tvClientContent = (TextView)convertView.findViewById(R.id.tv_item_foreman_comment_detail_stage_client_content);
			holder.tvProblem = (TextView)convertView.findViewById(R.id.tv_item_foreman_comment_detail_stage_problem);
			holder.tvProblemContent = (TextView)convertView.findViewById(R.id.tv_item_foreman_comment_detail_stage_problem_content);
			holder.ivVerticalLine = (ImageView)convertView.findViewById(R.id.iv_item_foreman_comment_detail_stage_line);
			holder.rbScore = (RatingBar)convertView.findViewById(R.id.rb_item_foreman_comment_detail_stage_score);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		ForemanComment foremanComment = getItem(position);
		String nodeName = foremanComment.getNode_name();
		if(nodeName.equals("竣工")){
			holder.ivStagePhoto.setBackgroundResource(R.drawable.foreman_comment_detail_complete);
		}else if(nodeName.equals("油漆")){
			holder.ivStagePhoto.setBackgroundResource(R.drawable.foreman_comment_detail_paint);
		}else if(nodeName.equals("泥木")){
			holder.ivStagePhoto.setBackgroundResource(R.drawable.foreman_comment_detail_mudwood);
		}else if(nodeName.equals("水电")){
			holder.ivStagePhoto.setBackgroundResource(R.drawable.foreman_comment_detail_complete_hydropower);
		}else if(nodeName.equals("开工")){
			holder.ivStagePhoto.setBackgroundResource(R.drawable.foreman_comment_detail_start);
		}else if(nodeName.equals("量房")){
			holder.ivStagePhoto.setBackgroundResource(R.drawable.foreman_comment_detail_measure_house);
		}
		LogUtil.e("position"+position+"getCount():"+getCount());
		if(position==getCount()-1){
			holder.ivVerticalLine.setVisibility(View.GONE);
		}else{
			holder.ivVerticalLine.setVisibility(View.VISIBLE);
		}
		holder.tvStageName.setText(nodeName+"阶段");
		holder.tvTime.setText(DateUtil.timestampToSdate(foremanComment.getTime(), "yyyy-MM-dd"));
		holder.rbScore.setRating(foremanComment.getRank());
		
		if(TextUtils.isEmpty(foremanComment.getContent())){
        	holder.tvClientContent.setText("默认好评");
        }else{
        	holder.tvClientContent.setText(foremanComment.getContent());
        }
		
		List<String> problems = foremanComment.getProblems();
		if(problems==null){
			holder.tvProblemContent.setVisibility(View.GONE);
			holder.tvProblem.setVisibility(View.GONE);
		}else{
			holder.tvProblemContent.setVisibility(View.VISIBLE);
			holder.tvProblem.setVisibility(View.VISIBLE);
			holder.tvProblemContent.setText(getSeparateProblems(problems));
		}
		return convertView;
	}
	public String getSeparateProblems(List<String> problems){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<problems.size();i++){
			int j = i+1;
			if(j==problems.size()){
				sb.append(j+". "+problems.get(i));
			}else{
				sb.append(j+". "+problems.get(i)+"\n");
			}
		}
		String separateProblems = sb.toString();
	    return separateProblems;
	}
	
	public class ViewHolder{
		private CircleImageView ivStagePhoto;
        private RatingBar rbScore;
        private TextView tvStageName;
        private TextView tvTime;
        private TextView tvClientContent;
        private TextView tvProblemContent;
        private TextView tvProblem;
        private ImageView ivVerticalLine;
	}
}
