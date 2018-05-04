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
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.LineChartView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

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

public class SilverFragment extends Fragment {
    private LineChartView silverChart;
    private DatabaseHelper databaseHelper;
    private Tooltip mTip;
    private View silverView;
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
    private CardView cvDayOfWeekPicker;
    private StatisticFilters statisticsRange;
    private TextView silverTv;
    private Integer dayOfWeek;
    private Tracker tracker;
    private String toDoCompletionPrefixSentence = "Total Silver Gained ";

    public SilverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getActivity());
        silverView = inflater.inflate(R.layout.activity_silver, container, false);
        mTip = new Tooltip(getActivity(), R.layout.linechart_tooltip, R.id.value);
        silverChart = (LineChartView) silverView.findViewById(R.id.silverChart);
        dailyButton = (ImageButton) silverView.findViewById(R.id.daily_silver);
        weeklyButton = (ImageButton) silverView.findViewById(R.id.weekly_silver);
        monthlyButton = (ImageButton) silverView.findViewById(R.id.monthly_silver);
        silverTv = (TextView) silverView.findViewById(R.id.silverTV);
        cvDayOfWeekPicker = (CardView) silverView.findViewById(R.id.card_view_daily_filter);
        cvDayOfWeekPicker.setVisibility(View.GONE);
        statisticsRange = StatisticFilters.WEEKLY;
        sundayButton = (ImageButton) silverView.findViewById(R.id.sunday);
        mondayButton = (ImageButton) silverView.findViewById(R.id.monday);
        tuesdayButton = (ImageButton) silverView.findViewById(R.id.tuesday);
        wednesdayButton = (ImageButton) silverView.findViewById(R.id.wednesday);
        thursdayButton = (ImageButton) silverView.findViewById(R.id.thursday);
        fridayButton = (ImageButton) silverView.findViewById(R.id.friday);
        saturdayButton = (ImageButton) silverView.findViewById(R.id.saturday);
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
        List<LineSet> dataSet = databaseHelper.getCompletedGoalTasks(statisticsRange, dayOfWeek);

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupSilverChart(dataSet, gridPaint);
        setupDateRangeFilters();
        setupDayOfWeekButtonFilters();

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Category")
                .setAction("SilverStatistics")
                .build());

        tracker.setScreenName("SilverStatistics");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        return silverView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Graph Data
        if(databaseHelper != null) {
            if (databaseHelper.getTotalDeletedCount(statisticsRange) != totalDeleted) {
                updateGraphs();
            }
        }
    }

    public void setupSilverChart(List<LineSet> dataSet, Paint gridPaint) {
        if (statisticsRange == StatisticFilters.DAILY) {
            dataSet.get(0)
                    .setColor(Color.WHITE)
                    .setThickness(6)
                    .setSmooth(false);
        } else {
            dataSet.get(0)
                    .setColor(Color.WHITE)
                    .setDotsStrokeColor(Color.WHITE)
                    .setDotsStrokeThickness(6)
                    .setDotsColor(Color.parseColor("#DC602E"))
                    .setThickness(6)
                    .setSmooth(false);
        }
        silverChart.addData(dataSet.get(0));
        if (dataSet.get(0).getMax().getValue() <= 2F) {
            silverChart.setAxisBorderValues(0, 3, 1);
        }
        silverChart.setYLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setLabelsFormat(new DecimalFormat("0"))
                .setAxisThickness(6)
                .setAxisLabelsSpacing(35)
                .setGrid(7, 0, gridPaint)
                .setTooltips(mTip)
                .show(new Animation().setInterpolator(new LinearInterpolator())
                        .setDuration(200)
                        .fromAlpha(0));
        silverChart.setBackgroundColor(Color.parseColor("#DC602E"));
    }

    private void updateGraphs() {
        silverChart.dismissAllTooltips();
        int updatedTotalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
        List<LineSet> dataSet = databaseHelper.getCompletedGoalTasks(statisticsRange, dayOfWeek);
        totalDeleted = updatedTotalDeleted;
        silverChart.reset();
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupSilverChart(dataSet, gridPaint);
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
                silverTv.setText(toDoCompletionPrefixSentence + statisticsRange.getDateRange());
                dailyButton.setImageResource(R.drawable.selected_daily);
                updateGraphs();
                cvDayOfWeekPicker.setVisibility(View.VISIBLE);
                setDayOfWeekButtonSelected(dayOfWeek);
            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.WEEKLY;
                resetAllButtonState();
                silverTv.setText(toDoCompletionPrefixSentence + statisticsRange.getDateRange());
                weeklyButton.setImageResource(R.drawable.selected_weekly);
                updateGraphs();
                cvDayOfWeekPicker.setVisibility(View.GONE);
            }
        });

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.MONTHLY;
                resetAllButtonState();
                silverTv.setText(toDoCompletionPrefixSentence + statisticsRange.getDateRange());
                monthlyButton.setImageResource(R.drawable.selected_monthly);
                updateGraphs();
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
