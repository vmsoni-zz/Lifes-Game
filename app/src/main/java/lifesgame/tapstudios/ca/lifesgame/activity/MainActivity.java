package lifesgame.tapstudios.ca.lifesgame.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lifesgame.tapstudios.ca.lifesgame.AnalyticsApplication;
import lifesgame.tapstudios.ca.lifesgame.NonSwipeableViewPager;
import lifesgame.tapstudios.ca.lifesgame.adapter.PagerAdapter;
import lifesgame.tapstudios.ca.lifesgame.fragment.PomodoroTimerFragment;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.fragment.HomeFragment;
import lifesgame.tapstudios.ca.lifesgame.fragment.StatisticsFragment;
import lifesgame.tapstudios.ca.lifesgame.fragment.StoreFragment;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_fragment)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_main)
    DrawerLayout mDrawerLayout;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Map<Integer, String> mFragmentTags;
    private PagerAdapter mAdapterViewPager;
    private DatabaseHelper databaseHelper;
    private Tracker tracker;
    Integer expiredGoalsAndTasksCount;
    private GoogleSignInAccount googleSignInAccount;
    private String displayName;
    private Bundle homeFragmentBundle;
    private static final int REQUEST_CODE_ENABLE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Category")
                .setAction("Homepage")
                .build());

        tracker.setScreenName("MainActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

/*        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            displayName = user.getDisplayName();
        }*/

        databaseHelper = new DatabaseHelper(this);
        expiredGoalsAndTasksCount = databaseHelper.resetExpiredGoalsAndTasks();
        homeFragmentBundle = new Bundle();
        homeFragmentBundle.putInt("EXPIRED_TODO_COUNT", expiredGoalsAndTasksCount);

        if (getIntent() != null) {
            if (getIntent().getBooleanExtra("PASSCODE_SET", false)) {
                Intent intent = new Intent(this, CustomPinActivity.class);
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
                startActivityForResult(intent, REQUEST_CODE_ENABLE);
            }
            getIntent().removeExtra("PASSCODE_SET");
        }
        fragmentManager = getSupportFragmentManager();
        setupSwipeFragments();
        setupSideNavView();
        initChannels(this);
        scrollToSpecificFragment();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setupSwipeFragments() {
        viewPager.setOffscreenPageLimit(4);
        mAdapterViewPager = new PagerAdapter(fragmentManager);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(homeFragmentBundle);
        mAdapterViewPager.addFragment(homeFragment, "Home");
        mAdapterViewPager.addFragment(new StatisticsFragment(), "Statistics");
        mAdapterViewPager.addFragment(new PomodoroTimerFragment(), "Pomodoro");
        mAdapterViewPager.addFragment(new StoreFragment(), "Store");
        viewPager.setAdapter(mAdapterViewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
                    Fragment fragment = mAdapterViewPager.getItem(position);
                    if (fragment != null) {
                        fragment.onResume();
                    }
                }
            }

            public void onPageSelected(int position) {
            }
        });
    }

    public void setupSideNavView() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.statistics:
                        viewPager.setCurrentItem(1, true);
                        break;
                    case R.id.pomodoro:
                        viewPager.setCurrentItem(2, true);
                        break;
                    case R.id.store:
                        viewPager.setCurrentItem(3, true);
                        break;
                }
                mDrawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
                else {
                    mDrawerLayout.closeDrawer(Gravity.START);
                }
            }
        });
    }

    public void scrollToSpecificFragment() {
        if (getIntent() != null) {
            Integer fragmentNumber = getIntent().getIntExtra("FRAGMENT_NUMBER", -1);
            if (fragmentNumber != -1) {
                if (fragmentNumber == 3) {
                    navigationView.setCheckedItem(R.id.store);
                } else if (fragmentNumber == 2) {
                    navigationView.setCheckedItem(R.id.pomodoro);
                } else if (fragmentNumber == 1) {
                    navigationView.setCheckedItem(R.id.statistics);
                } else {
                    navigationView.setCheckedItem(R.id.home);
                }
                viewPager.setCurrentItem(fragmentNumber, true);
            }
        }
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default_channel", "Life's Game Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Default notification channel for Life's Game");
        channel.enableVibration(true);
        notificationManager.createNotificationChannel(channel);
    }
}
