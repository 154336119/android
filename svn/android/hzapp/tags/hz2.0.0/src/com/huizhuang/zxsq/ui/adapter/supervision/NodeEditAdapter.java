package com.huizhuang.zxsq.ui.adapter.supervision;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.supervision.NodeEdit;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;


/**
 * 接单工长适配器
 * @author th
 *
 */
public class NodeEditAdapter extends CommonBaseAdapter<NodeEdit> {

	private ViewHolder mHolder;
	private Context mContext;
	private boolean mIsHistory = true;
	
	public NodeEditAdapter(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);
		NodeEdit nodeEdit = getItem(position);
		if (null == convertView) {
			mHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_node_edit_report, parent, false);
			mHolder.itemLinHisTory = (LinearLayout) convertView.findViewById(R.id.lin_history_des);
            mHolder.itemTvNodeName = (TextView) convertView.findViewById(R.id.tv_node_name);
			mHolder.itemTvScore = (TextView) convertView.findViewById(R.id.tv_score);
			mHolder.itemTvTime = (TextView) convertView.findViewById(R.id.tv_time);
			
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(position == 0){
		    if(nodeEdit.getStatus() > -1){//整改确认
		        mIsHistory = false;
		    }else{//整改中
		        mIsHistory = true;
		    }
		}
		
		if(mIsHistory){
		    mHolder.itemLinHisTory.setVisibility(View.GONE);
		}else{
		    if(position == 1){
		        mHolder.itemLinHisTory.setVisibility(View.VISIBLE);
		    }else{
		        mHolder.itemLinHisTory.setVisibility(View.GONE);
		    }
		}
		
		String nodeName = Util.getNodeNameById(nodeEdit.getNode_id()).substring(0,2)+"阶段\n(待验收)";
		SpannableString nodetext = new SpannableString(nodeName);
		nodetext.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mContext, 28)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		nodetext.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mContext, 20)), 4, nodeName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mHolder.itemTvNodeName.setText(nodetext);
		
		String score = nodeEdit.getTotal_score();
		if(!TextUtils.isEmpty(score)){
		    int score1 = Integer.valueOf(score);
	        if(score1 >= 80){
	            mHolder.itemTvScore.setTextColor(Color.GREEN);
	        }else if(score1 < 60){
	            mHolder.itemTvScore.setTextColor(Color.RED);
	        }else{
	            mHolder.itemTvScore.setTextColor(mContext.getResources().getColor(R.color.color_ff6c38));
	        }
            SpannableString scoreTxt = new SpannableString(score1+"分");
            if(score1 < 10){
                scoreTxt.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mContext, 36)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                scoreTxt.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mContext, 14)), 1, scoreTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else if(score1 < 100){
                scoreTxt.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mContext, 36)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                scoreTxt.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mContext, 14)), 2, scoreTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                scoreTxt.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mContext, 36)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                scoreTxt.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(mContext, 14)), 3, scoreTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            mHolder.itemTvScore.setText(scoreTxt);
		}
		String time = DateUtil.timestampToSdate(nodeEdit.getLast_time(), "yyyy-MM-dd");
		mHolder.itemTvTime.setText("提交报告时间\n"+time);
		return convertView;
	}
	
	static class ViewHolder {
	    private LinearLayout itemLinHisTory;
	    private TextView itemTvNodeName;
	    private TextView itemTvScore;
        private TextView itemTvTime;
	}

}
