package lifesgame.tapstudios.ca.lifesgame;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.BetterSpinner;

public class DialogAddGoalsAndTasks extends AppCompatActivity {

    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add_goals_and_tasks);

        final String[] improvementCategories = new String[]{"Task", "Goal", "Quest", "Epic"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, improvementCategories);
        BetterSpinner improvementCategory = (BetterSpinner) findViewById(R.id.spinner1);
        improvementCategory.setAdapter(spinnerAdapter);
        mainActivity = new MainActivity();
        addItem();
    }

    private void addItem() {
        Button userAcceptTaskGoalBtn = (Button) findViewById(R.id.btn_user_accept_goal_task);
        userAcceptTaskGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final EditText userTaskGoal = (EditText) findViewById(R.id.textDescription);
                if(!userTaskGoal.getText().toString().isEmpty()){
                    BetterSpinner improvementCategory = (BetterSpinner) findViewById(R.id.spinner1);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("DATA_DESCRIPTION", userTaskGoal.getText().toString());
                    intent.putExtra("DATA_CATEGORY", improvementCategory.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}


