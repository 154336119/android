package com.huizhuang.zxsq.ui.adapter.supervision;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.supervision.ComplaintsType;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;


/** 
* @ClassName: DesignerTitleListAdapter 
* @Description: 设计师设计头衔选项适配器
* @author th 
* @mail 342592622@qq.com   
* @date 2014-12-23 下午2:33:42 
*  
*/
public class ComplaintsListAdapter extends CommonBaseAdapter<ComplaintsType> {

    private Holder mHolder;
    
    /**
     * 用于记录选中的Item的Map
     */
    private SparseBooleanArray mSparseBooleanArray;

    public ComplaintsListAdapter(Context context) {
        super(context);
        this.mSparseBooleanArray = new SparseBooleanArray();
    }

    /**
     * 初始化的时候默认选择的选项
     * @param id 已逗号分隔的选择项ID字符串
     */
    public void setSleleteIds(int id){
        mSparseBooleanArray.put(id, true);
    }
    
    /**
     * 选择的ID列表，逗号分隔的ID字符串
     * 
     * @return
     */
    public String getSelectIds() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int index = 0; index < getCount(); index++) {
            if (mSparseBooleanArray.get(index)) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(getItem(index).getCategory_id());
            }
        }
        return stringBuffer.toString();
    }
    
    /**
     * 选择的名字列表，/分隔的名字字符串
     * 
     * @return
     */
    public String getSelectNames() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int index = 0; index < getCount(); index++) {
            if (mSparseBooleanArray.get(index)) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append("/");
                }
                stringBuffer.append(getItem(index).getCategory_name());
            }
        }
        return stringBuffer.toString();
    }
    
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            mHolder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_complaints_list, viewGroup, false);
            mHolder.itemRbQuestion = (RadioButton) view.findViewById(R.id.rb_question);
            mHolder.itemRbQuestion.setOnClickListener(clickListener);
            view.setTag(mHolder);
        } else {
            mHolder = (Holder) view.getTag();
        }
        ComplaintsType complaintsType = getItem(position);
        mHolder.itemRbQuestion.setText(complaintsType.getCategory_name());
        mHolder.itemRbQuestion.setTag(position);
        mHolder.itemRbQuestion.setChecked(mSparseBooleanArray.get(position)); 
        return view;
    }

    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            switch (v.getId()) {
            case R.id.rb_question:             
                mSparseBooleanArray.clear();
                mSparseBooleanArray.put(position, true);
                notifyDataSetChanged();
                break;
            default:
                break;
            }
        }
    };

    private class Holder {
        RadioButton itemRbQuestion;
    }
}