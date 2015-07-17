package com.huizhuang.zxsq.ui.activity.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.config.AppConfig;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.AtlasGroup;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.module.RankDetail;
import com.huizhuang.zxsq.module.Share;
import com.huizhuang.zxsq.module.parser.AtlasBrowseGroupParser;
import com.huizhuang.zxsq.module.parser.CompanyDetailParser;
import com.huizhuang.zxsq.ui.activity.AtlasBrowseActivity;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.ui.adapter.CompanyBrowseViewPagerAdapter;
import com.huizhuang.zxsq.ui.adapter.CompanyBrowseViewPagerAdapter.OnFirstCreateBitmapListener;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.DateUtil;
import com.huizhuang.zxsq.utils.ImageLoaderOptions;
import com.huizhuang.zxsq.utils.ImageUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.MyScrollView;
import com.huizhuang.zxsq.widget.MyScrollView.OnScrollListener;
import com.huizhuang.zxsq.widget.TasksCompletedView;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * @ClassName: CompanyDetailActivity
 * @Package com.huizhuang.zxsq.ui.activity
 * @Description: 公司详情
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-9-3  下午3:08:31
 */
@SuppressLint({ "UseValueOf", "ResourceAsColor" })
public class CompanyDetailActivity extends BaseActivity implements OnClickListener{
	private TextView tvName;   //公司名
	private TextView tvMoney; //预算
	private TextView tvCp; //预算
	private ImageView cvIcon; //公司LOGO
	private TextView tvDistance; //距离
	private RatingBar rbScore; //评分
	private TextView tvScore; //综合评分
	private TextView tvOrderNum; //预约人数
	private ImageView ivLocation;
	//电话、写日记、收藏
	private ImageButton ibCallPhone; 
	private ImageButton ibWriteComment;
	private ImageButton ibCollection;
	private TextView tvAddr;//地址
	//推荐指数
	private TasksCompletedView pgServiceAttitude;
	private TasksCompletedView pgProfessional_bility;
	private TasksCompletedView pgIssues;
	private TasksCompletedView pgProgressProject;
	//店铺评论
	private RelativeLayout rlComment;	
	private ImageView cvCommenticon;
	private TextView tvCommentname;
	private RatingBar rbCommentscore;
	private TextView tvCommentcomment;
	private TextView tvCommenttime;
	private TextView tvAllComment;	
	//所属日记
	private LinearLayout llRenovationDiary;//装修日记
	private LinearLayout llSupervisorDiary;//监理日记
	private TextView tvSupervisorDiary;
	private TextView tvRenovationDiary;	
	//公司简介
	private TextView tvIntroduction;
	private TextView tvAllIntroduction; 
	private TextView tvPartIntroduction;
	
	public static final int WRITE_DIARY_REFRESH = 0;
	private CommonActionBar mCommonActionBar;
	private Company mCompany;
	private Atlas mAtlas;
	private ImageView mIvPhoto;
	private ArrayList<Atlas> mAtlasList;
	private Dialog mDialog; //电话dialog
	private DataLoadingLayout mDataLoadingLayout;
	private final int REQ_LOGIN_CODE = 661;
	private final int REQ_DISCUSS = 662;
	
	private MyScrollView myScrollView;
	private ImageView imFloatView;
	private ImageView imTitleFloatView;
	private Bitmap mActionbarBackgroundBitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_detail);
		getIntentExtras();
		initActionBar();
		initDataLoadingLayout();
		initView();
        httpRequestCompantDetail();
	}

	private void initDataLoadingLayout() {
		mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.favorite_pictures_data_loading_layout);

	}

//	private void initViewPager(){
//		mViewPager = (com.huizhuang.zxsq.widget.HackyViewPager)findViewById(R.id.iv_photo);
//		mViewPager.setOnClickListener(this);
//		mAtlasList = new ArrayList<Atlas>();
//		mViewPager.setBackgroundResource(R.drawable.bg_photo_default);
//		mAdapter = new CompanyBrowseViewPagerAdapter(this, mAtlasList);
//		mAdapter.setOnFirstCreateBitmapListener(new OnFirstCreateBitmapListener() {
//			
//			@Override
//			public void OnFirstCreateBitmap(Bitmap bitmap) {
//				// TODO Auto-generated method stub
//				//mImageView.setImageBitmap(bitmap);
//				mCommonActionBar.setBackgroundDrawable(new BitmapDrawable(bitmap));
//			}
//		});
//		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
//			
//			@Override
//			public void onPageSelected(int arg0) {
//				// TODO Auto-generated method stub
//				 Log.d("MyScrollView", "onPageSelected:"+arg0+"");
//				 if(arg0!=lastPage&&mAdapter.getImagemap()!=null){
//					 ArrayList<String> keyList = new ArrayList<String>();
//					 Iterator iter = mAdapter.getImagemap().entrySet().iterator();
//					 while (iter.hasNext()) {    
//					        java.util.Map.Entry entry = (Map.Entry) iter.next();    
//					        keyList.add(String.valueOf(entry.getKey()));   
//					        Log.d("MyScrollView", "key:"+entry.getKey()+",value:"+entry.getValue());
//					    }  
//					 lastPage = arg0;
//					// mImageView.setImageBitmap(mAdapter.getImagemap().get(keyList.get(arg0)));
//					 mCommonActionBar.setBackgroundDrawable(new BitmapDrawable(mAdapter.getImagemap().get(keyList.get(arg0))));
//				 }
//			}
//			
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		mViewPager.setAdapter(mAdapter);
//	}

	private void initView(){
		mIvPhoto = (ImageView)findViewById(R.id.iv_photo);
		tvName = (TextView)findViewById(R.id.tv_name);   //公司名
		tvMoney = (TextView)findViewById(R.id.tv_money); //预算
		tvCp = (TextView)findViewById(R.id.tv_cp); //预算
		cvIcon = (ImageView)findViewById(R.id.cv_icon); //公司LOGO
		tvDistance = (TextView)findViewById(R.id.tv_distance); //距离
		rbScore = (RatingBar)findViewById(R.id.rb_score); //评分
		tvScore  = (TextView)findViewById(R.id.tv_score); //综合评分
		tvOrderNum = (TextView)findViewById(R.id.tv_order_num); //预约人数
		ivLocation = (ImageView)findViewById(R.id.iv_location);
		//电话、写日记、收藏
		ibCallPhone = (ImageButton)findViewById(R.id.ib_call_phone); 
		ibWriteComment = (ImageButton)findViewById(R.id.ib_write_comment);
		ibCollection = (ImageButton)findViewById(R.id.ib_collection);
		tvAddr = (TextView)findViewById(R.id.addr);//地址
		//推荐指数
		pgServiceAttitude= (TasksCompletedView)findViewById(R.id.pg_service_attitude);
		pgProfessional_bility= (TasksCompletedView)findViewById(R.id.pg_professional_bility);
		pgIssues = (TasksCompletedView)findViewById(R.id.pg_issues);
		pgProgressProject= (TasksCompletedView)findViewById(R.id.pg_progress_project);
		//店铺评论
		rlComment = (RelativeLayout)findViewById(R.id.rl_comment);	
		cvCommenticon = (ImageView)findViewById(R.id.cv_commenticon);
		tvCommentname = (TextView)findViewById(R.id.tv_commentname);
		rbCommentscore = (RatingBar)findViewById(R.id.rb_commentscore);
		tvCommentcomment = (TextView)findViewById(R.id.tv_commentcomment);
		tvCommenttime = (TextView)findViewById(R.id.tv_commenttime);
		tvAllComment = (TextView)findViewById(R.id.all_comment);	
		//所属日记
		llRenovationDiary= (LinearLayout)findViewById(R.id.ll_renovation_diary);//装修日记
		llSupervisorDiary= (LinearLayout)findViewById(R.id.ll_supervisor_diary);//监理日记
		tvSupervisorDiary = (TextView)findViewById(R.id.tv_supervisor_diary);
		tvRenovationDiary = (TextView)findViewById(R.id.tv_renovation_diary);	
		//公司简介
		tvIntroduction = (TextView)findViewById(R.id.tv_introduction);
		tvAllIntroduction = (TextView)findViewById(R.id.tv_all_introduction); 
		tvPartIntroduction = (TextView)findViewById(R.id.tv_part_introduction);	
		imFloatView = (ImageView)findViewById(R.id.iv_float);
		imTitleFloatView= (ImageView)findViewById(R.id.iv_title_float);
		imFloatView.getBackground().setAlpha(0);
		myScrollView = (MyScrollView)findViewById(R.id.scrollView);
		myScrollView.getScrollY();
		myScrollView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScroll(int scrollY) {
				// TODO Auto-generated method stub
				if(scrollY<286)
					imFloatView.getBackground().setAlpha((int) (scrollY*0.6));
				 //	mCommonActionBar.setBackgroundColor(R.color.white);

				if(scrollY>286){
					//mCommonActionBar.setBackgroundDrawable(new BitmapDrawable(mActionbarBackgroundBitmap));
					mCommonActionBar.setBackgroundColor(R.color.black_transparent);
				//	mCommonActionBar.getBackground().setAlpha((int) (286*0.6));
//					ivTitleFloat.setVisibility(View.VISIBLE);
//					ivTitleFloat.setAlpha(222);
					//imTitleFloatView.setVisibility(View.VISIBLE);
				}
				if(scrollY<286){
					mCommonActionBar.setBackgroundDrawable(null);
					//imTitleFloatView.setVisibility(View.GONE);
				}
				if(scrollY<0)
					imFloatView.getBackground().setAlpha(0);
				
			}
		}); 
	}
	/**
	 * 更新页面公司信息
	 */
	private void updataCompanyInfo() {
		// TODO Auto-generated method stub
		//公司介绍
		//initViewPager();

		//设置头像
		ImageUtil.displayImage(mCompany.getLogoImage(), cvIcon, ImageLoaderOptions.optionsUserHeader);
		//计算距离
		if(ZxsqApplication.getInstance().getUserPoint()!=null&&!TextUtils.isEmpty(mCompany.getPy())&&!TextUtils.isEmpty(mCompany.getPx())){
			LatLng start = ZxsqApplication.getInstance().getUserPoint();
			LatLng end = new LatLng(Double.parseDouble(mCompany.getPy()), Double.parseDouble(mCompany.getPx()));
			double distance = DistanceUtil.getDistance(start, end)/1000;
			String dis = String.valueOf(distance);
			dis = dis.substring(0,dis.indexOf(".")+2 );
			tvDistance.setText(dis+"km");
			ivLocation.setVisibility(View.VISIBLE);
			ivLocation.setOnClickListener(this);
		}else{
			tvDistance.setVisibility(View.GONE);
		}
		//该公司是否收藏
        if(mCompany.getIsFavored()){
       	 ibCollection.setImageDrawable(getResources().getDrawable(R.drawable.company_icon03_on));
        }
        //设置地址
		tvName.setText(mCompany.getFullName());
		//设置报价
		if("0".equals(mCompany.getContractPrice())){
			tvMoney.setText("暂无报价");
			tvCp.setText("暂无报价");
		}else{
			tvMoney.setText("￥"+mCompany.getContractPrice());
			tvCp.setText("￥"+mCompany.getContractPrice());
		}
		tvOrderNum.setText(mCompany.getOrderNum());
		tvAddr.setText(mCompany.getAddress());
		rbScore.setRating(mCompany.getScore());
		tvScore.setText("综合得分：" + String.valueOf(mCompany.getRank()));

      
		if(mCompany.getRankDetailList()!=null){
         for(int i=0;i<mCompany.getRankDetailList().size();i++){
        	 RankDetail mRankDetail= mCompany.getRankDetailList().get(i);
        	 if(mRankDetail.getName().equals("服务态度")){
        		 pgServiceAttitude.setProgress(Integer.parseInt(mRankDetail.getRecomment()));
        	 }else if(mRankDetail.getName().equals("专业能力")){
        		 pgProfessional_bility.setProgress(Integer.parseInt(mRankDetail.getRecomment()));
        	 }else if(mRankDetail.getName().equals("沟通情况")){
        		 pgIssues.setProgress(Integer.parseInt(mRankDetail.getRecomment()));
        	 }else if(mRankDetail.getName().equals("工程进度")){
        		 pgProgressProject.setProgress(Integer.parseInt(mRankDetail.getRecomment()));
        	 }
         }		
		}
		
		//设置店铺评论
        if(!"0".equals(mCompany.getDiscussNum())){
        	rlComment.setVisibility(View.VISIBLE);
    		ImageView cvCommenticon = (ImageView)findViewById(R.id.cv_commenticon);
    		TextView tvCommentname = (TextView)findViewById(R.id.tv_commentname);
    		RatingBar rbCommentscore = (RatingBar)findViewById(R.id.rb_commentscore);
    		TextView tvCommentcomment = (TextView)findViewById(R.id.tv_commentcomment);
    		TextView tvCommenttime = (TextView)findViewById(R.id.tv_commenttime);
    		TextView tvAllComment = (TextView)findViewById(R.id.all_comment);
  		    ImageUtil.displayImage(mCompany.getLatestComment().getUserThumb(), cvCommenticon, ImageLoaderOptions.optionsUserHeader);
 	     	tvCommentname.setText(mCompany.getLatestComment().getUsername());
 	    	rbCommentscore.setRating(mCompany.getLatestComment().getScore());
 	    	tvCommentcomment.setText(mCompany.getLatestComment().getContent());
	    	String date = DateUtil.timestampToSdate(mCompany.getLatestComment().getDatetime(), "yyyy-MM-dd HH:mm:ss");
 	    	tvCommenttime.setText(DateUtil.friendlyTime(date));
	    	tvAllComment.setText("查看所有"+mCompany.getDiscussNum()+"条口碑评价"); 
	    	tvAllComment.setOnClickListener(this);
         }
        
		//日记是否为空
		if(mCompany.getSupervisionDiaryCount()==null||mCompany.getSupervisionDiaryCount().equals("0")){
			tvSupervisorDiary.setText("0");
			}else{
				tvSupervisorDiary.setText(mCompany.getSupervisionDiaryCount());
				llSupervisorDiary.setOnClickListener(this);
			}
		if(mCompany.getRenovatioDiaryCount()==null||mCompany.getRenovatioDiaryCount().equals("0")){
			tvRenovationDiary.setText("0");
			}else{
				tvRenovationDiary.setText(mCompany.getRenovatioDiaryCount());
				llRenovationDiary.setOnClickListener(this);
			}
         //公司介绍
         tvIntroduction.setText(mCompany.getIntro());	
         Button btnAppointment = (Button)findViewById(R.id.btn_appointment);
         tvAllIntroduction.setOnClickListener(this);
         tvPartIntroduction.setOnClickListener(this);
         tvIntroduction.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LogUtil.d("tvIntroduction.getLineCount():"+tvIntroduction.getLineCount());
				if(tvIntroduction.getLineCount()>=7)
				tvAllIntroduction.setVisibility(View.VISIBLE);
			}
		});
         btnAppointment.setOnClickListener(this);
         ibWriteComment.setOnClickListener(this);
         ibCallPhone.setOnClickListener(this);
         ibCollection.setOnClickListener(this);
         llRenovationDiary.setOnClickListener(this);
         llSupervisorDiary.setOnClickListener(this);
         //展开收起功能
         tvAllIntroduction.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvPartIntroduction.setVisibility(View.VISIBLE);
				tvAllIntroduction.setVisibility(View.GONE);
				tvIntroduction.setMaxLines(200);
				tvIntroduction.setEllipsize(null);

			}
		});
         tvPartIntroduction.setOnClickListener(new OnClickListener() {
 			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tvPartIntroduction.setVisibility(View.GONE);
				tvAllIntroduction.setVisibility(View.VISIBLE);
				tvIntroduction.setMaxLines(7);
				tvIntroduction.setEllipsize(TruncateAt.END);
			}
		});
	}



	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.txt_title_select_company_detail);
		mCommonActionBar.setRightImgBtn(R.drawable.atlas_share, new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Share share = new Share();
//				String url=AppConfig.URL_SHARE_COMPANY+mCompany.getId();
//				share.setUrl(url);
//				share.setSite(url);
//				share.setSiteUrl(url);
				String txt=String.format(getString(R.string.txt_share_zxgs), mCompany.getFullName());
				share.setText(txt);
				Util.showShare(false, CompanyDetailActivity.this, share);
			}
		});
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				}
		});																																																																																																																																																																																																																																																																																																									
	}



	private void getIntentExtras() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if (intent != null) {
			mCompany = (Company) intent.getExtras().getSerializable(AppConstants.PARAM_COMPANY);
			//mAtlas = (Atlas) getIntent().getExtras().getSerializable(Constants.PARAM_ATLAS);

		}
	}



	private void httpRequestCompantDetail(){
		RequestParams params = new RequestParams();
		params.put("store_id", mCompany.getId());
		HttpClientApi.post(HttpClientApi.REQ_COMPANY_DETAIL, params, new CompanyDetailParser(), new RequestCallBack<Company>() {

			@Override
			public void onSuccess(Company company) {
				mDataLoadingLayout.showDataLoadSuccess();
				mCompany = company;
				mAtlas=mCompany.getAtlas();
				updataCompanyInfo();
				queryAtlasData(); 
				
			}

			@Override
			public void onFailure(NetroidError error) {
				mDataLoadingLayout.showDataLoadFailed(error.getMessage());
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			@Override
			public void onStart() {
				super.onStart();
				mDataLoadingLayout.showDataLoading();
			}
		});
	}
	
	/**
	 * 获取图集效果图列表
	 */
	private void queryAtlasData() {
		if (mAtlas == null || mAtlas.getAlbumId() == null)
			return;
		showWaitDialog("");
		RequestParams params = new RequestParams();
		params.put("album_id", mAtlas.getAlbumId());
		HttpClientApi.get(HttpClientApi.REQ_ATLAS_LIST, params, new AtlasBrowseGroupParser(), new RequestCallBack<AtlasGroup>() {

			@Override
			public void onSuccess(final AtlasGroup result) {
				if (result.size() > 0) {
					ImageUtil.displayImage(result.get(0).getImage(), mIvPhoto, ImageLoaderOptions.optionsDefaultEmptyPhoto,new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							Log.d("MyScrollView", "loadedImage.getHeight():"+loadedImage.getHeight());
							mActionbarBackgroundBitmap = loadedImage.createBitmap(loadedImage, 0, loadedImage.getHeight()-91, loadedImage.getWidth(), 91);
						//	mCommonActionBar.setBackgroundDrawable(new BitmapDrawable(mActionbarBackgroundBitmap));
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
					});
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

	
	


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//全部评论
		case R.id.all_comment:
			if(!"0".equals(mCompany.getDiscussNum())){
			Bundle bd2 = new Bundle();
			bd2.putSerializable(AppConstants.PARAM_COMPANY, mCompany);
			ActivityUtil.next(CompanyDetailActivity.this, CompanyDiscussListActivity.class, bd2, -1);
	     	}
			break;
		//拨打电话面板-拨号按钮	
		case R.id.btn_call:
			AnalyticsUtil.onEvent(this, UmengEvent.ID_CALL_PHONE);
			mDialog.dismiss();
			mDialog = null;
			Util.callPhone(CompanyDetailActivity.this, mCompany.getPhone());
			break;
		//装修日记列表
		case R.id.ll_renovation_diary:
			Bundle bd5 = new Bundle();
			bd5.putInt(CompanyArticlesActicity.COMPANY_ARTICLES, CompanyArticlesActicity.RENOVATION_ARTICLES);
			bd5.putInt(CompanyArticlesActicity.COMPANY_ID, mCompany.getId());
			ActivityUtil.next(CompanyDetailActivity.this, CompanyArticlesActicity.class, bd5, -1);
			break;
		//监理日记列表
		case R.id.ll_supervisor_diary:
			Bundle bd6 = new Bundle();
			bd6.putInt(CompanyArticlesActicity.COMPANY_ARTICLES, CompanyArticlesActicity.SUPERVISOR_ARTICLES);
			bd6.putInt(CompanyArticlesActicity.COMPANY_ID, mCompany.getId());
			ActivityUtil.next(CompanyDetailActivity.this, CompanyArticlesActicity.class, bd6, -1);
			break;
		//拨打电话面板-取消按钮
		case R.id.btn_cancel:
			mDialog.dismiss();
			mDialog = null;
			break;
		//预约监理
		case R.id.btn_appointment:
			AnalyticsUtil.onEvent(this, UmengEvent.ID_COMPANY_ORDER);
			Bundle bd3 = new Bundle();
			bd3.putSerializable(AppConstants.PARAM_COMPANY, mCompany);
			ActivityUtil.next(CompanyDetailActivity.this, CompanyOrderActivity.class,bd3, -1);
			break;
		//写评论
		case R.id.ib_write_comment:
			AnalyticsUtil.onEvent(THIS, UmengEvent.ID_DISCUSS);
			if (!ZxsqApplication.getInstance().isLogged()) {
				ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
				return;
			}
			//Intent intent = new Intent(CompanyDetailActivity.this, CompanyWriteCommentActivity.class);
			Bundle bd4 = new Bundle();
			bd4.putSerializable(AppConstants.PARAM_COMPANY, mCompany);
			ActivityUtil.next(CompanyDetailActivity.this, CompanyWriteCommentActivity.class, bd4, REQ_DISCUSS);
			break;
		//拨打电话面板
		case R.id.ib_call_phone:
			callPhone();
			break;
		//收藏按钮
		case R.id.ib_collection:
			AnalyticsUtil.onEvent(THIS, UmengEvent.ID_COMPANY_FAVORITES);
			if (!ZxsqApplication.getInstance().isLogged()) {
				ActivityUtil.next(THIS, UserLoginActivity.class, null, REQ_LOGIN_CODE);
				return;
			}
			httpRequestFavorite();
			break;
		//顶部效果图集
		case R.id.iv_photo:			
			Bundle bd7 = new Bundle();
			bd7.putSerializable(AppConstants.PARAM_ATLAS, mAtlas);
			ActivityUtil.next(CompanyDetailActivity.this, AtlasBrowseActivity.class, bd7, -1);
			break;
		//地图按钮-跳转到公司地图
		case R.id.iv_location:		
			double py = Double.parseDouble(mCompany.getPy());
			double px = Double.parseDouble(mCompany.getPx());
			Bundle bd8 = new Bundle();
			bd8.putDouble("px", px);
			bd8.putDouble("py", py);
			bd8.putString("company_name",mCompany.getFullName());
			bd8.putString("company_addr",mCompany.getAddress());
			ActivityUtil.next(CompanyDetailActivity.this, CompanyMap.class, bd8, -1);
			break;		
		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void callPhone() {
		final View view = LayoutInflater.from(this).inflate(R.layout.company_detail_call_phone_dialog, null);
		view.findViewById(R.id.btn_call).setOnClickListener(this);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
		
		Button callButton = (Button) view.findViewById(R.id.btn_call);
		callButton.setText("拨打电话  "+mCompany.getPhone());
		mDialog = new Dialog(CompanyDetailActivity.this, R.style.DialogBottomPop);
		mDialog.setContentView(view);
		Window window = mDialog.getWindow();
		window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.BOTTOM);
		mDialog.show();
	}
	
	/**
	 * 收藏公司
	 */
	private void httpRequestFavorite() {
		showWaitDialog("收藏中...");
		RequestParams params = new RequestParams();
		//params.put("user_id", AppContext.getInstance().getUser().getId());
		params.put("cnt_id", mCompany.getId());
		params.put("cnt_type", HttpClientApi.CntType.store);
		String url = null;
		if (mCompany.getIsFavored()) {
			url = HttpClientApi.REQ_COMMON_FAVORITE_DEL;
			
		} else {
			url = HttpClientApi.REQ_COMMON_FAVORITE;
		}
		HttpClientApi.post(url, params, new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(String response) {
				if (mCompany.getIsFavored()) {
					mCompany.setIsFavored(false);
					showToastMsg("取消成功");
				} else {
					mCompany.setIsFavored(true);
					showToastMsg("收藏成功");
				}
				updateFavorite();
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
	 * 更新收藏状态
	 */
	private void updateFavorite() {
		ImageButton ibCollection = (ImageButton)findViewById(R.id.ib_collection);

		if (mCompany.getIsFavored()) {
			ibCollection.setImageResource(R.drawable.company_icon03_on);
		} else {
			ibCollection.setImageResource(R.drawable.company_icon03_off);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == REQ_LOGIN_CODE){
 			if (resultCode == Activity.RESULT_OK) {
 				 httpRequestCompantDetail();
			}
         }
         if(requestCode == REQ_DISCUSS){ //写评论
 			if (resultCode == Activity.RESULT_OK) {
 				 httpRequestCompantDetail();
			}
         }
	}
	
}
