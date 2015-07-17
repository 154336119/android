package com.huizhuang.zxsq.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.common.Housing;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;

/**
 * 
 * @ClassName: HousingListAdapter
 * @Description: 小区
 * @author lim
 * @mail limshare@gmail.com
 * @date 2014-12-9 上午11:10:49
 * 
 */
public class HousingListAdapter extends CommonBaseAdapter<Housing> {

    private Holder mHolder; 
    
    public HousingListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            mHolder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_housing_list, viewGroup, false);
            mHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
            mHolder.tvAddress = (TextView) view.findViewById(R.id.tv_address);
            mHolder.tvArea = (TextView) view.findViewById(R.id.tv_area);
            view.setTag(mHolder);
        }else {
            mHolder = (Holder) view.getTag();
        }
        
        Housing housing = getItem(position);
        mHolder.tvName.setText(housing.getName());
        mHolder.tvAddress.setText(housing.getComarea());
        mHolder.tvArea.setText(housing.getDistrict());
        return view;
    }

    private class Holder {
        TextView tvName;
        TextView tvAddress;
        TextView tvArea;
    }
}