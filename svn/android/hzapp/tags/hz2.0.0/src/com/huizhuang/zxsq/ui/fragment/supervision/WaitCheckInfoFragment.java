package com.huizhuang.zxsq.ui.fragment.supervision;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.order.OrderDetailChild;
import com.huizhuang.zxsq.ui.activity.supervision.WaitResponseActivity;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * 等待应答验收信息
 * 
 * @author th
 * 
 */
public class WaitCheckInfoFragment extends BaseFragment implements OnClickListener {

    private CircleImageView mCivHead;
    private TextView mTvName;
    private TextView mTvCity;
    private TextView mTvOrderCount;
    private RatingBar mRbScore;
    private TextView mTvScore;
    private TextView mTvHelp;
    private TextView mTvDes;
    
    private String mPhone;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("onCreateView()");
        View view = inflater.inflate(R.layout.fragment_wait_check_info, container, false);
        initVews(view);
        return view;
    }

    private void initVews(View v) {
        mCivHead = (CircleImageView) v.findViewById(R.id.iv_head);
        mTvName = (TextView) v.findViewById(R.id.tv_name);
        mTvCity = (TextView) v.findViewById(R.id.tv_city);
        mTvOrderCount = (TextView) v.findViewById(R.id.tv_order_count);
        mRbScore = (RatingBar) v.findViewById(R.id.rb_score);
        mTvScore = (TextView) v.findViewById(R.id.tv_score);
        mTvHelp = (TextView) v.findViewById(R.id.tv_help);
        mTvDes = (TextView) v.findViewById(R.id.tv_des);

        v.findViewById(R.id.ib_send_message).setOnClickListener(this);
        v.findViewById(R.id.ib_to_call).setOnClickListener(this);
        v.findViewById(R.id.tv_help).setOnClickListener(this);
    }


    public void initData(List<OrderDetailChild> orders,String nodeId) {
        OrderDetailChild orderDetailChild = orders.get(0);
        mPhone = orderDetailChild.getMobile();
        Drawable drawable = getResources().getDrawable(R.drawable.icon_rz);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
         if(orderDetailChild.getIs_auth() == 1){
             mTvName.setCompoundDrawables(null, null, drawable, null);
         }else{
             mTvName.setCompoundDrawables(null, null, null, null);
         }
         if(!TextUtils.isEmpty(orderDetailChild.getAvater())){
             ImageLoader.getInstance().displayImage(orderDetailChild.getAvater(), mCivHead, ImageLoaderOptions.optionsDefaultEmptyPhoto);
         }
         mTvName.setText(orderDetailChild.getName());
         mTvCity.setText(orderDetailChild.getCity());
         mTvOrderCount.setText(orderDetailChild.getOrders()+"");
         float score = 0;
         if(TextUtils.isEmpty(orderDetailChild.getScore())){
             score = 5;
         }else{
             score = Float.valueOf(orderDetailChild.getScore());
         }
         mRbScore.setRating(score == 0 ? 5 : score);
         mTvScore.setText(score == 0?"5分":score+"分");
         String nodeName = Util.getNodeNameById(nodeId);
         mTvHelp.setText("猛戳我学习如何进行"+nodeName.substring(0, 2)+"验收^_^");
         String des = "";
         if("1".equals(nodeId)){
             des = "小惠正在施工中，请亲稍后来验收...";
         }else if("5".equals(orders)){
             des = "您的新家已装修完毕啦，小惠正在上传验收报告，请亲稍后.";
         }else{
             des = nodeName+"正在施工中，请亲稍后来验收...";
         }
         mTvDes.setText(des);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_send_message:
                 Uri smsToUri = Uri.parse("smsto:" + mPhone);
                 Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
                 startActivity(mIntent);
                break;
            case R.id.ib_to_call:
                 Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + mPhone));
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(intent);
                break;
            case R.id.tv_help:
                WaitResponseActivity waitResponseActivity = (WaitResponseActivity)getActivity();
                waitResponseActivity.switchFragment(1);
                break;
            default:
                break;
        }
    }
}
