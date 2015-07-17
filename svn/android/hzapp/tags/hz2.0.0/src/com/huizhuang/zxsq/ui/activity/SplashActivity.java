package com.huizhuang.zxsq.ui.activity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.CityOpen;
import com.huizhuang.zxsq.http.bean.Constant;
import com.huizhuang.zxsq.http.bean.ProviceCityArea;
import com.huizhuang.zxsq.http.bean.Result;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.http.parser.ConstantParser;
import com.huizhuang.zxsq.http.parser.ResultParser;
import com.huizhuang.zxsq.http.parser.UserParser;
import com.huizhuang.zxsq.http.task.CityDistrictTask;
import com.huizhuang.zxsq.http.task.CityOpenTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.FileUtil;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.utils.ToastUtil;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

/**
 * @author lim
 * @ClassName: SplashActivity
 * @Description: 应用启动页
 * 功能流程 ： 1.检测更新->自动登录
 * 2.获取系统常量数据
 * 3.加载闪屏图片
 * @mail lgmshare@gmail.com
 * @date 2014年6月9日 下午5:00:57
 */
public class SplashActivity extends BaseActivity {

    private final int SPLASH_TIME_DELAY = AppConfig.SPLASH_TIME_DELAY; // 延迟
    private long mInitTime;
    private ImageView mBgImageView;
    private boolean mNeedFocusUpdate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInitTime = System.currentTimeMillis();// 记录进入时间，页面跳转时间
        setContentView(R.layout.splash_activity);
        initViews();
        setUserIsExpore();
        loadLocalImageBg();
        httpRequestCityDistrict();
        httpRequestCityOpen();
        httpRequestVersionInfo();
        httpRequestConstant();
        httpRequestBgImage();
    }

	@Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        if (!ZxsqApplication.getInstance().isWifiConnectNetWork()) {
            showToastMsg("为了您更好的浏览体验，请切换至wifi状态");
        }
    }
	@Override
	protected void onPause() {                                                         
		super.onPause();
		JPushInterface.onPause(this);
	}                                                                                                                                                                                                     
    private void initViews() {
        TextView txtVersion = (TextView) findViewById(R.id.tv_version);
        txtVersion.setText(String.format(getString(R.string.txt_version_info), ZxsqApplication.getInstance().getVersionName()));
    }

    private void loadLocalImageBg() {
        mBgImageView = (ImageView) findViewById(R.id.iv_image);
        File file = new File(getFilesDir() + AppConfig.SPLASH_IMAGE_NAME);
        if (file.exists()) {
            LogUtil.i("file.exists() " + "  load new image");
            Drawable drawable = Drawable.createFromPath(file.toString());
            mBgImageView.setImageDrawable(drawable);
        }
    }

    private void httpRequestCityDistrict() {
        CityDistrictTask task = new CityDistrictTask(this);
        task.setCallBack(new AbstractHttpResponseHandler<List<ProviceCityArea>>() {

            @Override
            public void onSuccess(List<ProviceCityArea> list) {
            	FileUtil.saveFile(SplashActivity.this, "Areas", list);
                ZxsqApplication.getInstance().setCityDistricts(list);
            }

            @Override
            public void onFailure(int code, String error) {

            }
        });
        task.send();
    }

    private void httpRequestCityOpen() {
        CityOpenTask task = new CityOpenTask(this);
        task.setCallBack(new AbstractHttpResponseHandler<List<CityOpen>>() {

            @Override
            public void onSuccess(List<CityOpen> list) {
                FileUtil.saveFile(SplashActivity.this, "CityOpen", list);
                ZxsqApplication.getInstance().setmCityOpens(list);
            }

            @Override
            public void onFailure(int code, String error) {

            }
        });
        task.send();
    }
    
    /**
     * 从后台服务器获取版本更新信息
     */
    private void httpRequestVersionInfo() {
    	RequestParams requestParams = new RequestParams();
    	requestParams.add("name", "hz-APP");
    	requestParams.add("platform", "android");
    	requestParams.add("version", ZxsqApplication.getInstance().getmAppCurVersionName());
    	
        HttpClientApi.get(HttpClientApi.REQ_VERSION_INFO, requestParams, new ResultParser(), new RequestCallBack<Result>() {

            @Override
            public void onFailure(NetroidError arg0) {
                //toMainActivity();
            	doAutoLogin();
            }

            @Override
            public void onSuccess(Result result) {
                try {
                    JSONObject obj = new JSONObject(result.data);
                    mNeedFocusUpdate = obj.optBoolean("force_update");
                    httpRequestUpdateInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 请求闪屏广告
     */
    private void httpRequestBgImage() {
        HttpClientApi.get(HttpClientApi.REQ_SPLASH_IMG, new RequestParams(), new ResultParser(), new RequestCallBack<Result>() {

            @Override
            public void onFailure(NetroidError arg0) {
            }

            @Override
            public void onSuccess(Result result) {
                try {
                    JSONObject obj = new JSONObject(result.data);
                    JSONObject splObj = obj.optJSONObject("splash");
                    if (splObj != null) {
                        String url = splObj.optString("img_path");
                        httpRequestLoadBgImage(url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 下载闪屏图片
     *
     * @param url
     */
    private void httpRequestLoadBgImage(String url) {
        LogUtil.i("new url:" + url);
        HttpClientApi.get(url, new RequestCallBack<Bitmap>() {
            @Override
            public void onFailure(NetroidError error) {
                LogUtil.i("bitMap onFailure:");
                error.printStackTrace();
            }

            @Override
            public void onSuccess(Bitmap bitMap) {
                try {
                    bitMap = ImageUtil.resetImage(bitMap, 480, 800);
                    ImageUtil.saveBitmap(getFilesDir() + AppConfig.SPLASH_IMAGE_NAME, bitMap);
                    LogUtil.i("bitMap success:");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }, 3000, 3000);
    }

    /**
     * 从友盟平台获取安装下载包
     */
    private void httpRequestUpdateInfo() {
        LogUtil.i("httpRequestUpdateInfo ");
        checkVersion();
    }

    /**
     * 获取系统常量，并保存在app中
     */
    private void httpRequestConstant() {
        RequestParams params = new RequestParams();
        HttpClientApi.get(HttpClientApi.REQ_CONSTANTS, params, new ConstantParser(), new RequestCallBack<Constant>() {

            @Override
            public void onSuccess(Constant data) {
                LogUtil.i("jiengyh getConstant onSuccess data:" + data);
                FileUtil.saveFile(SplashActivity.this, "Constant", data);
                ZxsqApplication.getInstance().setConstant(data);
            }

            @Override
            public void onFailure(NetroidError error) {
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void toStartActivity() {
        long finishTime = System.currentTimeMillis();
        if (finishTime - mInitTime > SPLASH_TIME_DELAY) {
            ActivityUtil.next(SplashActivity.this, UserGuideActivity.class, R.anim.fade_in, R.anim.fade_out, true);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    ActivityUtil.next(SplashActivity.this, UserGuideActivity.class, R.anim.fade_in, R.anim.fade_out, true);
                }
            }, SPLASH_TIME_DELAY - (finishTime - mInitTime));
        }
    }

    private void toMainActivity() {
        long finishTime = System.currentTimeMillis();
        if (finishTime - mInitTime > SPLASH_TIME_DELAY) {
            ActivityUtil.next(SplashActivity.this, HomeActivity.class, R.anim.fade_in, R.anim.fade_out, true);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    ActivityUtil.next(SplashActivity.this, HomeActivity.class, R.anim.fade_in, R.anim.fade_out, true);
                }
            }, SPLASH_TIME_DELAY - (finishTime - mInitTime));
        }
    }


    /**
     * 版本检测
     */
    private void checkVersion() {
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

            @Override
            public void onClick(int arg0) {
                LogUtil.i("checkVersion UmengDialogButtonListener onClick arg0:" + arg0);
                if (!mNeedFocusUpdate) {
                    doAutoLogin();
                } else {
                    ToastUtil.showMessage(THIS, "您必须升级才能进入应用");
                    finish();
                }
            }
        });
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                if (updateInfo == null) {
                    doAutoLogin();
                    return;
                }
                String version = updateInfo.version;
                LogUtil.i("checkVersion updateInfo:" + updateInfo
                        + "  updateStatus:" + updateStatus + "  version:"
                        + version);
                if (UmengUpdateAgent.isIgnore(SplashActivity.this, updateInfo)) {
                    doAutoLogin();
                }

                switch (updateStatus) {
                    case 0:
                        UmengUpdateAgent.showUpdateDialog(THIS, updateInfo);
                        break;
                    case 1:
                    case 2:
                    case 3:
                        doAutoLogin();
                        break;
                    default:
                        break;
                }
                // case 0: // has update
                // case 1: // has no update
                // case 2: // none wifi
                // case 3: // time out
            }


        });

        UmengUpdateAgent.forceUpdate(SplashActivity.this);
        //UmengUpdateAgent.update(SplashActivity.this);

    }

    private void doAutoLogin() {
        final String userId = PrefersUtil.getInstance().getStrValue("userId");
        final String token = PrefersUtil.getInstance().getStrValue("token");
        final String mobile = PrefersUtil.getInstance().getStrValue("mobile");
        LogUtil.i("userId:" + userId + " token:" + token);
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token) && !TextUtils.isEmpty(mobile)) {
            //走自动登录流程
            User user = new User();
            user.setUser_id(userId);
            user.setToken(token);
            user.setMobile(mobile);
            ZxsqApplication.getInstance().setUser(user);
            HttpClientApi.post(HttpClientApi.USER_LOGIN_BY_TOKEN_AND_USER_ID, new UserParser(), new RequestCallBack<User>() {

                @Override
                public void onFailure(NetroidError arg0) {
                    ZxsqApplication.getInstance().setUser(null);
                    //进入app首页
                    toMainActivity();
                }

                @Override
                public void onSuccess(User user) {
                    user.setToken(token);
                    ZxsqApplication.getInstance().setUser(user);
                    PreferenceConfig.writeFailureTime();
                    //根据订单状态进入相应的页面
                    toMainActivity();
                }
            });
        } else {
    		//开启用户指引
    		if (ZxsqApplication.getInstance().isNewInstall()) {
    			toStartActivity();
    		}else{
    			//进入app首页
    			toMainActivity();
    		}
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ZxsqApplication.getInstance().exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * 设置用户是否失效
     */
    public void setUserIsExpore(){
    	if(DateUtil.getIsTimeExpore()){
            PrefersUtil.getInstance().setValue("userId", "");
            PrefersUtil.getInstance().setValue("token","");
    	}
    }
}
