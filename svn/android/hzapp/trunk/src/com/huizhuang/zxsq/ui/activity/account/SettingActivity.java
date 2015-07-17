package com.huizhuang.zxsq.ui.activity.account;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.ui.activity.HomeActivity;
import com.huizhuang.zxsq.ui.activity.base.UserLoginActivityBase;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.FileUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.fb.FeedbackAgent;

/**
 * 设置界面
 * 
 * @ClassName: SettingActivity.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-11-20 上午9:10:31
 */
public class SettingActivity extends UserLoginActivityBase {

	public static final int WHAT_SET_ALIAS = 4;//为JPush设置别名
	
    private TextView mTvCacheZize;
    private CheckBox mChbWifiLoadImage;
    private Context mContext;
	private Handler mhandler;

	private TagAliasCallback mTagAliasCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate Bundle = " + savedInstanceState);
        setContentView(R.layout.setting_activity);
        mContext = SettingActivity.this;
        initActionBar();
        initViews();
        getDiskCacheSizeByAsyncTask();
        setTagAliasCallback();
        setHandler();
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        LogUtil.d("initActionBar");

        CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        commonActionBar.setActionBarTitle(R.string.title_setting);
        commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        LogUtil.d("initViews");

        mTvCacheZize = (TextView) findViewById(R.id.tv_cache_size);
        mChbWifiLoadImage = (CheckBox) findViewById(R.id.ck_wifi_load_image);
        LogUtil.d("AppContext.getInstance().getUser().getIsOpenLogin():"
                + ZxsqApplication.getInstance().getUser().getIsOpenLogin());
        mChbWifiLoadImage.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceConfig.writeWIFILoadImageSwitch(SettingActivity.this, isChecked);
                ZxsqApplication.getInstance().getCloseLoadImgWhenNotInWifiState();
            }

        });
        mChbWifiLoadImage.setChecked(ZxsqApplication.getInstance()
                .getCloseLoadImgWhenNotInWifiState());
    }


    // /**
    // * 点击事件 - 消息推送
    // *
    // * @param v
    // */
    // public void itemPushOnClick(View v) {
    // ActivityUtil.next(SettingActivity.this, SettingPushActivity.class);
    // }

    /**
     * 点击事件 - 清除缓存
     * 
     * @param v
     */
    public void itemClearDiskCacheOnClick(View v) {
        ZxsqApplication.getInstance().clearCache();
        getDiskCacheSizeByAsyncTask();
    }

    /**
     * 点击事件 - 意见反馈
     * 
     * @param v
     */
    public void itemFeedBackOnClick(View v) {
        FeedbackAgent agent = new FeedbackAgent(SettingActivity.this);
        agent.startFeedbackActivity();
    }

    /**
     * 点击事件 - 给个好评
     * 
     * @param v
     */
    public void itemPraiseOnClick(View v) {
        
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(judge(SettingActivity.this, intent)){
            startActivity(intent);
        }else{
            showToastMsg("请先安装应用市场!");
        }
        
    }

    private boolean judge(Context paramContext, Intent paramIntent) {
        List<ResolveInfo> localList =
                paramContext.getPackageManager().queryIntentActivities(paramIntent,
                        PackageManager.GET_INTENT_FILTERS);
        if ((localList != null) && (localList.size() > 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 点击事件 - 版本信息
     * 
     * @param v
     */
    public void itemEditionOnClick(View v) {
        ActivityUtil.next(SettingActivity.this, SettingEditionActivity.class);
    }

    /**
     * 点击事件 - 退出登录
     * 
     * @param v
     */
    public void btnLogoutOnClick(View v) {
        ZxsqApplication.getInstance().setUser(null);
        ZxsqApplication.getInstance().setLogged(false);
        BroadCastManager.sendBroadCast(mContext, BroadCastManager.ACTION_USER_LOGOUT);
//        ZxsqActivityManager.getInstance().finishAllActivity();
        PrefersUtil.getInstance().removeSpf("token");
        PrefersUtil.getInstance().removeSpf("userId");
        //如果是从退出登录状态返回的主界面，则不弹出广告弹窗了
        PreferenceConfig.setReturnHomeFromLoginOut(true);
        Bundle bd = new Bundle();
        bd.putBoolean(AppConstants.PARAM_IS_LOGOUT, true);
        ActivityUtil.nextActivityWithClearTop(SettingActivity.this, HomeActivity.class,bd);
        String[] aliasAndTag = {"",""};//第一个值表示alias,第二个值表示tag
        setAliasAndTag(aliasAndTag);
        JPushInterface.stopPush(this);//停止推送
    }
    private void setAliasAndTag(String[] aliasAndTag) {
    	mhandler.sendMessage(mhandler.obtainMessage(WHAT_SET_ALIAS, aliasAndTag));
	}
    
    /**
	 * 将用户的别名绑定到JPush上之后的回调（可能会出现没有绑定成功的错误）
	 */
	private void setTagAliasCallback() {
		mTagAliasCallback = new TagAliasCallback() {
			@Override
			public void gotResult(int code, String alias, Set<String> tags) {
				String[] aliasAndTag = {"",""};
				switch(code){
				case 0://绑定成功
					LogUtil.e("用户Id作为别名绑定成功！");
					//这里可以往sharepreference里面写一个成功设置的状态。成功设置之后不必再次设置了。
					break;
				case 6002://设置超时，建议重试
					//延迟60秒来调用Handler设置别名
					mhandler.sendMessageDelayed(mhandler.obtainMessage(WHAT_SET_ALIAS, aliasAndTag), 1000*60);
					break;
				default:
					break;	
				}
			}
		};
	}
    private void setHandler() {
		mhandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				int what = msg.what;
				switch(what){
				case WHAT_SET_ALIAS://为JPush设置别名
					String[] aliasAndTag = (String[])msg.obj;
					LogUtil.e("调用JPush API，设置有效的别名");
					//退出登录将alias和tag都置为空
					String alias = aliasAndTag[0];
					Set<String> jpush_tag = new HashSet<String>();
					jpush_tag.add(aliasAndTag[1]);
					JPushInterface.setAliasAndTags(SettingActivity.this, alias, jpush_tag, mTagAliasCallback);
					break;
				default:
					break;
				}
			}
		};
	}
    /**
     * 获取缓存大小
     */
    private void getDiskCacheSizeByAsyncTask() {
        new DiskCacheSizeAsyncTask().execute();
    }

    /**
     * 计算磁盘缓存的AsyncTask
     */
    private class DiskCacheSizeAsyncTask extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... params) {
            String size = "0";
            if (ImageLoader.getInstance().getDiskCache().getDirectory().isDirectory()) {
                DecimalFormat df = new DecimalFormat(".##"); // 两位小数
                size =
                        df.format(FileUtil.getDirSize(ImageLoader.getInstance().getDiskCache()
                                .getDirectory()));
                if (size.startsWith(".")) {
                    size = "0" + size;
                }
            }
            return size;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mTvCacheZize.setText(result + "M");
        }


    }


    @Override
    protected void onUserLogin() {

    }

    @Override
    protected void onUserLogOut() {
        finish();
    }

}
