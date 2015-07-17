package com.huizhuang.zxsq.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.ZxsqApplication;
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

public class SelectHousingActivity extends BaseActivity{

    /**
     * 调试代码TAG
     */
    protected static final String TAG = SelectHousingActivity.class.getSimpleName();
    
    public static final String PARAM_HOUSING = "housing";
    public static final String PARAM_HOUSETYPE_LIST = "houseType_list";
    
    private DataLoadingLayout mDataLoadingLayout;
    
    private EditText mEtHousing;
    private TextView mTvTipsTxt;
    private HousingListAdapter mAdapter;
    
    private String mHousingName;
    
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
        mHousingName = getIntent().getStringExtra(PARAM_HOUSING);
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
        commonActionBar.setRightTxtBtn(R.string.txt_search_completed, new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtHousing.getText().toString();
                Housing housing = new Housing();
                housing.setId(0);
                housing.setName(name);
                itemClickSelectHousing(housing);
            }
        });
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
        mEtHousing.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString();
                updateTipsTitle();
                httpRequestSearchHousingByName(name, null, null);
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                
            }
        });
        
        mTvTipsTxt = (TextView) findViewById(R.id.tv_tips_txt);
        mTvTipsTxt.setText("选择附近的小区");
        
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
        LatLng latLng = ZxsqApplication.getInstance().getUserPoint();
        httpRequestSearchHousingByName(null, latLng.latitude+"", latLng.longitude+"");    
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
//        if (housing.getId() == 0) {
            backResult(housing, null);
//        }else{
//            httpRequestSearchHouseTypeById(housing);
//        }
    }
    
    private void backResult(Housing housing, List<HouseType> result){
        Bundle bd = new Bundle();
        bd.putSerializable(PARAM_HOUSING, housing);
        bd.putParcelableArrayList(PARAM_HOUSETYPE_LIST, (ArrayList<HouseType>)result);
        ActivityUtil.backWithResult(SelectHousingActivity.this, Activity.RESULT_OK, bd);
    }
    
    private void updateTipsTitle(){
        mTvTipsTxt.setText("选择小区");
    }
}

