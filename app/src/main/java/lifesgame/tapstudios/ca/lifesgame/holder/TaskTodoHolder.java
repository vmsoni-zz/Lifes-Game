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
import lifesgame.tapstudios.ca.lifesgame.model.TodoType;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.generic.Todo;

/**
 * Created by viditsoni on 2017-11-04.
 */

public class TaskTodoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.editButton)
    ImageButton editBtn;
    @BindView(R.id.deleteButton)
    ImageButton deleteBtn;
/*    @BindView(R.id.completedButton)
    ImageButton completedBtn;*/
    @BindView(R.id.failedButton)
    ImageButton failedBtn;
    @BindView(R.id.todo_title)
    TextView todoTitle;
    @BindView(R.id.todo_description)
    TextView todoDescription;
/*    @BindView(R.id.todo_type)
    TextView todoType;*/
/*    @BindView(R.id.silver_reward)
    TextView silverReward;
    @BindView(R.id.button_divider_completed)
    View buttonDividerCompleted;*/
/*    @BindView(R.id.button_divider_failed)
    View buttonDividerFailed;*/

    private Todo todo;
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

    public TaskTodoHolder(Context context,
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
        if (this.todo != null) {
            Toast.makeText(this.context, "Clicked on " + this.todo.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    public void bindGoalsAndTasks(Todo todo, final int position) {
        this.todo = todo;
        todoTitle.setText(this.todo.getTitle());
        if (this.todo.getDescription() == null || this.todo.getDescription().isEmpty()) {
            todoDescription.setVisibility(View.GONE);
        } else {
            todoDescription.setVisibility(View.VISIBLE);
            todoDescription.setText(this.todo.getDescription());
        }
        //todoType.setText(TodoType.TASK.getTodoTypeString());
        //silverReward.setText(String.valueOf(this.todo.getSilver()));
        //setupTodoTypeColor(this.todo);

        bindTask();

        //TODO: Setup this function
        //initializeImprovementTypeImages(todo, todoTag);
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

    private void initializeImprovementTypeImages(TaskTodo todo, TodoTag todoTag) {
        if (todoTag.isChores()) {
            improvementTypeImageViews.get("chores")
                    .setImageResource(itemView.getResources()
                            .getIdentifier("chores", "drawable", "lifesgame.tapstudios.ca.lifesgame"));
        }
        if (todoTag.isCreativity()) {
            improvementTypeImageViews.get("creativity")
                    .setImageResource(itemView.getResources()
                            .getIdentifier("creativity", "drawable", "lifesgame.tapstudios.ca.lifesgame"));
        }
        if (todoTag.isExercise()) {
            improvementTypeImageViews.get("exercise")
                    .setImageResource(itemView.getResources()
                            .getIdentifier("exercise", "drawable", "lifesgame.tapstudios.ca.lifesgame"));
        }
        if (todoTag.isHealthWellness()) {
            improvementTypeImageViews.get("health_wellness")
                    .setImageResource(itemView.getResources()
                            .getIdentifier("health_wellness", "drawable", "lifesgame.tapstudios.ca.lifesgame"));
        }
        if (todoTag.isHome()) {
            improvementTypeImageViews.get("home")
                    .setImageResource(itemView.getResources()
                            .getIdentifier("home", "drawable", "lifesgame.tapstudios.ca.lifesgame"));
        }
        if (todoTag.isSchool()) {
            improvementTypeImageViews.get("school")
                    .setImageResource(itemView.getResources()
                            .getIdentifier("school", "drawable", "lifesgame.tapstudios.ca.lifesgame"));
        }
        if (todoTag.isTeams()) {
            improvementTypeImageViews.get("teams")
                    .setImageResource(itemView.getResources()
                            .getIdentifier("teams", "drawable", "lifesgame.tapstudios.ca.lifesgame"));
        }
        if (todoTag.isWork()) {
            improvementTypeImageViews.get("work")
                    .setImageResource(itemView.getResources()
                            .getIdentifier("work", "drawable", "lifesgame.tapstudios.ca.lifesgame"));
        }
        if (todoTag.isOther()) {
            improvementTypeImageViews.get("other")
                    .setImageResource(itemView.getResources()
                            .getIdentifier("other", "drawable", "lifesgame.tapstudios.ca.lifesgame"));
        }

/*        Map<String, Boolean> improvementTypes = todoTag.getImprovementTypeMap();
        for (s.Entry<String, Boolean> improvement : improvementTypes.entrySet()) {


            if (true) {
                improvementTypeImageViews.get(improvement.getKey())
                        .setImageResource(itemView.getResources()
                                .getIdentifier(improvement.getKey(), "drawable", "lifesgame.tapstudios.ca.lifesgame"));
            }
            else {
                improvementTypeImageViews.get(improvement.getKey())
                        .setImageResource(itemView.getResources()
                                .getIdentifier("not_selected_" + improvement.getKey(), "drawable", "lifesgame.tapstudios.ca.lifesgame"));
            }
        }*/
    }

    private void initializeOnClickListeners(final int position) {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoHelper.displayGoalsAndTasksEditMenu(todo.getId());
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Fix delete permanent functionality
                //todoAdapter.deleteItemPermanent(position);
                todoAdapter.removeItemFromList(position);
                todoAdapter.notifyItemRemoved(position);
                todoAdapter.notifyItemRangeChanged(position, todoAdapter.getItemCount());
                showSnackBarDeleteWithUndo(position, true, true);
            }
        });

/*
        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoAdapter.removeItemFromList(position);
                todoAdapter.notifyItemRemoved(position);
                todoAdapter.notifyItemRangeChanged(position, todoAdapter.getItemCount());
                showSnackBarDeleteWithUndo(position, true, true);
            }
        });
*/

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoAdapter.removeItemFromList(position);
                todoAdapter.notifyItemRemoved(position);
                todoAdapter.notifyItemRangeChanged(position, todoAdapter.getItemCount());
                showSnackBarDeleteWithUndo(position, false, true);
            }
        });
    }

    private void disableUpdateTodoOptions() {
        //completedBtn.setEnabled(false);
        //completedBtn.setImageResource(R.drawable.disabled_checkmark);
        failedBtn.setEnabled(false);
        failedBtn.setImageResource(R.drawable.disabled_xmark);
    }

    private void enableUpdateTodoOptions() {
        //completedBtn.setEnabled(true);
        //completedBtn.setImageResource(R.drawable.check_mark);
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


/*    private void setupTodoTypeColor(Todo todo) {
        todoType.setTextColor(Color.parseColor("#27ae60"));
        buttonDividerCompleted.setBackgroundColor(Color.parseColor("#27ae60"));
        buttonDividerFailed.setBackgroundColor(Color.parseColor("#27ae60"));
    }*/

    private void displayCustomToastXPAndSilver() {
        //gameMechanicsHelper.addSilver(todo.getSilver());
/*        Map<String, Boolean> improvementType = todo.getImprovementTypeMap();
        for (String type : improvementType.keySet()) {
            if (improvementType.get(type)) {
                gameMechanicsHelper.addImprovementType(type);
            }
        }*/

        long xpAmount = todo.getSilver() / 10;
        boolean levelUp = true;
        //TODO: Fix the levelUp functionality
        //boolean levelUp = gameMechanicsHelper.addXp(xpAmount);
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

/*    private void setupStartDate() {
        if (todo.getStartDateString() != null) {
            todoStartDateLl.setVisibility(View.VISIBLE);
            todoStartDatetv.setText(todo.getStartDateString());
            if (getZeroTimeDate(new Date()).compareTo(getZeroTimeDate(todo.getStartDate())) < 0) {
                disableUpdateTodoOptions();
            }
        }
    }*/

    private void showSnackBarDeleteWithUndo(int position, Boolean completed, Boolean deleted) {
        List<Todo> undoList = new ArrayList<>();
        Snackbar snackbar = Snackbar
                .make(((Activity) context).findViewById(R.id.activity_main), "Updated Todo", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todoAdapter.addItemToList(position, todo);
                        //todoAdapter.notifyItemInserted(position);
                        undoList.add(todo);
                    }
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (undoList.size() == 0) {
                            todoAdapter.removeItem(todo, completed, deleted);
                            if (completed) {
                                displayCustomToastXPAndSilver();
                            } else {
                                displaySingleCustomToast(10, "Health");
                            }
                        }
                    }
                });
        snackbar.show();
    }


    private void bindTask() {
        //TODO: Fix time and start
        //dailyCountLl.setVisibility(View.GONE);
        //todoRemainingTimeLL.setVisibility(View.GONE);
        //dailySkipDayLl.setVisibility(View.GONE);
        //todoStartDateLl.setVisibility(View.VISIBLE);
        //setupStartDate();
    }
}
