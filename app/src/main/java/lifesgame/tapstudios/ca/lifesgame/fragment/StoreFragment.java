package lifesgame.tapstudios.ca.lifesgame.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lifesgame.tapstudios.ca.lifesgame.PagerAdapter;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.StatisticFilters;
import lifesgame.tapstudios.ca.lifesgame.fragment.MarketplaceFragment;
import lifesgame.tapstudios.ca.lifesgame.fragment.RewardsFragment;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

public class StoreFragment extends Fragment {
    private TabLayout tabs;
    private ViewPager viewPager;
    private View view;
    private PagerAdapter adapter;
    private DatabaseHelper databaseHelper;
    private int totalDeleted;
    private StatisticFilters statisticsRange;

    public StoreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statisticsRange = StatisticFilters.WEEKLY;
        databaseHelper = new DatabaseHelper(getActivity());
        view = inflater.inflate(R.layout.statistics_tabs, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabs = (TabLayout) view.findViewById(R.id.statistics_tab_layout);
        tabs.addTab(tabs.newTab());
        tabs.addTab(tabs.newTab());
        tabs.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Graph Data
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new RewardsFragment(), "Rewards");
        adapter.addFragment(new MarketplaceFragment(), "Marketplace");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Fragment fragment = adapter.getItem(position);
            }

            public void onPageSelected(int position) {
            }
        });
    }
}
