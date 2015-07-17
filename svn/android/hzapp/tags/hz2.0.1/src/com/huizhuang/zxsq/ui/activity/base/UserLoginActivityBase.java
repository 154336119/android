package com.huizhuang.zxsq.ui.activity.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.huizhuang.zxsq.utils.BroadCastManager;

public abstract class UserLoginActivityBase extends BaseActivity {

    private BroadcastReceiver mUserLoginReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册登录广播
        regiestUserBroadCast();
    }

    private void regiestUserBroadCast() {
        mUserLoginReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                if (BroadCastManager.ACTION_USER_LOGIN.equals(intent.getAction())) {
                    onUserLogin();
                } else if (BroadCastManager.ACTION_USER_LOGOUT.equals(intent.getAction())) {
                    onUserLogOut();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastManager.ACTION_USER_LOGIN);
        filter.addAction(BroadCastManager.ACTION_USER_LOGOUT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mUserLoginReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onUnregisterReceiver();
    }

    /**
     * 移除接收者
     */
    public void onUnregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUserLoginReceiver);
    }

    /**
     * 当用户登录成功以后需要做什么
     */
    protected abstract void onUserLogin();

    protected abstract void onUserLogOut();

}
