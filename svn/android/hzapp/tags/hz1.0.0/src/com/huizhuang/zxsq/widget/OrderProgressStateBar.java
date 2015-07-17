package com.huizhuang.zxsq.widget;

import com.huizhuang.hz.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 订单详情状态进度
 * 
 * @ClassName: OrderProgressStateBar.java
 * 
 * @author JEAN
 * 
 * @date 2014-12-12 上午10:21:48
 */
public class OrderProgressStateBar extends RelativeLayout {
	
    public final static int COMPLETE_ORDER = 1;
    public final static int MEASURE_HOUSE = 2;
    public final static int DESIGN_PRICE = 3;
    public final static int OBTAIN_SECURITY = 4;
    
    public enum ProgressSate {
        COMPLETE_ORDER, // 完成下单
        MEASURE_HOUSE, // 量房
        DESIGN_PRICE, // 设计报价
        OBTAIN_SECURITY // 获得保障
    }

    private ImageView mIvState1;
    private ImageView mIvState2;
    private ImageView mIvState3;
    private ImageView mIvState4;
    private ImageView mIvStateLine1;
    private ImageView mIvStateLine2;
    private ImageView mIvStateLine3;

    public OrderProgressStateBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public OrderProgressStateBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public OrderProgressStateBar(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        View rootLayout = LayoutInflater.from(context).inflate(R.layout.widget_order_progress_state_bar, this);
        mIvState1 = (ImageView) rootLayout.findViewById(R.id.iv_state_1);
        mIvState2 = (ImageView) rootLayout.findViewById(R.id.iv_state_2);
        mIvState3 = (ImageView) rootLayout.findViewById(R.id.iv_state_3);
        mIvState4 = (ImageView) rootLayout.findViewById(R.id.iv_state_4);
        mIvStateLine1 = (ImageView) rootLayout.findViewById(R.id.iv_state_line_1);
        mIvStateLine2 = (ImageView) rootLayout.findViewById(R.id.iv_state_line_2);
        mIvStateLine3 = (ImageView) rootLayout.findViewById(R.id.iv_state_line_3);
        mIvState1.setEnabled(false);
        mIvState2.setEnabled(false);
        mIvState3.setEnabled(false);
        mIvState4.setEnabled(false);
        mIvStateLine1.setEnabled(false);
        mIvStateLine2.setEnabled(false);
        mIvStateLine3.setEnabled(false);
    }

    /**
     * 设置当前状态
     * 
     * @param progressSate
     */
    private void setProgressState(ProgressSate progressSate) {
        switch (progressSate) {
        case COMPLETE_ORDER:
            mIvState1.setEnabled(true);
            mIvState2.setEnabled(false);
            mIvState3.setEnabled(false);
            mIvState4.setEnabled(false);
            break;
        case MEASURE_HOUSE:
            mIvState1.setEnabled(true);
            mIvState2.setEnabled(true);
            mIvState3.setEnabled(false);
            mIvState4.setEnabled(false);
            break;
        case DESIGN_PRICE:
            mIvState1.setEnabled(true);
            mIvState2.setEnabled(true);
            mIvState3.setEnabled(true);
            mIvState4.setEnabled(false);
            break;
        case OBTAIN_SECURITY:
            mIvState1.setEnabled(true);
            mIvState2.setEnabled(true);
            mIvState3.setEnabled(true);
            mIvState4.setEnabled(true); 
            break;
        default:
            break;
        }
    }

    /**
     * 设置当前的进度
     * 
     * @param status
     */
    public void setProgressState(int status) {
        ProgressSate progressSate = null;
        switch (status) {
        case OrderProgressStateBar.COMPLETE_ORDER:
            progressSate = ProgressSate.COMPLETE_ORDER;
            break;
        case OrderProgressStateBar.MEASURE_HOUSE:
            progressSate = ProgressSate.MEASURE_HOUSE;
            break;
        case OrderProgressStateBar.DESIGN_PRICE:
            progressSate = ProgressSate.DESIGN_PRICE;
            break;
        case OrderProgressStateBar.OBTAIN_SECURITY:
            progressSate = ProgressSate.OBTAIN_SECURITY;
            break;            
        default:
            progressSate = ProgressSate.COMPLETE_ORDER;
            break;
        }
        if (null != progressSate) {
            setProgressState(progressSate);
        }

    }

}
