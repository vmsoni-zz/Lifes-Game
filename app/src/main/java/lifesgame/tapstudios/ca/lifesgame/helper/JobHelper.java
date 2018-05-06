package lifesgame.tapstudios.ca.lifesgame.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import lifesgame.tapstudios.ca.lifesgame.model.ScheduledNotification;

/**
 * Created by viditsoni on 2018-03-28.
 */

public class JobHelper implements JobCreator {

    private Context context;

    public JobHelper(Context context) {
        super();
        this.context = context;
    }

    @Nullable
    @Override
    public Job create(@NonNull String tag) {

        switch (tag) {
            case ScheduledNotification.TAG:
                return new ScheduledNotification(context);
            default:
                return null;
        }
    }
}
