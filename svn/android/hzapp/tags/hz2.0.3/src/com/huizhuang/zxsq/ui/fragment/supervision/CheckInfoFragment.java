package com.huizhuang.zxsq.ui.fragment.supervision;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.common.Image;
import com.huizhuang.zxsq.http.bean.supervision.CheckInfo;
import com.huizhuang.zxsq.http.bean.supervision.CheckInfoDetail;
import com.huizhuang.zxsq.ui.activity.ImageSimpleBrowseActivity;
import com.huizhuang.zxsq.ui.activity.order.CostChangeActivity;
import com.huizhuang.zxsq.ui.activity.order.DelayOrderActivity;
import com.huizhuang.zxsq.ui.adapter.supervision.CheckInfoAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.huizhuang.zxsq.widget.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * 验收信息
 * 
 * @author th
 * 
 */
public class CheckInfoFragment extends BaseFragment implements OnClickListener {

    private LinearLayout mLinCostChange;
    private LinearLayout mLinSiteDelay;
    private TextView mTvCostChange;
    private TextView mTvSiteDelay;
    
    private LinearLayout mLinReport;
    private RelativeLayout mRlA;
    private RelativeLayout mRlB;
    private ImageView mIvA;
    private ImageView mIvB;
    private TextView mTvA;
    private TextView mTvB;
    private MyListView mListView;
    
    private CheckInfoAdapter mAdapter;
    private CheckInfo mCheckInfo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("onCreateView()");
        View view = inflater.inflate(R.layout.fragment_check_info, container, false);
        initVews(view);
        return view;
    }

    private void initVews(View v) {
        mLinCostChange = (LinearLayout)v.findViewById(R.id.lin_cost_change);
        mLinSiteDelay = (LinearLayout)v.findViewById(R.id.lin_site_delays);
        mTvCostChange = (TextView)v.findViewById(R.id.tv_cost_change_report);
        mTvSiteDelay = (TextView)v.findViewById(R.id.tv_site_delays_report);
        
        mLinReport = (LinearLayout)v.findViewById(R.id.lin_report);
        mLinReport.setVisibility(View.GONE);
        mRlA = (RelativeLayout)v.findViewById(R.id.rl_a);
        mRlB = (RelativeLayout)v.findViewById(R.id.rl_b);
        mIvA = (ImageView)v.findViewById(R.id.iv_a);
        mIvB = (ImageView)v.findViewById(R.id.iv_b);
        mTvA = (TextView)v.findViewById(R.id.tv_a_report);
        mTvB = (TextView)v.findViewById(R.id.tv_b_report);
        int width = (UiUtil.getScreenWidth(getActivity()) - DensityUtil.dip2px(getActivity(), 50))/2;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, (int) (width / 1.3));
        mRlA.setLayoutParams(lp);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width, (int) (width / 1.3));
        lp1.setMargins(DensityUtil.dip2px(getActivity(), 10), 0, 0, 0);
        mRlB.setLayoutParams(lp1);
        mListView = (MyListView)v.findViewById(R.id.lv_site_info);
        
        v.findViewById(R.id.lin_cost_change).setOnClickListener(this);
        v.findViewById(R.id.lin_site_delays).setOnClickListener(this);
        v.findViewById(R.id.rl_a).setOnClickListener(this);
        v.findViewById(R.id.rl_b).setOnClickListener(this);
    }


    public void initData(CheckInfo checkInfo) {
        mCheckInfo = checkInfo;
        if(checkInfo.getNode_id() == 1){//开工阶段无变更单、有需求单
            if(checkInfo.getA() != null && checkInfo.getA().size() > 0){
                mLinReport.setVisibility(View.VISIBLE);
                mRlA.setVisibility(View.VISIBLE);
                mTvA.setText("甲供需求单("+checkInfo.getA().size()+"张)");
                ImageLoader.getInstance().displayImage(checkInfo.getA().get(0).getImg_path(), mIvA, ImageLoaderOptions.optionsDefaultEmptyPhoto);
            }
            
            if(checkInfo.getB() != null && checkInfo.getB().size() > 0){
                mLinReport.setVisibility(View.VISIBLE);
                mRlB.setVisibility(View.VISIBLE);
                mTvB.setText("乙供需求单("+checkInfo.getB().size()+"张)");
                ImageLoader.getInstance().displayImage(checkInfo.getB().get(0).getImg_path(), mIvB, ImageLoaderOptions.optionsDefaultEmptyPhoto);
            }
        }else{
            if(checkInfo.getNode_id() == 5){//竣工阶段有竣工结算单、保修单
                if(checkInfo.getStatement_list() != null && checkInfo.getStatement_list().size() > 0){
                    mLinReport.setVisibility(View.VISIBLE);
                    mRlA.setVisibility(View.VISIBLE);
                    mTvA.setText("竣工结算单("+checkInfo.getStatement_list().size()+"张)");
                    ImageLoader.getInstance().displayImage(checkInfo.getStatement_list().get(0).getImg_path(), mIvA, ImageLoaderOptions.optionsDefaultEmptyPhoto);
                }
                
                if(checkInfo.getWarranty_list() != null && checkInfo.getWarranty_list().size() > 0){
                    mLinReport.setVisibility(View.VISIBLE);
                    mRlB.setVisibility(View.VISIBLE);
                    mTvB.setText("保修单("+checkInfo.getWarranty_list().size()+"张)");
                    ImageLoader.getInstance().displayImage(checkInfo.getWarranty_list().get(0).getImg_path(), mIvB, ImageLoaderOptions.optionsDefaultEmptyPhoto);
                }
            }
            
            if(checkInfo.getCost() == 1){//有费用变更单
                mLinCostChange.setVisibility(View.VISIBLE);
            }else{
                mLinCostChange.setVisibility(View.GONE);
            }
            if(checkInfo.getDelay() == 1){//有延期变更单
                mLinSiteDelay.setVisibility(View.VISIBLE);
            }else{
                mLinSiteDelay.setVisibility(View.GONE);
            }
        }
        mAdapter = new CheckInfoAdapter(getActivity(),mClickHandler);
        mAdapter.setList(checkInfo.getInfo());
        mListView.setAdapter(mAdapter);
    }

    @SuppressLint("HandlerLeak")
    private Handler mClickHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            int position = msg.arg1;
            ArrayList<String> imagesUrls = new ArrayList<String>();
            for (CheckInfoDetail checkInfoDetail : mAdapter.getList()) {
                if(!TextUtils.isEmpty(checkInfoDetail.getScene_img_path())){
                    imagesUrls.add(checkInfoDetail.getScene_img_path());
                }
            }
            switch (what) {
                case 0://查看缩略图
                    if(position < imagesUrls.size()){
                        seeBigImg1(imagesUrls, position);
                    }
                    break;
                default:
                    break;
            }
        }

    };
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_cost_change://费用变更单
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.PARAM_STAGE_ID, mCheckInfo.getStage_id());
                ActivityUtil.next(getActivity(), CostChangeActivity.class, bundle, false);
                break;
            case R.id.lin_site_delays://工程延期单
                Bundle bundle1 = new Bundle();
                bundle1.putString(AppConstants.PARAM_STAGE_ID, mCheckInfo.getStage_id());
                ActivityUtil.next(getActivity(), DelayOrderActivity.class, bundle1, false);
                break;
            case R.id.rl_a:
                if(mCheckInfo.getNode_id() == 1){//开工-甲供需求单
                    if(mCheckInfo.getA().size() > 0){
                        seeBigImg(mCheckInfo.getA());
                    }
                }else{//竣工结算单
                    if(mCheckInfo.getStatement_list().size() > 0){
                        seeBigImg(mCheckInfo.getStatement_list());
                    }
                }
                break;
            case R.id.rl_b:
                if(mCheckInfo.getNode_id() == 1){//开工-乙供需求单
                    if(mCheckInfo.getB().size() > 0){
                        seeBigImg(mCheckInfo.getB());
                    }
                }else{//保修单
                    if(mCheckInfo.getWarranty_list().size() > 0){
                        seeBigImg(mCheckInfo.getWarranty_list());
                    }
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 查看图集
     * @param images
     */
    private void seeBigImg(List<Image> images){
        Bundle bd = new Bundle();
        ArrayList<String> ImageUrs = new ArrayList<String>();
        for (Image image : images) {
            ImageUrs.add(image.getImg_path());
        }
        bd.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS, ImageUrs);
        ActivityUtil.next(getActivity(), ImageSimpleBrowseActivity.class,bd,-1);
    }
    
    /**
     * 查看图集
     * @param images
     */
    private void seeBigImg1(ArrayList<String> images,int position){
        Bundle bd = new Bundle();
        bd.putInt(ImageSimpleBrowseActivity.EXTRA_POSITION,position);
        bd.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS, images);
        ActivityUtil.next(getActivity(), ImageSimpleBrowseActivity.class,bd,-1);
    }
}
