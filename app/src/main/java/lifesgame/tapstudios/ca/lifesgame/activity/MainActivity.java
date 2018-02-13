package lifesgame.tapstudios.ca.lifesgame.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import lifesgame.tapstudios.ca.lifesgame.AnalyticsApplication;
import lifesgame.tapstudios.ca.lifesgame.NonSwipeableViewPager;
import lifesgame.tapstudios.ca.lifesgame.PagerAdapter;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.fragment.HomeFragment;
import lifesgame.tapstudios.ca.lifesgame.fragment.StatisticsFragment;
import lifesgame.tapstudios.ca.lifesgame.fragment.StoreFragment;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private NonSwipeableViewPager viewPager;
    private BottomNavigationView mBottomNavigation;
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

        //Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Category")
                .setAction("Homepage")
                .build());

        tracker.setScreenName("MainActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            displayName = user.getDisplayName();
        }

        databaseHelper = new DatabaseHelper(this);
        expiredGoalsAndTasksCount = databaseHelper.resetExpiredGoalsAndTasks();
        homeFragmentBundle = new Bundle();
        homeFragmentBundle.putInt("EXPIRED_TODO_COUNT", expiredGoalsAndTasksCount);

        if (getIntent() != null) {
            if(getIntent().getBooleanExtra("PASSCODE_SET", false)) {
                Intent intent = new Intent(this, CustomPinActivity.class);
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
                startActivityForResult(intent, REQUEST_CODE_ENABLE);
            }
            getIntent().removeExtra("PASSCODE_SET");
        }
        fragmentManager = getFragmentManager();
        setupSwipeFragments();
        setupBottomNavBar();
        scrollToSpecificFragment();
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    private void setupSwipeFragments() {
        viewPager = (NonSwipeableViewPager) findViewById(R.id.main_fragment);
        mAdapterViewPager = new PagerAdapter(fragmentManager);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(homeFragmentBundle);
        mAdapterViewPager.addFragment(homeFragment, "Home");
        mAdapterViewPager.addFragment(new StatisticsFragment(), "Statistics");
        mAdapterViewPager.addFragment(new StoreFragment(), "Statistics");
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

    public void setupBottomNavBar() {
        mBottomNavigation = (BottomNavigationView) findViewById(R.id.NavBot);
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.statistics:
                        viewPager.setCurrentItem(1, true);
                        break;
                    case R.id.store:
                        viewPager.setCurrentItem(2, true);
                        break;
                }
                return true;
            }
        });
    }

    public void scrollToSpecificFragment() {
        if (getIntent() != null) {
            Integer fragmentNumber = getIntent().getIntExtra("FRAGMENT_NUMBER", -1);
            if (fragmentNumber != -1) {
                if(fragmentNumber == 2) {
                    mBottomNavigation.setSelectedItemId(R.id.store);
                }
                else if(fragmentNumber == 1) {
                    mBottomNavigation.setSelectedItemId(R.id.statistics);
                }
                else {
                    mBottomNavigation.setSelectedItemId(R.id.home);
                }
                viewPager.setCurrentItem(fragmentNumber, true);
            }
        }
    }
}
