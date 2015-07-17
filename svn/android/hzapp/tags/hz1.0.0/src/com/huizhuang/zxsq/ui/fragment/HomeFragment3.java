package com.huizhuang.zxsq.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.CityDistrict;
import com.huizhuang.zxsq.http.bean.Logo;
import com.huizhuang.zxsq.http.bean.home.Advertise;
import com.huizhuang.zxsq.http.bean.home.EssenceDiary;
import com.huizhuang.zxsq.http.bean.home.GoodForemanCase;
import com.huizhuang.zxsq.http.bean.home.HomePageData;
import com.huizhuang.zxsq.http.task.HomePageTask;
import com.huizhuang.zxsq.module.Author;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.Visitor;
import com.huizhuang.zxsq.ui.activity.ImageSimpleBrowseActivity;
import com.huizhuang.zxsq.ui.activity.MainActivity;
import com.huizhuang.zxsq.ui.activity.PersonalHomepageActivity;
import com.huizhuang.zxsq.ui.activity.bill.BillMainActivity;
import com.huizhuang.zxsq.ui.activity.company.CompanyCityList;
import com.huizhuang.zxsq.ui.activity.diary.DiaryDetailActivity;
import com.huizhuang.zxsq.ui.activity.diary.DiaryEditActivity;
import com.huizhuang.zxsq.ui.activity.foreman.ForemanConstructionSiteActivity;
import com.huizhuang.zxsq.ui.activity.foreman.ForemanDetailsActivity;
import com.huizhuang.zxsq.ui.activity.foreman.SupportServicesActivity;
import com.huizhuang.zxsq.ui.activity.image.ImageSelectActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.ui.activity.zxbd.ZxbdIntroActivity;
import com.huizhuang.zxsq.ui.adapter.HomeAdvImageAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.CircleFlowIndicator;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.ViewFlow;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.component.ForemanListFilterBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 2.0新版首页
 *
 * @author wumaojie.gmail.com
 * @ClassName: HomeFragment3
 * @date 2015-2-2 上午11:47:19
 */
public class HomeFragment3 extends BaseFragment implements OnClickListener {

    //保障服务页 返回
    private static final int SUPPORTSERVICES = 34124;
    //城市列表返回
    private static final int CITY_LIST = 33545;

    //写日记获取图片 等处理返回
    private static final int REQ_SEARCH_CODE = 666;
    private static final int REQ_LOGIN_FOR_DIARY_CODE = 102;
    private static final int REQ_LOGIN_FOR_ZXJZ_CODE = 100;

    private CommonActionBar mCommonActionBar;
    private DataLoadingLayout mDataLoadingLayout;
    // 广告
    protected ViewFlow mViewFlow;
    // 广告导航
    protected CircleFlowIndicator circleFlowIndicator;
    // 抢，
    protected TextView grabforeman;
    // 效果图，宝典，日记，记账
    protected TextView effect, collection, diary, bookkeeping;
    // 免费装修总布局,省，担保
    protected LinearLayout free_all_layout, free_layout, preferential, guarantee;
    // 免费装修图片
    protected ImageView free_img;
    // 免费装修倒计时，内容
    protected TextView free_countdown, free_text;
    // 精华日记总布局和item1,item2
    protected LinearLayout essencediary_all_layout, essencediary_item1, essencediary_item2, item1_person, item1_diary, item2_person, item2_diary;
    // 精华日记更多和分隔线
    protected ImageView essencediary_more, essencediary_line;
    // 精华日记，更多， 名字，标题，内容
    protected TextView essencediary_name1, essencediary_name2, essencediary_title1, essencediary_title2, essencediary_text1, essencediary_text2;
    // 圆形头像
    protected CircleImageView essencediary_head1, essencediary_head2, goodforeman_head1, goodforeman_head2;
    // 惠装好工长总布局和item1,item2
    protected LinearLayout goodforeman_all_layout, goodforeman_item1, goodforeman_item2;
    // 惠装好工长评分控件
    protected RatingBar goodforeman_score1, goodforeman_score2;
    // 惠装好工长 图片 和 更多按钮
    protected ImageView goodforeman_img1_a, goodforeman_img1_b,
            goodforeman_img1_c, goodforeman_img2_a, goodforeman_img2_b,
            goodforeman_img2_c, goodforeman_more, goodforeman_line;
    // 惠装好工长 名字，小区，评分值，评分人数
    protected TextView goodforeman_name1, goodforeman_name2,
            goodforeman_community1, goodforeman_community2,
            goodforeman_score_tv1, goodforeman_score_tv2,
            goodforeman_score_count1, goodforeman_score_count2;

    private HomeAdvImageAdapter mHomePageAdapter;
    private HomePageData mHomePageData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home3, null);
        initActionBar(view);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initActionBar(View view) {
        mCommonActionBar = (CommonActionBar) view.findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("惠装-装修我最低");
        String city = ZxsqApplication.getInstance().getLocationCity();
        if (city != null) {
            city = city.replace("市", "");
        } else {
            city = "成都";
        }
        mCommonActionBar.setLeftBtn(R.drawable.home_page_title_location, city, new OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsUtil.onEvent(getActivity(), "click01");
                toCityList();
            }
        });
        mCommonActionBar.setRightImgBtn(R.drawable.home_page_title_phone, new OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入拨号页面
                Util.callPhone(getActivity(), AppConstants.SERVICE_PHONE);
            }
        });
    }

    private void initViews(View view) {
        // 加载
        mDataLoadingLayout = (DataLoadingLayout) view.findViewById(R.id.home_data_loading_layout);
        // 数据重载
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        mViewFlow = (ViewFlow) view.findViewById(R.id.main_page_viewflow);
        circleFlowIndicator = (CircleFlowIndicator) view.findViewById(R.id.main_page_viewflowindic);
        // 抢，省，担保
        grabforeman = (TextView) view.findViewById(R.id.main_page_grabforeman);
        preferential = (LinearLayout) view.findViewById(R.id.main_page_preferential);
        guarantee = (LinearLayout) view.findViewById(R.id.main_page_guarantee);
        // 效果图，宝典，日记，记账
        effect = (TextView) view.findViewById(R.id.main_page_effect);
        collection = (TextView) view.findViewById(R.id.main_page_collection);
        diary = (TextView) view.findViewById(R.id.main_page_diary);
        bookkeeping = (TextView) view.findViewById(R.id.main_page_bookkeeping);
        // 免费装修总布局
        free_all_layout = (LinearLayout) view.findViewById(R.id.main_page_free_all_layout);
        free_layout = (LinearLayout) view.findViewById(R.id.main_page_free_layout);
        // 免费装修图片
        free_img = (ImageView) view.findViewById(R.id.main_page_free_img);
        // 免费装修倒计时，内容
        free_countdown = (TextView) view.findViewById(R.id.main_page_free_countdown);
        free_text = (TextView) view.findViewById(R.id.main_page_free_text);
        // 精华日记总布局和item1,item2
        essencediary_all_layout = (LinearLayout) view.findViewById(R.id.main_page_essencediary_all_layout);
        essencediary_item1 = (LinearLayout) view.findViewById(R.id.main_page_essencediary_item1);
        essencediary_item2 = (LinearLayout) view.findViewById(R.id.main_page_essencediary_item2);
        item1_person = (LinearLayout) view.findViewById(R.id.main_page_essencediary_item1_person);
        item2_person = (LinearLayout) view.findViewById(R.id.main_page_essencediary_item2_person);
        item1_diary = (LinearLayout) view.findViewById(R.id.main_page_essencediary_item1_diary);
        item2_diary = (LinearLayout) view.findViewById(R.id.main_page_essencediary_item2_diary);
        // 精华日记 分隔线，更多
        essencediary_line = (ImageView) view.findViewById(R.id.main_page_essencediary_line);
        essencediary_more = (ImageView) view.findViewById(R.id.main_page_essencediary_more);
        // 精华日记， 名字，标题，内容
        essencediary_name1 = (TextView) view.findViewById(R.id.main_page_essencediary_name1);
        essencediary_name2 = (TextView) view.findViewById(R.id.main_page_essencediary_name2);
        essencediary_title1 = (TextView) view.findViewById(R.id.main_page_essencediary_title1);
        essencediary_title2 = (TextView) view.findViewById(R.id.main_page_essencediary_title2);
        essencediary_text1 = (TextView) view.findViewById(R.id.main_page_essencediary_text1);
        essencediary_text2 = (TextView) view.findViewById(R.id.main_page_essencediary_text2);

        // 精华日记头像
        essencediary_head1 = (CircleImageView) view.findViewById(R.id.main_page_essencediary_head1);
        essencediary_head2 = (CircleImageView) view.findViewById(R.id.main_page_essencediary_head2);
        // 惠装好工长头像
        goodforeman_head1 = (CircleImageView) view.findViewById(R.id.main_page_goodforeman_head1);
        goodforeman_head2 = (CircleImageView) view.findViewById(R.id.main_page_goodforeman_head2);

        // 惠装好工长总布局和item1,item2
        goodforeman_all_layout = (LinearLayout) view.findViewById(R.id.main_page_goodforeman_all_layout);
        goodforeman_item1 = (LinearLayout) view.findViewById(R.id.main_page_goodforeman_item1);
        goodforeman_item2 = (LinearLayout) view.findViewById(R.id.main_page_goodforeman_item2);

        // 惠装好工长评分控件
        goodforeman_score1 = (RatingBar) view.findViewById(R.id.main_page_goodforeman_score1);
        goodforeman_score2 = (RatingBar) view.findViewById(R.id.main_page_goodforeman_score2);

        // 惠装好工长 图片 和 更多按钮,分隔线
        goodforeman_img1_a = (ImageView) view.findViewById(R.id.main_page_goodforeman_img1_a);
        goodforeman_img1_b = (ImageView) view.findViewById(R.id.main_page_goodforeman_img1_b);
        goodforeman_img1_c = (ImageView) view.findViewById(R.id.main_page_goodforeman_img1_c);
        goodforeman_img2_a = (ImageView) view.findViewById(R.id.main_page_goodforeman_img2_a);
        goodforeman_img2_b = (ImageView) view.findViewById(R.id.main_page_goodforeman_img2_b);
        goodforeman_img2_c = (ImageView) view.findViewById(R.id.main_page_goodforeman_img2_c);
        goodforeman_more = (ImageView) view.findViewById(R.id.main_page_goodforeman_more);
        goodforeman_line = (ImageView) view.findViewById(R.id.main_page_goodforeman_line);

        // 惠装好工长 名字，小区，评分值，评分人数
        goodforeman_name1 = (TextView) view.findViewById(R.id.main_page_goodforeman_name1);
        goodforeman_name2 = (TextView) view.findViewById(R.id.main_page_goodforeman_name2);
        goodforeman_community1 = (TextView) view.findViewById(R.id.main_page_goodforeman_community1);
        goodforeman_community2 = (TextView) view.findViewById(R.id.main_page_goodforeman_community2);
        goodforeman_score_tv1 = (TextView) view.findViewById(R.id.main_page_goodforeman_score_tv1);
        goodforeman_score_tv2 = (TextView) view.findViewById(R.id.main_page_goodforeman_score_tv2);
        goodforeman_score_count1 = (TextView) view.findViewById(R.id.main_page_goodforeman_score_count1);
        goodforeman_score_count2 = (TextView) view.findViewById(R.id.main_page_goodforeman_score_count2);

        free_all_layout.setVisibility(View.VISIBLE);
        essencediary_all_layout.setVisibility(View.VISIBLE);
        goodforeman_all_layout.setVisibility(View.VISIBLE);

        // mViewFlow.setOnItemClickListener(this);
        grabforeman.setOnClickListener(this);
        preferential.setOnClickListener(this);
        guarantee.setOnClickListener(this);

        effect.setOnClickListener(this);
        collection.setOnClickListener(this);
        diary.setOnClickListener(this);
        bookkeeping.setOnClickListener(this);
        item1_person.setOnClickListener(this);
        item2_person.setOnClickListener(this);
        item1_diary.setOnClickListener(this);
        item2_diary.setOnClickListener(this);
        free_layout.setOnClickListener(this);
        // essencediary_item1.setOnClickListener(this);
        // essencediary_item2.setOnClickListener(this);
        goodforeman_item1.setOnClickListener(this);
        goodforeman_item2.setOnClickListener(this);

        essencediary_more.setOnClickListener(this);
        goodforeman_more.setOnClickListener(this);

        goodforeman_img1_a.setOnClickListener(this);
        goodforeman_img1_b.setOnClickListener(this);
        goodforeman_img1_c.setOnClickListener(this);
        goodforeman_img2_a.setOnClickListener(this);
        goodforeman_img2_b.setOnClickListener(this);
        goodforeman_img2_c.setOnClickListener(this);
        free_all_layout.setVisibility(View.GONE);
        //抢工长参数
        initFixedText("10000", "4554");
    }

    private void initFixedText(String str1, String str2) {
        SpannableString text = new SpannableString("抢口碑工长\n\n" + str1  + "名工长入驻");
        text.setSpan(new AbsoluteSizeSpan(45), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(30), 1, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        text.setSpan(new AbsoluteSizeSpan(20), 5, 19 + str1.length() + str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setSpan(new ForegroundColorSpan(0xffff6c38), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(0xffff6c38), 7, 7 + str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        text.setSpan(new ForegroundColorSpan(0xffff6c38), 15 + str1.length(), 15 + str1.length() + str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        grabforeman.setText(text);
    }

    private void initData() {
        HomePageTask task = new HomePageTask(getActivity());
        task.setCallBack(new AbstractHttpResponseHandler<HomePageData>() {
            @Override
            public void onStart() {
                super.onStart();
                mDataLoadingLayout.showDataLoading();
            }

            @Override
            public void onSuccess(HomePageData t) {
                mDataLoadingLayout.showDataLoadSuccess();
                netSuccess(t);
            }

            @Override
            public void onFailure(int code, String error) {
                mDataLoadingLayout.showDataLoadFailed(error);
            }
        });
        task.send();

    }

    private void netSuccess(HomePageData pageData) {
        this.mHomePageData = pageData;
        DisplayImageOptions defaultImageOption = ImageLoaderOptions.getDefaultImageOption();
        if (pageData != null) {
            // 轮播广告
            loadAdvertisementData(pageData.getAd_list());
            // 右侧广告
            if (pageData.getNav_ads() != null) {
                //第一条
                if (pageData.getNav_ads().size() > 0) {
                    ImageUtil.displayImage(getNotNullString(pageData.getNav_ads().get(0).getImage().getThumb_path()), (ImageView) preferential.getChildAt(0), defaultImageOption);
                }
                //第二条
                if (pageData.getNav_ads().size() > 1) {
                    ImageUtil.displayImage(getNotNullString(pageData.getNav_ads().get(1).getImage().getThumb_path()), (ImageView) guarantee.getChildAt(0), defaultImageOption);
                }
            }
            // 精华日记
            if (pageData.getDiary() != null && pageData.getDiary().size() > 0) {
                EssenceDiary diary1 = pageData.getDiary().get(0);
                //第一条内容
                // 头像
                if (diary1.getUser_logo() != null) {
                    ImageUtil.displayImage(getNotNullString(diary1.getUser_logo().getThumb_path()), essencediary_head1, defaultImageOption);
                }
                //用户名
                essencediary_name1.setText(getNotNullString(diary1.getUser_name())); String title = getNotNullString(diary1.getDiary_title());
                if (title.length() > 0) {
                    essencediary_title1.setText(title);
                } else {
                    essencediary_title1.setText("无标题");
                }
                //内容
                essencediary_text1.setText(getNotNullString(diary1.getContent()));
                //第二条内容
                if (pageData.getDiary().size() >= 2) {
                    EssenceDiary diary2 = pageData.getDiary().get(1);
                    // 头像
                    if (diary2.getUser_logo() != null) {
                        ImageUtil.displayImage(getNotNullString(diary2.getUser_logo().getThumb_path()), essencediary_head2, defaultImageOption);
                    }
                    //用户名
                    essencediary_name2.setText(getNotNullString(diary2.getUser_name()));
                    title = getNotNullString(diary2.getDiary_title());
                    if (title.length() > 0) {
                        essencediary_title2.setText(title);
                    } else {
                        essencediary_title2.setText("无标题");
                    }
                    //内容
                    essencediary_text2.setText(getNotNullString(diary2.getContent()));
                } else {
                    //如果没有底二天，隐藏
                    essencediary_line.setVisibility(View.GONE);
                    essencediary_item2.setVisibility(View.GONE);
                }
            } else {
                //如果没有内容 隐藏整个精华日记模块
                essencediary_all_layout.setVisibility(View.GONE);
            }

            // 好工长
            if (pageData.getCase_list() != null && pageData.getCase_list().size() > 0) {
                GoodForemanCase foremanCase1 = pageData.getCase_list().get(0);
                //第一条
                // 头像
                if (foremanCase1.getUser_logo() != null) {
                    ImageUtil.displayImage(getNotNullString(foremanCase1.getUser_logo().getThumb_path()), goodforeman_head1, defaultImageOption);
                }
                //名字
                goodforeman_name1.setText(getNotNullString(foremanCase1.getFull_name()));
                //小区
                goodforeman_community1.setText(getNotNullString(foremanCase1.getHousing_name()));

                //评分
                float score = 0f;
                try {
                    score = Float.valueOf(foremanCase1.getComment_avg());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(score > 0){
	                goodforeman_score1.setProgress((int) (score * 2));
	                goodforeman_score1.setMax(10);
	                goodforeman_score_tv1.setText(new DecimalFormat("0.0").format(((int) (score * 2)) / 2.0f));
	                goodforeman_score_count1.setText(getNotNullString("（" + foremanCase1.getComment_count()) + "人评价）");
	                goodforeman_score_count1.setVisibility(View.VISIBLE);
                }else{
                	goodforeman_score_count1.setVisibility(View.INVISIBLE);
                }
                //图片
                goodforeman_img1_a.setVisibility(View.INVISIBLE);
                goodforeman_img1_b.setVisibility(View.INVISIBLE);
                goodforeman_img1_c.setVisibility(View.INVISIBLE);
                if (foremanCase1.getImages() != null && foremanCase1.getImages().size() > 0) {
                    for (int i = 0; i < foremanCase1.getImages().size(); i++) {
                        Logo img = foremanCase1.getImages().get(i);
                        if (img != null) {
                            ImageView v = null;
                            switch (i) {
                                case 0:
                                    v = goodforeman_img1_a;
                                    break;
                                case 1:
                                    v = goodforeman_img1_b;
                                    break;
                                case 2:
                                    v = goodforeman_img1_c;
                                    break;
                                default:
                                    break;
                            }
                            if (v != null) {
                                v.setVisibility(View.VISIBLE);
                                ImageUtil.displayImage(getNotNullString(img.getThumb_path()), v, defaultImageOption);
                            }
                        }
                    }
                } else {
                    ((View) goodforeman_img1_a.getParent()).setVisibility(View.GONE);
                }
                //第二条
                if (pageData.getCase_list().size() >= 2) {
                    GoodForemanCase foremanCase2 = pageData.getCase_list().get(1);
                    // 头像
                    if (foremanCase2.getUser_logo() != null) {
                        ImageUtil.displayImage(getNotNullString(foremanCase2.getUser_logo().getThumb_path()), goodforeman_head2, defaultImageOption);
                    }
                    //名字
                    goodforeman_name2.setText(getNotNullString(foremanCase2.getFull_name()));
                    //小区
                    goodforeman_community2.setText(getNotNullString(foremanCase2.getHousing_name()));
                    //评分
                    score = 0f;
                    try {
                        score = Float.valueOf(foremanCase2.getComment_avg());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(score > 0){
	                    goodforeman_score2.setProgress((int) (score * 2));
	                    goodforeman_score2.setMax(10);
	                    goodforeman_score_tv2.setText(new DecimalFormat("0.0").format(((int) (score * 2)) / 2.0f));
	                    goodforeman_score_count2.setText(getNotNullString("（" + foremanCase2.getComment_count()) + "人评价）");
	                    goodforeman_score_count2.setVisibility(View.VISIBLE);
                    }else{
                    	goodforeman_score_count2.setVisibility(View.INVISIBLE);
                    }
                    //图片
                    goodforeman_img2_a.setVisibility(View.INVISIBLE);
                    goodforeman_img2_b.setVisibility(View.INVISIBLE);
                    goodforeman_img2_c.setVisibility(View.INVISIBLE);
                    if (foremanCase2.getImages() != null && foremanCase2.getImages().size() > 0) {
                        for (int i = 0; i < foremanCase2.getImages().size(); i++) {
                            Logo img = foremanCase2.getImages().get(i);
                            if (img != null) {
                                ImageView v = null;
                                switch (i) {
                                    case 0:
                                        v = goodforeman_img2_a;
                                        break;
                                    case 1:
                                        v = goodforeman_img2_b;
                                        break;
                                    case 2:
                                        v = goodforeman_img2_c;
                                        break;
                                    default:
                                        break;
                                }
                                if (v != null) {
                                    v.setVisibility(View.VISIBLE);
                                    ImageUtil.displayImage(getNotNullString(img.getThumb_path()), v, defaultImageOption);
                                }
                            }
                        }
                    } else {
                        ((View) goodforeman_img2_a.getParent()).setVisibility(View.GONE);
                    }
                } else {
                    goodforeman_line.setVisibility(View.GONE);
                    goodforeman_item2.setVisibility(View.GONE);
                }
            } else {
                goodforeman_all_layout.setVisibility(View.GONE);
            }
        }
    }

    //轮播广告
    private void loadAdvertisementData(List<Advertise> advList) {
        if (advList != null && advList.size() > 0) {
            // 点击在Adapter中
            mHomePageAdapter = new HomeAdvImageAdapter(getActivity());
            mHomePageAdapter.setList(advList);
            mViewFlow.setTimeSpan(4500);
            mViewFlow.setSelection(advList.size() * 1000);
            mViewFlow.setValidCount(advList.size());
            mViewFlow.setFlowIndicator(circleFlowIndicator);
            mViewFlow.setAdapter(mHomePageAdapter);

            mViewFlow.startAutoFlowTimer();
        }
    }

    private String getNotNullString(String str) {
        if (str == null) {
            str = "";
        }
        return str.trim();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.main_page_grabforeman:
                AnalyticsUtil.onEvent(getActivity(), "click03");
                // 抢工长
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).mainToForeman();
                }
                break;
            case R.id.main_page_preferential:
                // 省
                AnalyticsUtil.onEvent(getActivity(), "click04");
                toNavAds(0);
                break;
            case R.id.main_page_guarantee:
                // 担保
                AnalyticsUtil.onEvent(getActivity(), "click05");
                toNavAds(1);
                break;
            case R.id.main_page_effect:
                // 效果图
                AnalyticsUtil.onEvent(getActivity(), "click06");
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).mainToAtlas();
                }
                break;
            case R.id.main_page_collection:
                // 宝典
                AnalyticsUtil.onEvent(getActivity(), "click07");
                toZxbd();
                break;
            case R.id.main_page_diary:
                AnalyticsUtil.onEvent(getActivity(), "click08");
                // 日记
                toWritDiary();
                break;
            case R.id.main_page_bookkeeping:
                AnalyticsUtil.onEvent(getActivity(), "click09");
                // 记账
                toBill();
                break;
            case R.id.main_page_free_layout:
                // 免费装修
                break;
            case R.id.main_page_essencediary_item1:
                // 精华日记1
                break;
            case R.id.main_page_essencediary_item2:
                // 精华日记2
                break;
            case R.id.main_page_goodforeman_item1:
                // 惠装好工长1
                toForemanDetails(0);
                break;
            case R.id.main_page_goodforeman_item2:
                // 惠装好工长2
                toForemanDetails(1);
                break;
            case R.id.main_page_essencediary_more:
                // 精华日记更多
                AnalyticsUtil.onEvent(getActivity(), "click10");
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).mainToDiary();
                }
                break;
            case R.id.main_page_goodforeman_more:
                // 惠装好工长更多
                AnalyticsUtil.onEvent(getActivity(), "click20");
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).mainToForeman();
                }
                break;
            case R.id.main_page_essencediary_item1_person:
                toPersonal(0);
                break;
            case R.id.main_page_essencediary_item2_person:
                toPersonal(1);
                break;
            case R.id.main_page_essencediary_item1_diary:
                AnalyticsUtil.onEvent(getActivity(), "click11");
                toEssenceDiary(0);
                break;
            case R.id.main_page_essencediary_item2_diary:
                AnalyticsUtil.onEvent(getActivity(), "click11");
                toEssenceDiary(1);
                break;
            case R.id.main_page_goodforeman_img1_a:
                AnalyticsUtil.onEvent(getActivity(), "click12");
                toShowImage(0, 0);
                break;
            case R.id.main_page_goodforeman_img1_b:
                AnalyticsUtil.onEvent(getActivity(), "click12");
                toShowImage(0, 1);
                break;
            case R.id.main_page_goodforeman_img1_c:
                AnalyticsUtil.onEvent(getActivity(), "click12");
                toShowImage(0, 2);
                break;
            case R.id.main_page_goodforeman_img2_a:
                AnalyticsUtil.onEvent(getActivity(), "click12");
                toShowImage(1, 0);
                break;
            case R.id.main_page_goodforeman_img2_b:
                AnalyticsUtil.onEvent(getActivity(), "click12");
                toShowImage(1, 1);
                break;
            case R.id.main_page_goodforeman_img2_c:
                AnalyticsUtil.onEvent(getActivity(), "click12");
                toShowImage(1, 2);
                break;
            default:
                break;
        }
    }

    //抢工长右侧广告点击
    private void toNavAds(int position) {
        AnalyticsUtil.onEvent(getActivity(), "click02");
        if (mHomePageData.getNav_ads() != null && mHomePageData.getNav_ads().size() > position) {
            if (mHomePageData.getNav_ads().get(position) != null) {
                String url = mHomePageData.getNav_ads().get(position).getUrl();
                if (url != null && url.trim().length() > 0) {
                    try {
                        toBrowser(url);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    showToastMsg("url地址错误！");
                    return;
                }
            }
        }
        toSupportService();
    }

    //打开系统浏览器
    private void toBrowser(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    //跳转到个人信息
    private void toPersonal(int position) {
        if (mHomePageData != null && mHomePageData.getDiary() != null && mHomePageData.getDiary().size() > position) {
            EssenceDiary diary = mHomePageData.getDiary().get(position);
            // 查看个人主页
            Visitor visitor = new Visitor();
            visitor.setId(diary.getUser_id());
            visitor.setName(diary.getUser_name());
            Bundle bd = new Bundle();
            bd.putSerializable(AppConstants.PARAM_VISOTOR, visitor);
            ActivityUtil.next(getActivity(), PersonalHomepageActivity.class, bd, -1);
        }
    }

    //跳转到日记详情
    private void toEssenceDiary(int position) {
        if (mHomePageData != null && mHomePageData.getDiary() != null && mHomePageData.getDiary().size() > position) {
            EssenceDiary diaryE = mHomePageData.getDiary().get(position);
            int id = 0;
            try {
                id = Integer.valueOf(diaryE.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Author author = new Author();
            author.setId(diaryE.getUser_id());
            author.setName(diaryE.getUser_name());
            if (diaryE.getUser_logo() != null) {
                author.setAvatar(diaryE.getUser_logo().getThumb_path());
            }
            Diary diary = new Diary();
            diary.setId(id);
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConstants.PARAM_AUTHOR, author);
            bundle.putSerializable(AppConstants.PARAM_DIARY, diary);
            ActivityUtil.next(getActivity(), DiaryDetailActivity.class, bundle, -1);
        }
    }

    //进入写日记流程
    private void toWritDiary() {
        AnalyticsUtil.onEvent(getActivity(), AppConstants.UmengEvent.ID_DIARY_EDIT_FROM_LIST);
        if (ZxsqApplication.getInstance().isLogged()) {
            showTakePhotoDialog();
        } else {
            Intent intent = new Intent(getActivity(), UserLoginActivity.class);
            startActivityForResult(intent, REQ_LOGIN_FOR_DIARY_CODE);
        }
    }

    //跳转到记账
    private void toBill() {
        if (ZxsqApplication.getInstance().isLogged()) {
            ActivityUtil.next(getActivity(), BillMainActivity.class);
        } else {
            Intent intent = new Intent(getActivity(), UserLoginActivity.class);
            startActivityForResult(intent, REQ_LOGIN_FOR_ZXJZ_CODE);
        }
    }

    //跳转到装修宝典
    private void toZxbd() {
        ActivityUtil.next(getActivity(), ZxbdIntroActivity.class, null, -1);
    }

    //跳转到服务保障页
    private void toSupportService() {
        Intent intent = new Intent(getActivity(), SupportServicesActivity.class);
        startActivityForResult(intent, SUPPORTSERVICES);
    }

    //跳转到找工长详情
    private void toForemanDetails(int position) {
        if (mHomePageData != null && mHomePageData.getCase_list() != null && mHomePageData.getCase_list().size() > position) {
            Bundle bundle = new Bundle();
            bundle.putString("foreman_id", mHomePageData.getCase_list().get(position).getStore_id());
            ActivityUtil.next(getActivity(), ForemanDetailsActivity.class, bundle, -1);
        }
    }

    //跳转到城市列表
    private void toCityList() {
        String city = ZxsqApplication.getInstance().getLocationCity();
        if (city != null) {
            city = city.replace("市", "");
        }
        Intent intent = new Intent(getActivity(), CompanyCityList.class);
        intent.putExtra("myCity", city);
        startActivityForResult(intent, CITY_LIST);
    }

    //准备跳转到图片展示页面
    private void toShowImage(int i, int position) {
        if (mHomePageData != null && mHomePageData.getCase_list() != null && mHomePageData.getCase_list().size() > i) {
            Intent intent = new Intent(getActivity(), ForemanConstructionSiteActivity.class);
            intent.putExtra("showcase_id", mHomePageData.getCase_list().get(i).getId());
            intent.putExtra("foreman_id", mHomePageData.getCase_list().get(i).getStore_id());
            startActivity(intent);
//            if (mHomePageData.getCase_list().get(i).getImages() != null && mHomePageData.getCase_list().get(i).getImages().size() > position) {
//                List<String> imgs = new ArrayList<String>();
//                for (int j = 0; j < mHomePageData.getCase_list().get(i).getImages().size(); j++) {
//                    Logo img = mHomePageData.getCase_list().get(i).getImages().get(j);
//                    imgs.add(img.getImg_path());
//                }
//                showImage(imgs, position);
//            }
        }
    }

    //跳转到图片展示页面
    private void showImage(List<String> images, int position) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) images);
        bundle.putInt(ImageSimpleBrowseActivity.EXTRA_POSITION, position);
        ActivityUtil.next(getActivity(), ImageSimpleBrowseActivity.class, bundle, -1);
    }

    /**
     * 显示选择图片对话框
     */
    private void showTakePhotoDialog() {
        String path = Util.createImagePath(getActivity());
        if (path == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), ImageSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("crop", false);
        bundle.putString("image-path", path);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQ_SEARCH_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewFlow != null && mHomePageAdapter != null) {
            mViewFlow.startAutoFlowTimer(); // 启动自动播放
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mViewFlow != null) {
            mViewFlow.stopAutoFlowTimer();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SUPPORTSERVICES == requestCode && resultCode == Activity.RESULT_OK) {
            if (getActivity() instanceof MainActivity) {
                //切换到找工长tab
                ((MainActivity) getActivity()).mainToForeman();
            }
        } else if (CITY_LIST == requestCode && resultCode == Activity.RESULT_OK && data != null) {
            //切换城市
            String city = data.getStringExtra("city");
            ZxsqApplication.getInstance().setSelectCity(city);
            if (city == null) {
                city = ZxsqApplication.getInstance().getLocationCity();
                if (city != null) {
                    city = city.replace("市", "");
                } else {
                    city = getResources().getString(R.string.txt_city);
                }
            }
            mCommonActionBar.setLeftBtn(R.drawable.home_page_title_location, city, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCityList();
                }
            });
//            updateHomeData();
            updateFilterCityData();
        } else if (requestCode == REQ_SEARCH_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getParcelableExtra("image-path-uri");
                DiaryEditActivity.show(getActivity(), imageUri);
            }
        } else if (requestCode == REQ_LOGIN_FOR_DIARY_CODE && resultCode == Activity.RESULT_OK) {
            showTakePhotoDialog();
        } else if (requestCode == REQ_LOGIN_FOR_ZXJZ_CODE && resultCode == Activity.RESULT_OK) {
            if (ZxsqApplication.getInstance().isLogged()) {
                ActivityUtil.next(getActivity(), BillMainActivity.class);
            }
        }
    }

    private void updateHomeData() {
        initData();
    }

    //在选择城市后， 主动去刷新找工长列表页面 的附近筛选内容
    private void updateFilterCityData() {
        // 选择地区数据设置
        ZxsqApplication application = ZxsqApplication.getInstance();
        //判断是否有选择的城市
        String city = application.getSelectCity();
        if (city == null) {
            city = application.getLocationCity();
            if (city != null) {
                city = city.replace("市", "");
            }
        }
        //获取选择的城市的 下级区县
        List<CityDistrict> districts = application.getDistricts(city);
        //获取选择的城市
        CityDistrict cDistrict = application.getCity(city);
        int[] area_id;
        if (districts != null) {
            //刷新存储的数据
            ForemanListFilterBar.FILTER_VALUES[0] = new String[districts.size() + 1];
            area_id = new int[districts.size() + 1];
            for (int i = 0; i < districts.size(); i++) {
                ForemanListFilterBar.FILTER_VALUES[0][i + 1] = districts.get(i).getName();
                area_id[i + 1] = districts.get(i).getId();
            }
        } else {
            ForemanListFilterBar.FILTER_VALUES[0] = new String[1];
            area_id = new int[1];
        }
        //设置首个数据
        if (application.getSelectCity() == null) {
            ForemanListFilterBar.FILTER_VALUES[0][0] = "当前位置：" + application.getLocationDistrict();
            area_id[0] = -1;
        } else {
            ForemanListFilterBar.FILTER_VALUES[0][0] = "当前城市：" + city;
            if (cDistrict == null) {
                area_id[0] = -1;
            } else {
                area_id[0] = cDistrict.getId();
            }
        }
        //刷新数据
        if (ForemanListFilterBar.filterBar != null)
            ForemanListFilterBar.filterBar.initData();
    }

}
