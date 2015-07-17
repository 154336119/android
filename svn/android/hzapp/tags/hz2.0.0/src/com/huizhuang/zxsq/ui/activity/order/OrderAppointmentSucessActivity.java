package com.huizhuang.zxsq.ui.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/**
 * 预约签约成功
 * @author th
 *
 */
public class OrderAppointmentSucessActivity extends BaseActivity{

    private CommonActionBar mCommonActionBar;
    private CircleImageView mCivImg;
    private TextView mTvName;
    private TextView mTvCity;
    private TextView mTvOrderCount;
    private RatingBar mRbScore;    
    private TextView mTvScoreText;
    private ImageButton mIbendMessage;
    private ImageButton mIbToCall;
    private TextView mTvStatus;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_appointment_sucess);
        initActionBar();
        initViews();
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {

        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("预约成功");
    }

    private void initViews() {
        mCivImg = (CircleImageView)findViewById(R.id.iv_head);
        mTvName = (TextView)findViewById(R.id.tv_name);
        mTvCity = (TextView)findViewById(R.id.tv_city);
        mTvOrderCount = (TextView)findViewById(R.id.tv_order_count);
        mRbScore = (RatingBar)findViewById(R.id.rb_score);
        mTvScoreText = (TextView)findViewById(R.id.tv_score);
        mTvStatus = (TextView)findViewById(R.id.tv_status);
        mTvStatus.setVisibility(View.VISIBLE);
        mIbendMessage = (ImageButton)findViewById(R.id.ib_send_message);
        mIbToCall = (ImageButton)findViewById(R.id.ib_to_call);
    }
}
