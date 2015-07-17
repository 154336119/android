package com.huizhuang.zxsq.http.bean.order;

import java.util.ArrayList;
import java.util.List;

import com.huizhuang.zxsq.http.bean.KeyValue;

public class CostChange {

	private String add_cost_total;
	private String ded_cost_total;
	private List<CostChangeChild> list;
	private ArrayList<ArrayList<KeyValue>> KeyValuelist;
	
	public ArrayList<ArrayList<KeyValue>> getKeyValuelist() {
		return KeyValuelist;
	}
	public void setKeyValuelist(ArrayList<ArrayList<KeyValue>> keyValuelist) {
		KeyValuelist = keyValuelist;
	}
	public String getAdd_cost_total() {
		return add_cost_total;
	}
	public void setAdd_cost_total(String add_cost_total) {
		this.add_cost_total = add_cost_total;
	}
	public String getDed_cost_total() {
		return ded_cost_total;
	}
	public void setDed_cost_total(String ded_cost_total) {
		this.ded_cost_total = ded_cost_total;
	}
	public List<CostChangeChild> getList() {
		return list;
	}
	public void setList(List<CostChangeChild> list) {
		this.list = list;
	}
	
	public void initKeyValue(){
		CostChangeChild changeChild;
		KeyValuelist = new ArrayList<ArrayList<KeyValue>>();
		ArrayList<KeyValue> mKeyValuelist = null;
		for(int i=0;i<list.size();i++){
			mKeyValuelist = new ArrayList<KeyValue>();
			String type;
			changeChild = list.get(i);
			if(changeChild.getCost_type()==1){
				type = "增项";
			}else{
				type = "减项"; 
			}
			KeyValue keyValue = new KeyValue();
			keyValue.setName("增减类型");
			keyValue.setRemark(type);
			KeyValue keyValue1 = new KeyValue();
			keyValue1.setName("增减项目");
			keyValue1.setRemark(changeChild.getCost_name());
			KeyValue keyValue2 = new KeyValue();
			keyValue2.setName("工程量");
			keyValue2.setRemark(changeChild.getCost_number()+"");
			KeyValue keyValue3 = new KeyValue();
			keyValue3.setName("单价");
			keyValue3.setRemark(changeChild.getCost_price()+"");
			KeyValue keyValue4 = new KeyValue();
			keyValue4.setName("增减项费用");
			keyValue4.setRemark(changeChild.getTotal()+"");
			KeyValue keyValue5 = new KeyValue();
			keyValue5.setName("原因");
			keyValue5.setRemark(changeChild.getCause());
			
			mKeyValuelist.add(keyValue);
			mKeyValuelist.add(keyValue1);
			mKeyValuelist.add(keyValue2);
			mKeyValuelist.add(keyValue3);
			mKeyValuelist.add(keyValue4);
			mKeyValuelist.add(keyValue5);
			KeyValuelist.add(mKeyValuelist);
		}
	}
}
