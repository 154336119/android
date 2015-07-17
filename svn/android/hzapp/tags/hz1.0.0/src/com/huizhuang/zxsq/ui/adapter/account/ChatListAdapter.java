package com.huizhuang.zxsq.ui.adapter.account;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.account.ForeManChat;
import com.huizhuang.zxsq.http.bean.account.SystemChat;
import com.huizhuang.zxsq.http.bean.foreman.Foreman;
import com.huizhuang.zxsq.module.MyMessage.Message;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 会话列表Adapter
 * 
 * @ClassName: ChatListAdapter
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-10-29 上午09:40:00
 */
public class ChatListAdapter extends CommonBaseAdapter<ForeManChat> {
	private boolean mHasNewSystemMsg = false;
	private final int TYPE_SYSTEM=1,TYPE_FOREMAN=2;
	public ChatListAdapter(Context context,boolean hasNewSystemMsg) {
		super(context);
		mHasNewSystemMsg = hasNewSystemMsg;
	}

	public boolean ismHasNewSystemMsg() {
		return mHasNewSystemMsg;
	}

	public void setmHasNewSystemMsg(boolean mHasNewSystemMsg) {
		this.mHasNewSystemMsg = mHasNewSystemMsg;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);
		ViewHolderSytstem viewHolderSytstem= null;
		ViewHolderForeManChat viewHolderForeManChat = null;
		int type = getItemViewType(position);
		if (null == convertView) {
			switch (type) {
			case TYPE_SYSTEM:
				viewHolderSytstem = new ViewHolderSytstem();
				convertView = mLayoutInflater.inflate(R.layout.adapter_system_chat_list, parent, false);
				viewHolderSytstem.tv_new_msg = (TextView) convertView.findViewById(R.id.tv_new_msg);
				convertView.setTag(viewHolderSytstem);
				break;
			default:
					viewHolderForeManChat = new ViewHolderForeManChat();
					convertView = mLayoutInflater.inflate(R.layout.adapter_my_chat_list, parent, false);
					viewHolderForeManChat.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
					viewHolderForeManChat.iv_header = (ImageView) convertView.findViewById(R.id.iv_header);
					viewHolderForeManChat.tv_add_time = (TextView) convertView.findViewById(R.id.tv_add_time);
					viewHolderForeManChat.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
					convertView.setTag(viewHolderForeManChat);
				break;
			}
			
			
		} else {
			 switch (type) {
		      case TYPE_SYSTEM:
		    	  viewHolderSytstem=(ViewHolderSytstem) convertView.getTag();
		        break;
			 default:
		    	  viewHolderForeManChat=(ViewHolderForeManChat) convertView.getTag();
		        break;   
		      }
		}

			 switch (type) {
		      case TYPE_SYSTEM:
		    	  if(mHasNewSystemMsg)
		    		  viewHolderSytstem.tv_new_msg.setVisibility(View.VISIBLE);
		    		  else
		    			  viewHolderSytstem.tv_new_msg.setVisibility(View.INVISIBLE); 
		        break;
		      default:
		    	  ForeManChat foreManChat = getItem(position);
		  		LogUtil.d("getView Message = " + foreManChat);
				if(!foreManChat.getAvater().endsWith(AppConstants.DEFAULT_IMG)){
					ImageLoader.getInstance().displayImage(foreManChat.getAvater(), viewHolderForeManChat.iv_header, ImageLoaderOptions.optionsMyMessageHeader);
				}
				String date = DateUtil.timestampToSdate(foreManChat.getLast_time(), "yyyy-MM-dd HH:mm:ss");		  		
				viewHolderForeManChat.tv_add_time.setText(DateUtil.friendlyTime(date));
		  		viewHolderForeManChat.tv_user_name.setText(foreManChat.getUsername());
		  		viewHolderForeManChat.tv_content.setText(foreManChat.getLast_msg());
		        break;		        
			 }
		        return convertView;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0)
		      return TYPE_SYSTEM;
		    else
		      return TYPE_FOREMAN;
		  }
	static class ViewHolderForeManChat {
		private TextView tv_user_name;
		private ImageView iv_header;
		private TextView tv_add_time;
		private TextView tv_content;
	}
	
	static class ViewHolderSytstem {
		private TextView tv_new_msg;
	}
	
}
