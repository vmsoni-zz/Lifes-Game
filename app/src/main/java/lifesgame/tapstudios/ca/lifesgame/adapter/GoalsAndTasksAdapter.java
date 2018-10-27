package lifesgame.tapstudios.ca.lifesgame.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.holder.GoalsAndTasksHolder;
import lifesgame.tapstudios.ca.lifesgame.service.JobService;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GameMechanicsHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GoalsAndTasksHelper;
import lifesgame.tapstudios.ca.lifesgame.model.GoalsAndTasks;

/**
 * Created by Vidit Soni on 5/24/2017.
 */
public class GoalsAndTasksAdapter extends RecyclerView.Adapter<GoalsAndTasksHolder> {
    public List<GoalsAndTasks> goalsAndTasks;
    public LayoutInflater inflater;
    public DatabaseHelper databaseHelper;
    private int resource;
    private Context context;
    private GameMechanicsHelper gameMechanicsHelper;
    private GoalsAndTasksHelper goalsAndTasksHelper;
    private JobService jobService;

    public GoalsAndTasksAdapter(Context context,
                                int resource,
                                GameMechanicsHelper gameMechanicsHelper,
                                DatabaseHelper databaseHelper,
                                List<GoalsAndTasks> objects,
                                GoalsAndTasksHelper goalsAndTasksHelper,
                                LayoutInflater inflater) {
        this.context = context;
        this.resource = resource;
        this.gameMechanicsHelper = gameMechanicsHelper;
        this.databaseHelper = databaseHelper;
        this.goalsAndTasksHelper = goalsAndTasksHelper;
        goalsAndTasks = objects;
        this.inflater = inflater;
        jobService = new JobService(context);
    }

    @Override
    public GoalsAndTasksHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(resource, parent, false);
        return new GoalsAndTasksHolder(context, view, goalsAndTasksHelper, this, gameMechanicsHelper);
    }

    @Override
    public void onBindViewHolder(GoalsAndTasksHolder holder, int position) {
        GoalsAndTasks goalsAndTasks = this.goalsAndTasks.get(position);
        holder.bindGoalsAndTasks(goalsAndTasks, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return goalsAndTasks.size();
    }

    public GoalsAndTasks removeItemFromList(int position) {
        GoalsAndTasks goalsAndTask = goalsAndTasks.get(position);
        goalsAndTasks.remove(position);
        return goalsAndTask;
    }

    public void addItemToList(int position, GoalsAndTasks goalsAndTask) {
        goalsAndTasks.add(position, goalsAndTask);
    }

    public void removeItem(GoalsAndTasks goalsAndTask, Boolean completed, Boolean deleted) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        int completedCount = 0;
        int failedCount = 0;
        if (completed) {
            completedCount++;
        }
        else {
            failedCount++;
        }
        try {
            if (goalsAndTask != null) {
                databaseHelper.deleteData(goalsAndTask.getId(), completed, currentTime, deleted, completedCount, failedCount);
            }
            Integer notificationId = goalsAndTask.getNotificationId();
            if (notificationId != null) {
                jobService.cancelNotification(notificationId);
            }
        }
        catch (Exception e) {

        }
    }

    public void deleteItemPermanent(int position) {
        databaseHelper.deleteDataPermanent(goalsAndTasks.get(position).getId());
        goalsAndTasks.remove(position);
        Integer notificationId = goalsAndTasks.get(position).getNotificationId();
        if (notificationId != null) {
            jobService.cancelNotification(notificationId);
        }
    }

    public void updateDailyCompleted(int position, Boolean completed, Boolean deleted) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        goalsAndTasks.get(position).setCompletionDate(dt);
        if (completed) {
            goalsAndTasks.get(position).setCompletedCount((goalsAndTasks.get(position).getCompletedCount()) + 1);
        }
        else {
            goalsAndTasks.get(position).setFailedCount((goalsAndTasks.get(position).getFailedCount()) + 1);
        }
        databaseHelper.deleteData(goalsAndTasks.get(position).getId(), completed, currentTime, deleted, goalsAndTasks.get(position).getCompletedCount(), goalsAndTasks.get(position).getFailedCount());
    }

    public Long addDailyForNewDay(int position) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return databaseHelper.addData(
                goalsAndTasks.get(position).getDescription(),
                goalsAndTasks.get(position).getCategory().getTodoTypeString(),
                goalsAndTasks.get(position).getTitle(),
                goalsAndTasks.get(position).getSilver(),
                goalsAndTasks.get(position).getImprovementTypeMap(),
                goalsAndTasks.get(position).getDeadlineDateStringDatabase(),
                sdf.format(date),
                goalsAndTasks.get(position).getStartDateStringDatabase(),
                goalsAndTasks.get(position).getNotificationDateString(),
                goalsAndTasks.get(position).getCompletedCount(),
                goalsAndTasks.get(position).getFailedCount(),
                goalsAndTasks.get(position).getNotificationId()
        );
    }

    public void updateDailySkippedDaily(int position, Boolean completed, Boolean deleted) {
        try {
            if (goalsAndTasks.get(position) != null) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String completionDate = sdf.format(date);
                goalsAndTasks.get(position).setCompletionDate(date);
                databaseHelper.deleteData(goalsAndTasks.get(position).getId(), completed, completionDate, deleted, goalsAndTasks.get(position).getCompletedCount(), goalsAndTasks.get(position).getFailedCount());
            }
        }
        catch (Exception e) {
        }
    }

    public void removeDaily(int position, Boolean completed, Boolean deleted) {
        try {
            if (goalsAndTasks.get(position) != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String completionDate = sdf.format(goalsAndTasks.get(position).getCompletionDate());
                databaseHelper.deleteData(goalsAndTasks.get(position).getId(), completed, completionDate, deleted, goalsAndTasks.get(position).getCompletedCount(), goalsAndTasks.get(position).getFailedCount());
            }
        }
        catch (Exception e) {
        }
    }
}
