package com.huizhuang.zxsq.ui.adapter.account;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.account.CommentsWait;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;




/** 
* @ClassName: SupervisionOrderListAdapter 
* @Description:点评模块订单列表适配器
* @author th 
* @mail 342592622@qq.com   
* @date 2015-1-22 下午4:30:14 
*  
*/
public class SupervisionOrderListAdapter extends CommonBaseAdapter<CommentsWait> {

	private ViewHolder mHolder;
	private Handler mHandler;
	
	public SupervisionOrderListAdapter(Context context,Handler handler) {
		super(context);
		mHandler = handler;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);
		CommentsWait comments = getItem(position);
		if (null == convertView) {
			mHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_supervision_order_list, parent, false);
			mHolder.itemNodeName = (TextView) convertView.findViewById(R.id.tv_node);
			mHolder.itemTime = (TextView) convertView.findViewById(R.id.tv_time);
			mHolder.itemImg = (CircleImageView) convertView.findViewById(R.id.iv_img);
			mHolder.itemName = (TextView) convertView.findViewById(R.id.tv_name);
			mHolder.itemToScore = (Button) convertView.findViewById(R.id.btn_to_score);
			
			mHolder.itemToScore.setOnClickListener(mOnClickListener);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		mHolder.itemNodeName.setText(comments.getNode_name());
		mHolder.itemTime.setText(DateUtil.timestampToSdate(comments.getReport_time(), "yyyy年MM月dd日  HH:mm"));
		if(comments.getImage() != null){
			ImageLoader.getInstance().displayImage(comments.getImage().getImg_path(), mHolder.itemImg, ImageLoaderOptions.optionsDefaultEmptyPhoto);
		}
		mHolder.itemName.setText(comments.getName());
		mHolder.itemToScore.setTag(position);
		return convertView;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int what = 0;
			if (null != v.getTag()) {
				int position = (Integer) v.getTag();
				switch (v.getId()) {
				case R.id.btn_to_score:
					what = 0;
					break;
				default:
					break;
				}
				Message msg = mHandler.obtainMessage();
				msg.what = what;
				msg.arg1 = position;
				mHandler.sendMessage(msg);
			}
		}
	};

	
	static class ViewHolder {
		private TextView itemNodeName;
		private TextView itemTime;
		private CircleImageView itemImg;
		private TextView itemName;
		private Button itemToScore;	
	}

}
