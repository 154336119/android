package com.huizhuang.zxsq.ui.adapter;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.account.MyMessage;
import com.huizhuang.zxsq.http.bean.account.QuoteDetail;
import com.huizhuang.zxsq.ui.activity.account.QuoteDetailActivity;
import com.huizhuang.zxsq.ui.activity.foreman.ForemanConstructionSiteActivity;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 消息列表
 * 
 * @ClassName: MyMessagesListAdapter
 * 
 * @author jean
 * 
 * @date 2014-10-29 上午09:40:00
 */
public class MyMessagesListAdapter extends CommonBaseAdapter<MyMessage> {

	private final static int MESSAGE_WORD = 1;
	private final static int MESSAGE_IMAGE = 2;
	private final static int MESSAGE_CONSTRUCTION_SITE = 4;
	private final static int MESSAGE_QUOTE = 5;
	private Handler mHandle;
	private boolean mIsSystemMsg;
	public MyMessagesListAdapter(Context context,boolean isSystemMsg) {
		super(context);
		mIsSystemMsg = isSystemMsg;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);

		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adatper_message, parent, false);
			viewHolder.itemTime = (TextView) convertView.findViewById(R.id.tv_left_time);
			viewHolder.itemHeader = (ImageView) convertView.findViewById(R.id.iv_icon);
			viewHolder.itemContent = (TextView) convertView.findViewById(R.id.tv_left_content);
			viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.iv_left_image);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final MyMessage message = getItem(position);
		LogUtil.d("getView Message = " + message);
		if(mIsSystemMsg){
			viewHolder.itemHeader.setImageResource(R.drawable.ic_system_header);
		}else{
			if(!message.getSender().getImage().endsWith(AppConstants.DEFAULT_IMG)){
				ImageLoader.getInstance().displayImage(message.getSender().getImage(), viewHolder.itemHeader, ImageLoaderOptions.optionsMyMessageHeader);
			}
		}
		String date = DateUtil.timestampToSdate(message.getSend_time(), "yyyy/MM/dd HH:mm");
		if(message.getType()==MESSAGE_CONSTRUCTION_SITE){  //施工现场
			viewHolder.itemContent.setText("点击查看施工现场详情");
		}else if(message.getType()==MESSAGE_QUOTE){  //报价单详情
			viewHolder.itemContent.setText("点击查看报价单详情");
		}else if(message.getType()==MESSAGE_IMAGE){  //图片
			viewHolder.itemContent.setVisibility(View.GONE);
			viewHolder.itemImage.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(message.getImage().get(0).getPath(), viewHolder.itemImage, ImageLoaderOptions.optionsDefaultEmptyPhoto);
		}else{
			viewHolder.itemContent.setText(Html.fromHtml(message.getMessage()));			
		}
		viewHolder.itemTime.setText(date);
        viewHolder.itemContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				click(message.getType(),message.getMessage());
			}
		});		
		return convertView;
	}

	static class ViewHolder {
		private TextView itemTime;
		private ImageView itemHeader;
		private TextView itemContent;
		private ImageView itemImage;
	}
	private void click(int type,String id) {
		switch (type) {
		case MESSAGE_CONSTRUCTION_SITE:
			Intent intent = new Intent((Activity)mContext, ForemanConstructionSiteActivity.class);
			intent.putExtra("showcase_id", id);
			mContext.startActivity(intent);			
			break;
		case MESSAGE_QUOTE:
			Intent intent01 = new Intent((Activity)mContext, QuoteDetailActivity.class);
			intent01.putExtra(QuoteDetailActivity.EXTRA_QUOTE_ID, id);
			mContext.startActivity(intent01);						
			break;
		default:
			break;
		}
	}
}
