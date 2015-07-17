package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewRotate extends TextView {
    public TextViewRotate(Context context) {  
        super(context);  
    }  
       
    public TextViewRotate(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
   
    @Override  
    protected void onDraw(Canvas canvas) {  
        //倾斜度45,上下左右居中  
        canvas.rotate(45, (float)(getMeasuredWidth()/1.2), (float)(getMeasuredHeight()/1.6));  
        super.onDraw(canvas);  
    }  
           
}
