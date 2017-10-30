package lifesgame.tapstudios.ca.lifesgame;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GameMechanicsHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GoalsAndTasksHelper;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager fragmentManager;

    ListView listView;

    TextView charHealth;
    TextView charXp;
    TextView charLevel;
    TextView silverAmountTextView;

    ArrayList<GoalsAndTasks> arrayList;

    Button addItemToListBtn;
    GoalsAndTasksAdapter goalsAndTasksAdapter;
    DatabaseHelper databaseHelper;
    RoundCornerProgressBar healthBar;
    RoundCornerProgressBar xpBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String dataDescription = intent.getStringExtra("DATA_DESCRIPTION");
        String dataCategory = intent.getStringExtra("DATA_CATEGORY");
        String dataTitle = intent.getStringExtra("DATA_TITLE");
        Long dataSilver = intent.getLongExtra("DATA_SILVER", 0);
        Date dataEndDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            String endDate = intent.getStringExtra("DATA_ENDDATE");
            if (endDate != null) {
                dataEndDate = format.parse(intent.getStringExtra("DATA_ENDDATE"));
            }
            else {
                dataEndDate = null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            dataEndDate = null;
        }

        Map<String, Boolean> improvementType = (HashMap<String, Boolean>) intent.getSerializableExtra("DATA_IMPROVEMENT_TYPE");

        if(dataDescription != null || dataTitle != null){
            Long goalTaskId = addData(dataDescription, dataCategory, dataTitle, dataSilver, improvementType, dataEndDate);
            Toast.makeText(getApplicationContext(),"Inserted " + dataCategory, Toast.LENGTH_LONG).show();
        }

        setupBottomNavigation();

        fragmentManager = getSupportFragmentManager();
        fragment = new HomeFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_fragment, fragment).commit();

    }

    public void setupBottomNavigation() {
        fragmentManager = getSupportFragmentManager();
        BottomNavigationView mBottomNavigation;
        mBottomNavigation = (BottomNavigationView) findViewById(R.id.NavBot);
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.statistics:
                        fragment = new StatisticsFragment();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_fragment, fragment).commit();
                return true;
            }
        });
    }

    public Long addData(String description, String category, String title, Long silver,  Map<String, Boolean> improvementType, Date dataEndDate) {
        Long goalTaskId = databaseHelper.addData(description, category, title, silver, improvementType, dataEndDate);
        return goalTaskId;
    }
}
