package lifesgame.tapstudios.ca.lifesgame;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    private final TextView goalsAndTasksTodoType;
    private final TextView silverReward;
    private final View buttonDividerCompleted;
    private final View buttonDividerFailed;
    private final LinearLayout goalsAndTasksRemainingTimeLL;
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
        goalsAndTasksTodoType = (TextView) itemView.findViewById(R.id.todo_type);
        silverReward = (TextView) itemView.findViewById(R.id.silver_reward);
        goalsAndTasksRemainingTimeLL = (LinearLayout) itemView.findViewById(R.id.goal_task_remaining_time_ll);
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
        goalsAndTasksDescription.setText(goalsAndTasks.getDescription());
        goalsAndTasksTodoType.setText(goalsAndTasks.getCategory().getTodoTypeString());
        silverReward.setText(String.valueOf(goalsAndTasks.getSilver()));
        setupTodoTypeColor(goalsAndTasks);
        if (goalsAndTasks.getCategory() != TodoType.GOAL) {
            goalsAndTasksRemainingTimeLL.setVisibility(View.GONE);
        } else {
            goalsAndTasksRemainingTime.setText(goalsAndTasks.getDeadlineDateString());
        }
        if (goalsAndTasks.getCategory() == TodoType.DAILY) {
            Date todayDate = new Date();

            if (getZeroTimeDate(todayDate).compareTo(getZeroTimeDate(goalsAndTasks.getCompletionDate())) > 0) {
                goalsAndTasksAdapter.updateDailyNotCompleted(position);
                goalsAndTasks.setCompleted(false);
            }
            if (goalsAndTasks.getCompleted()) {
                disableDailyButtonValues();
            }
        }
        initializeImprovementTypeImages(goalsAndTasks);
        initializeOnClickListeners(position);
    }

    public void initializeImprovementTypeImageViews() {
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_HEALTH_EXERCISE, (ImageView) itemView.findViewById(R.id.health_exercise));
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_WORK, (ImageView) itemView.findViewById(R.id.work_img));
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_SCHOOL, (ImageView) itemView.findViewById(R.id.school_img));
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_FAMILY_FRIENDS, (ImageView) itemView.findViewById(R.id.family_friends));
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_LEARNING, (ImageView) itemView.findViewById(R.id.learning_img));
        improvementTypeImageViews.put(TABLE_TASKS_GOALS_OTHER, (ImageView) itemView.findViewById(R.id.other_img));
    }

    public void initializeImprovementTypeImages(GoalsAndTasks goalsAndTasks) {
        Map<String, Boolean> improvementTypes = goalsAndTasks.getImprovementTypeMap();
        for (Map.Entry<String, Boolean> improvement : improvementTypes.entrySet()) {
            if (improvement.getValue()) {
                improvementTypeImageViews.get(improvement.getKey())
                        .setImageResource(itemView.getResources()
                                .getIdentifier(improvement.getKey(), "drawable", "lifesgame.tapstudios.ca.lifesgame"));
            }
        }
    }

    public void initializeOnClickListeners(final int position) {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalsAndTasksHelper.displayGoalsAndTasksEditMenu(goalsAndTasks.getId());
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goalsAndTasks.getCategory() == TodoType.DAILY) {
                    goalsAndTasksAdapter.removeItem(position, true, true);
                } else {
                    goalsAndTasksAdapter.deleteItemPermanent(position);
                }
            }
        });

        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMechanicsHelper.addSilver(goalsAndTasks.getSilver());
                Integer xpAmount = goalsAndTasks.getSilver()/10;
                if(goalsAndTasks.getCategory() == TodoType.GOAL) {
                    long diff = getZeroTimeDate(new Date()).getTime() - getZeroTimeDate(goalsAndTasks.getCreationDate()).getTime();
                    long diffDays = diff / (24 * 60 * 60 * 1000)+1;
                    xpAmount += (int) diffDays;
                }
                Boolean levelUp = gameMechanicsHelper.addXp(xpAmount);
                Map<String, Boolean> improvementType = goalsAndTasks.getImprovementTypeMap();
                for (String type : improvementType.keySet()) {
                    if (improvementType.get(type)) {
                        gameMechanicsHelper.addImprovementType(type);
                    }
                }
                if (goalsAndTasks.getCategory() == TodoType.DAILY) {
                    goalsAndTasksAdapter.updateDailyCompleted(position, true, false);
                    disableDailyButtonValues();
                } else {
                    goalsAndTasksAdapter.removeItem(position, true, true);
                }
                displayCustomToastXPAndSilver(levelUp, xpAmount);
            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goalsAndTasks.getCategory() == TodoType.DAILY) {
                    goalsAndTasksAdapter.updateDailyCompleted(position, false, false);
                    disableDailyButtonValues();
                }
                else {
                    goalsAndTasksAdapter.removeItem(position, false, true);
                }
                gameMechanicsHelper.removeHealth();
                displaySingleCustomToast(10, "Health");
            }
        });
    }

    private void disableDailyButtonValues() {
        completedBtn.setEnabled(false);
        completedBtn.setImageResource(R.drawable.disabled_checkmark);
        failedBtn.setEnabled(false);
        failedBtn.setImageResource(R.drawable.disabled_xmark);
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

    public void setupTodoTypeColor(GoalsAndTasks goalsAndTasks) {
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

    public void displayCustomToastXPAndSilver(Boolean leveUp, Integer xpAmount) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast_layout_xp_silver, null);
        ((TextView) view.findViewById(R.id.silver_reward_toast)).setText("+ " + String.valueOf(goalsAndTasks.getSilver()));
        ((TextView) view.findViewById(R.id.xp_reward_toast)).setText("+ " + xpAmount);
        if (leveUp) {
            ((LinearLayout) view.findViewById(R.id.levelup_ll)).setVisibility(View.VISIBLE);
        }
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.show();
    }

    public void displaySingleCustomToast(Integer value, String type) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast_health, null);
        if (type.equals("Health")) {
            ((ImageView) view.findViewById(R.id.toast_icon)).setImageResource(R.drawable.health);
            ((TextView) view.findViewById(R.id.value_changed)).setText("- " + value);
        }
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.show();
    }
}
