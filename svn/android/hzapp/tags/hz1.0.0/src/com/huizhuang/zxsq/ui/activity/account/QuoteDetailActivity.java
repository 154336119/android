package com.huizhuang.zxsq.ui.activity.account;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.AbstractHttpResponseHandler;
import com.huizhuang.zxsq.http.bean.account.QuoteDetail;
import com.huizhuang.zxsq.http.bean.foreman.ForemanPriceList;
import com.huizhuang.zxsq.http.task.account.GetQuoteDetailTask;
import com.huizhuang.zxsq.http.task.foreman.ForemanPriceListTask;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.account.QuoteAdapter;
import com.huizhuang.zxsq.utils.DensityUtil;
import com.huizhuang.zxsq.utils.Util;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.actionbar.CommonActionBar;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 客户报价页面
 * 
 * @author jiengyh
 * 
 * @date 2014-12-15
 */
public class QuoteDetailActivity extends BaseActivity {

    private static final String TAG = "ClientQuoteActivity";

    public static final String EXTRA_CLIENT_ID = "client_id";
    public static final String EXTRA_QUOTE_ID = "quote_id";

    private Context mContext;
    private TextView mTxtTotalPrice;
    private TextView mTxtAveragePrice;
    private ExpandableListView mExpListView;
    private QuoteAdapter mAdapter;
    private DataLoadingLayout mLoadingLayout;
    private QuoteDetail mDetail;
    private Dialog mEditDialog;
    private Map<String, String> mDataMap;
    private CommonActionBar mCommonActionBar;
    private String mClientId;
    private String mQuoteId;
    private String mTotalPrice;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);
        mContext = this;
        getIntentExtras();
        initActionBar();
        initViews();
        httpGetQuoteDetail(mQuoteId);
       // httpGetQuoteData();
    }

	private void initActionBar() {
		mCommonActionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
		mCommonActionBar.setActionBarTitle("报价清单");
		mCommonActionBar.setLeftImgBtn(R.drawable.global_back_selector,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

    private void initViews() {
        mDataMap = new HashMap<String, String>();

        mLoadingLayout = (DataLoadingLayout) findViewById(R.id.data_loading_layout);
        mTxtTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        mTxtAveragePrice = (TextView) findViewById(R.id.tv_average_price);
        mExpListView = (ExpandableListView) findViewById(R.id.exp_list);
        View view = new View(mContext);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 54));
        view.setLayoutParams(params);
        mExpListView.addFooterView(view);
        mAdapter = new QuoteAdapter();
        mExpListView.setAdapter(mAdapter);
     
    }

    private void getIntentExtras() {
        mQuoteId = getIntent().getStringExtra(EXTRA_QUOTE_ID);

    }
    /**
     * http请求 - 请求推送报价单
     */
	private void httpGetQuoteDetail(String quoteId) {
		GetQuoteDetailTask task = new GetQuoteDetailTask(this, quoteId);
		task.setCallBack(new AbstractHttpResponseHandler<QuoteDetail>() {

			@Override
			public void onSuccess(QuoteDetail quoteDetail) {
				hideWaitDialog();
				setViewData(quoteDetail);
			}
			
          /**
         * @param detail
         */
        private void setViewData(QuoteDetail detail) {
	          mTxtTotalPrice.setText(Util.formatMoneyUnitYuan(detail.getTotal_price() + "") + "元");
	          mTotalPrice = detail.getTotal_price();
	          mTxtAveragePrice.setText(Util.formatMoneyUnitYuan(detail.getAverage_price() + "") + "/平米");
	          mAdapter.setDataList(mContext, detail.getCate_list());
	          mExpListView.setAdapter(mAdapter);
          }
			@Override
			public void onFailure(int code, String error) {
				hideWaitDialog();
				showToastMsg(error);
			}
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				showWaitDialog("请稍等");
			}
		});
		
		task.send();
	}
//    private void httpGetQuoteData() {
//        GetQuoteDetailTask task = new GetQuoteDetailTask(mClientId, mQuoteId);
//        task.setBeanClass(QuoteDetail.class);
//        task.setCallBack(new IHttpResponseHandler<QuoteDetail>() {
//
//            @Override
//            public void onSuccess(int statusCode, QuoteDetail detail) {
//                LoggerUtil.i(TAG, "onSuccess detail:" + detail);
//                mDetail = detail;
//                if (detail != null) {
//                    mQuoteId = detail.getId();
//                    setViewData(detail);
//                    mLoadingLayout.showDataLoadSuccess();
//                } else {
//                    mLoadingLayout.showDataEmptyView();
//                }
//            }
//
//            private void setViewData(QuoteDetail detail) {
//                mTxtMyQuote.setText(String.format(getString(R.string.txt_my_quote), detail.getNumber()));
//                mTxtTotalPrice.setText(ComUtil.formatMoneyUnitYuan(detail.getTotal_price() + "") + "元");
//                mTotalPrice = detail.getTotal_price();
//                mTxtAveragePrice.setText(ComUtil.formatMoneyUnitYuan(detail.getAverage_price() + "") + "/平米");
//                mAdapter.setDataList(mContext, detail.getCate_list());
//            }
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//
//            @Override
//            public void onError(int statusCode, String error) {
//                mLoadingLayout.showDataLoadFailed(error);
//            }
//
//            @Override
//            public void onDataError(int statusCode, String error) {
//                mLoadingLayout.showDataLoadFailed(error);
//            }
//        });
//        task.doGet(mContext);
//    }
//    }

    private String getJsonArray() {
        Set<Entry<String, String>> set = mDataMap.entrySet();
        Iterator<Entry<String, String>> it = set.iterator();
        JSONArray array = new JSONArray();
        while (it.hasNext()) {
            JSONObject obj = new JSONObject();
            Entry<String, String> entry = it.next();
            obj.put("item_id", entry.getKey());
            obj.put("quantity", entry.getValue());
            array.add(obj);
        }

        return array.toJSONString();
    }

 

}
