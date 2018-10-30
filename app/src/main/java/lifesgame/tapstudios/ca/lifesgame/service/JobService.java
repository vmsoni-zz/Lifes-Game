package lifesgame.tapstudios.ca.lifesgame.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.model.NotificationDate;
import lifesgame.tapstudios.ca.lifesgame.utility.DateUtils;

/**
 * Created by viditsoni on 2018-03-31.
 */

public class JobService {

    private final Context context;

    public JobService(Context context) {
        this.context = context;
    }

    public int setFutureNotification(NotificationDate notificationDate, String notificationTitle, String notificationMessage) {
        long jobRunTimeMillis = DateUtils.getZeroTimeDate(notificationDate.getNotificationDate()).getTime() + notificationDate.getMilliseconds() - System.currentTimeMillis();
        int notificationId = (int) System.currentTimeMillis();

        Intent notificationIntent = new Intent(context, NotificationService.class);
        notificationIntent.putExtra(NotificationService.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationService.NOTIFICATION, generateNotification(notificationTitle, notificationMessage));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate.getNotificationDate().getTime(), pendingIntent);

        return notificationId;
    }

    public int setFutureNotificationDailies(NotificationDate notificationDate, String notificationTitle, String notificationMessage) {
        long jobRunTimeMillis = DateUtils.getZeroTimeDate(notificationDate.getNotificationDate()).getTime() + notificationDate.getMilliseconds() - System.currentTimeMillis();
        int notificationId = (int) System.currentTimeMillis();

        Intent notificationIntent = new Intent(context, NotificationService.class);
        notificationIntent.putExtra(NotificationService.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationService.NOTIFICATION, generateNotification(notificationTitle, notificationMessage));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, jobRunTimeMillis, AlarmManager.INTERVAL_DAY, pendingIntent);

        return notificationId;
    }

    private Notification generateNotification(String notificationTitle, String notificationMessage) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationMessage);
        builder.setSmallIcon(R.drawable.ic_launcher);

        return builder.build();
    }

    public void cancelNotification(int notificationId) {
        Intent intent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
