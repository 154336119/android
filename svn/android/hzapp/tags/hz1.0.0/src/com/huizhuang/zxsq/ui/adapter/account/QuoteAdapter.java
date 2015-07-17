package com.huizhuang.zxsq.ui.adapter.account;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;

import com.huizhuang.zxsq.http.bean.account.QuoteCate;
import com.huizhuang.zxsq.http.bean.account.QuoteItem;
import com.huizhuang.zxsq.widget.QuoteItemDetailView;
import com.huizhuang.zxsq.widget.QuoteItemTitleView;

public class QuoteAdapter implements ExpandableListAdapter {
    private static final String TAG="ClientQuoteAdapter";
    private List<QuoteCate> mList=new ArrayList<QuoteCate>();
    private Context mContxt;
    
    public void setDataList(Context context,List<QuoteCate> list){
        mContxt=context;
        if(list==null) list=new ArrayList<QuoteCate>();
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
        if(mList.get(groupPosition).getItem_list()!=null)
        return mList.get(groupPosition).getItem_list().size();
        else return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getItem_list().get(childPosition);
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
            convertView=new QuoteItemTitleView(mContxt);
        }
        QuoteItemTitleView titleView=(QuoteItemTitleView) convertView;
        String s = mList.get(groupPosition).getName();
        titleView.setTitle(mList.get(groupPosition).getName());
        return convertView;
    }	

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=new QuoteItemDetailView(mContxt);
        }
        QuoteItemDetailView detailView =(QuoteItemDetailView) convertView;

        QuoteItem item=mList.get(groupPosition).getItem_list().get(childPosition);
        detailView.setInfos(item.getName(), String.valueOf(item.getPrice())+"㎡", item.getDesc());
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

}
