package com.huizhuang.zxsq.ui.activity.foreman;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.Logo;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.bean.foreman.ConstructionCase;
import com.huizhuang.zxsq.http.bean.foreman.ConstructionSiteDetails;
import com.huizhuang.zxsq.http.task.foreman.ConstructionSiteDetailsTask;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.ui.activity.ImageSimpleBrowseActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.order.OrderCheckPhoneActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 施工现成详情页
 */
public class ForemanConstructionSiteActivity extends BaseActivity implements
        OnItemClickListener, OnClickListener {

    // 装修阶段线条颜色值
    public static final int STAGE_LINE_COLOR_ACTIVATED = 0xffffd2c3,
            STAGE_LINE_COLOR_NOT_ACTIVATED = 0xffd1d1d1;

    private CommonActionBar mCommonActionBar;

    private XListView mXListView;

    protected MyAdapter myAdapter;

    // 图片
    protected ImageView foreman_constructionsite_topimg;
    // 免费申请参观
    protected TextView foreman_constructionsite_freevisit;
    // 标题
    protected TextView foreman_constructionsite_title;
    // 小区
    protected TextView foreman_constructionsite_community;
    // 面积
    protected TextView foreman_constructionsite_measure;
    // 造价
    protected TextView foreman_constructionsite_cost;
    // 风格
    protected TextView foreman_constructionsite_style;
    // 户型
    protected TextView foreman_constructionsite_housetype;
    // 工期
    protected TextView foreman_constructionsite_projecttime;
    protected TextView foreman_constructionsite_order_count;

    // 开工,水电,泥木,油漆,竣工 线
    protected ImageView foreman_constructionsite_start_line,
            foreman_constructionsite_hydropower_line,
            foreman_constructionsite_mudwood_line,
            foreman_constructionsite_paint_line,
            foreman_constructionsite_completed_line;

    // 开工,水电,泥木,油漆,竣工
    protected TextView foreman_constructionsite_start,
            foreman_constructionsite_hydropower,
            foreman_constructionsite_mudwood, foreman_constructionsite_paint,
            foreman_constructionsite_completed, foreman_constructionsite_real;

    private View foreman_constructions_header;

    private View header;

    protected String showcase_id;
    private String foreman_id = null;

    private String style;

    @SuppressWarnings("unused")
    private ConstructionSiteDetails details;

    private List<ConstructionCase> subcases = new ArrayList<ConstructionCase>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreman_constructionsite);
        getIntentExtra();
        initActionBar();
        initViews();
        initData();
        showWaitDialog("正在加载");
    }

    private void getIntentExtra() {
        showcase_id = getIntent().getStringExtra("showcase_id");
        foreman_id = getIntent().getStringExtra("foreman_id");
    }

    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("施工现场详情");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        mCommonActionBar.setRightImgBtn(R.drawable.foreman_details_share,
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AnalyticsUtil.onEvent(ForemanConstructionSiteActivity.this, "click27");
                        // 分享
                        Share share = new Share();
                        String text = "我在用#"+getResources().getString(R.string.app_name)+"APP#远程查看装修施工现场，不用守在工地就能看到施工进度啦！装修的人都在用这个软件，比传统装修节省40%的费用！强烈推荐你试试~";
                        share.setText(text);
                        Util.showShare(false, ForemanConstructionSiteActivity.this, share);
                    }
                });
    }

    private void initViews() {
        foreman_constructions_header = findViewById(R.id.foreman_constructions_header);

        mXListView = (XListView) findViewById(R.id.xlist);

        mXListView.setOnItemClickListener(this);
        mXListView.setAdapter(myAdapter = new MyAdapter());
        mXListView.setPullRefreshEnable(true);
        mXListView.setPullLoadEnable(false);
        mXListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        mXListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 1) {
                    foreman_constructions_header.setVisibility(View.VISIBLE);
                } else {
                    foreman_constructions_header.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {
        ConstructionSiteDetailsTask task = new ConstructionSiteDetailsTask(
                this, showcase_id);
        task.setCallBack(new AbstractHttpResponseHandler<ConstructionSiteDetails>() {

            @Override
            public void onSuccess(ConstructionSiteDetails t) {
                hideWaitDialog();
                mXListView.onRefreshComplete();
                if (header == null)
                    mXListView.addHeaderView(header = getListViewHeader());
                setData(t);
            }

            @Override
            public void onFailure(int code, String error) {
                hideWaitDialog();
                mXListView.onRefreshComplete();
            }
        });
        task.send();
    }

    protected void setData(ConstructionSiteDetails details) {
        this.details = details;
        // 头像
        if (details.getImage() != null) {
            ImageUtil.displayImage(getNotNullString(details.getImage()
                            .getThumb_path()), foreman_constructionsite_topimg,
                    ImageLoaderOptions.getDefaultImageOption());
        }
        foreman_constructionsite_title.setText(getNotNullString(details.getName()));
//		mCommonActionBar.setActionBarTitle(getNotNullString(details.getName()));
        String community = getNotNullString(details.getHousing_name());
        if (community.length() > 0) {
            foreman_constructionsite_community.setText(community);
        } else {
            foreman_constructionsite_community.setVisibility(View.GONE);
        }

        // 面积
        String size = getNotNullString(details.getSize());
        if (size.trim().length() == 0) {
            foreman_constructionsite_measure.setText("");
        } else {
            foreman_constructionsite_measure.setText(size + "㎡ ");
        }
        // 造价
        String coset = details.getCost() == 0 ? "" : details.getCost() + "万";

        if (details.getRenovation_way() != null) {
            if (details.getRenovation_way().equals("1")) {
                coset = coset + "/" + "半包";
            } else if (details.getRenovation_way().equals("1")) {
                coset = coset + "/" + "全包";
            }
        }

        foreman_constructionsite_cost.setText(coset);
        style = getNotNullString(details.getRoom_style());
        foreman_constructionsite_style.setText(style);

        foreman_constructionsite_housetype.setText(getNotNullString(details.getDoor_model()));

        foreman_constructionsite_projecttime.setText(details.getData().getTimeSlot() + "天");

        if (details.getOrder_count()>0){
            foreman_constructionsite_order_count.setText(details.getOrder_count() + "位业主预约");
        }else{
            foreman_constructionsite_order_count.setVisibility(View.GONE);
        }

        setDataHeader();

        if (details.getSubcases() != null && details.getSubcases().size() > 0) {
            subcases = details.getSubcases();
            myAdapter.notifyDataSetChanged();
        }

        // 阶段
        int stage = details.getPhrase();

        Drawable start = getResources().getDrawable(R.drawable.foreman_constructionsite_stage_start);
        Drawable hydropower = getResources().getDrawable(R.drawable.foreman_constructionsite_stage_hydropower);
        Drawable mudwood = getResources().getDrawable(R.drawable.foreman_constructionsite_stage_mudwood);
        Drawable paint = getResources().getDrawable(R.drawable.foreman_constructionsite_stage_paint);
        Drawable completed = getResources().getDrawable(R.drawable.foreman_constructionsite_stage_completed);
        Drawable stage_real = getResources().getDrawable(R.drawable.foreman_constructionsite_stage_real);

        switch (stage) {
            case 6:
                foreman_constructionsite_real.setCompoundDrawablesWithIntrinsicBounds(null, stage_real, null, null);
            case 5:
            	foreman_constructionsite_completed.setCompoundDrawablesWithIntrinsicBounds(null, completed, null, null);
            	foreman_constructionsite_paint_line.setBackgroundColor(STAGE_LINE_COLOR_ACTIVATED);
            case 4:
            	foreman_constructionsite_paint.setCompoundDrawablesWithIntrinsicBounds(null, paint, null, null);
            	foreman_constructionsite_mudwood_line.setBackgroundColor(STAGE_LINE_COLOR_ACTIVATED);
            case 3:
            	foreman_constructionsite_mudwood.setCompoundDrawablesWithIntrinsicBounds(null, mudwood, null, null);
            	foreman_constructionsite_hydropower_line.setBackgroundColor(STAGE_LINE_COLOR_ACTIVATED);
            case 2:
            	foreman_constructionsite_hydropower.setCompoundDrawablesWithIntrinsicBounds(null, hydropower, null, null);
            	foreman_constructionsite_start_line.setBackgroundColor(STAGE_LINE_COLOR_ACTIVATED);
            case 1:
            	foreman_constructionsite_start.setCompoundDrawablesWithIntrinsicBounds(null, start, null, null);
            default:
                break;
        }

    }

    public void setDataHeader() {


        foreman_constructions_header.findViewById(R.id.foreman_constructionsite_freevisit).setOnClickListener(this);

        TextView tv_community = (TextView) foreman_constructions_header.findViewById(R.id.foreman_constructionsite_community);
        TextView tv_measure = (TextView) foreman_constructions_header.findViewById(R.id.foreman_constructionsite_measure);
        TextView tv_style = (TextView) foreman_constructions_header.findViewById(R.id.foreman_constructionsite_style);
        TextView tv_cost = (TextView) foreman_constructions_header.findViewById(R.id.foreman_constructionsite_cost);
        TextView tv_housetype = (TextView) foreman_constructions_header.findViewById(R.id.foreman_constructionsite_housetype);
        TextView tv_projecttime = (TextView) foreman_constructions_header.findViewById(R.id.foreman_constructionsite_projecttime);
        TextView tv_order_count = (TextView) foreman_constructions_header.findViewById(R.id.foreman_constructionsite_order_count);

        String community = getNotNullString(details.getHousing_name());
        if (community.length() > 0) {
            tv_community.setText(community);
        } else {
            tv_community.setVisibility(View.GONE);
        }

        // 面积
        String size = getNotNullString(details.getSize());
        if (size.trim().length() == 0) {
            tv_measure.setText("");
        } else {
            tv_measure.setText(size + "㎡ ");
        }
        // 造价
        String coset = details.getCost() == 0 ? "" : details.getCost() + "万";

        if (details.getRenovation_way() != null) {
            if (details.getRenovation_way().equals("1")) {
                coset = coset + "/" + "半包";
            } else if (details.getRenovation_way().equals("1")) {
                coset = coset + "/" + "全包";
            }
        }

        tv_cost.setText(coset);
        style = getNotNullString(details.getRoom_style());
        tv_style.setText(style);

        tv_housetype.setText(getNotNullString(details.getDoor_model()));

        tv_projecttime.setText(details.getData().getTimeSlot() + "天");

        if (details.getOrder_count()>0){
            tv_order_count.setText(details.getOrder_count() + "位业主预约");
        }else{
            tv_order_count.setVisibility(View.GONE);
        }

    }


    private String getNotNullString(String str) {
        if (str == null) {
            str = "";
        }
        return str.trim();
    }

    private View getListViewHeader() {
        View view = View.inflate(this,
                R.layout.activity_foreman_constructionsite_header, null);

        foreman_constructionsite_topimg = (ImageView) view
                .findViewById(R.id.foreman_constructionsite_topimg);
        foreman_constructionsite_freevisit = (TextView) view
                .findViewById(R.id.foreman_constructionsite_freevisit);

        foreman_constructionsite_title = (TextView) view
                .findViewById(R.id.foreman_constructionsite_title);
        foreman_constructionsite_community = (TextView) view
                .findViewById(R.id.foreman_constructionsite_community);
        foreman_constructionsite_measure = (TextView) view
                .findViewById(R.id.foreman_constructionsite_measure);
        foreman_constructionsite_style = (TextView) view
                .findViewById(R.id.foreman_constructionsite_style);
        foreman_constructionsite_cost = (TextView) view
                .findViewById(R.id.foreman_constructionsite_cost);
        foreman_constructionsite_housetype = (TextView) view
                .findViewById(R.id.foreman_constructionsite_housetype);
        foreman_constructionsite_projecttime = (TextView) view
                .findViewById(R.id.foreman_constructionsite_projecttime);

        foreman_constructionsite_start_line = (ImageView) view
                .findViewById(R.id.foreman_constructionsite_start_line);
        foreman_constructionsite_hydropower_line = (ImageView) view
                .findViewById(R.id.foreman_constructionsite_hydropower_line);
        foreman_constructionsite_mudwood_line = (ImageView) view
                .findViewById(R.id.foreman_constructionsite_mudwood_line);
        foreman_constructionsite_paint_line = (ImageView) view
                .findViewById(R.id.foreman_constructionsite_paint_line);

        foreman_constructionsite_start = (TextView) view
                .findViewById(R.id.foreman_constructionsite_start);
        foreman_constructionsite_hydropower = (TextView) view
                .findViewById(R.id.foreman_constructionsite_hydropower);
        foreman_constructionsite_mudwood = (TextView) view
                .findViewById(R.id.foreman_constructionsite_mudwood);
        foreman_constructionsite_paint = (TextView) view
                .findViewById(R.id.foreman_constructionsite_paint);
        foreman_constructionsite_completed = (TextView) view
        		.findViewById(R.id.foreman_constructionsite_completed);
        foreman_constructionsite_real = (TextView) view
                .findViewById(R.id.foreman_constructionsite_real);
        foreman_constructionsite_order_count = (TextView) view
                .findViewById(R.id.foreman_constructionsite_order_count);

        foreman_constructionsite_freevisit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.foreman_constructionsite_freevisit:
                construction();
                break;

            default:
                break;
        }
    }

    //进入下单流程
    private void construction() {
        AnalyticsUtil.onEvent(this, "click28");
        int id = 0;
        try {
            id = Integer.valueOf(foreman_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.PARAM_ORDER_TYPE, Order.ORDER_TYPE_FOREMAN);
        bundle.putInt(AppConstants.PARAM_ORDER_COMPANY_ID, id);
        bundle.putString(AppConstants.PARAM_ORDER_CASE_ID, details.getId());
        bundle.putString(AppConstants.PARAM_ORDER_SOURCE_NAME, "app_showcase_detail");
        ActivityUtil.next(this, OrderCheckPhoneActivity.class, bundle, -1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view,
                            int position, long l) {

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return subcases.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(
                        ForemanConstructionSiteActivity.this,
                        R.layout.item_foreman_constructioncase, null);

                ItemViewHolder holder = new ItemViewHolder();

                holder.item_foreman_constructioncase_line = (ImageView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_line);
                holder.item_foreman_constructioncase_icon = (ImageView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_icon);

                holder.item_foreman_constructioncase_stage = (TextView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_stage);
                holder.item_foreman_constructioncase_style = (TextView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_style);
                holder.item_foreman_constructioncase_description = (TextView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_description);
                holder.item_foreman_constructioncase_datetime = (TextView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_datetime);

                holder.item_foreman_constructioncase_img1 = (ImageView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_img1);
                holder.item_foreman_constructioncase_img2 = (ImageView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_img2);
                holder.item_foreman_constructioncase_img3 = (ImageView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_img3);
                holder.item_foreman_constructioncase_img4 = (ImageView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_img4);
                holder.item_foreman_constructioncase_img5 = (ImageView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_img5);
                holder.item_foreman_constructioncase_img6 = (ImageView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_img6);
                holder.item_foreman_constructioncase_img7 = (ImageView) convertView
                        .findViewById(R.id.item_foreman_constructioncase_img7);

                holder.item_foreman_constructioncase_img1
                        .setOnClickListener(clickListener);
                holder.item_foreman_constructioncase_img2
                        .setOnClickListener(clickListener);
                holder.item_foreman_constructioncase_img3
                        .setOnClickListener(clickListener);
                holder.item_foreman_constructioncase_img4
                        .setOnClickListener(clickListener);
                holder.item_foreman_constructioncase_img5
                        .setOnClickListener(clickListener);
                holder.item_foreman_constructioncase_img6
                        .setOnClickListener(clickListener);
                holder.item_foreman_constructioncase_img7
                        .setOnClickListener(clickListener);
                convertView.setTag(holder);
            }

            ItemViewHolder holder = (ItemViewHolder) convertView.getTag();

            if (position == subcases.size() - 1) {
                holder.item_foreman_constructioncase_line
                        .setVisibility(View.INVISIBLE);
            } else {
                holder.item_foreman_constructioncase_line
                        .setVisibility(View.VISIBLE);
            }

            ConstructionCase constructionCase = subcases.get(position);

            int stage = 5;
            if (constructionCase.getZx_code() != null) {
                try {
                    stage = Integer.valueOf(constructionCase.getZx_code());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            String stageStr;
            switch (stage) {
            case 1:
                stage = R.drawable.foreman_constructionsite_stage_start_big;
                stageStr = "开工阶段";
            	break;
                case 2:
                    stage = R.drawable.foreman_constructionsite_stage_hydropower_big;
                    stageStr = "水电阶段";
                    break;
                case 3:
                    stage = R.drawable.foreman_constructionsite_stage_mudwood_big;
                    stageStr = "泥木阶段";
                    break;
                case 4:
                    stage = R.drawable.foreman_constructionsite_stage_paint_big;
                    stageStr = "油漆阶段";
                    break;
                case 5:
                    stage = R.drawable.foreman_constructionsite_stage_completed_big;
                    stageStr = "竣工阶段";
                    break;
                case 6:
                    stage = R.drawable.foreman_constructionsite_stage_real_big;
                    stageStr = "实景阶段";
                    break;
                default:
                    stage = R.drawable.foreman_constructionsite_stage_start_big;
                    stageStr = "开工阶段";
                    break;
            }
            holder.item_foreman_constructioncase_icon.setImageResource(stage);
            holder.item_foreman_constructioncase_stage.setText(stageStr);
            if (style == null || style.length() == 0) {
                holder.item_foreman_constructioncase_style
                        .setVisibility(View.GONE);
            } else {
                holder.item_foreman_constructioncase_style
                        .setVisibility(View.VISIBLE);
                holder.item_foreman_constructioncase_style.setText(style);
            }

            holder.item_foreman_constructioncase_description
                    .setText(getNotNullString(constructionCase.getDesc()));
            holder.item_foreman_constructioncase_datetime
                    .setText(DateUtil.timestampToSdate(constructionCase.getAdd_time(), "MM/dd HH:mm"));

            holder.item_foreman_constructioncase_img1.setVisibility(View.GONE);
            holder.item_foreman_constructioncase_img2.setVisibility(View.GONE);
            holder.item_foreman_constructioncase_img3.setVisibility(View.GONE);
            holder.item_foreman_constructioncase_img4.setVisibility(View.GONE);
            holder.item_foreman_constructioncase_img5.setVisibility(View.GONE);
            holder.item_foreman_constructioncase_img6.setVisibility(View.GONE);
            holder.item_foreman_constructioncase_img7.setVisibility(View.GONE);
            holder.item_foreman_constructioncase_img1.setTag(constructionCase
                    .getImages());
            holder.item_foreman_constructioncase_img2.setTag(constructionCase
                    .getImages());
            holder.item_foreman_constructioncase_img3.setTag(constructionCase
                    .getImages());
            holder.item_foreman_constructioncase_img4.setTag(constructionCase
                    .getImages());
            holder.item_foreman_constructioncase_img5.setTag(constructionCase
                    .getImages());
            holder.item_foreman_constructioncase_img6.setTag(constructionCase
                    .getImages());
            holder.item_foreman_constructioncase_img7.setTag(constructionCase
                    .getImages());
            if (constructionCase.getImages() != null
                    && constructionCase.getImages().size() > 0) {
                List<Logo> imgs = constructionCase.getImages();
                if (imgs.size() == 1) {
                    holder.item_foreman_constructioncase_img7
                            .setVisibility(View.VISIBLE);
                    ImageUtil.displayImage(getNotNullString(imgs.get(0)
                                    .getThumb_path()),
                            holder.item_foreman_constructioncase_img7,
                            ImageLoaderOptions.getDefaultImageOption());
                } else if (imgs.size() == 4) {
                    holder.item_foreman_constructioncase_img1
                            .setVisibility(View.VISIBLE);
                    holder.item_foreman_constructioncase_img2
                            .setVisibility(View.VISIBLE);
                    holder.item_foreman_constructioncase_img4
                            .setVisibility(View.VISIBLE);
                    holder.item_foreman_constructioncase_img5
                            .setVisibility(View.VISIBLE);
                    ImageUtil.displayImage(getNotNullString(imgs.get(0)
                                    .getThumb_path()),
                            holder.item_foreman_constructioncase_img1,
                            ImageLoaderOptions.getDefaultImageOption());
                    ImageUtil.displayImage(getNotNullString(imgs.get(1)
                                    .getThumb_path()),
                            holder.item_foreman_constructioncase_img2,
                            ImageLoaderOptions.getDefaultImageOption());
                    ImageUtil.displayImage(getNotNullString(imgs.get(2)
                                    .getThumb_path()),
                            holder.item_foreman_constructioncase_img4,
                            ImageLoaderOptions.getDefaultImageOption());
                    ImageUtil.displayImage(getNotNullString(imgs.get(3)
                                    .getThumb_path()),
                            holder.item_foreman_constructioncase_img5,
                            ImageLoaderOptions.getDefaultImageOption());
                } else {
                    for (int i = 0; i < imgs.size(); i++) {
                        ImageView iv = null;
                        if (i == 0) {
                            iv = holder.item_foreman_constructioncase_img1;
                        }
                        if (i == 1) {
                            iv = holder.item_foreman_constructioncase_img2;
                        }
                        if (i == 2) {
                            iv = holder.item_foreman_constructioncase_img3;
                        }
                        if (i == 3) {
                            iv = holder.item_foreman_constructioncase_img4;
                        }
                        if (i == 4) {
                            iv = holder.item_foreman_constructioncase_img5;
                        }
                        if (i == 5) {
                            iv = holder.item_foreman_constructioncase_img6;
                        }
                        iv.setVisibility(View.VISIBLE);
                        ImageUtil.displayImage(getNotNullString(imgs.get(i)
                                .getThumb_path()), iv, ImageLoaderOptions
                                .getDefaultImageOption());
                    }
                }
            }

            return convertView;
        }

        class ItemViewHolder {
            public ImageView item_foreman_constructioncase_line;
            public ImageView item_foreman_constructioncase_icon;

            public TextView item_foreman_constructioncase_stage;
            public TextView item_foreman_constructioncase_style;

            public TextView item_foreman_constructioncase_description;

            public TextView item_foreman_constructioncase_datetime;

            public ImageView item_foreman_constructioncase_img1;
            public ImageView item_foreman_constructioncase_img2;
            public ImageView item_foreman_constructioncase_img3;
            public ImageView item_foreman_constructioncase_img4;
            public ImageView item_foreman_constructioncase_img5;
            public ImageView item_foreman_constructioncase_img6;
            public ImageView item_foreman_constructioncase_img7;

        }
    }

    //点击图片，进入图片查看页
    private OnClickListener clickListener = new OnClickListener() {

        @SuppressWarnings({"unchecked"})
        @Override
        public void onClick(View v) {
            List<Logo> logos = (List<Logo>) v.getTag();
            List<String> imgs = new ArrayList<String>();
            boolean isFour = false;
            if (logos == null) {
                return;
            } else {
                isFour = logos.size() == 4;
                for (int i = 0; i < logos.size(); i++) {
                    if (logos.get(i) != null) {
                        imgs.add(logos.get(i).getImg_path());
                    }
                }
            }
            switch (v.getId()) {
                case R.id.item_foreman_constructioncase_img1:
                    showImage(imgs, 0);
                    break;
                case R.id.item_foreman_constructioncase_img2:
                    showImage(imgs, 1);
                    break;
                case R.id.item_foreman_constructioncase_img3:
                    showImage(imgs, 2);
                    break;
                case R.id.item_foreman_constructioncase_img4:
                    if (isFour) {
                        showImage(imgs, 2);
                    } else {
                        showImage(imgs, 3);
                    }

                    break;
                case R.id.item_foreman_constructioncase_img5:
                    if (isFour) {
                        showImage(imgs, 3);
                    } else {
                        showImage(imgs, 4);
                    }
                    break;
                case R.id.item_foreman_constructioncase_img6:
                    showImage(imgs, 5);
                    break;
                case R.id.item_foreman_constructioncase_img7:
                    showImage(imgs, 0);
                    break;
                default:
                    break;
            }
        }
    };

    //查看图片
    private void showImage(List<String> images, int position) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS,
                (ArrayList<String>) images);
        bundle.putInt(ImageSimpleBrowseActivity.EXTRA_POSITION, position);
        ActivityUtil.next(this, ImageSimpleBrowseActivity.class, bundle, -1);
    }

}
