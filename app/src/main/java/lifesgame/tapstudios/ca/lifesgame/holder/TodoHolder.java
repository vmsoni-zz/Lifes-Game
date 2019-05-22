/*
package lifesgame.tapstudios.ca.lifesgame.holder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.adapter.GoalsAndTasksAdapter;
import lifesgame.tapstudios.ca.lifesgame.helper.GameMechanicsHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GoalsAndTasksHelper;
import lifesgame.tapstudios.ca.lifesgame.model.GoalsAndTasks;
import lifesgame.tapstudios.ca.lifesgame.model.TodoType;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;

*/
/**
 * Created by viditsoni on 2017-11-04.
 *//*


public class TodoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.editButton) ImageButton editBtn;
    @BindView(R.id.deleteButton) ImageButton deleteBtn;
    @BindView(R.id.completedButton) ImageButton completedBtn;
    @BindView(R.id.failedButton) ImageButton failedBtn;
    @BindView(R.id.goal_task_title) TextView todoTitle;
    @BindView(R.id.goal_task_description) TextView todoDescription;
    @BindView(R.id.goal_task_remaining_time) TextView todoRemainingTime;
    @BindView(R.id.goal_task_start_date_tv) TextView todoStartDatetv;
    @BindView(R.id.todo_type) TextView todoTodoType;
    @BindView(R.id.silver_reward) TextView silverReward;
    @BindView(R.id.daily_completed_count_tv) TextView completedCountTV;
    @BindView(R.id.daily_failed_count_tv) TextView failedCountTV;
    @BindView(R.id.button_divider_completed) View buttonDividerCompleted;
    @BindView(R.id.button_divider_failed) View buttonDividerFailed;
    @BindView(R.id.goal_task_remaining_time_ll) LinearLayout todoRemainingTimeLL;
    @BindView(R.id.goal_task_start_date_ll) LinearLayout todoStartDateLl;
    @BindView(R.id.daily_count_ll) LinearLayout dailyCountLl;
    @BindView(R.id.dynamic_ll) LinearLayout dynamicLl;
    @BindView(R.id.daily_skip_day_Ll) LinearLayout dailySkipDayLl;

    private TaskTodo taskTodo;
    private Context context;
    private GoalsAndTasksHelper todoHelper;
    private GoalsAndTasksAdapter todoAdapter;
    private GameMechanicsHelper gameMechanicsHelper;
    private Map<String, ImageView> improvementTypeImageViews;

    private static final String TABLE_TASKS_GOALS_HEALTH_EXERCISE = "health_exercise";
    private static final String TABLE_TASKS_GOALS_WORK = "work";
    private static final String TABLE_TASKS_GOALS_SCHOOL = "school";
    private static final String TABLE_TASKS_GOALS_FAMILY_FRIENDS = "family_friends";
    private static final String TABLE_TASKS_GOALS_LEARNING = "learning";
    private static final String TABLE_TASKS_GOALS_OTHER = "other";

    public TodoHolder(Context context,
                      View itemView,
                      GoalsAndTasksHelper todoHelper,
                      GoalsAndTasksAdapter todoAdapter,
                      GameMechanicsHelper gameMechanicsHelper) {
        super(itemView);

        this.context = context;
        this.todoHelper = todoHelper;
        this.todoAdapter = todoAdapter;
        this.gameMechanicsHelper = gameMechanicsHelper;
        ButterKnife.bind(this, itemView);

        improvementTypeImageViews = new HashMap<>();
        itemView.setOnClickListener(this);
        initializeImprovementTypeImageViews();
    }

    @Override
    public void onClick(View view) {
        if (this.taskTodo != null) {
            Toast.makeText(this.context, "Clicked on " + this.taskTodo.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    public void bindGoalsAndTasks(TaskTodo taskTodo, final int position) {

        this.taskTodo = taskTodo;
        todoTitle.setText(taskTodo.getTitle());
        if(taskTodo.getDescription() == null || taskTodo.getDescription().isEmpty()) {
            todoDescription.setVisibility(View.GONE);
        }
        else {
            todoDescription.setVisibility(View.VISIBLE);
            todoDescription.setText(taskTodo.getDescription());
        }
        todoTodoType.setText(taskTodo*/
/**//*
.getCategory().getTodoTypeString());
        silverReward.setText(String.valueOf(taskTodo.getSilver()));
        setupTodoTypeColor(taskTodo);

        bindTask();
        initializeImprovementTypeImages(taskTodo);
        initializeOnClickListeners(position);
    }

    private void initializeImprovementTypeImageViews() {
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_HEALTH_EXERCISE, (ImageView) itemView.findViewById(R.id.health_exercise));
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_WORK, (ImageView) itemView.findViewById(R.id.work_img));
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_SCHOOL, (ImageView) itemView.findViewById(R.id.school_img));
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_FAMILY_FRIENDS, (ImageView) itemView.findViewById(R.id.family_friends));
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_LEARNING, (ImageView) itemView.findViewById(R.id.learning_img));
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_OTHER, (ImageView) itemView.findViewById(R.id.other_img));
    }

    private void initializeImprovementTypeImages(GoalsAndTasks todo) {
        Map<String, Boolean> improvementTypes = todo.getImprovementTypeMap();
        for (Map.Entry<String, Boolean> improvement : improvementTypes.entrySet()) {
            if (improvement.getValue()) {
                improvementTypeImageViews.get(improvement.getKey())
                        .setImageResource(itemView.getResources()
                                .getIdentifier(improvement.getKey(), "drawable", "lifesgame.tapstudios.ca.lifesgame"));
            }
            else {
                improvementTypeImageViews.get(improvement.getKey())
                        .setImageResource(itemView.getResources()
                                .getIdentifier("not_selected_" + improvement.getKey(), "drawable", "lifesgame.tapstudios.ca.lifesgame"));
            }
        }
    }

    private void initializeOnClickListeners(final int position) {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoHelper.displayGoalsAndTasksEditMenu(taskTodo.getId());
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoAdapter.deleteItemPermanent(position);
                todoAdapter.notifyItemRemoved(position);
                todoAdapter.notifyItemRangeChanged(position, todoAdapter.getItemCount());
            }
        });

        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskTodo.getCategory() == TodoType.DAILY) {
                    disableUpdateTodoOptions();
                    dailyShowSnackBarUpdateWithUndo(position, true, false, false);
                }
                else {
                    todoAdapter.removeItemFromList(position);
                    todoAdapter.notifyItemRemoved(position);
                    todoAdapter.notifyItemRangeChanged(position, todoAdapter.getItemCount());
                    showSnackBarDeleteWithUndo(position, true, true);
                }
            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskTodo.getCategory() == TodoType.DAILY) {
                    disableUpdateTodoOptions();
                    dailyShowSnackBarUpdateWithUndo(position, false, false, false);
                }
                else {
                    todoAdapter.removeItemFromList(position);
                    todoAdapter.notifyItemRemoved(position);
                    todoAdapter.notifyItemRangeChanged(position, todoAdapter.getItemCount());
                    showSnackBarDeleteWithUndo(position, false, true);
                }
            }
        });

        dailySkipDayLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskTodo.getCategory() == TodoType.DAILY) {
                    disableUpdateTodoOptions();
                    dailyShowSnackBarUpdateWithUndo(position, true, false, true);
                }
            }
        });
    }

    private void disableDailyButtonValuesAndUpdateCounts(int completedCount, int failedCount) {
        disableUpdateTodoOptions();
        completedCountTV.setText(String.valueOf(completedCount));
        failedCountTV.setText(String.valueOf(failedCount));
        dailySkipDayLl.setVisibility(View.GONE);
    }

    private void disableUpdateTodoOptions() {
        completedBtn.setEnabled(false);
        completedBtn.setImageResource(R.drawable.disabled_checkmark);
        failedBtn.setEnabled(false);
        failedBtn.setImageResource(R.drawable.disabled_xmark);
    }

    private void enableUpdateTodoOptions() {
        completedBtn.setEnabled(true);
        completedBtn.setImageResource(R.drawable.check_mark);
        failedBtn.setEnabled(true);
        failedBtn.setImageResource(R.drawable.x_mark);
    }

    private Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        return date;
    }


    private void setupTodoTypeColor(GoalsAndTasks todo) {
        switch (todo.getCategory()) {
            case DAILY:
                todoTodoType.setTextColor(Color.parseColor("#e67e22"));
                buttonDividerCompleted.setBackgroundColor(Color.parseColor("#e67e22"));
                buttonDividerFailed.setBackgroundColor(Color.parseColor("#e67e22"));
                break;
            case GOAL:
                todoTodoType.setTextColor(Color.parseColor("#8e44ad"));
                buttonDividerCompleted.setBackgroundColor(Color.parseColor("#8e44ad"));
                buttonDividerFailed.setBackgroundColor(Color.parseColor("#8e44ad"));
                break;
            case TASK:
                todoTodoType.setTextColor(Color.parseColor("#27ae60"));
                buttonDividerCompleted.setBackgroundColor(Color.parseColor("#27ae60"));
                buttonDividerFailed.setBackgroundColor(Color.parseColor("#27ae60"));
                break;
        }
    }

    private void displayCustomToastXPAndSilver() {
        gameMechanicsHelper.addSilver(taskTodo.getSilver());
        Map<String, Boolean> improvementType = taskTodo.getImprovementTypeMap();
        for (String type : improvementType.keySet()) {
            if (improvementType.get(type)) {
                gameMechanicsHelper.addImprovementType(type);
            }
        }
        Integer xpAmount = taskTodo.getSilver()/10;
        if(taskTodo.getCategory() == TodoType.GOAL) {
            long diff = getZeroTimeDate(new Date()).getTime() - getZeroTimeDate(taskTodo.getCreationDate()).getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000)+1;
            xpAmount += (int) diffDays;
        }
        boolean levelUp = gameMechanicsHelper.addXp(xpAmount);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast_layout_xp_silver, null);
        ((TextView) view.findViewById(R.id.silver_reward_toast)).setText("+ " + String.valueOf(todo.getSilver()));
        ((TextView) view.findViewById(R.id.xp_reward_toast)).setText("+ " + xpAmount);
        if (levelUp) {
            ((LinearLayout) view.findViewById(R.id.levelup_ll)).setVisibility(View.VISIBLE);
        }
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.show();
    }

    private void displaySingleCustomToast(Integer value, String type) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast_health, null);
        if (type.equals("Health")) {
            gameMechanicsHelper.removeHealth();
            ((ImageView) view.findViewById(R.id.toast_icon)).setImageResource(R.drawable.health);
            ((TextView) view.findViewById(R.id.value_changed)).setText("- " + value);
        }
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.show();
    }

    private void setupStartDate() {
        if(taskTodo.getStartDateString() != null) {
            todoStartDateLl.setVisibility(View.VISIBLE);
            todoStartDatetv.setText(taskTodo.getStartDateString());
            if (getZeroTimeDate(new Date()).compareTo(getZeroTimeDate(taskTodo.getStartDate())) < 0) {
                disableUpdateTodoOptions();
            }
        }
    }

    private void showSnackBarDeleteWithUndo(int position, Boolean completed, Boolean deleted) {
        List<GoalsAndTasks> undoList = new ArrayList<>();
        Snackbar snackbar = Snackbar
                .make(((Activity) context).findViewById(R.id.activity_main), "Updated Todo", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        todoAdapter.addItemToList(position, taskTodo);
                        todoAdapter.notifyItemInserted(position);
                        undoList.add(taskTodo);
                    }
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (undoList.size() == 0) {
                            todoAdapter.removeItem(taskTodo, completed, deleted);
                            if (completed) {
                                displayCustomToastXPAndSilver();
                            }
                            else {
                                displaySingleCustomToast(10, "Health");
                            }
                        }
                    }
                });
        snackbar.show();
    }

    private void dailyShowSnackBarUpdateWithUndo(int position, boolean completed, boolean deleted, boolean skippedDay) {
        List<GoalsAndTasks> undoList = new ArrayList<>();
        Snackbar snackbar = Snackbar
                .make(((Activity) context).findViewById(R.id.activity_main), "Updated Todo", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enableUpdateTodoOptions();
                        undoList.add(taskTodo);
                    }
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (undoList.size() == 0) {
                            if(skippedDay) {
                                todoAdapter.updateDailySkippedDaily(position, completed, deleted);
                            }
                            else {
                                todoAdapter.updateDailyCompleted(position, completed, deleted);
                                if (completed) {
                                    displayCustomToastXPAndSilver();
                                }
                                else {
                                    displaySingleCustomToast(10, "Health");
                                }
                            }
                            disableDailyButtonValuesAndUpdateCounts(taskTodo.getCompletedCount(), taskTodo.getFailedCount());
                        }
                    }
                });
        snackbar.show();
    }

    private void bindTask() {
        dailyCountLl.setVisibility(View.GONE);
        todoRemainingTimeLL.setVisibility(View.GONE);
        dailySkipDayLl.setVisibility(View.GONE);
        todoStartDateLl.setVisibility(View.VISIBLE);
        setupStartDate();
    }

    private void bindGoal() {
        todoRemainingTimeLL.setVisibility(View.VISIBLE);
        dailySkipDayLl.setVisibility(View.GONE);
        dailyCountLl.setVisibility(View.GONE);
        todoStartDateLl.setVisibility(View.GONE);
        todoRemainingTime.setText(taskTodo.getDeadlineDateString());
    }

    private void bindDaily(int position) {
        dailyCountLl.setVisibility(View.VISIBLE);
        dailySkipDayLl.setVisibility(View.VISIBLE);
        todoStartDateLl.setVisibility(View.GONE);
        completedCountTV.setText(String.valueOf(taskTodo.getCompletedCount()));
        failedCountTV.setText(String.valueOf(taskTodo.getFailedCount()));

        Date todayDate = new Date();
        Date completionDate = taskTodo.getCompletionDate();
        if (completionDate != null) {
            int dateComparison = getZeroTimeDate(todayDate).compareTo(getZeroTimeDate(completionDate));
            if (dateComparison > 0) {
                todoAdapter.removeDaily(position, taskTodo.getCompleted(), true);
                taskTodo.setCompleted(false);
                taskTodo.setId(todoAdapter.addDailyForNewDay(position));
            }
            if (dateComparison == 0) {
                disableDailyButtonValuesAndUpdateCounts(taskTodo.getCompletedCount(), taskTodo.getFailedCount());
            }
        }
    }
}
*/
