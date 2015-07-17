package com.huizhuang.zxsq.ui.activity.diary;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;

import cn.sharesdk.tencent.qq.QQ;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

/** 
* @ClassName: TipsFriendsOnlookersActivity 
* @Description: 邀请好友围观
* @author lim 
* @mail limshare@gmail.com   
* @date 2014-11-17 上午11:21:18 
*  
*/
public class TipsFriendsOnlookersActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_tips_friends_onlookers);
		initActionBar();
		initViews();
	}
	
	private void initActionBar(){
		CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		commonActionBar.setActionBarTitle("好友围观");
		commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initViews(){
		findViewById(R.id.btn_qq_friends).setOnClickListener(this);
		findViewById(R.id.btn_select).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_qq_friends:
			openWeiXin();
			break;
		case R.id.btn_select:
			openPhoneContract();
			break;

		default:
			break;
		}
	}
	
	private void openPhoneContract(){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(ContactsContract.Contacts.CONTENT_URI);
		startActivity(intent);
	}
	
	private void openWeiXin(){
		Share share = new Share();
		share.setPlatformName(QQ.NAME);
		share.setImageUrl("1");
		share.setImagePath("1");
		share.setText(getString(R.string.txt_share_bmcg));
		Util.showShare(true, this, share);
	}
	
}
