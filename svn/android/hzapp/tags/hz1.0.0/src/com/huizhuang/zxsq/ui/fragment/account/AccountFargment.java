package com.huizhuang.zxsq.ui.fragment.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.Order;
import com.huizhuang.zxsq.http.task.account.GetUserInfoTask;
import com.huizhuang.zxsq.module.User;
import com.huizhuang.zxsq.ui.activity.account.AccountAttentionActivity;
import com.huizhuang.zxsq.ui.activity.account.AccountBalanceActivity;
import com.huizhuang.zxsq.ui.activity.account.AccountBeAttentionActivity;
import com.huizhuang.zxsq.ui.activity.account.AccountDataEditorActivity;
import com.huizhuang.zxsq.ui.activity.account.AllorderListActivity;
import com.huizhuang.zxsq.ui.activity.account.ChatListActivity;
import com.huizhuang.zxsq.ui.activity.account.CommentsHistoryListActivity;
import com.huizhuang.zxsq.ui.activity.account.MyDiaryActivity;
import com.huizhuang.zxsq.ui.activity.account.MyFavoriteActivity;
import com.huizhuang.zxsq.ui.activity.account.MyOrderActivity;
import com.huizhuang.zxsq.ui.activity.account.SettingActivity;
import com.huizhuang.zxsq.ui.activity.account.SupervisionOrderListActivity;
import com.huizhuang.zxsq.ui.activity.common.OrderListActivity;
import com.huizhuang.zxsq.ui.activity.common.OrderListActivity.OrderType;
import com.huizhuang.zxsq.ui.activity.complaints.ComplaintsFeedbackChoiceActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.ui.activity.user.UserRegisterActivity;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.CircleImageView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 个人中心页面
 *
 * @author Xun.Zhang
 * @ClassName: AccountFargment.java
 * @date 2014-11-17 下午4:28:04
 */
public class AccountFargment extends BaseFragment implements OnClickListener {

    private CommonActionBar mCommonActionBar;

    /**
     * 登录后的视图
     */
    private View mLoggedView;
    private CircleImageView mCivHeadImg;
    private TextView mTvName;
    private TextView mTvArea;
    private TextView mTvMoney;
    private TextView mTvComments;
    private TextView mTvAttention;
    private TextView mTvFans;
    private TextView mTvMyMessageCount;
    private TextView mTvNoneCommentsCount;
    /**
     * 未登录视图
     */
    private View mUnloginView;

    public static AccountFargment newInstance() {
        return new AccountFargment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initActionBar(view);
        return view;
    }

    /**
     * 初始化ActionBar
     *
     * @param v
     */
    private void initActionBar(View v) {
        mCommonActionBar = (CommonActionBar) v.findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle(R.string.txt_bottom_tab_me);
        mCommonActionBar.setRightImgBtn(R.drawable.setting, new OnClickListener() {

            @Override
            public void onClick(View v) {
                btnSettingOnClick(v);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ZxsqApplication.getInstance().isLogged()) {
            stubLoggedView();
            httpRequestQueryUserProfile();
        } else {
            stubUnloginView();
        }
    }

    /**
     * 初始化未登录视图
     */
    private void stubUnloginView() {
        ViewStub stub = (ViewStub) findViewById(R.id.viewstub_import_unlogin);
        mUnloginView = stub.inflate();
        mUnloginView.findViewById(R.id.btn_login).setOnClickListener(this);
        mUnloginView.findViewById(R.id.btn_register).setOnClickListener(this);
    }

    /**
     * 初始化登录视图
     */
    private void stubLoggedView() {
        ViewStub stub = (ViewStub) findViewById(R.id.viewstub_import_logged);
        mLoggedView = stub.inflate();
        mCivHeadImg = (CircleImageView) mLoggedView.findViewById(R.id.civ_head);
        mTvName = (TextView) mLoggedView.findViewById(R.id.tv_name);
        mTvArea = (TextView) mLoggedView.findViewById(R.id.tv_area);
        mTvMoney = (TextView) mLoggedView.findViewById(R.id.tv_money);
        mTvComments = (TextView) mLoggedView.findViewById(R.id.tv_comments);
        mTvAttention = (TextView) mLoggedView.findViewById(R.id.tv_attention);
        mTvFans = (TextView) mLoggedView.findViewById(R.id.tv_fans);
        mTvMyMessageCount = (TextView) mLoggedView.findViewById(R.id.tv_my_message_count);
        mTvNoneCommentsCount = (TextView) mLoggedView.findViewById(R.id.tv_none_comments_count);
        User user = ZxsqApplication.getInstance().getUser();

        LogUtil.d("user.id:" + user.getId());
        // 初始化用户名和头像
        mTvName.setText(user.getNickname());
        ImageLoader.getInstance().displayImage(user.getAvatar(), mCivHeadImg, ImageLoaderOptions.getDefaultImageOption());
        mLoggedView.findViewById(R.id.btn_head).setOnClickListener(this);
        mLoggedView.findViewById(R.id.lin_account).setOnClickListener(this);
        mLoggedView.findViewById(R.id.lin_comments).setOnClickListener(this);
        mLoggedView.findViewById(R.id.lin_attention).setOnClickListener(this);
        mLoggedView.findViewById(R.id.lin_fans).setOnClickListener(this);

        mLoggedView.findViewById(R.id.btn_my_orders).setOnClickListener(this);
        mLoggedView.findViewById(R.id.btn_trading_guaranteed).setOnClickListener(this);
        mLoggedView.findViewById(R.id.btn_worksite_manager).setOnClickListener(this);
        mLoggedView.findViewById(R.id.btn_my_complaint).setOnClickListener(this);
        mLoggedView.findViewById(R.id.btn_supervision).setOnClickListener(this);
        mLoggedView.findViewById(R.id.btn_my_message).setOnClickListener(this);
        mLoggedView.findViewById(R.id.btn_none_comments).setOnClickListener(this);
        mLoggedView.findViewById(R.id.btn_my_collection).setOnClickListener(this);
        mLoggedView.findViewById(R.id.btn_my_diary).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != getView()) {
            if (ZxsqApplication.getInstance().isLogged()) {
                if (mLoggedView == null) {
                    stubLoggedView();
                }
                if (mUnloginView != null) {
                    mUnloginView.setVisibility(View.GONE);
                }
                mLoggedView.setVisibility(View.VISIBLE);
                mCommonActionBar.setRightImgBtnVisibility(View.VISIBLE);
            } else {
                if (mUnloginView == null) {
                    stubLoggedView();
                }
                if (mLoggedView != null) {
                    mLoggedView.setVisibility(View.GONE);
                }
                mUnloginView.setVisibility(View.VISIBLE);
                mCommonActionBar.setRightImgBtnVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (ZxsqApplication.getInstance().isLogged()) {
            httpRequestQueryUserProfile();
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.btn_login:// 登录
                ActivityUtil.next(getActivity(), UserLoginActivity.class);
                break;
            case R.id.btn_register:// 注册
                ActivityUtil.next(getActivity(), UserRegisterActivity.class);
                break;
            case R.id.btn_head:// 个人信息页
                ActivityUtil.next(getActivity(), AccountDataEditorActivity.class);
                break;
            case R.id.lin_account:// 账户余额
                ActivityUtil.next(getActivity(), AccountBalanceActivity.class);
                break;
            case R.id.lin_comments:// 点评
                ActivityUtil.next(getActivity(), CommentsHistoryListActivity.class);
                break;
            case R.id.lin_attention:// 关注
                AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_ACCOUNT_ME_FOLLOWED);
                ActivityUtil.next(getActivity(), AccountAttentionActivity.class);
                break;
            case R.id.lin_fans:// 粉丝
                AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_ACCOUNT_FOLLOWED_ME);
                ActivityUtil.next(getActivity(), AccountBeAttentionActivity.class);
                break;
            case R.id.btn_my_orders:// 我的订单
                ActivityUtil.next(getActivity(), MyOrderActivity.class);
                break;
            case R.id.btn_trading_guaranteed:// 交易担保
                ActivityUtil.next(getActivity(), AllorderListActivity.class);
                break;
            case R.id.btn_worksite_manager:// 工地管理
                AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_ACCOUNT_SCDEDULE_MANAGER);
                bundle.putSerializable(OrderListActivity.EXTRA_ORDER_TYPE, OrderType.SITE_MANAGEMENT);
                bundle.putInt(OrderListActivity.EXTRA_ORDER_TYP, Order.ORDER_TYPE_ALL);
                ActivityUtil.next(getActivity(), OrderListActivity.class, bundle, -1);
                break;
            case R.id.btn_my_complaint:// 我的投诉
                AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_ACCOUNT_SUPERVISION_ORDER);
                ActivityUtil.next(getActivity(), ComplaintsFeedbackChoiceActivity.class, bundle, -1);
                break;
            case R.id.btn_supervision:// 预约监理
                bundle.putSerializable(OrderListActivity.EXTRA_ORDER_TYPE, OrderType.MY_SUPERVISION);
                bundle.putInt(OrderListActivity.EXTRA_ORDER_TYP, Order.ORDER_TYPE_ALL);
                ActivityUtil.next(getActivity(), OrderListActivity.class, bundle,
                        -1);
                break;
            case R.id.btn_my_message:// 我的消息
                AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_MY_MESSAGES);
                if (ZxsqApplication.getInstance().isLogged()) {
                    ActivityUtil.next(getActivity(), ChatListActivity.class);
                } else {
                    ActivityUtil.next(getActivity(), UserLoginActivity.class);
                }
                break;
            case R.id.btn_none_comments:// 待点评
                ActivityUtil.next(getActivity(), SupervisionOrderListActivity.class);
                break;
            case R.id.btn_my_collection:// 我的收藏
                ActivityUtil.next(getActivity(), MyFavoriteActivity.class);
                break;
            case R.id.btn_my_diary:// 我的日记
                AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_MY_DIARY);
                ActivityUtil.next(getActivity(), MyDiaryActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 按钮点击事件 - 设置
     *
     * @param v
     */
    private void btnSettingOnClick(View v) {
        AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_SETTING_CENTER);
        if (ZxsqApplication.getInstance().isLogged()) {
            ActivityUtil.next(getActivity(), SettingActivity.class);
        } else {
            ActivityUtil.next(getActivity(), UserLoginActivity.class);
        }
    }

    private void httpRequestQueryUserProfile() {
        GetUserInfoTask task = new GetUserInfoTask(getActivity());
        task.setCallBack(new AbstractHttpResponseHandler<User>() {

            @Override
            public void onSuccess(User result) {
                initViewData(result);
            }

            @Override
            public void onFailure(int code, String error) {
                showToastMsg(error);
            }

            @Override
            public void onStart() {
                showWaitDialog("加载中...");
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }

        });
        task.send();
    }

    private void initViewData(User user) {
        if (user != null) {
            if (user.getUser_thumb() != null
                    && !TextUtils.isEmpty(user.getUser_thumb().getThumb_path())
                    && !user.getUser_thumb().getThumb_path().endsWith(AppConstants.DEFAULT_IMG)) {
                ImageLoader.getInstance().displayImage(user.getUser_thumb().getThumb_path(), mCivHeadImg, ImageLoaderOptions.getDefaultImageOption());
                ZxsqApplication.getInstance().getUser().setUser_thumb(user.getUser_thumb());
            }
            ZxsqApplication.getInstance().getUser().setNickname(user.getNickname());
            ZxsqApplication.getInstance().getUser().setGender(user.getGender());
            ZxsqApplication.getInstance().getUser().setProvince(user.getProvince());
            ZxsqApplication.getInstance().getUser().setCity(user.getCity());
            String name = TextUtils.isEmpty(user.getNickname()) ? user.getName() : user.getNickname();
            name = TextUtils.isEmpty(name) ? user.getMobile():name;
            mTvName.setText(name);
            String city = "";
            if (!TextUtils.isEmpty(user.getProvince())) {
                city += user.getProvince();
            }
            if (!TextUtils.isEmpty(user.getCity())) {
                city += (" " + user.getCity());
            }
            mTvArea.setText(city);
            Drawable drawable = null;
            if (user.getGender() == 1) {
                drawable = getResources().getDrawable(R.drawable.acc_boy_on);
            } else {
                drawable = getResources().getDrawable(R.drawable.acc_girl_on);
            }
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvArea.setCompoundDrawables(null, null, drawable, null);
            mTvMoney.setText(user.getAccount_amount() == null ? "￥0.00" : Util.formatMoney(user.getAccount_amount(), 2));
            mTvComments.setText(user.getComplete_comment() + "");
            mTvAttention.setText(user.getFollowing_count() + "");
            mTvFans.setText(user.getFollowed_count() + "");
            if (user.getMsg_count() > 0) {
                mTvMyMessageCount.setVisibility(View.VISIBLE);
                mTvMyMessageCount.setText(user.getMsg_count() + "条未读消息");
            } else {
                mTvMyMessageCount.setVisibility(View.INVISIBLE);
            }
            if (user.getTo_comment() > 0) {
                mTvNoneCommentsCount.setVisibility(View.VISIBLE);
                mTvNoneCommentsCount.setText(user.getTo_comment() + "条待评价");
            } else {
                mTvNoneCommentsCount.setVisibility(View.INVISIBLE);
            }
        }

    }
}