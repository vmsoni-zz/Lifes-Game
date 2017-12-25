package lifesgame.tapstudios.ca.lifesgame;

import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.renderer.XRenderer;
import com.db.chart.renderer.YRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.BarChartView;
import com.db.chart.view.LineChartView;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;


public class StatisticsFragment extends Fragment {
    private TabLayout tabs;
    private ViewPager viewPager;
    private View view;
    private PagerAdapter adapter;
    private DatabaseHelper databaseHelper;
    private int totalDeleted;
    private StatisticFilters statisticsRange;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statisticsRange = StatisticFilters.WEEKLY;
        databaseHelper = new DatabaseHelper(getContext());
        totalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
        view = inflater.inflate(R.layout.statistics_tabs, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabs = (TabLayout) view.findViewById(R.id.statistics_tab_layout);
        tabs.addTab(tabs.newTab());
        tabs.addTab(tabs.newTab());
        tabs.addTab(tabs.newTab());
        tabs.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Graph Data
        int updatedTotalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
        if(updatedTotalDeleted != totalDeleted) {
            adapter.getItem(0).onResume();
            totalDeleted = updatedTotalDeleted;
        }
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new CompletedToDoFragment(), "Completed ToDos");
        adapter.addFragment(new SilverFragment(), "Silver");
        adapter.addFragment(new ImprovementTypeXpFragment(), "Improvement");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Fragment fragment = adapter.getItem(position);
                if (fragment != null) {
                    fragment.onResume();
                }
            }

            public void onPageSelected(int position) {
            }
        });
    }
}
