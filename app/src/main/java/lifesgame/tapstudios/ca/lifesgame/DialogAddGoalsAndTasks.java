package lifesgame.tapstudios.ca.lifesgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

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
        silverSeekBar();
    }


    private void silverSeekBar () {
        SeekBar silverSeekBar = (SeekBar) findViewById(R.id.silver_seek_bar);

        silverSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progressValue;
                    final TextView silverTextView = (TextView) findViewById(R.id.silver_amount_text_view);
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        progressValue = i;
                        silverTextView.setText(String.valueOf(progressValue));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        return;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        silverTextView.setText(String.valueOf(progressValue));
                    }
                }
        );
    }

    private void addItem() {
        Button userAcceptTaskGoalBtn = (Button) findViewById(R.id.btn_user_accept_goal_task);
        userAcceptTaskGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final EditText userTaskGoalDescription = (EditText) findViewById(R.id.textDescription);
                final EditText userTaskGoalTitle = (EditText) findViewById(R.id.textTitle);
                final TextView userTaskGoalSilver = (TextView) findViewById(R.id.silver_amount_text_view);
                if(!userTaskGoalDescription.getText().toString().isEmpty() && !userTaskGoalTitle.getText().toString().isEmpty()){
                    BetterSpinner improvementCategory = (BetterSpinner) findViewById(R.id.spinner1);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("DATA_DESCRIPTION", userTaskGoalDescription.getText().toString());
                    intent.putExtra("DATA_CATEGORY", improvementCategory.getText().toString());
                    intent.putExtra("DATA_TITLE", userTaskGoalTitle.getText().toString());
                    intent.putExtra("DATA_SILVER", Long.valueOf(userTaskGoalSilver.getText().toString()));
                    startActivity(intent);
                }
            }
        });
    }
}


