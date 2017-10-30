package lifesgame.tapstudios.ca.lifesgame.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import lifesgame.tapstudios.ca.lifesgame.DialogAddGoalsAndTasks;
import lifesgame.tapstudios.ca.lifesgame.MainActivity;

/**
 * Created by Vidit Soni on 6/3/2017.
 */
public class GoalsAndTasksHelper {
    public Button addItemToListBtn;
    private final Context context;

    public GoalsAndTasksHelper(Button addItemToListBtn, Context context){
        this.addItemToListBtn = addItemToListBtn;
        this.context = context;
        goalsAndTasksRegisterButtons();
    }
    public void goalsAndTasksRegisterButtons(){
        addItemToListBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                displayGoalsAndTasksAddMenu();
            }
        });
    }

    public void displayGoalsAndTasksAddMenu(){
        Intent intent = new Intent(context, DialogAddGoalsAndTasks.class);
        context.startActivity(intent);
    }

    public void displayGoalsAndTasksEditMenu(final int position) {
        Intent intent = new Intent(context, DialogAddGoalsAndTasks.class);
        context.startActivity(intent);
    }
}
