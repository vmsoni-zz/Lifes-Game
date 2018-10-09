package lifesgame.tapstudios.ca.lifesgame;

import android.content.Context;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lifesgame.tapstudios.ca.lifesgame.helper.JobHelper;
import lifesgame.tapstudios.ca.lifesgame.model.NotificationDate;
import lifesgame.tapstudios.ca.lifesgame.utility.DateUtils;

import static java.util.Calendar.HOUR;
import static lifesgame.tapstudios.ca.lifesgame.model.ScheduledNotification.TAG;

/**
 * Created by viditsoni on 2018-03-31.
 */

public class JobService {

    private final Context context;

    public JobService(Context context) {
        this.context = context;
        JobManager.create(context).addJobCreator(new JobHelper(context));
    }

    public int setFutureNotification(NotificationDate notificationDate, String notificationTitle, String notificationMessage) {
        Date currentDateTime = new Date();
        PersistableBundleCompat extra = new PersistableBundleCompat();
        extra.putString("title", notificationTitle);
        extra.putString("message", notificationMessage);
        long jobRunTimeMillis = DateUtils.getZeroTimeDate(notificationDate.getNotificationDate()).getTime() + notificationDate.getMilliseconds() - currentDateTime.getTime();
        int id = new JobRequest.Builder(TAG)
                .setExact(jobRunTimeMillis)
                .setExtras(extra)
                .build()
                .schedule();
        return id;
    }

    public int setFutureNotificationDailies(NotificationDate notificationDate, String notificationTitle, String notificationMessage) {
        PersistableBundleCompat extra = new PersistableBundleCompat();
        extra.putString("title", notificationTitle);
        extra.putString("message", notificationMessage);
        long jobStartTimeMillis = notificationDate.getMilliseconds();
        long jobEndTimeMillis = jobStartTimeMillis + TimeUnit.MINUTES.toMillis(30);

        return DailyJob.schedule(new JobRequest.Builder(TAG).setExtras(extra), jobStartTimeMillis, jobEndTimeMillis);
    }


    public void cancelJobById(int id) {
        JobManager.instance().cancel(id);
    }

    private long timeTillNotification(Date date) {
        Calendar c = Calendar.getInstance();
        return (date.getTime() - c.getTimeInMillis());
    }

    private long timeInMillisSinceStartOfDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (date.getTime() - Calendar.getInstance().getTimeInMillis());
    }
}
