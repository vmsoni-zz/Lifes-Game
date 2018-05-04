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
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.LineChartView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.AnalyticsApplication;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.StatisticFilters;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

/**
 * Created by viditsoni on 2017-11-09.
 */

public class CompletedToDoFragment extends Fragment {
    public LineChartView completedToDoGraph;
    public DatabaseHelper databaseHelper;
    private Tooltip mTip;
    private View completedToDoView;
    private ColorfulRingProgressView completedToDoPercentageGraph;
    private TextView tvCompLineChart;
    private TextView tvCompPercChart;
    private TextView tvCompToDo;
    private CardView cvDayOfWeekPicker;

    private List<LineSet> dataSet;
    private int totalDeleted;
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
    private StatisticFilters statisticsRange;
    private String toDoCompletionPrefixSentence = "Total TODOs Completed ";
    private String toDoCompletionPercPrefixSentence = "TODOs Completion Percentage ";
    private Integer dayOfWeek;
    private Tracker tracker;

    public CompletedToDoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getActivity());
        completedToDoView = inflater.inflate(R.layout.activity_completed_todo, container, false);
        mTip = new Tooltip(getActivity(), R.layout.linechart_tooltip, R.id.value);
        completedToDoGraph = (LineChartView) completedToDoView.findViewById(R.id.mainChart);
        completedToDoPercentageGraph = (ColorfulRingProgressView) completedToDoView.findViewById(R.id.compToDoGraph);
        tvCompToDo = (TextView) completedToDoView.findViewById(R.id.tvCompToDo);
        tvCompLineChart = (TextView) completedToDoView.findViewById(R.id.todoTV);
        tvCompPercChart = (TextView) completedToDoView.findViewById(R.id.todoCompTV);
        cvDayOfWeekPicker = (CardView) completedToDoView.findViewById(R.id.card_view_daily_filter);
        dailyButton = (ImageButton) completedToDoView.findViewById(R.id.daily_completed_todo);
        weeklyButton = (ImageButton) completedToDoView.findViewById(R.id.weekly_completed_todo);
        monthlyButton = (ImageButton) completedToDoView.findViewById(R.id.monthly_completed_todo);
        sundayButton = (ImageButton) completedToDoView.findViewById(R.id.sunday);
        mondayButton = (ImageButton) completedToDoView.findViewById(R.id.monday);
        tuesdayButton = (ImageButton) completedToDoView.findViewById(R.id.tuesday);
        wednesdayButton = (ImageButton) completedToDoView.findViewById(R.id.wednesday);
        thursdayButton = (ImageButton) completedToDoView.findViewById(R.id.thursday);
        fridayButton = (ImageButton) completedToDoView.findViewById(R.id.friday);
        saturdayButton = (ImageButton) completedToDoView.findViewById(R.id.saturday);
        cvDayOfWeekPicker.setVisibility(View.GONE);
        statisticsRange = StatisticFilters.WEEKLY;
        dayOfWeek = 1;

        ((TextView) mTip.findViewById(R.id.value)).setTypeface(
                Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Semibold.ttf"));

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        mTip.setDimensions((int) Tools.fromDpToPx(58), (int) Tools.fromDpToPx(25));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

            mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

            mTip.setPivotX(Tools.fromDpToPx(65) / 2);
            mTip.setPivotY(Tools.fromDpToPx(25));
        }
        //Graph Data
        updateGraphs();
        setupDateRangeFilters();
        setupDayOfWeekButtonFilters();

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Category")
                .setAction("CompletedTodoStatistics")
                .build());

        tracker.setScreenName("CompletedTodoStatistics");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        return completedToDoView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Graph Data
        if (databaseHelper != null) {
            if (databaseHelper.getTotalDeletedCount(statisticsRange) != totalDeleted) {
                updateGraphs();
            }
        }
    }

    public void setupCompletedToDoGraph(List<LineSet> dataSet, Paint gridPaint) {
        if (statisticsRange == StatisticFilters.DAILY) {
            dataSet.get(1)
                    .setColor(Color.WHITE)
                    .setThickness(6)
                    .setSmooth(false);
        } else {
            dataSet.get(1)
                    .setColor(Color.WHITE)
                    .setDotsStrokeColor(Color.WHITE)
                    .setDotsStrokeThickness(6)
                    .setDotsColor(Color.parseColor("#04baa6"))
                    .setThickness(6)
                    .setSmooth(false);
        }
        completedToDoGraph.addData(dataSet.get(1));
        if (dataSet.get(1).getMax().getValue() <= 2F) {
            completedToDoGraph.setAxisBorderValues(0, 3, 1);
        }
        completedToDoGraph.setYLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setLabelsFormat(new DecimalFormat("0"))
                .setAxisThickness(6)
                .setAxisLabelsSpacing(35)
                .setGrid(7, 0, gridPaint)
                .setTooltips(mTip)
                .show(new Animation()
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(200)
                        .fromAlpha(0));
        completedToDoGraph.setBackgroundColor(Color.parseColor("#04baa6"));
    }

    private void updateGraphs() {
        completedToDoGraph.dismissAllTooltips();
        int updatedTotalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
        List<LineSet> dataSet = databaseHelper.getCompletedGoalTasks(statisticsRange, dayOfWeek);
        totalDeleted = updatedTotalDeleted;
        completedToDoGraph.reset();

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupCompletedToDoPercentageGraph(databaseHelper.getCompletedToDoPercentage(statisticsRange, dayOfWeek));
        setupCompletedToDoGraph(dataSet, gridPaint);
    }

    public void setupCompletedToDoPercentageGraph(int toDoCompPercentage) {
        completedToDoPercentageGraph.setStrokeWidthDp(12f);
        completedToDoPercentageGraph.setFgColorStart(0xFF00A4ff);
        completedToDoPercentageGraph.setFgColorEnd(0xFF02FEF1);
        completedToDoPercentageGraph.setPercent(toDoCompPercentage);
        tvCompToDo.setText(String.valueOf(toDoCompPercentage));
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
                statisticsRange = StatisticFilters.DAILY;
                resetAllButtonState();
                updateGraphs();
                dailyButton.setImageResource(R.drawable.selected_daily);
                tvCompLineChart.setText(toDoCompletionPrefixSentence + statisticsRange.getDateRange());
                tvCompPercChart.setText(toDoCompletionPercPrefixSentence + statisticsRange.getDateRange());
                cvDayOfWeekPicker.setVisibility(View.VISIBLE);
                setDayOfWeekButtonSelected(dayOfWeek);
            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.WEEKLY;
                resetAllButtonState();
                updateGraphs();
                weeklyButton.setImageResource(R.drawable.selected_weekly);
                tvCompLineChart.setText(toDoCompletionPrefixSentence + statisticsRange.getDateRange());
                tvCompPercChart.setText(toDoCompletionPercPrefixSentence + statisticsRange.getDateRange());
                cvDayOfWeekPicker.setVisibility(View.GONE);
            }
        });

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.MONTHLY;
                resetAllButtonState();
                updateGraphs();
                monthlyButton.setImageResource(R.drawable.selected_monthly);
                tvCompLineChart.setText(toDoCompletionPrefixSentence + statisticsRange.getDateRange());
                tvCompPercChart.setText(toDoCompletionPercPrefixSentence + statisticsRange.getDateRange());
                cvDayOfWeekPicker.setVisibility(View.GONE);
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
