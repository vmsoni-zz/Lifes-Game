package lifesgame.tapstudios.ca.lifesgame;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Vidit Soni on 6/3/2017.
 */
public class GoalsAndTasksHelper {
    public Button addItemToListBtn;
    public MainActivity mainActivity = new MainActivity();
    public GoalsAndTasksFormHelper goalsAndTasksFormHelper;

    public GoalsAndTasksHelper(Button addItemToListBtn, MainActivity mainActivity){
        this.addItemToListBtn = addItemToListBtn;
        this.mainActivity = mainActivity;
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
        Intent intent = new Intent(mainActivity, DialogAddGoalsAndTasks.class);
        mainActivity.startActivity(intent);
    }

    public void displayGoalsAndTasksEditMenu(final int position) {
        Intent intent = new Intent(mainActivity, DialogAddGoalsAndTasks.class);
        mainActivity.startActivity(intent);
    }
}
