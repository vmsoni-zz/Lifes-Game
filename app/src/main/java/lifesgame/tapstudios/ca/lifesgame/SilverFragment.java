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
import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.LineChartView;

import java.text.DecimalFormat;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

/**
 * Created by viditsoni on 2017-11-09.
 */

public class SilverFragment extends Fragment {
    public LineChartView silverChart;
    public DatabaseHelper databaseHelper;
    private Tooltip mTip;
    private View silverView;
    private int totalDeleted;

    public SilverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getContext());
        silverView = inflater.inflate(R.layout.activity_silver, container, false);
        mTip = new Tooltip(silverView.getContext(), R.layout.linechart_tooltip, R.id.value);
        silverChart = (LineChartView) silverView.findViewById(R.id.silverChart);

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

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupSilverChart(dataSet, gridPaint);

        return silverView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Graph Data
        int updatedTotalDeleted = databaseHelper.getTotalDeletedCount();
        if(databaseHelper.getTotalDeletedCount() != totalDeleted) {
            List<LineSet> dataSet = databaseHelper.getCompletedGoalTasks();
            totalDeleted = updatedTotalDeleted;
            updateGraphs(dataSet);
        }
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

    private void updateGraphs(List<LineSet> dataSet) {
        silverChart.reset();

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupSilverChart(dataSet, gridPaint);
    }
}
