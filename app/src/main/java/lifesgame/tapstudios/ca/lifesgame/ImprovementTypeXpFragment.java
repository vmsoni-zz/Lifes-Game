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
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.model.BarSet;
import com.db.chart.renderer.XRenderer;
import com.db.chart.renderer.YRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.BarChartView;

import java.text.DecimalFormat;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;


/**
 * Created by viditsoni on 2017-11-09.
 */

public class ImprovementTypeXpFragment extends Fragment {
    public BarChartView improvementTypeXpChart;
    public DatabaseHelper databaseHelper;
    private Tooltip mTip;
    private View improvementView;
    private ImageButton dailyButton;
    private ImageButton weeklyButton;
    private ImageButton monthlyButton;
    private int totalDeleted;
    private StatisticFilters statisticsRange;

    public ImprovementTypeXpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getContext());
        improvementView = inflater.inflate(R.layout.activity_improvement, container, false);
        mTip = new Tooltip(improvementView.getContext(), R.layout.linechart_tooltip, R.id.value);
        improvementTypeXpChart = (BarChartView) improvementView.findViewById(R.id.improvementTypesChart);
        dailyButton = (ImageButton) improvementView.findViewById(R.id.daily_improvement);
        weeklyButton = (ImageButton) improvementView.findViewById(R.id.weekly_improvement);
        monthlyButton = (ImageButton) improvementView.findViewById(R.id.monthly_improvement);
        statisticsRange = StatisticFilters.DAILY;

        //Graph Data
        BarSet barSet = databaseHelper.getImprovementTypesXP(statisticsRange);

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupImprovementTypeXpChart(barSet, gridPaint);
        setupDateRangeFilters();

        return improvementView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Graph Data

        int updatedTotalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
        if (updatedTotalDeleted != totalDeleted) {
            BarSet barSet = databaseHelper.getImprovementTypesXP(statisticsRange);
            totalDeleted = updatedTotalDeleted;
            updateGraphs(barSet);
        }
    }

    private void setupImprovementTypeXpChart(BarSet barSet, Paint gridPaint) {
        barSet.setColor(Color.parseColor("#fc2a53"));
        improvementTypeXpChart.addData(barSet);
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

    private void updateGraphs(BarSet barSet) {
        improvementTypeXpChart.reset();
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));
        setupImprovementTypeXpChart(barSet, gridPaint);
    }

    private void setupDateRangeFilters() {
        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.DAILY;
                resetAllButtonState();
                dailyButton.setImageResource(R.drawable.selected_daily);
                int updatedTotalDeleted = databaseHelper.getTotalDeletedCount(statisticsRange);
                BarSet barSet = databaseHelper.getImprovementTypesXP(statisticsRange);
                totalDeleted = updatedTotalDeleted;
                updateGraphs(barSet);
            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.WEEKLY;
                resetAllButtonState();
                weeklyButton.setImageResource(R.drawable.selected_weekly);

            }
        });

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsRange = StatisticFilters.MONTHLY;
                resetAllButtonState();
                monthlyButton.setImageResource(R.drawable.selected_monthly);
            }
        });
    }

    private void resetAllButtonState() {
        dailyButton.setImageResource(R.drawable.daily);
        weeklyButton.setImageResource(R.drawable.weekly);
        monthlyButton.setImageResource(R.drawable.monthly);
    }
}
