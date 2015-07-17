package com.huizhuang.zxsq.ui.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.receiver.NetChangeObserver.NetType;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.TipsToast;
import com.huizhuang.zxsq.widget.dialog.LoadingDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.PushAgent;

/**
 * @ClassName: BaseActivity
 * @Description: 基础activity
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午10:14:20
 */
public class BaseActivity extends Activity {

    public Activity THIS;
    /** 默认提示框 */
    private Toast mToast;
    /** 自定义提示框 */
    private TipsToast mTipsToast;
    /** 加载等待框 */
    private LoadingDialog mLoadingDialog;
    private Long mlastClickTime = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ZxsqApplication.getInstance().getStackManager().addActivity(this);
        LogUtil.d("onCreate");
        super.onCreate(savedInstanceState);
        THIS = this;
        PushAgent.getInstance(this).onAppStart();
        // 取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onStart() {
        LogUtil.d("onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        LogUtil.d("onResume");
        super.onResume();
        // AnalyticsUtil.onPageStart();//使用默认的页面统计方式，这一个方法SDK自动调用了，不用手动调用
        AnalyticsUtil.addAnalytics(THIS);
    }

    @Override
    protected void onPause() {
        LogUtil.d("onPause");
        super.onPause();
        // AnalyticsUtil.onPageEnd();//使用默认的页面统计方式，这一个方法SDK自动调用了，不用手动调用
        AnalyticsUtil.removeAnalytics(THIS);
        ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
    protected void onStop() {
        LogUtil.d("onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("onDestroy");
        ZxsqApplication.getInstance().getRequestQueue().cancelAll(this);
        ZxsqApplication.getInstance().getStackManager().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }

    /**
     * 设置页面标题
     * 
     * @param title
     */
    public void setHeader(String title) {
        TextView header = (TextView) findViewById(R.id.tv_title);
        header.setText(title);
    }

    /**
     * 设置页面标题及按钮事件
     * 
     * @param title
     * @param leftListener
     */
    public void setHeader(String title, View.OnClickListener leftListener) {
        TextView header = (TextView) findViewById(R.id.tv_title);
        header.setText(title);
        ImageView back = (ImageView) findViewById(R.id.btn_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(leftListener);
    }

    // /**
    // * 设置页面标题及按钮事件
    // *
    // * @param title
    // * @param rightesId
    // * @param leftListener
    // * @param rightListener
    // */
    // public void setHeader(String title, View.OnClickListener leftListener, View.OnClickListener
    // rightListener) {
    // TextView header = (TextView) findViewById(R.id.tv_title);
    // header.setText(title);
    // ImageView back = (ImageView) findViewById(R.id.btn_back);
    // back.setVisibility(View.VISIBLE);
    // back.setOnClickListener(leftListener);
    //
    // ImageView right = (ImageView) findViewById(R.id.btn_right);
    // right.setVisibility(View.VISIBLE);
    // right.setOnClickListener(rightListener);
    // }

    /**
     * 系统toast提示
     * 
     * @param msg
     */
    public void showToastMsg(String msg) {
        if (mToast != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mToast.cancel();
            }
        } else {
            mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        mToast.setText(msg);
        mToast.show();
    }

    /**
     * 自定义提示toast
     * 
     * @param icon 提示图片
     * @param msg
     */
    public void showTips(int icon, String msg) {
        if (mTipsToast != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mTipsToast.cancel();
            }
        } else {
            mTipsToast = TipsToast.makeText(this, icon, Toast.LENGTH_LONG);
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        mTipsToast.setIcon(icon);
        mTipsToast.setText(msg);
        mTipsToast.show();
    }

    /**
     * 显示加载框
     * 
     * @param msg
     */
    public void showWaitDialog(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setText(msg);
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /** 隐藏加载等待框 */
    public void hideWaitDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /** 隐藏系统软键盘 */
    public void hideSoftInput() {
        if (getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /**
     * 隐藏系统软键盘
     * 
     * @param view
     */
    public void hideSoftInput(View view) {
        if (view == null) {
            hideSoftInput();
            return;
        }
        if (getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(view.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 网络连接连接时调用
     */
    public void onConnect(NetType type) {};

    /**
     * 当前没有网络连接
     */
    public void onDisConnect() {};

    public void showActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    
    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mlastClickTime;
        if (timeD >= 0 && timeD <= 1000) {
            return true;
        } else {
            mlastClickTime = time;
            return false;
        }
    }
}
