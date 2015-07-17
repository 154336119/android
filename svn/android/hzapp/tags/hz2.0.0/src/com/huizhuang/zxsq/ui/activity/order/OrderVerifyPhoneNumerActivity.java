package com.huizhuang.zxsq.ui.activity.order;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.User;
import com.huizhuang.zxsq.http.task.account.VerifySmsLoginCodeTask;
import com.huizhuang.zxsq.http.task.order.OrderSendVirefyTask;
import com.huizhuang.zxsq.ui.activity.HomeActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.BroadCastManager;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.PrefersUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;


/** 
* @ClassName: OrderVerifyPhoneNumerActivity
* @Description: 未注册手机号下单验证页 
* @author th 
* @mail 342592622@qq.com   
* @date 2015-2-13 下午7:09:23 
*  
*/
public class OrderVerifyPhoneNumerActivity extends BaseActivity implements OnClickListener {

    public final static String PARAM_IS_LOGIN_KEY = "isLogin";
    public static final int WHAT_SET_ALIAS = 4;//为JPush设置别名
    private final int DELAYED_TIME = 1000;
    private  int mInitTime = 60;
    private CommonActionBar mCommonActionBar;

    private EditText mEtPhome;
    private EditText mEtCode;
    private EditText mEtName;
    private RadioGroup mRgGender;
    
    private Button mBtnGetCode;
    private TextView mDes;
    private TextView mTvBottomTip;
    private Button mBtnSubmit;
    private int mTimes;
    private int mResult = 0;
    private String mCityId;
    private String mAddr;
    private String mProvinceName;
    private String mCityName;
    private String mAera;
    private String mMeasureTime;
    private String mhouseName;
    private String mSourceName = "";
    private String mPx;
    private String mPy;
    private String mForemanId = "";
    private String mDistrict = "";
    private int mClueId;
    private String mPhone;
    private String mCode;
    private String mName;
    private String mGender = "2";
    private boolean mIsFristGetCode = true; //是否为第一次获取
    private boolean mIsContinue = false;
	private TagAliasCallback mTagAliasCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_vertify_phone_number);
        getIntentExtra();
        initActionBar();
        initViews();
        setTagAliasCallback();
      //  mTimes = WATING_TIME;
    }

    private void getIntentExtra() {
    	mProvinceName = getIntent().getStringExtra(AppConstants.PARAM_PRO);
    	mCityName = getIntent().getStringExtra(AppConstants.PARAM_CITY);
    	mCityId = getIntent().getStringExtra(AppConstants.PARAM_CITY_ID);
    	mAddr = getIntent().getStringExtra(AppConstants.PARAM_ARRD);
    	mAera = getIntent().getStringExtra(AppConstants.PARAM_AREA);
    	mMeasureTime = getIntent().getStringExtra(AppConstants.PARAM_MEASURE_TIME);
    	mSourceName = getIntent().getStringExtra(AppConstants.PARAM_ORDER_SOURCE_NAME);
    	mhouseName = getIntent().getStringExtra(AppConstants.PARAM_HOUSE_NAME);
    	mPx= getIntent().getStringExtra(AppConstants.PARAM_LAT);
    	mPy= getIntent().getStringExtra(AppConstants.PARAM_LNG);
    	mDistrict= getIntent().getStringExtra(AppConstants.PARAM_DISTRICT);
    	if( getIntent().getStringExtra(AppConstants.PARAM_ORDER_COMPANY_ID)!=null)
    		mForemanId = getIntent().getStringExtra(AppConstants.PARAM_ORDER_COMPANY_ID);
    	if( getIntent().getStringExtra(AppConstants.PARAM_ORDER_SOURCE_NAME)!=null)
    		mSourceName = getIntent().getStringExtra(AppConstants.PARAM_ORDER_SOURCE_NAME);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        mCommonActionBar.setActionBarTitle("验证手机");
        mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {

            @Override
            public void onClick(View v) {
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0012);
                finish();
            }
        });
    }

    private void initViews() {
    	mEtPhome = (EditText) findViewById(R.id.et_phone);
    	mEtCode = (EditText) findViewById(R.id.ed_code);
    	mEtName = (EditText) findViewById(R.id.ed_name);
    	mRgGender = (RadioGroup) findViewById(R.id.rg_gender);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        mDes = (TextView) findViewById(R.id.tv_des);
        mBtnGetCode = (Button) findViewById(R.id.btn_code);
        mBtnGetCode.setOnClickListener(this);
        mBtnGetCode.setClickable(false);
        mTvBottomTip = (TextView) findViewById(R.id.tv_bottom_tip);
        mTvBottomTip.setText(Html.fromHtml("<font color='#808080'>"+"点击“下一步”，即表示同意"+"</font>"+"<font color='#ff6c38'>"+"《惠装用户使用协议》"+"</font>"));
        mBtnSubmit = (Button)findViewById(R.id.btn_submit);
        mRgGender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton)OrderVerifyPhoneNumerActivity.this.findViewById(radioButtonId);
				mGender = ""+rb.getText();
				if (mGender.equals("先生")) {
					mGender = "1";
				}else{
					mGender = "2";
				}
			}
		});
        if(mResult == 1){
            mDes.setText("该手机已注册，验证手机后即可登录");
        }
        mEtPhome.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				String phone = mEtPhome.getText().toString();
				if(!TextUtils.isEmpty(phone)&&Util.isValidMobileNumber(phone) && mEtCode.getText().toString().length()>0 && mEtName.getText().toString().length()>0){
					mBtnSubmit.setEnabled(true);
				}else{
					mBtnSubmit.setEnabled(false);
                }
				
				if(!TextUtils.isEmpty(phone)&&Util.isValidMobileNumber(phone)){
                    mBtnGetCode.setText(String.format(getResources().getString(R.string.txt_test_phone),mTimes));
                    mBtnGetCode.setTextColor(getResources().getColor(R.color.orange_light));
                    mBtnGetCode.setClickable(true);
				}else{
					mBtnGetCode.setText(String.format(getResources().getString(R.string.txt_test_phone),mTimes));
                    mBtnGetCode.setTextColor(getResources().getColor(R.color.orange_drak));
					mBtnGetCode.setClickable(false);
				}
			}
		});
        mEtCode.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				String phone = mEtPhome.getText().toString();
				if(!TextUtils.isEmpty(phone)&&Util.isValidMobileNumber(phone) && mEtCode.getText().toString().length()>0 && mEtName.getText().toString().length()>0){
					mBtnSubmit.setEnabled(true);
				}else{
					mBtnSubmit.setEnabled(false);
                }
			}
		});
        
        mEtName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				String phone = mEtPhome.getText().toString();
				if(!TextUtils.isEmpty(phone)&&Util.isValidMobileNumber(phone) && mEtCode.getText().toString().length()>0 && mEtName.getText().toString().length()>0){
					mBtnSubmit.setEnabled(true);
				}else{
					mBtnSubmit.setEnabled(false);
                }
			}
		});
    }
    /**
	 * 将用户的别名绑定到JPush上之后的回调（可能会出现没有绑定成功的错误）
	 */
	private void setTagAliasCallback() {
		mTagAliasCallback = new TagAliasCallback() {
			@Override
			public void gotResult(int code, String alias, Set<String> tags) {
				switch(code){
				case 0://绑定成功
					LogUtil.i("用户Id作为别名绑定成功！");
					//这里可以往sharepreference里面写一个成功设置的状态。成功设置之后不必再次设置了。
					break;
				case 6002://设置超时，建议重试
					//延迟60秒来调用Handler设置别名
					mHandler.sendMessageDelayed(mHandler.obtainMessage(WHAT_SET_ALIAS, alias), 1000*60);
					break;
				default:
					break;	
				}
			}
		};
	}
    /**
     * http请求-验证手机
     */
    private void httpRequestCheck() {
    	
    	mPhone = mEtPhome.getText().toString();
    	mCode = mEtCode.getText().toString();
    	mName = mEtName.getText().toString();
    	if(TextUtils.isEmpty(mPhone)){
			showToastMsg("请先输入手机号");
		}else if(!Util.isValidMobileNumber(mPhone)){
			showToastMsg("请输入正确的手机号");
		}else if(TextUtils.isEmpty(mName)){
			showToastMsg("请输入姓名");
		}else if(mCode.length()==0){
			showToastMsg("请输入正确的验证码");
		}else{
			showWaitDialog("请求中...");
	    	VerifySmsLoginCodeTask task = new VerifySmsLoginCodeTask(OrderVerifyPhoneNumerActivity.this, mPhone, mCode, mName, mGender, mCityId, String.valueOf(mClueId), mForemanId);
	    	task.setCallBack(new AbstractHttpResponseHandler<String>() {
				
				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					System.out.println("========t:"+t);
					try {
						JSONObject jsonObject = new JSONObject(t);
						String token = jsonObject.optString("token");
						String user_id = jsonObject.optString("user_id");
						String order_id = jsonObject.optString("order_id");
						userLogin(token,user_id,mPhone);
						BroadCastManager.sendBroadCast(OrderVerifyPhoneNumerActivity.this, BroadCastManager.ACTION_REFRESH_USER_INFO); //刷新个人信息
		             if(order_id != null){
	                	Bundle bundle = new Bundle();
	                	if(TextUtils.isEmpty(mForemanId)){
	                    	bundle.putString(HomeActivity.ROB_ORDER_NOW, HomeActivity.ROB_ORDER_NOW);
	                       	bundle.putString(AppConstants.PARAM_ORDER_ID, String.valueOf(order_id));
	                        ActivityUtil.nextActivityWithClearTop(OrderVerifyPhoneNumerActivity.this, HomeActivity.class, bundle);
	                	}else{
	                	    bundle.putBoolean(AppConstants.PARAM_ORDER_SOURCE_NAME, true);
	                		bundle.putString(AppConstants.PARAM_ORDER_ID, String.valueOf(order_id));
	        				ActivityUtil.next(OrderVerifyPhoneNumerActivity.this, OrderWaitResponseActivity.class,bundle,false);
		                	}
		             }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//httpOrderSubmit();
				}
				
				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					showToastMsg(error);
				}
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					hideWaitDialog();
				}
		});
    	task.send();
		}
 
    }
    
	private void userLogin(String token,String user_id,String phone) {
		User user = new User();
		user.setToken(token);
		user.setUser_id(user_id);
		user.setMobile(phone);
		PrefersUtil.getInstance().setValue("userId", user.getUser_id());
		PrefersUtil.getInstance().setValue("token", user.getToken());
		PrefersUtil.getInstance().setValue("mobile", user.getMobile());
		ZxsqApplication.getInstance().setUser(user, true);
		JPushInterface.resumePush(this);//恢复推送
		setAlias(user);//将用户的id上传到JPush服务器，以便JPush针对单个用户推送消息
	} 
	private void setAlias(User user) {
		String alias = user.getUser_id();
		if(alias!=null){
			if(!Util.isValidTagAndAlias(alias)){
				LogUtil.i("针对JPush,不是有效的别名");
			}else{
				mHandler.sendMessage(mHandler.obtainMessage(WHAT_SET_ALIAS, alias));
			}
		}
	}
    /**
     * http请求-获取验证码
     */
    private void httpRequestGetCheckCode() {
        mPhone = mEtPhome.getText().toString();
    	if(TextUtils.isEmpty(mPhone)){
			showToastMsg("请先输入手机号");
		}else if(!Util.isValidMobileNumber(mPhone)){
			showToastMsg("请输入正确的手机号");
		}else{
	        OrderSendVirefyTask task = new OrderSendVirefyTask(OrderVerifyPhoneNumerActivity.this, mPhone, "", mhouseName, mAddr
	        		, mMeasureTime, mAera, mCityId, "android", mSourceName, mPx, mPy, mForemanId, mDistrict
	        		, mName, mGender, mProvinceName, mCityName);
	        task.setCallBack(new AbstractHttpResponseHandler<String>() {
				
				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					showToastMsg("验证码发送成功!");
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(t);
						mClueId = jsonObject.optInt("clue_id");
						/*mCode = String.valueOf(jsonObject.optInt("verify"));
						mEtCode.setText(mCode);*/
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	            	 mIsContinue = true;
					  mInitTime = 60;
					  new Thread(waitR).start();
				}
				
				@Override
				public void onFailure(int code, String error) {
					// TODO Auto-generated method stub
					showToastMsg(error);
				}
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					hideWaitDialog();
				}
				
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					showWaitDialog("请求中...");
				}
			});
	        task.send();
		}
    }   
    
    /**
     * http请求-下单
     */
    private void httpOrderSubmit() {
//        String name = mEtName.getText().toString();
//        OrderSubmitTask task = new OrderSubmitTask(OrderVerifyPhoneNumerActivity.this, mPhone, "", mhouseName
//        		, mAddr, mMeasureTime, mAera, mCityId, "android", mSourceName,mPx,mPy,mForemanId,mDistrict,mName,mGender);
//        task.setCallBack(new AbstractHttpResponseHandler<Order>() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                showWaitDialog("加载中");
//            }
//
//            @Override
//            public void onSuccess(Order result) {
//                if(result != null){
//                   	Bundle bundle = new Bundle();
//                   	bundle.putString(AppConstants.PARAM_ORDER_ID, String.valueOf(result.getOrder_id()));
//                	bundle.putString(HomeActivity.ROB_ORDER_NOW, HomeActivity.ROB_ORDER_NOW);
//                    ActivityUtil.nextActivityWithClearTop(OrderVerifyPhoneNumerActivity.this, HomeActivity.class, bundle);
//                }
//            }
//
//            @Override
//            public void onFailure(int code, String error) {
//                
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideWaitDialog();
//            }
//        });
//        task.send();
    }
    private void updateTimes() {
        Message msg = mHandler.obtainMessage();
        if (mTimes <= 0) {
            msg.what = 1;
        } else {
            msg.what = 0;
        }
        mHandler.sendMessageDelayed(msg, DELAYED_TIME);
    }

    @SuppressLint("HandlerLeak")
    protected Handler mHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 0:
                    mTimes--;
                    System.out.println("======mTimes:"+mTimes);
                    mBtnGetCode.setText(String.format(getResources().getString(R.string.txt_again_send_code),mTimes));
                    mBtnGetCode.setTextColor(getResources().getColor(R.color.orange_drak));
                    mBtnGetCode.setClickable(false);
                    updateTimes();
                    break;
                case 1:
                    mBtnGetCode.setText(getResources().getString(R.string.sent_verify));
                    mBtnGetCode.setClickable(true);
                    mBtnGetCode.setTextColor(getResources().getColor(R.color.orange_light));
                    break;
                case WHAT_SET_ALIAS://为JPush设置别名
					String alias = (String)msg.obj;
					LogUtil.i("调用JPush API，设置有效的别名");
					JPushInterface.setAliasAndTags(OrderVerifyPhoneNumerActivity.this, alias, null, mTagAliasCallback);
                default:
                    break;
            }
        }

        ;
    };
	  @SuppressLint("HandlerLeak")
	  private Handler mTimeHandler = new Handler() {

		  @Override
		  public void handleMessage(Message msg) {
			  super.handleMessage(msg);
			  int time = msg.arg1;
			  mBtnGetCode.setText("已发送("+time+"s)");
			  mBtnGetCode.setTextColor(getResources().getColor(R.color.orange_drak));
			  if(time  == 0){
				  mBtnGetCode.setClickable(true);
				  mIsContinue = false;
				  mBtnGetCode.setText(getResources().getString(R.string.sent_verify));
				  mBtnGetCode.setTextColor(getResources().getColor(R.color.orange_light));
			  }
		  }

	  };
    Runnable waitR = new Runnable() {

		  @Override
		  public void run() {
			  while(mIsContinue){
				  Message message = mTimeHandler.obtainMessage();
				  message.arg1 = mInitTime;
				  mTimeHandler.sendMessage(message);
				  try {
					  Thread.sleep(1000);
				  } catch (InterruptedException e) {
					  e.printStackTrace();
				  } 
				  --mInitTime;
			  }
		  }
	  };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_code:
            	if(mIsFristGetCode){
            		AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0010);
            		mIsFristGetCode = false;
            	}else{
            		AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0013);
            	}
                httpRequestGetCheckCode();
                break;
            case R.id.btn_submit:
                AnalyticsUtil.onEvent(THIS, AppConstants.UmengEvent.ID_CLICK_0011);
                httpRequestCheck();
                break;
            default:
                break;
        }
    }

}