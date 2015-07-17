package com.huizhuang.zxsq.ui.activity.supervision;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/**
 * 投诉成功页面
 * 
 * @author th
 * 
 */
public class ComplaintsSucessActivity extends BaseActivity {
    
    private TextView mTvComplaintsType;
    private TextView mTvComplaintsQuestion;
    
    private String mComplaintsType;
    private String mComplaintsQuestion;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_sucess);
        getIntentExtra();
        initActionBar();
        initViews();
    }

    private void getIntentExtra() {
        mComplaintsType = getIntent().getStringExtra(AppConstants.PARAM_COMPLAINTS_TYPE);
        mComplaintsQuestion = getIntent().getStringExtra(AppConstants.PARAM_COMPLAINTS_QUESTION);
    }

    private void initActionBar() {
        CommonActionBar mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle(R.string.txt_check_complaints_requestion);
        mCommonActionBar.setLeftImgBtn(R.drawable.back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bd = new Bundle();
                bd.putBoolean(AppConstants.PARAM_IS_COMPLAINTS_SUCESS, true);
                ActivityUtil.nextActivityWithClearTop(ComplaintsSucessActivity.this, ComplaintsListActivity.class, bd);
            }
        });
    }

    private void initViews() {
        mTvComplaintsType = (TextView)findViewById(R.id.tv_type);
        mTvComplaintsQuestion = (TextView)findViewById(R.id.tv_question);
        mTvComplaintsType.setText(String.format(getResources().getString(R.string.txt_checked_complaints_type), mComplaintsType));
        mTvComplaintsQuestion.setText(String.format(getResources().getString(R.string.txt_checked_complaints_question), mComplaintsQuestion));
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Bundle bd = new Bundle();
            bd.putBoolean(AppConstants.PARAM_IS_COMPLAINTS_SUCESS, true);
            ActivityUtil.nextActivityWithClearTop(ComplaintsSucessActivity.this, ComplaintsListActivity.class, bd);
        }
        return super.onKeyDown(keyCode, event);
    }
}
