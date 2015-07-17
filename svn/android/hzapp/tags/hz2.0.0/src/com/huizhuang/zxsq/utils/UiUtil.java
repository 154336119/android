package com.huizhuang.zxsq.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * @author lim
 * @ClassName: UiUtil
 * @Description:
 * @mail lgmshare@gmail.com
 * @date 2014-6-3  上午09:43:13
 */
public class UiUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static float sp2px(Context context, int spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.widthPixels;
        }
        return 0;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.heightPixels;
        }
        return 0;
    }

    /**
     * 获取根据屏幕宽度比例缩放大小
     *
     * @return
     */
    public static LayoutParams getScreenScale(Activity activity, float scale) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int height = (int) (screenWidth / scale);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth, height);
        return lp;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusHeight(Activity activity){  
        int statusHeight = 0;  
        Rect localRect = new Rect();  
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);  
        statusHeight = localRect.top;  
        if (0 == statusHeight){  
            Class<?> localClass;  
            try {  
                localClass = Class.forName("com.android.internal.R$dimen");  
                Object localObject = localClass.newInstance();  
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());  
                statusHeight = activity.getResources().getDimensionPixelSize(i5);  
            } catch (ClassNotFoundException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            } catch (InstantiationException e) {  
                e.printStackTrace();  
            } catch (NumberFormatException e) {  
                e.printStackTrace();  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (SecurityException e) {  
                e.printStackTrace();  
            } catch (NoSuchFieldException e) {  
                e.printStackTrace();  
            }  
        }  
        return statusHeight;  
    }  
}
