package com.huizhuang.zxsq.ui.adapter.coupon;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.http.bean.pay.Coupon;
import com.huizhuang.zxsq.ui.activity.base.BaseActivity;
import com.huizhuang.zxsq.ui.adapter.base.CommonBaseAdapter;
import com.huizhuang.zxsq.utils.DateUtil;

public class CouponAdapter extends CommonBaseAdapter<Coupon>{
	private final static int NOT_USED = 1; //未
	private final static int USED = 2; //已使用
	private final static int EXPIRED = 3; //过期
    private BaseActivity baseActivity;

    public CouponAdapter(BaseActivity baseActivity) {
        super(baseActivity);
        this.baseActivity = baseActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ItemViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(baseActivity, R.layout.adapter_coupon, null);

            holder = new ItemViewHolder();
            holder.rl = (LinearLayout) convertView.findViewById(R.id.ll_01);
            holder.tvSign = (TextView) convertView.findViewById(R.id.tv_sign);
            holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tvCoupon = (TextView) convertView.findViewById(R.id.tv_coupon);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            holder.ivLine = (ImageView) convertView.findViewById(R.id.iv_line);
            holder.tvEffectiveData = (TextView) convertView.findViewById(R.id.tv_effective_data);
            convertView.setTag(holder);
        }else{
        	holder = (ItemViewHolder) convertView.getTag();
        }
        Coupon coupon = getItem(position);
        if(coupon.getStatu() == NOT_USED){
        	holder.rl.setEnabled(true);
        	holder.tvSign.setTextColor(mContext.getResources().getColor(R.color.color_fc6e6d));
        	holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.color_fc6e6d));
        	holder.tvMoney.setText(coupon.getAmount());
        	holder.tvCoupon.setTextColor(mContext.getResources().getColor(R.color.color_fc6e6d));
        	holder.ivLine.setEnabled(true);
        	holder.tvEffectiveData.setText("有效日期至\n"+DateUtil.format(coupon.getEnd_time(), "yyyy-MM-dd", "yyyy年MM月dd日"));
        }else if(coupon.getStatu() == USED){
        	holder.rl.setEnabled(false);
        	holder.tvSign.setTextColor(mContext.getResources().getColor(R.color.color_808080));
        	holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.color_808080));
        	holder.tvMoney.setText(coupon.getAmount());
        	holder.tvCoupon.setTextColor(mContext.getResources().getColor(R.color.color_808080));
        	holder.ivLine.setEnabled(false);
        	holder.tvEffectiveData.setText("已使用");
        }else if(coupon.getStatu() == EXPIRED){
        	holder.rl.setEnabled(false);
        	holder.tvSign.setTextColor(mContext.getResources().getColor(R.color.color_808080));
        	holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.color_808080));
        	holder.tvMoney.setText(coupon.getAmount());
        	holder.tvCoupon.setTextColor(mContext.getResources().getColor(R.color.color_808080));
        	holder.ivLine.setEnabled(false);
        	holder.tvEffectiveData.setText("已过期");
        }
      
        return convertView;
    }
    

    class ItemViewHolder {
    	private LinearLayout rl;
        private TextView tvSign;
        private TextView tvMoney;
        private TextView tvCoupon;
        private TextView tvContent;
        private ImageView ivLine;
        private TextView tvEffectiveData;
    }
}
