package com.huizhuang.zxsq.ui.fragment.order;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.order.OrderDetailChild;
import com.huizhuang.zxsq.ui.adapter.order.AcceptForemanAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.utils.analytics.AnalyticsUtil;
import com.huizhuang.zxsq.widget.DataLoadingLayout;
import com.huizhuang.zxsq.widget.XListView;
import com.umeng.analytics.MobclickAgent;

/**
 * 接单工长
 * 
 * @author th
 * 
 */
public class AcceptForemanFragment extends BaseFragment {

    
    private XListView mXListView;
    private AcceptForemanAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("onCreateView()");
        View view = inflater.inflate(R.layout.fragment_accept_foreman, container, false);
        initVews(view);
        return view;
    }
    @Override
    public void onResume() { 
    	super.onResume();
    }
    @Override
    public void onPause() {
    	super.onPause();
    }
    private void initVews(View v) {
        
        mXListView = (XListView) v.findViewById(R.id.xlist);
        mXListView.setPullRefreshEnable(false);
        mXListView.setPullLoadEnable(false);
        mXListView.setAutoLoadMoreEnable(false);
        mXListView.setAutoRefreshEnable(false);
        mAdapter = new AcceptForemanAdapter(getActivity(), mClickHandler);
        mXListView.setAdapter(mAdapter);
    }

    @SuppressLint("HandlerLeak")
    private Handler mClickHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            int position = msg.arg1;
            String mobile = mAdapter.getList().get(position).getMobile();
            switch (what) {
                case 0:// 发信息
                    Uri smsToUri = Uri.parse("smsto:" + mobile);
                    Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
                    startActivity(mIntent);
                    break;
                case 1:// 打电话
                    Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + mobile));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }

    };

    public void initData(List<OrderDetailChild> orders) {
        if (orders != null && orders.size() > 0) {
            mAdapter.setList(orders);
            mAdapter.notifyDataSetChanged();
        }
    }
}
