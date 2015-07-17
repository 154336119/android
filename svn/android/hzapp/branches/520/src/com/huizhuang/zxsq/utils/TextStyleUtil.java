package com.huizhuang.zxsq.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;


/**
 * @ClassName: TextStyleUtil
 * @Description: 设置字体样式
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月9日  上午10:37:00
 */
public class TextStyleUtil {

	private SpannableString ss;

	public TextStyleUtil() {
	}
	
	public TextStyleUtil(String str) {
		ss = new SpannableString(str);
	}
	
	public void setString(String str) {
		ss = new SpannableString(str);
	}
	
	public SpannableString getSpannableString() {
		return ss;
	}

	public TextStyleUtil setAbsoluteSize(int size,int start,int end,boolean dp) {
		if (ss == null) {
			return this;
		}
		// 第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
		ss.setSpan(new AbsoluteSizeSpan(size, dp), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		return this;
	}

	public TextStyleUtil setRelativeSize(float size,int start,int end) {
		if (ss == null) {
			return this;
		}
		// 0.5f表示默认字体大小的一半
		ss.setSpan(new RelativeSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		return this;
	}
	
	public TextStyleUtil setForegroundColor(int color,int start,int end) {
		if (ss == null) {
			return this;
		}
		// 设置前景色为洋红色
		ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	
	public TextStyleUtil setBackgroundColor(int color,int start,int end) {
		if (ss == null) {
			return this;
		}
		// 设置前景色为洋红色
		ss.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	
	public static void setFakeBold(TextView textView,boolean isBold) {
		TextPaint tp = textView.getPaint();
		tp.setFakeBoldText(isBold);
	}
	
	/** 设置下划线*/
	public TextStyleUtil setUnderlineSpan(int start,int end) {
		if (ss == null) {
			return this;
		}
		// 设置前景色为洋红色
		ss.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		return this;
	}
	
	/** 设置删除线*/
	public TextStyleUtil setStrikethroughSpan(int start,int end) {
		if (ss == null) {
			return this;
		}
		// 设置前景色为洋红色
		ss.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}
	public TextStyleUtil setSubscriptSpan(int start,int end) {
		if (ss == null) {
			return this;
		}
		// 设置前景色为洋红色
		ss.setSpan(new SubscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		return this;
	}
	
	public TextStyleUtil setSuperscriptSpan(int start,int end) {
		if (ss == null) {
			return this;
		}
		// 设置前景色为洋红色
		ss.setSpan(new SuperscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		return this;
	}
	
}
