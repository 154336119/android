package com.huizhuang.zxsq.ui.activity.solution;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.XListRefreshType;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.common.Image;
import com.huizhuang.zxsq.http.bean.foreman.ConstructionCase;
import com.huizhuang.zxsq.http.bean.solution.ConstructionSite;
import com.huizhuang.zxsq.http.bean.solution.ConstructionSiteList;
import com.huizhuang.zxsq.http.bean.solution.SolutionDetail;
import com.huizhuang.zxsq.http.task.solution.ConstructionSiteTask;
import com.huizhuang.zxsq.http.task.solution.SolutionDetailTask;
import com.huizhuang.zxsq.ui.activity.ImageSimpleBrowseActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.foreman.ForemanDetailsActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.DistanceUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 施工现成详情页
 */
public class SolutionDetailActivity extends BaseActivity
        implements
            OnItemClickListener,
            OnClickListener {

    public static final String PARAME_SHOW_CASE_ID_KEY = "showcase_id";
    // 装修阶段线条颜色值
    public static final int STAGE_LINE_COLOR_ACTIVATED = 0xffffd2c3,
            STAGE_LINE_COLOR_NOT_ACTIVATED = 0xffd1d1d1;

    private CommonActionBar mCommonActionBar;

    private XListView mXListView;

    protected MyAdapter myAdapter;

    // 标题
    protected TextView foreman_constructionsite_title;
    // 小区距离
    protected TextView foreman_constructionsite_distance;
    // 面积和风格，居室
    protected TextView foreman_constructionsite_style_area;
    // 施工工期
    protected TextView foreman_constructionsite_duration;
    // 价格
    protected TextView foreman_constructionsite_price;
    
    // 开工,水电,泥木,油漆,竣工 线
    protected ImageView foreman_constructionsite_start_line,
            foreman_constructionsite_hydropower_line, foreman_constructionsite_mudwood_line,
            foreman_constructionsite_paint_line, foreman_constructionsite_complete_line;

    // 开工,水电,泥木,油漆,竣工 图标
    protected TextView foreman_constructionsite_start, foreman_constructionsite_hydropower,
            foreman_constructionsite_mudwood, foreman_constructionsite_paint,
            foreman_constructionsite_completed, foreman_constructionsite_real;

    private View header;
    protected String showcase_id;
    private String store_id;
    private String store_name;
	private TextView foreman_constructionsite_foreman_name;
	private RelativeLayout foreman_constructionsite_foreman_detail;
    
    private List<ConstructionCase> subcases = new ArrayList<ConstructionCase>();
    private List<ConstructionSite> mSubcaseList;
    private boolean mIsHead= false;
    private boolean mIsBottom= false;

    @SuppressWarnings("unused")
    private SolutionDetail details;
	public LatLng mMyLatLng;//我的地理位置
	private String mLatitude;//我的地理位置--纬度
	private String mLongitude;//我的地理位置--经度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_detail);
        getIntentExtra();
        initActionBar();
        initViews();
        showWaitDialog("正在加载");
    }

    @SuppressWarnings("unchecked")
    private void getIntentExtra() {
        if(getIntent().getExtras() != null){
            showcase_id = getIntent().getExtras().getString(PARAME_SHOW_CASE_ID_KEY);
            store_id = getIntent().getStringExtra(AppConstants.PARAM_ORDER_COMPANY_ID);
        }
    }

    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("施工现场详情");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        findViewById(R.id.ll_submit).setOnClickListener(this);
        mXListView = (XListView) findViewById(R.id.xlist);

        mXListView.setOnItemClickListener(this);
        mXListView.setAdapter(myAdapter = new MyAdapter());
        mXListView.setPullRefreshEnable(true);
        mXListView.setPullLoadEnable(false);
        mXListView.setAutoRefreshEnable(true);
        mXListView.setAutoLoadMoreEnable(false);
        mXListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    private void initData() {
        httpRequestQuerySolutionCase(XListRefreshType.ON_PULL_REFRESH);
        httpRequestQuerySolutionHead();
    }

    /**
     * HTTP请求 - 获取施工现场列表数据
     * 
     * @param xListRefreshType
     * @param filter
     * @param keyword
     * @param max_id
     * @param min_id
     */
    private void httpRequestQuerySolutionCase(final XListRefreshType xListRefreshType) {
        LogUtil.d("httpRequestQueryClientList XListRefreshType = " + xListRefreshType);
        ConstructionSiteTask task = new ConstructionSiteTask(SolutionDetailActivity.this,showcase_id,1,20);
        task.setCallBack(new AbstractHttpResponseHandler<ConstructionSiteList>() {

            @Override
            public void onSuccess(ConstructionSiteList result) {
                mIsBottom = true;
                LogUtil.d("onSuccess ConstructionSiteList = " + result);
                if (null != result && result.getSubcase() != null && result.getSubcase().size() > 0) {
                    // 加载更多还是刷新
                    if (XListRefreshType.ON_PULL_REFRESH == xListRefreshType) {
                        mSubcaseList = result.getSubcase();
                        initFillListSubCase();
                        myAdapter.notifyDataSetChanged();
                    }
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
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if(mIsBottom && mIsHead){
                    hideWaitDialog();
                }
            }

        });
        task.send();
    }
    
    private void initFillListSubCase(){
        if(subcases != null){
            subcases.clear();
        }
        for (ConstructionSite constructionSite : mSubcaseList) {
            ConstructionCase constructionCase = new ConstructionCase();
            constructionCase.setZx_code(constructionSite.getZx_node()+"");
            constructionCase.setDesc(constructionSite.getDesc());
            constructionCase.setAdd_time(constructionSite.getAdd_time());
            constructionCase.setImages(constructionSite.getImage());
            subcases.add(constructionCase);
            constructionCase = null;
        }
    }
    
    private void httpRequestQuerySolutionHead(){
    	LatLng latLng = ZxsqApplication.getInstance().getUserPoint();
        if(latLng == null){
            latLng = mMyLatLng;
        }
        if(latLng != null){
            mLatitude = latLng.latitude+"";
            mLongitude = latLng.longitude+"";
        }
        SolutionDetailTask task = new SolutionDetailTask(this, showcase_id,mLatitude,mLongitude);
        task.setCallBack(new AbstractHttpResponseHandler<SolutionDetail>() {

            @Override
            public void onSuccess(SolutionDetail t) {
                mIsHead = true;
                hideWaitDialog();
                mXListView.onRefreshComplete();
                if (header == null) mXListView.addHeaderView(header = getListViewHeader());
                setData(t);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int code, String error) {
                mXListView.onRefreshComplete();
            }
            
            @Override
            public void onFinish() {
                super.onFinish();
                if(mIsBottom && mIsHead){
                    hideWaitDialog();
                }
            }
        });
        task.send();
    }
    protected void setData(SolutionDetail details) {
        this.details = details;
        this.store_id = details.getStore_id();
        this.store_name = details.getStore_name();
        foreman_constructionsite_title.setText(getNotNullString(details.getHousing_name()));
        
        //距离
        String distance = DistanceUtil.getDiatance(details.getDistance());
        if(!TextUtils.isEmpty(distance)){
        	foreman_constructionsite_distance.setText(distance);
        	foreman_constructionsite_distance.setVisibility(View.VISIBLE);
        }else{
        	foreman_constructionsite_distance.setVisibility(View.GONE);
        }
        String styleAndArea = "";
        // 面积
        String area = getNotNullString(details.getArea());
        if (area!=null && area.trim().length() != 0) {
        	styleAndArea = styleAndArea+area.trim()+"㎡ ";
        }
        
        //户型
        String door_model = details.getDoor_model();
        if(door_model!=null && door_model.trim().length()!= 0){
        	if(!TextUtils.isEmpty(styleAndArea)){
        		styleAndArea = styleAndArea+" | ";
            }
        	styleAndArea = styleAndArea+door_model.trim();
        }
        //风格
        String room_style = details.getRoom_style();
        if(room_style!=null && room_style.trim().length()!= 0){
        	if(!TextUtils.isEmpty(styleAndArea)){
        		styleAndArea = styleAndArea+" | ";
            }
        	styleAndArea = styleAndArea+room_style.trim();
        }
        if(!TextUtils.isEmpty(styleAndArea)){
        	foreman_constructionsite_style_area.setText(styleAndArea);
        	foreman_constructionsite_style_area.setVisibility(View.VISIBLE);
        }else{
        	foreman_constructionsite_style_area.setVisibility(View.GONE);
        }
        
        // 造价
        String budget = "";
        if(details.getBudget()!=null){
        	budget = details.getBudget() == 0 ? "" : details.getBudget() + "万";
        }
        if (details.getRenovation_way() != null) {
        	if(!TextUtils.isEmpty(budget)){
        		budget = budget + "/";
        	}
            if (details.getRenovation_way().equals("1")) {
            	budget = budget + "半包";
            } else if (details.getRenovation_way().equals("2")) {
            	budget = budget + "全包";
            }
        }
        
        if(!TextUtils.isEmpty(budget)){
        	foreman_constructionsite_price.setVisibility(View.VISIBLE);
        	foreman_constructionsite_price.setText("施工造价："+budget);
        }else{
        	foreman_constructionsite_price.setVisibility(View.GONE);
        }
        
        if(details.getData() != null && !TextUtils.isEmpty(details.getData().getTimeSlot())){
        	foreman_constructionsite_duration.setText("施工工期："+details.getData().getTimeSlot() + "天");
        	foreman_constructionsite_duration.setVisibility(View.VISIBLE);
        }else{
        	foreman_constructionsite_duration.setVisibility(View.GONE);
        }
        
         if(!TextUtils.isEmpty(store_name)){
        	 foreman_constructionsite_foreman_detail.setVisibility(View.VISIBLE);
        	 foreman_constructionsite_foreman_name.setText(store_name);
         }else{
        	 foreman_constructionsite_foreman_detail.setVisibility(View.GONE);
         }
         
        //在向上滑动的过程中不需要再把工地信息显示在顶部了
        //setDataHeader();

        // 阶段
        int stage = details.getZx_node();

        Drawable start =
                getResources().getDrawable(R.drawable.foreman_constructionsite_stage_start);
        Drawable hydropower =
                getResources().getDrawable(R.drawable.foreman_constructionsite_stage_hydropower);
        Drawable mudwood =
                getResources().getDrawable(R.drawable.foreman_constructionsite_stage_mudwood);
        Drawable paint =
                getResources().getDrawable(R.drawable.foreman_constructionsite_stage_paint);
        Drawable completed =
                getResources().getDrawable(R.drawable.foreman_constructionsite_stage_completed);
        Drawable stage_real =
                getResources().getDrawable(R.drawable.foreman_constructionsite_stage_real);

        switch (stage) {
            case 6:
                foreman_constructionsite_real.setCompoundDrawablesWithIntrinsicBounds(null,
                        stage_real, null, null);
            case 5:
                foreman_constructionsite_completed.setCompoundDrawablesWithIntrinsicBounds(null,
                        completed, null, null);
                foreman_constructionsite_paint_line.setBackgroundColor(STAGE_LINE_COLOR_ACTIVATED);
            case 4:
                foreman_constructionsite_paint.setCompoundDrawablesWithIntrinsicBounds(null, paint,
                        null, null);
                foreman_constructionsite_mudwood_line
                        .setBackgroundColor(STAGE_LINE_COLOR_ACTIVATED);
            case 3:
                foreman_constructionsite_mudwood.setCompoundDrawablesWithIntrinsicBounds(null,
                        mudwood, null, null);
                foreman_constructionsite_hydropower_line
                        .setBackgroundColor(STAGE_LINE_COLOR_ACTIVATED);
            case 2:
                foreman_constructionsite_hydropower.setCompoundDrawablesWithIntrinsicBounds(null,
                        hydropower, null, null);
                foreman_constructionsite_start_line.setBackgroundColor(STAGE_LINE_COLOR_ACTIVATED);
            case 1:
                foreman_constructionsite_start.setCompoundDrawablesWithIntrinsicBounds(null, start,
                        null, null);
            default:
                break;
        }

    }

	private String getNotNullString(String str) {
        if (str == null) {
            str = "";
        }
        return str.trim();
    }

    private View getListViewHeader() {
        View view = View.inflate(this, R.layout.activity_constructionsite_detail_header, null);

        foreman_constructionsite_title =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_title);
        foreman_constructionsite_distance =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_distance);
        foreman_constructionsite_style_area =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_style_area);
        foreman_constructionsite_price =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_price);
        foreman_constructionsite_duration =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_duration);
 
        foreman_constructionsite_start_line =
                (ImageView) view.findViewById(R.id.iv_activity_constructionsite_detial_header_start_line);
        foreman_constructionsite_hydropower_line =
                (ImageView) view.findViewById(R.id.iv_activity_constructionsite_detial_header_hydropower_line);
        foreman_constructionsite_mudwood_line =
                (ImageView) view.findViewById(R.id.iv_activity_constructionsite_detial_header_mudwood_line);
        foreman_constructionsite_paint_line =
                (ImageView) view.findViewById(R.id.iv_activity_constructionsite_detial_header_paint_line);
        foreman_constructionsite_complete_line =
                (ImageView) view.findViewById(R.id.iv_activity_constructionsite_detial_header_completed_line);

        foreman_constructionsite_foreman_name = (TextView)view.findViewById(R.id.tv_activity_constructionsite_detial_header_foremanname);
        foreman_constructionsite_foreman_detail = (RelativeLayout) view.findViewById(R.id.rl_activity_constructionsite_detial_header_foreman_detail);
        view.findViewById(R.id.rl_activity_constructionsite_detial_header_foreman_detail).setOnClickListener(this);
        
        foreman_constructionsite_start =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_start);
        foreman_constructionsite_hydropower =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_hydropower);
        foreman_constructionsite_mudwood =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_mudwood);
        foreman_constructionsite_paint =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_paint);
        foreman_constructionsite_completed =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_completed);
        foreman_constructionsite_real =
                (TextView) view.findViewById(R.id.tv_activity_constructionsite_detial_header_real);
        
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_submit:
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0018);
                construction();
                break;
            case R.id.rl_activity_constructionsite_detial_header_foreman_detail://点击进入工长详情页
            	Bundle bd = new Bundle();
            	bd.putString(AppConstants.PARAM_ORDER_COMPANY_ID, store_id);
                bd.putString("latitude", mLatitude);
                bd.putString("longitude", mLongitude);
                bd.putString("foreman_name", store_name);
                ActivityUtil.next(this, ForemanDetailsActivity.class, bd, -1);
            	break;
            default:
                break;
        }
    }

    // 进入下单流程
    private void construction() {
        AnalyticsUtil.onEvent(this, "click28");
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.PARAM_ORDER_COMPANY_ID, store_id);
        bundle.putBoolean(AppConstants.PARAM_IS_FREE, true);
        bundle.putString(AppConstants.PARAM_ORDER_SOURCE_NAME, "app_showcase_detail");
        ActivityUtil.checkAppointmentToJump(this, bundle);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

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
        	ItemViewHolder holder = null;
            if (convertView == null) {
                convertView =
                        View.inflate(SolutionDetailActivity.this,
                                R.layout.item_foreman_constructioncase, null);

                holder = new ItemViewHolder();

                holder.item_foreman_constructioncase_line =
                        (ImageView) convertView
                                .findViewById(R.id.iv_item_foreman_constructioncase_line);
                holder.item_foreman_constructioncase_icon =
                        (ImageView) convertView
                                .findViewById(R.id.iv_item_foreman_constructioncase_icon);

                holder.item_foreman_constructioncase_stage =
                        (TextView) convertView
                                .findViewById(R.id.tv_item_foreman_constructioncase_stage);
                holder.item_foreman_constructioncase_description =
                        (TextView) convertView
                                .findViewById(R.id.tv_item_foreman_constructioncase_description);
                holder.item_foreman_constructioncase_datetime =
                        (TextView) convertView
                                .findViewById(R.id.tv_item_foreman_constructioncase_datetime);

                holder.item_foreman_constructioncase_image =
                        (ImageView) convertView
                                .findViewById(R.id.iv_item_foreman_constructioncase_image);
                holder.item_foreman_constructioncase_image_count = 
                		(TextView)convertView
                        .findViewById(R.id.tv_item_foreman_constructioncase_image_count);
                holder.item_foreman_constructioncase_frame = 
                		(FrameLayout)convertView
                        .findViewById(R.id.fl_item_foreman_constructioncase_frame);
                
                convertView.setTag(holder);
            }else{
            	holder = (ItemViewHolder) convertView.getTag();
            }

            if (position == subcases.size() - 1) {
                holder.item_foreman_constructioncase_line.setVisibility(View.INVISIBLE);
            } else {
                holder.item_foreman_constructioncase_line.setVisibility(View.VISIBLE);
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
            String description = getNotNullString(constructionCase.getDesc());
            if(TextUtils.isEmpty(description)){
            	holder.item_foreman_constructioncase_description.setVisibility(View.GONE);
            }else{
            	holder.item_foreman_constructioncase_description.setVisibility(View.VISIBLE);
            	holder.item_foreman_constructioncase_description.setText(description);
            }
            
            holder.item_foreman_constructioncase_datetime.setText(DateUtil.timestampToSdate(
                    constructionCase.getAdd_time(), "MM/dd HH:mm"));
           
            holder.item_foreman_constructioncase_frame.setTag(constructionCase.getImages());
            holder.item_foreman_constructioncase_frame.setOnClickListener(clickListener);
            
            if (constructionCase.getImages() != null && constructionCase.getImages().size() > 0) {
            	holder.item_foreman_constructioncase_frame.setVisibility(View.VISIBLE);
            	List<Image> imgs = constructionCase.getImages();
                ImageUtil.displayImage(getNotNullString(imgs.get(0).getThumb_path()),
                            holder.item_foreman_constructioncase_image,
                            ImageLoaderOptions.getDefaultImageOption());
                holder.item_foreman_constructioncase_image_count.
                		setText(constructionCase.getImages().size()+"张");
            }else if(constructionCase.getImages() == null || constructionCase.getImages().size() == 0){
            	holder.item_foreman_constructioncase_frame.setVisibility(View.GONE);
            }

            return convertView;
        }

        class ItemViewHolder {
            public ImageView item_foreman_constructioncase_line;
            public ImageView item_foreman_constructioncase_icon;
            public TextView item_foreman_constructioncase_stage;
            public TextView item_foreman_constructioncase_description;
            public TextView item_foreman_constructioncase_datetime;
            public ImageView item_foreman_constructioncase_image;
            public TextView item_foreman_constructioncase_image_count;
            private FrameLayout item_foreman_constructioncase_frame;
        }
    }

    // 点击图片，进入图片查看页
    private OnClickListener clickListener = new OnClickListener() {

        @SuppressWarnings({"unchecked"})
        @Override
        public void onClick(View v) {
            AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0003);
            List<Image> logos = (List<Image>) v.getTag();
            List<String> imgs = new ArrayList<String>();
            for (int i = 0; i < logos.size(); i++) {
                if (logos.get(i) != null) {
                    imgs.add(logos.get(i).getImg_path());
                }
            }
            showImage(imgs, 0);
        }
    };

    // 查看图片
    private void showImage(List<String> images, int position) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS,
                (ArrayList<String>) images);
        bundle.putInt(ImageSimpleBrowseActivity.EXTRA_POSITION, position);
        ActivityUtil.next(this, ImageSimpleBrowseActivity.class, bundle, -1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        ImageLoader.getInstance().clearMemoryCache();
    }
 
}
