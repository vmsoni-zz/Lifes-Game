package lifesgame.tapstudios.ca.lifesgame.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lifesgame.tapstudios.ca.lifesgame.GoalsAndTasksAdapter;
import lifesgame.tapstudios.ca.lifesgame.ProfilePicker;
import lifesgame.tapstudios.ca.lifesgame.R;
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
    private TextView charHealth;
    private TextView charXp;
    private TextView charLevel;
    private TextView silverAmountTextView;
    private TextView username;
    private List<GoalsAndTasks> arrayList;
    private ImageView profilePictureIV;
    private FloatingActionButton addItemToListBtn;
    private GoalsAndTasksAdapter goalsAndTasksAdapter;
    private GoalsAndTasksHelper goalsAndTasksHelper;
    private GameMechanicsHelper gameMechanicsHelper;
    private DatabaseHelper databaseHelper;
    private RoundCornerProgressBar healthBar;
    private RoundCornerProgressBar xpBar;
    private View homeFragment;
    private Integer expiredGoalsAndTasksCount;
    private static int PICK_IMAGE_REQUEST = 1;
    String displayName;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                    .setContentText("You failed to complete " + String.valueOf(expiredGoalsAndTasksCount) + " TODOs")
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
        return homeFragment;
    }

    public void setupFragmentElements() {
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
    }
}
