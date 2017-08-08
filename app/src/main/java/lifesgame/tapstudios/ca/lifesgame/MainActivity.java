package lifesgame.tapstudios.ca.lifesgame;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.ArrayList;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GameMechanicsHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GoalsAndTasksHelper;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    TextView charHealth;
    TextView charXp;
    TextView charLevel;
    TextView silverAmountTextView;

    ArrayList<GoalsAndTasks> arrayList;

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
        String dataTitle = intent.getStringExtra("DATA_TITLE");
        Long dataSilver = intent.getLongExtra("DATA_SILVER", 0);

        listView = (ListView) findViewById(R.id.goals_tasks);

        charHealth = (TextView) findViewById(R.id.charHealth);
        charLevel = (TextView) findViewById(R.id.charLevel);
        charXp = (TextView) findViewById(R.id.charXp);
        silverAmountTextView = (TextView) findViewById(R.id.silver_amount_text_view);
        xpBar = (RoundCornerProgressBar) findViewById(R.id.xpBar);
        healthBar = (RoundCornerProgressBar) findViewById(R.id.healthBar);

        addItemToListBtn = (Button) findViewById(R.id.add_item);
        goalsAndTasksHelper = new GoalsAndTasksHelper(addItemToListBtn, this);

        arrayList = new ArrayList<GoalsAndTasks>();
        databaseHelper = new DatabaseHelper(this);
        gameMechanicsHelper = new GameMechanicsHelper(charHealth, charXp, charLevel, silverAmountTextView, databaseHelper, healthBar, xpBar);
        goalsAndTasksAdapter = new GoalsAndTasksAdapter(getApplicationContext(), R.layout.goal_and_task_row, gameMechanicsHelper, databaseHelper, arrayList, goalsAndTasksHelper);
        listView.setAdapter(goalsAndTasksAdapter);

        gameMechanicsHelper.setUpGameTextViews();

        arrayList.addAll(databaseHelper.loadAllGoalsAndTask());
        if(arrayList != null) {
            goalsAndTasksAdapter.notifyDataSetChanged();
        }
        if(dataDescription != null || dataTitle != null){
            Long goalTaskId = addData(dataDescription, dataCategory, dataTitle, dataSilver);
            GoalsAndTasks goalsAndTasks = new GoalsAndTasks(dataTitle, dataDescription, dataCategory, goalTaskId, dataSilver);
            arrayList.add(goalsAndTasks);
            goalsAndTasksAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),"Inserted " + dataCategory, Toast.LENGTH_LONG).show();
        }

        setupBottomNavigation();
    }

    public void setupBottomNavigation() {
        BottomNavigationView mBottomNavigation;
        mBottomNavigation = (BottomNavigationView) findViewById(R.id.NavBot);
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.statistics:
                        Intent statisticsActivity= new Intent(getApplicationContext(), StatisticsActivity.class);
                        startActivity(statisticsActivity);
                        break;
                }
                return true;
            }
        });
    }

    public Long addData(String description, String category, String title, Long silver) {
        Long goalTaskId = databaseHelper.addData(description, category, title, silver);
        return goalTaskId;
    }
}
