package com.huizhuang.zxsq.ui.adapter.account;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.Constant.RankType;
import com.huizhuang.zxsq.http.bean.account.Score;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.LogUtil;




/** 
* @ClassName: CommentHistoryListAdapter 
* @Description: 已点评列表适配器
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-12 上午11:13:54 
*  
*/
public class CommentHistoryItemGridAdapter extends CommonBaseAdapter<Score> {

	private ViewHolder mViewHolder;
	private List<RankType> mRankTypes;

	public CommentHistoryItemGridAdapter(Context context,List<RankType> rankType) {
		super(context);
		this.mRankTypes = rankType;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);
		Score score = getItem(position);
		if (null == convertView) {
			mViewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_comments_history_list_grid_item, parent, false);
			mViewHolder.tvScoreName = (TextView) convertView.findViewById(R.id.tv_score_name);
			mViewHolder.tvScore = (TextView) convertView.findViewById(R.id.tv_score);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		for (RankType rankType : mRankTypes) {
			if(rankType.getId() == score.getId()){
				mViewHolder.tvScoreName.setText(rankType.getName()+":");
				break;
			}
		}
		mViewHolder.tvScore.setText(score.getScore() == 0 ? "0分":score.getScore()+"分");
		return convertView;
	}

	static class ViewHolder {	
		TextView tvScoreName;
		TextView tvScore;
	}

}
