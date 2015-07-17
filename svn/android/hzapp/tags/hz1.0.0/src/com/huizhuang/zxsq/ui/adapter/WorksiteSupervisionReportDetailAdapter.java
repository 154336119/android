package com.huizhuang.zxsq.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Problem;
import com.huizhuang.zxsq.module.ProblemPic;
import com.huizhuang.zxsq.module.ProblemSub;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;

/**
 * 质量报告详情页面Adapter
 * 
 * @ClassName: WorksiteSupervisionReportDetailAdapter.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-26 上午9:41:52
 */
public class WorksiteSupervisionReportDetailAdapter extends CommonBaseAdapter<Problem> {

	private ViewHolder mViewHolder;

	public WorksiteSupervisionReportDetailAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_worksite_supervision_report_detail, parent, false);
			mViewHolder.ivLaunch = (ImageView) convertView.findViewById(R.id.iv_launch);
			mViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mViewHolder.llContainer = (LinearLayout) convertView.findViewById(R.id.lin_item_container);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();

		}
		Problem problem = getList().get(position);
		ArrayList<ProblemSub> problemSubs = problem.getProblemSubs();
		initChildView(mViewHolder.llContainer, problemSubs);

		mViewHolder.tvTitle.setText(problem.getName());
		mViewHolder.ivLaunch.setTag(mViewHolder.llContainer);
		return convertView;
	}

	@SuppressLint("InflateParams")
	private void initChildView(LinearLayout lin, ArrayList<ProblemSub> problemSubs) {
		lin.removeAllViews();
		LogUtil.d("problemSubs = " + problemSubs);
		if (null != problemSubs) {
			LogUtil.d("problemSubs.size() = " + problemSubs.size());
		}
		for (ProblemSub problemSub : problemSubs) {
			LogUtil.d("problemSub = " + problemSub);
			View text = mLayoutInflater.inflate(R.layout.adapter_worksite_supervision_report_detail_text, null, false);
			TextView tv_sub_title = (TextView) text.findViewById(R.id.tv_sub_title);
			tv_sub_title.setText(problemSub.getName());
			ImageView iv_sub_ok = (ImageView) text.findViewById(R.id.iv_sub_ok);
			if (problemSub.getStatus() == 2) {// 合格
				iv_sub_ok.setImageResource(R.drawable.ic_order_ok);
			} else {
				iv_sub_ok.setImageResource(R.drawable.icon_error);
			}
			lin.addView(text);
			ArrayList<ProblemPic> leftPics = problemSub.getLeftPicList();
			ArrayList<ProblemPic> rightPics = problemSub.getRightPicList();
			if (leftPics != null) {
				for (int i = 0; i < leftPics.size(); i++) {
					ProblemPic problemPic_left = leftPics.get(i);
					View img = mLayoutInflater.inflate(R.layout.adapter_worksite_supervision_report_detail_img, null, false);
					img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtil.dp2px(mContext, 120)));
					RelativeLayout rl_no_ok = (RelativeLayout) img.findViewById(R.id.rl_no_ok);
					rl_no_ok.setVisibility(View.VISIBLE);
					ImageView iv_bg_img_left = (ImageView) img.findViewById(R.id.iv_bg_img_left);
					ImageUtil.displayImage(problemPic_left.getImgPath(), iv_bg_img_left, ImageLoaderOptions.getDefaultImageOption());
					TextView tv_img_nam_left = (TextView) img.findViewById(R.id.tv_img_nam_left);
					tv_img_nam_left.getBackground().setAlpha(80);
					tv_img_nam_left.setText(problemPic_left.getImgName());
					
					RelativeLayout rl_ok = (RelativeLayout) img.findViewById(R.id.rl_ok);
					if (i < rightPics.size()) {
						ProblemPic problemPic_right = rightPics.get(i);
						String imgName = problemPic_right.getImgName();
//						if (null != imgName && imgName.length() > 0) {
							rl_ok.setVisibility(View.VISIBLE);
							ImageView iv_bg_img_right = (ImageView) img.findViewById(R.id.iv_bg_img_right);
							ImageUtil.displayImage(problemPic_right.getImgPath(), iv_bg_img_right, ImageLoaderOptions.getDefaultImageOption());
							TextView tv_img_nam_right = (TextView) img.findViewById(R.id.tv_img_nam_right);
							tv_img_nam_right.getBackground().setAlpha(80);
							tv_img_nam_right.setText(problemPic_right.getImgName());
//						} else {
//							rl_ok.setVisibility(View.INVISIBLE);
//						}
					} else {
						rl_ok.setVisibility(View.INVISIBLE);
					}
					lin.addView(img);
				}
			}
			// else {
			// for (int i = 0; i < rightPics.size(); i++) {
			// ProblemPic problemPic_right = rightPics.get(i);
			// View img =
			// mLayoutInflater.inflate(R.layout.account_ws_report_detail_item_img,
			// null, false);
			// img.setLayoutParams(new
			// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
			// UiUtil.dp2px(mContext, 120)));
			// RelativeLayout rl_ok = (RelativeLayout)
			// img.findViewById(R.id.rl_ok);
			// String imgName = problemPic_right.getImgName();
			// if (null != imgName && imgName.length() > 0) {
			// rl_ok.setVisibility(View.VISIBLE);
			// ImageView iv_bg_img_right = (ImageView)
			// img.findViewById(R.id.iv_bg_img_right);
			// ImageUtil.displayImage(problemPic_right.getImgPath(),
			// iv_bg_img_right, ImageLoaderOptions.getDefaultImageOption());
			// TextView tv_img_nam_right = (TextView)
			// img.findViewById(R.id.tv_img_nam_right);
			// tv_img_nam_right.setText(problemPic_right.getImgName());
			// tv_img_nam_right.getBackground().setAlpha(80);
			// } else {
			// rl_ok.setVisibility(View.GONE);
			// }
			// RelativeLayout rl_no_ok = (RelativeLayout)
			// img.findViewById(R.id.rl_no_ok);
			// if (i < leftPics.size()) {
			// ProblemPic problemPic_left = leftPics.get(i);
			// rl_no_ok.setVisibility(View.VISIBLE);
			// ImageView iv_bg_img_left = (ImageView)
			// img.findViewById(R.id.iv_bg_img_left);
			// ImageUtil.displayImage(problemPic_left.getImgPath(),
			// iv_bg_img_left, ImageLoaderOptions.getDefaultImageOption());
			// TextView tv_img_nam_left = (TextView)
			// img.findViewById(R.id.tv_img_nam_left);
			// tv_img_nam_left.setText(problemPic_left.getImgName());
			// tv_img_nam_left.getBackground().setAlpha(80);
			// }
			// lin.addView(img);
			// }
			// }

		}
	}

	static class ViewHolder {
		private ImageView ivLaunch;
		private TextView tvTitle;
		private LinearLayout llContainer;
	}

}
