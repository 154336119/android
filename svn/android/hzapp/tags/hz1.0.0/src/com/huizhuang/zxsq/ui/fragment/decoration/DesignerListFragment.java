package com.huizhuang.zxsq.ui.fragment.decoration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.KeyValue;
import com.huizhuang.zxsq.ui.activity.foreman.ForemanDetailsActivity;
import com.huizhuang.zxsq.ui.fragment.base.BaseFragment;
import com.huizhuang.zxsq.utils.LogUtil;
import com.huizhuang.zxsq.widget.XListView;
import com.huizhuang.zxsq.widget.XListView.IXListViewListener;
import com.huizhuang.zxsq.widget.component.TopTypeFilterBar;
import com.huizhuang.zxsq.widget.component.TopTypeFilterBar.OnDropdownBarItemClickListener;
import com.huizhuang.zxsq.widget.component.TopTypeFilterBar.TopType;

/**
 * @ClassName: DesignerFragmentList
 * @Description: 找设计师
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-1-13 下午2:13:10
 * 
 */
public class DesignerListFragment extends BaseFragment implements
		OnDropdownBarItemClickListener, OnItemClickListener {

	private TopTypeFilterBar topTypeFilterBar;

	private XListView mXListView;

	@SuppressWarnings("unused")
	private MyAdapter myAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.fragment_decoration_designer_list, container, false);
		initVews(view);
		return view;
	}

	private void initVews(View v) {
		LogUtil.d("initVews View = " + v);

		topTypeFilterBar = (TopTypeFilterBar) v
				.findViewById(R.id.top_type_filter_bar);

		topTypeFilterBar.setOnDropdownBarItemClickListener(this);
		mXListView = (XListView) v.findViewById(R.id.xlist);

		mXListView.setOnItemClickListener(this);
		mXListView.setAdapter(myAdapter = new MyAdapter());
		mXListView.setPullRefreshEnable(true);
		mXListView.setPullLoadEnable(false);
		mXListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {

			}

			@Override
			public void onLoadMore() {

			}
		});
	}

	private class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return 8;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.item_designer, null);

				ItemViewHolder holder = new ItemViewHolder();

				holder.item_designer_head = (ImageView) convertView
						.findViewById(R.id.item_designer_head);
				
				holder.item_designer_name = (TextView) convertView
						.findViewById(R.id.item_designer_name);
				holder.item_designer_job = (TextView) convertView
						.findViewById(R.id.item_designer_job);
				
				holder.item_designer_name = (TextView) convertView
						.findViewById(R.id.item_designer_designfee);
				holder.item_designer_job = (TextView) convertView
						.findViewById(R.id.item_designer_freedesign);
				
				holder.item_designer_name = (TextView) convertView
						.findViewById(R.id.item_designer_representative);
				
				holder.item_designer_name = (TextView) convertView
						.findViewById(R.id.item_designer_appointment_count);
				holder.item_designer_job = (TextView) convertView
						.findViewById(R.id.item_designer_case_count);
				holder.item_designer_name = (TextView) convertView
						.findViewById(R.id.item_designer_job_years);
				holder.item_designer_job = (TextView) convertView
						.findViewById(R.id.item_designer_deal_count);


				convertView.setTag(holder);
			}

			ItemViewHolder holder = (ItemViewHolder) convertView.getTag();
			return convertView;
		}

		class ItemViewHolder {

			public ImageView item_designer_head;
			
			public TextView item_designer_name;
			public TextView item_designer_job;
			
			public TextView item_designer_designfee;
			public TextView item_designer_freedesign;
			
			public TextView item_designer_representative;
			
			public TextView item_designer_appointment_count;
			public TextView item_designer_case_count;
			public TextView item_designer_job_years;
			public TextView item_designer_deal_count;


		}

	}

	@Override
	public void onItemClick(TopType topType, KeyValue filterKeyValue) {

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long l) {
	}
}
