package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Discuss;
import com.huizhuang.zxsq.ui.adapter.base.MyBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;

public class DiscussListViewAdapter extends MyBaseAdapter<Discuss>{

	public static final int USER_FUL = 0;
	public static final int STEP = 1;
	
	private Handler mHandle;
	private ViewHolder mHolder;
	
	public DiscussListViewAdapter(Context context, Handler handler) {
		super(context);
		mHandle = handler;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			mHolder = new ViewHolder();
			view = getLayoutInflater().inflate(R.layout.item_company_comment_content, viewGroup, false);
			mHolder.cvIcon = (ImageView) view.findViewById(R.id.cv_icon);
			mHolder.rbScore = (RatingBar) view.findViewById(R.id.rb_score);
			mHolder.tvTime = (TextView) view.findViewById(R.id.tv_time);
			mHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
			
			mHolder.tvComment = (TextView) view.findViewById(R.id.tv_comment);
			mHolder.btnUseful = (Button) view.findViewById(R.id.btn_useful);
			mHolder.btnStep = (Button) view.findViewById(R.id.btn_step);

			view.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) view.getTag();
		}
		
		Discuss discuss = getList().get(position);
		
		mHolder.rbScore.setRating(discuss.getScore());
		String date = DateUtil.timestampToSdate(discuss.getDatetime(), "yyyy-MM-dd HH:mm:ss");
		mHolder.tvTime.setText(DateUtil.friendlyTime(date));
		mHolder.tvName.setText(discuss.getUsername());
		mHolder.tvComment.setText(discuss.getContent());
		mHolder.btnUseful.setText("有用("+discuss.getUpNum()+")");
		mHolder.btnStep.setText("踩("+discuss.getDownNum()+")");
		ImageUtil.displayImage(discuss.getUserThumb(), mHolder.cvIcon, ImageLoaderOptions.optionsUserHeader);

		mHolder.btnStep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				click(STEP,position);		
			}
		});
		mHolder.btnUseful.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				click(USER_FUL,position);
			}
		});

		return view;
	}

	
	static class ViewHolder {
		ImageView cvIcon;
		RatingBar rbScore;
		TextView tvTime;
		TextView tvName;
		TextView tvComment;
		Button btnUseful;
		Button btnStep;
		
	}

	private void click(int type, int position){
		Message message = mHandle.obtainMessage();
		message.arg1 = type;
		message.arg2 = position;
		mHandle.sendMessage(message);
	}
}
