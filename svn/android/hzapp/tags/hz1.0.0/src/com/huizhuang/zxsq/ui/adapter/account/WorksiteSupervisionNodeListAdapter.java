package com.huizhuang.zxsq.ui.adapter.account;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.module.SupervisionNode;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;

/**
 * @ClassName: WorksiteSupervisionNodeListAdapter
 * @Description: 监理节点列表适配器
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-1-22 上午9:24:19
 * 
 */
public class WorksiteSupervisionNodeListAdapter extends
		CommonBaseAdapter<KeyValue> {

	private Holder mHolder;
	private Context mContext;
	private List<SupervisionNode> mSupervisionNodes;

	public WorksiteSupervisionNodeListAdapter(Context context) {
		super(context);
		mContext = context;
	}

	public void setListSupervisionNodes(List<SupervisionNode> mSupervisionNodes){
		this.mSupervisionNodes = mSupervisionNodes;
		notifyDataSetChanged();
	}
	
	public List<SupervisionNode> getListSupervisionNodes(){
		return this.mSupervisionNodes;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		KeyValue keyValue = getItem(position);
		if (view == null) {
			mHolder = new Holder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_worksite_supervision_node_list, viewGroup,
					false);
			mHolder.itemContainer = (RelativeLayout) view.findViewById(R.id.rl_container);
			mHolder.itemImg = (ImageView) view.findViewById(R.id.iv_img);
			mHolder.itemName = (TextView) view.findViewById(R.id.tv_name);
			mHolder.itemHelp = (TextView) view.findViewById(R.id.tv_help);
			mHolder.itemReservations = (TextView) view.findViewById(R.id.tv_reservations);
			mHolder.itemTop = (LinearLayout) view.findViewById(R.id.lin_top);
			view.setTag(mHolder);
		} else {
			mHolder = (Holder) view.getTag();
		}		
		mHolder.itemName.setText(keyValue.getName());
		if ("p1".equals(keyValue.getId())) {
			mHolder.itemImg.setBackgroundResource(R.drawable.uc_node_selected1);
			mHolder.itemHelp.setText("约定为做完完工保洁后需三方到场");
//			mHolder.itemName.setText("开工技术交底");
		} else if ("p2".equals(keyValue.getId())) {
			mHolder.itemImg.setBackgroundResource(R.drawable.uc_node_selected2);
			mHolder.itemHelp.setText("约定为进场施工之日需三方到场");
//			mHolder.itemName.setText("水电工程验收");
		} else if ("p3".equals(keyValue.getId())) {
			mHolder.itemImg.setBackgroundResource(R.drawable.uc_node_selected3);
			mHolder.itemHelp.setText("约定为水电收方当天需三方到场");
//			mHolder.itemName.setText("泥木工程验收");			
		} else if ("p4".equals(keyValue.getId())) {
			mHolder.itemImg.setBackgroundResource(R.drawable.uc_node_selected4);
			mHolder.itemHelp.setText("约定为所有瓷砖贴完两天后需三方到场");
//			mHolder.itemName.setText("油漆工程验收");			
		} else if ("p5".equals(keyValue.getId())) {
			mHolder.itemImg.setBackgroundResource(R.drawable.uc_node_selected5);
			mHolder.itemHelp.setText("约定为墙，顶面完工两天后需三方到场");
//			mHolder.itemName.setText("竣工验收");
		}
		if(mSupervisionNodes != null && mSupervisionNodes.size() > 0){
			for (SupervisionNode supervisionNode : mSupervisionNodes) {
				if(supervisionNode.getNode_id().equals(keyValue.getId())){
					switch (supervisionNode.getStatu()) {
					case 0:
						mHolder.itemContainer.setBackgroundColor(mContext.getResources().getColor(R.color.black_transparent));
						mHolder.itemTop.setVisibility(View.VISIBLE);
						mHolder.itemReservations.setText("暂不能预约");
						break;
					case 2:
						mHolder.itemContainer.setBackgroundColor(mContext.getResources().getColor(R.color.black_transparent));
						mHolder.itemTop.setVisibility(View.VISIBLE);
						mHolder.itemReservations.setText("已预约");
						break;
					case 1:
						mHolder.itemContainer.setBackgroundColor(mContext.getResources().getColor(R.color.white));
						mHolder.itemTop.setVisibility(View.GONE);
						break;
					default:
						mHolder.itemContainer.setBackgroundColor(mContext.getResources().getColor(R.color.black_transparent));
						mHolder.itemTop.setVisibility(View.VISIBLE);
						mHolder.itemReservations.setText("暂不能预约");
						break;
					}
					break;
				}
			}
		}
		
		return view;
	}

	private class Holder {
		RelativeLayout itemContainer;
		ImageView itemImg;
		TextView itemName;
		TextView itemHelp;
		LinearLayout itemTop;
		TextView itemReservations;
	}

}
