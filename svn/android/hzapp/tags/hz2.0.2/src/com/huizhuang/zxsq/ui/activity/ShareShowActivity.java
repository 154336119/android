package com.huizhuang.zxsq.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cn.sharesdk.douban.Douban;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.config.PreferenceConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.Share;
import com.huizhuang.zxsq.share.OnekeyShare;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.ShareShowListAdapter;
import com.huizhuang.zxsq.utils.LogUtil;

/**
 * 
 * @ClassName: ShareShowActivity
 * @Description: 有界面的公共分享类 调用的时候必须传入
 *               share对象，在里面设置要分享的参数，不设置就分享默认的内容，share对象的imageUrl和imagePath必须有一项不位空
 *               ，imageUrl的优先级高于imagePath 如果需要回调结果必须设置sahre对象里面的isCallBack属性
 * @author th
 * @mail 342592622@qq.com
 * @date 2014-11-20 上午9:46:18
 * 
 */
public class ShareShowActivity extends BaseActivity implements OnClickListener,
		PlatformActionListener, Callback {

	private final static int MSG_CCALLBACK_SUCCESS = 1;// 分享成功
	private final static int MSG_CCALLBACK_ERROR = 2;// 分享失败
	private final static int MSG_CCALLBACK_CANCEL = 3;// 分享取消
	private static final int MSG_WECHAT_EXSIT = 6;// 没有安装微信客户端

	private GridView mGridView;
	private List<Share> mListShares;
	private ShareShowListAdapter mAdapter;
	private Share mShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("onCreate Bundle = " + savedInstanceState);

		setContentView(R.layout.share_show_dialog);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		getWindow().setAttributes(lp);
		initExtraData();
		initView();
		initListener();
		initGridView();
	}

	private void initExtraData() {
		if (getIntent().getExtras().getSerializable(AppConstants.PARAM_SHARE) == null) {
			mShare = new Share();
		} else {
			mShare = (Share) getIntent().getExtras().getSerializable(
					AppConstants.PARAM_SHARE);
		}
	}

	private void initView() {
		mGridView = (GridView) findViewById(R.id.gdv_share);
	}

	private void initListener() {
		findViewById(R.id.tv_cancel).setOnClickListener(this);
	}

	private void initGridView() {
		mListShares = new ArrayList<Share>();
		for (int i = 0; i < 3; i++) {
			Share share = new Share();
			switch (i) {
			case 0:
				//新浪微博
				share.setPlatformName(SinaWeibo.NAME);
				share.setImgResouceId(R.drawable.logo_sinaweibo);
				share.setName(getResources().getString(R.string.sinaweibo));
				break;
			case 1:
				//微信朋友圈
				share.setPlatformName(WechatMoments.NAME);
				share.setImgResouceId(R.drawable.logo_wechatmoments);
				share.setName(getResources().getString(R.string.wechatmoments));
				break;
			case 2:
				//微信好友
				share.setPlatformName(Wechat.NAME);
				share.setImgResouceId(R.drawable.logo_wechat);
				share.setName(getResources().getString(R.string.wechat));
				break;
			case 3:
				share.setPlatformName(QZone.NAME);
				share.setImgResouceId(R.drawable.logo_qzone);
				share.setName(getResources().getString(R.string.qzone));
				break;
			case 4:
				share.setPlatformName(Renren.NAME);
				share.setImgResouceId(R.drawable.logo_renren);
				share.setName(getResources().getString(R.string.renren));
				break;
			case 5:
				share.setPlatformName(Douban.NAME);
				share.setImgResouceId(R.drawable.logo_douban);
				share.setName(getResources().getString(R.string.douban));
				break;
			default:
				break;
			}
			mListShares.add(share);
			share = null;
		}
		mAdapter = new ShareShowListAdapter(this);
		mAdapter.setList(mListShares);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				LogUtil.i("onItemClick position：" + position);
				shareTo(mListShares.get(position).getPlatformName());
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel:
			finish();
			break;
		default:
			break;
		}
	}

	// 分享方法
	private void shareTo(String platformName) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher, "分享中...");
		LogUtil.e("shareTo():", platformName + "");
/*		if (WechatMoments.NAME.equals(platformName)) {//微信朋友圈
			oks.setTitle(mShare.getText());
		} else {
			oks.setTitle(mShare.getTitle());
		}
		oks.setTitleUrl(mShare.getTitleUrl());
		oks.setText(mShare.getText());// 分享内容
*/		
		oks.setTitle(mShare.getTitle());
		oks.setTitleUrl(mShare.getTitleUrl());
		if (SinaWeibo.NAME.equals(platformName)) {//新浪微博
			String text = new String();
			if(PreferenceConfig.getShareByForeman()){
				text = "我在#惠装APP#上找到了一个非常靠谱的工长"+PreferenceConfig.getShareByForemanName()+
						"，人品好服务棒！装修的人都在用这个软件，比传统装修节省40%的费用！强烈推荐你试试~";
				//PreferenceConfig.setShareByForeman(false);
			}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_COMMON){
				text = "#惠装#【我是惠装，比装修公司省40%，工薪阶层的装修首选！】"
						+ "下载惠装APP，足不出户，在线就能查看工地和验收报告，不能更洋气~猛戳下载→_→：";
			}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_START_WORKING){
				text = "#惠装#【比装修公司省40%，工薪阶层装修首选】"
						+ "耶~我家新房开工装修啦！想看我家啥样？猛戳下载最洋气的惠装APP，立马在线看装修进度照片→_→：";
			}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_WATER){
				text = "#惠装#【比装修公司省40%，工薪阶层装修首选】"
						+ "hoho~惠装工长刚给我发了水电验收报告，你想看我家装修进度照吗？猛戳下载最洋气的惠装APP→_→：";
			}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_WOOD){
				text = "#惠装#【比装修公司省41%，工薪阶层装修首选】"
						+ "hoho~刚收到惠装工长给我发的泥木验收报告，你想看我家装修进度照吗？猛戳下载最洋气的惠装APP→_→：";
			}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_PAINT){
				text = "#惠装#【比装修公司省42%，工薪阶层装修首选】"
						+ "hoho~我家装修油漆阶段达成！惠装工长刚给我发了水电验收报告，猛戳下载惠装APP，在线看现场照片→_→：";
			}else if(PreferenceConfig.getShareByOrderState()==AppConstants.SHARE_BY_ORDER_COMPLETE){
				text = "#惠装#【比装修公司省43%，工薪阶层装修首选】"
						+ "yoho~我家新房竣工啦！惠装工长刚给我发了竣工报告，想看我家装修毕业照？猛戳下载惠装APP吧！→_→：";
			}
			text=text+AppConfig.SHARE_URL;
			mShare.setText(text);
		} 
		PreferenceConfig.setShareByForeman(false);
		oks.setText(mShare.getText());
		LogUtil.i("mShare.getImageUrl():" + mShare.getImageUrl()
				+ "  mShare.getImagePath(): " + mShare.getImagePath());
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
		oks.setPlatform(platformName);
		oks.setAddress(mShare.getAddress());
		oks.setVenueName(mShare.getVenueName());
		oks.setVenueDescription(mShare.getVenueDescription());
		// 令编辑页面显示为Dialog模式
		//oks.setDialogMode();
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
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
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
		if ("WechatClientNotExistException"
				.equals(t.getClass().getSimpleName())) {
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
			if (!mShare.isCallBack()) {
				showToastMsg("分享成功");
			}
			String fName = msg.obj.getClass().getSimpleName();
			int shareType = 0;
			if ("SinaWeibo".equals(fName)) {
				shareType = 3;
			} else if ("QZone".equals(fName)) {
				shareType = 4;
			} else if ("Wechat".equals(fName)) {
				shareType = 1;
			} else if ("WechatMoments".equals(fName)) {
				shareType = 2;
			} else if ("Renren".equals(fName)) {
				shareType = 6;
			} else if ("Douban".equals(fName)) {
				shareType = 5;
			}
			Intent intent = getIntent();
			intent.putExtra("shareType", shareType);
			// 成功
			setResult(RESULT_OK, intent);
			finish();
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

}
