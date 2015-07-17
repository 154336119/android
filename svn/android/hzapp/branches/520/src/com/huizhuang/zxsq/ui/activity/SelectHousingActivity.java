package com.huizhuang.zxsq.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.common.HouseType;
import com.huizhuang.zxsq.http.bean.common.Housing;
import com.huizhuang.zxsq.http.task.common.SearchHouseTypeListTask;
import com.huizhuang.zxsq.http.task.common.SearchHousingTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.HousingListAdapter;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

public class SelectHousingActivity extends BaseActivity implements OnClickListener{

    /**
     * 调试代码TAG
     */
    protected static final String TAG = SelectHousingActivity.class.getSimpleName();
    
    public static final String PARAM_HOUSING = "housing";
    public static final String PARAM_HOUSETYPE_LIST = "houseType_list";
    public static final String PARAM_KEY = "key";
    private DataLoadingLayout mDataLoadingLayout;
    public static final String PARAM_LAT = "lat";
    public static final String PARAM_LNG = "lng";
    private EditText mEtHousing;
  //  private TextView mTvTipsTxt;
    private HousingListAdapter mAdapter;
    //地图
    private List<Housing> mHousingList = new ArrayList<Housing>();
    private BaiduMap mBaiduMap;
    private SuggestionSearch mSuggestionSearch;
    private String mHousingName;
	private double mLat;
	private double mlng;
   //兴趣点检索
  	private PoiSearch mPoiSearch;
  	private String mCityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_housing);
        getIntentExtras();
        initActionBar();
        initViews();
        initData();
    }
    
    private void getIntentExtras(){
        mHousingName = getIntent().getStringExtra(PARAM_KEY);
        mLat = getIntent().getDoubleExtra(PARAM_LAT, 30.67);
        mlng = getIntent().getDoubleExtra(PARAM_LNG, 104.06);
        mCityName = getIntent().getStringExtra(AppConstants.PARAM_CITY);
    }
    
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        LogUtil.i("initActionBar");
        CommonActionBar commonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        commonActionBar.setActionBarTitle(R.string.txt_select_housing);
        commonActionBar.setLeftImgBtn(R.drawable.global_back_selector, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        commonActionBar.setRightTxtBtn(R.string.txt_search_completed, new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = mEtHousing.getText().toString();
//                Housing housing = new Housing();
//                housing.setId(0);
//                housing.setName(name);
//                itemClickSelectHousing(housing);
//            }
//        });
    }
    
    private void initViews(){
        mDataLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        mDataLoadingLayout.setOnReloadClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        
        mEtHousing = (EditText) findViewById(R.id.et_housing_name);
        mEtHousing.setText(mHousingName);
        findViewById(R.id.iv_clean).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        mEtHousing.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String name = s.toString();
//                httpRequestSearchHousingForBaidu(name);
               // httpRequestSearchHousingByName(name, null, null);
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            	String name = s.toString();
                httpRequestSearchHousingForBaidu(name);
            }
        });
        mAdapter = new HousingListAdapter(this);
        ListView lvHousing = (ListView) findViewById(R.id.lv_solution);
        lvHousing.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClickSelectHousing(mAdapter.getItem(position));
            }
        });
        lvHousing.setAdapter(mAdapter);
    }
    
    private void initData(){
        if(TextUtils.isEmpty(mCityName)){
            mCityName = ZxsqApplication.getInstance().getLocationCity();
            if(TextUtils.isEmpty(mCityName)){
            	mCityName = "成都";
            }
        }
    	//获取建议搜索实例
    	mHousingList = new ArrayList<Housing>();
    	mSuggestionSearch = SuggestionSearch.newInstance();
    	mSuggestionSearch.setOnGetSuggestionResultListener(mOnGetSuggestionResultListener);
    	mPoiSearch = PoiSearch.newInstance();
    	mPoiSearch.setOnGetPoiSearchResultListener(mPoiListener);
        LatLng latLng = ZxsqApplication.getInstance().getUserPoint();
        httpRequestSearchHousingForBaidu(mHousingName);
       // httpRequestSearchHousingByName(null, latLng.latitude+"", latLng.longitude+"");    
    }
    
    private void httpRequestSearchHousingForBaidu(String key){
    	if(!TextUtils.isEmpty(key)){
    		//开始检索
    	    LogUtil.e("3mLat:mlng"+mLat+""+mlng);;
    		mPoiSearch.searchInCity(new PoiCitySearchOption() 
            .city(mCityName.replace("市", ""))  
            .keyword(key).pageCapacity(50));
    	}
    }
    
    private void httpRequestSearchHousingByName(String name, String lat, String lon){
        SearchHousingTask task = new SearchHousingTask(SelectHousingActivity.this,name, null, null);
        task.setCallBack(new AbstractHttpResponseHandler<List<Housing>>() {
			
			@Override
			public void onSuccess(List<Housing> result) {
				mDataLoadingLayout.showDataLoadSuccess();
                mAdapter.setList(result);
                mAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(int code, String error) {
				mDataLoadingLayout.showDataLoadFailed(error);
			}

			@Override
			public void onStart() {
				super.onStart();
				mDataLoadingLayout.showDataLoading();
			}

			@Override
			public void onFinish() {

				super.onFinish();
			}
			
			
		});
        task.send();
    }
    
    private void httpRequestSearchHouseTypeById(final Housing housing){
        SearchHouseTypeListTask task = new SearchHouseTypeListTask(SelectHousingActivity.this,housing.getId()+"");
        task.setCallBack(new AbstractHttpResponseHandler<List<HouseType>>() {
            
            @Override
            public void onSuccess(List<HouseType> result) {
                backResult(housing, result);
            }
            
            @Override
            public void onStart() {
            	super.onStart();
                showWaitDialog("获取小区户型...");
            }
            
            @Override
            public void onFinish() {
            	super.onFinish();
            	hideWaitDialog();
            }
            
            @Override
            public void onFailure(int statusCode, String error) {
            	
            }

        });
        task.send();
    }
    
    private void itemClickSelectHousing(Housing housing){
        hideSoftInput(mEtHousing);
            backResult(housing, null);
    }
    
    private void backResult(Housing housing, List<HouseType> result){
        Bundle bd = new Bundle();
        bd.putSerializable(PARAM_HOUSING, housing);
        bd.putParcelableArrayList(PARAM_HOUSETYPE_LIST, (ArrayList<HouseType>)result);
        ActivityUtil.backWithResult(SelectHousingActivity.this, Activity.RESULT_OK, bd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clean:
            	mEtHousing.setText("");
                break;
            case R.id.tv_cancel:
            	ActivityUtil.back(SelectHousingActivity.this);
                break;
            default:
                break;
        }
    }
    
    //，创建在线建议查询监听者
    OnGetSuggestionResultListener mOnGetSuggestionResultListener = new OnGetSuggestionResultListener() {  
        public void onGetSuggestionResult(SuggestionResult res) {  
            if (res == null || res.getAllSuggestions() == null) {  
                return;  
                //未找到相关结果  
            }  
        List<SuggestionInfo> suggestionInfoList = res.getAllSuggestions();
        	for(int i=0;i<suggestionInfoList.size();i++){
        	}
        }

    };
  //兴趣点回调函数
  	OnGetPoiSearchResultListener mPoiListener = new OnGetPoiSearchResultListener(){  
  	    public void onGetPoiResult(PoiResult result){  
  	    	List<PoiInfo> poiList= result.getAllPoi();
  	    	if(poiList!=null&&0!=poiList.size()){
  	    		mHousingList.clear();
  		    	for(int i=0;i<poiList.size();i++){
  		    		Housing housing = new Housing();
  		    		housing.setName(poiList.get(i).name);
  		    		housing.setDistrict(poiList.get(i).address);
  		    		housing.setLat(poiList.get(i).location.latitude+"");
  		    		housing.setLon(poiList.get(i).location.longitude+"");
  		    		mHousingList.add( housing);
  		    		}
  		    	mAdapter.setList(mHousingList);
  	    	}
  	    //获取POI检索结果  
  	    }  
  	    public void onGetPoiDetailResult(PoiDetailResult result){  
  	    //获取Place详情页检索结果  
  	    }  
  	};
}

