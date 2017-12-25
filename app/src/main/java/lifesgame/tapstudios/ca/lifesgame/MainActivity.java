package lifesgame.tapstudios.ca.lifesgame;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private ViewPager viewPager;
    private BottomNavigationView mBottomNavigation;
    private Map<Integer, String> mFragmentTags;
    private PagerAdapter mAdapterViewPager;

    private DatabaseHelper databaseHelper;

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
        String deadlineDate = intent.getStringExtra("DATA_ENDDATE");

        Map<String, Boolean> improvementType = (HashMap<String, Boolean>) intent.getSerializableExtra("DATA_IMPROVEMENT_TYPE");

        if (dataDescription != null || dataTitle != null) {
            Long goalTaskId = addData(dataDescription, dataCategory, dataTitle, dataSilver, improvementType, deadlineDate);
            Toast.makeText(getApplicationContext(), "Inserted " + dataCategory, Toast.LENGTH_LONG).show();
        }

        fragmentManager = getFragmentManager();
        setupSwipeFragments();
        setupBottomNavBar();
    }

    private void setupSwipeFragments() {
        viewPager = (ViewPager) findViewById(R.id.main_fragment);
        mAdapterViewPager = new PagerAdapter(fragmentManager);
        mAdapterViewPager.addFragment(new HomeFragment(), "Home");
        mAdapterViewPager.addFragment(new StatisticsFragment(), "Statistics");
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
                }
                return true;
            }
        });
    }

    public Long addData(String description, String category, String title, Long silver, Map<String, Boolean> improvementType, String deadlineDate) {
        Long goalTaskId = databaseHelper.addData(description, category, title, silver, improvementType, deadlineDate);
        return goalTaskId;
    }
}
