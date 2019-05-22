package lifesgame.tapstudios.ca.lifesgame.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.model.NotificationDate;
import lifesgame.tapstudios.ca.lifesgame.utility.DateUtils;
import lifesgame.tapstudios.ca.lifesgame.utility.TimeReceiver;

/**
 * Created by viditsoni on 2018-03-31.
 */

public class JobService {

    private final Context context;

    public JobService(Context context) {
        this.context = context;
    }

    public int setFutureNotification(NotificationDate notificationDate, String notificationTitle, String notificationMessage) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(notificationDate.getNotificationDate());
        cal.set(Calendar.HOUR, notificationDate.getHour());
        cal.set(Calendar.MINUTE, notificationDate.getMinute());
        int notificationId = (int) System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, TimeReceiver.class);
        notificationIntent.putExtra(NotificationService.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(NotificationService.NOTIFICATION, generateNotification(notificationTitle, notificationMessage));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        return notificationId;
    }

    public int setFutureNotificationDailies(NotificationDate notificationDate, String notificationTitle, String notificationMessage) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(notificationDate.getNotificationDate());
        int notificationId = (int) System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, NotificationService.class);
        notificationIntent.putExtra(NotificationService.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationService.NOTIFICATION, generateNotification(notificationTitle, notificationMessage));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        return notificationId;
    }

    private Notification generateNotification(String notificationTitle, String notificationMessage) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default_channel");
        builder.setContentTitle(notificationTitle);
        if (notificationMessage != null) {
            builder.setContentText(notificationMessage);
        }
        builder.setSmallIcon(R.mipmap.notification_icon_transparent);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_lifes_game));
        return builder.build();
    }

    public void cancelNotification(int notificationId) {
        Intent intent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
