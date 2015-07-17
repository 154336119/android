package com.huizhuang.zxsq.ui.adapter.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.http.bean.order.CostChange;
import com.huizhuang.zxsq.http.bean.order.CostChangeChild;
import com.huizhuang.zxsq.widget.CostChangeItemDetailView;
import com.huizhuang.zxsq.widget.CostChangeeItemTitleView;
import com.huizhuang.zxsq.widget.QuoteItemDetailView;
import com.huizhuang.zxsq.widget.QuoteItemTitleView;

public class CostChangeAdapter implements ExpandableListAdapter {
    private static final String TAG="ClientQuoteAdapter";
    private List<ArrayList<KeyValue>> mList=new ArrayList<ArrayList<KeyValue>>();    
    private Context mContxt;
    public static String[] proName = {
    		"增减类型","增减项目","工程量","单价","增项","原因"
    };
    public void setDataList(Context context,ArrayList<ArrayList<KeyValue>> list){
        mContxt=context;
        if(list==null) list= new ArrayList<ArrayList<KeyValue>>();
        mList=list;

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(mList!=null)
        return mList.get(groupPosition).size();
        else return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=new CostChangeeItemTitleView(mContxt);
        }
        CostChangeeItemTitleView titleView=(CostChangeeItemTitleView) convertView;
        String s = "增减项目（"+groupPosition+"）";
        titleView.setTitle(s);
        titleView.changeArrState(isExpanded);
        return convertView;

    }	

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=new CostChangeItemDetailView(mContxt);
        }
        CostChangeItemDetailView detailView =(CostChangeItemDetailView) convertView;

        //CostChangeChild item=mList.get(groupPosition).getItem_list().get(childPosition);
        KeyValue item= mList.get(groupPosition).get(childPosition);
        detailView.setInfos(item.getName(),item.getRemark());
        return detailView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
	static class ViewHolderParent {
        private TextView itemParentName;
	}
	static class ViewHolderChild {
        private TextView itemChildName;
		private TextView itemChildConent;
	}
}
