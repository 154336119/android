package com.huizhuang.zxsq.ui.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.Author;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.Visitor;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.account.FavoriteArticlesActivity;
import com.huizhuang.zxsq.ui.activity.account.FavoritePicturesActivity;
import com.huizhuang.zxsq.ui.activity.account.LoveFamilyProfileActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.diary.DiaryDetailActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.UiUtil;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


/** 
* @ClassName: PersonalHomepageActivity 
* @Description: 其他个人中心主页 （传入参数Constants.PARAM_VISOTOR）
* @author lim 
* @mail limshare@gmail.com   
* @date 2014-11-5 下午5:31:29 
*  
*/
public class PersonalHomepageActivity extends BaseActivity implements OnClickListener{

	private static final String SAVED_INSTANCE = "author";
	
	private final int REQ_LOGIN_CODE = 661;
	
	private Visitor mVisitor;
	private Button mBtnFollow;
	
	private Author mAuthor;
	private Diary mNewsDiary;
	private boolean mIsSelf = false;
	private String mFolledCount;

	private TextView txtFollowedCount;

	private int mFollowedcount;

	private TextView tvFollowCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mVisitor = (Visitor) getIntent().getExtras().getSerializable(AppConstants.PARAM_VISOTOR);
		setContentView(R.layout.personal_homepage_activty);
		
		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.btn_follow).setOnClickListener(this);
		findViewById(R.id.btn_ajda).setOnClickListener(this);
		findViewById(R.id.btn_look_more).setOnClickListener(this);
		findViewById(R.id.btn_favorite_imgs).setOnClickListener(this);
		findViewById(R.id.btn_favorite_articles).setOnClickListener(this);
		
		if (savedInstanceState != null) {
			mAuthor = (Author) savedInstanceState.getSerializable(SAVED_INSTANCE);
		}
		
		initUI();
		loadDetail();
	}
	
	private void initUI() {
		mBtnFollow = (Button) findViewById(R.id.btn_follow);
		ImageView ivHeadImg = (ImageView) findViewById(R.id.iv_headimg);
		ImageLoader.getInstance().displayImage(mVisitor.getAvatar(), ivHeadImg);
	}

	private void loadDetail(){
		showWaitDialog("");
		RequestParams params = new RequestParams();
		params.put("profile_id", mVisitor.getId());
		HttpClientApi.post(HttpClientApi.REQ_ACCOUNT_USER, params, new ResultParser(), new RequestCallBack<Result>() {
			
			@Override
			public void onSuccess(Result result) {
				try{
					processResult(result.data);
				} catch (JSONException e) {
					e.printStackTrace();
					showToastMsg("数据格式化错误");
				}
			}
			
			@Override
			public void onFailure(NetroidError error) {
				showToastMsg(error.getMessage());
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
			}
			
		});
	}
	
	/**
	 * 关注用户
	 */
	private void follow(){
		if (!ZxsqApplication.getInstance().isLogged()) {
			ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
			return;
		}
		
		//自己不能关注自己
		if (mVisitor.getId().equals(ZxsqApplication.getInstance().getUser().getId())) {
			return;
		}
		
		showWaitDialog("");
		RequestParams params = new RequestParams();
		if (ZxsqApplication.getInstance().isLogged()) {
			params.put("user_id", ZxsqApplication.getInstance().getUser().getId());
		}
		String url = null;
		mFollowedcount = Integer.parseInt(mFolledCount);
		if (mVisitor.isFollowed()) {
			url = HttpClientApi.REQ_USER_DELETE_FRIENDS;
			params.put("followings", "[" + mVisitor.getId() + "]");
			mFollowedcount-=1;
			
		} else {
			url = HttpClientApi.REQ_USER_SAVE_FRIENDS;
			params.put("friends", "[" + mVisitor.getId() + "]");
			mFollowedcount+=1;
		}
		HttpClientApi.post(url, params, new RequestCallBack<String>(){
			
			@Override
			public void onSuccess(String data) {
				if (mVisitor.isFollowed()) {
					mVisitor.setFollowed(false);
					showToastMsg("取消关注成功");
				}else{
					mVisitor.setFollowed(true);
					showToastMsg("关注成功");
				}
				updateFollowBtn();
			}
			
			@Override
			public void onFailure(NetroidError arg0) {
				showToastMsg(arg0.getMessage());
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
			}
			
		});
	}
	
	private void processResult(String result) throws JSONException {
		JSONObject dataJson = new JSONObject(result);
		//格式化用户详情
		String id = dataJson.optString("user_id");
		String nickname = dataJson.optString("nickname");
		String imagePath = dataJson.getString("user_thumb");
		String sex = dataJson.getString("gender");
		String province = dataJson.optString("province");
		String city = dataJson.optString("city");
		String roomStyle = dataJson.optString("room_style");
		String roomType = dataJson.optString("room_type");
		
		boolean isFollowed = false;
		if (!TextUtils.isEmpty(dataJson.getString("is_followed"))) {
			isFollowed = dataJson.getInt("is_followed") == 0 ? false : true;
		}
		mIsSelf = dataJson.getInt("is_self") == 0 ? false : true;
		
		String followCount = dataJson.optString("following_count");
		String followedCount = dataJson.optString("followed_count");
		mFolledCount=followedCount;
		String viewCount = dataJson.optString("view_count");
		
		if (dataJson.has("latest_diary") && dataJson.getJSONObject("latest_diary").length() > 0) {
			JSONObject diaryJson = dataJson.getJSONObject("latest_diary");
			String diaryWeather = diaryJson.optString("weather");
			String diaryCity = diaryJson.optString("city");
			String diaryDatetime = diaryJson.optString("add_time");
			String diaryContent = diaryJson.optString("content");
			JSONArray altasArray = diaryJson.optJSONArray("images");
			int length = altasArray.length();
			ArrayList<Atlas> arrayList = new ArrayList<Atlas>();
			for (int i = 0; i < length; i++) {
				JSONObject imageJson = altasArray.getJSONObject(i);
				Atlas atlas = new Atlas();
				atlas.setId(imageJson.getString("id"));
				atlas.setImage(imageJson.getJSONObject("path").getString("img_path"));
				arrayList.add(atlas);
			}
			mNewsDiary = new Diary();
			mNewsDiary.setWeather(diaryWeather);
			mNewsDiary.setCity(diaryCity);
			mNewsDiary.setContent(diaryContent);
			mNewsDiary.setDatetime(diaryDatetime);
			mNewsDiary.setAtlass(arrayList);
		}
		
		mAuthor = new Author();
		mAuthor.setId(id);
		mAuthor.setName(nickname);
		mAuthor.setAvatar(imagePath);
		mAuthor.setGender(sex);
		mAuthor.setProvince(province);
		mAuthor.setCity(city);
		mAuthor.setRoomStyle(roomStyle);
		mAuthor.setRoomType(roomType);
		mAuthor.setFollowed((isFollowed ? "1" : "0")); // 是否已关注 1 是 0 否
		
		mVisitor.setName(nickname);
		mVisitor.setSex(sex);
		mVisitor.setProvince(province);
		mVisitor.setCity(city);
		mVisitor.setFollowed(isFollowed);

		updateFollowBtn();
		updateUserInfoView(followedCount, followCount, viewCount);
		udpateDiaryView(mNewsDiary);
	}
	
	/**
	 * 更新用户信息
	 */
	private void updateUserInfoView(String followCount, String followedCount, String viewCount){
		TextView tvName = (TextView) findViewById(R.id.tv_name);
		TextView tvProvince = (TextView) findViewById(R.id.tv_province);
		TextView tvCity = (TextView) findViewById(R.id.tv_city);
		ImageView ivSex = (ImageView) findViewById(R.id.iv_sex);
		tvName.setText(mVisitor.getName());
		tvProvince.setText(mVisitor.getProvince());
		tvCity.setText(mVisitor.getCity());
		if (mVisitor.getSex().equals("1")) {
			ivSex.setImageResource(R.drawable.icon_boy);
		}else{
			ivSex.setImageResource(R.drawable.icon_girl);
		}
		tvFollowCount = (TextView) findViewById(R.id.tv_follow_he);
		txtFollowedCount = (TextView) findViewById(R.id.tv_he_follow);
		TextView tvVisitorCount = (TextView) findViewById(R.id.tv_visitor_num);

		tvFollowCount.setText(followCount);
		txtFollowedCount.setText(followedCount);
		tvVisitorCount.setText(viewCount + "人访问");
	}
	
	/**
	 * 更新显示最新的一条日记
	 * @param diary
	 */
	private void udpateDiaryView(Diary diary){
		if (diary == null) {
			findViewById(R.id.ll_diary_content).setVisibility(View.GONE);
		}else{
			findViewById(R.id.ll_diary_content).setVisibility(View.VISIBLE);
			TextView tvDate = (TextView) findViewById(R.id.tv_date);
			TextView tvAddress = (TextView) findViewById(R.id.tv_address);
			TextView tvWeather = (TextView) findViewById(R.id.tv_weather);
			TextView tvContent = (TextView) findViewById(R.id.tv_content);
			LinearLayout llImageContent = (LinearLayout) findViewById(R.id.ll_image_content);
			if(diary.getDatetime()!=null){
				tvDate.setText(DateUtil.timestampToSdate(diary.getDatetime(), "yyyy-MM-dd"));
			}
			tvAddress.setText(diary.getCity());
			tvWeather.setText(diary.getWeather());
			tvContent.setText(diary.getContent());
			
			int length = diary.getAtlass().size();
			DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.global_defaultmain)
					.showImageForEmptyUri(R.drawable.global_defaultmain)
					.imageScaleType(ImageScaleType.NONE) // 设置图片缩放
					.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
					.cacheOnDisk(true).build();
			int width = UiUtil.getScreenWidth(THIS);
			LayoutParams lp = new LayoutParams(width, width/2);
			lp.setMargins(0, 3, 0, 0);
			for (int i = 0; i < length; i++) {
				Atlas atlas = diary.getAtlass().get(i);
				ImageView iv = new ImageView(THIS);
				iv.setScaleType(ScaleType.FIT_XY);
				iv.setLayoutParams(lp);
				llImageContent.addView(iv);
				ImageLoader.getInstance().displayImage(atlas.getImage(), iv, mOptions);
			}
		}
	}
	
	/**
	 * 更新关注按钮
	 */
	private void updateFollowBtn(){
		//当前登录用户，自己不能关注自己
		if (mIsSelf) {
			mBtnFollow.setVisibility(View.GONE);
		}else{
			if (mVisitor.isFollowed()) {
				mBtnFollow.setText("-取消关注");
			}else{
				mBtnFollow.setText("+关注");
			}
		}
		if(txtFollowedCount!=null){
			mFolledCount=mFollowedcount+"";
			tvFollowCount.setText(mFollowedcount+"");
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(SAVED_INSTANCE, mAuthor);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_follow:
			follow();
			break;
		case R.id.btn_ajda:
			Bundle bundlelf=new Bundle();
			bundlelf.putString("userId", mVisitor.getId());
			LogUtil.i("jiengyh btn_favorite_imgs mVisitor.getId(:"+mVisitor.getId());
			ActivityUtil.next(THIS, LoveFamilyProfileActivity.class,bundlelf,-1);
			break;
		case R.id.btn_look_more:
			Bundle bd = new Bundle();
			bd.putSerializable(AppConstants.PARAM_AUTHOR, mAuthor);
			ActivityUtil.next(THIS, DiaryDetailActivity.class, bd, -1);
			break;
		case R.id.btn_favorite_imgs:
			Bundle bundle=new Bundle();
			bundle.putString("userId", mVisitor.getId());
			ActivityUtil.next(THIS, FavoritePicturesActivity.class,bundle,-1);
			break;
		case R.id.btn_favorite_articles:
			Bundle bundleFav=new Bundle();
			bundleFav.putString("userId", mVisitor.getId());
			ActivityUtil.next(THIS, FavoriteArticlesActivity.class,bundleFav,-1);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_LOGIN_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				//是否是当前登录账户
				if (mVisitor.getId().equals(ZxsqApplication.getInstance().getUser().getId())) {
					mIsSelf = true;
				}
				updateFollowBtn();
				follow();
			}
		}
	}
}
