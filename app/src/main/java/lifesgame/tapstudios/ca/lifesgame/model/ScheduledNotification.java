package lifesgame.tapstudios.ca.lifesgame.model;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import br.com.goncalves.pugnotification.notification.PugNotification;
import lifesgame.tapstudios.ca.lifesgame.R;

/**
 * Created by viditsoni on 2018-03-28.
 */
public class ScheduledNotification extends Job {
    public static final String TAG = "scheduled-reminder";
    private Context context;

    public ScheduledNotification(Context context) {
        super();
        this.context = context;

    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        PersistableBundleCompat extras = params.getExtras();
        String notificationTitle = extras.getString("title", "");
        String notificationMessage = extras.getString("message", notificationTitle);
        PugNotification.with(context)
                .load()
                .title(notificationTitle)
                .message(notificationMessage)
                .smallIcon(R.mipmap.notification_icon_transparent)
                .largeIcon(R.mipmap.icon_lifes_game)
                .flags(Notification.DEFAULT_ALL)
                .simple()
                .build();
        return Result.SUCCESS;
    }
}
