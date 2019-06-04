package lifesgame.tapstudios.ca.lifesgame.adapter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.holder.TaskTodoHolder;
import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.HabitTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.JoinModel.TaskTodoAndTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.JoinModel.generic.TodoAndTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTypes;
import lifesgame.tapstudios.ca.lifesgame.modelV2.generic.Todo;
import lifesgame.tapstudios.ca.lifesgame.repository.TodoRepository;
import lifesgame.tapstudios.ca.lifesgame.service.JobService;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GameMechanicsHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GoalsAndTasksHelper;

/**
 * Created by Vidit Soni on 5/24/2017.
 */
public class GoalsAndTasksAdapter extends RecyclerView.Adapter<TaskTodoHolder> {
    public List<Object> todoList;
    public LayoutInflater inflater;
    public DatabaseHelper databaseHelper;
    private Context context;
    private GameMechanicsHelper gameMechanicsHelper;
    private GoalsAndTasksHelper goalsAndTasksHelper;
    private JobService jobService;
    private TodoRepository todoRepository;

    public GoalsAndTasksAdapter(Context context,
                                GameMechanicsHelper gameMechanicsHelper,
                                DatabaseHelper databaseHelper,
                                List<Object> todoList,
                                GoalsAndTasksHelper goalsAndTasksHelper,
                                LayoutInflater inflater) {
        this.context = context;
        this.gameMechanicsHelper = gameMechanicsHelper;
        this.databaseHelper = databaseHelper;
        this.goalsAndTasksHelper = goalsAndTasksHelper;
        this.todoList = todoList;
        this.inflater = inflater;
        this.todoRepository = ViewModelProviders.of((FragmentActivity) context).get(TodoRepository.class);
        jobService = new JobService(context);
    }

    @Override
    public int getItemViewType(int position) {
        return ((TodoAndTag) todoList.get(position)).getTodo().getTodoType().ordinal();
    }

    @Override
    public TaskTodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource;
        switch(TodoTypes.TodoType.values()[viewType]) {
            case TASK:
                View view = inflater.inflate(R.layout.todo_task_row, parent, false);
                return new TaskTodoHolder(context, view, goalsAndTasksHelper, this, gameMechanicsHelper);
            case HABIT:
                break;
            case DAILY:
                break;
        }

        return new TaskTodoHolder(context, null, goalsAndTasksHelper, this, gameMechanicsHelper);
    }

    @Override
    public void onBindViewHolder(TaskTodoHolder holder, int position) {
        Object todo = this.todoList.get(position);
        holder.bindGoalsAndTasks(((TodoAndTag) todo).getTodo(), position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Object removeItemFromList(int position) {
        Object todo = todoList.get(position);
        todoList.remove(position);
        return todo;
    }

    public void addItemToList(int position, Object goalsAndTask) {
        todoList.add(position, goalsAndTask);
    }

    public void removeItem(Todo todo, Boolean completed, Boolean deleted) {
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
            if (todo != null) {
                switch (todo.getTodoType()) {
                    case TASK:
                        todoRepository.deleteTaskTodo((TaskTodo) todo);
                        break;
                    case DAILY:
                        todoRepository.deleteDailyTodo((DailyTodo) todo);
                        break;
                    case HABIT:
                        todoRepository.deleteHabitTodo((HabitTodo) todo);
                        break;
                }
                databaseHelper.deleteData(todo.getId(), completed, currentTime, deleted, completedCount, failedCount);
            }
            //TODO: Fix notification stuff
/*            Integer notificationId = taskTodo.getNotificationId();
            if (notificationId != -1) {
                jobService.cancelNotification(notificationId);
            }*/
        }
        catch (Exception e) {

        }
    }

    //TODO: Fix daily functionality
/*    public void deleteItemPermanent(int position) {
        databaseHelper.deleteDataPermanent(goalsAndTasks.get(position).getId());
        goalsAndTasks.remove(position);
        Integer notificationId = goalsAndTasks.get(position).getNotificationId();
        if (notificationId != -1) {
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
    }*/

    public Long addDailyForNewDay(int position) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

/*        return databaseHelper.addData(
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
        );*/
        return 1L;
    }

    //TODO: Fix Daily functionality
/*    public void updateDailySkippedDaily(int position, Boolean completed, Boolean deleted) {
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
    }*/

    public void setData(List<?> newData) {
        todoList.clear();
        if (newData != null) {
            todoList.addAll(newData);
            this.notifyDataSetChanged();
        }

    }
}
