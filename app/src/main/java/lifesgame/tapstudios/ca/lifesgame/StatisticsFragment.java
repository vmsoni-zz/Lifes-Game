package lifesgame.tapstudios.ca.lifesgame;

import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.model.BarSet;
import com.db.chart.model.ChartSet;
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
    public LineChartView completedToDoGraph;
    public LineChartView silverChart;
    public BarChartView improvementTypeXpChart;
    public DatabaseHelper databaseHelper;
    private Tooltip mTip;
    private View statisticsView;
    private ColorfulRingProgressView completedToDoPercentageGraph;
    private TextView tvCompToDo;

    public StatisticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getContext());
        statisticsView = inflater.inflate(R.layout.activity_statistics, container, false);
        mTip = new Tooltip(statisticsView.getContext(), R.layout.linechart_tooltip, R.id.value);
        completedToDoGraph = (LineChartView) statisticsView.findViewById(R.id.mainChart);
        completedToDoPercentageGraph = (ColorfulRingProgressView) statisticsView.findViewById(R.id.compToDoGraph);
        improvementTypeXpChart = (BarChartView) statisticsView.findViewById(R.id.improvementTypesChart);
        silverChart = (LineChartView) statisticsView.findViewById(R.id.silverChart);
        tvCompToDo = (TextView) statisticsView.findViewById(R.id.tvCompToDo);

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
        List<LineSet> dataSet = databaseHelper.getCompletedGoalTasks();
        BarSet barSet = databaseHelper.getImprovementTypesXP();

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupCompletedToDoPercentageGraph(databaseHelper.getCompletedToDoPercentage());
        setupCompletedToDoGraph(dataSet, gridPaint);
        setupSilverChart(dataSet, gridPaint);
        setupImprovementTypeXpChart(barSet, gridPaint);

        return statisticsView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Graph Data
        List<LineSet> dataSet = databaseHelper.getCompletedGoalTasks();
        BarSet barSet = databaseHelper.getImprovementTypesXP();

        updateGraphs(dataSet, barSet, databaseHelper.getCompletedToDoPercentage());
    }

    public void setupCompletedToDoGraph(List<LineSet> dataSet, Paint gridPaint) {
        dataSet.get(1)
                .setColor(Color.WHITE)
                .setDotsStrokeColor(Color.WHITE)
                .setDotsStrokeThickness(6)
                .setDotsColor(Color.parseColor("#04baa6"))
                .setThickness(6)
                .setSmooth(false);
        completedToDoGraph.addData(dataSet.get(1));
        completedToDoGraph.setYLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setLabelsFormat(new DecimalFormat("0"))
                .setAxisThickness(6)
                .setAxisLabelsSpacing(35)
                .setGrid(7, 0, gridPaint)
                .setTooltips(mTip)
                .show(new Animation()
                        .setInterpolator(new BounceInterpolator())
                        .fromAlpha(0));
        completedToDoGraph.setBackgroundColor(Color.parseColor("#04baa6"));
    }

    public void setupSilverChart(List<LineSet> dataSet, Paint gridPaint) {
        dataSet.get(0)
                .setColor(Color.WHITE)
                .setDotsStrokeColor(Color.WHITE)
                .setDotsStrokeThickness(6)
                .setDotsColor(Color.parseColor("#DC602E"))
                .setThickness(6)
                .setSmooth(false);
        silverChart.addData(dataSet.get(0));
        silverChart.setYLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setLabelsFormat(new DecimalFormat("0"))
                .setAxisThickness(6)
                .setAxisLabelsSpacing(35)
                .setGrid(7, 0, gridPaint)
                .setTooltips(mTip)
                .show(new Animation().setInterpolator(new BounceInterpolator())
                        .fromAlpha(0));
        silverChart.setBackgroundColor(Color.parseColor("#DC602E"));
    }

    public void setupImprovementTypeXpChart(BarSet barSet, Paint gridPaint) {
        barSet.setColor(Color.parseColor("#fc2a53"));
        improvementTypeXpChart.addData(barSet);
        improvementTypeXpChart.setXLabels(XRenderer.LabelPosition.OUTSIDE)
                .setYLabels(YRenderer.LabelPosition.OUTSIDE)
                .setLabelsFormat(new DecimalFormat("0"))
                .setAxisThickness(6)
                .setAxisLabelsSpacing(35)
                .setGrid(7, 0, gridPaint)
                .show(new Animation());
        improvementTypeXpChart.setBackgroundColor(Color.parseColor("#04baa6"));
    }

    private void updateGraphs(List<LineSet> dataSet, BarSet barSet, int toDoCompPercentage) {
        improvementTypeXpChart.reset();
        completedToDoGraph.reset();
        silverChart.reset();

        setupCompletedToDoPercentageGraph(toDoCompPercentage);

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupCompletedToDoPercentageGraph(databaseHelper.getCompletedToDoPercentage());
        setupCompletedToDoGraph(dataSet, gridPaint);
        setupSilverChart(dataSet, gridPaint);
        setupImprovementTypeXpChart(barSet, gridPaint);
    }

    public void setupCompletedToDoPercentageGraph(int toDoCompPercentage) {
        completedToDoPercentageGraph.setPercent(toDoCompPercentage);
        tvCompToDo.setText(String.valueOf(toDoCompPercentage));
        completedToDoPercentageGraph.setStrokeWidthDp(12f);
    }
}
