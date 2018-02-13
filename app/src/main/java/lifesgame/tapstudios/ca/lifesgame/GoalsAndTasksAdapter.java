package lifesgame.tapstudios.ca.lifesgame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public void removeItem(int position, Boolean completed, Boolean deleted) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        try {
            if (goalsAndTasks.get(position) != null) {
                databaseHelper.deleteData(goalsAndTasks.get(position).getId(), completed, currentTime, deleted);
                goalsAndTasks.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, goalsAndTasks.size());
            }
        }
        catch (Exception e) {

        }
    }

    public void deleteItemPermanent(int position) {
        databaseHelper.deleteDataPermanent(goalsAndTasks.get(position).getId());
        goalsAndTasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, goalsAndTasks.size());
    }

    public void updateDailyCompleted(int position, Boolean completed, Boolean deleted) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        databaseHelper.deleteData(goalsAndTasks.get(position).getId(), completed, currentTime, deleted);
    }

    public Long addDailyForNewDay(int position) {
        return databaseHelper.addData(
                goalsAndTasks.get(position).getDescription(),
                goalsAndTasks.get(position).getCategory().getTodoTypeString(),
                goalsAndTasks.get(position).getTitle(),
                goalsAndTasks.get(position).getSilver(),
                goalsAndTasks.get(position).getImprovementTypeMap(),
                goalsAndTasks.get(position).getDeadlineDateString(),
                goalsAndTasks.get(position).getCompletionDateString()
        );
    }

    public void removeDaily(int position, Boolean completed, Boolean deleted) {
        try {
            if (goalsAndTasks.get(position) != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String completionDate = sdf.format(goalsAndTasks.get(position).getCompletionDate());
                databaseHelper.deleteData(goalsAndTasks.get(position).getId(), completed, completionDate, deleted);
            }
        }
        catch (Exception e) {

        }
    }
}
