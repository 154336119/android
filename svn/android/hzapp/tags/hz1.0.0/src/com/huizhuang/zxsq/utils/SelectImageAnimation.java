package com.huizhuang.zxsq.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * 选择图片效果
 * 
 * @ClassName: SelectMembersAnimation.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-13 下午12:36:22
 */
public class SelectImageAnimation {

	private Activity mContext;
	private ViewGroup mAnimMaskLayout;
	private View mEndView;
	private View mAniView;
	private View mStartView;

	/**
	 * constructor.
	 * 
	 * @param context
	 */
	public SelectImageAnimation(Activity context) {
		mContext = context;

	}

	/**
	 * 
	 * @param aniView
	 *            产生动画效果的view
	 */
	public void setAnimationView(View aniView) {
		mAniView = aniView;
	}

	/**
	 * 
	 * @param startView
	 *            动画开始的起点view
	 * @param endView
	 *            动画结束的终点view
	 */
	public void setAniLocation(View startView, View endView) {
		this.mStartView = startView;
		this.mEndView = endView;
	}

	/**
	 * Before start, you should set aniView, startView and endView or it will do
	 * nothing.
	 */
	public void startAnimation(OnSelectAnimation onSelectAnimation) {
		if (mAniView == null || mStartView == null || mEndView == null) {
			return;
		}
		mOnSelectAnimation = onSelectAnimation;
		// 用来存储startView在屏幕的X、Y坐标
		int[] startLocation = new int[2];
		// 获取startView在屏幕的X、Y坐标（动画开始的坐标）
		mStartView.getLocationInWindow(startLocation);
		setAnimation(mAniView, startLocation); // 开始执行动画
	}

	private void setAnimation(final View animView, final int[] startLocation) {
		mAnimMaskLayout = createAnimLayout();
		mAnimMaskLayout.removeAllViews();
		mAnimMaskLayout.addView(animView);

		final View targetView = addViewToAnimLayout(animView, startLocation);
		int[] endLocation = new int[2]; // 这是用来存储动画结束位置的X、Y坐标
		mEndView.getLocationInWindow(endLocation); // 获取坐标

		int endX = endLocation[0] - startLocation[0];
		int endY = endLocation[1] - startLocation[1];

		final TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX, 0, 0);
		final TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationY.setInterpolator(new AnticipateInterpolator(2.5f));
		final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
		// final RotateAnimation rotateAnimation = new RotateAnimation(0f,
		// 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		// 0.5f);
		AnimationSet animationSet = new AnimationSet(false);
		// animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(translateAnimationX);
		animationSet.addAnimation(translateAnimationY);
		animationSet.setFillAfter(false);
		animationSet.setDuration(500); // 动画的执行时间
		animationSet.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				animView.setVisibility(View.VISIBLE);
				mEndView.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				animView.setVisibility(View.GONE);
				mEndView.setVisibility(View.VISIBLE);
				if (null != mOnSelectAnimation) {
					mOnSelectAnimation.onAnimationFinish();
				}
			}
		});
		targetView.startAnimation(animationSet);
	}

	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) mContext.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(final View view, int[] startLocation) {

		int x = startLocation[0];
		int y = startLocation[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

	public interface OnSelectAnimation {
		void onAnimationFinish();
	}

	private OnSelectAnimation mOnSelectAnimation;

}
