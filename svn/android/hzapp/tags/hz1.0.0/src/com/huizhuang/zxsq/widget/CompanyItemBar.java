package com.huizhuang.zxsq.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.Company;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CompanyItemBar extends RelativeLayout {

    private Context mContext;

    private ImageView mIvCover;
    private TextView mTvCompanyName;
    private TextView mTvPrice;
    private TextView mTvDistance;
    private TextView mTvScore;
    private TextView mTvOrderNum;
    private TextView mTvScore2;
    private TextView mTvOrderNum2;

    private ImageView mIvFree;
    private ImageView mIvQuality;
    private ImageView mIvEnsure;
    private ImageView mIvZero;
    private ImageView mIvBack;
    private ImageView mIvPromotion;
    private ImageView mIvTag;

    private LinearLayout mScoreLayout;
    private LinearLayout mScoreLayout2;

    private CheckBox mCbSelected;

    private boolean mSelected;

    public CompanyItemBar(Context context) {
        super(context);
        mContext = context;
        initViews();
    }

    public CompanyItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    private void initViews() {
        View rootLayout = LayoutInflater.from(mContext).inflate(R.layout.adapter_recommend_company, this);
        mIvCover = (ImageView) rootLayout.findViewById(R.id.iv_cover);
        mIvFree = (ImageView) rootLayout.findViewById(R.id.iv_free);
        mIvQuality = (ImageView) rootLayout.findViewById(R.id.iv_quality);
        mIvEnsure = (ImageView) rootLayout.findViewById(R.id.iv_ensure);
        mIvZero = (ImageView) rootLayout.findViewById(R.id.iv_zero);
        mIvBack = (ImageView) rootLayout.findViewById(R.id.iv_back);
        mIvPromotion = (ImageView) rootLayout.findViewById(R.id.iv_promotion);
        mIvTag = (ImageView) rootLayout.findViewById(R.id.iv_tag);

        mTvCompanyName = (TextView) rootLayout.findViewById(R.id.tv_company_name);
        mTvPrice = (TextView) rootLayout.findViewById(R.id.tv_average_price);
        mTvDistance = (TextView) rootLayout.findViewById(R.id.tv_undertake_price);
        mTvScore = (TextView) rootLayout.findViewById(R.id.tv_score);
        mTvOrderNum = (TextView) rootLayout.findViewById(R.id.tv_filter_context);
        mTvScore2 = (TextView) rootLayout.findViewById(R.id.tv_score2);
        mTvOrderNum2 = (TextView) rootLayout.findViewById(R.id.tv_filter_context2);

        mScoreLayout = (LinearLayout) rootLayout.findViewById(R.id.ll_score);
        mScoreLayout2 = (LinearLayout) rootLayout.findViewById(R.id.ll_score2);
        mCbSelected = (CheckBox) findViewById(R.id.cb_select);
    }

    public void setInfo(Company company){
        mTvCompanyName.setText(company.getName());
    }

    public void setInfo(String imageUrl, String name, String price, String distance, String score, String orderNum) {
        ImageLoader.getInstance().displayImage(imageUrl, mIvCover, ImageLoaderOptions.optionsDefaultEmptyPhoto);
        mTvCompanyName.setText(name);
        mTvPrice.setText(price);
        mTvDistance.setText(distance);
        mTvScore.setText(score);
        mTvOrderNum.setText(orderNum);
    }

    public void hasFreeIcon(boolean has) {
        if (has) {
            mIvFree.setVisibility(VISIBLE);
        } else {
            mIvFree.setVisibility(GONE);
        }
    }

    public void hasQualityIcon(boolean has) {
        if (has) {
            mIvQuality.setVisibility(VISIBLE);
        } else {
            mIvQuality.setVisibility(GONE);
        }
    }

    public void hasEnsureIcon(boolean has) {
        if (has) {
            mIvEnsure.setVisibility(VISIBLE);
        } else {
            mIvEnsure.setVisibility(GONE);
        }
    }

    public void hasZeroIcon(boolean has) {
        if (has) {
            mIvZero.setVisibility(VISIBLE);
        } else {
            mIvZero.setVisibility(GONE);
        }
    }

    public void hasBackIcon(boolean has) {
        if (has) {
            mIvBack.setVisibility(VISIBLE);
        } else {
            mIvBack.setVisibility(GONE);
        }
    }

    public void hasPromotionIcon(boolean has) {
        if (has) {
            mIvPromotion.setVisibility(VISIBLE);
        } else {
            mIvPromotion.setVisibility(GONE);
        }
    }

    public void setCheckModel(boolean model) {
        if (model) {
            mCbSelected.setVisibility(View.VISIBLE);
            mScoreLayout.setVisibility(View.GONE);
            mScoreLayout2.setVisibility(View.VISIBLE);
            mIvTag.setVisibility(View.VISIBLE);
        } else {
            mScoreLayout.setVisibility(View.VISIBLE);
            mCbSelected.setVisibility(View.GONE);
            mScoreLayout2.setVisibility(View.GONE);
            mIvTag.setVisibility(View.GONE);
        }
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
        mCbSelected.setChecked(selected);
    }
}
