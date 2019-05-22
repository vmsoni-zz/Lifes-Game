package lifesgame.tapstudios.ca.lifesgame.utility;

/**
 * Created by viditsoni on 2018-02-18.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import lifesgame.tapstudios.ca.lifesgame.R;

import lifesgame.tapstudios.ca.lifesgame.activity.MainActivity;

public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("RECIEVED2");
        Intent i = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context, "default");
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        b.setSound(notification)
                .setContentTitle("Lifes Game")
                .setAutoCancel(true)
                .setContentText("Time is up!")
                .setSmallIcon(R.mipmap.notification_icon_transparent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.icon_lifes_game))
                .setContentIntent(pIntent);

        Notification n = b.build();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, n);
    }
}