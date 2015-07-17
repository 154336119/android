package com.huizhuang.zxsq.ui.adapter.account;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.account.SupervisionPatrol;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;


/** 
* @ClassName: WorksiteScheduleManagerAdapter 
* @Description: 监理师巡查adapter
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-10 下午2:45:39 
*  
*/
public class WorksiteScheduleManagerAdapter extends CommonBaseAdapter<SupervisionPatrol> {

	private ViewHolder mViewHolder;

	public WorksiteScheduleManagerAdapter(Context context) {
		super(context);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SupervisionPatrol supervisionPatrol = getItem(position);
		LogUtil.d("getView position = " + position + " supervisionPatrol = " + supervisionPatrol);

		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_worksite_schedule_manager_item, parent, false);
			mViewHolder.tvYearAndMonth = (TextView) convertView.findViewById(R.id.tv_ym);
			mViewHolder.tvDay = (TextView) convertView.findViewById(R.id.tv_day);
			mViewHolder.tvWeek = (TextView) convertView.findViewById(R.id.tv_week);
			mViewHolder.ivHeader = (CircleImageView) convertView.findViewById(R.id.civ_head);
			mViewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_jlsName);
			mViewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			mViewHolder.tvIsRead = (TextView) convertView.findViewById(R.id.tv_is_read);
			mViewHolder.tvNoRead = (TextView) convertView.findViewById(R.id.tv_no_read);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		Calendar cal = Calendar.getInstance();
		if (TextUtils.isEmpty(supervisionPatrol.getTime())) {
			cal.setTime(new Date());
		} else {
			cal.setTime(DateUtil.timestampToDate(supervisionPatrol.getTime()));
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int week = cal.get(Calendar.DAY_OF_WEEK);
		String wek = "";
		String mon = month < 10 ? "0" + month : month + "";
		String da = day < 10 ? "0" + day : day + "";
		if (1 == week) {
			wek = mContext.getResources().getString(R.string.txt_week_7);
		} else if (2 == week) {
			wek = mContext.getResources().getString(R.string.txt_week_1);
		} else if (3 == week) {
			wek = mContext.getResources().getString(R.string.txt_week_2);
		} else if (4 == week) {
			wek = mContext.getResources().getString(R.string.txt_week_3);
		} else if (5 == week) {
			wek = mContext.getResources().getString(R.string.txt_week_4);
		} else if (6 == week) {
			wek = mContext.getResources().getString(R.string.txt_week_5);
		} else if (7 == week) {
			wek = mContext.getResources().getString(R.string.txt_week_6);
		}
		if(supervisionPatrol.getHas_read() == 1){
			mViewHolder.tvIsRead.setVisibility(View.VISIBLE);
			mViewHolder.tvNoRead.setVisibility(View.GONE);
		}else{
			mViewHolder.tvIsRead.setVisibility(View.GONE);
			mViewHolder.tvNoRead.setVisibility(View.VISIBLE);
		}
		mViewHolder.tvYearAndMonth.setText(year + "-" + mon);
		mViewHolder.tvDay.setText(da);
		mViewHolder.tvWeek.setText(wek);
		String s = mContext.getResources().getString(R.string.txt_jls_name);
		mViewHolder.tvName.setText(String.format(s, supervisionPatrol.getName()));
		mViewHolder.tvContent.setText(supervisionPatrol.getRemark());
		if(!TextUtils.isEmpty(supervisionPatrol.getAvater())){
			ImageLoader.getInstance().displayImage(supervisionPatrol.getAvater(), mViewHolder.ivHeader, ImageLoaderOptions.getDefaultImageOption());
		}
		return convertView;
	}

	static class ViewHolder {
		private TextView tvYearAndMonth;
		private TextView tvDay;
		private TextView tvWeek;
		private CircleImageView ivHeader;
		private TextView tvName;
		private TextView tvContent;
		private TextView tvIsRead;
		private TextView tvNoRead;
	}

}
