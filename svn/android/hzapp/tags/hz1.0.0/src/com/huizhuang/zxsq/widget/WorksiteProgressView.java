package com.huizhuang.zxsq.widget;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.SupervisionNode;
import com.huizhuang.zxsq.utils.UiUtil;

/** 
* @ClassName: WorksiteProgressView 
* @Description: 工地节点进度显示组件
* @author th 
* @mail 342592622@qq.com   
* @date 2015-1-26 上午10:49:56 
*  
*/
public class WorksiteProgressView extends LinearLayout {

	private ImageView mIvState1, mIvState2, mIvState3, mIvState4, mIvState5;
	private View mTvState1, mTvState2, mTvState3, mTvState4;
	public WorksiteProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public WorksiteProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public WorksiteProgressView(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.component_worksite_progress, this);
		mIvState1 = (ImageView) view.findViewById(R.id.iv_p1);		
		mIvState2 = (ImageView) view.findViewById(R.id.iv_p2);		
		mIvState3 = (ImageView) view.findViewById(R.id.iv_p3);		
		mIvState4 = (ImageView) view.findViewById(R.id.iv_p4);		
		mIvState5 = (ImageView) view.findViewById(R.id.iv_p5);
		int mImgViewWidth = (UiUtil.getScreenWidth((Activity)context) - UiUtil.dp2px(context, 80)) / 5;
		mIvState1.setLayoutParams(new LinearLayout.LayoutParams(mImgViewWidth, mImgViewWidth));
		mIvState2.setLayoutParams(new LinearLayout.LayoutParams(mImgViewWidth, mImgViewWidth));
		mIvState3.setLayoutParams(new LinearLayout.LayoutParams(mImgViewWidth, mImgViewWidth));
		mIvState4.setLayoutParams(new LinearLayout.LayoutParams(mImgViewWidth, mImgViewWidth));
		mIvState5.setLayoutParams(new LinearLayout.LayoutParams(mImgViewWidth, mImgViewWidth));;
		mTvState1 = (View) findViewById(R.id.tv_p1_l);
		mTvState2 = (View) findViewById(R.id.tv_p2_l);
		mTvState3 = (View) findViewById(R.id.tv_p3_l);
		mTvState4 = (View) findViewById(R.id.tv_p4_l);
	}
	
	/**
	 * 改变节点状态
	 * 
	 * @param supervisionNodes
	 */
	public void changeNode(List<SupervisionNode> supervisionNodes) {
		for (SupervisionNode supervisionNode : supervisionNodes) {
			if (supervisionNode.getsId().equals("p1")) {
				changeNodeItemState(supervisionNode.getStatus(), mIvState1, mTvState1, R.drawable.account_wsm_normal1, R.drawable.account_wsm_selected1);
			} else if (supervisionNode.getsId().equals("p2")) {
				changeNodeItemState(supervisionNode.getStatus(), mIvState2, mTvState2, R.drawable.account_wsm_normal2, R.drawable.account_wsm_selected2);
			} else if (supervisionNode.getsId().equals("p3")) {
				changeNodeItemState(supervisionNode.getStatus(), mIvState3, mTvState3, R.drawable.account_wsm_normal3, R.drawable.account_wsm_selected3);
			} else if (supervisionNode.getsId().equals("p4")) {
				changeNodeItemState(supervisionNode.getStatus(), mIvState4, mTvState4, R.drawable.account_wsm_normal4, R.drawable.account_wsm_selected4);
			} else if (supervisionNode.getsId().equals("p5")) {
				changeNodeItemState(supervisionNode.getStatus(), mIvState5, null, R.drawable.account_wsm_normal5, R.drawable.account_wsm_selected5);
			}
		}
	}

	/**
	 * 改变单个Item状态
	 * 
	 * @param status
	 * @param iv
	 * @param tv
	 * @param ivResourceIdNor
	 * @param ivResourceIdSel
	 */
	private void changeNodeItemState(int status, ImageView iv, View tv, int ivResourceIdNor, int ivResourceIdSel) {
		if (status == 1) {
			// 未监理
			iv.setImageResource(ivResourceIdNor);
			if (tv != null) {
				tv.setBackgroundResource(R.color.gray_disabled);
			}
		} else {
			iv.setImageResource(ivResourceIdSel);
			if (status == 5 && tv != null) {
				tv.setBackgroundResource(R.color.red_selected);
			}
		}
	}

}
