package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.ZxbdAnswer;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.LogUtil;

public class ZxbdQAListAdapter extends CommonBaseAdapter<ZxbdAnswer> {

	/**
	 * 提问和问答（两种状态）
	 */
	protected static final int TYPE_QUESTION = 0;
	protected static final int TYPE_ANSWER = 1;

	public ZxbdQAListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ZxbdAnswer zxbdAnswer = getItem(position);
		LogUtil.d("getView position = " + position + " zxbdAnswer = " + zxbdAnswer);

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.zxbd_qa_list_item, parent, false);
			viewHolder.llQuestion = (RelativeLayout) convertView.findViewById(R.id.rl_question);
			viewHolder.tvQuestion = (TextView) convertView.findViewById(R.id.tv_question);
			viewHolder.llAnswer = (RelativeLayout) convertView.findViewById(R.id.rl_answer);
			viewHolder.tvAnswer = (TextView) convertView.findViewById(R.id.tv_answer);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (TYPE_QUESTION == zxbdAnswer.type) {
			viewHolder.tvQuestion.setText(zxbdAnswer.content);
			viewHolder.llQuestion.setVisibility(View.VISIBLE);
			viewHolder.llAnswer.setVisibility(View.GONE);
		} else {
			viewHolder.tvAnswer.setText(zxbdAnswer.content);
			viewHolder.llAnswer.setVisibility(View.VISIBLE);
			viewHolder.llQuestion.setVisibility(View.GONE);
		}
		return convertView;
	}

	static class ViewHolder {
		private RelativeLayout llQuestion;
		private TextView tvQuestion;
		private RelativeLayout llAnswer;
		private TextView tvAnswer;
	}

}