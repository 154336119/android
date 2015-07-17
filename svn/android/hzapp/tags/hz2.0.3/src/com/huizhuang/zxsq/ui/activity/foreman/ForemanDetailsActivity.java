package com.huizhuang.zxsq.ui.activity.foreman;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.Share;
import com.huizhuang.zxsq.http.bean.foreman.ConstructionSite;
import com.huizhuang.zxsq.http.bean.foreman.ForemanComment;
import com.huizhuang.zxsq.http.bean.foreman.ForemanDetails;
import com.huizhuang.zxsq.http.task.foreman.ForemanDetailsTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderEditorInfoActivity;
import com.huizhuang.zxsq.ui.activity.solution.SolutionDetailActivity;
import com.huizhuang.zxsq.ui.adapter.foreman.ForemanDetailAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 工长详情
 */
public class ForemanDetailsActivity extends BaseActivity implements OnClickListener {

    private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;

    private XListView mXListView;
//    private View mVine;


    private ForemanDetailAdapter mAdapter;
    private String mForemanName;
    private List<Bitmap> mBits;
    private String mForemanId;
    private View mListViewHeaderView;
    private View mListViewFooterView;
    private int mScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreman_details);
        getIntentExtra();
        initActionBar();
        initViews();
    }

    private void getIntentExtra() {
        mForemanId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_COMPANY_ID);
        mForemanName = getIntent().getStringExtra("foreman_name");
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
        mCommonActionBar.setRightImgBtn(R.drawable.ic_actionbar_share, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                PreferenceConfig.setShareByForeman(true);
                PreferenceConfig.setShareByForemanName(mForemanName);
                Share share = new Share();
                Util.showShare(false, ForemanDetailsActivity.this, share);
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
        mXListView = (XListView) findViewById(R.id.x_list_view);
        mXListView.setPullRefreshEnable(false);
        mXListView.setPullLoadEnable(false);
        mXListView.setAutoLoadMoreEnable(false);
        mXListView.setAutoRefreshEnable(true);
        mXListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                httpRequestGetForemanDetail();
            }

            @Override
            public void onLoadMore() {}
        });
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mAdapter = new ForemanDetailAdapter(this, mScreenWidth);
        mXListView.setAdapter(mAdapter);

        findViewById(R.id.ll_submit).setOnClickListener(this);
        mXListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                if (position >= mXListView.getHeaderViewsCount()) {
                    if(position - 2 <= mAdapter.getList().size()){
                        ConstructionSite constructionSite = mAdapter.getList().get(position - 2);
                        toConstructionSite(constructionSite.getId());
                    }
                }
            }

        });
    }

    private void httpRequestGetForemanDetail() {
        ForemanDetailsTask task = new ForemanDetailsTask(this, mForemanId, null, null);
        task.setCallBack(new AbstractHttpResponseHandler<ForemanDetails>() {

            @Override
            public void onStart() {
                super.onStart();
                mDataLoadingLayout.showDataLoading();
                Log.e("onStart()", "onStart()");
            }

            @Override
            public void onSuccess(ForemanDetails foremanDetails) {
                mDataLoadingLayout.showDataLoadSuccess();
                mXListView.onRefreshComplete();
                mXListView.onLoadMoreComplete();
                if (mListViewFooterView == null) {
                    mListViewFooterView = getXlistViewFooter();
                    mXListView.addFooterView(mListViewFooterView);
                }
                if (foremanDetails != null) {
                    if (mListViewHeaderView == null) {
                        mListViewHeaderView = getXlistViewHeader(foremanDetails);
                        mXListView.addHeaderView(mListViewHeaderView);
                    }
                    if (foremanDetails.getShowcase() != null
                            && foremanDetails.getShowcase().size() > 0) {
                        mAdapter.setList(foremanDetails.getShowcase());
                        mAdapter.notifyDataSetChanged();
//                        mVine.setVisibility(View.VISIBLE);
                    }
//                    else{
//                        mVine.setVisibility(View.GONE);
//                    }
                } else {
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

    private View getXlistViewFooter() {
        mBits = new ArrayList<Bitmap>();
        View view = View.inflate(this, R.layout.activity_foreman_details_footer, null);
        ImageView ivSiteImg1 = (ImageView) view.findViewById(R.id.iv_site1);
        ImageView ivSiteImg2 = (ImageView) view.findViewById(R.id.iv_site2);
//        mVine = (View) view.findViewById(R.id.v_line);
        int width = mScreenWidth - DensityUtil.dip2px(getApplicationContext(), 34);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, (int) (width / 1.6));
        lp.setMargins(DensityUtil.dip2px(getApplicationContext(), 20), 0,
                DensityUtil.dip2px(getApplicationContext(), 20),
                DensityUtil.dip2px(getApplicationContext(), 20));
        ivSiteImg1.setLayoutParams(lp);
        Bitmap bitmap1 = ImageUtil.readBitMap(this, R.drawable.icon_foreman_site_img1);
        ivSiteImg1.setImageBitmap(bitmap1);
        mBits.add(bitmap1);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width, (int) (width / 1.6));
        lp1.setMargins(DensityUtil.dip2px(getApplicationContext(), 20), 0,
                DensityUtil.dip2px(getApplicationContext(), 20),
                DensityUtil.dip2px(getApplicationContext(), 30));
        ivSiteImg2.setLayoutParams(lp1);
        Bitmap bitmap2 = ImageUtil.readBitMap(this, R.drawable.icon_foreman_site_img2);
        ivSiteImg2.setImageBitmap(bitmap2);
        mBits.add(bitmap2);

        return view;
    }

    private View getXlistViewHeader(ForemanDetails foremanDetails) {
        View view = View.inflate(this, R.layout.activity_foreman_details_header, null);
        CircleImageView CvHead = (CircleImageView) view.findViewById(R.id.civ_head);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvCity = (TextView) view.findViewById(R.id.tv_city);
        TextView tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
        TextView tvQuotation = (TextView) view.findViewById(R.id.tv_quotation);
        TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
        TextView tvMsgCount = (TextView) view.findViewById(R.id.tv_comment);
        // 用户评价
        LinearLayout linComment = (LinearLayout) view.findViewById(R.id.lin_comment);
        CircleImageView cvUserHead = (CircleImageView) view.findViewById(R.id.iv_user_head);
        TextView tvUserPhone = (TextView) view.findViewById(R.id.tv_user_phone);
        TextView tvUserTime = (TextView) view.findViewById(R.id.tv_user_time);
        TextView tvUserComment = (TextView) view.findViewById(R.id.tv_user_content);
        TextView tvSiteCount = (TextView) view.findViewById(R.id.site_count);
        if (foremanDetails.getLogo() != null) {
            ImageLoader.getInstance().displayImage(foremanDetails.getLogo().getThumb_path(),
                    CvHead, ImageLoaderOptions.optionsUserHeader);
        }
        tvName.setText(foremanDetails.getFull_name());
        Drawable drawable = getResources().getDrawable(R.drawable.icon_rz);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (foremanDetails.getIs_auth() == 1) {
            tvName.setCompoundDrawables(null, null, drawable, null);
        } else {
            tvName.setCompoundDrawables(null, null, null, null);
        }
        tvCity.setText(foremanDetails.getPlace_name());
        tvAppointment.setText(foremanDetails.getOrder_count() + "");
        tvQuotation.setText(foremanDetails.getOrder_done_count() + "");
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
        tvPrice.setText(p + "万");
        tvMsgCount.setText(String.format(getResources().getString(R.string.txt_message_count),
                foremanDetails.getComment_count()));
        ForemanComment foremanComment = foremanDetails.getNew_comment();
        if (foremanComment != null && foremanDetails.getComment_count() > 0) {
            linComment.setVisibility(View.VISIBLE);
            if (foremanComment.getImage() != null) {
                ImageLoader.getInstance().displayImage(foremanComment.getImage().getThumb_path(),
                        cvUserHead, ImageLoaderOptions.optionsUserHeader);
            }
            String phone = foremanComment.getName();
            if (!TextUtils.isEmpty(phone)) {
                char[] phones = phone.toCharArray();
                if (Util.isNumeric(phone)) {
                    phone = "";
                    for (int i = 0; i < phones.length; i++) {
                        if (i > 2 && i < 8) {
                            phone += "*";
                        } else {
                            phone += phones[i];
                        }
                    }
                }
                tvUserPhone.setText(phone);
            }
            tvUserTime.setText(DateUtil.friendlyTime1(DateUtil.timestampToSdate(
                    foremanComment.getTime(), "yyyy-MM-dd HH:mm:ss")));
            if (TextUtils.isEmpty(foremanComment.getContent())) {
                tvUserComment.setText(getResources().getString(R.string.txt_default_comment));
            } else {
                tvUserComment.setText(foremanComment.getContent());
            }
        } else {
            linComment.setVisibility(View.GONE);
        }

        tvSiteCount.setText(String.format(getResources().getString(R.string.txt_site_count),
                foremanDetails.getShowcase() == null ? 0 : foremanDetails.getShowcase().size()));
        view.findViewById(R.id.rl_all_comment).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_submit:
                construction();
                break;

            case R.id.rl_all_comment:
                toPublicComments();
                break;
            default:
                break;
        }
    }

    // 进入下单流程
    private void construction() {
        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0016);
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.PARAM_ORDER_TYPE, OrderEditorInfoActivity.PARAM_TYPE_FOREMAN);
        bundle.putString(AppConstants.PARAM_ORDER_COMPANY_ID, mForemanId);
        bundle.putString(AppConstants.PARAM_ORDER_SOURCE_NAME, "app_gongzhang_detail");

        ActivityUtil.checkAppointmentToJump(this, bundle);
    }

    // 跳转到口碑评价
    private void toPublicComments() {
        AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0015);
        Intent intent = new Intent(this, ForemanCommentActivity.class);
        intent.putExtra(AppConstants.PARAM_ORDER_COMPANY_ID, mForemanId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBits != null) {
            for (Bitmap bitmap : mBits) {
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
            mBits.clear();
            mBits = null;
        }
        System.gc();
        ImageLoader.getInstance().clearMemoryCache();
        LogUtil.e("onDestroy()");
    }


    // 进入施工现场详情
    private void toConstructionSite(String showcase_id) {
        AnalyticsUtil.onEvent(this, "click23");
        Intent intent = new Intent(this, SolutionDetailActivity.class);
        intent.putExtra(SolutionDetailActivity.PARAME_SHOW_CASE_ID_KEY, showcase_id);
        intent.putExtra(AppConstants.PARAM_ORDER_COMPANY_ID, mForemanId);
        startActivity(intent);
    }

}
