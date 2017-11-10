package lifesgame.tapstudios.ca.lifesgame;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
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
    private MyPagerAdapter mAdapterViewPager;

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
        Date dataEndDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            String endDate = intent.getStringExtra("DATA_ENDDATE");
            if (endDate != null) {
                dataEndDate = format.parse(intent.getStringExtra("DATA_ENDDATE"));
            } else {
                dataEndDate = null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            dataEndDate = null;
        }

        Map<String, Boolean> improvementType = (HashMap<String, Boolean>) intent.getSerializableExtra("DATA_IMPROVEMENT_TYPE");

        if (dataDescription != null || dataTitle != null) {
            Long goalTaskId = addData(dataDescription, dataCategory, dataTitle, dataSilver, improvementType, dataEndDate);
            Toast.makeText(getApplicationContext(), "Inserted " + dataCategory, Toast.LENGTH_LONG).show();
        }

        fragmentManager = getFragmentManager();
        fragment = new HomeFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_fragment, fragment).commit();

        setupSwipeFragments();
        setupBottomNavBar();
    }

    private void setupSwipeFragments() {
        viewPager = (ViewPager) findViewById(R.id.main_fragment);
        mAdapterViewPager = new MyPagerAdapter(fragmentManager);
        viewPager.setAdapter(mAdapterViewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
                    Fragment fragment = mAdapterViewPager.getFragment(position);
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

    public Long addData(String description, String category, String title, Long silver, Map<String, Boolean> improvementType, Date dataEndDate) {
        Long goalTaskId = databaseHelper.addData(description, category, title, silver, improvementType, dataEndDate);
        return goalTaskId;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new StatisticsFragment();
                default:
                    return new HomeFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                String tag = fragment.getTag();
                mFragmentTags.put(position, tag);
            }
            return object;
        }

        public Fragment getFragment(int position) {
            Fragment fragment = null;
            String tag = mFragmentTags.get(position);
            if (tag != null) {
                fragment = fragmentManager.findFragmentByTag(tag);
            }
            return fragment;
        }
    }
}
