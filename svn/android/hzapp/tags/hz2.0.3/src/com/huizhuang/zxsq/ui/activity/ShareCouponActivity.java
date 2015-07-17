package com.huizhuang.zxsq.ui.activity;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.Share;
import com.huizhuang.zxsq.http.bean.coupon.Coupon;
import com.huizhuang.zxsq.http.bean.coupon.CouponType;
import com.huizhuang.zxsq.http.task.coupon.CouponAddTask;
import com.huizhuang.zxsq.http.task.coupon.CouponTypeTask;
import com.huizhuang.zxsq.share.OnekeyShare;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.ScreenShotUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.huizhuang.zxsq.widget.dialog.CouponDialog;

/**
 * 分享得优惠券页
 * 
 * @author th
 * 
 */
public class ShareCouponActivity extends BaseActivity
        implements
            OnClickListener,
            PlatformActionListener,
            Callback {

    private final static int MSG_CCALLBACK_SUCCESS = 1;// 分享成功
    private final static int MSG_CCALLBACK_ERROR = 2;// 分享失败
    private final static int MSG_CCALLBACK_CANCEL = 3;// 分享取消
    private static final int MSG_WECHAT_EXSIT = 6;// 没有安装微信客户端

    private DataLoadingLayout mDataLoadingLayout;
    private TextView mTvShareMoney;
    private Share mShare;
    private boolean mIsOrder = false;
    private String mOrdersId;
    private int mType = 3;// 1.下载 2.下单3.成交
    private String mMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate Bundle = " + savedInstanceState);

        setContentView(R.layout.activity_share_coupon);
        initExtraData();
        initActionBar();
        initView();
        initListener();
        httpRequestGetCouponTypes();
    }

    private void initExtraData() {
        mIsOrder = getIntent().getBooleanExtra(AppConstants.PARAM_IS_ORDER, false);
        mOrdersId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_ID);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        commonActionBar.setActionBarTitle("分享\"惠装APP\"获取优惠券");
        commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequestGetCouponTypes();
            }
        });
        mTvShareMoney = (TextView) findViewById(R.id.tv_share_money);
        mShare = new Share();
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.share);
        String path = ScreenShotUtil.savePic(bitmap, this);
        mShare.setImagePath(path);
    }

    private void initListener() {
        findViewById(R.id.tv_wchat).setOnClickListener(this);
        findViewById(R.id.tv_weib).setOnClickListener(this);
        findViewById(R.id.tv_friends).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_wchat:
                mShare.setPlatformName(Wechat.NAME);
                share();
                break;
            case R.id.tv_weib:
                mShare.setText("直接签约工长，比装修公司省40%！新用户下载APP即可获得"+mMoney+"元优惠券" + AppConfig.SHARE_URL);
                mShare.setPlatformName(SinaWeibo.NAME);
                share();
                break;
            case R.id.tv_friends:
                mShare.setPlatformName(WechatMoments.NAME);
                share();
                break;
            default:
                break;
        }
    }

    // 分享方法
    private void share() {
        final OnekeyShare oks = new OnekeyShare();
        oks.setNotification(R.drawable.ic_launcher, "分享中...");
        oks.setTitle(mShare.getTitle());
        oks.setTitleUrl(mShare.getTitleUrl());
        oks.setText(mShare.getText());
        LogUtil.i("mShare.getImageUrl():" + mShare.getImageUrl() + "  mShare.getImagePath(): "
                + mShare.getImagePath());
        if (TextUtils.isEmpty(mShare.getImageUrl())) {
            oks.setImagePath(mShare.getImagePath());
        } else {
            oks.setImageUrl(mShare.getImageUrl());
        }
        oks.setUrl(mShare.getUrl());
        oks.setComment(mShare.getComment());
        oks.setSite(mShare.getSite());
        oks.setSiteUrl(mShare.getSiteUrl());
        oks.setSilent(false);
        oks.setPlatform(mShare.getPlatformName());
        oks.setAddress(mShare.getAddress());
        oks.setVenueName(mShare.getVenueName());
        oks.setVenueDescription(mShare.getVenueDescription());
        // 令编辑页面显示为Dialog模式
        // oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        // 设置分享过后的回调
        oks.setCallback(this);
        oks.show(this);
    }

    @Override
    public void onCancel(Platform platform, int action) {
        LogUtil.e("onCancel()", "onCancel");
        Message msg = new Message();
        msg.what = MSG_CCALLBACK_CANCEL;
        msg.arg1 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        LogUtil.e("onComplete()", "onComplete");
        Message msg = new Message();
        msg.what = MSG_CCALLBACK_SUCCESS;
        msg.arg1 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform platform, int action, Throwable t) {
        LogUtil.e("onError()", "onError");
        t.printStackTrace();
        Message msg = new Message();
        if ("WechatClientNotExistException".equals(t.getClass().getSimpleName())) {
            msg.what = MSG_WECHAT_EXSIT;
        } else {
            msg.what = MSG_CCALLBACK_ERROR;
        }
        msg.arg1 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CCALLBACK_SUCCESS:
                LogUtil.e("handleMessage()", "MSG_CCALLBACK_SUCCESS");
                showToastMsg("分享成功");
                httpRequestAddCoupon();
                break;
            case MSG_CCALLBACK_ERROR:
                // 失败
                showToastMsg("分享失败");
                break;
            case MSG_CCALLBACK_CANCEL:
                // 取消
                showToastMsg("分享取消");
                break;
            case MSG_WECHAT_EXSIT:
                showToastMsg("请安装微信客户端");
                break;
        }
        return false;
    }

    /**
     * 得到优惠券类型
     */
    private void httpRequestGetCouponTypes() {
        CouponTypeTask task = new CouponTypeTask(this);
        task.setCallBack(new AbstractHttpResponseHandler<CouponType>() {

            @Override
            public void onStart() {
                super.onStart();
                mDataLoadingLayout.showDataLoading();
            }

            @Override
            public void onSuccess(CouponType couponType) {
                mDataLoadingLayout.showDataLoadSuccess();
                initDatas(couponType);
            }

            @Override
            public void onFailure(int code, String error) {
                mDataLoadingLayout.showDataLoadFailed(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        });
        task.send();
    }

    private void initDatas(CouponType couponType) {
        mMoney = couponType.getNew_user_down().getAmount();
        String content = "";
        if (mIsOrder) {
            content = "下单首次分享惠装APP\n立得<font color='#f2ae58'>"+couponType.getOrder_share().getAmount()+"</font>元优惠券";
        } else {
            content = "成交首次分享惠装APP\n立得<font color='#f2ae58'>"+couponType.getOrder_done_share().getAmount()+"</font>元优惠券";           
        }
        mShare.setTitle("直接签约工长，比装修公司省40%！新用户下载APP即可获得"+mMoney+"元优惠券");
        mShare.setText("直接签约工长，比装修公司省40%！新用户下载APP即可获得"+mMoney+"元优惠券");
        mTvShareMoney.setText(Html.fromHtml(content));
        
    }

    /**
     * 增加优惠券
     */
    private void httpRequestAddCoupon() {
        mType = 3;
        if (mIsOrder) {
            mType = 2;
        }
        CouponAddTask task = new CouponAddTask(this, mOrdersId, mType);
        task.setCallBack(new AbstractHttpResponseHandler<Coupon>() {

            @Override
            public void onStart() {
                super.onStart();
                showWaitDialog(getResources().getString(R.string.txt_on_waiting));
            }

            @Override
            public void onSuccess(Coupon coupon) {
                if (coupon.getStatu() == 1) {
                    CouponDialog couponDialog = new CouponDialog(ShareCouponActivity.this);
                    couponDialog.builder(coupon.getAmount());
                    couponDialog.show();
                }
            }

            @Override
            public void onFailure(int code, String error) {
                showToastMsg(error);
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
