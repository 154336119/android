package com.huizhuang.zxsq.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.Html;

import com.huizhuang.hz.R;


public class NotificationUtil {
	
	public static final int NOTIFY_ID_RECOMMEND_DIARY=3;
	
	
    /**
     * 创建通用的Notification
     * 
     * @param context
     * @param intent
     * @param title
     * @param content
     * @param tickerText
     * @return
     */
    private static Notification createNotification(Context context, Intent intent, String title, String content, String tickerText) {
    	LogUtil.i("createNotification intent:"+intent);
        Builder builder = new Builder(context);
        builder.setAutoCancel(true);
        if (null != intent) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);
        }
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(Html.fromHtml(title));
        builder.setContentText(Html.fromHtml(content));
        builder.setTicker(Html.fromHtml(tickerText));
        Notification notification = builder.build();
        // TODO - 后期扩展（在这里可以实现Notification的开关音效）
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        return notification;
    }
    /**
     * 显示特定NotifyId的Notification
     * 
     * @param context
     * @param notifyId
     * @param notification
     */
    private static void showNotificationByNotifyId(Context context, int notifyId, Notification notification) {
    	LogUtil.i("showNotificationByNotifyId");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, notification);
    }

    /**
     * 取消特定NotifyId的Notification
     * 
     * @param context
     * @param notifyId
     */
    public static void cancelNotificationByNotifyId(Context context, int notifyId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notifyId);
    }
    
    /**
     * 取消所有的Notification
     * 
     * @param context
     */
    public static void cancelAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
    
//    public static void notifyRecommendDiary(Context context, String title, String content, String tickerText,Author author) {
//    	LogUtil.i(" notifyRecommendDiary");
//        Intent intent = new Intent(context, DiaryDetailActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(AppConstants.PARAM_AUTHOR, author);
//        Notification notification = createNotification(context, intent, title, content, tickerText);
//        showNotificationByNotifyId(context, NOTIFY_ID_RECOMMEND_DIARY, notification);
//    }

}
