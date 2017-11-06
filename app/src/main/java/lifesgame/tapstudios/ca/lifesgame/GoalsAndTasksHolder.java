package lifesgame.tapstudios.ca.lifesgame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private GoalsAndTasks goalsAndTasks;
    private Context context;
    private GoalsAndTasksHelper goalsAndTasksHelper;
    private GoalsAndTasksAdapter goalsAndTasksAdapter;
    private GameMechanicsHelper gameMechanicsHelper;


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

        itemView.setOnClickListener(this);
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
        initializeOnClickListeners(position);
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
                goalsAndTasksAdapter.removeItem(position, false);

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
                goalsAndTasksAdapter.removeItem(position, true);
                gameMechanicsHelper.addXp();
            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalsAndTasksAdapter.removeItem(position, false);
                gameMechanicsHelper.removeHealth();
            }
        });
    }
}
