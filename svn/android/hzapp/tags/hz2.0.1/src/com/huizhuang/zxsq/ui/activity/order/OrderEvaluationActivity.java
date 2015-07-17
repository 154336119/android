package com.huizhuang.zxsq.ui.activity.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.config.PreferenceConfig;
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
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.supervision.WaitResponseActivity;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 跪求好评
 * 
 * @author th
 * 
 */
public class OrderEvaluationActivity extends BaseActivity {

    public static final String NODE_KEY = "app_comment_yanfang";
    private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;

    private LinearLayout mLinContainer;
    private String mOdersId;
    private ScoreOptionListList mScoreOptionListList;
    private Map<String, SparseBooleanArray> mAllSelected;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluation);
        getIntentExtra();
        initActionBar();
        initViews();
        httpRequestQueryScoreOption();
    }

    private void getIntentExtra() {
        mOdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
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
        mCommonActionBar.setActionBarTitle("跪求好评");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCommonActionBar.setRightImgBtn(R.drawable.ico_diary_share, new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
            	//量房评价
                PreferenceConfig.setShareByOrderState(AppConstants.SHARE_BY_ORDER_COMMON);
                Share share = new Share();
				Util.showShare(false, OrderEvaluationActivity.this,share);
            }
        });
    }

    private void initViews() {
        mLinContainer = (LinearLayout) findViewById(R.id.lin_container);
    }

    /**
     * 获取量房评价
     * 
     * @param xListRefreshType
     */
    private void httpRequestQueryOrderEvalution() {
        GetOrderEvalutionTask task =
                new GetOrderEvalutionTask(OrderEvaluationActivity.this, mOdersId);
        task.setCallBack(new AbstractHttpResponseHandler<List<OrderDetailChild>>() {

            @Override
            public void onSuccess(List<OrderDetailChild> result) {
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

    @SuppressLint("InflateParams")
    private void initData(List<OrderDetailChild> result) {
        mLinContainer.removeAllViews();
        for (int i = 0; i < result.size(); i++) {
            OrderDetailChild orderDetailChild = result.get(i);
            LinearLayout convertView =
                    (LinearLayout) LayoutInflater.from(this).inflate(
                            R.layout.activity_order_evaluation_child, null);
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 35, 0, 0);
            convertView.setLayoutParams(lp);
            mLinContainer.addView(convertView);
            CircleImageView itemImg = (CircleImageView) convertView.findViewById(R.id.iv_head);
            ImageView itemImgTextBg = (ImageView) convertView.findViewById(R.id.iv_img_text_bg);
            TextView itenImgText = (TextView) convertView.findViewById(R.id.tv_img_text);
            TextView itemName = (TextView) convertView.findViewById(R.id.tv_name);
            TextView itemCity = (TextView) convertView.findViewById(R.id.tv_city);
            TextView itemOrderCount = (TextView) convertView.findViewById(R.id.tv_order_count);
            RatingBar itemScore = (RatingBar) convertView.findViewById(R.id.rb_score);
            TextView itemScoreText = (TextView) convertView.findViewById(R.id.tv_score);
            TextView itemStatus = (TextView) convertView.findViewById(R.id.tv_status);
            View itemLine = (View) convertView.findViewById(R.id.v_line);
            LinearLayout itemContentContainer =
                    (LinearLayout) convertView.findViewById(R.id.lin_content_container);
            RatingBar itemGradeScore = (RatingBar) convertView.findViewById(R.id.rb_grade_score);

            Button itemSubmit = (Button) convertView.findViewById(R.id.btn_submit);
            itemSubmit.setTag(orderDetailChild.getStore_id());

            if (!TextUtils.isEmpty(orderDetailChild.getAvater())) {
                ImageLoader.getInstance().displayImage(orderDetailChild.getAvater(), itemImg,
                        ImageLoaderOptions.optionsDefaultEmptyPhoto);
            }
            itemName.setText(orderDetailChild.getName());
            Drawable drawable = getResources().getDrawable(R.drawable.icon_rz);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            if (orderDetailChild.getIs_auth() == 1) {
                itemName.setCompoundDrawables(null, null, drawable, null);
            } else {
                itemName.setCompoundDrawables(null, null, null, null);
            }
            itemCity.setText(orderDetailChild.getCity());
            if(TextUtils.isEmpty(orderDetailChild.getCity())){
                itemLine.setVisibility(View.GONE);
            }else{
                itemLine.setVisibility(View.VISIBLE);
            }
            itemOrderCount.setText(orderDetailChild.getOrders() + "");
            float score = 0;
            if(TextUtils.isEmpty(orderDetailChild.getScore())){
                score = 5;
            }else{
                score = Float.valueOf(orderDetailChild.getScore());
            }
            itemScore.setRating(score == 0 ? 5 : score);
            itemScoreText.setText(score == 0 ? "5分" : score + "分");
            LinearLayout linScoreOptionContainer = (LinearLayout) convertView.findViewById(R.id.lin_option_container);
            linScoreOptionContainer.setTag(orderDetailChild.getStore_id());
            //初始化分数项
            initCreateOptionView(getScoreOptionsByScore(itemGradeScore.getRating()), linScoreOptionContainer);
            
            if (orderDetailChild.getStatu() >= 3) {// 已量房
                itemImgTextBg.setVisibility(View.VISIBLE);
                itenImgText.setVisibility(View.VISIBLE);
                itemStatus.setVisibility(View.VISIBLE);
                itemStatus.setText("已量房");
                if (orderDetailChild.getIs_pj() == 0) {// 无评价
                    itemContentContainer.setVisibility(View.VISIBLE);
                    itenImgText.setText("跪求好评");
                } else {
                    itemContentContainer.setVisibility(View.GONE);
                    itenImgText.setText("已评价");
                }
            } else {
                itemStatus.setVisibility(View.GONE);
                itemContentContainer.setVisibility(View.GONE);
                itenImgText.setVisibility(View.GONE);
                itemImgTextBg.setVisibility(View.GONE);
            }
            
            // 监听打分
            itemGradeScore.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(RatingBar v, float rating, boolean arg2) {
                    View view = (View) v.getParent().getParent();
                    LinearLayout itemOptionContainer =
                            (LinearLayout) view.findViewById(R.id.lin_option_container);
                    if (rating < 1) {
                        v.setRating(1);
                    }
                    initCreateOptionView(getScoreOptionsByScore(v.getRating()), itemOptionContainer);
                }
            });
            
            // 打分提交
            itemSubmit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                   LinearLayout contentContainer = (LinearLayout)v.getParent();
                   EditText etContent = (EditText)contentContainer.findViewById(R.id.et_evaluation);
                   RatingBar gradeScore = (RatingBar)contentContainer.findViewById(R.id.rb_grade_score);
                   String content = etContent.getText().toString();
                   String store_id = v.getTag().toString();  
                   float score = gradeScore.getRating();
                   SparseBooleanArray sparseBooleanArray = mAllSelected.get(store_id);
                   String itemIds = "";
                   if(sparseBooleanArray != null){
                       // 取值
                       boolean isFirst = true;
                       StringBuffer sb = new StringBuffer();
                       sb.append("[");
                       for (int i = 0; i < sparseBooleanArray.size(); i++) {
                           int key = sparseBooleanArray.keyAt(i);
                           boolean isChecked = sparseBooleanArray.get(key);
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
                   httpRequestSubmit(NODE_KEY, store_id, content, score, itemIds);
                }
            });

        }
    }

    @SuppressLint("InflateParams")
    private void initCreateOptionView(List<ScoreOption> scoreOptions,LinearLayout linOptionContainer){
        String store_id = linOptionContainer.getTag().toString();
        if(mAllSelected == null){
            mAllSelected = new HashMap<String, SparseBooleanArray>();
        }
        SparseBooleanArray sparseBooleanArray = mAllSelected.get(store_id);
        if(sparseBooleanArray == null){
            sparseBooleanArray = new SparseBooleanArray();
            mAllSelected.put(store_id, sparseBooleanArray);
        }else{
            sparseBooleanArray.clear();
        }
        linOptionContainer.removeAllViews();
        if(scoreOptions != null && scoreOptions.size() > 0){
            for (ScoreOption scoreOption : scoreOptions) {
                LinearLayout convertView =
                        (LinearLayout) LayoutInflater.from(this).inflate(
                                R.layout.activity_evaluation_option, null);
                linOptionContainer.addView(convertView);
                
                TextView tvName = (TextView)convertView.findViewById(R.id.tv_option_name);
                tvName.setText(scoreOption.getName());
                CheckBox check = (CheckBox)convertView.findViewById(R.id.cb_option_check);
                check.setTag(scoreOption.getItem_id()+","+store_id);
                check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    
                    @Override
                    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                        String tags[] = v.getTag().toString().split(",");
                        int tag = Integer.valueOf(tags[0]);
                        SparseBooleanArray sparseBooleanArray = mAllSelected.get(tags[1]);
                        sparseBooleanArray.put(tag, isChecked);
                    }
                });
            }
        }
    }
    
    private List<ScoreOption> getScoreOptionsByScore(float scpre){
        if(mScoreOptionListList.getScore_list() != null){
            for (ScoreOptionList scoreOptionList : mScoreOptionListList.getScore_list()) {
                if(scpre == scoreOptionList.getScore()){
                    return scoreOptionList.getList();
                }
            }
        }
        return null;
    }
    
    /**
     * 获取评分选项
     */
    private void httpRequestQueryScoreOption() {
        ScoreOptionsTask task = new ScoreOptionsTask(OrderEvaluationActivity.this, NODE_KEY);
        task.setCallBack(new AbstractHttpResponseHandler<ScoreOptionListList>() {

            @Override
            public void onSuccess(ScoreOptionListList result) {
                httpRequestQueryOrderEvalution();
                mScoreOptionListList = result;
                mDataLoadingLayout.showDataLoadSuccess();

                if (null != result && result.getScore_list() != null) {
                    if (result.getScore_list().size() == 0) {
                        mDataLoadingLayout.showDataEmptyView();
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
     * 评价提交
     */
    private void httpRequestSubmit(String comment_key, String store_id, String content,
            Float score, String item_ids) {
        EvalutionSubmitTask task =
                new EvalutionSubmitTask(OrderEvaluationActivity.this, comment_key, store_id, mOdersId,content,
                        score, item_ids);
        task.setCallBack(new AbstractHttpResponseHandler<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.d("onSuccess result = " + result);
                showToastMsg("评价成功");
                httpRequestQueryOrderEvalution();
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
}
