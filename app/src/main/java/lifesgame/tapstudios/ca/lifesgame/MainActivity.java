package lifesgame.tapstudios.ca.lifesgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    TextView charHealth;
    TextView charXp;
    TextView charLevel;

    ArrayList<String> arrayList;

    Button addItemToListBtn;
    GoalsAndTasksAdapter goalsAndTasksAdapter;
    GoalsAndTasksHelper goalsAndTasksHelper;
    GameMechanicsHelper gameMechanicsHelper;
    DatabaseHelper databaseHelper;
    RoundCornerProgressBar healthBar;
    RoundCornerProgressBar xpBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String dataDescription = intent.getStringExtra("DATA_DESCRIPTION");
        String dataCategory = intent.getStringExtra("DATA_CATEGORY");

        listView = (ListView) findViewById(R.id.goals_tasks);

        charHealth = (TextView) findViewById(R.id.charHealth);
        charLevel = (TextView) findViewById(R.id.charLevel);
        charXp = (TextView) findViewById(R.id.charXp);
        xpBar = (RoundCornerProgressBar) findViewById(R.id.xpBar);
        healthBar = (RoundCornerProgressBar) findViewById(R.id.healthBar);

        addItemToListBtn = (Button) findViewById(R.id.add_item);
        goalsAndTasksHelper = new GoalsAndTasksHelper(addItemToListBtn, this);

        arrayList = new ArrayList<String>();
        databaseHelper = new DatabaseHelper(this);
        gameMechanicsHelper = new GameMechanicsHelper(charHealth, charXp, charLevel, databaseHelper, healthBar, xpBar);
        goalsAndTasksAdapter = new GoalsAndTasksAdapter(getApplicationContext(), R.layout.goal_and_task_row, gameMechanicsHelper, databaseHelper, arrayList, goalsAndTasksHelper);
        listView.setAdapter(goalsAndTasksAdapter);

        gameMechanicsHelper.setUpGameTextViews();

        arrayList.addAll(databaseHelper.loadAllGoalsAndTask());
        if(arrayList != null) {
            goalsAndTasksAdapter.notifyDataSetChanged();
        }
        if(dataDescription != null){
            addData(dataDescription, dataCategory);
            arrayList.add(dataDescription);
            goalsAndTasksAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),"Inserted " + dataCategory, Toast.LENGTH_LONG).show();
        }
    }

    public void addData(String newEntry, String category) {
        boolean insertData = databaseHelper.addData(newEntry, category);
    }
}
