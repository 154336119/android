package com.huizhuang.zxsq.ui.activity.foreman;

import java.text.DecimalFormat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.foreman.ForemanDetails;
import com.huizhuang.zxsq.http.task.foreman.ForemanDetailsTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderEditorInfoActivity;
import com.huizhuang.zxsq.ui.activity.solution.SolutionDetailActivity;
import com.huizhuang.zxsq.ui.adapter.foreman.ForemanDetailAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.MyListView;
import com.huizhuang.zxsq.widget.MyScrollView;
import com.huizhuang.zxsq.widget.MyScrollView.OnScrollListener;
import com.huizhuang.zxsq.widget.TextViewRotate;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 工长详情
 */
public class ForemanDetailsActivity extends BaseActivity implements OnClickListener {

    private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;

    private MyScrollView mMyScrollView;
    private CircleImageView mCivHead;
    private TextView mTvName;
    // private ImageView mIvSex;
    private TextView mTvCity;
    // private View mLine1;
    private TextView mTvAppointment;
//    private TextView mTvAppointmentTxt;
    // private View mLine2;
    private TextView mTvQuotation;
    // private TextView mTvQuotationTxt;
    private RatingBar mRbScore;
    private TextView mTvScore;
    private TextView mTvDis;
    private TextViewRotate mTvPrice;
    private TextView mTvComment;

    private LinearLayout mLinMore;
    private TextView mTvAddr;
    private TextView mTvExperience;
    private ImageView mIvMore;
    // tab
    private LinearLayout mLinTab;
    private ImageView mIvSite;
    private TextView mTvSite;
    private ImageView mIvLowerPrice;
    private TextView mTvLowerPrice;
    private ImageView mIvSafety;
    private TextView mTvSafety;
    // 浮动tab
    private LinearLayout mLinTab1;
    private ImageView mIvSite1;
    private TextView mTvSite1;
    private ImageView mIvLowerPrice1;
    private TextView mTvLowerPrice1;
    private ImageView mIvSafety1;
    private TextView mTvSafety1;

    private View mViewSubSite;
    private ImageView mIvSiteImg1;
    private ImageView mIvSiteImg2;
    private MyListView mListView;
    private View mVline;

    private View mViewSubPrice;
    private View mViewSubSafety;

    private String mForemanId;
    private String mLatitude;
    private String mLongitude;
    private boolean mIsShowMore = false;
    private int[] mLocaltions = new int[2];
    private Drawable mDrawableSitePress;
    private Drawable mDrawableSiteNormal;
    private Drawable mDrawablePricePress;
    private Drawable mDrawablePriceNormal;
    private Drawable mDrawableSafetyPress;
    private Drawable mDrawableSafetyNormal;

    private ForemanDetailAdapter mAdapter;
    private int mScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreman_details);
        getIntentExtra();
        initActionBar();
        initViews();
        initStubViewSite();
        initDrawable();
        httpRequestGetForemanDetail();
    }

    private void getIntentExtra() {
        mForemanId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_COMPANY_ID);
        mLatitude = getIntent().getStringExtra("latitude");
        mLongitude = getIntent().getStringExtra("longitude");
    }

    private void initActionBar() {
        LogUtil.d("initActionBar");

        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("工长详情");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequestGetForemanDetail();
            }
        });
        mMyScrollView = (MyScrollView) findViewById(R.id.my_scroll);
        mCivHead = (CircleImageView) findViewById(R.id.civ_head);
        mTvName = (TextView) findViewById(R.id.tv_name);
        // mIvSex = (ImageView) findViewById(R.id.iv_sex);
        mTvCity = (TextView) findViewById(R.id.tv_city);
        // mLine1 = (View) findViewById(R.id.v_line1);
        mTvAppointment = (TextView) findViewById(R.id.tv_appointment);
//        mTvAppointmentTxt = (TextView) findViewById(R.id.tv_appointment_txt);
        // mLine2 = (View) findViewById(R.id.v_line2);
        mTvQuotation = (TextView) findViewById(R.id.tv_quotation);
        // mTvQuotationTxt = (TextView) findViewById(R.id.tv_quotation_txt);
        mRbScore = (RatingBar) findViewById(R.id.rb_score);
        mTvScore = (TextView) findViewById(R.id.tv_score);
        mTvDis = (TextView) findViewById(R.id.tv_dis);
        mTvPrice = (TextViewRotate) findViewById(R.id.tv_price);

        mLinMore = (LinearLayout) findViewById(R.id.lin_more);
        mTvAddr = (TextView) findViewById(R.id.tv_addr);
        mTvExperience = (TextView) findViewById(R.id.tv_experience);
        mIvMore = (ImageView) findViewById(R.id.iv_more);
        mIvMore.setBackgroundResource(R.drawable.ic_fliter_arrow_down);
        mTvComment = (TextView) findViewById(R.id.tv_comment);

        mLinTab = (LinearLayout) findViewById(R.id.ic_main_menu);
        mIvSite = (ImageView) findViewById(R.id.iv_site);
        mTvSite = (TextView) findViewById(R.id.tv_construction_site);
        mIvLowerPrice = (ImageView) findViewById(R.id.iv_lower_price);
        mTvLowerPrice = (TextView) findViewById(R.id.tv_lower_price);
        mIvSafety = (ImageView) findViewById(R.id.iv_safety);
        mTvSafety = (TextView) findViewById(R.id.tv_safety);

        mLinTab1 = (LinearLayout) findViewById(R.id.ic_menu);
        mIvSite1 = (ImageView) mLinTab1.findViewById(R.id.iv_site);
        mTvSite1 = (TextView) mLinTab1.findViewById(R.id.tv_construction_site);
        mIvLowerPrice1 = (ImageView) mLinTab1.findViewById(R.id.iv_lower_price);
        mTvLowerPrice1 = (TextView) mLinTab1.findViewById(R.id.tv_lower_price);
        mIvSafety1 = (ImageView) mLinTab1.findViewById(R.id.iv_safety);
        mTvSafety1 = (TextView) mLinTab1.findViewById(R.id.tv_safety);


        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.lin_more).setOnClickListener(this);
        findViewById(R.id.rl_all_comment).setOnClickListener(this);
        findViewById(R.id.lin_site).setOnClickListener(this);
        findViewById(R.id.lin_lower_prices).setOnClickListener(this);
        findViewById(R.id.lin_safety).setOnClickListener(this);

        mLinTab1.findViewById(R.id.lin_site).setOnClickListener(this);
        mLinTab1.findViewById(R.id.lin_lower_prices).setOnClickListener(this);
        mLinTab1.findViewById(R.id.lin_safety).setOnClickListener(this);
        mMyScrollView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(int scrollY) {
                mLinTab.getLocationOnScreen(mLocaltions);
                int y = mLocaltions[1];// 获取组件当前位置的纵坐标
                mCommonActionBar.getLocationOnScreen(mLocaltions);
                int y1 = mLocaltions[1];// 获取组件当前位置的纵坐标
                int height = mCommonActionBar.getHeight();
                if (y <= (y1 + height)) {
                    mLinTab1.setVisibility(View.VISIBLE);
                } else {
                    mLinTab1.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 初始化施工现场view
     */
    private void initStubViewSite() {
        ViewStub stub = (ViewStub) findViewById(R.id.viewstub_site);
        mViewSubSite = stub.inflate();
        mListView = (MyListView) mViewSubSite.findViewById(R.id.lv_construction_site);
        mIvSiteImg1 = (ImageView) mViewSubSite.findViewById(R.id.iv_site1);
        mIvSiteImg2 = (ImageView) mViewSubSite.findViewById(R.id.iv_site2);
        mVline = (View) mViewSubSite.findViewById(R.id.v_line); 
        int width =
                UiUtil.getScreenWidth(ForemanDetailsActivity.this)
                        - DensityUtil.dip2px(getApplicationContext(), 40);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, (int) (width / 1.6));
        lp.setMargins(DensityUtil.dip2px(getApplicationContext(), 20), 0,
                DensityUtil.dip2px(getApplicationContext(), 20),
                DensityUtil.dip2px(getApplicationContext(), 20));
        mIvSiteImg1.setLayoutParams(lp);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width, (int) (width / 1.6));
        lp1.setMargins(DensityUtil.dip2px(getApplicationContext(), 20), 0,
                DensityUtil.dip2px(getApplicationContext(), 20),
                DensityUtil.dip2px(getApplicationContext(), 80));
        mIvSiteImg2.setLayoutParams(lp1);
        mAdapter = new ForemanDetailAdapter(this, width);
        mListView.setAdapter(mAdapter);
//        mListView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                toConstructionSite(mAdapter.getList().get(position).getId());
//            }
//
//        });
    }

    /**
     * 初始化最低价view
     */
    private void initStubViewPrice() {
        ViewStub stub = (ViewStub) findViewById(R.id.viewstub_price);
        mViewSubPrice = stub.inflate();
        ImageView imageView = (ImageView)mViewSubPrice.findViewById(R.id.iv_lower_price_bg);
        mScreenWidth = UiUtil.getScreenWidth(ForemanDetailsActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Double.valueOf(mScreenWidth*1.36).intValue());
        lp.setMargins(0, 0, 0, DensityUtil.dip2px(getBaseContext(), 80));
        imageView.setLayoutParams(lp);
    }

    /**
     * 初始化最安全view
     */
    private void initStubViewSafety() {
        ViewStub stub = (ViewStub) findViewById(R.id.viewstub_safety);
        mViewSubSafety = stub.inflate();
        ImageView imageView = (ImageView)mViewSubSafety.findViewById(R.id.iv_lower_safety_bg);
        mScreenWidth = UiUtil.getScreenWidth(ForemanDetailsActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Double.valueOf(mScreenWidth*3.05).intValue());
        lp.setMargins(0, 0, 0, DensityUtil.dip2px(getBaseContext(), 80));
        imageView.setLayoutParams(lp);
    }

    private void httpRequestGetForemanDetail() {
        ForemanDetailsTask task = new ForemanDetailsTask(this, mForemanId, mLatitude, mLongitude);
        task.setCallBack(new AbstractHttpResponseHandler<ForemanDetails>() {

            @Override
            public void onStart() {
                super.onStart();
                mDataLoadingLayout.showDataLoading();
            }

            @Override
            public void onSuccess(ForemanDetails foremanDetails) {
                mDataLoadingLayout.showDataLoadSuccess();
                if(foremanDetails != null){
                    if (foremanDetails.getShowcase() != null && foremanDetails.getShowcase().size() > 0) {
                        mAdapter.setList(foremanDetails.getShowcase());
                        mAdapter.notifyDataSetChanged();
                        mVline.setVisibility(View.VISIBLE);
                    }else{
                        mVline.setVisibility(View.GONE);
                    }
                    initData(foremanDetails);
                }else{
                    mDataLoadingLayout.showDataEmptyView();
                }
            }

            @Override
            public void onFailure(int code, String error) {
                mDataLoadingLayout.showDataLoadFailed(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
        task.send();
    }

    private void initData(ForemanDetails foremanDetails) {
        if (foremanDetails.getLogo() != null) {
            ImageLoader.getInstance().displayImage(foremanDetails.getLogo().getThumb_path(),
                    mCivHead, ImageLoaderOptions.optionsUserHeader);
        }
        mTvName.setText(foremanDetails.getFull_name());
        Drawable drawable = getResources().getDrawable(R.drawable.icon_rz);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (foremanDetails.getIs_auth() == 1) {
            mTvName.setCompoundDrawables(null, null, drawable, null);
        } else {
            mTvName.setCompoundDrawables(null, null, null, null);
        }
        float score = 5;
        try {
            if (!TextUtils.isEmpty(foremanDetails.getRank())) {
                score =
                        Float.valueOf(foremanDetails.getRank()) == 0 ? 5 : Float
                                .valueOf(foremanDetails.getRank());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mRbScore.setRating(score);
        mTvScore.setText(new DecimalFormat("0.0").format(score) + "分");
        Float price = 2f;
        if (!TextUtils.isEmpty(foremanDetails.getAvg_price())
                && !foremanDetails.getAvg_price().equals("0")) {
            price = Float.valueOf(foremanDetails.getAvg_price()) / 10000;
        }
        DecimalFormat df = new DecimalFormat("#.#");
        String p = df.format(price);
        if ("0".equals(p)) {
            p = "2";
        }
        String str1 = "均价\n" + p + "万";
        SpannableString text = new SpannableString(str1);
        text.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(getApplicationContext(), 10)), 0, 3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(getApplicationContext(), 12)), 3,
                text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvPrice.setText(text);
        if (TextUtils.isEmpty(foremanDetails.getDistance())) {
            mTvDis.setVisibility(View.GONE);
        } else {
            mTvDis.setVisibility(View.VISIBLE);
            float distance = Float.valueOf(foremanDetails.getDistance());
            if (distance > 1) {
                mTvDis.setText(df.format(distance) + "Km");
            } else {
                int dis = Float.valueOf((distance * 1000) + "").intValue();
                mTvDis.setText(dis + "m");
            }
        }
        mTvCity.setText(foremanDetails.getPlace_name());
        mTvAppointment.setText(foremanDetails.getOrder_count() + "");
        mTvQuotation.setText(foremanDetails.getWork_age() + "年");
        if (TextUtils.isEmpty(foremanDetails.getCompany_address())
                && TextUtils.isEmpty(foremanDetails.getExperience())) {
            mLinMore.setVisibility(View.GONE);
        } else {
            mLinMore.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(foremanDetails.getCompany_address())) {
            mTvAddr.setVisibility(View.GONE);
        } else {
            mTvAddr.setVisibility(View.VISIBLE);
        }
        mTvAddr.setText(foremanDetails.getCompany_address());
        if (TextUtils.isEmpty(foremanDetails.getExperience())) {
            mTvExperience.setVisibility(View.GONE);
        } else {
            mTvExperience.setVisibility(View.VISIBLE);
        }
        mTvExperience.setText(foremanDetails.getExperience());
        mTvComment.setText("全部评价(" + foremanDetails.getComment_count() + ")");
    }

    private void initDrawable() {
        mDrawableSitePress = getResources().getDrawable(R.drawable.icon_foreman_camera_press);
        mDrawableSitePress.setBounds(0, 0, mDrawableSitePress.getMinimumWidth(),
                mDrawableSitePress.getMinimumHeight());
        mDrawableSiteNormal = getResources().getDrawable(R.drawable.icon_foreman_camera_normal);
        mDrawableSiteNormal.setBounds(0, 0, mDrawableSiteNormal.getMinimumWidth(),
                mDrawableSiteNormal.getMinimumHeight());

        mDrawablePricePress = getResources().getDrawable(R.drawable.icon_foreman_diamonds_press);
        mDrawablePricePress.setBounds(0, 0, mDrawablePricePress.getMinimumWidth(),
                mDrawablePricePress.getMinimumHeight());
        mDrawablePriceNormal = getResources().getDrawable(R.drawable.icon_foreman_diamonds_normal);
        mDrawablePriceNormal.setBounds(0, 0, mDrawablePriceNormal.getMinimumWidth(),
                mDrawablePriceNormal.getMinimumHeight());

        mDrawableSafetyPress = getResources().getDrawable(R.drawable.icon_foreman_safety_press);
        mDrawableSafetyPress.setBounds(0, 0, mDrawableSafetyPress.getMinimumWidth(),
                mDrawableSafetyPress.getMinimumHeight());
        mDrawableSafetyNormal = getResources().getDrawable(R.drawable.icon_foreman_safety_normal);
        mDrawableSafetyNormal.setBounds(0, 0, mDrawableSafetyNormal.getMinimumWidth(),
                mDrawableSafetyNormal.getMinimumHeight());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_submit:
                construction();
                break;
            case R.id.lin_more:
                if (mIsShowMore) {
                    mIvMore.setBackgroundResource(R.drawable.ic_fliter_arrow_down);
                    mTvAddr.setSingleLine(true);
                    mTvExperience.setSingleLine(true);
                    mIsShowMore = false;
                } else {
                    mIvMore.setBackgroundResource(R.drawable.ic_fliter_arrow_up);
                    mTvAddr.setSingleLine(false);
                    mTvExperience.setSingleLine(false);
                    mIsShowMore = true;
                }
                break;
            case R.id.rl_all_comment:
                toPublicComments();
                break;
            case R.id.lin_site:// 施工现场
                mViewSubSite.setVisibility(View.VISIBLE);
                if (mViewSubSafety != null) {
                    mViewSubSafety.setVisibility(View.GONE);
                }
                if (mViewSubPrice != null) {
                    mViewSubPrice.setVisibility(View.GONE);
                }
                mIvSite.setVisibility(View.VISIBLE);
                mTvSite.setTextColor(getResources().getColor(R.color.color_ff6c38));
                mTvSite.setCompoundDrawables(null, mDrawableSitePress, null, null);

                mIvLowerPrice.setVisibility(View.GONE);
                mTvLowerPrice.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvLowerPrice.setCompoundDrawables(null, mDrawablePriceNormal, null, null);

                mIvSafety.setVisibility(View.GONE);
                mTvSafety.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvSafety.setCompoundDrawables(null, mDrawableSafetyNormal, null, null);

                mIvSite1.setVisibility(View.VISIBLE);
                mTvSite1.setTextColor(getResources().getColor(R.color.color_ff6c38));
                mTvSite1.setCompoundDrawables(null, mDrawableSitePress, null, null);

                mIvLowerPrice1.setVisibility(View.GONE);
                mTvLowerPrice1.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvLowerPrice1.setCompoundDrawables(null, mDrawablePriceNormal, null, null);

                mIvSafety1.setVisibility(View.GONE);
                mTvSafety1.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvSafety1.setCompoundDrawables(null, mDrawableSafetyNormal, null, null);
                break;
            case R.id.lin_lower_prices:// 最低价
                if (mViewSubPrice == null) {
                    initStubViewPrice();
                }
                mViewSubPrice.setVisibility(View.VISIBLE);
                if (mViewSubSite != null) {
                    mViewSubSite.setVisibility(View.GONE);
                }
                if (mViewSubSafety != null) {
                    mViewSubSafety.setVisibility(View.GONE);
                }
                mIvSite.setVisibility(View.GONE);
                mTvSite.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvSite.setCompoundDrawables(null, mDrawableSiteNormal, null, null);

                mIvLowerPrice.setVisibility(View.VISIBLE);
                mTvLowerPrice.setTextColor(getResources().getColor(R.color.color_ff6c38));
                mTvLowerPrice.setCompoundDrawables(null, mDrawablePricePress, null, null);

                mIvSafety.setVisibility(View.GONE);
                mTvSafety.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvSafety.setCompoundDrawables(null, mDrawableSafetyNormal, null, null);

                mIvSite1.setVisibility(View.GONE);
                mTvSite1.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvSite1.setCompoundDrawables(null, mDrawableSiteNormal, null, null);

                mIvLowerPrice1.setVisibility(View.VISIBLE);
                mTvLowerPrice1.setTextColor(getResources().getColor(R.color.color_ff6c38));
                mTvLowerPrice1.setCompoundDrawables(null, mDrawablePricePress, null, null);

                mIvSafety1.setVisibility(View.GONE);
                mTvSafety1.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvSafety1.setCompoundDrawables(null, mDrawableSafetyNormal, null, null);
                break;
            case R.id.lin_safety:// 最安全
                if (mViewSubSafety == null) {
                    initStubViewSafety();
                }
                mViewSubSafety.setVisibility(View.VISIBLE);
                if (mViewSubPrice != null) {
                    mViewSubPrice.setVisibility(View.GONE);
                }
                if (mViewSubSite != null) {
                    mViewSubSite.setVisibility(View.GONE);
                }
                mIvSite.setVisibility(View.GONE);
                mTvSite.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvSite.setCompoundDrawables(null, mDrawableSiteNormal, null, null);

                mIvLowerPrice.setVisibility(View.GONE);
                mTvLowerPrice.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvLowerPrice.setCompoundDrawables(null, mDrawablePriceNormal, null, null);

                mIvSafety.setVisibility(View.VISIBLE);
                mTvSafety.setTextColor(getResources().getColor(R.color.color_ff6c38));
                mTvSafety.setCompoundDrawables(null, mDrawableSafetyPress, null, null);

                mIvSite1.setVisibility(View.GONE);
                mTvSite1.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvSite1.setCompoundDrawables(null, mDrawableSiteNormal, null, null);

                mIvLowerPrice1.setVisibility(View.GONE);
                mTvLowerPrice1.setTextColor(getResources().getColor(R.color.gray_disabled));
                mTvLowerPrice1.setCompoundDrawables(null, mDrawablePriceNormal, null, null);

                mIvSafety1.setVisibility(View.VISIBLE);
                mTvSafety1.setTextColor(getResources().getColor(R.color.color_ff6c38));
                mTvSafety1.setCompoundDrawables(null, mDrawableSafetyPress, null, null);
                break;
            default:
                break;
        }
    }

    // 进入下单流程
    private void construction() {
        AnalyticsUtil.onEvent(this, "click24");
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.PARAM_ORDER_TYPE, OrderEditorInfoActivity.PARAM_TYPE_FOREMAN);
        bundle.putString(AppConstants.PARAM_ORDER_COMPANY_ID, mForemanId);
        bundle.putString(AppConstants.PARAM_ORDER_SOURCE_NAME, "app_gongzhang_detail");
        
        ActivityUtil.checkAppointmentToJump(this, bundle);
    }

    // 跳转到口碑评价
    private void toPublicComments() {
        AnalyticsUtil.onEvent(this, "click22");
        Intent intent = new Intent(this, PublicCommentsActivity.class);
        intent.putExtra(AppConstants.PARAM_ORDER_COMPANY_ID, mForemanId);
        startActivity(intent);
    }

    // //跳转到服务保障
    // private void toSupportService() {
    // AnalyticsUtil.onEvent(this, "click21");
    // Intent intent = new Intent(this, SupportServicesActivity.class);
    // intent.putExtra(SupportServicesActivity.FROM_TYPE, 1);
    // startActivity(intent);
    // }

    // //跳转到报价清单
    // private void toPriceList() {
    // AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0015);
    // if (foremanDetails != null && foremanDetails.getQuote_template_count() > 0) {
    // Intent intent = new Intent(this, ForemanPriceListActivity.class);
    // intent.putExtra("foreman_id", foreman_id);
    // startActivity(intent);
    // }
    // }

    // 进入施工现场详情
    private void toConstructionSite(String showcase_id) {
        AnalyticsUtil.onEvent(this, "click23");
        Intent intent = new Intent(this, SolutionDetailActivity.class);
        intent.putExtra(SolutionDetailActivity.PARAME_SHOW_CASE_ID_KEY, showcase_id);
        intent.putExtra(AppConstants.PARAM_ORDER_COMPANY_ID, mForemanId);
        startActivity(intent);
    }


    // @Override
    // public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // super.onActivityResult(requestCode, resultCode, data);
    // if (requestCode == REQ_LOGIN_FOR_COLLECTION_CODE && resultCode == Activity.RESULT_OK) {
    // if (ZxsqApplication.getInstance().isLogged()) {
    // toCollection();
    // }
    // }
    // }

}
