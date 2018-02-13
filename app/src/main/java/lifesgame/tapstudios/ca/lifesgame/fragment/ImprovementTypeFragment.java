package lifesgame.tapstudios.ca.lifesgame.fragment;

import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.model.BarSet;
import com.db.chart.renderer.XRenderer;
import com.db.chart.renderer.YRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.BarChartView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.DecimalFormat;
import java.util.Calendar;

import lifesgame.tapstudios.ca.lifesgame.AnalyticsApplication;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.StatisticFilters;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;


/**
 * Created by viditsoni on 2017-11-09.
 */

public class ImprovementTypeFragment extends Fragment {
    public BarChartView improvementTypeXpChart;
    public DatabaseHelper databaseHelper;
    private Tooltip mTip;
    private View improvementView;
    private ImageButton dailyButton;
    private ImageButton weeklyButton;
    private ImageButton monthlyButton;
    private ImageButton sundayButton;
    private ImageButton mondayButton;
    private ImageButton tuesdayButton;
    private ImageButton wednesdayButton;
    private ImageButton thursdayButton;
    private ImageButton fridayButton;
    private ImageButton saturdayButton;
    private CardView cvDayOfWeekPicker;
    private int totalDeleted;
    private StatisticFilters statisticsRange;
    private Integer dayOfWeek;
    private Tracker tracker;

    public ImprovementTypeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getActivity());
        improvementView = inflater.inflate(R.layout.activity_improvement, container, false);
        mTip = new Tooltip(getActivity(), R.layout.linechart_tooltip, R.id.value);
        improvementTypeXpChart = (BarChartView) improvementView.findViewById(R.id.improvementTypesChart);
        dailyButton = (ImageButton) improvementView.findViewById(R.id.daily_improvement);
        weeklyButton = (ImageButton) improvementView.findViewById(R.id.weekly_improvement);
        monthlyButton = (ImageButton) improvementView.findViewById(R.id.monthly_improvement);
        sundayButton = (ImageButton) improvementView.findViewById(R.id.sunday);
        mondayButton = (ImageButton) improvementView.findViewById(R.id.monday);
        tuesdayButton = (ImageButton) improvementView.findViewById(R.id.tuesday);
        wednesdayButton = (ImageButton) improvementView.findViewById(R.id.wednesday);
        thursdayButton = (ImageButton) improvementView.findViewById(R.id.thursday);
        fridayButton = (ImageButton) improvementView.findViewById(R.id.friday);
        saturdayButton = (ImageButton) improvementView.findViewById(R.id.saturday);
        cvDayOfWeekPicker = (CardView) improvementView.findViewById(R.id.card_view_daily_filter);
        cvDayOfWeekPicker.setVisibility(View.GONE);
        statisticsRange = StatisticFilters.WEEKLY;
        dayOfWeek = 1;

        //Graph Data
        BarSet barSet = databaseHelper.getImprovementTypesXP(statisticsRange, dayOfWeek);
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupImprovementTypeXpChart(barSet, gridPaint);
        setupDateRangeFilters();
        setupDayOfWeekButtonFilters();

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Category")
                .setAction("ImprovementTypeStatistics")
                .build());

        tracker.setScreenName("ImprovementTypeStatistics");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        return improvementView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Graph Data

        if (databaseHelper.getTotalDeletedCount(statisticsRange) != totalDeleted) {
            updateGraphs();
        }
    }

    private void setupImprovementTypeXpChart(BarSet barSet, Paint gridPaint) {
        barSet.setColor(Color.parseColor("#fc2a53"));
        improvementTypeXpChart.addData(barSet);
        if (barSet.getMax().getValue() <= 2F) {
            improvementTypeXpChart.setAxisBorderValues(0, 3, 1);
        }
        improvementTypeXpChart.setXLabels(XRenderer.LabelPosition.OUTSIDE)
                .setYLabels(YRenderer.LabelPosition.OUTSIDE)
                .setLabelsFormat(new DecimalFormat("0"))
                .setAxisThickness(6)
                .setAxisLabelsSpacing(35)
                .setGrid(7, 0, gridPaint)
                .show(new Animation()
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(200)
                        .fromAlpha(0));
        improvementTypeXpChart.setBackgroundColor(Color.parseColor("#04baa6"));
    }

    private void updateGraphs() {
        improvementTypeXpChart.dismissAllTooltips();
        int updatedTotalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
        BarSet barSet = databaseHelper.getImprovementTypesXP(statisticsRange, dayOfWeek);
        totalDeleted = updatedTotalDeleted;
        improvementTypeXpChart.reset();
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));
        setupImprovementTypeXpChart(barSet, gridPaint);
    }

    private void setupDayOfWeekButtonFilters() {
        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfWeek = 1;
                setDayOfWeekButtonSelected(dayOfWeek);
                updateGraphs();
            }
        });

        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfWeek = 2;
                setDayOfWeekButtonSelected(dayOfWeek);
                updateGraphs();
            }
        });

        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfWeek = 3;
                setDayOfWeekButtonSelected(dayOfWeek);
                updateGraphs();
            }
        });

        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfWeek = 4;
                setDayOfWeekButtonSelected(dayOfWeek);
                updateGraphs();
            }
        });

        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfWeek = 5;
                setDayOfWeekButtonSelected(dayOfWeek);
                updateGraphs();
            }
        });

        fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfWeek = 6;
                setDayOfWeekButtonSelected(dayOfWeek);
                updateGraphs();
            }
        });

        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfWeek = 7;
                setDayOfWeekButtonSelected(dayOfWeek);
                updateGraphs();
            }
        });
    }

    private void setupDateRangeFilters() {
        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar curdate = Calendar.getInstance();
                dayOfWeek = curdate.get(Calendar.DAY_OF_WEEK);
                setDayOfWeekButtonSelected(dayOfWeek);
                statisticsRange = StatisticFilters.DAILY;
                resetAllButtonState();
                dailyButton.setImageResource(R.drawable.selected_daily);
                cvDayOfWeekPicker.setVisibility(View.VISIBLE);
                updateGraphs();
            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.WEEKLY;
                resetAllButtonState();
                weeklyButton.setImageResource(R.drawable.selected_weekly);
                cvDayOfWeekPicker.setVisibility(View.GONE);
                updateGraphs();

            }
        });

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.MONTHLY;
                resetAllButtonState();
                monthlyButton.setImageResource(R.drawable.selected_monthly);
                cvDayOfWeekPicker.setVisibility(View.GONE);
                updateGraphs();
            }
        });
    }

    private void setDayOfWeekButtonSelected(Integer dayOfWeekSelected) {
        switch (dayOfWeekSelected) {
            case 1:
                resetAllDayOfWeekButtons();
                sundayButton.setImageResource(R.drawable.selected_sunday);
                break;
            case 2:
                resetAllDayOfWeekButtons();
                mondayButton.setImageResource(R.drawable.selected_monday);
                break;
            case 3:
                resetAllDayOfWeekButtons();
                tuesdayButton.setImageResource(R.drawable.selected_tuesday);
                break;
            case 4:
                resetAllDayOfWeekButtons();
                wednesdayButton.setImageResource(R.drawable.selected_wednesday);
                break;
            case 5:
                resetAllDayOfWeekButtons();
                thursdayButton.setImageResource(R.drawable.selected_thursday);
                break;
            case 6:
                resetAllDayOfWeekButtons();
                fridayButton.setImageResource(R.drawable.selected_friday);
                break;
            case 7:
                resetAllDayOfWeekButtons();
                saturdayButton.setImageResource(R.drawable.selected_saturday);
                break;
        }
    }

    private void resetAllDayOfWeekButtons() {
        sundayButton.setImageResource(R.drawable.sunday);
        mondayButton.setImageResource(R.drawable.monday);
        tuesdayButton.setImageResource(R.drawable.tuesday);
        wednesdayButton.setImageResource(R.drawable.wednesday);
        thursdayButton.setImageResource(R.drawable.thursday);
        fridayButton.setImageResource(R.drawable.friday);
        saturdayButton.setImageResource(R.drawable.saturday);
    }

    private void resetAllButtonState() {
        dailyButton.setImageResource(R.drawable.daily);
        weeklyButton.setImageResource(R.drawable.weekly);
        monthlyButton.setImageResource(R.drawable.monthly);
    }
}
