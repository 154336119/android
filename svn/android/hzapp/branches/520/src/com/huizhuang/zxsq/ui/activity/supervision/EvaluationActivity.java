package com.huizhuang.zxsq.ui.activity.supervision;

import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.Share;
import com.huizhuang.zxsq.http.bean.order.OrderDetailChild;
import com.huizhuang.zxsq.http.bean.supervision.ScoreOption;
import com.huizhuang.zxsq.http.bean.supervision.ScoreOptionList;
import com.huizhuang.zxsq.http.bean.supervision.ScoreOptionListList;
import com.huizhuang.zxsq.http.task.order.GetOrderEvalutionTask;
import com.huizhuang.zxsq.http.task.supervision.EvalutionSubmitTask;
import com.huizhuang.zxsq.http.task.supervision.ScoreOptionsTask;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderShowPayActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 各阶段评价
 * 
 * @author th
 * 
 */
public class EvaluationActivity extends BaseActivity implements OnClickListener {

    private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;

    private CircleImageView mCivHead;
    // private ImageView mIvDes;
    // private TextView mTvDes;
    private TextView mTvName;
    private TextView mTvCity;
    private TextView mTvOrderCount;
    private RatingBar mRbScore;
    private TextView mTvScore;
    private TextView mTvStatus;
    private View mVLine;

    private RatingBar mRbGradeScore;
    private LinearLayout mLinContainerOption;
    private EditText mEtContent;

    private String mOdersId;
    private SparseBooleanArray mSelectedId;
    private String mNodeId;
    private List<OrderDetailChild> mOrderDetailChilds;
    private ScoreOptionListList mScoreOptionListList;
    private String mCommentKey;
    private int mIsCost;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        getIntentExtra();
        initActionBar();
        initViews();
        httpRequestQueryOrderEvalution();
        httpRequestQueryScoreOption();
    }

    private void getIntentExtra() {
        mOdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
        mNodeId = getIntent().getStringExtra(AppConstants.PARAM_NODE_ID);
        mIsCost = getIntent().getIntExtra(AppConstants.PARAM_IS_CODE, 0);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_load_layout);
        mDataLoadingLayout.setOnReloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequestQueryOrderEvalution();
            }
        });
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("给个好评吧");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCommonActionBar.setRightImgBtn(R.drawable.ic_actionbar_share, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
            	//根据不同的订单状态来确定分享的文案
                Util.setShareByNodeId(mNodeId);
                Share share = new Share();
				Util.showShare(false, EvaluationActivity.this,share);
            }
        });
    }

    private void initViews() {
        mCivHead = (CircleImageView) findViewById(R.id.iv_head);
        // mIvDes = (ImageView) findViewById(R.id.iv_img_text_bg);
        // mTvDes = (TextView) findViewById(R.id.tv_img_text);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvCity = (TextView) findViewById(R.id.tv_city);
        mTvOrderCount = (TextView) findViewById(R.id.tv_order_count);
        mRbScore = (RatingBar) findViewById(R.id.rb_score);
        mTvScore = (TextView) findViewById(R.id.tv_score);
        mTvStatus = (TextView) findViewById(R.id.tv_status);
        mVLine = (View) findViewById(R.id.v_line);

        mRbGradeScore = (RatingBar) findViewById(R.id.rb_grade_score);
        mLinContainerOption = (LinearLayout) findViewById(R.id.lin_option_container);

        mEtContent = (EditText) findViewById(R.id.et_evaluation);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        // 监听打分
        mRbGradeScore.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar v, float rating, boolean arg2) {
                if (rating < 1) {
                    v.setRating(1);
                }
                if(getScoreOptionsByScore(v.getRating()) != null && getScoreOptionsByScore(v.getRating()).size() > 0){
                    initCreateOptionView(getScoreOptionsByScore(v.getRating()));
                }
            }
        });
        getKey();
    }

    private void getKey() {
        switch (Integer.valueOf(mNodeId)) {
            case 1:
                mCommentKey = "app_comment_kaigong";
                break;
            case 2:
                mCommentKey = "app_comment_shuidian";
                break;
            case 3:
                mCommentKey = "app_comment_nimu";
                break;
            case 4:
                mCommentKey = "app_comment_youqi";
                break;
            case 5:
                mCommentKey = "app_comment_jugong";
                break;
            default:
                break;
        }
    }

    private List<ScoreOption> getScoreOptionsByScore(float scpre) {
        if (mScoreOptionListList.getScore_list() != null) {
            for (ScoreOptionList scoreOptionList : mScoreOptionListList.getScore_list()) {
                if (scpre == scoreOptionList.getScore()) {
                    return scoreOptionList.getList();
                }
            }
        }
        return null;
    }

    @SuppressLint("InflateParams")
    private void initCreateOptionView(List<ScoreOption> scoreOptions) {
        if (mSelectedId == null) {
            mSelectedId = new SparseBooleanArray();
        } else {
            mSelectedId.clear();
        }
        mLinContainerOption.removeAllViews();
        for (ScoreOption scoreOption : scoreOptions) {
            LinearLayout convertView =
                    (LinearLayout) LayoutInflater.from(this).inflate(
                            R.layout.activity_evaluation_option, null);
            mLinContainerOption.addView(convertView);

            TextView tvName = (TextView) convertView.findViewById(R.id.tv_option_name);
            tvName.setText(scoreOption.getName());
            CheckBox check = (CheckBox) convertView.findViewById(R.id.cb_option_check);
            check.setTag(scoreOption.getItem_id());
            check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                    int tag = Integer.valueOf(v.getTag().toString());
                    mSelectedId.put(tag, isChecked);
                }
            });
        }
    }

    /**
     * 获取量房评价
     * 
     * @param xListRefreshType
     */
    private void httpRequestQueryOrderEvalution() {
        GetOrderEvalutionTask task = new GetOrderEvalutionTask(EvaluationActivity.this, mOdersId);
        task.setCallBack(new AbstractHttpResponseHandler<List<OrderDetailChild>>() {

            @Override
            public void onSuccess(List<OrderDetailChild> result) {
                mOrderDetailChilds = result;
                mDataLoadingLayout.showDataLoadSuccess();

                if (null != result) {
                    if (result.size() == 0) {
                        mDataLoadingLayout.showDataEmptyView();
                    } else {
                        initData(result);
                    }
                }
            }

            @Override
            public void onFailure(int code, String error) {
                mDataLoadingLayout.showDataLoadFailed(error);
            }

            @Override
            public void onStart() {
                super.onStart();
                mDataLoadingLayout.showDataLoading();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });
        task.send();
    }

    /**
     * 获取评分选项
     */
    private void httpRequestQueryScoreOption() {
        ScoreOptionsTask task = new ScoreOptionsTask(EvaluationActivity.this, mCommentKey);
        task.setCallBack(new AbstractHttpResponseHandler<ScoreOptionListList>() {

            @Override
            public void onSuccess(ScoreOptionListList result) {
                mScoreOptionListList = result;
                mDataLoadingLayout.showDataLoadSuccess();

                if (null != result && result.getScore_list() != null) {
                    if (result.getScore_list().size() == 0) {
                        mDataLoadingLayout.showDataEmptyView();
                    } else {
                        if(getScoreOptionsByScore(mRbGradeScore.getRating()) != null && getScoreOptionsByScore(mRbGradeScore.getRating()).size() > 0){
                            initCreateOptionView(getScoreOptionsByScore(mRbGradeScore.getRating()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(int code, String error) {
                mDataLoadingLayout.showDataLoadFailed(error);
            }

            @Override
            public void onStart() {
                super.onStart();
                mDataLoadingLayout.showDataLoading();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });
        task.send();
    }

    @SuppressLint("InflateParams")
    private void initData(List<OrderDetailChild> result) {
        for (int i = 0; i < result.size(); i++) {
            final OrderDetailChild orderDetailChild = result.get(i);

            if (!TextUtils.isEmpty(orderDetailChild.getAvater())) {
                ImageLoader.getInstance().displayImage(orderDetailChild.getAvater(), mCivHead,
                        ImageLoaderOptions.optionsDefaultEmptyPhoto);
            }
            mTvName.setText(orderDetailChild.getName());
            Drawable drawable = getResources().getDrawable(R.drawable.icon_rz);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            if (orderDetailChild.getIs_auth() == 1) {
                mTvName.setCompoundDrawables(null, null, drawable, null);
            } else {
                mTvName.setCompoundDrawables(null, null, null, null);
            }
            mTvCity.setText(orderDetailChild.getCity());
            if (TextUtils.isEmpty(orderDetailChild.getCity())) {
                mVLine.setVisibility(View.GONE);
            } else {
                mVLine.setVisibility(View.VISIBLE);
            }
            mTvOrderCount.setText(orderDetailChild.getOrders() + "");
            float score = 0;
            if(TextUtils.isEmpty(orderDetailChild.getScore())){
                score = 5;
            }else{
                score = Float.valueOf(orderDetailChild.getScore());
            }
            mRbScore.setRating(score == 0 ? 5 : score);
            mTvScore.setText(score == 0 ? "5分" : score + "分");
            String nodeName = Util.getNodeNameById(mNodeId);
            if ("1".equals(mNodeId)) {
                nodeName = "开工";
            }
            mTvStatus.setText(nodeName + "验收");
        }
    }

    /**
     * 评价提交
     * 
     * @param orderDetailChild
     */
    private void submit() {
        String itemIds = "";
        if(mSelectedId != null){
            // 取值
            boolean isFirst = true;
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            for (int i = 0; i < mSelectedId.size(); i++) {
                int key = mSelectedId.keyAt(i);
                boolean isChecked = mSelectedId.get(key);
                if (isChecked) {
                    if (!isFirst) {
                        sb.append(",");
                    }
                    isFirst = false;
                    sb.append("" + key + "");
                }
            }
            sb.append("]");
            itemIds = sb.toString();
        }
        float score = mRbGradeScore.getRating();
        String content = mEtContent.getText().toString();
        String store_id = mOrderDetailChilds.get(0).getStore_id();
        httpRequestSubmit(mCommentKey, store_id, content, score, itemIds);
    }

    /**
     * 评价提交
     */
    private void httpRequestSubmit(String comment_key, String store_id, String content,
            Float score, String item_ids) {
        EvalutionSubmitTask task =
                new EvalutionSubmitTask(EvaluationActivity.this, comment_key, store_id,mOdersId, content,
                        score, item_ids);
        task.setCallBack(new AbstractHttpResponseHandler<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.d("onSuccess result = " + result);
                showToastMsg("评价成功");
                Bundle bd = new Bundle();
                bd.putString(AppConstants.PARAM_ORDER_ID, mOdersId);
                // 跳转到下一阶段
                if ("1".equals(mNodeId)) {
                    bd.putString(AppConstants.PARAM_NODE_ID, (Integer.valueOf(mNodeId) + 1) + "");
                    ActivityUtil.next(EvaluationActivity.this, WaitResponseActivity.class, bd, true);
                }else if("5".equals(mNodeId)){
                    if(mIsCost == 1){//有费用
                        // 支付页面
                        bd.putString(AppConstants.PARAM_NODE_ID, mNodeId);
                        ActivityUtil.next(EvaluationActivity.this, OrderShowPayActivity.class, bd, true);
                    }else{//无费用变更单，完工，闭环
                        ActivityUtil.nextActivityWithClearTop(EvaluationActivity.this, MyOrderActivity.class);
                    }
                } else {
                    // 支付页面
                    bd.putString(AppConstants.PARAM_NODE_ID, mNodeId);
                    ActivityUtil.next(EvaluationActivity.this, OrderShowPayActivity.class, bd, true);
                }
            }

            @Override
            public void onFailure(int code, String error) {
                showToastMsg(error);
            }

            @Override
            public void onStart() {
                super.onStart();
                LogUtil.d("onStart");
                showWaitDialog(getResources().getString(R.string.txt_on_waiting));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }

        });
        task.send();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                submit();
                break;

            default:
                break;
        }
    }

}
