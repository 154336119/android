package com.huizhuang.zxsq.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class AssortView extends Button {
	private Context context;
	public interface OnTouchAssortListener{
		public void onTouchAssortListener(String s);
		public void onTouchAssortUp();
	}
	public AssortView(Context context) {
		super(context);
		this.context = context;
	}

	public AssortView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public AssortView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	//分类
	public String[] assort = { "?", "#", "A", "B", "C", "D", "E", "F", "G",  
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",  
            "U", "V", "W", "X", "Y", "Z"};
	private Paint paint = new Paint();
	//选择的索引
	private int selectIndex = -1;
	//字母监听器
	private OnTouchAssortListener ontouch;
	
	//设置监听器
	public void setOnTouchAssortListener(OnTouchAssortListener ontouch){
		this.ontouch = ontouch;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		canvas.drawColor(0xFFF5F5F5);
		int height = getHeight();
		int width = getWidth();
		int interval = height/assort.length;
		for(int i=0;i<assort.length;i++){
			//抗锯齿
			paint.setAntiAlias(true);
			//默认粗体
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			//黑色
			paint.setColor(Color.BLACK);
			paint.setTextSize(25);
			if(i == selectIndex){
				//被选择的字母改变颜色和粗体
				paint.setFakeBoldText(true);
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setTextSize(30);
			}
			//计算字母的x坐标
			float xPos = width/2-paint.measureText(assort[i])/2;
			//计算字母的Y坐标
			float yPos = interval*i+interval;
			canvas.drawText(assort[i], xPos, yPos, paint);
			paint.reset();
		}
		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		float y = event.getY();
		int index = (int)y*assort.length/getHeight();
		if(index>=0 && index < assort.length){
			switch(event.getAction()){
			case MotionEvent.ACTION_MOVE:
				//如果滑动改变
				if(selectIndex != index){
					selectIndex = index;
					if(ontouch!=null){
						ontouch.onTouchAssortListener(assort[index]);
					}
				}
				break;
			case MotionEvent.ACTION_DOWN:
				selectIndex = index;
				if(ontouch!=null){
					ontouch.onTouchAssortListener(assort[index]);
				}
				break;
			case MotionEvent.ACTION_UP:
				selectIndex = -1;
				if(ontouch!=null){
					ontouch.onTouchAssortUp();
				}
				break;
			}
		}else {
			selectIndex = -1;
			if(ontouch!=null){
				ontouch.onTouchAssortUp();
			}
		}
		invalidate();
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	
}
