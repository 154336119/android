package com.huizhuang.zxsq.ui.adapter.account;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.LogUtil;
import com.umeng.message.proguard.T;





/** 
* @ClassName: ComplaintQuestionListAdapter 
* @Description: 投诉问题adapter 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-1-23 上午11:45:06 
*  
*/
public class ComplaintQuestionListAdapter extends CommonBaseAdapter<T> {

	private ViewHolder mHolder;
	
	public ComplaintQuestionListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);

		if (null == convertView) {
			mHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_complaint_question_list, parent, false);
			mHolder.itemName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		
//		Message message = getItem(position);
//		LogUtil.d("getView Message = " + message);
//
//		ImageLoader.getInstance().displayImage(message.getUserThumb(), mHolder.itemHeader, ImageLoaderOptions.optionsMyMessageHeader);
//		String date = DateUtil.dateToStrLong(new Date(message.getAddTime()));
//		mHolder.itemTime.setText(date);
//		mHolder.itemContent.setText(Html.fromHtml(message.getContent()));
		return convertView;
	}


	
	static class ViewHolder {
		private TextView itemName;	
	}

}
