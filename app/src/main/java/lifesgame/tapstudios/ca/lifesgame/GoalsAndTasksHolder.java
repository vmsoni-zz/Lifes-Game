package lifesgame.tapstudios.ca.lifesgame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import lifesgame.tapstudios.ca.lifesgame.helper.GameMechanicsHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GoalsAndTasksHelper;

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
        goalsAndTasksRemainingTimeLL = (LinearLayout) itemView.findViewById(R.id.goal_task_remaining_time_ll);
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
        if (!goalsAndTasks.getCategory().equals("Goal")) {
            goalsAndTasksRemainingTimeLL.setVisibility(View.GONE);
        }
        else {
            goalsAndTasksRemainingTime.setText(goalsAndTasks.getDeadlineDateString());
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
                goalsAndTasksHelper.displayGoalsAndTasksEditMenu(position);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalsAndTasksAdapter.removeItem(position, false, true);

            }
        });

        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMechanicsHelper.addSilver(goalsAndTasks.getSilver());
                Map<String, Boolean> improvementType = goalsAndTasks.getImprovementTypeMap();
                for (String type : improvementType.keySet()) {
                    if (improvementType.get(type)) {
                        gameMechanicsHelper.addImprovementType(type);
                    }
                }
                goalsAndTasksAdapter.removeItem(position, true, true);
                gameMechanicsHelper.addXp();
            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalsAndTasksAdapter.removeItem(position, false, true);
                gameMechanicsHelper.removeHealth();
            }
        });
    }
}
