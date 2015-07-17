package com.huizhuang.zxsq.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhuang.hz.R;
import com.huizhuang.zxsq.utils.DensityUtil;

public class CouponDialog {

    private Context context;
    private Dialog mDialog;
    private ImageView mIvCancel;
    private DisplayMetrics mMetrics = new DisplayMetrics();

    public CouponDialog(Context context) {
        this.context = context;
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(mMetrics);
    }

    @SuppressLint("InflateParams")
    public CouponDialog builder(String money) {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_coupon, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(mMetrics.widthPixels);

        // 获取自定义Dialog布局中的控件
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        String text = "亲，您已成功获得<big>"+money+"元</big>惠装优惠券，可在\"我的优惠券\"中查看!";
        tvContent.setText(Html.fromHtml(text));
        mIvCancel = (ImageView) view.findViewById(R.id.iv_cancel);
        mIvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        // 定义Dialog布局和参数
        mDialog = new Dialog(context, R.style.CouponDialogStyle);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int width = mMetrics.widthPixels - DensityUtil.dip2px(context, 80);
        lp.width = width;
        lp.height = Double.valueOf(width * 1.4).intValue();
        dialogWindow.setAttributes(lp);

        return this;
    }

    public void setOnCancelClickListener(OnClickListener onClickListener) {
        mIvCancel.setOnClickListener(onClickListener);
    }

    public void dismiss(){
        mDialog.dismiss();
    }
    public void show() {
        mDialog.show();
    }
}
