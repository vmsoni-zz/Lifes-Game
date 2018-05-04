package lifesgame.tapstudios.ca.lifesgame.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lifesgame.tapstudios.ca.lifesgame.GoalsAndTasksAdapter;
import lifesgame.tapstudios.ca.lifesgame.ProfilePicker;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.TodoType;
import lifesgame.tapstudios.ca.lifesgame.activity.IntroActivity;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GameMechanicsHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GoalsAndTasksHelper;
import lifesgame.tapstudios.ca.lifesgame.model.GoalsAndTasks;

/**
 * Created by Vidit Soni on 8/12/2017.
 */
public class HomeFragment extends Fragment {
    private RecyclerView listView;
    private TextView todoTypeTv;
    private TextView charHealth;
    private TextView charXp;
    private TextView charLevel;
    private TextView silverAmountTextView;
    private TextView noTodosTv;
    private TextView username;
    private List<GoalsAndTasks> arrayList;
    private ImageView profilePictureIV;
    private ImageButton todoFilterBtn;
    private FloatingActionButton addItemToListBtn;
    private GoalsAndTasksAdapter goalsAndTasksAdapter;
    private GoalsAndTasksHelper goalsAndTasksHelper;
    private GameMechanicsHelper gameMechanicsHelper;
    private DatabaseHelper databaseHelper;
    private RoundCornerProgressBar healthBar;
    private RoundCornerProgressBar xpBar;
    private View homeFragment;
    private Integer expiredGoalsAndTasksCount;
    private LayoutInflater layoutInflater;
    private int filterSelected;
    private static int PICK_IMAGE_REQUEST = 1;
    String displayName;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        homeFragment = inflater.inflate(R.layout.home_layout, container, false);
        expiredGoalsAndTasksCount = getArguments().getInt("EXPIRED_TODO_COUNT");
        setupFragmentElements();
        goalsAndTasksHelper = new GoalsAndTasksHelper(addItemToListBtn, getActivity());
        arrayList = new ArrayList<GoalsAndTasks>();
        databaseHelper = new DatabaseHelper(getActivity());
        gameMechanicsHelper = new GameMechanicsHelper(charHealth, charXp, charLevel, silverAmountTextView, databaseHelper, healthBar, xpBar, getActivity());
        goalsAndTasksAdapter = new GoalsAndTasksAdapter(getActivity(), R.layout.goal_and_task_row, gameMechanicsHelper, databaseHelper, arrayList, goalsAndTasksHelper, inflater);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        listView.setAdapter(goalsAndTasksAdapter);
        gameMechanicsHelper.setUpGameTextViews();
        if (expiredGoalsAndTasksCount > 0) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Mistakes Happen In Life")
                    .setContentText("You failed " + String.valueOf(expiredGoalsAndTasksCount) + " TODOs")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            for (int i = 0; i < expiredGoalsAndTasksCount; i++) {
                                gameMechanicsHelper.removeHealth();
                            }
                            expiredGoalsAndTasksCount = 0;
                            sDialog.dismissWithAnimation();
                            getArguments().remove("EXPIRED_TODO_COUNT");
                        }
                    })
                    .show();
        }
        //Setup listview with all goals and tasks
        arrayList.addAll(databaseHelper.loadAllCompletedGoalsAndTask());
        if (arrayList != null) {
            goalsAndTasksAdapter.notifyDataSetChanged();
        }
        hideFABOnScroll();
        setupProfilePicture();
        setupListeners();
        checkAndDisplayNoTodosAddedTv();
        filterSelected = 0;
        return homeFragment;
    }

    public void setupFragmentElements() {
        todoTypeTv = (TextView) homeFragment.findViewById(R.id.todo_type_tv);
        listView = (RecyclerView) homeFragment.findViewById(R.id.goals_tasks);
        charHealth = (TextView) homeFragment.findViewById(R.id.charHealth);
        charLevel = (TextView) homeFragment.findViewById(R.id.charLevel);
        charXp = (TextView) homeFragment.findViewById(R.id.charXp);
        username = (TextView) homeFragment.findViewById(R.id.username);
        //username.setText(displayName);
        silverAmountTextView = (TextView) homeFragment.findViewById(R.id.silver_amount_text_view);
        xpBar = (RoundCornerProgressBar) homeFragment.findViewById(R.id.xpBar);
        healthBar = (RoundCornerProgressBar) homeFragment.findViewById(R.id.healthBar);
        addItemToListBtn = (FloatingActionButton) homeFragment.findViewById(R.id.add_item);
        profilePictureIV = (ImageView) homeFragment.findViewById(R.id.profile_picture);
        todoFilterBtn = (ImageButton) homeFragment.findViewById(R.id.todo_filter_button);
        noTodosTv = (TextView) homeFragment.findViewById(R.id.no_todos_tv);
    }

    public void hideFABOnScroll() {
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    addItemToListBtn.hide();
                } else {
                    addItemToListBtn.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void checkAndDisplayNoTodosAddedTv() {
        if (arrayList.size() == 0) {
            noTodosTv.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            noTodosTv.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    private void setupProfilePicture() {
        final Bitmap profilePicture = databaseHelper.getProfilePicture();
        if (profilePicture != null) {
            displayPPSeperateThread(profilePicture);
        } else {
            startTutorial();
        }
    }

    private void displayPPSeperateThread(final Bitmap profilePicture) {
        profilePictureIV.post(new Runnable() {
            @Override
            public void run() {
                // set the downloaded image here
                profilePictureIV.setImageBitmap(profilePicture);
            }
        });
    }

    private void getImageFromUser() {
        Intent intent = new Intent(getActivity(), ProfilePicker.class);
        startActivity(intent);
    }

    private void startTutorial() {
        Intent intent = new Intent(getActivity(), IntroActivity.class); // Call the AppIntro java class
        startActivity(intent);
    }

    private void setupListeners() {
        profilePictureIV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getImageFromUser();
            }
        });

        todoFilterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                displayFilterDialog();
            }
        });
    }

    private void displayFilterDialog() {
        String title = "Todo Filter";
        String[] filterChoices = {"All", "Task", "Daily", "Goal"};

        new MaterialDialog.Builder(getActivity())
                .title(title)
                .titleColor(getResources().getColor(R.color.colorPrimaryDark))
                .items(filterChoices)
                .itemsCallbackSingleChoice(
                        filterSelected,
                        (dialog, view, which, text) -> {
                            applyFilter(which);
                            return true;
                        })
                .backgroundColor(Color.WHITE)
                .positiveText("Choose")
                .negativeText("Cancel")
                .choiceWidgetColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)))
                .contentColor(getResources().getColor(R.color.colorPrimaryDark))
                .show();
    }

    private void applyFilter(int filter) {
        filterSelected = filter;
        switch (filter) {
            case 0:
                todoTypeTv.setText("All");
                arrayList.clear();
                arrayList.addAll(databaseHelper.loadAllCompletedGoalsAndTask());
                goalsAndTasksAdapter = new GoalsAndTasksAdapter(getActivity(), R.layout.goal_and_task_row, gameMechanicsHelper, databaseHelper, arrayList, goalsAndTasksHelper, layoutInflater);
                listView.setAdapter(goalsAndTasksAdapter);
                break;
            case 1:
                filterTodoList(TodoType.TASK);
                break;
            case 2:
                filterTodoList(TodoType.DAILY);
                break;
            case 3:
                filterTodoList(TodoType.GOAL);
                break;
            default:
                break;
        }
    }

    private void filterTodoList(TodoType todoType) {
        todoTypeTv.setText(todoType.getTodoTypeString());
        List<GoalsAndTasks> allTodos = databaseHelper.loadAllCompletedGoalsAndTask();
        arrayList.clear();
        for (GoalsAndTasks goalsAndTasks : allTodos) {
            if (goalsAndTasks.getCategory() == todoType) {
                arrayList.add(goalsAndTasks);
            }
        }
        goalsAndTasksAdapter = new GoalsAndTasksAdapter(getActivity(), R.layout.goal_and_task_row, gameMechanicsHelper, databaseHelper, arrayList, goalsAndTasksHelper, layoutInflater);
        listView.setAdapter(goalsAndTasksAdapter);
    }
}
