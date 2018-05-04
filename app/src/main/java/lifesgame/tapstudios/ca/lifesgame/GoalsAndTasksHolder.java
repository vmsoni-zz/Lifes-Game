package lifesgame.tapstudios.ca.lifesgame;

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

import lifesgame.tapstudios.ca.lifesgame.helper.GameMechanicsHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GoalsAndTasksHelper;
import lifesgame.tapstudios.ca.lifesgame.model.GoalsAndTasks;

/**
 * Created by viditsoni on 2017-11-04.
 */

public class GoalsAndTasksHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ImageButton editBtn;
    private final ImageButton deleteBtn;
    private final ImageButton completedBtn;
    private final ImageButton failedBtn;
    private final TextView goalsAndTasksTitle;
    private final TextView goalsAndTasksDescription;
    private final TextView goalsAndTasksRemainingTime;
    private final TextView todoStartDatetv;
    private final TextView goalsAndTasksTodoType;
    private final TextView silverReward;
    private final TextView completedCountTV;
    private final TextView failedCountTV;
    private final View buttonDividerCompleted;
    private final View buttonDividerFailed;
    private final LinearLayout goalsAndTasksRemainingTimeLL;
    private final LinearLayout todoStartDateLl;
    private final LinearLayout dailyCountLl;
    private final LinearLayout dynamicLl;
    private final LinearLayout dailySkipDayLl;
    private GoalsAndTasks goalsAndTasks;
    private Context context;
    private GoalsAndTasksHelper goalsAndTasksHelper;
    private GoalsAndTasksAdapter goalsAndTasksAdapter;
    private GameMechanicsHelper gameMechanicsHelper;
    private Map<String, ImageView> improvementTypeImageViews;

    private static final String TABLE_TASKS_GOALS_HEALTH_EXERCISE = "health_exercise";
    private static final String TABLE_TASKS_GOALS_WORK = "work";
    private static final String TABLE_TASKS_GOALS_SCHOOL = "school";
    private static final String TABLE_TASKS_GOALS_FAMILY_FRIENDS = "family_friends";
    private static final String TABLE_TASKS_GOALS_LEARNING = "learning";
    private static final String TABLE_TASKS_GOALS_OTHER = "other";

    public GoalsAndTasksHolder(Context context,
                               View itemView,
                               GoalsAndTasksHelper goalsAndTasksHelper,
                               GoalsAndTasksAdapter goalsAndTasksAdapter,
                               GameMechanicsHelper gameMechanicsHelper) {
        super(itemView);

        this.context = context;
        this.goalsAndTasksHelper = goalsAndTasksHelper;
        this.goalsAndTasksAdapter = goalsAndTasksAdapter;
        this.gameMechanicsHelper = gameMechanicsHelper;

        editBtn = (ImageButton) itemView.findViewById(R.id.editButton);
        deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteButton);
        completedBtn = (ImageButton) itemView.findViewById(R.id.completedButton);
        failedBtn = (ImageButton) itemView.findViewById(R.id.failedButton);
        goalsAndTasksTitle = (TextView) itemView.findViewById(R.id.goal_task_title);
        goalsAndTasksDescription = (TextView) itemView.findViewById(R.id.goal_task_description);
        goalsAndTasksRemainingTime = (TextView) itemView.findViewById(R.id.goal_task_remaining_time);
        todoStartDatetv = (TextView) itemView.findViewById(R.id.goal_task_start_date_tv);
        goalsAndTasksTodoType = (TextView) itemView.findViewById(R.id.todo_type);
        silverReward = (TextView) itemView.findViewById(R.id.silver_reward);
        completedCountTV = (TextView) itemView.findViewById(R.id.daily_completed_count_tv);
        failedCountTV = (TextView) itemView.findViewById(R.id.daily_failed_count_tv);
        goalsAndTasksRemainingTimeLL = (LinearLayout) itemView.findViewById(R.id.goal_task_remaining_time_ll);
        todoStartDateLl = (LinearLayout) itemView.findViewById(R.id.goal_task_start_date_ll);
        dailyCountLl = (LinearLayout) itemView.findViewById(R.id.daily_count_ll);
        dynamicLl = (LinearLayout) itemView.findViewById(R.id.dynamic_ll);
        dailySkipDayLl = (LinearLayout) itemView.findViewById(R.id.daily_skip_day_Ll);
        buttonDividerCompleted = (View) itemView.findViewById(R.id.button_divider_completed);
        buttonDividerFailed = (View) itemView.findViewById(R.id.button_divider_failed);
        improvementTypeImageViews = new HashMap<>();
        itemView.setOnClickListener(this);
        initializeImprovementTypeImageViews();
    }

    @Override
    public void onClick(View view) {
        if (this.goalsAndTasks != null) {
            Toast.makeText(this.context, "Clicked on " + this.goalsAndTasks.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    public void bindGoalsAndTasks(GoalsAndTasks goalsAndTasks, final int position) {
        this.goalsAndTasks = goalsAndTasks;
        goalsAndTasksTitle.setText(goalsAndTasks.getTitle());
        if(goalsAndTasks.getDescription() == null || goalsAndTasks.getDescription().isEmpty()) {
            goalsAndTasksDescription.setVisibility(View.GONE);
        }
        else {
            goalsAndTasksDescription.setVisibility(View.VISIBLE);
            goalsAndTasksDescription.setText(goalsAndTasks.getDescription());
        }
        goalsAndTasksTodoType.setText(goalsAndTasks.getCategory().getTodoTypeString());
        silverReward.setText(String.valueOf(goalsAndTasks.getSilver()));
        setupTodoTypeColor(goalsAndTasks);
        if (goalsAndTasks.getCategory() == TodoType.GOAL) {
            goalsAndTasksRemainingTimeLL.setVisibility(View.VISIBLE);
            dailySkipDayLl.setVisibility(View.GONE);
            dailyCountLl.setVisibility(View.GONE);
            todoStartDateLl.setVisibility(View.GONE);
            goalsAndTasksRemainingTime.setText(goalsAndTasks.getDeadlineDateString());
        }
        else if(goalsAndTasks.getCategory() == TodoType.DAILY) {
            dailyCountLl.setVisibility(View.VISIBLE);
            dailySkipDayLl.setVisibility(View.VISIBLE);
            completedCountTV.setText(String.valueOf(goalsAndTasks.getCompletedCount()));
            failedCountTV.setText(String.valueOf(goalsAndTasks.getFailedCount()));
        }
        else {
            dailyCountLl.setVisibility(View.GONE);
            goalsAndTasksRemainingTimeLL.setVisibility(View.GONE);
            dailySkipDayLl.setVisibility(View.GONE);
            todoStartDateLl.setVisibility(View.VISIBLE);
            setupStartDate();
        }
        if (goalsAndTasks.getCategory() == TodoType.DAILY) {
            Date todayDate = new Date();
            Date completionDate = goalsAndTasks.getCompletionDate();
            if (completionDate != null) {
                int dateComparison = getZeroTimeDate(todayDate).compareTo(getZeroTimeDate(completionDate));
                if (dateComparison > 0) {
                    goalsAndTasksAdapter.removeDaily(position, goalsAndTasks.getCompleted(), true);
                    goalsAndTasks.setCompleted(false);
                    goalsAndTasks.setId(goalsAndTasksAdapter.addDailyForNewDay(position));
                }
                if (dateComparison == 0) {
                    disableDailyButtonValuesAndUpdateCounts(goalsAndTasks.getCompletedCount(), goalsAndTasks.getFailedCount());
                }
            }
        }
        initializeImprovementTypeImages(goalsAndTasks);
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

    private void initializeImprovementTypeImages(GoalsAndTasks goalsAndTasks) {
        Map<String, Boolean> improvementTypes = goalsAndTasks.getImprovementTypeMap();
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
                goalsAndTasksHelper.displayGoalsAndTasksEditMenu(goalsAndTasks.getId());
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalsAndTasksAdapter.deleteItemPermanent(position);
                goalsAndTasksAdapter.notifyItemRemoved(position);
                goalsAndTasksAdapter.notifyItemRangeChanged(position, goalsAndTasksAdapter.getItemCount());
            }
        });

        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goalsAndTasks.getCategory() == TodoType.DAILY) {
                    disableUpdateTodoOptions();
                    dailyShowSnackBarUpdateWithUndo(position, true, false, false);
                }
                else {
                    goalsAndTasksAdapter.removeItemFromList(position);
                    goalsAndTasksAdapter.notifyItemRemoved(position);
                    goalsAndTasksAdapter.notifyItemRangeChanged(position, goalsAndTasksAdapter.getItemCount());
                    showSnackBarDeleteWithUndo(position, true, true);
                }
            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goalsAndTasks.getCategory() == TodoType.DAILY) {
                    disableUpdateTodoOptions();
                    dailyShowSnackBarUpdateWithUndo(position, false, false, false);
                }
                else {
                    goalsAndTasksAdapter.removeItemFromList(position);
                    goalsAndTasksAdapter.notifyItemRemoved(position);
                    goalsAndTasksAdapter.notifyItemRangeChanged(position, goalsAndTasksAdapter.getItemCount());
                    showSnackBarDeleteWithUndo(position, false, true);
                }
            }
        });

        dailySkipDayLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goalsAndTasks.getCategory() == TodoType.DAILY) {
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


    private void setupTodoTypeColor(GoalsAndTasks goalsAndTasks) {
        switch (goalsAndTasks.getCategory()) {
            case DAILY:
                goalsAndTasksTodoType.setTextColor(Color.parseColor("#e67e22"));
                buttonDividerCompleted.setBackgroundColor(Color.parseColor("#e67e22"));
                buttonDividerFailed.setBackgroundColor(Color.parseColor("#e67e22"));
                break;
            case GOAL:
                goalsAndTasksTodoType.setTextColor(Color.parseColor("#8e44ad"));
                buttonDividerCompleted.setBackgroundColor(Color.parseColor("#8e44ad"));
                buttonDividerFailed.setBackgroundColor(Color.parseColor("#8e44ad"));
                break;
            case TASK:
                goalsAndTasksTodoType.setTextColor(Color.parseColor("#27ae60"));
                buttonDividerCompleted.setBackgroundColor(Color.parseColor("#27ae60"));
                buttonDividerFailed.setBackgroundColor(Color.parseColor("#27ae60"));
                break;
        }
    }

    private void displayCustomToastXPAndSilver() {
        gameMechanicsHelper.addSilver(goalsAndTasks.getSilver());
        Map<String, Boolean> improvementType = goalsAndTasks.getImprovementTypeMap();
        for (String type : improvementType.keySet()) {
            if (improvementType.get(type)) {
                gameMechanicsHelper.addImprovementType(type);
            }
        }
        Integer xpAmount = goalsAndTasks.getSilver()/10;
        if(goalsAndTasks.getCategory() == TodoType.GOAL) {
            long diff = getZeroTimeDate(new Date()).getTime() - getZeroTimeDate(goalsAndTasks.getCreationDate()).getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000)+1;
            xpAmount += (int) diffDays;
        }
        boolean levelUp = gameMechanicsHelper.addXp(xpAmount);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast_layout_xp_silver, null);
        ((TextView) view.findViewById(R.id.silver_reward_toast)).setText("+ " + String.valueOf(goalsAndTasks.getSilver()));
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
        if(goalsAndTasks.getStartDateString() != null) {
            todoStartDateLl.setVisibility(View.VISIBLE);
            todoStartDatetv.setText(goalsAndTasks.getStartDateString());
            if (getZeroTimeDate(new Date()).compareTo(getZeroTimeDate(goalsAndTasks.getStartDate())) < 0) {
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
                        goalsAndTasksAdapter.addItemToList(position, goalsAndTasks);
                        goalsAndTasksAdapter.notifyItemInserted(position);
                        undoList.add(goalsAndTasks);
                    }
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (undoList.size() == 0) {
                            goalsAndTasksAdapter.removeItem(goalsAndTasks, completed, deleted);
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
                        undoList.add(goalsAndTasks);
                    }
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (undoList.size() == 0) {
                            if(skippedDay) {
                                goalsAndTasksAdapter.updateDailySkippedDaily(position, completed, deleted);
                            }
                            else {
                                goalsAndTasksAdapter.updateDailyCompleted(position, completed, deleted);
                                if (completed) {
                                    displayCustomToastXPAndSilver();
                                }
                                else {
                                    displaySingleCustomToast(10, "Health");
                                }
                            }
                            disableDailyButtonValuesAndUpdateCounts(goalsAndTasks.getCompletedCount(), goalsAndTasks.getFailedCount());
                        }
                    }
                });
        snackbar.show();
    }
}
