package com.huizhuang.zxsq.ui.adapter.order;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.order.OrderCancelRason;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.LogUtil;

/**
 * 取消订单原因适配器
 * @author th
 *
 */
public class OrderCancelReasonAdapter extends CommonBaseAdapter<OrderCancelRason> {

	private ViewHolder mHolder;
	/**
     * 用于记录选中的Item的Map
     */
    private SparseBooleanArray mSparseBooleanArray;
    
	public OrderCancelReasonAdapter(Context context) {
		super(context);
		this.mSparseBooleanArray = new SparseBooleanArray();
	}
	/**
	 * 用于记录选择的ID项
	 * @param id
	 */
	public void setSelectIds(int id){
	    mSparseBooleanArray.clear();
	    mSparseBooleanArray.put(id, true);
	    notifyDataSetChanged();
	}
	
    /**
     * 选择的ID
     * 
     * @return
     */
    public int getSelectIds() {
        for (int index = 0; index < getCount(); index++) {
            if (mSparseBooleanArray.get(getItem(index).getCode())) {
                return getItem(index).getCode();
            }
        }
        return -1;
    }
    
    public String getSelectName(){
        for (int index = 0; index < getCount(); index++) {
            if (mSparseBooleanArray.get(getItem(index).getCode())) {
                return getItem(index).getLabel();
            }
        }
        return null;
    }
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.d("getView position = " + position);
		OrderCancelRason orderCancelRason = getItem(position);
		if (null == convertView) {
			mHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_order_cancel_reason, parent, false);
            mHolder.itemName = (TextView) convertView.findViewById(R.id.tv_name);
			mHolder.itemCheck = (ImageView) convertView.findViewById(R.id.iv_right);
			mHolder.itemLine = (View) convertView.findViewById(R.id.v_line);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(position == (getList().size()-1)){
		    mHolder.itemLine .setVisibility(View.INVISIBLE);
		}else{
		    mHolder.itemLine .setVisibility(View.VISIBLE);
		}
		mHolder.itemName.setText(orderCancelRason.getLabel());
		if(mSparseBooleanArray.get(orderCancelRason.getCode())){
		    mHolder.itemCheck.setVisibility(View.VISIBLE);
		}else{
		    mHolder.itemCheck.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	static class ViewHolder {
        private TextView itemName;
		private ImageView itemCheck;
		private View itemLine;
	}

}
