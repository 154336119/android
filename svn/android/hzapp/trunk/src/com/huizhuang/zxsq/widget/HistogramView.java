package com.huizhuang.zxsq.widget;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.Set;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.http.bean.Constant;
import com.huizhuang.zxsq.http.bean.Constant.JourneyType;
import com.huizhuang.zxsq.utils.LogUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class HistogramView extends View {

	private Context context;
	Map<Integer, Double> mDataMap;
	private int mMaxInt;
	private List<JourneyType> typeList;
	private String mMin;
	private String mMax;
	private int[] colors = { 0xcc85d3de,
			0xccdeaf85, 0xcca7e39b,
			0xccd6ade7, 0xcceabbbb,
			0xccdadada };

	public HistogramView(Context context) {
		super(context);
		this.context = context;
	}

	public HistogramView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void setDataMap(Map<Integer, Double> dataMap,String min,String max) {
		mMin=min;
		mMax=max;
		mDataMap = dataMap;
		if(mDataMap==null) mDataMap=new HashMap<Integer, Double>();
		// 获取最大Y值
		Set<Entry<Integer, Double>> set = mDataMap.entrySet();
		Iterator<Entry<Integer, Double>> iterator = set.iterator();
		double mMaxValue = 0;
		while (iterator.hasNext()) {
			Entry<Integer, Double> entry = iterator.next();
			double value = entry.getValue();
			if (mMaxValue < value) {
				mMaxValue = value;
			}
		}
		//if(mMaxValue==0) return;
		BigDecimal bigD=new BigDecimal(mMaxValue);
		//获取最大Y坐标
		String strValue =bigD.toPlainString();;
		if(strValue!=null&&strValue.contains(".")){
			strValue = strValue.substring(0, strValue.indexOf("."));
		}
		String strFirst = strValue.substring(0, 1);
		mMaxInt = Integer.parseInt(strFirst)+1 ;
		int index = strValue.length();
		for (int i = 0; i < index - 1; i++) {
			mMaxInt *= 10;
		}
		LogUtil.i("  strValue:"+strValue+"  mMaxInt:"+mMaxInt);
		Constant constant = ZxsqApplication.getInstance().getConstant();
		typeList = constant.getJourneyTypeList();

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		LogUtil.i("jiengyh onMeasure widthMeasureSpec:"+widthMeasureSpec+"   heightMeasureSpec:"+heightMeasureSpec);
	}
	
	private int marginLeft=130;
	private int marginButtom=65;
	private int marginTop=35;
	private int marginRight=30;
	private int mWidth;
	private int mHeight;
	
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mWidth = getWidth();
		mHeight = getHeight();
		marginLeft=mWidth/6;
		marginButtom=mHeight/6;
		marginTop=mHeight/13;
		marginRight=mWidth/21;
		
		
		
		int xPoint=marginLeft;//原点X坐标
		int yPoint=mHeight-marginButtom;//原点Y坐标
		
		int lengthX=mWidth-marginLeft-marginRight;//X轴长度
		int lengthY=mHeight-marginButtom-marginTop;
		
		int xResolation=lengthX/7;//x轴方向的坐标分度值
		
		int yResolation=lengthY/6;//y轴方向的坐标分度值（一个刻度代表多少坐标）
		
		
		
		
		Paint paint=new Paint();
		
		
		
		//画坐标轴
		canvas.drawLine(xPoint,yPoint, xPoint, marginTop, paint);//y轴
		canvas.drawLine(xPoint,yPoint,  mWidth-marginRight,yPoint, paint);
		drawXText(canvas, xPoint, yPoint, xResolation);
		
		int maxY=drawYResolation(canvas, xPoint, yPoint, yResolation);
		
		double yNumResolation=maxY/(float)mMaxInt;//坐标轴内  每一个像素代表多少数字
		LogUtil.i("jiengyh onDraw width:"+mWidth+"   height:"+mHeight+"  mDataMap:"+mDataMap+"  yNumResolation:"+yNumResolation);
		
		Iterator<Entry<Integer, Double>> iterator =mDataMap.entrySet().iterator();
		int index=0;
		int numResolation=mMaxInt/5;
		LogUtil.i("numResolation:"+numResolation+"  mMaxInt:"+mMaxInt);
		while(iterator.hasNext()){
			Entry<Integer, Double>  entry=iterator.next();
			double value=entry.getValue();
			//保证最小值看起来不小于最小分度值的一半
			if(value<numResolation/2&&value!=0){
				value=numResolation/2;
			}
			
			int xTextCorrect=mWidth/35;//x轴上文字的矫正值 使文字放在中间  数字体现在X轴上
			float yValue=(float) (yNumResolation*value);
			//LogUtil.i("jiengyh onDraw ")''
			Paint paintRect=new Paint();
			if(index<colors.length){
				paintRect.setColor(colors[index]);
			}
			index++;
			canvas.drawRect(xPoint+xResolation*index-xTextCorrect, yPoint-yValue, xPoint+xResolation*index-xTextCorrect+30, yPoint,paintRect );
			
			
		}
	}

	private int drawYResolation(Canvas canvas, int xPoint, int yPoint,
			int yResolation) {
		Paint paint=new Paint();
		paint.setTextSize(26);
		paint.setColor(0xff808080);
		paint.setTextAlign(Align.RIGHT);
		float yTextMargin=mHeight/20f ;
		int yTextCorrect=6;//修正值 使数字的中间与横线对其
		float numResolation=mMaxInt/5;
		int maxY=0;
		Paint linePaint =new Paint();
		linePaint.setColor(0x44808080);
		for(int i=1;i<6;i++){
			float yNum=numResolation*i;
			DecimalFormat df = new DecimalFormat("0.00");
			String yValue = df.format(yNum);
			LogUtil.i("yValue:"+yValue+" yNum:"+yNum);
			if(yNum>10000){
				//大于10000  写成万做单位
				if(yNum<1000000){
					yValue=df.format((yNum/10000F))+"万";
				}else{
					yValue=(int) ((numResolation*i)/10000)+"万";
				}
			}else{
				int yNumMin=(int) (numResolation*i);
				yValue=yNumMin+"";
			}
			canvas.drawLine(xPoint, yPoint-yResolation*i, mWidth-marginRight, yPoint-yResolation*i, linePaint);
			canvas.drawText(yValue+"", xPoint-yTextMargin, yPoint-yResolation*i+yTextCorrect, paint);
			maxY=yResolation*i+yTextCorrect;
		}
		return maxY;
		
	}

	private void drawXText(Canvas canvas, int xPoint, int yPoint,
			int xResolation) {
		int xTextMargin=mHeight/15;//X轴上的文字距离X轴的距离，数字体现在Y轴上
		int xTextCorrect=mWidth/25;//x轴上文字的矫正值 使文字放在中间  数字体现在X轴上
		Paint paintText=new Paint();
		paintText.setTextSize(26);
		paintText.setColor(0xff808080);
		canvas.drawText("设计",xPoint+xResolation-xTextCorrect , yPoint+xTextMargin, paintText);
		canvas.drawText("主材",xPoint+xResolation*2-xTextCorrect , yPoint+xTextMargin, paintText);
		canvas.drawText("装修",xPoint+xResolation*3-xTextCorrect , yPoint+xTextMargin, paintText);
		canvas.drawText("软装",xPoint+xResolation*4-xTextCorrect , yPoint+xTextMargin, paintText);
		canvas.drawText("家电",xPoint+xResolation*5-xTextCorrect , yPoint+xTextMargin, paintText);
		canvas.drawText("其他",xPoint+xResolation*6-xTextCorrect , yPoint+xTextMargin, paintText);
		if(mMin==null||mMax==null) {
			canvas.drawText(mMin+"-"+mMax+"消费支出情况", 30, 25, paintText);
		}else{
			canvas.drawText("消费支出情况", 30, 25, paintText);
		}
		
	}

}