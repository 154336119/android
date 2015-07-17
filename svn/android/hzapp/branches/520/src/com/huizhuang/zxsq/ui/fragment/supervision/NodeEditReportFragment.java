package com.huizhuang.zxsq.ui.fragment.supervision;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.constants.AppConstants;
import com.huizhuang.zxsq.http.bean.supervision.NodeEdit;
import com.huizhuang.zxsq.ui.activity.supervision.CheckInfoActivity;
import com.huizhuang.zxsq.ui.adapter.supervision.NodeEditAdapter;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.ActivityUtil;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.MyListView;

/**
 * 
 * 整改报告
 * 
 * @author th
 * 
 */
public class NodeEditReportFragment extends BaseFragment{

    private MyListView mListView;
    private TextView mTvWaitDes;
    
    private NodeEditAdapter mAdapter;
    private String mOrdersId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("onCreateView()");
        View view = inflater.inflate(R.layout.fragment_node_edit_report, container, false);
        initVews(view);
        return view;
    }

    private void initVews(View v) {
        mTvWaitDes = (TextView)v.findViewById(R.id.tv_wait_des);
        mListView = (MyListView)v.findViewById(R.id.lv_report);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                NodeEdit nodeEdit = mAdapter.getList().get(position);
                int status = nodeEdit.getStatus();
                Bundle bd = new Bundle();
                bd.putString(AppConstants.PARAM_ORDER_ID, mOrdersId);
                bd.putString(AppConstants.PARAM_NODE_ID, nodeEdit.getNode_id());
                bd.putString(AppConstants.PARAM_STAGE_ID, nodeEdit.getStage_id());
                if(status > -1){//验收确认页
                    ActivityUtil.next(getActivity(), CheckInfoActivity.class,bd,-1);
                }else{//验收历史页
                    ActivityUtil.next(getActivity(), CheckInfoActivity.class,bd,-1);
                }
            }
            
        });
    }


    public void initData(List<NodeEdit> nodeEdits,String ordersId) {
        mOrdersId = ordersId;
        if(nodeEdits.get(0).getStatus() == -1){
            mTvWaitDes.setVisibility(View.VISIBLE);
        }else{
            mTvWaitDes.setVisibility(View.GONE);
        }
        mAdapter = new NodeEditAdapter(getActivity());
        mAdapter.setList(nodeEdits);
        mListView.setAdapter(mAdapter);
    }
    
}
