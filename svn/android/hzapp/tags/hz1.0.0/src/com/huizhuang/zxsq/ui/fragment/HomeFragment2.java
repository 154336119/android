package com.huizhuang.zxsq.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.constants.AppConstants.UmengEvent;
import com.huizhuang.zxsq.http.HttpClientApi;
import com.huizhuang.zxsq.http.bean.Constant;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.module.Advertisement;
import com.huizhuang.zxsq.module.Atlas;
import com.huizhuang.zxsq.module.Author;
import com.huizhuang.zxsq.module.Company;
import com.huizhuang.zxsq.module.Diary;
import com.huizhuang.zxsq.module.DiaryGroup;
import com.huizhuang.zxsq.module.Result;
import com.huizhuang.zxsq.module.parser.AdvertisementListParser;
import com.huizhuang.zxsq.module.parser.DiaryGroupParser;
import com.huizhuang.zxsq.module.parser.ResultParser;
import com.huizhuang.zxsq.ui.activity.AtlasBrowseActivity;
import com.huizhuang.zxsq.ui.activity.MainActivity;
import com.huizhuang.zxsq.ui.activity.WebActivity;
import com.huizhuang.zxsq.ui.activity.account.LoveFamilyProfileActivity;
import com.huizhuang.zxsq.ui.activity.bill.BillMainActivity;
import com.huizhuang.zxsq.ui.activity.company.CompanyDetailActivity;
import com.huizhuang.zxsq.ui.activity.diary.DiaryDetailActivity;
import com.huizhuang.zxsq.ui.activity.diary.DiaryEditActivity;
import com.huizhuang.zxsq.ui.activity.image.ImageSelectActivity;
import com.huizhuang.zxsq.ui.activity.user.UserLoginActivity;
import com.huizhuang.zxsq.ui.activity.zxbd.ZxbdIntroActivity;
import com.huizhuang.zxsq.ui.activity.zxbd.ZxbdListActivity;
import com.huizhuang.zxsq.ui.activity.zxbd.ZxbdQAActivity;
import com.huizhuang.zxsq.ui.adapter.DiaryListViewAdapter;
import com.huizhuang.zxsq.ui.adapter.HomeAdvImageAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseListFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.FileUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.CircleFlowIndicator;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.MainBottomBar.BottomBarFragmentType;
import com.huizhuang.zxsq.widget.ViewFlow;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;
import com.lgmshare.http.netroid.RequestCallBack;
import com.lgmshare.http.netroid.RequestParams;
import com.lgmshare.http.netroid.exception.NetroidError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * @ClassName: HomeFragment
 * @Description: 首页
 * @author lim
 * @mail limshare@gmail.com
 * @date 2014-10-24 上午9:33:11
 * 
 */
public class HomeFragment2 extends BaseListFragment implements OnClickListener {

	private static final int REQ_DETAIL_CODE = 10;
	private static final int REQ_CAPTURE_CODE = 666;
	private static final int REQ_LOGIN_FOR_ZXJZ_CODE = 100;
	private static final int REQ_LOGIN_FOR_AJDA_CODE = 101;
	private static final int REQ_LOGIN_FOR_DIARY_CODE = 102;

	
	private CommonActionBar mCommonActionBar;
	private DataLoadingLayout mDataLoadingLayout;

	// 顶部广告
	private View mHeaderView;
	private ViewFlow mViewFlow;
	private ArrayList<Advertisement> mAdvertise;

	// 效果图listview
	private ListView mListView;
	private DiaryListViewAdapter mAdapter;
	private DiaryGroup mAtlasGroup;
	private int mPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home2, container, false);
		initActionBar(view);
		initViews(view);
		return view;
	}
	
	private void initActionBar(View view){
		mCommonActionBar = (CommonActionBar) view.findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.txt_hz_app);
		mCommonActionBar.setLeftBtn(R.drawable.acc_location, R.string.txt_city, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	private void initViews(View view) {
		mDataLoadingLayout = (DataLoadingLayout) view.findViewById(R.id.home_data_loading_layout);
		mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadHomepageData();
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadHomepageData();
	}

	/**
	 * 初始化广告view,再初始化listview
	 */
	private void initAdvView() {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_adv_layout, null, false);
		mViewFlow = (ViewFlow) view.findViewById(R.id.viewflow_home);
		mViewFlow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LogUtil.i("onItemClick");
				itemAdvertiseOnClick(position);
			}
		});
		mHeaderView = view;
		view.findViewById(R.id.btn_zxbd).setOnClickListener(this);
		view.findViewById(R.id.btn_atlas).setOnClickListener(this);
		view.findViewById(R.id.btn_zxjz).setOnClickListener(this);
		view.findViewById(R.id.btn_zxrj).setOnClickListener(this);

		HomeAdvImageAdapter adapter = new HomeAdvImageAdapter(getActivity());
		//adapter.setList(mAdvertise);
		mViewFlow.setTimeSpan(4500);
		mViewFlow.setAdapter(adapter);
		mViewFlow.setSelection(mAdvertise.size() * 1000); // 设置初始位置
		mViewFlow.setValidCount(mAdvertise.size()); // 实际图片张数，
													// 我的ImageAdapter实际图片张数为3
		CircleFlowIndicator indic = (CircleFlowIndicator) mHeaderView.findViewById(R.id.viewflowindic);
		mViewFlow.setFlowIndicator(indic);
		mViewFlow.startAutoFlowTimer();
	}

	private void initListView() {
		mAdapter = new DiaryListViewAdapter(getActivity());
		mListView = getListView();
		mListView.addHeaderView(mHeaderView);
		mListView.setAdapter(mAdapter);
		// 滚动不加载
		PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true);
		mListView.setOnScrollListener(listener);

	}

	private void updateListView() {
		mAdapter.setList(mAtlasGroup);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 加载广告及推荐效果图数据
	 */
	private void loadHomepageData() {
		RequestParams params = new RequestParams();
		HttpClientApi.get(HttpClientApi.REQ_HOMEPAGE_DATA, params, new ResultParser(), new RequestCallBack<Result>() {

			@Override
			public void onStart() {
				super.onStart();
				mDataLoadingLayout.showDataLoading();
			}

			@Override
			public void onSuccess(Result result) {
				try {
					ArrayList<Advertisement> advList = new AdvertisementListParser().parse(result.data);
					mAtlasGroup = new DiaryGroupParser().parse(result.data);
					mAdvertise = new ArrayList<Advertisement>();
					mAdvertise.clear();
					mAdvertise.addAll(advList);
					loadData();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			private void loadData() {
				FileUtil.saveFile(getActivity(), "homeData", mAtlasGroup);
				FileUtil.saveFile(getActivity(), "homeAd", mAdvertise);
				initAdvView();
				initListView();
				updateListView();
				mDataLoadingLayout.showDataLoadSuccess();
			}

			@Override
			public void onFailure(NetroidError error) {
				mAtlasGroup = (DiaryGroup) FileUtil.readFile(getActivity(), "homeData");
				mAdvertise = (ArrayList<Advertisement>) FileUtil.readFile(getActivity(), "homeAd");
				if (mAtlasGroup != null) {
					loadData();
				} else {
					showToastMsg(error.getMessage());
					mDataLoadingLayout.showDataLoadFailed(error.getMessage());
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				hideWaitDialog();
			}

		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mViewFlow != null) {
			mViewFlow.startAutoFlowTimer(); // 启动自动播放
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mViewFlow != null) {
			mViewFlow.stopAutoFlowTimer();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (position >= mListView.getHeaderViewsCount()) {
			Author author = mAdapter.getList().get(position - mListView.getHeaderViewsCount()).getAuthor();
			Diary diary = mAdapter.getList().get(position - mListView.getHeaderViewsCount());
			Bundle bd = new Bundle();
			bd.putSerializable(AppConstants.PARAM_AUTHOR, author);
			bd.putSerializable(AppConstants.PARAM_DIARY, diary);
			ActivityUtil.next(getActivity(), DiaryDetailActivity.class, bd, -1);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_zxbd:
			AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_ZXBD);
			ActivityUtil.next(getActivity(), ZxbdIntroActivity.class);
			break;
		case R.id.btn_atlas:
			// 切换到找装修
			((MainActivity) getActivity()).switchToFragment(BottomBarFragmentType.COMPANY);
			break;
		case R.id.btn_zxjz:
			AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_BILL_ACCOUNTING);
			itemDecorateAccountingOnClick();
			break;
		case R.id.btn_zxrj:
			AnalyticsUtil.onEvent(getActivity(), UmengEvent.ID_DIARY_EDIT_FROM_HOME);
			itemDiaryCaptureOnClick();
			break;

		default:
			break;
		}
	}

	/**
	 * 模块名称 模块编号 能否作广告链接 链接到首页规则 链接到详情规则 装修公司 1 能 1 1+公司id 效果图 2 能 2 2+专辑id 装修日记
	 * 3 能 3 3+用户id 个人中心 4 否 无 无 App首页 5 否 无 无 装修记账 6 否 无 无 装修宝典 7 能 7 无 防坑大全 8
	 * 能 8 无
	 * 
	 * @param position
	 */
	private void itemAdvertiseOnClick(int position) {
		Advertisement adv = mAdvertise.get(position);
		LogUtil.i("adv.getUrl():" + adv.getUrl());
		String url = adv.getUrl();
		// url="huizhuangzxsq://type=1&service_city=成都&decoration_type=家装&renovation_way=全包";
		// url="huizhuangzxsq://type=2&room_type=两居室&room_part=吊顶&room_space=客厅&room_style=欧式";
		// url="huizhuangzxsq://type=3&room_style=欧式&room_type=两居室";
		// url="huizhuangzxsq://type=9&name=防坑大全&id=1";
		// url="http://www.baidu.com";
		// url="huizhuangzxsq://";
		if (url == null)
			return;
		if (url.startsWith("huizhuangzxsq://")) {
			url = url.replace("huizhuangzxsq://", "");
			String args[] = url.split("&");
			Map<String, String> dataMap = new HashMap<String, String>();
			for (int i = 0; i < args.length; i++) {
				String keyVaules[] = args[i].split("=");
				if (keyVaules.length < 2)
					continue;
				String key = keyVaules[0];
				if (key != null) {
					key = key.trim();
				}
				String value = keyVaules[1];
				if (value != null) {
					value = value.trim();
				}
				dataMap.put(key, value);
			}
			jumpToActiovity(dataMap);
		} else if(url.startsWith("http")&&url.equals("www")){
			Intent intent = new Intent(getActivity(), WebActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
		}

	}

	private void jumpToActiovity(Map<String, String> dataMap) {
		String type = dataMap.get("type");
		if ("1".equals(type)) {
			// 跳转到公司列表或者公司详情页面
			jumpToCompany(dataMap);
			return;
		} else if ("2".equals(type)) {
			// 跳转到效果图首页或者效果图详情页
			jumpToAtlas(dataMap);
			return;
		} else if ("3".equals(type)) {
			jumpToDiaryActivity(dataMap);
		} else if ("4".equals(type)) {
			jumpToUserCenter();
		} else if ("6".equals(type)) {
			if (ZxsqApplication.getInstance().isLogged()) {
				ActivityUtil.next(getActivity(), BillMainActivity.class);
			} else {
				Intent intent = new Intent(getActivity(), UserLoginActivity.class);
				startActivityForResult(intent, REQ_LOGIN_FOR_AJDA_CODE);
			}
		} else if ("7".equals(type)) {
			Bundle bd = new Bundle();
			ActivityUtil.next(getActivity(), ZxbdIntroActivity.class, bd, -1);
		} else if ("8".equals(type)) {
			Bundle bd = new Bundle();
			ActivityUtil.next(getActivity(), ZxbdListActivity.class, bd, -1);
		} else if ("9".equals(type)) {
			jumpToZxbdActivity(dataMap);
		} else if ("11".equals(type)) {
			if (ZxsqApplication.getInstance().isLogged()) {
				ActivityUtil.next(getActivity(), LoveFamilyProfileActivity.class);
			} else {
				Intent intent = new Intent(getActivity(), UserLoginActivity.class);
				startActivityForResult(intent, REQ_LOGIN_FOR_AJDA_CODE);
			}
		}
	}

	private void jumpToUserCenter() {
		((MainActivity) getActivity()).switchToFragment(BottomBarFragmentType.ACCOUNT);
	}

	private void jumpToZxbdActivity(Map<String, String> dataMap) {
		String name = dataMap.get("name");
		String id = dataMap.get("id");
		Intent intent = new Intent();
		intent.putExtra("name", name);
		intent.putExtra("id", id);
		intent.setClass(getActivity(), ZxbdQAActivity.class);
		startActivity(intent);
	}

	private void jumpToDiaryActivity(Map<String, String> dataMap) {
		String profileId = dataMap.get("profile_id");
		if (profileId != null) {
			Author author = new Author();
			author.setId(profileId);
			Bundle bd = new Bundle();
			bd.putSerializable(AppConstants.PARAM_AUTHOR, author);
			ActivityUtil.next(getActivity(), DiaryDetailActivity.class, bd, -1);
		} else {
			String roomStyle = dataMap.get("room_style");
			String roomType = dataMap.get("room_type");
			LogUtil.i("roomType:" + roomType);
			String costRange = dataMap.get("cost_range");
			String orderby = dataMap.get("orderby");
			String zdNode = dataMap.get("zx_node");
			Constant constant = ZxsqApplication.getInstance().getConstant();
			Bundle bd = new Bundle();
			if (roomStyle != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getRoomStyles(), roomStyle);
				bd.putSerializable("condition_1", keyValue);
			}
			if (roomType != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getRoomTypes(), roomType);
				bd.putSerializable("condition_2", keyValue);
			}
			if (costRange != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getCostRanges(), costRange);
				bd.putSerializable("condition_3", keyValue);
			}
			if (orderby != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getDiaryOrderOptions(), orderby);
				bd.putSerializable("condition_4", keyValue);
			}
			if (zdNode != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getZxNodes(), zdNode);
				bd.putSerializable("condition_5", keyValue);
			}

			((MainActivity) getActivity()).switchToFragment(BottomBarFragmentType.DIARY);
			((MainActivity) getActivity()).updateDiaryData(bd);
		}
	}

	private void jumpToAtlas(Map<String, String> dataMap) {
		String albumId = dataMap.get("album_id");
		String sketchId = dataMap.get("sketch_id");
		if (albumId != null) {
			Atlas atlas = new Atlas();
			atlas.setAlbumId(albumId);
			atlas.setId(sketchId);
			Bundle bundle = new Bundle();
			bundle.putSerializable(AppConstants.PARAM_ATLAS, atlas);
			ActivityUtil.next(getActivity(), AtlasBrowseActivity.class, bundle, -1);
		} else {
			String roomType = dataMap.get("room_type");
			LogUtil.i("roomType:" + roomType);
			String roomSpace = dataMap.get("room_space");
			String roomPart = dataMap.get("room_part");
			String roomStyle = dataMap.get("room_style");
			Constant constant = ZxsqApplication.getInstance().getConstant();
			Bundle bd = new Bundle();
			if (roomType != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getRoomTypes(), roomType);
				LogUtil.i("roomType:" + roomType + "  name:" + keyValue.getName());
				bd.putSerializable("condition_4", keyValue);
			}
			if (roomSpace != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getRoomSpaces(), roomSpace);
				bd.putSerializable("condition_2", keyValue);
			}
			if (roomPart != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getRoomParts(), roomPart);
				bd.putSerializable("condition_3", keyValue);
			}
			if (roomStyle != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getRoomStyles(), roomStyle);
				bd.putSerializable("condition_1", keyValue);
			}
			((MainActivity) getActivity()).switchToFragment(BottomBarFragmentType.ATLAS);
			((MainActivity) getActivity()).updateAtlasData(bd);
		}
	}

	private void jumpToCompany(Map<String, String> dataMap) {
		String storeId = dataMap.get("store_id");
		if (storeId != null) {
			// TODO:跳转到公司详情页面
			Company company = new Company();
			company.setId(Integer.parseInt(storeId));
			Bundle bd = new Bundle();
			bd.putSerializable(AppConstants.PARAM_COMPANY, company);
			ActivityUtil.next(getActivity(), CompanyDetailActivity.class, bd, -1);
			return;
		} else {
			// 获取过滤数据，跳转到列表页面
			String serviceCity = dataMap.get("service_city");
			String decorationType = dataMap.get("decoration_type");
			String renovationWay = dataMap.get("renovation_way");
			String rankType = dataMap.get("rank_type");
			String costRange = dataMap.get("cost_range");
			String keyword = dataMap.get("keyword");
			Bundle bd = new Bundle();
			Constant constant = ZxsqApplication.getInstance().getConstant();
			if (serviceCity != null) {
				bd.putString("city", serviceCity);
			}
			if (keyword != null) {
				bd.putString("keyword", keyword);
			}
			if (decorationType != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getDecorationTypes(), decorationType);
				bd.putSerializable("condition_1", keyValue);
			}
			if (renovationWay != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getRenovationWays(), renovationWay);
				bd.putSerializable("condition_2", keyValue);
			}
			if (rankType != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getCostRanges(), rankType);
				bd.putSerializable("condition_3", keyValue);
			}
			if (costRange != null) {
				KeyValue keyValue = constant.getKeyValueByKeyName(constant.getStoreOrderOptions(), costRange);
				bd.putSerializable("condition_4", keyValue);
			}

			((MainActivity) getActivity()).switchToFragment(BottomBarFragmentType.COMPANY);
			((MainActivity) getActivity()).updateCompanyData(bd);
			// ((MainActivity)getActivity()).get
			return;
		}
	}

	/**
	 * 点击装修记账事件
	 */
	private void itemDecorateAccountingOnClick() {
		if (ZxsqApplication.getInstance().isLogged()) {
			ActivityUtil.next(getActivity(), BillMainActivity.class);
		} else {
			Intent intent = new Intent(getActivity(), UserLoginActivity.class);
			startActivityForResult(intent, REQ_LOGIN_FOR_ZXJZ_CODE);
		}
	}

	/**
	 * 装修日记拍照
	 */
	private void itemDiaryCaptureOnClick() {
		// 先登录之后再拍照
		if (!ZxsqApplication.getInstance().isLogged()) {
			Intent intent = new Intent(getActivity(), UserLoginActivity.class);
			startActivityForResult(intent, REQ_LOGIN_FOR_DIARY_CODE);
			return;
		}

		String mImagePath = Util.createImagePath(getActivity());
		if (mImagePath == null) {
			return;
		}
		Intent intent = new Intent(getActivity(), ImageSelectActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("crop", false);
		bundle.putString("image-path", mImagePath);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQ_CAPTURE_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.i("requestCode:" + requestCode + "  resultCode:" + resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CAPTURE_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri imageUri = data.getParcelableExtra("image-path-uri");
				DiaryEditActivity.show(getActivity(), imageUri);
			}
		} else if (requestCode == REQ_LOGIN_FOR_ZXJZ_CODE && resultCode == Activity.RESULT_OK) {
			if (ZxsqApplication.getInstance().isLogged()) {
				ActivityUtil.next(getActivity(), BillMainActivity.class);
			}
		} else if (requestCode == REQ_LOGIN_FOR_AJDA_CODE && resultCode == Activity.RESULT_OK) {
			ActivityUtil.next(getActivity(), LoveFamilyProfileActivity.class);
		} else if (resultCode == Activity.RESULT_OK && requestCode == REQ_DETAIL_CODE) {
			// TODO:这里我注视了 不知道换掉以后该写什么 请补充

			// String shareNum = data.getStringExtra("shareNum");
			// mAdapter.getList().get(mPosition).setShareNum(shareNum);
			// String favorNum = data.getStringExtra("favorNum");
			// mAdapter.getList().get(mPosition).setFavoriteNum(favorNum);
			// mAdapter.updateView(mListView, mPosition + 1, mPosition);
		} else if (requestCode == REQ_LOGIN_FOR_DIARY_CODE && resultCode == Activity.RESULT_OK) {
			itemDiaryCaptureOnClick();
		}

	}

}
