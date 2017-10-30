package lifesgame.tapstudios.ca.lifesgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.ArrayList;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GameMechanicsHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GoalsAndTasksHelper;

/**
 * Created by Vidit Soni on 8/12/2017.
 */
public class HomeFragment extends Fragment {
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
    public View homeFragment;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeFragment = inflater.inflate(R.layout.home_layout, container, false);

        listView = (ListView) homeFragment.findViewById(R.id.goals_tasks);

        charHealth = (TextView) homeFragment.findViewById(R.id.charHealth);
        charLevel = (TextView) homeFragment.findViewById(R.id.charLevel);
        charXp = (TextView) homeFragment.findViewById(R.id.charXp);
        silverAmountTextView = (TextView) homeFragment.findViewById(R.id.silver_amount_text_view);
        xpBar = (RoundCornerProgressBar) homeFragment.findViewById(R.id.xpBar);
        healthBar = (RoundCornerProgressBar) homeFragment.findViewById(R.id.healthBar);

        addItemToListBtn = (Button) homeFragment.findViewById(R.id.add_item);
        goalsAndTasksHelper = new GoalsAndTasksHelper(addItemToListBtn, getActivity());

        arrayList = new ArrayList<GoalsAndTasks>();
        databaseHelper = new DatabaseHelper(getContext());
        gameMechanicsHelper = new GameMechanicsHelper(charHealth, charXp, charLevel, silverAmountTextView, databaseHelper, healthBar, xpBar);
        goalsAndTasksAdapter = new GoalsAndTasksAdapter(getContext(), R.layout.goal_and_task_row, gameMechanicsHelper, databaseHelper, arrayList, goalsAndTasksHelper);
        listView.setAdapter(goalsAndTasksAdapter);

        gameMechanicsHelper.setUpGameTextViews();

        arrayList.addAll(databaseHelper.loadAllGoalsAndTask());
        if(arrayList != null) {
            goalsAndTasksAdapter.notifyDataSetChanged();
        }
        return homeFragment;
    }
}
