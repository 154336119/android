package com.huizhuang.zxsq.ui.adapter.account;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.bean.account.CommentHistory;
import com.huizhuang.zxsq.http.bean.account.CommentsWait;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;




/** 
* @ClassName: CommentHistoryListAdapter 
* @Description: 已点评列表适配器
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-12 上午11:13:54 
*  
*/
public class CommentHistoryListAdapter extends CommonBaseAdapter<CommentHistory> {

	private ViewHolder mViewHolder;
	private Context mContext;

	public CommentHistoryListAdapter(Context context) {
		super(context);
		mContext = context;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);
		CommentHistory commentHistory = getItem(position);
		if (null == convertView) {
			mViewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_comments_history_list, parent, false);
			mViewHolder.linSupervisionContainer = (LinearLayout) convertView.findViewById(R.id.lin_container);
			mViewHolder.ivSupervisionHead = (CircleImageView) convertView.findViewById(R.id.iv_img);
			mViewHolder.tvSupervisionName = (TextView) convertView.findViewById(R.id.tv_name);
			mViewHolder.tvSupervisionNodeName = (TextView) convertView.findViewById(R.id.tv_node_name);
			mViewHolder.tvSupervisionTime = (TextView) convertView.findViewById(R.id.tv_time);
			mViewHolder.gvSupervisionScore = (MyGridView) convertView.findViewById(R.id.gv_score);
			mViewHolder.tvSupervisionContent = (TextView) convertView.findViewById(R.id.tv_content);
			
			mViewHolder.linForemanContainer = (LinearLayout) convertView.findViewById(R.id.lin_foreman_container);
			mViewHolder.ivForemanHead = (CircleImageView) convertView.findViewById(R.id.iv_foreman_img);
			mViewHolder.tvForemanName = (TextView) convertView.findViewById(R.id.tv_foreman_name);
			mViewHolder.tvForemanNodeName = (TextView) convertView.findViewById(R.id.tv_foreman_node_name);
			mViewHolder.tvForemanTime = (TextView) convertView.findViewById(R.id.tv_foreman_time);
			mViewHolder.gvForemanScore = (MyGridView) convertView.findViewById(R.id.gv_foreman_score);
			mViewHolder.tvForemanContent = (TextView) convertView.findViewById(R.id.tv_foreman_content);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		CommentsWait staff = commentHistory.getStaff();
		if(staff != null){
			mViewHolder.linSupervisionContainer.setVisibility(View.VISIBLE);
			if(staff.getImage() != null && !TextUtils.isEmpty(staff.getImage().getThumb_path())){
				ImageLoader.getInstance().displayImage(staff.getImage().getThumb_path(), mViewHolder.ivSupervisionHead, 
						ImageLoaderOptions.getDefaultImageOption());
			}
			mViewHolder.tvSupervisionName.setText(staff.getName());
			mViewHolder.tvSupervisionNodeName.setText(staff.getNode_name());
			mViewHolder.tvSupervisionContent.setText(staff.getContent());
			mViewHolder.tvSupervisionTime.setText(DateUtil.timestampToSdate(staff.getReport_time(), "yyyy-MM-dd"));
			CommentHistoryItemGridAdapter mAdapter = new CommentHistoryItemGridAdapter(mContext, ZxsqApplication.getInstance().getConstant().getJlStaff());
			mAdapter.setList(staff.getRank());
			mViewHolder.gvSupervisionScore.setAdapter(mAdapter);
		}else{
			mViewHolder.linSupervisionContainer.setVisibility(View.GONE);
		}
		
		CommentsWait store = commentHistory.getStore();
		if(store != null && "p5".equals(store.getNode_id())){
			mViewHolder.linForemanContainer.setVisibility(View.VISIBLE);
			if(store.getImage() != null && !TextUtils.isEmpty(store.getImage().getThumb_path())){
				ImageLoader.getInstance().displayImage(store.getImage().getThumb_path(), mViewHolder.ivForemanHead, 
						ImageLoaderOptions.getDefaultImageOption());
			}
			mViewHolder.tvForemanName.setText(store.getName());
			mViewHolder.tvForemanNodeName.setText(store.getNode_name());
			mViewHolder.tvForemanContent.setText(store.getContent());
			mViewHolder.tvForemanTime.setText(DateUtil.timestampToSdate(store.getReport_time(), "yyyy-MM-dd"));
			CommentHistoryItemGridAdapter mAdapter = new CommentHistoryItemGridAdapter(mContext, ZxsqApplication.getInstance().getConstant().getJlGongzhang());
			mAdapter.setList(store.getRank());
			mViewHolder.gvForemanScore.setAdapter(mAdapter);
		}else{
			mViewHolder.linForemanContainer.setVisibility(View.GONE);
		}
		return convertView;
	}

	static class ViewHolder {	
		LinearLayout linSupervisionContainer;
		CircleImageView ivSupervisionHead;
		TextView tvSupervisionName;
		TextView tvSupervisionNodeName;
		TextView tvSupervisionTime;
		MyGridView gvSupervisionScore;
		TextView tvSupervisionContent;
		
		LinearLayout linForemanContainer;		
		CircleImageView ivForemanHead;
		TextView tvForemanName;
		TextView tvForemanNodeName;
		TextView tvForemanTime;
		MyGridView gvForemanScore;
		TextView tvForemanContent;
	}

}
