package lifesgame.tapstudios.ca.lifesgame;

import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.LineChartView;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.text.DecimalFormat;
import java.util.List;

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
    private List<LineSet> dataSet;
    private int totalDeleted;
    private ImageButton dailyButton;
    private ImageButton weeklyButton;
    private ImageButton monthlyButton;
    private StatisticFilters statisticsRange;
    private String toDoCompletionPrefixSentence = "Total TODOs Completed ";
    private String toDoCompletionPercPrefixSentence = "TODOs Completion Percentage ";


    public CompletedToDoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getContext());
        completedToDoView = inflater.inflate(R.layout.activity_completed_todo, container, false);
        mTip = new Tooltip(completedToDoView.getContext(), R.layout.linechart_tooltip, R.id.value);
        completedToDoGraph = (LineChartView) completedToDoView.findViewById(R.id.mainChart);
        completedToDoPercentageGraph = (ColorfulRingProgressView) completedToDoView.findViewById(R.id.compToDoGraph);
        tvCompToDo = (TextView) completedToDoView.findViewById(R.id.tvCompToDo);
        tvCompLineChart = (TextView) completedToDoView.findViewById(R.id.todoTV);
        tvCompPercChart = (TextView) completedToDoView.findViewById(R.id.todoCompTV);
        dailyButton = (ImageButton) completedToDoView.findViewById(R.id.daily_completed_todo);
        weeklyButton = (ImageButton) completedToDoView.findViewById(R.id.weekly_completed_todo);
        monthlyButton = (ImageButton) completedToDoView.findViewById(R.id.monthly_completed_todo);
        statisticsRange = StatisticFilters.WEEKLY;

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
        dataSet = databaseHelper.getCompletedGoalTasks(statisticsRange);
        totalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupCompletedToDoPercentageGraph(databaseHelper.getCompletedToDoPercentage(statisticsRange));
        setupCompletedToDoGraph(dataSet, gridPaint);
        setupDateRangeFilters();
        return completedToDoView;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Graph Data
        int updatedTotalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
        if (databaseHelper.getTotalDeletedCount(statisticsRange) != totalDeleted) {
            List<LineSet> updateDataSet = databaseHelper.getCompletedGoalTasks(statisticsRange);
            totalDeleted = updatedTotalDeleted;
            updateGraphs(updateDataSet, databaseHelper.getCompletedToDoPercentage(statisticsRange));
        }
    }

    public void setupCompletedToDoGraph(List<LineSet> dataSet, Paint gridPaint) {
        if (statisticsRange == StatisticFilters.DAILY) {
            dataSet.get(1)
                    .setColor(Color.WHITE)
                    .setThickness(6)
                    .setSmooth(true);
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

    private void updateGraphs(List<LineSet> dataSet, int toDoCompPercentage) {
        completedToDoGraph.reset();

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupCompletedToDoPercentageGraph(toDoCompPercentage);
        setupCompletedToDoGraph(dataSet, gridPaint);
    }

    public void setupCompletedToDoPercentageGraph(int toDoCompPercentage) {
        completedToDoPercentageGraph.setStrokeWidthDp(12f);
        completedToDoPercentageGraph.setFgColorStart(0xFF00A4ff);
        completedToDoPercentageGraph.setFgColorEnd(0xFF02FEF1);
        completedToDoPercentageGraph.setPercent(toDoCompPercentage);
        tvCompToDo.setText(String.valueOf(toDoCompPercentage));
    }

    private void setupDateRangeFilters() {
        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.DAILY;
                resetAllButtonState();
                dailyButton.setImageResource(R.drawable.selected_daily);
                int updatedTotalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
                List<LineSet> updateDataSet = databaseHelper.getCompletedGoalTasks(statisticsRange);
                totalDeleted = updatedTotalDeleted;
                updateGraphs(updateDataSet, databaseHelper.getCompletedToDoPercentage(statisticsRange));
                tvCompLineChart.setText(toDoCompletionPrefixSentence + statisticsRange.getDateRange());
                tvCompPercChart.setText(toDoCompletionPercPrefixSentence + statisticsRange.getDateRange());

            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.WEEKLY;
                resetAllButtonState();
                weeklyButton.setImageResource(R.drawable.selected_weekly);
                int updatedTotalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
                List<LineSet> updateDataSet = databaseHelper.getCompletedGoalTasks(statisticsRange);
                totalDeleted = updatedTotalDeleted;
                updateGraphs(updateDataSet, databaseHelper.getCompletedToDoPercentage(statisticsRange));
                tvCompLineChart.setText(toDoCompletionPrefixSentence + statisticsRange.getDateRange());
                tvCompPercChart.setText(toDoCompletionPercPrefixSentence + statisticsRange.getDateRange());
            }
        });

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.MONTHLY;
                resetAllButtonState();
                monthlyButton.setImageResource(R.drawable.selected_monthly);
                int updatedTotalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
                List<LineSet> updateDataSet = databaseHelper.getCompletedGoalTasks(statisticsRange);
                totalDeleted = updatedTotalDeleted;
                updateGraphs(updateDataSet, databaseHelper.getCompletedToDoPercentage(statisticsRange));
                tvCompLineChart.setText(toDoCompletionPrefixSentence + statisticsRange.getDateRange());
                tvCompPercChart.setText(toDoCompletionPercPrefixSentence + statisticsRange.getDateRange());
            }
        });
    }

    private void resetAllButtonState() {
        dailyButton.setImageResource(R.drawable.daily);
        weeklyButton.setImageResource(R.drawable.weekly);
        monthlyButton.setImageResource(R.drawable.monthly);
    }
}
