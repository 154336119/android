package com.huizhuang.zxsq.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.module.Author;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.Visitor;
import com.huizhuang.zxsq.ui.activity.PersonalHomepageActivity;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DiaryListViewAdapter extends CommonBaseAdapter<Diary> {

	private ViewHolder mViewHolder;

	private List<KeyValue> mStyles;
	private int mGirdViewWidth;

	public DiaryListViewAdapter(Context context) {
		super(context);
		mGirdViewWidth = (UiUtil.getScreenWidth((Activity) mContext) - UiUtil.dp2px(context, 96));
		mStyles = ZxsqApplication.getInstance().getConstant().getRoomStyles();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Diary diary = getItem(position);
		LogUtil.d("getView position = " + position + " Diary = " + diary);

		if (null == convertView) {
			convertView = mLayoutInflater.inflate(R.layout.diary_list_item, viewGroup, false);
			mViewHolder = new ViewHolder();
			mViewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
			mViewHolder.ivHeadimg = (ImageView) convertView.findViewById(R.id.iv_headimg);
			mViewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			mViewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
			mViewHolder.tvWeather = (TextView) convertView.findViewById(R.id.tv_weather);
			mViewHolder.tvStyle = (TextView) convertView.findViewById(R.id.tv_style);
			mViewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			mViewHolder.tvLikeNum = (TextView) convertView.findViewById(R.id.tv_like_num);
			mViewHolder.tvDiscussNum = (TextView) convertView.findViewById(R.id.tv_discuss_num);
			mViewHolder.tvShareNum = (TextView) convertView.findViewById(R.id.tv_share_num);
			mViewHolder.tvReaderNum = (TextView) convertView.findViewById(R.id.tv_read_num);
			mViewHolder.tvNoneReader = (TextView) convertView.findViewById(R.id.tv_none_reader);
			mViewHolder.ivHeadimg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int itemIndex = (Integer) v.getTag();

					Author author = getItem(itemIndex).getAuthor();
					Visitor visitor = new Visitor();
					visitor.setId(author.getId());
					visitor.setName(author.getName());
					visitor.setAvatar(author.getAvatar());

					Bundle bundle = new Bundle();
					bundle.putSerializable(AppConstants.PARAM_VISOTOR, visitor);
					ActivityUtil.next((Activity) mContext, PersonalHomepageActivity.class, bundle, -1);
				}
			});

			mViewHolder.gridView = (GridView) convertView.findViewById(R.id.my_gv);
			mViewHolder.gridView.setFocusable(false);
			mViewHolder.gridView.setLayoutParams(new RelativeLayout.LayoutParams(mGirdViewWidth, RelativeLayout.LayoutParams.WRAP_CONTENT));
			mViewHolder.gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					int itemIndex = (Integer) parent.getTag();

					Visitor visitor = getItem(itemIndex).getVisitors().get(position);
					Bundle bundle = new Bundle();
					bundle.putSerializable(AppConstants.PARAM_VISOTOR, visitor);
					ActivityUtil.next((Activity) mContext, PersonalHomepageActivity.class, bundle, -1);
				}
			});
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		// 设置TAG记住当前的ITEM位置
		mViewHolder.ivHeadimg.setTag(position);
		mViewHolder.gridView.setTag(position);

		mViewHolder.tvDate.setText(DateUtil.timestampToSdate(diary.getDatetime(), "yyyy-MM-dd"));
		mViewHolder.tvAddress.setText(diary.getCity());
		if ("晴".equals(diary.getWeather())) {
			mViewHolder.tvWeather.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_weather_1, 0, 0, 0);
		} else if ("多云".equals(diary.getWeather())) {
			mViewHolder.tvWeather.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_weather_2, 0, 0, 0);
		} else if ("雪".equals(diary.getWeather())) {
			mViewHolder.tvWeather.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_weather_3, 0, 0, 0);
		} else if ("雷阵雨".equals(diary.getWeather())) {
			mViewHolder.tvWeather.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_weather_4, 0, 0, 0);
		} else if ("阴".equals(diary.getWeather())) {
			mViewHolder.tvWeather.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_weather_5, 0, 0, 0);
		} else if ("雨".equals(diary.getWeather())) {
			mViewHolder.tvWeather.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diary_weather_6, 0, 0, 0);
		}
		mViewHolder.tvWeather.setText(diary.getWeather());

		String style = getStyle(diary.getAuthor().getRoomStyle());
		if (TextUtils.isEmpty(style)) {
			mViewHolder.tvStyle.setText("");
			mViewHolder.tvStyle.setVisibility(View.GONE);
		} else {
			mViewHolder.tvStyle.setText(style + "风格");
			mViewHolder.tvStyle.setVisibility(View.VISIBLE);
		}
		mViewHolder.tvContent.setText(diary.getContent());
		mViewHolder.tvLikeNum.setText(diary.getLikeNum());
		mViewHolder.tvDiscussNum.setText(diary.getDiscussNum());
		mViewHolder.tvShareNum.setText(diary.getShareNum());

		if (diary.getVisitors().size() > 0) {
			mViewHolder.gridView.setVisibility(View.VISIBLE);
			mViewHolder.tvReaderNum.setVisibility(View.VISIBLE);
			mViewHolder.tvNoneReader.setVisibility(View.GONE);
			mViewHolder.gridView.setAdapter(new VistiorHeadImgAdapter(mContext, diary.getVisitors()));
			if (Integer.valueOf(diary.getReadNum()) > 5) {
				mViewHolder.tvReaderNum.setText("等" + diary.getReadNum() + "人看过");
			} else {
				mViewHolder.tvReaderNum.setText(diary.getReadNum() + "人看过");
			}
		} else {
			mViewHolder.gridView.setVisibility(View.GONE);
			mViewHolder.tvReaderNum.setVisibility(View.GONE);
			mViewHolder.tvNoneReader.setVisibility(View.VISIBLE);
		}

		ImageLoader.getInstance().displayImage(diary.getAuthor().getAvatar(), mViewHolder.ivHeadimg, ImageLoaderOptions.optionsUserHeader);
		if (diary.getAtlass() != null && diary.getAtlass().size() > 0) {
			ImageLoader.getInstance().displayImage(diary.getAtlass().get(0).getImage(), mViewHolder.ivPhoto, ImageLoaderOptions.getDefaultImageOption());
		}

		return convertView;
	}

	private String getStyle(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		int length = mStyles.size();
		String name = null;
		for (int i = 0; i < length; i++) {
			KeyValue keyValue = mStyles.get(i);
			if (keyValue.getId().equals(str)) {
				name = keyValue.getName();
				break;
			}
		}
		return name;
	}

	static class ViewHolder {
		private ImageView ivPhoto;
		private ImageView ivHeadimg;
		private TextView tvDate;
		private TextView tvAddress;
		private TextView tvWeather;
		private TextView tvStyle;
		private TextView tvContent;
		private TextView tvLikeNum;
		private TextView tvDiscussNum;
		private TextView tvShareNum;
		private TextView tvNoneReader;
		private TextView tvReaderNum;
		private GridView gridView;
	}

}
