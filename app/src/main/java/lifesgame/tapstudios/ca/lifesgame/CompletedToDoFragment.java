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
import android.widget.TextView;

import com.db.chart.animation.Animation;
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
    private TextView tvCompToDo;
    private  List<LineSet> dataSet;
    private int totalDeleted;

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
        dataSet = databaseHelper.getCompletedGoalTasks();
        totalDeleted = databaseHelper.getTotalDeletedCount();

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupCompletedToDoPercentageGraph(databaseHelper.getCompletedToDoPercentage());
        setupCompletedToDoGraph(dataSet, gridPaint);

        return completedToDoView;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Graph Data
        int updatedTotalDeleted = databaseHelper.getTotalDeletedCount();
        if(databaseHelper.getTotalDeletedCount() != totalDeleted) {
            List<LineSet> updateDataSet = databaseHelper.getCompletedGoalTasks();
            totalDeleted = updatedTotalDeleted;
            updateGraphs(updateDataSet, databaseHelper.getCompletedToDoPercentage());
        }
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

    private void updateGraphs(List<LineSet> dataSet, int toDoCompPercentage) {
        completedToDoGraph.reset();

        setupCompletedToDoPercentageGraph(toDoCompPercentage);

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupCompletedToDoPercentageGraph(databaseHelper.getCompletedToDoPercentage());
        setupCompletedToDoGraph(dataSet, gridPaint);
    }

    public void setupCompletedToDoPercentageGraph(int toDoCompPercentage) {
        completedToDoPercentageGraph.setStrokeWidthDp(12f);
        completedToDoPercentageGraph.setFgColorStart(0xFF00A4ff);
        completedToDoPercentageGraph.setFgColorEnd(0xFF02FEF1);
        completedToDoPercentageGraph.setPercent(toDoCompPercentage);
        tvCompToDo.setText(String.valueOf(toDoCompPercentage));
    }
}
