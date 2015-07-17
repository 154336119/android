package com.huizhuang.zxsq.ui.activity.account;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class SettingEditionActivity extends BaseActivity implements
		OnClickListener {

	private ImageButton btnCheck;
	private TextView tvEdition;
	private CommonActionBar mCommonActionBar;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_edition_activity);
		initActionBar();
		initView();
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_about);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				}
		});
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		tvEdition = (TextView) findViewById(R.id.tv_edition);
		btnCheck = (ImageButton) findViewById(R.id.btn_check);
		tvEdition.setText(Util
				.getCurrentVersionName(SettingEditionActivity.this));
		btnCheck.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_check:
			checkVersion();
			break;
		default:
			break;

		}
	}
	
	  /** 版本检测 */
    private void checkVersion() {
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
			
			@Override
			public void onClick(int arg0) {
				LogUtil.i("checkVersion UmengDialogButtonListener onClick arg0:"+arg0);
				if(arg0==6){
					//以后再说
				}
				else if(arg0==5){//立即更新
					
				}
			}
		});
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
        	
        

            @Override
            public void onUpdateReturned(int updateStatus,
                    UpdateResponse updateInfo) {
            	LogUtil.i("checkVersion updateInfo:"+updateInfo+"  updateStatus:"+updateStatus);
            	if(updateInfo==null){
            		return;
            	}
                if (UmengUpdateAgent.isIgnore(SettingEditionActivity.this, updateInfo)) {
                }
                
                
                switch (updateStatus) {
                case 0:
                	
					break;
				case 1:
				case 2: 
				case 3:
					showToastMsg("已经是最新版本");
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
        
        
        UmengUpdateAgent.forceUpdate(SettingEditionActivity.this);
        //UmengUpdateAgent.update(SplashActivity.this);
        
    }

}
