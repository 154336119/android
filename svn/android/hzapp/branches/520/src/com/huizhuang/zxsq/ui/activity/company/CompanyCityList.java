package com.huizhuang.zxsq.ui.activity.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.ProviceCityArea;
import com.huizhuang.zxsq.http.bean.SortModel;
import com.huizhuang.zxsq.http.parser.CharacterParser;
import com.huizhuang.zxsq.http.task.CityDistrictTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.SortAdapter;
import com.huizhuang.zxsq.utils.FileUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class CompanyCityList extends BaseActivity {
	private ListView sortListView;
	private SortAdapter adapter;
	private CommonActionBar mCommonActionBar;
	private String myCity;
	private TextView tVLocation;
	private HashMap<String, Integer> mCityMap;
	private int mCityId;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_citylist);
		getIntentExtras();
		initActionBar();
		initData();
	}

	private void initData() {
		List<ProviceCityArea> list = ZxsqApplication.getInstance().getmAllcitys();
		mCityMap = new HashMap<String, Integer>();
		if (list!=null&&list.size()>0) {
			setDate(list);
		} else {
			httpRequestCityDistrict();
		}
	}

	private void httpRequestCityDistrict() {
	    CityDistrictTask task = new CityDistrictTask(this);
		task.setCallBack(new AbstractHttpResponseHandler<List<ProviceCityArea>>() {
			@Override
			public void onStart() {
				super.onStart();
				showWaitDialog("请稍后...");
			}

			@Override
			public void onSuccess(List<ProviceCityArea> list) {
			    FileUtil.saveFile(CompanyCityList.this, "Areas", list);
			    ZxsqApplication.getInstance().setCityDistricts(list);
				setDate(ZxsqApplication.getInstance().getmAllcitys());
				hideWaitDialog();
			}

			@Override
			public void onFailure(int code, String error) {
				hideWaitDialog();
			}
		});
		task.send();
	}
    
	private void setDate(List<ProviceCityArea> list) {
		if (list == null) {
			showToastMsg("获取数据失败");
		} else {
			setCityMap(list);
			String[] citys = new String[list.size()];
			for (int i = 0; i < citys.length; i++) {
				citys[i] = list.get(i).getArea_name();
			}
			initViews(citys);
		}
	}

	private void setCityMap(List<ProviceCityArea> list){
		for(ProviceCityArea city : list){
			mCityMap.put(city.getArea_name(), city.getArea_id());
		}
	}
	private void initViews(String[] citys) {
		LinearLayout cityBar = (LinearLayout) findViewById(R.id.ll_mycity_bar);
		tVLocation = (TextView) findViewById(R.id.tv_location);
		// 没有获取定位城市信息隐藏控件
		if (!TextUtils.isEmpty(myCity)) {
			cityBar.setVisibility(View.VISIBLE);
			tVLocation.setText(myCity);
			tVLocation.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Bundle bd = new Bundle();
					Intent mIntent = new Intent();
					mIntent.putExtras(bd);
					setResult(Activity.RESULT_OK, mIntent);
					finish();
				}
			});
		}

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				String city = ((SortModel) adapter.getItem(position)).getName();
				Bundle bd = new Bundle();
				bd.putString("city", city);
				bd.putString(AppConstants.PARAM_CITY_ID, String.valueOf(mCityMap.get(city)));
				Intent mIntent = new Intent();
				mIntent.putExtras(bd);
				setResult(Activity.RESULT_OK, mIntent);
				finish();
			}
		});
		LogUtil.d("getResources().getStringArray(R.array.company_city).length:"
				+ getResources().getStringArray(R.array.company_city).length);
		SourceDateList = filledData(citys);

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);

	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle(R.string.title_city_list);
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void getIntentExtras() {
		// TODO Auto-generated method stub
		myCity = getIntent().getStringExtra("myCity");
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			if (pinyin.equals("zhongqingshi")) {
				pinyin = "chongqingshi";
			}
			System.out.println("pinyin：" + pinyin);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	public class PinyinComparator implements Comparator<SortModel> {

		public int compare(SortModel o1, SortModel o2) {
			if (o1.getSortLetters().equals("@")
					|| o2.getSortLetters().equals("#")) {
				return -1;
			} else if (o1.getSortLetters().equals("#")
					|| o2.getSortLetters().equals("@")) {
				return 1;
			} else {
				return o1.getSortLetters().compareTo(o2.getSortLetters());
			}
		}

	}
}
