package lifesgame.tapstudios.ca.lifesgame.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.AnalyticsApplication;
import lifesgame.tapstudios.ca.lifesgame.activity.DialogAddRewards;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.adapter.RewardsAdapter;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.model.Rewards;

public class RewardsFragment extends Fragment {
    public DatabaseHelper databaseHelper;
    RecyclerView rewardsListView;
    private View rewardsView;
    List<Rewards> rewardsList;
    FloatingActionButton addRewardToListBtn;
    RewardsAdapter rewardsAdapter;
    private Tracker tracker;

    public RewardsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getActivity());
        rewardsView = inflater.inflate(R.layout.activity_rewards, container, false);
        rewardsList = new ArrayList<Rewards>();
        rewardsAdapter = new RewardsAdapter(getActivity(), R.layout.rewards_row, databaseHelper, rewardsList);
        addRewardToListBtn = rewardsView.findViewById(R.id.add_reward_item);
        rewardsListView = (RecyclerView) rewardsView.findViewById(R.id.rewards);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardsListView.setLayoutManager(linearLayoutManager);
        rewardsListView.setHasFixedSize(true);
        rewardsListView.setAdapter(rewardsAdapter);
        rewardsList.addAll(databaseHelper.loadAllRewards());
        if (rewardsList != null) {
            rewardsAdapter.notifyDataSetChanged();
        }
        setupAddRewardButtonListener();
        hideFABOnScroll();

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Category")
                .setAction("Rewards")
                .build());

        tracker.setScreenName("Rewards");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        return rewardsView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setupAddRewardButtonListener() {
        addRewardToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogRewards();
            }
        });
    }

    public void displayDialogRewards() {
        Intent intent = new Intent(getActivity(), DialogAddRewards.class);
        Activity activity = getActivity();
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.no_anim);
    }

    public void hideFABOnScroll() {
        rewardsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    addRewardToListBtn.hide();
                } else {
                    addRewardToListBtn.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
