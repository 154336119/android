package com.huizhuang.zxsq.ui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.BillDate;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.DiaryNode;
import com.huizhuang.zxsq.module.Visitor;
import com.huizhuang.zxsq.ui.activity.ImageSimpleBrowseActivity;
import com.huizhuang.zxsq.ui.activity.PersonalHomepageActivity;
import com.huizhuang.zxsq.ui.adapter.base.MyBaseAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.LocalRestrictClicksUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;

/**
 * 日记详情列表Adapter
 * 
 * @ClassName: DiaryDetailListViewAdapter.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-28 下午3:24:33
 */
public class DiaryDetailListViewAdapter extends MyBaseAdapter<Diary> {

	private Context mContext;

	private ViewHolder mViewHolder;
	private Handler mHandler;
	private String mTagId = null;// 日记点赞标记ID

	private ArrayList<DiaryNode> mNodes;

	public DiaryDetailListViewAdapter(Context context, Handler handler) {
		super(context);
		mContext = context;
		mHandler = handler;
		if (null != ZxsqApplication.getInstance().getUser()) {
			mTagId = ZxsqApplication.getInstance().getUser().getId() + "_diary";
		}
	}

	public ArrayList<DiaryNode> getNodes() {
		return mNodes;
	}

	public void setNodes(ArrayList<DiaryNode> nodes) {
		this.mNodes = nodes;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Diary diary = getItem(position);
		LogUtil.d("getView position = " + position + " Diary = " + diary);

		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = getLayoutInflater().inflate(R.layout.diary_detail_list_item, viewGroup, false);
			mViewHolder.tvNode = (TextView) convertView.findViewById(R.id.tv_node);
			mViewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			mViewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
			mViewHolder.tvWeather = (TextView) convertView.findViewById(R.id.tv_weather);
			mViewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			mViewHolder.tvLikeNum = (TextView) convertView.findViewById(R.id.tv_like);
			mViewHolder.tvDiscussNum = (TextView) convertView.findViewById(R.id.tv_discuss);
			mViewHolder.tvShareNum = (TextView) convertView.findViewById(R.id.tv_share);
			mViewHolder.tvReadNum = (TextView) convertView.findViewById(R.id.tv_read_num);
			mViewHolder.tvNoneReader = (TextView) convertView.findViewById(R.id.tv_none_reader);
			mViewHolder.tvLikeNum.setOnClickListener(clickListener);
			mViewHolder.tvDiscussNum.setOnClickListener(clickListener);
			mViewHolder.tvShareNum.setOnClickListener(clickListener);
			mViewHolder.tvBill = (TextView) convertView.findViewById(R.id.tv_bill);
			mViewHolder.llBill = (LinearLayout) convertView.findViewById(R.id.ll_bill);
			mViewHolder.gvDiaryPhoto = (GridView) convertView.findViewById(R.id.gv_image);
			mViewHolder.gvHeader = (GridView) convertView.findViewById(R.id.my_gv);
			mViewHolder.gvHeader.setFocusable(false);
			mViewHolder.gvHeader.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					int groupIndex = (Integer) parent.getTag();
					Visitor visitor = getList().get(groupIndex).getVisitors().get(position);
					Bundle bd = new Bundle();
					bd.putSerializable(AppConstants.PARAM_VISOTOR, visitor);
					ActivityUtil.next((Activity) mContext, PersonalHomepageActivity.class, bd, -1);
				}
			});
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		mViewHolder.tvLikeNum.setTag(position);
		mViewHolder.tvDiscussNum.setTag(position);
		mViewHolder.tvShareNum.setTag(position);
		mViewHolder.gvHeader.setTag(position);
		mViewHolder.gvDiaryPhoto.setTag(position);

		if (position == 0) {
			for (int i = 0; i < mNodes.size(); i++) {
				if (mNodes.get(i).getId().equals(diary.getZxNode())) {
					mViewHolder.tvNode.setText(mNodes.get(i).getName() + "(" + mNodes.get(i).getDiaryCount() + "篇)");
					mViewHolder.tvNode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_date_axis_node, 0, 0, 0);
				}
			}
		} else {
			if (getItem(position - 1).getZxNode().equals(diary.getZxNode())) {
				mViewHolder.tvNode.setText("");
				mViewHolder.tvNode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_date_axis, 0, 0, 0);
			} else {
				for (int i = 0; i < mNodes.size(); i++) {
					if (mNodes.get(i).getId().equals(diary.getZxNode())) {
						mViewHolder.tvNode.setText(mNodes.get(i).getName() + "(" + mNodes.get(i).getDiaryCount() + ")");
						mViewHolder.tvNode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_date_axis_node, 0, 0, 0);
					}
				}
			}
		}
		mViewHolder.tvDate.setText(DateUtil.timestampToSdate(diary.getDatetime(), "yyyy-MM-dd"));

		// 设置城市
		String city = diary.getCity();
		if (null != city && city.length() > 0) {
			mViewHolder.tvAddress.setVisibility(View.VISIBLE);
			mViewHolder.tvAddress.setText(city);
		} else {
			mViewHolder.tvAddress.setVisibility(View.GONE);
		}

		// 设置天气
		String weather = diary.getWeather();
		if (null != weather && weather.length() > 0) {
			mViewHolder.tvWeather.setVisibility(View.VISIBLE);
			mViewHolder.tvWeather.setText(weather);
		} else {
			mViewHolder.tvWeather.setVisibility(View.GONE);
		}

		// 设置日志文本内容
		String content = diary.getContent();
		if (null != content && content.length() > 0) {
			mViewHolder.tvContent.setVisibility(View.VISIBLE);
			mViewHolder.tvContent.setText(content);
		} else {
			mViewHolder.tvContent.setVisibility(View.GONE);
		}

		// 最大显示为3张图片

		int length = diary.getAtlass().size();
		if (length > 0) {
			int column = 3;
			if (length == 1) {
				column = 1;
			}
			mViewHolder.gvDiaryPhoto.setVisibility(View.VISIBLE);
			mViewHolder.gvDiaryPhoto.setNumColumns(column);
			mViewHolder.gvDiaryPhoto.setAdapter(new DiaryImageGridViewAdapter(mContext, diary.getAtlass(), column));
			mViewHolder.gvDiaryPhoto.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					int itemIndex = (Integer) parent.getTag();
					ArrayList<Atlas> atlasList = getItem(itemIndex).getAtlass();
					ArrayList<String> imageUrlList = new ArrayList<String>();
					for (Atlas atlas : atlasList) {
						imageUrlList.add(atlas.getImage());
					}
					Bundle bundle = new Bundle();
					bundle.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS, imageUrlList);
					bundle.putInt(ImageSimpleBrowseActivity.EXTRA_POSITION, position);
					ActivityUtil.next((Activity) mContext, ImageSimpleBrowseActivity.class, bundle, -1);
				}
			});
		} else {
			mViewHolder.gvDiaryPhoto.setVisibility(View.GONE);
		}

		// 显示日记看过的人
		if (diary.getVisitors().size() > 0) {
			int readNumber = Integer.valueOf(diary.getReadNum());
			mViewHolder.tvNoneReader.setVisibility(View.GONE);
			mViewHolder.gvHeader.setVisibility(View.VISIBLE);
			mViewHolder.tvReadNum.setVisibility(View.VISIBLE);
			if (readNumber > 5) {
				mViewHolder.tvReadNum.setText(mContext.getString(R.string.txt_format_number_people_see, readNumber));
			} else {
				mViewHolder.tvReadNum.setText(mContext.getString(R.string.txt_format_number_people_see_less_than_5, readNumber));
			}
			mViewHolder.gvHeader.setAdapter(new VistiorHeadImgAdapter(mContext, diary.getVisitors()));
		} else {
			mViewHolder.gvHeader.setVisibility(View.GONE);
			mViewHolder.tvReadNum.setVisibility(View.GONE);
			mViewHolder.tvNoneReader.setVisibility(View.VISIBLE);
		}

		// 设置相关清单
		mViewHolder.llBill.removeAllViews();
		int billSize = diary.getBills().size();
		for (int i = 0; i < billSize; i++) {
			BillDate bill = diary.getBills().get(i);
			RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.diary_detail_bill_item, null);
			TextView tvName = (TextView) rl.findViewById(R.id.tv_name);
			TextView tvAmount = (TextView) rl.findViewById(R.id.tv_amount);
			tvName.setText(bill.getTtypeName() + "-" + bill.getCtypeName());
			tvAmount.setText(Util.formatMoney(bill.getAmount(), 2));
			mViewHolder.llBill.addView(rl);
		}

		if (billSize > 0) {
			mViewHolder.tvBill.setVisibility(View.VISIBLE);
			mViewHolder.llBill.setVisibility(View.VISIBLE);
		} else {
			mViewHolder.tvBill.setVisibility(View.GONE);
			mViewHolder.llBill.setVisibility(View.GONE);
		}

		// 设置底部赞、评论、分享数据
		mViewHolder.tvLikeNum.setText(diary.getLikeNum());
		if (!LocalRestrictClicksUtil.getInstance().isUserCanClick(mTagId, String.valueOf(diary.getId()))) {
			// 不能点击，按钮标记为蓝色
			mViewHolder.tvLikeNum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_list_like_checked, 0, 0, 0);
		} else {
			mViewHolder.tvLikeNum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_list_like, 0, 0, 0);
		}
		mViewHolder.tvDiscussNum.setText(diary.getDiscussNum());
		mViewHolder.tvShareNum.setText(diary.getShareNum());

		return convertView;
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int what = 0;
			int position = (Integer) v.getTag();
			switch (v.getId()) {
			case R.id.tv_like:
				AnalyticsUtil.onEvent(mContext, UmengEvent.ID_DIARY_LIKE);
				what = 0;
				break;
			case R.id.tv_discuss:
				AnalyticsUtil.onEvent(mContext, UmengEvent.ID_DIARY_DISCUSS);
				what = 1;
				break;
			case R.id.tv_share:
				AnalyticsUtil.onEvent(mContext, UmengEvent.ID_DIARY_SHARE);
				what = 2;
				break;

			default:
				break;
			}
			Message msg = mHandler.obtainMessage();
			msg.what = what;
			msg.arg1 = position;
			mHandler.sendMessage(msg);
		}
	};

	public void updateView(ListView listView, int position) {
		int visiblePosition = listView.getFirstVisiblePosition();
		int headerCount = listView.getHeaderViewsCount();
		View v = listView.getChildAt(position - visiblePosition + headerCount);
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		Diary diary = getList().get(position);
		if (viewHolder != null) {
			viewHolder.tvLikeNum.setText(diary.getLikeNum());
			// 不能点击，按钮标记为蓝色
			if (!LocalRestrictClicksUtil.getInstance().isUserCanClick(mTagId, String.valueOf(diary.getId()))) {
				viewHolder.tvLikeNum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_list_like_checked, 0, 0, 0);
			}
			viewHolder.tvShareNum.setText(diary.getShareNum());
		}
	}

	static class ViewHolder {
		private TextView tvNode;
		private TextView tvDate;
		private TextView tvAddress;
		private TextView tvWeather;
		private TextView tvContent;
		private TextView tvLikeNum;
		private TextView tvDiscussNum;
		private TextView tvShareNum;
		private TextView tvReadNum;
		private TextView tvNoneReader;
		private GridView gvHeader;
		private GridView gvDiaryPhoto;
		private TextView tvBill;
		private LinearLayout llBill;
	}

}
