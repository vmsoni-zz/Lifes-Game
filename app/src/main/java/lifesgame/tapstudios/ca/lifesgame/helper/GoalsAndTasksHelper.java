package lifesgame.tapstudios.ca.lifesgame.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import lifesgame.tapstudios.ca.lifesgame.activity.DialogAddGoalsAndTasks;
import lifesgame.tapstudios.ca.lifesgame.R;

/**
 * Created by Vidit Soni on 6/3/2017.
 */
public class GoalsAndTasksHelper {
    public FloatingActionButton addItemToListBtn;
    private final Context context;

    public GoalsAndTasksHelper(FloatingActionButton addItemToListBtn, Context context) {
        this.addItemToListBtn = addItemToListBtn;
        this.context = context;
        goalsAndTasksRegisterButtons();
    }

    public void goalsAndTasksRegisterButtons() {
        addItemToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayGoalsAndTasksAddMenu();
            }
        });
    }

    public void displayGoalsAndTasksAddMenu() {
        Intent intent = new Intent(context, DialogAddGoalsAndTasks.class);
        Activity activity = (Activity) context;
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.no_anim);

    }

    public void displayGoalsAndTasksEditMenu(Long id) {
        Intent intent = new Intent(context, DialogAddGoalsAndTasks.class);
        intent.putExtra("ID", id);
        context.startActivity(intent);
    }
}
